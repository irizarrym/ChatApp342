/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

public class ChatProtocol
{
    public static enum ServerPacket
    {
        Unknown,
        SetUserName,
        RequestUserList,
        SendMessageToUser,
        SendMessageToAll
    }
    
    public static enum ClientPacket
    {
        Unknown,
        ReceiveUserList,
        ReceivePublicMessage,
        ReceivePrivateMessage
    }
    
    public static ServerPacket serverPacketType(String packet)
    {
        // TODO
        return ServerPacket.Unknown;
    }
    
    public static ClientPacket clientPacketType(String packet)
    {
        // TODO
        return ClientPacket.Unknown;
    }
    
    public static String clientSetUserName(String username)
    {
        // TODO
        return "";
    }
    
    public static String clientRequestUserList()
    {
        // TODO
        return "";
    }
    
    public static String[] clientReceiveUserList(String packet)
    {
        // TODO
        return null;
    }
    
    public static String clientMessageToUser(String username, String message)
    {
        // TODO
        return "";
    }
    
    public static String clientMessageToAll(String[] userlist, String message)
    {
        // TODO
        return "";
    }
    
    public static String[] clientReceiveMessage(String packet)
    {
        // TODO
        return null;
    }
}
