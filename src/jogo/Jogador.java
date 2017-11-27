package jogo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import conexao.Cliente;
import java.io.Serializable;

public class Jogador implements Serializable {

    private Cliente cliente;
    private String nome;
    private int escolha;

    public Jogador(String nome) {
        this.cliente = new Cliente("localhost", 12345);
        this.nome = nome;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEscolha() {
        return escolha;
    }    

    public void jogar() {

        cliente.conectar();

        Scanner scan = new Scanner(System.in);

        System.out.println("Escolha uma jogada:\n"
                + "1 - Papel\n"
                + "2 - Pedra\n"
                + "3 - Tesoura\n");

        int escolha = scan.nextInt();
        
        this.escolha = escolha;       
        

    }

    public static String convertToString(Jogador obj) {
        try {
            String str;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            byte[] objeto = baos.toByteArray();
            str = Base64.encode(objeto);
            oos.close();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Jogador convertFromString(String str) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(str));
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Jogador) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("Nome: %s\nEscolha: %d", nome, escolha);
    }
    
}
