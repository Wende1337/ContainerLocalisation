import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CSETAButton implements ActionListener{

    private CoordPanel panel;
    private static boolean CSETAbool;


    public CSETAButton(CoordPanel panel) {
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        computeCSETA();
        if (CSETAbool == true ) {
            CSETA.execute(panel);
        }


        if (CSETAbool == false) {
            panel.emptyTri_Ann();
        }
        System.out.println(getCSETA());
    }

    public void computeCSETA() {
        CSETAbool = !CSETAbool;
    }

    public static boolean getCSETA() {
        return CSETAbool;
    }

}
