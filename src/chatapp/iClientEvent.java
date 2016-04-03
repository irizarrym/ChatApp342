/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

public interface iClientEvent
{
    public void connectServer(int portNumber);
    public void disconnectServer();
    public void setUserName(String username);
    public void requestUserList();
    public void sendMessageToUser(String to, String message);
    public void sendMessageToAll(String message);
    public void receiveMessage(String from, String message, boolean isPrivate);
    public void chatClientError(String err);
}
