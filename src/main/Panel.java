package main;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Panel extends JPanel implements MouseListener {

    private BufferedImage lastFrame;
    private Handler h;

    public Panel(Handler h) {

        this.h = h;
        this.addMouseListener(this);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(lastFrame, 0, 0, null);
    }

    public void setLastFrame(BufferedImage b) {
        this.lastFrame = b;
    }

    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        h.mousePressed(e);
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}

