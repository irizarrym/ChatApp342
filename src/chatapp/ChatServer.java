/*
    Group 58:
    Michael Irizarry (miriza6@uic.edu)
    Wenkan Zhu (wzhu24@uic.edu)

    CS 342 - Project 4
    Networked Chat Application
 */

package chatapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer
{
    private ServerSocket socket = null;
    private static ServerEvent frontend;
    private static ArrayList<ClientConnection> clients;
    private ClientListener listener;
    
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
        listener = new ClientListener(portNumber);
    }
    
    /**
     * Stop listening for new connections and disconnect all active connections
     */
    public void stop()
    {
        try
        {
        	socket.close();
        	for(int i=0;i<clients.size();i++)
        	{
        		if(clients.get(i).active) clients.get(i).disconnect();
        	}
        	frontend.stopServer();
        }
        catch (IOException e)
        {
        	System.err.println("Could not stop.");
        	System.exit(1);
        }
    }
    
    public static void refreshUsers() throws IOException
    {
    	List<String> userlist = new ArrayList<String>();
    	
    	for(int i=0;i<clients.size();i++)
    	{
    		if(clients.get(i).active) userlist.add(clients.get(i).getUserName());
    	}
    	
    	for(int i=0;i<clients.size();i++)
    	{
    		if(clients.get(i).active) 
    		{
    			clients.get(i).sendPacket(ServerPacket.constructUserListPacket(userlist));
    			frontend.sendUserList(clients.get(i).getUserName());
    		}
    	}
    }
    
    public static void publicMessage(String packet)
    {
    	for(int i=0;i<clients.size();i++)
    	{
    		if(clients.get(i).active)
    		{
				try
				{
					clients.get(i).sendPacket(packet);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
    		}
    	}
    }
    
    public static void privateMessage(String to, String packet)
    {
    	for(int i=0;i<clients.size();i++)
    	{
    		if(clients.get(i).active && clients.get(i).getUserName().equals(to))
    		{
				try
				{
					clients.get(i).sendPacket(packet);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
    		}
    		else
    		{
    			frontend.chatServerError("Receiver not found.");
    		}
    	}
    }
    
    private class ClientListener extends Thread
    {
    	int portNumber;
    	
    	public ClientListener (int port)
    	{
    		portNumber = port;
    		start();
    	}
    	
    	@Override
    	public void run()
    	{
    		try
            {
    			socket = new ServerSocket(portNumber);
    			frontend.startServer(portNumber);
    			try
    			{
    				while(true)
    				{
    					clients.add(new ClientConnection (socket.accept()));
                        refreshUsers();
    				}
    			}
    			catch (IOException e)
    			{
    				System.err.println("Accept failed.");
    			}
    		} 
            catch (IOException e) 
            {
    			System.err.println("Could not start.");
    			//System.exit(1);
    		}
    	}
    }
    
    /**
     * Each instance of this class handles a client connected to the server
     */
    private class ClientConnection extends Thread implements ServerPacket
    {
        private String ip;
    	private String username;
        private Socket clientSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        boolean active = false;
        
        public ClientConnection(Socket clientSoc) throws IOException
        {
            super();
        	clientSocket = clientSoc;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            ip = clientSocket.getRemoteSocketAddress().toString();
            // username = "New Client";
            username = ip;
            active = true;
            frontend.openConnection(ip);
            super.start();
        }
        
        @Override
        public void run()
        {
        	
        	try
            {
                String packet;
                while((packet = (String)in.readObject()) != null)
                {
                    ServerPacket.processPacket(this, packet);
                }
                
            }
            catch (IOException ex)
            {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void disconnect()
        {
        	if(active)
        	{
        		try
        		{
        			in.close();
					out.close();
					clientSocket.close();
				} 
        		catch (IOException e) 
        		{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		finally
        		{
        			active = false;
                    try
                    {
                        ChatServer.refreshUsers();
                        frontend.closeConnection(ip);
                    }
                    catch (IOException ex)
                    {
                        Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
        		}
        	}
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
            frontend.setUserName(ip, username);
            try
            {
                ChatServer.refreshUsers();
            }
            catch (IOException ex)
            {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void sendPacket(String packet) throws IOException
        {
        	out.writeObject(packet);
        }

        @Override
        public void sendPublicMessage(String packet)
        {
            ChatServer.publicMessage(packet);
        	frontend.sendMessageToAll(username, packet);
        }

        @Override
        public void sendPrivateMessage(String to, String packet)
        {
            ChatServer.privateMessage(to, packet);
        	frontend.sendMessageToUser(username, to, packet);
        }

        @Override
        public void receiveStatusPacket(String status, boolean isError)
        {
            frontend.receiveStatusPacket(status, isError);
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void packetError(String packet, String error)
        {
            frontend.chatServerError(packet + " : " + error);
        }
    }
}
