/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programmabotpubblicita;

import TelegramAPI.*;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author HP
 */
public class JFrame extends javax.swing.JFrame {

    Gestore_json gj;
    public JFrame() {
        try {
            initComponents();
            gj = new Gestore_json("5209120531:AAEAcuXnl2pUC0ibZiDTuYWeONiDoXc4RzM");
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
                                Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } catch (MalformedURLException ex) {
                            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParserConfigurationException ex) {
                            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SAXException ex) {
                            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            trm.start();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_offerta = new javax.swing.JTextArea();
        txt_luogo = new javax.swing.JTextField();
        btn_invia = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Offerta");

        jLabel2.setText("Luogo");

        txt_offerta.setColumns(20);
        txt_offerta.setRows(5);
        jScrollPane1.setViewportView(txt_offerta);

        btn_invia.setText("Invia");
        btn_invia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_inviaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_luogo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(btn_invia)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(133, 133, 133)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txt_luogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72)
                        .addComponent(btn_invia))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_inviaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_inviaActionPerformed
        try {
            FileReader fr = null;

            String filename = "file.xml";
            URL url = new URL("https://nominatim.openstreetmap.org/search?q=" + txt_luogo.getText() + "&format=xml&addressdetails=1");
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

            double lat_punto = Double.parseDouble(lat);
            double lon_punto = Double.parseDouble(lon);
            double lat_saved = 0;
            double lon_saved = 0;
            try {
                String file_csv = "locations.txt";
                fr = new FileReader(file_csv);
                BufferedReader br_csv = new BufferedReader(fr);
                String line = "";
                boolean gia_presente = false;
                do {
                    try {
                        line = br_csv.readLine();
                        if (line != null) {
                            String[] campi_csv = line.split(";");
                            lat_saved = Float.parseFloat(campi_csv[2]);
                            lon_saved = Float.parseFloat(campi_csv[2]);
                            double lat1Radians = (lat_punto * Math.PI / 180);
                            double lon1Radians = (lon_punto * Math.PI / 180);
                            double lat2Radians = (lat_saved * Math.PI / 180);
                            double lon2Radians = (lon_saved * Math.PI / 180);

                            double distanza = Math.acos(Math.sin(lat1Radians) * Math.sin(lat2Radians) + Math.cos(lat1Radians) * Math.cos(lat2Radians) * Math.cos(lon2Radians - lat1Radians)) * 6371;
                            if(distanza<100){
                                gj.SendMessage(Integer.parseInt(campi_csv[0]), txt_offerta.getText());
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } while (line != null);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(JFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_inviaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_invia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_luogo;
    private javax.swing.JTextArea txt_offerta;
    // End of variables declaration//GEN-END:variables
}
