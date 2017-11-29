package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

import jogo.Jogador;

public class ServidorThread implements Runnable{

	private Socket socket;
	private int contador;	
	private Servidor servidor;

	public ServidorThread(Socket socket, int contador, Servidor servidor) {
		this.socket = socket;
		this.contador = contador;
		this.servidor = servidor;
	}

	@Override
	public void run() {
		BufferedReader in;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String msg = null;
			Jogador jogador;

			while((msg = in.readLine()) != null) {				
				jogador = Jogador.convertFromString(msg);
				
				synchronized (this) {					
					servidor.getPartida().add(jogador);

					if(servidor.getPartida().getJogadores().size() == 2) {					
						notifyAll();					
					} else {					
						wait();
					}
				}
				System.out.println("aaa" + jogador);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
