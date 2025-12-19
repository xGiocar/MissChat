public class Main {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server("127.0.0.1", 8080);

        Thread serverListen = new Thread(server);
        serverListen.start();

        int x = 0;
        while(x < 100000000){
            server.listenForMessages();
            Thread.sleep(1000);
            x++;
        }


    }
}