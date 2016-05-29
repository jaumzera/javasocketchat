package org.jaumzera.javasocketchat.old;

import org.jaumzera.javasocketchat.Message;
import org.jaumzera.javasocketchat.MainFrame;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jaumzera
 */
public class MainFrameTest {
    
    public MainFrameTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void shouldLogin() {
        MainFrame mf1 = new MainFrame("Jaumzera1");
        mf1.send(new Message(mf1.getUser(), "first message"));
        
    }

    
}
