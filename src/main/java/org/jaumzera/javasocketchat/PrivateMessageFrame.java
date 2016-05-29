package org.jaumzera.javasocketchat;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PrivateMessageFrame extends Frame implements Constants {

    private static final long serialVersionUID = 1L;

    private TextArea input;
    private TextField output;

    private User from, to;

    private ActionListener outputListener;
    private WindowAdapter frameListener;
    
    private boolean active = true;
    
    private MainFrame mainFrame;

    public PrivateMessageFrame(MainFrame mainFrame, User to) {
        super("Private chat between " + mainFrame.getUser().getName() + " and " + to);
        this.mainFrame = mainFrame;
        this.from = mainFrame.getUser();
        this.to = to;
        initView();
        this.input.append(from.getName() + " private chat to " + to.getName());
    }

    public void initView() {
        outputListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TextField origem = (TextField) e.getSource();
                send(origem.getText());
                origem.setText("");
            }
        };

        frameListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                active = false;
                dispose();
            }
        };

        input = new TextArea("Private chat to " + to + "\n");
        input.setEditable(false);

        output = new TextField();
        output.addActionListener(outputListener);

        addWindowListener(frameListener);
        setLayout(new BorderLayout());

        int x = (int) (Math.random() * 500);
        int y = (int) (Math.random() * 500);

        setBounds(x, y, 640, 480);

        add("Center", input);
        add("South", output);

        setVisible(true);
        output.requestFocus();
    }

    public void append(PrivateMessage message) {
        input.append("\n" + message);
        input.setCaretPosition(input.getText().length());
    }

    public void send(String text) {
        PrivateMessage message = new PrivateMessage(from, to, text);
        mainFrame.send(message);
        append(message);
    }
    
    public boolean isActive() {
        return active;
    }
}
