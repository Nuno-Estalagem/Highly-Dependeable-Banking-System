package managers;


import domain.ADEBInstance;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class ADEBInstanceManager {

    private Hashtable<String, ADEBInstance> inputInstances;

    public ADEBInstanceManager(){
        this.inputInstances=new Hashtable<>();
    }


    public synchronized ADEBInstance getInstance(String hash) {
        if(inputInstances.containsKey(hash)){
            return inputInstances.get(hash);
        }
        ADEBInstance instance= new ADEBInstance();
        inputInstances.put(hash,instance);
        return instance;
    }

    public synchronized void deliver(String hashofParams) {
        if(inputInstances.containsKey(hashofParams)) {
            ADEBInstance instance = inputInstances.get(hashofParams);
            instance.deliver();
            inputInstances.remove(hashofParams);

        }
    }

    public int test(){
        for(String b: inputInstances.keySet())
            System.out.println(b+" key");
        return inputInstances.size();

    }
}
