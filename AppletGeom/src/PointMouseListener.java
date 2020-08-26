import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PointMouseListener extends MouseAdapter{

    private CoordPanel panel;
    private static Color color=new Color(255,0,0);
	private static List<Point> points = CoordPanel.getPoints();

    // Variable die Definiert ob mit der Maus Punkte gezogen werden oder eingef�gt werden

    // Variable die Definiert ob mit der Maus Punkte gezogen werden oder eingef�gt werden
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
		movingpoint=inReach(e.getX(), e.getY());
		pointmove=false;
		// if no point in euclid dist add new point
    	if (movingpoint==null) {
	        Point point = new Point(e.getX() , e.getY(), color);
	        panel.addPoint(point);
	        Pointevent = true;
	        pointmove = false;
    	}
    	//if point in euclid distance that point can be dragged, see below
    	if (movingpoint != null) {
    		pointmove =true;
			System.out.println("check");

    	}
    	//if button is active and another Point is added, update algorithm
    	if (ColorcombrectButton.getColorrect()==true & Pointevent == true  ) {
    		Colorcombrect.execute(panel);
    	}
		if (CSETButton.getCSETTRI()==true & Pointevent == true  ) {
			CSET.execute(panel);
		}
    	
    }

    public void mouseDragged (MouseEvent e) {
    	//move point
    	if (pointmove==true) {
    		movingpoint.setX(e.getX());
    		movingpoint.setY(e.getY());
    		Pointevent = true;
    		panel.repaint();
    		
    		//if Points have been moved, update colorrect algorithm
    		if (ColorcombrectButton.getColorrect()==true) {
        		Colorcombrect.execute(panel);
        		panel.repaint();
    		}
			if (CSETButton.getCSETTRI()==true) {
				CSET.execute(panel);
				panel.repaint();
			}
    	}
    }
    
    public void mouseReleased (MouseEvent e) {
    	movingpoint= null;
    }
    
    public static void Pointevent() {
    	Pointevent = !Pointevent;
    }
    
    
    public static void setrandomColor(Color randomColor) {
//        int R = (int) (Math.random() * 256);
//        int G = (int) (Math.random() * 256);
//        int B = (int) (Math.random() * 256);
//        Color randomColor = new Color(R, G, B);
        color = randomColor;
    }


	// check if a point is in range
	public static Point inReach(int x, int y) {
		int eucliddist = 10000000;
		for (Point p: points) {
			//compute euclid distance
			eucliddist= (p.getX() - x) * (p.getX() - x) + (p.getY() - y) * (p.getY() - y);
			if (eucliddist <= p.getDiameter()*p.getDiameter()) {
				return p;
			}
		}
		return null;
	}

}


