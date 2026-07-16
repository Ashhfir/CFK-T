import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.Timer;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;

public class GameScreen extends JPanel implements KeyListener, MouseMotionListener
{
    private Ship ship; // used in multiple methods, so it is declared here
    private Asteroid[] asteroid;
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
        asteroid = new Asteroid[10];
        for (int i = 0; i < asteroid.length; i++)
        {
            asteroid[i] = new Asteroid();
        }

        JFrame frame = new JFrame("Asteroids");
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
        addMouseMotionListener(this);

        movementTimer = new Timer(16, (event) -> {
            moveStuff();
        });
        movementTimer.start();
    }

    public void moveStuff()
    {
        ship.move(leftPressed, rightPressed, upPressed, downPressed);
        for (Asteroid a : asteroid)
        {
            a.move();
        }
        handleAsteroidCollisions();
        repaint();
        if (collidedWith(ship))
        {
            // System.out.println("Collision detected!");
            resetGame();
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // clear background
        ship.paint(g);
        for (Asteroid a : asteroid)
        {
            a.paint(g);
        }
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

    private int normalizeMovementKey(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                return KeyEvent.VK_UP;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                return KeyEvent.VK_DOWN;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                return KeyEvent.VK_LEFT;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                return KeyEvent.VK_RIGHT;
            default:
                return keyCode;
        }
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
        int normalizedKey = normalizeMovementKey(e.getKeyCode());

        switch (normalizedKey)
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
                break;
            case KeyEvent.VK_R:
                resetGame();
                break; // not really necessary, but good practice
        }
    }

    public boolean collidedWith(Ship ship)
    {
        int shipRadius = 25;
        for (Asteroid a : asteroid)
        {
            int ax = a.x;
            int ay = a.y;
            int ar = a.size / 2;

            int dx = ship.x - ax;
            int dy = ship.y - ay;
            int distSq = dx * dx + dy * dy;
            int radiusSum = shipRadius + ar;
            if (distSq <= radiusSum * radiusSum)
            {
                return true;
            }
        }
        return false;
    }

    private void handleAsteroidCollisions()
    {
        for (int i = 0; i < asteroid.length; i++)
        {
            Asteroid a = asteroid[i];
            for (int j = i + 1; j < asteroid.length; j++)
            {
                Asteroid b = asteroid[j];
                int dx = a.x - b.x;
                int dy = a.y - b.y;
                int rsum = (a.size / 2) + (b.size / 2);
                if (dx * dx + dy * dy <= rsum * rsum)
                {
                    // reverse directions to separate them
                    a.direction = (a.direction + 180) % 360;
                    b.direction = (b.direction + 180) % 360;

                    // nudge them apart slightly
                    double angle = Math.atan2(dy, dx);
                    int push = 5;
                    a.x += (int)(Math.cos(angle) * push);
                    a.y += (int)(Math.sin(angle) * push);
                    b.x -= (int)(Math.cos(angle) * push);
                    b.y -= (int)(Math.sin(angle) * push);
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        ship.followMouse(e);
        repaint();
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        int normalizedKey = normalizeMovementKey(e.getKeyCode());

        if (normalizedKey == KeyEvent.VK_UP)
        {
            removeInput(verticalInputQueue, KeyEvent.VK_UP);
        }
        else if (normalizedKey == KeyEvent.VK_DOWN)
        {
            removeInput(verticalInputQueue, KeyEvent.VK_DOWN);
        }
        else if (normalizedKey == KeyEvent.VK_LEFT)
        {
            removeInput(horizontalInputQueue, KeyEvent.VK_LEFT);
        }
        else if (normalizedKey == KeyEvent.VK_RIGHT)
        {
            removeInput(horizontalInputQueue, KeyEvent.VK_RIGHT);
        }
    }

    public static void main(String[] args)
    {
        new GameScreen();
        System.out.println("Test");
    }

    public void resetGame()
    {
        if (movementTimer != null)
        {
            movementTimer.stop();
        }

        ship = new Ship();

        if (asteroid == null)
        {
            asteroid = new Asteroid[10];
        }
        for (int i = 0; i < asteroid.length; i++)
        {
            asteroid[i] = new Asteroid();
        }

        horizontalInputQueue.clear();
        verticalInputQueue.clear();
        updatePressedStates();

        if (movementTimer == null)
        {
            movementTimer = new Timer(16, (event) -> moveStuff());
        }
        movementTimer.start();

        requestFocusInWindow();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void mouseDragged(MouseEvent e) {}
}
