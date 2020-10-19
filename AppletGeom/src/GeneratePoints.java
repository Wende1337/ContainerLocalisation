import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GeneratePoints implements ActionListener {


    //necessary objects to mana gepoints
    private static List<Point> points = CoordPanel.getPoints();
    private CoordPanel panel;

    public GeneratePoints(CoordPanel panel) {
        super();
        this.panel = panel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //try and catch to catch right amount of Colors, currently limited to 12
        try {
            genPoints(Integer.parseInt(Layout.point_num.getText()), Integer.parseInt(Layout.color_num.getText()));
        } catch (NumberFormatException a) {
            System.out.println("Input for number Wrong" + a);
        }

    }

    public void genPoints(int point_num, int color_num) {

        //input points are divided by color to know how many points of each color are generated
        int p_percolor = Math.round(point_num / color_num);
        //first loop for color, second for points
        for (int i = 0; i < color_num; i++) {
            for (int p = 0; p < p_percolor; p++) {
                Point point = new Point((int) (Math.random() * ((panel.getBounds().getWidth() - 7) + 1) + 7), (int) (Math.random() * ((panel.getBounds().getHeight() - 7) + 1) + 7), ColorButton.getColor(i));
                panel.addPoint(point);
            }
        }
        panel.repaint();
    }


}