package edu.gvsu.cis.cis656;
/*
    This is the UI thread class
 */
import edu.gvsu.cis.cis656.clock.VectorClock;
import edu.gvsu.cis.cis656.message.Message;
import edu.gvsu.cis.cis656.message.MessageComparator;
import edu.gvsu.cis.cis656.message.MessageTypes;
import edu.gvsu.cis.cis656.queue.PriorityQueue;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ChatClient {
    static String HOST = "localhost";
    static int PORT = 8000;
    int selfPid;
    VectorClock vectorClock;
    PriorityQueue<Message> priorityQueue;
    String username;
    DatagramSocket datagramSocket;
    InetAddress inetAddres;

    public ChatClient(String username){
        this.username = username;
    }

    public void start(){
        this.initNetwork();
        // Client registration
        this.clientRegistration();
        System.out.println("Hello " + this.username + " you entered to the chat.");
    }

    /*
     Auxiliar functions
     */

    // Client registration
    private void clientRegistration(){
        Message response = this.postMessage(MessageTypes.REGISTER, this.username, 0, null, "");
        if(response.type == MessageTypes.ACK){
            this.selfPid = response.pid;
            vectorClock = new VectorClock();
            vectorClock.addProcess(this.selfPid, 0);
            this.priorityQueue = new PriorityQueue<>(new MessageComparator());
        }else if (response.type == MessageTypes.ERROR){
            System.out.println(response.message);
            System.exit(1);
        }
    }

    //Send mesage
    private Message postMessage(int type, String username, int pid, VectorClock ts, String message){
        Message msg = new Message(MessageTypes.REGISTER, this.username, 0, null, "");
        Message.sendMessage(msg, this.datagramSocket, this.inetAddres, PORT);
        return Message.receiveMessage(this.datagramSocket);
    }

    //Network config
    private void initNetwork(){
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
