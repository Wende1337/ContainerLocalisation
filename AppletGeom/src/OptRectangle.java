
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;

public class OptRectangle {


    private static List<Point> pointsx = CoordPanel.getPoints();
    private static List<Point> pointsy = new ArrayList<Point>(pointsx.size());
    private static int step;


    public static Rectangle algo1() {


        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle = new Rectangle(0, 0, 0, 0);
        Rectangle compareRectangle = new Rectangle(0, 0, 0, 0);
        pointsy.clear();

        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());

        for (int i = 0; i < pointsx.size(); i++) {
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
                        System.out.println("ccomb: "+ ccomb+ "getcolor: "+ pointsy.get(s).getColor());
                        System.out.println("colors: "+ colors);
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
            Rectangle rect = new Rectangle(0, 0, 0, 0);
            rect = algo1();
            if (rect != null) {
                panel.setRect(rect);
                Layout.circum2.setText(Integer.toString(rect.getCircum()));
                Layout.area2.setText(Integer.toString(rect.getArea()));
            }
        }
    }

    //SELBER ALGORITHMUS BEGINNT MIT STEPS

    public static Rectangle algo1step(CoordPanel panel, int out_step) {

        step=0;
        panel.emptyRects();
        Rectangle rect = new Rectangle(0, 0, 0, 0);
        rect = algo1();

        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle = new Rectangle(0, 0, 0, 0);
        Rectangle compareRectangle = new Rectangle(0, 0, 0, 0);

        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());

        for (int i = 0; i < pointsx.size(); i++) {
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

        if (out_step==0) {
            panel.setLines(0, null);
            panel.setLines(1, null);
            panel.setLines(2, null);
            panel.setLines(3, null);
            return null;
        }

        if (out_step==1) {
            Line pline = new Line(rect.getX(), 0, rect.getX(), 5000);
            panel.setLines(0, pline);
            ccomb.clear();
            return null;
        }
        step++;

        if (out_step==2) {
            Line qline = new Line(0, rect.getY(), 5000, rect.getY());
            panel.setLines(1, qline);
            ccomb.clear();
            return null;
        }
        step++;



        cspanning.clear();
        for (int i = 0; i < colors.size(); i++) {
            cspanning.add(null);
        }

        L1:
        //r = rechte Grenze
        for (int r = 0; r < pointsx.size(); r++) {
            //1. Fall r_x muss größer als linke Grenze p sein
            if (pointsx.get(r).getX() < rect.getX()) {
                continue;
            }
            //2.Fall r_y muss größer oder gleich p_y sein
            if (pointsx.get(r).getY() < rect.getY()) {
                continue;
            }

            //setze Farbe an Stelle r
            cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

            step += 1;
            if (step == out_step) {
                Line rline = new Line(pointsx.get(r).getX(), 0, pointsx.get(r).getX(), 5000);
                panel.setLines(2, rline);
                panel.setLines(3, null);
                step = 0;
                ccomb.clear();

                System.out.println("rline changed: " + pointsx.get(r).getX());
                return null;
            }


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
                    if (pointsy.get(s).getX() < rect.getX()) {
                        continue;
                    }
                    //2. Fall s_y muss über untere Grenze q sein
                    if (pointsy.get(s).getY() < rect.getY()) {
                        continue;
                    }
                    //3. Fall s_x muss kleiner als r_x sein
                    if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                        continue;
                    }

                    step += 1;
                    if (step == out_step) {
                        Line sline = new Line(0, pointsy.get(s).getY(), 5000, pointsy.get(s).getY());
                        panel.setLines(3, sline);
                        step = 0;
                        System.out.println("sline changed: " + pointsx.get(r).getX());
                        ccomb.clear();
                        return null;
                    }



                }
            }
        }
        ccomb.clear();
        OptRectStepButton.resetOut_Step();
        step=0;
        panel.setLines(0, null);
        panel.setLines(1, null);
        panel.setLines(2, null);
        panel.setLines(3, null);
        panel.setRect(rect);
        return null;
    }

}
