import java.awt.*;
import java.util.*;
import java.util.List;

public class Colorcombrect {

    private static List<Point> points = CoordPanel.getPoints();
    private static int[] rect = new int[4];


    public static int[] Algorithm() {
//		if List is empty
        if (points.size()==0) {
            return null;
        }

//      sort point List after x coordinate
        Collections.sort(points, new ComparatorPointX());

//      create List for colors to extract and get all distinct colors
        List<Color> colors = new LinkedList<Color>();
        colors.add(points.get(0).getColor());
//        get all point colors

        for (int k = 1; k < points.size(); k++) {
            for (int l = 0; l< colors.size(); l++){
                if (colors.contains(points.get(k).getColor())==false){
                    colors.add(points.get(k).getColor());
                }
            }
        }


//      initiate Array with all points of different colors in combination, variable for min/max x,y and area,
//      circumference
        Point[] comb = new Point[colors.size()];
        int min_x1,min_y1,max_x1,max_y1, area1, circum1;
//      variable for min/max x,y and area,circumference
        int min_x2,min_y2,max_x2,max_y2;
        min_x2=min_y2=max_x2=max_y2=0;
        int area2,circum2;
        area2=circum2=2137483647;
        int index = 0;

        for (int i = 0; i < points.size(); i++) {
//            get color index to be synchronized with array "colors"
            index = colors.indexOf(points.get(i).getColor());
            comb[index] = points.get(i);





//          loop through comb list to find out if a valid combination is found
            boolean combfound = false;
            for (int n = 0; n < comb.length; n++) {
//              check array if its null break Loop, else set combfound true, if all comb elements not null -> rect found
                if (comb[n] == null) {
                    combfound = false;
                    break;
                } else {
                    combfound = true;
                }
            }

//          rectangle found, calculate area and circumference
            if (combfound == true) {
//              set random x,y for comparison
                min_x1 = comb[0].getX();
                max_x1 = comb[0].getX();
                min_y1 = comb[0].getY();
                max_y1 = comb[0].getY();

//              compare min_x, min_y, max_x, max_y
                for (int c = 1; c < comb.length; c++) {
                    if (min_x1 > comb[c].getX()) {
                        min_x1 = comb[c].getX();
                    }
                    if (min_y1 > comb[c].getY()) {
                        min_y1 = comb[c].getY();
                    }
                    if (max_x1 < comb[c].getX()) {
                        max_x1 = comb[c].getX();
                    }
                    if (max_y1 < comb[c].getY()) {
                        max_y1 = comb[c].getY();
                    }
                }
//              calculate area and surface
                area1 = (max_x1 - min_x1) * (max_y1 - min_y1);
                circum1 = ((max_x1 - min_x1) + (max_y1 - min_y1)) * 2;


//          check if a combination already exists, if it does not exists copy newly found combination to validrect
//          (area = 0) means no reactangle found yet
//          or compare properties of rectangle and choose more optimal one
                if (area1 <= area2) {
                    area2 = area1;
                    circum2 = circum1;
                    min_x2=min_x1;
                    min_y2=min_y1;
                    max_x2=max_x1;
                    max_y2=max_y1;
                }


            }

        }

//       führe selben Algorithmus wie oben aus nur für y Koordinaten
        Collections.sort(points, new ComparatorPointY());

        for (int i = 0; i < points.size(); i++) {
//            get color index to be synchronized with array "colors"
            index = colors.indexOf(points.get(i).getColor());
            comb[index] = points.get(i);


//          loop through comb list to find out if a valid combination is found
            boolean combfound = false;
            for (int n = 0; n < comb.length; n++) {
//              check array if its null break Loop, else set combfound true, if all comb elements not null -> rect found
                if (comb[n] == null) {
                    combfound = false;
                    break;
                } else {
                    combfound = true;
                }
            }

//          rectangle found, calculate area and circumference
            if (combfound == true) {
//              set random x,y for comparison
                min_x1 = comb[0].getX();
                max_x1 = comb[0].getX();
                min_y1 = comb[0].getY();
                max_y1 = comb[0].getY();

//              compare min_x, min_y, max_x, max_y
                for (int c = 1; c < comb.length; c++) {
                    if (min_x1 > comb[c].getX()) {
                        min_x1 = comb[c].getX();
                    }
                    if (min_y1 > comb[c].getY()) {
                        min_y1 = comb[c].getY();
                    }
                    if (max_x1 < comb[c].getX()) {
                        max_x1 = comb[c].getX();
                    }
                    if (max_y1 < comb[c].getY()) {
                        max_y1 = comb[c].getY();
                    }
                }
//              calculate area and surface
                area1 = (max_x1 - min_x1) * (max_y1 - min_y1);
                circum1 = ((max_x1 - min_x1) + (max_y1 - min_y1)) * 2;


//          check if a combination already exists, if it does not exists copy newly found combination to validrect
//          (area = 0) means no reactangle found yet
//          or compare properties of rectangle and choose more optimal one
                if (area1 <= area2) {
                    area2 = area1;
                    circum2 = circum1;
                    min_x2=min_x1;
                    min_y2=min_y1;
                    max_x2=max_x1;
                    max_y2=max_y1;
                }


            }

        }



        int[] xy_widht_height= new int[4];
        xy_widht_height[0]=min_x2;
        xy_widht_height[1]=min_y2;
        xy_widht_height[2]=max_x2-min_x2;
        xy_widht_height[3]=max_y2-min_y2;


        return xy_widht_height;
    }

    //function to create rectangle with this algorithm
    public static void execute(CoordPanel panel) {

        if (points.size()!=0) {
            rect=Colorcombrect.Algorithm();
            Rectangle rectangle= new Rectangle(rect[0],rect[1],rect[2],rect[3]);
            panel.setRect(rectangle);
        }
    }

    public static <T> int getNulls(T[] arr){
        int count = 0;
        for(T el : arr)
            if (el != null)
                ++count;
        return count;
    }

}



