package ar.edu.itba.ss;

import ar.edu.itba.ss.GranularMedia.GranularMediaForce;
import ar.edu.itba.ss.Integrators.GearPredictor;
import ar.edu.itba.ss.Integrators.Integrator;
import ar.edu.itba.ss.io.Input;

public class Simulation
{
    public static void main( String[] args )
    {
        // Initial conditions
        Input input = new Input();
        Double simulationDT = 0.1*Math.sqrt(input.getMass()/input.getKn());   //Default ; TODO: Check if there is a better one
        Double printDT = simulationDT / 2d;
        Integrator integrator = new GearPredictor(simulationDT, new GranularMediaForce());

        //Simulation
        for (double time = 0d ; time < input.getEndTime() ; time += simulationDT){
//            integrator.moveParticle();

            if (time % printDT == 0){
                //Print
            }
        }

        // Output


    }
}
