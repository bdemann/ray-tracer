package rayTracer;

public class Color {

	private double red;
	private double green;
	private double blue;
	
	public Color(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public double getRed() {
		return red;
	}

	public void setRed(double red) {
		this.red = red;
	}

	public double getGreen() {
		return green;
	}

	public void setGreen(double green) {
		this.green = green;
	}

	public double getBlue() {
		return blue;
	}

	public void setBlue(double blue) {
		this.blue = blue;
	}
	
	public String toString(){
		return "R: " + red + " G: " + green + " B: " + blue;
	}
	
	public java.awt.Color toJColor(){
		return new java.awt.Color((int)(this.getRed() * 255), (int)(this.getGreen() * 255), (int)(this.getBlue() * 255));
	}
	
	public Color toColor(java.awt.Color color){
		return new Color(color.getRed()/255,color.getGreen()/255,color.getBlue()/255);
	}

	public Color darken() {
		return toColor(this.toJColor());
		//return this;
	}

}
