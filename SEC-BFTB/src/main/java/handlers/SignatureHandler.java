package handlers;

import com.google.protobuf.ByteString;

import java.security.*;

public class SignatureHandler {


    public byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature rsaForSign= Signature.getInstance("SHA256withRSA");
        rsaForSign.initSign(privateKey);
        rsaForSign.update(data);
        return rsaForSign.sign();
    }

    public boolean verifySignature(byte[] signature, PublicKey key, byte[] id) throws Exception {
        Signature rsaForVerify = Signature.getInstance("SHA256withRSA");
        rsaForVerify.initVerify(key);
        rsaForVerify.update(id);
        return rsaForVerify.verify(signature);

    }


}
