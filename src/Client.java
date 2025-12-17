import java.net.Socket;

public class Client {
    String username;
    Socket clientSocket;

    Client(Socket socket) {
        this.username = "guest";
        this.clientSocket = socket;
    }

    public void setClientSocket(Socket socket) {
        this.clientSocket = socket;
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }
}
