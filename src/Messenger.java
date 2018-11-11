import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Queue;

public class Messenger implements Runnable {
    private int bufferSize = 140;
    private int timeout = 10;
    private OutputStream output;
    private InputStream input;
    private ServerSocket server;
    private ArrayList<String> incoming = new ArrayList<>();
    private Config c_ = new Config();
    private Socket connection;
    private int sleepTime = 100;

    public void sendMessage(String id, String message) {
        Socket sendConnection = null;
        //Try the following
        try {
            String hostname = Users.userInfo.get(id)[2];
            String portNumber = Users.userInfo.get(id)[3];
            //Create a new InetAddress object called address
            InetAddress address;
            //Create an integer called port
            int port;
            //Set the Inet address to the address of the server at the hostname parameter
            address = InetAddress.getByName(hostname);
            //Set the port integer to the portNumber parameter parsed to an integer
            port = Integer.parseInt(portNumber);
            //Set the connection socket to a new socket with the address and port values parsed as parameters
            sendConnection = new Socket(address, port);
            //Set the socket timeout to the timeout value
            sendConnection.setSoTimeout(timeout);
            //Print that that the server is being connected to and the server details
            System.out.println("--* Connecting to " + sendConnection.toString());
            //Set the output stream to the socket's output stream
            output = sendConnection.getOutputStream();
            output.write(message.getBytes());
            System.out.println("message sent");
        }
        //If an exception is thrown print it
        catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
    }

    public void run() {
        try {
            server = new ServerSocket(c_.mPort_); // make a socket
            System.out.println("--* Starting server " + server.toString());
            connection = server.accept();
            //Set the input stream property to the connection's input stream
            input = connection.getInputStream();
            //Instantiate a boolean called clientConnected as true
            boolean clientConnected = true;
            //Output that a connection was made and the inet address, host name, and port of the client
            System.out.println("New connection ... " +
                    connection.getInetAddress().getHostName() + ":" +
                    connection.getPort());
            //While the client connected boolean is true do the following
            while (clientConnected) {
                //Create a new array of bytes called buffer
                byte[] buffer = new byte[bufferSize];
                //Instantiate an integer called b as 0
                int b = 0;
                //If b is less than one do the following
                if (b < 1) {
                    //Call the sleep method on the thread with the sleepTime parameter
                    Thread.sleep(sleepTime);
                    //Set b to the input stream read to the buffer array
                    b = input.read(buffer);
                    //If first item in the array is 0 (null) close the connection and output that the client has
                    //disconnected, then set clientConnected to false
                    if (buffer[0] == 0) {
                        connection.close();
                        System.out.println("Client disconnected");
                        clientConnected = false;
                    }

                }

                //If b is more than 0 do the following
                if (b > 0) {
                    //Create a new array of bytes of size b called message
                    byte[] message = new byte[b];
                    //Copy the buffer array to the message array
                    System.arraycopy(buffer, 0, message, 0, b);
                    //Create a string called s from the message array
                    String s = new String(message);
                    incoming.add(s);
                }
            }


        } catch (IOException e) {
            System.err.println("IO Exception: " + e.getMessage());
        }
        //If exceptions are thrown print them
        catch (InterruptedException e) {
            System.err.println("Interrupted Exception: " + e.getMessage());
        }

    }

    public ArrayList<String> getIncoming() {
        ArrayList<String> incomingArray = new ArrayList<>();
        while (!incoming.isEmpty()) {
            incomingArray.add(incoming.remove(0));
        }
        return incomingArray;
    }
}
