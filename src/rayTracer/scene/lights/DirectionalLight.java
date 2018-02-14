package rayTracer.scene.lights;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.shaders.Color;

public class DirectionalLight extends Light {

	private Point3D lightDir;

	public DirectionalLight(Color lightColor, Point3D lightDir){
		super(new Point3D(0, 0, 0), lightColor);
		this.lightDir = lightDir;
	}
	
	@Override
	public Point3D getDirectionToLight(Point3D origin) {
		return lightDir;
	}
}
