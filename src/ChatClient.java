import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {
    JTextArea tA = new JTextArea(20,40);
    JTextField tF = new JTextField(40);
    BufferedReader bR;
    PrintWriter pW;
    Socket s;


    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.go();
    }

    public void go() {
        JFrame jfr = new JFrame("ChatClient");
        JPanel jp = new JPanel();
        tA.setLineWrap(true);
        tA.setWrapStyleWord(true);
        tA.setEditable(false);
        JScrollPane sP = new JScrollPane(tA);
        sP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JButton send = new JButton("Send");
        send.addActionListener(new SendListener());
        jp.add(sP);
        jp.add(tF);
        jp.add(send);
        setUpNetworking();

        new Thread(new InReader()).start();

        jfr.getContentPane().add(BorderLayout.CENTER,jp);
        jfr.setSize(500,600);
        jfr.setVisible(true);

    }

    private void setUpNetworking() {
        try {
            s = new Socket("127.0.0.1",1984);
            bR = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pW = new PrintWriter(s.getOutputStream());
            System.out.println("Connect!");

        } catch (IOException ex) {
            tA.setForeground(Color.RED);
            tA.append("Server and/or port is/are NOT AVAILABLE");
            ex.printStackTrace();
        }
    }

    public class SendListener implements ActionListener {
        public  void  actionPerformed(ActionEvent ev) {
            try{
                pW.println(tF.getText());
                pW.flush();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            tF.setText("");
            tF.requestFocus();
        }
    }

    public class InReader implements Runnable {
        public void run() {
            String message;
            try {

                while((message = bR.readLine()) != null) {
                    System.out.println("read " + message);
                    tA.append(message + "\n");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
