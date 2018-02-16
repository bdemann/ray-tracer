package rayTracer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import rayTracer.scene.Camera;
import rayTracer.scene.IScene;
import rayTracer.scene.MedianSplitScene;
import rayTracer.scene.TestScenes;
import rayTracer.scene.geo.Point3D;
import rayTracer.scene.geo.Ray3D;
import rayTracer.scene.geo.Shape;
import rayTracer.scene.geo.Sphere;
import rayTracer.scene.lights.AmbientLight;
import rayTracer.scene.lights.Light;
import rayTracer.scene.shaders.Color;
import rayTracer.scene.shaders.Shader;

public class RayTracer {
	private static final int MAX_DEPTH = 10;
	private int maxDepth;
	IScene scene;
	private double indexOfRefraction = 1.0003;
	private final double iorAir = 1.0003;
	private boolean inRefract = false;

	FileWriter fw = null;
	
	public static void main(String[] args){
		boolean testImages = false;
		System.out.println("Starting Ray Tracer");
		if(testImages){
			for(int i = 0; i < 11; i++){
				new RayTracer(i, 14).traceImage("./results/image" + i + ".png");
				System.out.println("Finished image " + i);
			}
		} else {
			for(int frame = 0; frame < 72; frame++){
				int sceneNumber = 9;
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
		this.scene = tests.getTestScene(scene, frame);
//		this.scene = new MedianSplitScene(tests.getTestScene(scene, frame));
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
			} else if(in.intersectionPoint == null){
				in.intersectedShape = shapes.get(i);
				in.intersectionPoint = intersect;
			} else {
				double newDistance = ray.getOrigin().distance(intersect);
				double oldDistance = ray.getOrigin().distance(in.intersectionPoint);
				if(newDistance < oldDistance) {
					in.intersectedShape = shapes.get(i);
					in.intersectionPoint = intersect;
				}
			}
		}
		
		return in;
	}
	
	private Color getPhongContribution(Shape target, Point3D point, List<Light> lights, AmbientLight ambientLight){
		Point3D n = target.getNormal(point).normalize();
		Color diffuse = target.getShader().getDiffuse();

		Color ambient = ambientLight.getColor();
		Color cl = lights.get(0).getColor();
		Point3D l = lights.get(0).getDirectionToLight(point);
		Point3D e = (scene.getCamera(0).getCenterOfProjection().subtract(point)).normalize();
		double nDotL = n.dotProduct(l);
		Point3D r = ((n.multiply(2).multiply(nDotL)).subtract(l)).normalize();
		Color cp = target.getShader().getSpec();
		
		//cl*cp*max(0,eDotR)^p
		diffuse = diffuse.multiply(ambient.add(cl.multiply(Math.max(0,nDotL))));
		diffuse = diffuse.add(cl.multiply(cp.multiply(Math.pow(Math.max(0, e.dotProduct(r)), target.getShader().getPhongConst()))));

		return diffuse;
	}
	
	private Color getMultiLightPhongContribuition(Shape target, Point3D point, List<Light> lights, AmbientLight ambientLight) {
		Point3D n = target.getNormal(point).normalize();
		Color diffuse = target.getShader().getDiffuse();
		Color ambient = ambientLight.getColor();
		
		
		Point3D eye = (scene.getCamera(0).getCenterOfProjection().subtract(point)).normalize();
		Color cp = target.getShader().getSpec();
		
		Color ambientShading = diffuse.multiply(ambient);
		Color shading = new Color(0,0,0);
		
		for (Light light : lights){
			Color cl = light.getColor();
			Point3D l = light.getDirectionToLight(point);
			double nDotL = n.dotProduct(l);
			Point3D r = ((n.multiply(2).multiply(nDotL)).subtract(l)).normalize();
			Color diffuseShading = diffuse.multiply(cl.multiply(Math.max(0,nDotL)));
			Color specularShading = cl.multiply(cp.multiply(Math.pow(Math.max(0, eye.dotProduct(r)), target.getShader().getPhongConst())));
			shading = shading.add(diffuseShading.add(specularShading));
		}
		
		return ambientShading.add(shading);
	}
	
	private Color getSpecular(Shape target, Point3D point, List<Light> lights, AmbientLight ambientLight){
		Point3D n = target.getNormal(point).normalize();
		Point3D eye = (scene.getCamera(0).getCenterOfProjection().subtract(point)).normalize();
		Color cp = target.getShader().getSpec();
		Color shading = new Color(0,0,0);
		
		for (Light light : lights){
			Color cl = light.getColor();
			Point3D l = light.getDirectionToLight(point);
			double nDotL = n.dotProduct(l);
			Point3D r = ((n.multiply(2).multiply(nDotL)).subtract(l)).normalize();
			Color specularShading = cl.multiply(cp.multiply(Math.pow(Math.max(0, eye.dotProduct(r)), target.getShader().getPhongConst())));
			shading = shading.add(specularShading);
		}
		return shading;
	}
	
	public Color traceRay(Ray3D ray, int depth){
		if(depth >= maxDepth){
			return scene.getBackgroundColor();
		}
		
		List<Shape> shapes = scene.getShapes();
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
		double scalar = 0.00001;
		Point3D launchPoint = closest.add(n.multiply(scalar));
		
		//figure out if in shadow
		double shadowAmount = shadowAmount(launchPoint);
		
		
		Color diffuse = target.getShader().getDiffuse();
		
		if(target.getShader().getType() == Shader.DIFFUSE){
			diffuse = getMultiLightPhongContribuition(target, closest, scene.getLights(), ambientLight);
//			diffuse = getPhongContribution(target, closest, scene.getLights(), ambientLight);
		} else if(target.getShader().getType() == Shader.REFLECTIVE){
			Color spec = getSpecular(target, closest, scene.getLights(), ambientLight);
			Point3D reflection = reflect(ray, n);
			Color reflect = traceRay(new Ray3D(launchPoint, reflection), depth + 1);
//			diffuse = diffuse.add(reflect.multiply(target.getShader().getReflectInt())).add(spec);
			diffuse = diffuse.add(reflect.multiply(target.getShader().getReflectInt()));
		} else if(target.getShader().getType() == Shader.TRANSPARENT){
			Point3D refractDirection = null;
			if (target instanceof Sphere) {
				if(inRefract){
					refractDirection = refract(ray, target.getShader().getIndexOfRefraction(), this.indexOfRefraction, n);
					indexOfRefraction = iorAir;
					inRefract = false;
				} else {
					refractDirection = refract(ray, this.indexOfRefraction, target.getShader().getIndexOfRefraction(),n);
					inRefract = true;
					indexOfRefraction = target.getShader().getIndexOfRefraction();
				}
			} else {
				refractDirection = refract(ray, this.indexOfRefraction, target.getShader().getIndexOfRefraction(),n);
			}
			if(ray.toVector().dotProduct(n) < 1) {
				scalar *= -1;
			}
			launchPoint = closest.add(n.multiply(scalar));
			Ray3D refractedRay = new Ray3D(launchPoint, refractDirection);

			diffuse = diffuse.multiply(target.getShader().getTransparencyAmt()).add(traceRay(refractedRay, depth + 1));
		}
		
		if(shadowAmount > 0 && target.getShader().getType() != Shader.REFLECTIVE){
			if(shadowAmount >= 1){
				diffuse = diffuse.blacken();
			} else {
//				diffuse = diffuse.darkenGood();
				diffuse = diffuse.multiply(1.0/7);
//				diffuse = diffuse.blacken();
			}
		}
		
		diffuse = diffuse.clipColor();
		
		return diffuse;
	}

	private Point3D reflect(Ray3D ray, Point3D n) {
		// r = d � 2n(d � n)
		Point3D d = ray.getDirection();
		
		double dDotN = d.dotProduct(n);
		Point3D twoNDDotN = n.multiply(2 * dDotN);
		
		return d.subtract(twoNDDotN);
	}
	
	private static Point3D refract(Ray3D ray, double srcIOR, double desIOR, Point3D normal) {
		Point3D I = ray.getDirection();
		Point3D T = null;
		Point3D N = normal;
		double theta = I.getAngle(N);
//		if(theta < Math.PI/2) {
//			N = normal.multiply(-1);
//			theta = I.getAngle(N);
//		}
		double n1 = srcIOR;
		double n2 = desIOR;
		double n = n1/n2;
		double c = Math.cos(theta);
		
		Point3D vector1 = I.multiply(n);
		double s = Math.sqrt(1 + Math.pow(n, 1) * (Math.pow(c, 1) + 1) );
		double scalar1 = n + c - s;
		Point3D vector2 = N.multiply(scalar1);
		T = vector1.add(vector2);
				
		return T;
	}

	private double shadowAmount(Point3D origin) {
		double shadow = 0;
		for (Light light : scene.getLights()) {
			Point3D lightDir = light.getDirectionToLight(origin);
			Ray3D ray = new Ray3D(origin, lightDir);
			if (inShadow(ray)) {
				shadow += 1;
			}
		}
		return shadow/(double)scene.getLights().size();
	}
	
	private boolean inShadow(Ray3D ray) {
		List<Shape> shapes = scene.getShapes();
		for(int i = 0; i < shapes.size(); i++){
			Point3D intersect = shapes.get(i).intersect(ray);
			if(intersect != null && shapes.get(i).getShader().getType() != Shader.TRANSPARENT){
				return true;
			}
		}
		return false;
	}
}
