package ar.edu.itba.ss.Integrators;

import ar.edu.itba.ss.models.ForceFunction;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.State;
import ar.edu.itba.ss.models.Vector2D;

import java.util.List;

public class Beeman extends Integrator {
    public Beeman(Double dt, ForceFunction forceFunction) {
        super(dt, forceFunction);
    }

    @Override
    public void moveParticle(Particle particle, Double time, List<Particle> neighbours) {
        Vector2D force = forceFunction.getForce(particle.getPosition(), particle.getVelocity(), neighbours);
        Vector2D previousAcceleration;
        if (time == 0){
            previousAcceleration = new Vector2D(
                    force.getX()/particle.getMass(),
                    force.getY()/particle.getMass()
            );
        }else{
            previousAcceleration = new Vector2D(
                    particle.getPreviousAcceleration().getX(),
                    particle.getPreviousAcceleration().getX()
            );
        }
        Vector2D position = particle.getPosition()
                .add(particle.getVelocity().multiply(dt))
                .add(particle.getAcceleration().multiply(2d*dt*dt/3d))
                .add(previousAcceleration.multiply(-dt*dt/6d));
        Vector2D predictedAcceleration = forceFunction.getForce(
                new Vector2D(position.getX(), position.getY()), particle.getVelocity(), neighbours)
                .multiply(1d/particle.getMass());
        Vector2D velocity = particle.getVelocity()
                .add(predictedAcceleration.multiply(dt/3d))
                .add(particle.getAcceleration().multiply(5d*dt/6d))
                .add(previousAcceleration.multiply(-dt/6d));

        particle.setFutureState(new State(
                position.getX(),position.getY(),
                velocity.getX(), velocity.getY(),
                predictedAcceleration.getX(), predictedAcceleration.getY()
        ));
    }
}
