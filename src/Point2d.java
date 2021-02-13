public class Point2d {
    //Координата x
    private double _x;
    //Координата y
    private double _y;
    //Конструктор инициализации
    public Point2d(double x, double y){
        _x = x;
        _y = y;
    }
    //Конструктор по умолчанию
    public Point2d(){ this(0, 0); }
    //Получение координаты x
    public double getX() { return _x; }
    //Получение координаты y
    public double getY() { return _y; }
    //Установка координаты x
    public void setX(double x) { _x = x; }
    //Установка координаты y
    public void setY(double y) { _y = y; }
    //Функция сравнения полей объектов
    public boolean equals(Point2d other){
        return _x == other._x && _y == other._y;
    }
}