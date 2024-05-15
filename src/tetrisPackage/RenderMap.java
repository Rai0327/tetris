package tetrisPackage;

import java.awt.Color;
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
	ArrayList<ArrayList<Integer>> oldMap = new ArrayList<ArrayList<Integer>>();

	int sX = 0;
	int sY = 0;
	int rW;
	
	boolean initialDraw = true;
	
	ArrayList<BufferedImage> image = new ArrayList<BufferedImage>();
	
	private BufferedImage bufferedImage;
	
	public RenderMap(Point startPos, int rectWidth, ArrayList<ArrayList<Integer>> map) throws IOException {
		
		sX = startPos.x;
		sY = startPos.y;
		

		rW = rectWidth;
		
		this.map = map;
		
		image.add(ImageIO.read(new File("bg0.jpeg")));
		image.add(ImageIO.read(new File("bg1.jpeg")));
		image.add(ImageIO.read(new File("bg2.png")));
		
		bufferedImage = new BufferedImage(rectWidth*map.get(0).size(), rectWidth*map.size(), BufferedImage.TYPE_INT_ARGB);
		
		oldMap = (ArrayList<ArrayList<Integer>>) map.clone();
		
		
	}
	
	public void paintShape(ArrayList<ArrayList<Color>> colors) {

	    Graphics2D g2 = bufferedImage.createGraphics();
		
		for (int y = 0; y < map.size(); y++) {
			for (int x = 0; x < map.get(y).size(); x++) {
				
				Integer tile = map.get(y).get(x);
				
				if ((oldMap.get(y).get(x) != (map.get(y).get(x))) | initialDraw | tile == TileType.BLOCK) {
					
					BufferedImage buffer;
					if (tile == 2) {
						buffer = changeColor(image.get(tile), colors.get(y).get(x));
					} else {
						buffer = image.get(tile);
					}
					
					
					Rectangle rect = new Rectangle(sY+x*rW, sX+y*rW, rW, rW);
					TexturePaint text = new TexturePaint(buffer, rect);
					g2.setPaint(text);
					g2.fill(rect);
				}
			
			}
		}
		
		oldMap = (ArrayList<ArrayList<Integer>>) map.clone();
	    initialDraw = false;
	}
	
	public void paint(Graphics g, ArrayList<ArrayList<Color>> colors) {
		Graphics2D g2 = (Graphics2D)g;
		
	    if (bufferedImage != null) {

		    paintShape(colors);
	        g2.drawImage(bufferedImage, 0, 0, null);
	     }
				
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
	
	
	
	
	
}
