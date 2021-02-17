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
    private int rows_remaining;

    private JComboBox<FractalGenerator> combo;
    private JButton reset;
    private JButton save;

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
            if(rows_remaining == 0) {
                double xCoord = FractalGenerator.getCoord(d.x, d.x + d.width, 600, e.getX());
                double yCoord = FractalGenerator.getCoord(d.y, d.y + d.height, 600, e.getY());
                fg.recenterAndZoomRange(d, xCoord, yCoord, 0.5);
                drawFractal();
            }
        }
        public void mouseEntered(MouseEvent e){ }
        public void mouseExited(MouseEvent e){ }
    }

    private class FractalWorker extends SwingWorker<Object, Object> {
        private int row;
        private int size;
        int rgb[];
        FractalWorker(int y, int size){
            this.row = y;
            this.size = size;
        }
        public Object doInBackground(){
            rgb = new int[size];
            for(int x = 0; x < size; x++){
                double xCoord = FractalGenerator.getCoord(d.x, d.x + d.width, 600, x);
                double yCoord = FractalGenerator.getCoord(d.y, d.y + d.height, 600, row);
                int iter = fg.numIterations(xCoord, yCoord);
                if(iter == -1)
                    rgb[x] = 0;
                else{
                    float hue = 0.7f + (float)iter / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    rgb[x] = rgbColor;
                }
            }
            return null;
        }
        public void done(){
            for(int x = 0; x < size; x++)
                jid.drawPixel(x, row, rgb[x]);
            jid.repaint(0, 0, row,size,1);
            rows_remaining--;
            if(rows_remaining <= 0)
                enableUI(true);
        }
    }

    void enableUI(boolean val){
        combo.setEnabled(val);
        reset.setEnabled(val);
        save.setEnabled(val);
    }

    private void drawFractal(){
        enableUI(false);
        rows_remaining = jid.getHeight();
        FractalWorker workers[] = new FractalWorker[jid.getHeight()];
        for(int y = 0; y < jid.getHeight(); y++)
            workers[y] = new FractalWorker(y, jid.getWidth());
        for(int y = 0; y < jid.getHeight(); y++)
            workers[y].execute();
    }

    public void createAndShowGUI(){
        JFrame frame = new JFrame("Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.add(jid, BorderLayout.CENTER);
        ZoomHandler zoom = new ZoomHandler();
        jid.addMouseListener(zoom);

        JPanel down_panel = new JPanel();
        save = new JButton("Save Image");
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
        reset = new JButton("Reset Display");
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
        combo = new JComboBox<>();
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
