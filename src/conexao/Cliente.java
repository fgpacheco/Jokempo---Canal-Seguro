package conexao;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import jogo.Jogador;
import seguranca.Seguranca;


public class Cliente {

	private String host;
	private int porta;
	private Socket socket;
		
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

			seguranca.obterPublicaDestinatario();
			seguranca.criarChaveSessao();

			comunicacao =  new Comunicacao(socket, seguranca);
			comunicacao.enviarSessao();

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