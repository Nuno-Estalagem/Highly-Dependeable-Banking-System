package services;


import com.SEC.grpc.ADEBOuterClass;
import com.SEC.grpc.BFTB;
import com.SEC.grpc.BTFTBGrpc;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.protobuf.ByteString;

import domain.*;
import handlers.FileHandler;
import handlers.SignatureHandler;
import io.grpc.stub.StreamObserver;
import managers.ADEBInstanceManager;
import managers.ADEBManager;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class bftbService extends BTFTBGrpc.BTFTBImplBase {

    private File CLIENTFILE;
    Hashtable<PublicKey, ClientProperties> clientTable;
    Hashtable<PublicKey, Parameters> temporaryNonce = new Hashtable<>();
    private List<String> usernames;
    Server server;
    PrivateKey serverKey;

    private Client serverClient;
    private ADEBManager adebManager;

    private int port;
    private ADEBInstanceManager adebInstanceManager;


    public bftbService(int port, ADEBManager adebManager, Client serverClient, String PROJECT_DIR, File CLIENTFILE, ADEBInstanceManager adebInstanceManager) {
        this.adebManager = adebManager;
        this.serverClient = serverClient;
        this.CLIENTFILE = CLIENTFILE;
        this.adebInstanceManager = adebInstanceManager;
        this.port = port;


        try {
            String SERVERFILE = PROJECT_DIR + File.separator + "server_" + port + ".jks";
            this.server = Server.create(SERVERFILE, "server_" + port);
            this.serverKey = (PrivateKey) server.getKeyStore().getKey("server_" + port, ("server_" + port).toCharArray());
            this.clientTable = (Hashtable<PublicKey, ClientProperties>) FileHandler.loadState(CLIENTFILE);

            this.usernames = getUsernames(clientTable);
            this.serverClient = serverClient;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getUsernames(Hashtable<PublicKey, ClientProperties> clientTable) {
        List<String> usernames = new ArrayList<>();
        Set<PublicKey> chaves = clientTable.keySet();
        for (Key chave : chaves) {
            usernames.add(clientTable.get(chave).getUsername());
        }
        return usernames;
    }


    @Override
    public void openAccount(BFTB.OpenAccountRequest request, StreamObserver<BFTB.APIresponse> responseObserver) {
        try {

            PublicKey key = getPubKey(request.getKey());
            String username = request.getUsername();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int balance = (int) request.getBalance();

            byte[] balancePlusWidsign = request.getBalancePlusWidSign().toByteArray();

            byte[] params = request.getSignature().toByteArray();

            outputStream.write(key.getEncoded());
            outputStream.write(username.getBytes());
            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(balancePlusWidsign);

            BFTB.APIresponse.Builder response = BFTB.APIresponse.newBuilder();

            if (new SignatureHandler().verifySignature(params, key, outputStream.toByteArray())) {


                if (!usernames.contains(username)) {
                    if (!clientTable.containsKey(key)) {
                        clientTable.put(key, new ClientProperties(balance, new ArrayList<>(), new ArrayList<>(), username, 0, 0, balancePlusWidsign, new byte[256]));
                        usernames.add(username);

                        try {

                            FileHandler.saveState(CLIENTFILE, clientTable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        response.setResponse("Account created successfully!");
                        response.setBytes(ByteString.copyFrom(new SignatureHandler().sign("Account created successfully!".getBytes(), serverKey)));


                    } else {
                        response.setBytes(ByteString.copyFrom(new SignatureHandler().sign("An account associated with this key already exists!".getBytes(), serverKey)));
                        response.setResponse("An account associated with this key already exists!");

                    }
                } else {
                    response.setBytes(ByteString.copyFrom(new SignatureHandler().sign("An account associated with this username already exists!".getBytes(), serverKey)));
                    response.setResponse("An account associated with this username already exists!");
                }
            } else {
                response.setResponse("you are not who you claim you claim to be")
                        .setBytes(ByteString.copyFrom(new SignatureHandler().sign("you are not who you claim you claim to be".getBytes(), serverKey)));
            }

            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro a assinar");
        }
    }


    @Override
    public void sendAmount(BFTB.SendAmountRequest request, StreamObserver<BFTB.SendAmountResponse> responseObserver) {
        try {
            String output = "";
            BFTB.SendAmountResponse.Builder response = BFTB.SendAmountResponse.newBuilder();

            SignatureHandler signatureHandler = new SignatureHandler();

            BFTB.Transaction transaction = request.getTransaction();
            ByteString sourceByte = transaction.getSource();
            ByteString destByte = transaction.getDest();

            PublicKey source = getPubKey(sourceByte);
            PublicKey dest = getPubKey(destByte);

            int amount = transaction.getAmount();
            int wid = transaction.getWid();
            int balance = request.getBalance();
            String username = transaction.getSenderUsername();
            String destUsername = transaction.getReceiverUsername();

            byte[] balancePlusWidSign = request.getBalancePlusWidSign().toByteArray();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            outputStream.write(String.valueOf(amount).getBytes());
            outputStream.write(source.getEncoded());
            outputStream.write(dest.getEncoded());
            outputStream.write(username.getBytes());
            outputStream.write(String.valueOf(wid).getBytes());
            outputStream.write(destUsername.getBytes());
            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(balancePlusWidSign);
            outputStream.write(transaction.toByteArray());


            byte[] messageSignature = request.getMessageSignature().toByteArray();
            byte[] transactionSignature = transaction.getSignature().toByteArray();
            byte[] hash = outputStream.toByteArray();

            ClientProperties senderProperties = clientTable.get(source);
            if (senderProperties != null) {

                ClientProperties destProperties = clientTable.get(dest);
                int widfromServer = senderProperties.getWid();

                if (destProperties != null) {

                    if (!Arrays.equals(source.getEncoded(), dest.getEncoded())) {
                        if (signatureHandler.verifySignature(messageSignature, source, hash) && widfromServer < wid && (balance + amount) == senderProperties.getBalance()) {
                            if (amount > 0) {

                                if (senderProperties.canReduceAccounting(amount)) {
                                    senderProperties.reduceAccounting(amount);


                                    Transaction trans = new Transaction(source, dest, amount, senderProperties.getUsername(), destProperties.getUsername(), transactionSignature, wid);
                                    senderProperties.setWid(wid);
                                    senderProperties.setWidandBalanceSignature(balancePlusWidSign);

                                    destProperties.getPendingTransactions().add(trans);

                                    senderProperties.getAudit().add(trans);
                                    clientTable.put(dest, destProperties);
                                    clientTable.put(source, senderProperties);

                                    SecureRandom secureRandom = new SecureRandom();
                                    byte[] rand = new byte[256];
                                    secureRandom.nextBytes(rand);

                                    serverClient = Client.loadClient("src/main/resources/", "src/main/resources/" + port + "/" + "server_" + port + ".jks", "server_" + port, "server_" + port);
                                    ADEBOuterClass.EchoRequest echoRequest = ADEBOuterClass.EchoRequest.newBuilder().setParams(ByteString.copyFrom(hash)).setNonce(ByteString.copyFrom(rand))
                                            .setSigned(ByteString.copyFrom(signatureHandler.sign(hash, serverClient.getPrivateKey()))).setPubkey(ByteString.copyFrom(serverClient.getPublicKey().getEncoded())).build();


                                    ADEBInstance instance = adebInstanceManager.getInstance(Arrays.toString(hash));
                                    adebManager.echo(echoRequest, serverClient);
                                    instance.await();
                                    System.out.println("Completed adeb");
                                    output = "Transaction awaits approval! :-)";
                                    try {
                                        FileHandler.saveState(CLIENTFILE, clientTable);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    response
                                            .setSignature(ByteString.copyFrom(signatureHandler.sign((wid + output).getBytes(), serverKey)))
                                            .setMessage(output)
                                            .setWid(wid);



                                } else {
                                    output = "You do not have enough money to perform the transaction!";
                                    response
                                            .setSignature(ByteString.copyFrom(signatureHandler.sign((wid + output).getBytes(), serverKey)))
                                            .setMessage(output)
                                            .setWid(wid);


                                }
                            } else {
                                output = "You can't send a negative amount!";
                                response
                                        .setMessage(output)
                                        .setSignature(ByteString.copyFrom(signatureHandler.sign((wid + output).getBytes(), serverKey)))
                                        .setWid(wid);


                            }
                        } else {

                            output = "You are not who you claim to be!";
                            response
                                    .setMessage(output)
                                    .setSignature(ByteString.copyFrom(signatureHandler.sign((wid + output).getBytes(), serverKey)))
                                    .setWid(wid);

                        }

                    } else {

                        output = "You can't send money to yourself!";
                        response.setMessage(output)
                                .setSignature(ByteString.copyFrom(signatureHandler.sign((wid + output).getBytes(), serverKey)))
                                .setWid(wid);


                    }
                } else {

                    output = "Receiver does not exist!";
                    response.setMessage(output)
                            .setSignature(ByteString.copyFrom(signatureHandler.sign((wid + output).getBytes(), serverKey)))
                            .setWid(wid);


                }
            } else {

                output = "Sender does not exist!";
                response.setMessage(output)
                        .setSignature(ByteString.copyFrom(signatureHandler.sign((wid + output).getBytes(), serverKey)))
                        .setWid(wid);

            }


            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (Exception E) {
            E.printStackTrace();

        }
    }


    @Override
    public void checkAccount(BFTB.CheckAccountRequest request, StreamObserver<BFTB.AccountResponse> responseObserver) {
        try {

            PublicKey keyToCheck = getPubKey(request.getToCheckPubKey());
            int rid = request.getRid();
            PublicKey myPubKey = getPubKey(request.getMyPubkey());
            long pow = request.getPow();
            byte[] concat = request.getConcatenated().toByteArray();
            byte[] challenge = clientTable.get(keyToCheck).getChallenge();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(request.getToCheckPubKey().toByteArray());
            outputStream.write(String.valueOf(request.getRid()).getBytes());
            outputStream.write(request.getMyPubkey().toByteArray());
            outputStream.write(String.valueOf(pow).getBytes());

            BFTB.AccountResponse.Builder response = BFTB.AccountResponse.newBuilder();

            try {
                ClientProperties properties = clientTable.get(keyToCheck);
                byte[] concatCheck = Bytes.concat(request.getToCheckPubKey().toByteArray(), challenge);
                if (new SignatureHandler().verifySignature(request.getSignature().toByteArray(), myPubKey, outputStream.toByteArray())) {
                    if (verifyProofOfWork(concat, pow, 2) && Arrays.equals(concat, concatCheck)) {

                        if (clientTable.get(myPubKey).getRid() < rid) {
                        clientTable.get(myPubKey).setRid(rid);


                        int balance = properties.getBalance();


                        List<BFTB.Transaction> transactions = new ArrayList<>();
                        response.setBalance(balance);

                        ArrayList<Transaction> nonGRPCtransactions = properties.getPendingTransactions();

                        for (int i = 0; i < nonGRPCtransactions.size(); i++) {
                            Transaction t = nonGRPCtransactions.get(i);
                            int t_number = i + 1;
                            BFTB.Transaction bftbTransaction = BFTB.Transaction.newBuilder()
                                    .setAmount(t.getAmount())
                                    .setSource(ByteString.copyFrom(t.getSender().getEncoded()))
                                    .setSenderUsername(t.getSenderUsername())
                                    .setDest(ByteString.copyFrom(t.getReceiver().getEncoded()))
                                    .setReceiverUsername(t.getReceiverUsername())
                                    .setWid(t.getWid())
                                    .setPosition(t_number)
                                    .setSignature(ByteString.copyFrom(t.getSignature())).build();

                            transactions.add(bftbTransaction);

                        }
                        byte[] balancePluswid = clientTable.get(keyToCheck).getSignature();
                        int wid = clientTable.get(keyToCheck).getWid();
                        String toSign = String.valueOf(balance) + transactions + wid + Arrays.toString(balancePluswid);
                        byte[] signed = new SignatureHandler().sign(toSign.getBytes(), serverKey);
                        response.addAllList(transactions)
                                .setWid(wid)
                                .setBalancePlusWidSign(ByteString.copyFrom(balancePluswid))
                                .setRid(rid)
                                .setMessageSign(ByteString.copyFrom(signed));

                        try {
                            FileHandler.saveState(CLIENTFILE, clientTable);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {

                        response.setMessage("invalid rid");
                        response.setMessageSign(ByteString.copyFrom(new SignatureHandler().sign(("invalid rid"+rid).getBytes(), serverKey)));
                        response.setRid(rid);

                    }

                    } else {
                        response.setMessage("No Proof of Work");
                        response.setMessageSign(ByteString.copyFrom(new SignatureHandler().sign(("No Proof of Work"+rid).getBytes(), serverKey)));
                        response.setRid(rid);

                    }
                }else{
                    response.setMessage("invalid signature");
                    response.setMessageSign(ByteString.copyFrom(new SignatureHandler().sign(("invalid signature"+rid).getBytes(), serverKey)));
                    response.setRid(rid);

                }

            } catch (Exception e) {
                response.setMessage("invalid check parameter");
                response.setRid(rid);
                response.setMessageSign(ByteString.copyFrom(new SignatureHandler().sign(("invalid check parameter"+rid).getBytes(), serverKey)));




            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();

        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    @Override
    public void receiveAmount(BFTB.ReceiveAmountRequest request, StreamObserver<BFTB.ReceiveAmountResponse> responseObserver) {
        try {
            String output = "";
            PublicKey pubKey = getPubKey(request.getKey());

            int newBalance = request.getBalance();


            BFTB.Transaction newTransaction = request.getTransaction();
            int id = Integer.parseInt(request.getId());
            String idString = request.getId();
            byte[] signature = request.getSignature().toByteArray();
            SignatureHandler handler = new SignatureHandler();
            int wid = request.getWid();
            byte[] balancePluswidSigned = request.getWidAndBalance().toByteArray();


            BFTB.ReceiveAmountResponse.Builder response = BFTB.ReceiveAmountResponse.newBuilder();
            try {


                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                outputStream.write(String.valueOf(newBalance).getBytes());
                outputStream.write(String.valueOf(wid).getBytes());
                outputStream.write(idString.getBytes());
                outputStream.write(pubKey.getEncoded());
                outputStream.write(balancePluswidSigned);
                outputStream.write(newTransaction.toByteArray());

                ArrayList<Transaction> transactions = clientTable.get(pubKey).getPendingTransactions();

                if (handler.verifySignature(signature, pubKey, outputStream.toByteArray()) && wid > clientTable.get(pubKey).getWid()) {
                    if (newBalance == clientTable.get(pubKey).getBalance() + newTransaction.getAmount()) {
                        clientTable.get(pubKey).setWid(wid);

                        try {
                            Transaction transaction = transactions.get(id - 1);
                            if (Arrays.equals(transaction.getReceiver().getEncoded(), pubKey.getEncoded())) {

                                SecureRandom secureRandom = new SecureRandom();
                                byte[] rand = new byte[256];
                                secureRandom.nextBytes(rand);

                                ADEBOuterClass.EchoRequest echoRequest = ADEBOuterClass.EchoRequest.newBuilder().setParams(ByteString.copyFrom(signature)).setNonce(ByteString.copyFrom(rand))
                                        .setSigned(ByteString.copyFrom(handler.sign(signature, serverClient.getPrivateKey()))).setPubkey(ByteString.copyFrom(serverClient.getPublicKey().getEncoded())).build();

                                ADEBInstance instance = adebInstanceManager.getInstance(Arrays.toString(signature));
                                adebManager.echo(echoRequest, serverClient);
                                instance.await();
                                System.out.println("Completed adeb");

                                int amount = transaction.getAmount();

                                ClientProperties receiverProperties = clientTable.get(pubKey);

                                receiverProperties.addAccounting(amount);
                                Transaction newT = new Transaction(getPubKey(newTransaction.getSource()), getPubKey(newTransaction.getDest()), newTransaction.getAmount(), newTransaction.getSenderUsername(), newTransaction.getReceiverUsername(), newTransaction.getSignature().toByteArray(), newTransaction.getWid());
                                receiverProperties.getAudit().add(newT);
                                receiverProperties.getPendingTransactions().remove(id - 1);

                                clientTable.put(pubKey, receiverProperties);
                                clientTable.get(pubKey).setWidandBalanceSignature(balancePluswidSigned);

                                try {
                                    FileHandler.saveState(CLIENTFILE, clientTable);
                                } catch (Exception e) {
                                    output = "Could not write to file";
                                    response.setResponse(output);
                                }
                                output = "You received the Amount " + amount + " from " + transaction.getSenderUsername();
                                String toSign = receiverProperties.getWid() + output;
                                response.setResponse(output)
                                        .setWid(receiverProperties.getWid())
                                        .setSignature(ByteString.copyFrom(new SignatureHandler().sign(toSign.getBytes(), serverKey)));

                            } else {
                                output = "You cant recieve a transaction that you sent!";
                                response.setResponse(output);
                                response.setWid(wid);
                                response.setSignature(ByteString.copyFrom(handler.sign((wid+output).getBytes(),serverKey)));
                            }
                        } catch (IndexOutOfBoundsException | NumberFormatException N) {
                            output="That transaction does not exist!";
                            response.setResponse(output);
                            response.setWid(wid);
                            response.setSignature(ByteString.copyFrom(handler.sign((wid+output).getBytes(),serverKey)));

                        }
                    }
                } else {
                    output="Non-Repudiation not assured!!!!";
                    response.setResponse(output);
                    response.setWid(wid);
                    response.setSignature(ByteString.copyFrom(handler.sign((wid+output).getBytes(),serverKey)));
                }

            } catch (NumberFormatException E) {
                output="That transaction does not exist!";
                response.setResponse(output);
                response.setWid(wid);
                response.setSignature(ByteString.copyFrom(handler.sign((wid+output).getBytes(),serverKey)));

            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void audit(BFTB.AuditRequest request, StreamObserver<BFTB.AuditTransactionResponse> responseObserver) {
        try {
            long pow = request.getPow();
            PublicKey myPubKey = getPubKey(request.getMyKey());
            PublicKey keyToCheck = getPubKey(request.getKeyToCheck());
            int rid = request.getRid();
            byte[] concat = request.getConcatenated().toByteArray();
            byte[] challenge = clientTable.get(keyToCheck).getChallenge();

            BFTB.AuditTransactionResponse.Builder response = BFTB.AuditTransactionResponse.newBuilder();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(request.getKeyToCheck().toByteArray());
            outputStream.write(String.valueOf(request.getRid()).getBytes());
            outputStream.write(request.getMyKey().toByteArray());
            outputStream.write(String.valueOf(pow).getBytes());

            try {
                List<BFTB.Transaction> transactions = new ArrayList<>();
                ArrayList<Transaction> nonGRPCtransactions = clientTable.get(keyToCheck).getAudit();

                byte[] concatCheck = Bytes.concat(request.getKeyToCheck().toByteArray(), challenge);
                if (new SignatureHandler().verifySignature(request.getSignature().toByteArray(), myPubKey, outputStream.toByteArray())) {
                    if (verifyProofOfWork(concat, pow, 2) && Arrays.equals(concat, concatCheck)) {
                        if (clientTable.get(myPubKey).getRid() < rid) {
                            clientTable.get(myPubKey).setRid(rid);

                            for (int i = 0; i < nonGRPCtransactions.size(); i++) {
                                Transaction t = nonGRPCtransactions.get(i);

                                BFTB.Transaction bftbTransaction = BFTB.Transaction.newBuilder()
                                        .setAmount(t.getAmount())
                                        .setSource(ByteString.copyFrom(t.getSender().getEncoded()))
                                        .setSenderUsername(t.getSenderUsername())
                                        .setDest(ByteString.copyFrom(t.getReceiver().getEncoded()))
                                        .setReceiverUsername(t.getReceiverUsername())
                                        .setWid(t.getWid())
                                        .setPosition(i + 1)
                                        .setSignature(ByteString.copyFrom(t.getSignature())).build();

                                transactions.add(bftbTransaction);

                            }

                            String toSign = String.valueOf(rid) + transactions;
                            response.addAllList(transactions)
                                    .setRid(rid)
                                    .setSignature(ByteString.copyFrom(new SignatureHandler().sign(toSign.getBytes(), serverKey)));

                            try {
                                FileHandler.saveState(CLIENTFILE, clientTable);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            response.setMessage("invalid rid");
                            response.setSignature(ByteString.copyFrom(new SignatureHandler().sign(("invalid rid"+rid).getBytes(), serverKey)));
                            response.setRid(rid);

                        }


                    } else {
                        response.setMessage("No Proof of Work");
                        response.setSignature(ByteString.copyFrom(new SignatureHandler().sign(("No Proof of Work"+rid).getBytes(), serverKey)));
                        response.setRid(rid);

                    }
                }else{
                    response.setMessage("Invalid Signature");
                    response.setSignature(ByteString.copyFrom(new SignatureHandler().sign(("Invalid Signature"+rid).getBytes(), serverKey)));
                    response.setRid(rid);

                }

            } catch (Exception e) {

                response.setMessage("invalid audit parameter");
                response.setSignature(ByteString.copyFrom(new SignatureHandler().sign(("invalid audit parameter"+rid).getBytes(), serverKey)));
                response.setRid(rid);


            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        private Key getKey(ByteString key) {
            return new SecretKeySpec(key.toByteArray(), 0, key.toByteArray().length, "AES");
        }
    */

    @Override
    public void getRid(BFTB.GetRidRequest request, StreamObserver<BFTB.RidResponse> responseObserver) {
        BFTB.RidResponse.Builder response = BFTB.RidResponse.newBuilder();
        try {

            PublicKey key = getPubKey(request.getMyKey());
            if (new SignatureHandler().verifySignature(request.getSignature().toByteArray(), key, key.getEncoded())) {
                int rid = clientTable.get(key).getRid();
                byte[] signed = new SignatureHandler().sign((rid + "signed").getBytes(), serverKey);
                response.setMyRid(rid)
                        .setResponse(ByteString.copyFrom(signed));
            } else {
                response.setResponse(ByteString.copyFrom(new SignatureHandler().sign("invalid".getBytes(), serverKey)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();

    }


    @Override
    public void checkAccountWb(BFTB.CAWBRequest request, StreamObserver<BFTB.CAWBResponse> responseObserver) {
        try {
            BFTB.CAWBResponse.Builder response = BFTB.CAWBResponse.newBuilder();
            String output = "";
            int balance = request.getBalance();
            int rid = request.getRid();
            List<BFTB.Transaction> transactions = request.getTransactionList();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] balancePlusWid = request.getBalancePlusWid().toByteArray();
            int wid = request.getWid();
            byte[] mykey = request.getMyKey().toByteArray();
            PublicKey myKey = getPubKey(request.getMyKey());
            PublicKey keyToCheckKey = getPubKey(request.getCheckedKey());
            byte[] checkedKey = request.getCheckedKey().toByteArray();
            byte[] signature = request.getSignature().toByteArray();

            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(String.valueOf(rid).getBytes());
            outputStream.write(String.valueOf(transactions).getBytes());
            outputStream.write(balancePlusWid);
            outputStream.write(String.valueOf(wid).getBytes());
            outputStream.write(mykey);
            outputStream.write(checkedKey);

            if (new SignatureHandler().verifySignature(signature, myKey, outputStream.toByteArray())) {
                if (wid < clientTable.get(keyToCheckKey).getWid()) {
                    try {
                        SignatureHandler handler = new SignatureHandler();
                        ADEBOuterClass.EchoRequest echoRequest = ADEBOuterClass.EchoRequest.newBuilder().setParams(ByteString.copyFrom(outputStream.toByteArray())).setNonce(ByteString.copyFrom(outputStream.toByteArray()))
                                .setSigned(ByteString.copyFrom(handler.sign(outputStream.toByteArray(), serverClient.getPrivateKey()))).setPubkey(ByteString.copyFrom(serverClient.getPublicKey().getEncoded())).build();

                        ADEBInstance instance = adebInstanceManager.getInstance(Arrays.toString(outputStream.toByteArray()));
                        adebManager.echo(echoRequest, serverClient);
                        instance.await();
                        System.out.println("Completed adeb");


                        ClientProperties properties = clientTable.get(keyToCheckKey);
                        ArrayList<Transaction> pending = new ArrayList<>();
                        for (BFTB.Transaction grpcT : transactions) {
                            pending.add(new Transaction(getPubKey(grpcT.getSource()), getPubKey(grpcT.getDest()), grpcT.getAmount(), grpcT.getSenderUsername(), grpcT.getReceiverUsername(), grpcT.getSignature().toByteArray(), grpcT.getWid()));
                        }
                        properties.setPending(pending);
                        properties.setWid(wid);
                        properties.setBalance(balance);
                        properties.setWidandBalanceSignature(balancePlusWid);

                        try {
                            FileHandler.saveState(CLIENTFILE, clientTable);
                            output = "written back!";
                            response.setMessage(output);
                            response.setByte(ByteString.copyFrom(new SignatureHandler().sign(output.getBytes(), serverKey)));
                        } catch (Exception e) {
                            output = "Could not write to file";
                            response.setMessage(output);
                            response.setByte(ByteString.copyFrom(new SignatureHandler().sign(output.getBytes(), serverKey)));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    output = "written back!";
                    response.setMessage(output);
                    response.setByte(ByteString.copyFrom(new SignatureHandler().sign(output.getBytes(), serverKey)));
                }
            } else {
                output = "invalid sign for wb";
                response.setMessage(output);
                response.setByte(ByteString.copyFrom(new SignatureHandler().sign(output.getBytes(), serverKey)));
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void auditWb(BFTB.ADWBRequest request, StreamObserver<BFTB.ADWBResponse> responseObserver) {
        try {
            BFTB.ADWBResponse.Builder response = BFTB.ADWBResponse.newBuilder();

            String output = "";

            int rid = request.getRid();
            List<BFTB.Transaction> transactions = request.getTransactionList();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] mykey = request.getMyKey().toByteArray();

            PublicKey myKey = getPubKey(request.getMyKey());
            PublicKey keyToCheckKey = getPubKey(request.getCheckedKey());

            byte[] checkedKey = request.getCheckedKey().toByteArray();
            byte[] signature = request.getSignature().toByteArray();

            outputStream.write(String.valueOf(rid).getBytes());
            outputStream.write(String.valueOf(transactions).getBytes());
            outputStream.write(mykey);
            outputStream.write(checkedKey);

            if (new SignatureHandler().verifySignature(signature, myKey, outputStream.toByteArray())) {
                try {

                    SignatureHandler handler = new SignatureHandler();
                    ADEBOuterClass.EchoRequest echoRequest = ADEBOuterClass.EchoRequest.newBuilder().setParams(ByteString.copyFrom(outputStream.toByteArray())).setNonce(ByteString.copyFrom(outputStream.toByteArray()))
                            .setSigned(ByteString.copyFrom(handler.sign(outputStream.toByteArray(), serverClient.getPrivateKey()))).setPubkey(ByteString.copyFrom(serverClient.getPublicKey().getEncoded())).build();

                    ADEBInstance instance = adebInstanceManager.getInstance(Arrays.toString(outputStream.toByteArray()));
                    adebManager.echo(echoRequest, serverClient);
                    instance.await();
                    System.out.println("Completed adeb");

                    ClientProperties properties = clientTable.get(keyToCheckKey);
                    ArrayList<Transaction> audit = new ArrayList<>();
                    for (BFTB.Transaction grpcT : transactions) {
                        audit.add(new Transaction(getPubKey(grpcT.getSource()), getPubKey(grpcT.getDest()), grpcT.getAmount(), grpcT.getSenderUsername(), grpcT.getReceiverUsername(), grpcT.getSignature().toByteArray(), grpcT.getWid()));
                    }
                    properties.setAudit(audit);

                    try {
                        FileHandler.saveState(CLIENTFILE, clientTable);
                        output = "written back!";
                        response.setMessage(output);
                        response.setByte(ByteString.copyFrom(new SignatureHandler().sign(output.getBytes(), serverKey)));
                    } catch (Exception e) {
                        output = "Could not write to file";
                        response.setMessage(output);
                        response.setByte(ByteString.copyFrom(new SignatureHandler().sign(output.getBytes(), serverKey)));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                output = "invalid sign for wb";
                response.setMessage(output);
                response.setByte(ByteString.copyFrom(new SignatureHandler().sign(output.getBytes(), serverKey)));
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pOWRequest(BFTB.PowRequest request, StreamObserver<BFTB.PowResponse> responseObserver) {
        try {

            BFTB.PowResponse.Builder response = BFTB.PowResponse.newBuilder();
            PublicKey myKey = getPubKey(request.getMyPublicKey());
            long nonce = request.getNonce();
            byte[] signature = request.getSignature().toByteArray();
            byte[] data = (nonce + Arrays.toString(myKey.getEncoded())).getBytes();
            try {
                ClientProperties sender = clientTable.get(myKey);
                if (new SignatureHandler().verifySignature(signature, myKey, data)) {

                    SecureRandom secureRandom = new SecureRandom();
                    byte[] rand = new byte[256];
                    secureRandom.nextBytes(rand);
                    response.setChallenge(ByteString.copyFrom(rand));
                    nonce = nonce + 1;
                    response.setNonce(nonce);
                    response.setSignature(ByteString.copyFrom(new SignatureHandler().sign((Arrays.toString(rand) + nonce).getBytes(), serverKey)));
                    sender.setChallenge(rand);
                } else {
                    response.setNonce(nonce);
                    response.setSignature(ByteString.copyFrom(new SignatureHandler().sign(String.valueOf(nonce).getBytes(), serverKey)));
                }
            } catch (Exception e) {
                response.setNonce(nonce);
                response.setSignature(ByteString.copyFrom(new SignatureHandler().sign(String.valueOf(nonce).getBytes(), serverKey)));
            }
            responseObserver.onNext(response.build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erro a assinar");
        }

    }

    private PublicKey getPubKey(ByteString key) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key.toByteArray()));
        } catch (Exception e) {
            return null;
        }
    }

    public static long generateProofOfWork(byte[] bytes, int powDifficulty) {
        long pow = 0L;
        while (!verifyProofOfWork(bytes, pow, powDifficulty)) pow++;
        return pow;
    }

    public static boolean verifyProofOfWork(byte[] bytes, long pow, int powDifficulty) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash =
                    md.digest(ByteBuffer.allocate(bytes.length + Long.BYTES).put(bytes).putLong(pow).array());
            for (int i = 0; i < powDifficulty; i++) {
                if (hash[i] != 0) return false;
            }
            return true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }

    }

}
