import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

public class MessageForm extends JFrame {
    private BorderLayout overallLayout;
    private FlowLayout inputLayout;
    private GridLayout messagePanelLayout;

    private JPanel messagePanel;
    private JPanel inputPanel;

    private JTextField messageInput;
    private JButton send;

    private ClientSocket clientSocket;
    private String username;
    private String ipAddress;
    private int port;
    private int color;
    private JScrollPane scrollPane;

    private String msg;

    private final Font font = new Font("SansSerif", Font.BOLD, 20);
    private ImageIcon logo = new ImageIcon("resources/miss_chat_logo.jpg");

    private Color[] styles = {
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.CYAN,
            Color.PINK,
            Color.MAGENTA,
            new Color(255, 0, 102),
            new Color(153, 255, 153),
            new Color(255, 51, 0)

    };

    public MessageForm(ClientSocket clientSock, String username, String ipAddress, int port) {
        this.clientSocket = clientSock;
        this.username = username;
        this.ipAddress = ipAddress;
        this.port = port;

        Random rand = new Random();
        this.color = rand.nextInt(11);

        clientSock.sendMessage("__HEADER__");
        clientSock.sendMessage(username);
        clientSock.sendMessage(Integer.toString(this.color));

        this.overallLayout = new BorderLayout();
        this.messagePanel = new JPanel();
        this.inputPanel = new JPanel();
        this.inputLayout = new FlowLayout();
        this.messageInput = new JTextField(23);
        this.messagePanelLayout = new GridLayout(0,1);
        this.send = new JButton("Send");
        this.setIconImage(logo.getImage());
        this.setResizable(false);

        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setBackground(new Color(122, 0, 153));
        scrollPane.setBackground(new Color(216, 60, 255));
        //messagePanel.add(scrollPane, BorderLayout.CENTER);

        this.setSize(500, 700);
        //this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(overallLayout);
        this.setTitle("MISS_CHAT Client");

        messagePanel.setBackground(Color.BLACK);
        messagePanel.setLayout(messagePanelLayout);


        inputPanel.setBackground(new Color(216, 60, 255));
        //inputPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() / 12));
        inputPanel.setLayout(inputLayout);
        inputPanel.add(messageInput);
        inputPanel.add(send);

        messageInput.setBackground(new Color(122, 0, 153));
        messageInput.setSize(this.getSize());
        messageInput.setBorder(BorderFactory.createEmptyBorder());
        messageInput.setFont(font);
        messageInput.setForeground(Color.WHITE);
        messageInput.setCaretColor(Color.WHITE);


        this.add(scrollPane, BorderLayout.CENTER);
        this.add(inputPanel, BorderLayout.SOUTH);

        send.setFont(font);
        send.setBorder(BorderFactory.createCompoundBorder());
        send.setBackground(new Color(122, 0, 153));
        send.setForeground(Color.WHITE);
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writeMyMessage(clientSock);
            }
        });

        messageInput.addActionListener(e -> writeMyMessage(clientSock));

        this.setVisible(true);

        Thread serverListener = createThread();
        serverListener.start();
    }

    private void writeMyMessage(ClientSocket clientSock) {
        String msg = messageInput.getText();
        if (!msg.isEmpty()) {
            Random rand = new Random();
            clientSock.sendMessage(msg);
            addMessage(msg, 1, styles[color], "");
            messageInput.setText("");
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());

        }
    }

    public Thread createThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = clientSocket.listenToServer();
                        if (message == null) {
                            continue;
                        }

                        if (message.contains("connected") || message.contains("disconnected")) {
                            addMessage(message, 2, Color.WHITE, "");
                            continue;
                        }
                        String info[] = message.split("@");
                        addMessage(info[2], 0, styles[Integer.parseInt(info[0])], info[1]);

                    } catch (IOException e) {
                        System.err.println("Can't connect to server");
                        break;
                    }

                }
            }
        });
    }


    public void addMessage(String message, int sender, Color color, String user) {
        int orientation = SwingConstants.LEFT;

        if (sender == 1) orientation = SwingConstants.RIGHT;
        if (sender == 2) orientation = SwingConstants.CENTER;

        if (!user.isEmpty()) {
            user += ": " + message;
            message = user;
        }


        JLabel messageBody = new JLabel(message, orientation);
        //messageBody.setSize(this.getWidth(), this.getHeight() / 10);
        messageBody.setOpaque(true);
        //messageBody.setBackground(style.getBackgroundColor());
        messageBody.setMaximumSize(new Dimension(300, 50));
        messageBody.setBackground(Color.BLACK);
        messageBody.setForeground(color);
        messageBody.setVisible(true);
        messageBody.setFont(font);
        messageBody.setText(message);
        //messageBody.setAlignmentX(Component.RIGHT_ALIGNMENT);
        messageBody.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));


        messagePanel.add(messageBody);
        this.revalidate();
        this.repaint();

        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
}
