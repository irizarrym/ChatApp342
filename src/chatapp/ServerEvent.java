/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

/**
 * A basic interface to notify the server front end of certain events
 */
public interface ServerEvent
{
    /**
     * Server started and is listening for incoming connections
     * 
     * @param portNumber    Open port to listen for incoming connections
     */
    public void startServer(int portNumber);
    
    /**
     * Server shutdown and all active clients were disconnected
     */
    public void stopServer();
    
    /**
     * New incoming connection
     * 
     * @param ip    IP address of connection
     */
    public void openConnection(String ip);
    
    /**
     * Closed active connection
     * 
     * @param ip    IP address of connection
     */
    public void closeConnection(String ip);
    
    /**
     * A request was received from a client to set their username
     * 
     * @param ip        IP address of client
     * @param username  Username requested by client
     */
    public void setUserName(String ip, String username);
    
    /**
     * A private message was received from a client
     * 
     * @param from      Username of client sending this message
     * @param to        Username of client receiving this message
     * @param message   Text of private message
     */
    public void sendMessageToUser(String from, String to, String message);
    
    /**
     * A public message was received from a client
     * 
     * @param from      Username of client sending this message
     * @param message   Text of public message
     */
    public void sendMessageToAll(String from, String message);
    
    /**
     * The list of usernames was sent to this client
     * 
     * @param username  Username of client receiving user list
     */
    public void sendUserList(String username);
    
    /**
     * A generic status/error packet was received from a client
     * 
     * @param message   Text describing status or error
     * @param isError   True if error, false if status
     */
    public void receiveStatusPacket(String message, boolean isError);
    
    /**
     * An internal error occurred in the server backend
     * 
     * @param err   Text describing the error
     */
    public void chatServerError(String err);
}
