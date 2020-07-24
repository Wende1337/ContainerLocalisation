import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Actionlistener to get random Color on Button Click
public class ColorButton implements ActionListener {

	// RBG Values distinct to each other
	private Color[] colors = {new Color(255,0,0),new Color(255,255,0),new Color(0,234,255),new Color(170,0,255),
			new Color(255,127,0),new Color(106,255,0),new Color(0,149,255),new Color(255,0,170),new Color(0,64,255),
			new Color(100,100,100),new Color(79,143,35),new Color(231,233,185)};
	private int colorcounter= 1;
	
	
    @Override
    public void actionPerformed(ActionEvent e) {
        PointMouseListener.setrandomColor(colors[colorcounter]);
        System.out.println(colors.length);
        colorcounter+=1;
        if (colorcounter == 12) {
        	colorcounter = 0;
        }
    }

}
