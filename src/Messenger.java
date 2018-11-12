import java.io.*;
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

    public boolean sendMessage(String id, String message) {
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
            sendConnection.close();
            return true;
        }
        //If an exception is thrown print it
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

            try {
                connection = server.accept();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while(reader.ready()) {
                    incoming.add(reader.readLine());
                }


            }
            catch (SocketTimeoutException ignored) {
                // no incoming data - just ignore
            }
            catch (NullPointerException ignored){

            }
            catch(IOException e){

            }
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
