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
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            username = in.readLine();
            Main.sendMessage(new Message(username, "joined the chat!"));
            Main.messages.forEach(msg -> {
                try {
                    out.write(msg.toString());
                    out.flush();
                } catch (Exception e) {

                }
            });
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        Main.handle(this, line);
                    }
                    Main.remove(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            System.out.println("Client failed " + (username != null ? username : ""));
            Main.remove(this);
            e.printStackTrace();
        }

    }
}
