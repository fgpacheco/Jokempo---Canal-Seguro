package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import seguranca.Seguranca;
import utils.Conversor;

public class Cliente {

	private String host;
	private int porta;
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	//Segurança
	private Seguranca seguranca;	

	public Cliente(String host, int porta) {
		this.host = host;
		this.porta = porta; 
		seguranca = new Seguranca();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPorta() {
		return porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	public void conectar() {
		try {
			socket = new Socket(host, porta);   
			System.out.println("Cliente conectado!");
			
//			seguranca = new Seguranca(socket);
			seguranca.obterPublicaDestinatario();
			seguranca.criarChaveSessao();
			seguranca.enviarChaveSimetrica();
//			System.out.println(seguranca.getChaveAutenticacaoServer());
//			System.out.println(seguranca.getChaveAutenticacaoClient());

			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			/*
			String s = Conversor.byteArrayToString(seguranca.enviarChaveSimetrica());
			System.out.println("Chave Simétrica: " + s);
			enviar(s);
			*/			

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enviar(String msg) {
		out.println(msg);
	}

	public String receber() throws IOException {
		return in.readLine();
	}

}