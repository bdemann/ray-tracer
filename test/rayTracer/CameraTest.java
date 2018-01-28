//package rayTracer;
//
//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import javafx.geometry.Point3D;
//
//public class CameraTest {
//
//	@Test
//	public void test() {
////		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void testViewToWindow() {
//		Point3D cop = new Point3D(0,0,1);
//		Point3D lap = new Point3D(0,0,0);
//		Point3D uVec = new Point3D(0,1,0);
//		int width = 1920;
//		int height = 1080;
//		for(int j = 1079; j < height; j++){
//			for(int i = 1910; i < width; i++){
//				Camera cam = new Camera(cop, lap, uVec, width, height, 60);
//				Point3D result = cam.viewPortToWindow(i, j);
//				assert(result.getX() == -width/2.0 + i);
//				assert(result.getY() == -height/2.0 + j);
//				assert(result.getZ() == 0);
//			}
//		}
//		
//	}
//	
//	@Test
//	public void testOneViewToWindow() {
//		Point3D cop = new Point3D(0,0,1);
//		Point3D lap = new Point3D(0,0,0);
//		Point3D uVec = new Point3D(0,1,0);
//	
//		Camera cam = new Camera(cop, lap, uVec, 4, 3, 60);
//		Point3D result = cam.viewPortToWindow(0, 0);
//		assert(result.getX() == -2);
//		assert(result.getY() == -1.5);
//		result = cam.viewPortToWindow(1, 1);
//		assert(result.getX() == -1);
//		assert(result.getY() == -0.5);
//		result = cam.viewPortToWindow(2, 2);
//		assert(result.getX() == 0);
//		assert(result.getY() == 0.5);
//		result = cam.viewPortToWindow(3, 2);
//		assert(result.getX() == 1);
//		assert(result.getY() == 0.5);
//	}
//	
//	@Test
//	public void testPixelCenter() {
//		Point3D cop = new Point3D(0,0,1);
//		Point3D lap = new Point3D(0,0,0);
//		Point3D uVec = new Point3D(0,1,0);
//	
//		Camera cam = new Camera(cop, lap, uVec, 4, 3, 60);
//		Point3D result = cam.getPixelCenter(0, 0);
//		System.out.println(result);
//		result = cam.getPixelCenter(0, 0, true);
//		System.out.println(result);
//		assert(result.getX() == -1.5);
//		assert(result.getY() == -1.5);
//		assert(result.getZ() == 0);
//	}
//	
//	@Test
//	public void testWindowToWorld() {
//		Point3D cop = new Point3D(0,0,1);
//		Point3D lap = new Point3D(0,0,0);
//		Point3D uVec = new Point3D(0,1,0);
//	
//		Camera cam = new Camera(cop, lap, uVec, 4, 3, 60);
//		Point3D result = cam.viewPortToWindow(0, 0);
//		result = cam.windowToWorld(result);
//		assert(result.getX() == -2);
//		assert(result.getY() == -1.5);
//		assert(result.getZ() == 0);
//		
//		//Move in Z
//		cop = new Point3D(0,0,2);
//		lap = new Point3D(0,0,1);
//		uVec = new Point3D(0,1,0);
//	
//		cam = new Camera(cop, lap, uVec, 4, 3, 60);
//		result = cam.viewPortToWindow(0, 0);
//		result = cam.windowToWorld(result);
//		assert(result.getX() == -2);
//		assert(result.getY() == -1.5);
//		assert(result.getZ() == 1);
//		
//		//Move in X
//		cop = new Point3D(1,0,1);
//		lap = new Point3D(1,0,0);
//		uVec = new Point3D(0,1,0);
//	
//		cam = new Camera(cop, lap, uVec, 4, 3, 60);
//		result = cam.viewPortToWindow(0, 0);
//		result = cam.windowToWorld(result);
//		assert(result.getX() == -1);
//		assert(result.getY() == -1.5);
//		assert(result.getZ() == 0);
//		
//		//Move in Y
//		cop = new Point3D(0,1,1);
//		lap = new Point3D(0,1,0);
//		uVec = new Point3D(0,1,0);
//	
//		cam = new Camera(cop, lap, uVec, 4, 3, 60);
//		result = cam.viewPortToWindow(0, 0);
//		result = cam.windowToWorld(result);
//		assert(result.getX() == -2);
//		assert(result.getY() == -0.5);
//		assert(result.getZ() == 0);
//	}
//
//}
