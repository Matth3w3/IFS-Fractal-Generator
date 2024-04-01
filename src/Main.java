import java.awt.Dimension;
import java.awt.Toolkit;

public class Main {
    public static void main(String[] args) {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) (size.getHeight() * 0.9);
        int width = (int) (size.getWidth() * 0.9);
        int length = Math.min(height, width);
        FractalGenerator fGenerator = new FractalGenerator(length, length);
        fGenerator.start();
    }
}
