import java.io.IOException;
import java.net.ConnectException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        ClientSocket clientSocket = new ClientSocket();
        try {
            clientSocket.connectToServer("127.0.0.1", 8080);
        } catch (ConnectException e) {
            System.err.println("The server is offline");
            return;
        }

        System.out.println("Succesfully connected to server");

        String string = "";

        Thread serverListener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        clientSocket.listenToServer();
                    } catch (IOException e) {
                        System.err.println("Can't connect to server");
                        break;
                    }

                }
            }
        });

        serverListener.start();

        while (string.toLowerCase() != "x") {
            Scanner scanner = new Scanner(System.in);
            string = scanner.nextLine();
            clientSocket.sendMessage(string);
        }


        //clientSocket.close();

    }
}