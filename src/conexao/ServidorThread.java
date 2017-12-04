package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.sun.prism.paint.Stop;

import jogo.Jogador;
import utils.Conversor;

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
		PrintWriter out;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

			String msg = null;
			Jogador jogador;

			while((msg = in.readLine()) != null) {				
				jogador = (Jogador) Conversor.convertFromString(msg);
				System.out.println(jogador.getNome());
				
				synchronized (this.servidor) {					
					servidor.getPartida().add(jogador);

					if(servidor.getPartida().getJogadores().size() == 2) {					
						//this.servidor.trava.notifyAll();
						this.servidor.notifyAll();
					} else {					
						//this.servidor.trava.wait();
						this.servidor.wait();
					}
					
					jogador = servidor.resultado();
					msg = Conversor.convertToString(jogador);
					out.println(msg);
				}
			}
			

		} catch (IOException e) {			
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
