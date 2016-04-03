/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

/**
 * Facilities for constructing and extracting packets for the chat protocol
 */
public class ChatProtocol
{
    /**
     * Packet types that can be received by the server
     */
    public static enum ServerPacket
    {
        Unknown,
        Error,
        SetUserName,
        SendMessageToUser,
        SendMessageToAll
    }
    
    /**
     * Packet types that can be received by the client
     */
    public static enum ClientPacket
    {
        Unknown,
        Error,
        ReceiveUserList,
        ReceivePublicMessage,
        ReceivePrivateMessage
    }
    
    /**
     * Identify the type of incoming packet to the server
     * 
     * @param packet    Packet received from client
     * @return          Packet type
     */
    public static ServerPacket serverPacketType(String packet)
    {
        // TODO
        return ServerPacket.Unknown;
    }
    
    /**
     * Extract the username that a client requested
     * 
     * @param packet    Packet received from client
     * @return          Username that the client requested
     */
    public static String serverReceiveUserName(String packet)
    {
        // TODO
        return "";
    }
    
    /**
     * Construct a user list packet to send to clients
     * 
     * @param userlist  List of usernames
     * @return          Packet containing user list
     */
    public static String serverSendUserList(String[] userlist)
    {
        // TODO
        return "";
    }
    
    /**
     * Extract the username and message from a SendMessageToUser packet
     * 
     * @param packet    Packet received from client
     * @return          String array containing:
     *                  [username, message]
     */
    public static String[] serverReceiveMessageToUser(String packet)
    {
        // TODO
        return null;
    }
    
    /**
     * Extract the message from a SendMessageToAll packet
     * 
     * @param packet    Packet received from client
     * @return          Message to send to all clients
     */
    public static String[] serverReceiveMessageToAll(String packet)
    {
        // TODO
        return null;
    }
    
    /**
     * Construct a packet containing a private message to a client
     * 
     * @param from      Client username that sent the packet
     * @param message   Message being sent to client
     * @return          Packet containing private message
     */
    public static String serverSendMessage(String from, String message)
    {
        // TODO
        return "";
    }
    
    /**
     * Construct a packet containing a public message for all clients
     * 
     * @param message   Message being sent to all clients
     * @return          Packet containing public message
     */
    public static String serverSendMessageToAll(String from, String message)
    {
        // TODO
        return "";
    }
    
    
    
    
    
    /**
     * Identify the type of incoming packet to the client
     * 
     * @param packet    Packet received from server
     * @return          Packet type
     */
    public static ClientPacket clientPacketType(String packet)
    {
        // TODO
        return ClientPacket.Unknown;
    }
    
    /**
     * Construct a packet for the server requesting a certain username
     * 
     * @param username  Username that the client requests
     * @return          Packet containing username request
     */
    public static String clientSendUserName(String username)
    {
        // TODO
        return "";
    }
    
    /**
     * Extract the usernames received from a server packet
     * 
     * @param packet    Packet received from server
     * @return          String array containing list of usernames
     */
    public static String[] clientReceiveUserList(String packet)
    {
        // TODO
        return null;
    }
    
    /**
     * Construct a packet containing a private message to be sent to a client
     * 
     * @param username  Client username to send message to
     * @param message   Message to be sent to client
     * @return          Packet containing a private message
     */
    public static String clientSendMessageToUser(String username, String message)
    {
        // TODO
        return "";
    }
    
    /**
     * Construct a packet containing a public message to be sent to all clients
     * 
     * @param message   Message to be sent to all clients
     * @return          Packet containing a public message
     */
    public static String clientSendMessageToAll(String message)
    {
        // TODO
        return "";
    }
    
    /**
     * Extract the username and message from a public or private message packet
     * 
     * @param packet    Message packet received from server
     * @return          String array containing:
     *                  [username, message]
     */
    public static String[] clientReceiveMessage(String packet)
    {
        // TODO
        return null;
    }
    
    
    
    
    
    /**
     * Extract an error message received from a server or client
     * 
     * @param packet    Packet received from server or client
     * @return          Error message stored in packet
     */
    public static String receiveError(String packet)
    {
        // TODO
        return "";
    }
}
