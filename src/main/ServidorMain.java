package main;

import conexao.Servidor;

public class ServidorMain {
	
	public static void main(String[] args) {
		Servidor servidor = new Servidor();		
		servidor.start();
		
	}
}
