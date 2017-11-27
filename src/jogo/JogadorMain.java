package jogo;

import java.util.Scanner;

public class JogadorMain {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Digite seu nome: ");
        String nome = scan.nextLine();

        Jogador jogador = new Jogador(nome);
        jogador.jogar();
        
        String msg = Jogador.convertToString(jogador);

        jogador.getCliente().enviar(msg);

    }
}
