package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Arquivos {
	
	public static final String PATH = "keys/public.key";
	
	public static void gravarObjeto(Object obj) {
		
		ObjectOutputStream out;
		try {
			out = new ObjectOutputStream(new FileOutputStream(PATH));
			out.writeObject(obj);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}
	
	public static Object lerObjeto() {
		
		ObjectInputStream in = null;
		try {	
			in = new ObjectInputStream(new FileInputStream(PATH));
			return in.readObject();			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
		
		return in;			
	}
	
	public static boolean isChavePublica() {
		File f = new File(PATH);
		if(f.isFile())
			return true;
		
		return false;
	}
	
}
