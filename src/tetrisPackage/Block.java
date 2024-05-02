package tetrisPackage;

import java.awt.Point;
import java.util.ArrayList;

public class Block {
	private int[][] tile;
	
	private Point position;
	
	public Block(int piece) {
		if (piece == 1) { //Line block
			int[][] temp = { {0, 0, 0, 0}, 
							 {2, 2, 2, 2}, 
							 {0, 0, 0, 0}, 
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 2) { //Square block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 2, 2, 0},
							 {0, 2, 2, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 3) { //T-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 2, 0, 0},
							 {2, 2, 2, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 4) { //J-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 2, 0, 0},
							 {0, 2, 2, 2},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 5) { //L-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 0, 2, 0},
							 {2, 2, 2, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 6) { //S-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 2, 2, 0},
							 {2, 2, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 7) { //Z-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 2, 2, 0},
							 {0, 0, 2, 2},
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
				tile[r][c] = temp[tile.length - 1 - c][r];
			}
		}
	}
	
	public int[][] getTile() {
		return tile;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point p) {
		
		position = p;
	}
	
	public void setTile(int[][] tile) {
		this.tile = tile;
	}
	
	public ArrayList<ArrayList<Integer>> getTrimmedTile() {
		ArrayList<ArrayList<Integer>> trimmedTiles = new ArrayList<ArrayList<Integer>>();
		
		for (int r = 0; r < tile.length; r++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			boolean isRowNotZero = false;
			for (int c = 0; c < tile[0].length; c++) {
				if (tile[r][c] != 0) {
					isRowNotZero |= true;
				}
				temp.add(tile[r][c]);
			}
			if (isRowNotZero) {
				trimmedTiles.add(temp);
			}
		}
		
		for (int c = 0; c < trimmedTiles.size(); c++) {
			boolean isColumnZero = true;
			for (int r = 0; r < trimmedTiles.get(c).size(); r++) {
				if (trimmedTiles.get(c).get(r) != 0) {
					isColumnZero = false;
					break;
				}
			}

			if (isColumnZero) {

				for (int r = 0; r < trimmedTiles.size(); r++) {
					trimmedTiles.get(r).remove(c);
				}
			}
		}
		
		return trimmedTiles;
	}
}
