package com.pwr.client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket)
    {
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);// this represents a ClientHandler object, so we sent information to array list
            broadcastMessage("SERVER: " + clientUserName + "has entered a game session!");
        } catch (IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    @Override
    public void run() {

    }

    public void broadcastMessage(String message)
    {
        for(ClientHandler clientHandler: clientHandlers)
        {
            try{
                if(!clientHandler.clientUserName.equals(clientUserName))
                {
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.flush();
                }
            }
            catch (IOException e)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler()
    {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUserName + " has left the group session");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        removeClientHandler();
        try{
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
