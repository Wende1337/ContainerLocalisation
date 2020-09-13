import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptRectangleButton implements ActionListener{

    private CoordPanel panel;
    private static boolean OptRect;


    public OptRectangleButton(CoordPanel panel) {
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        computeOptRect();
        if (OptRect == true ) {
            OptRectangle.execute(panel);
        }


        if (OptRect == false) {
            panel.emptyRects();
        }
        System.out.println(getOptRect());
    }

    public void computeOptRect() {
        OptRect = !OptRect;
    }

    public static boolean getOptRect() {
        return OptRect;
    }

}
