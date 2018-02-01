package rayTracer.scene;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.geo.Shape;
import rayTracer.scene.geo.Sphere;
import rayTracer.scene.geo.Triangle;
import rayTracer.scene.lights.AmbientLight;
import rayTracer.scene.lights.DirectionalLight;
import rayTracer.scene.shaders.Color;

public class TestScenes {

	private Scene testScene;
	
	public TestScenes(int scene){
		testScene = new Scene();

		int frame = 0;
		
		switch(scene){
		case 0:
			makeTestScene(frame);
			break;
		case 1:
			makeScene1(frame);
			break;
		case 2:
			makeScene2(frame);
			break;
		case 3:
			makeScene3(frame);
			break;
		case 4:
			makeScene(frame);
			break;
		case 5:
			makeScene5(frame);
			break;
		default:
			makeTestScene(frame);
		}
	}
	
	public void makeTestScene(int frame){
		Point3D eye = new Point3D(0, 0, 1.2);
		Point3D lookAt = new Point3D(0,0,0);
		Point3D upVector = new Point3D(0,1,0);
		testScene.addCamera(new Camera(eye, lookAt, upVector, 300, 300, 55));
		
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		
		testScene.setAmbient(new AmbientLight(new Color(0,0,0)));
		testScene.addLight(new DirectionalLight(new Color(1,1,1), new Point3D(0,0,1)));
		
		Color sphereColor = new Color(0.0, 0.75, 0.75);
		Color sphereColor2 = new Color(0.0, 0, 0.75);
		Color sphereColor3 = new Color(1, 0, 0);
		double radius = .5;
		double radius2 = .05;
		testScene.addShape(new Sphere(new Point3D(0, 0, 0), radius, Shape.REFLECTIVE, sphereColor));
		testScene.addShape(new Sphere(new Point3D(0.1, 0.1, 0.5), radius2, Shape.DIFFUSE, sphereColor2, new Color(1,1,1), 32));
		testScene.addShape(new Sphere(new Point3D(-0.1, -0.1, 0.6), radius2, Shape.DIFFUSE, sphereColor3, new Color(1,1,1), 32));
		testScene.addShape(new Sphere(new Point3D(-.1, .15, .7), .05, Shape.DIFFUSE, new Color(0,1,0), new Color(1,1,1), 32));
		testScene.addShape(new Triangle(new Point3D(0, 0.2, 0), new Point3D(0.2, 0.2, 0), new Point3D(-0.2, -0.2, 0), Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 4));
//		shapes.add(new Triangle(new Point3D(0, 0.5, 0), new Point3D(0.5, 0, 0), new Point3D(-0.5, 0, 0), Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 4));
//		shapes.add(new Sphere(new Point3D(0, 0.5, 0), .05, Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 32));
//		shapes.add(new Sphere(new Point3D(0.5, 0, 0), .05, Shape.DIFFUSE, new Color(0,1,0), new Color(1,1,1), 32));
//		shapes.add(new Sphere(new Point3D(-0.5, 0, 0), .05, Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
	}
	
	private void makeScene1(int frame){
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(frame));
		double zPos = radius * Math.sin(Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 900;
		System.out.print("This is the eye location at frame ");
		System.out.print(frame);
		System.out.println(eye);
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 54));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		testScene.addShape(new Sphere(new Point3D(0.35, 0, -0.1), 0.05, Shape.DIFFUSE, new Color(1,1,1), new Color(1,1,1), 4));
		testScene.addShape(new Sphere(new Point3D(0.2, 0, -0.1), 0.075, Shape.DIFFUSE, new Color(1,0,0), new Color(.5,1,.5), 32));
		testScene.addShape(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 32));
		testScene.addShape(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
		testScene.addShape(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene5(int frame){
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(frame));
		double zPos = radius * Math.sin(Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(xPos,0, 1 - zPos);
		Point3D eye = new Point3D(0, 0, 1);
		Point3D upVector = new Point3D(0,1,0);
		int width = 900;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 54));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		Color backgroundColor = new Color(0.2, 0.2, 0.2);
		testScene.setBackgroundColor(backgroundColor);
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		testScene.addShape(new Sphere(new Point3D(0.35, 0, -0.1), 0.05, Shape.DIFFUSE, new Color(1,1,1), new Color(1,1,1), 4));
		testScene.addShape(new Sphere(new Point3D(0.2, 0, -0.1), 0.075, Shape.DIFFUSE, new Color(1,0,0), new Color(.5,1,.5), 32));
		testScene.addShape(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 32));
		testScene.addShape(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
		testScene.addShape(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene(int frame){
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(frame));
		double zPos = radius * Math.sin(Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 900;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 54));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		Color backgroundColor = new Color(0.2, 0.2, 0.2);
		testScene.setBackgroundColor(backgroundColor);
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		double angle = Math.acos(1.0/3.0) - Math.toRadians(90);
		double bottomAngle = Math.toRadians(120) - Math.toRadians(90);
		
		double largeRadius = 0.3;
		double smallRadius = 0.05;
		double legLen = largeRadius + smallRadius;
		double yOffset = legLen * Math.sin(angle);
		double xOffset = legLen * Math.cos(angle);
		
		double xOffsetBot = Math.sin(bottomAngle) * xOffset;
		double zOffsetBot = Math.cos(bottomAngle) * xOffset;
		
		testScene.addShape(new Sphere(new Point3D(0, 0, 0), largeRadius, Shape.DIFFUSE, new Color(1,1,1), new Color(1,1,1), 4));
		testScene.addShape(new Sphere(new Point3D(0, legLen, 0), smallRadius, Shape.DIFFUSE, new Color(1,0,1), new Color(1,1,1), 4));
		testScene.addShape(new Sphere(new Point3D(xOffset, yOffset, 0), smallRadius, Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
		testScene.addShape(new Sphere(new Point3D(-xOffsetBot, yOffset, zOffsetBot), smallRadius, Shape.DIFFUSE, new Color(0,1,1), new Color(1,1,1), 4));
		testScene.addShape(new Sphere(new Point3D(-xOffsetBot, yOffset, -zOffsetBot), smallRadius, Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 4));
//		shapes.add(new Sphere(new Point3D(0, 0, -0.1), 0.075, Shape.DIFFUSE, new Color(1,0,0), new Color(.5,1,.5), 32));
//		shapes.add(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 32));
//		shapes.add(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
//		shapes.add(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene2(int frame){
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(0, 0, 1.2);
		Point3D upVector = new Point3D(0,1,0);
		testScene.addCamera(new Camera(eye, lookAt, upVector, 300, 300, 110));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(0,1,0);		
		Color ambientColor = new Color(0,0,0);
		Color backgroundColor = new Color(0.2, 0.2, 0.2);
		testScene.setBackgroundColor(backgroundColor);
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		testScene.addShape(new Sphere(new Point3D(0, 0.3, 0), 0.2, Shape.REFLECTIVE, new Color(0.75,0.75,0.75)));
		//shapes.add(new Sphere(new Point3D(0, 0.3, 0), 0.2, Shape.DIFFUSE, new Color(0.75,0.75,0.75), new Color(1,1,1), 4));
		testScene.addShape(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(1, 0.5, 0), new Point3D(0, -0.5, -0.5), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 4));
		testScene.addShape(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(0, -0.5, -0.5), new Point3D(-1, 0.5, 0), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void makeScene3(int frame){
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(0, 0, 1.2);
		Point3D upVector = new Point3D(0,1,0);
		testScene.addCamera(new Camera(eye, lookAt, upVector, 300, 300, 35));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(0,0,1);		
		Color ambientColor = new Color(0,0,0);
		Color backgroundColor = new Color(0.2, 0.2, 0.2);
		testScene.setBackgroundColor(backgroundColor);
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));
		
		testScene.addShape(new Sphere(new Point3D(0,0,0), 0.2322, Shape.TRANSPARENT, new Color(1,1,1), 1.3));
		testScene.addShape(new Sphere(new Point3D(0.25, 0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(1,1,0), new Color(0,0,0), 4));
		testScene.addShape(new Sphere(new Point3D(-0.25, 0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(1,0,0), new Color(0,0,0), 4));
		testScene.addShape(new Sphere(new Point3D(-0.25, -0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(0,0,1), new Color(0,0,0), 4));
		testScene.addShape(new Sphere(new Point3D(0.25, -0.25, -0.6), 0.1161, Shape.DIFFUSE, new Color(0,1,0), new Color(0,0,0), 4));
	}

	public Scene getTestScene() {
		return testScene;
	}
}
