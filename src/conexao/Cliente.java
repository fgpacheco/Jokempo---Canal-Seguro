/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Felipe
 */
public class Cliente {
    
    private String ip;
    private String porta;
    private Socket socket;
    
    public Cliente(String ip, String porta) {
        this.ip = ip;
        this.porta = porta;        
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }
    
    public Socket conectar() {
        try {
            socket = new Socket("127.0.0.1", 12345);            
            return socket;
        } catch (IOException ex) {
            ex.printStackTrace();            
            return null;
        }
    }   
    
}
