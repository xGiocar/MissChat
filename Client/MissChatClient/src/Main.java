import java.net.ConnectException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ClientSocket clientSocket = new ClientSocket();
        try {
            clientSocket.connectToServer("127.0.0.1", 8080);
        } catch (ConnectException e) {
            System.err.println("The server is offline");
            return;
        }

        System.out.println("Succesfully connected to server");

        clientSocket.sendMessage("Hello world");

        //clientSocket.close();

    }
}