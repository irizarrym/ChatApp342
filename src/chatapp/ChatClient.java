/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.*;

public class ChatClient 
{
    private final ClientEvent frontend;
    private String username = "";
    ServerConnection server = null;
    private boolean connected = false;
    
    /**
     * Constructor which receives an object for the frontend
     * 
     * @param frontend  A generic frontend which receives and displays output
     *                  from the backend
     */
    public ChatClient(ClientEvent frontend)
    {
        this.frontend = frontend;
    }
    
    /**
     * Is the client connected to a server?
     * 
     * @return True if connected, else false
     */
    public boolean connected()
    {
    	return connected;
    }
    
    /**
     * Connect to a chat server at the specified IP address and port number
     * 
     * @param ip            IP address of server
     * @param portNumber    Destination port number
     */
    public void start(String ip, int portNumber)
    {
        if(connected) return;
    	
    	try
        {
            server = new ServerConnection(ip, portNumber);
            frontend.connectServer(ip, portNumber);
            connected = true;
        }
        catch (IOException ex)
        {
            frontend.chatClientError("Failed to connect to server; " + ex.getMessage());
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Stop any active connection to the server
     */
    public void stop()
    {
        if(!connected) return;
    	
    	try
        {
            server.disconnect();
            frontend.disconnectServer();
            connected = false;
        }
        catch (IOException ex)
        {
            frontend.chatClientError("Failed to stop server; " + ex.getMessage());
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Set the username for this client
     * 
     * @param name  Username for this client
     */
    public void setUserName(String name)
    {
        username = name;
        if(connected) server.changeUserName(username);
    }
    
    /**
     * Gets the currently set username for this client
     * 
     * @return  Username for this client
     */
    public String getUserName()
    {
        return username;
    }
    
    /**
     * Send a public message to all clients on the server
     * 
     * @param message   Text of public message
     */
    public void sendMessage(String message)
    {
        if(server != null)
    	
        {
            try
            {
                server.sendPacket(ClientPacket.constructPublicMessagePacket(message));
            	
                frontend.sendMessageToAll(message);
            }
            catch (IOException ex)
            {
                frontend.chatClientError("Failed to send message; " + ex.getMessage());
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Send a private message to the client with the specified username
     * 
     * @param username  Username of client
     * @param message   Text of private message
     */
    public void sendMessage(String username, String message)
    {
        if(server != null)
    	
        {
            try
            {
                server.sendPacket(ClientPacket.constructPrivateMessagePacket(username, message));
                
                frontend.sendMessageToUser(username, message);
            }
            catch (IOException ex)
            {
                frontend.chatClientError("Failed to send message; " + ex.getMessage());
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Creates a new connection to a chat server and listens for incoming
     * traffic on a new thread
     */
    private class ServerConnection extends Thread implements ClientPacket
    {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        boolean active = false;
        
        /**
         * Connects to the server at the specified IP address and port number
         * and starts a new thread which listens for incoming traffic from the
         * server
         * 
         * @param IP    IP address of server
         * @param port  Destination port number
         * @throws IOException 
         */
        public ServerConnection(String IP, int port) throws IOException
        {
            super();
            socket = new Socket(IP, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            active = true;
            changeUserName(username);
            super.start();
        }
        
        /**
         * Stop any active connections to the server
         * 
         * @throws IOException 
         */
        public void disconnect() throws IOException
        {
            out.writeObject("OFFLINE");
        	
        	try
            {
                in.close();
                out.close();
                socket.close();
            }
            finally
            {
                active = false;
            }
        }
        
        /**
         * Entry point for the new thread which listens for incoming traffic
         * and forwards the packet to the proper handler
         */
        @Override
        public void run()
        {
            try
            {
                String packet;
                while((packet = (String)in.readObject()) != null)
                {
                    ClientPacket.processPacket(this, packet);
                }
                
                in.close();
                out.close();
                socket.close();
            }
            catch (EOFException ex)
            {
            	active = false;
            	connected = false;
            	frontend.chatClientError("Server offline.");
                frontend.disconnectServer();
            }
            catch (SocketException ex)
        	{
        		
        	}
            catch (IOException ex)
            {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        /**
         * Sends a request to the server to change the username of this client
         * 
         * @param username New username for this client
         */
        public void changeUserName(String username)
        {
        	try 
        	{
				out.writeObject(ClientPacket.constructUserNamePacket(username));
			} 
        	catch (IOException e) 
        	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        /**
         * Event: A list of user names was received from the server
         * 
         * @param userlist 
         */
        @Override
        public void setUserList(List<String> userlist)
        {
            frontend.receiveUserList(userlist);
        }

        /**
         * Event: A public message was received from the server sent by 
         * another client
         * 
         * @param senderName    Username of client which sent this message
         * @param message       Text of public message
         */
        @Override
        public void receivePublicMessage(String senderName, String message)
        {
            if(!username.equals(senderName)) frontend.receiveMessage(senderName, message, false);
        }

        /**
         * Event: A private message was received from the server sent by
         * another client
         * 
         * @param username      Username of client which sent this message
         * @param message       Text of private message
         */
        @Override
        public void receivePrivateMessage(String username, String message)
        {
            frontend.receiveMessage(username, message, true);
        }

        /**
         * Event: A generic status/error packet was received from the server
         * 
         * @param status    Text describing the status or error
         * @param isError   True if error, false if status
         */
        @Override
        public void receiveStatusPacket(String status, boolean isError)
        {
            frontend.receiveStatusPacket(status, isError);
        }

        /**
         * Event: An error occurred parsing this packet
         * 
         * @param packet    Raw text of packet
         * @param error     Text describing the error
         */
        @Override
        public void packetError(String packet, String error)
        {
            frontend.chatClientError("PACKET ERROR (" + error + "): " + packet);
        }
        
        /**
         * Send a raw packet to the server
         * 
         * @param packet        Raw text of packet
         * @throws IOException 
         */
        private void sendPacket(String packet) throws IOException
        {
            out.writeObject(packet);
        }
    }
    
}
