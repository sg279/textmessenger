/*
 * CS2003 coursework Net2 demo
 * Saleem Bhatti, Oct 2018
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Users implements Runnable {

  private java.awt.List users; // java.util.List also exists!
  private Notifications notifications;

  private final static int sleepTime = 5000; // ms, 5s between checks

  Users(java.awt.List u, Notifications n) {
    users = u;
    notifications = n;
  }

  /*
   * Runnable method - required.
   * Control plane messages - discovery of other users.
   */
  @Override
  public void run()
  {
    while (true) { // forever

      // Check the list of users
      ArrayList<String> checklist = MessageCheckerCommon.users_list();

      /*
      ** If any of the currently listed users are no longer on the checklist,
      ** they have now gone offline.
      */
      for (int u = 0; u < users.getItemCount(); ++u) {
        String s_u = users.getItem(u);
        boolean found = false;

        for (int c = 0; c < checklist.size(); ++c) {
          String s_c = checklist.get(c);
          if (s_u.equals(s_c)) {
            found = true;
            checklist.remove(c); // finished checking this one
            break;
          }
        }

        if (!found) { // user has gone offline
          notifications.notify(s_u + " - offline.");
          users.remove(u);
        }
      } // for (u < users.size())

      /*
      ** If the checklist contains users not on the list of current users,
      ** they must have just come online.
      */
      for (int c = 0; c < checklist.size(); ++c) {
        String s_c = checklist.get(c);
        notifications.notify(s_c + " - online.");
        users.add(s_c);
      }
      checklist.clear(); // not strictly necessary

      try { Thread.sleep(sleepTime); } // do not need to check constantly
      catch (InterruptedException e) { } // do not care

    } // while(true)

  } // run()

} // class Users