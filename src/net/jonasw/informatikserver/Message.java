package net.jonasw.informatikserver;

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
