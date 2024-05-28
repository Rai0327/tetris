package tetrisPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class RenderMap {
	
	// https://stackoverflow.com/questions/15327220/fill-rectangle-with-pattern-in-java-swing
	
	ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>();
	
	//next block code
	ArrayList<ArrayList<Integer>> next;
	Color nextColor;
	
	ArrayList<ArrayList<Integer>> hold;
	Color holdColor;

	int sX = 0;
	int sY = 0;
	int rW;
	
	boolean initialDraw = true;
	
	ArrayList<BufferedImage> image = new ArrayList<BufferedImage>();
	
	private BufferedImage bufferedImage;
	
	private BufferedImage nextImage;
	
	private BufferedImage holdImage;
	
	public RenderMap(Point startPos, int rectWidth, ArrayList<ArrayList<Integer>> map, Block nextB) throws IOException {
		
		sX = startPos.x;
		sY = startPos.y;
		

		rW = rectWidth;
		
		this.map = map;
		next = nextB.getTrimmedTile();
		nextColor = nextB.getColor();
		
		image.add(ImageIO.read(new File("bg0.jpeg")));
		image.add(ImageIO.read(new File("bg1.jpeg")));
		image.add(ImageIO.read(new File("bg2.png")));

		image.add(ImageIO.read(new File("bg2.png")));
		
		bufferedImage = new BufferedImage(rectWidth*map.get(0).size(), rectWidth*map.size(), BufferedImage.TYPE_INT_ARGB);
		
		nextImage = new BufferedImage(rectWidth*next.get(0).size(), rectWidth*next.size(), BufferedImage.TYPE_INT_ARGB);
		
	}
	
	public void paintShape(ArrayList<ArrayList<Color>> colors) {

	    Graphics2D g2 = bufferedImage.createGraphics();
		
		for (int y = 0; y < map.size(); y++) {
			for (int x = 0; x < map.get(y).size(); x++) {
				
				Integer tile = map.get(y).get(x);
							
					BufferedImage buffer;
					
					if (y == map.size()-1) {

						buffer = image.get(TileType.WALL);
					}
					else if (tile == 2 | tile == 3) {
						buffer = changeColor(image.get(tile), colors.get(y).get(x));
					}
					else {
						buffer = image.get(tile);
					}
					
					
					Rectangle rect = new Rectangle(sY+x*rW, sX+y*rW, rW, rW);
					TexturePaint text = new TexturePaint(buffer, rect);
					g2.setPaint(text);
					g2.fill(rect);
			
			}
		}
		
		
	    initialDraw = false;
	}
	
	public void paintBlock(ArrayList<ArrayList<Color>> colors, ArrayList<ArrayList<Integer>> arr, BufferedImage im, Color c) {
		Graphics2D g3 = im.createGraphics();
		
		for (int y = 0; y < arr.size(); y++) {
			for (int x = 0; x < arr.get(0).size(); x++) {
				Integer tile = arr.get(y).get(x);
				BufferedImage buffer;
				
				if (tile == 0) {
					buffer = changeColor(image.get(tile), new Color(255, 255, 255));
				} else {
					buffer = changeColor(image.get(tile), c);
				}
				
				Rectangle rect = new Rectangle(sY+x*rW, sX+y*rW, rW, rW);
				TexturePaint text = new TexturePaint(buffer, rect);
				g3.setPaint(text);
				g3.fill(rect);
			}
		}
	}
	
	public void paint(Graphics g, ArrayList<ArrayList<Color>> colors, Block nextB, Block holdB) {
		Graphics2D g2 = (Graphics2D)g;
		Graphics2D g3 = (Graphics2D)g;
		
		next = nextB.getTrimmedTile();
		nextColor = nextB.getColor();
		nextImage = new BufferedImage(rW*next.get(0).size(), rW*next.size(), BufferedImage.TYPE_INT_ARGB);
		
		g.setFont(new Font("Calibri", Font.PLAIN, 25)); 
		g.drawString("Next Block:", 325, 100);
		
		g.setFont(new Font("Calibri", Font.PLAIN, 25)); 
		g.drawString("Hold Block:", 325, 225);
		
	    if (bufferedImage != null) {

		    paintShape(colors);
	        g2.drawImage(bufferedImage, 0, 0, null);
	     }
	    if (nextImage != null) {
	    	paintBlock(colors, next, nextImage, nextColor);
	    	g3.drawImage(nextImage, rW*map.get(0).size() + 25, 125, null);
	    }
	    if (holdB != null) {
	    	g3 = (Graphics2D)g;
	    	hold = holdB.getTrimmedTile();
			holdColor = holdB.getColor();
			holdImage = new BufferedImage(rW*hold.get(0).size(), rW*hold.size(), BufferedImage.TYPE_INT_ARGB);
			paintBlock(colors, hold, holdImage, holdColor);
	    	g3.drawImage(holdImage, rW*map.get(0).size() + 25, 250, null);
	    }
	}
	
	public void paintDeath(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setFont(new Font("Helvetica", Font.BOLD, 50)); 
		g2.drawString("YOU DIED", rW*map.get(0).size()/9, 100);
	}
	
	
	public BufferedImage changeColor(BufferedImage image, Color color) {
		
		BufferedImage newImage = deepCopyImage(image);
				
		Graphics2D g2d = newImage.createGraphics();
		g2d.setColor(color);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		return newImage;
	}
	
	public BufferedImage deepCopyImage(BufferedImage buffer) {
		 ColorModel model = buffer.getColorModel();
		 
		 boolean isAlphaPremultiplied = model.isAlphaPremultiplied();
		 
		 WritableRaster raster = buffer.copyData(null);
		 
		 return new BufferedImage(model, raster, isAlphaPremultiplied, null);
	}
	
	public void setMap(ArrayList<ArrayList<Integer>> m) {
		this.map = m;
	}
	
	
	
	
}
