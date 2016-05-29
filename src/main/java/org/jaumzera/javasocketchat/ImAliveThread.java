package org.jaumzera.javasocketchat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;

public class ImAliveThread extends Thread implements Constants {

    private static final Logger LOG = Logger.getLogger(ImAliveThread.class.getName());

    private User user;
    @Getter
    @Setter
    private boolean running = true;

    public ImAliveThread(User user) {
        super("ImAliveThread_" + user);
        this.user = user;
    }

    public void run() {
        while (running) {
            try {
                Message message = new Message(user, IM_ALIVE);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(message);
                oos.flush();
                DatagramPacket datagram = new DatagramPacket(bos.toByteArray(), bos.size(), InetAddress.getByName(MULTICAST_IP), MULTICAST_PORTA);
                SocketUtils.socket().send(datagram);
                LOG.info(user + " is alive.");
                sleep(IM_ALIVE_PERIOD);
            } catch (InterruptedException | IOException e) {
                LOG.log(Level.SEVERE, null, e);
            }
        }
    }
}
