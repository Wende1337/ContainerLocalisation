import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Actionlistener to get random Color on Button Click
public class ColorButton implements ActionListener {

    // RBG Values distinct to each other, 12 Colors
    private static final Color[] colors = {new Color(255, 0, 0), new Color(255, 255, 0), new Color(0, 234, 255), new Color(170, 0, 255),
            new Color(255, 127, 0), new Color(106, 255, 0), new Color(156, 156, 156, 255), new Color(255, 0, 170), new Color(0, 64, 255),
            new Color(0, 0, 0), new Color(60, 142, 0), new Color(217, 220, 145)};

    //counter for pseudo random colors
    private static int colorcounter = 1;


    @Override
    public void actionPerformed(ActionEvent e) {
        //colorcounter incrementet by button press
        PointMouseListener.setrandomColor(colors[colorcounter]);
        colorcounter += 1;
        if (colorcounter == 12) {
            colorcounter = 0;
        }
    }

    //specific color choices for color palette
    public static void setColor(int c) {
        colorcounter = c;
        PointMouseListener.setrandomColor(colors[colorcounter]);
    }

    public static Color getColor(int i) {
        return colors[i];
    }
}
