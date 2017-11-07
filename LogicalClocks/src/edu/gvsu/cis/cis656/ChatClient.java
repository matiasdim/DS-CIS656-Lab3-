package edu.gvsu.cis.cis656;
/*
    This is the UI thread class
 */
import edu.gvsu.cis.cis656.message.Message;
import edu.gvsu.cis.cis656.message.MessageTypes;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChatClient {
    static String HOST = "localhost";
    static int PORT = 8000;
    String username;
    DatagramSocket datagramSocket;
    InetAddress inetAddres;

    public ChatClient(String username){
        this.username = username;
    }

    public void start(){
        Message msg = new Message(MessageTypes.REGISTER, this.username, 0, null, "");
        this.initNet();
        Message.sendMessage(msg, this.datagramSocket, this.inetAddres, PORT);
    }

    private void initNet(){
        try {
            this.datagramSocket = new DatagramSocket();
            this.inetAddres = InetAddress.getByName(HOST);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e){
            e.printStackTrace();
        }
    }
}
