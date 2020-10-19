import java.awt.*;
import java.util.Random;


public class Point {

    //Points Object with x and y coordinates diameter to have a more round appearance and a color
    private int x;
    private int y;
    private int diameter = 14;
    private Color color;
    Random rand = new Random();


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDiameter() {
        return diameter;
    }

    public Color getColor() {
        return color;
    }

    //function to draw the point
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
    }
}
