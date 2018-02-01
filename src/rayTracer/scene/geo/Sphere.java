package rayTracer.scene.geo;

import rayTracer.scene.shaders.Color;

public class Sphere extends Shape {

	private Point3D center;
	private double radius;
	
	public Sphere(Point3D center, double radius, int type, Color reflectInt) {
		super(type, reflectInt);
		this.center = center;
		this.radius = radius;
		if(type != REFLECTIVE){
			System.out.println("Warning you aren't doing the constructor you think you are. Reflective");
		}
	}

	public Sphere(Point3D center, double radius, int type, Color diffuse, Color highlight, int phongConst) {
		super(type,diffuse,highlight,phongConst);
		this.center = center;
		this.radius = radius;
		if(type != DIFFUSE){
			System.out.println("Warning you aren't doing the constructor you think you are. Diffuse");
		}
	}

	public Sphere(Point3D center, double radius, int type, Color color, double indexOfRefraction) {
		super(type,color,indexOfRefraction);
		this.center = center;
		this.radius = radius;
		if(type != TRANSPARENT){
			System.out.println("Warning you aren't doing the constructor you think you are. Transparent");
		}
	}

	@Override
	public Point3D intersect(Ray3D ray) {
		// TODO Auto-generated method stub
		boolean useGeometric = false;
		if(useGeometric){
			return geometricIntersection(ray);
		} else {
			return quadraticIntersection(ray);
		}
		
	}
	
	private Point3D quadraticIntersection(Ray3D ray){
		double t;
		Point3D r0 = ray.getOrigin();
		Point3D rD = ray.getDirection().normalize();
		//Calculate B and C of the quadratic
		//B = 2(xdxo - xdxc + ydyo - ydyc + zdzo - zdzc)
		//System.out.println(rD.toString());
		//System.out.println(rO.toString());
		//System.out.println(center.toString());
		double b = 2 * (
				  rD.getX() * r0.getX() - rD.getX() * center.getX()
				+ rD.getY() * r0.getY() - rD.getY() * center.getY()
				+ rD.getZ() * r0.getZ() - rD.getZ() * center.getZ());

		//C = xo^2- 2xoxc+ xc^2+ yo^2- 2yoyc+ yc^2+ zo^2-2zozc+ zc^2 - r^2 
		double c = 
				  Math.pow(r0.getX(), 2) - 2*r0.getX()*center.getX() + Math.pow(center.getX(), 2)
				+ Math.pow(r0.getY(), 2) - 2*r0.getY()*center.getY() + Math.pow(center.getY(), 2)
				+ Math.pow(r0.getZ(), 2) - 2*r0.getZ()*center.getZ() + Math.pow(center.getZ(), 2)
				- Math.pow(radius, 2);
		
		//Calculate the discriminant D = B^2-4C
		double d = Math.pow(b, 2)-4*c;
		//If D < 0 return null; (no interesection point)
		//System.out.println("b " + b + " c: " + c);
		if(d < 0){
			return null;
		}
		//Calclate smaller intersection paramter
			//t0 = (-B-sqrt(D))/2
		double t0 = (-b - Math.sqrt(d))/2;
		//If t0 < 0 then calculate larger t value
		if(t0 <= 0){
			//t1 = (B + sqrt(D))/2
			double t1 = (-b + Math.sqrt(d))/2;
			//If t1 < 0 return null (intersetion point behind ray)
			if(t1 <= 0){
				return null;
			} else { //else set t = t1
				t = t1;
				if(t1 < 1){
					//System.out.println("It's t1 and its between 0 and 1");
				} else {
					//System.out.println("It's t1");
				}
			}
		} else {//else set t = t0
			t = t0;
			if(t0 < 1){
				//System.out.println("It's t0 and its between 0 and 1");
			} else {
				//System.out.println("It's t0");
			}
		}
		//return intersection point p = r(t) = r0 + rdt
		return r0.add(rD.multiply(t));
	}
	
	private Point3D geometricIntersection(Ray3D ray){
		//Set up for ease of read
		Point3D r0 = ray.getOrigin();
		Point3D rd = ray.getDirection().normalize();
		
		//1 Determine whether the ray's origin is outside the sphere
		Point3D OC = center.subtract(ray.getOrigin());
		boolean inside = false;
		if(OC.magnitude() < radius){
			inside = true;
		}
		/*
		 * Project A onto B
		 *  1) Unit vector B = rd
		 *  2) a.dotProduct(b)
		 *  3) b.multpliy(dot) = aOnB
		 */
		//2 Find the closest approach of the ray to the sphere's center.
		Point3D aOnB = rd.multiply(rd.dotProduct(OC));
		double d = OC.subtract(aOnB).magnitude();
		double tca = rd.dotProduct(OC);
		
		//3 If tca < 0 and r0 lies outside the sphere, the ray does not intersect the sphere
		if(tca < 0 && !inside){
			return null;
		}
		
		//4 Otherwise, compute thc, the distance from the closet approach to the sphere's surface
		/*
		 * 	thc^2 = r^2 - d^2
			d^2 = ||OC||^2 - tca^2,  so
			thc^2 = r^2 - ||OC||^2 + tca^2

		 */
		double thcSqrd = Math.pow(radius, 2) - Math.pow(OC.magnitude(), 2) + Math.pow(tca, 2);
		//double thcSqrd = (Math.pow(radius, 2) - Math.pow(d, 2)); // So did I just calculate d for nothing?
		
		//5 If thcSqrd < 0 the ray does not intersect the sphere
		if(thcSqrd < 0){
			return null;
		}
		
		//6 Otherwise calculate the intersection distance
		double t;
		if(inside){
			t = tca - Math.sqrt(thcSqrd);
		} else {
			t = tca + Math.sqrt(thcSqrd);
		}
		
		Point3D result = r0.add(rd.multiply(t));
		
		return result;
	}
	
	public String toString(){
		String colorName;
		double red = this.color.getRed();
		double green = this.color.getGreen();
		double blue = this.color.getBlue();
		if(red == 1 && green == 1 && blue == 1) {
			colorName = "white";
		} else if(red == 1 && green == 0 && blue == 1) {
			colorName = "magenta";
		} else if(red == 0 && green == 1 && blue == 1) {
			colorName = "cyan";
		} else if(red == 0 && green == 0 && blue == 1) {
			colorName = "blue";
		} else if(red == 0 && green == 1 && blue == 0) {
			colorName = "green";
		} else if(red == 1 && green == 0 && blue == 0) {
			colorName = "red";
		} else if(red == 1 && green == 1 && blue == 0) {
			colorName = "yellow";
		} else {
			colorName = "black";
		}
		return "Sphere " + center + " " + colorName;
	}

	@Override
	public Point3D getNormal(Point3D i) {
		double x = (i.getX() - center.getX())/radius;
		double y = (i.getY() - center.getY())/radius;
		double z = (i.getZ() - center.getZ())/radius;
		
		return new Point3D(x,y,z);
	}

	public double getRadius(){
		return radius;
	}

	public Point3D getCenter() {
		return center;
	}
}
