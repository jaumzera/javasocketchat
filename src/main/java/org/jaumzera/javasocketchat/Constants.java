package org.jaumzera.javasocketchat;

public interface Constants {

    public static final String MULTICAST_IP = "224.0.0.1";

    public static final int MULTICAST_PORTA = 3333;

    public static final int MESSAGE_LENGTH = 1024 * 100;

    public static final long IM_ALIVE_PERIOD = 1000L;
    
    public static final long MAX_IDLE = 30 * 1000L;

    public static final String IM_ALIVE = "[IM_ALIVE]";
    
    public static final String DISCONNECT = "[DISCONNECTING]";
    
}
