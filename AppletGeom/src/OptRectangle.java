
import java.awt.*;
import java.util.*;
import java.util.List;

public class OptRectangle {


    private static List<Point> pointsx = CoordPanel.getPoints();
    private static List<Point> pointsy = new ArrayList<Point>(pointsx.size());

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

        //wenn weniger als 2 Farben
        if (colors.size() < 2) {
            return null;
        }

        //Liste die aktuelle Farben zählt
        ArrayList<Color> cspanning = new ArrayList<Color>(colors.size());
        ArrayList<Color> ccomb = new ArrayList<Color>(colors.size());
        //Eckpunkte für die Variablen


        for (int i=0; i< pointsx.size(); i++){
            System.out.println("Point i="+i+" X: "+pointsx.get(i).getX()+" Y: "+pointsx.get(i).getY());
        }
        for (int i=0; i< pointsy.size(); i++){
            System.out.println("Point i="+i+" X: "+pointsy.get(i).getX()+" Y: "+pointsy.get(i).getY());
        }

        //iteriere durch jedes Punktpaar durch, p= linke, q= untere Grenze
        for (int p = 0; p < pointsx.size(); p++) {
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q = 0; q < pointsx.size(); q++) {
                if (p==0 && q==2 ){
                    System.out.println("Das ist die Kombination asdasdasdasdadasdasdasdasd");
                }
                // Fall falls die zwei Punkte keinen linkeren unten Eckpunkt bilden
                if (pointsx.get(q).getY() > pointsx.get(p).getY()) {
                    System.out.println("Fehler 0.1: q_y: "+ pointsx.get(q).getY()+ " > p_y: "+pointsx.get(p).getY());
                    continue;
                }
                if (pointsx.get(q).getX() < pointsx.get(p).getX()) {
                    System.out.println("Fehler 0.2: q_x: "+ pointsx.get(q).getX()+ " < p_x: "+pointsx.get(p).getX());
                    continue;
                }
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()) {
                    System.out.println("Fehler 0.3: q color: "+ pointsx.get(p).getColor()+ " == p : "+pointsx.get(q).getColor());
                    continue;
                }

                //init. Liste mit Nullwerten der Größe von Color size
                cspanning.clear();
                for (int i = 0; i < colors.size(); i++) {
                    cspanning.add(null);
                }
                //r = rechte Grenze
                for (int r = 0; r < pointsx.size(); r++) {
                    if (p==0 && q==2 & r == 2 ){
                        System.out.println("Das ist die Kombination asdasdasdasdadasdasdasdasd");
                    }
                    //1. Fall r_x muss größer als linke Grenze p sein
                    if (pointsx.get(r).getX() <= pointsx.get(p).getX()) {
                        System.out.println("Fehler 1.1: r_x: "+ pointsx.get(r).getX()+ " <= p_x : "+pointsx.get(p).getX());
                        continue;
                    }
                    //2. Fall r_y muss über untere Grenze q sein
                    if (pointsx.get(r).getY() < pointsx.get(q).getY()) {
                        System.out.println("Fehler 1.2: r_y: "+ pointsx.get(r).getY()+ " < q_y : "+pointsx.get(q).getY());
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
                                System.out.println("Fehler 2.1: s_x: "+ pointsy.get(s).getX()+ " < p_x: "+pointsx.get(p).getX());
                                continue;
                            }
                            //2. Fall s_y muss über untere Grenze q sein
                            if (pointsy.get(s).getY() < pointsx.get(q).getY()) {
                                System.out.println("Fehler 2.2: s_y: "+ pointsy.get(s).getY()+ " < q_y: "+pointsx.get(q).getY());
                                continue;
                            }
                            //3. Fall s_x muss kleiner als r_x sein
                            if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                                System.out.println("Fehler 2.3: s_x: "+ pointsy.get(s).getX()+ " > p_x: "+pointsx.get(r).getX());
                                continue;
                            }

                            //setze Farbe in List
                            ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                            System.out.println("X: "+pointsx.get(p).getX());
                            System.out.println("Y: "+pointsx.get(q).getY());
                            System.out.println("Width: "+(pointsx.get(r).getX() - pointsx.get(p).getX()));
                            System.out.println("r_x: "+pointsx.get(r).getX() +"q_x: "+ pointsx.get(q).getX());
                            System.out.println("p_x: "+pointsx.get(p).getX() +"s_x: "+ pointsy.get(s).getX());
                            System.out.println("Height: "+(pointsy.get(s).getY() - pointsx.get(q).getY()));

                            System.out.println("ccomb size: "+ccomb);

                            //wenn Color Spanning
                            if (!ccomb.contains(null)) {


                                compareRectangle.setX(pointsx.get(p).getX());
                                compareRectangle.setY(pointsx.get(q).getY());
                                compareRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                                compareRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(q).getY());

                                //vergleiche nach "area" oder "scope"
                                System.out.println("CompareRect area: " + Rectangle.getArea(compareRectangle) + " Opt Rect area: " + Rectangle.getArea(optimumRectangle));

                                //vergleiche Rechtecke
                                if (Rectangle.getArea(optimumRectangle) == 0) {
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


        //iteriere durch jedes Punktpaar durch, p= linke, q= untere Grenze
        for (int p = 0; p < pointsx.size(); p++) {
            // x(p) < x(q) zu jedem Zeiptunkt
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
                              /*  System.out.println("p_x: "+ pointsx.get(p).getX());
                                System.out.println("p_y: "+ pointsx.get(q).getY());
                                System.out.println("width: "+ (pointsx.get(r).getX()-pointsx.get(p).getX()));
                                System.out.println("height: "+ (pointsy.get(s).getY()-pointsx.get(q).getY()));*/


                            compareRectangle.setX(pointsx.get(p).getX());
                            compareRectangle.setY(pointsx.get(p).getY());
                            compareRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                            compareRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(p).getY());

                            //vergleiche nach "area" oder "scope"
                            System.out.println("CompareRect area: " + Rectangle.getArea(compareRectangle) + " Opt Rect area: " + Rectangle.getArea(optimumRectangle));

                            //vergleiche Rechtecke
                            if (Rectangle.getArea(optimumRectangle) == 0) {
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
        return optimumRectangle;
    }


    public static void execute(CoordPanel panel) {

        if (pointsx.size() >= 2) {
            panel.setRect(algo1());
            System.out.println("New Rectangle");
        }
    }
}