import java.awt.*;

public class Rectangle {

    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(int x, int y, int width,int height)
    {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }

    public void setX(int x){
        this.x=x;
    }
    public void setY(int y){
        this.y=y;
    }
    public void setWidth(int width){
        this.width=width;
    }
    public void setHeight(int height){
        this.height=height;
    }
    public int getX()
    {
        return this.x;
    }
    public int getY()
    {
        return this.y;
    }
    public int getWidth()
    {
        return this.width;
    }
    public int getHeight()
    {
        return this.height;
    }


    public static int getArea(Rectangle a){
        return    a.getWidth()*a.getHeight();
    }

    //Comparing Rectangles after Area or Scope and returning The Rectangle which is smaller
    public static Rectangle compareRectangles(Rectangle a, Rectangle b, String compareType)
    {
        if (compareType == "area" )
        {
            int area1= a.getWidth()*a.getHeight();
            int area2= b.getWidth()*b.getHeight();
            if (area1<area2)
            {
                return a;
            }
            else
            {
                return b;
            }
        }
        if (compareType == "scope")
        {
            int scope1= a.getHeight()*2 + a.getWidth()*2 ;
            int scope2= b.getHeight()*2 + b.getWidth()*2 ;
            if (scope1 < scope2) {
                return a;
            }
            else {
                return b;
            }
        }
        Rectangle Fehlerrectangle= new Rectangle(0,0,0,0);
        return Fehlerrectangle;
    }

}
