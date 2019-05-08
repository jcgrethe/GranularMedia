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
    private final double minRadio = 0.02;
    private final double maxRadio = 0.03;
    private final double Kn = Math.pow(10, 5);
    private final double Kt = 2d*Kn;
    private final double y = 70d;
    private final double mass = 0.01;
    private double endTime = 5.0;

    private Grid grid;
    private HashSet<Pair<Integer, Integer>> usedCells;

    private double totalTries = 1E6;    //TODO: Not too much?
    private double tries = 0;

    public Input(){

    }

    /**
     * Empty constructor generates random inputs based in the max and min setted for each variable.
     */
    public Input(Long quantity, double dt){
        System.out.print("[Generating Input... ");
        this.particles = new ArrayList<>();
//        this.grid = new Grid(10d, L); TODO INITIALIZE GRID!!! Need to be rectangular?

        //Maximum particle quantity
        while(particles.size() < particlesQuantity && tries < totalTries) {
            Particle potential = new Particle(
                    Math.random()*(this.minRadio - this.maxRadio) + this.minRadio,
                    this.mass,
                    Math.random()*this.W, Math.random()*this.L + 1,
                    0,0 //TODO: Initial Velocity and Acceleration?
            );
            if (noOverlapParticle(potential.getX(), potential.getY())){
                particles.add(potential);
            }else{
                tries++;
            }
        }
        grid.setParticles(particles);
        System.out.println("Done.]");
    }

    private boolean noOverlapParticle(Double x, Double y){
        if (particles.size() == 0) return true;
        for (Particle particle : particles){
                if ( (Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2)) <= Math.pow(particle.getRadius()*2, 2)){
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

    public Grid getGrid() {
        return grid;
    }

    public HashSet<Pair<Integer, Integer>> getUsedCells() {
        return usedCells;
    }

    public static double getGravity() {
        return gravity;
    }
}
