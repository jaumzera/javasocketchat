package org.jaumzera.javasocketchat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.jaumzera.javasocketchat.Constants.MULTICAST_IP;
import static org.jaumzera.javasocketchat.Constants.MULTICAST_PORTA;

/**
 *
 * @author jaumzera
 */
public class SocketUtils {

    private static final Logger LOG = Logger.getLogger(SocketUtils.class.getName());

    private static MulticastSocket multicastSocket;

    private static void createMultiCastSocket() {
        try {
            multicastSocket = new MulticastSocket(MULTICAST_PORTA);
            multicastSocket.joinGroup(InetAddress.getByName(MULTICAST_IP));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
    
    public static MulticastSocket socket() {
        if(multicastSocket == null || !multicastSocket.isConnected()) {
            createMultiCastSocket();
        }
        return multicastSocket;
    }
}
