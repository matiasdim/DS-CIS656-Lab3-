package edu.gvsu.cis.cis656;
import edu.gvsu.cis.cis656.ChatClient;

public class Main {
    public static void main (String[] args){
        if(args.length != 1){
            System.out.println("Usage: java Main [username]");
            return;
        }
        String username = args[0];
        ChatClient chatClient = new ChatClient(username);
        chatClient.start();
    }
}
