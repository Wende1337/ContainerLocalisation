import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.util.*;
import java.util.List;


//Color Spanning Equilateral Triangular Annulus
public class CSETA {


    private static List<Point> pointsx = CoordPanel.getPoints();
    static Triangle[] tri_ann = new Triangle[2];



    public static Triangle[] AlgorithmCSETA() {

        //		if List is less than 1
        if (pointsx.size() <= 2) {
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
        System.out.println("Colors: "+colors.size());

        //wenn weniger als 2 Farben
        if (colors.size()<2){
            return null;
        }

        //ein Paar Parameter wie Steigung berchnen für Geradengleichung, wir brauchen 60° und 120°
        // tan(60) = sqrt(3), tan(120)= -(sqrt(3))

        //Schnittpunkt der Gerade die wir mit dem Punktpaar aufspannen (x,y)
        double[] v_pq = new double[2];
        double b_p, b_q =0 ;
        double q_x,q_y,r_x,r_y,s_x,s_y;
        double min_d, µ;

        ArrayList<Point> wedgepoints = new ArrayList<Point>();
        LinkedList<Color> allwedgecolors = new LinkedList<>();
        ArrayList<Color> wedgecolors = new ArrayList<>();
        //initiliaze Distance Array for Annulus compute
        ArrayList<Integer> D = new ArrayList<Integer>(colors.size());
        int[] trix = new int[3];
        int[] triy = new int[3];
        //temporary placeholder for corner point
        int[] trixopt = new int[3];
        int[] triyopt = new int[3];
        //um die zwei Dreiecke zu setzen, Eintrag 0 und 1 für C_out oben corner, 1 und 2 für links etc.. siehe unten für Indizierung.
        int[] tri_ann = new int[6];
        int[] tri_annoptx = new int[3];
        int[] tri_annopty = new int[3];


        int triarea;
        int triopt = 2137483647;
        //Variablen für Trigonometrie und Annulus
        int triannulus,width =0;
        double b,b1,b2,d,e;


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

                for (int idx=0; idx<wedgepoints.size(); idx++ ){
                    //füge Punktfarben zu List hinzu um für Colorspanning check
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())){
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }

                    //check if color spanning
                    if( wedgecolors.size() == colors.size()){
                        trix[0]=(int) v_pq[0];
                        triy[0]=(int) v_pq[1];
                        triy[1]=wedgepoints.get(idx).getY();
                        triy[2]=wedgepoints.get(idx).getY();
                        trix[1]=(int) ((wedgepoints.get(idx).getY() - b_p)/-Math.sqrt(3));
                        trix[2]=(int) ((wedgepoints.get(idx).getY() - b_q)/Math.sqrt(3));
                        //berechne optimale Fläche
                        triarea =   ((trix[2]-trix[1]) * (triy[1]-triy[0]))/2;

                        System.out.println("Triarea: "+ triarea);

                        //make DIstance Array size of total colors k
                        for (int i = 0; i < colors.size(); i++) {
                            D.add(null);
                        }

                        //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                        wedgecolors.clear();
                        for (int r=0; r<=idx ; r++){
                            for (int s=0; s< r ; s++){
                                //rechne Punkt q aus, Schnittpunkt der Linie druch die SPitze des Dreiecks und gerade von r
                                q_x= v_pq[0];
                                q_y= Math.sqrt(3)*wedgepoints.get(r).getX()+b_p;
                                r_x=wedgepoints.get(r).getX();
                                r_y=wedgepoints.get(r).getY();
                                s_x=wedgepoints.get(s).getX();
                                s_y=wedgepoints.get(s).getY();



                                //test auf Halbebenenschnitt und y Höhe für Punkt s, wenn ja füge zu Wedgecolors hinzu
                                //(s_x-r_x)*((s_y-r_x)/2)+(q_x-r_x)*((q_y-r_x)/2)+(r_x-q_x)*((r_y-q_x)/2) Links gerade checkt ob links davon liegt
                                //(s_x-r_x)*((s_y-r_x)/2)+(q_x-r_x)*((q_y-r_x)/2)+(r_x-q_x)*((r_y-q_x)/2) >= für links oder auf geraden
                                //selbe für die rechte gerade nur r_x=(r_x+2*(q_x-r_x))
                                if ((s_x-r_x)*((s_y-r_x)/2)+(q_x-r_x)*((q_y-r_x)/2)+(r_x-q_x)*((r_y-q_x)/2)>0 ||
                                        ((s_x-(r_x+2*(q_x-r_x)))*((s_y-(r_x+2*(q_x-r_x)))/2)+(q_x-(r_x+2*(q_x-r_x)))*((q_y-(r_x+2*(q_x-r_x)))/2)+((r_x+2*(q_x-r_x))-q_x)*((r_y-q_x)/2)< 0)){
                                    wedgecolors.add(wedgepoints.get(s).getColor());
                                }

                                // wenn color spanning berechne Distanz und speichere Einträge für die entsprechende Farbe in D
                                if (wedgecolors.size()==colors.size()){
                                    System.out.println("D size: "+ D.size());
                                    System.out.println("width annulus über basis: "+ (wedgepoints.get(r).getY()-wedgepoints.get(s).getY()));
                                    System.out.println("width annulus über top corner : "+ ((int)q_y-(int)v_pq[1])/2);
                                    System.out.println("Wedgepoint index in List D: "+wedgepoints.get(r).getColor());

                                    //µ = min distance of top corner to annulus or base to annulus
                                    µ=Math.min((wedgepoints.get(r).getY()-wedgepoints.get(s).getY()),((int)q_y-(int)v_pq[1])/2);
                                    System.out.println("µ : "+ µ);
                                    if (D.get(colors.indexOf(wedgepoints.get(r).getColor()))==null){
                                        min_d= µ;
                                    } else {
                                        min_d=Math.min(µ,D.get(colors.indexOf(wedgepoints.get(r).getColor())));
                                    }
                                    D.set(colors.indexOf(wedgepoints.get(r).getColor()), (int)min_d);
                                }
                            }
                        }
                        //wenn die Schleife vorbei ist, wurde D für jede Stelle ausgerechnet d.h. wir müssen nur noch die
                        //Punkte für das Dreieck berechnen und die Fläche abziehen um die Dreiecke zu vergleichen
                        //Annulus: Eintrag 4 ist der obere Eckpunkt, 5 links auf der Base, 6 rechts auf der Base
                        System.out.println("D: "+ D );

                        //make null entries 0 so they wont be considered
                        for (int i = 0; i < D.size(); i++) {
                            if (D.get(i) == null){
                                D.set(i, 0);
                            }
                        }
                        //reset width, might not be reset from former iteration
                        width=0;
                        //compute total maximum width of annulus
                        for (int k=0; k< D.size(); k++){
                            width=Math.max(D.get(k),width);
                        }
                        if (width==0){
                            D.clear();
                            continue;
                        }

                        D.clear();

                        System.out.println("Width: " + width);
                        //berechne Trigonometrisch oberen Eckpunkt
                        b1=Math.sin(30)*width;
                        d=Math.sin(60)*width;
                        b2=Math.tan(60)/d;
                        b=b1+b2;
                        //Reihenfolge ist x_1,y_1,x_2,y_2,x_3,y_3
                        /*tri_ann[0]= corn_x;
                        tri_ann[1]= corn_y+width*2;
                        //berechne linken untereren und rechten unteren Eckpunkt aus Geradegleichung, x= (y-b)/m
                        tri_ann[2]=(int)((wedgepoints.get(idx).getY()-width-b_p)/Math.sqrt(3));
                        tri_ann[3]=wedgepoints.get(idx).getY()-width;
                        tri_ann[4]=(int)((wedgepoints.get(idx).getY()-width-b_q)/-Math.sqrt(3));
                        tri_ann[5]=wedgepoints.get(idx).getY()-width;
*/
                        tri_ann[0]=(int) v_pq[0];
                        tri_ann[1]= (int)v_pq[1] + width;
                        //for other cornerpoints use pythagoras: a^2=b^2+b^2 <=> a^2= b^4 <=> b= sqrt(a)
                        tri_ann[2]= trix[1]+(int)(Math.sqrt(3)*(width));
                        System.out.println("Tri_ann[2]: "+ tri_ann[2]+ "trix: "+ trix[1]+"Hy: "+ (int)(Math.sqrt(3)*(width*2)));
                        tri_ann[3]= triy[1]-(int)(Math.sqrt(3)*(width));
                        tri_ann[4]= trix[2]-(int)(Math.sqrt(3)*(width));
                        tri_ann[5]= triy[2]-(int)(Math.sqrt(3)*(width));


                        //berechne Fläche und ziehe von Triarea ab
                        triannulus=((tri_ann[4]-tri_ann[2]) * (tri_ann[3]-tri_ann[1]))/2;

                        System.out.println("Triannulus area: "+ triannulus);
                        triarea=triarea-triannulus;

                        if (triarea <= triopt){
                            System.out.println("Triarea: " +triarea+ "Triopt: " +triopt);
                            triopt = triarea;
                            trixopt[0]= trix[0];
                            trixopt[1]= trix[1];
                            trixopt[2]= trix[2];
                            triyopt[0]= triy[0];
                            triyopt[1]= triy[1];
                            triyopt[2]= triy[2];
                            tri_annoptx[0]=tri_ann[0];
                            tri_annoptx[1]=tri_ann[2];
                            tri_annoptx[2]=tri_ann[4];
                            tri_annopty[0]=tri_ann[1];
                            tri_annopty[1]=tri_ann[3];
                            tri_annopty[2]=tri_ann[5];

                            for (int i =0; i<=2; i++){
                                System.out.println("Trix nr. "+i+ " mit Koordinate: "+ trixopt[i]+ " Trixann nr. "+i+ " mit Koordinate: "+ tri_annoptx[i]);
                                System.out.println("Triy nr. "+i+ " mit Koordinate: "+ triyopt[i]+ " Triyann nr. "+i+ " mit Koordinate: "+ tri_annopty[i]);

                            }

                            wedgecolors.clear();
                            wedgepoints.clear();
                        }
                    }
                }
                wedgecolors.clear();
                wedgepoints.clear();
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }

        System.out.println("Tri_ann length: "+ tri_annoptx.length +" "+tri_annopty.length);
        Triangle[] cseta =  new Triangle[2];
        Triangle tri = new Triangle(trixopt,triyopt);
        Triangle ann = new Triangle(tri_annoptx,tri_annopty);

        cseta[0]=tri;
        cseta[1]=ann;



        return cseta;
    }

    public static void execute(CoordPanel panel) {

        if (pointsx.size()>2) {
            tri_ann=AlgorithmCSETA();
            if (tri_ann == null) {
                System.out.println("Annulus null");
            } else {
                System.out.println("Annulus not Null");
            }
            panel.setTri_Ann(tri_ann);
        }
    }
}