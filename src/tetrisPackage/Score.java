package tetrisPackage;

public class Score {
	private double score;
	private String name;
	
	public Score (double score, String name) {
		this.score = score;
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}