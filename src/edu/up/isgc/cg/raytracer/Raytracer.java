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
        System.out.println(new Date());
        Scene scene01 = new Scene();
        scene01.setCamera(new Camera(new Vector3D(0, 0, -4), 60, 60,
                800, 800, 0.1, 50.0));
        //scene01.addLight(new DirectionalLight(new Vector3D(0.0, -0.3, 1.0), Color.WHITE, 1));
        //scene01.addLight(new PointLight(new Vector3D(0, -1, 0), Color.WHITE, 25));
        scene01.addLight(new SpotLight(new Vector3D(0, 0, 3), new Vector3D(0, -1, 0), Color.WHITE, 4, 60));
        //scene01.addLight(new AreaLight(new Vector3D(0, 0, 3), new Vector3D(2, 0, 0), new Vector3D(0, 0, 2), new Vector3D(0, -1, 0), Color.WHITE, 15, 64));
        //scene01.addLight(new PointLight(new Vector3D(0, 0, 0), Color.WHITE, 1));
        //scene01.addObject(new Sphere(new Vector3D(0.5, 1, 8), 0.8, Color.RED));
        //cene01.addObject(new Sphere(new Vector3D(0, -2.5, 3), 0.5, Color.CYAN, 0, 0.8, 0, 0));
        //scene01.addObject(new Sphere(new Vector3D(0, 2.5, 3), 0.5, Color.GREEN, 0, 0, 0, 0));
        scene01.addObject(new Model3D(new Vector3D(0, -2.6, 0),
                new Triangle[]{
                        new Triangle(new Vector3D(-6, 0, 0), new Vector3D(6, 0, 0), new Vector3D(-6, 0, 12)),
                        new Triangle(new Vector3D(-6, 0, 12), new Vector3D(6, 0, 0), new Vector3D(6, 0, 12))},
                Color.YELLOW, 0, 0, 0, 0));
        Model3D teapot = OBJReader.getModel3D("SmallTeapot.obj", new Vector3D(0, -2.5, 3), Color.CYAN, 0.8, 0.8, 0, 0);
        scene01.addObject(teapot);

        //scene01.addObject(OBJReader.getModel3D("Cube.obj", new Vector3D(2, -2, 3), Color.RED, 75, 0.8, 1.5, 0));
        //BufferedImage image = raytrace(scene01);
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
            if(!executorService.awaitTermination(10, TimeUnit.MINUTES)){
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
                    Vector3D lightDir = Vector3D.normalize(Vector3D.substract(areaLight.getPosition(), closestIntersection.getPosition()));
                    double ndotl = Math.max(Vector3D.dotProduct(areaLight.getDirection(), lightDir), 0.0);
                    if(true){
                        for(PointLight pointLight : areaLight.getSamplePoints()){
                            pixelColor = ColorTools.addColor(pixelColor, getCombinedLightedColor(closestIntersection, pointLight, objects, clippingPlanes, objColor, viewerPosition));
                        }
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
        if(obstaculeIntersection == null)
            return false;
        if(obstaculeIntersection.getObject().getRefractionIndex() > 0)
            return isShadowed(obstaculeIntersection, destinyLight, objects, clippingPlanes);
        else
            return true;
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

    public static Color getReflectedColor(Ray ray, Intersection closestIntersection, List<Object3D> objects, List<Light> lights, double[] clippingPlanes, int depth){
        Vector3D reflectedDirection = Vector3D.normalize(Vector3D.substract(ray.getDirection(), Vector3D.scalarMultiplication(closestIntersection.getNormal(), 2 * Vector3D.dotProduct(ray.getDirection(), closestIntersection.getNormal()))));
        Vector3D offset = Vector3D.scalarMultiplication(closestIntersection.getNormal(), EPSILON);
        Vector3D reflectedOrigin = Vector3D.add(closestIntersection.getPosition(), offset);
        Ray reflectedRay = new Ray(reflectedOrigin, reflectedDirection);
        Color reflectedColor = rayTraceColor(reflectedRay, closestIntersection.getPosition(), objects, lights, clippingPlanes, depth + 1, closestIntersection.getObject());
        return ColorTools.scaleColor(reflectedColor, 0.9);
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
        Color refractedColor = rayTraceColor(refractedRay, refractedOrigin, objects, lights, clippingPlanes, depth, intersection.getObject());
        double R0 = Math.pow((n1 - n2) / (n1 + n2), 2);
        double Rtheta = R0 + (1 - R0) * Math.pow(1 - c1, 5);
        refractedColor= ColorTools.addWeightedColor(reflectedColor, refractedColor, Rtheta);
        if(entry) {
            Intersection exitIntersection = raycast(refractedRay, objects, intersection.getObject(), clippingPlanes);
            if (exitIntersection != null) {
                // Calculate the distance the light travels inside the object
                double distance = Vector3D.distance(intersection.getPosition(), exitIntersection.getPosition());
                // Get the color of the refracting object
                Color complementaryColor = ColorTools.getComplementaryColor(intersection.getObject().getColor());
                // Apply Beer's Law to the refracted color using the object's color, distance, and absorption factor
                refractedColor = applyBeersLaw(refractedColor, complementaryColor, distance, intersection.getObject().getAbsortionFactor());
            }
        }

        return refractedColor;
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
