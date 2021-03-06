package seguranca;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import utils.Arquivos;

public class Seguranca {
	private PrivateKey chavePrivada;
	private PublicKey chavePublica;
	private PublicKey chavePublicaDestinatario;
	private Sessao sessao;  
	
	public Seguranca(){
		
	}
	
	public void gerarChaves() {				
		gerarChaveAssimetrica();
//		gerarChaveSimetrica();		
	}
	
	private void gerarChaveAssimetrica() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		KeyPairGenerator assimetrica = null;

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
		System.out.println("Chave p�blica armazenada!");
	}
	
	public void salvarChavePrivada() {
		Arquivos.gravarObjeto(chavePrivada, "privada");
		System.out.println("Chave privada armazenada!");
	}
	
	public void obterPublicaDestinatario() {		
		chavePublicaDestinatario = (PublicKey) Arquivos.lerObjeto();
		System.out.println("Chave p�blica lida com sucesso no cliente!");				
	}
	
	public void chavePrivada() {		
		chavePrivada = (PrivateKey) Arquivos.lerObjetoPrivada();
		System.out.println("Chave privada lida com sucesso!");				
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
	
    public byte[] criptografaSimetrica(byte[] data, SecretKey secret) {
        try {
            return process(data, true, secret);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return null;
    }

    public byte[] decriptografiaSimetrica(byte[] data, SecretKey secret) {
        try {
            return process(data, false, secret);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return null;
    }
	
	private byte[] process(byte[] input, boolean encrypt, SecretKey secret) throws DataLengthException, IllegalStateException, InvalidCipherTextException {
        CBCBlockCipher cbcBlockCipher = new CBCBlockCipher(new AESEngine());
        BlockCipherPadding bcp = new PKCS7Padding();
        PaddedBufferedBlockCipher pbbc = new PaddedBufferedBlockCipher(cbcBlockCipher, bcp);
        System.out.println("CHAVE:  "+ secret);
        int blockSize = cbcBlockCipher.getBlockSize();
        int inputOffset = 0;
        int inputLength = input.length;
        int outputOffset = 0;

        byte[] iv = new byte[blockSize];
        if (encrypt) {
            //random.nextBytes(iv);
            outputOffset += blockSize;
        } else {
            System.arraycopy(input, 0, iv, 0, blockSize);
            inputOffset += blockSize;
            inputLength -= blockSize;
        }

        pbbc.init(encrypt, new ParametersWithIV(new KeyParameter(secret.getEncoded()), iv));
        byte[] output = new byte[pbbc.getOutputSize(inputLength) + outputOffset];

        if (encrypt) {
            System.arraycopy(iv, 0, output, 0, blockSize);
        }

        int outputLength = outputOffset + pbbc.processBytes(input, inputOffset, inputLength, output, outputOffset);
//        System.out.println("OUTPUT:  "+pbbc.doFinal(output, outputLength));
        outputLength += pbbc.doFinal(output, outputLength);

        byte[] out = new byte[outputLength];
        System.arraycopy(output, 0, out, 0, outputLength);

        return out;
    }
	
	public byte[] autenticacao(byte[] input, SecretKey secret) throws DataLengthException, InvalidCipherTextException {
		CBCBlockCipher cbcBlockCipher = new CBCBlockCipher(new AESEngine());
		//SecureRandom random = new SecureRandom();
		BlockCipherPadding bcp = new PKCS7Padding();
		PaddedBufferedBlockCipher pbbc = new PaddedBufferedBlockCipher(cbcBlockCipher, bcp);

		int blockSize = cbcBlockCipher.getBlockSize();
		int inputOffset = 0;
		int inputLength = input.length;
		int outputOffset = 0;

		byte[] iv = new byte[blockSize];
		outputOffset += blockSize;

		pbbc.init(true, new ParametersWithIV(new KeyParameter(secret.getEncoded()), iv));
		byte[] output = new byte[pbbc.getOutputSize(inputLength) + outputOffset];

		System.arraycopy(iv, 0, output, 0, blockSize);

		int outputLength = outputOffset + pbbc.processBytes(input, inputOffset, inputLength, output, outputOffset);

		outputLength += pbbc.doFinal(output, outputLength);

		//Pego apenas o último bloco
		byte[] out = new byte[blockSize];
		System.arraycopy(output, outputLength - blockSize, out, 0, blockSize);

		return out;
	}
	
	public void criarChaveSessao() throws NoSuchAlgorithmException{
		sessao = new Sessao();
		
		KeyGenerator keyGenerator1 = KeyGenerator.getInstance("AES");
		keyGenerator1.init(256, new SecureRandom());
		sessao.setChaveEncriptacaoServer(keyGenerator1.generateKey());
		System.out.println("ENC SERVER: "+sessao.getChaveEncriptacaoServer());
		
		sessao.setChaveEncriptacaoClient(keyGenerator1.generateKey());
		System.out.println("ENC CLIENT: "+sessao.getChaveEncriptacaoClient());
		
		sessao.setChaveAutenticacaoServer(keyGenerator1.generateKey());
		System.out.println("AUT SERVER: "+sessao.getChaveAutenticacaoServer());
		
		sessao.setChaveAutenticacaoClient(keyGenerator1.generateKey());
		System.out.println("AUT CLIENT: "+sessao.getChaveAutenticacaoClient());
	}

	public PrivateKey getChavePrivada() {
		return this.chavePrivada;
	}

	public void setChavePrivada(PrivateKey chavePrivada) {
		this.chavePrivada = chavePrivada;
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

	public Sessao getSessao() {
		return sessao;
	}

	public void setSessao(Sessao sessao) {
		this.sessao = sessao;
	}
	
	
	
	
	/*
	 * Métodos referentes ao certificado
	 * */
	 public PrivateKey getPrivateKeyFromCert(String path, String senha) {
	        PrivateKey pKey = null;
	        try {
	            KeyStore p12 = KeyStore.getInstance("pkcs12");
	            p12.load(new FileInputStream(path), senha.toCharArray());
	            Enumeration e = p12.aliases();
	            String alias = (String) e.nextElement();
	            pKey = (PrivateKey) p12.getKey(alias, senha.toCharArray());

	        } catch (KeyStoreException ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (IOException ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (NoSuchAlgorithmException ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (CertificateException ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (UnrecoverableKeyException ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        }

	        return pKey;
	    }

	    public PublicKey getPublicKeyFromCert(String path) {
	        PublicKey pk = null;
	        try {
	            FileInputStream fin = new FileInputStream(path);
	            CertificateFactory f = CertificateFactory.getInstance("X.509");
	            X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
	            pk = certificate.getPublicKey();
	        } catch (FileNotFoundException ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        } catch (CertificateException ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return pk;
	    }

	    public PublicKey getPublicKeyFromCert(File path) {
	        PublicKey pk = null;
	        try {
	            FileInputStream fin = new FileInputStream(path);
	            CertificateFactory f = CertificateFactory.getInstance("X.509");
	            X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
	            pk = certificate.getPublicKey();
	        } catch (Exception ex) {
	            Logger.getLogger(Security.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return pk;
	    }
	
	
	
	public static void main(String[] args) {
        Seguranca s = new Seguranca();
        PrivateKey privateKeyFromCert = s.getPrivateKeyFromCert("certificado/SERVIDORSEGURANCA-2017-12-13-010148.p12", "password");
        PublicKey publicKeyFromCert = s.getPublicKeyFromCert("certificado/PUBLIC_SERVIDORSEGURANCA-2017-12-13-010541.cer");

        String a = "texto1 normal a ser encriptadoáéíóLoÇô";
        System.out.println("NORMAL: " + a);

        byte[] criptografaAssimetrica = s.criptografa(a.getBytes(), publicKeyFromCert);
        System.out.println("ENCRIP: " + criptografaAssimetrica);

        byte[] decriptografiaAssimetrica = s.decriptografa(criptografaAssimetrica, privateKeyFromCert);
        System.out.println("DECRIP: " + new String(decriptografiaAssimetrica));
        
        
    }
}