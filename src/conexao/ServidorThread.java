package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.sun.prism.paint.Stop;

import jogo.Jogador;
import seguranca.Seguranca;
import seguranca.Sessao;
import utils.Conversor;

public class ServidorThread implements Runnable{

	private Socket socket;
	private int contador;	
	private Servidor servidor;
	private Seguranca seguranca;
	private Comunicacao comunicacao;

	public ServidorThread(Socket socket, int contador, Servidor servidor, Seguranca seguranca) {
		this.socket = socket;
		this.contador = contador;
		this.servidor = servidor;
		this.seguranca = seguranca;
		this.comunicacao =  new Comunicacao(socket, seguranca);
	}

	@Override
	public void run() {
		ObjectInputStream in;
		ObjectOutputStream out;

		try {			
			//out = new ObjectOutputStream(socket.getOutputStream());
			//in = new ObjectInputStream(socket.getInputStream());

			seguranca.setSessao(new Sessao());
			/*System.out.println(comunicacao.receberChaveSimetrica());
			System.out.println(comunicacao.receberChaveSimetrica());
			System.out.println(comunicacao.receberChaveSimetrica());
			System.out.println(comunicacao.receberChaveSimetrica());*/

			seguranca.getSessao().setChaveEncriptacaoClient(comunicacao.receberChaveSimetrica());
			seguranca.getSessao().setChaveEncriptacaoServer(comunicacao.receberChaveSimetrica());
			seguranca.getSessao().setChaveAutenticacaoClient(comunicacao.receberChaveSimetrica());
			seguranca.getSessao().setChaveAutenticacaoServer(comunicacao.receberChaveSimetrica());


			//String msg = null;
			
			if(seguranca.getSessao().getChaveAutenticacaoClient().equals(comunicacao.receberKeyAuth())) {
				System.out.println("Mensagem autenticado");
				Jogador jogador = (Jogador) comunicacao.receberObjeto(true);//.readObject();

				while(jogador != null) {				
					//jogador = (Jogador) Conversor.convertFromString(msg);
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

						Jogador vencedor = servidor.resultado();
						comunicacao.enviarObjeto(vencedor, false);
						//msg = Conversor.convertToString(jogador);
						//out.println(msg);
						//out.writeObject(jogador);
						
					}

					jogador = (Jogador) comunicacao.receberObjeto(true);//.readObject();
				}

			} else {
				System.out.println("Mensagem não autenticado");
				return;
			}



		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}