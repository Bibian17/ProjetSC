public class Entier implements java.io.Serializable {

	private int data;
	
	public Entier(int i) {
		data = i;
	}
	
	public void write(int i) {
		data = i;
	}
	
	public int read() {
		return data;	
	}
	
}