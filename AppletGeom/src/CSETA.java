import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.util.*;


//Color Spanning Equilateral Triangular Annulus
public class CSETA {


    private static ArrayList<Point> pointsx = CoordPanel.getPoints();
    static Triangle[] tri_ann = new Triangle[2];
    public static int triglobalcircum;
    public static int triglobalarea;
    private static int step;


    public static Triangle[] AlgorithmCSETA() {

        //Algorithm runs 4 times: once for 2 points as wedges, once for 1 points as wedge, this is repeated twice
        //for the base above the wedge, and once for the base below wedge


        //		if List is less than 1
        if (pointsx.size() < 2) {
            return null;
        }
        //      sort point List after x coordinate
        Collections.sort(pointsx, new ComparatorPointX());

        //List for Colors
        ArrayList<Color> colors = new ArrayList<>(pointsx.size());
        colors.add(pointsx.get(0).getColor());
        //        get all point colors

        for (int k = 1; k < pointsx.size(); k++) {
            for (int l = 0; l < colors.size(); l++) {
                if (colors.contains(pointsx.get(k).getColor()) == false) {
                    colors.add(pointsx.get(k).getColor());
                }
            }
        }

        //if <2 colors and Algorithm cant run
        if (colors.size() <= 2) {
            return null;
        }

        // some parameters for line equations,
        // tan(60) = sqrt(3), tan(120)= -(sqrt(3))

        //Y-Axis section for wedge lines p and q
        double[] v_pq = new double[2];
        double b_p, b_q, bp_mid, bq_mid;
        double min_d, µ, α, width = 0;

        ArrayList<Point> wedgepoints = new ArrayList<Point>(pointsx.size());
        ArrayList<Color> allwedgecolors = new ArrayList<>(colors.size());
        ArrayList<Color> wedgecolors = new ArrayList<>(colors.size());

        //initiliaze Distance Array for Annulus compute
        ArrayList<Double> D = new ArrayList<Double>(colors.size());

        //points on lines of Wedge p und q
        double[] line_p = new double[2];
        double[] line_q = new double[2];
        //triangle x and y array for coordinates of the corner points
        int[] trix = new int[3];
        int[] triy = new int[3];
        //temporary placeholder for corner point of C_out
        int[] trixopt = new int[3];
        int[] triyopt = new int[3];
        //for triangle C_in that defines the annulus, [0] is x, [1] is y,..-
        int[] tri_ann = new int[6];
        //for C_in same as tri_ann but Triangle obejct require Array Format with 3 cells
        int[] tri_annoptx = new int[3];
        int[] tri_annopty = new int[3];


        int triarea, triannarea, triannulus = 0;
        int triopt = 2137483647;

        //MIRRORED VERSIONS (2) , THEN NORMAL (2)

        //iterates for each point MIRRORED and 1 Point defining Wedge
        for (int v = 0; v < pointsx.size(); v++) {

            //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
            //compute b_p mit -sqrt(3), b_q with sqrt(3)), because java coordinate origin is mirrored
            b_p = -Math.sqrt(3) * pointsx.get(v).getX() + pointsx.get(v).getY();
            b_q = Math.sqrt(3) * pointsx.get(v).getX() + pointsx.get(v).getY();

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
            for (int windx = 0; windx < pointsx.size(); windx++) {


                //because of conversion double to int there may be cases where the Wedgepoint isn't added
                //add manually
                if (windx == v) {
                    wedgepoints.add(pointsx.get(windx));
                    //add color to List to later check if wedge is color spanning
                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                    continue;
                }

                //filter points that lie in wedge with half pane intersection through triangle area computation
                //"Halbebenschnitt" from Module "Algorithmic Geometry"
                if (((pointsx.get(windx).getX() - line_p[0]) * ((pointsx.get(windx).getY() + line_p[1]) / 2) +
                        (v_pq[0] - pointsx.get(windx).getX()) * ((v_pq[1] + pointsx.get(windx).getY()) / 2) +
                        (line_p[0] - v_pq[0]) * ((line_p[1] + v_pq[1]) / 2)) >= 0 &&
                        ((pointsx.get(windx).getX() - v_pq[0]) * ((pointsx.get(windx).getY() + v_pq[1]) / 2) +
                                (line_q[0] - pointsx.get(windx).getX()) * ((line_q[1] + pointsx.get(windx).getY()) / 2) +
                                (v_pq[0] - line_q[0]) * ((v_pq[1] + line_q[1]) / 2)) >= 0 &&
                        (pointsx.get(windx).getY() <= v_pq[1])) {
                    wedgepoints.add(pointsx.get(windx));
                    //add color to List to later check if wedge is color spanning
                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            //sort Wedgepoints in Y-coord ascending order
            Collections.sort(wedgepoints, new ComparatorPointY());


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
                    trix[0] = (int) Math.round(v_pq[0]);
                    triy[0] = (int) Math.round(v_pq[1]);
                    triy[1] = wedgepoints.get(idx).getY();
                    triy[2] = wedgepoints.get(idx).getY();
                    trix[1] = (int) Math.round((wedgepoints.get(idx).getY() - b_p) / Math.sqrt(3));
                    trix[2] = (int) Math.round((wedgepoints.get(idx).getY() - b_q) / -Math.sqrt(3));
                    //compute area
                    triarea = Math.abs(((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2);

                    //make Distance Array size of total colors k
                    for (int i = 0; i < colors.size(); i++) {
                        D.add(null);
                    }

                    //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                    for (int s = wedgepoints.size() - 1; s > idx; s--) {


                        //Point of Intersection of wedge, l_pq = v_pq[0]
                        //get alpha y coord
                        if (wedgepoints.get(s).getX() <= v_pq[0]) {
                            α = Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        } else {
                            α = -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        }


                        //µ = min distance of top corner to annulus or base to annulus
                        µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs((α - v_pq[1]) / 2));

                        //If entry in D is null or bigger than the new entry µ
                        if (D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                            min_d = µ;
                            D.set(colors.indexOf(wedgepoints.get(s).getColor()), min_d);
                        }
                    }

                    //if the loop is terminated all Distances of D are computed, C_in can be created
                    //compute corner coints of Triangle C_in and then compute Area

                    //make null entries 0 so they wont be considered
                    for (int i = 0; i < D.size(); i++) {
                        if (D.get(i) == null) {
                            D.set(i, 0.0);
                        }
                    }

                    //reset width, might not be reset from former iteration
                    width = 0;
                    //compute total maximum width of annulus, so Annulus is color spanning
                    for (int k = 0; k < D.size(); k++) {
                        //get maximum D Value
                        if (D.get(k) >= width) {
                            width = D.get(k);
                        }
                    }

                    // for percision purposes
                    if (width <= 0.01) {
                        D.clear();
                        continue;
                    }
                    D.clear();


                    //C_in Cornerpoints
                    //xy of upper cornerpoint [0,1]
                    //xy of left bottom cornerpoint [2,3]
                    //xy of lower right cornerpoint [4,5]
                    tri_ann[0] = (int) Math.round(v_pq[0]);
                    tri_ann[1] = (int) Math.round(v_pq[1] - (int) width * 2);
                    tri_ann[3] = wedgepoints.get(idx).getY() + (int) Math.round(width);
                    tri_ann[5] = wedgepoints.get(idx).getY() + (int) Math.round(width);

                    // y=mx+b      b=-mx+y     x=(y-b)/m
                    bp_mid = (-Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                    bq_mid = (Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                    tri_ann[2] = (int) Math.round((tri_ann[3] - bp_mid) / Math.sqrt(3));
                    tri_ann[4] = (int) Math.round((tri_ann[5] - bq_mid) / -Math.sqrt(3));


                    //compute C_out area and subtract C_in Area
                    triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;
                    triarea = triarea - triannulus;

                    // if current Annulus Area is smaller then already existing opt. annulus area, set current to opt.
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


        //iterates for each point MIRRORED and 2 Points defining Wedge
        for (int p = 0; p < pointsx.size(); p++) {
            // x(p) < x(q)
            for (int q = p + 1; q < pointsx.size(); q++) {
                //color of p and q are not allowd to be the same
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()) {
                    continue;
                }

                //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
                //compute b_p mit -sqrt(3), b_q with sqrt(3)), because java coordinate origin is mirrored
                b_p = -Math.sqrt(3) * pointsx.get(p).getX() + pointsx.get(p).getY();
                b_q = Math.sqrt(3) * pointsx.get(q).getX() + pointsx.get(q).getY();

                // compute intersection of line equations
                // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
                // for algorithm: l_pq = v_pq[0]
                // v_pq[0] = x         v_pq[1] =y
                v_pq[0] = (b_p - b_q) / (2 * -Math.sqrt(3));
                v_pq[1] = Math.sqrt(3) * (v_pq[0]) + b_p;


                //loop to find Points in Wedge_p,q
                for (int windx = 0; windx < pointsx.size(); windx++) {


                    //because of conversion double to int there may be cases where the Wedgepoint isn't added
                    //add manually
                    if (windx == p) {
                        if (v_pq[1] >= pointsx.get(p).getY()) {
                            wedgepoints.add(pointsx.get(windx));

                            //add color to List to later check if wedge is color spanning
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

                            //add color to List to later check if wedge is color spanning
                            if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                                allwedgecolors.add(pointsx.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }


                    //"Halbebenenschnitt" half pane intersection was not working too well so we used line equations
                    // to filter wedgepoints
                    if (pointsx.get(windx).getY() <= v_pq[1] &&
                            pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / Math.sqrt(3) &&
                            pointsx.get(windx).getX() <= (pointsx.get(windx).getY() - b_q) / -Math.sqrt(3)) {
                        wedgepoints.add(pointsx.get(windx));


                        //add color to List to later check if wedge is color spanning
                        if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                            allwedgecolors.add(pointsx.get(windx).getColor());
                        }
                    }
                }

                //sort Wedgepoints in Y-coord ascending order
                Collections.sort(wedgepoints, new ComparatorPointY());

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
                        trix[0] = (int) Math.round(v_pq[0]);
                        triy[0] = (int) Math.round(v_pq[1]);
                        triy[1] = wedgepoints.get(idx).getY();
                        triy[2] = wedgepoints.get(idx).getY();
                        trix[1] = (int) Math.round((wedgepoints.get(idx).getY() - b_p) / Math.sqrt(3));
                        trix[2] = (int) Math.round((wedgepoints.get(idx).getY() - b_q) / -Math.sqrt(3));
                        //compute area
                        triarea = Math.abs(((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2);

                        //make Distance Array size of total colors k
                        for (int i = 0; i < colors.size(); i++) {
                            D.add(null);
                        }

                        //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                        for (int s = wedgepoints.size() - 1; s > idx; s--) {


                            //Point of Intersection of wedge, l_pq = v_pq[0]
                            //get alpha y coord
                            if (wedgepoints.get(s).getX() <= v_pq[0]) {
                                α = Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                            } else {
                                α = -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                            }


                            //µ = min distance of top corner to annulus or base to annulus
                            µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs((α - v_pq[1]) / 2));

                            //If entry in D is null or bigger than the new entry µ
                            if (D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                                min_d = µ;
                                D.set(colors.indexOf(wedgepoints.get(s).getColor()), min_d);
                            }
                        }

                        //if the loop is terminated all Distances of D are computed, C_in can be created
                        //compute corner coints of Triangle C_in and then compute Area

                        //make null entries 0 so they wont be considered
                        for (int i = 0; i < D.size(); i++) {
                            if (D.get(i) == null) {
                                D.set(i, 0.0);
                            }
                        }
                        //reset width, might not be reset from former iteration
                        width = 0;
                        //compute total maximum width of annulus, so Annulus is color spanning
                        for (int k = 0; k < D.size(); k++) {
                            //get maximum D Value
                            if (D.get(k) >= width) {
                                width = D.get(k);
                            }
                        }

                        // for percision purposes
                        if (width <= 0.01) {
                            D.clear();
                            continue;
                        }

                        D.clear();


                        //C_in Cornerpoints
                        //xy of upper cornerpoint [0,1]
                        //xy of left bottom cornerpoint [2,3]
                        //xy of lower right cornerpoint [4,5]
                        tri_ann[0] = (int) Math.round(v_pq[0]);
                        tri_ann[1] = (int) Math.round(v_pq[1] - width * 2);
                        tri_ann[3] = wedgepoints.get(idx).getY() + (int) Math.round(width);
                        tri_ann[5] = wedgepoints.get(idx).getY() + (int) Math.round(width);

                        // y=mx+b      b=-mx+y     x=(y-b)/m
                        bp_mid = (-Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                        bq_mid = (Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                        tri_ann[2] = (int) Math.round((tri_ann[3] - bp_mid) / Math.sqrt(3));
                        tri_ann[4] = (int) Math.round((tri_ann[5] - bq_mid) / -Math.sqrt(3));


                        //compute C_out area and subtract C_in Area
                        triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;
                        triarea = triarea - triannulus;

                        // if current Annulus Area is smaller then already existing opt. annulus area, set current to opt.
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


        //iterates for each point NOT MIRRORED and 1 Point defining Wedge
        for (int v = 0; v < pointsx.size(); v++) {

            //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
            //compute b_p mit -sqrt(3), b_q with sqrt(3)), because java coordinate origin is mirrored
            b_p = Math.sqrt(3) * pointsx.get(v).getX() + pointsx.get(v).getY();
            b_q = -Math.sqrt(3) * pointsx.get(v).getX() + pointsx.get(v).getY();

            // compute intersection of line equations
            // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
            // for algorithm: l_pq = v_pq[0]
            // v_pq[0] = x         v_pq[1] =y
            v_pq[0] = (-b_p + b_q) / (2 * -Math.sqrt(3));
            v_pq[1] = -Math.sqrt(3) * (v_pq[0]) + b_p;

            // compute points on both lines with p the right and q the left line, set y and calculate x coors
            line_p[1] = v_pq[1] + 200;
            line_q[1] = v_pq[1] + 200;
            line_p[0] = (line_p[1] - b_p) / -Math.sqrt(3);
            line_q[0] = (line_q[1] - b_q) / Math.sqrt(3);


            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsx.size(); windx++) {

                //because of conversion double to int there may be cases where the Wedgepoint isn't added
                //add manually
                if (windx == v) {
                    wedgepoints.add(pointsx.get(windx));
                    //add color to List to later check if wedge is color spanning
                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                    continue;
                }

                //filter points that lie in wedge with half pane intersection through triangle area computation
                //"Halbebenschnitt" from Module "Algorithmic Geometry"
                if (((pointsx.get(windx).getX() - line_p[0]) * ((pointsx.get(windx).getY() + line_p[1]) / 2) +
                        (v_pq[0] - pointsx.get(windx).getX()) * ((v_pq[1] + pointsx.get(windx).getY()) / 2) +
                        (line_p[0] - v_pq[0]) * ((line_p[1] + v_pq[1]) / 2)) >= 0 &&
                        ((pointsx.get(windx).getX() - v_pq[0]) * ((pointsx.get(windx).getY() + v_pq[1]) / 2) +
                                (line_q[0] - pointsx.get(windx).getX()) * ((line_q[1] + pointsx.get(windx).getY()) / 2) +
                                (v_pq[0] - line_q[0]) * ((v_pq[1] + line_q[1]) / 2)) >= 0 &&
                        (pointsx.get(windx).getY() >= v_pq[1])) {
                    wedgepoints.add(pointsx.get(windx));
                    //add color to List to later check if wedge is color spanning
                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            //sort Wedgepoints in Y-coord ascending order
            Collections.sort(wedgepoints, new ComparatorPointY());


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
            for (int idx = 0; idx < wedgepoints.size(); idx++) {
                //add color to color array if the colors is not used so far
                if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                    wedgecolors.add(wedgepoints.get(idx).getColor());
                }

                //check if the base with index idx is color spanning, if so compute Triangle with the found candidates
                // and area and compare to optimum yet found triangle
                if (wedgecolors.size() == colors.size()) {
                    trix[0] = (int) Math.round(v_pq[0]);
                    triy[0] = (int) Math.round(v_pq[1]);
                    triy[1] = wedgepoints.get(idx).getY();
                    triy[2] = wedgepoints.get(idx).getY();
                    trix[1] = (int) Math.round((wedgepoints.get(idx).getY() - b_p) / -Math.sqrt(3));
                    trix[2] = (int) Math.round((wedgepoints.get(idx).getY() - b_q) / Math.sqrt(3));
                    //compute area
                    triarea = Math.abs(((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2);
                    //make Distance Array size of total colors k
                    for (int i = 0; i < colors.size(); i++) {
                        D.add(null);
                    }

                    //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                    for (int s = 0; s < idx; s++) {


                        //Point of Intersection of wedge, l_pq = v_pq[0]
                        //get alpha y coord
                        if (wedgepoints.get(s).getX() <= v_pq[0]) {
                            α = -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        } else {
                            α = Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        }


                        //µ = min distance of top corner to annulus or base to annulus
                        µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs((α - v_pq[1]) / 2));

                        //If entry in D is null or bigger than the new entry µ
                        if (D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                            min_d = µ;
                            D.set(colors.indexOf(wedgepoints.get(s).getColor()), min_d);
                        }
                    }

                    //if the loop is terminated all Distances of D are computed, C_in can be created
                    //compute corner coints of Triangle C_in and then compute Area

                    //make null entries 0 so they wont be considered
                    for (int i = 0; i < D.size(); i++) {
                        if (D.get(i) == null) {
                            D.set(i, 0.0);
                        }
                    }
                    //reset width, might not be reset from former iteration
                    width = 0;
                    //compute total maximum width of annulus, so Annulus is color spanning
                    for (int k = 0; k < D.size(); k++) {
                        //get maximum D Value
                        if (D.get(k) >= width) {
                            width = D.get(k);
                        }
                    }

                    // for percision purposes
                    if (width <= 0.01) {
                        D.clear();
                        continue;
                    }

                    D.clear();


                    //C_in Cornerpoints
                    //xy of upper cornerpoint [0,1]
                    //xy of left bottom cornerpoint [2,3]
                    //xy of lower right cornerpoint [4,5]
                    tri_ann[0] = (int) Math.round(v_pq[0]);
                    tri_ann[1] = (int) Math.round(v_pq[1] + width * 2);
                    tri_ann[3] = wedgepoints.get(idx).getY() - (int) Math.round(width);
                    tri_ann[5] = wedgepoints.get(idx).getY() - (int) Math.round(width);

                    // y=mx+b      b=-mx+y     x=(y-b)/m
                    bp_mid = (Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                    bq_mid = (-Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                    tri_ann[2] = (int) Math.round((tri_ann[3] - bp_mid) / -Math.sqrt(3));
                    tri_ann[4] = (int) Math.round((tri_ann[5] - bq_mid) / Math.sqrt(3));


                    //compute C_out area and subtract C_in Area
                    triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;
                    triarea = triarea - triannulus;

                    // if current Annulus Area is smaller then already existing opt. annulus area, set current to opt.
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


        //iterates for each point NOT MIRRORED and 2 Points defining Wedge
        for (int p = 0; p < pointsx.size(); p++) {
            // x(p) < x(q)
            for (int q = p + 1; q < pointsx.size(); q++) {
                //color of p and q are not allowd to be the same
                if (pointsx.get(p).getColor() == pointsx.get(q).getColor()) {
                    continue;
                }

                //set lines for q,p Wedges, y=mx+b <=> b=-mx+y
                //compute b_p mit -sqrt(3), b_q with sqrt(3)), because java coordinate origin is mirrored
                b_p = Math.sqrt(3) * pointsx.get(p).getX() + pointsx.get(p).getY();
                b_q = -Math.sqrt(3) * pointsx.get(q).getX() + pointsx.get(q).getY();

                // compute intersection of line equations
                // tan(60)x +b1 = -tan(60)x +b2 <=> 2*tan(60)x +b1 = b2 <=> x = (-b1 + b2)/2*tan(60)
                // for algorithm: l_pq = v_pq[0]
                // v_pq[0] = x         v_pq[1] =y
                v_pq[0] = (-b_p + b_q) / (2 * -Math.sqrt(3));
                v_pq[1] = -Math.sqrt(3) * (v_pq[0]) + b_p;

                if (v_pq[1] >= pointsx.get(p).getY() || v_pq[1] >= pointsx.get(q).getY()) {
                    continue;
                }

                //loop to find Points in Wedge_p,q
                for (int windx = 0; windx < pointsx.size(); windx++) {


                    //because of conversion double to int there may be cases where the Wedgepoint isn't added
                    //add manually
                    if (windx == p) {
                        if (v_pq[1] <= pointsx.get(p).getY()) {
                            wedgepoints.add(pointsx.get(windx));

                            //add color to List to later check if wedge is color spanning
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

                            //add color to List to later check if wedge is color spanning
                            if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                                allwedgecolors.add(pointsx.get(windx).getColor());
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }


                    //"Halbebenenschnitt" half pane intersection was not working too well so we used line equations
                    // to filter wedgepoints
                    if (pointsx.get(windx).getY() >= v_pq[1] &&
                            pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3) &&
                            pointsx.get(windx).getX() <= (pointsx.get(windx).getY() - b_q) / Math.sqrt(3)) {
                        wedgepoints.add(pointsx.get(windx));


                        //add color to List to later check if wedge is color spanning
                        if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                            allwedgecolors.add(pointsx.get(windx).getColor());
                        }
                    }
                }

                //sort Wedgepoints in Y-coord ascending order
                Collections.sort(wedgepoints, new ComparatorPointY());

                // Check if Wedge Color Condition is met, reset lists if not
                if (allwedgecolors.size() != colors.size()) {
                    allwedgecolors.clear();
                    wedgepoints.clear();
                    wedgecolors.clear();
                    continue;
                }
                allwedgecolors.clear();

                //sweep for the base of the Algorithm
                for (int idx = 0; idx < wedgepoints.size(); idx++) {
                    //add color to color array if the colors is not used so far
                    if (!wedgecolors.contains(wedgepoints.get(idx).getColor())) {
                        wedgecolors.add(wedgepoints.get(idx).getColor());
                    }

                    //check if the base with index idx is color spanning, if so compute Triangle with the found candidates
                    // and area and compare to optimum yet found triangle
                    if (wedgecolors.size() == colors.size()) {
                        trix[0] = (int) Math.round(v_pq[0]);
                        triy[0] = (int) Math.round(v_pq[1]);
                        triy[1] = wedgepoints.get(idx).getY();
                        triy[2] = wedgepoints.get(idx).getY();
                        trix[1] = (int) Math.round((wedgepoints.get(idx).getY() - b_p) / -Math.sqrt(3));
                        trix[2] = (int) Math.round((wedgepoints.get(idx).getY() - b_q) / Math.sqrt(3));
                        //compute area
                        triarea = Math.abs(((trix[2] - trix[1]) * (triy[1] - triy[0])) / 2);

                        //make Distance Array size of total colors k
                        for (int i = 0; i < colors.size(); i++) {
                            D.add(null);
                        }

                        //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                        for (int s = 0; s < idx; s++) {


                            //Point of Intersection of wedge, l_pq = v_pq[0]
                            //get alpha y coord
                            if (wedgepoints.get(s).getX() <= v_pq[0]) {
                                α = -Math.sqrt(3) * v_pq[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());

                            } else {
                                α = Math.sqrt(3) * v_pq[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                            }


                            //µ = min distance of top corner to annulus or base to annulus
                            µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs((α - v_pq[1]) / 2));

                            //If entry in D is null or bigger than the new entry µ
                            if (D.get(colors.indexOf(wedgepoints.get(s).getColor())) == null || (µ <= D.get(colors.indexOf(wedgepoints.get(s).getColor())))) {
                                min_d = µ;
                                D.set(colors.indexOf(wedgepoints.get(s).getColor()), min_d);
                            }
                        }

                        //if the loop is terminated all Distances of D are computed, C_in can be created
                        //compute corner coints of Triangle C_in and then compute Area

                        //make null entries 0 so they wont be considered
                        for (int i = 0; i < D.size(); i++) {
                            if (D.get(i) == null) {
                                D.set(i, 0.0);
                            }
                        }
                        //reset width, might not be reset from former iteration
                        width = 0;
                        //compute total maximum width of annulus, so Annulus is color spanning
                        for (int k = 0; k < D.size(); k++) {
                            //get maximum D Value
                            if (D.get(k) >= width) {
                                width = D.get(k);
                            }
                        }

                        // for percision purposes
                        if (width <= 0.01) {
                            D.clear();
                            continue;
                        }

                        D.clear();

                        //C_in Cornerpoints
                        //xy of upper cornerpoint [0,1]
                        //xy of left bottom cornerpoint [2,3]
                        //xy of lower right cornerpoint [4,5]
                        tri_ann[0] = (int) Math.round(v_pq[0]);
                        tri_ann[1] = (int) Math.round(v_pq[1] + width * 2);
                        tri_ann[3] = wedgepoints.get(idx).getY() - (int) Math.round(width);
                        tri_ann[5] = wedgepoints.get(idx).getY() - (int) Math.round(width);

                        // y=mx+b      b=-mx+y     x=(y-b)/m
                        bp_mid = (Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                        bq_mid = (-Math.sqrt(3) * tri_ann[0] + tri_ann[1]);
                        tri_ann[2] = (int) Math.round((tri_ann[3] - bp_mid) / -Math.sqrt(3));
                        tri_ann[4] = (int) Math.round((tri_ann[5] - bq_mid) / Math.sqrt(3));


                        //compute C_out area and subtract C_in Area
                        triannulus = (Math.abs(tri_ann[4] - tri_ann[2]) * Math.abs(tri_ann[3] - tri_ann[1])) / 2;
                        triarea = triarea - triannulus;

                        // if current Annulus Area is smaller then already existing opt. annulus area, set current to opt.
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


        //convert into right array types to return annulus that consists of two triangles, (triangle array)
        Triangle[] cseta = new Triangle[2];
        Triangle tri = new Triangle(trixopt, triyopt);
        Triangle ann = new Triangle(tri_annoptx, tri_annopty);

        cseta[0] = tri;
        cseta[1] = ann;

        triglobalcircum = tri.getCircum();
        triglobalarea = tri.getArea() - ann.getArea();


        return cseta;
    }

    //execute for the Container of CSETA
    public static void execute(CoordPanel panel) {
        //check if there are enugh points
        if (pointsx.size() >= 2) {
            //count time for Algorithm Information and set annulus
            long startTime = System.nanoTime();
            tri_ann = AlgorithmCSETA();
            long stopTime = System.nanoTime();
            if (tri_ann != null) {
                panel.setTri_Ann(tri_ann);

                //algorithm Information
                Layout.circum3.setText(Integer.toString(Math.abs(triglobalcircum)));
                Layout.area3.setText(Integer.toString(Math.abs(triglobalarea)));
                Layout.time3.setText(Long.toString(stopTime - startTime));
            }
        }
    }

    public static Triangle[] AlgorithmCSETAstep(CoordPanel panel, int out_step) {

        // SAME ALGORITH AS ABOVE, BUT WEDGE DEFINING POINTS ARE GIVEN AND IT ONLY SWEEPS FOR BASE
        //OPERATIONS ARE THE SAME, FOR COMMENTS TO ALGORITHM LOOK ABOVE

        panel.emptyTri_Ann();
        step = 0;
        panel.emptyTri();
        Triangle[] tri = new Triangle[2];
        tri = AlgorithmCSETA();

        //      sort point List after x coordinate
        Collections.sort(pointsx, new ComparatorPointX());

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

        //if less than 2 colors Algorithm terminates
        if (colors.size() <= 2) {
            return null;
        }

        //Rectangle for Optimum and Comparing with Optimum
        Triangle optTri = new Triangle(null, null);
        double b_p, b_q = 0;
        int y_q;
        //for point of intersection of pair of wedges
        double bp_mid, bq_mid;
        double µ, α = 0;

        ArrayList<Point> wedgepoints = new ArrayList<Point>(pointsx.size());
        ArrayList<Color> allwedgecolors = new ArrayList<>(colors.size());
        ArrayList<Color> wedgecolors = new ArrayList<>(colors.size());


        //same indeces as above
        int[] trix_ann = new int[3];
        int[] triy_ann = new int[3];


        //reset Step by Step Lines
        if (out_step == 0) {
            panel.setLines(0, null);
            panel.setLines(1, null);
            panel.setLines(2, null);
            panel.setLines(3, null);
            return null;
        }

        //2 cases: base above or below wedge

        //base above wedge:
        if (tri[0].getY()[0] >= tri[0].getY()[1]) {

            //for lines that set the wedge
            b_p = -Math.sqrt(3) * tri[0].getX()[1] + tri[0].getY()[1];
            b_q = Math.sqrt(3) * tri[0].getX()[2] + tri[0].getY()[2];
            y_q = (int) Math.round(-Math.sqrt(3) * 5000 + b_q);

            //draw line of p
            if (out_step == 1) {
                Line pline = new Line(0, (int) Math.round(b_p), tri[0].getX()[0], tri[0].getY()[0]);
                panel.setLines(0, pline);
                return null;
            }
            step++;

            //draw line of q
            if (out_step == 2) {
                Line qline = new Line(5000, y_q, tri[0].getX()[0], tri[0].getY()[0]);
                panel.setLines(1, qline);
                return null;
            }
            step++;


            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsx.size(); windx++) {
                //exactly same operations as above

                // add 0.9 and subtract -0.9 for percision purpose, compute wedgepoints
                if (pointsx.get(windx).getY() <= tri[0].getY()[0] &&
                        pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / Math.sqrt(3) - 0.9 &&
                        pointsx.get(windx).getX() <= 0.9 + (pointsx.get(windx).getY() - b_q) / -Math.sqrt(3)) {
                    wedgepoints.add(pointsx.get(windx));


                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            Collections.sort(wedgepoints, new ComparatorPointY());


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
                    panel.setLines(3, null);
                    panel.setTri_annStep(null);
                    step = 0;
                    return null;
                }


                //when color spanning draw annulus
                if (wedgecolors.size() == colors.size()) {

                    //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                    for (int s = wedgepoints.size() - 1; s > idx; s--) {

                        // intersection of line that goes through mid of triangle, l_pq 0 v_pq[0]
                        //get alpha y coord
                        if (wedgepoints.get(s).getX() <= tri[0].getX()[0]) {
                            α = Math.sqrt(3) * tri[0].getX()[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());

                        } else {
                            α = -Math.sqrt(3) * tri[0].getX()[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        }


                        //µ = min distance of top corner to annulus or base to annulus
                        µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs((α - tri[0].getY()[0]) / 2));

                        //if µ is annulus of point that defines p or q
                        if (µ <= 0.01) {
                            continue;
                        }

                        //draw sweep that sweep through the current C_out that is a candidate for the annulus
                        step += 1;
                        if (step == out_step) {
                            Line sline = new Line(0, wedgepoints.get(s).getY(), 5000, wedgepoints.get(s).getY());
                            panel.setLines(3, sline);
                            panel.setTri_annStep(null);
                            step = 0;
                            return null;
                        }

                        //compute annulus as above
                        trix_ann[0] = tri[0].getX()[0];
                        triy_ann[0] = (int) Math.round(tri[0].getY()[0] - µ * 2);
                        triy_ann[1] = wedgepoints.get(idx).getY() + (int) Math.round(µ);
                        triy_ann[2] = wedgepoints.get(idx).getY() + (int) Math.round(µ);

                        // y=mx+b      b=-mx+y     x=(y-b)/m
                        bp_mid = (-Math.sqrt(3) * trix_ann[0] + triy_ann[0]);
                        bq_mid = (Math.sqrt(3) * trix_ann[0] + triy_ann[0]);
                        trix_ann[1] = (int) Math.round((triy_ann[1] - bp_mid) / Math.sqrt(3));
                        trix_ann[2] = (int) Math.round((triy_ann[2] - bq_mid) / -Math.sqrt(3));


                        // draw the annulus candidate
                        step += 1;
                        if (step == out_step) {
                            Triangle ann = new Triangle(trix_ann, triy_ann);
                            panel.setTri_annStep(ann);
                            step = 0;
                            return null;
                        }
                    }
                }
            }
        }

        wedgecolors.clear();
        wedgepoints.clear();


        //base below wedge:
        if (tri[0].getY()[0] < tri[0].getY()[1]) {


            //for lines that set the wedge
            b_p = Math.sqrt(3) * tri[0].getX()[1] + tri[0].getY()[1];
            b_q = -Math.sqrt(3) * tri[0].getX()[2] + tri[0].getY()[2];
            y_q = (int) Math.round((Math.sqrt(3) * 5000 + b_q));

            //draw line of p
            if (out_step == 1) {
                Line pline = new Line(0, (int) Math.round(b_p), tri[0].getX()[0], tri[0].getY()[0]);
                panel.setLines(0, pline);
                return null;
            }
            step++;

            //draw line of q
            if (out_step == 2) {
                Line qline = new Line(5000, y_q, tri[0].getX()[0], tri[0].getY()[0]);
                panel.setLines(1, qline);
                return null;
            }
            step++;

            //loop to find Points in Wedge_p,q
            for (int windx = 0; windx < pointsx.size(); windx++) {
                //exactly same operations as above


                // add 0.9 and subtract -0.9 for percision purpose, compute wedgepoints
                if (pointsx.get(windx).getY() >= tri[0].getY()[0] &&
                        pointsx.get(windx).getX() >= (pointsx.get(windx).getY() - b_p) / -Math.sqrt(3) - 0.9 &&
                        pointsx.get(windx).getX() <= 0.9 + (pointsx.get(windx).getY() - b_q) / Math.sqrt(3)) {
                    wedgepoints.add(pointsx.get(windx));


                    if (!allwedgecolors.contains(pointsx.get(windx).getColor())) {
                        allwedgecolors.add(pointsx.get(windx).getColor());
                    }
                }
            }

            Collections.sort(wedgepoints, new ComparatorPointY());


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

                // draw step line that is the sweep line for the base of triangle
                step += 1;
                if (step == out_step) {
                    Line wline = new Line(0, wedgepoints.get(idx).getY(), 5000, wedgepoints.get(idx).getY());
                    panel.setLines(2, wline);
                    panel.setLines(3, null);
                    panel.setTri_annStep(null);
                    step = 0;
                    return null;
                }


                //when color spanning draw annulus
                if (wedgecolors.size() == colors.size()) {

                    //compute annulus with distances saved in D, clear wedgecolor sizes to check colorspanning
                    for (int s = 0; s < idx; s++) {


                        // intersection of line that goes through mid of triangle, l_pq 0 v_pq[0]
                        //get alpha y coord
                        if (wedgepoints.get(s).getX() <= tri[0].getX()[0]) {
                            α = -Math.sqrt(3) * tri[0].getX()[0] + (Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        } else {
                            α = Math.sqrt(3) * tri[0].getX()[0] + (-Math.sqrt(3) * wedgepoints.get(s).getX() + wedgepoints.get(s).getY());
                        }


                        //µ = min distance of top corner to annulus or base to annulus
                        µ = Math.min(Math.abs(wedgepoints.get(idx).getY() - wedgepoints.get(s).getY()), Math.abs((α - tri[0].getY()[0]) / 2));

                        //if µ is annulus of point that defines p or q
                        if (µ == 0.0) {
                            continue;
                        }

                        //draw sweep that sweep through the current C_out that is a candidate for the annulus
                        step += 1;
                        if (step == out_step) {
                            Line sline = new Line(0, wedgepoints.get(s).getY(), 5000, wedgepoints.get(s).getY());
                            panel.setLines(3, sline);
                            panel.setTri_annStep(null);
                            step = 0;
                            return null;
                        }


                        //compute annulus as above
                        trix_ann[0] = tri[0].getX()[0];
                        triy_ann[0] = (int) Math.round(tri[0].getY()[0] + µ * 2);
                        triy_ann[1] = wedgepoints.get(idx).getY() - (int) µ;
                        triy_ann[2] = wedgepoints.get(idx).getY() - (int) µ;

                        // y=mx+b      b=-mx+y     x=(y-b)/m
                        bp_mid = (Math.sqrt(3) * trix_ann[0] + triy_ann[0]);
                        bq_mid = (-Math.sqrt(3) * trix_ann[0] + triy_ann[0]);
                        trix_ann[1] = (int) Math.round((triy_ann[1] - bp_mid) / -Math.sqrt(3));
                        trix_ann[2] = (int) Math.round((triy_ann[2] - bq_mid) / Math.sqrt(3));

                        // draw the annulus candidate
                        step += 1;
                        if (step == out_step) {
                            Triangle ann = new Triangle(trix_ann, triy_ann);
                            panel.setTri_annStep(ann);
                            step = 0;
                            return null;
                        }

                    }
                }
            }
        }

        wedgecolors.clear();
        wedgepoints.clear();


        //reset outer and inner steps for Step by Step Algorithm
        CSETAStepButton.resetOut_Step();
        step = 0;

        //delete Step lines and draw Annulus
        panel.setLines(0, null);
        panel.setLines(1, null);
        panel.setLines(2, null);
        panel.setLines(3, null);
        panel.setTri_annStep(null);
        panel.setTri_Ann(tri);
        return null;
    }

    public static void resetStep() {
        step = 0;
    }

}

