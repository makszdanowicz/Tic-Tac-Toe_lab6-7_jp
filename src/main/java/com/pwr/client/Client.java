package com.pwr.client;

import com.pwr.server.GameRoom;
import com.pwr.server.PlayerFeaturesInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userToken;
    private String role;
    private String connectedRoomToken;

    public Client(Socket socket,String userToken)
    {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userToken = userToken;
        }catch (IOException e)
        {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getConnectedRoomToken() {
        return connectedRoomToken;
    }

    public void setConnectedRoomToken(String connectedRoomToken) {
        this.connectedRoomToken = connectedRoomToken;
    }

    public void sendMessage(String message)
    {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (IOException e)
        {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void listenMessage()
    {
        try{
            String messageFromSession = bufferedReader.readLine();
            System.out.println("Server: "+messageFromSession);
        }
        catch (IOException e)
        {
            System.out.println("Closing");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void playerMenuPanel()
    {
        System.out.println(".-----------------------------------------------------.");
        System.out.println("|In player mode you can:                              |");
        System.out.println("|Create a game room -- type [1]                       |");
        System.out.println("|See the list of created rooms -- type [2]            |");
        System.out.println("|Join a game room -- type [3]                         |");
        System.out.println("|Delete a game room -- type [4]                       |");
        System.out.println("|Exit from program -- type [5]                        |");
        System.out.println("._____________________________________________________.");
        System.out.println("Type and send option that u want to do:");
    }

    public void createRoomPanel(PlayerFeaturesInterface player) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("                   CREATING A GAME ROOM");
        System.out.println("Provide a room name: ");
        String roomName = scanner.nextLine();
        System.out.println(player.createGameRoom(roomName));
        //String gameRoomToken = UUID.randomUUID().toString() + "@" + roomName;


    }

    public void checkListOfRoomsPanel(PlayerFeaturesInterface player) throws RemoteException {
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("                    LIST OF GAME ROOMS");
        List<String> gameRooms = player.showRooms();
        for(String room : gameRooms)
        {
            System.out.println(room);
        }

    }

    public void joinGameRoomPanel(PlayerFeaturesInterface player) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Provide a token of game room, in which u want to connect: ");
        String roomToken = scanner.nextLine();
        System.out.println(player.joinGameRoom(getUserToken(),roomToken));

    }

    public void deleteGameRoomPanel(PlayerFeaturesInterface player) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Provide a token of game room that u want do delete: ");
        String roomToken = scanner.nextLine();
        System.out.println(player.deleteRoom(roomToken));
    }



    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
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
