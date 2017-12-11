package criptografia;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

public class TrocaMensagem {
	
	public static PrivateKey chavePrivada;
	public static PublicKey chavePublica;
	
	public void createKeysServidor() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		KeyPairGenerator assimetrica = null;
		
		try {
			assimetrica = KeyPairGenerator.getInstance("RSA", "BC");
			assimetrica.initialize(1024, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KeyPair keyPair = assimetrica.generateKeyPair();
		chavePrivada = keyPair.getPrivate();
		chavePublica = keyPair.getPublic();

		System.out.println("Chaves publica e privadas criadas com sucesso!");
	}
	
	public static void main(String[] args) {
		TrocaMensagem tm = new TrocaMensagem();
		tm.createKeysServidor();
		System.out.println("CHAVE PRIVADA: " + tm.chavePrivada);
		System.out.println("CHAVE PUBLICA: " + tm.chavePublica);
	}
	
}
