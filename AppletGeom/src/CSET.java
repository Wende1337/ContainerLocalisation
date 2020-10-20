import java.awt.*;
import java.util.*;


//Color Spanning Equilateral Triangular Annulus
public class CSET {


    //get ArrayList of points from Coordpanel, use pointsy and pointsy for sweep
    private static ArrayList<Point> pointsy = CoordPanel.getPoints();
    //empty Triangle for execute function
    static Triangle triangle = new Triangle(null, null);
    //two Variables for the TextField in GUI to get Information of optimal Triangle
    public static int triglobalcircum;
    public static int triglobalarea;
    //For Step by Step Algorithm to synchronize with the Button press
    private static int step;


    public static Triangle AlgorithmCSET() {

        //Algorithm runs 4 times: once for 2 points as wedges, once for 1 points as wedge, this is repeated twice
        //for the base above the wedge, and once for the base below wedge

        //		if List is less than 1
        if (pointsy.size() < 2) {
            return null;
        }
        //sort in x order ascending
        Collections.sort(pointsy, new ComparatorPointY());

        //List for Colors
        ArrayList<Color> colors = new ArrayList<Color>(pointsy.size());
        colors.add(pointsy.get(0).getColor());

        //        get all point colors
        for (int k = 1; k < pointsy.size(); k++) {
            for (int l = 0; l < colors.size(); l++) {
                if (colors.contains(pointsy.get(k).getColor()) == false) {
                    colors.add(pointsy.get(k).getColor());
                }
            }
        }

        //if <2 colors and Algorithm cant run
        if (colors.size() < 2) {
            return null;
        }

        // some parameters for line equations,
        // tan(60) = sqrt(3), tan(120)= -(sqrt(3))

        //Coordinates of Intersection of the two points which are wedge defining
        double[] v_pq = new double[2];
        //Y-Axis section for wedge lines p and q
        double b_p, b_q = 0;
        ArrayList<Point> wedgepoints = new ArrayList<Point>(pointsy.size());
        ArrayList<Color> allwedgecolors = new ArrayList<>(colors.size());
        ArrayList<Color> wedgecolors = new ArrayList<>(colors.size());
        //triangle x and y array for coordinates of the corner points
        int[] trix = new int[3];
        int[] triy = new int[3];
        //temporary placeholder for corner point
        double[] line_p = new double[2];
        double[] line_q = new double[2];
        int[] trixopt = new int[3];
        int[] triyopt = new int[3];

        int triarea;
        int triopt = 2137483647;

        //MIRRORED VERSIONS (2) , THEN NORMAL (2)

        //iterates for each point MIRRORED and 1 Point defining Wedge
        for (int v = 0; v < pointsy.size(); v++) {
            //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
            //compute b_p mit -sqrt(3), b_q with sqrt(3)), because java coordinate origin is mirrored
            b_p = -Math.sqrt(3) * pointsy.get(v).getX() + pointsy.get(v).getY();
            b_q = Math.sqrt(3) * pointsy.get(v).getX() + pointsy.get(v).getY();

            // compute intersection of line equations
            // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
            // for algorithm: l_pq = v_pq[0]
            // v_pq[0] = x         v_pq[1] =y
            v_pq[0] = (b_p - b_q) / (2 * -Math.sqrt(3));
            v_pq[1] = Math.sqrt(3) * (v_pq[0]) + b_p;

            // compute points on both lines with p the right and q the left line, set y and calculate x coords
            line_p[1] = v_pq[1] + 200;
            line_q[1] = v_pq[1] + 200;
            line_p[0] = (line_p[1] - b_p) / Math.sqrt(3);
            line_q[0] = (line_q[1] - b_q) / -Math.sqrt(3);


            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsy.size(); windx++) {


                //because of conversion double to int there may be cases where the Wedgepoint isn't added
                //add manually
                if (windx == v) {
                    wedgepoints.add(pointsy.get(windx));
                    //add color to List to later check if wedge is color spanning
                    if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                        allwedgecolors.add(pointsy.get(windx).getColor());
                    }
                    continue;
                }


                //filter points that lie in wedge with half pane intersection through triangle area computation
                //"Halbebenschnitt" from Module "Algorithmic Geometry"
                if (((pointsy.get(windx).getX() - line_p[0]) * ((pointsy.get(windx).getY() + line_p[1]) / 2) +
                        (v_pq[0] - pointsy.get(windx).getX()) * ((v_pq[1] + pointsy.get(windx).getY()) / 2) +
                        (line_p[0] - v_pq[0]) * ((line_p[1] + v_pq[1]) / 2)) >= 0 &&
                        ((pointsy.get(windx).getX() - v_pq[0]) * ((pointsy.get(windx).getY() + v_pq[1]) / 2) +
                                (line_q[0] - pointsy.get(windx).getX()) * ((line_q[1] + pointsy.get(windx).getY()) / 2) +
                                (v_pq[0] - line_q[0]) * ((v_pq[1] + line_q[1]) / 2)) >= 0 &&
                        (pointsy.get(windx).getY() <= v_pq[1])) {
                    wedgepoints.add(pointsy.get(windx));
                    //add color to List to later check if wedge is color spanning
                    if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                        allwedgecolors.add(pointsy.get(windx).getColor());
                    }
                }
            }


            // Check if Wedge Color Condition is met, reset lists if not
            if (allwedgecolors.size() != colors.size()) {
                allwedgecolors.clear();
                wedgepoints.clear();
                wedgecolors.clear();
                continue;
            }
            allwedgecolors.clear();
            wedgecolors.clear();


            //sweep for the base of the Algorithm
            for (int idx = wedgepoints.size() - 1; idx >= 0; idx--) {
                //add color to color array if the colors is not used so far
                if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                    wedgecolors.add(wedgepoints.get(idx).getColor());
                }
                //check if the base with index idx is color spanning, if so compute Triangle with the found candidates
                // and area and compare to optimum yet found triangle
                if (wedgecolors.size() == colors.size()) {
                    trix[0] = (int) v_pq[0];
                    triy[0] = (int) v_pq[1];
                    triy[1] = wedgepoints.get(idx).getY();
                    triy[2] = wedgepoints.get(idx).getY();
                    trix[1] = (int) ((wedgepoints.get(idx).getY() - b_p) / Math.sqrt(3));
                    trix[2] = (int) ((wedgepoints.get(idx).getY() - b_q) / -Math.sqrt(3));
                    //compute area
                    triarea = Math.abs(((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2);
                    if (triarea <= triopt) {
                        triopt = triarea;
                        trixopt[0] = trix[0];
                        trixopt[1] = trix[1];
                        trixopt[2] = trix[2];
                        triyopt[0] = triy[0];
                        triyopt[1] = triy[1];
                        triyopt[2] = triy[2];
                        wedgecolors.clear();
                        wedgepoints.clear();
                        break;
                    }
                }
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }


        //iterates through a points pair, MIRRORED, 2 points that define wedge
        for (int p = 0; p < pointsy.size(); p++) {
            // x(p) < x(q)
            for (int q = 0; q < pointsy.size(); q++) {
                //if color of p and q are the same
                if (pointsy.get(p).getColor() == pointsy.get(q).getColor()) {
                    continue;
                }
                // if p X-coordinate is lower than q
                if(pointsy.get(p).getX()>=pointsy.get(q).getX()){
                    continue;
                }

                //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
                //compute b_p with -sqrt(3), b_q mit sqrt(3)
                b_p = -Math.sqrt(3) * pointsy.get(p).getX() + pointsy.get(p).getY();
                b_q = Math.sqrt(3) * pointsy.get(q).getX() + pointsy.get(q).getY();

                // compute points of intersection for wedge
                // -tan(60)x +b1 = tan(60)x +b2 <=> 2*tan(60)x +b2 = b1 <=> x = (b1 - b2)/2*tan(60)
                // line l_pq is v_pq[0]
                // v_pq[0] = x         v_pq[1] =y
                v_pq[0] = (b_p - b_q) / (2 * -Math.sqrt(3));
                v_pq[1] = Math.sqrt(3) * (v_pq[0]) + b_p;


                //loop to find Points in Wedge_p,q
                for (int windx = 0; windx < pointsy.size(); windx++) {


                    //because of conversion double to int, wedge defining points may not always be in wedge, add manually
                    if (windx == p) {
                        if (v_pq[1] >= pointsy.get(p).getY()) {
                            wedgepoints.add(pointsy.get(windx));

                            //add color to List to later check if color Spanning
                            if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                                allwedgecolors.add(pointsy.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q) {
                        if (v_pq[1] >= pointsy.get(q).getY()) {

                            wedgepoints.add(pointsy.get(windx));
                            //add color to List to later check if color Spanning
                            if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                                allwedgecolors.add(pointsy.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q && v_pq[1] >= pointsy.get(q).getY()) {
                        wedgepoints.add(pointsy.get(windx));

                        //add color to List to later check if color Spanning
                        if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                            allwedgecolors.add(pointsy.get(windx).getColor());
                        }
                        continue;
                    }

                    //"Halbebenenschnitt" half pane intersection was not working too well so we used line equations
                    // to filter wedgepoints
                    if (pointsy.get(windx).getY() <= v_pq[1] &&
                            pointsy.get(windx).getX() >= (pointsy.get(windx).getY() - b_p) / Math.sqrt(3) &&
                            pointsy.get(windx).getX() <= (pointsy.get(windx).getY() - b_q) / -Math.sqrt(3)) {
                        wedgepoints.add(pointsy.get(windx));


                        //add color to List to check later if Color Spanning
                        if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                            allwedgecolors.add(pointsy.get(windx).getColor());
                        }
                    }
                }


                // reset if not Color Spanning
                if (allwedgecolors.size() != colors.size()) {
                    allwedgecolors.clear();
                    wedgepoints.clear();
                    wedgecolors.clear();
                    continue;
                }

                allwedgecolors.clear();
                wedgecolors.clear();

                //sweep for base of triangle
                for (int idx = wedgepoints.size() - 1; idx >= 0; idx--) {
                    //add color to later check if color spanning
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }

                    //color spanning check, if so create the triangle with found candidates and compare to optimum yet
                    if (wedgecolors.size() == colors.size()) {
                        trix[0] = (int) v_pq[0];
                        triy[0] = (int) v_pq[1];
                        triy[1] = wedgepoints.get(idx).getY();
                        triy[2] = wedgepoints.get(idx).getY();
                        trix[1] = (int) ((wedgepoints.get(idx).getY() - b_p) / Math.sqrt(3));
                        trix[2] = (int) ((wedgepoints.get(idx).getY() - b_q) / -Math.sqrt(3));
                        //compute triangle area
                        triarea = Math.abs(((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2);
                        if (triarea <= triopt) {
                            triopt = triarea;
                            trixopt[0] = trix[0];
                            trixopt[1] = trix[1];
                            trixopt[2] = trix[2];
                            triyopt[0] = triy[0];
                            triyopt[1] = triy[1];
                            triyopt[2] = triy[2];
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


        //iterates for each point, NOT MIRRORED, 1 Wedge defining Point
        for (int v = 0; v < pointsy.size(); v++) {

            //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
            //compute b_p with sqrt(3), b_q mit -(sqrt(3))
            b_p = Math.sqrt(3) * pointsy.get(v).getX() + pointsy.get(v).getY();
            b_q = -Math.sqrt(3) * pointsy.get(v).getX() + pointsy.get(v).getY();

            // compute points of intersection for wedge
            // -tan(60)x +b1 = tan(60)x +b2 <=> 2*tan(60)x +b2 = b1 <=> x = (b1 - b2)/2*tan(60)
            // line l_pq is v_pq[0]
            // v_pq[0] = x         v_pq[1] =y
            v_pq[0] = (-b_p + b_q) / (2 * -Math.sqrt(3));
            v_pq[1] = -Math.sqrt(3) * (v_pq[0]) + b_p;

            // compute points on both lines with p the right and q the left line, set y and calculate x coords
            line_p[1] = v_pq[1] + 200;
            line_q[1] = v_pq[1] + 200;
            line_p[0] = (line_p[1] - b_p) / -Math.sqrt(3);
            line_q[0] = (line_q[1] - b_q) / Math.sqrt(3);


            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsy.size(); windx++) {


                //because of not percise double to int conversion, add wedgepoint manually
                if (windx == v) {
                    wedgepoints.add(pointsy.get(windx));
                    //add color to List to later check if color Spanning
                    if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                        allwedgecolors.add(pointsy.get(windx).getColor());
                    }
                    continue;
                }

                //filter points that lie in wedge with half pane intersection through triangle area computation
                //"Halbebenschnitt" from Module "Algorithmic Geometry"
                if (((pointsy.get(windx).getX() - line_p[0]) * ((pointsy.get(windx).getY() + line_p[1]) / 2) +
                        (v_pq[0] - pointsy.get(windx).getX()) * ((v_pq[1] + pointsy.get(windx).getY()) / 2) +
                        (line_p[0] - v_pq[0]) * ((line_p[1] + v_pq[1]) / 2)) >= 0 &&
                        ((pointsy.get(windx).getX() - v_pq[0]) * ((pointsy.get(windx).getY() + v_pq[1]) / 2) +
                                (line_q[0] - pointsy.get(windx).getX()) * ((line_q[1] + pointsy.get(windx).getY()) / 2) +
                                (v_pq[0] - line_q[0]) * ((v_pq[1] + line_q[1]) / 2)) >= 0 &&
                        (pointsy.get(windx).getY() >= v_pq[1])) {
                    wedgepoints.add(pointsy.get(windx));
                    //add color to List to later check if color Spanning
                    if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                        allwedgecolors.add(pointsy.get(windx).getColor());
                    }
                }
            }



            //color Spanning check
            if (allwedgecolors.size() != colors.size()) {
                allwedgecolors.clear();
                wedgepoints.clear();
                wedgecolors.clear();
                continue;
            }
            allwedgecolors.clear();
            wedgecolors.clear();


            //sweep for base of triangle
            for (int idx = 0; idx < wedgepoints.size(); idx++) {
                //add color to List to later check if color Spanning
                if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                    wedgecolors.add(wedgepoints.get(idx).getColor());
                }
                //color spanning check, if so create the triangle with found candidates and compare to optimum yet
                if (wedgecolors.size() == colors.size()) {
                    trix[0] = (int) v_pq[0];
                    triy[0] = (int) v_pq[1];
                    triy[1] = wedgepoints.get(idx).getY();
                    triy[2] = wedgepoints.get(idx).getY();
                    trix[1] = (int) ((wedgepoints.get(idx).getY() - b_p) / -Math.sqrt(3));
                    trix[2] = (int) ((wedgepoints.get(idx).getY() - b_q) / Math.sqrt(3));
                    //compute area
                    triarea = ((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2;
                    if (triarea <= triopt) {
                        triopt = triarea;
                        trixopt[0] = trix[0];
                        trixopt[1] = trix[1];
                        trixopt[2] = trix[2];
                        triyopt[0] = triy[0];
                        triyopt[1] = triy[1];
                        triyopt[2] = triy[2];
                        wedgecolors.clear();
                        wedgepoints.clear();
                        break;
                    }
                }
            }
            wedgecolors.clear();
            wedgepoints.clear();
        }

        //iterates for each point, NOT MIRRORED, 2 Wedge defining Point
        for (int p = 0; p < pointsy.size(); p++) {
            // x(p) < x(q) zu jedem Zeiptunkt
            for (int q = 0; q < pointsy.size(); q++) {
                //if color of p and q are the same skip
                if (pointsy.get(p).getColor() == pointsy.get(q).getColor()) {
                    continue;
                }
                // if p X-coordinate is lower than q
                if(pointsy.get(p).getX()>=pointsy.get(q).getX()){
                    continue;
                }

                //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
                //compute b_p with sqrt(3), b_q mit -(sqrt(3))
                b_p = Math.sqrt(3) * pointsy.get(p).getX() + pointsy.get(p).getY();
                b_q = -Math.sqrt(3) * pointsy.get(q).getX() + pointsy.get(q).getY();

                // compute points of intersection for wedge
                // -tan(60)x +b1 = tan(60)x +b2 <=> 2*tan(60)x +b2 = b1 <=> x = (b1 - b2)/2*tan(60)
                // line l_pq is v_pq[0]
                // v_pq[0] = x         v_pq[1] =y
                v_pq[0] = (-b_p + b_q) / (2 * -Math.sqrt(3));
                v_pq[1] = -Math.sqrt(3) * (v_pq[0]) + b_p;


                //loop to find Points in Wedge_p,q
                for (int windx = 0; windx < pointsy.size(); windx++) {

                    //because of conversion double to int, wedge defining points may not always be in wedge, add manually
                    if (windx == p) {
                        if (v_pq[1] <= pointsy.get(p).getY()) {
                            wedgepoints.add(pointsy.get(windx));
                            //add color to List to later check if color Spanning
                            if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                                allwedgecolors.add(pointsy.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q) {
                        if (v_pq[1] <= pointsy.get(q).getY()) {

                            wedgepoints.add(pointsy.get(windx));
                            //add color to List to later check if color Spanning
                            if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                                allwedgecolors.add(pointsy.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    if (windx == q && v_pq[1] <= pointsy.get(q).getY()) {
                        wedgepoints.add(pointsy.get(windx));
                        //add color to List to later check if color Spanning
                        if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                            allwedgecolors.add(pointsy.get(windx).getColor());
                        }
                        continue;
                    }

                    //"Halbebenenschnitt" half pane intersection was not working too well so we used line equations
                    // to filter wedgepoints
                    if (pointsy.get(windx).getY() >= v_pq[1] &&
                            pointsy.get(windx).getX() >= (pointsy.get(windx).getY() - b_p) / -Math.sqrt(3) &&
                            pointsy.get(windx).getX() <= (pointsy.get(windx).getY() - b_q) / Math.sqrt(3)) {
                        wedgepoints.add(pointsy.get(windx));


                        //add color to List to check later if Color Spanning
                        if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                            allwedgecolors.add(pointsy.get(windx).getColor());
                        }
                    }
                }


                // reset if not Color Spanning
                if (allwedgecolors.size() != colors.size()) {
                    allwedgecolors.clear();
                    wedgepoints.clear();
                    wedgecolors.clear();
                    continue;
                }
                allwedgecolors.clear();
                wedgecolors.clear();

                //sweep for base of triangle
                for (int idx = 0; idx < wedgepoints.size(); idx++) {
                    //add color to later check if color spanning
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }

                    //color spanning check, if so create the triangle with found candidates and compare to optimum yet
                    if (wedgecolors.size() == colors.size()) {
                        trix[0] = (int) v_pq[0];
                        triy[0] = (int) v_pq[1];
                        triy[1] = wedgepoints.get(idx).getY();
                        triy[2] = wedgepoints.get(idx).getY();
                        trix[1] = (int) ((wedgepoints.get(idx).getY() - b_p) / -Math.sqrt(3));
                        trix[2] = (int) ((wedgepoints.get(idx).getY() - b_q) / Math.sqrt(3));
                        //compute triangle area
                        triarea = ((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2;
                        if (triarea <= triopt) {
                            triopt = triarea;
                            trixopt[0] = trix[0];
                            trixopt[1] = trix[1];
                            trixopt[2] = trix[2];
                            triyopt[0] = triy[0];
                            triyopt[1] = triy[1];
                            triyopt[2] = triy[2];
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


        // if no triangle is found, triopt area stood the same then reset
        if (triopt == 2137483647) {
            return null;
        }

        //use tri opt arrays to get the best triangle
        Triangle tri = new Triangle(trixopt, triyopt);
        //get triangle information for Textfields in GUI
        triglobalcircum = tri.getCircum();
        triglobalarea = tri.getArea();
        return tri;
    }

    //execute for the Container of CSET
    public static void execute(CoordPanel panel) {
        //check if there are enugh points
        if (pointsy.size() >= 2) {
            //count time for Algorithm Information and set Triangle
            long startTime = System.nanoTime();
            triangle = AlgorithmCSET();
            long stopTime = System.nanoTime();
            if (triangle != null) {
                panel.setTri(triangle);

                //algorithm Information
                Layout.circum2.setText(Integer.toString(Math.abs(triangle.getCircum())));
                Layout.area2.setText(Integer.toString(Math.abs(triangle.getArea())));
                Layout.time2.setText(Long.toString(stopTime - startTime));
            }
        }
    }

    public static Triangle AlgorithmCSETstep(CoordPanel panel, int out_step) {

        // SAME ALGORITH AS ABOVE, BUT WEDGE DEFINING POINTS ARE GIVEN AND IT ONLY SWEEPS FOR BASE
        //OPERATIONS ARE THE SAME, FOR COMMENTS TO ALGORITHM LOOK ABOVE

        step = 0;
        panel.emptyTri();
        Triangle tri = new Triangle(null, null);
        tri = AlgorithmCSET();

        // sort point List after y coordinate
        Collections.sort(pointsy, new ComparatorPointY());

        //List for Colors
        ArrayList<Color> colors = new ArrayList<Color>(pointsy.size());
        colors.add(pointsy.get(0).getColor());

        //get all point colors
        for (int k = 1; k < pointsy.size(); k++) {
            for (int l = 0; l < colors.size(); l++) {
                if (colors.contains(pointsy.get(k).getColor()) == false) {
                    colors.add(pointsy.get(k).getColor());
                }
            }
        }

        //if less than 2 colors Algorithm terminates
        if (colors.size() < 2) {
            return null;
        }

        //Rectangle for Optimum and Comparing with Optimum
        Triangle optTri = new Triangle(null, null);
        double b_p, b_q = 0;
        int y_q;


        //sort List y order and initialize Lists
        Collections.sort(pointsy, new ComparatorPointY());
        ArrayList<Point> wedgepoints = new ArrayList<Point>(pointsy.size());
        ArrayList<Color> allwedgecolors = new ArrayList<>(colors.size());
        ArrayList<Color> wedgecolors = new ArrayList<>(colors.size());

        //out_step is the times the button has been pressed

        //reset Step by Step Lines
        if (out_step == 0) {
            panel.setLines(0, null);
            panel.setLines(1, null);
            panel.setLines(2, null);
            return null;
        }

        //2 cases: base above or below wedge

        //above wedge:
        if (tri.getY()[0] >= tri.getY()[1]) {

            b_p = -Math.sqrt(3) * tri.getX()[1] + tri.getY()[1];
            b_q = Math.sqrt(3) * tri.getX()[2] + tri.getY()[2];
            y_q = (int) (-Math.sqrt(3) * 5000 + b_q);

            //draw line of p
            if (out_step == 1) {
                Line pline = new Line(0, (int) b_p, tri.getX()[0], tri.getY()[0]);
                panel.setLines(0, pline);
                return null;
            }
            step++;

            //draw line of q
            if (out_step == 2) {
                Line qline = new Line(5000, y_q, tri.getX()[0], tri.getY()[0]);
                panel.setLines(1, qline);
                return null;
            }
            step++;


            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsy.size(); windx++) {
                //exactly same operations as above

                if (pointsy.get(windx).getY() <= tri.getY()[0] &&
                        pointsy.get(windx).getX() >= (pointsy.get(windx).getY() - b_p) / Math.sqrt(3) &&
                        pointsy.get(windx).getX() <= 1 + (pointsy.get(windx).getY() - b_q) / -Math.sqrt(3)) {
                    wedgepoints.add(pointsy.get(windx));


                    if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                        allwedgecolors.add(pointsy.get(windx).getColor());
                    }
                }
            }


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

                // draw step line that is the sweep line for the base of triangle
                step += 1;
                if (step == out_step) {
                    Line wline = new Line(0, wedgepoints.get(idx).getY(), 5000, wedgepoints.get(idx).getY());
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

        //wedge above base
        if (tri.getY()[0] < tri.getY()[1]) {

            b_p = Math.sqrt(3) * tri.getX()[1] + tri.getY()[1];
            b_q = -Math.sqrt(3) * tri.getX()[2] + tri.getY()[2];
            y_q = (int) (Math.sqrt(3) * 5000 + b_q);

            //print line of p
            if (out_step == 1) {
                Line pline = new Line(0, (int) b_p, tri.getX()[0], tri.getY()[0]);
                panel.setLines(0, pline);
                return null;
            }
            step++;

            //print line of q
            if (out_step == 2) {
                Line qline = new Line(5000, y_q, tri.getX()[0], tri.getY()[0]);
                panel.setLines(1, qline);
                return null;
            }
            step++;

            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsy.size(); windx++) {
                //exactly same operations as above

                if (pointsy.get(windx).getY() >= tri.getY()[0] &&
                        pointsy.get(windx).getX() >= (pointsy.get(windx).getY() - b_p) / -Math.sqrt(3) &&
                        pointsy.get(windx).getX() <= 1 + (pointsy.get(windx).getY() - b_q) / Math.sqrt(3)) {
                    wedgepoints.add(pointsy.get(windx));


                    if (!allwedgecolors.contains(pointsy.get(windx).getColor())) {
                        allwedgecolors.add(pointsy.get(windx).getColor());
                    }
                }
            }


            if (allwedgecolors.size() != colors.size()) {
                allwedgecolors.clear();
                wedgepoints.clear();
                wedgecolors.clear();
                return null;
            }

            allwedgecolors.clear();
            wedgecolors.clear();


            for (int idx = 0; idx < wedgepoints.size(); idx++) {
                if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                    wedgecolors.add(wedgepoints.get(idx).getColor());
                }

                //when color spanning draw step line that is the sweep line for the base of triangle
                step += 1;
                if (step == out_step) {
                    Line wline = new Line(0, wedgepoints.get(idx).getY(), 5000, wedgepoints.get(idx).getY());
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

        //reset outer and inner steps for Step by Step Algorithm
        CSETStepButton.resetOut_Step();
        step = 0;
        //delete lines and draw triangle
        panel.setLines(0, null);
        panel.setLines(1, null);
        panel.setLines(2, null);
        panel.setTri(tri);
        return null;
    }

    public static void resetStep() {
        step = 0;
    }
}

