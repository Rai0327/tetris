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

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JPanel implements KeyListener {
	
	private Block block = new Block(3);
	
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
		
		System.out.println(map);
		
		
		try {
			dispBlock = new DisplayBlock(new Point(0, 0), 30, block);
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

    	System.out.println("Rahul");
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
		
    	if (e.getKeyCode() == 87 || e.getKeyCode() == 38) {
    		dispBlock.rotateBlock();
    		dispBlock.updateRect();
    		
    	}
    	if (e.getKeyCode() == 68 || e.getKeyCode() == 39) {
    		dispBlock.right();
    	}
    	if (e.getKeyCode() == 65 || e.getKeyCode() == 37) {
    		dispBlock.left();
    	}
    	if (e.getKeyCode() == 83 || e.getKeyCode() == 40) {
    		dispBlock.down();
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

}