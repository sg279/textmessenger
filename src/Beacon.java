/*
  Beacon

  Saleem Bhatti, Oct 2018

  Send out a multicast heartbeat and listen out
  for other heartbeats.

*/

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

/**
 * This class sends out a beacon every five seconds to the UDP group and reads incoming beacons and adds them to a list of active users
 * and stores their data
 */
public class Beacon implements Runnable{

    private String userName;
    public static Config c_=new Config();
    private MulticastEndpoint m_;
    static Set<String> users = new HashSet<>();
    static Map<String, String[]> userBeacons = new HashMap<>();

    public void run() {

        c_ = new Config();
        m_ = new MulticastEndpoint(c_);
        m_.join();
        //Clear the users list, sends a beacon to the UDP group and for five seconds call the rxHeartBeat method that reads incoming
        //beacons, adds the sender to a list of active users and stores their information
        while (true) {
            try {
                long end = System.currentTimeMillis() + 5000;
                users.clear();
                txHeartBeat();
                while (System.currentTimeMillis() <= end) {
                    rxHeartBeat();
                }

            } catch (Exception e) {
            }

        }
        //m_.leave();
    }

    public Beacon(String id){
        userName=id;
    }

    /**
     * This method reads incoming beacons, adds the sender to a list of active users and stores their information
     */
    private void rxHeartBeat() {
        try {
            byte[] b = new byte[c_.msgSize_];
            //If there is a beacon to read read the bytes to a byte array b and do the following
            if (m_.rx(b) && b.length > 0) {
                //Remove the square brackets from the beacon string and split the components to an array
                String beacon = new String(b);
                beacon = beacon.replaceAll("]", "");
                beacon = beacon.replaceFirst("\\[", "");
                String[] beaconArray = beacon.split("\\[");
                //Rearrange the array to be placed in the map of user data with the user's ID as the key
                String[] userInfoArray = {beaconArray[0].trim(), beaconArray[2].trim(), beaconArray[3].trim(), beaconArray[4].trim()};
                //If the user's beacon says they are online add them to the list of online users and save their information to the map
                if (beaconArray[2].toLowerCase().equals("online")) {
                    userBeacons.put(beaconArray[1], userInfoArray);
                    users.add(beaconArray[1]);
                }
                //If the user is listed as unavailable add them to the list of users and save their information with 'unavailable' after their name
                if (beaconArray[2].toLowerCase().equals("unavailable")) {
                    userBeacons.put(beaconArray[1]+" (unavailable)", userInfoArray);
                    users.add(beaconArray[1]+" (unavailable)");
                }
                System.out.println("-> rx : " + new String(b).trim());
            }

        } catch (Exception e) {
        }
    }

    /**
     * This method sends the beacon to the UDP group
     */
    private void txHeartBeat() {
        byte[] b = new byte[0];
        //Create the beacon string with the heartbeat method
        String h = getBeacon();
        try {
            b = h.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Problem: " + e.getMessage());
        }
        //Send the byte array to the UDP group and print to the console if the message sends
        if (m_.tx(b)) {
            System.out.println("<- tx : " + new String(b).trim());
        }
    }

    /**
     * This method creates and returns the beacon
     * @return A timestamp, the user ID, the user status, the user FQDN, and the local port separated by square brackets
     */
    private String getBeacon() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
        String now = sdf.format(new Date());
        //Set the user status based on the config property
        String status = "online";
        if(!c_.available){
            status="unavailable";
        }
        if (!c_.online) {
            status = "offline";
        }
        String s = "[" + now + "][" + userName + "][" + status + "][" + c_.hostInfo_ + "][21251]";
        return s;
    }

}
