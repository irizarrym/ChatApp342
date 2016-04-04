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
 * A general interface for handling packets received by the client
 */
public interface ClientPacket
{
    /**
     * Identifies the packet type and passes it's contents to the appropriate
     * function in the handler object
     * 
     * @param handler   Object which acts upon the packets contents
     * @param packet    Packet received by client from server
     */
    public static void processPacket(ClientPacket handler, String packet)
    {
        // TODO
    }
    
    /**
     * Construct a packet requesting the server to set your username
     * 
     * @param username  Username to be set for this client
     * @return          Packet containing set username request
     */
    public static String constructUserNamePacket(String username)
    {
        // TODO
        return "";
    }
    
    /**
     * Construct a packet containing a public message to be sent to all other
     * clients
     * 
     * @param message   Message to be sent to all clients
     * @return          Packet containing a public message
     */
    public static String constructPublicMessagePacket(String message)
    {
        // TODO
        return "";
    }
    
    /**
     * Construct a packet containing a private message to be sent to a specific
     * client
     * 
     * @param username  Client to send the message to
     * @param message   Message to be sent to client
     * @return          Packet containing a private message
     */
    public static String constructPrivateMessagePacket(String username, String message)
    {
        // TODO
        return "";
    }
    
    /**
     * Construct a packet sending a general status message to client(s)
     * 
     * @param message   Message describing the error
     * @param isError   The status message indicates an error
     * @return          Packet containing status/error message
     */
    public static String constructStatusPacket(String message, boolean isError)
    {
        // TODO
        return "";
    }
    
    /**
     * A new or updated user list was received from the server
     * 
     * @param userlist  User list received from server
     */
    public void setUserList(List<String> userlist);
    
    /**
     * Received public message from another client
     * 
     * @param username  Username of the client which sent this message
     * @param message   Message sent by the client
     */
    public void receivePublicMessage(String username, String message);
    
    /**
     * Received private message from another client
     * 
     * @param username  Username of the client which sent this message
     * @param message   Message sent by the client
     */
    public void receivePrivateMessage(String username, String message);
    
    /**
     * A general status or error message was received from a client
     * 
     * @param status    Status message
     * @param isError   True if status message indicates error; False otherwise
     */
    public void receiveStatusPacket(String status, boolean isError);
    
    /**
     * An unrecognized or invalid packet type was received from a client
     * 
     * @param packet    Packet received from client
     */
    public void unknownPacketType(String packet);
}
