package rayTracer.scene;

import rayTracer.output.ImageBuffer;
import rayTracer.scene.geo.Point3D;
import rayTracer.scene.shaders.Color;

public class Camera {
	
	private ImageBuffer result;
	private Point3D eye;
	private Point3D lookAt;
	private double fieldOfView;
	private double focalLength;
	private double viewWindowWidth;
	private double viewWindowHeight;
	private Point3D u;
	private Point3D v;
	private Point3D n;

	public static void main(String[] args) {

	}
	
	public Camera(Point3D centerOfProjection, Point3D lookAtPoint, Point3D upVector, int resX, int resY, double fov){
		result = new ImageBuffer(resX,resY);
		this.eye = centerOfProjection;
		this.lookAt = lookAtPoint;
		this.setUpViewPort(fov);
		
		n = this.eye.subtract(this.lookAt).normalize();
		u = upVector.crossProduct(n).normalize();
		v = this.n.crossProduct(this.u).normalize();
	}
	
	private void setUpViewPort(double fov){
		fieldOfView = Math.toRadians(fov);
		focalLength = eye.subtract(lookAt).magnitude();
		viewWindowWidth = 2 * focalLength * (Math.tan(fieldOfView/2));
		viewWindowHeight = viewWindowWidth * ((double)result.getHeight()/result.getWidth());
	}

	
	public Point3D getPixelCenter(int i, int j) {
		double width = result.getWidth();
		double stepX = viewWindowWidth/width;
		double height = result.getHeight();
		double stepY = viewWindowHeight/height;
		
		double iTrans = i - width/2;
		double jTrans = j - height/2;
		
		double u = iTrans * stepX + stepX/2;
		double v = jTrans * stepY + stepY/2;

		Point3D windowPoint = this.viewPortToWindow(i, j);
//		u = windowPoint.getX() + step/2;
//		v = windowPoint.getY() + step/2;
		double n = windowPoint.getZ();
		
		return new Point3D(u,v*-1,n);
	}
	
	public Point3D getPixelCenter(int i, int j, boolean special) {
		double width = result.getWidth();
		double stepX = viewWindowWidth/width;
		double height = result.getHeight();
		double stepY = viewWindowHeight/height;
		
		Point3D windowPoint = this.viewPortToWindow(i, j);
		
		double u = windowPoint.getX() + stepX/2;
		double v = windowPoint.getY() + stepY/2;
		double n = windowPoint.getZ();
		
		return windowToWorld(new Point3D(u, -v, n));
	}
	
	public Point3D windowToWorld(Point3D windowPoint) {
		Point3D newU = u.multiply(windowPoint.getX());
		Point3D newV = v.multiply(windowPoint.getY());
		Point3D newN = n.multiply(windowPoint.getZ());
		Point3D s = lookAt.add(newU.add(newV).add(newN));
		return s;
	}
	
	public Point3D viewPortToWindow(int i, int j) {
		int iMin = 0;
		int jMin = 0;
		int iMax = result.getWidth();
		int jMax = result.getHeight();
		
		double uMin = -viewWindowWidth/2.0;
		double uMax = uMin + viewWindowWidth;
		double vMin = -viewWindowHeight/2.0;
		double vMax = vMin + viewWindowHeight;
		
		double u = (i - iMin) * ((uMax - uMin)/(iMax - iMin)) + uMin;
		double v = (j - jMin) * ((vMax - vMin)/(jMax - jMin)) + vMin;
		
		return new Point3D(u,v,0);
	}

	public int getWidth() {
		return result.getWidth();
	}
	
	public int getHeight() {
		return result.getHeight();
	}

	public void captureColor(int i, int j, Color c) {
		result.setColor(i, j, c);
	}

	public void save(String fileName) {
		result.save(fileName);
	}

	public Point3D getCenterOfProjection() {
		return eye;
	}
}
