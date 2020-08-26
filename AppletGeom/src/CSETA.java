import java.awt.*;
import java.util.*;
import java.util.List;


//Color Spanning Equilateral Triangular Annulus
public class CSETA {


    private static List<Point> pointsx = CoordPanel.getPoints();


    public static int[] AlgorithmCSETA() {

        //		if List is empty
        if (pointsx.size() == 0) {
            return null;
        }
        //      sort point List after x coordinate
        Collections.sort(pointsx, new ComparatorPointX());

        //List for Colors
        List<Color> colors = new LinkedList<Color>();
        colors.add(pointsx.get(0).getColor());
        //        get all point colors

        for (int k = 1; k < pointsx.size(); k++) {
            for (int l = 0; l< colors.size(); l++){
                if (colors.contains(pointsx.get(k).getColor())==false){
                    colors.add(pointsx.get(k).getColor());
                }
            }

        //ein Paar Parameter wie Steigung berchnen für Geradengleichung, wir brauchen 60° und 120°
        // tan(60) = sqrt(3), tan(120)= -(sqrt(3))

        //Schnittpunkt der Gerade die wir mit dem Punktpaar aufspannen (x,y)
        double[] v_pq = new double[2];
        double b_p, b_q =0 ;
        LinkedList<Point> wedgepoints = new LinkedList<Point>();

        //iteriere durch jedes Punktpaar durch
        for (int p = 0; p<pointsx.size(); p++){
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q= p+1; q<pointsx.size(); q++){

                //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
                //berechne b_p mit sqrt(3), b_q mit -(sqrt(3))
                b_p = -Math.sqrt(3)*pointsx.get(p).getX()+pointsx.get(p).getY();
                b_q = Math.sqrt(3)*pointsx.get(q).getX()+pointsx.get(q).getY();

                //berechne Schnittpunkt aus Geradengleichungen
                // tan(60)x +b1 = -tan(60) +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
                // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
                //v_pq[0] = x         v_pq[1] =y
                v_pq[0]=(-b_p + b_q)/(2*Math.sqrt(3));
                v_pq[1]= Math.sqrt(3)*v_pq[0] + b_p;

                //loop to find Points in Wedge_p,q
                for (int windx=0; windx < pointsx.size(); windx++) {
                    //checke ob y Koordinate größer ist
                    //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                    //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist
                    if (pointsx.get(windx).getY() > v_pq[1] && pointsx.get(windx).getX() > (pointsx.get(windx).getY() - b_p) / Math.sqrt(3)
                            && pointsx.get(windx).getX() < (pointsx.get(windx).getY() - b_q) / -Math.sqrt(3)) {
                        wedgepoints.add(pointsx.get(windx));
                    }
                }
                //füge temporär die punkte q und p dazu um zu schauen ob Wedge Color-Spanning sein kann
                //sortiere nach Y
                wedgepoints.add(pointsx.get(p));
                wedgepoints.add(pointsx.get(q));
                Collections.sort(wedgepoints, new ComparatorPointY());



                // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
                if (!ColorTestClass.getColorCombBool(wedgepoints, colors.size())){
                    wedgepoints.clear();
                    continue;
                }

                for (int idx=0; idx<wedgepoints.size(); idx++ ){

                }



            }
        }
    }














        return null;
    }


}
