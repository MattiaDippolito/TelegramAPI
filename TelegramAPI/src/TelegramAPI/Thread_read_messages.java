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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author HP
 */
public class Thread_read_messages extends Thread {

    private String api_bot;
    private String toDo;
    private int previousID;

    public Thread_read_messages(String api_bot) {
        this.api_bot = api_bot;
        toDo = "https://api.telegram.org/bot" + api_bot + "/getUpdates";
        previousID = 0;
    }

    @Override
    public void run() {
        while (true) {
            try {
                URL url = new URL(toDo);

                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String s;
                String tutto = "";
                do {
                    s = br.readLine();
                    if (s != null) {
                        tutto += s;
                    }
                } while (s != null);

                JSONObject Jobj = new JSONObject(tutto);
                JSONArray result = Jobj.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    Object content = result.get(i);/*"{"update_id":93552028,"message":{"date":1646331313,"chat":{"id":959044774,"type":"private","first_name":"Mattia"},"message_id":6,"from":{"language_code":"it","id":959044774,"is_bot":false,"first_name":"Mattia"},"text":"ciao"}}"*/
                    JSONObject obj_content = new JSONObject(content.toString());
                    if (!obj_content.isNull("message")) {
                        JSONObject obj_message = obj_content.getJSONObject("message");
                        if(obj_message.getInt("message_id") > previousID){
                            previousID = obj_message.getInt("message_id");
                            System.out.println(obj_message.getString("text"));
                        }
                    }
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(Thread_read_messages.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Thread_read_messages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
