import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class GameScreen extends JPanel implements KeyListener, MouseMotionListener
{
    Ship ship; // used in multiple methods, so it is declared here

    public GameScreen()
    {
        super();
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(900, 900));
        
        ship = new Ship();

        JFrame frame = new JFrame();
        frame.add(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);
        frame.setVisible(true);
        frame.pack();

        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // clear background
        ship.paint(g);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        ship.move(e.getKeyCode());
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args)
    {
        new GameScreen();
        System.out.println("Test");
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        ship.followMouse(e);
        repaint();
    }
}
