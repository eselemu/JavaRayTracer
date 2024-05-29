package edu.up.isgc.cg.raytracer.objects;

import edu.up.isgc.cg.raytracer.Intersection;
import edu.up.isgc.cg.raytracer.Ray;
import edu.up.isgc.cg.raytracer.Vector3D;
import edu.up.isgc.cg.raytracer.tools.Barycentric;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Model3D extends Object3D{
    private List<Triangle> triangles;
    private List<Triangle> originalTriangles;
    public Model3D(Vector3D position, Triangle[] triangles, Color color, double shininess, double reflectiveness, double refractionIndex, double absortionFactor) {
        super(position, color);
        setOriginalTriangles(triangles);
        setTriangles(triangles);
        setShininess(shininess);
        setReflectiveness(reflectiveness);
        setRefractionIndex(refractionIndex);
        setAbsortionFactor(absortionFactor);
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(Triangle[] triangles) {
        Vector3D position = getPosition();
        Set<Vector3D> uniqueVertices = new HashSet<>();
        for(Triangle triangle : triangles){
            uniqueVertices.addAll(Arrays.asList(triangle.getVertices()));
        }

        for(Vector3D vertex : uniqueVertices){
            vertex.setX(vertex.getX() + position.getX());
            vertex.setY(vertex.getY() + position.getY());
            vertex.setZ(vertex.getZ() + position.getZ());
        }
        this.triangles = Arrays.asList(triangles);
    }

    public void setOriginalTriangles(Triangle[] triangles){
        originalTriangles = new ArrayList<>();
        for(Triangle triangle : triangles){
            originalTriangles.add(triangle.clone());
        }
    }


    @Override
    public Intersection getIntersection(Ray ray) {
        double distance = -1;
        Vector3D position = Vector3D.ZERO();
        Vector3D normal = Vector3D.ZERO();

        for (Triangle triangle : getTriangles()) {
            Intersection intersection = triangle.getIntersection(ray);
            if (intersection != null) {
                double intersectionDistance = intersection.getDistance();
                if (intersectionDistance > 0 && (intersectionDistance < distance || distance < 0)) {
                    distance = intersectionDistance;
                    position = intersection.getPosition();
                    normal = Vector3D.ZERO();
                    double[] uVw = Barycentric.CalculateBarycentricCoordinates(position, triangle);
                    Vector3D[] normals = triangle.getNormals();
                    for (int i = 0; i < uVw.length; i++) {
                        normal = Vector3D.add(normal, Vector3D.scalarMultiplication(normals[i], uVw[i]));
                    }
                    normal = Vector3D.normalize(normal); // Normalize the resulting normal
                }
            }
        }

        if (distance == -1) {
            return null;
        }

        return new Intersection(position, distance, normal, this, this.getColor());
    }

    /*public void scale(double scaleFactor) {
        Vector3D position = getPosition();

        for (int i = 0; i < originalTriangles.size(); i++) {
            Triangle originalTriangle = originalTriangles.get(i).clone();
            Vector3D[] transformedVertices = new Vector3D[]{Vector3D.ZERO(), Vector3D.ZERO(), Vector3D.ZERO()};
            for (int j = 0; j < 3; j++) {

                transformedVertices[j] = Vector3D.scalarMultiplication(originalTriangle.getVertices()[j], scaleFactor);

                transformedVertices[j].setX(transformedVertices[j].getX() + position.getX());
                transformedVertices[j].setY(transformedVertices[j].getY() + position.getY());
                transformedVertices[j].setZ(transformedVertices[j].getZ() + position.getZ());
            }

            triangles.set(i, new Triangle(transformedVertices, originalTriangle.getNormals()));
        }
    }
    public void rotate(double angleX, double angleY, double angleZ) {
        Vector3D position = getPosition();
        angleX = Math.toRadians(angleX);
        angleY = Math.toRadians(angleY);
        angleZ = Math.toRadians(angleZ);

        // Rotation matrices
        double[][] rotateMatrixX = {
                {1, 0, 0},
                {0, Math.cos(angleX), -Math.sin(angleX)},
                {0, Math.sin(angleX), Math.cos(angleX)}
        };

        double[][] rotateMatrixY = {
                {Math.cos(angleY), 0, Math.sin(angleY)},
                {0, 1, 0},
                {-Math.sin(angleY), 0, Math.cos(angleY)}
        };

        double[][] rotateMatrixZ = {
                {Math.cos(angleZ), -Math.sin(angleZ), 0},
                {Math.sin(angleZ), Math.cos(angleZ), 0},
                {0, 0, 1}
        };

        for (int i = 0; i < originalTriangles.size(); i++) {
            //System.out.println(originalTriangles.get(i) + " " + triangles.get(i));
            Triangle originalTriangle = originalTriangles.get(i).clone();
            Vector3D[] transformedVertices = new Vector3D[]{originalTriangle.getVertices()[0], originalTriangle.getVertices()[1], originalTriangle.getVertices()[2]};
            for (int j = 0; j < 3; j++) {

                /*transformedVertices[j].setX(transformedVertices[j].getX() - position.getX());
                transformedVertices[j].setY(transformedVertices[j].getY() - position.getY());
                transformedVertices[j].setZ(transformedVertices[j].getZ() - position.getZ());

                transformedVertices[j].multiplyByMatrix(rotateMatrixX);

                transformedVertices[j].multiplyByMatrix(rotateMatrixY);

                transformedVertices[j].multiplyByMatrix(rotateMatrixZ);

                transformedVertices[j].setX(transformedVertices[j].getX() + position.getX());
                transformedVertices[j].setY(transformedVertices[j].getY() + position.getY());
                transformedVertices[j].setZ(transformedVertices[j].getZ() + position.getZ());
            }

            triangles.set(i, new Triangle(transformedVertices, originalTriangle.getNormals()));
        }
    }*/

    public void transform(double scale, double angleX, double angleY, double angleZ) {
        Vector3D position = getPosition();
        angleX = Math.toRadians(angleX);
        angleY = Math.toRadians(angleY);
        angleZ = Math.toRadians(angleZ);

        // Rotation matrices
        double[][] rotateMatrixX = {
                {1, 0, 0},
                {0, Math.cos(angleX), -Math.sin(angleX)},
                {0, Math.sin(angleX), Math.cos(angleX)}
        };

        double[][] rotateMatrixY = {
                {Math.cos(angleY), 0, Math.sin(angleY)},
                {0, 1, 0},
                {-Math.sin(angleY), 0, Math.cos(angleY)}
        };

        double[][] rotateMatrixZ = {
                {Math.cos(angleZ), -Math.sin(angleZ), 0},
                {Math.sin(angleZ), Math.cos(angleZ), 0},
                {0, 0, 1}
        };

        for (int i = 0; i < originalTriangles.size(); i++) {
            Triangle originalTriangle = originalTriangles.get(i).clone();
            //Vector3D[] transformedVertices = new Vector3D[]{originalTriangle.getVertices()[0],originalTriangle.getVertices()[1],originalTriangle.getVertices()[2]};
            Vector3D[] transformedVertices = new Vector3D[]{Vector3D.ZERO(), Vector3D.ZERO(),Vector3D.ZERO()};
            Vector3D[] transformedNormals = new Vector3D[]{Vector3D.ZERO(), Vector3D.ZERO(),Vector3D.ZERO()};

            for (int j = 0; j < 3; j++) {
                /*transformedVertices[j].setX(transformedVertices[j].getX() - position.getX());
                transformedVertices[j].setY(transformedVertices[j].getY() - position.getY());
                transformedVertices[j].setZ(transformedVertices[j].getZ() - position.getZ());*/

                transformedVertices[j] = Vector3D.scalarMultiplication(originalTriangle.getVertices()[j], scale);
                transformedNormals[j] = originalTriangle.getNormals()[j].clone();

                transformedVertices[j].multiplyByMatrix(rotateMatrixX);
                transformedNormals[j].multiplyByMatrix(rotateMatrixX);

                transformedVertices[j].multiplyByMatrix(rotateMatrixY);
                transformedNormals[j].multiplyByMatrix(rotateMatrixY);

                transformedVertices[j].multiplyByMatrix(rotateMatrixZ);
                transformedNormals[j].multiplyByMatrix(rotateMatrixZ);

                transformedVertices[j].setX(transformedVertices[j].getX() + position.getX());
                transformedVertices[j].setY(transformedVertices[j].getY() + position.getY());
                transformedVertices[j].setZ(transformedVertices[j].getZ() + position.getZ());

                transformedNormals[j] = Vector3D.normalize(transformedNormals[j]);
            }

            triangles.set(i, new Triangle(transformedVertices, transformedNormals));
        }
    }


}
