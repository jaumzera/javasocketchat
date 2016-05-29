package org.jaumzera.javasocketchat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jaumzera.javasocketchat.User;

/**
 *
 * @author jaumzera
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PrivateMessage extends Message {
    
    private static final long SerialVersionUID = 1L;
    
    protected User to;
    
    public PrivateMessage(User from, User to, String content) {
        super(from, content);
        this.to = to;
    }
    
    @Override
    public String toString() {
        return "[" + FORMAT.format(sent) + "] " + from + " privately says: " + content;
    }
}
