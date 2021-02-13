import java.util.Scanner;
public class Lab1 {
    public static void main(String[] args){
        //Вводим координаты трех точек
        Scanner in = new Scanner(System.in);
        Point3d point1 = getPoint3d(in);
        Point3d point2 = getPoint3d(in);
        Point3d point3 = getPoint3d(in);
        //Проверка на совпадение
        if(point1.equals(point2) || point1.equals(point3) ||
           point2.equals(point3)) {
            System.out.print("Одна из точек равна другой! " +
                    "Ошибка рассчета площади.");
            return;
        }
        //Выводим введенные точки на консоль
        printPoint3d(point1);
        printPoint3d(point2);
        printPoint3d(point3);
        //Рассчитываем площадь треугольника
        double area = computeArea(point1, point2, point3);
        //Выводим в консоль площадь с точность до
        //двух знаков после запятой
        System.out.printf("Площадь треугольника: %.2f", area);
    }
    //Функция расчета площади треугольника по координатам вершин
    //Принимает три аргумента типа Point3d
    //Возвращает число с плавающей точкой типа double
    public static double computeArea(Point3d a, Point3d b, Point3d c){
        double ab = a.distanceTo(b);
        double ac = a.distanceTo(c);
        double bc = b.distanceTo(c);
        double p = (ab + ac + bc) / 2.0;
        return Math.sqrt(p * (p - ab) * (p - ac) * (p - bc));
    }
    //Функция ввода 3-х координаты для точки Point3d
    //Возвращает экземпляр объекта типа Point3d
    //Принимает сканнер для ввода с консоли
    public static Point3d getPoint3d(Scanner in){
        System.out.print("Введите 3 координаты: ");
        double x, y, z;
        x = in.nextDouble(); y = in.nextDouble(); z = in.nextDouble();
        return new Point3d(x, y, z);
    }
    //Функция вывода объекта типа Point3d в консоль
    public static void printPoint3d(Point3d point){
        System.out.print("x: " + point.getX());
        System.out.print(" y: " + point.getY());
        System.out.println(" z: " + point.getZ());
    }
}