package jogo;

import java.io.Serializable;

public class Jogador implements Serializable {

	private String nome;
	private String escolha;

	public Jogador(String nome, String escolha) {
		this.nome = nome;
		this.escolha = escolha;	
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEscolha() {
		return escolha;
	}

	public void setEscolha(String escolha) {
		this.escolha = escolha;
	}	
}
