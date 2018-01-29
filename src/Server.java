import java.io.*;
import java.net.*;

public class Server extends Thread {
    private ServerSocket server;
    private Socket client = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
        }catch(IOException ioe){
            System.out.println("Could not initialize server");
        }
    }

    public void start() {
        try {
            Socket client = server.accept();
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
        }catch(IOException ioe){
            System.out.println("Unable to initialize connection to client");
        }

    }

    public void run(){

    }

    public int read(){
        try{
            return in.readInt();
        }catch(IOException ioe){
            System.out.println("Failed to read from server");
            return -1;
        }
    }

    public void write(int w){
        try{
            out.write(w);
        }catch(IOException ioe){
            System.out.println("Failed to write to server");
        }
    }

    public void close(){
        try{
            client.close();
            server.close();
        }catch (IOException ioe){
            System.out.println("Failed to close connection to server");
        }
    }

}