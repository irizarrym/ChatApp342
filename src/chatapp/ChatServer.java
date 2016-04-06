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

public class ChatServer
{
    private ServerSocket socket = null;
    private final ServerEvent frontend;
    private ArrayList<ClientConnection> clients;
    
    public ChatServer(ServerEvent frontend)
    {
        this.frontend = frontend;
        clients = new ArrayList<>();
    }
    
    /**
     * Listen for incoming connections on specified port number
     * 
     * @param portNumber
     */
    public void start(int portNumber)
    {
        // TODO
        frontend.startServer(portNumber);
    }
    
    /**
     * Stop listening for new connections and disconnect all active connections
     */
    public void stop()
    {
        // TODO
        frontend.stopServer();
    }
    
    /**
     * Each instance of this class handles a client connected to the server
     */
    private class ClientConnection extends Thread implements ServerPacket
    {
        private String username;
        
        public ClientConnection()
        {
            
        }
        
        @Override
        public void run()
        {
            // TODO listen for incoming traffic
        }
        
        @Override
        public String getUserName()
        {
            return username;
        }

        @Override
        public void setUserName(String username)
        {
            this.username = username;
        }

        @Override
        public void sendPublicMessage(String packet)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void sendPrivateMessage(String username, String packet)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void receiveStatusPacket(String status, boolean isError)
        {
            frontend.receiveStatusPacket(status, isError);
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void packetError(String packet, String error)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
