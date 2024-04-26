package tetrisPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JPanel implements KeyListener {
	
	private Block block = new Block(3);
	
	private DisplayBlock dispBlock;
	

	public void paint(Graphics g) {

		super.paintComponent(g);

		dispBlock.updateRect();
		dispBlock.paint(g);
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		
		
		try {
			dispBlock = new DisplayBlock(new Point(0, 0), 30, block);

			
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
		
    	if (e.getKeyCode() == 68) {
    		dispBlock.rotateBlock();
    		dispBlock.updateRect();
    		
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
	

}