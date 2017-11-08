package edu.gvsu.cis.cis656;
/*
    This is the UI thread class
 */
import edu.gvsu.cis.cis656.clock.VectorClock;
import edu.gvsu.cis.cis656.message.Message;
import edu.gvsu.cis.cis656.message.MessageComparator;
import edu.gvsu.cis.cis656.message.MessageTypes;
import edu.gvsu.cis.cis656.queue.PriorityQueue;
import org.json.JSONObject;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    static String HOST = "localhost";
    static int PORT = 8000;
    int selfPid;
    VectorClock vectorClock;
    PriorityQueue<Message> priorityQueue;
    String username;
    DatagramSocket datagramSocket;
    InetAddress inetAddres;
    Message topMessage;
    Boolean printCondition;

    public ChatClient(String username){
        this.username = username;
    }

    public void start(){
        this.initNetwork();
        // Client registration
        this.clientRegistration();
        System.out.println("Hello " + this.username + " you entered to the chat.");
        // Init Message listener thread
        Thread msgListener = new Thread(new MessageListener(this.datagramSocket, this));
        msgListener.start();
        // Command line reader
        Scanner reader = new Scanner(System.in);
        String clInput = "";
        Message response;
        while (true){
            System.out.print("Write a message or 'exit' to finish: ");
            clInput = reader.nextLine();
            switch (clInput){
                case "exit":
                    System.out.println("Finishsing...");
                    msgListener.interrupt();
                    System.exit(0);
                    break;
                case "":
                    break;
                default:
                    this.vectorClock.tick(this.selfPid);
                    this.postMessage(MessageTypes.CHAT_MSG, this.username, this.selfPid, this.vectorClock, clInput.toString());
                    break;
            }
        }
    }

    /*
     Auxiliar functions
     */

    // Receiver function that recieves inputMessages fro Listener
    public void messageReceiver(Message receivedMessage){
        this.priorityQueue.add(receivedMessage);
        topMessage = this.priorityQueue.peek();
        while (topMessage != null){
            printCondition = checkForPrint(topMessage.ts, topMessage.pid);
            if (printCondition){
                System.out.println();
                System.out.println(receivedMessage.sender + ": " + topMessage.message);
                System.out.print("Write a message or 'exit' to finish: ");
                this.priorityQueue.remove(topMessage);
                this.vectorClock.update(topMessage.ts);
                topMessage = this.priorityQueue.peek();
            }else{
                topMessage = null;
            }
        }
    }

    // This is similar to happened before method from vector clock, but for printing this validation should exclude the pid of the messagesender
    private Boolean happenedBeforeForPrint(VectorClock senderVC, int senderVCPid){
        JSONObject jsonObject = new JSONObject(this.vectorClock.toString());
        Boolean happenedBefore = true;
        for(String key: jsonObject.keySet()){
            if(!key.equals(Integer.toString(senderVCPid)) && jsonObject.getInt(key) < senderVC.getTime(Integer.parseInt(key))){
                happenedBefore = false;
                break;
            }
        }
        return happenedBefore;
    }
    // Print condition checker
    // Time del pid del que me envio mensaje tiene que ser +1 del que yo le tengo
    // los otros times del que me envio mensaje tienen q ser menores e iguales al mío
    private Boolean checkForPrint(VectorClock senderVC, int senderVCPid){
        Boolean happenedBefore = happenedBeforeForPrint(senderVC, senderVCPid);
        if (senderVC.getTime(senderVCPid) == this.vectorClock.getTime(senderVCPid) + 1 && happenedBefore)
        {
            return true;
        }
        return false;
    }

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
        Message msg = new Message(type, username, pid, ts, message);
        Message.sendMessage(msg, this.datagramSocket, this.inetAddres, PORT);
        // This validation is to avoid waiting for a server answer when the message is not a registration message
        if (type != MessageTypes.REGISTER) {
            return null;
        }
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
