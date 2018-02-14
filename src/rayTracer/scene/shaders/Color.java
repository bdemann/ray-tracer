package rayTracer.scene.shaders;

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
		return toColor(this.toJColor().darker());
	}
	
	public Color add(Color c){
		double red = this.red + c.red;
		double green = this.green + c.green;
		double blue = this.blue + c.blue;
		return new Color(red, green, blue);
	}
	
	public Color multiply(Color c){
		double red = this.red * c.red;
		double green = this.green * c.green;
		double blue = this.blue * c.blue;
		return new Color(red, green, blue);
	}
	
	public Color multiply(double d){
		double red = this.red * d;
		double green = this.green * d;
		double blue = this.blue * d;
		return new Color(red, green, blue);
	}
	
	public Color blacken() {
		return new Color(0,0,0);
	}
	
	public Color darkenGood() {
		return new Color(0.5,0.5,0.5);
	}
	
	public Color clipColor(){
		double red = this.red;
		double green = this.green;
		double blue = this.blue;
		
		if(red > 1){
			red = 1;
		}
		if(green > 1){
			green = 1;
		}
		if(blue > 1){
			blue = 1;
		}

		if(red < 0){
			red = 0;
		}
		if(green < 0){
			green = 0;
		}
		if(blue < 0){
			blue = 0;
		}
		return new Color(red, green, blue);
	}

	public Color subtract(Color c) {
		double red = this.red - c.red;
		double green = this.green - c.green;
		double blue = this.blue - c.blue;
		return new Color(red, green, blue);
	}

}
