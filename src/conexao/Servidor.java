/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Felipe
 */
public class Servidor {

    public static void main(String[] args) {

        ServerSocket serverSocket;
        Socket socket;
        ArrayList<BufferedReader> jogadores = new ArrayList<>();
        ArrayList<String> listEscolhas = new ArrayList<>();
        	
//        try{
//                    serverSocket = new ServerSocket(12345);
		        	Conexao c = new Conexao(12345);
//		            System.out.println("Aguardando jogadores...");
		            c.init();
		
		            while(true) {
//		                socket = serverSocket.accept();
//		                jogadores.add(new BufferedReader(new InputStreamReader(socket.getInputStream())));
//		                
//		               
//		                new ServidorThread(socket, jogadores, listEscolhas).start();
		            }
//        }catch (IOException e) {
//			// TODO: handle exception
//        	e.printStackTrace();
//		}
        
    }
}
