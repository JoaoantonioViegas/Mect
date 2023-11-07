package serverSide.main;

import java.rmi.registry.*;
import java.rmi.*;
import java.rmi.server.*;
import serverSide.objects.*;
import interfaces.*;
import genclass.GenericIO;
/**
 *    Server side of the ConcentrationSite.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ServerConcentrationSite{

    /**
     *  Flag signaling the service is active.
     */

    public static boolean end = false;

    /**
	 *  Main method.
	 *
	 *        args[0] - port number for listening to service requests
	 *        args[1] - name of the platform where is located the RMI registering service
	 *        args[2] - port number where the registering service is listening to service requests
	 */

     public static void main(String[] args) {
        int portNumb = -1;                          // port number for listening to service requests
		String rmiRegHostName;                      // name of the platform where is located the RMI registering service
		int rmiRegPortNumb = -1;                    // port number where the registering service is listening to service requests

        /*Name of the objects */
        String nameEntryGeneralRepo = "GeneralRepository";
        String nameEntryConcentrationSite = "ConcentrationSite";

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

        rmiRegHostName = args[1];
        try { 
            rmiRegPortNumb = Integer.parseInt (args[2]);
        } catch (NumberFormatException e) { 
            GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) { 
            GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        /* create and install the security manager */

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        GenericIO.writelnString ("Security manager was installed!");

        /* Locate RMI registry service */

        Registry registry = null; // remote reference for registration in the RMI registry service

		try
		{ registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
		}
		catch (RemoteException e)
		{ GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}
		GenericIO.writelnString ("RMI registry was created!");


        /* get a remote reference to the general repository object */
        GeneralRepoInterface repoStub = null;                        // remote reference to the general repository object

        try
		{ repoStub = (GeneralRepoInterface) registry.lookup (nameEntryGeneralRepo);
		}
		catch (RemoteException e)
		{ GenericIO.writelnString ("General Repository lookup exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}
		catch (NotBoundException e)
		{ GenericIO.writelnString ("General Repository not bound exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}

        /* get a remote reference to the concentrationSite object */
        ConcentrationSite concentrationSite = new ConcentrationSite(repoStub);
        ConcentrationSiteInterface concentrationSiteStub = null;                        // remote reference to the concentrationSite object

        try
        { concentrationSiteStub = (ConcentrationSiteInterface) UnicastRemoteObject.exportObject (concentrationSite, portNumb);
        }
        catch (RemoteException e)
		{ GenericIO.writelnString ("ConcentrationSite stub generation exception: " + e.getMessage ());
		e.printStackTrace ();
		System.exit (1);
		}
		GenericIO.writelnString ("ConcentrationSite stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";                      // public name of the object that enables the registration of other remote objects
        Register reg = null;                                           // remote reference to the object that enables the registration of other remote objects

        try
        { reg = (Register) registry.lookup (nameEntryBase);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject lookup exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("RegisterRemoteObject not bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        try
        { reg.bind (nameEntryConcentrationSite, concentrationSiteStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ConcentrationSite registration exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (AlreadyBoundException e)
        { GenericIO.writelnString ("ConcentrationSite already bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        GenericIO.writelnString ("ConcentrationSite object was registered!");

        /* Wait for the service to end */

        GenericIO.writelnString ("ConcentrationSite is in operation!");

        try
        { while (!end)
            synchronized (Class.forName ("serverSide.main.ServerConcentrationSite"))
            { try
                { (Class.forName ("serverSide.main.ServerConcentrationSite")).wait ();
                }
                catch (InterruptedException e)
                { GenericIO.writelnString ("ConcentrationSite main thread was interrupted!");
                }
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerConcentrationSite was not found (blocking)!");
        e.printStackTrace ();
        System.exit (1);
        }

        /* server shutdown */

        boolean shutdownDone = false;                                  // flag signalling the shutdown of the general repository service

        try
        { reg.unbind (nameEntryConcentrationSite);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ConcentrationSite deregistration exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("ConcentrationSite not bound exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        GenericIO.writelnString ("ConcentrationSite was deregistered!");

        try
        { shutdownDone = UnicastRemoteObject.unexportObject (concentrationSite, true);
        }
        catch (NoSuchObjectException e)
        { GenericIO.writelnString ("ConcentrationSite unexport exception: " + e.getMessage ());
        e.printStackTrace ();
        System.exit (1);
        }

        if (shutdownDone)
            GenericIO.writelnString ("ConcentrationSite was shutdown!");

    }

    /**
     *  Close of operations.
     */

    public static void shutdown ()
    {
        end = true;
        try
        { synchronized (Class.forName ("serverSide.main.ServerConcentrationSite"))
            { (Class.forName ("serverSide.main.ServerConcentrationSite")).notify ();
            }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type ServerConcentrationSite was not found (waking up)!");
        e.printStackTrace ();
        System.exit (1);
        }
    }

}