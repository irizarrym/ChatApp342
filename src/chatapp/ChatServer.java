/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.net.*;

public class ChatServer
{
    private ServerSocket socket = null;
    private final ServerEvent frontend;
    
    public ChatServer(ServerEvent frontend)
    {
        this.frontend = frontend;
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
}
