package Handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class CreateNewUserHandler {
    private final static String KEYS_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "keystores" + File.separator;


    public static void createUser() throws Exception {

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        Scanner sc=new Scanner(System.in);
        System.out.println("-Insert your username");

        String username = sc.next();

        while (new File(KEYS_DIR + username + ".jks").exists()) {
            System.out.println("An account with this username already exists! Please choose another username!");
            username = sc.next();
            System.out.println("-Insert your username");
        }

            System.out.println("-Insert your password");
            String password = sc.next();
            while (password.length() < 6) {
                System.out.println("The password needs to have at least 6 characters");
                System.out.println("-Insert your password");
                password = sc.next();

            }



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
            builder.directory(new File(KEYS_DIR));
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