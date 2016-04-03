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
    private final iServerEvent frontend;
    
    public ChatServer(iServerEvent frontend)
    {
        this.frontend = frontend;
    }
    
    public void start(int portNumber)
    {
        
    }
    
    public void stop()
    {
        
    }
}
