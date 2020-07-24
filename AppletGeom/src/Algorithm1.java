import org.w3c.dom.ls.LSOutput;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;


public class Algorithm1 {

    private static List<Point> points = CoordPanel.getPoints();



    public static Rectangle algo1() {

        //Rectangle for Optimum and Comparing with Optimum
        Rectangle optimumRectangle= new Rectangle(0,0,0,0);
        Rectangle compareRectangle= new Rectangle(0,0,0,0);

        //temporary Variables Color,x-y Coordinate
        int px, py ,pcol;
        px= py= pcol = 0;

        //sort point List after x coordinate
        Collections.sort(points, new ComparatorPoint());

        List<Color> colors = new LinkedList<Color>();

        List<Point> setOfCornerPoints = new ArrayList<Point>();




        // Get amount of color for a rectangle
        int colorsize=0;
        colors.add(points.get(0).getColor());

        for (int k = 1; k < points.size(); k++) {
            for (int l = 0; l < colors.size(); l++){
                if (colors.contains(points.get(k).getColor())==false){
                    colors.add(points.get(k).getColor());
                }
            }
        }
        colorsize=colors.size();





        //Find all the possible lower left Corner Combinations or Points

        //Maximum Value is everyPoint has max n-k possible partners, k=0 for first furthest left Point
        int [] [] cornerPointIdea = new int[points.size()][points.size()*points.size()];




        for(int cp=0; cp< points.size(); cp++)
        {
            Point pivot = points.get(cp);

            for (int partnercounter= cp + 1 ; partnercounter < points.size(); partnercounter++)
            {
                Point temp = points.get(partnercounter);
                int tempY = temp.getY();
                int tempX = temp.getX();

                if( (pivot.getY() >= tempY) && (pivot.getX() < tempX))
                {
                    cornerPointIdea[cp][partnercounter] = 1;
                }
            }
        }

        for ( int i = 0; i < cornerPointIdea.length; i++)
        {
            for ( int j = 0; j < (points.size()); j++)
            {
                //leftCornerWeComputeWith
                if(cornerPointIdea[i][j] == 1)
                {
                    Point cornerleft = points.get(i);
                    if (points.get(i).getY() >= points.get(j).getY())
                    {
                        cornerleft.setY(points.get(j).getY());
                    }
                    setOfCornerPoints.add(cornerleft);
                }
            }
        }




        //Set of Points above and to the right of the Corner Point

        List<Point> PointsAboveAndToTheRight = new ArrayList<Point>();

        for ( int i = 0; i < points.size(); i++ ) {
            for ( int j = 0; j < points.size(); j++ ) {
                if ( points.get(j) != setOfCornerPoints.get(i) ) {
                    if ( points.get(j).getY() >= setOfCornerPoints.get(i).getY() && points.get(j).getX() >= setOfCornerPoints.get(i).getX() ) {
                        PointsAboveAndToTheRight.add( points.get(j) );
                    }
                }
            }


            //richtig----------
            //Start of real Algorithm

            //Aktueller Corner Punkt
            Point actualCornerPoint = setOfCornerPoints.get(i);

            //sortieren nach x
            Collections.sort(PointsAboveAndToTheRight, new ComparatorPoint());




            //y Coordinate Infinite
            py = 100000;

            //x Cordinate the Point you are at first start
            px = setOfCornerPoints.get(i).getX();
            int tempI = i;
            //Clear List colors
            colors.clear();

            Color actualTopEdgeColor ;
            Point rightEdge;
            boolean combFound=false;


            //PointsInRectangle
            List<Point> setOfPointsInRectangle = new ArrayList<Point>();

            //TempPoint we have decreased
            Point tempPointDecreasedYpoint;

            //testing class if colors are there
            ColorTestClass testing=new ColorTestClass();

            for ( int k = 0; k < PointsAboveAndToTheRight.size(); k++)
            {
                if ( PointsAboveAndToTheRight.get(k).getX() <= px && PointsAboveAndToTheRight.get(k).getY() <= py )
                {
                    setOfPointsInRectangle.add(PointsAboveAndToTheRight.get(k));
                    if (colors.contains(PointsAboveAndToTheRight.get(k).getColor())==false)
                    {

                        colors.add(PointsAboveAndToTheRight.get(k).getColor());

                        if (colors.size()==colorsize)
                        {
                            combFound=true;

                            //NextStep sortiere nach Y-Order 1 tes Element Größtes
                            Collections.sort(setOfPointsInRectangle,new ComparatorPointY());

                            for ( int l=0; combFound && l<setOfPointsInRectangle.size() ; l++ ) {

                                //TempPunkt speichern falls ohne in keine combFound mehr existiert
                                tempPointDecreasedYpoint = setOfPointsInRectangle.get(l);
                                setOfPointsInRectangle.remove(0);

                                //testing ob Farben enthalten sind
                                if (testing.getColorCombBool(setOfPointsInRectangle, colorsize) == false) {

                                    //hinzufuegen des gebrauchten Punktes
                                    setOfPointsInRectangle.add(tempPointDecreasedYpoint);

                                    //erstellen eines moeglichen Optimum Rectangle
                                    //compareRectangle.setHeight();

                                    break;
                                }
                            }

                            // 2. Then, in decreasing y-order, the top edge steps through the sites
                            // of the rectangle as long as it still contains all colors; when this
                            //stops, we have found a candidate.



                        }
                    }
                }


                //1. X Corner px is moved to the right over the sites of AboveAndtoTheRightofCorner

                if ((colors.size() <= colorsize) && (k == PointsAboveAndToTheRight.size()-1))
                {
                    colors.clear();
                    setOfPointsInRectangle.clear();
                    px = PointsAboveAndToTheRight.get(++tempI).getX();
                    k = 0;
                }

            }

        }


            PointsAboveAndToTheRight.clear();


        return optimumRectangle;
    }






        /*1. Initially the top edge of the rectangle starts at infinity. The right
        edge starts at the x-coordinate of the corner, it is moved right
        over the sites of U until all colors are contained in the actual
        rectangle.
        2. Then, in decreasing y-order, the top edge steps through the sites
        of the rectangle as long as it still contains all colors; when this
        stops, we have found a candidate.
        3. The right edge advances until it reaches a site with the color of
        the site at the actual top edge.
        4. As long as the right edge has not stepped over all points of U,
        we repeat from step 2.

         */































}

