package sharedRegions;

import main.*;
import entities.*;

import commInfra.*;

/**
 * Concentration Site (shared region)
 * 
 * This class implements the concentration site shared region.
 */

public class ConcentrationSite {

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

    private GeneralRepo generalRepo;

    /**
     * Reference to the Master Thief
     */

    private MasterThief masterThief;

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

    public ConcentrationSite(GeneralRepo generalRepo) {
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

        int thiefId = ((OrdinaryThief) Thread.currentThread()).getThiefId();
        OrdinaryThief currentThief = ((OrdinaryThief) Thread.currentThread());
        currentThief.setThiefState(OrdinaryThiefStates.WAITING_UNTIL_NEEDED);

        generalRepo.resetOrdinaryAll(thiefId, OrdinaryThiefStates.WAITING_UNTIL_NEEDED);

        if (currentThief.getThiefState() == OrdinaryThiefStates.WAITING_UNTIL_NEEDED) {

            // If the current thief is not in the waitingThieves FIFO
            if (waitingInConcentrationSite[thiefId] == false) {
                try {
                    // Add the thief to the FIFO
                    waitingThieves.push(thiefId);
                    waitingInExcursion[thiefId] = false;
                    waitingInConcentrationSite[thiefId] = true;
                    notifyAll();
                } catch (Exception e) {
                    System.err.println("Error pushing thief to waitingThieves");
                }
            }
            // Wait while the thief is not needed
            while (waitingInConcentrationSite[thiefId] == true && endAssault == false) {
                try {
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
        int thiefId = ((OrdinaryThief) Thread.currentThread()).getThiefId();
        waitingInExcursion[thiefId] = true;

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

            masterThief = ((MasterThief) Thread.currentThread());
            masterThief.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);
            generalRepo.setMasterThiefState(MasterThiefStates.ASSEMBLING_A_GROUP);

            while (!hasEnoughThieves()) {
                try {
                    wait();
                } catch (Exception e) {
                }
            }
            ;

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
        masterThief = (MasterThief) Thread.currentThread();
        ((MasterThief) Thread.currentThread()).setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        generalRepo.setMasterThiefState(MasterThiefStates.PRESENTING_THE_REPORT);
        endAssault = true;
        System.out.println("Number of stolen paintings: " + numStolenPaintings);
        notifyAll();
    }

}