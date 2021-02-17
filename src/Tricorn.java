import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double range){
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }
    public int numIterations(double x, double y){
        double re = 0, im = 0;
        int iterations = 0;
        while(iterations < MAX_ITERATIONS && re * re + im * im < 4) {
            double t1 = re * re, t2 = im * im;
            im = -(2 * re * im + y);
            re = t1 - t2 + x;
            ++iterations;
        }
        if(iterations == MAX_ITERATIONS) return -1;
        return iterations;
    }
}