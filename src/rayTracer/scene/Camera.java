package rayTracer.scene;

import java.util.Random;

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
		double[] result = {0,0};
		int sampleSize = 10000;
		for(int i = 0; i < sampleSize; i++){
			double[] temp = gaussian(i);
			result[0] += temp[0];
			result[1] += temp[1];
//			result += random(i);
		}
		System.out.println(result[0]/sampleSize);
		System.out.println(result[1]/sampleSize);
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
	
	public Point3D getPixelCenter(int i, int j, boolean distributed) {
		double width = result.getWidth();
		double stepX = viewWindowWidth/width;
		double height = result.getHeight();
		double stepY = viewWindowHeight/height;
		
		Point3D windowPoint = this.viewPortToWindow(i, j);
		
		double u = windowPoint.getX() + stepX/2;
		double v = windowPoint.getY() + stepY/2;
		double n = windowPoint.getZ();
		
		u = rand(u + stepX/2, u - stepX/2);
		v = rand(v + stepY/2, v - stepY/2);
		
//		u *= random(1);
//		v *= random(1);
		
		return windowToWorld(new Point3D(u, -v, n));
	}
	
	private static double rand(double max, double min) {
		Random rand = new Random();
		double exponent = -(Math.pow(rand.nextDouble() - .5, 2) * 20);
		double gaussian = Math.exp(exponent);
		double result = gaussian * (max - min) + min;
		return result;
	}
	
	private static double[] gaussian(long seed){
		Random rand = new Random();
		double x1, x2, w, y1, y2;
		 
        do {
                x1 = 2.0 * rand.nextDouble() - 1.0;
                x2 = 2.0 * rand.nextDouble() - 1.0;
                w = x1 * x1 + x2 * x2;
        } while ( w >= 1.0 );

        w = Math.sqrt( (-2.0 * Math.log( w ) ) / w );
        y1 = x1 * w;
        y2 = x2 * w;
        double[]result = {y1, y2};
        return result;
	}
	
	private static double random(long seed) {
		Random rand = new Random();
		return rand.nextDouble();
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
