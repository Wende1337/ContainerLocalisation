
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;

public class OptRectangle {


    private static ArrayList<Point> pointsx = CoordPanel.getPoints();
    private static ArrayList<Point> pointsy = new ArrayList<Point>(pointsx.size());
    //Für Step Algorithmus
    private static int step ;
    private static int s_ending_y=21000000;
    private static int s_ending_x=21000000;
    private static Color s_color_glob=null;



    public static Rectangle algo1() {

        step=0;
        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle = new Rectangle(0, 0, 0, 0);
        Rectangle currentRectangle = new Rectangle(0, 0, 0, 0);

        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());

        for (int i = 0; i < pointsx.size(); i++) {
            pointsy.add(pointsx.get(i));
        }
        Collections.sort(pointsy, new ComparatorPointY());

        //List for Colors
        ArrayList<Color> colors = new ArrayList<Color>(pointsx.size());
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
        Color s_color=null;


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
            for (int q = 0; q < pointsy.size(); q++) {
                // Fall falls die zwei Punkte keinen linkeren unten Eckpunkt bilden
                if (pointsy.get(q).getY() > pointsx.get(p).getY()) {
                    continue;
                }
                if (pointsy.get(q).getX() < pointsx.get(p).getX()) {
                    continue;
                }
                if (pointsx.get(p).getColor() == pointsy.get(q).getColor()) {
                    continue;
                }

                //init. Liste mit Nullwerten der Größe von Color size
                cspanning.clear();
                for (int i = 0; i < colors.size(); i++) {
                    cspanning.add(null);
                }
                int s_ending_Size = pointsy.size();
                s_color=null;
                //r = rechte Grenze
                for (int r = p; r < pointsx.size(); r++) {
                    //1. Fall r_x muss größer als linke Grenze p sein
                    if (pointsx.get(r).getX() <= pointsx.get(p).getX()) {
                        continue;
                    }
                    //2. Fall r_y muss über untere Grenze q sein
                    if (pointsx.get(r).getY() < pointsy.get(q).getY()) {
                        continue;
                    }


                    //addiere Grenzpunkte p und q hinzu
                    cspanning.set(colors.indexOf(pointsx.get(p).getColor()), pointsx.get(p).getColor());
                    cspanning.set(colors.indexOf(pointsy.get(q).getColor()), pointsy.get(q).getColor());

                    //setze Farbe an Stelle r
                    cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

                    if(s_color!= null && pointsx.get(r).getColor()!= s_color){
                        System.out.println("s_colo: "+s_color+" != "+" r color: "+ pointsx.get(r).getColor());
                        System.out.println("skip");
                        continue;
                    }

                    //wenn color Spanning
                    if (!cspanning.contains(null)) {
                        //init. Liste mit Nullwerten der Größe von Color size
                        ccomb.clear();
                        for (int i = 0; i < colors.size(); i++) {
                            ccomb.add(null);
                        }

                        //obere Grenze
                        for (int s = q; s < s_ending_Size; s++) {
                            //1. Fall s_x muss größer als linke Grenze p sein
                            if (pointsy.get(s).getX() < pointsx.get(p).getX()) {
                                continue;
                            }
                            //2. Fall s_y muss über untere Grenze q sein
                            if (pointsy.get(s).getY() < pointsy.get(q).getY()) {
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
                                s_ending_Size = s;
                                s_color=pointsy.get(s).getColor();



                                currentRectangle.setX(pointsx.get(p).getX());
                                currentRectangle.setY(pointsy.get(q).getY());
                                currentRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                                currentRectangle.setHeight(pointsy.get(s).getY() - pointsy.get(q).getY());


                                //vergleiche Rechtecke
                                if (optimumRectangle.getArea() == 0) {
                                    optimumRectangle.setX(currentRectangle.getX());
                                    optimumRectangle.setY(currentRectangle.getY());
                                    optimumRectangle.setWidth(currentRectangle.getWidth());
                                    optimumRectangle.setHeight(currentRectangle.getHeight());

                                }
                                if (Rectangle.compareRectangles(optimumRectangle, currentRectangle, "area") == currentRectangle) {
                                    optimumRectangle.setX(currentRectangle.getX());
                                    optimumRectangle.setY(currentRectangle.getY());
                                    optimumRectangle.setWidth(currentRectangle.getWidth());
                                    optimumRectangle.setHeight(currentRectangle.getHeight());
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
            int s_ending_Size = pointsy.size();
                //r = rechte Grenze
            for (int r = p; r < pointsx.size(); r++) {
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


                //wenn color Spanning
                if (!cspanning.contains(null)) {
                    //init. Liste mit Nullwerten der Größe von Color size
                    ccomb.clear();
                    for (int i = 0; i < colors.size(); i++) {
                        ccomb.add(null);
                    }

                    //s = obere Grenze
                    for (int s = 0; s < s_ending_Size ; s++) {
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
                                s_ending_Size = s;


                            currentRectangle.setX(pointsx.get(p).getX());
                            currentRectangle.setY(pointsx.get(p).getY());
                            currentRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                            currentRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(p).getY());


                            //vergleiche Rechtecke
                            if (optimumRectangle.getArea() == 0) {
                                optimumRectangle.setX(currentRectangle.getX());
                                optimumRectangle.setY(currentRectangle.getY());
                                optimumRectangle.setWidth(currentRectangle.getWidth());
                                optimumRectangle.setHeight(currentRectangle.getHeight());

                            }
                            if (Rectangle.compareRectangles(optimumRectangle, currentRectangle, "area") == currentRectangle) {
                                optimumRectangle.setX(currentRectangle.getX());
                                optimumRectangle.setY(currentRectangle.getY());
                                optimumRectangle.setWidth(currentRectangle.getWidth());
                                optimumRectangle.setHeight(currentRectangle.getHeight());
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
            long startTime = System.nanoTime();
            rect = algo1();
            long stopTime = System.nanoTime();
            if (rect != null) {
                panel.setRect(rect);
                Layout.circum1.setText(Integer.toString(rect.getCircum()));
                Layout.area1.setText(Integer.toString(rect.getArea()));
                Layout.time1.setText(Long.toString(stopTime - startTime));
            }
        }
    }

    //SELBER ALGORITHMUS BEGINNT MIT STEPS

    public static Rectangle algo1step(CoordPanel panel, int out_step) {

        panel.emptyRects();
        Rectangle rect;
        rect = algo1();


        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());

        for (int i = 0; i < pointsx.size(); i++) {
            pointsy.add(pointsx.get(i));
        }
        Collections.sort(pointsy, new ComparatorPointY());

        //List for Colors
        ArrayList<Color> colors = new ArrayList<Color>(pointsx.size());
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
        int s_ending_Size = pointsy.size();


        //wenn weniger als 2 Farben
        if (colors.size() < 2) {
            colors.clear();
            ccomb.clear();
            pointsy.clear();
            return null;
        }


        cspanning.clear();
        for (int i = 0; i < colors.size(); i++) {
            cspanning.add(null);
        }


        if (out_step==0) {
            panel.setLines(0, null);
            panel.setLines(1, null);
            panel.setLines(2, null);
            return null;
        }

        if (out_step==1) {
            Line pline = new Line(rect.getX(), 0, rect.getX(), 5000);
            panel.setLines(0, pline);
            return null;
        }
        step++;

        if (out_step==2) {
            Line qline = new Line(0, rect.getY(), 5000, rect.getY());
            panel.setLines(1, qline);
            return null;
        }
        step++;




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

            System.out.println("outstep: "+ out_step+ " step: "+ (step+1));

            if(s_color_glob!= null && pointsx.get(r).getColor()!= s_color_glob){
                step+=1;
                continue;
            }


            step += 1;
            if (step == out_step) {
                Line rline = new Line(pointsx.get(r).getX(), 0, pointsx.get(r).getX(), 5000);
                System.out.println("new r set");
                panel.setLines(2, rline);
                panel.setLines(3, null);
                step = 0;
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
                for (int s = 0; s <= s_ending_Size; s++) {
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
                    //setze Farbe in List
                    ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                    if (!ccomb.contains(null)) {
                        s_ending_Size=s;
                        s_color_glob=pointsy.get(s).getColor();
                    }
                    step += 1;
                    if (step == out_step) {
                        Line sline = new Line(0, pointsy.get(s).getY(), 5000, pointsy.get(s).getY());
                        panel.setLines(3, sline);
                        step = 0;
                        return null;
                    }
                }
            }
        }
        ccomb.clear();
        OptRectStepButton.resetOut_Step();
        s_ending_y=21000000;
        s_ending_x=21000000;

        step=0;
        s_color_glob=null;
        panel.setLines(0, null);
        panel.setLines(1, null);
        panel.setLines(2, null);
        panel.setLines(3, null);
        panel.setRect(rect);
        return null;
    }

    public static void resetStep(){
        step=0;
        s_color_glob=null;
    }
    public static void resetPointsy(){
        pointsy.clear();
    }
}
