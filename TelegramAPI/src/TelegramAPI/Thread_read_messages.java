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
    private int update_id;
    private EventListenerList listeners;
    private boolean first;

    public Thread_read_messages(String api_bot) throws FileNotFoundException {
        this.api_bot = api_bot;
        update_id = 0;
        listeners = new EventListenerList();
        first = true;
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
                String tutto = "";
                if (first) {
                    URL url = new URL("https://api.telegram.org/bot" + api_bot + "/getUpdates");
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String s;
                    do {
                        s = br.readLine();
                        if (s != null) {
                            tutto += s;
                        }
                    } while (s != null);
                    first = false;
                } else {
                    URL url = new URL("https://api.telegram.org/bot" + api_bot + "/getUpdates?offset=" + update_id);
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String s;
                    do {
                        s = br.readLine();
                        if (s != null) {
                            tutto += s;
                        }
                    } while (s != null);
                }

                JSONObject Jobj = new JSONObject(tutto);
                JSONArray result = Jobj.getJSONArray("result");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject obj_message = result.getJSONObject(i).getJSONObject("message");
                    int idChat = obj_message.getJSONObject("chat").getInt("id");
                    String m = obj_message.getString("text");
                    String nome = obj_message.getJSONObject("from").getString("first_name");
                    update_id = result.getJSONObject(i).getInt("update_id") + 1;
                    JMessaggio messaggio = new JMessaggio(idChat, m, nome);
                    fireNewMessage(messaggio);
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(Thread_read_messages.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Thread_read_messages.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Thread_read_messages.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
