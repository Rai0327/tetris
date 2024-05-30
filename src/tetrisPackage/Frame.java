package tetrisPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.util.Queue;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

public class Frame extends JPanel implements KeyListener, ActionListener {

	private Queue<Block> blocks = new LinkedList<>();
	private ArrayList<Block> oldBlocks = new ArrayList<>();
	private Queue<Block> hold = new LinkedList<>();

	private int width = 10;
	private int height = 20;
	
	private FileManager fManager = new FileManager();
	
	boolean canHold = true;
	
	private Dimension saveButtonSize = new Dimension(400, 100);

	// Map of tiles, represented as numbers (see TileType)
	private ArrayList<ArrayList<Integer>> map = new ArrayList<>();

	// Map of colors
	private ArrayList<ArrayList<Color>> colors = new ArrayList<>();

	// Current tile, but "trimmed" into an ArrayList
	// "Trimmed" means that if a row of a tile is 0, that row is removed
	private ArrayList<ArrayList<Integer>> trimmedTile = new ArrayList<>();

	// Renderer that renders the tiles (including walls)
	private RenderMap renderer;

	// Tick count for gravity
	private int gravityTickCount = 0;
	
	// Count until block is respawned
	private int touchCount = 0;

	// Factor to tick gravity by
	// Higher means slower gravity
	// Lower means faster gravity
	private int gravityFactor = 29;

	// Score of game
	private int score = 1;
	
	// If dead or not
	private boolean isDead = false;
	
	// If paused or not
	private boolean isPaused = false;
	
	// Threshold of score
	private int threshold = 5;
	
	// Background color
	private Color bgColor = new Color(96, 204, 175);
	
	// Button color
	private Color buttonColor = new Color(19, 135, 104);
	
	private Font customFont;
	
	JPanel mainPanel = new JPanel();
  
	@Override
	public void paint(Graphics g) {
		
		// Update block on map
		updateBlockOnMap(false);

		// Paint frame
		super.paintComponent(g);
		
		// Paint renderer
		renderer.paint(g, colors, getNext(blocks), hold.peek());
		
		// Checking for death logic
		if (blocks.peek().getPosition().y == 1 && checkBottomCollision()) {
			try {
				death();
			} catch (NullPointerException e) {
				System.exit(-1);
			} 
			isDead = true;
		}
	
		if (!isDead | !isPaused) {
			
			renderer.paint(g, colors, getNext(blocks), hold.peek());

			Point curr = blocks.peek().getPosition();
			trimmedTile = getTrimmedTile();
			
			if (curr != null) {
        
			gravityTickCount++;

			if (curr.y >= map.size() - trimmedTile.size() - 1 || checkBottomCollision()) {
				touchCount++;
				if (touchCount == 30) {
					
					updateBlockOnMap(true);
					
					oldBlocks.add(blocks.peek());
					blocks.remove();
					blocks.add(new Block((int) (Math.random() * 7) + 1));
					blocks.peek().setPosition(new Point(width / 2 - 2, 1));
					touchCount = 0;
					canHold = true;
				}

			}

			// implement gravity
			if (gravityTickCount % gravityFactor == 0 && !checkBottomCollision()) {
				curr.y++;

				blocks.peek().setPosition(curr);

				while (curr.y >= map.size() - trimmedTile.size()) {
					curr.y--;
						blocks.peek().setPosition(curr);
					}
	
				}
				while (score > threshold && gravityFactor != 1) {
					gravityFactor--;
					threshold += 5;
				}
	
				updateBlockOnMap(false);
				
				customFont = customFont.deriveFont(40f);
				g.setFont(customFont); 
				g.drawString("Current Score: " + score, 325, 50);
			}

			score += removeIfCompleteRow();
			
		} else {
			renderer.paintDeath(g);
		}
	}

	public static void main(String[] arg) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Frame f = new Frame();
	}

	JFrame frame;
	Store store;
	SimpleAudioPlayer audioPlayer;
	
	public Frame() throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		initMap();
		
		JPanel panel = new JPanel();
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel textPanel = new JPanel();

		JButton openButton = new JButton("Open texture pack");
		openButton.addActionListener(this);
		openButton.setPreferredSize(saveButtonSize);
		openButton.setMinimumSize(saveButtonSize);
		openButton.setMaximumSize(saveButtonSize);
		openButton.setFocusable(false);
		openButton.setMnemonic(79);
		openButton.setBackground(buttonColor);
		panel.add(openButton);
		
		frame = new JFrame("Tetris Game");
		try {
			
			customFont= Font.createFont(Font.TRUETYPE_FONT, new File("./font/PixelifySans-Regular.ttf"));

			
			blocks.add(new Block((int) (Math.random() * 7) + 1));
			blocks.add(new Block((int) (Math.random() * 7) + 1));
			blocks.peek().setPosition(new Point(width / 2 - 2, 1));

			trimmedTile = getTrimmedTile();

			initMap();
			updateBlockOnMap(false);
			
			renderer = new RenderMap(new Point(0, 0), 30, map, getNext(blocks), fManager, bgColor, customFont);
		} catch (IOException | FontFormatException e) {

			System.out.println("Something went wrong.");
		}


		customFont = customFont.deriveFont(30f);
		openButton.setFont(customFont);
		
		frame.setSize(new Dimension(900, 900));
		frame.setMinimumSize(new Dimension(900, 900));
		frame.setMaximumSize(new Dimension(900, 900));
		frame.setPreferredSize(new Dimension(900, 900));
		frame.setBackground(bgColor);
		frame.addKeyListener(this);
		

        panel.setPreferredSize(saveButtonSize);
		panel.setMinimumSize(saveButtonSize);
		panel.setMaximumSize(saveButtonSize);
        panel.setSize(getPreferredSize());
        panel.setBackground(bgColor);
        
		mainPanel.add(this);
		mainPanel.add(panel);
		
		mainPanel.addKeyListener(this);
		mainPanel.setFocusable(true);
		
		this.setBackground(bgColor);
        mainPanel.setBackground(bgColor);
        
		frame.add(mainPanel);
		

		frame.setResizable(false);

		Timer t = new Timer(16, this);
		t.start();

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		

		store = new Store();
		
		try {
			 audioPlayer = new SimpleAudioPlayer(); 
	  
			 audioPlayer.play();
		} catch(Error e) {
			System.out.println("error with music");
		}
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (!isDead && !isPaused) {
			trimmedTile = getTrimmedTile();

			Point current = blocks.peek().getPosition();
			
			if (current == null) {
				blocks.add(new Block((int) (Math.random() * 7) + 1));
				blocks.peek().setPosition(new Point(width / 2 - 2, 1));
				current = blocks.peek().getPosition();
			}

			if ((e.getKeyCode() == 87 || e.getKeyCode() == 38)) {

				if (!checkCollisionsForRotation()) {

					while (current.x >= map.get(0).size() - trimmedTile.get(0).size()) {
						current.x -= 1;
						blocks.peek().setPosition(current);
					}

					while (current.y >= map.size() - trimmedTile.size()) {
						current.y -= 1;
						blocks.peek().setPosition(current);
					}

					if (map.get(0).size() - current.x > trimmedTile.size()) {
						blocks.peek().rotate();
					}

				}

			}
			if ((e.getKeyCode() == 68 || e.getKeyCode() == 39) && !checkRightCollision()) {

				if (current.x < map.get(0).size() - trimmedTile.get(0).size() - 1) {

					current.x += 1;

					blocks.peek().setPosition(current);

				}

			}
			if ((e.getKeyCode() == 65 || e.getKeyCode() == 37) && !checkLeftCollision()) {

				if (current.x > 1) {

					current.x -= 1;

					blocks.peek().setPosition(current);

				}
			}
			if ((e.getKeyCode() == 83 || e.getKeyCode() == 40) && !checkBottomCollision()) {

				if (current.y < map.size() - trimmedTile.size() - 1) {

					current.y += 1;

					blocks.peek().setPosition(current);

				}
			}

			if (e.getKeyCode() == 32) {
				// adjust when implementing switching blocks
				while (current.y < map.size() - trimmedTile.size() - 1 && !checkBottomCollision()) {
					current.y++;
				}

				blocks.peek().setPosition(current);

				updateBlockOnMap(true);
				
				oldBlocks.add(blocks.peek());
				blocks.remove();
				blocks.add(new Block((int) (Math.random() * 7) + 1));
				blocks.peek().setPosition(new Point(width / 2 - 2, 1));
				touchCount = 0;
				canHold = true;
			}
			
			if (e.getKeyCode() == 67 && canHold) {
				hold.add(blocks.peek());
				if (hold.size() == 1) {
					blocks.remove();
				} else {
					setFirstElement(blocks, hold.remove());
				}
				hold.peek().reset();
				blocks.add(new Block((int) (Math.random() * 7) + 1));
				blocks.peek().setPosition(new Point(width / 2 - 2, 1));
				touchCount = 0;
				canHold = false;
			}
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
        // if the user presses the save button show the save dialog
        String com = arg0.getActionCommand();
 
        if (com != null && com.equals("Open texture pack")) {
            
            JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            j.setCurrentDirectory(new java.io.File("."));
            j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            // Pauses program
    		isPaused = true;
    		
            int approve = j.showOpenDialog(null);
            
            // Executes after the dialog is closed
            isPaused = false;
          
            if (approve == JFileChooser.APPROVE_OPTION) {
                // set the label to the path of the selected directory
            	try {
					renderer.refreshFiles(j.getSelectedFile());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                //l.setText(j.getSelectedFile().getAbsolutePath());

            }
            
            getTopLevelAncestor().requestFocus(); // Required to regain controls after menu!
            
        }
		// TODO Auto-generated method stub
		repaint();
	}

	public void initMap() {
		map.clear();

		ArrayList<Integer> temp = new ArrayList<>();
		ArrayList<Color> tempC = new ArrayList<>();

		for (int i = 0; i < width; i++) {
			temp.add(TileType.WALL);
			tempC.add(new Color(0, 0, 0));
		}

		map.add(temp);

		colors.add(tempC);

		for (int j = 1; j < height - 1; j++) {
			ArrayList<Integer> temp1 = new ArrayList<>();
			ArrayList<Color> temp2C = new ArrayList<>();
			for (int i = 0; i < width; i++) {
				if (i == 0) {
					temp1.add(TileType.WALL);
				} else if (i == width - 1) {
					temp1.add(TileType.WALL);
				} else {
					temp1.add(TileType.EMPTY);
				}

				temp2C.add(new Color(0, 0, 0));
			}

			map.add(temp1);
			colors.add(temp2C);
		}

		ArrayList<Integer> temp2 = new ArrayList<>();
		ArrayList<Color> temp3C = new ArrayList<>();
		for (int i = 0; i < width; i++) {
			temp2.add(TileType.WALL);
			temp3C.add(new Color(0, 0, 0));
		}

		map.add(temp2);
		colors.add(temp3C);

	}

	public void updateBlockOnMap(boolean permanent) {
		if (!isDead && !isPaused) {
			
			Point p = blocks.peek().getPosition();

			trimmedTile = getTrimmedTile();

			int trimmedWidth = trimmedTile.size();
			int trimmedHeight = trimmedTile.get(0).size();
			


			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
				
					if (x == width-1 | y == height-1) {
						map.get(y).set(x, TileType.WALL);
					}
					else if (map.get(y).get(x) == TileType.SELECTED) {
						map.get(y).set(x, TileType.EMPTY);
						
					}
				}
			}
			
			
			if ((p.y >= 0 && p.y < map.size() - trimmedWidth + 1)
					&& (p.x >= 0 && p.x < map.get(0).size() - trimmedHeight + 1)) {
				for (int y = p.y; y < p.y + trimmedWidth; y++) {
					for (int x = p.x; x < p.x + trimmedHeight; x++) {
						
						if (trimmedTile.get(y - p.y).get(x - p.x) != 0) {
							if (!permanent) {
								map.get(y).set(x, trimmedTile.get(y-p.y).get(x-p.x));
							} else {
								map.get(y).set(x, TileType.BLOCK);
							}

							colors.get(y).set(x, blocks.peek().getColor());

						}
					}
				}
			}

		}
	}

	public void death() { // death logic
		if (isDead != true) {
			
			 audioPlayer.stop();

			isDead = true;
			System.out.println("Score: " + score);
			// Creating the main window of our application
			frame = new JFrame();
	
			// Release the window and quit the application when it has been closed
			frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	
			// Creating a button and setting its action
			String name = "";
			name = JOptionPane.showInputDialog(null,
					"You Died! Your score is: " + score + ". \nPlease enter your name to save your score.", "Tetris",
					JOptionPane.PLAIN_MESSAGE);
			if (name != null) {
				name = name.replaceAll("\\s+","");
				while (name.length() == 0 ) {
					name = JOptionPane.showInputDialog(null,
							"Please enter a valid name to save your score.", "Tetris",
							JOptionPane.PLAIN_MESSAGE);
					if (name != null) {
						name = name.replaceAll("\\s+","");
					}
				}
				name = name.replaceAll("\\s+","");
				store.addScore(score, name);
		
				Score highest = store.getHighestScore();
				Score lowest = store.getLowestScore();
		
				JOptionPane.showMessageDialog(null,
						"Your score has been saved. The highest score is " + highest.getName() + " with a score of "
								+ highest.getScore() + "! The lowest score is " + lowest.getName() + " with a score of "
								+ lowest.getScore() + "!",
						"Tetris", JOptionPane.INFORMATION_MESSAGE);
			}
	
			System.exit(-1);
			
		}
	}

	public boolean checkLeftCollision() {
		return checkAllCollisions().get(0).equals("left");
	}

	public boolean checkRightCollision() {
		return checkAllCollisions().get(1).equals("right");
	}

	public boolean checkBottomCollision() {
		return checkAllCollisions().get(2).equals("bottom");
	}

	public boolean checkTopCollision() {
		return checkAllCollisions().get(3).equals("top");
	}

	public boolean checkCollisionsForRotation() {

		// Create variables representing old map
		ArrayList<ArrayList<Integer>> oldMap =  new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Color>> oldColors = new ArrayList<ArrayList<Color>>();
		
		// Deep copies existing map as an "old" map
		oldMap = clearMapAndClone(oldMap, map);
		oldColors = clearMapAndClone(oldColors, colors);
		
		// Rotates current block
		blocks.peek().rotate();
		
		// Updates map to new state
		updateBlockOnMap(false);
		
		// Check if any block collides with wall or already placed block
		boolean result = false;
		
		for (int r = 0; r < oldMap.size(); r++) {
			for (int c = 0; c < oldMap.get(r).size(); c++) {
				// Check if current block is selected type, then check if it collides with wall or already placed block
				// Uses the oldMap to find walls and old blocks before collision
				if (map.get(r).get(c) == TileType.SELECTED && (oldMap.get(r).get(c) == TileType.WALL || oldMap.get(r).get(c) == TileType.BLOCK)) {
					result = true;
				}
			}
		}
		
		// Rotate block back to original state
		for (int i = 0; i < 3; i++) {
			blocks.peek().rotate();
		}
		
		// Clone back map and colors to original arrays
		map = clearMapAndClone(map, oldMap);
		colors = clearMapAndClone(colors, oldColors);
		
		// Update map and colors back to original state
		updateBlockOnMap(false);
		
		// True if collides, false if not collides
		return result;
		
	}
	
	/**
	 * Deep clone a 2D array (map)
	 * @param <T> Type parameter T
	 * @param prevMap Previous map
	 * @param currentMap Current map
	 * @return prevMap Data from current map is cloned to previous map
	 */
	public <T> ArrayList<ArrayList<T>> clearMapAndClone(ArrayList<ArrayList<T>> prevMap,  ArrayList<ArrayList<T>> currentMap) {
		prevMap.clear();
		
		for(ArrayList<T> arr : currentMap) {
		    prevMap.add((ArrayList<T>) arr.clone());
		}
		return prevMap;
	}

	public ArrayList<String> checkAllCollisions() {
		Point p = blocks.peek().getPosition();

		trimmedTile = getTrimmedTile();

		ArrayList<Point> leftCandidates = new ArrayList<>();

		ArrayList<Point> rightCandidates = new ArrayList<>();

		ArrayList<Point> bottomCandidates = new ArrayList<>();

		ArrayList<Point> topCandidates = new ArrayList<>();

		// List of restrictions, i.e. left, right, top, bottom
		ArrayList<String> restrictions = new ArrayList<>();

		restrictions.add("");
		restrictions.add("");
		restrictions.add("");
		restrictions.add("");

		// Add candidates from left
		if (p!= null) {
			for (int r = 0; r < trimmedTile.size(); r++) {
				for (int c = 0; c < trimmedTile.get(r).size(); c++) {
	
					int x = p.x + c - 1;
					int y = p.y + r;
	
					if (0 <= y && x < height) {
						if (0 <= y && x < width) {
							if (trimmedTile.get(r).get(c) != TileType.EMPTY) {
								leftCandidates.add(new Point(x, y));
								break;
							}
						}
					}
				}
			}
	
			// Add candidates from right
			for (int r = trimmedTile.size() - 1; r >= 0; r--) {
				for (int c = trimmedTile.get(r).size() - 1; c >= 0; c--) {
	
					int x = p.x + c + 1;
					int y = p.y + r;
	
					if (0 <= y && x < height) {
						if (0 <= y && x < width) {
	
							if (trimmedTile.get(r).get(c) != TileType.EMPTY) {
								rightCandidates.add(new Point(p.x + c + 1, p.y + r));
								break;
							}
	
						}
					}
				}
			}
	
			// Add candidates from bottom
			for (int c = trimmedTile.get(0).size() - 1; c >= 0; c--) {
				for (int r = trimmedTile.size() - 1; r >= 0; r--) {
	
					int x = p.x + c;
					int y = p.y + r + 1;
					if (0 <= y && x < height) {
						if (0 <= y && x < width) {
							if (trimmedTile.get(r).get(c) != TileType.EMPTY) {
								bottomCandidates.add(new Point(x, y));
								break;
							}
						}
					}
				}
			}
	
			// Add candidates from top
			for (int c = 0; c < trimmedTile.get(0).size(); c++) {
				for (int r = 0; r < trimmedTile.size(); r++) {
					int x = p.x + c;
					int y = p.y + r - 1;
	
					if (0 <= y && x < height) {
						if (0 <= y && x < width) {
							if (trimmedTile.get(r).get(c) != TileType.EMPTY) {
								topCandidates.add(new Point(x, y));
								break;
							}
						}
					}
				}
			}
		}

		for (Point point : leftCandidates) {
			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK | map.get(point.y).get(point.x) == TileType.SELECTED
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(0, "left");
					}
				}
			}
		}

		for (Point point : rightCandidates) {

			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK |  map.get(point.y).get(point.x) == TileType.SELECTED
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(1, "right");
					}
				}
			}
		}

		for (Point point : bottomCandidates) {

			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK |  map.get(point.y).get(point.x) == TileType.SELECTED
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(2, "bottom");
					}
				}
			}
		}

		for (Point point : topCandidates) {

			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK | map.get(point.y).get(point.x) == TileType.SELECTED
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(3, "top");
					}
				}
			}
		}

		return restrictions;

	}
	
	/**
	 * Removes row at coordinate r
	 * @param r Coordinate of row
	 */
	public void removeRowAtCoordinate(int r) {
		// Remove at coordinate r
		map.remove(r);
		colors.remove(r);
		
		// Create new ArrayList to append back at beginning
		ArrayList<Color> newColors = new ArrayList<Color>();
		ArrayList<Integer> newRow = new ArrayList<Integer>();
		
		// Add wall at beginning of list
		newRow.add(TileType.WALL);
		newColors.add(new Color(0, 0, 0));
		
		// Add empty spaces in middle
		for (int c = 1; c < map.get(r).size()-1; c++) {
			newRow.add(TileType.EMPTY);
			newColors.add(new Color(0, 0, 0));
		}
		
		// Add wall at end of list
		newRow.add(TileType.WALL);
		newColors.add(0, new Color(0, 0, 0));

		
		// Add row to the beginning of the map
		map.add(1, newRow);
		colors.add(1, newColors);
		
		// Update map
		updateBlockOnMap(false);
		
	}
	
	/**
	 * Removes rows if complete, returns score
	 * @return score - number of rows removed
	 */
	public int removeIfCompleteRow() {
		// Counts number of completed rows
		int count = 0;
		
		for (int r = 1; r < map.size()-1; r++) {
			
			// Sum variable
			int sum = 0;
			
			boolean allBlock = true;
			
			// Sums row
			for (int c = 1; c < map.get(0).size()-2; c++) {
				allBlock &= (map.get(r).get(c) == TileType.BLOCK) && map.get(r).get(c).equals(map.get(r).get(c+1));
			}
			
			// If sum is entirely BLOCK, then remove row
			// Prevents removal of row when SELECTED block travels through
			if (allBlock) {
				removeRowAtCoordinate(r);
				// Success! Increment count by 1
				count++;
			}
			
		}
		
		return count;
	}
	
	/**
	 * Returns the current tile, but trimmed so that no row is entirely empty
	 * @return trimmedTile - 2D array of trimmed tile
	 */
	public ArrayList<ArrayList<Integer>> getTrimmedTile() {
		if (blocks.peek().isEmpty()) {
			blocks.remove();
			return getTrimmedTile();
		} else if (blocks.isEmpty()) {
			return null;
		} 
		else {
			return blocks.peek().getTrimmedTile();
		}
		
	}
	
	public void setFirstElement(Queue<Block> q, Block b) {
		Queue<Block> temp = new LinkedList<>();
		temp.add(b);
		q.remove();
		while (!q.isEmpty()) {
			temp.add(q.remove());
		}
		while (!temp.isEmpty()) {
			q.add(temp.remove());
		}
	}
	
	public Block getNext(Queue<Block> q) {
		int count = 0;
		for (Block el : q) {
			if (count != 0) {
				return el;
			}
			count++;
		}
		return null;
	}
}
