package tetrisPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Frame extends JPanel implements KeyListener, ActionListener {
	
	private Queue<Block> blocks = new LinkedList<Block>();
	private ArrayList<Block> oldBlocks = new ArrayList<Block>();
	
	private int width = 10;
	private int height = 20;
	
	private ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>(); 

	private RenderMap renderer;
	
	//private DisplayBlock dispBlock;
	
	private ArrayList<ArrayList<Integer>> trimmedTile = new ArrayList<ArrayList<Integer>>();
	
	private int count = 0;
	private int touchCount = 0;
	
	private int level = 29;
	

	public void paint(Graphics g) {

		super.paintComponent(g);
			
		renderer.paint(g);
		
		Point curr = blocks.peek().getPosition();
		trimmedTile = blocks.peek().getTrimmedTile();
		
		count++;
		
		if (curr.y >= map.size()-trimmedTile.size()-1 || collides()) {
			touchCount++;
			if (touchCount == 30) {
				oldBlocks.add(blocks.peek());
				blocks.remove();
				blocks.add(new Block((int) (Math.random() * 7) + 1));
				blocks.peek().setPosition(new Point(width/2-2, 1));
				touchCount = 0;
			}
		}
		
		//implement gravity
		if (count % level == 0 && !collides()) {
			curr.y++;
			
			blocks.peek().setPosition(curr);
			
			while (curr.y >= map.size()-trimmedTile.size()) {
				curr.y--;
				blocks.peek().setPosition(curr);
			}
			
			updateBlockOnMap();
		}
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		
		initMap();
		
		try {
			blocks.add(new Block((int) (Math.random() * 7) + 1));
			blocks.peek().setPosition(new Point(width/2-2, 1));

			trimmedTile = blocks.peek().getTrimmedTile();
			updateBlockOnMap();
			renderer = new RenderMap(new Point(0,0), 30, map);
			renderer.randomizeBlockColor();
		} catch (IOException e) {

		      System.out.println("Something went wrong.");
		}
		
		JFrame frame = new JFrame("Tetris Game");
		frame.setSize(new Dimension(900, 900));
		frame.setBackground(Color.blue);
		frame.addKeyListener(this);
		frame.add(this);
		
		
		frame.setResizable(false);
		
		Timer t = new Timer(16, this);
		t.start();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
    	

		trimmedTile = blocks.peek().getTrimmedTile();

		Point current = blocks.peek().getPosition();
		
    	if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {

    		blocks.peek().rotate();
    		
    		updateBlockOnMap();
    		
    		while (current.x >= map.get(0).size() - trimmedTile.get(0).size()) {
    			current.x--;
    			blocks.peek().setPosition(current);
    		}
    		
    		while (current.y >= map.size()-trimmedTile.size()) {
    			current.y--;
    			blocks.peek().setPosition(current);
    		}
    		
    		updateBlockOnMap();
    		
    	}
    	if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
    		
    		if (current.x < map.get(0).size()-trimmedTile.get(0).size()-1) {
    		
    		current.x += 1;
    		
    		blocks.peek().setPosition(current);

    		updateBlockOnMap();
    		}
    		
    	}
    	if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
    		
    		if (current.x > 1) {
    		
    		current.x -= 1;
    		
    		blocks.peek().setPosition(current);

    		updateBlockOnMap();
    		}
    	}
    	if ((e.getKeyCode() == 83 || e.getKeyCode() == 40) && !collides()) {

    		
    		if (current.y < map.size()-trimmedTile.size()-1) {
    		
    		
    		current.y += 1;
    		
    		blocks.peek().setPosition(current);

    		updateBlockOnMap();
    		}
    	}
    	
    	if (e.getKeyCode() == 32) {
    		//adjust when implementing switching blocks
    		while (current.y <= map.size()-trimmedTile.size()) {
    			current.y++;
    		}
    		
    		blocks.peek().setPosition(current);

    		updateBlockOnMap();
    	}
    	
    	this.paint(getGraphics());
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
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < width; i++) {
			temp.add(TileType.WALL);
		}
		
		map.add(temp);
		
		
		for (int j = 1; j < height-1; j++) {
			ArrayList<Integer> temp1 = new ArrayList<Integer>();
			for (int i = 0; i < width; i++) {
				if (i == 0) {
					temp1.add(TileType.WALL);
				} else if (i == width-1) {
					temp1.add(TileType.WALL);
				} else {
					temp1.add(TileType.EMPTY);
				}
			}
			
			map.add(temp1);
		}
		
		
		ArrayList<Integer> temp2 = new ArrayList<Integer>();
		for (int i = 0; i < width; i++) {
			temp2.add(TileType.WALL);
		}
		
		map.add(temp2);
	
	}
	
	public void updateBlockOnMap() {
		
		Point p = blocks.peek().getPosition();
		
		trimmedTile = blocks.peek().getTrimmedTile();
		
		int trimmedWidth = trimmedTile.size();
		int trimmedHeight = trimmedTile.get(0).size();
		
		System.out.println("TrimW" + trimmedWidth);

		System.out.println("TrimH" + trimmedHeight);
		
		System.out.println(trimmedTile);
		
		initMap();
		
		if ((p.y >= 0 && p.y < map.size()-trimmedWidth+1) && (p.x >= 0 && p.x < map.get(0).size()-trimmedHeight+1)) {
			for (int y = p.y; y < p.y+trimmedWidth; y++) {
				for (int x = p.x; x < p.x+trimmedHeight; x++) {
					
					if (trimmedTile.get(y-p.y).get(x-p.x) != 0) {
						map.get(y).set(x, trimmedTile.get(y-p.y).get(x-p.x));
					}
				}
			}
		}
	}
	
public boolean collides() {
		
		Point p1 = blocks.peek().getPosition();
		
		trimmedTile = blocks.peek().getTrimmedTile();
		
		for (Block b : oldBlocks) {
			Point p2 = b.getPosition();
			ArrayList<ArrayList<Integer>> bTrimmed = b.getTrimmedTile();
			int[][] space = new int[height][width];
			if (p1.y < p2.y) {
				if (p1.x < p2.x) {
					for (int r = 0; r < trimmedTile.size(); r++) {
						for (int c = 0; c < trimmedTile.get(r).size(); c++) {
							space[r][c] = 1;
						}
					}
					for (int r = 0; r < bTrimmed.size(); r++) {
						for (int c = 0; c < bTrimmed.get(r).size(); c++) {
							space[r + (p2.y - p1.y)][c + (p2.x - p1.x)] = 2;
						}
					}
				} else {
					for (int r = 0; r < trimmedTile.size(); r++) {
						for (int c = 0; c < trimmedTile.get(r).size(); c++) {
							space[r][c + (p1.x - p2.x)] = 1;
						}
					}
					for (int r = 0; r < bTrimmed.size(); r++) {
						for (int c = 0; c < bTrimmed.get(r).size(); c++) {
							space[r + (p2.y - p1.y)][c] = 2;
						}
					}
				}
			} else {
				if (p1.x < p2.x) {
					for (int r = 0; r < trimmedTile.size(); r++) {
						for (int c = 0; c < trimmedTile.get(r).size(); c++) {
							space[r + (p1.y - p2.y)][c] = 1;
						}
					}
					for (int r = 0; r < bTrimmed.size(); r++) {
						for (int c = 0; c < bTrimmed.get(r).size(); c++) {
							space[r][c + (p2.x - p1.x)] = 2;
						}
					}
				} else {
					for (int r = 0; r < trimmedTile.size(); r++) {
						for (int c = 0; c < trimmedTile.get(r).size(); c++) {
							space[r + (p1.y - p2.y)][c + (p1.x - p2.x)] = 1;
						}
					}
					for (int r = 0; r < bTrimmed.size(); r++) {
						for (int c = 0; c < bTrimmed.get(r).size(); c++) {
							space[r][c] = 2;
						}
					}
				}
			}
			for (int r = 0; r < space.length; r++) {
				for (int c = 0; c < space[r].length; c++) {
					if (space[r][c] != 0) {
						if (r != 0 && space[r][c] != space[r - 1][c] && space[r - 1][c] != 0) {
							return true;
						}
//						if (r != space.length - 1 && space[r][c] != space[r + 1][c] && space[r + 1][c] != 0) {
//							return true;
//						}
//						if (c != 0 && space[r][c] != space[r][c - 1] && space[r][c - 1] != 0) {
//							return true;
//						}
//						if (c != space[r].length - 1 && space[r][c] != space[r][c + 1] && space[r][c + 1] != 0) {
//							return true;
//						}
					}
				}
			}
		}
		return false;
	}

}