import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class TCPManager implements Runnable {
    private int timeout = 10;
    private OutputStream output;
    private ServerSocket server;
    //A list of all received messages
    private ArrayList<String> incoming = new ArrayList<>();
    private Socket connection;

    public boolean sendMessage(String id, String message) {
        Socket sendConnection;
        //Get the receiver's FQDN and port number from the user info map, open a TCP connection
        //with them, send the message, and close the connection
        try {
            String hostname = Users.userInfo.get(id)[2];
            String portNumber = Users.userInfo.get(id)[3];
            InetAddress address;
            int port;
            address = InetAddress.getByName(hostname);
            port = Integer.parseInt(portNumber);
            sendConnection = new Socket(address, port);
            sendConnection.setSoTimeout(timeout);
            System.out.println("--* Connecting to " + sendConnection.toString());
            output = sendConnection.getOutputStream();
            output.write(message.getBytes());
            System.out.println("message sent");
            sendConnection.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    public void run() {

        try {
            server = new ServerSocket(21251); // make a socket
            server.setSoTimeout(10);
            System.out.println("--* Starting server " + server.toString());
        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
        while (true) {
            //If the user is online wait for a connection to be made
            if(Beacon.c_.online) {
                try {
                    connection = server.accept();
                    //Read any received messages and add them to the array list of received messages
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while (reader.ready()) {
                        incoming.add(reader.readLine());
                    }
                    connection.close();



                } catch (SocketTimeoutException ignored) {
                    // no incoming data - just ignore
                } catch (NullPointerException ignored) {

                } catch (IOException e) {

                }
            }
        }


    }

    //Return an array of the received messages and clear the array
    public ArrayList<String> getIncoming() {
        ArrayList<String> incomingArray = new ArrayList<>();
        incomingArray.addAll(incoming);
        incoming.clear();
        return incomingArray;
    }
}
