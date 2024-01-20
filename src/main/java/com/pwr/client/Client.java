package com.pwr.client;

import com.pwr.server.PlayerFeaturesInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("|Leave a game room -- type [4]                        |");
        System.out.println("|Delete a game room -- type [5]                       |");
        System.out.println("|Exit from program -- type [0]                        |");
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
        int result = player.joinGameRoom(getUserToken(),roomToken);
        if(result == -1)
        {
            System.out.println("ERROR!The room with provided token doesn't exit.Try again!");
        }
        else if(result == 0)
        {
            System.out.println("You can't connect to this room, because lobby is full. Try again later or connect to other room.");
        }
        else if(result == 1)
        {
            setConnectedRoomToken(roomToken);
            System.out.println("You have successfully connected to room " + roomToken.substring(roomToken.indexOf("@")+1) + "!");
            int numberOfPlayers = player.getNumberOfPlayersInRoom(roomToken);
            showGameSession(roomToken.substring(roomToken.indexOf("@")+1),numberOfPlayers,player);
        }

    }

    private void showGameSession(String roomName, int numberOfPlayers, PlayerFeaturesInterface player) throws RemoteException {
        System.out.println();
        System.out.println("{---------------------------------------------------------}");
        System.out.println("                    " + roomName.toUpperCase() +" ROOM");
        System.out.println("Room token: " + getConnectedRoomToken());
        System.out.println("Number of players in room: " + numberOfPlayers);
        if(numberOfPlayers == 1)
        {
            System.out.println("Can't start a game.You need to wait for other player to join");
            System.out.println("Would you like to wait(press 'w') or u want to leave this room(press 'l'):");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            if(answer.equals("w"))
            {
                while (answer.equals("w"))
                {
                    if(player.getNumberOfPlayersInRoom(getConnectedRoomToken()) == 1)
                    {
                        System.out.println("Waiting to other player to join...");
                        System.out.println("Would you like to continue waiting(press 'w') or u want to leave this room(press 'l'):");
                        answer = scanner.nextLine();
                    } else if (player.getNumberOfPlayersInRoom(getConnectedRoomToken()) == 2) {
                        break;
                    }
                }
                if(answer.equals("l"))
                {
                    leaveGameRoomPanel(player);
                }
                else {
                    startGameSession(player);
                }
            } else if (answer.equals("l")) {
                leaveGameRoomPanel(player);
            }

        }
        else if(numberOfPlayers == 2)
        {
            startGameSession(player);
        }

    }

    private void startGameSession(PlayerFeaturesInterface player) throws RemoteException
    {
        System.out.println();
        System.out.println("-------------|[START OF GAME]|---------------");
        String figure = player.getFigureOfPlayer(getConnectedRoomToken(),getUserToken());
        String opponentToken = player.getTokenOfOpponent(getConnectedRoomToken(),getUserToken());
        System.out.println("Your figure is: " + figure);
        System.out.println("Your opponent is: " + opponentToken.substring(opponentToken.indexOf("@")+1));
        
    }

    public void leaveGameRoomPanel(PlayerFeaturesInterface player) throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Provide a token of game room that u want to leave");
        String roomToken = scanner.nextLine();
        int result = player.leaveGameRoom(getUserToken(),roomToken);
        if(result == -1)
        {
            System.out.println("ERROR! There is no room with this token. Please check the token of the room that u want to leave and try again!");
        }
        else if(result == 0)
        {
            System.out.println("ERROR! You can't leave this room, because you are not connected to this room!");
        }
        else if(result == 1)
        {
            System.out.println("You have successfully leave chosen room!");
        }
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
