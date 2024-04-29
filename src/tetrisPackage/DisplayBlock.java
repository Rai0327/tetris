package tetrisPackage;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DisplayBlock {
	
	// https://stackoverflow.com/questions/15327220/fill-rectangle-with-pattern-in-java-swing

	Block block;
	Rectangle[][] rects = new Rectangle[4][4];
	TexturePaint[][] textures = new TexturePaint[4][4];
	int sX = 0;
	int sY = 0;
	int rW;
	
	BufferedImage image;
	
	
	public DisplayBlock(Point startPos, int rectWidth, Block block) throws IOException {
		
		this.block = block;
		
		sX = startPos.x;
		sY = startPos.y;
		

		rW = rectWidth;
		
		image = ImageIO.read(new File("bg0.jpeg"));
		
		initRect();
		
	}
	
	public void initRect() {
		
		int[][] tile = block.getTile();
		
		for (int r = 0; r < rects.length; r++) {
			for (int c = 0; c < rects[r].length; c++) {
				
				if (tile[r][c] != 0) {
					rects[r][c] = new Rectangle(sX+c*rW, sY+r*rW, rW, rW);
					textures[r][c] = new TexturePaint(image, rects[r][c]);
				} else {
					rects[r][c] = null;
					textures[r][c] = null;
				}
				
			}
		}
	}
	
	public void updateRect() {
		// TODO - add texture loading (easy)
		initRect();
	}
	
	public void rotateBlock() {
		block.rotate();
	}
	
	public void right() {
		sX += 10;
	}
	
	public void left() {
		sX -= 10;
	}
	
	public void down() {
		sY += 10;
	}
	
	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;

		for (int r = 0; r < rects.length; r++) {
			for (int c = 0; c < rects[r].length; c++) {
				if (textures[r][c] != null) {
					g2.setPaint(textures[r][c]);
					g2.fill(rects[r][c]);
				}
			}
		}
	}
	
	
	public boolean bottomCollision(Rectangle other) {
		
		boolean isCollided = false;
		
		for (int r = 0; r < rects.length; r++) {
			for (int c = 0; c < rects[r].length; c++) {

				if (rects[r][c] != null) {

					if (other.intersects(rects[r][c])) {

						System.out.println("Floor max y" + other.getMaxY());
						System.out.println("Floor min y" + other.getMinY());
						
						System.out.println("Rect max y" + rects[r][c].getMaxY());

						System.out.println("Rect min y" + rects[r][c].getMaxY());
						if (other.getMinY() == rects[r][c].getMaxY()) {
							isCollided = true;
						}
					}
				}
			}
		}
		return isCollided;
	}
	
	public boolean sideCollision(Rectangle other) {
		for (int r = 0; r < rects.length; r++) {
			for (int c = 0; c < rects[r].length; c++) {

				if (rects[r][c] != null) {
				if (other.getMaxX() > rects[r][c].getMaxX()) {
					return true;
				}
				if (other.getMinX() < rects[r][c].getMinX()) {
					return true;
				}
				}
			}
		}
		return false;
	}
	
	
	
	
}
