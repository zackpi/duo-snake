import java.io.*;
import java.net.*;

public class Client {

    private Socket server = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;

    public Client(String host, int port){
        try {
            server = new Socket(host, port);
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            DataInputStream in = new DataInputStream(server.getInputStream());
        }catch(IOException e) {
            e.printStackTrace();
        }
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
            server.close();
        }catch (IOException ioe){
            System.out.println("Failed to close connection to server");
        }
    }
}