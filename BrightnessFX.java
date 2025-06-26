import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class BrightnessFX implements ImageFX {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 50;
    private static final int YPOS = 140;
    private static final int SPEED = 3; // Speed of movement

    private GamePanel panel;

    private int x;
    private int y;
    private int dx; // Horizontal velocity

    private BufferedImage rainCloudImage;
    private BufferedImage copy;

    int brightness;
    int brightnessChange;

    public BrightnessFX(GamePanel p) {
        panel = p;

        x = (int) (Math.random() * (panel.getWidth() - WIDTH));
        y = YPOS;

        brightness = 0;
        brightnessChange = 5;

        rainCloudImage = ImageManager.loadBufferedImage("images/cloud.png");

        // Randomize initial direction
        dx = Math.random() < 0.5 ? SPEED : -SPEED;
    }

    private int truncate(int colorValue) {
        if (colorValue > 255)
            return 255;

        if (colorValue < 0)
            return 0;

        return colorValue;
    }

    private int brighten(int pixel) {
        int alpha, red, green, blue;
        int newPixel;

        alpha = (pixel >> 24) & 255;
        red = (pixel >> 16) & 255;
        green = (pixel >> 8) & 255;
        blue = pixel & 255;

        red = red + brightness;
        green = green + brightness;
        blue = blue + brightness;

        red = truncate(red);
        green = truncate(green);
        blue = truncate(blue);

        newPixel = blue | (green << 8) | (red << 16) | (alpha << 24);

        return newPixel;
    }

    public void draw(Graphics2D g2) {
        copy = ImageManager.copyImage(rainCloudImage);

        int imWidth = copy.getWidth();
        int imHeight = copy.getHeight();

        int[] pixels = new int[imWidth * imHeight];
        copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = brighten(pixels[i]);
        }

        copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

        g2.drawImage(copy, x, y, WIDTH, HEIGHT, null);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
    }

    public void update() {
        brightness = brightness + brightnessChange;

        if (brightness > 50) {
            brightness = 50;
            brightnessChange = -brightnessChange;
        } else if (brightness < -150) {
            brightness = -150;
            brightnessChange = -brightnessChange;
        }

        // Move the raincloud horizontally
        x += dx;

        // Change direction if hitting the panel boundaries
        if (x <= 0 || x + WIDTH >= panel.getWidth()) {
            dx = -dx; // Change direction
        }
    }

}