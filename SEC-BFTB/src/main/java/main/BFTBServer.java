package main;

import com.SEC.grpc.BTFTBGrpc;
import domain.Client;
import domain.ClientProperties;
import handlers.ADEBHandler;
import handlers.FileHandler;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import managers.ADEBInstanceManager;
import managers.ADEBManager;
import services.ADEBservice;
import services.KsCerService;
import services.bftbService;

import java.io.File;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class BFTBServer {


    private static Client serverClient;

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        int numServers = Integer.parseInt(args[1]);

        String PROJECT_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + port;
        File CLIENTFILE = new File(PROJECT_DIR + File.separator + "ClientData.cif");
        File dir = new File(PROJECT_DIR);

        if (!dir.exists()) {
            new File(PROJECT_DIR).mkdir();
            Hashtable<Key, ClientProperties> clients = new Hashtable<>();
            CLIENTFILE.createNewFile();
            FileHandler.saveState(CLIENTFILE, clients);
            System.out.println("Server files created successfully");
            KsCerService.createUser("server_" + port, "server_" + port, port);
        }


        serverClient = Client.loadClient("src/main/resources/", "src/main/resources/" + port + "/" + "server_" + port + ".jks", "server_" + port, "server_" + port);






        //Starting the server


        List<String> ports= getPorts(numServers);



        ADEBInstanceManager adebInstanceManager= new ADEBInstanceManager();
        ADEBHandler adebHandler = new ADEBHandler();
        ADEBManager adebManager = new ADEBManager(adebHandler,ports, (numServers+Integer.parseInt(args[2]))/2+1);

        Server startServer = ServerBuilder.forPort(port).addService(new bftbService(port, adebManager,serverClient,PROJECT_DIR,CLIENTFILE,adebInstanceManager)).addService(new ADEBservice((numServers+Integer.parseInt(args[2]))/2+1,serverClient,adebManager,adebInstanceManager,port)).build();
        startServer.start();
        System.out.println("Server is listening at port " + startServer.getPort());



        startServer.awaitTermination();


    }

    private static List<String> getPorts(int numServers) {
        List<String>ports = new ArrayList<>();
        int firstPort=8080;
        while (numServers>0){
            ports.add(String.valueOf(firstPort));
            firstPort++;
            numServers--;
        }
        return ports;
    }

}



