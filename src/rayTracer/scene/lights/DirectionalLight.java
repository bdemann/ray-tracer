package rayTracer.scene.lights;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.shaders.Color;

public class DirectionalLight extends Light {

	private Point3D lightDir;

	public DirectionalLight(Color lightColor, Point3D lightDir){
		super(lightColor);
		this.lightDir = lightDir;
	}
	
	@Override
	public Point3D getCenter() {
		return lightDir;
	}
}
