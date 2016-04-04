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
 * A general interface for handling packets received by the server
 */
public interface ServerPacket
{
    /**
     * Identifies the packet type and passes it's contents to the appropriate
     * function in the handler object
     * 
     * @param handler   Object which acts upon the packets contents
     * @param packet    Packet received by server from client
     */
    public static void processPacket(ServerPacket handler, String packet)
    {
        // TODO
    }
    
    /**
     * Construct a packet containing a user list to be sent to client(s)
     * 
     * @param userlist  List of usernames connected to server
     * @return          Packet containing list of usernames
     */
    public static String constructUserListPacket(List<String> userlist)
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
     * A request was received from the client to set their username
     * 
     * @param username  Username requested by the client
     */
    public void setUserName(String username);
    
    /**
     * A public message is to be sent to all other clients
     * 
     * @param packet    Raw packet which can be sent directly over a socket
     */
    public void sendPublicMessage(String packet);
    
    /**
     * A private message is to be sent to a specific client
     * 
     * @param username  Username of the client to forward this packet to
     * @param packet    Raw packet which can be sent directly over a socket
     */
    public void sendPrivateMessage(String username, String packet);
    
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
