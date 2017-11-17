/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Felipe
 */
public class Game {
    
    private int jogador;    
    
    public Game() {        
    
    }
    
    /*
        1 -> Papel | 2 -> Pedra | 3 -> Tesoura
        1 - 2 = -1, jogador1
        1 - 3 = -2, jogador2
        2 - 1 = 1, jogador2
        2 - 3 = -1, jogador1
        3 - 1 = 2, jogador1
        3 - 2 =, jogador2
    */
    public void jogo() {
        /*jogador1 = new Jogador(1);
        //jogador2 = new Jogador(2);        
        
        for (int i = 0; i < 5; i++) {
            int escolhaJog1 = jogador1.jogar();
            int escolhaJog2 = jogador2.jogar();
            
            if ((escolhaJog1 - escolhaJog2) == -1
                    || (escolhaJog1 - escolhaJog2) == 2) {
                System.out.println("Jogador 1 venceu !");
            } else {
                System.out.println("Jogador 2 venceu !");
            }
        
        } */      
        
    }
    
    /*public static void main(String[] args) {
        new Game().jogo();
    }*/
    
}
