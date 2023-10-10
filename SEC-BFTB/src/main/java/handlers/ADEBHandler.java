package handlers;

import com.SEC.grpc.ADEBGrpc;
import com.SEC.grpc.ADEBOuterClass;
import com.google.protobuf.ByteString;

import java.security.PublicKey;


public class ADEBHandler {



    public ADEBHandler(){




    }


    public String echo(ADEBGrpc.ADEBBlockingStub adebBlockingStub, ADEBOuterClass.EchoRequest echoRequest, PublicKey publicKey) {
        try {

            byte[] nonce = echoRequest.getNonce().toByteArray();
            ADEBOuterClass.EchoResponse response = adebBlockingStub.echo(echoRequest);
            byte[] responseSigned = response.getSigned().toByteArray();
            SignatureHandler signatureHandler = new SignatureHandler();
            if (signatureHandler.verifySignature(responseSigned, publicKey, nonce)) {
                return response.getAck();
            }
            System.out.println("INVALID ECHO SIGN");
            return "invalid Sign from sent echo";
        }catch (Exception e){
            return "";
        }

    }

    public String ready(ADEBGrpc.ADEBBlockingStub adebBlockingStub, ADEBOuterClass.ReadyRequest readyRequest, PublicKey publicKey) {
        try {
            byte[] nonce = readyRequest.getNonce().toByteArray();
            ADEBOuterClass.ReadyResponse response = adebBlockingStub.ready(readyRequest);
            byte[] responseSigned = response.getSigned().toByteArray();
            SignatureHandler signatureHandler = new SignatureHandler();
            if (signatureHandler.verifySignature(responseSigned, publicKey, nonce)) {
                return response.getAck();
            }
            System.out.println("INVALID READY SIGN");
            return "invalid Sign from sent echo";
        } catch (Exception e) {
            return "";
        }
    }
}
