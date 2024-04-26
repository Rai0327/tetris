package tetrisPackage;

public class Block {
	private int[][] tile;
	
	public Block(int piece) {
		if (piece == 1) { //Line block
			int[][] temp = { {0, 0, 0, 0}, 
							 {1, 1, 1, 1}, 
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
							 {0, 3, 0, 0},
							 {3, 3, 3, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 4) { //J-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 4, 0, 0},
							 {0, 4, 4, 4},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 5) { //L-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 0, 5, 0},
							 {5, 5, 5, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 6) { //S-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 6, 6, 0},
							 {6, 6, 0, 0},
							 {0, 0, 0, 0} };
			tile = temp;
		} else if (piece == 7) { //Z-block
			int[][] temp = { {0, 0, 0, 0},
							 {0, 7, 7, 0},
							 {0, 0, 7, 7},
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
	
	public void setTile(int[][] tile) {
		this.tile = tile;
	}
}
