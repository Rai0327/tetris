package tetrisPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Store {
	String fileName = "./src/tetrisPackage/scores.txt";
	
	public static void main (String[] args) {		
		Store store = new Store();
		Score highest = store.getHighestScore();
		Score lowest = store.getLowestScore();
		
		System.out.println("The highest score is " + highest.getName() + " with a score of " + highest.getScore() + "!");
		System.out.println("The lowest score is " + lowest.getName() + " with a score of " + lowest.getScore() + "!");
	}
	
	public void addScore(double score, String name) {
		try {
			FileWriter fileWriter = new FileWriter(fileName);
		    PrintWriter printWriter = new PrintWriter(fileWriter);
		    printWriter.print(score + " " + name);
		    printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
	    	System.out.println("UNABLE TO WRITE TO FILE");
		}
	}
	
	private ArrayList<Score> scanScores() {
		try {
		    File file = new File(fileName);
		    Scanner scanner = new Scanner(file);
		    
			ArrayList<Score> scores = new ArrayList<Score>();
		    
		    while (scanner.hasNextLine()) {
		    	double score = scanner.nextDouble();
		    	String name = scanner.next();
		    	
		    	scores.add(new Score(score, name));
		    }
		    
		    scanner.close();
		    
		    return scores;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
	    	System.out.println("MISSING FILE");
		}
		return null;
	}
	
	public Score getHighestScore() {
		ArrayList<Score> scores = scanScores();
		
		if (scores.size() <= 0) {return new Score(-1, "");}
		
		Score highest = scores.get(0);
		
		for (Score score : scores) {
			if (score.getScore() > highest.getScore()) {
				highest = score;
			}
		}
		
		return highest;
	}
	
	public Score getLowestScore() {
		ArrayList<Score> scores = scanScores();
		
		if (scores.size() <= 0) {return new Score(-1, "");}
		
		Score lowest = scores.get(0);
		
		for (Score score : scores) {
			if (score.getScore() < lowest.getScore()) {
				lowest = score;
			}
		}
		
		return lowest;
	}
}