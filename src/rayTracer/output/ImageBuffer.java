package rayTracer.output;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import rayTracer.scene.shaders.Color;

public class ImageBuffer {
	
	private BufferedImage image;
	
	public ImageBuffer(int width, int height){
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public void setRed(int x, int y, int red){
		int color = image.getRGB(x, y);
		int redChannel = red << 16;
		color &= 0b11111111000000001111111111111111;
		int rgb = color | redChannel;
		image.setRGB(x, y, rgb);
	}
	
	public void setGreen(int x, int y, int green){
		int color = image.getRGB(x, y);
		int redChannel = green << 16;
		color &= 0b11111111000000001111111111111111;
		int rgb = color | redChannel;
		image.setRGB(x, y, rgb);
	}
	
	public void setBlue(int x, int y, int blue){
		int color = image.getRGB(x, y);
		int redChannel = blue << 16;
		color &= 0b11111111000000001111111111111111;
		int rgb = color | redChannel;
		image.setRGB(x, y, rgb);
	}
	
	public int getWidth(){
		return image.getWidth();
	}
	
	public int getHeight(){
		return image.getHeight();
	}

	public void setColor(int x, int y, rayTracer.scene.shaders.Color c) {
		java.awt.Color color = c.toJColor();
		image.setRGB(x, y, color.getRGB());
	}

	public void save(String string) {
		File file = new File(string);
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
