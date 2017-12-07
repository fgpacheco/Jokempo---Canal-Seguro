package conexao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import jogo.Jogador;
import seguranca.Seguranca;
import utils.Conversor;

public class Comunicacao {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Seguranca seguranca;
	
	public Comunicacao(Socket socket, Seguranca seguranca) {
		try {
			this.socket = socket;
			this.seguranca = seguranca;			
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarChaveSimetrica() {
		try {
			byte[] ciphertext = seguranca.criptografa(seguranca.getSessao().getChaveEncriptacaoClient().getEncoded(), seguranca.getChavePublicaDestinatario());			
			output.writeObject(Conversor.byteArrayToString(ciphertext));
	
			byte[] ciphertext1 = seguranca.criptografa(seguranca.getSessao().getChaveEncriptacaoServer().getEncoded(), seguranca.getChavePublicaDestinatario());			
			output.writeObject(Conversor.byteArrayToString(ciphertext1));
			
			byte[] ciphertext2 = seguranca.criptografa(seguranca.getSessao().getChaveAutenticacaoClient().getEncoded(), seguranca.getChavePublicaDestinatario());			
			output.writeObject(Conversor.byteArrayToString(ciphertext2));
			
			byte[] ciphertext3 = seguranca.criptografa(seguranca.getSessao().getChaveAutenticacaoServer().getEncoded(), seguranca.getChavePublicaDestinatario());			
			output.writeObject(Conversor.byteArrayToString(ciphertext3));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SecretKey receberChaveSimetrica() {
		try {
			String ba = (String) input.readObject();
			byte [] ba1 = Conversor.stringToByteArray(ba);
			byte[] decryptedText = seguranca.decriptografa(ba1, seguranca.getChavePrivada());
			seguranca.setChaveSimetrica(new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES"));

			System.out.println("Chave simetrica do destinatario recebida com sucesso!");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return seguranca.getChaveSimetrica();
	}
	
	public void enviarObjeto(Object obj, boolean client) {
		try {
			byte[] b = Conversor.convertToByteArray(obj);
			byte[] b1;

			if(client) {
				output.writeObject(seguranca.getSessao().getChaveAutenticacaoClient());
				b1 = seguranca.criptografaSimetrica(b, seguranca.getSessao().getChaveEncriptacaoClient());
				output.writeObject(Conversor.byteArrayToString(b1));
			} else {
				output.writeObject(seguranca.getSessao().getChaveAutenticacaoServer());
				b1 = seguranca.criptografaSimetrica(b, seguranca.getSessao().getChaveEncriptacaoServer());
				output.writeObject(Conversor.byteArrayToString(b1));
			}
			
			//output.writeObject(Conversor.byteArrayToString(b1));
			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (DataLengthException e) {			
			e.printStackTrace();
		} 		
	}
	
	public Object receberObjeto(boolean client) {
		try {
			String s = (String) input.readObject(); 
			byte[] b = Conversor.stringToByteArray(s);
			byte[] b1;
			if(client) {				
				b1 = seguranca.decriptografiaSimetrica(b, seguranca.getSessao().getChaveEncriptacaoClient());				
			} else {
				b1 = seguranca.decriptografiaSimetrica(b, seguranca.getSessao().getChaveEncriptacaoServer());
			}
			
			return Conversor.convertFromByteArray(b1);


		} catch (ClassNotFoundException | IOException e) {		
			e.printStackTrace();
		}
		return null;
	}

	public SecretKey receberKeyAuth() {
		try {	
			SecretKey k = (SecretKey) input.readObject(); 
			return k;
			
		} catch (ClassNotFoundException | IOException e) {		
			e.printStackTrace();
		}
		return null;
	}
	
}