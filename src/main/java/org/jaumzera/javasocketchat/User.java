package org.jaumzera.javasocketchat;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jaumzera
 */
@EqualsAndHashCode(of = "name")
@Getter
@Setter
public class User implements Comparable<User>, Serializable {
    
    private static final long SerialVersionUID = 1L;
    
    private String name;
    private long lastActivity;
    
    public User(String name) {  
        this.name = name;
        this.lastActivity = System.currentTimeMillis();
    }

    @Override
    public int compareTo(User o) {
        if(o != null) {
            return name.compareTo(o.name);
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
