package main;

import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

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
				"3 - Tesoura\n");
		
		int escolha = scan.nextInt();
		
		Jogador jogador = new Jogador(nome, Partida.escolhaToString(escolha));
		//String msg;		
		
		//msg = Conversor.convertToString(jogador);
		c.enviar(jogador);
		
		//String resposta = null;
		Jogador vencedor = null;

		while(vencedor == null) {
			try {
				vencedor = c.receber();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}								
		}

		//jogador = (Jogador) Conversor.convertFromString(resposta);

		String resultado = (vencedor == null) ? "Empate" : vencedor.getNome() + " venceu: " + vencedor.getEscolha();
		
		System.out.println(resultado);

	}
}
