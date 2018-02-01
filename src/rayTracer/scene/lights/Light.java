package rayTracer.scene.lights;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.shaders.Color;

public abstract class Light {

	private Color cl;
	
	public Light(Color cl){
		this.cl = cl;
	}

	public abstract Point3D getCenter();
	
	public Color getColor(){
		return cl;
	}
}
