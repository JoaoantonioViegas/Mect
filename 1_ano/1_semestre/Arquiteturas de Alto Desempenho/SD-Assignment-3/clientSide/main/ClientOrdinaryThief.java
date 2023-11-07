package clientSide.main;

import genclass.GenericIO;
import interfaces.*;
import serverSide.main.SimulPar;
import clientSide.entities.*;

import java.rmi.registry.*;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.*;

/**
 *  Client side of the Hesit problem (Master thief).
 *
 *	Implementation of a client-server model of type 2 (server replication).
 *	Communication is based on Java RMI.
 */

public class ClientOrdinaryThief {

    /**
	 *  Main method.
	 *
	 *     @param args runtime arguments
     *         args[0] - name of the platform where is located the RMI registering service
     *         args[1] - port number where the registering service is listening to service requests
	 */

    public static void main(String[] args) {
        
        /* Name of the platform where is located the RMI registering service  */
        String rmiRegHostName;

        /* Port number where the registering service is listening to service request */
		int rmiRegPortNumb = -1;

        /* Getting problem runtime arguments */

        if (args.length != 2){ 
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }
        rmiRegHostName = args[0];

        try { 
            rmiRegPortNumb = Integer.parseInt (args[1]);
        } catch (NumberFormatException e) { 
            GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }

        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) { 
            GenericIO.writelnString ("args[1] is not a valid port number!");
            System.exit (1);
        }

        /* problem initialization */
        String nameEntryConcentrationSite = "ConcentrationSite";
        String nameEntryControlSite = "ControlSite";
        String nameEntryAssaultParty0 = "AssaultParty0";
        String nameEntryAssaultParty1 = "AssaultParty1";
        String nameEntryGeneralRepo = "GeneralRepository";
        String nameEntryMuseum = "Museum";

        GeneralRepoInterface generalRepositoryStub = null;
        ConcentrationSiteInterface concentrationSiteStub = null;
        ControlSiteInterface controlSiteStub = null;
        AssaultPartyInterface[] assaultPartyStub = new AssaultPartyInterface[2];
        MuseumInterface museumStub = null;
        Registry registry = null;
        OrdinaryThief[] ordinaryThief = new OrdinaryThief[SimulPar.M - 1];

        /* Generating random values */

        int[] agility = new int[SimulPar.M - 1];
        for (int i = 0; i < SimulPar.M - 1; i++) {
            agility[i] = new Random().nextInt(5) + 2;
        }

        int[] distanceToRoom = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++) {
            distanceToRoom[i] = new Random().nextInt(16) + 15;
        }

        int[] canvasInRoom = new int[SimulPar.N];
        int total = 0;
        for (int i = 0; i < SimulPar.N; i++) {
            canvasInRoom[i] = new Random().nextInt(9) + 8;
            total += canvasInRoom[i];
        }

        GenericIO.writelnString("LOGGING: Total number of canvas in the museum: " + total);

        //Write to the file /home/amaral/Heist/generalRepository/total.txt the value of total
        try {
            FileWriter fw = new FileWriter("./total.txt");
            fw.write(String.valueOf(total));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Locating stubs*/

        /* Registry */
		try {
			registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
		} catch (RemoteException e) {
			GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		}

        /* General Repository */
        try {
			generalRepositoryStub = (GeneralRepoInterface) registry.lookup(nameEntryGeneralRepo);
		} catch( RemoteException e ) {
			GenericIO.writelnString ("General Repository lookup exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		} catch( NotBoundException e ) {
			GenericIO.writelnString ("General Repository not bound exception: " + e.getMessage ());
	        e.printStackTrace ();
	        System.exit (1);			
		}

        /* Concentration Site */
        try {
            concentrationSiteStub = (ConcentrationSiteInterface) registry.lookup(nameEntryConcentrationSite);
        } catch (RemoteException e) {
            GenericIO.writelnString("Concentration Site lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Concentration Site not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* Control Site */
        try {
            controlSiteStub = (ControlSiteInterface) registry.lookup(nameEntryControlSite);
        } catch (RemoteException e) {
            GenericIO.writelnString("Control Site lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Control Site not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* Assault Party 0 */
        try{
            assaultPartyStub[0] = (AssaultPartyInterface) registry.lookup(nameEntryAssaultParty0);
        } catch (RemoteException e) {
            GenericIO.writelnString("Assault Party 0 lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Assault Party 0 not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /* Assault Party 1 */
        try{
            assaultPartyStub[1] = (AssaultPartyInterface) registry.lookup(nameEntryAssaultParty1);
        } catch (RemoteException e) {
            GenericIO.writelnString("Assault Party 1 lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Assault Party 1 not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } 

        /* Museum */
        try {
            museumStub = (MuseumInterface) registry.lookup(nameEntryMuseum);
        } catch (RemoteException e) {
            GenericIO.writelnString("Museum lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Museum not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            generalRepositoryStub.setOrdinaryThiefMaxDisplacement(agility);
        } catch (Exception e) {
            GenericIO.writelnString("Error setting Ordinary Thief Max Displacement: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            generalRepositoryStub.setDistanceToRoom(distanceToRoom);
        } catch (Exception e) {
            GenericIO.writelnString("Error setting Distance to Room: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        try {
            generalRepositoryStub.setPaintingsInRoom(canvasInRoom);
        } catch (Exception e) {
            GenericIO.writelnString("Error setting Paintings in Room: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        try {
            assaultPartyStub[0].setDistanceToRoom(distanceToRoom);
        } catch (Exception e) {
            GenericIO.writelnString("0 - Error setting Distance to Room: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            assaultPartyStub[1].setDistanceToRoom(distanceToRoom);
        } catch (Exception e) {
            GenericIO.writelnString("1 - Error setting Distance to Room: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        try {
            museumStub.setCanvas(canvasInRoom);
        } catch (Exception e) {
            GenericIO.writelnString("Error setting Paintings in Room: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        for (int i = 0; i < SimulPar.M - 1; i++) {
            ordinaryThief[i] = new OrdinaryThief(i, agility[i], museumStub, concentrationSiteStub, controlSiteStub, assaultPartyStub);
        }

        /* start of the simulation */
        for (int i = 0; i < SimulPar.M - 1; i++) {
            GenericIO.writelnString("Launching Ordinary Thief Thread " + i + "!");
            ordinaryThief[i].start();
        }

        /* waiting for the end of the simulation */
        for (int i = 0; i < SimulPar.M - 1; i++) {
            try {
                ordinaryThief[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("The Ordinary Thief " + i + " has terminated.");
            GenericIO.writelnString("Shutting down servers...");
        }

        try {
            assaultPartyStub[0].shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        try {
            assaultPartyStub[1].shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        try {
            concentrationSiteStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        try {
            museumStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        try {
            generalRepositoryStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        try {
            controlSiteStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
    }
    
}
