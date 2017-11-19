/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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
        
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Aguardando jogadores...");

            while(true) {
                socket = serverSocket.accept();
                jogadores.add(new BufferedReader(new InputStreamReader(socket.getInputStream())));
                
                new ServidorThread(socket, jogadores, listEscolhas).start();
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Problema no servidor...\n" + ex.getMessage(),
                    "", JOptionPane.ERROR_MESSAGE);
        } 
        
    }
}
