/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }

    @Override
    public boolean equals(Object obj){
        //Проверяем ссылки obj и this
        //Location test = new Location();
        //test.equals(test);
        if(obj == this)
            return true;
        //Если объект == null или тип объекта не равен
        //Location
        if(obj == null || obj.getClass() != this.getClass())
            return false;
        Location other = (Location)obj;
        return xCoord == other.xCoord && yCoord == other.yCoord;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + xCoord;
        result = prime * result + yCoord;
        return result;
    }
}