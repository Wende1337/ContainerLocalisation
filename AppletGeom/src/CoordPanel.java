import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

//2D Panel für Darstellung der Punkte und Container in Panel
public class CoordPanel extends JPanel {

    private static ArrayList<Point> points = new ArrayList<Point>();
    private static ArrayList<Rectangle> rects = new ArrayList<Rectangle>(5);
    private static ArrayList<Triangle> tris = new ArrayList<Triangle>(5);
    private static ArrayList<Line> lines = new ArrayList<>(5);




    public CoordPanel() {
        super();
        this.setBorder(new LineBorder(Color.WHITE, 2));
        this.setBackground(Color.WHITE);
        for (int i=0; i<4; i++) {
            tris.add(null);
        }
        for (int i=0; i<4; i++) {
            lines.add(null);
        }
    }

    public static ArrayList<Point> getPoints() {
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
            g.drawRect( r.getX(),  r.getY(),  r.getWidth(),  r.getHeight());
        }
        for (Triangle t: tris){
            if (t==null){
                continue;
            }
            g.setColor(Color.BLACK);
            g.drawPolygon(t.getX(),t.getY(),3);
        }
        for (Line l: lines){
            if (l==null){
                continue;
            }
            g.setColor(Color.BLACK);
            g.drawLine(l.getX1(),l.getY1(),l.getX2(),l.getY2());
        }
        if (lines.get(3)!=null){
            g.setColor(Color.RED);
            g.drawLine(lines.get(3).getX1(),lines.get(3).getY1(),lines.get(3).getX2(),lines.get(3).getY2());
        }
    }

    
    public void addPoint(Point point){
        points.add(point);
        this.repaint();
    }

    public void setLines( int idx, Line line){
        lines.set(idx, line);
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

    public void setTri_annStep(Triangle tri){
        tris.set(3,tri);
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

    public void emptyLine() {
        for (int i=0; i<4; i++) {
            lines.set(i,null);
        }
    }
    
    public void emptyAll() {
    	points.clear();
    	rects.clear();

    	emptyTri_Ann();
    	emptyTri();
    	emptyLine();

    	OptRectangle.resetStep();
    	CSET.resetStep();
        CSETA.resetStep();
    	OptRectStepButton.resetOut_Step();
    	OptRectangle.resetPointsy();
    	CSETStepButton.resetOut_Step();
        CSETAStepButton.resetOut_Step();
    	this.repaint();
    }
}
