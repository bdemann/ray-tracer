package rayTracer.scene.shaders;

public class Shader {
	public static final int REFLECTIVE = 0;
	public static final int TRANSPARENT = 1;
	public static final int DIFFUSE = 2;
	
	public static final Shader WHITE_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(1,1,1), new Color(1,1,1), 32);
	public static final Shader RED_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 32);
	public static final Shader GREEN_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(0,1,0), new Color(1,1,1), 32);
	public static final Shader BLUE_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32);
	public static final Shader YELLOW_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 32);
	public static final Shader MAGENTA_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(1,0,1), new Color(1,1,1), 32);
	public static final Shader CYAN_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(0,1,1), new Color(1,1,1), 32);
	public static final Shader BLACK_DIFFUSE = new Shader(Shader.DIFFUSE, new Color(0,0,0), new Color(1,1,1), 32);
	
	public static final Shader WHITE_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(1,1,1));
	public static final Shader RED_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(1,0,0));
	public static final Shader GREEN_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(0,1,0));
	public static final Shader BLUE_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(0,0,1));
	public static final Shader YELLOW_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(1,1,0));
	public static final Shader PURPLE_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(1,0,1));
	public static final Shader CYAN_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(0,1,1));
	public static final Shader BLACK_REFLECTIVE = new Shader(Shader.REFLECTIVE, new Color(0,0,0));
	
	public static final Shader WHITE_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(1,1,1), 1.5, .1);
	public static final Shader RED_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(1,0,0), 1.5, .1);
	public static final Shader GREEN_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(0,1,0), 1.5, .1);
	public static final Shader BLUE_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(0,0,1), 1.5, .1);
	public static final Shader YELLOW_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(1,1,0), 1.5, .1);
	public static final Shader PURPLE_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(1,0,1), 1.5, .1);
	public static final Shader CYAN_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(0,1,1), 1.5, .1);
	public static final Shader BLACK_TRANSPARENT = new Shader(Shader.TRANSPARENT, new Color(0,0,0), 1.5, .1);
	
	private int surfaceType;
	
	private double indexOfRefraction;
	private Color reflectionColor;
	private Color refractColor;
	private double transparencyAmount;
	
	double diffuseWeight;
	double reflectWeight;
	double transparentWeight;
	
	private DiffuseComponent diffuse;
	
	public Shader(int type, Color reflectInt) {
		diffuseWeight = 0;
		reflectWeight = 1;
		transparentWeight = 0;
		this.surfaceType = type;
		this.refractColor = new Color(0, 0, 0);
		diffuse = new DiffuseComponent(new Color(0,0,0), new Color(1,1,1), 32);
		this.surfaceType = type;
		this.reflectionColor = reflectInt;
		if(type != REFLECTIVE){
			System.out.println("Warning you aren't doing the constructor you think you are. Reflective");
		}
	}
	
	public Shader(int type, Color color, Color highlight, int phongConst){
		diffuseWeight = 1;
		reflectWeight = 0;
		transparentWeight = 0;
		this.surfaceType = type;
		this.reflectionColor = new Color(0, 0, 0);
		this.refractColor = new Color(0, 0, 0);
		diffuse = new DiffuseComponent(color, highlight, phongConst);
		if(type != DIFFUSE){
			System.out.println("Warning you aren't doing the constructor you think you are. Diffuse");
		}
	}

	public Shader(int type, Color refractInt, double indexOfRefraction, double transparencyAmt) {
		diffuseWeight = 0;
		reflectWeight = 0;
		transparentWeight = 1;
		this.surfaceType = type;
		this.reflectionColor = new Color(0, 0, 0);
		diffuse = new DiffuseComponent(refractInt, new Color(1,1,1), 32);
		this.surfaceType = type;
		this.setRefractColor(refractInt);
		this.indexOfRefraction = indexOfRefraction;
		this.transparencyAmount = transparencyAmt;
		if(type != TRANSPARENT){
			System.out.println("Warning you aren't doing the constructor you think you are. Transparent");
		}
	}
	
	public Color getColor(){
		double totalWeight = diffuseWeight + transparentWeight + reflectWeight;
		Color diffuseContrib = diffuse.getDiffuse().multiply(diffuseWeight/totalWeight);
		Color reflectContrib = reflectionColor.multiply(reflectWeight/totalWeight);
		Color transContrip = refractColor.multiply(transparentWeight/totalWeight);
		return diffuseContrib.add(reflectContrib).add(transContrip);
	}
	
	public DiffuseComponent getDiffuse(){
		return diffuse;
	}
	
	public int getType() {
		return surfaceType;
	}

	public void setType(int type) {
		this.surfaceType = type;
	}
	
	public Color getReflectInt() {
		return reflectionColor;
	}

	public void setReflectInt(Color reflectInt) {
		this.reflectionColor = reflectInt;
	}

	public double getIndexOfRefraction() {
		return indexOfRefraction;
	}

	public Color getRefractColor() {
		return refractColor;
	}

	public void setRefractColor(Color refractColor) {
		this.refractColor = refractColor;
	}
	
	public String toString() {
		double red = this.diffuse.getDiffuse().getRed();
		double green = this.diffuse.getDiffuse().getGreen();
		double blue = this.diffuse.getDiffuse().getBlue();

		String colorName;
		if(red == 1 && green == 1 && blue == 1) {
			colorName = "white";
		} else if(red == 1 && green == 0 && blue == 1) {
			colorName = "magenta";
		} else if(red == 0 && green == 1 && blue == 1) {
			colorName = "cyan";
		} else if(red == 0 && green == 0 && blue == 1) {
			colorName = "blue";
		} else if(red == 0 && green == 1 && blue == 0) {
			colorName = "green";
		} else if(red == 1 && green == 0 && blue == 0) {
			colorName = "red";
		} else if(red == 1 && green == 1 && blue == 0) {
			colorName = "yellow";
		} else {
			colorName = "black";
		}
		return colorName;
	}

	public double getTransparencyAmt() {
		return transparencyAmount;
	}
}
