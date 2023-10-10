package domain;

import java.security.PublicKey;

public class Parameters {
    private byte[] nonce;
    private PublicKey dest;
    private int amount;

    private byte[] signature;

    public Parameters(byte[]nonce, PublicKey dest, int amount, byte[] hash){
        this.dest=dest;
        this.nonce=nonce;
        this.amount=amount;
        this.signature=hash;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public PublicKey getDest() {
        return dest;
    }



    public int getAmount() {
        return amount;
    }

    public byte[] getSignature() {
        return signature;
    }
}

