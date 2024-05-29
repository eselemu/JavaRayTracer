package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;

import java.util.concurrent.TransferQueue;

public class Triangle implements IIntersectable {
    public static final double EPSILON = 0.0000000000001;
    private Vector3D[] vertices;
    private Vector3D[] normals;

    public Triangle(Vector3D v0, Vector3D v1, Vector3D v2) {
        setVertices(v0, v1, v2);
        setNormals(null);
    }

    public Triangle(Vector3D[] vertices, Vector3D[] normals) {
        if(vertices.length == 3){
            setVertices(vertices[0], vertices[1], vertices[2]);
        } else {
            setVertices(Vector3D.ZERO(),Vector3D.ZERO(),Vector3D.ZERO());
        }
        setNormals(normals);
    }

    public Vector3D[] getVertices() {
        return vertices;
    }

    private void setVertices(Vector3D[] vertices) {
        this.vertices = vertices;
    }

    public void setVertices(Vector3D v0, Vector3D v1, Vector3D v2) {
        setVertices(new Vector3D[]{v0, v1, v2});
    }

    public Vector3D getNormal(){
        Vector3D normal = Vector3D.ZERO();
        Vector3D[] normals = this.normals;

        if(normals ==null) {
            Vector3D[] vertices = getVertices();
            Vector3D v = Vector3D.substract(vertices[1], vertices[0]);
            Vector3D w = Vector3D.substract(vertices[0], vertices[2]);
            normal = Vector3D.normalize(Vector3D.crossProduct(v, w));
        } else{
            for(int i = 0; i < normals.length; i++){
                normal.setX(normal.getX() + normals[i].getX());
                normal.setY(normal.getY() + normals[i].getY());
                normal.setZ(normal.getZ() + normals[i].getZ());
            }
            normal.setX(normal.getX() / normals.length);
            normal.setY(normal.getY() / normals.length);
            normal.setZ(normal.getZ() / normals.length);
        }
        return normal;
    }

    public Vector3D[] getNormals() {
        if(normals == null) {
            Vector3D normal = getNormal();
            setNormals(new Vector3D[]{normal, normal, normal});
        }
        return normals;
    }

    private void setNormals(Vector3D[] normals) {
        this.normals = normals;
    }

    public void setNormals(Vector3D vn0, Vector3D vn1, Vector3D vn2) {
        setNormals(new Vector3D[]{vn0, vn1, vn2});
    }

    public Triangle clone() {
        Vector3D[] clonedVertices = new Vector3D[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            clonedVertices[i] = vertices[i].clone();
        }
        return new Triangle(
                clonedVertices,
                normals == null ? null : normals.clone()
        );
    }

    @Override
    public Intersection getIntersection(Ray ray) {
        Intersection intersection = new Intersection(null, -1, null, null, null);

        Vector3D[] vert = getVertices();
        Vector3D v2v0 = Vector3D.substract(vert[2], vert[0]);
        Vector3D v1v0 = Vector3D.substract(vert[1], vert[0]);
        Vector3D vectorP = Vector3D.crossProduct(ray.getDirection(), v1v0);
        double det = Vector3D.dotProduct(v2v0, vectorP);

        // Check if the determinant is close to zero
        if (Math.abs(det) < EPSILON) {
            return null; // No intersection, or ray is parallel to the triangle
        }

        double invDet = 1.0 / det;
        Vector3D vectorT = Vector3D.substract(ray.getOrigin(), vert[0]);
        double u = invDet * Vector3D.dotProduct(vectorT, vectorP);

        // Use epsilon for robustness
        if (u < 0.0 - EPSILON || u > 1.0 + EPSILON) {
            return null; // No intersection
        }

        Vector3D vectorQ = Vector3D.crossProduct(vectorT, v2v0);
        double v = invDet * Vector3D.dotProduct(ray.getDirection(), vectorQ);

        // Use epsilon for robustness
        if (v < 0.0 - EPSILON || (u + v) > 1.0 + EPSILON) {
            return null; // No intersection
        }

        double t = invDet * Vector3D.dotProduct(vectorQ, v1v0);

        if (t > EPSILON) { // Ensure the intersection is in front of the ray origin
            intersection.setDistance(t);
            intersection.setPosition(Vector3D.add(ray.getOrigin(), Vector3D.scalarMultiplication(ray.getDirection(), t)));
            intersection.setNormal(Vector3D.normalize(Vector3D.crossProduct(v1v0, v2v0)));
        }

        return intersection;
    }
}
