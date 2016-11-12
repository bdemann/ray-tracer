package rayTracer;

import javafx.geometry.Point3D;

public abstract class Shape {

	public static final int REFLECTIVE = 0;
	public static final int TRANSPARENT = 1;
	public static final int DIFFUSE = 2;
	
	protected int type;
	protected Color color;
	protected Color highlight;
	protected int phongConst;
	protected double indexOfRefraction;
	protected Color reflectInt;
	protected Color refractInt;
	public boolean shadowing;
	
	public Shape(int type, Color reflectInt){
		this(type, new Color(0,0,0), new Color(1,1,1), 32);
		this.type = type;
		this.reflectInt = reflectInt;
	}
	
	public Shape(int type, Color color, Color highlight, int phongConst){
		this.type = type;
		this.color = color;
		this.highlight = highlight;
		this.phongConst = phongConst;
	}

	public Shape(int type, Color refractInt, double indexOfRefraction) {
		this(type, new Color(1,1,1), new Color(1,1,1), 32);
		this.type = type;
		this.refractInt = refractInt;
		this.indexOfRefraction = indexOfRefraction;
	}

	public abstract Point3D intersect(Ray3D ray);

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Color getDiffuse() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getSpec() {
		return highlight;
	}

	public void setHighlight(Color highlight) {
		this.highlight = highlight;
	}

	public int getPhongConst() {
		return phongConst;
	}

	public void setPhongConst(int phongConst) {
		this.phongConst = phongConst;
	}
	
	public Color getReflectInt() {
		return reflectInt;
	}

	public void setReflectInt(Color reflectInt) {
		this.reflectInt = reflectInt;
	}

	public abstract Point3D getNormal(Point3D closest);

	public double getIndexOfRefraction() {
		return indexOfRefraction;
	}
}
