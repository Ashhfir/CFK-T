import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen extends JPanel implements KeyListener, MouseMotionListener, MouseListener
{
    private Ship ship;
    private final List<Laserbeam> lasers = new ArrayList<>();
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
        addMouseListener(this);

        movementTimer = new Timer(16, (event) -> moveStuff());
        movementTimer.start();
    }

    public void moveStuff()
    {
        ship.move(leftPressed, rightPressed, upPressed, downPressed);

        Iterator<Laserbeam> laserIterator = lasers.iterator();
        while (laserIterator.hasNext())
        {
            Laserbeam laser = laserIterator.next();
            laser.move();
            if (laser.isOffScreen(getWidth(), getHeight()))
            {
                laserIterator.remove();
            }
        }

        for (Asteroid a : asteroid)
        {
            if (a != null)
            {
                a.move();
            }
        }
        handleLaserAsteroidCollisions();
        handleAsteroidCollisions();
        repaint();
        if (collidedWith(ship))
        {
            resetGame();
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (Laserbeam laser : lasers)
        {
            laser.paint(g);
        }
        ship.paint(g);
        for (Asteroid a : asteroid)
        {
            if (a != null)
            {
                a.paint(g);
            }
        }
    }

    private void fireLaser()
    {
        int offsetX = (int)(Math.cos(Math.toRadians(ship.direction)) * 26);
        int offsetY = (int)(Math.sin(Math.toRadians(ship.direction)) * 26);
        lasers.add(new Laserbeam(ship.x + offsetX, ship.y + offsetY, ship.direction));
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
                fireLaser();
                break;
            case KeyEvent.VK_Q:
                ship.decelerate();
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_R:
                resetGame();
                break;
        }
    }

    public boolean collidedWith(Ship ship)
    {
        int shipRadius = 25;
        for (Asteroid a : asteroid)
        {
            if (a == null)
            {
                continue;
            }

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

    private void handleLaserAsteroidCollisions()
    {
        Iterator<Laserbeam> laserIterator = lasers.iterator();
        while (laserIterator.hasNext())
        {
            Laserbeam laser = laserIterator.next();
            boolean destroyed = false;
            for (int i = 0; i < asteroid.length; i++)
            {
                Asteroid a = asteroid[i];
                if (a == null)
                {
                    continue;
                }

                int dx = laser.x - a.x;
                int dy = laser.y - a.y;
                int radius = (a.size / 2) + 4;
                if (dx * dx + dy * dy <= radius * radius)
                {
                    asteroid[i] = null;
                    destroyed = true;
                    break;
                }
            }

            if (destroyed)
            {
                laserIterator.remove();
            }
        }
    }

    private void handleAsteroidCollisions()
    {
        for (int i = 0; i < asteroid.length; i++)
        {
            Asteroid a = asteroid[i];
            if (a == null)
            {
                continue;
            }

            for (int j = i + 1; j < asteroid.length; j++)
            {
                Asteroid b = asteroid[j];
                if (b == null)
                {
                    continue;
                }

                int dx = a.x - b.x;
                int dy = a.y - b.y;
                int rsum = (a.size / 2) + (b.size / 2);
                if (dx * dx + dy * dy <= rsum * rsum)
                {
                    a.direction = (a.direction + 180) % 360;
                    b.direction = (b.direction + 180) % 360;

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
    public void mouseClicked(MouseEvent e)
    {
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            fireLaser();
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        requestFocusInWindow();
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            fireLaser();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

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

        lasers.clear();
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
