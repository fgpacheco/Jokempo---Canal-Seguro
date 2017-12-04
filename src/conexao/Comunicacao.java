package conexao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.crypto.spec.SecretKeySpec;

import seguranca.Seguranca;

public class Comunicacao {
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Seguranca seguranca;
	
	public Comunicacao(Socket socket, Seguranca seguranca) {
		try {
			this.socket = socket;
			this.seguranca = seguranca;
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarChaveSimetrica() {
		try {
			byte[] ciphertext = seguranca.criptografa(seguranca.sessao.getChaveEncriptacaoClient().getEncoded(), seguranca.chavePublicaDestinatario);	
			output.writeObject(ciphertext);
			byte[] ciphertext1 = seguranca.criptografa(seguranca.sessao.getChaveEncriptacaoServer().getEncoded(), seguranca.chavePublicaDestinatario);	
			output.writeObject(ciphertext1);
			byte[] ciphertext2 = seguranca.criptografa(seguranca.sessao.getChaveAutenticacaoClient().getEncoded(), seguranca.chavePublicaDestinatario);	
			output.writeObject(ciphertext2);
			byte[] ciphertext3 = seguranca.criptografa(seguranca.sessao.getChaveAutenticacaoServer().getEncoded(), seguranca.chavePublicaDestinatario);	
			output.writeObject(ciphertext3);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void receberChaveSimetrica() {
		try {
			byte[] ba = (byte[]) input.readObject();
			byte[] decryptedText = seguranca.decriptografa(ba, seguranca.getChavePrivada());

			chaveSimetrica = new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES");
			System.out.println("Chave simetrica do destinatario recebida com sucesso!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
