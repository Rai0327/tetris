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
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class RenderMap {
	
	// https://stackoverflow.com/questions/15327220/fill-rectangle-with-pattern-in-java-swing
	
	ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>();
	ArrayList<ArrayList<Integer>> oldMap = new ArrayList<ArrayList<Integer>>();
	int sX = 0;
	int sY = 0;
	int rW;
	
	boolean initialDraw = true;
	
	ArrayList<BufferedImage> image = new ArrayList<BufferedImage>();
	
	
	public RenderMap(Point startPos, int rectWidth, ArrayList<ArrayList<Integer>> map) throws IOException {
		
		sX = startPos.x;
		sY = startPos.y;
		

		rW = rectWidth;
		
		this.map = map;
		
		image.add(ImageIO.read(new File("bg0.jpeg")));
		image.add(ImageIO.read(new File("bg1.jpeg")));
		image.add(ImageIO.read(new File("bg2.jpeg")));
		image.add(ImageIO.read(new File("bg3.jpeg")));
		
		
	}
	
	public void paint(Graphics g) {
		
		Graphics2D g2 = (Graphics2D)g;

		int y = 0;
		int x = 0;
		
		ArrayList<ArrayList<Integer>> diff = findDifference();
		
		if (diff == null | initialDraw) {
			diff = map;
		}
		
		for (ArrayList<Integer> rows : map) {
			for (Integer tile : rows) {
				

				if (tile > 1) {
					Rectangle rect = new Rectangle(sX+x*rW, sY+y*rW, rW, rW);
					TexturePaint text = new TexturePaint(image.get(tile), rect);
					g2.setPaint(text);
					g2.fill(rect);
				} else if (initialDraw) {
					Rectangle rect = new Rectangle(sX+x*rW, sY+y*rW, rW, rW);
					TexturePaint text = new TexturePaint(image.get(tile), rect);
					g2.setPaint(text);
					g2.fill(rect);
				}
				
				x++;
			}
			x = 0;
			y++;
		}
		
		initialDraw = false;
		
		oldMap = map;
				
	}
	
	public ArrayList<ArrayList<Integer>> findDifference() {
		ArrayList<ArrayList<Integer>> diff = new ArrayList<ArrayList<Integer>>();
		
		if (map.size() == oldMap.size() && map.get(0).size() == oldMap.get(0).size()) {
			for (int r = 0; r < map.size(); r ++ ) {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				for (int c = 0; c < map.get(0).size(); c++) {
					if (map.get(r).get(c) != oldMap.get(r).get(c)) {
						temp.add(map.get(r).get(c));
					} else {
						temp.add(0);
					}
				}
				diff.add(temp);
			}
		}
		
		return diff;
		
		
	}
	
	
	
	
}
