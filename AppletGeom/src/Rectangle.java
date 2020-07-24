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
    public void setY(int x){
        this.x=x;
    }
    public void setWidth(int x){
        this.x=x;
    }
    public void setHeight(int x){
        this.x=x;
    }
    public int getX()
    {
        return this.x;
    }
    public int gety()
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


    //Comparing Rectangles after Area or Scope and returning The Rectangle which is smaller
    public Rectangle compareRectangles(Rectangle a, Rectangle b, String compareType)
    {
        if (compareType == "area" )
        {
            int area1= a.getWidth()*a.getWidth();
            int area2= b.getHeight()*b.getWidth();
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
