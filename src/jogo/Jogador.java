package jogo;

import java.io.Serializable;

import javax.crypto.SecretKey;

public class Jogador implements Serializable {

	private String nome;
	private String escolha;
	//private SecretKey auth;

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
	/*

	public SecretKey getAuth() {
		return auth;
	}

	public void setAuth(SecretKey auth) {
		this.auth = auth;
	}
	*/
	
}
