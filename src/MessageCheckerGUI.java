/*
 * CS2003 coursework Net2 demo
 * Saleem Bhatti, Oct 2018
 */

import java.awt.*;
import java.awt.event.*;

/*
 * For convenience, a list of relevant documentation for AWT widgest used.
 * Frame https://docs.oracle.com/javase/8/docs/api/java/awt/Frame.html
 * WindowListener https://docs.oracle.com/javase/8/docs/api/java/awt/event/WindowListener.html
 * ActionListener https://docs.oracle.com/javase/8/docs/api/java/awt/event/ActionListener.html
 * FlowLayout https://docs.oracle.com/javase/8/docs/api/java/awt/FlowLayout.html
 * Label https://docs.oracle.com/javase/8/docs/api/java/awt/Label.html
 * List https://docs.oracle.com/javase/8/docs/api/java/awt/List.html
 * Panel https://docs.oracle.com/javase/8/docs/api/java/awt/Panel.html
 * TextField https://docs.oracle.com/javase/8/docs/api/java/awt/TextField.html
 * TextArea https://docs.oracle.com/javase/8/docs/api/java/awt/TextArea.html
 */

public class MessageCheckerGUI
        extends Frame
        implements ActionListener,
        WindowListener {
    // Label for the GUI
    private String name;

    // For outgoing and incoming messages.
    private Messages messages;

    // Where new users will be listed.
    private Users users;

    // Where general information and notifications are displayed.
    private Notifications notifications;

    //Create a Beacon property
    private Beacon beacon;

    public MessageCheckerGUI(String name) {
        super(name + " : notifications and users"); // call the Frame constructor
        super.addWindowListener(new WindowAdapter() {});

    /*
     * The AWT code below lays out the widgets as follows.

     +------------------ Frame ---------------------+
     |                                              |
     |  +-------- Panel (Notifications) ---------+  |
     |  | +-- Label --+ +------ TextArea ------+ |  |
     |  | |           | |                      | |  |
     |  | +-----------+ +----------------------+ |  |
     |  +----------------------------------------+  |
     |                                              |
     |  +------------ Panel (Users) -------------+  |
     |  | +-- Label --+ +-------- List --------+ |  |
     |  | |           | |                      | |  |
     |  | +-----------+ +----------------------+ |  |
     |  +----------------------------------------+  |
     |                                              |
     +----------------------------------------------+

     * The Frame and Panel objects are not visible -- they
     * form part of the GUI construction.
     *
     */

        /*
         * Simple GUI layout - FlowLayout.
         * GridBagLayout would be better, giving more precise control
         * over layout, but would require a lot more code.
         */
        setLayout(new FlowLayout());
        setBounds(0, 0, 800, 425); // Size of Frame

        Panel p; // tmp variable

        /**
         * This panel displays the notification the user receives
         */
        p = new Panel();
        p.add(new Label("Notifications"));
        TextArea n = new TextArea("", 10, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
        p.add(n);
        add(p); // to this Frame
        notifications = new Notifications(n);


        /**
         * This panel displays the users that are online in a java.awt list
         */
        p = new Panel();
        p.add(new Label("Users"));
        java.awt.List u = new List(10, false);
        //Add an action listener to the list of users that enters the user's name is the messages bar when clicked
        u.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                messages.input.setText(u.getSelectedItem()+":");
            }
        });
        p.add(u);
        add(p);  // to this Frame
        //Run an instance of a Users class in its own thread
        users = new Users(u, notifications);
        Thread t = new Thread(users); // let it look after itself
        t.start();


        /**
         * This panel contains the button that allows the user to go offline
         */
        p = new Panel();
        Button b = new Button();
        b.setLabel("Go offline");
        //Add an action listener that toggles the user's online status in the config file and the button label to match
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (beacon.c_.online) {
                    b.setLabel("Go online");
                    beacon.c_.online = false;
                } else {
                    b.setLabel("Go offline");
                    beacon.c_.online = true;
                }
            }
        });
        p.add(b);
        add(p);


        /**
         * This panel contains a checkbox that allows the user to mark themselves as unavailable
         */
        p = new Panel();
        Checkbox available = new Checkbox();
        available.setLabel("Available");
        available.setState(true);
        //Add an action listener to change the user's availability status in the beacon's
        //config property to match the checkbox when it is changed
        available.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                //When the user sets themselves to available also set online to true
                if(available.getState()){
                    Beacon.c_.available=true;
                    b.setLabel("Go offline");
                    Beacon.c_.online=true;
                }
                else{
                    beacon.c_.available=false;
                }
            }
        });
        p.add(available);
        add(p);


        /**
         * Create and display a messages window and run an instance of the messages class in a new thread
         */
        messages = new Messages(name, notifications);
        messages.setVisible(true);
        t = new Thread(messages); // let it look after itself
        t.start();


        /**
         * Set the beacon property to a new beacon and run the beacon in a new thread
         */
        beacon = new Beacon(name);
        t = new Thread(beacon);
        t.start();


        /**
         * Create a new chat viewer window and display it
         */
        ChatViewer chatViewer = new ChatViewer(name);
        chatViewer.setVisible(true);

    } // MessageCheckerGUI()


    @Override
    public void windowClosing(WindowEvent we) {
        Window w = we.getWindow();
        w.dispose();
        System.exit(0);
    }

    /*
     * These are required for WindowListener, but we are
     * not interested in them, so they are empty methods.
     */
    @Override
    public void windowClosed(WindowEvent we) {
    }

    @Override
    public void windowActivated(WindowEvent we) {
    }

    @Override
    public void windowDeactivated(WindowEvent we) {
    }

    @Override
    public void windowIconified(WindowEvent we) {
    }

    @Override
    public void windowDeiconified(WindowEvent we) {
    }

    @Override
    public void windowOpened(WindowEvent we) {
    }

    /*
     * ActionListener method - required.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
    } // empty

} // class MessageCheckerGUI
