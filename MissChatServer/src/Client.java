import java.io.*;
import java.net.Socket;
import java.time.LocalTime;

public class Client {
    private String username;
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private int color;

    Client() {
        this.username = "";
        this.clientSocket = null;
        this.inputStream = null;
        this.outputStream = null;
        this.color = 0;
    }

    Client(Socket socket) {
        this(socket, "guest");

    }

    Client(Socket socket, String username) {
        this.username = username;
        this.clientSocket = socket;
        try {
            this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("The client socket is invalid");
        }

    }

    public Message readMessage() throws IOException{
        String text = inputStream.readLine();
        if (text == null) return null;

        if (text.equals("__HEADER__")) {
            text = inputStream.readLine();
            username = text;
            text = inputStream.readLine();
            color = Integer.parseInt(text);
            return new Message(username, null, null, 0);
        }
        String timestamp = LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
        Message message = new Message(text, this, timestamp, color);
        /*TODO: Add the message to message history*/

        return message;
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

    public PrintWriter getOutputStream() {
        return outputStream;
    }

    public int getColor() {
        return color;
    }
}
