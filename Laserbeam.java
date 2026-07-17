import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Laserbeam
{
    int x;
    int y;
    int speed = 12;
    int direction = 90;

    public Laserbeam(int shipX, int shipY, int shipDirection)
    {
        x = shipX;
        y = shipY;
        direction = shipDirection;
        speed = 18;
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(80, 220, 255));
        g2.setStroke(new BasicStroke(4));

        int endX = x + (int)(Math.cos(Math.toRadians(direction)) * 24);
        int endY = y + (int)(Math.sin(Math.toRadians(direction)) * 24);
        g2.drawLine(x, y, endX, endY);

        g2.setColor(new Color(255, 255, 170));
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(x, y, endX, endY);
        g2.dispose();
    }

    public void move()
    {
        x += (int)(speed * Math.cos(Math.toRadians(direction)));
        y += (int)(speed * Math.sin(Math.toRadians(direction)));
    }

    public boolean isOffScreen(int width, int height)
    {
        return x < -50 || x > width + 50 || y < -50 || y > height + 50;
    }
}
