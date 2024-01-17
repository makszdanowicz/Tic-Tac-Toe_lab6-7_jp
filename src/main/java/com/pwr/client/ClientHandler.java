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
    private String role;

    public ClientHandler(Socket clientSocket,String name)
    {
        try{
            this.socket = clientSocket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = name;
            //this.clientUserName = bufferedReader.readLine();
            //this.role = role;
            clientHandlers.add(this);// this represents a ClientHandler object, so we sent information to array list
            broadcastMessage("SERVER: " + clientUserName + "has entered a game session!");
        } catch (IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    @Override
    public void run() {
        try {
            String messageFromSession = bufferedReader.readLine();
            System.out.println(messageFromSession);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
