package tetrisPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Frame extends JPanel implements KeyListener, ActionListener {

	private Queue<Block> blocks = new LinkedList<>();
	private ArrayList<Block> oldBlocks = new ArrayList<>();
	private Queue<Block> hold = new LinkedList<>();

	private int width = 10;
	private int height = 20;
	
	boolean canHold = true;

	private ArrayList<ArrayList<Integer>> map = new ArrayList<>();

	private ArrayList<ArrayList<Color>> colors = new ArrayList<>();

	private RenderMap renderer;

	private ArrayList<ArrayList<Integer>> trimmedTile = new ArrayList<>();

	private int count = 0;
	private int touchCount = 0;

	private int level = 29;

	private int score = 1;
	
	private int additionalScore = 0;
	
	private int threshold = 100;

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		renderer.paint(g, colors);
	
		if (!isDead) {

			Point curr = blocks.peek().getPosition();
			trimmedTile = getTrimmedTile();

			count++;

			if (curr.y >= map.size() - trimmedTile.size() - 1 || checkBottomCollision()) {
				touchCount++;
				if (touchCount == 30) {
					

					score += additionalScore;
					additionalScore = 0;
					
					oldBlocks.add(blocks.peek());
					blocks.remove();
					blocks.add(new Block((int) (Math.random() * 7) + 1));
					blocks.peek().setPosition(new Point(width / 2 - 2, 1));
					touchCount = 0;
					canHold = true;
				}

			}

			// implement gravity
			if (count % level == 0 && !checkBottomCollision()) {
				curr.y++;

				blocks.peek().setPosition(curr);

				while (curr.y >= map.size() - trimmedTile.size()) {
					curr.y--;
					blocks.peek().setPosition(curr);
				}

			}
			System.out.println(score);
			if (score > threshold && level != 1) {
				level--;
				threshold += 100;
			}

			updateBlockOnMap();
			
			additionalScore = removeIfCompleteRow();
			
			g.setFont(new Font("Calibri", Font.PLAIN, 50)); 
			g.drawString("Current Score: " + score, 325, 300);
		}
	}

	public static void main(String[] arg) {
		Frame f = new Frame();
	}

	JFrame frame;
	Store store;

	public Frame() {

		initMap();
		
		frame = new JFrame("Tetris Game");

		try {
			blocks.add(new Block((int) (Math.random() * 7) + 1));
			blocks.peek().setPosition(new Point(width / 2 - 2, 1));

			trimmedTile = getTrimmedTile();
			updateBlockOnMap();
			renderer = new RenderMap(new Point(0, 0), 30, map);
		} catch (IOException e) {

			System.out.println("Something went wrong.");
		}

		frame.setSize(new Dimension(900, 900));
		frame.setBackground(Color.blue);
		frame.addKeyListener(this);
		frame.add(this);

		frame.setResizable(false);

		Timer t = new Timer(16, this);
		t.start();

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);

		store = new Store();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (!isDead) {
			trimmedTile = getTrimmedTile();

			Point current = blocks.peek().getPosition();

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

				score += additionalScore;

				additionalScore = 0;
				
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

			if (e.getKeyCode() == 8) {
			removeRowAtCoordinate(19+1);
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

	public void updateBlockOnMap() {
		if (!isDead) {
			
			initMap();
			
			Point p = blocks.peek().getPosition();

			trimmedTile = getTrimmedTile();

			int trimmedWidth = trimmedTile.size();
			int trimmedHeight = trimmedTile.get(0).size();

			if ((p.y >= 0 && p.y < map.size() - trimmedWidth + 1)
					&& (p.x >= 0 && p.x < map.get(0).size() - trimmedHeight + 1)) {
				for (int y = p.y; y < p.y + trimmedWidth; y++) {
					for (int x = p.x; x < p.x + trimmedHeight; x++) {

						if (trimmedTile.get(y - p.y).get(x - p.x) != 0) {
							map.get(y).set(x, trimmedTile.get(y - p.y).get(x - p.x));

							colors.get(y).set(x, blocks.peek().getColor());

						}
					}
				}
			}

			for (Block tile : oldBlocks) {
				if (tile != null) {
					  if (!tile.isEmpty()) {
						  p = tile.getPosition();
				
							if (p != null) {
			
							if (p.y == 1) {
								death();
							}
			
							trimmedTile = tile.getTrimmedTile();
							trimmedWidth = trimmedTile.size();
							trimmedHeight = trimmedTile.get(0).size();
							if ((p.y >= 0 && p.y < map.size() - trimmedWidth + 1)
									&& (p.x >= 0 && p.x < map.get(0).size() - trimmedHeight + 1)) {
								for (int y = p.y; y < p.y + trimmedWidth; y++) {
									for (int x = p.x; x < p.x + trimmedHeight; x++) {
			
										if (trimmedTile.get(y - p.y).get(x - p.x) != 0) {
			
											map.get(y).set(x, trimmedTile.get(y - p.y).get(x - p.x));
											colors.get(y).set(x, tile.getColor());
										}
									}
								}
							}
				}
					  }
				}
			}
		}
	}

	boolean isDead = false;

	public void death() { // death logic
		isDead = true;
		System.out.println("Score: " + score);
		// Creating the main window of our application
		frame = new JFrame();

		// Release the window and quit the application when it has been closed
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// Creating a button and setting its action
		String name = JOptionPane.showInputDialog(null,
				"You Died! Your score is: " + score + ". \nPlease enter your name to save your score.", "Tetris",
				JOptionPane.PLAIN_MESSAGE);
		name = name.replaceAll("\\s+","");
		store.addScore(score, name);

		Score highest = store.getHighestScore();
		Score lowest = store.getLowestScore();

		JOptionPane.showMessageDialog(null,
				"Your score has been saved. The highest score is " + highest.getName() + " with a score of "
						+ highest.getScore() + "! The lowest score is " + lowest.getName() + " with a score of "
						+ lowest.getScore() + "!",
				"Tetris", JOptionPane.INFORMATION_MESSAGE);

		System.exit(-1);
	}

	public boolean checkLeftCollision() {
		if (checkAllCollisions().get(0).equals("left")) {
			return true;
		}
		return false;
	}

	public boolean checkRightCollision() {
		if (checkAllCollisions().get(1).equals("right")) {
			return true;
		}
		return false;
	}

	public boolean checkBottomCollision() {
		if (checkAllCollisions().get(2).equals("bottom")) {
			return true;
		}
		return false;
	}

	public boolean checkTopCollision() {
		if (checkAllCollisions().get(3).equals("top")) {
			return true;
		}
		return false;
	}

	public boolean checkCollisionsForRotation() {
		
		blocks.peek().rotate();
		
		updateBlockOnMap();
		
		boolean res = checkTopCollision() | checkBottomCollision();
		
		for (int i = 0; i < 3; i++) {
			blocks.peek().rotate();
		}
		
		updateBlockOnMap();
		
		
				return res;
		
	}

	public ArrayList<String> checkAllCollisions() {
		Point p = blocks.peek().getPosition();

		trimmedTile = getTrimmedTile();

		ArrayList<Point> leftCandidates = new ArrayList<>();

		ArrayList<Point> rightCandidates = new ArrayList<>();

		ArrayList<Point> bottomCandidates = new ArrayList<>();

		ArrayList<Point> topCandidates = new ArrayList<>();

		ArrayList<String> restrictions = new ArrayList<>();

		restrictions.add("");
		restrictions.add("");
		restrictions.add("");
		restrictions.add("");

		// Add candidates from left
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

		for (Point point : leftCandidates) {
			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(0, "left");
					}
				}
			}
		}

		for (Point point : rightCandidates) {

			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(1, "right");
					}
				}
			}
		}

		for (Point point : bottomCandidates) {

			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(2, "bottom");
					}
				}
			}
		}

		for (Point point : topCandidates) {

			if (0 <= point.y && point.y < height) {
				if (0 <= point.x && point.x < width) {
					if (map.get(point.y).get(point.x) == TileType.BLOCK
							| map.get(point.y).get(point.x) == TileType.WALL) {
						restrictions.set(3, "top");
					}
				}
			}
		}

		return restrictions;

	}
	
	public void removeRowAtCoordinate(int r) {
		
		ArrayList<Block> blockArray = new ArrayList<Block>(blocks);
		
		ArrayList<Block> oldBlockArray = new ArrayList<Block>(oldBlocks);
		
		blockArray.addAll(oldBlockArray);
		
		
		
		for (Block b : blockArray) {
			

			if (b.isEmpty()) {
				blocks.remove(b);
			}
			

			//System.out.println("R: " + r + );
			
			
			if (r > b.getPosition().y && r < b.getPosition().y+b.getTrimmedTile().size()-1) {
				int row = r-b.getPosition().y;
				System.out.println("REmoved at" + row);
				b.removeRow(row);
			}
			
		}
		
		for (Block b : blockArray) {
			if (b.getPosition().y <= r && !(b.equals(blocks.peek()))) {
				b.getPosition().y++;
			}
		}
		
		blocks.remove();
		blocks.add(blockArray.get(0));
		
		blockArray.remove(0);
		
		oldBlocks.clear();
		oldBlocks.addAll(blockArray);
		
		updateBlockOnMap();
		
	}
	
	public int removeIfCompleteRow() {
		int count = 0;
		
		for (int r = 1; r < map.size()-1; r++) {
			boolean rowIsSame =  true;
			for (int c = 1; c < map.get(0).size()-2; c++) {

				if ((map.get(r).get(c) != 2 | map.get(r).get(c+1) != 2))  {
						rowIsSame = false;
						break;
					
				}
			}
			
			if (rowIsSame) {
				removeRowAtCoordinate(r+3);
			}
			
			count++;
			
		}
		
		return count;
	}
	
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

}