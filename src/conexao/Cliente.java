package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.swing.plaf.synth.SynthSeparatorUI;

import jogo.Jogador;
import seguranca.Seguranca;
import utils.Conversor;

public class Cliente {

	private String host;
	private int porta;
	private Socket socket;
	//private PrintWriter out;
	//private BufferedReader in;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	//Seguran�a
	private Seguranca seguranca;	

	private Comunicacao comunicacao;

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

			//seguranca.gerarChaves();
			seguranca.obterPublicaDestinatario();
			seguranca.criarChaveSessao();

			comunicacao =  new Comunicacao(socket, seguranca);
			comunicacao.enviarSessao();

			//out = new ObjectOutputStream(socket.getOutputStream());
			//in = new ObjectInputStream(socket.getInputStream());


		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		}
	}

	public void enviar(Jogador j) {		
		comunicacao.enviarObjetoCliente(j);
	}

	public Jogador receber() {

		return (Jogador) comunicacao.receberObjetoServidor();

	}

}