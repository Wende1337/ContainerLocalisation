import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CSETButton implements ActionListener{

    private CoordPanel panel;
    private static boolean CSETtri;


    public CSETButton(CoordPanel panel) {
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        computeCSETTri();
        if (CSETtri == true ) {
            CSET.execute(panel);
        }


        if (CSETtri == false) {
            panel.emptyTri();
        }
        System.out.println(getCSETtri());
    }

    public void computeCSETTri() {
        CSETtri = !CSETtri;
    }

    public static boolean getCSETtri() {
        return CSETtri;
    }

}
