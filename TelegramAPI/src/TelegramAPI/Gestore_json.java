/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelegramAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author dippolito_mattia
 */
public class Gestore_json {

    public String api_bot;

    public Gestore_json(String api_bot) {
        this.api_bot = api_bot;
    }

    public void SendMessage(String id, String message) throws MalformedURLException, IOException {
        String string = "https://api.telegram.org/bot"+api_bot+"/sendMessage?chat_id="+id+"&text="+URLEncoder.encode(message,"utf8");
        System.out.println(string);
        URL url = new URL(string);
        url.openStream();
        /*BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String s, str = "";
        do {
            s = br.readLine();
            if (s != null) {
                str += s;
            }
        } while (s != null);*/
    }
}
