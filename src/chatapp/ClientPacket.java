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
        String[] lines = packet.split("\\r?\\n");
        Pattern username = Pattern.compile(".+");
        
        if(lines.length > 0)
        {
            // Identify header of packet
            switch(lines[0])
            {
                case "USERLIST":
                {
                    if(lines.length < 3)
                    {
                        handler.packetError(packet, "USERLIST packet must contain three lines");
                        return;
                    }
                    
                    if(false && lines[1] != "\r\n")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Construct ArrayList of user names
                    ArrayList<String> userList = new ArrayList<>();
                    for(int i = 2; i < lines.length; ++i)
                    {
                        userList.add(lines[i]);
                    }
                    
                    handler.setUserList(userList);
                } break;

                case "PUBLIC_MESSAGE":
                {
                    if(lines.length < 4)
                    {
                        handler.packetError(packet, "PUBLIC_MESSAGE packet must contain four lines");
                        return;
                    }
                    
                    if(false && lines[2] != "")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Check username on second line
                    Matcher m = username.matcher(lines[1]);
                    if(!m.matches())
                    {
                        handler.packetError(packet, "username contains invalid characters");
                        return;
                    }
                    
                    // Construct message from remaining lines of packet
                    String message = "";
                    for(int i = 3; i < lines.length; ++i)
                    {
                        message += lines[i];
                    }
                    
                    handler.receivePublicMessage(lines[1], message);
                } break;

                case "PRIVATE_MESSAGE":
                {
                    if(lines.length < 4)
                    {
                        handler.packetError(packet, "PRIVATE_MESSAGE packet must contain four lines");
                        return;
                    }
                    
                    if(false && lines[2] != "")
                    {
                        handler.packetError(packet, "missing blank line between header and body");
                        return;
                    }
                    
                    // Check username on second line
                    Matcher m = username.matcher(lines[1]);
                    if(!m.matches())
                    {
                        handler.packetError(packet, "username contains invalid characters");
                        return;
                    }
                    
                    // Construct message from remaining lines of packet
                    String message = "";
                    for(int i = 3; i < lines.length; ++i)
                    {
                        message += lines[i];
                    }
                    
                    handler.receivePrivateMessage(lines[1], message);
                } break;

                case "STATUS":
                {
                    if(lines.length < 3)
                    {
                        handler.packetError(packet, "STATUS packet must contain at least three lines");
                        return;
                    }
                    
                    if(false && lines[1] != "")
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

                case "ERROR":
                {
                    if(lines.length < 3)
                    {
                        handler.packetError(packet, "ERROR packet must contain at least three lines");
                        return;
                    }
                    
                    if(false && lines[1] != "")
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
     * Construct a packet requesting the server to set your username
     * 
     * @param username  Username to be set for this client
     * @return          Packet containing set username request
     */
    public static String constructUserNamePacket(String username)
    {
        return "USERNAME\n\n" + username;
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
        return "PUBLIC_MESSAGE\n\n" + message;
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
        return "PRIVATE_MESSAGE\n" + username + "\n\n" + message;
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
     * There was an error parsing the incoming packet due to an invalid header
     * or is improperly formatted
     * 
     * @param packet    Packet received from client
     * @param error     Specific error message
     */
    public void packetError(String packet, String error);
}
