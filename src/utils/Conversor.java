package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Conversor {
	
	/*
	 *ConvertTo 
	 */
	public static String convertToString(Object obj) {		
		byte[] objeto = writeOutputStream(obj);
		String str = Base64.encode(objeto);
		
		return str;
	}
	
	public static byte[] convertToByteArray(Object obj) {
		return writeOutputStream(obj);
	}
	
	private static byte[] writeOutputStream(Object obj) {		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();	
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			byte[] bs = baos.toByteArray();
			out.close();
			return bs;
		} catch (IOException e) {			
			e.printStackTrace();
		}		
		return null;
	}
	
	
	/*
	 * ConvertFrom 
	 */
	public static Object convertFromString(String str) {
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(str));
		return readInputStream(bais);		
	}

	public static Object convertFromByteArray(byte[] bs) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bs);
		return readInputStream(bais);	
	}

	private static Object readInputStream(ByteArrayInputStream bais) {
		try {
			ObjectInputStream in = new ObjectInputStream(bais);
			return in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;				
	}
	
	/*
	 *ByteArrayToString 
	 */
	public static String byteArrayToString(byte[] bs) {		
		String str = Base64.encode(bs);		
		return str;
	}
	
	public static  byte[] stringToByteArray(String str) {
		byte[] bs = Base64.decode(str);
		return bs;		
	}

}
