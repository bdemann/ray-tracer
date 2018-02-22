package rayTracer.scene.geo;

import rayTracer.random.Gaussian;

public class Square {
	
	Point3D center;
	Point3D x;
	Point3D y;
	double size;

	public Square(Point3D dir, double size) {
		Point3D n = Point3D.ORIGIN.subtract(dir);
		center = dir;
		this.size = size;
		Point3D u = n.crossProduct(Point3D.UP).normalize();
		Point3D v = u.crossProduct(n).normalize();

		x = u;
		y = v;
	}
	
	public Point3D getRandomPoint(){
		double[] offset = Gaussian.gaussian(0);
		return center.add(x.multiply(size * offset[0])).add(y.multiply(size * offset[1]));
	}

}
