import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorTestClass {

    public ColorTestClass(){
    }

    public boolean getColorCombBool(List<Point> Points, int numberOfColor)
    {
        int numberOfActualColors=0;
        ArrayList<Color> ColorsInList =new ArrayList<Color>();
        for( int i=0; i < Points.size(); i++ )
        {
            if(!(ColorsInList.contains(Points.get(i).getColor())))
            {
                ColorsInList.add(Points.get(i).getColor());
                numberOfActualColors++;
            }
        }
        return numberOfActualColors == numberOfColor;
    }
}
