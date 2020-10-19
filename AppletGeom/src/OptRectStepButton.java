import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptRectStepButton implements ActionListener {

    //step Button for the Algorithm, Step Button is used to increment the step by step Display
    private CoordPanel panel;
    private static int out_step = 1;

    public OptRectStepButton(CoordPanel panel) {
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //increment step for each Button Press
        OptRectangle.algo1step(panel, out_step);
        out_step += 1;
    }

    //to get to default empty step go to step = -1
    public static void resetOut_Step() {
        out_step = -1;
    }

    public static void increment_Step() {
        out_step += 1;
    }

}
