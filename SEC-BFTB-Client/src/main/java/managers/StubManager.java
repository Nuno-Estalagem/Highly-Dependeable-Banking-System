package managers;

import Handlers.SignatureHandler;
import Handlers.StubHandler;
import com.SEC.grpc.BFTB;
import com.SEC.grpc.BTFTBGrpc;
import com.google.common.primitives.Ints;
import com.google.protobuf.ByteString;
import domain.Client;
import io.grpc.Server;

import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class StubManager {

    private StubHandler stubHandler;

    private int byzantineQuorum;

    private int byzantineServers;

    private HashMap<String, BTFTBGrpc.BTFTBBlockingStub> stubs;

    public StubManager(StubHandler stubHandler, int byzantineQuorum, int byzantineServers, HashMap<String, BTFTBGrpc.BTFTBBlockingStub> stubs) {
        this.byzantineQuorum = byzantineQuorum;
        this.stubHandler = stubHandler;
        this.byzantineServers = byzantineServers;
        this.stubs = stubs;


    }


    public Object generateMethod(String input, Object request, Client cliente) {
        try {
            int bizantinos = 0;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            SignatureHandler signatureHandler = new SignatureHandler();
            Hashtable<PublicKey, Object> outputs = new Hashtable<>();
            Set<String> keys = stubs.keySet();

            if (input.equals("open")) {
                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {
                            BFTB.APIresponse response = stubHandler.open_account(stubs.get(key), (BFTB.OpenAccountRequest) request, countDownLatch);
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();
                synchronized (outputs) {
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {
                        BFTB.APIresponse apIresponse = (BFTB.APIresponse) outputs.get(serverKey);
                        byte[] server_signature = apIresponse.getBytes().toByteArray();
                        String response = apIresponse.getResponse();
                        if (new SignatureHandler().verifySignature(server_signature, serverKey, response.getBytes())) {
                            return response;
                        } else {
                            bizantinos++;
                            if (bizantinos > byzantineServers) {
                                return "servidores bizantinos";
                            }
                        }
                    }


                }
            }

            if (input.equals("check")) {
                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {
                            BFTB.AccountResponse response = stubHandler.check_account(stubs.get(key), (BFTB.CheckAccountRequest) request, countDownLatch, (PublicKey) cliente.getServerPubKeys().get(key), cliente.getPrivateKey());
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();
                int responseMaxWid = -1;
                int maxRid = -1;
                BFTB.AccountResponse maxResponse = null;
                synchronized (outputs) {
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {

                        BFTB.CheckAccountRequest accountRequest = (BFTB.CheckAccountRequest) request;
                        ByteString checkedPubKey = accountRequest.getToCheckPubKey();
                        PublicKey checkedKey = getPubKey(checkedPubKey);
                        BFTB.AccountResponse accountResponse = (BFTB.AccountResponse) outputs.get(serverKey);
                        byte[] messageSignature = accountResponse.getMessageSign().toByteArray();
                        int rid = accountResponse.getRid();
                        String message = accountResponse.getMessage();

                        if (message.length() != 0) {
                            if (signatureHandler.verifySignature(messageSignature, serverKey, (message + rid).getBytes())) {
                                if (maxRid < rid && responseMaxWid == -1)
                                    maxResponse = accountResponse;

                            } else {
                                bizantinos++;
                                if (bizantinos > byzantineServers) {
                                    System.out.println("Byzantine Servers");
                                    return "servidores bizantinos";
                                }
                            }
                        } else{
                            List<BFTB.Transaction> transactions = accountResponse.getListList();
                        int wid = accountResponse.getWid();
                        int balance = accountResponse.getBalance();
                        byte[] balancePlusWid = accountResponse.getBalancePlusWidSign().toByteArray();
                        String toSign = String.valueOf(balance) + transactions + wid + Arrays.toString(balancePlusWid);
                        HashSet<byte[]> signatures = new HashSet<>();
                        byte[] toSignBytes = toSign.getBytes();

                        if (signatureHandler.verifySignature(messageSignature, serverKey, toSignBytes)) {
                            if (signatureHandler.verifySignature(balancePlusWid, checkedKey, (String.valueOf(balance) + wid).getBytes()) && rid == accountRequest.getRid()) {
                                if (transactions.size() > 0) {
                                    for (BFTB.Transaction transaction : transactions) {
                                        byte[] transactSign = transaction.getSignature().toByteArray();
                                        int amount = transaction.getAmount();
                                        int tWid = transaction.getWid();
                                        byte[] source = transaction.getSource().toByteArray();
                                        byte[] dest = transaction.getDest().toByteArray();
                                        String senderUsername = transaction.getSenderUsername();
                                        String receiverUsername = transaction.getReceiverUsername();

                                        outputStream.write(String.valueOf(amount).getBytes());
                                        outputStream.write(source);
                                        outputStream.write(dest);
                                        outputStream.write(senderUsername.getBytes());
                                        outputStream.write(String.valueOf(tWid).getBytes());
                                        outputStream.write(receiverUsername.getBytes());


                                        if (signatureHandler.verifySignature(transactSign, getPubKey(ByteString.copyFrom(source)), outputStream.toByteArray())) {
                                            if (signatures.add(transactSign)) {
                                                if (wid > responseMaxWid) {
                                                    responseMaxWid = wid;
                                                    maxResponse = accountResponse;
                                                }
                                            }
                                        } else {
                                            bizantinos++;
                                            if (bizantinos > byzantineServers) {
                                                System.out.println("Byzantine Servers");
                                                return "servidores bizantinos";
                                            }
                                        }
                                        outputStream.reset();

                                    }
                                } else if (wid > responseMaxWid) {
                                    responseMaxWid = wid;
                                    maxResponse = accountResponse;
                                }
                            }
                        }
                    }

                    }
                }

                return maxResponse;

            }
            if (input.equals("audit")) {
                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {

                            BFTB.AuditTransactionResponse response = stubHandler.audit(stubs.get(key), (BFTB.AuditRequest) request, countDownLatch, cliente.getPrivateKey(), (PublicKey) cliente.getServerPubKeys().get(key));
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();

                int responseMaxWid = -1;
                int maxRid = -1;
                BFTB.AuditTransactionResponse maxResponse = null;
                synchronized (outputs) {
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {


                        BFTB.AuditRequest auditRequest = (BFTB.AuditRequest) request;
                        ByteString checkedPubKey = auditRequest.getKeyToCheck();
                        PublicKey checkedKey = getPubKey(checkedPubKey);

                        BFTB.AuditTransactionResponse auditTransactionResponse = (BFTB.AuditTransactionResponse) outputs.get(serverKey);
                        List<BFTB.Transaction> transactions = auditTransactionResponse.getListList();
                        int rid = auditTransactionResponse.getRid();

                        byte[] messageSignature = auditTransactionResponse.getSignature().toByteArray();


                        String message = auditTransactionResponse.getMessage();

                        if (message.length() != 0) {
                            if (signatureHandler.verifySignature(messageSignature, serverKey, (message + rid).getBytes())) {
                                if (maxRid < rid && responseMaxWid == -1)
                                    maxResponse = auditTransactionResponse;

                            } else {
                                bizantinos++;
                                if (bizantinos > byzantineServers) {
                                    System.out.println("Byzantine Servers");
                                    return "servidores bizantinos";
                                }
                            }
                        } else {
                            String toSign = String.valueOf(rid) + transactions;
                            HashSet<byte[]> signatures = new HashSet<>();
                            if (signatureHandler.verifySignature(messageSignature, serverKey, toSign.getBytes())) {
                                if (rid == auditRequest.getRid()) {
                                    boolean areValid = true;
                                    for (BFTB.Transaction transaction : transactions) {
                                        byte[] transactSign = transaction.getSignature().toByteArray();
                                        int amount = transaction.getAmount();
                                        int tWid = transaction.getWid();
                                        byte[] source = transaction.getSource().toByteArray();
                                        byte[] dest = transaction.getDest().toByteArray();
                                        String senderUsername = transaction.getSenderUsername();
                                        String receiverUsername = transaction.getReceiverUsername();


                                        outputStream.write(String.valueOf(amount).getBytes());
                                        outputStream.write(source);
                                        outputStream.write(dest);
                                        outputStream.write(senderUsername.getBytes());
                                        outputStream.write(String.valueOf(tWid).getBytes());
                                        outputStream.write(receiverUsername.getBytes());

                                        if (signatureHandler.verifySignature(transactSign, checkedKey, outputStream.toByteArray())) {
                                            if (signatures.add(transactSign)) {
                                                areValid = true;
                                            }
                                        } else {
                                            areValid = false;
                                        }
                                        outputStream.reset();
                                    }
                                    if (areValid) {
                                        if (transactions.size() > responseMaxWid) {
                                            responseMaxWid = transactions.size();
                                            maxResponse = auditTransactionResponse;
                                        }
                                    } else {
                                        bizantinos++;
                                        if (bizantinos > byzantineServers) {
                                            System.out.println("Byzantine Servers");
                                            return "servidores bizantinos";
                                        }
                                    }
                                }

                            } else {
                                bizantinos++;
                                if (bizantinos > byzantineServers) {
                                    System.out.println("Byzantine Servers");
                                    return "servidores bizantinos";
                                }
                            }
                        }


                    }
                    return maxResponse;
                }
            }

            if (input.equals("receive")) {

                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {
                            BFTB.ReceiveAmountResponse response = stubHandler.receive_amount(stubs.get(key), (BFTB.ReceiveAmountRequest) request, countDownLatch);
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();

                int maxWid = -1;
                String response = "";
                synchronized (outputs) {
                    int receiveWid = ((BFTB.ReceiveAmountRequest) request).getWid();
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {
                        BFTB.ReceiveAmountResponse receiveAmountResponse = (BFTB.ReceiveAmountResponse) outputs.get(serverKey);
                        int wid = receiveAmountResponse.getWid();
                        String output = receiveAmountResponse.getResponse();
                        byte[] messageSign = receiveAmountResponse.getSignature().toByteArray();
                        if (signatureHandler.verifySignature(messageSign, serverKey, (wid + output).getBytes())) {
                            if (wid == receiveWid) {
                                if (wid > maxWid) {
                                    response = output;
                                    maxWid = wid;
                                }
                            }
                        } else {
                            bizantinos++;
                            if (bizantinos > byzantineServers) {
                                System.out.println("Byzantine Servers");
                                return "servidores bizantinos";
                            }
                        }

                    }
                    return response;
                }


            }

            if (input.equals("send")) {
                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {
                            BFTB.SendAmountResponse response = stubHandler.send_amount(stubs.get(key), (BFTB.SendAmountRequest) request, countDownLatch);
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();
                int maxWid = -1;
                String response = "";
                synchronized (outputs) {
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {
                        BFTB.SendAmountResponse sendAmountResponse = (BFTB.SendAmountResponse) outputs.get(serverKey);
                        int wid = sendAmountResponse.getWid();
                        String output = sendAmountResponse.getMessage();
                        byte[] messageSign = sendAmountResponse.getSignature().toByteArray();
                        if (signatureHandler.verifySignature(messageSign, serverKey, (wid + output).getBytes())) {
                            if (wid > maxWid) {
                                response = output;
                                maxWid = wid;
                            }
                        } else {
                            bizantinos++;
                            if (bizantinos > byzantineServers) {
                                System.out.println("Byzantine Servers");
                                return "servidores bizantinos";
                            }
                        }


                    }
                    return response;
                }


            }
            if (input.equals("rid")) {
                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {
                            BFTB.RidResponse response = stubHandler.getRid(stubs.get(key), (BFTB.GetRidRequest) request, countDownLatch);
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();

                int maxRid = -1;
                synchronized (outputs) {
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {
                        BFTB.RidResponse response = (BFTB.RidResponse) outputs.get(serverKey);
                        int rid = response.getMyRid();
                        byte[] signature = response.getResponse().toByteArray();
                        if (signatureHandler.verifySignature(signature, serverKey, (rid + "signed").getBytes())) {
                            if (rid > maxRid) {
                                maxRid = rid;
                            }
                        }

                    }
                }
                return maxRid;
            }

            if (input.equals("CAWB")) {
                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {
                            BFTB.CAWBResponse response = stubHandler.cawb(stubs.get(key), (BFTB.CAWBRequest) request, countDownLatch);
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();
                synchronized (outputs) {
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {
                        BFTB.CAWBResponse response = (BFTB.CAWBResponse) outputs.get(serverKey);
                        byte[] signed = response.getByte().toByteArray();
                        if (signatureHandler.verifySignature(signed, serverKey, response.getMessage().getBytes()))
                            return response;
                        else {
                            bizantinos++;
                            if (bizantinos > byzantineServers) {
                                System.out.println("Byzantine Servers");
                                return "servidores bizantinos";
                            }
                        }

                    }

                }

            }

            if (input.equals("ADWB")) {
                CountDownLatch countDownLatch = new CountDownLatch(this.byzantineQuorum);
                for (String key : keys) {
                    Thread thread = new Thread(() -> {
                        try {
                            BFTB.ADWBResponse response = stubHandler.adwb(stubs.get(key), (BFTB.ADWBRequest) request, countDownLatch);
                            outputs.put((PublicKey) cliente.getServerPubKeys().get(key), response);
                        } catch (Exception e) {
                            System.out.println("counted as failure");
                        }
                    });
                    thread.start();
                }
                countDownLatch.await();
                synchronized (outputs) {
                    Set<PublicKey> serverKeys = outputs.keySet();
                    for (PublicKey serverKey : serverKeys) {
                        BFTB.ADWBResponse response = (BFTB.ADWBResponse) outputs.get(serverKey);
                        byte[] signed = response.getByte().toByteArray();
                        if (signatureHandler.verifySignature(signed, serverKey, response.getMessage().getBytes()))
                            return response;
                        else {
                            bizantinos++;
                            if (bizantinos > byzantineServers) {
                                System.out.println("Byzantine Servers");
                                return "servidores bizantinos";
                            }
                        }

                    }

                }

            }


            return "invalid command";

        } catch (
                Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PublicKey getPubKey(ByteString key) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key.toByteArray()));
        } catch (Exception e) {
            return null;
        }
    }

}