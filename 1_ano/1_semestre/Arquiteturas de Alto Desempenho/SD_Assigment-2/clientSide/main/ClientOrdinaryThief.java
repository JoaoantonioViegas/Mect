package clientSide.main;

import clientSide.stubs.*;

import java.util.Random;

import clientSide.entities.*;
import genclass.GenericIO;
import serverSide.main.SimulPar;

//debug
import java.io.FileWriter;
import java.io.IOException;

/**
 *  Client side of the Hesit problem (Ordinary thief).
 *
 *	Implementation of a client-server model of type 2 (server replication).
 *	Communication is based on a communication channel under the TCP protocol.
 */

public class ClientOrdinaryThief {

    /**
	 *  Main method.
	 *
	 *     @param args runtime arguments
     *         args[0] - name of the platform where is located the ConcentrationSite server
	 *         args[1] - port number for listening to service requests
     *		   args[2] - name of the platform where is located the ControlSite server
	 *         args[3] - port number for listening to service requests
	 *         args[4] - name of the platform where is located the Museum server
	 *         args[5] - port number for listening to service requests
     *         args[6] - name of the platform where is located the AssaultParty0 server
     *         args[7] - port number for listening to service requests
     *         args[8] - name of the platform where is located the AssaultParty1 server
     *         args[9] - port number for listening to service requests
     *         args[10] - name of the platform where is located the General Repository server
     *         args[11] - port number for listening to service requests
	 */

    public static void main(String[] args) {
        
        OrdinaryThief[] ordinaryThief = new OrdinaryThief[SimulPar.M-1];
        ConcentrationSiteStub concentrationSiteStub;
        ControlSiteStub controlCollectionSiteStub;
        MuseumStub museumStub;
        AssaultPartyStub[] assaultPartyStub = new AssaultPartyStub[SimulPar.A];
        GeneralRepoStub generalRepositoryStub;

        String concentrationServerHostName, controlCollectionServerHostName, museumServerHostName, assaultParty0ServerHostName, assaultParty1ServerHostName, generalRepositoryServerHostName;
        int concentrationServerPortNumb = -1, controlCollectionServerPortNumb = -1, museumServerPortNumb = -1, assaultParty0ServerPortNumb = -1, assaultParty1ServerPortNumb = -1,generalRepositoryServerPortNumb = -1;

        if(args.length != 12) {
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

        museumServerHostName = args[4];
        try {
            museumServerPortNumb = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[5] is not a number!");
            System.exit(1);
        }
        if ((museumServerPortNumb < 4000) || (museumServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[5] is not a valid port number!");
            System.exit(1);
        }

        assaultParty0ServerHostName = args[6];
        try {
            assaultParty0ServerPortNumb = Integer.parseInt(args[7]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[7] is not a number!");
            System.exit(1);
        }
        if ((assaultParty0ServerPortNumb < 4000) || (assaultParty0ServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[7] is not a valid port number!");
            System.exit(1);
        }

        assaultParty1ServerHostName = args[8];
        try {
            assaultParty1ServerPortNumb = Integer.parseInt(args[9]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[9] is not a number!");
            System.exit(1);
        }
        if ((assaultParty1ServerPortNumb < 4000) || (assaultParty1ServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[9] is not a valid port number!");
            System.exit(1);
        }

        generalRepositoryServerHostName = args[10];
        try {
            generalRepositoryServerPortNumb = Integer.parseInt(args[11]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[11] is not a number!");
            System.exit(1);
        }
        if ((generalRepositoryServerPortNumb < 4000) || (generalRepositoryServerPortNumb >= 65536)) {
            GenericIO.writelnString("args[11] is not a valid port number!");
            System.exit(1);
        }

        /* problem initialization */
        concentrationSiteStub = new ConcentrationSiteStub(concentrationServerHostName, concentrationServerPortNumb);
        controlCollectionSiteStub = new ControlSiteStub(controlCollectionServerHostName, controlCollectionServerPortNumb);
        museumStub = new MuseumStub(SimulPar.N, museumServerHostName, museumServerPortNumb);
        assaultPartyStub[0] = new AssaultPartyStub(0, assaultParty0ServerHostName, assaultParty0ServerPortNumb);
        assaultPartyStub[1] = new AssaultPartyStub(1, assaultParty1ServerHostName, assaultParty1ServerPortNumb);

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

        generalRepositoryStub = new GeneralRepoStub(generalRepositoryServerHostName, generalRepositoryServerPortNumb);

        generalRepositoryStub.setOrdinaryThiefMaxDisplacement(agility);
        generalRepositoryStub.setDistanceToRoom(distanceToRoom);
        generalRepositoryStub.setPaintingsInRoom(canvasInRoom);
        
        assaultPartyStub[0].setRoomDistances(distanceToRoom);
        assaultPartyStub[1].setRoomDistances(distanceToRoom);
        
        museumStub.setPaintingsInRoom(canvasInRoom);

        for (int i = 0; i < SimulPar.M - 1; i++) {
            ordinaryThief[i] = new OrdinaryThief(i, agility[i], museumStub, concentrationSiteStub, controlCollectionSiteStub, assaultPartyStub);
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

        assaultPartyStub[0].shutdown();
        assaultPartyStub[1].shutdown();
        concentrationSiteStub.shutdown();
        museumStub.shutdown();
        generalRepositoryStub.shutdown();
        controlCollectionSiteStub.shutdown();

    }
    
}
