package net.jonasw.informatikserver;

/**
 * So ähnlich wie auf der Client seite nur das die toString methode so aussieht,
 * das ichs direkt an den Client schicken kann und er auch versteht was er damit
 * anfangen soll
 * 
 * Die Zeit wird zu der Jetzigen Zeit gesetzt
 */
public class Message {
    String username;
    String msg;
    long time;

    public Message(String username, String msg) {
        this.username = username;
        this.msg = msg;
        this.time = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "MSG_//|//_" + this.username + "_//|//_" + this.msg + "_//|//_" + String.valueOf(this.time) + "\n";
    }
}
