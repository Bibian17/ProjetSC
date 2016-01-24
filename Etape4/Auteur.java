public class Auteur implements java.io.Serializable {

	private String data;
	
	private Sentence_itf sentence;
	
	public Auteur(String s, Sentnce_itf se) {
		data = s;
		Sentence_itf = se;
	}
	
	public void write(String s) {
		this.data = s;
	}
	
	public String read() {
		return this.data;	
	}
	
	public Sentence_itf getSentence() {
		return this.sentence
	}
	
	public void setSentence(Sentence_itf s) {
		this.sentence = s;
	}
	
}