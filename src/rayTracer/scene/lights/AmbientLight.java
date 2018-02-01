package rayTracer.scene.lights;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.shaders.Color;

public class AmbientLight extends Light {

	public AmbientLight(Color lightColor) {
		super(lightColor);
	}

	@Override
	public Point3D getCenter() {
		return new Point3D(0, 0, 0);
	}

}
