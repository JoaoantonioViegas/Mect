package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;
import java.util.Random;

/**
 *    Server side of the Museum.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ServerMuseum{

    /**
     *  Flag signaling the service is active.
     */

    public static boolean waitConnection;

    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - port number for listening to service requests
     *        args[1] - name of the platform where is located the server for the general repository
     *        args[2] - port number where the server for the general repository is listening to service requests
     */

     public static void main(String[] args) {
        Museum museum;                                          // museum object
        MuseumInterface museumInter;                            // interface to the museum
        GeneralRepoStub repoStub;                               // stub to the general repository
        ServerCom scon, sconi;                                  // communication channels
        int portNumb = -1;                                      // port number for listening to service requests
        String repoServerName;                                  // name of the platform where is located the server for the general repository
        int repoServerPortNumb = -1;                            // port number where the server for the general repository is listening to service requests

        if (args.length != 3){ 
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }

        try { 
            portNumb = Integer.parseInt (args[0]);
        } catch (NumberFormatException e) { 
            GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) { 
            GenericIO.writelnString ("args[0] is not a valid port number!");
            System.exit (1);
        }

        repoServerName = args[1];
        try { 
            repoServerPortNumb = Integer.parseInt (args[2]);
        } catch (NumberFormatException e) { 
            GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if ((repoServerPortNumb < 4000) || (repoServerPortNumb >= 65536)) { 
            GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        /* Service is established */

        repoStub = new GeneralRepoStub(repoServerName, repoServerPortNumb);

        museum = new Museum(repoStub, SimulPar.N);
        museumInter = new MuseumInterface(museum);
        scon = new ServerCom (portNumb);
        scon.start ();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        /* service request processing */

        MuseumClientProxy cliProxy;                               // service provider agent
        waitConnection = true;   
        
        while(waitConnection)
            try{ 
                sconi = scon.accept ();                          // enter listening procedure
                cliProxy = new MuseumClientProxy(sconi, museumInter);  
                cliProxy.start ();
            }catch (SocketTimeoutException e){ }
        scon.end ();
        GenericIO.writelnString ("Server was shutdown!");
    }

}