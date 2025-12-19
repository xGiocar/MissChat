import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;
import java.time.*;

public class ClientSocket implements AutoCloseable {
    private Socket serverSocket;
    private Socket clientSocket;
    private String username;


    public ClientSocket() {
        this.username = "guest";
        this.clientSocket = new Socket();
        this.serverSocket = null;
    }

    public ClientSocket(String username) {
        this.username = username;
        this.clientSocket = new Socket();

    }

    public void sendMessage(String message) {
        try {
            PrintWriter outStream = new PrintWriter(clientSocket.getOutputStream(), true);
            outStream.println(message);

        } catch (IOException e){
            System.err.println("The client isn't connected to the server");
            return;
        }



    }

    public void connectToServer(String address, int port) throws ConnectException{
        SocketAddress serverSock = new InetSocketAddress(address, port);
        try {
            clientSocket.connect(serverSock);
            sendMessage("__HEADER__");
            sendMessage(username);
        } catch (IOException e) {
            throw new ConnectException("Server is offline");
        }
    }

    @Override
    public void close() {
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            System.err.println(e);
        }

        System.out.println("The client socket closed successfully");
    }

    public Socket getSocket() {
        return clientSocket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername() {
        this.username = username;
    }
}
