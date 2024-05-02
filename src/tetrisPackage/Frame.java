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
			blocks.add(new Block(2));
			blocks.peek().setPosition(new Point(width/2-2, 1));
			updateBlockOnMap();
			renderer = new RenderMap(new Point(0,0), 30, map);
			
		} catch (IOException e) {

		      System.out.println("Something went wrong.");
		}
		
		JFrame frame = new JFrame("Tetris Game");
		frame.setSize(new Dimension(900, 600));
		frame.setBackground(Color.blue);
		frame.addKeyListener(this);
		frame.add(this);
		
		
		frame.setResizable(false);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
		
    	if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
    		blocks.peek().rotate();

    		updateBlockOnMap();
    		
    	}
    	if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
    		Point current = blocks.peek().getPosition();
    		
    		current.x += 1;
    		
    		blocks.peek().setPosition(current);

    		updateBlockOnMap();
    		
    	}
    	if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
    		Point current = blocks.peek().getPosition();
    		
    		current.x -= 1;
    		
    		blocks.peek().setPosition(current);

    		updateBlockOnMap();
    	}
    	if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
    		Point current = blocks.peek().getPosition();
    		
    		current.y += 1;
    		
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

		ArrayList<ArrayList<Integer>> blockArray = blocks.peek().getTrimmedTile();
		
		int trimmedWidth = blockArray.size();
		int trimmedHeight = blockArray.get(0).size();
		
		initMap();
		
		if ((p.x > 0 && p.x < map.size()-trimmedWidth) && (p.y > 0 && p.y < map.get(0).size()-trimmedHeight)) {
			for (int x = p.x; x < p.x+trimmedWidth; x++) {
				for (int y = p.y; y < p.y+trimmedHeight; y++) {
					
					if (blockArray.get(x-p.x).get(y-p.y) != 0) {
						map.get(x).set(y, blockArray.get(x-p.x).get(y-p.y));
					}
				}
			}
		}
	}

}