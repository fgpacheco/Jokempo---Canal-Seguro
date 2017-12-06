package conexao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
	
			/*byte[] ciphertext1 = seguranca.criptografa(seguranca.getSessao().getChaveEncriptacaoServer().getEncoded(), seguranca.getChavePublicaDestinatario());			
			output.writeObject(Conversor.byteArrayToString(ciphertext1));
			byte[] ciphertext2 = seguranca.criptografa(seguranca.getSessao().getChaveAutenticacaoClient().getEncoded(), seguranca.getChavePublicaDestinatario());			
			output.writeObject(Conversor.byteArrayToString(ciphertext2));
			byte[] ciphertext3 = seguranca.criptografa(seguranca.getSessao().getChaveAutenticacaoServer().getEncoded(), seguranca.getChavePublicaDestinatario());			
			output.writeObject(Conversor.byteArrayToString(ciphertext3));*/
			
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
	
	public void enviarObjeto(Object obj) {
		try {
			output.writeObject(obj);
		} catch (IOException e) {			
			e.printStackTrace();
		}		
	}
	
	public Object receberObjeto() {
		try {
			return input.readObject();
		} catch (ClassNotFoundException | IOException e) {		
			e.printStackTrace();
		}
		return null;
	}
	
}