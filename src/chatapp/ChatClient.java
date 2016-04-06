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

public class ChatClient
{
    private final ClientEvent frontend;
    private String username = "";
    ServerConnection server = null;
    
    public ChatClient(ClientEvent frontend)
    {
        this.frontend = frontend;
    }
    
    public void start(String ip, int portNumber)
    {
        try
        {
            server = new ServerConnection(ip, portNumber);
            frontend.connectServer(ip, portNumber);
        }
        catch (IOException ex)
        {
            frontend.chatClientError("Failed to connect to server; " + ex.getMessage());
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stop()
    {
        frontend.disconnectServer();
        // TODO disconnect from server
    }
    
    public void setUserName(String name)
    {
        username = name;
    }
    
    public String getUserName()
    {
        return username;
    }
    
    public void sendMessage(String message) throws IOException
    {
        if(server != null)
        {
            server.sendPacket(ClientPacket.constructPublicMessagePacket(message));
            frontend.sendMessageToAll(message);
        }
    }
    
    public void sendMessage(String username, String message) throws IOException
    {
        if(server != null)
        {
            server.sendPacket(ClientPacket.constructPrivateMessagePacket(username, message));
            frontend.sendMessageToUser(username, message);
        }
    }

    private class ServerConnection extends Thread implements ClientPacket
    {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        boolean active = false;
        
        public ServerConnection(String IP, int port) throws IOException
        {
            super();
            socket = new Socket(IP, port);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            active = true;
        }
        
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
            catch (IOException ex)
            {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void setUserList(List<String> userlist)
        {
            frontend.receiveUserList(userlist);
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
        
        private void sendPacket(String packet) throws IOException
        {
            out.writeObject(packet);
        }
    }
}
