public class Point3d {
    private double _x;
    private double _y;
    private double _z;
    public Point3d(double x, double y, double z){
        _x = x;
        _y = y;
        _z = z;
    }
    public Point3d() { this(0,0,0); }
    public double getX() { return _x; }
    public double getY() { return _y; }
    public double getZ() { return _z; }
    public void setX(double x) { _x = x; }
    public void setY(double y) { _y = y; }
    public void setZ(double z) { _z = z; }
    public boolean equals(Point3d other){
        return _x == other._x &&
               _y == other._y &&
               _z == other._z;
    }
    public double distanceTo(Point3d target){
        double res = Math.sqrt(Math.pow(target._x - _x, 2) +
                Math.pow(target._y - _y, 2) +
                Math.pow(target._z - _z, 2));
        return Math.round(res * 100.0) / 100.0;
    }
}