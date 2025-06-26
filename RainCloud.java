import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class RainCloud {

    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;
    private static final int DARKEN_INCREMENT = 2;

    private int x;
    private int y;
    private Image rainCloudImage;
    private int darkness;
    private boolean darkening;

    public RainCloud(JPanel panel) {
        respawn(panel);
        rainCloudImage = ImageManager.loadImage("images/raincloud.gif");
        darkness = 0;
        darkening = true;
    }

    public void draw(Graphics2D g2) {
        // Adjust brightness based on darkness
        int r = 255 - darkness;
        int g = 255 - darkness;
        int b = 255 - darkness;
        g2.setColor(new Color(r, g, b));
        g2.drawImage(rainCloudImage, x, y, WIDTH, HEIGHT, null);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
    }

    public void respawn(JPanel panel) {
        x = (int) (Math.random() * (panel.getWidth() - WIDTH));
        y = (int) (Math.random() * (panel.getHeight() - HEIGHT - 150)) + 150;
    }

    public void update() {
        // Update darkness
        if (darkening) {
            darkness += DARKEN_INCREMENT;
            if (darkness >= 255) {
                darkness = 255;
                darkening = false;
            }
        } else {
            darkness -= DARKEN_INCREMENT;
            if (darkness <= 0) {
                darkness = 0;
                darkening = true;
            }
        }
    }
}