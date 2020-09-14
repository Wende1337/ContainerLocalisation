
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;

public class OptRectangle {


    private static List<Point> pointsx = CoordPanel.getPoints();
    private static List<Point> pointsy = new ArrayList<Point>(pointsx.size());
    private static int step=-1;



    public static Rectangle algo1() {

        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle = new Rectangle(0, 0, 0, 0);
        Rectangle compareRectangle = new Rectangle(0, 0, 0, 0);

        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());

        for (int i=0; i< pointsx.size(); i++){
            pointsy.add(pointsx.get(i));
        }
        Collections.sort(pointsy, new ComparatorPointY());

        //List for Colors
        List<Color> colors = new LinkedList<Color>();
        colors.add(pointsx.get(0).getColor());
        //        get all point colors
        for (int k = 1; k < pointsx.size(); k++) {
            for (int l = 0; l < colors.size(); l++) {
                if (colors.contains(pointsx.get(k).getColor()) == false) {
                    colors.add(pointsx.get(k).getColor());
                }
            }
        }

        //Liste die aktuelle Farben zählt
        ArrayList<Color> cspanning = new ArrayList<Color>(colors.size());
        ArrayList<Color> ccomb = new ArrayList<Color>(colors.size());
        //Eckpunkte für die Variablen

        //wenn weniger als 2 Farben
        if (colors.size() < 2) {
            colors.clear();
            ccomb.clear();
            pointsy.clear();
            return null;
        }



        //iteriere durch jedes Punktpaar durch, p= linke, q= untere Grenze
        for (int p = 0; p < pointsx.size(); p++) {
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q = 0; q < pointsx.size(); q++) {
                // Fall falls die zwei Punkte keinen linkeren unten Eckpunkt bilden
                if (pointsx.get(q).getY() > pointsx.get(p).getY()) {
                    continue;
                }
                if (pointsx.get(q).getX() < pointsx.get(p).getX()) {
                    continue;
                }
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()) {
                    continue;
                }

                //init. Liste mit Nullwerten der Größe von Color size
                cspanning.clear();
                for (int i = 0; i < colors.size(); i++) {
                    cspanning.add(null);
                }
                //r = rechte Grenze
                for (int r = 0; r < pointsx.size(); r++) {
                    //1. Fall r_x muss größer als linke Grenze p sein
                    if (pointsx.get(r).getX() <= pointsx.get(p).getX()) {
                        continue;
                    }
                    //2. Fall r_y muss über untere Grenze q sein
                    if (pointsx.get(r).getY() < pointsx.get(q).getY()) {
                        continue;
                    }

                    //addiere Grenzpunkte p und q hinzu
                    cspanning.set(colors.indexOf(pointsx.get(p).getColor()), pointsx.get(p).getColor());
                    cspanning.set(colors.indexOf(pointsx.get(q).getColor()), pointsx.get(q).getColor());

                    //setze Farbe an Stelle r
                    cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

                    //wenn color Spanning
                    if (!cspanning.contains(null)) {
                        //init. Liste mit Nullwerten der Größe von Color size
                        ccomb.clear();
                        for (int i = 0; i < colors.size(); i++) {
                            ccomb.add(null);
                        }

                        //s = obere Grenze
                        for (int s = 0; s < pointsy.size(); s++) {
                            //1. Fall s_x muss größer als linke Grenze p sein
                            if (pointsy.get(s).getX() < pointsx.get(p).getX()) {
                                continue;
                            }
                            //2. Fall s_y muss über untere Grenze q sein
                            if (pointsy.get(s).getY() < pointsx.get(q).getY()) {
                                continue;
                            }
                            //3. Fall s_x muss kleiner als r_x sein
                            if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                                continue;
                            }

                            //setze Farbe in List
                            ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                            //wenn Color Spanning
                            if (!ccomb.contains(null)) {


                                compareRectangle.setX(pointsx.get(p).getX());
                                compareRectangle.setY(pointsx.get(q).getY());
                                compareRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                                compareRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(q).getY());


                                //vergleiche Rechtecke
                                if (optimumRectangle.getArea() == 0) {
                                    optimumRectangle.setX(compareRectangle.getX());
                                    optimumRectangle.setY(compareRectangle.getY());
                                    optimumRectangle.setWidth(compareRectangle.getWidth());
                                    optimumRectangle.setHeight(compareRectangle.getHeight());

                                }
                                if (Rectangle.compareRectangles(optimumRectangle, compareRectangle, "area") == compareRectangle) {
                                    optimumRectangle.setX(compareRectangle.getX());
                                    optimumRectangle.setY(compareRectangle.getY());
                                    optimumRectangle.setWidth(compareRectangle.getWidth());
                                    optimumRectangle.setHeight(compareRectangle.getHeight());
                                }
                            }
                        }
                    }
                }
            }
        }


        //iteriere durch jedes Punktpaar durch, p= linke, p= untere Grenze
        for (int p = 0; p < pointsx.size(); p++) {
            // Fall falls die zwei Punkte keinen linkeren unten Eckpunkt bilden
            //init. Liste mit Nullwerten der Größe von Color size

            cspanning.clear();
            for (int i = 0; i < colors.size(); i++) {
                cspanning.add(null);
            }

            //r = rechte Grenze
            for (int r = 0; r < pointsx.size(); r++) {
                //1. Fall r_x muss größer als linke Grenze p sein
                if (pointsx.get(r).getX() < pointsx.get(p).getX()) {
                    continue;
                }
                //2.Fall r_y muss größer oder gleich p_y sein
                if (pointsx.get(r).getY() < pointsx.get(p).getY()) {
                    continue;
                }



                //addiere Grenzpunkte p und q hinzu
                cspanning.set(colors.indexOf(pointsx.get(p).getColor()), pointsx.get(p).getColor());

                //setze Farbe an Stelle r
                cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

                //System.out.println("Cspanning: "+cspanning.size() + cspanning);

                //wenn color Spanning
                if (!cspanning.contains(null)) {
                    //init. Liste mit Nullwerten der Größe von Color size
                    ccomb.clear();
                    for (int i = 0; i < colors.size(); i++) {
                        ccomb.add(null);
                    }

                    //s = obere Grenze
                    for (int s = 0; s < pointsy.size(); s++) {
                        //1. Fall s_x muss größer als linke Grenze p sein
                        if (pointsy.get(s).getX() < pointsx.get(p).getX()) {
                            continue;
                        }
                        //2. Fall s_y muss über untere Grenze q sein
                        if (pointsy.get(s).getY() < pointsx.get(p).getY()) {
                            continue;
                        }
                        //3. Fall s_x muss kleiner als r_x sein
                        if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                            continue;
                        }


                        //setze Farbe in List
                        ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                        //wenn Color Spanning
                        if (!ccomb.contains(null)) {

                            compareRectangle.setX(pointsx.get(p).getX());
                            compareRectangle.setY(pointsx.get(p).getY());
                            compareRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                            compareRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(p).getY());


                            //vergleiche Rechtecke
                            if (optimumRectangle.getArea() == 0) {
                                optimumRectangle.setX(compareRectangle.getX());
                                optimumRectangle.setY(compareRectangle.getY());
                                optimumRectangle.setWidth(compareRectangle.getWidth());
                                optimumRectangle.setHeight(compareRectangle.getHeight());

                            }
                            if (Rectangle.compareRectangles(optimumRectangle, compareRectangle, "area") == compareRectangle) {
                                optimumRectangle.setX(compareRectangle.getX());
                                optimumRectangle.setY(compareRectangle.getY());
                                optimumRectangle.setWidth(compareRectangle.getWidth());
                                optimumRectangle.setHeight(compareRectangle.getHeight());
                            }
                        }
                    }
                }
            }
        }
        colors.clear();
        ccomb.clear();
        pointsy.clear();
        cspanning.clear();

        return optimumRectangle;
    }


    public static void execute(CoordPanel panel) {

        if (pointsx.size() >= 2) {
            Rectangle rect = new Rectangle(0,0,0,0);
            rect= algo1();
            if (rect!=null){
                panel.setRect(rect);
                Layout.circum2.setText(Integer.toString(rect.getCircum()));
                Layout.area2.setText(Integer.toString(rect.getArea()));
            }
        }
    }

    //SELBER ALGORITHMUS BEGINNT MIT STEPS

    public static void algo1step(CoordPanel panel, int out_step) {

        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle = new Rectangle(0, 0, 0, 0);
        Rectangle compareRectangle = new Rectangle(0, 0, 0, 0);

        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());

        for (int i=0; i< pointsx.size(); i++){
            pointsy.add(pointsx.get(i));
        }
        Collections.sort(pointsy, new ComparatorPointY());

        //List for Colors
        List<Color> colors = new LinkedList<Color>();
        colors.add(pointsx.get(0).getColor());
        //        get all point colors
        for (int k = 1; k < pointsx.size(); k++) {
            for (int l = 0; l < colors.size(); l++) {
                if (colors.contains(pointsx.get(k).getColor()) == false) {
                    colors.add(pointsx.get(k).getColor());
                }
            }
        }

        //Liste die aktuelle Farben zählt
        ArrayList<Color> cspanning = new ArrayList<Color>(colors.size());
        ArrayList<Color> ccomb = new ArrayList<Color>(colors.size());
        //Eckpunkte für die Variablen

        //wenn weniger als 2 Farben
        if (colors.size() < 2) {
            colors.clear();
            ccomb.clear();
            pointsy.clear();
        }



        //iteriere durch jedes Punktpaar durch, p= linke, q= untere Grenze
        P1:
        for (int p = 0; p < pointsx.size(); p++) {
            step+=1;
            if (step == out_step){
                Line pline = new Line(pointsx.get(p).getX(),0,pointsx.get(p).getX(),5000);
                panel.setLines(0,pline );
                step=0;
                break P1;
            }

            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q = 0; q < pointsx.size(); q++) {
                step+=1;
                if (step == out_step){
                    Line qline = new Line(0,pointsx.get(q).getY(),5000,pointsx.get(q).getY());
                    panel.setLines(1, qline );
                    step=0;
                    break P1;
                }
                // Fall falls die zwei Punkte keinen linkeren unten Eckpunkt bilden
                if (pointsx.get(q).getY() > pointsx.get(p).getY()) {
                    continue;
                }
                if (pointsx.get(q).getX() < pointsx.get(p).getX()) {
                    continue;
                }
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()) {
                    continue;
                }

                //init. Liste mit Nullwerten der Größe von Color size
                cspanning.clear();
                for (int i = 0; i < colors.size(); i++) {
                    cspanning.add(null);
                }

                //r = rechte Grenze
                for (int r = 0; r < pointsx.size(); r++) {
                    step+=1;
                    if (step == out_step){
                        Line rline = new Line(pointsx.get(q).getX(),0,pointsx.get(q).getX(),5000);
                        panel.setLines(2, rline );
                        step=0;
                        break P1;
                    }

                    //1. Fall r_x muss größer als linke Grenze p sein
                    if (pointsx.get(r).getX() <= pointsx.get(p).getX()) {
                        continue;
                    }
                    //2. Fall r_y muss über untere Grenze q sein
                    if (pointsx.get(r).getY() < pointsx.get(q).getY()) {
                        continue;
                    }

                    //addiere Grenzpunkte p und q hinzu
                    cspanning.set(colors.indexOf(pointsx.get(p).getColor()), pointsx.get(p).getColor());
                    cspanning.set(colors.indexOf(pointsx.get(q).getColor()), pointsx.get(q).getColor());

                    //setze Farbe an Stelle r
                    cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

                    //wenn color Spanning
                    if (!cspanning.contains(null)) {
                        //init. Liste mit Nullwerten der Größe von Color size
                        ccomb.clear();
                        for (int i = 0; i < colors.size(); i++) {
                            ccomb.add(null);
                        }

                        //s = obere Grenze
                        for (int s = 0; s < pointsy.size(); s++) {
                            step+=1;
                            if (step == out_step){
                                Line sline = new Line(0,pointsy.get(s).getY(),5000,pointsy.get(s).getY());
                                panel.setLines(3, sline );
                                step=0;
                                break P1;
                            }

                            //1. Fall s_x muss größer als linke Grenze p sein
                            if (pointsy.get(s).getX() < pointsx.get(p).getX()) {
                                continue;
                            }
                            //2. Fall s_y muss über untere Grenze q sein
                            if (pointsy.get(s).getY() < pointsx.get(q).getY()) {
                                continue;
                            }
                            //3. Fall s_x muss kleiner als r_x sein
                            if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                                continue;
                            }

                            //setze Farbe in List
                            ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                            //wenn Color Spanning
                            if (!ccomb.contains(null)) {


                                compareRectangle.setX(pointsx.get(p).getX());
                                compareRectangle.setY(pointsx.get(q).getY());
                                compareRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                                compareRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(q).getY());

                                panel.setRect(compareRectangle);

                                //vergleiche Rechtecke
                                if (optimumRectangle.getArea() == 0) {
                                    optimumRectangle.setX(compareRectangle.getX());
                                    optimumRectangle.setY(compareRectangle.getY());
                                    optimumRectangle.setWidth(compareRectangle.getWidth());
                                    optimumRectangle.setHeight(compareRectangle.getHeight());

                                }
                                if (Rectangle.compareRectangles(optimumRectangle, compareRectangle, "area") == compareRectangle) {
                                    optimumRectangle.setX(compareRectangle.getX());
                                    optimumRectangle.setY(compareRectangle.getY());
                                    optimumRectangle.setWidth(compareRectangle.getWidth());
                                    optimumRectangle.setHeight(compareRectangle.getHeight());
                                }
                            }
                        }
                    }
                }
            }
        }
        step = 0;


        //iteriere durch jedes Punktpaar durch, p= linke, p= untere Grenze
        P2:
        for (int p = 0; p < pointsx.size(); p++) {
            // Fall falls die zwei Punkte keinen linkeren unten Eckpunkt bilden
            //init. Liste mit Nullwerten der Größe von Color size

            step+=1;
            if (step == out_step){
                Line p1line = new Line(0, pointsx.get(p).getY(),5000,pointsx.get(p).getY());
                Line p2line = new Line(pointsx.get(p).getY(),0,pointsx.get(p).getY(),5000);
                panel.setLines(0, p1line );
                panel.setLines(1, p2line );
                step=0;
                break P2;
            }

            cspanning.clear();
            for (int i = 0; i < colors.size(); i++) {
                cspanning.add(null);
            }

            //r = rechte Grenze
            for (int r = 0; r < pointsx.size(); r++) {
                step+=1;
                if (step == out_step){
                    Line rline = new Line(pointsx.get(r).getY(),0,pointsx.get(r).getY(),5000);
                    panel.setLines(2, rline );
                    step=0;
                    break P2;
                }
                //1. Fall r_x muss größer als linke Grenze p sein
                if (pointsx.get(r).getX() < pointsx.get(p).getX()) {
                    continue;
                }
                //2.Fall r_y muss größer oder gleich p_y sein
                if (pointsx.get(r).getY() < pointsx.get(p).getY()) {
                    continue;
                }



                //addiere Grenzpunkte p und q hinzu
                cspanning.set(colors.indexOf(pointsx.get(p).getColor()), pointsx.get(p).getColor());

                //setze Farbe an Stelle r
                cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

                //System.out.println("Cspanning: "+cspanning.size() + cspanning);

                //wenn color Spanning
                if (!cspanning.contains(null)) {
                    //init. Liste mit Nullwerten der Größe von Color size
                    ccomb.clear();
                    for (int i = 0; i < colors.size(); i++) {
                        ccomb.add(null);
                    }

                    //s = obere Grenze
                    for (int s = 0; s < pointsy.size(); s++) {
                        step+=1;
                        if (step == out_step){
                            Line sline = new Line(0,pointsy.get(s).getY(),5000,pointsy.get(s).getY());
                            panel.setLines(3, sline );
                            step=0;
                            break P2;
                        }
                        //1. Fall s_x muss größer als linke Grenze p sein
                        if (pointsy.get(s).getX() < pointsx.get(p).getX()) {
                            continue;
                        }
                        //2. Fall s_y muss über untere Grenze q sein
                        if (pointsy.get(s).getY() < pointsx.get(p).getY()) {
                            continue;
                        }
                        //3. Fall s_x muss kleiner als r_x sein
                        if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                            continue;
                        }


                        //setze Farbe in List
                        ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                        //wenn Color Spanning
                        if (!ccomb.contains(null)) {

                            compareRectangle.setX(pointsx.get(p).getX());
                            compareRectangle.setY(pointsx.get(p).getY());
                            compareRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                            compareRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(p).getY());

                            panel.setRect(compareRectangle);

                            //vergleiche Rechtecke
                            if (optimumRectangle.getArea() == 0) {
                                optimumRectangle.setX(compareRectangle.getX());
                                optimumRectangle.setY(compareRectangle.getY());
                                optimumRectangle.setWidth(compareRectangle.getWidth());
                                optimumRectangle.setHeight(compareRectangle.getHeight());

                            }
                            if (Rectangle.compareRectangles(optimumRectangle, compareRectangle, "area") == compareRectangle) {
                                optimumRectangle.setX(compareRectangle.getX());
                                optimumRectangle.setY(compareRectangle.getY());
                                optimumRectangle.setWidth(compareRectangle.getWidth());
                                optimumRectangle.setHeight(compareRectangle.getHeight());
                            }
                        }
                    }
                }
            }
        }
        colors.clear();
        ccomb.clear();
        pointsy.clear();
        cspanning.clear();


        OptRectStepButton.resetStep();
        panel.setRect(optimumRectangle);
    }
}