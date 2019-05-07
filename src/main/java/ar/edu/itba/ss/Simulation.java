package ar.edu.itba.ss;

import ar.edu.itba.ss.GranularMedia.GranularMediaForce;
import ar.edu.itba.ss.Integrators.GearPredictor;
import ar.edu.itba.ss.Integrators.Integrator;

public class Simulation
{
    public static void main( String[] args )
    {
        Double dt = 0.1;
        Integrator integrator = new GearPredictor(dt, new GranularMediaForce());
    }
}
