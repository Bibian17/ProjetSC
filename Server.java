public class Server extends UnicastRemoteObject implements Server_itf{
	
	private HashMap<String,Integer> nomid;
	
	public Server() {
		super();
		this.nomid = new HashMap<String,Integer>();
	}

	public int lookup(String name) throws java.rmi.RemoteException {
		return nomid.get(name);
	}

	public void register(String name, int id) throws java.rmi.RemoteException {
		this.nomid.put(name,id);
	}

	public int create(Object o) throws java.rmi.RemoteException {
		
	}

	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException {
		
	}

	public Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException {
		
	}

}
