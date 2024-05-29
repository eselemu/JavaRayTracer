package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

public class PointLight extends Light{
    private int fallOff;
    public PointLight(Vector3D position, Color color, double intensity) {
        super(position, color, intensity);
        this.fallOff = 2;
    }


    public int getFallOff() {
        return fallOff;
    }

    public void setFallOff(int fallOff) {
        this.fallOff = fallOff;
    }

    @Override
    public double getNDotL(Intersection intersection) {
        Vector3D lightDirection = Vector3D.normalize(Vector3D.substract(intersection.getPosition(), getPosition()));
        double lambertSurface = Math.max(Vector3D.dotProduct(intersection.getNormal(), Vector3D.scalarMultiplication(lightDirection, -1.0)), 0.0);
        return (lambertSurface / Math.pow(Math.abs(Vector3D.distance(intersection.getPosition(), getPosition())), getFallOff()));
    }
}
