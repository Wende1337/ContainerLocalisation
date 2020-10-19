import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PointMouseListener extends MouseAdapter {

    //Variables necessary to manage the points and set colors of points and drag points
    private CoordPanel panel;
    private static Color color = new Color(255, 0, 0);
    private static List<Point> points = CoordPanel.getPoints();

    // Variables that are used for real time running of the Algorithms
    private static boolean pointmove = true;
    private static Point movingpoint;
    private static boolean Pointevent;


    public PointMouseListener(CoordPanel panel) {
        super();
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //get point in reach (euclid distance within diameter radius)
        movingpoint = inReach(e.getX(), e.getY());
        pointmove = false;
        // if no point in euclid dist add new point
        if (movingpoint == null) {
            Point point = new Point(e.getX(), e.getY(), color);
            panel.addPoint(point);
            Pointevent = true;
            pointmove = false;
        }
        //if point in euclid distance that point can be dragged, see below
        if (movingpoint != null) {
            pointmove = true;

        }
        //if button is active and another Point is added, update algorithm
        if (CSETButton.getCSETtri() == true & Pointevent == true) {
            CSET.execute(panel);
        }
        if (CSETAButton.getCSETA() == true & Pointevent == true) {
            CSETA.execute(panel);
        }
        if (OptRectangleButton.getOptRect() == true & Pointevent == true) {
            OptRectangle.execute(panel);
        }

    }

    public void mouseDragged(MouseEvent e) {
        //move point
        if (pointmove == true) {
            movingpoint.setX(e.getX());
            movingpoint.setY(e.getY());
            Pointevent = true;
            panel.repaint();

            //if Points have been moved, update algorithm
            if (OptRectangleButton.getOptRect() == true) {
                OptRectangle.execute(panel);
                panel.repaint();
            }
            if (CSETButton.getCSETtri() == true) {
                CSET.execute(panel);
                panel.repaint();
            }
            if (CSETAButton.getCSETA() == true) {
                CSETA.execute(panel);
                panel.repaint();
            }
        }
    }


    public void mouseReleased(MouseEvent e) {
        movingpoint = null;
    }

    public static void Pointevent() {
        Pointevent = !Pointevent;
    }


    public static void setrandomColor(Color randomColor) {
        color = randomColor;
    }


    // check if a point is in range of mouse
    public static Point inReach(int x, int y) {
        int eucliddist = 10000000;
        for (Point p : points) {
            //compute euclid distance
            eucliddist = (p.getX() - x) * (p.getX() - x) + (p.getY() - y) * (p.getY() - y);
            if (eucliddist <= p.getDiameter() * p.getDiameter()) {
                return p;
            }
        }
        return null;
    }

}


