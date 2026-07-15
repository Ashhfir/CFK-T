import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class Ship
{
    int x;
    int y;
    int speed = 5;
    int direction = 90;

    public Ship()
    {
        x = 400;
        y = 400;
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(Math.toRadians(direction), x, y);
        
        g2.setColor(Color.RED);
        g2.fillOval(x-25, y-25, 50, 50);
        g2.fillRect(x+20, y-5, 15, 10);
        
        g2.rotate(Math.toRadians(direction), x, y);
    }

    public void move(int direction)
    {
        switch (direction)
        {
            case KeyEvent.VK_UP: // Up
                y -= 10;
                break;
            case KeyEvent.VK_DOWN: // Down
                y += 10;
                break;
            case KeyEvent.VK_LEFT: // Left
                x -= 10;
                break;
            case KeyEvent.VK_RIGHT: // Right
                x += 10;
                break;
        }

        // x += speed*(int)Math.cos(Math.toRadians(direction));
        // y += speed*(int)Math.sin(Math.toRadians(direction));
        
        System.out.println("Ship moved");
    }

    public void followMouse(MouseEvent mouse)
    {
        int dx = mouse.getX() - x;
        int dy = mouse.getY() - y;
        direction = (int)Math.toDegrees(Math.atan2(dy, dx));
        // move(direction);
    }
}
