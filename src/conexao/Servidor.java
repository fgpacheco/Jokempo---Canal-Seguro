package conexao;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	
	public static final int PORTA = 12345;
	private ServerSocket serverSocket;
	private Socket socket;
	
	public Servidor() {
		try {
			serverSocket = new ServerSocket(PORTA);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Socket start() {
		System.out.println("Aguardando o cliente...");	
		
		while(true) {
			try {
				socket = serverSocket.accept();
				System.out.println("Cliente conectado");
				
				new ServidorThread(socket).start();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}		
	}	

}
