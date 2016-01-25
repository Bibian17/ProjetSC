import java.awt.*;
import java.awt.event.*;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import java.rmi.registry.*;

public class TestEtape4 extends Frame {
	public TextArea text;
	public TextField data1;
	public TextField data2;
	private static Sentence_itf sentence;
	private static Auteur_itf auteur;

	public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java TestEtape4 <name>");
			return;
		}
		
		// initialize the system
		Client.init();
		
		sentence = (Sentence_itf) Client.create(new Sentence());
		/*sentence = (Sentence_itf) Client.lookup("Sentence");
		if (sentence == null) {
			sentence = (Sentence_itf) Client.create(new Sentence());
			Client.register("Sentence", sentence);
		}*/
		auteur = (Auteur_itf) Client.lookup("Auteur");
		if (auteur == null) {
			auteur = (Auteur_itf) Client.create(new Auteur(argv[0],sentence));
			Client.register("Auteur", auteur);
		}
        // create the graphical part
        new TestEtape4();
	}

	public static Auteur_itf getAuteur() {
		return auteur;
	}

	public static Sentence_itf getSentence() {
		return sentence;
	}

	public TestEtape4() {
	
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);
	
		data1=new TextField(20);
		add(data1);
		data2=new TextField(20);
		add(data2);
	
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListenerTest(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListenerTest(this));
		add(read_button);
		
		setSize(500,400);
		text.setBackground(Color.black); 
		show();
	}

}

class readListenerTest implements ActionListener {

	TestEtape4 te4;
	public readListenerTest (TestEtape4 t) {
		te4 = t;
	}
	
	public void actionPerformed (ActionEvent e) {
	
        try{
			Auteur_itf so1 = TestEtape4.getAuteur();
			Sentence_itf so2 = TestEtape4.getSentence();
			so1.lock_read();
			String s1 = so1.read();
			so1.unlock();
			te4.text.append("Auteur : " + s1 + " ; ");
			so2.lock_read();
			String s2 = so2.read();
			so2.unlock();
			te4.text.append("Sentence : " + s2 + "\n");
			te4.data1.setText("");
			te4.data2.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
	}
	
}

class writeListenerTest implements ActionListener {

	TestEtape4 te4;
	public writeListenerTest (TestEtape4 t) {
		te4 = t;
	}
	
	public void actionPerformed (ActionEvent e) {
		
		try {
			String s1 = te4.data1.getText();
			Auteur_itf so1 = TestEtape4.getAuteur();
			so1.lock_write();
			so1.write(s1);
			so1.unlock();
			te4.data1.setText("");
			String s2 = te4.data2.getText();
			Sentence_itf so2 = TestEtape4.getSentence();
			so2.lock_write();
			so2.write(s2);
			so2.unlock();
			te4.data2.setText("");
		} catch (Exception ex) {
            ex.printStackTrace();
        }
	}
		
}
