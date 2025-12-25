import java.io.*;
import java.net.*;
import java.time.*;

public class ClientSocket implements AutoCloseable {
    private Socket clientSocket;
    private String username;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private String color; //an integer that represents a color from the list


    public ClientSocket() {
        this.username = "guest";
        this.color = "0";
        this.clientSocket = new Socket();
    }

    public ClientSocket(String username, String color) {
        this.username = username;
        this.color = color;
        this.clientSocket = new Socket();

    }

    public String listenToServer() throws IOException{
        String text = inputStream.readLine();
        System.out.println(text);
        return text;
    }

    public void sendMessage(String message) {
        outputStream.println(message);
    }

    public void connectToServer(String address, int port) throws ConnectException {
        SocketAddress serverSock = new InetSocketAddress(address, port);
        try {
            clientSocket.connect(serverSock);
            this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
            sendMessage("__HEADER__");
            sendMessage(username);
            sendMessage(color);
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
