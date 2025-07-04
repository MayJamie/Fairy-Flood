import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for Layout Managers
import java.awt.event.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame 
				implements ActionListener,
					   KeyListener,
					   MouseListener
{
	// declare instance variables for user interface objects

	// declare labels 

	private JLabel statusBarL;
	private JLabel scoreL;
	private JLabel highScoreL;

	// declare text fields

	private JTextField statusBarTF;
	public JTextField scoreTF;
	public JTextField highScoreTF;

	// declare buttons

	private JButton startB;
	private JButton pauseB;
	private JButton endB;
	private JButton restartB;
	private JButton focusB;
	private JButton exitB;

	private Container c;

	private JPanel mainPanel;
	private GamePanel gamePanel;

	private Timer scoreUpdateTimer; // Timer for updating scores

	@SuppressWarnings({"unchecked"})
	public GameWindow() {
 
		setTitle ("Fairy Flood!!!");
		setSize (700, 585);

		// create user interface objects
		// Create timer for updating scores

		// create labels

		statusBarL = new JLabel ("Application Status: ");
		scoreL = new JLabel("Score: ");
		highScoreL = new JLabel("High Score: ");

		// create text fields and set their colour, etc.

		statusBarTF = new JTextField (25);
		scoreTF = new JTextField (25);
		highScoreTF = new JTextField (25);

		statusBarTF.setEditable(false);
		scoreTF.setEditable(false);
		highScoreTF.setEditable(false);

		statusBarTF.setBackground(Color.CYAN);
		scoreTF.setBackground(Color.YELLOW);
		highScoreTF.setBackground(Color.GREEN);

		// create buttons

	        startB = new JButton ("Start Game");
	        pauseB = new JButton ("Pause Game");
	        endB = new JButton ("End Game");
		restartB = new JButton ("Start New Game");
	        focusB = new JButton ("Focus on Key");
		exitB = new JButton ("Exit");


		// add listener to each button (same as the current object)

		startB.addActionListener(this);
		pauseB.addActionListener(this);
		endB.addActionListener(this);
		restartB.addActionListener(this);
		focusB.addActionListener(this);
		exitB.addActionListener(this);
		
		// create mainPanel

		mainPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		mainPanel.setLayout(flowLayout);

		GridLayout gridLayout;

		// create the gamePanel for game entities

		gamePanel = new GamePanel();
    gamePanel.setPreferredSize(new Dimension(400, 400));

		// create infoPanel

		JPanel infoPanel = new JPanel();
		gridLayout = new GridLayout(3, 2);
		infoPanel.setLayout(gridLayout);
		infoPanel.setBackground(new Color (135,206,235));

		// add user interface objects to infoPanel
	
		infoPanel.add (statusBarL);
		infoPanel.add (statusBarTF);

		infoPanel.add (scoreL);
		infoPanel.add (scoreTF);		

		infoPanel.add (highScoreL);
		infoPanel.add (highScoreTF);

		
		// create buttonPanel

		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(2, 3);
		buttonPanel.setLayout(gridLayout);

		// add buttons to buttonPanel

		buttonPanel.add (startB);
		buttonPanel.add (pauseB);
		buttonPanel.add (endB);
		buttonPanel.add (restartB);
		buttonPanel.add (focusB);
		buttonPanel.add (exitB);

		// add sub-panels with GUI objects to mainPanel and set its colour

		mainPanel.add(infoPanel);
		mainPanel.add(gamePanel);
		mainPanel.add(buttonPanel);
		mainPanel.setBackground(new Color(176, 224, 230));

		// set up mainPanel to respond to keyboard and mouse

		gamePanel.addMouseListener(this);
		mainPanel.addKeyListener(this);

		// add mainPanel to window surface

		c = getContentPane();
		c.add(mainPanel);

		// set properties of window

		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);

		// set status bar message

		statusBarTF.setText("Application started.");

		//

		scoreTF.setText("" + gamePanel.getScore());
		highScoreTF.setText("" + gamePanel.getHighScore());

		scoreUpdateTimer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					updateScores();
			}
		});
		scoreUpdateTimer.start();
	}

	private void updateScores() {
		scoreTF.setText(Integer.toString(gamePanel.getScore()));
		highScoreTF.setText(Integer.toString(gamePanel.getHighScore()));
	}

	// implement single method in ActionListener interface

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		
		statusBarTF.setText(command + " button clicked.");

		if (command.equals(startB.getText())) {
			gamePanel.startGame();
		}

		if (command.equals(pauseB.getText())) {
			gamePanel.pauseGame();
		}

/*
		if (command.equals("Pause Game")) {
			gamePanel.pauseGame();
			pauseB.setText ("Resume");
		}

		if (command.equals("Resume")) {
			gamePanel.pauseGame();
			pauseB.setText ("Pause Game");
		}
*/
		
		if (command.equals(endB.getText())) {
			gamePanel.endGame();
		}

		if (command.equals(restartB.getText()))
			gamePanel.restartGame();

		if (command.equals(focusB.getText()))
			mainPanel.requestFocus();

		if (command.equals(exitB.getText()))
			System.exit(0);

		mainPanel.requestFocus();
	}


	// implement methods in KeyListener interface
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		String keyText = e.getKeyText(keyCode);

		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			gamePanel.updateBat (1);
		}

		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			gamePanel.updateBat (2);
		}

		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			gamePanel.updateBat (3);
		}

		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			gamePanel.updateBat (4);
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}


	// implement methods in MouseListener interface

	public void mouseClicked(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();

		if (gamePanel.isOnBat(x, y)) {
			statusBarTF.setText ("Mouse click on bat!");
			statusBarTF.setBackground(Color.RED);
		}
		else {
			statusBarTF.setText ("");
			statusBarTF.setBackground(Color.CYAN);
		}

		// mouseTF.setText("(" + x +", " + y + ")");

	}


	public void mouseEntered(MouseEvent e) {
	
	}

	public void mouseExited(MouseEvent e) {
	
	}

	public void mousePressed(MouseEvent e) {
	
	}

	public void mouseReleased(MouseEvent e) {
	
	}

}