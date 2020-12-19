package net.jonasw.informatikserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.prefs.Preferences;
import net.jonasw.informatikserver.*;

public class Main {
    public static ArrayList<Client> clients = new ArrayList<>();// hier werden alle Clients hinzugefügt, damit ich alle
                                                                // anschreiben kann
    public static ArrayList<Message> messages = new ArrayList<>();// Hier werden alle bis jetzt geschriebenen
                                                                  // Nachrichten hinzugefügt
    public static ServerSocket server;

    public static void main(String[] args) {
        try {
            server = new ServerSocket(54321); // Einen Socket erstellen der auf den Port 54321 Hört
            Socket s;// Hier wird unten der Verbindende Socket gespeichert
            System.out.println("Started Server on port " + server.getLocalPort());// Damit man in der Konsole sieht, das
                                                                                  // was passiert
            while (!server.isClosed()) {
                s = server.accept();// wartet bis ein Client sich connecten will
                System.out.println("New Connection from " + s.getInetAddress() + ":" + s.getPort());
                clients.add(new Client(s));// Socket wird übergeben und er wartet wieder auf einen neuen Socket
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handle(Client c, String line) {
        // Hier wird eine Nachricht vom Client Gehandelt
        // ich hatte zuerst vor noch andere "Commands" hinzuzufügen, damit man vlt auch
        // bilder Schicken kann o.ä. aber dafür reicht die Zeit nicht
        String[] args = line.split(Pattern.quote("_//|//_"));// Damit ich "MSG" von der Richtigen Nachricht Gescheit
                                                             // trennen kann
        if (args.length < 1)// Ansonsten fang ich damit nix an
            return;
        if (args[0].equals("MSG")) {
            Message m = new Message(c.username, args[1]);// Neues Message Object wird erstellt
            sendMessage(m);// Sie wird an alle CLients geschickt
        }
    }

    /**
     * In dieser Methode wird die message @param m, an alle Clients geschickt und
     * falls ein Client die Nachricht nicht haben will wird er von der Clientsliste
     * removed
     */
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

    // Damit man die schöne "left the Chat" message hat und es aus der liste
    // entfernt wird
    public static void remove(Client c) {
        clients.remove(c);
        if (c.username == null)
            return;
        sendMessage(new Message(c.username, "left the chat!"));
    }
}