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
        System.out.println(".____________________________________________.");
//        String[][] map = player.getMap(getConnectedRoomToken());
//        showMap(map);
//        System.out.println();
        boolean isYourTurn = player.turnStatus(getConnectedRoomToken(),getUserToken());
        //X - first Turn
        int resultOfCombination = player.checkCombination(getConnectedRoomToken(),figure);
        //1 - game over
        //0 - draw
        //2 - nextMove
        while(resultOfCombination == 2) // playing until don't have a draw or someone won
        {
            if(isYourTurn)
            {
                playGame(player,figure);
                resultOfCombination = player.checkCombination(getConnectedRoomToken(),figure);
                isYourTurn = player.turnStatus(getConnectedRoomToken(),getUserToken());
            }
            else {
                System.out.println("Waiting for the opponent to make a move...");
                System.out.println();
                while(!isYourTurn)
                {
                    isYourTurn = player.turnStatus(getConnectedRoomToken(),getUserToken());
                }
            }
        }
        if(resultOfCombination == 1)
        {
            System.out.println("End of the game. " + figure + "won!");
        }
        else if(resultOfCombination == 0)
        {
            System.out.println("End of the game. It's Draw!");
        }
        System.out.println("Would you like to play a new game (press 'n') or you want to leave from this room(press 'l')?");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine();
        if(answer.equals("n"))
        {
            startGameSession(player);
        }
        else if(answer.equals("l"))
        {
            System.out.println("Token of game room: " + getConnectedRoomToken());
            leaveGameRoomPanel(player);
        }

    }

    private void playGame(PlayerFeaturesInterface player,String figure) throws RemoteException {
        String[][] map = player.getMap(getConnectedRoomToken());
        showMap(map);
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the number in which you want enter a move:");
        int number = scanner.nextInt();
        int resultOfMove = player.makeMove(getConnectedRoomToken(),getUserToken(),figure,number);
            /* RESULT OF MOVE
              1 - EVERYTHING IS OKAY
              0 - ON THIS POSITION FIGURE IS ALREADY
             -1 - ERROR There is no slot with this number
             */
        if(resultOfMove == -1 || resultOfMove == 0)
        {
            while(resultOfMove ==-1 || resultOfMove == 0)
            {
//TODO                System.out.println("ERROR! Map don't have any slot with this number");
                System.out.println("Please try again");
                System.out.println("Type the number in which you want enter a move:");
                number = scanner.nextInt();
                resultOfMove = player.makeMove(getConnectedRoomToken(),getUserToken(),figure,number);
            }
        }
//        if(resultOfMove == 0) {
//            while(resultOfMove == 0)
//            {
//                System.out.println("On chosen position figure is already");
//                System.out.println("Please try another position");
//                System.out.println("Type the number in which you want enter a move:");
//                number = scanner.nextInt();
//                resultOfMove = player.makeMove(getConnectedRoomToken(),getUserToken(),figure,number);
//            }
//        }
        if(resultOfMove == 1)
        {
            System.out.println("The position № " + number + " now is " + figure);
//            map = player.getMap(getConnectedRoomToken());
//            showMap(map);
            System.out.println();
        }
        //SETTURNSTATUS -> FALSE
        //SETOPPONNENT -> TRUE
    }

    private void showMap(String[][] map)
    {
        System.out.println("-------------");
        for(int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map.length; j++)
            {
                System.out.print("| " + map[i][j] + " ");
            }
            System.out.println("|");
            System.out.println("|-----------|");
        }
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

        int numberOfPlayers = player.getNumberOfPlayersInRoom(roomToken);
        if(numberOfPlayers == 0)
        {
            System.out.println(player.deleteRoom(roomToken));
        }
        else {
            System.out.println("You can't delete this room, cause amount of people in room is not 0!");
        }
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
