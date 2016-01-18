import java.rmi.RemoteException;
import java.util.ArrayList;

public class ServerObject implements ServerObject_itf {
	
	private Object objet;
	private int id;
	
	private static final int NL = 0;
	private static final int RL = 1;
	private static final int WL = 2;
	private int lock;
	
	private ArrayList<Client_itf> lecteurs;
	private Client_itf redacteur;
	
	public ServerObject(Object o, int id) {
		this.objet = o;
		this.id = id;
		this.lecteurs = new ArrayList<Client_itf>();
		this.redacteur = null;
		this.lock = 0;
	}
	
	public synchronized Object lock_read(Client_itf client) {
		try {
			if (this.lock==WL) {
				this.objet = this.redacteur.reduce_lock(this.id);
				this.lecteurs.add(this.redacteur);
				this.redacteur=null;
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.lock = RL;
		this.lecteurs.add(client);
		return this.objet;
	}

	public synchronized Object lock_write(Client_itf client) {
		switch (this.lock) {
		case NL :
			
		break;
		case RL : 
			for (Client_itf c : this.lecteurs) {
				try {
					c.invalidate_reader(this.id);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			this.lecteurs.clear();
		break;
		case WL : 
			try {
				this.objet = redacteur.invalidate_writer(this.id);
				this.redacteur = null;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		break;
		}
		this.lock = 2;
		this.redacteur = client;;
		return this.objet;
	}
	
}
