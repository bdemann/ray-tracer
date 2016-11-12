package rayTracer;

import java.util.ArrayList;

import javafx.geometry.Point3D;

public class RayTracer {
	private static final int MAX_DEPTH = 2;
	private ImageBuffer result;
	private Point3D eye;
	private Point3D lookAt;
	private double fieldOfView;
	private double focalLength;
	private double viewPortWidth;
	private double viewPortHeight;
	private int maxDepth;
	private ArrayList<Shape> shapes;
	private Color backgroundColor;
	private Color ambient;
	private Color cl;
	private Point3D lightDir;
	
	public static void main(String[] args){
		System.out.println("Starting Ray Tracer");
		for(int i = 0; i < 4; i++){
			new RayTracer(i).traceImage("./results/image" + i + ".png");
			System.out.println("Finished image " + i);
		}
//		new RayTracer(0).traceImage("./results/image" + 0 + ".png");
		System.out.println("Finishing Ray Tracer");
	}
	
	public RayTracer(int scene) {
		switch(scene){
		case 0:
			makeTestScene();
			break;
		case 1:
			makeScene1();
			break;
		case 2:
			makeScene2();
			break;
		case 3:
			makeScene3();
			break;
		default:
			makeTestScene();
		}

		result = new ImageBuffer(512,512);
		maxDepth = MAX_DEPTH;
	}
	
	public void traceImage(String fileName){
		int width = result.getWidth();
		int height = result.getHeight();
		for(int i = 0; i < width; i++){ 		//For each row
			for(int j = 0; j < height; j++){	//For each pixel in the row
				if(i == 345 && j == 245){
					System.out.println("Start a breakpoint");
				}
				//Calculate ray
				Point3D pixelCenter = getPixelCenter(i, j);
				//System.out.println("Pixel: (" + i + "," + j + "). Pixel Center is at: " + pixelCenter);
				Ray3D primaryRay = new Ray3D(eye, pixelCenter.subtract(eye));
				Color c = tracePixel(primaryRay, 0);
				result.setColor(i,j,c);
			}
		}
		result.save(fileName);
	}
	
	private Point3D getPixelCenter(int i, int j) {
		double width = result.getWidth();
		double step = viewPortWidth/width;
		
		double iTrans = i - width/2;
		double jTrans = j - width/2;
		
		double u = iTrans * step + step/2;
		double v = jTrans * step + step/2;
		
		return new Point3D(u,v*-1,0);
	}

	public Color tracePixel(Ray3D ray, int depth){
		if(depth >= maxDepth){
			return backgroundColor;
		}
		
		//Figure out what object was hit
		Shape target = null;
		Point3D closest = null;
		for(int i = 0; i < shapes.size(); i++){
			Point3D intersect = shapes.get(i).intersect(ray);
			if(intersect == null){
				continue;
			} else if(closest == null || intersect.getZ() > closest.getZ()){
				target = shapes.get(i);
				closest = intersect;
			}
		}
		
		if(target == null){ //doesn't hit anything.
			return backgroundColor;
		}

		Point3D n = target.getNormal(closest).normalize();
		//calculate launch point of new ray
		Point3D launchPoint = closest.add(n.multiply(0.1));
		
		//figure out if in shadow
		boolean shadow = inShadow(new Ray3D(launchPoint, lightDir));
		
		
		Color diffuse = target.getDiffuse();
		double red = diffuse.getRed();
		double green = diffuse.getGreen();
		double blue = diffuse.getBlue();
		
		if(target.getType() == Shape.DIFFUSE){
			Point3D l = lightDir;
			Point3D e = (eye.subtract(closest)).normalize();
			double nDotL = n.dotProduct(l);
			Point3D r = ((n.multiply(2).multiply(nDotL)).subtract(l)).normalize();
			Color cp = target.getSpec();
			
			//cl*cp*max(0,eDotR)^p
			red *= (ambient.getRed() + cl.getRed() * Math.max(0,nDotL)); 
			red += cl.getRed() * cp.getRed() * Math.pow(Math.max(0, e.dotProduct(r)), target.getPhongConst());
			green *= (ambient.getGreen() + cl.getGreen() * Math.max(0,nDotL));
			green += cl.getGreen() * cp.getGreen() * Math.pow(Math.max(0, e.dotProduct(r)), target.getPhongConst());
			blue *= (ambient.getBlue() + cl.getBlue() * Math.max(0,nDotL));
			blue += cl.getBlue() * cp.getBlue() * Math.pow(Math.max(0, e.dotProduct(r)), target.getPhongConst());
		} else if(target.getType() == Shape.REFLECTIVE){
			Point3D reflection = reflect(ray, n);
			Color reflect = tracePixel(new Ray3D(launchPoint, reflection), depth + 1);
			red += reflect.getRed() * (target.getReflectInt().getRed());
			blue += reflect.getBlue() * (target.getReflectInt().getBlue());
			green += reflect.getGreen() * (target.getReflectInt().getGreen());
		} else if(target.getType() == Shape.TRANSPARENT){
			Point3D refract = refract(ray, target.getIndexOfRefraction());
			Color transmission = tracePixel(new Ray3D(launchPoint, refract), depth + 1);
			red = transmission.getRed();
			green = transmission.getGreen();
			blue = transmission.getBlue();
		}
		
		if(red > 1){
			red = 1;
		}
		if(green > 1){
			green = 1;
		}
		if(blue > 1){
			blue = 1;
		}

		if(shadow){
			red = 0;
			green = 0;
			blue = 0;
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
		Color color = new Color(red, green, blue);
		//color = diffuse;
		//Color = ambient + diffuse + specular + shadow + reflectedColor + refractedColor

		
		return color;
	}

	private Point3D reflect(Ray3D ray, Point3D n) {
		// r = d � 2n(d � n)
		Point3D d = ray.getDirection();
		
		double dDotN = d.dotProduct(n);
		Point3D twoNDDotN = n.multiply(2 * dDotN);
		
		return d.subtract(twoNDDotN);
	}

	private Point3D refract(Ray3D ray, double indexOfRefraction) {
		return ray.getDirection();
	}

	private boolean inShadow(Ray3D ray) {
		for(int i = 0; i < shapes.size(); i++){
			shapes.get(i).cheat = true;
			Point3D intersect = shapes.get(i).intersect(ray);
			if(intersect != null){
				return true;
			}
			shapes.get(i).cheat = false;
		}
		return false;
	}
	
	private void setUpViewPort(double fov){
		fieldOfView = Math.toRadians(fov);
		focalLength = eye.subtract(lookAt).magnitude();
		viewPortWidth = 2 * focalLength * (Math.tan(fieldOfView/2));
		viewPortHeight = viewPortWidth;
	}
	
	public void makeTestScene(){
		eye = new Point3D(0, 0, 1.2);
		lookAt = new Point3D(0,0,0);
		setUpViewPort(55);
		
		backgroundColor = new Color(0.2, 0.2, 0.2);
		shapes = new ArrayList<Shape>();
		
		ambient = new Color(0,0,0);
		cl = new Color(1,1,1);
		lightDir = new Point3D(0,0,1);
		
		Color sphereColor = new Color(0.0, 0.75, 0.75);
		Color sphereColor2 = new Color(0.0, 0, 0.75);
		Color sphereColor3 = new Color(1, 0, 0);
		double radius = .5;
		double radius2 = .05;
		shapes.add(new Sphere(new Point3D(0, 0, 0), radius, Shape.REFLECTIVE, sphereColor));
		shapes.add(new Sphere(new Point3D(0.1, 0.1, 0.5), radius2, Shape.DIFFUSE, sphereColor2, new Color(1,1,1), 32));
		shapes.add(new Sphere(new Point3D(-0.1, -0.1, 0.6), radius2, Shape.DIFFUSE, sphereColor3, new Color(1,1,1), 32));
		shapes.add(new Sphere(new Point3D(-.1, .15, .7), .05, Shape.DIFFUSE, new Color(0,1,0), new Color(1,1,1), 32));
		shapes.add(new Triangle(new Point3D(0, 0.2, 0), new Point3D(0.2, 0.2, 0), new Point3D(-0.2, -0.2, 0), Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 4));
//		shapes.add(new Triangle(new Point3D(0, 0.5, 0), new Point3D(0.5, 0, 0), new Point3D(-0.5, 0, 0), Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 4));
//		shapes.add(new Sphere(new Point3D(0, 0.5, 0), .05, Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 32));
//		shapes.add(new Sphere(new Point3D(0.5, 0, 0), .05, Shape.DIFFUSE, new Color(0,1,0), new Color(1,1,1), 32));
//		shapes.add(new Sphere(new Point3D(-0.5, 0, 0), .05, Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
	}
	
	private void makeScene1(){
		lookAt = new Point3D(0,0,0);
		eye = new Point3D(0, 0, 1);
		setUpViewPort(54);
		
		cl = new Color(1,1,1);
		lightDir = new Point3D(1000000,0,0);		
		ambient = new Color(0.1,0.1,0.1);
		backgroundColor = new Color(0.2, 0.2, 0.2);

		shapes = new ArrayList<Shape>();
		shapes.add(new Sphere(new Point3D(0.35, 0, -0.1), 0.05, Shape.DIFFUSE, new Color(1,1,1), new Color(1,1,1), 4));
		shapes.add(new Sphere(new Point3D(0.2, 0, -0.1), 0.075, Shape.DIFFUSE, new Color(1,0,0), new Color(.5,1,.5), 32));
		shapes.add(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 32));
		shapes.add(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
		shapes.add(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene2(){
		lookAt = new Point3D(0,0,0);
		eye = new Point3D(0, 0, 1.2);
		setUpViewPort(110);
		
		cl = new Color(1,1,1);
		lightDir = new Point3D(0,1,0);		
		ambient = new Color(0,0,0);
		backgroundColor = new Color(0.2, 0.2, 0.2);

		shapes = new ArrayList<Shape>();
		shapes.add(new Sphere(new Point3D(0, 0.3, 0), 0.2, Shape.REFLECTIVE, new Color(0.75,0.75,0.75)));
		shapes.add(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(1, 0.5, 0), new Point3D(0, -0.5, -0.5), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 4));
		shapes.add(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(0, -0.5, -0.5), new Point3D(-1, 0.5, 0), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene3(){
		lookAt = new Point3D(0,0,0);
		eye = new Point3D(0, 0, 1.2);
		setUpViewPort(35);
		
		cl = new Color(1,1,1);
		lightDir = new Point3D(0,0,1);		
		ambient = new Color(0,0,0);
		backgroundColor = new Color(0.2, 0.2, 0.2);

		shapes = new ArrayList<Shape>();
		shapes.add(new Sphere(new Point3D(0, 0, 0), 0.2322, Shape.TRANSPARENT, new Color(1,1,1), 1.33));
		shapes.add(new Sphere(new Point3D(0.25, 0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(1,1,0), new Color(0,0,0), 4));
		shapes.add(new Sphere(new Point3D(-0.25, 0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(1,0,0), new Color(0,0,0), 4));
		shapes.add(new Sphere(new Point3D(-0.25, -0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(0,0,1), new Color(0,0,0), 4));
		shapes.add(new Sphere(new Point3D(0.25, -0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(0,1,0), new Color(0,0,0), 4));
	}
}
