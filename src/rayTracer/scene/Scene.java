package rayTracer.scene;

import java.util.ArrayList;
import java.util.List;

import rayTracer.scene.geo.Shape;
import rayTracer.scene.lights.AmbientLight;
import rayTracer.scene.lights.Light;
import rayTracer.scene.shaders.Color;

public class Scene {
	double startTime;
	double endTime;
	double currTime;
	List<Camera> cameras;
	List<Shape> shapes;
	List<Light> lights;
	AmbientLight ambient;
	Color background;
	
	public Scene(){
		cameras = new ArrayList<Camera>();
		shapes = new ArrayList<Shape>();
		lights = new ArrayList<Light>();
		ambient = new AmbientLight(new Color(1,1,1));
		background = new Color(0, 0, 0);
		startTime = 0;
		endTime = 240;
		currTime = 0;
	}
	
	public void addCamera(Camera cam){
		cameras.add(cam);
	}
	
	public Camera getCamera(int index) {
		return cameras.get(index);
	}
	
	public void addShape(Shape shape) {
		shapes.add(shape);
	}
	
	public Shape getShape(int index) {
		return shapes.get(index);
	}
	
	private double interpolate(double startValue, double endValue, double startTime, double endTime){
		double totalTime = endTime - startTime;
		double percent  = 0;
		if (currTime < startTime){
			percent = 0;
		} else if (currTime > endTime){
			percent = 1;
		} else {
			percent = currTime / totalTime;
		}
		return startValue + ((endValue - startValue) * percent);
	}

	public Color getBackgroundColor() {
		return background;
	}

	public List<Shape> getShapes() {
		return shapes;
	}

	public Light getLight(int index) {
		return lights.get(index);
	}

	public AmbientLight getAmbientLight() {
		return ambient;
	}

	public void setBackgroundColor(Color color) {
		background = color;
	}

	public void setAmbient(AmbientLight ambientLight) {
		ambient = ambientLight;
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public List<Light> getLights() {
		return lights;
	}
}
