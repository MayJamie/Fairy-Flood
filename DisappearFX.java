import java.util.Random;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;

public class DisappearFX implements ImageFX {

    private static final int WIDTH = 80;       // width of the image
    private static final int HEIGHT = 80;      // height of the image
    private static final int YPOS = 10;        // vertical position of the image

    private GamePanel panel;

    private int x;
    private int y;

    private BufferedImage spriteImage;        // image for sprite effect
    private BufferedImage copy;                // copy of image

    Graphics2D g2;

    private int alpha;                         // alpha value (for alpha transparency byte)
    private int alphaChange;                   // how to update alpha in game loop

    public DisappearFX(GamePanel p) {
        panel = p;

        Random random = new Random();
        x = random.nextInt(panel.getWidth() - WIDTH);
        y = YPOS;

        alpha = 255;                            // set to 255 (fully opaque)
        alphaChange = 10;                        // how to update alpha in game loop

        spriteImage = ImageManager.loadBufferedImage("images/sunshine.gif");
        copy = ImageManager.copyImage(spriteImage);
                                                // make a copy of the original image
    }

    public void draw(Graphics2D g2) {
        int imWidth = copy.getWidth();
        int imHeight = copy.getHeight();

        int[] pixels = new int[imWidth * imHeight];
        copy.getRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

        int a, red, green, blue, newValue;

        for (int i = 0; i < pixels.length; i++) {
            a = (pixels[i] >> 24);
            red = (pixels[i] >> 16) & 255;
            green = (pixels[i] >> 8) & 255;
            blue = pixels[i] & 255;

            if (a != 0) {
                newValue = blue | (green << 8) | (red << 16) | (alpha << 24);
                pixels[i] = newValue;
            }
        }

        copy.setRGB(0, 0, imWidth, imHeight, pixels, 0, imWidth);

        g2.drawImage(copy, x, y, WIDTH, HEIGHT, null);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
    }

    public void update() {
        // Increase or decrease alpha value within the range [10, 255]
        alpha -= alphaChange;

        // Reverse the alpha change direction when reaching the limits
        if (alpha <= 100 || alpha >= 255) {
            alphaChange = -alphaChange;
        }
    }
}
