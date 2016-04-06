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

public class GuiServer extends JFrame implements ActionListener, ServerEvent
{
    private ChatServer server;
    
    private JTextField textPort;
    private JButton buttonStart;
    private JButton buttonStop;
    private JTextArea serverHistory;
    
    public GuiServer()
    {
        // Basic GUI setup and configuration
        super("Chat Server");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new BorderLayout());
        super.setSize(640, 480);
        
        // Initialize top panel
        textPort = new JTextField();
        buttonStart = new JButton("Start Server");
        buttonStart.setEnabled(true);
        buttonStop = new JButton("Stop Server");
        buttonStop.setEnabled(false);
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
        super.add(serverHistory, BorderLayout.CENTER);
        
        // Initialize server backend
        server = new ChatServer(this);
        
        // Display GUI
        setLocationRelativeTo(null); // Center window on screen
        super.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == buttonStart)
        {
            int portNumber = Integer.parseInt(textPort.getText());
            server.start(portNumber);
        }
        if(e.getSource() == buttonStop)
        {
            server.stop();
        }
    }

    @Override
    public void startServer(int portNumber)
    {
        appendHistory("Started server on port " + portNumber);
        buttonStart.setEnabled(false);
        buttonStop.setEnabled(true);
    }

    @Override
    public void stopServer()
    {
        appendHistory("Stopped server");
        buttonStart.setEnabled(true);
        buttonStop.setEnabled(false);
    }

    @Override
    public void openConnection(String ip)
    {
        appendHistory("Open connection on " + ip);
    }

    @Override
    public void closeConnection(String ip)
    {
        appendHistory("Close connection on " + ip);
    }

    @Override
    public void setUserName(String ip, String username)
    {
        appendHistory(ip + " has set username to " + username);
    }

    @Override
    public void sendMessageToUser(String from, String to, String message)
    {
        appendHistory(from + " has sent a private message to " + to + ": " + message);
    }

    @Override
    public void sendMessageToAll(String from, String message)
    {
        appendHistory(from + " has sent a public message: " + message);
    }

    @Override
    public void sendUserList(String username)
    {
        appendHistory("Sent userlist to user " + username);
    }
    
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

    @Override
    public void chatServerError(String err)
    {
        appendHistory("SERVER ERROR: " + err);
    }
    
    private void appendHistory(String line)
    {
        serverHistory.append(line + System.getProperty("line.separator"));
    }
    
    public static void main(String[] args)
    {
        GuiServer gui = new GuiServer();
    }
}
