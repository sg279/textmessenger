/*
  HeartBeat

  Saleem Bhatti, Oct 2018

  Send out a multicast heartbeat and listen out
  for other heartbeats.

*/

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class HeartBeat implements Runnable{

    private String userName;
    public static Config c_=new Config();
    private MulticastEndpoint m_;
    static Set<String> users = new HashSet<>();
    static Map<String, String[]> userBeacons = new HashMap<>();

    public void run() {
        c_ = new Config();
        m_ = new MulticastEndpoint(c_);

        m_.join();
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

    public HeartBeat(String id){
        userName=id;
    }


    private void rxHeartBeat() {
        try {
            byte[] b = new byte[c_.msgSize_];
            if (m_.rx(b) && b.length > 0) {
                String beacon = new String(b);
                beacon = beacon.replaceAll("]", "");
                beacon = beacon.replaceFirst("\\[", "");
                String[] beaconArray = beacon.split("\\[");
                String[] userInfoArray = {beaconArray[0].trim(), beaconArray[2].trim(), beaconArray[3].trim(), beaconArray[4].trim()};
                if (beaconArray[2].toLowerCase().equals("online")) {
                    userBeacons.put(beaconArray[1], userInfoArray);
                    users.add(beaconArray[1]);
                }
                if (beaconArray[2].toLowerCase().equals("unavailable")) {
                    userBeacons.put(beaconArray[1]+" (unavailable)", userInfoArray);
                    users.add(beaconArray[1]+" (unavailable)");
                }
                System.out.println("-> rx : " + new String(b).trim());
            }

        } catch (Exception e) {
        }
    }

    private void txHeartBeat() {
        byte[] b = new byte[0];
        String h = heartBeat();

        try {
            b = h.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Problem: " + e.getMessage());
        }

        if (m_.tx(b)) {
            System.out.println("<- tx : " + new String(b).trim());
        }
    }


    private String heartBeat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss.SSS");
        String now = sdf.format(new Date());
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
