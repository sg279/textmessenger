import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ChatViewer extends Frame
        implements ActionListener,
        WindowListener {

    private String name;

    public ChatViewer(String name ){
        super(name + " : chats");
        super.addWindowListener(new WindowAdapter() {});
        setLayout(new FlowLayout());
        setBounds(0, 0, 800, 325); // Size of Frame

        Panel p; // tmp variable

        p = new Panel();
        p.add(new Label("Chat"));
        TextArea c = new TextArea("", 10, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
        p.add(c);
        add(p); // to this Frame

        p = new Panel();
        p.add(new Label("Users"));
        java.awt.List u = new List(4, false);

        String directory = System.getProperty("user.dir")+"/chats/";
        for (String user: new File(directory).list()
             ) {
            u.add(user);
        }


        u.addActionListener(new ActionListener() {
            @Override
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
