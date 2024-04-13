import java.awt.Color;
import java.awt.event.*;
import javax.swing.event.MouseInputListener;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FractalGenerator implements MouseInputListener, KeyListener, ActionListener{

    Render render;
    BufferedImage image;
    double[][] IFSTable;
    ArrayList<double[][]> presets;
    public FractalGenerator(int width, int height) {
        render = new Render(width, height, 0, 1, 0, 1);
        render.addKeyListener(this);
        render.addMouseInputListener(this);
        //this.IFSTable = IFSTables.GASKET;
        presets = new ArrayList<>();
        presets.add(IFSTables.GASKET);
        presets.add(IFSTables.GASKET2);
        presets.add(IFSTables.GASKET3);
        presets.add(IFSTables.CARPET);
        presets.add(IFSTables.KOCH);
        presets.add(IFSTables.SQUAREKOCH);
        presets.add(IFSTables.TREE);
        presets.add(IFSTables.CHRISTMASTREE);
        presets.add(IFSTables.SPIRAL);
    }

    boolean next = false, reset = false, select = true;
    int choice = 0;
    public void start() {
        render.initFrame(Color.white, true, true, "Fractals");
        render.setClearColor(Color.black);
        reset();
        while (render.frameIsShowing()) {
            if (reset) { reset();}
            if (select) {
                IFSTable = presets.get(choice);
                render.setTitle(IFSTables.getName(IFSTable));
                select = false;
            }
            if (next) {
                render.drawFromCurrentImage(IFSTable);
                next = false;
            }
            render.show();
        }
    }

    char shape = 's';
    Color[] colors = new Color[]{
        Color.blue, Color.red, Color.green, Color.cyan, Color.yellow, Color.black, Color.magenta, Color.orange, Color.pink};
    boolean randomColor = false, resetColor = false;
    Color color = Color.white;
    public void reset() {
        render.clear();
        if (resetColor) {
            color = Color.white;
            resetColor = false;
            randomColor = false;
        }
        if (randomColor) color = randomColor();
        render.setColor(color);
        switch (shape) {
            case 's' -> render.fillSquare(0.5, 0.5, 0.5);
            case 't' -> render.fillPolygon(new double[]{0, 0.5, 1}, new double[]{0, 1, 0});
            case 'c' -> render.fillCircle(0.5, 0.5, 0.5);
            case 'r' -> {
                int npoints = randomInt(3, 10);
                int nshapes = randomInt(1, 5);
                for (int j = 0; j < nshapes; j++) {
                    double[] x = new double[npoints], y = new double[npoints];
                    for (int i = 0; i < npoints; i++) {
                        x[i] = Math.random();
                        y[i] = Math.random();
                    }
                    render.fillPolygon(x, y);
                }

            }
        }
        render.show();
        reset = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void actionPerformed(ActionEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) { 
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) next = true; 
        if (key == KeyEvent.VK_BACK_SPACE) reset = true;

        if (key == KeyEvent.VK_1) {
            select = true; choice = 0;
        } else if (key == KeyEvent.VK_2) {
            select = true; choice = 1;
        }else if (key == KeyEvent.VK_3) {
            select = true; choice = 2;
        }else if (key == KeyEvent.VK_4) {
            select = true; choice = 3;
        } else if (key == KeyEvent.VK_5) {
            select = true; choice = 4;
        } else if (key == KeyEvent.VK_6) {
            select = true; choice = 5;
        } else if (key == KeyEvent.VK_7) {
            select = true; choice = 6;
        } else if (key == KeyEvent.VK_8) {
            select = true; choice = 7;
        } else if (key == KeyEvent.VK_9) {
            select = true; choice = 8;
        }

        if (key == KeyEvent.VK_M) {
            try {
                render.saveImage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (key == KeyEvent.VK_0) randomColor = !randomColor;
        if (key == KeyEvent.VK_P) resetColor = true;

        if (key == KeyEvent.VK_T) shape = 't';
        else if (key == KeyEvent.VK_S) shape = 's';
        else if (key == KeyEvent.VK_C) shape = 'c';
        else if (key == KeyEvent.VK_R) shape = 'r';
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    public static int randomInt(int min, int max) { return (int) (Math.random() * (max - min)) + min; } 

    public static Color randomColor() {
        List<Integer> rgb = Arrays.asList(255, 0, randomInt(0, 255));
        Collections.shuffle(rgb);
        return new Color(rgb.get(0), rgb.get(1), rgb.get(2));
    }
}
