package domain;

import java.io.Serializable;
import java.security.PublicKey;

public class Transaction implements Serializable {

    //private final String ID= UUID.randomUUID().toString();
    private int amount;
    private PublicKey sender;
    private PublicKey receiver;
    private String senderUsername;
    private String receiverUsername;

    private byte [] signature;

    private int wid;


    public Transaction(PublicKey sender, PublicKey receiver, int amount, String senderUsername, String receiverUsername, byte[] signature, int wid){
        this.amount=amount;
        this.sender=sender;
        this.receiver=receiver;
        this.senderUsername=senderUsername;
        this.receiverUsername=receiverUsername;
        this.signature=signature;
        this.wid=wid;


    }




    public PublicKey getReceiver() {
        return receiver;
    }

    public PublicKey getSender() {
        return sender;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public int getAmount() {
        return amount;
    }

    /*
    public String getID(){
        return ID;
    }
    */

    public byte[] getSignature() {
        return signature;
    }

    public int getWid() {
        return wid;
    }


}
