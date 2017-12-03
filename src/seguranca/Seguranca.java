package seguranca;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import utils.Arquivos;

public class Seguranca {
	
	private KeyPairGenerator assimetrica;
	private PrivateKey chavePrivada;
	private PublicKey chavePublica;
	private PublicKey chavePublicaDestinatario;
	private SecretKey chaveSimetrica;	
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public void gerarChaves() {				
		gerarChaveAssimetrica();
		gerarChaveSimetrica();		
	}
	
	private void gerarChaveAssimetrica() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		try {
			assimetrica = KeyPairGenerator.getInstance("RSA", "BC");
			assimetrica.initialize(1024, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		} catch (NoSuchProviderException e) {			
			e.printStackTrace();
		}
		KeyPair keyPair = assimetrica.generateKeyPair();
		chavePrivada = keyPair.getPrivate();
		chavePublica = keyPair.getPublic();

		System.out.println("Chaves publica e privadas criadas com sucesso!");
	}
	
	private void gerarChaveSimetrica() {
		try {
			KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
			chaveSimetrica = keygenerator.generateKey();

			System.out.println("Chave simetrica criada com sucesso!");
		} catch (NoSuchAlgorithmException e) {		
			e.printStackTrace();
		}
	}
	
	public void salvarChavePublica() {
		Arquivos.gravarObjeto(chavePublica);
		System.out.println("Chave pública armazenada!");
	}
	
	public void obterPublicaDestinatario() {		
		chavePublicaDestinatario = (PublicKey) Arquivos.lerObjeto();
		System.out.println("Chave pública lida com sucesso no cliente!");				
	}
	
	public byte[] enviarChaveSimetrica() {
		byte[] ciphertext = criptografa(chaveSimetrica.getEncoded(), chavePublicaDestinatario);
		return ciphertext;
		/*
		try {
			byte[] ciphertext = criptografa(chaveSimetrica.getEncoded(), chavePublicaDestinatario);
			output.writeObject(ciphertext);

			System.out.println("Chave simétrica enviada...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public void receberChaveSimetrica() {
		try {
			byte[] ba = (byte[]) input.readObject();
			byte[] decryptedText = decriptografa(ba, chavePrivada);

			chaveSimetrica = new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES");
			System.out.println("Chave simetrica do destinatario recebida com sucesso!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	
	/**
	 * Criptografa o texto puro usando a chave pública.
	 */
	private byte[] criptografa(byte[] texto, PublicKey chave) {
		byte[] cipherText = null;

		try {
			Cipher cipher = Cipher.getInstance("RSA");

			// Criptografa o texto puro usando a chave PÃºblica
			cipher.init(Cipher.ENCRYPT_MODE, chave);
			cipherText = cipher.doFinal(texto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}

	/**
	 * Decriptografa o texto puro usando a chave privada.
	 */
	private byte[] decriptografa(byte[] texto, PrivateKey chave) {
		byte[] dectyptedText = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");

			// Decriptografa o texto puro usando a chave Privada
			cipher.init(Cipher.DECRYPT_MODE, chave);
			dectyptedText = cipher.doFinal(texto);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return dectyptedText;
	}	

}
