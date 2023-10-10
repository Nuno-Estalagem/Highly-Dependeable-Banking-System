import Handlers.CommandHandler;
import Handlers.SignatureHandler;
import Handlers.StubHandler;
import cipher.CipherHandler;
import com.SEC.grpc.BFTB;
import com.SEC.grpc.BTFTBGrpc;
import com.google.protobuf.ByteString;
import domain.Client;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import managers.FakeStubManager;
import managers.StubManager;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClientTest {

    String path = "src/main/resources/keystores";
    String regex = "server_[0-9][0-9][0-9][0-9].cer";
    Pattern p = Pattern.compile(regex);

    List<ManagedChannel> channels = new ArrayList<>();
    HashMap<String, BTFTBGrpc.BTFTBBlockingStub> stubs = new HashMap<>();

    private final static String KEYS_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "keystores" + File.separator;

    File dir = new File(path);

    private int byzantineServers;

    private int byzantineQuorum;

    private StubManager stubManager;

    private FakeStubManager fakeStubManager;

    private CommandHandler commandHandler;


    private int rid=0;

    @BeforeEach
    public void setUp() {
        try {
            commandHandler=new CommandHandler(2,7);
            if (dir.isDirectory()) {
                File[] ficheiros = dir.listFiles((File file) -> p.matcher(file.getName()).matches());
                for (File file : ficheiros) {
                    String port = file.getPath().split("_")[1].split("\\.")[0];
                    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", Integer.parseInt(port)).usePlaintext().build();
                    channels.add(channel);
                    stubs.put(port, BTFTBGrpc.newBlockingStub(channel));



                }

            }
            this.byzantineServers = 2;
            byzantineQuorum = (7 + byzantineServers) / 2 + 1;
            StubHandler handler= new StubHandler();
            this.stubManager=new StubManager(handler,byzantineQuorum,byzantineServers,stubs);
            this.fakeStubManager=new FakeStubManager(handler,byzantineQuorum,byzantineServers,stubs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @AfterEach
    public void destroy() throws InterruptedException {
        commandHandler.shutdown();
        for (ManagedChannel channel : channels) {
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }





    @Test
    @Order(1)
    void valid_open_account() {
        try{
            setParams("username", "password");
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/username.jks", "password", "username");


            assertEquals( "Account created successfully!",commandHandler.open_account(client.getPublicKey(),"username",600,client,stubManager));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    @Order(2)
    void invalid_open_account() {
        try {
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/username.jks", "password", "username");
            assertEquals("An account associated with this key already exists!", commandHandler.open_account(client.getPublicKey(), "username2", 600, client, stubManager));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    void invalid_open_account_username() {
        try {
            setParams("username2", "password2");
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username2" + ".jks", "password2", "username2");
            assertEquals( "An account associated with this username already exists!",commandHandler.open_account(client.getPublicKey(), "username", 600, client, stubManager));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Order(4)
    void check_account() {
        try {
            rid+=1;
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username" + ".jks", "password", "username");
            assertEquals("ACCOUNT BALANCE: 600\n" ,commandHandler.check_account(client.getPublicKey(),client,stubManager,rid));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    void incorrect_check_account() {
        try {
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username" + ".jks", "password", "username");
            assertEquals("invalid rid" ,commandHandler.check_account(client.getPublicKey(),client,stubManager,rid));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @Order(6)
    void send_amount() {
        try {
            rid=rid+1;
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username" + ".jks", "password", "username");
            Client client2 = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username2" + ".jks", "password2", "username2");
            commandHandler.open_account(client2.getPublicKey(),"username2",600,client2,stubManager);
            assertEquals("Transaction awaits approval! :-)",commandHandler.send_amount(client.getPublicKey(),client2.getPublicKey(),100,client,"username2",stubManager,rid,"username"));
        } catch (Exception e) {
          e.printStackTrace();
        }


    }
    @Test
    @Order(7)
    void tooMuch_send_amount() {
        try {
            rid=rid+2;
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username" + ".jks", "password", "username");
            Client client2 = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username2" + ".jks", "password2", "username2");
            commandHandler.open_account(client2.getPublicKey(),"username2",600,client2,stubManager);
            assertEquals("You do not have enough money to perform the transaction!",commandHandler.send_amount(client.getPublicKey(),client2.getPublicKey(),9000,client,"username2",stubManager,rid,"username"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(8)
    void byzantineSend_amount() {
        try {

            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username" + ".jks", "password", "username");
            Client client2 = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username2" + ".jks", "password2", "username2");
            rid=rid+3;
            BFTB.CheckAccountRequest checkAccountRequest = BFTB.CheckAccountRequest.newBuilder().setToCheckPubKey(ByteString.copyFrom(client.getPublicKey().getEncoded())).setRid(rid).setMyPubkey(ByteString.copyFrom(client.getPublicKey().getEncoded())).build();
            BFTB.AccountResponse accountResponse = (BFTB.AccountResponse) stubManager.generateMethod("check", checkAccountRequest, client);

            int wid=accountResponse.getWid();
            wid=wid+1;

            int balance= accountResponse.getBalance()-10;
            int balance2=accountResponse.getBalance()-20;
            int amount=10;
            int amount2=20;

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(String.valueOf(amount).getBytes());
            outputStream.write(client.getPublicKey().getEncoded());
            outputStream.write(client2.getPublicKey().getEncoded());
            outputStream.write("username".getBytes());
            outputStream.write(String.valueOf(wid).getBytes());
            outputStream.write("username2".getBytes());

            ByteArrayOutputStream outputStreamFake = new ByteArrayOutputStream();
            outputStreamFake.write(String.valueOf(amount2).getBytes());
            outputStreamFake.write(client.getPublicKey().getEncoded());
            outputStreamFake.write(client2.getPublicKey().getEncoded());
            outputStreamFake.write("username".getBytes());
            outputStreamFake.write(String.valueOf(wid).getBytes());
            outputStreamFake.write("username2".getBytes());

            ByteString sourceByte = ByteString.copyFrom(client.getPublicKey().getEncoded());
            ByteString destByte = ByteString.copyFrom(client2.getPublicKey().getEncoded());

            SignatureHandler signatureHandler = new SignatureHandler();


            BFTB.Transaction transaction = BFTB.Transaction.newBuilder().setAmount(amount).setDest(destByte).setSource(sourceByte).setSenderUsername("username").setReceiverUsername("username2").setWid(wid).setSignature(ByteString.copyFrom(signatureHandler.sign(outputStream.toByteArray(), client.getPrivateKey()))).build();
            BFTB.Transaction transaction2 = BFTB.Transaction.newBuilder().setAmount(amount2).setDest(destByte).setSource(sourceByte).setSenderUsername("username").setReceiverUsername("username2").setWid(wid).setSignature(ByteString.copyFrom(signatureHandler.sign(outputStreamFake.toByteArray(), client.getPrivateKey()))).build();

            String balancePluswid = String.valueOf(balance) + wid;

            byte[] signedBalancePlusWid = signatureHandler.sign(balancePluswid.getBytes(), client.getPrivateKey());

            String balancePluswid2 = String.valueOf(balance2) + wid;

            byte[] signedBalancePlusWid2 = signatureHandler.sign(balancePluswid2.getBytes(), client.getPrivateKey());



            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(signedBalancePlusWid);
            outputStream.write(transaction.toByteArray());

            outputStreamFake.write(String.valueOf(balance2).getBytes());
            outputStreamFake.write(signedBalancePlusWid2);
            outputStreamFake.write(transaction2.toByteArray());



            BFTB.SendAmountRequest sendAmountRequest = BFTB.SendAmountRequest.newBuilder().setTransaction(transaction).setBalance(balance).setBalancePlusWidSign(ByteString.copyFrom(signedBalancePlusWid)).setMessageSignature(ByteString.copyFrom(signatureHandler.sign(outputStream.toByteArray(), client.getPrivateKey()))).build();
            BFTB.SendAmountRequest sendAmountRequest2 = BFTB.SendAmountRequest.newBuilder().setTransaction(transaction2).setBalance(balance2).setBalancePlusWidSign(ByteString.copyFrom(signedBalancePlusWid2)).setMessageSignature(ByteString.copyFrom(signatureHandler.sign(outputStreamFake.toByteArray(), client.getPrivateKey()))).build();

            List<BFTB.SendAmountRequest> lista= new ArrayList<>();
            lista.add(sendAmountRequest);
            lista.add(sendAmountRequest);
            lista.add(sendAmountRequest);
            lista.add(sendAmountRequest);
            lista.add(sendAmountRequest);
            lista.add(sendAmountRequest2);
            lista.add(sendAmountRequest2);

            String contents = (String) fakeStubManager.generateMethod("send", Collections.singletonList(lista),client );
            assertEquals("Transaction awaits approval! :-)",contents);





        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Test
    @Order(9)
    void check_account_badRid() {
        try {
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username" + ".jks", "password", "username");
            rid=rid+1;
            BFTB.CheckAccountRequest checkAccountRequest = BFTB.CheckAccountRequest.newBuilder().setToCheckPubKey(ByteString.copyFrom(client.getPublicKey().getEncoded())).setRid(rid).setMyPubkey(ByteString.copyFrom(client.getPublicKey().getEncoded())).build();
            BFTB.AccountResponse accountResponse = (BFTB.AccountResponse) stubManager.generateMethod("check", checkAccountRequest, client);
            assertEquals("invalid rid",accountResponse.getMessage());

        }catch (Exception e){
                e.printStackTrace();
        }
    }

    @Test
    @Order(10)
    void receive_amount_badly_signed() {
        try {
            Client client2 = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username2" + ".jks", "password2", "username2");
            SignatureHandler handler = new SignatureHandler();
            ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
            rid=rid+4;
            BFTB.CheckAccountRequest checkAccountRequest = BFTB.CheckAccountRequest.newBuilder().setToCheckPubKey(ByteString.copyFrom(client2.getPublicKey().getEncoded())).setRid(rid).setMyPubkey(ByteString.copyFrom(client2.getPublicKey().getEncoded())).build();
            BFTB.AccountResponse accountResponse = (BFTB.AccountResponse) stubManager.generateMethod("check", checkAccountRequest, client2);

            List<BFTB.Transaction> transactions= accountResponse.getListList();
            BFTB.Transaction t= transactions.get(Integer.parseInt("1")-1);

            int wid=accountResponse.getWid();
            wid=wid+1;

            int balance=accountResponse.getBalance()+t.getAmount();

            byte[] balancePluswidSigned = handler.sign((String.valueOf(balance) + wid).getBytes(), client2.getPrivateKey());

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
                    .setSignature(ByteString.copyFrom(handler.sign(transacStream.toByteArray(), client2.getPrivateKey()))).build();


            outputStream.write(String.valueOf(balance).getBytes());
            outputStream.write(String.valueOf(wid).getBytes());
            outputStream.write("1".getBytes());
            outputStream.write(client2.getPublicKey().getEncoded());
            outputStream.write(new byte[256]);
            outputStream.write(newTransaction.toByteArray());

            byte[]sign=handler.sign(outputStream.toByteArray(),client2.getPrivateKey());

            BFTB.ReceiveAmountRequest receiveAmountRequest = BFTB.ReceiveAmountRequest.newBuilder().setKey(ByteString.copyFrom(client2.getPublicKey().getEncoded()))
                    .setId("1").setWid(wid).setBalance(balance).setWidAndBalance(ByteString.copyFrom(balancePluswidSigned)).setTransaction(newTransaction).setSignature(ByteString.copyFrom(sign)).build();

             String value = (String) stubManager.generateMethod("receive", receiveAmountRequest, client2);
             assertEquals("Non-Repudiation not assured!!!!",value);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Order(11)
    void receive_amount() {
        try {
            rid=rid+5;
            Client client2 = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username2" + ".jks", "password2", "username2");
            assertEquals("You received the Amount 100 from username",commandHandler.receive_amount(client2.getPublicKey(),"1",client2,stubManager,rid));


        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Test
    @Order(13)
    void inexistent_transaction() {
        try {
            rid=rid+6;
            Client client2 = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username2" + ".jks", "password2", "username2");
            assertEquals("There is no transaction with that id",commandHandler.receive_amount(client2.getPublicKey(),"5",client2,stubManager,rid));


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Test
    @Order(14)
    void audit() {
        try {
            Client client = Client.loadClient("src/main/resources/keystores", "src/main/resources/keystores/" + "username" + ".jks", "password", "username");
            rid=rid+7;
            assertEquals("TRANSACTION FROM: username TO: username2 AMOUNT: 100 ID: 1\n" + "TRANSACTION FROM: username TO: username2 AMOUNT: 10 ID: 2\n",commandHandler.audit(client.getPublicKey(),client,stubManager,rid,"username"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }









    private void setParams(String username, String password) {
        try {
            String[] ksArray = new String[12];
            ksArray[0] = "keytool";
            ksArray[1] = "-genkey";
            ksArray[2] = "-alias";
            ksArray[3] = username;
            ksArray[4] = "-keyalg";
            ksArray[5] = "RSA";
            ksArray[6] = "-keystore";
            ksArray[7] = username + ".jks";
            ksArray[8] = "-dname";
            ksArray[9] = "CN=, OU=, O=, L=, S=, C=";
            ksArray[10] = "-storepass";
            ksArray[11] = password;


            ProcessBuilder builder = new ProcessBuilder(ksArray);
            builder.directory(new File(KEYS_DIR));
            Process pr = builder.start();
            pr.waitFor();

            String[] cerArray = new String[10];
            cerArray[0] = "keytool";
            cerArray[1] = "-export";
            cerArray[2] = "-alias";
            cerArray[3] = username;
            cerArray[4] = "-storepass";
            cerArray[5] = password;
            cerArray[6] = "-file";
            cerArray[7] = username + ".cer";
            cerArray[8] = "-keystore";
            cerArray[9] = username + ".jks";


            builder.command(cerArray).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}