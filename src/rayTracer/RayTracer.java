package rayTracer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import rayTracer.scene.Camera;
import rayTracer.scene.Scene;
import rayTracer.scene.TestScenes;
import rayTracer.scene.geo.Point3D;
import rayTracer.scene.geo.Ray3D;
import rayTracer.scene.geo.Shape;
import rayTracer.scene.lights.AmbientLight;
import rayTracer.scene.lights.Light;
import rayTracer.scene.shaders.Color;

public class RayTracer {
	private static final int MAX_DEPTH = 10;
	private int maxDepth;
	Scene scene;

	FileWriter fw = null;
	
	public static void main(String[] args){
		boolean testImages = true;
		System.out.println("Starting Ray Tracer");
		if(testImages){
			for(int i = 0; i < 4; i++){
				new RayTracer(i, 1).traceImage("./results/image" + i + ".png");
				System.out.println("Finished image " + i);
			}
		} else {
			for(int frame = 0; frame < 360; frame++){
				int sceneNumber = 1;
				String path = String.format("./results/turnAround/%d/", sceneNumber);
				File dir = new File(path);
				if(!dir.exists()) {
					dir.mkdirs();
					System.out.print("Looks like the dir is made now!");
				}
				String fileName = String.format("image%d.%04d.png", sceneNumber, frame);
				new RayTracer(sceneNumber, frame).traceImage(path + fileName);
			}
		}
//		new RayTracer(0).traceImage("./results/image" + 0 + ".png");
		System.out.println("Finishing Ray Tracer");
	}
	
	public RayTracer(int scene, int frame) {
		maxDepth = MAX_DEPTH;
		TestScenes tests = new TestScenes(scene);
		this.scene = tests.getTestScene();
	}
	
	public void traceImage(String fileName){

		Camera cam = scene.getCamera(0);
		
		boolean skipTracing = false;
		if(skipTracing) {
			return;
		}
		
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
				Color c = traceRay(primaryRay, 0);
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

	private class Intersection {
		public Shape intersectedShape = null;
		public Point3D intersectionPoint = null;
	}
	
	private Intersection getClosetIntersection(List<Shape> shapes, Ray3D ray){
		//Figure out what object was hit
		Intersection in = new Intersection();
		
		for(int i = 0; i < shapes.size(); i++){
			Point3D intersect = shapes.get(i).intersect(ray);
			if(intersect == null){
				continue;
			} else if(in.intersectionPoint == null || intersect.getZ() > in.intersectionPoint.getZ()){
				in.intersectedShape = shapes.get(i);
				in.intersectionPoint = intersect;
			}
		}
		
		return in;
	}
	
	private Color getPhongContribution(Shape target, Point3D point, List<Light> lights, AmbientLight ambientLight){
		Point3D n = target.getNormal(point).normalize();
		Color diffuse = target.getDiffuse();

		Color ambient = ambientLight.getColor();
		Color cl = lights.get(0).getColor();
		Point3D l = lights.get(0).getCenter();
		Point3D e = (scene.getCamera(0).getCenterOfProjection().subtract(point)).normalize();
		double nDotL = n.dotProduct(l);
		Point3D r = ((n.multiply(2).multiply(nDotL)).subtract(l)).normalize();
		Color cp = target.getSpec();
		
		//cl*cp*max(0,eDotR)^p
		diffuse = diffuse.multiply(ambient.add(cl.multiply(Math.max(0,nDotL))));
		diffuse = diffuse.add(cl.multiply(cp.multiply(Math.pow(Math.max(0, e.dotProduct(r)), target.getPhongConst()))));

		return diffuse;
	}
	
	public Color traceRay(Ray3D ray, int depth){
		if(depth >= maxDepth){
			return scene.getBackgroundColor();
		}
		
		List<Shape> shapes = scene.getShapes();
		Light light = scene.getLight(0);
		AmbientLight ambientLight = scene.getAmbientLight();
		
		Intersection in = getClosetIntersection(shapes, ray);
		Shape target = in.intersectedShape;
		Point3D closest = in.intersectionPoint;
		if(target == null){ //doesn't hit anything.
			return scene.getBackgroundColor();
		}
		
		try {
			fw.append("addpoint(geoself(), set(");
			fw.append(in.intersectionPoint.getX() + "," + in.intersectionPoint.getY() + "," + in.intersectionPoint.getZ());
			fw.append("));\n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Point3D n = target.getNormal(closest).normalize();
		//calculate launch point of new ray
		Point3D launchPoint = closest.add(n.multiply(0.00001));
		
		Ray3D rayToLight = rayToLight(launchPoint, light);
		
		//figure out if in shadow
		boolean shadow = inShadow(rayToLight);
		
		
		Color diffuse = target.getDiffuse();
		
		if(target.getType() == Shape.DIFFUSE){
			diffuse = getPhongContribution(target, closest, scene.getLights(), ambientLight);
		} else if(target.getType() == Shape.REFLECTIVE){
			Point3D reflection = reflect(ray, n);
			Color reflect = traceRay(new Ray3D(launchPoint, reflection), depth + 1);
			diffuse = diffuse.add(reflect.multiply(target.getReflectInt()));
		} else if(target.getType() == Shape.TRANSPARENT){
			Point3D refract = refract(ray, target.getIndexOfRefraction(),n);
			diffuse = traceRay(new Ray3D(launchPoint, refract), depth + 1);
		}
		
		if(shadow && target.getType() != Shape.REFLECTIVE){
			diffuse = diffuse.blacken();
		}
		
		diffuse = diffuse.clipColor();
		
		return diffuse;
	}

	private Ray3D rayToLight(Point3D launchPoint, Light light) {
		return new Ray3D(launchPoint, light.getCenter());
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
		boolean noShadow= true;
		if(noShadow) {
			return false;
		}
		List<Shape> shapes = scene.getShapes();
		for(int i = 0; i < shapes.size(); i++){
			Point3D intersect = shapes.get(i).intersect(ray);
			if(intersect != null){
				return true;
			}
		}
		return false;
	}
}
