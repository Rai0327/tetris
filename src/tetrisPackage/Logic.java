package tetrisPackage;

public class Logic {
	public static void main(String[] args) {
		Block piece = new Block(7);
		for (int i = 0; i < 4; i++) {
			int[][] temp = piece.getTile();
			for (int r = 0; r < temp.length; r++) {
				for (int c = 0; c < temp[r].length; c++) {
					System.out.print(temp[r][c]);
				}
				System.out.println();
			}
			piece.rotate();
		}
	}
}
