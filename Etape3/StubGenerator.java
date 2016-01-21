import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.Parameter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

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
		System.out.println(System.getProperty("user.dir"));
		for (String s : listeFichiers) {
			if (s.endsWith(".java")) {
				Class<?> classeJava = null;
				try {
					classeJava = Class.forName(s.substring(0,s.length()-5));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				Type[] interfacesImplementees = classeJava.getGenericInterfaces();
				for (Type c : interfacesImplementees) {
					if (c.getTypeName().equals("java.io.Serializable")) {
						if (!classeJava.getName().equals("SharedObject")) {
							classesSerializables.add(classeJava);
						}
					}
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
			compilateur.run(null, null, null, f.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String creerCode (String nom) {
		String s = "";
		try {
			s = s.concat("public class " + nom + "_stub extends SharedObject implements " + nom + "_itf {\n");
			s = s.concat("\tpublic " + nom + "_stub(Object o,int i) {\n");
			s = s.concat("\t\tsuper(o,i);\n");
			s = s.concat("\t}\n");
			Class<?> c = Class.forName(nom);
			Method[] methodes = c.getDeclaredMethods();
			for (Method m : methodes) {
				int modifieurs = m.getModifiers();
				if (Modifier.isPublic(modifieurs)) {
					s = s.concat("\tpublic ");
				} else if (Modifier.isProtected(modifieurs)) {
					s = s.concat("\tprotected ");
				} else if (Modifier.isPrivate(modifieurs)) {
					s = s.concat("\tprivate ");
				}
				if (Modifier.isAbstract(modifieurs)) {
					s = s.concat("abstract ");
				}

				if (Modifier.isFinal(modifieurs)) {
					s = s.concat("final ");
				}

				if (Modifier.isStatic(modifieurs)) {
					s = s.concat("static ");
				}
				Class<?> typeRetour = m.getReturnType();
				s = s.concat(typeRetour.getName() + " " + m.getName() + "(");
				Parameter[] parametres = m.getParameters();
				if (parametres.length!=0) {
					for (Parameter p : parametres) {
						s = s.concat(p.getType().getName() + " " + p.getName() + ",");
					}
					s = s.substring(0,s.length()-1);
				}
				s = s.concat(") {\n\t\t");
				if (!typeRetour.getName().equals("void")) {
					s = s.concat(typeRetour.getName() + " res = ");
				}
				s = s.concat("((" + nom + ")this.obj)." + m.getName() + "(");
				if (parametres.length!=0) {
					for (Parameter p : parametres) {
						s = s.concat(p.getName() + ",");
					}
					s = s.substring(0,s.length()-1);
				}
				s = s.concat(");\n");
				if (!typeRetour.getName().equals("void")) {
					s = s.concat("\t\treturn res;\n");
				}
				s = s.concat("\t}\n");
			}
			s = s.concat("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
}
