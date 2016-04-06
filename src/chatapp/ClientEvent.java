/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.util.*;

/**
 * A basic interface to notify the client front end of certain events
 */
public interface ClientEvent
{
    public void connectServer(String ip, int portNumber);
    public void disconnectServer();
    public void setUserName(String username);
    public void receiveUserList(List<String> userlist);
    public void sendMessageToUser(String to, String message);
    public void sendMessageToAll(String message);
    public void receiveMessage(String from, String message, boolean isPrivate);
    public void receiveStatusPacket(String message, boolean isError);
    public void chatClientError(String err);
}
