import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
        fg = new Mandelbrot();
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

        JPanel down_panel = new JPanel();
        JButton save = new JButton("Save Image");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fc.setFileFilter(filter);
                fc.setAcceptAllFileFilterUsed(false);
                int res = fc.showSaveDialog(frame);
                if(res == JFileChooser.APPROVE_OPTION){
                    File selectedFile = fc.getSelectedFile();
                    try {
                        ImageIO.write(jid.image, "png", selectedFile);
                    } catch(IOException exp) {
                        JOptionPane.showMessageDialog(frame, exp.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        JButton reset = new JButton("Reset Display");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                fg.getInitialRange(d);
                drawFractal();
            }
        });
        down_panel.add(save);
        down_panel.add(reset);
        contentPane.add(down_panel, BorderLayout.SOUTH);

        JPanel up_panel = new JPanel();
        JComboBox<FractalGenerator> combo = new JComboBox<>();
        combo.addItem(fg);
        combo.addItem(new Tricorn());
        combo.addItem(new BurningShip());
        combo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fg = (FractalGenerator) combo.getSelectedItem();
                fg.getInitialRange(d);
                drawFractal();
            }
        });
        JLabel label = new JLabel("Fractal: ");
        up_panel.add(label);
        up_panel.add(combo);
        contentPane.add(up_panel, BorderLayout.NORTH);

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
