/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelegramAPI;

/**
 *
 * @author HP
 */
public class JMessaggio {
    private int id;
    private String messaggio;
    private String nome;

    public JMessaggio(int id, String messaggio, String nome) {
        this.id = id;
        this.messaggio = messaggio;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getMessaggio() {
        return messaggio;
    }
    
    public String getNome() {
        return nome;
    }
}
