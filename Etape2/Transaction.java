import java.util.HashMap;

public class Transaction {

	private static Transaction transaction;
	
    private boolean active;
	
	private HashMap<Integer,SharedObject> objetsaccedes;
	
	public Transaction() {
		transaction = this;
		this.active = false;
		this.objetsaccedes = new HashMap<Integer,SharedObject>();
	}
	
	public HashMap<Integer,SharedObject> getObjetsAccedes() {
		return this.objetsaccedes;
	}
	
	public static Transaction getCurrentTransaction() {
		return transaction;
	}

	// indique si l'appelant est en mode transactionnel
	public boolean isActive() {
		return this.active;
	}
	
	// demarre une transaction (passe en mode transactionnel)
	public void start() {
		this.active = true;
	}
	
	// termine une transaction et passe en mode non transactionnel
	public boolean commit() {
		this.objetsaccedes.clear();
		this.active = false;
		return true;
	}
		
	// abandonne et annule une transaction (et passe en mode non transactionnel)
	public void abort() {
		for(Integer i : this.objetsaccedes.keySet()) {
            ((SharedObject) Client.getCorrespondances().get(i)).obj = this.objetsaccedes.get(i);
        }
		this.objetsaccedes.clear();
		this.active = false;
	}
    
    public static void main(String[] args){
        Transaction t = new Transaction();
    }
	
}