package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Vector2D;

import java.util.List;

public interface ForceFunction {
    public Vector2D getForce(Vector2D position, Vector2D velocity, List<Particle> neighbors);
}
