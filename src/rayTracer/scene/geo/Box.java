package rayTracer.scene.geo;

import rayTracer.scene.shaders.Shader;

public class Box extends Shape {

	private Point3D min;
	private Point3D max;
	
	public Box(Point3D min, Point3D max, Shader shader) {
		super(shader);
		this.min = min;
		this.max = max;
	}

	@Override
	public Point3D intersect(Ray3D ray) {
		double tNear = Double.MIN_VALUE;
		double tFar = Double.MAX_VALUE;
		double[]times = {tNear, tFar};
		
		double xd = ray.getDirection().getX();
		double xo = ray.getOrigin().getX();
		double xl = min.getX();
		double xh = max.getX();
		double[] xTimes = planerIntersect(times, xd, xo, xl, xh);
		if(xTimes == null){
			return null;
		}
		times = xTimes;
		double yd = ray.getDirection().getY();
		double yo = ray.getOrigin().getY();
		double yl = min.getY();
		double yh = max.getY();
		double[] yTimes = planerIntersect(times, yd, yo, yl, yh);
		if(yTimes == null){
			return null;
		}
		times = yTimes;
		double zd = ray.getDirection().getZ();
		double zo = ray.getOrigin().getZ();
		double zl = min.getZ();
		double zh = max.getZ();
		double[] zTimes = planerIntersect(times, zd, zo, zl, zh);
		if(zTimes == null){
			return null;
		}
		times = yTimes;
		return ray.getOrigin().add(ray.getDirection().multiply(times[0]));
	}
	
	private double[] planerIntersect(double[] t, double direction, double origin, double min, double max){
		if(direction == 0) {
			//The ray is parallel to the planes
			if(origin < min || origin > max) {
				//Origin is not between planes
				return null;
			}
		} else {
			double t1 = (min - origin)/direction;
			double t2 = (max - origin)/direction;
			if(t1 > t2){
				double temp = t1;
				t1 = t2;
				t2 = temp;
			}
			if(t1 > t[0]) {
				t[0] = t1;
			}
			if(t2 < t[1]) {
				t[1] = t2;
			}
			if(t[0] > t[1]) {
				//Box is missed
				return null;
			}
			if(t[1] < 0) {
				//box is behind ray
				return null;
			}
		}
		return t;
	}

	@Override
	public Point3D getNormal(Point3D closest) {
		//TODO I don't know how to calculate the normal for these boxes
		return closest;
	}

	@Override
	public Box getBoudingBox() {
		return this;
	}

	public Point3D getMin() {
		return min;
	}

	public void setMin(Point3D min) {
		this.min = min;
	}

	public Point3D getMax() {
		return max;
	}

	public void setMax(Point3D max) {
		this.max = max;
	}

}
