import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private String username;
    private Socket clientSocket;
    private BufferedReader inputStream;

    Client(Socket socket) {
        this.username = "guest";
        this.clientSocket = socket;
        try {
            this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.err.println("The client socket is invalid");
            return;
        }

    }

    public void setClientSocket(Socket socket) {
        this.clientSocket = socket;
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public BufferedReader getInputStream() {
        return inputStream;
    }

    public void setUsername(String name) {
        username = name;
    }

    public String getUsername() {
        return username;
    }
}
