/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A GUI frontend for the chat server which starts / stops the server
 * and logs events
 */
public class GuiServer extends JFrame implements ActionListener, ServerEvent
{
    private ChatServer server;
    private JTextField textPort;
    private JButton buttonStart;
    private JButton buttonStop;
    private JTextArea serverHistory;
    private JScrollPane scroll;
    private JList userList;
    private DefaultListModel userListModel;
    
    /**
     * Constructor initializes GUI and waits for user input
     */
    public GuiServer()
    {
        // Basic GUI setup and configuration
        super("Chat Server");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new BorderLayout());
        super.setSize(640, 480);
        
        // Initialize top panel
        textPort = new JTextField();
        textPort.setText("Enter Port Number like: 8888");
        textPort.selectAll();
        buttonStart = new JButton("Start Server");
        buttonStart.setEnabled(true);
        buttonStop = new JButton("Stop Server");
        buttonStop.setEnabled(false);
        textPort.addActionListener(this);
        buttonStart.addActionListener(this);
        buttonStop.addActionListener(this);
        JPanel topPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(textPort);
        topPanel.add(buttonStart);
        topPanel.add(buttonStop);
        super.add(topPanel, BorderLayout.NORTH);
        
        // Initialize history text area
        serverHistory = new JTextArea();
        serverHistory.setEditable(false);
        scroll = new JScrollPane(serverHistory);
        super.add(scroll, BorderLayout.CENTER);
        
        // Initializer user list
        userListModel = new DefaultListModel();
        userList = new JList(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(-1);
        userList.setPreferredSize(new Dimension(100, 100));
        userList.setBorder(BorderFactory.createLineBorder(Color.black));
        new JScrollPane(userList);
        super.add(userList, BorderLayout.WEST);
        
        // Initialize server backend
        server = new ChatServer(this);
        
        // Display GUI
        setLocationRelativeTo(null); // Center window on screen
        super.setVisible(true);
    }
    
    /**
     * Action listener for server GUI
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == textPort || e.getSource() == buttonStart)
        {
            try
            {
            	int portNumber = Integer.parseInt(textPort.getText());
            	server.start(portNumber);
            }
            catch (NumberFormatException ex)
            {
            	appendHistory("Please enter a port number.");
            }
        	
        }
        if(e.getSource() == buttonStop)
        {
            server.stop();
        }
    }

    /**
     * Event: Server has started listening for incoming connections on the
     * specified port number
     * 
     * @param portNumber    Port for incoming connections
     */
    @Override
    public void startServer(int portNumber)
    {
        appendHistory("Started server on port " + portNumber);
        textPort.setEnabled(false);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);
    }

    /**
     * Event: Server shutdown and terminated all active connections
     */
    @Override
    public void stopServer()
    {
        appendHistory("Stopped server");
        textPort.setEnabled(true);
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
    }

    /**
     * Event: Incoming connection received from the specified IP address
     * 
     * @param ip    IP address of incoming connection
     */
    @Override
    public void openConnection(String ip)
    {
        appendHistory("Open connection on " + ip);
    }

    /**
     * Event: Connection closed to the specified IP address
     * 
     * @param ip    IP address of closed 
     */
    @Override
    public void closeConnection(String ip)
    {
        appendHistory("Close connection on " + ip);
        userListModel.clear();
    }

    /**
     * Event: A client requested a new username
     * 
     * @param ip        IP address of client
     * @param username  Username requested by client
     */
    @Override
    public void setUserName(String ip, String username)
    {
        appendHistory(ip + " has set username to " + username);
        
        // Update user list
        userListModel.clear();
        for(String s : server.getUserList())
        {
            userListModel.addElement(s);
        }
    }

    /**
     * Event: A client is sending a private message to another client
     * 
     * @param from      Username of client sending this message
     * @param to        Username of client message is being forwarded to
     * @param message   Text of message
     */
    @Override
    public void sendMessageToUser(String from, String to, String message)
    {
        appendHistory(from + " has sent a private message to " + to + ": " + message);
    }

    /**
     * Event: A client is sending a public message to all other clients
     * 
     * @param from      Username of client sending this message
     * @param message   Text of message
     */
    @Override
    public void sendMessageToAll(String from, String message)
    {
        appendHistory(from + " has sent a public message: " + message);
    }

    /**
     * Event: An updated user list is being sent to the client with the
     * specified username
     * 
     * @param username  Username of the client receiving the user list
     */
    @Override
    public void sendUserList(String username)
    {
        appendHistory("Sent userlist to user " + username);
    }
    
    /**
     * A generic status or error message was received from a client
     * 
     * @param message   Message describing status or error
     * @param isError   True if error, false if status
     */
    @Override
    public void receiveStatusPacket(String message, boolean isError)
    {
        if(isError)
        {
            appendHistory("Client Error Message: " + message);
        }
        else
        {
            appendHistory("Client Status Message: " + message);
        }
    }

    /**
     * An internal error occurred in the server backend
     * 
     * @param err   String describing the internal error
     */
    @Override
    public void chatServerError(String err)
    {
        appendHistory("SERVER ERROR: " + err);
    }
    
    /**
     * Append text to the history log
     * 
     * @param line  Text to append
     */
    private void appendHistory(String line)
    {
        serverHistory.append(line + System.getProperty("line.separator"));
        serverHistory.setCaretPosition(serverHistory.getDocument().getLength());
    }
    
    /**
     * Entry point which launches the server GUI
     * 
     * @param args  Command line arguments
     */
    public static void main(String[] args)
    {
        GuiServer gui = new GuiServer();
    }
}
