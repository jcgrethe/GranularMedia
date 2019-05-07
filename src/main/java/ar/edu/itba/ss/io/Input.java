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
    private int particlesQuantity;
    private double L;
    private double W;
    private double D;
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

    public Input(){

    }

    /**
     * Empty constructor generates random inputs based in the max and min setted for each variable.
     */
    public Input(Long quantity, double dt){
        System.out.print("[Generating Input... ");
//        this.interactionRadio = r;
//        this.cellSideQuantity = (int) Math.ceil(boxWidth/interactionRadio);
//        this.ParticlesQuantity = quantity;
//        this.particles = new ArrayList<>();
//
//        for (int p = 0 ; p < ParticlesQuantity ; p++ ){
//            Double x,y,vX,vY;
//            do{
//                x =  ThreadLocalRandom.current().nextDouble(r, boxHeight-r);
//                y =  ThreadLocalRandom.current().nextDouble(r, boxHeight-r);
//                double random = 2 * Math.PI * Math.random();
//                vX = defaultVelocity*Math.cos(random);
//                vY = defaultVelocity*Math.sin(random);
//            }while(!noOverlapParticle(x,y));
//            this.particles.add(new Particle(
//                    ParticleRadio,
//                    ParticleMass,
//                    x,
//                    y,
//                    vX,
//                    vY,
//                    dt
//            ));
//        }
        System.out.println("Done.]");
    }

    private boolean noOverlapParticle(Double x, Double y){

        // TODO: Check if need to add the current particle radio or some minimum value

        if (particles.size() == 0) return true;
        for (Particle particle : particles){
                if ( (Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2)) <= Math.pow(particle.getRadius(), 2)){
                    return false;
            }
        }
        return true;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public int getParticlesQuantity() {
        return particlesQuantity;
    }

    public double getMinL() {
        return minL;
    }

    public double getMaxL() {
        return maxL;
    }

    public double getMinW() {
        return minW;
    }

    public double getMaxW() {
        return maxW;
    }

    public double getMinD() {
        return minD;
    }

    public double getMaxD() {
        return maxD;
    }

    public boolean isContornConditions() {
        return contornConditions;
    }

    public double getMinRadio() {
        return minRadio;
    }

    public double getMaxRadio() {
        return maxRadio;
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
}
