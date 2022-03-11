/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programmabotpubblicita;

import TelegramAPI.*;
import java.io.IOException;
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
        gj.SendMessage("959044774", "ciao");
        
        Thread_read_messages trm = new Thread_read_messages("5209120531:AAEAcuXnl2pUC0ibZiDTuYWeONiDoXc4RzM");
        trm.addMyListener(new MyEventListener() {
            @Override
            public void onNewMessage(long id, String message) {
                System.out.println("Messaggio! "+id+" - "+message);
                
            }
        });
        
        trm.start();
    }
}
