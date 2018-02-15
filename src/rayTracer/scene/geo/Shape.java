package rayTracer.scene.geo;

import rayTracer.scene.shaders.Shader;

public abstract class Shape {
	
	private Shader shader;
	
	public Shape(Shader shader){
		this.setShader(shader);
	}

	public abstract Point3D getNormal(Point3D closest);
	public abstract Point3D intersect(Ray3D ray);
	public abstract Box getBoudingBox();

	public Shader getShader() {
		return shader;
	}

	public void setShader(Shader shader) {
		this.shader = shader;
	}
}
