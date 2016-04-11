/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.util.*;

/**
 * A basic interface to notify the client front end of certain events
 * from the backend
 */
public interface ClientEvent
{
    /**
     * Connected to the server
     * 
     * @param ip            IP address of the server
     * @param portNumber    Destination port number
     */
    public void connectServer(String ip, int portNumber);
    
    /**
     * Disconnected from server
     */
    public void disconnectServer();
    
    /**
     * Set username for this client
     * 
     * @param username  Username set for this client
     */
    public void setUserName(String username);
    
    /**
     * User list received from server
     * 
     * @param userlist  List of usernames of clients connected to this server
     */
    public void receiveUserList(List<String> userlist);
    
    /**
     * Sent private message to a specific client
     * 
     * @param to        Username of client to forward this message to
     * @param message   Text of private message
     */
    public void sendMessageToUser(String to, String message);
    
    /**
     * Sent a public message to all clients
     * 
     * @param message   Text of public message
     */
    public void sendMessageToAll(String message);
    
    /**
     * Received a message from the server
     * 
     * @param from          Username of client which sent this message
     * @param message       Text of message
     * @param isPrivate     True if private message, false if public message
     */
    public void receiveMessage(String from, String message, boolean isPrivate);
    public void receiveStatusPacket(String message, boolean isError);
    public void chatClientError(String err);
}
