package Handlers;

import cipher.CipherHandler;
import com.SEC.grpc.BFTB;
import com.SEC.grpc.BTFTBGrpc;
import com.google.common.primitives.Ints;
import com.google.protobuf.ByteString;
import domain.Client;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import managers.StubManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class CommandHandler {
    List<ManagedChannel> channels = new ArrayList<>();
    String path = "src/main/resources/keystores";
    String regex = "server_[0-9][0-9][0-9][0-9].cer";
    Pattern p = Pattern.compile(regex);
    File dir = new File(path);

    private int byzantineServers;

    private int byzantineQuorum;

    private int rid;



    // stubs - generate from proto
    HashMap<String, BTFTBGrpc.BTFTBBlockingStub> stubs = new HashMap<>();//BTFTBGrpc.BTFTBBlockingStub userStub = BTFTBGrpc.newBlockingStub(channel);
    private Client cliente = null;
    private final String ERROR = "Error: Invalid Parameters";
    private final static String KEYS_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "keystores" + File.separator;
    private String username;

    private StubManager stubManager;

    private int numServer;

    public CommandHandler(int byzantineServers, int numServer) {

        if (dir.isDirectory()) {
            File[] ficheiros = dir.listFiles((File file) -> p.matcher(file.getName()).matches());
            for (File file : ficheiros) {
                String port = file.getPath().split("_")[1].split("\\.")[0];
                ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", Integer.parseInt(port)).usePlaintext().build();
                channels.add(channel);
                stubs.put(port, BTFTBGrpc.newBlockingStub(channel));
            }

        }
        this.byzantineServers = byzantineServers;
        byzantineQuorum = (numServer + byzantineServers) / 2 + 1;


    }

    public String createAccount() {
        try {
            CreateNewUserHandler.createUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        System.out.println("You'll be redirected to the login page again!\n\n\n");
        List<String> parameters = getParameters();
        String username = parameters.get(0);
        String password = parameters.get(1);
        int balance = Integer.parseInt(parameters.get(2));

            login(username, password);
            this.rid = 0;
            return open_account(cliente.getPublicKey(), username, balance,cliente,stubManager);

        } catch (Exception e) {
            System.out.println("invalid parameters!");
            System.exit(1);
        }
        return "";
    }


    public String handleCommand(String command) {

        List<String> full_command = new ArrayList<>(Arrays.asList(command.split(" ")));
        String commandType = full_command.get(0);
        full_command.remove(0);

        if (commandType.contentEquals("send") || commandType.contentEquals("sa")) {
            if (full_command.size() == 2) {
                if (!full_command.get(0).contentEquals(username)) {
                    try {
                        this.rid+=1;
                        return send_amount(cliente.getPublicKey(), CipherHandler.getCertificateFromTrustStore(KEYS_DIR + full_command.get(0) + ".cer").getPublicKey(), Integer.parseInt(full_command.get(1)), cliente,full_command.get(0),stubManager,rid,username);
                    } catch (Exception e) {
                        return "You can't perform a transaction to someone who does not exist!";
                    }

                }
                return "You can't perform a transaction to yourself";
            } else {
                return ERROR;
            }

        }
        if (commandType.contentEquals("check") || commandType.contentEquals("ca")) {
            if (full_command.size() == 1) {
                try {
                    this.rid=rid+1;
                    return check_account(CipherHandler.getCertificateFromTrustStore(KEYS_DIR + full_command.get(0) + ".cer").getPublicKey(), cliente, stubManager,rid);
                } catch (Exception e) {
                    return "That user does not exist!";
                }
            }
            return ERROR;
        }
        if (commandType.contentEquals("receive") || commandType.contentEquals("ra")) {
            if (full_command.size() == 1) {
                this.rid+=1;
                return receive_amount(cliente.getPublicKey(), full_command.get(0), cliente,stubManager,rid);


            } else {
                return ERROR;
            }
        }

        if (commandType.contentEquals("audit") || commandType.contentEquals("ad")) {
            if (full_command.size() == 1) {
                try {
                    this.rid=rid+1;
                    return audit(CipherHandler.getCertificateFromTrustStore(KEYS_DIR + full_command.get(0) + ".cer").getPublicKey(), cliente,stubManager,rid,username);
                } catch (Exception e) {
                    return "That user does not exist!";
                }

            } else {
                return ERROR;
            }

        }

        return ERROR;
    }


    public String open_account(PublicKey publicKey, String username, int balance,Client cliente, StubManager stubManager) {
        try {
            SignatureHandler handler = new SignatureHandler();
            String balancePluswid = String.valueOf(balance) + 0;
            byte[] signedbalancepluswid = handler.sign(balancePluswid.getBytes(), cliente.getPrivateKey());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            outputStream.write(publicKey.getEncoded());
            outputStream.write(username.getBytes());
            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(signedbalancepluswid);

            byte[] signature= handler.sign(outputStream.toByteArray(), cliente.getPrivateKey());

            BFTB.OpenAccountRequest openAccountRequest = BFTB.OpenAccountRequest.newBuilder()
                    .setKey(ByteString.copyFrom(publicKey.getEncoded()))
                    .setUsername(username)
                    .setBalance(balance)
                    .setWid(0)
                    .setBalancePlusWidSign(ByteString.copyFrom(signedbalancepluswid))
                    .setSignature(ByteString.copyFrom(signature)).build();

            return (String) stubManager.generateMethod("open", openAccountRequest, cliente);
        } catch (Exception e) {
            System.out.println("Exception signing");
            return "Could not create account due to signing error";
        }
    }


    public String check_account(PublicKey publicKey, Client cliente,StubManager stubManager, int rid) {
        try {
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();


        BFTB.CheckAccountRequest checkAccountRequest = BFTB.CheckAccountRequest.newBuilder().setToCheckPubKey(ByteString.copyFrom(publicKey.getEncoded())).setRid(rid).setMyPubkey(ByteString.copyFrom(cliente.getPublicKey().getEncoded())).build();


            BFTB.AccountResponse accountResponse = (BFTB.AccountResponse) stubManager.generateMethod("check", checkAccountRequest, cliente);
            if(accountResponse.getMessage().length()!=0){
                return accountResponse.getMessage();
            }
            StringBuilder stringBuilder = new StringBuilder();
            int balance=accountResponse.getBalance();
            List<BFTB.Transaction> transactions=accountResponse.getListList();
            stringBuilder.append("ACCOUNT BALANCE: " + balance + "\n");
            int i = 0;
            for (BFTB.Transaction transacao : transactions) {

                stringBuilder.append("TRANSACTION FROM: " + transacao.getSenderUsername() + " TO: " + transacao.getReceiverUsername() + " AMOUNT: " + transacao.getAmount() + " ID: " + transacao.getPosition() + "\n");
            }


            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(String.valueOf(accountResponse.getRid()).getBytes());
            outputStream.write(String.valueOf(accountResponse.getListList()).getBytes());
            outputStream.write(accountResponse.getBalancePlusWidSign().toByteArray());
            outputStream.write(String.valueOf(accountResponse.getWid()).getBytes());
            outputStream.write(cliente.getPublicKey().getEncoded());
            outputStream.write(publicKey.getEncoded());

            BFTB.CAWBRequest cawbRequest = BFTB.CAWBRequest.newBuilder()
                    .setBalance(balance)
                    .setRid(accountResponse
                            .getRid())
                    .addAllTransaction(accountResponse.getListList())
                    .setBalancePlusWid(accountResponse.getBalancePlusWidSign())
                    .setWid(accountResponse.getWid())
                    .setMyKey(ByteString.copyFrom(cliente.getPublicKey().getEncoded()))
                    .setCheckedKey(ByteString.copyFrom(publicKey.getEncoded()))
                    .setSignature(ByteString.copyFrom(new SignatureHandler().sign(outputStream.toByteArray(),cliente.getPrivateKey()))).build();

            BFTB.CAWBResponse cawbResponse = (BFTB.CAWBResponse) stubManager.generateMethod("CAWB",cawbRequest,cliente);
            System.out.println(cawbResponse.getMessage());

            return stringBuilder.toString();

        }catch (Exception e){
            return "";
        }


    }



    public String receive_amount(PublicKey publicKey, String id, Client cliente, StubManager stubManager, int rid) {
        try {
            SignatureHandler handler = new SignatureHandler();
            ByteArrayOutputStream outputStream= new ByteArrayOutputStream();

            BFTB.CheckAccountRequest checkAccountRequest = BFTB.CheckAccountRequest.newBuilder().setToCheckPubKey(ByteString.copyFrom(publicKey.getEncoded())).setRid(rid).setMyPubkey(ByteString.copyFrom(cliente.getPublicKey().getEncoded())).build();
            BFTB.AccountResponse accountResponse = (BFTB.AccountResponse) stubManager.generateMethod("check", checkAccountRequest, cliente);

            List<BFTB.Transaction> transactions= accountResponse.getListList();
            BFTB.Transaction t= transactions.get(Integer.parseInt(id)-1);

            int wid=accountResponse.getWid();
            wid=wid+1;

            int balance=accountResponse.getBalance()+t.getAmount();

            byte[] balancePluswidSigned = handler.sign((String.valueOf(balance) + wid).getBytes(), cliente.getPrivateKey());

            ByteArrayOutputStream transacStream = new ByteArrayOutputStream();

            transacStream.write(String.valueOf(t.getAmount()).getBytes());
            transacStream.write(t.getSource().toByteArray());
            transacStream.write(t.getDest().toByteArray());
            transacStream.write(t.getSenderUsername().getBytes());
            transacStream.write(String.valueOf(wid).getBytes());
            transacStream.write(t.getReceiverUsername().getBytes());

            BFTB.Transaction newTransaction = BFTB.Transaction.newBuilder()
                    .setAmount(t.getAmount())
                    .setSource(t.getSource())
                    .setDest(t.getDest())
                    .setWid(wid)
                    .setSenderUsername(t.getSenderUsername())
                    .setReceiverUsername(t.getReceiverUsername())
                            .setSignature(ByteString.copyFrom(handler.sign(transacStream.toByteArray(), cliente.getPrivateKey()))).build();


            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(String.valueOf(wid).getBytes());
            outputStream.write(id.getBytes());
            outputStream.write(publicKey.getEncoded());
            outputStream.write(balancePluswidSigned);
            outputStream.write(newTransaction.toByteArray());

            byte[]sign=handler.sign(outputStream.toByteArray(),cliente.getPrivateKey());

            BFTB.ReceiveAmountRequest receiveAmountRequest = BFTB.ReceiveAmountRequest.newBuilder().setKey(ByteString.copyFrom(publicKey.getEncoded()))
                    .setId(id).setWid(wid).setBalance(balance).setWidAndBalance(ByteString.copyFrom(balancePluswidSigned)).setTransaction(newTransaction).setSignature(ByteString.copyFrom(sign)).build();

            return (String) stubManager.generateMethod("receive", receiveAmountRequest, cliente);


        } catch (Exception e) {
            //e.printStackTrace();
            return "There is no transaction with that id";
        }
    }


    public String send_amount(PublicKey source, PublicKey dest, int amount, Client cliente, String destUsername, StubManager stubManager, int rid, String username) {

        try {

            BFTB.CheckAccountRequest checkAccountRequest = BFTB.CheckAccountRequest.newBuilder().setToCheckPubKey(ByteString.copyFrom(source.getEncoded())).setRid(rid).setMyPubkey(ByteString.copyFrom(cliente.getPublicKey().getEncoded())).build();
            BFTB.AccountResponse accountResponse = (BFTB.AccountResponse) stubManager.generateMethod("check", checkAccountRequest, cliente);

            int wid=accountResponse.getWid();
            wid=wid+1;

            int balance= accountResponse.getBalance()-amount;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            outputStream.write(String.valueOf(amount).getBytes());
            outputStream.write(source.getEncoded());
            outputStream.write(dest.getEncoded());
            outputStream.write(username.getBytes());

            outputStream.write(String.valueOf(wid).getBytes());
            outputStream.write(destUsername.getBytes());


            ByteString sourceByte = ByteString.copyFrom(source.getEncoded());
            ByteString destByte = ByteString.copyFrom(dest.getEncoded());

            SignatureHandler signatureHandler = new SignatureHandler();


            BFTB.Transaction transaction = BFTB.Transaction.newBuilder().setAmount(amount).setDest(destByte).setSource(sourceByte).setSenderUsername(username).setReceiverUsername(destUsername).setWid(wid).setSignature(ByteString.copyFrom(signatureHandler.sign(outputStream.toByteArray(), cliente.getPrivateKey()))).build();

            String balancePluswid = String.valueOf(balance) + wid;

            byte[] signedBalancePlusWid = signatureHandler.sign(balancePluswid.getBytes(), cliente.getPrivateKey());


            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(signedBalancePlusWid);
            outputStream.write(transaction.toByteArray());



            BFTB.SendAmountRequest sendAmountRequest = BFTB.SendAmountRequest.newBuilder().setTransaction(transaction).setBalance(balance).setBalancePlusWidSign(ByteString.copyFrom(signedBalancePlusWid)).setMessageSignature(ByteString.copyFrom(signatureHandler.sign(outputStream.toByteArray(), cliente.getPrivateKey()))).build();

            String contents = (String) stubManager.generateMethod("send", sendAmountRequest, cliente);

            return contents;


        } catch (Exception e) {
            System.out.println("Error in Send_amount");
        }


        return null;
    }


    public String audit(PublicKey publicKey, Client cliente, StubManager stubManager, int rid, String username) {
        BFTB.AuditRequest audit = BFTB.AuditRequest.newBuilder().setKeyToCheck(ByteString.copyFrom(publicKey.getEncoded())).setRid(rid).setMyKey(ByteString.copyFrom(cliente.getPublicKey().getEncoded())).build();
        try {
            BFTB.AuditTransactionResponse auditTransactionResponse = (BFTB.AuditTransactionResponse) stubManager.generateMethod("audit", audit, cliente);
            if(auditTransactionResponse.getMessage().length()!=0){
                return auditTransactionResponse.getMessage();
            }
            StringBuilder stringBuilder = new StringBuilder();
            List<BFTB.Transaction> transactions=auditTransactionResponse.getListList();
            for (BFTB.Transaction transacao : transactions) {

                stringBuilder.append("TRANSACTION FROM: " + transacao.getSenderUsername() + " TO: " + transacao.getReceiverUsername() + " AMOUNT: " + transacao.getAmount() + " ID: " + transacao.getPosition() + "\n");
            }


            ByteArrayOutputStream outputStream= new ByteArrayOutputStream();

            outputStream.write(String.valueOf(auditTransactionResponse.getRid()).getBytes());
            outputStream.write(String.valueOf(auditTransactionResponse.getListList()).getBytes());
            outputStream.write(cliente.getPublicKey().getEncoded());
            outputStream.write(publicKey.getEncoded());

            BFTB.ADWBRequest cawbRequest = BFTB.ADWBRequest.newBuilder()
                    .setRid(auditTransactionResponse.getRid())
                    .addAllTransaction(auditTransactionResponse.getListList())
                    .setMyKey(ByteString.copyFrom(cliente.getPublicKey().getEncoded()))
                    .setCheckedKey(ByteString.copyFrom(publicKey.getEncoded()))
                    .setSignature(ByteString.copyFrom(new SignatureHandler().sign(outputStream.toByteArray(),cliente.getPrivateKey()))).build();

            BFTB.ADWBResponse adwbResponse= (BFTB.ADWBResponse) stubManager.generateMethod("ADWB",cawbRequest,cliente);
            System.out.println(adwbResponse.getMessage());

            return stringBuilder.toString();

        }catch (Exception e){
            return "";
        }
    }


    public void login(String username, String password) throws UnrecoverableKeyException, CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
        this.username = username;
        this.cliente = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + username + ".jks", password, username);
        StubHandler stubHandler = new StubHandler();
        stubManager = new StubManager(stubHandler, byzantineQuorum, byzantineServers, stubs);

    }

    private static List<String> getParameters() {
        Scanner sc = new Scanner(System.in);
        List<String> parameters = new ArrayList<>();


        System.out.println("-Insert your username");
        parameters.add(sc.next());

        System.out.println("-Insert your Keystore's password");
        parameters.add(sc.next());

        System.out.println("-Insert the balance you'd like to open your account with");
        parameters.add(sc.next());


        return parameters;
    }


    public void shutdown() throws InterruptedException {
        for (ManagedChannel channel : channels)
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void loadRid() {
        try {
            BFTB.GetRidRequest getRidRequest = BFTB.GetRidRequest.newBuilder()
                    .setMyKey(ByteString.copyFrom(cliente.getPublicKey().getEncoded()))
                    .setSignature(ByteString.copyFrom(new SignatureHandler().sign(cliente.getPublicKey().getEncoded(), cliente.getPrivateKey()))).build();
            try {
                this.rid = (int) stubManager.generateMethod("rid", getRidRequest, cliente);
            }
            catch (Exception e){
                System.out.println("No quorum for RID");
            }

        }
        catch (Exception e){
            System.out.println("erro a assinar");
        }
    }
}
