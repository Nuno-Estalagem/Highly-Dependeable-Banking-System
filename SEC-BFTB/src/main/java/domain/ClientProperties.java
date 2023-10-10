package domain;

import java.io.Serializable;
import java.security.Key;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class ClientProperties implements Serializable {
    private int balance;

    private ArrayList<Transaction> pending;

    private ArrayList<Transaction> audit;

    private String username;

    //private HashSet<Integer> nonces;

    private int wid;

    private int rid;

    private byte [] signature;

    private byte [] challenge;

    public ClientProperties(int balance, ArrayList<Transaction> pending , ArrayList<Transaction> audit, String username,  int wid, int rid, byte[] signature, byte[] challenge){
        this.balance=balance;
        this.pending=pending;
        this.username=username;
        this.wid=wid;
        this.signature=signature;
        this.audit=audit;
        this.challenge=challenge;

    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalance(){
        return this.balance;
    }

    public String getUsername() {
        return username;
    }

    public  ArrayList<Transaction>getPendingTransactions(){
        return this.pending;
    }


    public ArrayList<Transaction> getAudit() {
        return this.audit;
    }


    public void setPending(ArrayList<Transaction> pending) {
        this.pending = pending;
    }

    public void setAudit(ArrayList<Transaction> audit) {
        this.audit = audit;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public void setWidandBalanceSignature(byte[] balancePlusWidSign) {
        this.signature=balancePlusWidSign;
    }

    public byte[] getSignature() {
        return signature;
    }

    public boolean canReduceAccounting(int amount) {
        return amount<=balance;
    }

    public void reduceAccounting(int amount) {
        this.balance-=amount;
    }

    public void addAccounting(float amount) {
        this.balance+=amount;
    }


    public void setChallenge(byte[] challenge) {
        this.challenge = challenge;
    }

    public byte[] getChallenge() {
        return challenge;
    }
}
