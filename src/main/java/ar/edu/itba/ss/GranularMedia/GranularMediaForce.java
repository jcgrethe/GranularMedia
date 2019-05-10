package ar.edu.itba.ss.GranularMedia;

import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.models.ForceFunction;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector2D;
import ar.edu.itba.ss.models.Wall;

import java.util.List;
import java.util.Optional;

public class GranularMediaForce  implements ForceFunction {
    private double Kn;
    private double Kt;
    private double boxWidth;
    private double boxHeigth;

    public GranularMediaForce(double kn, double kt, double boxWidth, double boxHeigth) {
        Kn = kn;
        Kt = kt;
        this.boxWidth = boxWidth;
        this.boxHeigth = boxHeigth;
    }

    @Override
    public Vector2D getForce(Particle particle, List<Particle> neighbours, List<Wall> walls) {
        Vector2D force = new Vector2D();
        double xDistanceFraction, yDistanceFraction, distance, overlapSize, relativeVelocity;

        // Force from particles
        for (Particle neighbour : neighbours){
            distance = particle.getDistance(neighbour);
            xDistanceFraction = (particle.getX() - neighbour.getX())/distance;
            yDistanceFraction = (particle.getY() - neighbour.getY())/distance;

            Vector2D tan = new Vector2D(-xDistanceFraction, yDistanceFraction); //TODO Check '-'
            overlapSize = overlapSize(particle, neighbour);
            relativeVelocity = getRelativeVelocity(particle, neighbour, tan);

            Vector2D forceNormalAndTan = getNormalAndTangencialVector(overlapSize, relativeVelocity);
            force = force.add(
                    forceNormalAndTan.getX() * xDistanceFraction + forceNormalAndTan.getY() * -yDistanceFraction,
                    forceNormalAndTan.getX() * yDistanceFraction + forceNormalAndTan.getY() * xDistanceFraction
            );
        }

        // Force from walls
        for (Wall wall : walls){
            overlapSize = overlapSize(particle, wall);
            relativeVelocity = getRelativeVelocity(particle, wall);
            Vector2D forceNormalAndTan = getNormalAndTangencialVector(Math.abs(overlapSize), relativeVelocity);
            force = addForceFromWall(force, wall, forceNormalAndTan);
        }

        // Force from gravity
        force = force.add(
                0d,
                -Input.getGravity()
        );

        return force;
    }

    private Vector2D getNormalAndTangencialVector(double overlapSize, double relativeVelocity){
        return new Vector2D(
                -Kn * overlapSize,
                -Kt * overlapSize * relativeVelocity
        );
    }

    private static double overlapSize(Particle one, Particle another) {
        double overlapSize = one.getRadius() + another.getRadius() - one.getDistance(another);
        return (overlapSize < 0)? 0 : overlapSize;
    }

    private double overlapSize(Particle p, Wall wall){
        switch (wall.getTypeOfWall()){
            case TOP:
                return p.getRadius() - Math.abs(p.getY());
            case RIGHT:
                return p.getRadius() - Math.abs(boxWidth - p.getX());
            case BOTTOM:
                return p.getRadius() - Math.abs(boxHeigth - p.getY());
            case LEFT:
                return p.getRadius() - Math.abs(p.getX());
        }
        return 0d;
    }

    private static double getRelativeVelocity(Particle one, Particle another, Vector2D tan) {
        Vector2D v = another.getVelocity().subtract(one.getVelocity());
        return v.getX() * tan.getX() + v.getY() * tan.getY();
    }

    private static double getRelativeVelocity(Particle p, Wall wall) {
        switch (wall.getTypeOfWall()){
            case TOP: // --->
                return p.getVelocity().getX();
            case RIGHT: // v
                return p.getVelocity().getY();
            case BOTTOM: // <---
                return -p.getVelocity().getX();
            case LEFT: // ^
                return -p.getVelocity().getY();
        }
        return 0d;  //Not should happen.
    }

    private Vector2D addForceFromWall(Vector2D force, Wall wall, Vector2D normalAndTan){
        switch (wall.getTypeOfWall()){
            case TOP: // normal [0,1] ; tan [1,0]
                return force.add(
                        normalAndTan.getY(),    // Only tan
                        normalAndTan.getX()     // Only normal
                );
            case RIGHT: // normal [-1,0] ; tan [0,1]
                return force.add(
                    -normalAndTan.getX(),
                    normalAndTan.getY()
                );
            case BOTTOM: // normal [0,-1] ; tan [-1,0]
                return force.add(
                    -normalAndTan.getY(),
                    -normalAndTan.getX()
                );
            case LEFT: // normal [1,0] ; tan [0,-1]
                return force.add(
                    normalAndTan.getX(),
                    -normalAndTan.getY()
                );
        }
        return force;
    }
}
