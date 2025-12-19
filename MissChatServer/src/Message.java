import java.sql.Time;

public class Message {
    private String body;
    private String timeStamp;
    private Client sender;


    Message(String body, Client sender, String time) {
        this.body = body;
        this.sender = sender;
        this.timeStamp = time;
    }

    Message(String body, Client sender) {
        this(body, sender, null);
    }

    public String getBody() {
        return body;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Client getSender() {
        return sender;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
