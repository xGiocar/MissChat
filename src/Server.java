import java.io.IOException;
import java.net.*;

public class Server implements AutoCloseable, Runnable {
    public static final int MAX_CLIENTS = 3;
    private final int port;
    private final String address;
    private ServerSocket serverSocket;
    private Message[] messageHistory;
    private Client[] clients;
    private int connectedClients;

    public Server(String address, int port) {

        this.port = port;
        this.address = address;
        this.messageHistory = new Message[256];
        this.clients = new Client[MAX_CLIENTS];
        this.connectedClients = 0;

        try {
            InetAddress addr = InetAddress.getByName(this.address);
            this.serverSocket = new ServerSocket(port, MAX_CLIENTS, addr);

            System.out.format("Connected to %s on port %d\n", this.address, this.port);

        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public synchronized void waitForClients() {
        if (connectedClients >= MAX_CLIENTS) {
            System.err.format("The maximum number of clients connected in a session is %d\n", MAX_CLIENTS);
            return;
        }

        try {
            Socket clientSock = serverSocket.accept();
            clients[connectedClients] = new Client(clientSock); // saves the socket descriptor for the client that connects to the server

        } catch (IOException e) {
            System.err.println(e.toString());
        }

        System.out.printf("Client connected from address %s\n", clients[connectedClients].getClientSocket().getInetAddress());
        connectedClients++;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return this.address;
    }

    public int getConnectedClients() {
        return this.connectedClients;
    }

    @Override
    public void close() {
        for (Client client : clients) {
            if (client == null) {
                continue;
            }
            try {
                client.getClientSocket().close();
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }

        System.out.println("The clients' sockets were closed successfully");

        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }

        System.out.println("The server's sockets was closed successfully");
    }

    @Override
    public void run() {
        while (this.getConnectedClients() < Server.MAX_CLIENTS) {
            this.waitForClients();
        }
    }
}
