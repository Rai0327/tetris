package tetrisPackage;

import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.net.URL;

public class Block {
	private int[][] tile;
	
	public Block(int piece) {
		if (piece == 0) { //Line block
			int[][] temp = { {1, 1, 1, 1}, 
							 {0, 0, 0, 0}, 
							 {0, 0, 0, 0}, 
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 1) { //Square block
			int[][] temp = { {1, 1, 0, 0},
							 {1, 1, 0, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 2) { //T-block
			int[][] temp = { {0, 1, 0, 0},
							 {1, 1, 1, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 3) { //J-block
			int[][] temp = { {1, 0, 0, 0},
							 {1, 1, 1, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 4) { //L-block
			int[][] temp = { {0, 0, 1, 0},
							 {1, 1, 1, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 5) { //S-block
			int[][] temp = { {0, 1, 1, 0},
							 {1, 1, 0, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 6) { //Z-block
			int[][] temp = { {1, 1, 0, 0},
							 {0, 1, 1, 0},
							 {0, 0, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		}
	}
	
	public void rotate() {
		int[][] temp = new int[tile.length][tile[0].length];
		for (int r = 0; r < tile.length; r++) {
			for (int c = 0; c < tile[r].length; c++) {
				temp[r][c] = tile[r][c];
			}
		}
		for (int r = 0; r < tile.length; r++) {
			for (int c = 0; c < tile[r].length; c++) {
				tile[r][c] = temp[c][r];
			}
		}
	}
	
	public int[][] getTile() {
		return tile;
	}
	
	public void setTile() {
		
	}
}
