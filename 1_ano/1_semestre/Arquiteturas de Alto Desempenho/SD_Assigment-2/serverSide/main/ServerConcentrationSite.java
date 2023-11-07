package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 *    Server side of the Concentration Site.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ServerConcentrationSite {
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
        ConcentrationSite concentrationSite;                                          // concentrationSite object
        ConcentrationSiteInterface concentrationSiteInter;                            // interface to the concentrationSite
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
        concentrationSite = new ConcentrationSite(repoStub);
        concentrationSiteInter = new ConcentrationSiteInterface(concentrationSite);
        scon = new ServerCom(portNumb);
        scon.start();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        /* Service request processing */

        ConcentrationSiteClientProxy cliProxy;
        waitConnection = true;

        while (waitConnection)
            try{ 
                sconi = scon.accept();
                cliProxy = new ConcentrationSiteClientProxy(sconi, concentrationSiteInter);
                System.out.println("New message!");
                cliProxy.start();
            } catch (SocketTimeoutException e){ }
        scon.end();
        GenericIO.writelnString ("Server was shutdown.");
    }
    
}
