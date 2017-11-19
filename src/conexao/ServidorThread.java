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
import java.net.Socket;
import java.util.ArrayList;
/**
 *
 * @author Felipe
 */
public class ServidorThread extends Thread {

    private Socket socket;
    private ArrayList<BufferedReader> jogadores;
    private ArrayList<String> list;

    public ServidorThread(Socket socket, ArrayList<BufferedReader> jogadores, ArrayList<String> list) {
        this.socket = socket;
        this.jogadores = jogadores;
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("Jogador Conectado, aguardando item...");
        BufferedReader in;
        PrintWriter out;
        try {
            //Ler dados vindos do cliente
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String escolha = in.readLine();
            list.add(escolha);
            System.out.println("Escolha recebida: " + escolha);

            //Determinar que escolha é a vencedora
            resultado();
            
            if(list.size() == 2) {
                return;
            }

        } catch (IOException e) {
            // TODO: handle exception
        }
    }

    public ArrayList<String> resultado() {
        
        if (jogadores.size() == 2 && list.size() == 2) {
            System.out.println(list.get(0));
            System.out.println(list.get(1));               
        }
        
        return list;
    }

}
