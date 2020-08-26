import java.awt.*;

public class Triangle {

    private int[] x = new int[3];
    private int[] y = new int[3];

    public Triangle(int[] x, int[] y)
    {
        this.x=x;
        this.y=y;
    }

    public void setX(int[] x) {
        this.x = x;
    }

    public void setY(int[] y) {
        this.y = y;
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }
}

