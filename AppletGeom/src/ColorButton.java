import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Actionlistener to get random Color on Button Click
public class ColorButton implements ActionListener {

	// RBG Values distinct to each other
	private Color[] colors = {new Color(96,96,96),new Color(192,160,0),new Color(224,192,96),new Color(255,224,0),
			new Color(255,144,32),new Color(240,0,0),new Color(224,128,144),new Color(176,96,96),new Color(160,0,64),
			new Color(96,0,144),new Color(96,96,192),new Color(160,160,255),new Color(0,144,255),new Color(96,192,144),
			new Color(160,224,128),new Color(0,192,0), new Color(0,128,0)};
	private int colorcounter= 0;
	
	
    @Override
    public void actionPerformed(ActionEvent e) {
    	boolean move=false;
		PointMouseListener.switchmove(move);
        PointMouseListener.setrandomColor(colors[colorcounter]);
        colorcounter+=1;
        if (colorcounter == 17) {
        	colorcounter = 0;
        }
    }

}
