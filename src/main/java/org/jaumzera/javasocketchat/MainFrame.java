package org.jaumzera.javasocketchat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import lombok.Getter;
import lombok.Setter;
import static org.jaumzera.javasocketchat.Constants.DISCONNECT;
import static org.jaumzera.javasocketchat.Constants.MULTICAST_IP;
import static org.jaumzera.javasocketchat.Constants.MULTICAST_PORTA;

public class MainFrame extends Frame {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(MainFrame.class.getName());

    private TextArea messagesTextArea;
    private TextField outputTextField;
    private JList onlineUsersJList;
    private ScrollPane onlineUserScrollPane;

    private WindowAdapter windowCloseListener;
    private ActionListener outputListener;
    private MouseAdapter onlineUsersListener;

    @Getter
    @Setter
    private Map<String, User> onlineUsers;
    private Map<String, PrivateMessageFrame> privateMessages;
    @Getter
    @Setter
    private User user;
    private ControllerThread controllerThread;
    private ImAliveThread imAliveThread;

    public MainFrame(String username) {
        super("Java Socket Chat [" + username + "]");

        user = new User(username);
        onlineUsers = new TreeMap<>();
        privateMessages = new TreeMap<>();

        initView();
        
        imAliveThread = new ImAliveThread(user);
        imAliveThread.start();

        controllerThread = new ControllerThread(this);
        controllerThread.start();
    }

    public void initView() {
        windowCloseListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        };

        outputListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TextField origem = (TextField) e.getSource();
                send(new Message(user, origem.getText()));
                origem.setText("");
            }
        };

        onlineUsersListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    JList jlAux = (JList) e.getSource();
                    User to = (User) jlAux.getSelectedValue();
                   
                    if(to == null) {
                        JOptionPane.showMessageDialog(null, "Usuário não disponível");
                    } else if(user.equals(to)) {
                        LOG.severe(">>> You can't chat to yourself");
                    } else {
                        openPrivateChat(new PrivateMessage(user, to, user + " has open a private chat"));
                    }
                }
            }
        };
        
        onlineUsersJList = new JList();
        onlineUsersJList.addMouseListener(onlineUsersListener);
        onlineUsersJList.setSize(new Dimension(60, 280));
        onlineUsersJList.setMinimumSize(new Dimension(60, 250));
        updateUserList();

        onlineUserScrollPane = new ScrollPane();
        onlineUserScrollPane.add(onlineUsersJList);

        messagesTextArea = new TextArea("Hello " + user);
        messagesTextArea.setEditable(false);
        messagesTextArea.setSize(300, 280);

        outputTextField = new TextField();
        outputTextField.addActionListener(outputListener);

        addWindowListener(windowCloseListener);

        setSize(640, 480);
        setLayout(new BorderLayout());

        add("North", new JLabel("Java Socket Chat"));
        add("Center", messagesTextArea);
        add("South", outputTextField);
        add("East", onlineUserScrollPane);

        setVisible(true);
        requestFocus();
    }

    public void send(Message message) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(message);
            oos.flush();
            DatagramPacket datagram = new DatagramPacket(bos.toByteArray(), bos.size(), InetAddress.getByName(MULTICAST_IP), MULTICAST_PORTA);
            SocketUtils.socket().send(datagram);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    private void disconnect() {
        send(new Message(user, DISCONNECT));
        controllerThread.setRunning(false);
        imAliveThread.setRunning(false);
        LOG.info(user + " has left the chat.");
    }

    public void openPrivateChat(PrivateMessage message) {
        if (!privateMessages.containsKey(message.getTo().getName()) || !privateMessages.get(message.getTo().getName()).isActive()) {
            privateMessages.put(message.getTo().getName(), new PrivateMessageFrame(this, message.getTo()));
        }
 
        privateMessages.get(message.getTo().getName()).append(message);
    }
    
    public void receive(PrivateMessage message) {
        if (!privateMessages.containsKey(message.getFrom().getName()) || !privateMessages.get(message.getFrom().getName()).isActive()) {
            privateMessages.put(message.getFrom().getName(), new PrivateMessageFrame(this, message.getFrom()));
        }
 
        privateMessages.get(message.getFrom().getName()).append(message);
    }
    
    public void addMessage(Message message) {
        messagesTextArea.append("\n" + message);
        messagesTextArea.setCaretPosition(messagesTextArea.getText().length());
    }

    public void exit() {
        disconnect();
        dispose();
    }

    public void updateUserList() {
        DefaultListModel<User> listModel = new DefaultListModel<>();
        synchronized(onlineUsers) {
            for (User user : onlineUsers.values()) {
                if(!user.equals(this.user)) {
                    listModel.addElement(user);
                }
            }
        }
        onlineUsersJList.setModel(listModel);
    }

    public static void main(String args[]) {
        String username = JOptionPane.showInputDialog("Enter a username (max length: 20 chars): ");
        if (username != null && !username.equals("")) {
            if (username.length() > 20) {
                username = username.substring(0, 20);
            }
            new MainFrame(username);
        } else {
            JOptionPane.showMessageDialog(null, "You must enter a username");
        }
    }
}
