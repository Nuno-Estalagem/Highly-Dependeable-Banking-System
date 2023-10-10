package managers;

import com.SEC.grpc.ADEBGrpc;
import com.SEC.grpc.ADEBOuterClass;
import com.SEC.grpc.BTFTBGrpc;
import domain.Client;
import handlers.ADEBHandler;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class ADEBManager {

    private ADEBHandler adebHandler;

    private List<String> ports;

    private int byzantineQuorum;

    private List<ManagedChannel> channels = new ArrayList<>();
    private HashMap<String, ADEBGrpc.ADEBBlockingStub> stubs = new HashMap<>();

    public ADEBManager(ADEBHandler adebHandler, List<String> ports, int byzantineQuorum) {

        this.adebHandler = adebHandler;
        this.ports = ports;
        this.byzantineQuorum = byzantineQuorum;

        for (String port : ports) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", Integer.parseInt(port)).usePlaintext().build();
            channels.add(channel);
            stubs.put(port, ADEBGrpc.newBlockingStub(channel));
        }

    }


    public void echo(ADEBOuterClass.EchoRequest echoRequest, Client cliente) {
        try {
            List<String> outputs = new ArrayList<>();

            Set<String> keys = stubs.keySet();
            for (String key : keys) {
                Thread thread = new Thread(() -> {
                    String response = adebHandler.echo(stubs.get(key), echoRequest,(PublicKey) cliente.getServerPubKeys().get(key));
                    outputs.add(response);
                });
                thread.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ready( ADEBOuterClass.ReadyRequest readyRequest, Client cliente){
        try {
            List<String> outputs = new ArrayList<>();

            Set<String> keys = stubs.keySet();
            for (String key : keys) {
                Thread thread = new Thread(() -> {
                    String response = adebHandler.ready(stubs.get(key), readyRequest,(PublicKey) cliente.getServerPubKeys().get(key));
                    outputs.add(response);
                });
                thread.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
