import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


//Actionlistener to get random Color on Button Click
public class MovePointButton implements ActionListener {

    private static List<Point> points = CoordPanel.getPoints();
	
    @Override
    public void actionPerformed(ActionEvent e) {
    	boolean move=true;
		PointMouseListener.switchmove(move);
    }
    
    // check if a point is in range 
    public static Point inReach(int x, int y) {
    	int eucliddist = 10000000;
    	for (Point p: points) {
    		//compute euclid distance 
    		eucliddist= (p.getX()-x)*(p.getX()-x) + (p.getY()-y)*(p.getY()-y);
    		if (eucliddist <= p.getDiameter()*p.getDiameter()) {
    			return p;
    		}
    	}
    	return null;
    }

}
