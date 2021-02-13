public class Lab1 {
    public static void main(String[] args){
        Point2d test = new Point2d(1, 2);
        Point2d test1 = new Point2d(2, 1);
        Point2d test2 = new Point2d(1, 2);
        System.out.println(test == test1);
        System.out.println(test == test2);
        System.out.println(test.equals(test1));
        System.out.println(test.equals(test2));
    }
}