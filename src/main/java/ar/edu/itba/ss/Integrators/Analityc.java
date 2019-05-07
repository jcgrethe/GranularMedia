package ar.edu.itba.ss.Integrators;

import ar.edu.itba.ss.models.ForceFunction;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.State;

import java.util.List;

public class Analityc extends Integrator {

    private Double A;
    private Double K;
    private Double y;

    public Analityc(Double dt, ForceFunction forceFunction, Double A, Double K, Double y) {
        super(dt, forceFunction);
        this.A = A;
        this.K = K;
        this.y = y;
    }

    @Override
    public void moveParticle(Particle particle, Double time, List<Particle> neighbors) {
        Double M = particle.getMass(), t  = time;
        Double nextY = A*Math.exp((-t)*(y/(2*M)))*Math.cos(Math.sqrt((K/M)-((y*y)/(4.0*M*M)))*t);
        State newState = new State(
                time, nextY,
                particle.getvX(),
                particle.getvY()
        );
        particle.setFutureState(newState);
    }
}
