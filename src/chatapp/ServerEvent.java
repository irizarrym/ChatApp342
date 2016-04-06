/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

/**
 * A basic interface to notify the server front end of certain events
 */
public interface ServerEvent
{
    public void startServer(int portNumber);
    public void stopServer();
    public void openConnection(String ip);
    public void closeConnection(String ip);
    public void setUserName(String ip, String username);
    public void sendMessageToUser(String from, String to, String message);
    public void sendMessageToAll(String from, String message);
    public void sendUserList(String username);
    public void receiveStatusPacket(String message, boolean isError);
    public void chatServerError(String err);
}
