package main;


import Handlers.CommandHandler;
import com.google.common.primitives.Bytes;

import java.io.File;
import java.util.*;

public class BFTBClient {
    private final static String KEYS_DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "keystores" + File.separator;

    public static void main(String[] args) {
        final String ERROR = "Error: Invalid Parameters";
        boolean newClient = false;
        boolean finished = false;
        CommandHandler commandHandler = new CommandHandler(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        try {

            Scanner sc = new Scanner(System.in);

            List<String> parameters = getParameters(sc);

            String username = parameters.get(0);
            String password = parameters.get(1);

            File store = new File(KEYS_DIR + username + ".jks");

            if (!store.exists()) {

                System.out.println("There is no account associated to that username!\n Insert <yes> or <y> to open a new account.");

                String resposta = sc.next().toLowerCase();

                if (resposta.contentEquals("y") || resposta.contentEquals("yes")) {
                    String create=commandHandler.createAccount();
                    System.out.println(create);


                } else {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }

            } else {
                try {
                    commandHandler.login(username, password);
                } catch (Exception e) {
                    System.out.println("Invalid password!");
                    System.exit(0);
                }
            }


            System.out.println("------Welcome to the BFT Banking System!------");
            commandHandler.loadRid();
            showMenu();
            Scanner scanner = new Scanner(System.in);
            while (!finished) {
                System.out.println("Insert a command!");
                String command = scanner.nextLine();
                if (command.contentEquals("quit") || command.contentEquals("q")) {
                    finished = true;
                } else {
                    String serverOutput = commandHandler.handleCommand(command);
                    if (serverOutput.contentEquals(ERROR)) {
                        System.out.println(serverOutput);
                        showMenu();
                    }
                    else{
                        System.out.println(serverOutput);
                    }
                }
            }
            System.out.println("You have logged out, Bye!");
            commandHandler.shutdown();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void showMenu() {
        System.out.println("_______________________________________________");
        System.out.println("|      Options       |   Parameters           |");
        System.out.println("______________________________________________|");
        System.out.println("|      send/sa       |  <receiverID> <amount> |");
        System.out.println("|      check/ca      |  <accountID>           |");
        System.out.println("|      receive/ra    |  <TransactionId>       |");
        System.out.println("|      audit/ad      |  <accountID>           |");
        System.out.println("|      quit/q        |                        |");
        System.out.println("|____________________|________________________|");
    }


    private static List<String> getParameters(Scanner sc) {
        List<String> parameters = new ArrayList<>();


        System.out.println("-Insert your username");
        parameters.add(sc.next());
        String username = parameters.get(0);

        System.out.println("-Insert your Keystore's password");
        parameters.add(sc.next());
        String password = parameters.get(1);
        return Arrays.asList(username, password);
    }

}


