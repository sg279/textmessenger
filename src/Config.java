/*
  Beacon v2 - Config information

  Saleem Bhatti, Oct 2018

  Send out a multicast heartbeat and listen out
  for other heartbeats.
*/

/*
  This is an object that gets passed around, containing useful information.
*/

import java.io.*;
import java.net.*;

public class Config {
    // https://www.iana.org/assignments/multicast-addresses/multicast-addresses.xhtml
    // 239.0.0.0 is organisation-local scope
    // these default values could be overriden in a config file
    public String mAddr_ = "239.42.42.42"; // CS2003 whole class gorup
    public int mPort_ = 10101; // random(ish)
    public int ttl_ = 4; // plenty for the lab
    public int soTimeout_ = 1; // ms
    public boolean loopbackOff_ = true; // ignore my own transmissions
    public boolean reuseAddr_ = true; // allow address use by otehr apps
    public boolean online = true;
    public boolean available = true;
    // application config
    public int msgSize_ = 256;

    // these should not be loaded from a config file, of course
    public InetAddress mGroup_;
    public String hostInfo_;

    Config() {
        InetAddress i;
        String s = "hostname";

        try {
            i = InetAddress.getLocalHost();
            s = i.getHostName();

            hostInfo_ = s;
        } catch (UnknownHostException e) {
            System.out.println("Problem: " + e.getMessage());
        }

  /*
    What is could be used here is some code that reads in
    the values of the variables above from a file that has
    name-value pairs, e.g.:

     mAddr: 239.42.42.42
     mPort: 4242
     ttl: 4
     soTimeout: 10

    and so on.

    This might be done by having a new constructor for
    Config that takes a filename as an argument, or just
    having Config look for a file with a given name, e.g.
    heartbeat.config, in the current dir and if it finds
    it, it will read new values for the variables above
    from that file.

    e.g. use the Properties class as documented at
    https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html
    */

    }
}
