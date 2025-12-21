import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server("127.0.0.1", 8080);

        while (true) {
            try {
                server.listenForClients();

            } catch (IOException e) {
                System.out.println(e);
            }
        }


        //server.close();


    }
}