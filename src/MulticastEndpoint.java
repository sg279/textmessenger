/*
  HeartBeat - multicast goop wrapper.

  Saleem Bhatti, Oct 2018

  Send out a multicast heartbeat and
  listen out for other heartbeats.

*/

/*
  This encapsulates the process of setting up a multicast
  communication endpoint, as well as sending and receiving
  from that endpoint.
*/

import java.io.*;
import java.net.*;

public class MulticastEndpoint
{
  // https://docs.oracle.com/javase/8/docs/api/java/net/MulticastSocket.html
  MulticastSocket mSocket_;
  Config          c_;

  MulticastEndpoint(Config config)
  {
    InetAddress  mGroup;
    c_ = config;

    try {
      mGroup   = InetAddress.getByName(c_.mAddr_);
      mSocket_ = new MulticastSocket(c_.mPort_);

      mSocket_.setLoopbackMode(c_.loopbackOff_);
      mSocket_.setReuseAddress(c_.reuseAddr_);
      mSocket_.setTimeToLive(c_.ttl_);
      mSocket_.setSoTimeout(c_.soTimeout_); // non-blocking

      config.mGroup_ = mGroup;
    }

    catch (IOException e) {
      System.out.println("MulticastEndpoint() problem: " + e.getMessage());
    }
  }

  void join()
  {
    try {
      mSocket_.joinGroup(c_.mGroup_);
    }
    catch (IOException e) {
      System.out.println("join() problem: " + e.getMessage());
    }
  }

  void leave()
  {
    try {
      mSocket_.leaveGroup(c_.mGroup_);
      mSocket_.close();
    }
    catch (IOException e) {
      System.out.println("leave() problem: " + e.getMessage());
    }
  }

  boolean rx(byte b[])
  {
    boolean done;
    DatagramPacket d;

    done = false;
    d = new DatagramPacket(b, b.length);

    try {
      mSocket_.receive(d);
      done = true;
    }
    catch (SocketTimeoutException e) {
      // do nothing
    }
    catch (IOException e) {
      System.out.println("rx() problem: " + e.getMessage());
    }

    return done;
  }

  boolean tx(byte b[])
  {
    boolean done;
    DatagramPacket d;

    done = false;
    try {
      d = new DatagramPacket(b, b.length, c_.mGroup_, c_.mPort_);
      mSocket_.send(d);
      done = true;
    }

    catch (SocketTimeoutException e) {
      System.out.println("tx() problem: could not send - " + e.getMessage());
    }
    catch (IOException e) {
      System.out.println("tx() problem: " + e.getMessage());
    }

    return done;
  }
}
