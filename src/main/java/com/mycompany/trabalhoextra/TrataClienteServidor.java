
package com.mycompany.trabalhoextra;

import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Vector;

public class TrataClienteServidor implements Runnable {

    private Socket soquete_cliente;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private Vector <String> infracao;
    public TrataClienteServidor(Socket soquete_cliente, Vector<String> infracao) throws Exception {
        super();
        this.soquete_cliente = soquete_cliente;
        this.saida = new ObjectOutputStream(this.soquete_cliente.getOutputStream());
        this.entrada = new ObjectInputStream(this.soquete_cliente.getInputStream());
        this.infracao = infracao;
    }

    public void enviar_mensagem(Object mensagem) throws Exception {
        this.saida.writeObject(mensagem);
    }

    public Object receber_mensagem() throws Exception {
        return this.entrada.readObject();
    }

    public void finalizar() throws IOException {
        this.soquete_cliente.close();
    }


    @Override
    public void run() {
        try {
            String solicitacaoCliente = (String) receber_mensagem();
            
            String[] parametrosSolicitacao = solicitacaoCliente.split(",");
            
            String carro = parametrosSolicitacao[0]; 
            String modo = parametrosSolicitacao[1];           
            
            if (modo.equalsIgnoreCase("PUBLICAR")) { 
                int temp = Integer.parseInt(parametrosSolicitacao[2]);
                int press = Integer.parseInt(parametrosSolicitacao[3]);
                if (temp < 80 || temp > 100 || press < 40 || press > 60){
                    infracao.add("Infração para " + carro + " às " + new Date());
                    enviar_mensagem("Infração Detectada");
                } else{
                    enviar_mensagem("OK");
                }
                
            } else {
                enviar_mensagem(infracao);
            }

            finalizar();
            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
