package rayTracer.scene;

import java.util.List;

import rayTracer.scene.geo.Shape;
import rayTracer.scene.lights.AmbientLight;
import rayTracer.scene.lights.Light;
import rayTracer.scene.shaders.Color;

public interface IScene {

	public void addCamera(Camera cam);
	public Camera getCamera(int index);
	
	public void addShape(Shape shape);
	public Shape getShape(int index);
	public List<Shape> getShapes();

	public void setBackgroundColor(Color color);
	public Color getBackgroundColor();

	public void addLight(Light light);
	public List<Light> getLights();
	public Light getLight(int index);

	public AmbientLight getAmbientLight();
	public void setAmbient(AmbientLight ambientLight);
	
}
