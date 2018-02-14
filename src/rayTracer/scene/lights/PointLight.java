package rayTracer.scene.lights;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.shaders.Color;

public class PointLight extends Light {

	public PointLight(Point3D p, Color cl) {
		super(p, cl);
	}

	@Override
	public Point3D getDirectionToLight(Point3D origin) {
		return super.position.subtract(origin);
	}

}
