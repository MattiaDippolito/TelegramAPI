/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelegramAPI;

import java.util.EventListener;

/**
 *
 * @author dippolito_mattia
 */
 public interface MyEventListener extends EventListener{
     //Metodi da implementare per essere un ascoltatore del sorgente
     public void onNewMessage(JMessaggio messaggio);
    //eventuali altri metodi
 }   
 