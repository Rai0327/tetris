package tetrisPackage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileManager {
	
	File directory;
	
	String defaultFile = "./packs/default";
	
	private ArrayList<File> listOfFiles = new ArrayList<File>();
	
	public FileManager() {
		directory = new File(defaultFile);
		try {
			refreshFiles(directory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<File> getListOfFiles() {
		return listOfFiles;
	}
	
	public void refreshFiles(File directory) throws Exception {
		File[] files = directory.listFiles();
		
		ArrayList<File> tempListOfFiles = (ArrayList<File>) listOfFiles.clone();
		
		tempListOfFiles .clear();
		
		for (int i = 0; i < 4; i++) {
			tempListOfFiles.add(null);
		}
		
		for (File f : files) {
			if (f.getName().equals("empty.png")) {
				tempListOfFiles .set(0, f);
			} else if (f.getName().equals("wall.png")) {
				tempListOfFiles.set(1, f);
			} else if (f.getName().equals("block.png")) {
				tempListOfFiles.set(2, f);
			} else if (f.getName().equals("selected.png")) {
				System.out.println("True");
				tempListOfFiles.set(3, f);
			}
		}
		
		System.out.println(tempListOfFiles);
		
		if (tempListOfFiles.size() != 4) {
			throw new Exception("Incorrect file names or too little files!");
		} else {
			listOfFiles = tempListOfFiles;
		}
	}
	
	
	
    

}
