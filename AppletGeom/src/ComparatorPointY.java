import java.util.Comparator;

public class ComparatorPointY implements Comparator<Point> {


    @Override
    public int compare(Point p1, Point p2) {
        if(p1.getY() < p2.getY()){
            return -1;
        } else {
            return 1;
        }
    }
}
