package rayTracer.scene.lights;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.geo.Square;
import rayTracer.scene.shaders.Color;

public class AreaLight extends Light {

	Square area;
	
	public AreaLight(Point3D position, Point3D direction, Color cl, double width) {
		super(position, cl);
		area = new Square(direction, width);
	}

	@Override
	public Point3D getDirectionToLight(Point3D origin) {
		return area.getRandomPoint();
	}

}
