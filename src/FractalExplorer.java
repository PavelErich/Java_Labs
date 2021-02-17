import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int width;
    private int height;
    private JImageDisplay jid;
    private FractalGenerator fg;
    private Rectangle2D.Double d;

    public FractalExplorer(int width, int height){
        this.width = width;
        this.height = height;
        jid = new JImageDisplay(width, height);
        fg = new BurningShip();
        d = new Rectangle2D.Double();
        fg.getInitialRange(d);
    }

    private class ZoomHandler implements MouseListener{
        public void mousePressed(MouseEvent e){ }
        public void mouseReleased(MouseEvent e){ }
        public void mouseClicked(MouseEvent e){
            double xCoord = FractalGenerator.getCoord(d.x, d.x + d.width,600,e.getX());
            double yCoord = FractalGenerator.getCoord(d.y, d.y + d.height, 600, e.getY());
            fg.recenterAndZoomRange(d, xCoord, yCoord, 0.5);
            drawFractal();
        }
        public void mouseEntered(MouseEvent e){ }
        public void mouseExited(MouseEvent e){ }
    }

    private void drawFractal(){
        for(int y = 0; y < jid.getHeight(); y++){
            for(int x = 0; x < jid.getWidth(); x++){
                double xCoord = FractalGenerator.getCoord(d.x, d.x + d.width, 600, x);
                double yCoord = FractalGenerator.getCoord(d.y, d.y + d.height, 600, y);
                int iter = fg.numIterations(xCoord, yCoord);
                if(iter == -1)
                    jid.drawPixel(x, y, 0);
                else{
                    float hue = 0.7f + (float)iter / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    jid.drawPixel(x, y, rgbColor);
                }
            }
        }
        jid.repaint();
    }

    public void createAndShowGUI(){
        JFrame frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.add(jid, BorderLayout.CENTER);

        ZoomHandler zoom = new ZoomHandler();
        jid.addMouseListener(zoom);

        JButton clear = new JButton("Reset Display");
        contentPane.add(clear, BorderLayout.SOUTH);

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                fg.getInitialRange(d);
                drawFractal();
            }
        });

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void main(String[] args){
        FractalExplorer app = new FractalExplorer(600, 600);
        app.createAndShowGUI();
        app.drawFractal();
    }
}
