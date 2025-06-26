import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.awt.Image;

public class Fairy {

   private JPanel panel;
   private int x;
   private int y;
   private int width;
   private int height;

   private int dx;
   private int dy;

   private Rectangle2D.Double bat;

   private Color backgroundColour;

   private Image batImage;
   private Image batLeftImage;
   private Image batRightImage;

   public Fairy (JPanel p, int xPos, int yPos) {
      panel = p;
      backgroundColour = panel.getBackground ();
      x = xPos;
      y = yPos;

      dx = 20;
      dy = 20;

      width = 50;
      height = 50;

      batLeftImage = ImageManager.loadImage ("images/FairyLeft.gif");
      batRightImage = ImageManager.loadImage ("images/FairyRight.gif");

      batImage = batRightImage;
   }

   public int getDx(){
      return dx;
   }

   public void setDx(int newDX){
      dx = newDX;
   }

   public void draw(Graphics2D g2) {
      g2.drawImage(batImage, x, y, width, height, null);
  }

   public void erase () {
      Graphics g = panel.getGraphics ();
      Graphics2D g2 = (Graphics2D) g;

      // erase bat by drawing a rectangle on top of it with the background colour

      g2.setColor (backgroundColour);
      g2.fill (new Rectangle2D.Double (x, y, width, height));

      g.dispose();
   }

   
public void move(int direction) {
   if (!panel.isVisible()) return;

   if (direction == 1) {
       x = x - dx;     // move left
       batImage = batLeftImage;
       if (x < -30)    // move to right of GamePanel
           x = 380;
   } else if (direction == 2) {
       x = x + dx; 
       batImage = batRightImage;
       if (x > 380)  
           x = -30;
   } else if (direction == 3) {
       y = y - dy;
       if (y < 0)
           y = 0;
   } else if (direction == 4) {
       y = y + dy;
       if (y > panel.getHeight() - height)
           y = panel.getHeight() - height;
   }
}

   public boolean isOnBat (int x, int y) {
      if (bat == null)
      	  return false;

      return bat.contains(x, y);
   }


   public Rectangle2D.Double getBoundingRectangle() {
      return new Rectangle2D.Double (x, y, width, height);
   }

   public boolean collidesWithCrystal(Crystal crystal) {
      Rectangle2D.Double batRect = new Rectangle2D.Double(x, y, width, height);
      Rectangle2D.Double crystalRect = crystal.getBoundingRectangle();
      return batRect.intersects(crystalRect);
  }

  public boolean collidesWithRaindrop(Raindrop raindrop){
      Rectangle2D.Double batRect = new Rectangle2D.Double(x, y, width, height);
      Rectangle2D.Double raindropRect = raindrop.getBoundingRectangle();
      return batRect.intersects(raindropRect);
  }
}