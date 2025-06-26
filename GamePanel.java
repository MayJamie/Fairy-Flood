import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {

    private static final int NUM_ALIENS = 4;

    private SoundManager soundManager;
    private Fairy bat;
    private Raindrop[] aliens;
    private Crystal crystal;
    private boolean alienDropped;
    private boolean isRunning;
    private boolean isPaused;
    private int score = 0;
    private int highScore = 0;

    private Thread gameThread;

    private BufferedImage image;
    private Image backgroundImage;

    private ImageFX imageFX;
    private ImageFX imageFX2;

    public GamePanel() {
        bat = null;
        aliens = null;
        crystal = null;
        alienDropped = false;
        isRunning = false;
        isPaused = false;

        soundManager = SoundManager.getInstance();
        soundManager.setVolume("appear", 0.6f);
        soundManager.setVolume("hit", 0.7f);
        soundManager.setVolume("background", 1f);
		soundManager.setVolume("sparkle", 0.7f);
        soundManager.setVolume("thunder", 0.8f);

        backgroundImage = ImageManager.loadImage("images/Background.jpg");

        image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
    }

    public void createGameEntities() {
        bat = new Fairy(this, 175, 300);
        aliens = new Raindrop[NUM_ALIENS];
        crystal = new Crystal(this);

        for (int i = 0; i < NUM_ALIENS; i++) {
            aliens[i] = new Raindrop(this, 80 + i * 80, 10, bat);
        }

        imageFX = new DisappearFX(this);
        imageFX2 = new BrightnessFX(this);
    }

    public void run() {
        try {
            isRunning = true;
            while (isRunning) {
                if (!isPaused) {
                    gameUpdate();
                }
                gameRender();
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateBat(int direction) {
        if (bat != null && !isPaused) {
            bat.move(direction);
        }
    }

    public void gameUpdate() {
        for (int i = 0; i < NUM_ALIENS; i++) {
            aliens[i].move();
        }

        // Check for collision between bat and crystal
        if (!crystal.isCollided() && bat.getBoundingRectangle().intersects(crystal.getBoundingRectangle())) {
            crystal.respawn(this);
            // Increment fairy points or perform any other action
            score++;
        }

        // Collision between alien and bat.
        for (int i = 0; i < NUM_ALIENS; i++){
            if (aliens[i].collidesWithBat()){
                soundManager.playClip("hit", false);
                aliens[i].setLocation();
                if (score > 0){
                    score--;
                }
            }
        }

        // Check collision between aliens and brightness effect
        BrightnessFX raincloud = (BrightnessFX) imageFX2;
        for (int i = 0; i < NUM_ALIENS; i++) {
            if (aliens[i].getBoundingRectangle().intersects(raincloud.getBoundingRectangle())) {
                aliens[i].respawn(this);
            }
        }

        // Collision between bat and brightness effect.
        if (bat.getBoundingRectangle().intersects(raincloud.getBoundingRectangle())) {
            soundManager.playClip("thunder", false);
        }

        imageFX.update();
        imageFX2.update();
    }

    public void gameRender() {
        // Double Buffering for the background image.
        BufferedImage offscreen = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        Graphics2D offscreenContext = offscreen.createGraphics();

        // Draw the game objects on the off-screen buffer.
        offscreenContext.drawImage(backgroundImage, 0, 0, null);

        if (bat != null) {
            bat.draw(offscreenContext);
        }

        if (aliens != null) {
            for (int i = 0; i < NUM_ALIENS; i++) {
                aliens[i].draw(offscreenContext);
            }
        }

        if (crystal != null) {
            crystal.draw(offscreenContext);
        }

        if (imageFX != null) {
            imageFX.draw(offscreenContext);
            imageFX2.draw(offscreenContext);
        }

        // Draw the off-screen buffer onto the actual screen.
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.drawImage(offscreen, 0, 0, 400, 400, null);
        g2.dispose();
    }

    public int getScore(){
        return score;
    }

    public int getHighScore(){
        if (score > highScore){
            highScore = score;
        }
        return highScore;
    }

    public void dropAlien() {
        if (!alienDropped) {
            gameThread = new Thread(this);
            gameThread.start();
            soundManager.setVolume("background", 0.7f);
            soundManager.playClip("background", true);
            alienDropped = true;
        }
    }

    public void startGame() {
        if (gameThread == null) {
            soundManager.setVolume("background", 0.7f);
            soundManager.playClip("background", true);
            createGameEntities();
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void restartGame() {
        isPaused = false;
        if (gameThread == null || !isRunning) {
            soundManager.setVolume("background", 0.7f);
            soundManager.playClip("background", true);
            createGameEntities();
            score = 0;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    public void pauseGame() {
        if (isRunning) {
            isPaused = !isPaused;
        }
    }

    public void endGame() {
        isRunning = false;
    }

    public boolean isOnBat(int x, int y) {
        return bat.isOnBat(x, y);
    }
}