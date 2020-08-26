import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class CoordPanel extends JPanel {

    private static List<Point> points = new LinkedList<Point>();
    private static List<Rectangle> rects = new LinkedList<Rectangle>();
    private static List<Triangle> tris = new LinkedList<Triangle>();

    public CoordPanel() {
        super();
        this.setBorder(new LineBorder(Color.WHITE, 2));
        this.setBackground(Color.WHITE);
    }

    public static List<Point> getPoints() {
        return points;
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        for (Point p : points) {
            p.draw(g);
        }
        for (Rectangle r: rects){
            g.setColor(Color.BLACK);
            g.drawRect((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
        }
        for (Triangle t: tris){
            g.setColor(Color.BLACK);
            g.drawPolygon(t.getX(),t.getY(),3);
        }
    }

    
    public void addPoint(Point point){
        points.add(point);
        this.repaint();
    }
    
    public void addRect(Rectangle rect){
        rects.add(rect);
        this.repaint();
    }

    public void setRect(Rectangle rect){
    	if (rects.size()==0) {
    		rects.add(rect);
    	} else {
    		rects.set(0, rect);
    	}
        this.repaint();
    }

    public void setTri(Triangle tri){
        if (tris.size()==0){
            tris.add(tri);
        } else {
            tris.set(0, tri);
        }
        this.repaint();
    }

    
    public void emptyPoints() {
    	points.clear();
    	this.repaint();
    }
    
    public void emptyRects() {
    	rects.clear();
    	this.repaint();
    }

    public void emptyTris() {
        tris.clear();
        this.repaint();
    }
    
    public void emptyAll() {
    	points.clear();
    	rects.clear();
    	tris.clear();
    	this.repaint();
    }
    
    
    
}
