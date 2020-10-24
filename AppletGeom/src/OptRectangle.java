import java.awt.*;
import java.util.*;

public class OptRectangle {


    //Two Lists for point sweep, ascending, pointsx gets poin
    private static ArrayList<Point> pointsx = CoordPanel.getPoints();
    private static ArrayList<Point> pointsy = new ArrayList<Point>(pointsx.size());
    //For Step Algorithm
    private static int step;


    public static Rectangle algo1() {

        //bugfixing for Step Algorithm
        step = 0;

        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle = new Rectangle(0, 0, 0, 0);
        Rectangle currentRectangle = new Rectangle(0, 0, 0, 0);

        //sort in x order ascending
        Collections.sort(pointsx, new ComparatorPointX());

        //create y List out of x List and sort ascending in y order
        for (int i = 0; i < pointsx.size(); i++) {
            pointsy.add(pointsx.get(i));
        }
        Collections.sort(pointsy, new ComparatorPointY());

        //List for Colors
        ArrayList<Color> colors = new ArrayList<Color>(pointsx.size());
        colors.add(pointsx.get(0).getColor());
        //get all point colors
        for (int k = 1; k < pointsx.size(); k++) {
            for (int l = 0; l < colors.size(); l++) {
                if (colors.contains(pointsx.get(k).getColor()) == false) {
                    colors.add(pointsx.get(k).getColor());
                }
            }
        }

        //List that counts color spanning status of sweep in x first and then y order
        ArrayList<Color> cspanning = new ArrayList<Color>(colors.size());
        ArrayList<Color> ccomb = new ArrayList<Color>(colors.size());
        Color s_color;


        //If less than 2 colors, can't run Algorithm
        if (colors.size() < 2) {
            colors.clear();
            ccomb.clear();
            pointsy.clear();
            return null;
        }

        //iteration through every two pairs , p= left-, q= upper bound
        for (int p = 0; p < pointsx.size(); p++) {
            for (int q = 0; q < pointsy.size(); q++) {
                // catch several cases that can't set a upper left corner

                // if left corner upper bound is lower than left bound skip
                if (pointsy.get(q).getY() > pointsx.get(p).getY()) {
                    continue;
                }

                // if left bound is right of upper bound skip
                if (pointsy.get(q).getX() < pointsx.get(p).getX()) {
                    continue;
                }

                //if color of both points are the same skip
                if (pointsx.get(p).getColor() == pointsy.get(q).getColor()) {
                    continue;
                }

                //init. List with null in size of color size
                cspanning.clear();
                for (int i = 0; i < colors.size(); i++) {
                    cspanning.add(null);
                }

                //initialize ending size to skip unnecessary sweeps to the right
                int s_ending_Size = pointsy.size();
                s_color = null;
                //r = right bound
                for (int r = p; r < pointsx.size(); r++) {
                    //r must be to the right of the left bound, else skip
                    if (pointsx.get(r).getX() <= pointsx.get(p).getX()) {
                        continue;
                    }
                    //r must be lower than upper bound else skip
                    if (pointsx.get(r).getY() < pointsy.get(q).getY()) {
                        continue;
                    }


                    //add p and q to colors manually
                    cspanning.set(colors.indexOf(pointsx.get(p).getColor()), pointsx.get(p).getColor());
                    cspanning.set(colors.indexOf(pointsy.get(q).getColor()), pointsy.get(q).getColor());

                    //add color of r to later check if color spanning
                    cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

                    //right sweep point color must be the latest lower bound color or else it is not necessary to check
                    if (s_color != null && pointsx.get(r).getColor() != s_color) {
                        continue;
                    }

                    //if color spanning
                    if (!cspanning.contains(null)) {
                        //init. List with null in size of color size
                        ccomb.clear();
                        for (int i = 0; i < colors.size(); i++) {
                            ccomb.add(null);
                        }

                        //upper bound s sweep
                        for (int s = q; s < s_ending_Size; s++) {
                            //s must be left to left boundary
                            if (pointsy.get(s).getX() < pointsx.get(p).getX()) {
                                continue;
                            }
                            //s must be higher than upper boundary
                            if (pointsy.get(s).getY() < pointsy.get(q).getY()) {
                                continue;
                            }
                            //s must be left to right boundary
                            if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                                continue;
                            }

                            //add color of s to color spanning array
                            ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                            //if color spanning
                            if (!ccomb.contains(null)) {
                                //set ending size for s Algorithm so it only gets smaller
                                s_ending_Size = s;
                                //set color of color spanning s for sweep to the right
                                s_color = pointsy.get(s).getColor();


                                //current rectangle to compare with global rectangle
                                currentRectangle.setX(pointsx.get(p).getX());
                                currentRectangle.setY(pointsy.get(q).getY());
                                currentRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                                currentRectangle.setHeight(pointsy.get(s).getY() - pointsy.get(q).getY());


                                //compare rectangle area to find optimum rectangle
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
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }


        //iteration through every one par that defines a corner , p= left- and upper bound
        for (int p = 0; p < pointsx.size(); p++) {

            //init. List with null in size of color size
            cspanning.clear();
            for (int i = 0; i < colors.size(); i++) {
                cspanning.add(null);
            }

            //initialize ending size to skip unnecessary sweeps to the right
            int s_ending_Size = pointsy.size();
            s_color = null;
            //r = right bound
            for (int r = p; r < pointsx.size(); r++) {
                //r must be to the right of the left bound, else skip
                if (pointsx.get(r).getX() < pointsx.get(p).getX()) {
                    continue;
                }
                //r must be lower than upper bound else skip
                if (pointsx.get(r).getY() < pointsx.get(p).getY()) {
                    continue;
                }


                //add p to colors manually
                cspanning.set(colors.indexOf(pointsx.get(p).getColor()), pointsx.get(p).getColor());

                //add color of r to later check if color spanning
                cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());

                //right sweep point color must be the latest lower bound color or else it is not necessary to check
                if (s_color != null && pointsx.get(r).getColor() != s_color) {
                    continue;
                }

                //if color spanning
                if (!cspanning.contains(null)) {
                    //init. List with null in size of color size
                    ccomb.clear();
                    for (int i = 0; i < colors.size(); i++) {
                        ccomb.add(null);
                    }

                    //upper bound s sweep
                    for (int s = 0; s < s_ending_Size; s++) {
                        //s must be left to left boundary
                        if (pointsy.get(s).getX() < pointsx.get(p).getX()) {
                            continue;
                        }
                        //s must be higher than upper boundary
                        if (pointsy.get(s).getY() < pointsx.get(p).getY()) {
                            continue;
                        }
                        //s must be left to right boundary
                        if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                            continue;
                        }


                        //add color of s to color spanning array
                        ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());

                        //if color spanning
                        if (!ccomb.contains(null)) {
                            //set ending size for s Algorithm so it only gets smaller
                            s_ending_Size = s;
                            //set color of color spanning s for sweep to the right
                            s_color = pointsy.get(s).getColor();

                            //current rectangle to compare with global rectangle
                            currentRectangle.setX(pointsx.get(p).getX());
                            currentRectangle.setY(pointsx.get(p).getY());
                            currentRectangle.setWidth(pointsx.get(r).getX() - pointsx.get(p).getX());
                            currentRectangle.setHeight(pointsy.get(s).getY() - pointsx.get(p).getY());


                            //compare rectangle area to find optimum rectangle
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
                                break;
                            }
                        }
                    }
                }
            }
        }

        //clear arrays for next uses, return the rectangle
        colors.clear();
        ccomb.clear();
        pointsy.clear();
        cspanning.clear();

        return optimumRectangle;
    }

    //execute for the Container of rectangle
    public static void execute(CoordPanel panel) {
        //check if there are enugh points
        if (pointsx.size() >= 2) {
            //count time for Algorithm Information and set Triangle
            Rectangle rect = new Rectangle(0, 0, 0, 0);
            long startTime = System.nanoTime();
            rect = algo1();
            long stopTime = System.nanoTime();

            //if a cs rectangle exists
            if (rect != null) {
                panel.setRect(rect);

                //algorithm Information
                Layout.circum1.setText(Integer.toString(rect.getCircum()));
                Layout.area1.setText(Integer.toString(rect.getArea()));
                Layout.time1.setText(Long.toString(stopTime - startTime));
            }
        }
    }


    public static Rectangle algo1step(CoordPanel panel, int out_step) {

        // SAME ALGORITH AS ABOVE, BUT P AND Q ARE ALREADY GIVEN


        panel.emptyRects();
        Rectangle rect;
        rect = algo1();
        Color s_color = null;


        //sort in x order, create List y and sort in y order
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

        //List that check color spanning and count colors
        ArrayList<Color> cspanning = new ArrayList<Color>(colors.size());
        ArrayList<Color> ccomb = new ArrayList<Color>(colors.size());
        int s_ending_Size = pointsy.size();


        //if less than 2 colors algorithm can't run
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

        //step for 0 zero which means reset of the Step
        if (out_step == 0) {
            panel.setLines(0, null);
            panel.setLines(1, null);
            panel.setLines(2, null);
            return null;
        }

        // draw left boundary
        if (out_step == 1) {
            Line pline = new Line(rect.getX(), 0, rect.getX(), 5000);
            panel.setLines(0, pline);
            return null;
        }
        step++;

        //draw upper boundary
        if (out_step == 2) {
            Line qline = new Line(0, rect.getY(), 5000, rect.getY());
            panel.setLines(1, qline);
            return null;
        }
        step++;


        //algorithm operation is the same, only step code is commented


        //r = right boundary
        for (int r = 0; r < pointsx.size(); r++) {
            if (pointsx.get(r).getX() < rect.getX()) {
                continue;
            }
            if (pointsx.get(r).getY() < rect.getY()) {
                continue;
            }

            cspanning.set(colors.indexOf(pointsx.get(r).getColor()), pointsx.get(r).getColor());


            //draw sweep line of right boundary
            step += 1;
            if (step == out_step) {
                Line rline = new Line(pointsx.get(r).getX(), 0, pointsx.get(r).getX(), 5000);
                panel.setLines(2, rline);
                panel.setLines(3, null);
                step = 0;
                return null;
            }

            if (s_color != null && pointsx.get(r).getColor() != s_color) {
                continue;
            }


            if (!cspanning.contains(null)) {
                ccomb.clear();
                for (int i = 0; i < colors.size(); i++) {
                    ccomb.add(null);
                }


                //s = upper boundary
                for (int s = 0; s < s_ending_Size; s++) {
                    if (pointsy.get(s).getX() < rect.getX()) {
                        continue;
                    }
                    if (pointsy.get(s).getY() < rect.getY()) {
                        continue;
                    }
                    if (pointsy.get(s).getX() > pointsx.get(r).getX()) {
                        continue;
                    }
                    ccomb.set(colors.indexOf(pointsy.get(s).getColor()), pointsy.get(s).getColor());


                    //set lower boundary sweep line
                    step += 1;
                    if (step == out_step) {
                        Line sline = new Line(0, pointsy.get(s).getY(), 5000, pointsy.get(s).getY());
                        panel.setLines(3, sline);
                        step = 0;
                        return null;
                    }

                    //increment ending size of s and color which r has to sweep for if color spanning
                    if (!ccomb.contains(null)) {
                        s_ending_Size = s + 1;
                        s_color = pointsy.get(s).getColor();
                        ccomb.clear();
                        break;
                    }
                }
            }
        }
        ccomb.clear();
        OptRectStepButton.resetOut_Step();


        //reset lines and draw the optimum rectangle
        step = 0;
        s_color = null;
        panel.setLines(0, null);
        panel.setLines(1, null);
        panel.setLines(2, null);
        panel.setLines(3, null);
        panel.setRect(rect);
        return null;
    }

    public static void resetStep() {
        step = 0;
    }

    public static void resetPointsy() {
        pointsy.clear();
    }
}