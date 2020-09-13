import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorcombrectButton implements ActionListener{

    private CoordPanel panel;
    private static boolean Colorrect;


    public ColorcombrectButton(CoordPanel panel) {
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        computeColorRect();
    	if (Colorrect == true ) {
    		Colorcombrect.execute(panel);	
    	}
    	

        if (Colorrect == false) {
        	panel.emptyRects();
        }
        System.out.println(getColorrect());
    }
    
    public void computeColorRect() {
    	Colorrect = !Colorrect;
    }
    	
    public static boolean getColorrect() {
    	return Colorrect;
    }
    
}
