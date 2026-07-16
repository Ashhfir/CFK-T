import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class Asteroid
{
    int x = 750;
    int y = 750;
    int speed = 12;
    int size = 50;
    int direction = 90;

    public Asteroid()
    {
        size = (int)(Math.random() * 50) + 20;
        x = (int)(Math.random() * (900 - size)) + size/2;
        y = (int)(Math.random() * (900 - size)) + size/2;
        speed = 10;
        direction = (int)(Math.random() * 360);
    }

    public void paint(Graphics g)
    {
        // Graphics2D g2 = (Graphics2D) g;
        // g2.rotate(Math.toRadians(direction), x, y);
        
        g.setColor(Color.DARK_GRAY);
        g.fillOval(x-(size/2), y-(size/2), size, size);
    }

    public void move()
    {
        x += (int)(speed * Math.cos(Math.toRadians(direction)));
        y += (int)(speed * Math.sin(Math.toRadians(direction)));

        if (x < -450) x = 900;
        if (x > 900) x = 50;
        if (y < -450) y = 900;
        if (y > 900) y = 50;
    }

    public void followMouse(MouseEvent mouse)
    {
        int dx = mouse.getX() - x;
        int dy = mouse.getY() - y;
        direction = (int)Math.toDegrees(Math.atan2(dy, dx));
    }
}
