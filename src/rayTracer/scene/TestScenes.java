package rayTracer.scene;

import rayTracer.scene.geo.Point3D;
import rayTracer.scene.geo.Sphere;
import rayTracer.scene.geo.Triangle;
import rayTracer.scene.lights.AmbientLight;
import rayTracer.scene.lights.DirectionalLight;
import rayTracer.scene.lights.PointLight;
import rayTracer.scene.shaders.Color;
import rayTracer.scene.shaders.Shader;

public class TestScenes {

	private IScene testScene;
	
	public TestScenes(int scene){

//		int frame = 0;
//		setUpScene(scene, frame);
	}
	private void setUpScene(int scene, int frame){

		testScene = new Scene();
		
		switch(scene){
		case 0:
			makeTestScene(frame);
			break;
		case 1:
			triangleSpheresInLine(frame);
			break;
		case 2:
			trianglesInReflection(frame);
			break;
		case 3:
			spheresInRefraction(frame);
			break;
		case 4:
			tetrahedralSpheres(frame);
			break;
		case 5:
			justTrianglesInLine(frame);
			break;
		case 6:
			refractionPlane(frame);
			break;
		case 7:
			spheres(frame);
			break;
		case 8:
			pointLights(frame);
			break;
		case 9:
			boxedInScene(frame);
			break;
		case 10:
			boxScene(frame);
			break;
		case 11:
			doubleRefractivePlane(frame);
			break;
		default:
			makeTestScene(frame);
		}
	}
	
	public void refractionTriangle(int frame){
		System.out.print(frame);
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(5 * frame));
		double zPos = radius * Math.sin(Math.toRadians(5 * frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 300;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 54));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));
		
		testScene.addShape(new Sphere(new Point3D(0.35, 0, -0.1), 0.05, Shader.WHITE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0.2, 0, -0.1), 0.075, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shader.GREEN_REFLECTIVE));
		testScene.addShape(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shader.BLUE_TRANSPARENT));
		testScene.addShape(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shader.YELLOW_DIFFUSE));
	}
	
	public void refractionPlane(int frame){
		System.out.print(frame);
		double radius = 2;
		double xPos = radius * Math.cos(5 * Math.toRadians(frame));
		double zPos = radius * Math.sin(5 * Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 300;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 60));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		double x = 0.6;
		double y = 0.6;
		double z = 0;
		double rad = 0.075;
		
		//Spheres on the rectangle and set back a little bit
		testScene.addShape(new Sphere(new Point3D(x, 0, rad), rad, Shader.BLUE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-x, 0, -rad), rad, Shader.YELLOW_DIFFUSE));
		
		//Spheres in the back and the front
		testScene.addShape(new Sphere(new Point3D(0, 0, 1), rad, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, 0, -1), rad, Shader.GREEN_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(x, y, z), new Point3D(-x, -y, z), new Point3D(-x, y, z), Shader.BLUE_TRANSPARENT));
		testScene.addShape(new Triangle(new Point3D(x, -y, z), new Point3D(-x, -y, z), new Point3D(x, y, z), Shader.BLUE_TRANSPARENT));
//		testScene.addShape(new Sphere(new Point3D(0, 0, 0), x, Shape.DIFFUSE, new Color(0,1,1), new Color(.5,1,.5), 32));
//		testScene.addShape(new Triangle(new Point3D(x, y, z), new Point3D(-x, -y, z), new Point3D(-x, y, z), Shape.DIFFUSE, new Color(0,0,1), new Color(.5,1,.5), 15));
//		testScene.addShape(new Triangle(new Point3D(x, y, z), new Point3D(-x, -y, z), new Point3D(x, -y, z), Shape.DIFFUSE, new Color(0,0,1), new Color(.5,1,.5), 15));
	}
	
	public void doubleRefractivePlane(int frame){
		double radius = 2;
		double xPos = radius * Math.cos(5 * Math.toRadians(frame));
		double zPos = radius * Math.sin(5 * Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 300;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 60));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		double x = 0.6;
		double y = 0.6;
		double z = 1;
		double rad = 0.075;
		
		//Spheres in the back and the front
		testScene.addShape(new Sphere(new Point3D(0, 0, z), rad * 3, Shader.GREEN_TRANSPARENT));
		testScene.addShape(new Sphere(new Point3D(0, 0, z + 2), rad, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, 0, z - 2), rad, Shader.BLUE_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(x, y, z -1), new Point3D(-x, -y, z -1), new Point3D(-x, y, z -1), Shader.BLUE_TRANSPARENT));
		testScene.addShape(new Triangle(new Point3D(x, -y, z -1), new Point3D(-x, -y, z -1), new Point3D(x, y, z -1), Shader.BLUE_TRANSPARENT));
		testScene.addShape(new Triangle(new Point3D(x, y, z + 1), new Point3D(-x, -y, z + 1), new Point3D(-x, y, z + 1), Shader.BLUE_TRANSPARENT));
		testScene.addShape(new Triangle(new Point3D(x, -y, z + 1), new Point3D(-x, -y, z + 1), new Point3D(x, y, z + 1), Shader.BLUE_TRANSPARENT));
	}
	
	public void boxedInScene(int frame){
		System.out.print(frame);
		double radius = 2.5;
		double xPos = radius * Math.cos(5 * Math.toRadians(frame));
		double zPos = radius * Math.sin(5 * Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 1920;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 60));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,3,0);		
		Color ambientColor = new Color(.5,.5,.5);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.addLight(new PointLight(new Point3D(0, 1, 0), cl));
		testScene.setAmbient(new AmbientLight(ambientColor));

		double x = 1;
		double y = 0.6;
		double z = -0.6;
		double rad = 0.075 * 3;
		

		testScene.addShape(new Sphere(new Point3D(.5, 0, 0), rad, new Shader(Shader.REFLECTIVE, new Color(0.75,0.75,0.75))));
		testScene.addShape(new Sphere(new Point3D(-.5, 0, 0), rad, Shader.BLUE_DIFFUSE));
//		testScene.addShape(new Sphere(new Point3D(0, 0, 0), rad, Shape.DIFFUSE, new Color(0,1,1), new Color(.5,1,.5), 32));
		//Back wall
		testScene.addShape(new Triangle(new Point3D(x, y, z), new Point3D(-x, -y, z), new Point3D(-x, y, z), Shader.BLUE_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(x, -y, z), new Point3D(-x, -y, z), new Point3D(x, y, z), Shader.BLUE_DIFFUSE));
//		//Top wall
//		testScene.addShape(new Triangle(new Point3D(x, y, z), new Point3D(-x, y, -z), new Point3D(-x, y, z), Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 15));
//		testScene.addShape(new Triangle(new Point3D(x, y, -z), new Point3D(-x, y, -z), new Point3D(x, y, z), Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 15));
		//Bottom wall
		testScene.addShape(new Triangle(new Point3D(-x, -y, z), new Point3D(-x, -y, -z), new Point3D(x, -y, z), Shader.RED_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(x, -y, z), new Point3D(-x, -y, -z), new Point3D(x, -y, -z), Shader.RED_DIFFUSE));
		//Right wall
		testScene.addShape(new Triangle(new Point3D(x, -y, z), new Point3D(x, -y, -z), new Point3D(x, y, z), Shader.YELLOW_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(x, y, z), new Point3D(x, -y, -z), new Point3D(x, y, -z), Shader.YELLOW_DIFFUSE));
		//Left wall
		testScene.addShape(new Triangle(new Point3D(-x, y, z), new Point3D(-x, -y, -z), new Point3D(-x, -y, z), Shader.CYAN_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(-x, y, -z), new Point3D(-x, -y, -z), new Point3D(-x, y, z), Shader.CYAN_DIFFUSE));
		//Refraction plane
		testScene.addShape(new Triangle(new Point3D(0, y, z), new Point3D(0, -y, -z), new Point3D(0, -y, z), Shader.BLUE_TRANSPARENT));
		testScene.addShape(new Triangle(new Point3D(0, y, -z), new Point3D(0, -y, -z), new Point3D(0, y, z), Shader.BLUE_TRANSPARENT));
	}
	
	public void boxScene(int frame){
		System.out.print(frame);
		double radius = 2.5;
		double xPos = radius * Math.cos(5 * Math.toRadians(frame));
		double zPos = radius * Math.sin(5 * Math.toRadians(frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 1920;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 60));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,3,0);		
		Color ambientColor = new Color(.5,.5,.5);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.addLight(new PointLight(new Point3D(0, 1, 0), cl));
		testScene.setAmbient(new AmbientLight(ambientColor));

		double rad = 0.075 * 3;
		
		testScene.addShape(new Sphere(new Point3D(.5, 0, 0), rad, new Shader(Shader.REFLECTIVE, new Color(0.75,0.75,0.75))).getBoudingBox());
		testScene.addShape(new Sphere(new Point3D(-.5, 0, 0), rad, Shader.BLUE_DIFFUSE).getBoudingBox());
	}
	
	public void spheres(int frame){
		System.out.print(frame);
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(5 * frame));
		double zPos = radius * Math.sin(Math.toRadians(5 * frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 300;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 60));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		double rad = 0.075;
		double dia = rad * 2;
		
		//Spheres on the rectangle and set back a little bit
		testScene.addShape(new Sphere(new Point3D(0, 0, 0), rad, Shader.BLUE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(dia, 0, 0), rad, Shader.GREEN_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-dia, 0, 0), rad, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, dia, 0), rad, Shader.CYAN_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, -dia, 0), rad, Shader.MAGENTA_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, 0, dia), rad, Shader.YELLOW_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, 0, -dia), rad, Shader.WHITE_DIFFUSE));
	}
	
	public void pointLights(int frame){
		System.out.print(frame);
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(5 * frame));
		double zPos = radius * Math.sin(Math.toRadians(5 * frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 1920;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 60));
		
		Color cl = new Color(1,1,1);
		Color ambientColor = new Color(0.1,0.1,0.1);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new PointLight(new Point3D(0,0, 2), cl));
		testScene.addLight(new PointLight(new Point3D(0,0,-2), cl));
		testScene.setAmbient(new AmbientLight(ambientColor));

		double rad = 0.075;
		double dia = rad * 2;
		
		//Spheres on the rectangle and set back a little bit
//		testScene.addShape(new Sphere(new Point3D(0, 0, 0), rad, Shape.DIFFUSE, new Color(0,0,1), new Color(.5,1,.5), 32));
		testScene.addShape(new Sphere(new Point3D(dia, 0, 0), rad, Shader.GREEN_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-dia, 0, 0), rad, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, dia, 0), rad, Shader.CYAN_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, -dia, 0), rad, Shader.MAGENTA_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, 0, dia), rad, Shader.YELLOW_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, 0, -dia), rad, Shader.WHITE_DIFFUSE));
	}
	
	public void makeTestScene(int frame){
		System.out.print(frame);
		Point3D eye = new Point3D(0, 0, 1.2);
		Point3D lookAt = new Point3D(0,0,0);
		Point3D upVector = new Point3D(0,1,0);
		testScene.addCamera(new Camera(eye, lookAt, upVector, 300, 300, 55));
		
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		
		testScene.setAmbient(new AmbientLight(new Color(0,0,0)));
		testScene.addLight(new DirectionalLight(new Color(1,1,1), new Point3D(0,0,1)));
		
		double radius = .5;
		double radius2 = .05;
		testScene.addShape(new Sphere(new Point3D(0, 0, 0), radius, Shader.CYAN_REFLECTIVE));
		testScene.addShape(new Sphere(new Point3D(0.1, 0.1, 0.5), radius2, Shader.BLUE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-0.1, -0.1, 0.6), radius2, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-.1, .15, .7), .05, Shader.GREEN_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(0, 0.2, 0), new Point3D(0.2, 0.2, 0), new Point3D(-0.2, -0.2, 0), Shader.RED_DIFFUSE));
//		shapes.add(new Triangle(new Point3D(0, 0.5, 0), new Point3D(0.5, 0, 0), new Point3D(-0.5, 0, 0), Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 4));
//		shapes.add(new Sphere(new Point3D(0, 0.5, 0), .05, Shape.DIFFUSE, new Color(1,0,0), new Color(1,1,1), 32));
//		shapes.add(new Sphere(new Point3D(0.5, 0, 0), .05, Shape.DIFFUSE, new Color(0,1,0), new Color(1,1,1), 32));
//		shapes.add(new Sphere(new Point3D(-0.5, 0, 0), .05, Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
	}
	
	private void triangleSpheresInLine(int frame){
		System.out.print(frame);
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(5 * frame));
		double zPos = radius * Math.sin(Math.toRadians(5 * frame));
		
		Point3D lookAt = new Point3D(0,0,0);
		Point3D eye = new Point3D(xPos, 0, zPos);
		Point3D upVector = new Point3D(0,1,0);
		int width = 300;
		testScene.addCamera(new Camera(eye, lookAt, upVector, width, (int)(width*(9.0/16.0)), 54));
		
		Color cl = new Color(1,1,1);
		Point3D lightDir = new Point3D(1,0,0);		
		Color ambientColor = new Color(0.1,0.1,0.1);
		testScene.setBackgroundColor(new Color(0.2, 0.2, 0.2));
		testScene.addLight(new DirectionalLight(cl, lightDir));
		testScene.setAmbient(new AmbientLight(ambientColor));

		testScene.addShape(new Sphere(new Point3D(0.35, 0, -0.1), 0.05, Shader.WHITE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0.2, 0, -0.1), 0.075, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shader.GREEN_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shader.BLUE_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shader.YELLOW_DIFFUSE));
	}
	
	private void justTrianglesInLine(int frame){
		System.out.print(frame);
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(5 * frame));
		double zPos = radius * Math.sin(Math.toRadians(5 * frame));
		
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

		testScene.addShape(new Sphere(new Point3D(0.35, 0, -0.1), 0.05, Shader.WHITE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0.2, 0, -0.1), 0.075, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shader.GREEN_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shader.BLUE_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shader.YELLOW_DIFFUSE));
	}
	
	private void tetrahedralSpheres(int frame){
		System.out.print(frame);
		double radius = 2;
		double xPos = radius * Math.cos(Math.toRadians(5 * frame));
		double zPos = radius * Math.sin(Math.toRadians(5 * frame));
		
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
		
		testScene.addShape(new Sphere(new Point3D(0, 0, 0), largeRadius, Shader.WHITE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0, legLen, 0), smallRadius, Shader.MAGENTA_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(xOffset, yOffset, 0), smallRadius, Shader.YELLOW_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-xOffsetBot, yOffset, zOffsetBot), smallRadius, Shader.CYAN_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-xOffsetBot, yOffset, -zOffsetBot), smallRadius, Shader.RED_DIFFUSE));
//		shapes.add(new Sphere(new Point3D(0, 0, -0.1), 0.075, Shape.DIFFUSE, new Color(1,0,0), new Color(.5,1,.5), 32));
//		shapes.add(new Sphere(new Point3D(-0.6, 0, 0), 0.3, Shape.DIFFUSE, new Color(0,1,0), new Color(.5,1,.5), 32));
//		shapes.add(new Triangle(new Point3D(0.3, -0.3, -0.4), new Point3D(0, 0.3, -0.1), new Point3D(-0.3, -0.3, 0.2), Shape.DIFFUSE, new Color(0,0,1), new Color(1,1,1), 32));
//		shapes.add(new Triangle(new Point3D(-0.2, 0.1, 0.1), new Point3D(-0.2, -0.5, 0.2), new Point3D(-0.2, 0.1, -0.3), Shape.DIFFUSE, new Color(1,1,0), new Color(1,1,1), 4));
	}
	
	private void trianglesInReflection(int frame){
		System.out.print(frame);
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

		testScene.addShape(new Sphere(new Point3D(0, 0.3, 0), 0.2, Shader.WHITE_REFLECTIVE));
		//shapes.add(new Sphere(new Point3D(0, 0.3, 0), 0.2, Shape.DIFFUSE, new Color(0.75,0.75,0.75), new Color(1,1,1), 4));
		testScene.addShape(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(1, 0.5, 0), new Point3D(0, -0.5, -0.5), Shader.BLUE_DIFFUSE));
		testScene.addShape(new Triangle(new Point3D(0, -0.5, 0.5), new Point3D(0, -0.5, -0.5), new Point3D(-1, 0.5, 0), Shader.YELLOW_DIFFUSE));
	}
	
	private void spheresInRefraction(int frame){
		System.out.print(frame);
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
		
		testScene.addShape(new Sphere(new Point3D(0,0,0), 0.2322, Shader.WHITE_TRANSPARENT));
		testScene.addShape(new Sphere(new Point3D(0.25, 0.25, -0.6), 0.1161, Shader.YELLOW_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-0.25, 0.25, -0.6), 0.1161, Shader.RED_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(-0.25, -0.25, -0.6), 0.1161, Shader.BLUE_DIFFUSE));
		testScene.addShape(new Sphere(new Point3D(0.25, -0.25, -0.6), 0.1161, Shader.GREEN_DIFFUSE));
	}

	public IScene getTestScene(int scene, int frame) {
		setUpScene(scene, frame);
		return testScene;
	}
}
