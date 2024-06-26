package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.lights.*;
import edu.up.isgc.cg.raytracer.objects.*;
import edu.up.isgc.cg.raytracer.tools.ColorTools;
import edu.up.isgc.cg.raytracer.tools.OBJReader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Raytracer {
    public static int REBOUNDCAP = 4;
    public static double EPSILON = 0.0001;
    public static void main(String[] args) {
        Color goldColor = new Color(184, 134, 11);
        Color silverColor = new Color(138, 149, 151);
        Color brownColor = new Color(65, 63, 54);
        Color blueColor = new Color(68, 107, 235);
        Color purpleColor = new Color(137, 68, 235);
        Color backGroundColor = new Color(166, 187, 255);
        backGroundColor = Color.WHITE;
        System.out.println(new Date());
        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 64, 36,
                4096, 2160, 0.1, 50.0));
        //scene01.addLight(new DirectionalLight(new Vector3D(0.0, -0.3, 1.0), Color.WHITE, 1));
        //scene01.addLight(new PointLight(new Vector3D(0, -1, 0), Color.WHITE, 25));
        //scene01.addLight(new SpotLight(new Vector3D(0, 0, 3), new Vector3D(0, -1, 0), Color.WHITE, 4, 60));
        //scene01.addLight(new AreaLight(new Vector3D(0, 0, 3), new Vector3D(2, 0, 0), new Vector3D(0, 0, 2), new Vector3D(0, -1, 0), Color.WHITE, 15, 64));
        scene01.addLight(new PointLight(new Vector3D(0, 0, -2), Color.WHITE, 4));
        scene01.addLight(new PointLight(new Vector3D(0, 1, 7.5), Color.MAGENTA, 6));
        scene01.addLight(new PointLight(new Vector3D(0, 2.3, 1.5), Color.MAGENTA, 1));
        scene01.addLight(new PointLight(new Vector3D(0, -1.5, 1.5), Color.CYAN, 1));
        //scene01.addLight(new PointLight(new Vector3D(-3.5, -2.5, 1.5), Color.CYAN, 0.5));
        //scene01.addLight(new PointLight(new Vector3D(0, 5, 2.7), Color.CYAN, 2));
        //scene01.addLight(new PointLight(new Vector3D(3.5, -2.5, 1.5), Color.CYAN, 0.5));
        //scene01.addLight(new PointLight(new Vector3D(0, -2.5, 3.5), Color.MAGENTA, 2));
        scene01.addObject(new Model3D(new Vector3D(0, -2.6, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-6, 0, 0), new Vector3D(6, 0, 0), new Vector3D(-6, 0, 12)),
                        new Triangle(new Vector3D(-6, 0, 12), new Vector3D(6, 0, 0), new Vector3D(6, 0, 12))},
                backGroundColor, 0, 0, 0, 0));
        scene01.addObject(new Model3D(new Vector3D(0, 9.4, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-6, 0, 0), new Vector3D(-6, 0, 12), new Vector3D(6, 0, 0)),
                        new Triangle(new Vector3D(-6, 0, 12), new Vector3D(6, 0, 12), new Vector3D(6, 0, 0))},
                backGroundColor, 0, 0, 0, 0));
        scene01.addObject(new Model3D(new Vector3D(-6, -2.6, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(0, 0, 0), new Vector3D(0, 0, 12), new Vector3D(0, 12, 0)),
                        new Triangle(new Vector3D(0, 0, 12), new Vector3D(0, 12, 12), new Vector3D(0, 12, 0))},
                backGroundColor, 0, 0, 0, 0));
        scene01.addObject(new Model3D(new Vector3D(6, -2.6, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(0, 0, 0), new Vector3D(0, 12, 0), new Vector3D(0, 0, 12)),
                        new Triangle(new Vector3D(0, 0, 12), new Vector3D(0, 12, 0), new Vector3D(0, 12, 12))},
                backGroundColor, 0, 0, 0, 0));
        scene01.addObject(new Model3D(new Vector3D(0, -2.6, 12),
                new Triangle[]{
                        new Triangle(new Vector3D(-6, 0, 0), new Vector3D(6, 0, 0), new Vector3D(-6, 12, 0)),
                        new Triangle(new Vector3D(-6, 12, 0), new Vector3D(6, 0, 0), new Vector3D(6, 12, 0))},
                backGroundColor, 0, 0, 0, 0));


        //scene01.addObject(new Sphere(new Vector3D(1.2, 0, 1.5), 0.8, silverColor, 0, 0.8, 0, 0));
        Model3D tri01 = OBJReader.getModel3D("objTriangle01.obj", new Vector3D(0, -2, 0), silverColor, 0, 0.8, 1.5, 0);
        tri01.transform(1, 0, 0, 0);
        scene01.addObject(tri01);

        Model3D tri02 = OBJReader.getModel3D("objTriangle02.obj", new Vector3D(1.1, -1.6, 0), silverColor, 0, 0.8, 1.5, 0);
        tri02.transform(1.2, 0, 25, 25);
        scene01.addObject(tri02);

        Model3D tri03 = OBJReader.getModel3D("objTriangle02.obj", new Vector3D(-1, 1.8, 0), silverColor, 0, 0.8, 1.5, 0);
        tri03.transform(1, -15, 0, 215);
        scene01.addObject(tri03);

        Model3D tri04 = OBJReader.getModel3D("objTriangle01.obj", new Vector3D(1.3, 1.6, 0), silverColor, 0, 0.8, 1.5, 0);
        tri04.transform(1, 5, 5, 150);
        scene01.addObject(tri04);

        Model3D tri05 = OBJReader.getModel3D("objTriangle01.obj", new Vector3D(-1.5, 0, 0), silverColor, 0, 0.8, 1.5, 0.1);
        tri05.transform(0.5, 60, 5, 0);
        scene01.addObject(tri05);

        Model3D tri06 = OBJReader.getModel3D("objTriangle02.obj", new Vector3D(-1.6, 1.35, 0), silverColor, 0, 0.8, 1.5, 0.1);
        tri06.transform(0.7, 115, 5, 3);
        scene01.addObject(tri06);

        Model3D tri07 = OBJReader.getModel3D("objTriangle02.obj", new Vector3D(1.7, -0.7, 0), silverColor, 0, 0.8, 1.5, 0.1);
        tri07.transform(0.8, 5, 60, 55);
        scene01.addObject(tri07);

        Model3D tri08 = OBJReader.getModel3D("objTriangle02.obj", new Vector3D(2.5, 1, 0), silverColor, 0, 0.8, 1.5, 0.1);
        tri08.transform(1.5, -5, -7, -25);
        scene01.addObject(tri08);

        Model3D tri09 = OBJReader.getModel3D("objTriangle02.obj", new Vector3D(-2.7, -1, 0), silverColor, 0, 0.8, 1.5, 0.1);
        tri09.transform(1.5, -5, -7, 165);
        scene01.addObject(tri09);

        Model3D prism01 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(-1, -0.5, 0), silverColor, 0, 0.8, 1.5, 0);
        prism01.transform(1, 15, 0, 215);
        scene01.addObject(prism01);

        Model3D prism02 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(-0.1, 0.1, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism02.transform(0.4, 15, 0, 0);
        scene01.addObject(prism02);

        Model3D prism03 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(-0.9, 0.5, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism03.transform(0.35, 95, 5, 5);
        scene01.addObject(prism03);

        Model3D prism04 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(0.12, 1.25, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism04.transform(0.5, 50, 5, -15);
        scene01.addObject(prism04);

        Model3D prism05 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(-0.6, -1.25, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism05.transform(0.3, 145, 5, 22);
        scene01.addObject(prism05);

        Model3D prism06 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(1.5, 0.6, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism06.transform(0.6, 45, 45, 13);
        scene01.addObject(prism06);

        Model3D prism07 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(-3.2, 0.8, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism07.transform(1.5, 7, 5, 15);
        scene01.addObject(prism07);

        Model3D prism08 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(3.2, -0.8, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism08.transform(1.5, -7, -5, 15);
        scene01.addObject(prism08);

        Model3D prism09 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(-2.5, -0.4, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism09.transform(0.5, -7, -5, 158);
        scene01.addObject(prism09);

        Model3D prism10 = OBJReader.getModel3D("objPrism01.obj", new Vector3D(2.5, 0.1, 0), silverColor, 0, 0.8, 1.5, 0.1);
        prism10.transform(0.5, -13, 13, 89);
        scene01.addObject(prism10);

        Model3D skull = OBJReader.getModel3D("objSkull02.obj", new Vector3D(0, 0, 5), silverColor, 0.7, 0, 0, 0);
        skull.transform(1.2, 0, 0, 0);
        scene01.addObject(skull);

        BufferedImage image = parallelRayTrace(scene01, 4);
        File outputImage = new File("image.png");
        try {
            ImageIO.write(image, "png", outputImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new Date());
    }

    public static BufferedImage parallelRayTrace(Scene scene, int nThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        Camera mainCamera = scene.getCamera();
        double[] nearFarPlanes = mainCamera.getNearFarPlanes();
        BufferedImage image = new BufferedImage(mainCamera.getResolutionWidth(), mainCamera.getResolutionHeight(), BufferedImage.TYPE_INT_RGB);
        List<Object3D> objects = scene.getObjects();
        List<Light> lights = scene.getLights();
        Vector3D[][] posRaytrace = mainCamera.calculatePositionsToRay();
        Vector3D pos = mainCamera.getPosition();
        double cameraZ = pos.getZ();
        for (int i = 0; i < posRaytrace.length; i++) {
            for (int j = 0; j < posRaytrace[i].length; j++) {
                Runnable runnable = rayTracePixel(i, j, image, posRaytrace, pos, mainCamera, objects, lights, cameraZ, nearFarPlanes);
                executorService.execute(runnable);
            }
        }
        executorService.shutdown();
        try{
            if(!executorService.awaitTermination(8, TimeUnit.HOURS)){
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            if(!executorService.isTerminated()){
                System.err.println("Cancel non-finished");
            }
        }
        executorService.shutdownNow();
        return image;
    }
    public static Runnable rayTracePixel(int i, int j, BufferedImage image, Vector3D[][] posRaytrace, Vector3D pos, Camera mainCamera,
                                         List<Object3D> objects, List<Light> lights, double cameraZ, double[] nearFarPlanes){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double x = posRaytrace[i][j].getX() + pos.getX();
                double y = posRaytrace[i][j].getY() + pos.getY();
                double z = posRaytrace[i][j].getZ() + pos.getZ();
                Ray ray = new Ray(mainCamera.getPosition(), new Vector3D(x, y, z));
                double [] clippingPlanes = new double[]{cameraZ + nearFarPlanes[0], cameraZ + nearFarPlanes[1]};
                Color pixelColor = rayTraceColor(ray, mainCamera.getPosition(), objects, lights, clippingPlanes, 0, null);
                setRGBToImage(i, j, pixelColor, image);
            }
        };
        return runnable;
    }

    public static Color rayTraceColor(Ray ray, Vector3D viewerPosition,
                                      List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth, Object3D caster){
        Intersection closestIntersection = raycast(ray, objects, caster, clippingPlanes);
        Color pixelColor = Color.BLACK;
        if (closestIntersection != null) {
            Color objColor = closestIntersection.getObject().getColor();

            for (Light light : lights) {
                if(light instanceof AreaLight){
                    AreaLight areaLight = (AreaLight) light;
                    for(PointLight pointLight : areaLight.getSamplePoints()){
                        pixelColor = ColorTools.addColor(pixelColor, getCombinedLightedColor(closestIntersection, pointLight, objects, clippingPlanes, objColor, viewerPosition));
                    }
                }
                else{
                    pixelColor = ColorTools.addColor(pixelColor, getCombinedLightedColor(closestIntersection, light, objects, clippingPlanes, objColor, viewerPosition));
                }
            }
            if(closestIntersection.getObject().getReflectiveness() > 0 && depth < REBOUNDCAP){
                Color reflectedColor = getReflectedColor(ray, closestIntersection, objects, lights, clippingPlanes, depth);
                reflectedColor = ColorTools.addWeightedColor(reflectedColor, pixelColor, closestIntersection.getObject().getReflectiveness());
                if(closestIntersection.getObject().getRefractionIndex() == 0){
                    pixelColor =  reflectedColor;
                }
                else{
                    pixelColor = getRefractedColor(ray, closestIntersection, objects, lights, clippingPlanes, depth, reflectedColor);
                }
            }
        }
        return pixelColor;
    }
    public static synchronized void setRGBToImage(int i, int j, Color pixelColor, BufferedImage image){
        image.setRGB(i, j, pixelColor.getRGB());
    }
    public static Intersection raycast(Ray ray, List<Object3D> objects, Object3D caster, double[] clippingPlanes) {
        Intersection closestIntersection = null;

        for (int i = 0; i < objects.size(); i++) {
            Object3D currObj = objects.get(i);
            if (caster == null || !currObj.equals(caster)) {
                Intersection intersection = currObj.getIntersection(ray);
                if (intersection != null) {
                    double distance = intersection.getDistance();
                    double intersectionZ = intersection.getPosition().getZ();

                    if (distance >= 0 &&
                            (closestIntersection == null || distance < closestIntersection.getDistance()) &&
                            (clippingPlanes == null || (intersectionZ >= clippingPlanes[0] && intersectionZ <= clippingPlanes[1]))) {
                        closestIntersection = intersection;
                    }
                }
            }
        }
        return closestIntersection;
    }

    public static boolean isShadowed(Intersection originIntersection, Light destinyLight,  List<Object3D> objects, double[] clippingPlanes){
        Vector3D lightDirection = Vector3D.normalize(Vector3D.substract(destinyLight.getPosition(), originIntersection.getPosition()));
        Ray ray = new Ray(originIntersection.getPosition(), lightDirection);
        Intersection obstaculeIntersection = raycast(ray, objects, originIntersection.getObject(), clippingPlanes);
        double distanceToLight = Vector3D.distance(destinyLight.getPosition(), originIntersection.getPosition());
        if(obstaculeIntersection == null)
            return false;
        else {
            double distanceToObstacle = Vector3D.distance(obstaculeIntersection.getPosition(), originIntersection.getPosition());
            if(distanceToLight < distanceToObstacle)
                return false;
            else if(obstaculeIntersection.getObject().getRefractionIndex() > 0)
                return isShadowed(obstaculeIntersection, destinyLight, objects, clippingPlanes);
            else
                return true;
        }
    }
    public static Color getCombinedLightedColor(Intersection intersection, Light light, List<Object3D> objects, double[] clippingPlanes, Color objColor, Vector3D viewerPosition){
        Color pixelColor = Color.BLACK;
        if(!isShadowed(intersection, light, objects,clippingPlanes)){

            Color diffuse = getDiffuseColor(intersection, light, objColor);

            pixelColor = ColorTools.addColor(pixelColor, diffuse);
            if(intersection.getObject().getShininess() < Object3D.shininessCap){
                Color specular = getSpecularColor(intersection, light, viewerPosition);
                Color ambientColor = getAmbientColor(objColor, 0.1f);
                pixelColor = ColorTools.addColor(pixelColor, specular);
                pixelColor = ColorTools.addColor(pixelColor, ambientColor);
            }
        }
        return pixelColor;
    }
    public static Color getDiffuseColor(Intersection closestIntersection, Light light, Color objColor){
        double nDotL = light.getNDotL(closestIntersection);
        Color lightColor = light.getColor();
        double intensity = light.getIntensity() * nDotL;

        double[] lightColors = new double[]{lightColor.getRed() / 255.0, lightColor.getGreen() / 255.0, lightColor.getBlue() / 255.0};
        double[] objColors = new double[]{objColor.getRed() / 255.0, objColor.getGreen() / 255.0, objColor.getBlue() / 255.0};
        for (int colorIndex = 0; colorIndex < objColors.length; colorIndex++) {
            objColors[colorIndex] *= intensity * lightColors[colorIndex];
        }
        Color diffuseColor = new Color((float) Math.clamp(objColors[0], 0.0, 1.0),
                (float) Math.clamp(objColors[1], 0.0, 1.0),
                (float) Math.clamp(objColors[2], 0.0, 1.0));
        /*if(depth > 0)
            System.out.println("");*/
        return diffuseColor;
    }
    public static Color getSpecularColor(Intersection closestIntersection, Light light, Vector3D viewerPosition){
        Vector3D L = Vector3D.normalize(Vector3D.substract(light.getPosition(), closestIntersection.getPosition()));
        Vector3D V = Vector3D.normalize(Vector3D.substract(viewerPosition, closestIntersection.getPosition()));
        Vector3D H = Vector3D.normalize(Vector3D.add(L, V));

        double NdotH = Math.max(Vector3D.dotProduct(closestIntersection.getNormal(), H), 0);
        double specularIntensity = Math.pow(NdotH, closestIntersection.getObject().getShininess());

        return new Color((float)(light.getColor().getRed() / 255.0 * specularIntensity),
                (float)(light.getColor().getGreen() / 255.0 * specularIntensity),
                (float)(light.getColor().getBlue() / 255.0 * specularIntensity));
    }

    public static Color getAmbientColor(Color objColor, float ambientIntensity){
        return new Color((float)(objColor.getRed() / 255.0 * ambientIntensity),
                (float)(objColor.getGreen() / 255.0 * ambientIntensity),
                (float)(objColor.getBlue() / 255.0 * ambientIntensity));
    }

    public static Color getReflectedColor(Ray ray, Intersection closestIntersection, List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth) {
        Vector3D reflectedDirection = Vector3D.normalize(Vector3D.substract(ray.getDirection(), Vector3D.scalarMultiplication(closestIntersection.getNormal(), 2 * Vector3D.dotProduct(ray.getDirection(), closestIntersection.getNormal()))));
        Vector3D offset = Vector3D.scalarMultiplication(closestIntersection.getNormal(), EPSILON);
        Vector3D reflectedOrigin = Vector3D.add(closestIntersection.getPosition(), offset);
        Ray reflectedRay = new Ray(reflectedOrigin, reflectedDirection);
        Color totalReflectedColor = Color.BLACK;
        double totalWeight = 0.0;

        for (Light light : lights) {
            double ndotl = light.getNDotL(closestIntersection);
            if (!isShadowed(closestIntersection, light, objects, clippingPlanes)) {
                Color reflectedColor = rayTraceColor(reflectedRay, closestIntersection.getPosition(), objects, List.of(light), clippingPlanes, depth + 1, closestIntersection.getObject());
                reflectedColor = ColorTools.scaleColor(reflectedColor, ndotl * 0.9);
                totalReflectedColor = ColorTools.addColor(totalReflectedColor, reflectedColor);
                totalWeight += ndotl;
            }
        }

        if (totalWeight > 0) {
            totalReflectedColor = ColorTools.scaleColor(totalReflectedColor, 1.0 / totalWeight);
        }

        return totalReflectedColor;
    }


    public static Color getRefractedColor(Ray ray, Intersection intersection, List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth, Color reflectedColor) {
        Vector3D offset = Vector3D.scalarMultiplication(intersection.getNormal(), -EPSILON);
        Vector3D refractedOrigin = Vector3D.add(intersection.getPosition(), offset);
        double n1, n2, c1, c2, n;
        boolean entry = true;
        Vector3D normal = intersection.getNormal().clone();
        c1 = Vector3D.dotProduct(ray.getDirection(), normal);
        n1 = 1;
        n2 = intersection.getObject().getRefractionIndex();

        if (c1 < 0) {
            c1 = -c1;
        } else {
            n1 = intersection.getObject().getRefractionIndex();
            n2 = 1;
            normal = Vector3D.scalarMultiplication(normal, -1);
            entry = false;
        }

        n = n1 / n2;
        double k = 1 - n * n * (1 - c1 * c1);

        if (k < 0) {
            return reflectedColor;
        }
        c2 = Math.sqrt(k);
        Vector3D T = Vector3D.normalize(Vector3D.add(Vector3D.scalarMultiplication(ray.getDirection(), n), Vector3D.scalarMultiplication(normal, (n * c1 - c2))));

        Ray refractedRay = new Ray(refractedOrigin, T);
        Color totalRefractedColor = Color.BLACK;
        double totalWeight = 0.0;

        Color refractedColor = rayTraceColor(refractedRay, refractedOrigin, objects, lights, clippingPlanes, depth, intersection.getObject());

        for (Light light : lights) {
            double ndotl = light.getNDotL(intersection);
            if (!isShadowed(intersection, light, objects, clippingPlanes)) {
                double R0 = Math.pow((n1 - n2) / (n1 + n2), 2);
                double Rtheta = R0 + (1 - R0) * Math.pow(1 - c1, 5);
                refractedColor = ColorTools.addWeightedColor(reflectedColor, refractedColor, Rtheta);

                if (entry) {
                    Intersection exitIntersection = raycast(refractedRay, objects, intersection.getObject(), clippingPlanes);
                    if (exitIntersection != null) {
                        double distance = Vector3D.distance(intersection.getPosition(), exitIntersection.getPosition());
                        Color complementaryColor = ColorTools.getComplementaryColor(intersection.getObject().getColor());
                        refractedColor = applyBeersLaw(refractedColor, complementaryColor, distance, intersection.getObject().getAbsortionFactor());
                    }
                }

                refractedColor = ColorTools.scaleColor(refractedColor, ndotl);
                totalRefractedColor = ColorTools.addColor(totalRefractedColor, refractedColor);
                totalWeight += ndotl;
            }
        }

        if (totalWeight > 0) {
            totalRefractedColor = ColorTools.scaleColor(totalRefractedColor, 1.0 / totalWeight);
        }

        return totalRefractedColor;
    }


    public static Color applyBeersLaw(Color refractedColor, Color colorToAbsorb, double distance, double absorptionCoefficient) {
        // Calculate the absorbance for each color channel
        double absorbanceRed = absorptionCoefficient * distance * (colorToAbsorb.getRed() / 255.0);
        double absorbanceGreen = absorptionCoefficient * distance * (colorToAbsorb.getGreen() / 255.0);
        double absorbanceBlue = absorptionCoefficient * distance * (colorToAbsorb.getBlue() / 255.0);

        // Calculate the transparency for each color channel
        double transparencyRed = Math.exp(-absorbanceRed);
        double transparencyGreen = Math.exp(-absorbanceGreen);
        double transparencyBlue = Math.exp(-absorbanceBlue);

        // Apply the transparency to the refracted color
        int newRed = (int) Math.min(255, refractedColor.getRed() * transparencyRed);
        int newGreen = (int) Math.min(255, refractedColor.getGreen() * transparencyGreen);
        int newBlue = (int) Math.min(255, refractedColor.getBlue() * transparencyBlue);

        return new Color(newRed, newGreen, newBlue);
    }
}
