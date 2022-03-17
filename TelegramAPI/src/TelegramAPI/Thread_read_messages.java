/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TelegramAPI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import javax.swing.event.EventListenerList;

/**
 *
 * @author HP
 */
public class Thread_read_messages extends Thread {

    private String api_bot;
    private String toDo;
    private int previousID;
    private EventListenerList listeners;
    private String filename = "id.txt";

    public Thread_read_messages(String api_bot) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        int id = Integer.valueOf(br.readLine());

        this.api_bot = api_bot;
        toDo = "https://api.telegram.org/bot" + api_bot + "/getUpdates";
        previousID = id;
        listeners = new EventListenerList();
    }

    public void addMyListener(MyEventListener listener) {
        listeners.add(MyEventListener.class, listener);
    }

    public void removeMyListener(MyEventListener listener) {
        listeners.remove(MyEventListener.class, listener);
    }

    private void fireNewMessage(JMessaggio messaggio) {
        Object[] listenersArray = listeners.getListenerList();
        for (int i = listenersArray.length - 2; i >= 0; i -= 2) {
            if (listenersArray[i] == MyEventListener.class) {
                ((MyEventListener) listenersArray[i + 1]).onNewMessage(messaggio);
            }
        }
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
                        if (obj_message.getInt("message_id") > previousID) {
                            previousID = obj_message.getInt("message_id");
                            
                            int idChat = obj_message.getJSONObject("chat").getInt("id");
                            String m = obj_message.getString("text");
                            String nome = obj_message.getJSONObject("from").getString("first_name");
                            JMessaggio messaggio = new JMessaggio(idChat, m, nome);
                            fireNewMessage(messaggio);

                            /*------------------------------------------------------------------*/
                            //SALVO ID
                            FileWriter writer = new FileWriter(filename);
                            writer.write(String.valueOf(previousID));
                            writer.close();
                            /*------------------------------------------------------------------*/
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
