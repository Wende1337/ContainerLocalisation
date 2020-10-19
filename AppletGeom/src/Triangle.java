import java.awt.*;

public class Triangle {

    // connecting Points in following order: 0, 1, 2
    private int[] x = new int[3];
    private int[] y = new int[3];

    public Triangle(int[] x, int[] y) {
        this.x = x;
        this.y = y;
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

    public int getArea() {
        return (Math.abs((this.getX()[2] - this.getX()[1]) * (this.getY()[1] - this.getY()[0])) / 2);
    }

    public int getCircum() {
        return (int) (Math.sqrt((this.getX()[0] - this.getX()[1]) * (this.getX()[0] - this.getX()[1]) + (this.getY()[0] - this.getY()[1]) * (this.getY()[0] - this.getY()[1])) +
                Math.sqrt((this.getX()[1] - this.getX()[2]) * (this.getX()[1] - this.getX()[2]) + (this.getY()[1] - this.getY()[2]) * (this.getY()[1] - this.getY()[2])) +
                Math.sqrt((this.getX()[2] - this.getX()[0]) * (this.getX()[2] - this.getX()[0]) + (this.getY()[2] - this.getY()[0]) * (this.getY()[2] - this.getY()[0])));

    }
}

