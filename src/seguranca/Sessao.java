package seguranca;

import javax.crypto.SecretKey;

public class Sessao {
	
	private SecretKey chaveEncriptacaoServer;
	private SecretKey chaveEncriptacaoClient;
	private SecretKey chaveAutenticacaoServer;
	private SecretKey chaveAutenticacaoClient;
	
	public Sessao(SecretKey keyS, SecretKey keyC, SecretKey autKeyS, SecretKey autKeyC){
		chaveEncriptacaoServer = keyS;
		chaveEncriptacaoClient = keyC;
		chaveAutenticacaoServer = autKeyS;
		chaveAutenticacaoClient = autKeyC;
	}
	
	public Sessao(){
		
	}
	
	public SecretKey getChaveEncriptacaoServer() {
		return chaveEncriptacaoServer;
	}
	public void setChaveEncriptacaoServer(SecretKey chaveEncriptacaoServer) {
		this.chaveEncriptacaoServer = chaveEncriptacaoServer;
	}
	public SecretKey getChaveEncriptacaoClient() {
		return chaveEncriptacaoClient;
	}
	public void setChaveEncriptacaoClient(SecretKey chaveEncriptacaoClient) {
		this.chaveEncriptacaoClient = chaveEncriptacaoClient;
	}
	public SecretKey getChaveAutenticacaoServer() {
		return chaveAutenticacaoServer;
	}
	public void setChaveAutenticacaoServer(SecretKey chaveAutenticacaoServer) {
		this.chaveAutenticacaoServer = chaveAutenticacaoServer;
	}
	public SecretKey getChaveAutenticacaoClient() {
		return chaveAutenticacaoClient;
	}
	public void setChaveAutenticacaoClient(SecretKey chaveAutenticacaoClient) {
		this.chaveAutenticacaoClient = chaveAutenticacaoClient;
	}
}
