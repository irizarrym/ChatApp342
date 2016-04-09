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
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.List;

public class GuiClient extends JFrame implements ActionListener, ClientEvent
{
    private ChatClient client;
    private String serverIP;
    private int serverPort;
    
    private MenuBar menuBar;
    private JLabel inputName;
    private JTextField nameField;
    private JButton connectButton;
    private JButton changeName;
    private JList userList;
    private DefaultListModel userListModel;
    private JTextArea chatHistory;
    private JScrollPane scroll;
    private JTextField messageBox;
    private JButton sendButton;
    
    public GuiClient()
    {
        // Basic GUI setup and configuration
        super("Chat Client");
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new BorderLayout());
        super.setSize(640, 480);
        
        // Create menu bar
        menuBar = new MenuBar();
        super.add(menuBar.getMenu(), BorderLayout.NORTH);
        
        
        
        
        // Initializer user list
        userListModel = new DefaultListModel();
        userList = new JList(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setLayoutOrientation(JList.VERTICAL);
        userList.setVisibleRowCount(-1);
        userList.setPreferredSize(new Dimension(100, 100));
        userList.setBorder(BorderFactory.createLineBorder(Color.black));
        new JScrollPane(userList);
        // userListModel.addElement("(All Users)");
        super.add(userList, BorderLayout.WEST);
        
        // Initialize chat history
        chatHistory = new JTextArea("Starting client...\n");
        chatHistory.setEditable(false);
        chatHistory.setBorder(BorderFactory.createLineBorder(Color.black));
        scroll = new JScrollPane(chatHistory);
        
        // Initialize rename and connection buttons
        inputName = new JLabel("Enter Username:");
        nameField = new JTextField("New Client " + (int)(Math.random()*10000));
        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        changeName = new JButton("Change Username");
        changeName.addActionListener(this);
        JPanel connectPanel = new JPanel();
        connectPanel.add(inputName);
        connectPanel.add(nameField);
        connectPanel.add(connectButton);
        connectPanel.add(changeName);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(connectPanel, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);
        super.add(centerPanel, BorderLayout.CENTER);
        
        // Initialize message box and send button
        messageBox = new JTextField();
        messageBox.addActionListener(this);
        messageBox.setEnabled(false);
        sendButton = new JButton("Send");
        sendButton.addActionListener(this);
        sendButton.setEnabled(false);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageBox, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        super.add(bottomPanel, BorderLayout.SOUTH);
        
        // Initialize client backend
        client = new ChatClient(this);
        
        // Display GUI
        setLocationRelativeTo(null); // Center window on screen
        super.setVisible(true);
        
        // Ask for default values
        askServerIP(true);
        askServerPort(true);
        askUserName(true);
        
        appendHistory("Please enter a username and connect.");
    }
    
    private class MenuBar implements ActionListener
    {
        private JMenuBar menuBar;
        private JMenu menuServer;
        private JMenuItem menuConnect;
        private JMenuItem menuDisconnect;
        private JMenuItem menuSetIP;
        private JMenuItem menuSetUserName;
        private JMenu menuDebug;
        private JMenuItem menuStatus;
        
        public MenuBar()
        {
            menuBar = new JMenuBar();
            menuServer = new JMenu("Server");
            menuConnect = new JMenuItem("Connect");
            menuDisconnect = new JMenuItem("Disconnect");
            menuSetIP = new JMenuItem("Set Server IP and Port");
            menuSetUserName = new JMenuItem("Set Username");
            menuDebug = new JMenu("Debug");
            menuStatus = new JMenuItem("Send Test Status");
            
            menuBar.add(menuServer);
            menuBar.add(menuDebug);
            
            menuServer.add(menuConnect);
            menuServer.add(menuDisconnect);
            menuServer.add(menuSetIP);
            menuServer.add(menuSetUserName);
            
            menuDebug.add(menuStatus);
            
            menuConnect.addActionListener(this);
            menuDisconnect.addActionListener(this);
            menuSetIP.addActionListener(this);
            menuSetUserName.addActionListener(this);
            menuStatus.addActionListener(this);
        }
        
        public JMenuBar getMenu()
        {
            return menuBar;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == menuConnect)
            {
                client.start(serverIP, serverPort);
            }
            else if(e.getSource() == menuDisconnect)
            {
                client.stop();
            }
            else if(e.getSource() == menuSetIP)
            {
                askServerIP(false);
                askServerPort(false);
            }
            else if(e.getSource() == menuSetUserName)
            {
                askUserName(false);
            }
            else if(e.getSource() == menuStatus)
            {
                receiveStatusPacket("Test status", false);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == sendButton || e.getSource() == messageBox)
        {
            if(userList.getSelectedIndex() < 0)
            {
                appendHistory("Please select a receiver from the left, then try again.\nFor public message, select (All users).");
            }
            else if(messageBox.getText().equals(""))
            {
            	return;
            }
            else if(userList.getSelectedIndex() == 0)
            {
                client.sendMessage(messageBox.getText());
            }
            else
            {
                client.sendMessage(userList.getSelectedValue().toString(),
                    messageBox.getText());
            }
            
            messageBox.setText("");
        }
        else if(e.getSource() == connectButton)
        {
        	askUserName(false);
        	client.start(serverIP, serverPort);
        }
        else if(e.getSource() == changeName)
        {
        	askUserName(false);
        }
    }

    @Override
    public void connectServer(String ip, int portNumber)
    {
        appendHistory("Connected to server at " + ip + ":" + portNumber);
        messageBox.setEnabled(true);
        sendButton.setEnabled(true);
        connectButton.setEnabled(false);
    }

    @Override
    public void disconnectServer()
    {
        appendHistory("Disconnected from server");
        messageBox.setEnabled(false);
        sendButton.setEnabled(false);
        connectButton.setEnabled(true);
        messageBox.setText("");
        userListModel.clear();
    }

    @Override
    public void setUserName(String username)
    {
        client.setUserName(username);
        appendHistory("Set username to: " + username);
    }

    @Override
    public void receiveUserList(List<String> userlist)
    {
    	userListModel.clear();
        userListModel.addElement("(All Users)");
        for(String s : userlist)
        {
            userListModel.addElement(s);
        }
    }

    @Override
    public void sendMessageToUser(String to, String message)
    {
        appendHistory("<" + client.getUserName() + " to " + to + "> " + message);
    }

    @Override
    public void sendMessageToAll(String message)
    {
        appendHistory("<" + client.getUserName() + " to (All Users)> " + message);
    }

    @Override
    public void receiveMessage(String from, String message, boolean isPrivate)
    {
        if(isPrivate)
        {
            appendHistory("<" + from + " (private)> " + message);
        }
        else
        {
            appendHistory("<" + from + "> " + message);
        }
    }
    
    @Override
    public void receiveStatusPacket(String message, boolean isError)
    {
        if(isError)
        {
            appendHistory("Server error: " + message);
        }
        else
        {
            appendHistory("Server status: " + message);
        }
    }

    @Override
    public void chatClientError(String err)
    {
        appendHistory("ERROR: " + err);
    }
    
    private void askServerIP(boolean force)
    {
        final String message = "Type in the server IP address:";
        String result = null;
        
        do
        {
            result = JOptionPane.showInputDialog(message);
        } while(force && (result == null || result.equals("")));
        
        if(result != null) serverIP = result;
    }
    
    private void askServerPort(boolean force)
    {
        final String message = "Type in the server port number:";
        String result = null;
        
        do
        {
            result = JOptionPane.showInputDialog(message);
        } while(force && (result == null || result.equals("")));
        
        if(result != null) serverPort = Integer.parseInt(result);
    }
    
    private void askUserName(boolean force)
    {
        final String message = "Type in a username:";   
        String result = nameField.getText();
        
        while(force && (result == null || result.equals("")))
        {
            result = JOptionPane.showInputDialog(message);
        } 
        
        if(result != null) client.setUserName(result);
    }
    
    private void appendHistory(String line)
    {
        chatHistory.append(line + System.getProperty("line.separator"));
        chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
    }
    
    public static void main(String[] args)
    {
        GuiClient gui = new GuiClient();
    }
}
