package ar.edu.itba.ss.io;
import ar.edu.itba.ss.models.Grid;
import ar.edu.itba.ss.models.Particle;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Input {
    private List<Particle> particles;
    private int particlesQuantity = Integer.MAX_VALUE;  // Will put maximum possible particles
    private double L;
    private double W;
    private double D;
    private static final double gravity = 9.8;
    private final double minL = 1d;
    private final double maxL = 1.5;   
    private final double minW = 0.3;   
    private final double maxW = 0.4;   
    private final double minD = 0.15;   
    private final double maxD = 0.25;   
    private final boolean contornConditions = true; //Defined; Only on after the opening
    private final double minRadio = 0.01;
    private final double maxRadio = 0.015;
    private final double Kn = Math.pow(10, 5);
    private final double Kt = 2d*Kn;
    private final double y = 70d;
    private final double mass = 0.01;
    private double endTime = 5.0;
    private double dt;
    private double cellSideLength;
    private double interactionRadio = 0.0;

    private double totalTries = 1E6;    //TODO: Not too much?
    private double tries = 0;

    public Input(){

    }

    /**
     * Empty constructor generates random inputs based in the max and min setted for each variable.
     */
    public Input(Long quantity){
        System.out.print("[Generating Input... ");
//        dt = 0.1*Math.sqrt(mass/Kn);
        dt = 0.00001;
        this.particles = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // size of the silo
        L = random.nextDouble(minL,maxL);
        W = random.nextDouble(minW,maxW);
        D = random.nextDouble(minD,maxD);
        this.cellSideLength = Math.ceil(L/(maxRadio * 3 ));

        System.out.println("L:" + L + "; W:" + W + "; D:" + D);


        //Maximum particle quantity
        while(particles.size() < quantity && tries < totalTries) {
            Particle potential = new Particle(
                    random.nextDouble(minRadio,maxRadio),
                    mass,
                    random.nextDouble(0 + maxRadio,W - maxRadio),
                    random.nextDouble(0 + maxRadio,L/3- maxRadio), //TODO remove /3 just for testing
                    0.0,
                    0.0,
                    dt
            );
            if (noOverlapParticle(potential.getX(), potential.getY(), potential.getRadius())) {
                particles.add(potential);
            }
            tries++;
        }
        System.out.println("Done.]");
    }

    public boolean noOverlapParticle(Double x, Double y, Double radio){
        if (particles.size() == 0) return true;
        for (Particle particle : particles){
                if ( (Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2)) <= Math.pow(particle.getRadius() + radio, 2)){
                    return false;
            }
        }
        return true;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public double getKn() {
        return Kn;
    }

    public double getKt() {
        return Kt;
    }

    public double getY() {
        return y;
    }

    public double getMass() {
        return mass;
    }

    public double getEndTime() {
        return endTime;
    }

    public double getL() {
        return L;
    }

    public double getW() {
        return W;
    }

    public double getD() {
        return D;
    }

    public static double getGravity() {
        return gravity;
    }

    public double getDt() {
        return dt;
    }

    public double getCellSideLength() {
        return cellSideLength;
    }

    public double getInteractionRadio() {
        return interactionRadio;
    }

    public double getMaxRadio() {
        return maxRadio;
    }
}
