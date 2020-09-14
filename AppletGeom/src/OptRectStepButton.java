import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptRectStepButton implements ActionListener{

    private CoordPanel panel;
    private static int out_step=0;

    public OptRectStepButton(CoordPanel panel) {
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptRectangle.algo1step(panel, out_step);
        out_step+=1;
    }

    public static void resetStep(){
        out_step=0;
    }

}
