import java.awt.*;
import java.util.*;
import java.util.List;


//Color Spanning Equilateral Triangular Annulus
public class CSET {


    private static List<Point> pointsx = CoordPanel.getPoints();
    static Triangle triangle = new Triangle(null, null);
    public static int triglobalcircum;
    public static int triglobalarea;
    private static int step;





    public static Triangle AlgorithmCSET() {

        //		if List is less than 1
        if (pointsx.size() < 2) {
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
        //Y-Achsenabschnitt für gerade p und q
        double b_p, b_q =0 ;
        LinkedList<Point> wedgepoints = new LinkedList<Point>();
        LinkedList<Color> allwedgecolors = new LinkedList<>();
        ArrayList<Color> wedgecolors = new ArrayList<>();
        int[] trix = new int[3];
        int[] triy = new int[3];
        //temporary placeholder for corner point
        double[] line_p= new double[2];
        double[] line_q= new double[2];
        int[] trixopt = new int[3];
        int[] triyopt = new int[3];

        int triarea;
        int triopt = 2137483647;

        //GESPIEGELTE VERSION, DANN NORMAL

        //iteriere für jeden Punkt einmal GESPIEGELT
        for (int v = 0; v<pointsx.size(); v++){
            //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
            //berechne b_p mit -sqrt(3), b_q mit sqrt(3)), da das Koordinatensystem Spiegelverkehrt
            b_p = -Math.sqrt(3)*pointsx.get(v).getX()+pointsx.get(v).getY();
            b_q = Math.sqrt(3)*pointsx.get(v).getX()+pointsx.get(v).getY();

            //berechne Schnittpunkt aus Geradengleichungen
            // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
            // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
            //v_pq[0] = x         v_pq[1] =y
            v_pq[0]= (b_p - b_q)/(2*-Math.sqrt(3));
            v_pq[1]= Math.sqrt(3)*(v_pq[0]) + b_p;

            // compute points on both lines with p the right and q the left line, set y and calculate x coors
            line_p[1]=v_pq[1]+ 200;
            line_q[1]=v_pq[1]+ 200;
            line_p[0]=(line_p[1]-b_p)/Math.sqrt(3);
            line_q[0]=(line_q[1]-b_q)/-Math.sqrt(3);


            //loop to find Points in Wedge_p,q
            for (int windx=0; windx < pointsx.size(); windx++) {
                //checke ob y Koordinate größer ist
                //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

                //skip Fall, in dem der Algorithmus den spannenden Wedgepunkt analysiert und checkt ob dieser in Wedge ist
                //aufgrund Konvertierung und ungenauigkeit von double auf Int ist der Halbebenencheck manchmal zu ungenau
                if (windx == v){
                    wedgepoints.add(pointsx.get(windx));
                    //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                    if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                    continue;
                }

                //(windx_x - p_x)*((windx_y+p_y)/2) + (v_pq_x - windx_x)*((v_pq_y+windx_y)/2) + (p_x - v_pq_x)*((p_y+v_pq_y)/2) < 0 Links gerade checkt ob links davon liegt
                //(windx_x - q_x)*((windx_y+q_y)/2) + (v_pq_x - windx_x)*((v_pq_y+windx_y)/2) + (q_x - v_pq_x)*((q_y+v_pq_y)/2) < 0 Links gerade checkt ob links davon liegt
                if (((pointsx.get(windx).getX() - line_p[0])*((pointsx.get(windx).getY()+line_p[1])/2) +
                        (v_pq[0] - pointsx.get(windx).getX())*((v_pq[1]+pointsx.get(windx).getY())/2) +
                        (line_p[0] - v_pq[0])*((line_p[1]+v_pq[1])/2)) >= 0 &&
                        ((pointsx.get(windx).getX() - v_pq[0])*((pointsx.get(windx).getY()+v_pq[1])/2) +
                                (line_q[0] - pointsx.get(windx).getX())*((line_q[1]+pointsx.get(windx).getY())/2) +
                                (v_pq[0] - line_q[0])*((v_pq[1]+ line_q[1])/2)) >= 0 &&
                        (pointsx.get(windx).getY()<= v_pq[1])){
                    wedgepoints.add(pointsx.get(windx));
                    //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                    if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            //sortiere nach Y
            Collections.sort(wedgepoints, new ComparatorPointY());


            // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
            if (allwedgecolors.size() != colors.size()){
                allwedgecolors.clear();
                wedgepoints.clear();
                wedgecolors.clear();
                continue;
            }
            allwedgecolors.clear();
            wedgecolors.clear();


            for (int idx=wedgepoints.size()-1; idx>=0 ; idx-- ){
                if (!wedgecolors.contains(wedgepoints.get(idx).getColor())){
                    wedgecolors.add(wedgepoints.get(idx).getColor());
                }
                if( wedgecolors.size() == colors.size()){
                    trix[0]=(int) v_pq[0];
                    triy[0]=(int) v_pq[1];
                    triy[1]=wedgepoints.get(idx).getY();
                    triy[2]=wedgepoints.get(idx).getY();
                    trix[1]=(int) ((wedgepoints.get(idx).getY() - b_p)/Math.sqrt(3));
                    trix[2]=(int) ((wedgepoints.get(idx).getY() - b_q)/-Math.sqrt(3));
                    //berechne optimale Fläche
                    triarea =   Math.abs(((trix[2]-trix[1]) * (triy[1]-triy[0]))/2);
                    if (triarea <= triopt){
                        triopt = triarea;
                        trixopt[0]= trix[0];
                        trixopt[1]= trix[1];
                        trixopt[2]= trix[2];
                        triyopt[0]= triy[0];
                        triyopt[1]= triy[1];
                        triyopt[2]= triy[2];
                        wedgecolors.clear();
                        wedgepoints.clear();
                        break;
                    }
                }
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }



        //iteriere durch jedes Punktpaar durch, diesmal gespiegelt!
        for (int p = 0; p<pointsx.size(); p++){
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q= p + 1 ; q<pointsx.size(); q++){
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()){
                    continue;
                }

                //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
                //berechne b_p mit sqrt(3), b_q mit -(sqrt(3))
                b_p = -Math.sqrt(3)*pointsx.get(p).getX()+pointsx.get(p).getY();
                b_q = Math.sqrt(3)*pointsx.get(q).getX()+pointsx.get(q).getY();

                //berechne Schnittpunkt aus Geradengleichungen
                // -tan(60)x +b1 = tan(60)x +b2 <=> 2*tan(60)x +b2 = b1 <=> x = (b1 - b2)/2*tan(60)
                // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
                //v_pq[0] = x         v_pq[1] =y
                v_pq[0]= (b_p - b_q)/(2*-Math.sqrt(3));
                v_pq[1]= Math.sqrt(3)*(v_pq[0]) + b_p;


                //loop to find Points in Wedge_p,q
                for (int windx=0; windx < pointsx.size(); windx++) {
                    //checke ob y Koordinate größer ist
                    //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                    //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

                    //skip Fall, in dem der Algorithmus den spannenden Wedgepunkt analysiert und checkt ob dieser in Wedge ist
                    //aufgrund Konvertierung und ungenauigkeit von double auf Int ist der Halbebenencheck manchmal zu ungenau
                    if (windx == p){
                        if( v_pq[1]>= pointsx.get(p).getY()){
                            wedgepoints.add(pointsx.get(windx));

                            //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                            if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                                allwedgecolors.add(pointsx.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q) {
                        if (v_pq[1] >= pointsx.get(q).getY()) {

                            wedgepoints.add(pointsx.get(windx));
                            //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                            if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                                allwedgecolors.add(pointsx.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q && v_pq[1]>= pointsx.get(q).getY()){
                        wedgepoints.add(pointsx.get(windx));

                        //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                        if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                            allwedgecolors.add(pointsx.get(windx).getColor());
                        }
                        continue;
                    }

                    //Halbebenenschnitt hat bei Punktepaar nicht so gut funktioniert wie in Geradengleichung einsetzen.
                    if (pointsx.get(windx).getY() <= v_pq[1] &&
                            pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / Math.sqrt(3) &&
                            pointsx.get(windx).getX() <= (pointsx.get(windx).getY() - b_q) / -Math.sqrt(3)) {
                        wedgepoints.add(pointsx.get(windx));


                        //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                        if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                            allwedgecolors.add(pointsx.get(windx).getColor());
                        }
                    }
                }

                //sortiere nach Y
                Collections.sort(wedgepoints, new ComparatorPointY());

                // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
                if (allwedgecolors.size() != colors.size()){
                    allwedgecolors.clear();
                    wedgepoints.clear();
                    wedgecolors.clear();
                    continue;
                }
                allwedgecolors.clear();
                wedgecolors.clear();
                for (int idx=wedgepoints.size()-1; idx>=0 ; idx-- ){
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())){
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }
                    if( wedgecolors.size() == colors.size()){
                        trix[0]=(int) v_pq[0];
                        triy[0]=(int) v_pq[1];
                        triy[1]=wedgepoints.get(idx).getY();
                        triy[2]=wedgepoints.get(idx).getY();
                        trix[1]=(int) ((wedgepoints.get(idx).getY() - b_p)/Math.sqrt(3));
                        trix[2]=(int) ((wedgepoints.get(idx).getY() - b_q)/-Math.sqrt(3));
                        //berechne optimale Fläche
                        triarea =   Math.abs(((trix[2]-trix[1]) * (triy[1]-triy[0]))/2);
                        if (triarea <= triopt){
                            triopt = triarea;
                            trixopt[0]= trix[0];
                            trixopt[1]= trix[1];
                            trixopt[2]= trix[2];
                            triyopt[0]= triy[0];
                            triyopt[1]= triy[1];
                            triyopt[2]= triy[2];
                            wedgecolors.clear();
                            wedgepoints.clear();
                            break;
                        }
                    }
                }
                wedgecolors.clear();
                wedgepoints.clear();
            }
        }

        //iteriere für jeden Punkt einmal
        for (int v = 0; v<pointsx.size(); v++){
            //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
            //berechne b_p mit -sqrt(3), b_q mit sqrt(3)), da das Koordinatensystem Spiegelverkehrt
            b_p = Math.sqrt(3)*pointsx.get(v).getX()+pointsx.get(v).getY();
            b_q = -Math.sqrt(3)*pointsx.get(v).getX()+pointsx.get(v).getY();

            //berechne Schnittpunkt aus Geradengleichungen
            // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
            // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
            //v_pq[0] = x         v_pq[1] =y
            v_pq[0]= (-b_p + b_q)/(2*-Math.sqrt(3));
            v_pq[1]= -Math.sqrt(3)*(v_pq[0]) + b_p;

            // compute points on both lines with p the right and q the left line, set y and calculate x coors
            line_p[1]=v_pq[1]+ 200;
            line_q[1]=v_pq[1]+ 200;
            line_p[0]=(line_p[1]-b_p)/-Math.sqrt(3);
            line_q[0]=(line_q[1]-b_q)/Math.sqrt(3);


            //loop to find Points in Wedge_p,q
            for (int windx=0; windx < pointsx.size(); windx++) {
                //checke ob y Koordinate größer ist
                //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

                //skip Fall, in dem der Algorithmus den spannenden Wedgepunkt analysiert und checkt ob dieser in Wedge ist
                //aufgrund Konvertierung und ungenauigkeit von double auf Int ist der Halbebenencheck manchmal zu ungenau
                if (windx == v){
                    wedgepoints.add(pointsx.get(windx));
                    //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                    if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                    continue;
                }

                    //(windx_x - p_x)*((windx_y+p_y)/2) + (v_pq_x - windx_x)*((v_pq_y+windx_y)/2) + (p_x - v_pq_x)*((p_y+v_pq_y)/2) < 0 Links gerade checkt ob links davon liegt
                    //(windx_x - q_x)*((windx_y+q_y)/2) + (v_pq_x - windx_x)*((v_pq_y+windx_y)/2) + (q_x - v_pq_x)*((q_y+v_pq_y)/2) < 0 Links gerade checkt ob links davon liegt
                if (((pointsx.get(windx).getX() - line_p[0])*((pointsx.get(windx).getY()+line_p[1])/2) +
                        (v_pq[0] - pointsx.get(windx).getX())*((v_pq[1]+pointsx.get(windx).getY())/2) +
                        (line_p[0] - v_pq[0])*((line_p[1]+v_pq[1])/2)) >= 0 &&
                    ((pointsx.get(windx).getX() - v_pq[0])*((pointsx.get(windx).getY()+v_pq[1])/2) +
                            (line_q[0] - pointsx.get(windx).getX())*((line_q[1]+pointsx.get(windx).getY())/2) +
                            (v_pq[0] - line_q[0])*((v_pq[1]+ line_q[1])/2)) >= 0 &&
                    (pointsx.get(windx).getY()>= v_pq[1])){
                    wedgepoints.add(pointsx.get(windx));
                    //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                    if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            //sortiere nach Y
            Collections.sort(wedgepoints, new ComparatorPointY());


                // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
                if (allwedgecolors.size() != colors.size()){
                    allwedgecolors.clear();
                    wedgepoints.clear();
                    wedgecolors.clear();
                    continue;
                }
                allwedgecolors.clear();
                wedgecolors.clear();


            for (int idx=0; idx<wedgepoints.size(); idx++ ){
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())){
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }
                    if( wedgecolors.size() == colors.size()){
                        trix[0]=(int) v_pq[0];
                        triy[0]=(int) v_pq[1];
                        triy[1]=wedgepoints.get(idx).getY();
                        triy[2]=wedgepoints.get(idx).getY();
                        trix[1]=(int) ((wedgepoints.get(idx).getY() - b_p)/-Math.sqrt(3));
                        trix[2]=(int) ((wedgepoints.get(idx).getY() - b_q)/Math.sqrt(3));
                        //berechne optimale Fläche
                        triarea =   ((trix[2]-trix[1]) * (triy[1]-triy[0]))/2;
                        if (triarea <= triopt){
                            triopt = triarea;
                            trixopt[0]= trix[0];
                            trixopt[1]= trix[1];
                            trixopt[2]= trix[2];
                            triyopt[0]= triy[0];
                            triyopt[1]= triy[1];
                            triyopt[2]= triy[2];
                            wedgecolors.clear();
                            wedgepoints.clear();
                            break;
                        }
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
                v_pq[0]= (-b_p + b_q)/(2*-Math.sqrt(3));
                v_pq[1]= -Math.sqrt(3)*(v_pq[0]) + b_p;



                //loop to find Points in Wedge_p,q
                for (int windx=0; windx < pointsx.size(); windx++) {
                    //checke ob y Koordinate größer ist
                    //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                    //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

                    //skip Fall, in dem der Algorithmus den spannenden Wedgepunkt analysiert und checkt ob dieser in Wedge ist
                    //aufgrund Konvertierung und ungenauigkeit von double auf Int ist der Halbebenencheck manchmal zu ungenau
                    if (windx == p){
                        if( v_pq[1]<= pointsx.get(p).getY()){
                            wedgepoints.add(pointsx.get(windx));
                            //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                            if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                                allwedgecolors.add(pointsx.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q) {
                        if (v_pq[1] <= pointsx.get(q).getY()) {

                            wedgepoints.add(pointsx.get(windx));
                            //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                            if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                                allwedgecolors.add(pointsx.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q && v_pq[1]<= pointsx.get(q).getY()){
                        wedgepoints.add(pointsx.get(windx));
                        //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                        if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                            allwedgecolors.add(pointsx.get(windx).getColor());
                        }
                        continue;
                    }

                    //Halbebenenschnitt hat bei Punktepaar nicht so gut funktioniert wie in Geradengleichung einsetzen.
                    if (pointsx.get(windx).getY() >= v_pq[1] &&
                            pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3) &&
                            pointsx.get(windx).getX() <= (pointsx.get(windx).getY() - b_q) / Math.sqrt(3)) {
                        wedgepoints.add(pointsx.get(windx));


                        //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                        if(!allwedgecolors.contains(pointsx.get(windx).getColor())){
                            allwedgecolors.add(pointsx.get(windx).getColor());
                        }
                    }
                }

                //sortiere nach Y
                Collections.sort(wedgepoints, new ComparatorPointY());

                // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
                if (allwedgecolors.size() != colors.size()){
                    allwedgecolors.clear();
                    wedgepoints.clear();
                    wedgecolors.clear();
                    continue;
                }
                allwedgecolors.clear();
                wedgecolors.clear();
                for (int idx=0; idx<wedgepoints.size(); idx++ ){
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())){
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }
                    if( wedgecolors.size() == colors.size()){

                        trix[0]=(int) v_pq[0];
                        triy[0]=(int) v_pq[1];
                        triy[1]=wedgepoints.get(idx).getY();
                        triy[2]=wedgepoints.get(idx).getY();
                        trix[1]=(int) ((wedgepoints.get(idx).getY() - b_p)/-Math.sqrt(3));
                        trix[2]=(int) ((wedgepoints.get(idx).getY() - b_q)/Math.sqrt(3));
                        //berechne optimale Fläche
                        triarea =   ((trix[2]-trix[1]) * (triy[1]-triy[0]))/2;

                        if (triarea <= triopt){
                            triopt = triarea;
                            trixopt[0]= trix[0];
                            trixopt[1]= trix[1];
                            trixopt[2]= trix[2];
                            triyopt[0]= triy[0];
                            triyopt[1]= triy[1];
                            triyopt[2]= triy[2];
                            wedgecolors.clear();
                            wedgepoints.clear();
                            break;
                        }
                    }
                }
                wedgecolors.clear();
                wedgepoints.clear();
            }
        }


        if (triopt == 2137483647){
            return null;
        }

        Triangle tri = new Triangle(trixopt,triyopt);
        triglobalcircum = tri.getCircum();
        triglobalarea = tri.getArea();
        return tri;
    }

    public static void execute(CoordPanel panel) {

        if (pointsx.size()>=2) {
            triangle=AlgorithmCSET();
            if (triangle!= null){
            panel.setTri(triangle);
            Layout.circum3.setText(Integer.toString(Math.abs(triangle.getCircum())));
            Layout.area3.setText(Integer.toString(Math.abs(triangle.getArea())));
            }
        }
    }

    public static Triangle AlgorithmCSETstep (CoordPanel panel, int out_step) {

        step = 0;
        panel.emptyTri();
        Triangle tri = new Triangle(null, null);
        tri = AlgorithmCSET();

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

        //Rectangle for Optimum and Comparing with Optimum
        Triangle optTri = new Triangle(null, null);
        double b_p, b_q = 0;
        int x_q,y_q;


        //sortieren nach x und nach y
        Collections.sort(pointsx, new ComparatorPointX());
        LinkedList<Point> wedgepoints = new LinkedList<Point>();
        LinkedList<Color> allwedgecolors = new LinkedList<>();
        ArrayList<Color> wedgecolors = new ArrayList<>();

        if (out_step==0) {
            panel.setLines(0, null);
            panel.setLines(1, null);
            panel.setLines(2, null);
            return null;
        }

        //2 Fälle: Dreieck nach oben geöffnet, Dreieck nach unten geöffnet
        //gespiegelt
        if (tri.getY()[0] >= tri.getY()[1]) {


            b_p = -Math.sqrt(3) * tri.getX()[1] + tri.getY()[1];
            b_q = Math.sqrt(3) * tri.getX()[2] + tri.getY()[2];

            y_q=(int) (-Math.sqrt(3) * 5000 + b_q);

            if (out_step==1) {
                Line pline = new Line(0,(int)b_p,tri.getX()[0],tri.getY()[0]);
                panel.setLines(0, pline);
                return null;
            }
            step++;

            if (out_step==2) {
                Line qline = new Line(5000, y_q , tri.getX()[0],tri.getY()[0]);
                panel.setLines(1, qline);
                return null;
            }
            step++;



            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsx.size(); windx++) {
                //checke ob y Koordinate größer ist
                //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

                //skip Fall, in dem der Algorithmus den spannenden Wedgepunkt analysiert und checkt ob dieser in Wedge ist
                //aufgrund Konvertierung und ungenauigkeit von double auf Int ist der Halbebenencheck manchmal zu ungenau


                //Halbebenenschnitt hat bei Punktepaar nicht so gut funktioniert wie in Geradengleichung einsetzen.
                if (pointsx.get(windx).getY() <= tri.getY()[0] &&
                        pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / Math.sqrt(3) &&
                        pointsx.get(windx).getX() <= 1+(pointsx.get(windx).getY() - b_q) / -Math.sqrt(3)) {
                    wedgepoints.add(pointsx.get(windx));


                    //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            //sortiere nach Y
            Collections.sort(wedgepoints, new ComparatorPointY());


            // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
            if (allwedgecolors.size() != colors.size()) {
                allwedgecolors.clear();
                wedgepoints.clear();
                wedgecolors.clear();
                return null;
            }

            allwedgecolors.clear();
            wedgecolors.clear();
            for (int idx = wedgepoints.size() - 1; idx >= 0; idx--) {
                if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                    wedgecolors.add(wedgepoints.get(idx).getColor());
                }
                step += 1;
                if (step == out_step) {
                    Line wline = new Line(0, wedgepoints.get(idx).getY(),5000, wedgepoints.get(idx).getY());
                    panel.setLines(2, wline);
                    step = 0;
                    return null;
                }


                if (wedgecolors.size() == colors.size()) {
                    break;
                    }
                }
            }

        wedgecolors.clear();
        wedgepoints.clear();

        //nach unten geöffnet
        if (tri.getY()[0] < tri.getY()[1]) {


            b_p = Math.sqrt(3) * tri.getX()[1] + tri.getY()[1];
            b_q = -Math.sqrt(3) * tri.getX()[2] + tri.getY()[2];

            y_q=(int) (Math.sqrt(3) * 5000 + b_q);

            if (out_step==1) {
                Line pline = new Line(0,(int)b_p,tri.getX()[0],tri.getY()[0]);
                panel.setLines(0, pline);
                return null;
            }
            step++;

            if (out_step==2) {
                Line qline = new Line(5000, y_q , tri.getX()[0],tri.getY()[0]);
                panel.setLines(1, qline);
                return null;
            }
            step++;

            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsx.size(); windx++) {
                //checke ob y Koordinate größer ist
                //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

                //aufgrund Konvertierung und ungenauigkeit von double auf Int ist der Halbebenencheck manchmal zu ungenau



                //Halbebenenschnitt hat bei Punktepaar nicht so gut funktioniert wie in Geradengleichung einsetzen.
                if (pointsx.get(windx).getY() >= tri.getY()[0] &&
                        pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3) &&
                        pointsx.get(windx).getX() <= 1+(pointsx.get(windx).getY() - b_q) / Math.sqrt(3)) {
                    wedgepoints.add(pointsx.get(windx));


                    //füge Farbe der Liste hinzu um zu gucken ob alle Farben überhaupt in Wedge drin sind
                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            //sortiere nach Y
            Collections.sort(wedgepoints, new ComparatorPointY());


            // Wedge Color-Spanning Abfrage, falls nein gehe zu nächstem Punkt paar
            if (allwedgecolors.size() != colors.size()) {
                allwedgecolors.clear();
                wedgepoints.clear();
                wedgecolors.clear();
                return null;
            }

            allwedgecolors.clear();
            wedgecolors.clear();
            for (int idx=0; idx<wedgepoints.size(); idx++ ){
                if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                    wedgecolors.add(wedgepoints.get(idx).getColor());
                }
                step += 1;
                if (step == out_step) {
                    Line wline = new Line(0, wedgepoints.get(idx).getY(),5000, wedgepoints.get(idx).getY());
                    panel.setLines(2, wline);
                    step = 0;
                    return null;
                }


                if (wedgecolors.size() == colors.size()) {
                    break;
                }
            }
        }

        wedgecolors.clear();
        wedgepoints.clear();

        CSETStepButton.resetOut_Step();
        step=0;
        panel.setLines(0, null);
        panel.setLines(1, null);
        panel.setLines(2, null);
        panel.setTri(tri);
        return null;
    }

    public static void resetStep(){
        step=0;
    }
}

