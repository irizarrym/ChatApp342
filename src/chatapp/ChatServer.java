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
    }
    
    /**
     * Stop listening for new connections and disconnect all active connections
     */
    public void stop()
    {
        // TODO
    }
    
    /**
     * Each instance of this class handles a client connected to the server
     */
    private class ClientConnection extends Thread implements ServerPacket
    {
        // TODO
    }
}
