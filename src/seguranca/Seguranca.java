package seguranca;

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

import utils.Arquivos;

public class Seguranca {
	
	private KeyPairGenerator assimetrica;
	private PrivateKey chavePrivada;
	private PublicKey chavePublica;
	private PublicKey chavePublicaDestinatario;
	private SecretKey chaveSimetrica;
	
	private Sessao sessao;  
	
	public Seguranca(){
		
	}
	
	public void gerarChaves() {				
		gerarChaveAssimetrica();
//		gerarChaveSimetrica();		
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
		setChavePrivada(keyPair.getPrivate());
		chavePublica = keyPair.getPublic();

		System.out.println("Chaves publica e privadas criadas com sucesso!");
	}
	
	public void salvarChavePublica() {
		Arquivos.gravarObjeto(chavePublica, "publica");
		System.out.println("Chave pública armazenada!");
	}
	
	public void salvarChavePrivada() {
		Arquivos.gravarObjeto(chavePrivada, "privada");
		System.out.println("Chave privada armazenada!");
	}
	
	public void obterPublicaDestinatario() {		
		chavePublicaDestinatario = (PublicKey) Arquivos.lerObjeto();
		System.out.println("Chave pública lida com sucesso no cliente!");				
	}
	
	public void chavePrivada() {		
		chavePrivada = (PrivateKey) Arquivos.lerObjetoPrivada();
		System.out.println("Chave privada lida com sucesso no cliente!");				
	}
	
	/**
	 * Criptografa o texto puro usando a chave p�blica.
	 */
	public byte[] criptografa(byte[] texto, PublicKey chave) {
		byte[] cipherText = null;

		try {
			Cipher cipher = Cipher.getInstance("RSA");

			// Criptografa o texto puro usando a chave Pública
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
	public byte[] decriptografa(byte[] texto, PrivateKey chave) {
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
	
	public void criarChaveSessao() throws NoSuchAlgorithmException{
		sessao = new Sessao();
		
		KeyGenerator keyGenerator1 = KeyGenerator.getInstance("AES");
		keyGenerator1.init(256, new SecureRandom());
		sessao.setChaveEncriptacaoServer(keyGenerator1.generateKey());

		KeyGenerator keyGenerator2 = KeyGenerator.getInstance("AES");
		keyGenerator2.init(256, new SecureRandom());
		sessao.setChaveEncriptacaoClient(keyGenerator2.generateKey());
		
		KeyGenerator keyGenerator3 = KeyGenerator.getInstance("AES");
		keyGenerator3.init(256, new SecureRandom());
		sessao.setChaveAutenticacaoServer(keyGenerator3.generateKey());
		
		KeyGenerator keyGenerator4 = KeyGenerator.getInstance("AES");
		keyGenerator4.init(256, new SecureRandom());
		sessao.setChaveAutenticacaoClient(keyGenerator4.generateKey());
	}

	public PrivateKey getChavePrivada() {
		return this.chavePrivada;
	}

	public void setChavePrivada(PrivateKey chavePrivada) {
		this.chavePrivada = chavePrivada;
	}

	public KeyPairGenerator getAssimetrica() {
		return assimetrica;
	}

	public void setAssimetrica(KeyPairGenerator assimetrica) {
		this.assimetrica = assimetrica;
	}

	public PublicKey getChavePublica() {
		return chavePublica;
	}

	public void setChavePublica(PublicKey chavePublica) {
		this.chavePublica = chavePublica;
	}

	public PublicKey getChavePublicaDestinatario() {
		return chavePublicaDestinatario;
	}

	public void setChavePublicaDestinatario(PublicKey chavePublicaDestinatario) {
		this.chavePublicaDestinatario = chavePublicaDestinatario;
	}

	public SecretKey getChaveSimetrica() {
		return chaveSimetrica;
	}

	public void setChaveSimetrica(SecretKey chaveSimetrica) {
		this.chaveSimetrica = chaveSimetrica;
	}

	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	
	/*private void gerarChaveSimetrica() {
	try {
		KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
		chaveSimetrica = keygenerator.generateKey();
		System.out.println("Chave simetrica criada com sucesso!");
	} catch (NoSuchAlgorithmException e) {		
		e.printStackTrace();
	}
}*/
	
}