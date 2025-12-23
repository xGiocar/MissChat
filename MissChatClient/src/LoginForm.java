import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ConnectException;

public class LoginForm extends JFrame {

    private JPanel panel = new JPanel();
    private FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
    private JTextField usernameTf = new JTextField(20);
    private JTextField ipTf = new JTextField(20);
    private JTextField portTf = new JTextField(20);

    private JButton submitBtn = new JButton("Connect");
    private Label welcomeLbl = new Label("Connect to a server");
    private ImageIcon logo = new ImageIcon("resources/miss_chat_logo.jpg");
    private JLabel imageLbl = new JLabel(logo);

    private JLabel errorMsg = new JLabel("");

    ClientSocket clientSocket;
    String username;
    String ipAddress;
    int port;

    private Image resizeImage(Image icon) {
        return icon.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
    }

    public LoginForm() {

        this.setTitle("MISS_CHAT Client");
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 650);
        this.setResizable(false);
        panel.setLayout(layout);
        panel.setBackground(new Color(0,0,0));

        logo.setImage(resizeImage(logo.getImage()));

        Font font = new Font("SansSerif", Font.BOLD, 20);
        Font tfFont = new Font("SansSerif", Font.BOLD, 15);

        welcomeLbl.setFont(font);
        welcomeLbl.setForeground(new Color(216, 60, 255));
        //153, 255, 60
        Color tfText = Color.WHITE;
        Color tfColor = new Color(0,0,0);

        TitledBorder tfBorderUser = BorderFactory.createTitledBorder("Username");
        tfBorderUser.setTitleColor(Color.WHITE);

        TitledBorder tfBorderIp = BorderFactory.createTitledBorder("Server IP");
        tfBorderIp.setTitleColor(Color.WHITE);

        TitledBorder tfBorderPort = BorderFactory.createTitledBorder("Server Port");
        tfBorderPort.setTitleColor(Color.WHITE);


        usernameTf.setBorder(tfBorderUser);
        usernameTf.setBackground(tfColor);
        usernameTf.setFont(tfFont);
        usernameTf.setForeground(tfText);

        ipTf.setBorder(tfBorderIp);
        ipTf.setBackground(tfColor);
        ipTf.setFont(tfFont);
        ipTf.setForeground(tfText);

        portTf.setBorder(tfBorderPort);
        portTf.setBackground(tfColor);
        portTf.setFont(tfFont);
        portTf.setForeground(tfText);

        submitBtn.setPreferredSize(new Dimension(100, 50));
        submitBtn.setFont(font);
        submitBtn.setBorder(BorderFactory.createCompoundBorder());
        submitBtn.setBackground(new Color(216, 60, 255));
        submitBtn.setForeground(Color.BLACK);

        errorMsg.setFont(font);
        errorMsg.setForeground(Color.RED);

        panel.add(imageLbl);
        panel.add(welcomeLbl);
        panel.add(usernameTf);
        panel.add(ipTf);
        panel.add(portTf);
        panel.add(submitBtn);
        panel.add(errorMsg);
        errorMsg.setVisible(false);

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientSocket = new ClientSocket();
                username = getTfUsername();
                ipAddress = getTfIp();
                port = getTfPort();

                if (username.isEmpty()) {
                    writeError("Please complete all the fields");
                    return;
                }

                if (ipAddress.isEmpty()) {
                    writeError("Please complete all the fields");
                    return;
                }

                if (port <= 0) {
                    writeError(" Invalid input data   ");
                    return;
                }
                else {
                    try {
                        clientSocket.connectToServer(ipAddress, port);
                        removeError();
                        System.out.println("Succesfully connected to server");
                    } catch (ConnectException ex) {
                        writeError("Can't connect to server");
                        System.err.println("The server is offline");
                        return;
                    }
                }

            }
        });


        this.setContentPane(panel);
        this.setVisible(true);
    }

    public void writeError(String err) {
        errorMsg.setText(err);
        errorMsg.setVisible(true);

    }

    public void removeError() {
        errorMsg.setVisible(false);
    }

    public String getTfUsername() {
        return usernameTf.getText();
    }

    public String getTfIp() {
        return ipTf.getText();
    }

    public int getTfPort() {
        try {
            return Integer.parseInt(portTf.getText());
        } catch (NumberFormatException e) {
            writeError(" Invalid input data   ");
            return 0;
        }

    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public ClientSocket getClientSocket() {
        return clientSocket;
    }
}
