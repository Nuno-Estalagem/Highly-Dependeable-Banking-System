package services;

import com.SEC.grpc.ADEBGrpc;
import com.SEC.grpc.ADEBOuterClass;
import com.SEC.grpc.BFTB;
import com.google.protobuf.ByteString;
import domain.ADEBInstance;
import domain.Client;
import handlers.SignatureHandler;
import io.grpc.stub.StreamObserver;
import managers.ADEBInstanceManager;
import managers.ADEBManager;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Hashtable;

public class ADEBservice extends ADEBGrpc.ADEBImplBase {


    private ADEBManager adebManager;

    private final SignatureHandler signatureHandler;
    private final int byzantineQuorum;

    private ADEBInstanceManager adebInstanceManager;

    private Client serverClient;

    private int port;

    public ADEBservice(int byzantineQuorum, Client serverClient, ADEBManager adebManager, ADEBInstanceManager adebInstanceManager, int port) {
        signatureHandler = new SignatureHandler();
        this.byzantineQuorum = byzantineQuorum;
        this.adebInstanceManager = adebInstanceManager;
        this.serverClient = serverClient;
        this.adebManager=adebManager;
        this.port=port;
    }


    @Override
    public void echo(ADEBOuterClass.EchoRequest request, StreamObserver<ADEBOuterClass.EchoResponse> responseObserver) {
        try {
            System.out.println("Received echo");
            byte[] nonceToSign = request.getNonce().toByteArray();
            byte[] hashofParams = request.getParams().toByteArray();
            byte[] signedParams = request.getSigned().toByteArray();
            PublicKey senderPukey = getPubKey(request.getPubkey());
            if (signatureHandler.verifySignature(signedParams, senderPukey, hashofParams)) {
                ADEBInstance instance = adebInstanceManager.getInstance(Arrays.toString(hashofParams));
                synchronized (instance) {
                    instance.addEcho();
                    //System.out.println(adebInstanceManager.test());
                    //System.out.println(instance.getEchoCounter()+"-----"+byzantineQuorum);
                    if (instance.getEchoCounter() >= byzantineQuorum) {
                        if (!instance.HasSentReady()) {
                            instance.setHasSentReady();
                            //System.out.println("cona4");
                            byte[] rand = new byte[256];
                            SecureRandom secureRandom = new SecureRandom();
                            secureRandom.nextBytes(rand);
                            serverClient = Client.loadClient("src/main/resources/", "src/main/resources/" + port + "/" + "server_" + port + ".jks", "server_" + port, "server_" + port);
                            ADEBOuterClass.ReadyRequest readyRequest = ADEBOuterClass.ReadyRequest.newBuilder().setParams(ByteString.copyFrom(hashofParams)).setNonce(ByteString.copyFrom(rand))
                                    .setSigned(ByteString.copyFrom(signatureHandler.sign(hashofParams, serverClient.getPrivateKey()))).setPubkey(ByteString.copyFrom(serverClient.getPublicKey().getEncoded())).build();
                            System.out.println("Sending ready");
                            adebManager.ready(readyRequest, serverClient);
                        }
                    }
                }

                byte[] signed = signatureHandler.sign(nonceToSign, serverClient.getPrivateKey());
                ADEBOuterClass.EchoResponse.Builder response = ADEBOuterClass.EchoResponse.newBuilder();
                response.setSigned(ByteString.copyFrom(signed));
                response.setAck("ack");

                responseObserver.onNext(response.build());
                responseObserver.onCompleted();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ready(ADEBOuterClass.ReadyRequest request, StreamObserver<ADEBOuterClass.ReadyResponse> responseObserver) {
        try {
            System.out.println("Received ready");
            byte[] nonceToSign = request.getNonce().toByteArray();
            byte[] hashofParams = request.getParams().toByteArray();
            byte[] signedParams = request.getSigned().toByteArray();
            PublicKey senderPukey = getPubKey(request.getPubkey());


            if (signatureHandler.verifySignature(signedParams, senderPukey, hashofParams)) {
                ADEBInstance instance = adebInstanceManager.getInstance(Arrays.toString(hashofParams));
                synchronized (instance) {
                    instance.addReady();
                    //System.out.println(adebInstanceManager.test());
                    //System.out.println(instance.getReadyCounter()+"-----"+byzantineQuorum);
                    //System.out.println("aqui");
                    if (instance.getReadyCounter() >= byzantineQuorum) {
                        if (!instance.HasSentReady()) {
                            instance.setHasSentReady();
                            byte[] rand = new byte[256];
                            SecureRandom secureRandom = new SecureRandom();
                            secureRandom.nextBytes(rand);
                            ADEBOuterClass.ReadyRequest readyRequest = ADEBOuterClass.ReadyRequest.newBuilder().setParams(ByteString.copyFrom(hashofParams)).setNonce(ByteString.copyFrom(rand))
                                    .setSigned(ByteString.copyFrom(signatureHandler.sign(hashofParams, serverClient.getPrivateKey()))).setPubkey(ByteString.copyFrom(serverClient.getPublicKey().getEncoded())).build();
                            adebManager.ready(readyRequest, serverClient);
                        }
                        if (!instance.hasDelivered() && instance.getReadyCounter()>=byzantineQuorum){
                            adebInstanceManager.deliver(Arrays.toString(hashofParams));

                        }
                    }
                }

                byte[] signed = signatureHandler.sign(nonceToSign, serverClient.getPrivateKey());
                ADEBOuterClass.ReadyResponse.Builder response = ADEBOuterClass.ReadyResponse.newBuilder();
                response.setSigned(ByteString.copyFrom(signed));
                response.setAck("ack");

                responseObserver.onNext(response.build());
                responseObserver.onCompleted();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private PublicKey getPubKey(ByteString key) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key.toByteArray()));
        } catch (Exception e) {
            return null;
        }
    }
}
