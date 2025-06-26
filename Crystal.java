import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Image;
import javax.swing.JPanel;

public class Crystal {

    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;

    private int x;
    private int y;

    private boolean collided;

    private Image crystalImage;

    public Crystal(JPanel panel) {
        respawn(panel);
        collided = false;

        crystalImage = ImageManager.loadImage("images/crystal.png");
    }

    public void draw(Graphics2D g2) {
        if (!collided) {
            g2.drawImage(crystalImage, x, y, WIDTH, HEIGHT, null);
        }
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
    }

    public void respawn(JPanel panel) {
      x = (int) (Math.random() * (panel.getWidth() - WIDTH));
      y = (int) (Math.random() * (panel.getHeight() - HEIGHT - 180)) + 180;
      SoundManager.getInstance().playClip("sparkle",false);
  }  

    public boolean isCollided() {
      return collided;
    }

    public void setCollided(boolean collided) {
        this.collided = collided;
    } 
}