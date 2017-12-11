package conexao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;

import jogo.Jogador;
import jogo.JogadorSent;
import seguranca.Seguranca;
import seguranca.Sessao;
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
			output = new ObjectOutputStream(this.socket.getOutputStream());
			input = new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void enviarSessao() {
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
	
	public Sessao receberSessao() {
		Sessao s = new Sessao();
		try {
			
			
			String ba = (String) input.readObject();
			byte [] ba1 = Conversor.stringToByteArray(ba);
			byte[] decryptedText = seguranca.decriptografa(ba1, seguranca.getChavePrivada());
			s.setChaveEncriptacaoClient(new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES"));
			
			
			ba = (String) input.readObject();
			ba1 = Conversor.stringToByteArray(ba);
			decryptedText = seguranca.decriptografa(ba1, seguranca.getChavePrivada());
			s.setChaveEncriptacaoServer(new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES"));
			
			ba = (String) input.readObject();
			ba1 = Conversor.stringToByteArray(ba);
			decryptedText = seguranca.decriptografa(ba1, seguranca.getChavePrivada());
			s.setChaveAutenticacaoClient(new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES"));
			
			
			ba = (String) input.readObject();
			ba1 = Conversor.stringToByteArray(ba);
			decryptedText = seguranca.decriptografa(ba1, seguranca.getChavePrivada());
			s.setChaveAutenticacaoServer(new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES"));
			

			System.out.println("Chave simetrica do destinatario recebida com sucesso!");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	/*public void enviarObjeto(Object obj, boolean client) {
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
	}*/
	
	public void enviarObjetoCliente(Object obj) {
		enviarObjeto(obj, seguranca.getSessao().getChaveAutenticacaoClient(),  seguranca.getSessao().getChaveEncriptacaoClient());
		
	}
	
	public void enviarObjetoServidor(Object obj) {
		enviarObjeto(obj, seguranca.getSessao().getChaveAutenticacaoServer(),  seguranca.getSessao().getChaveEncriptacaoServer());
		
	}
	
	public void enviarObjeto(Object obj, SecretKey autenticacao, SecretKey encriptacao) {
		try {
			byte[] b = Conversor.convertToByteArray(obj);			

			byte[] encrypt = seguranca.criptografaSimetrica(b, encriptacao);
			byte[] auth = seguranca.autenticacao(b, autenticacao);
			
			JogadorSent js = new JogadorSent(auth, encrypt);
			
			output.writeObject(js);
			
			//falta autenticar
			//Criar um objeto para encapsular os dois byte []			
			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (DataLengthException e) {			
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {			
			e.printStackTrace();
		} 		
	}
	
	public Object receberObjetoCliente() {
		return receberObjeto(seguranca.getSessao().getChaveAutenticacaoClient(), seguranca.getSessao().getChaveEncriptacaoClient());
	}
	
	public Object receberObjetoServidor() {
		return receberObjeto(seguranca.getSessao().getChaveAutenticacaoServer(), seguranca.getSessao().getChaveEncriptacaoServer());
	}
	
	public Object receberObjeto(SecretKey autenticacao, SecretKey encriptacao) {
		try {
			JogadorSent js = (JogadorSent) input.readObject();
			byte[] decrypt = seguranca.decriptografiaSimetrica(js.getEncriptacao(), encriptacao);
			Jogador j = (Jogador) Conversor.convertFromByteArray(decrypt);
			byte[] byteJogador = Conversor.convertToByteArray(j);
			
			byte[] auth = seguranca.autenticacao(byteJogador, autenticacao);
			
			if(Arrays.equals(auth, js.getAutenticacao())) {
				System.out.println("Autenticado.");
				return j;
			} else {
				System.out.println("Autenticação Falhou");
			}

		} catch (ClassNotFoundException | IOException e) {		
			e.printStackTrace();
		} catch (DataLengthException e) {			
			e.printStackTrace();
		} catch (InvalidCipherTextException e) {			
			e.printStackTrace();
		}
		
		return null;
	}
	
}