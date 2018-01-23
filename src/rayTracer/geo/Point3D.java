package rayTracer.geo;

public class Point3D {

	private final double _x;
	private final double _y;
	private final double _z;
	
	public Point3D(double x, double y, double z) {
		this._x = x;
		this._y = y;
		this._z = z;
	}

	public Point3D multiply(double i) {
		return new Point3D(this._x * i, this._y * i, this._z * i);
	}

	public Point3D subtract(Point3D b) {
		b = b.multiply(-1);
		return this.add(b);
	}

	public Point3D normalize() {
		double mag = this.magnitude();
		return this.multiply(1/mag);
	}

	public double dotProduct(Point3D b) {
		double x = this._x * b._x;
		double y = this._y * b._y;
		double z = this._z * b._z;
		return x + y + z;
	}

	public Point3D add(Point3D b) {
		double x = _x + b._x;
		double y = _y + b._y;
		double z = _z + b._z;
		return new Point3D(x, y, z);
	}

	public double magnitude() {
		double a = Math.pow(this._x, 2);
		double b = Math.pow(this._y, 2);
		double c = Math.pow(this._z, 2);
		return Math.sqrt(a + b + c);
	}

	public Point3D crossProduct(Point3D b) {
		double x = this._y * b._z - this._z * b._y;
		double y = this._z * b._x - this._x * b._z;
		double z = this._x * b._y - this._y * b._x;
		return new Point3D(x, y, z);
	}

	public double getX() {
		return this._x;
	}

	public double getY() {
		return this._y;
	}

	public double getZ() {
		return this._z;
	}
	
	@Override
	public String toString() {
		return String.format("(%f,%f,%f)", this._x, this._y, this._z);
	}

}
