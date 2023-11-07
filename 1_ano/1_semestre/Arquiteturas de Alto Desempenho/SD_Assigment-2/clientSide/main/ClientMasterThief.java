package clientSide.main;

import clientSide.stubs.*;

import clientSide.entities.*;
import genclass.GenericIO;
import serverSide.main.SimulPar;

/**
 *  Client side of the Hesit problem (Master thief).
 *
 *	Implementation of a client-server model of type 2 (server replication).
 *	Communication is based on a communication channel under the TCP protocol.
 */
public class ClientMasterThief {

    /**
     *  Main method.
     *
     *     @param args runtime arguments
     *         args[0] - name of the platform where is located the ConcentrationSite server
     *         args[1] - port number for listening to service requests
     *		   args[2] - name of the platform where is located the ControlSite server
     *         args[3] - port number for listening to service requests
     *         args[4] - name of the platform where is located the AssaultParty0 server
     *         args[5] - port number for listening to service requests
     *         args[6] - name of the platform where is located the AssaultParty1 server
     *         args[7] - port number for listening to service requests
     *         args[8] - name of the platform where is located the General Repository server
     *         args[9] - port number for listening to service requests
     */

    public static void main(String[] args) {
        
        MasterThief masterThief;
        ConcentrationSiteStub concentrationSiteStub;
        ControlSiteStub controlCollectionSiteStub;
        AssaultPartyStub[] assaultPartyStub = new AssaultPartyStub[SimulPar.A];
        GeneralRepoStub generalRepositoryStub;

        String concentrationServerHostName, controlCollectionServerHostName , assaultParty0ServerHostName, assaultParty1ServerHostName, generalRepositoryServerHostName;
        int concentrationServerPortNumb = -1, controlCollectionServerPortNumb = -1, assaultParty0ServerPortNumb = -1, assaultParty1ServerPortNumb = -1, generalRepositoryServerPortNumb = -1;

        if(args.length != 10) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }

        concentrationServerHostName = args[0];
        try {
            concentrationServerPortNumb = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[1] is not a number!");
            System.exit(1);
        }
        if ((concentrationServerPortNumb < 4000) || (concentrationServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[1] is not a valid port number!");
            System.exit(1);
        }

        controlCollectionServerHostName = args[2];
        try {
            controlCollectionServerPortNumb = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if ((controlCollectionServerPortNumb < 4000) || (controlCollectionServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }

        assaultParty0ServerHostName = args[4];
        try {
            assaultParty0ServerPortNumb = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[5] is not a number!");
            System.exit(1);
        }
        if ((assaultParty0ServerPortNumb < 4000) || (assaultParty0ServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[5] is not a valid port number!");
            System.exit(1);
        }

        assaultParty1ServerHostName = args[6];
        try {
            assaultParty1ServerPortNumb = Integer.parseInt(args[7]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[7] is not a number!");
            System.exit(1);
        }
        if ((assaultParty1ServerPortNumb < 4000) || (assaultParty1ServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[7] is not a valid port number!");
            System.exit(1);
        }

        generalRepositoryServerHostName = args[8];
        try {
            generalRepositoryServerPortNumb = Integer.parseInt(args[9]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[9] is not a number!");
            System.exit(1);
        }
        if ((generalRepositoryServerPortNumb < 4000) || (generalRepositoryServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[9] is not a valid port number!");
            System.exit(1);
        }

        /* problem initialization */
        concentrationSiteStub = new ConcentrationSiteStub(concentrationServerHostName, concentrationServerPortNumb);
        controlCollectionSiteStub = new ControlSiteStub(controlCollectionServerHostName, controlCollectionServerPortNumb);
        assaultPartyStub[0] = new AssaultPartyStub(0, assaultParty0ServerHostName, assaultParty0ServerPortNumb);
        assaultPartyStub[1] = new AssaultPartyStub(1, assaultParty1ServerHostName, assaultParty1ServerPortNumb);
        generalRepositoryStub = new GeneralRepoStub(generalRepositoryServerHostName, generalRepositoryServerPortNumb);
        masterThief = new MasterThief(concentrationSiteStub, controlCollectionSiteStub, assaultPartyStub);
    
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
        generalRepositoryStub.endAssault();

    }
}
