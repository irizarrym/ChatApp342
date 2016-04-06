/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.util.*;
import java.util.regex.*;

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
        String[] lines = packet.split(System.getProperty("line.separator"));
        Pattern username = Pattern.compile("^[A-Za-z0-9]+$");
        
        if(lines.length > 0)
        {
            // Identify header of packet
            switch(lines[0])
            {
                case "USERNAME": 
                {
                    if(lines.length < 3)
                    {
                        handler.packetError(packet, "USERNAME packet must contain three lines");
                        return;
                    }
                    
                    if(lines[1] != "")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Check that username only contains alphanumeric characters
                    Matcher m = username.matcher(lines[2]);
                    
                    if(m.matches())
                    {
                        handler.setUserName(lines[2]);
                    }
                    else
                    {
                        handler.packetError(packet, "username contains invalid characters");
                        return;
                    }
                } break;

                case "PUBLIC_MESSAGE":
                {
                    if(lines.length < 3)
                    {
                        handler.packetError(packet, "PUBLIC_MESSAGE packet must contain at least three lines");
                        return;
                    }
                    
                    if(lines[1] != "")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Construct new packet to be sent to clients
                    String newPacket = "PUBLIC_MESSAGE\n" + handler.getUserName() + "\n\n";
                    for(int i = 2; i < lines.length; ++i)
                    {
                        newPacket += lines[i];
                    }
                    
                    handler.sendPublicMessage(newPacket);
                } break;

                case "PRIVATE_MESSAGE":
                {
                    if(lines.length < 4)
                    {
                        handler.packetError(packet, "PRIVATE_MESSAGE packet must contain at least four lines");
                        return;
                    }
                    
                    if(lines[2] != "")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Check second line is a valid username
                    Matcher m = username.matcher(lines[1]);
                    if(!m.matches())
                    {
                        handler.packetError(packet, "username contains invalid characters");
                        return;
                    }
                    
                    // Construct new packet to be sent to the client
                    String newPacket = "PRIVATE_MESSAGE\n" + handler.getUserName() + "\n\n";
                    for(int i = 2; i < lines.length; ++i)
                    {
                        newPacket += lines[i];
                    }
                    
                    handler.sendPrivateMessage(lines[1], newPacket);
                } break;

                case "STATUS":
                {
                    if(lines.length < 3)
                    {
                        handler.packetError(packet, "STATUS packet must contain at least three lines");
                        return;
                    }
                    
                    if(lines[1] != "")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Construct message from remaining lines of packet
                    String message = "";
                    for(int i = 2; i < lines.length; ++i)
                    {
                        message += lines[i];
                    }
                    
                    handler.receiveStatusPacket(message, false);
                } break;
                
                case "OFFLINE":
                {
                	handler.disconnect();
                } break;

                case "ERROR":
                {
                    if(lines.length < 3)
                    {
                        handler.packetError(packet, "ERROR packet must contain at least three lines");
                        return;
                    }
                    
                    if(lines[1] != "")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Construct message from remaining lines of packet
                    String message = "";
                    for(int i = 2; i < lines.length; ++i)
                    {
                        message += lines[i];
                    }
                    
                    handler.receiveStatusPacket(message, true);
                } break;

                default:
                {
                    handler.packetError(packet, "unknown packet type");
                    return;
                } // break;
            }
        }
        else
        {
            handler.packetError(packet, "empty packet");
            return;
        }
    }
    
    /**
     * Construct a packet containing a user list to be sent to client(s)
     * 
     * @param userlist  List of usernames connected to server
     * @return          Packet containing list of usernames
     */
    public static String constructUserListPacket(List<String> userlist)
    {
        String result = "USERLIST\n\n";
        for(String s : userlist) result += s + "\n";
        return result;
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
        String result = isError ? "STATUS\n\n" : "ERROR\n\n";
        return result + message;
    }
    
    /**
     * Get the current username for this client
     * 
     * @return client username
     */
    public String getUserName();
    
    public void disconnect();
    
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
     * There was an error parsing the incoming packet due to an invalid header
     * or is improperly formatted
     * 
     * @param packet    Packet received from client
     * @param error     Specific error message
     */
    public void packetError(String packet, String error);
}
