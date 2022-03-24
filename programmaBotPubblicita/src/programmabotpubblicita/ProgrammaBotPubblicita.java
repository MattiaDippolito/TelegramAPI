/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programmabotpubblicita;

import TelegramAPI.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author dippolito_mattia
 */
public class ProgrammaBotPubblicita {

    public static void main(String[] args) throws IOException {
        Gestore_json gj = new Gestore_json("5209120531:AAEAcuXnl2pUC0ibZiDTuYWeONiDoXc4RzM");

        Thread_read_messages trm = new Thread_read_messages("5209120531:AAEAcuXnl2pUC0ibZiDTuYWeONiDoXc4RzM");
        trm.addMyListener(new MyEventListener() {
            @Override
            public void onNewMessage(JMessaggio messaggio) {
                String file_csv = "locations.txt";

                String[] campi = messaggio.getMessaggio().split(" ");
                if (campi[0].equals("/città")) {
                    try {
                        String nome_citta = "";
                        for (int i = 1; i < campi.length; i++) {
                            if (i != campi.length - 1) {
                                nome_citta += campi[i] + " ";
                            } else {
                                nome_citta += campi[i];
                            }
                        }

                        //Cerco la citta su osm
                        String filename = "file.xml";
                        URL url = new URL("https://nominatim.openstreetmap.org/search?q=" + nome_citta + "&format=xml&addressdetails=1");
                        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                        FileWriter writer = new FileWriter(filename);
                        String s;
                        do {
                            s = br.readLine();
                            if (s != null) {
                                writer.write(s);
                                writer.write("\n");
                            }
                        } while (s != null);
                        writer.close();

                        DocumentBuilderFactory factory;
                        DocumentBuilder builder;
                        Element root, place, city_town;

                        factory = DocumentBuilderFactory.newInstance();
                        builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(filename);

                        root = (Element) doc.getDocumentElement();
                        NodeList list = root.getElementsByTagName("place");

                        boolean trovato = false;
                        String display_name = "", lat = "", lon = "";
                        for (int i = 0; i < list.getLength() && !trovato; i++) {
                            place = (Element) list.item(i);
                            if (place.getAttribute("class") != null) {
                                trovato = true;
                                display_name = place.getAttribute("display_name");
                                lat = place.getAttribute("lat");
                                lon = place.getAttribute("lon");
                            }
                        }

                        String file = "";
                        FileReader fr = new FileReader(file_csv);
                        BufferedReader br_csv = new BufferedReader(fr);
                        String line = "";
                        boolean gia_presente = false;
                        do {
                            line = br_csv.readLine();
                            if (line != null) {
                                String[] campi_csv = line.split(";");
                                int id = Integer.parseInt(campi_csv[0]);
                                if (id == messaggio.getId()) {
                                    file += messaggio.getId() + ";" + messaggio.getNome() + ";" + lat + ";" + lon + ";\n";
                                    gia_presente = true;
                                } else {
                                    file += line + "\n";
                                }
                            }
                        } while (line != null);
                        if (!gia_presente) {
                            file += messaggio.getId() + ";" + messaggio.getNome() + ";" + lat + ";" + lon + ";\n";
                        }
                        FileWriter writer_csv = new FileWriter(file_csv);
                        writer_csv.write(file);
                        writer_csv.close();
                        
                        try {
                            gj.SendMessage(messaggio.getId(), display_name + " Latitudine: " + lat + " Longitudine: " + lon);
                        } catch (IOException ex) {
                            Logger.getLogger(ProgrammaBotPubblicita.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (MalformedURLException ex) {
                        Logger.getLogger(ProgrammaBotPubblicita.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ProgrammaBotPubblicita.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(ProgrammaBotPubblicita.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SAXException ex) {
                        Logger.getLogger(ProgrammaBotPubblicita.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        trm.start();
    }
}
