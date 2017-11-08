package edu.gvsu.cis.cis656;

import edu.gvsu.cis.cis656.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;

public class MessageListener implements Runnable {
    DatagramSocket datagramSocket;
    ChatClient chatClient;
    Message receivedMessage;
    public MessageListener(DatagramSocket datagramSocket, ChatClient chatClient){
        this.datagramSocket = datagramSocket;
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        while (true){
            receivedMessage = Message.receiveMessage(this.datagramSocket);
            this.chatClient.messageReceiver(receivedMessage);
        }
    }
}
