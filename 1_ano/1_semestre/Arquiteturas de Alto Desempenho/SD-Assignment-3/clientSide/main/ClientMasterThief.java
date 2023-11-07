package clientSide.main;

import genclass.GenericIO;
import interfaces.*;
import clientSide.entities.*;
import java.rmi.registry.*;
import java.rmi.*;

/**
 *  Client side of the Hesit problem (Master thief).
 *
 *	Implementation of a client-server model of type 2 (server replication).
 *	Communication is based on Java RMI.
 */

public class ClientMasterThief {

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

        GeneralRepoInterface generalRepositoryStub = null;
        ConcentrationSiteInterface concentrationSiteStub = null;
        ControlSiteInterface controlSite = null;
        AssaultPartyInterface[] assaultPartyStub = new AssaultPartyInterface[2];
        MasterThief masterThief;
        Registry registry = null;

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
            controlSite = (ControlSiteInterface) registry.lookup(nameEntryControlSite);
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

        /* Initialize master */

        masterThief = new MasterThief(concentrationSiteStub, controlSite, assaultPartyStub);
    
        /* start of the simulation */

        masterThief.start();

        /* waiting for the end of the simulation */

        GenericIO.writelnString();
        try {
            masterThief.join();
        } catch (InterruptedException e) {
            GenericIO.writelnString("Master Thief thread has ended.");
        }
        
        /* shutdown of the simulation */

        GenericIO.writelnString("The heist has ended.");

        try {
            generalRepositoryStub.endAssault();
        } catch (RemoteException e) {
            GenericIO.writelnString("Remote exception: " + e.getMessage());
            System.exit(1);
        }
        
    }
}
