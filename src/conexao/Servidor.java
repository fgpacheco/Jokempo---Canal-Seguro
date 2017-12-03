package conexao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jogo.Partida;
import seguranca.Seguranca;
import utils.Arquivos;
import jogo.Jogador;

public class Servidor {
	
	public static final int PORTA = 5000;
	private ServerSocket serverSocket;
	private Socket socket;
	private ExecutorService executor;
	private int contador = 0;
	private Partida partida;
	
	//Segurança
	private Seguranca seguranca;
	
	//Servirá como monitor
	public Object trava = new Object();
	
	public Servidor() {
		try {
			serverSocket = new ServerSocket(PORTA);
			executor = Executors.newFixedThreadPool(2);		
			partida = new Partida();
			seguranca = new Seguranca();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void start() {
		System.out.println("Aguardando o cliente...");	
		seguranca.gerarChaves();
		
		if(!Arquivos.isChavePublica())
			seguranca.salvarChavePublica();
		
		
		while(true) {
			try {
				socket = serverSocket.accept();				
				System.out.println("Cliente conectado");
				
				executor.execute(new ServidorThread(socket, contador, this));
				contador++;
				
			} catch (IOException e) {				
				e.printStackTrace();
			}				
		}		
	}

	public Partida getPartida() {
		return partida;
	}

	public void setPartida(Partida partida) {
		this.partida = partida;
	}
	
	public Jogador resultado() {		
		return partida.vencedor();	
	}
	
	
	
}
