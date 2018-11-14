import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * This class creates a window that allows the user to open and read previous conversations
 */
public class ChatViewer extends Frame
        implements ActionListener,
        WindowListener {

    private String name;

    public ChatViewer(String name ){
        super(name + " : chats");
        super.addWindowListener(new WindowAdapter() {});
        setLayout(new FlowLayout());
        setBounds(0, 0, 800, 425); // Size of Frame

        Panel p; // tmp variable

        //Add a text area in which to display previous conversations
        p = new Panel();
        p.add(new Label("Chat"));
        TextArea c = new TextArea("", 10, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
        p.add(c);
        add(p);

        //Add a list panel that displays which user's the user has had conversations with
        p = new Panel();
        p.add(new Label("Users"));
        java.awt.List u = new List(10, false);

        //Get the directory which contains the src folder and add the name of every file in the chats folder to the user's window
        String directory = System.getProperty("user.dir")+"/chats/";
        if(new File(directory).list().length==0){
            c.insert("No previous conversations.",0);
        }
        for (String user: new File(directory).list()
             ) {
            u.add(user);
        }

        //Add an action listener for the list of past user chats
        u.addActionListener(new ActionListener() {
            @Override
            //When an item is clicked on read the selected file and output the content to the chats panel
            public void actionPerformed(ActionEvent actionEvent) {
                c.setText("");
                String filename = directory+u.getSelectedItem();
                try{
                    FileReader fileReader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    int i = 0;
                    while(bufferedReader.ready()){
                        c.insert(bufferedReader.readLine()+"\n",i);
                        i++;
                    }
                    bufferedReader.close();
                    fileReader.close();
                }
                catch(FileNotFoundException e){}
                catch(IOException e){}
            }
        });

        p.add(u);
        add(p);  // to this Frame
    }

    @Override
    public void windowClosing(WindowEvent we) {
        Window w = we.getWindow();
        w.dispose();
        System.exit(0);
    }

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

}
