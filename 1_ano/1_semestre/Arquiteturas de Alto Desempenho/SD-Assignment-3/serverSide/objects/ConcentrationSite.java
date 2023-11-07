package serverSide.objects;

import java.rmi.RemoteException;
import genclass.*;
import clientSide.entities.*;
import interfaces.*;
import serverSide.main.*;
import commInfra.*;

/**
 * ConcentrationSite (shared region)
 * 
 * This class implements the ConcentrationSite shared region.
 * 
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on Java RMI.
 */
public class ConcentrationSite implements ConcentrationSiteInterface{

    /**
     * Reference to the currentAssaultPartyId
     */

    private int currentAssaultPartyId;

    /**
     * Reference to the currentRoomId
     */

    private int currentRoomId;

    /**
     * reference to the thieves waiting in excursion
     */

    private boolean[] waitingInExcursion;

    private boolean masterNeedsWake;

    /**
     * Reference to the General Repository
     */

    private GeneralRepoInterface generalRepo;

    /**
     * Reference to the waiting thieves
     */

    private MemFIFO<Integer> waitingThieves;

    /**
     * Reference to the thieves (id) waiting in concentration site
     */

    private boolean[] waitingInConcentrationSite;

    /**
     * Reference to the number of thieves
     */

    private int numThieves;

    /**
     * Reference to the end of the assault
     */

    private boolean endAssault;

    /**
     * ConcentrationSite constructor
     * 
     * @param generalRepo
     */

    public ConcentrationSite(GeneralRepoInterface generalRepo) {
        this.numThieves = SimulPar.M - 1;
        waitingInConcentrationSite = new boolean[numThieves];
        waitingInExcursion = new boolean[numThieves];
        this.masterNeedsWake = false;
        try {
            this.waitingThieves = new MemFIFO<Integer>(numThieves);
        } catch (Exception e) {
            System.err.println("Error creating waitingThieves");
            System.exit(1);
        }
        this.endAssault = false;
        for (int i = 0; i < numThieves; i++) {
            try {
                waitingThieves.push(i);
                waitingInConcentrationSite[i] = false;
                waitingInExcursion[i] = false;
            } catch (Exception e) {
                System.err.println("Error pushing thieves to waitingThieves");
            }
        }

        this.generalRepo = generalRepo;
    }

    /**
     * Operation am I needed
     * It is called by the ordinary thief to know if he is needed
     * 
     * @param thiefId thief id
     * @return true if the thief is needed, false otherwise and thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized ReturnBoolean amINeeded(int thiefId) throws RemoteException{
        try {
            if (endAssault) {
                return new ReturnBoolean(false, OrdinaryThiefStates.WAITING_UNTIL_NEEDED);
            }
        } catch (Exception e) {}

        System.out.println("Thief " + thiefId + " is waiting to be needed");

        generalRepo.resetOrdinaryAll(thiefId, OrdinaryThiefStates.WAITING_UNTIL_NEEDED);


        // If the current thief is not in the waitingThieves FIFO
        if (waitingInConcentrationSite[thiefId] == false) {
            try {
                // Add the thief to the FIFO
                waitingThieves.push(thiefId);
                waitingInExcursion[thiefId] = false;
                waitingInConcentrationSite[thiefId] = true;
                notifyAll();
                System.out.println("Thief "+ thiefId +"was added to waitingThieves");
            } catch (Exception e) {
                System.err.println("Error pushing thief to waitingThieves");
            }
        }
        // Wait while the thief is not needed
        while (waitingInConcentrationSite[thiefId] == true && endAssault == false) {
            try {
                System.out.println("Thief " + thiefId + " is waiting");
                wait();
            } catch (Exception e) {
                System.err.println("amIneeded wait error");
                System.exit(1);
            }
        }

        // If the thief is not needed anymore
        if (endAssault) {
            return new ReturnBoolean(false, OrdinaryThiefStates.WAITING_UNTIL_NEEDED);
        }
        return new ReturnBoolean(true, OrdinaryThiefStates.WAITING_UNTIL_NEEDED);
    }

    /**
     * Operation prepare excursion
     * It is called by the ordinary thief to prepare the excursion
     * 
     * @param thiefId thief id
     * @return assault party id and room id
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized int[] prepareExcursion(int thiefId) throws RemoteException{
        waitingInExcursion[thiefId] = true;

        System.out.println("Thief " + thiefId + " is preparing the excursion");

        int numWaiting = 0;
        for (int i = 0; i < numThieves; i++) {
            if (waitingInExcursion[i] == true) {
                numWaiting++;
            }
        }
        // If this thief is the last preparing the excursion
        if (numWaiting == 3) {

            for (int i = 0; i < numThieves; i++) {
                if (waitingInExcursion[i] == true) {
                    waitingInExcursion[i] = false;
                }
            }
            // wake up the master thief
            this.masterNeedsWake = false;
            notifyAll();
        } else {
            while (waitingInExcursion[thiefId] == true) {
                try {
                    wait();
                } catch (Exception e) {
                    System.err.println("prepareExcursion wait error");
                    System.exit(1);
                }
            }
        }
        return new int[] { currentAssaultPartyId, currentRoomId };
    }

    /**
     * Operation prepare assault party
     * It is called by the master thief to prepare the assault party
     * 
     * @param assaultPartyId assault party id
     * @param roomId room id
     * @return master thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized int prepareAssaultParty(int assaultPartyId, int roomId) throws RemoteException{
        synchronized (this) {

            if (roomId == -1 || assaultPartyId == -1) {
                System.err.println("Invalid assault party ID or room ID");
                System.exit(1);
            }

            currentAssaultPartyId = assaultPartyId;
            currentRoomId = roomId;

            System.out.println("Master thief is choosing thieves for the assault party " + assaultPartyId);
            generalRepo.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);

            while (!hasEnoughThieves()) {
                try {
                    System.out.println("Master thief is waiting for thieves to arrive");
                    wait();
                } catch (Exception e) {
                }
            }
            
            System.out.println("Thieves arrived");

            for (int i = 0; i < 3; i++) {
                try {
                    int thiefId = waitingThieves.pop();
                    waitingInConcentrationSite[thiefId] = false;
                } catch (Exception e) {
                    System.err.println("Error popping thief from waitingThieves");
                }
            }
            this.masterNeedsWake = true;
            notifyAll(); // wake up all the thieves that are waiting to be needed

            System.out.println("Master thief is waiting for the thieves to prepare the assault party " + assaultPartyId);

            while (masterNeedsWake == true) // while the master thief is in the state ASSEMBLING_A_GROUP
            {
                try {
                    wait(); // wait for the master thief to change state
                } catch (Exception e) {
                    System.err.println("Error waiting");
                    System.exit(1);
                }
            }
        }
        return MasterThiefStates.ASSEMBLING_A_GROUP;
    }

    /**
     * Internal method to check if there are enough thieves to form an assault party
     * 
     * @return true if there are enough thieves, false otherwise
     */

    private boolean hasEnoughThieves() {
        int numWait = 0;
        for (int i = 0; i < numThieves; i++) {
            if (waitingInConcentrationSite[i] == true) {
                numWait++;
            }
        }
        if (numWait >= 3) {
            return true;
        } else {
            return false;
        }
    }

    /** 
     * Operation sum up results
     * It is called by the master thief to sum up the results
     * 
     * @param numStolenPaintings number of stolen paintings
     * @return master thief state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public synchronized int sumUpResults(int numStolenPaintings) throws RemoteException{
        generalRepo.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        endAssault = true;
        System.out.println("Number of stolen paintings: " + numStolenPaintings);
        notifyAll();
        return MasterThiefStates.PRESENTING_THE_REPORT;
    }

    /**
     * Operation shutdown
     * 
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry fail
     */
    @Override
    public void shutdown() throws RemoteException{
        ServerConcentrationSite.shutdown();
    }
}
