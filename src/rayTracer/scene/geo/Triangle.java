package rayTracer.scene.geo;

import rayTracer.scene.shaders.Color;

public class Triangle extends Shape {

	private Point3D a;
	private Point3D b;
	private Point3D c;

	public Triangle(Point3D a, Point3D b, Point3D c, int type, Color color, Color highlight, int phongConst) {
		super(type, color, highlight, phongConst);
		this.a = a.multiply(1);
		this.b = b.multiply(1);
		this.c = c.multiply(1);
	}
	
	private double distanceToOrigin(){
		Point3D x = a.subtract(b).normalize();
		Point3D y = a.subtract(c).normalize();
		Point3D z = a.subtract(new Point3D(0,0,0));
		
		Point3D zOnX = x.multiply(x.dotProduct(z));
		Point3D zOnY = y.multiply(y.dotProduct(z));
		
		Point3D result = zOnX.add(zOnY);
		
		return result.magnitude();
	}
	
	@Override
	public Point3D intersect(Ray3D ray) {
		Point3D r0 = ray.getOrigin();
		Point3D rd = ray.getDirection().normalize();
		Point3D n = getNormal(null).normalize();
		
		/*
		 * calculate distance from (0,0,0) to plane
		 * We should be able to do this by getting a vector from on of the triangle points to the origin. Next we need to dot project it to the normal vector
		 */
		Point3D toOrigin = new Point3D(0,0,0).subtract(this.a);
		//double d = n.multiply(toOrigin.dotProduct(n)).magnitude();

		double d = toOrigin.dotProduct(n);
		
		double tNum = -(n.dotProduct(r0) + d);
		double tDen = n.dotProduct(rd);
		
		if(tDen == 0){
			return null;
		}
		
//		if(tDen > 0){
//			return null;
//		}
		
		double t = tNum/tDen;
		
		if(t <= 0){
			return null;
		}
		
		Point3D point = r0.add(rd.multiply(t));
		
		if(!sameSide(this.a, this.b, point, this.c)){
			return null;
		}
		if(!sameSide(this.b, this.c, point, this.a)){
			return null;
		}
		if(!sameSide(this.c, this.a, point, this.b)){
			return null;
		}
		
		//System.out.println("This is the point in the triangle: " + point);
		
		return point;
	}
	
	private boolean sameSide(Point3D a, Point3D b, Point3D yes, Point3D maybe){
		Point3D cp1 = b.subtract(a).crossProduct(maybe.subtract(a));
		Point3D cp2 = b.subtract(a).crossProduct(yes.subtract(a));
		if(cp1.dotProduct(cp2) >= 0){
			return true;
		}
		return false;
	}

	@Override
	public Point3D getNormal(Point3D closest) {
		return b.subtract(a).crossProduct(c.subtract(a));
	}
	
	public String toString(){
		return "Triangle";
	}

}
