package rayTracer.scene.geo;

import rayTracer.scene.shaders.Shader;

public class Plane extends Shape {

	public Plane(Point3D min, Point3D max, Shader shader) {
		super(shader);
	}

	@Override
	public Point3D getNormal(Point3D closest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point3D intersect(Ray3D ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Box getBoudingBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
