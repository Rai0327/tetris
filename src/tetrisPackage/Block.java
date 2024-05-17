package tetrisPackage;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Block {
	private int[][] tile;
	
	private Point position;
	
	private Color color;
	
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
		
		color = randomColor();
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
	
	public void removeRow(int r) {
			boolean isRowNotZero = false;
			
			for (int c = tile[0].length-1; c >= 0; c--) {
				if (tile[r][c] != 0) {
					isRowNotZero = true;
					break;
				}
			}
			
			if (isRowNotZero) {
				for (int c = 0; c < tile[0].length; c++) {
					tile[r][c] = 0;
				}
				
			}
		
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
		
		for (int c = 0; c < trimmedTiles.get(0).size(); c++) {
			boolean isColumnZero = true;
			for (int r = 0; r < trimmedTiles.size(); r++) {
				if (trimmedTiles.get(r).get(c) != 0) {
					isColumnZero = false;
					
				}
			}

			if (isColumnZero) {

				for (int r = 0; r < trimmedTiles.size(); r++) {
					trimmedTiles.get(r).remove(c);
				}
				c--;
			}
		}
		
		return trimmedTiles;
	}
	
	private Color randomColor() {
	    
		ArrayList<Integer> randoms = new ArrayList<Integer>();
		
		for (int i = 0; i < 3; i++) {
			randoms.add((int)(Math.random()*255.0) +0 );
		}
		
		return new Color(randoms.get(0), randoms.get(1), randoms.get(2), 128);
	}
	
	public Color getColor() {
		return color;
	}
	
	public boolean isEmpty() {
		boolean isNotEmpty = false;
		for (int r = 0; r < tile.length; r++) {
			for (int c = 0; c < tile[0].length; c++) {
				if (tile[r][c] != 0) {
					isNotEmpty = true;
					break;
				}
			}
		}
		
		return !isNotEmpty;
	}
}
