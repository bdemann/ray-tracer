package rayTracer;

import javafx.geometry.Point3D;

public class Ray3D {

	private Point3D origin;
	private Point3D direction;
	
	public Ray3D(Point3D origin, Point3D direction){
		this.origin = origin;
		this.direction = direction;
	}
	
	public Point3D getOrigin() {
		return origin;
	}
	public void setOrigin(Point3D origin) {
		this.origin = origin;
	}
	public Point3D getDirection() {
		return direction;
	}
	public void setDirection(Point3D direction) {
		this.direction = direction;
	}
	
	public String toString(){
		return "Origin: " + origin.toString() + " Direction: " + direction.toString();
	}
}
