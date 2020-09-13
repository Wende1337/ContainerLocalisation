import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;


//Color Spanning Equilateral Triangular Annulus
public class CSETA {


    private static List<Point> pointsx = CoordPanel.getPoints();
    static Triangle[] tri_ann = new Triangle[2];
    public static int triglobalcircum;
    public static int triglobalarea;


    public static Triangle[] AlgorithmCSETA() {

        //		if List is less than 1
        if (pointsx.size() < 2) {
            return null;
        }
        //      sort point List after x coordinate
        Collections.sort(pointsx, new ComparatorPointX());

        //List for Colors
        ArrayList<Color> colors = new ArrayList<>();
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

        //ein Paar Parameter wie Steigung berchnen für Geradengleichung, wir brauchen 60° und 120°
        // tan(60) = sqrt(3), tan(120)= -(sqrt(3))

        //Schnittpunkt der Gerade die wir mit dem Punktpaar aufspannen (x,y)
        double[] v_pq = new double[2];
        double b_p, b_q, bp_mid, bq_mid;
        double q_x, mid_x, mid_y;
        double min_d, µ, α=0;

        ArrayList<Point> wedgepoints = new ArrayList<Point>();
        LinkedList<Color> allwedgecolors = new LinkedList<>();
        ArrayList<Color> wedgecolors = new ArrayList<>();

        //initiliaze Distance Array for Annulus compute
        ArrayList<Integer> D = new ArrayList<Integer>(colors.size());

        //Punkte auf den Geraden p und q
        double[] line_p= new double[2];
        double[] line_q= new double[2];

        int[] trix = new int[3];
        int[] triy = new int[3];
        //temporary placeholder for corner point
        int[] trixopt = new int[3];
        int[] triyopt = new int[3];
        //um die zwei Dreiecke zu setzen, Eintrag 0 und 1 für C_out oben corner, 1 und 2 für links etc.. siehe unten für Indizierung.
        int[] tri_ann = new int[6];
        int[] tri_annoptx = new int[3];
        int[] tri_annopty = new int[3];


        int triarea, triannarea, triannulus, width = 0;;
        int triopt = 2137483647;
        //Variablen für Trigonometrie und Annulus
        double b, b1, b2, d, e;


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
                    //make Distance Array size of total colors k
                    for (int i = 0; i < colors.size(); i++) {
                        D.add(null);
                    }

                    //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                    for (int s = 0; s <= idx; s++) {
                        if (wedgepoints.get(s) == pointsx.get(v)){
                            D.set(colors.indexOf(wedgepoints.get(s).getColor()), null);
                            continue;
                        }

                        // Schnittpunkt der Linie durch die Spitze des Dreiecks und Gerade von r ist v_pq[0]
                        //get alpha y coord
                        if(wedgepoints.get(s).getX() <= v_pq[0] ) {
                            α =   -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());

                        } else {
                            α =   Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        }


                        //µ = min distance of top corner to annulus or base to annulus
                        µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs(( α -  v_pq[1] ) / 2));

                        //wenn der Eintrag in D null oder größer ist überschreibe mit mü
                        if ( D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                            min_d = µ;
                            D.set(colors.indexOf(wedgepoints.get(s).getColor()), (int) min_d);
                        }
                    }

                    //wenn die Schleife vorbei ist, wurde D für jede Stelle ausgerechnet d.h. wir müssen nur noch die
                    //Punkte für das Dreieck berechnen und die Fläche abziehen um die Dreiecke zu vergleichen
                    //Annulus: Eintrag 4 ist der obere Eckpunkt, 5 links auf der Base, 6 rechts auf der Base

                    //make null entries 0 so they wont be considered
                    for (int i = 0; i < D.size(); i++) {
                        if (D.get(i) == null) {
                            D.set(i, 0);
                        }
                    }
                    //reset width, might not be reset from former iteration
                    width = 0;
                    //compute total maximum width of annulus
                    for (int k = 0; k < D.size(); k++) {
                        //get maximum D Value
                        if (D.get(k)>= width){
                            width=D.get(k);
                        }
                    }

                    if (width == 0) {
                        D.clear();
                        continue;
                    }

                    D.clear();


                    //xy von Punkt 1 oben in [0,1] , xy von Punkt 1 links unten in [2,3] , xy von Punkt 1 rechts unten in [4,5]
                    tri_ann[0] = (int) v_pq[0];
                    tri_ann[1] =(int)v_pq[1]+width*2;
                    tri_ann[3] = wedgepoints.get(idx).getY()-width;
                    tri_ann[5] = wedgepoints.get(idx).getY()-width;

                    // y=mx+b      b=-mx+y     x=(y-b)/m
                    bp_mid=(int)(Math.sqrt(3)*tri_ann[0]+tri_ann[1]);
                    bq_mid=(int)(-Math.sqrt(3)*tri_ann[0]+tri_ann[1]);
                    tri_ann[2] =(int)((tri_ann[3]-bp_mid)/-Math.sqrt(3));
                    tri_ann[4] =(int)((tri_ann[5]-bq_mid)/Math.sqrt(3));


                    //berechne Fläche und ziehe von Triarea ab
                    triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;

                    triarea = triarea - triannulus;

                    if (triarea <= triopt) {
                        triopt = triarea;
                        trixopt[0] = trix[0];
                        trixopt[1] = trix[1];
                        trixopt[2] = trix[2];
                        triyopt[0] = triy[0];
                        triyopt[1] = triy[1];
                        triyopt[2] = triy[2];
                        tri_annoptx[0] = tri_ann[0];
                        tri_annoptx[1] = tri_ann[2];
                        tri_annoptx[2] = tri_ann[4];
                        tri_annopty[0] = tri_ann[1];
                        tri_annopty[1] = tri_ann[3];
                        tri_annopty[2] = tri_ann[5];


                    }
                }
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }


    //iteriere durch jedes Punktpaar durch
        for (int p = 0; p < pointsx.size(); p++) {
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q = p + 1; q < pointsx.size(); q++) {
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()) {
                    continue;
                }

                //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
                //berechne b_p mit sqrt(3), b_q mit -(sqrt(3))
                b_p = Math.sqrt(3) * pointsx.get(p).getX() + pointsx.get(p).getY();
                b_q = -Math.sqrt(3) * pointsx.get(q).getX() + pointsx.get(q).getY();

                //berechne Schnittpunkt aus Geradengleichungen
                // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
                // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
                //v_pq[0] = x         v_pq[1] =y
                v_pq[0] = (-b_p + b_q) / (2 * -Math.sqrt(3));
                v_pq[1] = -Math.sqrt(3) * (v_pq[0]) + b_p;


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
                if (allwedgecolors.size() != colors.size()) {
                    allwedgecolors.clear();
                    wedgepoints.clear();
                    wedgecolors.clear();
                    continue;
                }
                allwedgecolors.clear();

                for (int idx = 0; idx < wedgepoints.size(); idx++) {
                    //füge Punktfarben zu List hinzu um für Colorspanning check
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }

                    //check if color spanning
                    if (wedgecolors.size() == colors.size()) {
                        trix[0] = (int) v_pq[0];
                        triy[0] = (int) v_pq[1];
                        triy[1] = wedgepoints.get(idx).getY();
                        triy[2] = wedgepoints.get(idx).getY();
                        trix[1] = (int) ((wedgepoints.get(idx).getY() - b_p) / -Math.sqrt(3));
                        trix[2] = (int) ((wedgepoints.get(idx).getY() - b_q) / Math.sqrt(3));
                        //berechne optimale Fläche
                        triarea = ((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2;

                        //make Distance Array size of total colors k
                        for (int i = 0; i < colors.size(); i++) {
                            D.add(null);
                        }

                        //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                        for (int s = 0; s <= idx; s++) {

                            if (wedgepoints.get(s) == pointsx.get(p) || wedgepoints.get(s) == pointsx.get(q)){
                                D.set(colors.indexOf(wedgepoints.get(s).getColor()), null);
                                continue;
                            }


                            // Schnittpunkt der Linie durch die Spitze des Dreiecks und Gerade von r ist v_pq[0]
                            //get alpha y coord
                            if(wedgepoints.get(s).getX() <= v_pq[0] ) {
                                α =   -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());

                            } else {
                                α =   Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                            }


                            //µ = min distance of top corner to annulus or base to annulus
                            µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs(( α -  v_pq[1] ) / 2));

                            //wenn der Eintrag in D null oder größer ist überschreibe mit mü
                            if ( D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                                min_d = µ;
                                D.set(colors.indexOf(wedgepoints.get(s).getColor()), (int) min_d);
                            }
                        }

                        //wenn die Schleife vorbei ist, wurde D für jede Stelle ausgerechnet d.h. wir müssen nur noch die
                        //Punkte für das Dreieck berechnen und die Fläche abziehen um die Dreiecke zu vergleichen
                        //Annulus: Eintrag 4 ist der obere Eckpunkt, 5 links auf der Base, 6 rechts auf der Base

                        //make null entries 0 so they wont be considered
                        for (int i = 0; i < D.size(); i++) {
                            if (D.get(i) == null) {
                                D.set(i, 0);
                            }
                        }
                        //reset width, might not be reset from former iteration
                        width = 0;
                        //compute total maximum width of annulus
                        for (int k = 0; k < D.size(); k++) {
                            //get maximum D Value
                            if (D.get(k)>= width){
                                width=D.get(k);
                            }
                        }

                        if (width == 0) {
                            D.clear();
                            continue;
                        }

                        D.clear();

                        //xy von Punkt 1 oben in [0,1] , xy von Punkt 1 links unten in [2,3] , xy von Punkt 1 rechts unten in [4,5]
                        tri_ann[0] = (int) v_pq[0];
                        tri_ann[1] =(int)v_pq[1]+width*2;
                        tri_ann[3] = wedgepoints.get(idx).getY()-width;
                        tri_ann[5] = wedgepoints.get(idx).getY()-width;

                        // y=mx+b      b=-mx+y     x=(y-b)/m
                        bp_mid=(int)(Math.sqrt(3)*tri_ann[0]+tri_ann[1]);
                        bq_mid=(int)(-Math.sqrt(3)*tri_ann[0]+tri_ann[1]);
                        tri_ann[2] =(int)((tri_ann[3]-bp_mid)/-Math.sqrt(3));
                        tri_ann[4] =(int)((tri_ann[5]-bq_mid)/Math.sqrt(3));


                        //berechne Fläche und ziehe von Triarea ab
                        triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;

                        triarea = triarea - triannulus;

                        if (triarea <= triopt) {
                            triopt = triarea;
                            trixopt[0] = trix[0];
                            trixopt[1] = trix[1];
                            trixopt[2] = trix[2];
                            triyopt[0] = triy[0];
                            triyopt[1] = triy[1];
                            triyopt[2] = triy[2];
                            tri_annoptx[0] = tri_ann[0];
                            tri_annoptx[1] = tri_ann[2];
                            tri_annoptx[2] = tri_ann[4];
                            tri_annopty[0] = tri_ann[1];
                            tri_annopty[1] = tri_ann[3];
                            tri_annopty[2] = tri_ann[5];

                        }
                    }
                }
                wedgecolors.clear();
                wedgepoints.clear();
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }

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

                    //make Distance Array size of total colors k
                    for (int i = 0; i < colors.size(); i++) {
                        D.add(null);
                    }

                    //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                    for (int s = wedgepoints.size()-1; s >= idx; s--) {
                        if (wedgepoints.get(s) == pointsx.get(v)){
                            D.set(colors.indexOf(wedgepoints.get(s).getColor()), null);
                            continue;
                        }

                        // Schnittpunkt der Linie durch die Spitze des Dreiecks und Gerade von r ist v_pq[0]
                        //get alpha y coord
                        if(wedgepoints.get(s).getX() <= v_pq[0] ) {
                            α =   Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        } else {
                            α =   -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        }


                        //µ = min distance of top corner to annulus or base to annulus
                        µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs(( α -  v_pq[1] ) / 2));

                        //wenn der Eintrag in D null oder größer ist überschreibe mit mü
                        if ( D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                            min_d = µ;
                            D.set(colors.indexOf(wedgepoints.get(s).getColor()), (int) min_d);
                        }
                    }

                    //wenn die Schleife vorbei ist, wurde D für jede Stelle ausgerechnet d.h. wir müssen nur noch die
                    //Punkte für das Dreieck berechnen und die Fläche abziehen um die Dreiecke zu vergleichen
                    //Annulus: Eintrag 4 ist der obere Eckpunkt, 5 links auf der Base, 6 rechts auf der Base

                    //make null entries 0 so they wont be considered
                    for (int i = 0; i < D.size(); i++) {
                        if (D.get(i) == null) {
                            D.set(i, 0);
                        }
                    }
                    //reset width, might not be reset from former iteration
                    width = 0;
                    //compute total maximum width of annulus
                    for (int k = 0; k < D.size(); k++) {
                        //get maximum D Value
                        if (D.get(k)>= width){
                            width=D.get(k);
                        }
                    }

                    System.out.println("D: "+ D);

                    if (width == 0) {
                        D.clear();
                        continue;
                    }

                    D.clear();


                    //xy von Punkt 1 oben in [0,1] , xy von Punkt 1 links unten in [2,3] , xy von Punkt 1 rechts unten in [4,5]
                    tri_ann[0] = (int) v_pq[0];
                    tri_ann[1] =(int)v_pq[1]-width*2;
                    tri_ann[3] = wedgepoints.get(idx).getY()+width;
                    tri_ann[5] = wedgepoints.get(idx).getY()+width;

                    // y=mx+b      b=-mx+y     x=(y-b)/m
                    bp_mid=(int)(-Math.sqrt(3)*tri_ann[0]+tri_ann[1]);
                    bq_mid=(int)(Math.sqrt(3)*tri_ann[0]+tri_ann[1]);
                    tri_ann[2] =(int)((tri_ann[3]-bp_mid)/Math.sqrt(3));
                    tri_ann[4] =(int)((tri_ann[5]-bq_mid)/-Math.sqrt(3));


                    //berechne Fläche und ziehe von Triarea ab
                    triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;

                    System.out.println("triarea: "+ triarea + "triannulus: "+ triannulus);

                    triarea = triarea - triannulus;

                    if (triarea <= triopt) {
                        triopt = triarea;
                        trixopt[0] = trix[0];
                        trixopt[1] = trix[1];
                        trixopt[2] = trix[2];
                        triyopt[0] = triy[0];
                        triyopt[1] = triy[1];
                        triyopt[2] = triy[2];
                        tri_annoptx[0] = tri_ann[0];
                        tri_annoptx[1] = tri_ann[2];
                        tri_annoptx[2] = tri_ann[4];
                        tri_annopty[0] = tri_ann[1];
                        tri_annopty[1] = tri_ann[3];
                        tri_annopty[2] = tri_ann[5];


                    }
                }
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }



        //iteriere durch jedes Punktpaar durch, diesmal gespiegelt!
        for (int p = 0; p<pointsx.size(); p++) {
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q = p + 1; q < pointsx.size(); q++) {
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()) {
                    continue;
                }

                //setze Geraden für W_q,p Wedges, y=mx+b <=> b=-mx+y
                //berechne b_p mit sqrt(3), b_q mit -(sqrt(3))
                b_p = -Math.sqrt(3) * pointsx.get(p).getX() + pointsx.get(p).getY();
                b_q = Math.sqrt(3) * pointsx.get(q).getX() + pointsx.get(q).getY();

                //berechne Schnittpunkt aus Geradengleichungen
                // -tan(60)x +b1 = tan(60)x +b2 <=> 2*tan(60)x +b2 = b1 <=> x = (b1 - b2)/2*tan(60)
                // Gerade l_pq hat immer den selben x wert gespeichert in v_pq[0]
                //v_pq[0] = x         v_pq[1] =y
                v_pq[0] = (b_p - b_q) / (2 * -Math.sqrt(3));
                v_pq[1] = Math.sqrt(3) * (v_pq[0]) + b_p;


                //loop to find Points in Wedge_p,q
                for (int windx = 0; windx < pointsx.size(); windx++) {
                    //checke ob y Koordinate größer ist
                    //checke ob es in Kante die p aufspannt rechts davon liegt, also größer ist, y=mx+b <=> x = (y-b)/m
                    //checke ob es in Kante die b aufspannt links davon liegt, also kleiner ist

                    //skip Fall, in dem der Algorithmus den spannenden Wedgepunkt analysiert und checkt ob dieser in Wedge ist
                    //aufgrund Konvertierung und ungenauigkeit von double auf Int ist der Halbebenencheck manchmal zu ungenau
                    if (windx == p) {
                        if (v_pq[1] >= pointsx.get(p).getY()) {
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
                    if (windx == q && v_pq[1] >= pointsx.get(q).getY()) {
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
                    continue;
                }
                allwedgecolors.clear();
                wedgecolors.clear();
                for (int idx = wedgepoints.size() - 1; idx >= 0; idx--) {
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }
                    if (wedgecolors.size() == colors.size()) {
                        trix[0] = (int) v_pq[0];
                        triy[0] = (int) v_pq[1];
                        triy[1] = wedgepoints.get(idx).getY();
                        triy[2] = wedgepoints.get(idx).getY();
                        trix[1] = (int) ((wedgepoints.get(idx).getY() - b_p) / Math.sqrt(3));
                        trix[2] = (int) ((wedgepoints.get(idx).getY() - b_q) / -Math.sqrt(3));
                        //berechne optimale Fläche
                        triarea = Math.abs(((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2);

                        //make Distance Array size of total colors k
                        for (int i = 0; i < colors.size(); i++) {
                            D.add(null);
                        }

                        //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                        for (int s = wedgepoints.size() - 1; s >= idx; s--) {
                            if (wedgepoints.get(s) == pointsx.get(p) || wedgepoints.get(s) == pointsx.get(q)) {
                                D.set(colors.indexOf(wedgepoints.get(s).getColor()), null);
                                continue;
                            }

                            // Schnittpunkt der Linie durch die Spitze des Dreiecks und Gerade von r ist v_pq[0]
                            //get alpha y coord
                            if (wedgepoints.get(s).getX() <= v_pq[0]) {
                                α = Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                            } else {
                                α = -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                            }


                            //µ = min distance of top corner to annulus or base to annulus
                            µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs((α - v_pq[1]) / 2));

                            //wenn der Eintrag in D null oder größer ist überschreibe mit mü
                            if (D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                                min_d = µ;
                                D.set(colors.indexOf(wedgepoints.get(s).getColor()), (int) min_d);
                            }
                        }

                        //wenn die Schleife vorbei ist, wurde D für jede Stelle ausgerechnet d.h. wir müssen nur noch die
                        //Punkte für das Dreieck berechnen und die Fläche abziehen um die Dreiecke zu vergleichen
                        //Annulus: Eintrag 4 ist der obere Eckpunkt, 5 links auf der Base, 6 rechts auf der Base

                        //make null entries 0 so they wont be considered
                        for (int i = 0; i < D.size(); i++) {
                            if (D.get(i) == null) {
                                D.set(i, 0);
                            }
                        }
                        //reset width, might not be reset from former iteration
                        width = 0;
                        //compute total maximum width of annulus
                        for (int k = 0; k < D.size(); k++) {
                            //get maximum D Value
                            if (D.get(k) >= width) {
                                width = D.get(k);
                            }
                        }

                        System.out.println("D: " + D);

                        if (width == 0) {
                            D.clear();
                            continue;
                        }

                        D.clear();


                        //xy von Punkt 1 oben in [0,1] , xy von Punkt 1 links unten in [2,3] , xy von Punkt 1 rechts unten in [4,5]
                        tri_ann[0] = (int) v_pq[0];
                        tri_ann[1] = (int) v_pq[1] - width * 2;
                        tri_ann[3] = wedgepoints.get(idx).getY() + width;
                        tri_ann[5] = wedgepoints.get(idx).getY() + width;

                        // y=mx+b      b=-mx+y     x=(y-b)/m
                        bp_mid = (int) (-Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                        bq_mid = (int) (Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                        tri_ann[2] = (int) ((tri_ann[3] - bp_mid) / Math.sqrt(3));
                        tri_ann[4] = (int) ((tri_ann[5] - bq_mid) / -Math.sqrt(3));


                        //berechne Fläche und ziehe von Triarea ab
                        triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;

                        System.out.println("triarea: " + triarea + "triannulus: " + triannulus);

                        triarea = triarea - triannulus;

                        if (triarea <= triopt) {
                            triopt = triarea;
                            trixopt[0] = trix[0];
                            trixopt[1] = trix[1];
                            trixopt[2] = trix[2];
                            triyopt[0] = triy[0];
                            triyopt[1] = triy[1];
                            triyopt[2] = triy[2];
                            tri_annoptx[0] = tri_ann[0];
                            tri_annoptx[1] = tri_ann[2];
                            tri_annoptx[2] = tri_ann[4];
                            tri_annopty[0] = tri_ann[1];
                            tri_annopty[1] = tri_ann[3];
                            tri_annopty[2] = tri_ann[5];


                        }
                    }
                }
                wedgecolors.clear();
                wedgepoints.clear();
            }
        }



        Triangle[] cseta = new Triangle[2];
        Triangle tri = new Triangle(trixopt, triyopt);
        Triangle ann = new Triangle(tri_annoptx, tri_annopty);

        cseta[0] = tri;
        cseta[1] = ann;

        triglobalcircum = tri.getCircum();
        triglobalarea = tri.getArea() - ann.getArea();


        return cseta;
    }

    public static void execute(CoordPanel panel) {

        if (pointsx.size() >= 2) {
            tri_ann = AlgorithmCSETA();
            panel.setTri_Ann(tri_ann);
            Layout.circum4.setText(Integer.toString(Math.abs(triglobalcircum)));
            Layout.area4.setText(Integer.toString(Math.abs(triglobalarea)));
        }
    }
}