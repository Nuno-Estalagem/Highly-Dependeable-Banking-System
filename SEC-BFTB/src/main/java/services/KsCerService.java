package services;

import java.io.File;

public class KsCerService {


    public static void createUser(String username, String password,int port) throws Exception {

        String SERVER_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + port;
        
        String[] ksArray = new String[12];
        ksArray[0] = "keytool";
        ksArray[1] = "-genkey";
        ksArray[2] = "-alias";
        ksArray[3] = username;
        ksArray[4] = "-keyalg";
        ksArray[5] = "RSA";
        ksArray[6] = "-keystore";
        ksArray[7] = username + ".jks";
        ksArray[8] = "-dname";
        ksArray[9] = "CN=, OU=, O=, L=, S=, C=";
        ksArray[10] = "-storepass";
        ksArray[11] = password;


        ProcessBuilder builder = new ProcessBuilder(ksArray);
        builder.directory(new File(SERVER_DIR));
        Process pr = builder.start();
        pr.waitFor();

        String[] cerArray = new String[10];
        cerArray[0] = "keytool";
        cerArray[1] = "-export";
        cerArray[2] = "-alias";
        cerArray[3] = username;
        cerArray[4] = "-storepass";
        cerArray[5] = password;
        cerArray[6] = "-file";
        cerArray[7] = username + ".cer";
        cerArray[8] = "-keystore";
        cerArray[9] = username + ".jks";


        builder.command(cerArray).start();
    }

}
