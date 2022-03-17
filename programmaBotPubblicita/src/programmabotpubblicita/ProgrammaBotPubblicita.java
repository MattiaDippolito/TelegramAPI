/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programmabotpubblicita;

import TelegramAPI.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author dippolito_mattia
 */
public class ProgrammaBotPubblicita {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Gestore_json gj = new Gestore_json("5209120531:AAEAcuXnl2pUC0ibZiDTuYWeONiDoXc4RzM");
        /*gj.SendMessage("959044774", "ciao");*/
        
        Thread_read_messages trm = new Thread_read_messages("5209120531:AAEAcuXnl2pUC0ibZiDTuYWeONiDoXc4RzM");
        trm.addMyListener(new MyEventListener() {
            @Override
            public void onNewMessage(JMessaggio messaggio) {
                /*try {
                    gj.SendMessage(messaggio.getId(), messaggio.getNome() + " ha scritto: " + messaggio.getMessaggio());
                } catch (IOException ex) {
                    Logger.getLogger(ProgrammaBotPubblicita.class.getName()).log(Level.SEVERE, null, ex);
                }*/
                String[] campi = messaggio.getMessaggio().split(" ");
                if(campi[0].equals("/città")){
                    String nome_citta = "";
                    for (int i = 1; i < campi.length; i++) {
                        nome_citta += campi[i] + " ";
                    }
                    try {
                        gj.SendMessage(messaggio.getId(), messaggio.getNome() + " ha scritto: " + nome_citta);
                    } catch (IOException ex) {
                        Logger.getLogger(ProgrammaBotPubblicita.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        trm.start();
    }
}
//quando spengo e riaccendo il programma legge anche i messaggi precedenti