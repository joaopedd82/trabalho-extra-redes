
package com.mycompany.trabalhoextra;

import java.net.*;
import java.io.IOException;
import java.util.Vector;

public class Servidor {

    private ServerSocket soquete_servidor;
    private static Vector <String> infracao = new Vector<>();
    
    public Servidor(int porta) throws Exception {
        super();
        this.soquete_servidor = new ServerSocket(porta);
    }

    public void finalizar() throws IOException {
        this.soquete_servidor.close();
    }

    public static void main(String[] args) throws Exception {
        Servidor servidor = new Servidor(16000);
        Socket soqueteCliente = null;
        
        while (true) { 
            try {
                soqueteCliente = servidor.soquete_servidor.accept();
                new Thread(new TrataClienteServidor(soqueteCliente, infracao)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

