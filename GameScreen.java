import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class GameScreen extends JPanel implements KeyListener
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
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            ship.move("up"); // Move up
            repaint();
            System.out.println("Up arrow pressed");
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            ship.move("down"); // Move down
            repaint();
            System.out.println("Down arrow pressed");
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            ship.move("left"); // Move left
            repaint();
            System.out.println("Left arrow pressed");
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            ship.move("right"); // Move right
            repaint();
            System.out.println("Right arrow pressed");
        }

        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                ship.move("up");
                repaint();
                break;
            case KeyEvent.VK_DOWN:
                ship.move("down");
                repaint();
                break;
            case KeyEvent.VK_LEFT:
                ship.move("left");
                repaint();
                break;
            case KeyEvent.VK_RIGHT:
                ship.move("right");
                repaint();
                break;
        }
    }

    // @Override
    public void keyReleased(KeyEvent e) {}

    // @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args)
    {
        new GameScreen();
        System.out.println("Test");
    }
}
