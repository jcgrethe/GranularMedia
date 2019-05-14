package ar.edu.itba.ss;

import ar.edu.itba.ss.GranularMedia.GranularMediaForce;
import ar.edu.itba.ss.Integrators.*;
import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.io.Output;
import ar.edu.itba.ss.models.Grid;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;

import java.io.IOException;
import java.util.*;

public class Simulation
{

    static long PARTICLES = 150;



    public static void main( String[] args )
    {

        simulation1();
        // Output
    }

    public static void simulation1(){
        // Initial conditions
        //Double simulationDT = 0.1*Math.sqrt(input.getMass()/input.getKn());   //Default ; TODO: Check if there is a better one
        Input input = new Input(PARTICLES);
        double simulationDT = 0.00001;
        Integer printDT = 500;
        Integer iteration = 0;
        System.out.println("DT: "+input.getDt() + " | Print DT: " + printDT);
        Integrator integrator = new VelocityVerlet(simulationDT,
                new GranularMediaForce(input.getKn(), input.getKt(), input.getW(), input.getL()),
                input.getW(), input.getL(), input.getD()
        );
        // Can use other integrator.
        Map<Particle, List<Particle>> neighbours = new HashMap<>();
        List<Particle> particles = input.getParticles();
        Output.generateXYZFile();

        //Simulation
        for (double time = 0d ; time < input.getEndTime() ; time += simulationDT, iteration++){
            Grid grid = new Grid(input.getCellSideLength(),input.getW(), input.getL());
            grid.setParticles(input.getParticles());
//            integrator.moveParticle();
            neighbours.clear();
            neighbours.putAll(NeighborDetection.getNeighbours(
                    grid, grid.getUsedCells(),
                    input.getInteractionRadio(), false
            ));
            particles.stream().parallel().forEach( particle -> {
                integrator.moveParticle(
                        particle, simulationDT,
                        neighbours.getOrDefault(particle,new LinkedList<>()),
                        getWallsCollisions(particle, input.getW(), input.getL(), input.getD())
                );
            });
            particles.stream().parallel().forEach(particle -> {
                particle.updateState();
                if (particle.getY() < -input.getL()/10){
                    //Vertical Contorn Condition
                    particle.reset(input);  //TODO: Same velocity and X position?
                }
            });
            if (iteration % printDT == 0){ //TODO CHECK!!
                //Print
                try {
                    System.out.println(time);
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
        if (p.getY() < p.getRadius())
            if(p.getX() < boxWidth / 2 - D / 2  || p.getX() > boxWidth / 2 + D / 2 ) // apertura
                walls.add(new Wall(Wall.typeOfWall.BOTTOM));
        return walls;
    }
}
