package serverSide.sharedRegions;

import serverSide.entities.*;
import clientSide.stubs.GeneralRepoStub;
import commInfra.*;
import serverSide.main.ServerConcentrationSite;
import serverSide.main.SimulPar;
import clientSide.entities.*;

/**
 * Concentration Site (shared region)
 * 
 * This class implements the concentration site shared region.
 */

public class ConcentrationSite {

    /**
	 * Number of entity groups requesting the shutdown
	 */
	private int nEntities;

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

    private GeneralRepoStub generalRepo;

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

    public ConcentrationSite(GeneralRepoStub generalRepo) {
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
     * Called by the ordinary thief
     * 
     * @return true if the thief is needed, false otherwise
     */

    public synchronized boolean amINeeded() {
        try {
            if (endAssault) {
                return false;
            }
        } catch (Exception e) {
        }

        int thiefId = ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryThiefId();
        System.out.println("Thief " + thiefId + " is waiting to be needed");
        ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryThiefState(OrdinaryThiefStates.WAITING_UNTIL_NEEDED);

        generalRepo.resetOrdinaryAll(thiefId, OrdinaryThiefStates.WAITING_UNTIL_NEEDED);

        if (((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryThiefState() == OrdinaryThiefStates.WAITING_UNTIL_NEEDED) {

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
                return false;
            }
        }
        return true;
    }

    /**
     * Called by the ordinary thief
     * The thief waits for the other thieves of the party
     * If the thief is the last one to arrive, he wakes up the master thief
     * 
     * @return the assault party id and the room id
     */

    public synchronized int[] prepareExcursion() {
        int thiefId = ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryThiefId();
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
     * Called by the master thief
     * The master choses 3 thieves to form an assault party
     * He wakes up the thieves and waits for them to prepare the excursion
     * 
     * @param assaultPartyID
     * @param RoomID
     */

    public void prepareAssaultParty(int assaultPartyID, int RoomID) {

        synchronized (this) {

            if (RoomID == -1 || assaultPartyID == -1) {
                System.err.println("Invalid assault party ID or room ID");
                System.exit(1);
            }

            currentAssaultPartyId = assaultPartyID;
            currentRoomId = RoomID;

            System.out.println("Master thief is choosing thieves for the assault party " + assaultPartyID);

            ((ConcentrationSiteClientProxy) Thread.currentThread()).setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);
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

            System.out.println("Master thief is waiting for the thieves to prepare the assault party " + assaultPartyID);

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

    /*
     * Called by master thief
     * He ends the assault and wakes up the thieves
     * 
     * @param numStolenPaintings
     * 
     * @return
     */

    public synchronized void sumUpResults(int numStolenPaintings) {
        ((ConcentrationSiteClientProxy) Thread.currentThread()).setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        generalRepo.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        endAssault = true;
        System.out.println("Number of stolen paintings: " + numStolenPaintings);
        notifyAll();
    }

    /**
     * Operation to shut down the Concentration Site server
     */

     public synchronized void shutdown(){
        ServerConcentrationSite.waitConnection = false;
    }

}