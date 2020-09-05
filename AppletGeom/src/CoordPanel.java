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
        for (int i=0; i<3; i++) {
            tris.add(null);
        }
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
            if (t==null){
                continue;
            }
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
        tris.set(0, tri);
        this.repaint();
    }

    public void setTri_Ann(Triangle[] tri){
        tris.set(1,tri[0]);
        tris.set(2,tri[1]);
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

    public void emptyTri() {
        tris.set(0,null);
        this.repaint();
    }

    public void emptyTri_Ann() {
        tris.set(1,null);
        tris.set(2,null);
        this.repaint();
    }
    
    public void emptyAll() {
    	points.clear();
    	rects.clear();
    	emptyTri_Ann();
    	emptyTri();
    	this.repaint();
    }
    
    
    
}
