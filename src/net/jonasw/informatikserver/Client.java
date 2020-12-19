package net.jonasw.informatikserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    BufferedWriter out;
    BufferedReader in;
    String username;

    public Client(Socket s) {
        try {
            // Damit ich mit dem Client Schreiben lesen kann :))
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            username = in.readLine();
            // Damit alle sehen ah der "username " ist da
            Main.sendMessage(new Message(username, "joined the chat!"));
            // Erstmal alle alten Nachrichten schicken
            Main.messages.forEach(msg -> {
                try {
                    out.write(msg.toString());
                    out.flush();
                } catch (Exception e) {

                }
            });
            // Hier hört er auf Nachrichten von dem Client so lange, bis die COnnection
            // endet dann ist readLine == null
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        Main.handle(this, line);
                    }
                    // von der liste entfernen wenn er keinen bock mehr hat
                    Main.remove(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            // von der liste entfernen wenn er keinen bock mehr hat
            System.out.println("Client failed " + (username != null ? username : ""));
            Main.remove(this);
            e.printStackTrace();
        }

    }
}
