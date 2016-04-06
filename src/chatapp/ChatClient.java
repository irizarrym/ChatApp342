/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.net.*;
import java.util.*;

public class ChatClient implements ClientPacket
{
    private Socket socket = null;
    private final ClientEvent frontend;
    private String username;
    
    public ChatClient(ClientEvent frontend)
    {
        this.frontend = frontend;
    }
    
    public void start(String ip, int portNumber)
    {
        frontend.connectServer(ip, portNumber);
        // TODO
    }
    
    public void stop()
    {
        frontend.disconnectServer();
        // TODO
    }
    
    public void setUserName(String name)
    {
        username = name;
    }
    
    public String getUserName()
    {
        return username;
    }
    
    public void sendMessage(String message)
    {
        frontend.sendMessageToAll(message);
        // TODO
    }
    
    public void sendMessage(String username, String message)
    {
        frontend.sendMessageToUser(username, message);
        // TODO
    }

    @Override
    public void setUserList(List<String> userlist)
    {
        frontend.receiveUserList(userlist);
        // TODO
    }

    @Override
    public void receivePublicMessage(String username, String message)
    {
        frontend.receiveMessage(username, message, false);
    }

    @Override
    public void receivePrivateMessage(String username, String message)
    {
        frontend.receiveMessage(username, message, true);
    }

    @Override
    public void receiveStatusPacket(String status, boolean isError)
    {
        frontend.receiveStatusPacket(status, isError);
    }

    @Override
    public void packetError(String packet, String error)
    {
        frontend.chatClientError("PACKET ERROR (" + error + "): " + packet);
    }
}
