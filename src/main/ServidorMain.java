package main;

import conexao.Servidor;
import jogo.Jogador;

public class ServidorMain {
	
	public static void main(String[] args) {
		Servidor servidor = new Servidor();		
		servidor.start();
		
	}
}
