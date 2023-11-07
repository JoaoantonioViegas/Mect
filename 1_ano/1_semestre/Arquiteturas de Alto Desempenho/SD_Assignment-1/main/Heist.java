package main;

import entities.*;
import sharedRegions.*;

import java.util.Random;

public class Heist {

    /**
     * Main program.
     * 
     * @param args runtime arguments
     */

    public static void main(String[] args) {
        MasterThief masterThief;
        OrdinaryThief[] ordinaryThieves = new OrdinaryThief[SimulPar.M - 1];

        ControlCollectionSite controlCollectionSite;
        ConcentrationSite concentrationSite;
        Museum museum;
        AssaultParty[] assaultParty = new AssaultParty[SimulPar.A];

        GeneralRepo repos;

        int[] canvasInRoom = new int[SimulPar.N];
        int total = 0;
        for (int i = 0; i < SimulPar.N; i++) {
            canvasInRoom[i] = new Random().nextInt(9) + 8;
            total += canvasInRoom[i];
        }

        int[] distanceToRoom = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++) {
            distanceToRoom[i] = new Random().nextInt(16) + 15;
        }

        int[] agility = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            agility[i] = new Random().nextInt(5) + 2;
        }

        /* Simulation initialization */

        System.out.println("Heist to the Museum");
        System.out.println(total + " total canvas");
        repos = new GeneralRepo("log.txt", canvasInRoom, distanceToRoom, agility);
        controlCollectionSite = new ControlCollectionSite(repos);
        System.out.println("Control collection site created");
        assaultParty[0] = new AssaultParty(0, SimulPar.M - 3, -1, distanceToRoom, SimulPar.S, repos);
        System.out.println("Assault party 0 created");
        assaultParty[1] = new AssaultParty(1, SimulPar.M - 3, -1, distanceToRoom, SimulPar.S, repos);
        System.out.println("Assault party 1 created");
        concentrationSite = new ConcentrationSite(repos);
        System.out.println("Concentration site created");
        museum = new Museum(repos, SimulPar.N, canvasInRoom);
        System.out.println("Museum created");

        masterThief = new MasterThief(repos, concentrationSite, controlCollectionSite, assaultParty);
        System.out.println("Master thief created");

        for (int i = 0; i < SimulPar.M - 1; i++) {
            ordinaryThieves[i] = new OrdinaryThief(i, agility[i], repos, museum, concentrationSite,
                    controlCollectionSite,
                    assaultParty);
            System.out.println("Ordinary thief " + i + " created");
        }

        /* Start of the simulation */

        System.out.println("Starting the ordinary thieves");
        for (int i = 0; i < SimulPar.M - 1; i++) {
            ordinaryThieves[i].start();
        }

        System.out.println("Starting the master thief");
        masterThief.start();

        /* Wait for the end of the simulation */

        System.out.println("\n\n");

        for (int i = 0; i < SimulPar.M - 1; i++) {
            try {
                ordinaryThieves[i].join();
                System.out.println("Joined ordinary thief " + i);
            } catch (InterruptedException e) {
                System.out.println("Ordinary thief " + i + " terminated");
            }
        }

        try {
            masterThief.join();
            System.out.println("Joined master thief");
        } catch (InterruptedException e) {
            System.out.println("Master thief terminated");
        }

        /* End of simulation */
        repos.endAssault();

        System.out.println("End of simulation");
    }
}