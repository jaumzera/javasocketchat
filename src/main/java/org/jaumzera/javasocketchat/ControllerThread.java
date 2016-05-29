package org.jaumzera.javasocketchat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.jaumzera.javasocketchat.Constants.*;
import lombok.Getter;
import lombok.Setter;

public class ControllerThread extends Thread {

    private static final Logger LOG = Logger.getLogger(ControllerThread.class.getName());

    private MainFrame mainFrame;
    @Getter
    @Setter
    private boolean running = true;

    public ControllerThread(MainFrame main) {
        super("ReceptoraThread_" + main.getUser().getName());
        this.mainFrame = main;
    }

    public void run() {
        while (running) {
            try {
                process(receive());
                removeIdleUsers();
            } catch (ClassNotFoundException | IOException e) {
                LOG.log(Level.SEVERE, null, e);
            }
        }
    }

    private Message receive() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[MESSAGE_LENGTH];
        DatagramPacket datagram = new DatagramPacket(buffer, buffer.length);
        SocketUtils.socket().receive(datagram);
        ByteArrayInputStream bos = new ByteArrayInputStream(buffer);
        ObjectInputStream ois = new ObjectInputStream(bos);
        Message message = (Message) ois.readObject();
        ois.close();
        return message;
    }

    private void process(Message message) {
        if (message.getContent().equals(IM_ALIVE)) {
            updateUserState(message.getFrom());
        } else if (message.getContent().equals(DISCONNECT)) {
            removeUser(message.getFrom().getName());
            exit();
        } else if (message instanceof PrivateMessage) {
            PrivateMessage pm = (PrivateMessage) message;
            if(pm.getTo().equals(mainFrame.getUser())) {
                mainFrame.receive(pm);
            }
        } else {
            mainFrame.addMessage(message);
        }
        mainFrame.updateUserList();
    }

    private void updateUserState(User user) {
        if (mainFrame.getOnlineUsers().containsKey(user.getName())) {
            mainFrame.getOnlineUsers().get(user.getName()).setLastActivity(System.currentTimeMillis());
        } else {
            mainFrame.getOnlineUsers().put(user.getName(), user);
            user.setLastActivity(System.currentTimeMillis());
        }
    }

    private void removeIdleUsers() {
        for (Iterator<User> it = mainFrame.getOnlineUsers().values().iterator(); it.hasNext();) {
            User user = it.next();
            if (System.currentTimeMillis() - user.getLastActivity() > MAX_IDLE) {
                it.remove();
                mainFrame.updateUserList();
            }
        }
    }

    private void removeUser(String username) {
        mainFrame.getOnlineUsers().remove(username);
        mainFrame.updateUserList();
    }

    private void exit() {
        mainFrame.updateUserList();
        running = false;
        SocketUtils.socket().close();
    }
}
