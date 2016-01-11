import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.HashMap;
import java.net.*;

public class Client extends UnicastRemoteObject implements Client_itf {
	
	private static HashMap<Integer,SharedObject_itf> correspondances;
	
	private static Server_itf serveur;
	private static Client_itf client;

	public Client() throws RemoteException {
		super();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		correspondances = new HashMap<Integer,SharedObject_itf>();
		// à compléter
	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) throws RemoteException {
		int id = serveur.lookup(name);
		return (SharedObject) correspondances.get(id);
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) throws RemoteException {
		serveur.register(name,((SharedObject) so).getID());
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		SharedObject so = null;
		try {
			int idso = serveur.create(0);
			so = new SharedObject(o,idso);
			correspondances.put(idso,so);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return so;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		Object o = null;
		try {
			o = serveur.lock_read(id, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return o;
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
		Object o = null;
		try {
			o = serveur.lock_write(id, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return o;
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		return ((SharedObject) correspondances.get(id)).reduce_lock();
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		((SharedObject) correspondances.get(id)).invalidate_reader();
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		return ((SharedObject) correspondances.get(id)).invalidate_writer();
	}
}
