
import java.awt.*;
import java.util.*;
import java.util.List;

public class OptRectangle {


    private static List<Point> pointsx = CoordPanel.getPoints();
    private static List<Point> pointsy = CoordPanel.getPoints();

    public static Rectangle algo1() {

        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle = new Rectangle(0, 0, 0, 0);
        Rectangle compareRectangle = new Rectangle(0, 0, 0, 0);

        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());
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
        System.out.println("Colors: " + colors.size());

        //wenn weniger als 2 Farben
        if (colors.size() < 2) {
            return null;
        }

        //Liste die aktuelle Farben zählt
        ArrayList<Color> cspanning = new ArrayList<Color>(colors.size());
        ArrayList<Color> ccomb = new ArrayList<Color>(colors.size());
        //Eckpunkte für die Variablen

        //iteriere durch jedes Punktpaar durch, p= linke, q= untere Grenze
        for (int p = 0; p < pointsx.size(); p++) {
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q = 0; q < pointsx.size(); q++) {
                // Fall falls die zwei Punkte keinen linkeren unten Eckpunkt bilden
                if (pointsx.get(p).getY() < pointsx.get(q).getY()) {
                    continue;
                }
                if(pointsx.get(p).getColor() == pointsx.get(q).getColor()){
                    continue;
                }

                //init. Liste mit Nullwerten der Größe von Color size
                cspanning.clear();
                for (int i = 0; i < colors.size(); i++) {
                    cspanning.add(null);
                }

                //r = rechte Grenze
                for (int r = 0 ; r < pointsx.size(); r++) {
                    //1. Fall r_x muss größer als linke Grenze p sein
                    if (pointsx.get(r).getX()< pointsx.get(p).getX()){
                        continue;
                    }
                    //2. Fall r_y muss über untere Grenze q sein
                    if (pointsx.get(r).getY()< pointsx.get(q).getY()){
                        continue;
                    }



                    //addiere Grenzpunkte p und q hinzu
                    cspanning.set(colors.indexOf(pointsx.get(p).getColor()),pointsx.get(p).getColor());
                    cspanning.set(colors.indexOf(pointsx.get(q).getColor()),pointsx.get(q).getColor());

                    //setze Farbe an Stelle r
                    cspanning.set(colors.indexOf(pointsx.get(r).getColor()),pointsx.get(r).getColor());

                    //System.out.println("Cspanning: "+cspanning.size() + cspanning);

                    //wenn color Spanning
                    if (!cspanning.contains(null)){
                        //init. Liste mit Nullwerten der Größe von Color size
                        ccomb.clear();
                        for (int i = 0; i < colors.size(); i++) {
                            ccomb.add(null);
                        }

                        //s = obere Grenze
                        for (int s = 0; s<pointsy.size(); s++){
                            //1. Fall s_x muss größer als linke Grenze p sein
                            if (pointsy.get(s).getX()< pointsx.get(p).getX()){
                                continue;
                            }
                            //2. Fall s_y muss über untere Grenze q,p,r sein
                            if (pointsy.get(s).getY()< pointsx.get(q).getY()){
                                continue;
                            }
                            //3. Fall s_x muss kleiner als r_x sein
                            if (pointsy.get(s).getX()> pointsx.get(r).getX()){
                                continue;
                            }


                            //System.out.println("Check4");

                            //setze Farbe in List
                            ccomb.set(colors.indexOf(pointsy.get(s).getColor()),pointsy.get(s).getColor());

                            //System.out.println("Ccomb: "+ccomb.size() + ccomb);

                            //wenn Color Spanning
                            if (!ccomb.contains(null)){
                              /*  System.out.println("p_x: "+ pointsx.get(p).getX());
                                System.out.println("p_y: "+ pointsx.get(q).getY());
                                System.out.println("width: "+ (pointsx.get(r).getX()-pointsx.get(p).getX()));
                                System.out.println("height: "+ (pointsy.get(s).getY()-pointsx.get(q).getY()));*/


                                compareRectangle.setX(pointsx.get(p).getX());
                                compareRectangle.setY(pointsx.get(q).getY());
                                compareRectangle.setWidth(pointsx.get(r).getX()-pointsx.get(p).getX());
                                compareRectangle.setHeight(pointsy.get(s).getY()-pointsx.get(q).getY());

                                //vergleiche nach "area" oder "scope"
                                System.out.println("CompareRect area: "+Rectangle.getArea(compareRectangle)+" Opt Rect area: "+Rectangle.getArea(optimumRectangle));

                                //vergleiche Rechtecke
                                if (Rectangle.getArea(optimumRectangle)==0){
                                    optimumRectangle.setX(compareRectangle.getX());
                                    optimumRectangle.setY(compareRectangle.getY());
                                    optimumRectangle.setWidth(compareRectangle.getWidth());
                                    optimumRectangle.setHeight(compareRectangle.getHeight());

                                }
                                if(Rectangle.compareRectangles(optimumRectangle,compareRectangle, "area")==compareRectangle){
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
        return optimumRectangle;
    }

    public static void execute(CoordPanel panel) {

        if (pointsx.size() >= 2) {
            panel.setRect(algo1());
            System.out.println("New Rectangle");
        }
    }
}