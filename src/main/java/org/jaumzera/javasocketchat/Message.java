package org.jaumzera.javasocketchat;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Data;
import org.jaumzera.javasocketchat.User;

/**
 *
 * @author jaumzera
 */
@Data
public class Message implements Serializable {
    
    private static final long SerialVersionUID = 1L;
    
    protected static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    protected User from;

    protected Date sent;
    
    protected String content;
    
    public Message(User from, String content) {
        this.from = from;
        this.content = content;
        this.sent = new Date();
    }
    
    @Override
    public String toString() {
        return "[" + FORMAT.format(sent) + "] " + from + " said: " + content;
    }
}
