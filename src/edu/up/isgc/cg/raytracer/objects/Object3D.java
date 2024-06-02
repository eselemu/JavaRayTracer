package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.awt.*;

public abstract class Object3D implements IIntersectable{
    private Color color;
    private Vector3D position;
    private double shininess;
    private double reflectiveness;
    private double refractionIndex;
    private double absortionFactor;
    public static int shininessCap = 100;

    public Object3D(Vector3D position, Color color) {
        setPosition(position);
        setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public double getShininess() {
        return shininess;
    }

    public void setShininess(double shininess) {
        this.shininess = Math.max((Object3D.shininessCap - (shininess * Object3D.shininessCap)), 0);
    }

    public double getReflectiveness() {
        return reflectiveness;
    }

    public void setReflectiveness(double reflectiveness) {
        this.reflectiveness = reflectiveness;
    }

    public double getRefractionIndex() {
        return refractionIndex;
    }

    public void setRefractionIndex(double refractionIndex) {
        this.refractionIndex = refractionIndex;
    }

    public double getAbsortionFactor() {
        return absortionFactor;
    }

    public void setAbsortionFactor(double absortionFactor) {
        this.absortionFactor = absortionFactor;
    }
}
