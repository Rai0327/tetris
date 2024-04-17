package tetrisPackage;

import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.net.URL;

public class Block {
	private int[][] tile;
	
	public Block(int piece) {
		if (piece == 0) { //line piece
			int[][] tile = { {1, 1, 1, 1}, 
							 {0, 0, 0, 0}, 
							 {0, 0, 0, 0}, 
							 {0, 0, 0, 0} };
		} else if (piece == 1) { //square piece
			int[][] tile = { {1, 1, 0, 0},
							 {1, 1, 0, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
		} else if (piece == 2) { //t-piece
			int[][] tile = { {0, 1, 0, 0},
							 {1, 1, 1, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
		}
	}
	
	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Block.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
}
