package ar.edu.itba.ss;

import ar.edu.itba.ss.GranularMedia.GranularMediaForce;
import ar.edu.itba.ss.Integrators.GearPredictor;
import ar.edu.itba.ss.Integrators.Integrator;
import ar.edu.itba.ss.Integrators.NeighborDetection;
import ar.edu.itba.ss.Integrators.VelocityVerlet;
import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.io.Output;
import ar.edu.itba.ss.models.Grid;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;

import java.io.IOException;
import java.util.*;

public class Simulation
{

    static long PARTICLES = 100;



    public static void main( String[] args )
    {

        simulation1();
        // Output
    }

    public static void simulation1(){
        // Initial conditions
        //Double simulationDT = 0.1*Math.sqrt(input.getMass()/input.getKn());   //Default ; TODO: Check if there is a better one
        Input input = new Input(PARTICLES);
        double simulationDT = input.getDt();
        Double printDT = simulationDT *10;
        Integrator integrator = new VelocityVerlet(simulationDT,
                new GranularMediaForce(input.getKn(), input.getKt(), input.getW(), input.getL()),
                input.getW(), input.getL(), input.getD()
        );
        // Can use other integrator.
        Map<Particle, List<Particle>> neighbours;
        List<Particle> particles = input.getParticles();
        Output.generateXYZFile();

        //Simulation
        for (double time = 0d ; time < input.getEndTime() ; time += simulationDT){
            System.out.println(time);
            Grid grid = new Grid(input.getCellSideQuantity(),input.getL());
            grid.setParticles(input.getParticles());
//            integrator.moveParticle();
            neighbours = NeighborDetection.getNeighbours(
                    grid, grid.getUsedCells(),
                    input.getInteractionRadio(), false
            );
            for (Particle particle : particles){
                integrator.moveParticle(
                        particle, simulationDT,
                        neighbours.getOrDefault(particle,new LinkedList<>()),
                        getWallsCollisions(particle, input.getW(), input.getL(), input.getD())
                );
            }
            for (Particle particle : particles){
                particle.updateState();
                if (particle.getY() < -input.getL()/10){
                    //Vertical Contorn Condition
                    particle.reset(input);  //TODO: Same velocity and X position?
                }
            }
            if (time % printDT >= simulationDT){ //TODO CHECK!!
                //Print
                try {
                    Output.printToFile(particles);
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }


    public static List<Wall> getWallsCollisions(Particle p, Double boxWidth, Double boxHeight, Double D){
        List<Wall> walls = new LinkedList<>();
        if (p.getX() < p.getRadius())
            walls.add(new Wall(Wall.typeOfWall.LEFT));
        if (boxWidth - p.getX() < p.getRadius())
            walls.add(new Wall(Wall.typeOfWall.RIGHT));
        if (boxHeight - p.getY() < p.getRadius())
            if(p.getX() < boxWidth / 2 - D / 2  || p.getX() > boxWidth / 2 + D / 2 ) // apertura
                walls.add(new Wall(Wall.typeOfWall.BOTTOM));
        return walls;
    }
}
