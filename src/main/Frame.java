package main;

import javax.swing.JFrame;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

public class Frame extends JFrame {

    private Panel panel;

    // constructors
    public Frame(Handler h) {

        panel = new Panel(h);
        panel.setFocusable(false);

        this.setTitle("Nine Men's Morris");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setFocusable(true);

        this.setContentPane(panel);
        
        this.pack();

        this.setBounds(0, 0, 500, 500);
        this.setVisible(true);

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                h.draw();
            }
        });
    }

    public Panel getPanel() {
        return panel;
    }
    
}
