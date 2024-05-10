package tetrisPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JPanel implements KeyListener {
	
	private Stack<Block> blocks = new Stack<Block>();
	
	private Rectangle rect = new Rectangle(0, 200, 500, 10);
	
	private int width = 10;
	private int height = 20;
	
	private ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>(); 

	private RenderMap renderer;
	
	private DisplayBlock dispBlock;
	
	private ArrayList<ArrayList<Integer>> trimmedTile = new ArrayList<ArrayList<Integer>>(); 
	

	public void paint(Graphics g) {

		super.paintComponent(g);
			
			renderer.paint(g);
		
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		
		initMap();

		
		try {
			blocks.add(new Block(5));
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
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
    	

		trimmedTile = blocks.peek().getTrimmedTile();

		Point current = blocks.peek().getPosition();
		
    	if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
    		if ((current.x < map.get(0).size()-trimmedTile.get(0).size()) && ((current.y < map.size()-trimmedTile.size()-1))) {

    		blocks.peek().rotate();

    		updateBlockOnMap();
    		}
    		
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
    	if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {

    		
    		if (current.y < map.size()-trimmedTile.size()-1) {
    		
    		
    		current.y += 1;
    		
    		blocks.peek().setPosition(current);

    		updateBlockOnMap();
    		}
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

}