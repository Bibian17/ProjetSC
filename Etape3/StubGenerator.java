import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import javax.tools.JavaCompiler;

public class StubGenerator {

	private HashMap<String,Class<?>> classes;
	
	private final static File repCourant = new File(new File("").getAbsolutePath());
	
	public StubGenerator() {
		this.classes = new HashMap<String,Class<?>>();
	}
	
	public static void main(String[] args) throws IOException {
		ArrayList<Class<?>> classesSerializables = getClassesSerializables();
		for (Class<?> c : classesSerializables) {
			creerStub(c.getName());
		}
	}
	
	private static ArrayList<Class<?>> getClassesSerializables() {
		ArrayList<Class<?>> classesSerializables = new ArrayList<Class<?>>();
		String[] listeFichiers = repCourant.list();
		for (String s : listeFichiers) {
			if (s.endsWith(".java")) {
				try {
					Class<?> classeJava = Class.forName(s.substring(0,s.length-5));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				Class<?>[] interfacesImplementees = classeJava.getInterfaces();
				for (Class<?> c : interfacesImplementees) {
					if (c.getName().equals("java.io.Serializable");
					this.classesSerializables.add(classeJava);
				}
			}
		}
		return classesSerializables;
	}
	
	private static void creerStub(String nom) {
		try {
			File f = new File(nom + "_stub.java");
			BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			String s = creerCode(nom);
			writer.write(s);
			writer.close();
			JavaCompiler compilateur = ToolProvider.getSystemJavaCompiler();
			unCompilateur.run(null, null, null, f.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Sting creerCode (String nom) {
		
	}
	
}