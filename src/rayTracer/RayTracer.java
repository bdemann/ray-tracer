package rayTracer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import rayTracer.geo.Point3D;
import rayTracer.geo.Ray3D;
import rayTracer.geo.Shape;
import rayTracer.geo.Sphere;
import rayTracer.geo.Triangle;

public class RayTracer {
	private static final int MAX_DEPTH = 10;
	private int maxDepth;
	private ArrayList<Shape> shapes;
	private Color backgroundColor;
	private Color ambient;
	private Color cl;
	private Point3D lightDir;
	private Camera cam;

	FileWriter fw = null;
	
	public static void main(String[] args){
		boolean testImages = false;
		System.out.println("Starting Ray Tracer");
		if(testImages){
			for(int i = 0; i < 4; i++){
				new RayTracer(i, 1).traceImage("./results/image" + i + ".png");
				System.out.println("Finished image " + i);
			}
		} else {
			for(int i = 0; i < 360; i++){
				new RayTracer(4, i).traceImage("./results/turnAround/image" + i + ".png");
			}
		}
//		new RayTracer(0).traceImage("./results/image" + 0 + ".png");
		System.out.println("Finishing Ray Tracer");
	}
	
	public RayTracer(int scene, int frame) {
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
		case 4:
			makeScene(frame);
			break;
		default:
			makeTestScene();
		}
		
		maxDepth = MAX_DEPTH;
	}
	
	public void traceImage(String fileName){

		try {
			fw = new FileWriter("results/pointsQuad.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = cam.getWidth();
		int height = cam.getHeight();
		for(int i = 0; i < width; i++){ 		//For each row
			for(int j = 0; j < height; j++){	//For each pixel in the row
				if(i == 345 && j == 245){
					System.out.println("Start a breakpoint");
				}
				//Calculate ray
				Point3D pixelCenter = cam.getPixelCenter(i, j, true);
				//System.out.println("Pixel: (" + i + "," + j + "). Pixel Center is at: " + pixelCenter);
				Point3D eye = cam.getCenterOfProjection();
				Ray3D primaryRay = new Ray3D(eye, pixelCenter.subtract(eye));
				Color c = tracePixel(primaryRay, 0);
				cam.captureColor(i,j,c);
			}
		}
		try {
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cam.save(fileName);
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

		try {
			fw.append("addpoint(geoself(), set(");
			fw.append(closest.getX() + "," + closest.getY() + "," + closest.getZ());
			fw.append("));\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Point3D n = target.getNormal(closest).normalize();
		//calculate launch point of new ray
		Point3D launchPoint = closest.add(n.multiply(0.00001));
		
		//figure out if in shadow
		boolean shadow = inShadow(new Ray3D(launchPoint, lightDir));
		
		
		Color diffuse = target.getDiffuse();
		double red = diffuse.getRed();
		double green = diffuse.getGreen();
		double blue = diffuse.getBlue();
		
		if(target.getType() == Shape.DIFFUSE){
			Point3D l = lightDir;
			Point3D e = (cam.getCenterOfProjection().subtract(closest)).normalize();
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
			
			if(target instanceof Sphere && false){
				red = (closest.subtract(((Sphere)target).getCenter()).magnitude()) / (((Sphere)target).getRadius() * 2);

				red = Math.abs(closest.getZ() - ((Sphere)target).getCenter().getZ()) / (((Sphere)target).getRadius() * 2);
				//System.out.println(red);
				blue = red;
				green = red;
			}
		} else if(target.getType() == Shape.REFLECTIVE){
			Point3D reflection = reflect(ray, n);
			Color reflect = tracePixel(new Ray3D(launchPoint, reflection), depth + 1);
			red += reflect.getRed() * (target.getReflectInt().getRed());
			blue += reflect.getBlue() * (target.getReflectInt().getBlue());
			green += reflect.getGreen() * (target.getReflectInt().getGreen());
		} else if(target.getType() == Shape.TRANSPARENT){
			Point3D refract = refract(ray, target.getIndexOfRefraction(),n);
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

		if(shadow && target.getType() != Shape.REFLECTIVE){
			red = 0;
			green = 0;
			blue = 0;
		}
		
//		if(red < 0){
//			red = 0;
//		}
//		if(green < 0){
//			green = 0;
//		}
//		if(blue < 0){
//			blue = 0;
//		}
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

	private Point3D refract(Ray3D ray, double indexOfRefraction, Point3D normal) {
		double ni = ray.indexOfRefraction;
		double nt = indexOfRefraction;
		//angle = acos(v1�v2)
		Point3D rd = ray.getDirection().normalize();
		Point3D n = normal.normalize();
		double theta = Math.acos(n.dotProduct(rd));
		double sinPhi = (ni * Math.sin(theta))/nt;
		double phi = Math.asin(sinPhi);
		
		double sin = Math.pow(ni/nt, 2) + (1 + Math.pow(Math.cos(theta), 2));
		Point3D t = rd.multiply(ni/nt).subtract(normal.multiply((ni/nt) * Math.cos(theta) + Math.sqrt(1-sin)));
		
		return ray.getDirection();
	}

	private boolean inShadow(Ray3D ray) {
		for(int i = 0; i < shapes.size(); i++){
			Point3D intersect = shapes.get(i).intersect(ray);
			if(intersect != null){
				return true;
			}
		}
		return false;
	}
	
	public void makeTestScene(){
		Point3D eye = new Point3D(0, 0, 1.2);
		Point3D lookAt = new Point3D(0,0,0);
		Point3D upVector = new Point3D(0,1,0);
		cam = new Camera(eye, lookAt, upVector, 300, 300, 55);
		
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
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(2, 0, 0);
		Point3D upVector = new Point3D(0,1,0);
		int width = 900;
		cam = new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 54);
		
		cl = new Color(1,1,1);
		lightDir = new Point3D(1,0,0);		
		ambient = new Color(0.1,0.1,0.1);
		backgroundColor = new Color(0.2, 0.2, 0.2);

		shapes = new ArrayList<Shape>();
		shapes.add(new Sphere(new Point3D(0.35, 0, -0.1), 0.05, Shape.DIFFUSE, new Color(1,1,1), new Color(1,1,1), 4));
		shapes.add(new Sphere(new Point3D(0.2, 0, -0.1), 0.075, Shape.DIFFUSE, new Color(1,0,0), new Color(.5,1,.5), 32));
		shapes.add(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 32));
//		shapes.add(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
//		shapes.add(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene(int frame){
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(frame));
		double zPos = radius * Math.sin(Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 900;
		cam = new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 54);
		
		cl = new Color(1,1,1);
		lightDir = new Point3D(1,0,0);		
		ambient = new Color(0.1,0.1,0.1);
		backgroundColor = new Color(0.2, 0.2, 0.2);

		double angle = Math.acos(1.0/3.0) - Math.toRadians(90);
		double bottomAngle = Math.toRadians(120) - Math.toRadians(90);
		
		double largeRadius = 0.3;
		double smallRadius = 0.05;
		double legLen = largeRadius + smallRadius;
		double yOffset = legLen * Math.sin(angle);
		double xOffset = legLen * Math.cos(angle);
		
		double xOffsetBot = Math.sin(bottomAngle) * xOffset;
		double zOffsetBot = Math.cos(bottomAngle) * xOffset;
		
		shapes = new ArrayList<Shape>();
		shapes.add(new Sphere(new Point3D(0, 0, 0), largeRadius, Shape.DIFFUSE, new Color(1,1,1), new Color(1,1,1), 4));
		shapes.add(new Sphere(new Point3D(0, legLen, 0), smallRadius, Shape.DIFFUSE, new Color(1,0,1), new Color(1,1,1), 4));
		shapes.add(new Sphere(new Point3D(xOffset, yOffset, 0), smallRadius, Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
		shapes.add(new Sphere(new Point3D(-xOffsetBot, yOffset, zOffsetBot), smallRadius, Shape.DIFFUSE, new Color(0,1,1), new Color(1,1,1), 4));
		shapes.add(new Sphere(new Point3D(-xOffsetBot, yOffset, -zOffsetBot), smallRadius, Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 4));
//		shapes.add(new Sphere(new Point3D(0, 0, -0.1), 0.075, Shape.DIFFUSE, new Color(1,0,0), new Color(.5,1,.5), 32));
//		shapes.add(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 32));
//		shapes.add(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
//		shapes.add(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene2(){
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(0, 0, 1.2);
		Point3D upVector = new Point3D(0,1,0);
		cam = new Camera(eye, lookAt, upVector, 300, 300, 110);
		
		cl = new Color(1,1,1);
		lightDir = new Point3D(0,1,0);		
		ambient = new Color(0,0,0);
		backgroundColor = new Color(0.2, 0.2, 0.2);

		shapes = new ArrayList<Shape>();
		shapes.add(new Sphere(new Point3D(0, 0.3, 0), 0.2, Shape.REFLECTIVE, new Color(0.75,0.75,0.75)));
		//shapes.add(new Sphere(new Point3D(0, 0.3, 0), 0.2, Shape.DIFFUSE, new Color(0.75,0.75,0.75), new Color(1,1,1), 4));
		shapes.add(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(1, 0.5, 0), new Point3D(0, -0.5, -0.5), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 4));
		shapes.add(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(0, -0.5, -0.5), new Point3D(-1, 0.5, 0), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene3(){
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(0, 0, 1.2);
		Point3D upVector = new Point3D(0,1,0);
		cam = new Camera(eye, lookAt, upVector, 300, 300, 35);
		
		cl = new Color(1,1,1);
		lightDir = new Point3D(0,0,1);		
		ambient = new Color(0,0,0);
		backgroundColor = new Color(0.2, 0.2, 0.2);

		shapes = new ArrayList<Shape>();
		
		shapes.add(new Sphere(new Point3D(0,0,0), 0.2322, Shape.TRANSPARENT, new Color(1,1,1), 1.3));
		shapes.add(new Sphere(new Point3D(0.25, 0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(1,1,0), new Color(0,0,0), 4));
		shapes.add(new Sphere(new Point3D(-0.25, 0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(1,0,0), new Color(0,0,0), 4));
		shapes.add(new Sphere(new Point3D(-0.25, -0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(0,0,1), new Color(0,0,0), 4));
		shapes.add(new Sphere(new Point3D(0.25, -0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(0,1,0), new Color(0,0,0), 4));
	}
}
