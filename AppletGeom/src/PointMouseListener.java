import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PointMouseListener extends MouseAdapter{

    private CoordPanel panel;
    private static Color color;

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
    	if (pointmove == false) {
	        Point point = new Point(e.getX() , e.getY(), color);
	        panel.addPoint(point);
	        Pointevent = true;
    	}
    	//get point in reach (euclid distance within diameter radius)
    	if (pointmove == true) {
    		movingpoint = MovePointButton.inReach(e.getX(), e.getY());

    	}
    	//if button is active and another Point is added, update algorithm
    	if (ColorcombrectButton.getColorrect()==true & Pointevent == true  ) {
    		Colorcombrect.execute(panel);
    	}
    	
    }

    public void mouseDragged (MouseEvent e) {
    	//move point 
    	if (pointmove == true & movingpoint != null) {
    		movingpoint.setX(e.getX());
    		movingpoint.setY(e.getY());
    		Pointevent = true;
    		
    		//if Points have been moved, update colorrect algorithm
    		if (ColorcombrectButton.getColorrect()==true) {
        		Colorcombrect.execute(panel);
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
    
    

    
    public static void switchmove(boolean statement) {
    	if (statement == true) {
    		pointmove = true;
    	} else if (statement == false) {
    		pointmove = false;
    	}
    }
}
