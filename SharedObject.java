import java.io.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SharedObject implements Serializable, SharedObject_itf {

	private int id;

	public Object objet;

	private static final int NL = 0;
	private static final int RLC = 1;
	private static final int WLC = 2;
	private static final int RLT = 3;
	private static final int WLT = 4;
	private static final int RLT_WLC = 5;
	private int lock;
	
	private Client client;

	public SharedObject (Object o, int id) {
		this.objet = o;
		this.id = id;
		this.lock = NL;
	}

	public int getID() {
		return this.id;
	}

	public int getLockType() {
		return this.lock;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
		switch(this.lock) {
		case NL :
			this.lock = RLT;
			this.objet = Client.lock_read(this.id);
		break;
		case RLC :
			this.lock = RLT;
		break;
		case WLC :
			this.lock = RLT_WLC;
		break;
		}
	}

	// invoked by the user program on the client node
	public void lock_write() {
		switch(this.lock) {
		case NL :
			this.lock = WLT;
			this.objet = Client.lock_write(this.id);
		break;
		case RLC :
			this.lock = WLT;
			this.objet = Client.lock_write(this.id);
		break;
		case WLC :
			this.lock = WLT;
		break;
		}
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch(this.lock) {
		case RLT :
			this.lock = RLC;
		break;
		case WLT :
			this.lock = WLC;
		break;
		case RLT_WLC :
			this.lock = WLC;
		break;
		}
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		while(lock == WLT || lock == RLT_WLC) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock = RLC;
		return this.obj;
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		while(lock == RLT) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
		lock = NL;
	}

	public synchronized Object invalidate_writer() {
		while(lock == RLT_WLC || lock == WLT) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock = NL;
		return this.obj;
	}
}
