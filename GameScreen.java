import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.Timer;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

public class GameScreen extends JPanel implements KeyListener, MouseMotionListener
{
    private Ship ship; // used in multiple methods, so it is declared here
    private Timer movementTimer;

    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private final ArrayDeque<Integer> horizontalInputQueue = new ArrayDeque<>();
    private final ArrayDeque<Integer> verticalInputQueue = new ArrayDeque<>();

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

        movementTimer = new Timer(16, (event) -> {
            ship.move(leftPressed, rightPressed, upPressed, downPressed);
            repaint();
        });
        movementTimer.start();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // clear background
        ship.paint(g);
    }

    private void updatePressedStates()
    {
        Integer horizontalKey = horizontalInputQueue.peekLast();
        Integer verticalKey = verticalInputQueue.peekLast();

        leftPressed = horizontalKey != null && horizontalKey == KeyEvent.VK_LEFT;
        rightPressed = horizontalKey != null && horizontalKey == KeyEvent.VK_RIGHT;
        upPressed = verticalKey != null && verticalKey == KeyEvent.VK_UP;
        downPressed = verticalKey != null && verticalKey == KeyEvent.VK_DOWN;
    }

    private void addInput(ArrayDeque<Integer> inputQueue, int keyCode)
    {
        inputQueue.remove(keyCode);
        inputQueue.addLast(keyCode);
        updatePressedStates();
    }

    private void removeInput(ArrayDeque<Integer> inputQueue, int keyCode)
    {
        inputQueue.remove(keyCode);
        updatePressedStates();
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_UP:
                addInput(verticalInputQueue, KeyEvent.VK_UP);
                break;
            case KeyEvent.VK_DOWN:
                addInput(verticalInputQueue, KeyEvent.VK_DOWN);
                break;
            case KeyEvent.VK_LEFT:
                addInput(horizontalInputQueue, KeyEvent.VK_LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                addInput(horizontalInputQueue, KeyEvent.VK_RIGHT);
                break;
            case KeyEvent.VK_SPACE:
                ship.accelerate();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break; // not really necessary, but good practice
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        ship.followMouse(e);
        repaint();
    }

    public static void main(String[] args)
    {
        new GameScreen();
        System.out.println("Test");
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            removeInput(verticalInputQueue, KeyEvent.VK_UP);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            removeInput(verticalInputQueue, KeyEvent.VK_DOWN);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            removeInput(horizontalInputQueue, KeyEvent.VK_LEFT);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            removeInput(horizontalInputQueue, KeyEvent.VK_RIGHT);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {}
}
