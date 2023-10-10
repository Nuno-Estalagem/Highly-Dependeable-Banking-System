package domain;

import java.util.concurrent.CountDownLatch;

public class ADEBInstance {

    private CountDownLatch countDownLatch;

    private int echoCounter;

    private int readyCounter;

    private boolean hasSentReady;

    public ADEBInstance(){

        this.countDownLatch= new CountDownLatch(1);
        this.echoCounter=0;
        this.readyCounter=0;
    }


    public void addEcho(){
        this.echoCounter+=1;
    }

    public void addReady(){
        this.readyCounter+=1;
    }

    public  void await(){
        try {
            this.countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deliver(){
        countDownLatch.countDown();
    }

    public int getEchoCounter() {
        return echoCounter;
    }

    public int getReadyCounter() {
        return readyCounter;
    }

    public boolean HasSentReady() {
        return hasSentReady;
    }

    public void setHasSentReady() {
        this.hasSentReady = true;
    }

    public boolean hasDelivered(){
        return countDownLatch.getCount() == 0;
    }
}
