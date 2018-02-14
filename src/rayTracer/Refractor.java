package rayTracer;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.geo.Ray3D;

public class Refractor {


	public static void main(String[] args) {
		for(int i = 0; i < 360; i+=5){
			makeReport(Math.toRadians(i));
		}
		double sqrt2 = Math.sqrt(2);
		Point3D direction = new Point3D(1/sqrt2, -1/sqrt2, 0);
		Ray3D ray = new Ray3D(new Point3D(0, 0, 0), direction);
		Point3D normal = new Point3D(0, 1, 0);
		double indexOfRefraction1 = 1.33;
		double indexOfRefraction2 = 1.52;
		
		Point3D newDirection = refract(ray, normal, indexOfRefraction1, indexOfRefraction2);
		System.out.println(direction);
		System.out.println(newDirection);
		System.out.println();
	}
	
	public static void makeReport(double radians){
		double radius = 2;
		double xPos = radius * Math.cos(radians);
		double zPos = radius * Math.sin(radians);
		Point3D direction = new Point3D(xPos, 0, zPos);
		Ray3D ray = new Ray3D(new Point3D(0, 0, 0), direction);
		Point3D normal = new Point3D(-1, 0, 0);
		double indexOfRefraction1 = 1.0003;
		double indexOfRefraction2 = 1.5;
		
		Point3D newDirection = refract(ray, normal, indexOfRefraction1, indexOfRefraction1);
		System.out.println(Math.toDegrees(radians) + " degrees");
		System.out.println(radians + " radians");
		System.out.println(direction);
		System.out.println(newDirection);
		System.out.println();
	}
	
	public static Point3D refract(Ray3D ray, Point3D normal, double indexOfRefraction1, double indexOfRefraction2){
		Point3D I = ray.getDirection();
		Point3D T = null;
		Point3D N = normal;
		double theta = I.getSmallAngle(N);
		double othertheta = I.getAngle(N);
		double n1 = indexOfRefraction1;
		double n2 = indexOfRefraction2;
		double n = n1/n2;
		double c = Math.cos(theta);
		
//		System.out.println(I);
//		System.out.println(normal);
//		System.out.print("N: " + n + " should be ");
//		System.out.println("0.875");
//		System.out.print(theta + " should be ");
//		System.out.println(Math.PI/4.0);
//		System.out.println(othertheta);
		
		Point3D vector1 = I.multiply(n);
		double s = Math.sqrt(1 + Math.pow(n, 1) * (Math.pow(c, 1) + 1) );
		double scalar1 = n + c - s;
		Point3D vector2 = N.multiply(scalar1);
		T = vector1.add(vector2);
		return T;
	}
}
