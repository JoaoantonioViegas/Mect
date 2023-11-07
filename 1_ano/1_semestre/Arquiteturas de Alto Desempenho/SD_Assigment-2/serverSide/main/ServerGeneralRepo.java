package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 *    Server side of the General Repository of Information.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */


public class ServerGeneralRepo {
    
    /**
     *  Flag signaling the service is active.
     */

    public static boolean waitConnection;

    /**
    *  Main method.
    *
    *    @param args runtime arguments
    *        args[0] - port number for listening to service requests
    */

    public static void main(String[] args) {
        GeneralRepo generalRepo;                                          // general repository of information (service provider)
        GeneralRepoInterface generalRepoInter;                            // general repository of information interface
        ServerCom scon, sconi;                                            // communication channels
        int portNumb = -1;                                                // port number for listening to service requests

        if (args.length != 1) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }
        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            GenericIO.writelnString("args[0] is not a valid port number!");
            System.exit(1);
        }

        generalRepo = new GeneralRepo();
        generalRepoInter = new GeneralRepoInterface(generalRepo);
        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString("Service is established!");
        GenericIO.writelnString("Server is listening for service requests.");

        /* service is established */

        GeneralRepoClientProxy cliProxy;                                  // service provider agent

        waitConnection = true;

        while (waitConnection) {
            try {
                sconi = scon.accept();
                cliProxy = new GeneralRepoClientProxy(sconi, generalRepoInter);
                cliProxy.start();
            } catch (SocketTimeoutException e) {
            }
        }
        scon.end();
        GenericIO.writelnString("Server was shutdown.");
        
    }

}
