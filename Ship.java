import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class Ship
{
    int x;
    int y;
    int speed = 12;
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

    public void move(boolean leftPressed, boolean rightPressed, boolean upPressed, boolean downPressed)
    {
        if (leftPressed)
        {
            x -= speed;
        }

        if (rightPressed)
        {
            x += speed;
        }

        if (upPressed)
        {
            y -= speed;
        }

        if (downPressed)
        {
            y += speed;
        }
    }

    public void accelerate()
    {
        speed += 3;
    }

    public void followMouse(MouseEvent mouse)
    {
        int dx = mouse.getX() - x;
        int dy = mouse.getY() - y;
        direction = (int)Math.toDegrees(Math.atan2(dy, dx));
    }
}
