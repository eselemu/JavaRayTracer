package edu.up.isgc.cg.raytracer.lights;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.lights.PointLight;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AreaLight extends Light{
    private Vector3D width, height;
    private Vector3D direction;
    private int numSamples;

    public AreaLight(Vector3D position, Vector3D width, Vector3D height, Vector3D direction, Color color, double intensity, int numSamples) {
        super(position, color, intensity);
        setWidth(width);
        setHeight(height);
        setDirection(direction);
        setNumSamples(numSamples);
    }


    public Vector3D getWidth() {
        return width;
    }

    public void setWidth(Vector3D width) {
        this.width = width;
    }

    public Vector3D getHeight() {
        return height;
    }

    public void setHeight(Vector3D height) {
        this.height = height;
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public int getNumSamples() {
        return numSamples;
    }

    public void setNumSamples(int numSamples) {
        this.numSamples = numSamples;
    }

    public List<PointLight> getSamplePoints() {
        List<PointLight> samplePoints = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numSamples; i++) {
            double randW = rand.nextDouble();
            double randH = rand.nextDouble();
            Vector3D samplePos = Vector3D.add(getPosition(), Vector3D.add(Vector3D.scalarMultiplication(getWidth(), randW), Vector3D.scalarMultiplication(getWidth(), randH)));
            samplePoints.add(new PointLight(samplePos, getColor(), getIntensity() / numSamples));
        }
        return samplePoints;
    }

    @Override
    public double getNDotL(Intersection intersection) {
        return 0;
    }
}
