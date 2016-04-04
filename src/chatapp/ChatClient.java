/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.net.*;

public class ChatClient
{
    private Socket socket = null;
    private final ClientEvent frontend;
    
    public ChatClient(ClientEvent frontend)
    {
        this.frontend = frontend;
    }
    
    public void start(String ip, int portNumber)
    {
        // TODO
    }
    
    public void stop()
    {
        // TODO
    }
}
