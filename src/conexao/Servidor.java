package conexao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jogo.Partida;
import jogo.Jogador;

public class Servidor {
	
	public static final int PORTA = 5000;
	private ServerSocket serverSocket;
	private Socket socket;
	private ExecutorService executor;
	private int contador = 0;
	private Partida partida;
	public Object trava = new Object();
	
	public Servidor() {
		try {
			serverSocket = new ServerSocket(PORTA);
			executor = Executors.newFixedThreadPool(2);		
			partida = new Partida();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public void start() {
		System.out.println("Aguardando o cliente...");	
		
		while(true) {
			try {
				socket = serverSocket.accept();				
				System.out.println("Cliente conectado");
				
				executor.execute(new ServidorThread(socket, contador, this));
				contador++;				
				
				//new ServidorThread(socket).start();	
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
