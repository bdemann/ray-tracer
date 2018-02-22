package rayTracer.scene.lights;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.shaders.Color;

public abstract class Light {

	protected Color color;
	protected Point3D position;
	
	public Light(Point3D position, Color cl){
		this.position = position;
		this.color = cl;
	}

	public abstract Point3D getDirectionToLight(Point3D origin);
	
	public Color getColor(){
		return color;
	}
	
	public Point3D getPosition() {
		return position;
	}
}
