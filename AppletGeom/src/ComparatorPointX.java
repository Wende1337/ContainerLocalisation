import java.util.Comparator;

public class ComparatorPointX implements Comparator<Point> {


    @Override
    public int compare(Point p1, Point p2) {
        if(p1.getX() < p2.getX()){
            return -1;
        } else {
            return 1;
        }
    }
}
