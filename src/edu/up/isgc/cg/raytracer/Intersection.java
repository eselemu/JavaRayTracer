package edu.up.isgc.cg.raytracer;

import edu.up.isgc.cg.raytracer.objects.Object3D;

import java.awt.*;

public class Intersection {

    private double distance;
    private Vector3D position;
    private Vector3D normal;
    private Object3D object;
    private Color color;

    public Intersection(Vector3D position, double distance, Vector3D normal, Object3D object, Color color) {
        setPosition(position);
        setDistance(distance);
        setNormal(normal);
        setObject(object);
        setColor(color);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Vector3D getPosition() {
        return position;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public void setNormal(Vector3D normal) {
        this.normal = normal;
    }

    public Object3D getObject() {
        return object;
    }

    public void setObject(Object3D object) {
        this.object = object;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
