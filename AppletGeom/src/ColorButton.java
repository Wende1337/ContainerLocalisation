import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Actionlistener to get random Color on Button Click
public class ColorButton implements ActionListener {

	// RBG Values distinct to each other
	private static Color[] colors = {new Color(255,0,0),new Color(255,255,0),new Color(0,234,255),new Color(170,0,255),
			new Color(255,127,0),new Color(106,255,0),new Color(156, 156, 156, 255),new Color(255,0,170),new Color(0,64,255),
			new Color(0, 0, 0),new Color(60, 142, 0),new Color(217, 220, 145)};
	private static int colorcounter= 1;

	
    @Override
    public void actionPerformed(ActionEvent e) {
        PointMouseListener.setrandomColor(colors[colorcounter]);
        colorcounter+=1;
        if (colorcounter == 12) {
        	colorcounter = 0;
        }
    }

    public static void setColor(int c){
        colorcounter= c;
        PointMouseListener.setrandomColor(colors[colorcounter]);
    }

    public static Color getColor(int i){
        return colors[i];
    }
}
