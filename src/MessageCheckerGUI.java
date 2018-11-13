/*
 * CS2003 coursework Net2 demo
 * Saleem Bhatti, Oct 2018
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

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

    private HeartBeat heartBeat;

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
        setBounds(0, 0, 800, 225); // Size of Frame

        Panel p; // tmp variable

        p = new Panel();
        p.add(new Label("Notifications"));
        TextArea n = new TextArea("", 4, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
        p.add(n);
        add(p); // to this Frame
        notifications = new Notifications(n);

        p = new Panel();
        p.add(new Label("Users"));
        java.awt.List u = new List(4, false);

        u.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                messages.input.setText(u.getSelectedItem()+":");
            }
        });

        p.add(u);
        add(p);  // to this Frame

        users = new Users(u, notifications);
        Thread t = new Thread(users); // let it look after itself
        t.start();

        p = new Panel();
        Button b = new Button();
        b.setLabel("Go offline");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (heartBeat.c_.online) {
                    b.setLabel("Go online");
                    heartBeat.c_.online = false;
                } else {
                    b.setLabel("Go offline");
                    heartBeat.c_.online = true;
                }
            }
        });
        p.add(b);
        add(p);

        p = new Panel();
        Checkbox available = new Checkbox();
        available.setLabel("Available");
        available.setState(true);
        available.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(available.getState()){
                    HeartBeat.c_.available=true;
                    b.setLabel("Go offline");
                    HeartBeat.c_.online=true;
                }
                else{
                    heartBeat.c_.available=false;
                }
            }
        });
        p.add(available);
        add(p);

        // This is a separate Frame
        messages = new Messages(name, notifications);
        messages.setVisible(true);
        t = new Thread(messages); // let it look after itself
        t.start();

        heartBeat = new HeartBeat(name);
        t = new Thread(heartBeat);
        t.start();

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
