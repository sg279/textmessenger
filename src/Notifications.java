/*
 * CS2003 coursework Net2 demo
 * Saleem Bhatti, Oct 2018
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class Notifications {

    private TextArea notifications;

    Notifications(TextArea n) {
        notifications = n;
    }

    void notify(String s) {
        SimpleDateFormat d = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");
        String t = d.format(new Date());
        s = t + " " + s + "\n";
        notifications.insert(s, 0); // add to top
    } // notify()

} // class Notifications
