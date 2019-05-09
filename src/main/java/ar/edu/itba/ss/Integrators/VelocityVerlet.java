package ar.edu.itba.ss.Integrators;

import ar.edu.itba.ss.GranularMedia.GranularMediaForce;
import ar.edu.itba.ss.Simulation;
import ar.edu.itba.ss.models.*;

import java.util.List;

public class VelocityVerlet extends Integrator {

    public VelocityVerlet(Double dt, ForceFunction forceFunction, Double W, Double L, Double D) {
        super(dt, forceFunction, W, L, D);
    }

    @Override
    public void moveParticle(Particle particle, Double time, List<Particle> neighbours, List<Wall> walls) {
        if (forceFunction instanceof GranularMediaForce){
            Vector2D force = forceFunction.getForce(particle, neighbours, walls);
            Vector2D predictedPosition = particle.getPosition().multiply(2d).add(particle.getPreviousPosition().multiply(-1d)).add(force.multiply(dt*dt/particle.getMass()));
            Vector2D predictedVelocity = predictedPosition.add(particle.getPreviousPosition().multiply(-1d)).multiply(1d/(2d*dt));

            particle.setFutureState(new State(
                    predictedPosition.getX(), predictedPosition.getY(),
                    predictedVelocity.getX(), predictedVelocity.getY(),
                    0d,0d
            ));

        }else{
            Vector2D force = forceFunction.getForce(particle, neighbours, walls);
            Double x = particle.getX() + dt*particle.getvX() + dt*dt/particle.getMass()*force.getX();
            Double y = particle.getY() + dt*particle.getvY() + dt*dt/particle.getMass()*force.getY();

            Particle predictedParticle = new Particle(particle.getRadius(), particle.getMass(), x,y, particle.getvX(),particle.getvY());

            //TODO: CHECK IF REALLY NEED TO RECALCULATE WALLS!?
            walls = Simulation.getWallsCollisions(predictedParticle, W, L, D);

            Vector2D predictedForce = forceFunction.getForce(predictedParticle, neighbours, walls);

            Double vX = particle.getvX() + dt*(force.getX() + predictedForce.getX())/(2*particle.getMass());
            Double vY = particle.getvY() + dt*(force.getY() + predictedForce.getY())/(2*particle.getMass());
            particle.setFutureState(new State(
                    x,y,vX,vY
            ));
        }
    }
}
