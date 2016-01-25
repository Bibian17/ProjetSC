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
		try {
			client = new Client();
			serveur = (Server_itf) Naming.lookup("//localhost:5000/Server");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) {
		SharedObject so = null;
		try {
			int id = serveur.lookup(name);
			System.out.println("lookup" + id);
			if (id!=-1) {
				if (correspondances.containsKey(id)) {
					so = (SharedObject) correspondances.get(id);
				} else {
					so = new SharedObject(null,id);
					correspondances.put(id,so);
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return so;
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		try {
			int idso = ((SharedObject) so).getID();
			serveur.register(name,idso);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		SharedObject so = null;
		try {
			int idso = serveur.create(o);
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
			System.out.println("lock_read");
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
			System.out.println("lock_write");
			o = serveur.lock_write(id, client);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return o;
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		System.out.println("reduce_lock");
		return ((SharedObject) correspondances.get(id)).reduce_lock();
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		System.out.println("invalidate_reader");
		((SharedObject) correspondances.get(id)).invalidate_reader();
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		System.out.println("invalidate_writer");
		return ((SharedObject) correspondances.get(id)).invalidate_writer();
	}
}
