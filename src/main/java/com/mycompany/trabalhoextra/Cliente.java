package com.mycompany.trabalhoextra;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Cliente {

    private Socket soquete;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;

    public Cliente(String endereco, int porta) throws Exception {
        super();
        this.soquete = new Socket(endereco, porta);
        this.saida = new ObjectOutputStream(this.soquete.getOutputStream());
        this.entrada = new ObjectInputStream(this.soquete.getInputStream());
    }

    public void enviar_mensagem(Object mensagem) throws Exception {
        this.saida.writeObject(mensagem);
    }

    public Object receber_mensagem() throws Exception {
        return this.entrada.readObject();
    }

    public void finalizar() throws IOException {
        this.soquete.close();
    }

    public static void main(String[] args) throws Exception {

        Scanner entrada = new Scanner(System.in);
        String carro;
        String modo;
        int temp, press;
        Random r = new Random();

        System.out.print("1 - FERRARI \n2 - MERCEDES \n==> ");
        carro = (entrada.nextInt() == 1 ? "FERRARI" : "MERCEDES");

        System.out.print("1 - PUBLICAR \n2 - CONSULTAR \n==> ");
        modo = (entrada.nextInt() == 1 ? "PUBLICAR" : "CONSULTAR");

        if (modo.equalsIgnoreCase("PUBLICAR")) {
            while (true) {
                temp = r.nextInt(50) + 60;
                press = r.nextInt(50) + 20;

                Cliente cliente = new Cliente("127.0.0.1", 16000);
                cliente.enviar_mensagem(carro + "," + modo + "," + temp + "," + press);

                String mensagem = (String) cliente.receber_mensagem();
                System.out.println(mensagem);

                Thread.sleep(5000);
                cliente.finalizar();
            }
        } else {
            Cliente cliente = new Cliente("127.0.0.1", 16000);
            cliente.enviar_mensagem(carro + "," + modo);
            Vector<String> mensagem = (Vector<String>) cliente.receber_mensagem();
            for (String s : mensagem) {
                System.out.println(s);

            }
            cliente.finalizar();
        }
    }
}
