package jogo;

import java.util.ArrayList;

public class Partida {
	
	private ArrayList<Jogador> jogadores;
	private Jogador empate;
	
	public Partida() {
		jogadores = new ArrayList<>();
		empate = new Jogador("Empate", "0");
	}
	
	public void add(Jogador jogador) {
		jogadores.add(jogador);
	}
	
	public Jogador vencedor() {
		Jogador jogador1 = jogadores.get(0);
		Jogador jogador2 = jogadores.get(1);
		
		int escolhaJog1 = escolhaToInt(jogador1.getEscolha());
		int escolhaJog2 = escolhaToInt(jogador2.getEscolha());
		

		if((escolhaJog1 - escolhaJog2) == 0) {			
			return empate;		
		} else if((escolhaJog1 - escolhaJog2 == -1) || (escolhaJog1 - escolhaJog2 == 2)) {
			return jogador1;
		} else {
			return jogador2;
		}

	}

	public ArrayList<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(ArrayList<Jogador> jogadores) {
		this.jogadores = jogadores;
	}
	
	public static int escolhaToInt(String s) {
		switch(s) {
			case "Papel":
				return 1;
			case "Pedra":
				return 2;
			case "Tesoura":
				return 3;
			default:
				return 0;
		}
	}
	
	public static String escolhaToString(int escolha) {
		switch(escolha) {
			case 1:
				return "Papel";
			case 2:
				return "Pedra";
			case 3: 
				return "Tesoura";
			default:
				return null;
		}
	}
	

}
