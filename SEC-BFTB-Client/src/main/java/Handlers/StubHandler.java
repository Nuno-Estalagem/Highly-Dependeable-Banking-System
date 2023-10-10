package Handlers;

import com.SEC.grpc.BFTB;
import com.SEC.grpc.BTFTBGrpc;
import com.google.common.primitives.Bytes;
import com.google.protobuf.ByteString;
import domain.Client;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class StubHandler {


    public StubHandler() {

    }


    public  BFTB.APIresponse open_account(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.OpenAccountRequest openAccountRequest, CountDownLatch countDownLatch) {
        try {
            BFTB.APIresponse response = btftbBlockingStub.openAccount(openAccountRequest);
            countDownLatch.countDown();
            return response;
        }catch (Exception e){
            System.out.println("A server died...");
            return null;
        }
    }



    public BFTB.ReceiveAmountResponse receive_amount(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.ReceiveAmountRequest request, CountDownLatch countDownLatch) {
            try {
                BFTB.ReceiveAmountResponse response = btftbBlockingStub.receiveAmount(request);
                countDownLatch.countDown();
                return response;
            }catch (Exception e){
                System.out.println("A server died...");
                return null;
            }
    }


    public BFTB.SendAmountResponse send_amount(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.SendAmountRequest request, CountDownLatch countDownLatch) {
        try {
            BFTB.SendAmountResponse response = btftbBlockingStub.sendAmount(request);
            countDownLatch.countDown();
            return response;
        }catch (Exception e){
            System.out.println("A server died...");
            return null;
        }


    }

    public BFTB.AuditTransactionResponse audit(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.AuditRequest request, CountDownLatch countDownLatch, PrivateKey privateKey, PublicKey serverKey) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            long nonce = secureRandom.nextLong();
            PublicKey myKey= getPubKey(request.getMyKey());
            byte [] toCheckKey=request.getKeyToCheck().toByteArray();
            byte[] signature= (nonce+ Arrays.toString(myKey.getEncoded())).getBytes();
            SignatureHandler signatureHandler= new SignatureHandler();
            BFTB.PowRequest powRequest= BFTB.PowRequest.newBuilder().setMyPublicKey(request.getMyKey()).setNonce(nonce).setSignature(ByteString.copyFrom(signatureHandler.sign(signature,privateKey))).build();
            BFTB.PowResponse powResponse=btftbBlockingStub.pOWRequest(powRequest);

            byte [] challenge=powResponse.getChallenge().toByteArray();
            byte [] signed = powResponse.getSignature().toByteArray();
            long repliedNonce=powResponse.getNonce();

            if (repliedNonce==nonce+1){
                if (signatureHandler.verifySignature(signed,serverKey,(Arrays.toString(challenge)+repliedNonce).getBytes())){
                    byte[] salted= Bytes.concat(toCheckKey,challenge);

                    long pow = generateProofOfWork(salted,2);

                    ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                    outputStream.write(request.getKeyToCheck().toByteArray());
                    outputStream.write(String.valueOf(request.getRid()).getBytes());
                    outputStream.write(request.getMyKey().toByteArray());
                    outputStream.write(String.valueOf(pow).getBytes());


                    BFTB.AuditRequest auditRequest= BFTB.AuditRequest.newBuilder()
                            .setKeyToCheck(request.getKeyToCheck())
                            .setRid(request.getRid())
                            .setMyKey(request.getMyKey())
                            .setPow(pow)
                            .setConcatenated(ByteString.copyFrom(salted))
                            .setSignature(ByteString.copyFrom(signatureHandler.sign(outputStream.toByteArray(),privateKey))).build();


                    BFTB.AuditTransactionResponse auditResponse = btftbBlockingStub.audit(auditRequest);
                    countDownLatch.countDown();
                    return auditResponse;
                }
                else{
                    System.out.println("invalid server challenge");
                    return null;
                }
            }else{
                System.out.println("invalid nonce");
                return  null;
            }





        }catch (Exception e){
            System.out.println("A server died...");
            return null;
        }
    }



    public BFTB.AccountResponse check_account(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.CheckAccountRequest request, CountDownLatch countDownLatch, PublicKey serverKey, PrivateKey privateKey) {

        try {
            SecureRandom secureRandom = new SecureRandom();
            long nonce = secureRandom.nextLong();
            PublicKey myKey= getPubKey(request.getMyPubkey());
            byte [] toCheckKey=request.getToCheckPubKey().toByteArray();
            byte[] signature= (nonce+ Arrays.toString(myKey.getEncoded())).getBytes();
            SignatureHandler signatureHandler= new SignatureHandler();
            BFTB.PowRequest powRequest= BFTB.PowRequest.newBuilder().setMyPublicKey(request.getMyPubkey()).setNonce(nonce).setSignature(ByteString.copyFrom(signatureHandler.sign(signature,privateKey))).build();
            BFTB.PowResponse powResponse=btftbBlockingStub.pOWRequest(powRequest);

            byte [] challenge=powResponse.getChallenge().toByteArray();
            byte [] signed = powResponse.getSignature().toByteArray();
            long repliedNonce=powResponse.getNonce();

            if (repliedNonce==nonce+1){
                if (signatureHandler.verifySignature(signed,serverKey,(Arrays.toString(challenge)+repliedNonce).getBytes())){

                    byte[] salted= Bytes.concat(toCheckKey,challenge);

                    long pow = generateProofOfWork(salted,2);

                    ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
                    outputStream.write(request.getToCheckPubKey().toByteArray());
                    outputStream.write(String.valueOf(request.getRid()).getBytes());
                    outputStream.write(request.getMyPubkey().toByteArray());
                    outputStream.write(String.valueOf(pow).getBytes());

                    BFTB.CheckAccountRequest checkAccountRequest= BFTB.CheckAccountRequest.newBuilder()
                            .setToCheckPubKey(request.getToCheckPubKey())
                            .setRid(request.getRid())
                            .setMyPubkey(request.getMyPubkey())
                            .setPow(pow)
                            .setConcatenated(ByteString.copyFrom(salted))
                            .setSignature(ByteString.copyFrom(signatureHandler.sign(outputStream.toByteArray(),privateKey))).build();


                    BFTB.AccountResponse accountResponseResponse = btftbBlockingStub.checkAccount(checkAccountRequest);
                    countDownLatch.countDown();
                    return accountResponseResponse;
                }
                else{
                    System.out.println("invalid server challenge");
                    return null;
                }
            }else{
                System.out.println("invalid nonce");
                return  null;
            }





        }catch (Exception e){
            System.out.println("A server died...");
            return null;
        }

    }


    public BFTB.RidResponse getRid(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.GetRidRequest request, CountDownLatch countDownLatch) {
        try {
            BFTB.RidResponse response = btftbBlockingStub.getRid(request);
            countDownLatch.countDown();
            return response;
        }catch (Exception e){
            System.out.println("A server died...");
            return null;
        }
    }

    public BFTB.CAWBResponse cawb(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.CAWBRequest request, CountDownLatch countDownLatch) {
        try {
            BFTB.CAWBResponse response = btftbBlockingStub.checkAccountWb(request);
            countDownLatch.countDown();
            return response;
        }catch (Exception e){
            System.out.println("A server died...");
            return null;
        }
    }

    public BFTB.ADWBResponse adwb(BTFTBGrpc.BTFTBBlockingStub btftbBlockingStub, BFTB.ADWBRequest request, CountDownLatch countDownLatch) {
        try {
            BFTB.ADWBResponse response = btftbBlockingStub.auditWb(request);
            countDownLatch.countDown();
            return response;
        }catch (Exception e){
            System.out.println("A server died...");
            return null;
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
