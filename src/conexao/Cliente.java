package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    
    private String host;
    private int porta;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    
    
    public Cliente(String host, int porta) {
        this.host = host;
        this.porta = porta;        
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
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void enviar(String msg) {
    	out.println(msg);
	}
    
}