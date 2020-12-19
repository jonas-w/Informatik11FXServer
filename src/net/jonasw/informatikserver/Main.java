package net.jonasw.informatikserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.prefs.Preferences;
import net.jonasw.informatikserver.*;

public class Main {
    public static ArrayList<Client> clients = new ArrayList<>();
    public static ArrayList<Message> messages = new ArrayList<>();
    public static ServerSocket server;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(54321);
            Socket s;
            System.out.println("Started Server on port " + server.getLocalPort());
            while (!server.isClosed()) {
                s = server.accept();
                System.out.println("New Connection from " + s.getInetAddress() + ":" + s.getPort());
                clients.add(new Client(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handle(Client c, String line) {
        String[] args = line.split(Pattern.quote("_//|//_"));
        if (args.length < 1)
            return;
        if (args[0].equals("MSG")) {
            Message m = new Message(c.username, args[1]);
            sendMessage(m);
        }
    }

    public static void sendMessage(Message m) {
        messages.add(m);
        ArrayList<Client> toRemove = new ArrayList();
        clients.forEach(client -> {
            try {
                client.out.write(m.toString());
                client.out.flush();
            } catch (Exception e) {
                toRemove.add(client);
            }
        });
        toRemove.forEach(client -> {
            remove(client);
        });
    }

    public static void remove(Client c) {
        clients.remove(c);
        if (c.username == null)
            return;
        sendMessage(new Message(c.username, "left the chat!"));
    }
}