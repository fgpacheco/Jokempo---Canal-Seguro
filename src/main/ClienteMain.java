package main;

import java.io.IOException;
import java.util.Scanner;
import conexao.Cliente;
import conexao.Servidor;
import jogo.Jogador;
import jogo.Partida;

public class ClienteMain {
	
	public static void main(String[] args) {
		
		Cliente c = new Cliente("localhost", Servidor.PORTA);		
		c.conectar();
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Digite seu nome: ");
		String nome = scan.nextLine();		
		
		System.out.println("Escolha uma jogada:\n" +
				"1 - Papel\n" + 
				"2 - Pedra\n" +
				"3 - Tesoura\n" +
				"0 - SAIR\n");
		
		int escolha = scan.nextInt();
		
		Jogador jogador = new Jogador(nome, Partida.escolhaToString(escolha));
		String msg;
		
		
		
		msg = Jogador.convertToString(jogador);
		c.enviar(msg);
		
		String s = null;
		
		try {
			while(s == null) {
				s = c.receber();								
			}
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		System.out.println(s);

	}
}
