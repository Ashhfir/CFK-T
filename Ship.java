import java.awt.Graphics;
import java.awt.Color;

public class Ship
{
    int x;
    int y;

    public Ship()
    {
        x = 400;
        y = 400;
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.RED);
        g.fillOval(x-25, y-25, 50, 50);
        g.fillRect(x-5, y-35, 10, 15);
    }

    public void move(String direction)
    {
        switch (direction)
        {
            case "up": // Up
                y -= 10;
                break;
            case "down": // Down
                y += 10;
                break;
            case "left": // Left
                x -= 10;
                break;
            case "right": // Right
                x += 10;
                break;
        }
        
        System.out.println("Ship moved");
    }
}
