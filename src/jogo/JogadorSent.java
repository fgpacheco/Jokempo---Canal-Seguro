package jogo;

import java.io.Serializable;

public class JogadorSent implements Serializable {
	
	byte[] autenticacao;
	byte[] encriptacao;
	
	public JogadorSent(byte[] a, byte[] e) {
		this.autenticacao = a;
		this.encriptacao = e;
	}

	public byte[] getAutenticacao() {
		return autenticacao;
	}

	public void setAutenticacao(byte[] autenticacao) {
		this.autenticacao = autenticacao;
	}

	public byte[] getEncriptacao() {
		return encriptacao;
	}

	public void setEncriptacao(byte[] encriptacao) {
		this.encriptacao = encriptacao;
	}

}
