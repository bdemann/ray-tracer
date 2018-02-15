package rayTracer.scene;

import java.util.List;

import rayTracer.scene.geo.Box;
import rayTracer.scene.geo.Point3D;
import rayTracer.scene.geo.Shape;
import rayTracer.scene.lights.AmbientLight;
import rayTracer.scene.lights.Light;
import rayTracer.scene.shaders.Color;

public class MedianSplitScene implements IScene {
	
	public MedianSplitScene(IScene scene) {
		List<Shape> shapes = scene.getShapes();
		Point3D least = new Point3D(0, 0, 0);
		Point3D most = new Point3D(0,0,0);
		for(Shape shape : shapes) {
			Box bbx = shape.getBoudingBox();
		}
	}

	@Override
	public void addCamera(Camera cam) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Camera getCamera(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addShape(Shape shape) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Shape getShape(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Shape> getShapes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBackgroundColor(Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getBackgroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLight(Light light) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Light> getLights() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Light getLight(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmbientLight getAmbientLight() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAmbient(AmbientLight ambientLight) {
		// TODO Auto-generated method stub
		
	}

}
