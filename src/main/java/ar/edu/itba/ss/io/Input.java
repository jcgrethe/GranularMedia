package ar.edu.itba.ss.io;
import ar.edu.itba.ss.models.Particle;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Input {


    private Long ParticlesQuantity;
    private List<Particle> particles;
    private static final Double defaultVelocity = 10.0;
    public static Double time = 35.0;
    private static final Double dt = 0.1;
    private static final Double ParticleRadio = 0.0;
    private static final Double ParticleMass = 0.1;
    private int cellSideQuantity;
    private double interactionRadio;

    // Default Parameters
    private Double Rm = 1.0;
    private Double epsilon = 2.0;
    private Double m = 0.1;
    private Double r = 5.0;
    private Integer boxWidth = 400;
    private Integer boxHeight = 200;
    private Integer middleBoxWidth = boxWidth/2;
    private Integer orificeLength = 10;

    // Default Parameters for Oscillation
    private final Double A = 1.0;       // TODO: Put real value
    private final Double K = 10000.0;   // In N/m
    private final Double y = 100.0;    // In kg/s
    private final Double Tf = 5.0;     // In s
    private final Double M = 70.0;     // In Kilogrames
    private final Double initialX = 1.0;          // In m
    private final Double initialV = -A*y/(2*M);   // In m/s
    private final Double initialA = (-K*initialX-y*initialV)/M;   // In m/s
    private static Double endTime = 5.0; // In s


    public Input(){

    }

    /**
     * Empty constructor generates random inputs based in the max and min setted for each variable.
     */
    public Input(Long quantity, double dt){
        System.out.print("[Generating Input... ");
        this.interactionRadio = r;
        this.cellSideQuantity = (int) Math.ceil(boxWidth/interactionRadio);
        this.ParticlesQuantity = quantity;
        this.particles = new ArrayList<>();

        for (int p = 0 ; p < ParticlesQuantity ; p++ ){
            Double x,y,vX,vY;
            do{
                x =  ThreadLocalRandom.current().nextDouble(r, boxHeight-r);
                y =  ThreadLocalRandom.current().nextDouble(r, boxHeight-r);
                double random = 2 * Math.PI * Math.random();
                vX = defaultVelocity*Math.cos(random);
                vY = defaultVelocity*Math.sin(random);
            }while(!noOverlapParticle(x,y));
            this.particles.add(new Particle(
                    ParticleRadio,
                    ParticleMass,
                    x,
                    y,
                    vX,
                    vY,
                    dt
            ));
        }
        System.out.println("Done.]");
    }

    private boolean noOverlapParticle(Double x, Double y){
        if (particles.size() == 0) return true;
        for (Particle particle : particles){
                if ( (Math.pow(particle.getX() - x, 2) + Math.pow(particle.getY() - y, 2)) <= Math.pow(particle.getRadius() + ParticleRadio + r, 2)){
                    return false;
            }
        }
        return true;
    }



    public List<Particle> getParticles() {
        return particles;
    }

    public int getSystemSideLength() {
        return boxWidth>boxHeight?boxWidth:boxHeight;
    }

    public int getCellSideQuantity() {
        return cellSideQuantity;
    }

    public void setCellSideQuantity(int cellSideQuantity) {
        this.cellSideQuantity = cellSideQuantity;
    }

    public double getInteractionRadio() {
        return interactionRadio;
    }

    public Long getParticlesQuantity() {
        return ParticlesQuantity;
    }

    public static Double getDefaultVelocity() {
        return defaultVelocity;
    }

    public static Double getTime() {
        return time;
    }

    public static Double getDt() {
        return dt;
    }

    public static Double getParticleRadio() {
        return ParticleRadio;
    }

    public static Double getParticleMass() {
        return ParticleMass;
    }

    public Double getRm() {
        return Rm;
    }

    public Double getEpsilon() {
        return epsilon;
    }

    public Double getM() {
        return m;
    }

    public Double getMass() {
        return M;
    }

    public Double getR() {
        return r;
    }

    public Integer getBoxWidth() {
        return boxWidth;
    }

    public Integer getBoxHeight() {
        return boxHeight;
    }

    public Integer getMiddleBoxWidth() {
        return middleBoxWidth;
    }

    public Integer getOrificeLength() {
        return orificeLength;
    }

    public Double getA() {
        return A;
    }

    public Double getInitialV() {
        return initialV;
    }

    public static Double getEndTime() {
        return endTime;
    }

    public Double getK() {
        return K;
    }

    public Double getY() {
        return y;
    }

    public Double getTf() {
        return Tf;
    }

    public Double getInitialX() {
        return initialX;
    }
    public Double getInitialA() {
        return initialA;
    }
}
