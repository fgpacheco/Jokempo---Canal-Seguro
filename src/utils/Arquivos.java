package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Arquivos {

	public static final String PATH_PUBLIC = "keys/public.key";

	public static final String PATH_PRIVATE = "keys/private.key";


	public static void gravarObjeto(Object obj, String tipo) {

		ObjectOutputStream out;
		try {
			if(tipo.equals("publica"))
				out = new ObjectOutputStream(new FileOutputStream(PATH_PUBLIC));
			else
				out = new ObjectOutputStream(new FileOutputStream(PATH_PRIVATE));

			out.writeObject(obj);
		} catch (IOException e) {			
			e.printStackTrace();
		}

	}

	public static Object lerObjeto() {

		ObjectInputStream in = null;
		try {	
			in = new ObjectInputStream(new FileInputStream(PATH_PUBLIC));
			return in.readObject();			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}

		return in;			
	}

	public static Object lerObjetoPrivada() {

		ObjectInputStream in = null;
		try {	
			in = new ObjectInputStream(new FileInputStream(PATH_PRIVATE));
			return in.readObject();			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}

		return in;			
	}

	public static boolean isChavePublica() {
		File f = new File(PATH_PUBLIC);
		if(f.isFile())
			return true;

		return false;
	}

}
