package ar.edu.itba.ss;

import ar.edu.itba.ss.GranularMedia.GranularMediaForce;
import ar.edu.itba.ss.Integrators.GearPredictor;
import ar.edu.itba.ss.Integrators.Integrator;
import ar.edu.itba.ss.Integrators.NeighborDetection;
import ar.edu.itba.ss.io.Input;
import ar.edu.itba.ss.io.Output;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Wall;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulation
{
    public static void main( String[] args )
    {
        // Initial conditions
        Input input = new Input();
        Double simulationDT = 0.1*Math.sqrt(input.getMass()/input.getKn());   //Default ; TODO: Check if there is a better one
        Double printDT = simulationDT / 2d;
        Integrator integrator = new GearPredictor(simulationDT,
                new GranularMediaForce(input.getKn(), input.getKt(), input.getW(), input.getL()));
        // Can use other integrator.
        Map<Particle, List<Particle>> neighbours = new HashMap<>();
        List<Particle> particles = input.getParticles();
        Output.generateXYZFile();

        //Simulation
        for (double time = 0d ; time < input.getEndTime() ; time += simulationDT){
//            integrator.moveParticle();
            neighbours = NeighborDetection.getNeighbours(
                    input.getGrid(), input.getUsedCells(),
                    0d, false
            );
            for (Particle particle : particles){
                integrator.moveParticle(
                        particle, simulationDT,
                        neighbours.get(particle),
                        getWallsCollisions(particle, input.getW(), input.getL())
                );
            }
            for (Particle particle : particles){
                particle.updateState();
            }
            if (time % printDT == 0){
                //Print
                try {
                    Output.printToFile(particles);
                }catch (IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }

        // Output
    }

    public static List<Wall> getWallsCollisions(Particle p, Double boxWidth, Double boxHeight){
        List<Wall> walls = Collections.emptyList();
        if (p.getX() - p.getRadius() < 0)
            walls.add(new Wall(Wall.typeOfWall.LEFT));
        if (p.getX() + p.getRadius() < boxWidth)
            walls.add(new Wall(Wall.typeOfWall.RIGHT));
        if (p.getY() - p.getRadius() < 0)
            walls.add(new Wall(Wall.typeOfWall.TOP));
        if (p.getY() + p.getRadius() < boxHeight)
            walls.add(new Wall(Wall.typeOfWall.BOTTOM));
        return walls;
    }
}
