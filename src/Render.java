import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputListener;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Render {
    private static final Color DEFAULT_PEN_COLOR   = Color.black;
    private static final Color DEFAULT_CLEAR_COLOR = Color.white;
    private static final double DEFAULT_PEN_RADIUS = 0.002;
    private static final double DEFAULT_STROKE_WIDTH = 1;
    private static final int DEFAULT_SIZE = 512;
    private static final double BORDER = 0.00;
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = 1.0;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = 1.0;
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 16);

    BufferedImage offscreenImage, onscreenImage;
    public Graphics2D offscreen, onscreen;
    Color penColor = DEFAULT_PEN_COLOR, clearColor = DEFAULT_CLEAR_COLOR;
    FontMetrics fontMetrics;
    Font font;

    int width = DEFAULT_SIZE, height = DEFAULT_SIZE;
    double penRadius = DEFAULT_PEN_RADIUS, strokeWidth = DEFAULT_STROKE_WIDTH;
    double xmin, ymin, xmax, ymax, xcenter, ycenter;

    int frames = 0;
    double fps = 0;
    long startTime = 0;

    JFrame frame;

    public Render(int width, int height, double xmin, double xmax, double ymin, double ymax) {
        System.setProperty("sun.java2d.opengl", "True");
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) { 
            e1.printStackTrace(); 
        }
        this.width = width; this.height = height;
        setXscale(xmin, xmax); setYscale(ymin, ymax);
        xcenter = (xmax - xmin) * 0.5; ycenter = (ymax - ymin) * 0.5;
        offscreenImage = createCompatibleBufferedImage(width, height, true);
        onscreenImage = createCompatibleBufferedImage(width, height, true);
        offscreen = offscreenImage.createGraphics();
        onscreen = onscreenImage.createGraphics();
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);
        setStrokeWidth(DEFAULT_STROKE_WIDTH);
        setFont(DEFAULT_FONT);
        ImageIcon icon = new ImageIcon(onscreenImage);
        JLabel canvas = new JLabel(icon);
        frame = new JFrame();
        frame.setContentPane(canvas);
    }

    public Render(int width, int height) { this(width, height, DEFAULT_XMIN, DEFAULT_YMIN, DEFAULT_XMAX, DEFAULT_YMAX); }

    public Render() { this(DEFAULT_SIZE, DEFAULT_SIZE); }

    public void addKeyListener(KeyListener listener) { frame.addKeyListener(listener); }
    public void addMouseListener(MouseListener listener) { frame.addMouseListener(listener); }
    public void addMouseMotionListener(MouseMotionListener listener) { frame.addMouseMotionListener(listener); }
    public void addMouseWheelListener(MouseWheelListener listener) { frame.addMouseWheelListener(listener); }
    public void addMouseInputListener(MouseInputListener listener) {
        addMouseListener(listener); addMouseMotionListener(listener);
    }

    public void initFrame(Color backgroundColor, boolean resizable, boolean disposeOnClose, String title) { 
        frame.setBackground(backgroundColor);  frame.setLocation(0, 0); frame.setResizable(resizable);
        frame.setDefaultCloseOperation(disposeOnClose ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);
        frame.setTitle(title); frame.pack(); frame.requestFocusInWindow(); frame.setVisible(true);
    }

    public void setFont(Font font) { this.font = font; }

    public void setXscale(double min, double max) {
        double size = max - min;
        xmin = min - BORDER * size; xmax = max + BORDER * size;
    }

    public void setYscale(double min, double max) {
        double size = max - min;
        ymin = min - BORDER * size; ymax = max + BORDER * size;
    }

    public void setScale(double min, double max) { setXscale(min, max); setYscale(min, max); }

    public void setColor(int red, int green, int blue) { setColor(new Color(red, green, blue)); }
    public void setColor(int red, int green, int blue, int alpha) { setColor(new Color(red, green, blue, alpha)); }
    public void setColor(Color color) { penColor = color; offscreen.setColor(penColor); }
    public void setClearColor(Color color) { clearColor = color; }

    public void setStrokeWidth(double width) {
        strokeWidth = width;
        BasicStroke stroke = new BasicStroke((float) strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        offscreen.setStroke(stroke);
    }

    public void setTitle(String title) {frame.setTitle(title);}

    private double scaleX(double x) { return width  * (x - xmin) / (xmax - xmin); }
    private double scaleY(double y) { return height * (ymax - y) / (ymax - ymin); }
    private double factorX(double w) { return w * width  / Math.abs(xmax - xmin); }
    private double factorY(double h) { return h * height / Math.abs(ymax - ymin); }
    public double userX(double x) { return xmin + x * (xmax - xmin) / width; }
    public double userY(double y) { return ymax - y * (ymax - ymin) / height; }

    public void clear() { clear(clearColor); }
    public void clear(int red, int green, int blue) { clear(new Color(red, green, blue)); }
    public void clear(int red, int green, int blue, int alpha) { clear(new Color(red, green, blue, alpha)); }
    public void clear(Color color) {
        startTime = System.currentTimeMillis();
        offscreen.setColor(color); offscreen.fillRect(0, 0, width, height); offscreen.setColor(penColor);
    }

    public static void pause(int t) {
        try { Thread.sleep(t); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void show() { 
        //updateFPS();
        onscreen.drawImage(offscreenImage, 0, 0, null); frame.repaint(); 
        frames++;
    }

    public void updateFPS() {
        if (frames == 10) {
            long df = System.currentTimeMillis()-startTime;
            if (df != 0) fps = 10000/df;
            frames = 0;
        }
        setColor(Color.black);
        drawTextLeft(xmin, ymax - 0.25, "FPS : " + String.valueOf(fps));
    }

    public void line(double x1, double y1, double x2, double y2) { 
        offscreen.draw(new Line2D.Double(scaleX(x1), scaleY(y1), scaleX(x2), scaleY(y2))); 
    }

    private void pixel(double x, double y) { 
        offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1); 
    }

    public void drawPoint(double x, double y) {
        double xs = scaleX(x), ys = scaleY(y);
        if (strokeWidth <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - strokeWidth/2, ys - strokeWidth/2, strokeWidth, strokeWidth));
    }

    public void drawCircle(double x, double y, double radius) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*radius), hs = factorY(2*radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void fillCircle(double x, double y, double radius) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*radius), hs = factorY(2*radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void drawEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*semiMajorAxis), hs = factorY(2*semiMinorAxis);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void fillEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*semiMajorAxis), hs = factorY(2*semiMinorAxis);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void drawArc(double x, double y, double radius, double angle1, double angle2) {
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*radius);
        double hs = factorY(2*radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
    }

    public void fillArc(double x, double y, double radius, double angle1, double angle2) {
        while (angle2 < angle1) angle2 += 360;
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*radius);
        double hs = factorY(2*radius);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Arc2D.Double(xs - ws/2, ys - hs/2, ws, hs, angle1, angle2 - angle1, Arc2D.OPEN));
    }

    public void drawSquare(double x, double y, double halfLength) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*halfLength), hs = factorY(2*halfLength);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void fillSquare(double x, double y, double halfLength) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*halfLength), hs = factorY(2*halfLength);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void drawRectangle(double x, double y, double halfWidth, double halfHeight) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*halfWidth), hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.draw(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void fillRectangle(double x, double y, double halfWidth, double halfHeight) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(2*halfWidth), hs = factorY(2*halfHeight);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else offscreen.fill(new Rectangle2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void drawPolygon(double[] x, double[] y) {
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < x.length; i++) path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath(); offscreen.draw(path);
    }

    public void fillPolygon(double[] x, double[] y) {
        GeneralPath path = new GeneralPath();
        path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
        for (int i = 0; i < x.length; i++) path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
        path.closePath();
        offscreen.fill(path);
    }

    public void drawText(double x, double y, String text) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x), ys = scaleY(y);
        int ws = metrics.stringWidth(text), hs = metrics.getDescent();
        offscreen.drawString(text, (float) (xs - ws/2.0), (float) (ys + hs));
    }

    public void drawText(double x, double y, String text, double degrees) {
        double xs = scaleX(x), ys = scaleY(y);
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        drawText(x, y, text);
        offscreen.rotate(Math.toRadians(+degrees), xs, ys);
    }

    public void drawTextLeft(double x, double y, String text) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x), ys = scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float) xs, (float) (ys + hs));
    }

    public void drawTextRight(double x, double y, String text) {
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = scaleX(x), ys = scaleY(y);
        int ws = metrics.stringWidth(text), hs = metrics.getDescent();
        offscreen.drawString(text, (float) (xs - ws), (float) (ys + hs));
    }

    public void drawImage(Image image, double x, double y, ImageObserver observer) {
        offscreen.drawImage(image, (int) x, (int) y, observer);
    }

    public void drawImage(BufferedImage image, double x, double y, double scaledWidth, double scaledHeight) {
        double xs = scaleX(x), ys = scaleY(y);
        double ws = factorX(scaledWidth), hs = factorY(scaledHeight);
        if (ws < 0 || hs < 0) throw new IllegalArgumentException("image is corrupt");
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else {
            offscreen.drawImage(image, (int) Math.round(xs - ws/2.0),
                                       (int) Math.round(ys - hs/2.0),
                                       (int) Math.round(ws),
                                       (int) Math.round(hs), null);
        }
    }

    private void drawImageIFS(Image image, double r, double s, double theta, double phi, double e, double f) {
        double[] affine = new double[]{r * Math.cos(theta), -s * Math.sin(phi), r * Math.sin(theta),  s * Math.cos(phi), e * width, height*(1-f)};
        AffineTransform transform = new AffineTransform(affine);
        AffineTransform inverse = new AffineTransform();
        try {
            inverse = transform.createInverse();
        } catch (NoninvertibleTransformException e1) {
            e1.printStackTrace();
        }
        offscreen.transform(transform);
        offscreen.drawImage(image, 0, (int) (-height), null);
        offscreen.transform(inverse);
    }

    public void drawImage(Image image, double[][] IFSTable) {
        for (double[] IFS : IFSTable) {
            drawImageIFS(image, IFS[0], IFS[1], Math.toRadians(-IFS[2]), Math.toRadians(-IFS[3]), IFS[4], IFS[5]);
        }
    }

    public void drawFromCurrentImage(double[][] IFSTable) {
        BufferedImage current = copyOffScreenImage();
        clear(); drawImage(makeColorTransparent(current, clearColor), IFSTable);
    }

    public void saveImage() throws IOException {
        BufferedImage image = copyOnScreenImage();
        File file = new File("images/image.png");
        ImageIO.write(image, "png", file);
    }

    public boolean frameIsShowing() { return frame.isShowing(); }

    public BufferedImage getOnScreenImage() { return this.onscreenImage; }
    public BufferedImage getOffScreenImage() { return this.offscreenImage; }

    public BufferedImage copyOnScreenImage() { return copyImage(onscreenImage);}
    public BufferedImage copyOffScreenImage() { return copyImage(offscreenImage);}

    public static Image makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
    
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;
    
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };
    
        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    private static BufferedImage copyImage(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    private static BufferedImage createCompatibleBufferedImage(int width, int height, boolean allowTransparency) {
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        return gfxConfig.createCompatibleImage(width, height, allowTransparency ? Transparency.BITMASK : Transparency.OPAQUE);
    }
}
