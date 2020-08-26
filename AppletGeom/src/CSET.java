import java.awt.*;
import java.util.*;
import java.util.List;


//Color Spanning Equilateral Triangular Annulus
public class CSET {


    private static List<Point> pointsx = CoordPanel.getPoints();
    static Triangle triangle = new Triangle(null, null);


    public static Triangle AlgorithmCSET() {

        //		if List is less than 1
        if (pointsx.size() <= 2) {
            return null;
        }
        //      sort point List after x coordinate
        Collections.sort(pointsx, new ComparatorPointX());

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
        if (colors.size()<2){
            return null;
        }

        //ein Paar Parameter wie Steigung berchnen für Geradengleichung, wir brauchen 60° und 120°
        // tan(60) = sqrt(3), tan(120)= -(sqrt(3))

        //Schnittpunkt der Gerade die wir mit dem Punktpaar aufspannen (x,y)
        double[] v_pq = new double[2];
        double b_p, b_q =0 ;
        LinkedList<Point> wedgepoints = new LinkedList<Point>();
        LinkedList<Color> allwedgecolors = new LinkedList<>();
        ArrayList<Color> wedgecolors = new ArrayList<>();
        int[] trix = new int[3];
        int[] triy = new int[3];
        //temporary placeholder for corner point
        int corn_x, corn_y;
        int[] trixopt = new int[3];
        int[] triyopt = new int[3];

        int triarea;
        int triopt = 2137483647;

        //iteriere für jeden Punkt einmal

        for (int p = 0; p<pointsx.size(); p++){



            //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
            //berechne b_p mit sqrt(3), b_q mit -(sqrt(3))
            b_p = Math.sqrt(3)*pointsx.get(p).getX()+pointsx.get(p).getY();
            b_q = -Math.sqrt(3)*pointsx.get(p).getX()+pointsx.get(p).getY();

            //berechne Schnittpunkt aus Geradengleichungen
            // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
            // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
            //v_pq[0] = x         v_pq[1] =y
            v_pq[0]=-(-b_p + b_q)/(2*Math.sqrt(3));
            v_pq[1]= Math.sqrt(3)*(-v_pq[0]) + b_p;

//                    System.out.println("vpr X: " + v_pq[0] +" Y: "+ v_pq[1]);
//                    System.out.println(pointsx.get(p).getY() +pointsx.get(q).getY());
            // Oberster Punkt des Dreiecks
            corn_x=(int) v_pq[0];
            corn_y=(int) v_pq[1];

//                    System.out.println("trix 0 :" + trix[0] + "triy 0: "+ triy[0]);



            //loop to find Points in Wedge_p,q
            for (int windx=0; windx < pointsx.size(); windx++) {
                //checke ob y Koordinate größer ist
                //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

/*                        if ( pointsx.get(windx).getY() > v_pq[1]){
                        System.out.println("Punkt nr. "+windx+" in der Wedge. Y ist niedriger, Check 1 von 3");
                    }
                    if ( pointsx.get(windx).getX() > (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3)){
                        System.out.println("Punkt nr. "+windx+" in der Wedge. X ist größer als linke Gerade, Check 2 von 3");
                    }
                    if ( pointsx.get(windx).getX() < (pointsx.get(windx).getY() - b_q) / Math.sqrt(3)){
                        System.out.println("Punkt nr. "+windx+" in der Wedge. X ist keliner als die rechte Gerade, Check 3 von 3\n");
                    }*/


                if (pointsx.get(windx).getY() >= v_pq[1] &&
                        pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3) &&
                        pointsx.get(windx).getX() <= (pointsx.get(windx).getY() - b_q) / Math.sqrt(3)) {
                    wedgepoints.add(pointsx.get(windx));
                    //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                    if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                        allwedgecolors.add(pointsx.get(windx).getColor());
                        System.out.println("CHeck2 ");
                    }
                }
            }
            //füge temporär die punkte q und p dazu um zu schauen ob Wedge Color-Spanning sein kann
            //sortiere nach Y
            wedgepoints.add(pointsx.get(p));
//                    allwedgecolors.add(pointsx.get(p).getColor());
            Collections.sort(wedgepoints, new ComparatorPointY());

            // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
            if (allwedgecolors.size() != colors.size()){
                System.out.println("Allwedge color size: "+ allwedgecolors.size()+ "colors.size: "+ colors.size());
                System.out.println("not color spanning :( \n");
                allwedgecolors.clear();
                continue;
            }
            System.out.println("Allwedge color size: "+ allwedgecolors.size()+ "colors.size: "+ colors.size());
            allwedgecolors.clear();

            System.out.println("\n Color Spanning! \n");
            System.out.println("corner x 0 : " + corn_x + "corner y 0 : "+ corn_y);

            int count =0;
            System.out.println("Wedgepoint size: "+ wedgepoints.size());
            for (int idx=0; idx<wedgepoints.size(); idx++ ){
                System.out.println("Wedgepoint size: "+wedgepoints.size());
                System.out.println("Wedge color size: "+ wedgecolors.size()+ "colors.size: "+ colors.size());
                System.out.println("Count :"+count++);

                if (wedgecolors.contains(wedgepoints.get(idx).getColor())){
                    continue;
                }
                wedgecolors.add(wedgepoints.get(idx).getColor());

                if(!wedgecolors.contains(null)){
                    trix[0]=corn_x;
                    triy[0]=corn_y;
                    triy[1]=wedgepoints.get(idx).getY();
                    triy[2]=wedgepoints.get(idx).getY();
                    trix[1]=(int) ((wedgepoints.get(idx).getY() - b_p)/-Math.sqrt(3));
                    trix[2]=(int) ((wedgepoints.get(idx).getY() - b_q)/Math.sqrt(3));
                    //berechne optimale Fläche
                    triarea =   ((trix[2]-trix[1]) * (triy[1]-triy[0]))/2;
                    System.out.println("Area des Dreiecks: " + triarea);
                    if (triarea < triopt){
                        triopt = triarea;
                        triyopt=triy;
                        trixopt=trix;
                        System.out.println("Area des Opt Dreiecks: " + triopt);
                    }
                    break;
                }
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }


            //iteriere durch jedes Punktpaar durch
            for (int p = 0; p<pointsx.size(); p++){
                // x(p) < x(q) zu jedem Zeiptunkt
                for (int q= p + 1 ; q<pointsx.size(); q++){
                    if (pointsx.get(p).getColor() == pointsx.get(q).getColor()){
                        continue;
                    }

                    //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
                    //berechne b_p mit sqrt(3), b_q mit -(sqrt(3))
                    b_p = Math.sqrt(3)*pointsx.get(p).getX()+pointsx.get(p).getY();
                    b_q = -Math.sqrt(3)*pointsx.get(q).getX()+pointsx.get(q).getY();

                    //berechne Schnittpunkt aus Geradengleichungen
                    // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
                    // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
                    //v_pq[0] = x         v_pq[1] =y
                    v_pq[0]=-(-b_p + b_q)/(2*Math.sqrt(3));
                    v_pq[1]= Math.sqrt(3)*(-v_pq[0]) + b_p;

//                    System.out.println("vpr X: " + v_pq[0] +" Y: "+ v_pq[1]);
//                    System.out.println(pointsx.get(p).getY() +pointsx.get(q).getY());
                    // Oberster Punkt des Dreiecks
                    corn_x=(int) v_pq[0];
                    corn_y=(int) v_pq[1];

//                    System.out.println("trix 0 :" + trix[0] + "triy 0: "+ triy[0]);



                    //loop to find Points in Wedge_p,q
                    for (int windx=0; windx < pointsx.size(); windx++) {
                        //checke ob y Koordinate größer ist
                        //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                        //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

/*                        if ( pointsx.get(windx).getY() > v_pq[1]){
                            System.out.println("Punkt nr. "+windx+" in der Wedge. Y ist niedriger, Check 1 von 3");
                        }
                        if ( pointsx.get(windx).getX() > (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3)){
                            System.out.println("Punkt nr. "+windx+" in der Wedge. X ist größer als linke Gerade, Check 2 von 3");
                        }
                        if ( pointsx.get(windx).getX() < (pointsx.get(windx).getY() - b_q) / Math.sqrt(3)){
                            System.out.println("Punkt nr. "+windx+" in der Wedge. X ist keliner als die rechte Gerade, Check 3 von 3\n");
                        }*/


                        if (pointsx.get(windx).getY() >= v_pq[1] &&
                                pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3) &&
                                pointsx.get(windx).getX() <= (pointsx.get(windx).getY() - b_q) / Math.sqrt(3)) {
                            wedgepoints.add(pointsx.get(windx));
                            //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                            if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                                allwedgecolors.add(pointsx.get(windx).getColor());
                                System.out.println("CHeck2 ");
                            }
                        }
                    }
                    //füge temporär die punkte q und p dazu um zu schauen ob Wedge Color-Spanning sein kann
                    //sortiere nach Y
                    wedgepoints.add(pointsx.get(p));
//                    allwedgecolors.add(pointsx.get(p).getColor());
                    wedgepoints.add(pointsx.get(q));
//                    allwedgecolors.add(pointsx.get(q).getColor());
                    Collections.sort(wedgepoints, new ComparatorPointY());

                    // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
                    if (allwedgecolors.size() != colors.size()){
                        System.out.println("Allwedge color size: "+ allwedgecolors.size()+ "colors.size: "+ colors.size());
                        System.out.println("not color spanning :( \n");
                        allwedgecolors.clear();
                        continue;
                    }
                    System.out.println("Allwedge color size: "+ allwedgecolors.size()+ "colors.size: "+ colors.size());
                    allwedgecolors.clear();

                    System.out.println("\n Color Spanning! \n");
                    System.out.println("corner x 0 : " + corn_x + "corner y 0 : "+ corn_y);
                    int count = 0;
                    for (int idx=0; idx<wedgepoints.size(); idx++ ){
                        System.out.println("Wedgepoint size: "+wedgepoints.size());
                        System.out.println("Wedge color size: "+ wedgecolors.size()+ "colors.size: "+ colors.size());
                        System.out.println("Count :"+count++);

                        if (wedgecolors.contains(wedgepoints.get(idx).getColor())){
                            continue;
                        }
                        wedgecolors.add(wedgepoints.get(idx).getColor());

                        if(!wedgecolors.contains(null)){
                            trix[0]=corn_x;
                            triy[0]=corn_y;
                            triy[1]=wedgepoints.get(idx).getY();
                            triy[2]=wedgepoints.get(idx).getY();
                            trix[1]=(int) ((wedgepoints.get(idx).getY() - b_p)/-Math.sqrt(3));
                            trix[2]=(int) ((wedgepoints.get(idx).getY() - b_q)/Math.sqrt(3));
                            //berechne optimale Fläche
                            triarea =   ((trix[2]-trix[1]) * (triy[1]-triy[0]))/2;
                            System.out.println("Area des Dreiecks: " + triarea);
                            if (triarea < triopt){
                                triopt = triarea;
                                triyopt=triy;
                                trixopt=trix;
                                System.out.println("Area des Opt Dreiecks: " + triopt);
                                }


                        }
                    }
                    wedgecolors.clear();
                    wedgepoints.clear();
                }
            }
        Triangle tri = new Triangle(trixopt,triyopt);

        return tri;
    }

    public static void execute(CoordPanel panel) {

        if (pointsx.size()>2) {
            triangle=AlgorithmCSET();
            panel.setTri(triangle);
            System.out.println("execute reached");
        }
    }
}
