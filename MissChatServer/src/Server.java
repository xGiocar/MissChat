import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server implements AutoCloseable, Runnable {
    public static final int MAX_CLIENTS = 3;
    private final int port;
    private final String address;
    private ServerSocket serverSocket;
    private List<Message> messageHistory;
    private List<Client> clients;
    private int connectedClients = 0;

    public Server(String address, int port) {

        this.port = port;
        this.address = address;
        this.messageHistory = new ArrayList<>();
        this.connectedClients = 0;
        this.clients = new ArrayList<>();

        try {
            InetAddress addr = InetAddress.getByName(this.address);
            this.serverSocket = new ServerSocket(port, MAX_CLIENTS, addr);

            System.out.format("Connected to %s on port %d\n", this.address, this.port);

        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    private void manageMessage(Message message) throws IOException{
        System.out.println(message.getTimeStamp() + " " + message.getSender().getUsername() + ": " + message.getBody());

        for (Client client : clients) {
            if (client != message.getSender()) {
                client.getOutputStream().println(message.getSender().getUsername() + ": " + message.getBody());
            }
        }
    }

    public void listenForClients() throws IOException {
        Socket clientSock = serverSocket.accept();
        if (clientSock.isConnected()) {
            Thread clientThread = new Thread(new Runnable() {
                @Override
                public void run(){
                    Client client = new Client(clientSock);
                    clients.add(client);
                    System.out.printf("Client connected from address %s on port %d\n", clientSock.getInetAddress(), clientSock.getPort());
                    Message message;
                    while(true) {
                        try {
                            message = client.readMessage();
                            if (message != null) {
                                manageMessage(message);

                            }
                        } catch (IOException e) {
                            System.out.println(client.getUsername() + " disconnected");
                            clients.remove(client);
                            break;
                        }

                    }

                }
            });

            clientThread.start();
        }
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
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println(e.toString());
        }

        System.out.println("The server's sockets was closed successfully");
    }

    public void saveMessage() {

    }

    @Override
    public void run() {
        while (this.getConnectedClients() <= Server.MAX_CLIENTS) {
            //this.listenForClients();
        }
    }
}
