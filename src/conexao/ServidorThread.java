package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import jogo.Jogador;

public class ServidorThread extends Thread {  //implements Runnable{

    private Socket socket;

    public ServidorThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Jogador jogador = Jogador.convertFromString(in.readLine());
            System.out.println(jogador);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
