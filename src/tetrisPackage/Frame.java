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

	
	private ArrayList<ArrayList<Color>> colors = new ArrayList<ArrayList<Color>>(); 

	private RenderMap renderer;
	
	//private DisplayBlock dispBlock;
	
	private ArrayList<ArrayList<Integer>> trimmedTile = new ArrayList<ArrayList<Integer>>();
	
	private int count = 0;
	private int touchCount = 0;
	
	private int level = 29;
	

	public void paint(Graphics g) {

		super.paintComponent(g);
			
		renderer.paint(g, colors);
		
		Point curr = blocks.peek().getPosition();
		trimmedTile = blocks.peek().getTrimmedTile();
		
		count++;
		
		if (curr.y >= map.size()-trimmedTile.size()-1 || checkBottomCollision()) {
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
		if (count % level == 0 && !checkBottomCollision()) { // TODO - Bottom collision
			curr.y++;
			
			blocks.peek().setPosition(curr);
			
			while (curr.y >= map.size()-trimmedTile.size()) {
				curr.y--;
				blocks.peek().setPosition(curr);
			}
			
		}
		
		

		updateBlockOnMap();
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
    		
    		
    		while (current.x >= map.get(0).size() - trimmedTile.get(0).size()) {
    			current.x -= 1;
    			blocks.peek().setPosition(current);
    		}
    		
    		while (current.y >= map.size()-trimmedTile.size()) {
    			current.y -= 1;
    			blocks.peek().setPosition(current);
    		}
    		
    		System.out.println("Distance from wall: " + (map.get(0).size()-current.x));
    		System.out.println("Tile: " + trimmedTile.size());
    		
    		if (map.get(0).size()-current.x > trimmedTile.size()) {
    			blocks.peek().rotate();
    		}
    		
    	}
    	if ((e.getKeyCode() == 68 || e.getKeyCode() == 39) && !checkRightCollision()) {
    		
    		if (current.x < map.get(0).size()-trimmedTile.get(0).size()-1) {
    		
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

    		
    		if (current.y < map.size()-trimmedTile.size()-1) {
    		
    		
    		current.y += 1;
    		
    		blocks.peek().setPosition(current);

    		}
    	}
    	
    	if (e.getKeyCode() == 32) {
    		//adjust when implementing switching blocks
    		while (current.y < map.size()-trimmedTile.size()-1 && !checkBottomCollision()) {
    			current.y++;
    		}
    		
    		blocks.peek().setPosition(current);
    		
    		oldBlocks.add(blocks.peek());
			blocks.remove();
			blocks.add(new Block((int) (Math.random() * 7) + 1));
			blocks.peek().setPosition(new Point(width/2-2, 1));
			touchCount = 0;
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
		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Color> tempC = new ArrayList<Color>();
		
		
		for (int i = 0; i < width; i++) {
			temp.add(TileType.WALL);
			tempC.add(new Color(0, 0, 0));
		}
		
		map.add(temp);
		
		colors.add(tempC);
		
		for (int j = 1; j < height-1; j++) {
			ArrayList<Integer> temp1 = new ArrayList<Integer>();
			ArrayList<Color> temp2C = new ArrayList<Color>();
			for (int i = 0; i < width; i++) {
				if (i == 0) {
					temp1.add(TileType.WALL);
				} else if (i == width-1) {
					temp1.add(TileType.WALL);
				} else {
					temp1.add(TileType.EMPTY);
				}

				temp2C.add(new Color(0, 0, 0));
			}
			
			map.add(temp1);
			colors.add(temp2C);
		}
		
		
		ArrayList<Integer> temp2 = new ArrayList<Integer>();
		ArrayList<Color> temp3C = new ArrayList<Color>();
		for (int i = 0; i < width; i++) {
			temp2.add(TileType.WALL);
			temp3C.add(new Color(0, 0, 0));
		}
		
		map.add(temp2);
		colors.add(temp3C);
	
	}
	
	public void updateBlockOnMap() {
		
		Point p = blocks.peek().getPosition();
		
		trimmedTile = blocks.peek().getTrimmedTile();

		
		int trimmedWidth = trimmedTile.size();
		int trimmedHeight = trimmedTile.get(0).size();
		
		initMap();
		
		if ((p.y >= 0 && p.y < map.size()-trimmedWidth+1) && (p.x >= 0 && p.x < map.get(0).size()-trimmedHeight+1)) {
			for (int y = p.y; y < p.y+trimmedWidth; y++) {
				for (int x = p.x; x < p.x+trimmedHeight; x++) {
					
					if (trimmedTile.get(y-p.y).get(x-p.x) != 0) {
						map.get(y).set(x, trimmedTile.get(y-p.y).get(x-p.x));
						
						colors.get(y).set(x, blocks.peek().getColor());
						
					}
				}
			}
		}
		
		for (Block tile : oldBlocks) {
			p = tile.getPosition();
			trimmedTile = tile.getTrimmedTile();
			trimmedWidth = trimmedTile.size();
			trimmedHeight = trimmedTile.get(0).size();
			if ((p.y >= 0 && p.y < map.size()-trimmedWidth+1) && (p.x >= 0 && p.x < map.get(0).size()-trimmedHeight+1)) {
				for (int y = p.y; y < p.y+trimmedWidth; y++) {
					for (int x = p.x; x < p.x+trimmedHeight; x++) {
						
						if (trimmedTile.get(y-p.y).get(x-p.x) != 0) {
							
							map.get(y).set(x, trimmedTile.get(y-p.y).get(x-p.x));
							colors.get(y).set(x, tile.getColor());
						}
					}
				}
			}
		}
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
	
	
	public ArrayList<String> checkAllCollisions() {
		Point p = blocks.peek().getPosition();
		
		trimmedTile = blocks.peek().getTrimmedTile();
		
		ArrayList<Point> leftCandidates = new ArrayList<Point>();

		
		ArrayList<Point> rightCandidates = new ArrayList<Point>();
		
		ArrayList<Point> bottomCandidates = new ArrayList<Point>();
		
		ArrayList<String> restrictions = new ArrayList<String>();
		
		restrictions.add("");
		restrictions.add("");
		restrictions.add("");
		
		// Add candidates from left
		for (int r = 0; r < trimmedTile.size(); r++) {
			for (int c = 0; c < trimmedTile.get(r).size(); c++) {
				if (trimmedTile.get(r).get(c) != TileType.EMPTY) {
					leftCandidates.add(new Point(p.x+c-1, p.y+r));
					break;
				}
			}
		}
		
		// Add candidates from right
		for (int r = trimmedTile.size()-1; r >= 0; r--) {
			for (int c = trimmedTile.get(r).size()-1; c >= 0; c--) {
				if (trimmedTile.get(r).get(c) != TileType.EMPTY) {
					rightCandidates.add(new Point(p.x+c+1, p.y+r));
					break;
				}
			}
		}
		
		// Add candidates from bottom
		for (int c = trimmedTile.get(0).size()-1; c >= 0; c--) {
			for (int r = trimmedTile.size()-1; r >= 0; r--) {
				if (trimmedTile.get(r).get(c) != TileType.EMPTY) {
					bottomCandidates.add(new Point(p.x+c, p.y+r+1));
					break;
				}
			}
		}
		
		
		for (Point point : leftCandidates) {
			if (map.get(point.y).get(point.x) == TileType.BLOCK) {
				restrictions.set(0, "left");
			}
		}
		
		for (Point point : rightCandidates) {
			if (map.get(point.y).get(point.x) == TileType.BLOCK) {
				restrictions.set(1, "right");
			}
		}
		for (Point point : bottomCandidates) {
			if (map.get(point.y).get(point.x) == TileType.BLOCK | map.get(point.y).get(point.x) == TileType.WALL) {
				restrictions.set(2, "bottom");
			}
		}
		
		return restrictions;
		
	}
	


}