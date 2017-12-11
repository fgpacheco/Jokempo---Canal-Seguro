package conexao;

import java.net.Socket;
import jogo.Jogador;
import seguranca.Seguranca;

public class ServidorThread implements Runnable{

	private int contador;	
	private Servidor servidor;
	private Seguranca seguranca;
	private Comunicacao comunicacao;

	public ServidorThread(Socket socket, int contador, Servidor servidor, Seguranca seguranca) {
		this.contador = contador;
		this.servidor = servidor;
		this.seguranca = seguranca;
		this.comunicacao =  new Comunicacao(socket, seguranca);
	}

	@Override
	public void run() {
		try {

			seguranca.setSessao(comunicacao.receberSessao());

			Jogador jogador = (Jogador) comunicacao.receberObjetoCliente();

			while(jogador != null) {
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
					comunicacao.enviarObjetoServidor(vencedor);
				}

				jogador = (Jogador) comunicacao.receberObjetoCliente();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}