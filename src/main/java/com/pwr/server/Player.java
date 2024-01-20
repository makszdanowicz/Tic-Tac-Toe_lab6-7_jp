package com.pwr.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Player extends UnicastRemoteObject implements PlayerFeaturesInterface {

    private static HashMap<String, GameRoom> gameRooms= new HashMap<>();

    //UnicastRemoteObject ze nasze objekty tej klasy moga przekazywac sie do klienta  po RMI Serveru
    public Player () throws RemoteException
    {
        super();
    }
    @Override
    public String createGameRoom(String roomName) throws RemoteException {
        String roomToken = UUID.randomUUID().toString() + "@" + roomName;
        if(gameRooms.containsKey(roomToken))
        {
            //System.out.println("ERROR!Can't create a new game room using this token, cause room with this token already exists!");
            return "ERROR!Can't create a new game room using this token, cause room with this token already exists!";
        }
        else {
            gameRooms.put(roomToken, new GameRoom(roomName,roomToken));
            //System.out.println("Your room named " + roomName + " was created!");
            //System.out.println("If u want to connect to this room use this token: " + roomToken);
            return "The room named " + roomName + " was created!\n" + "If u want to connect to this room use this token: " + roomToken;
        }

    }

    @Override
    public int joinGameRoom(String playerToken, String roomToken) throws RemoteException {
        /*
           result -1 - ERROR! The room with provided token doesn't exit.Try again!
           result 0 - You can't connect to this room, because lobby is full. Try again later or connect to other room.
           result 1 - You have successfully connected to room!
         */
        int result = -1;
        for(String key : gameRooms.keySet())
        {
            if(key.equals(roomToken))
            {
                GameRoom room = gameRooms.get(roomToken);
                int status = room.addNewPlayer(playerToken);
                if(status == 0)
                {
                    result = 0;
                    return 0;
                } else if (status == 1) {
                    result = 1;
                    return 1;
                }
            }
        }
        return result;
    }

    @Override
    public int leaveGameRoom(String playerToken, String roomToken) throws RemoteException {
        if(!gameRooms.containsKey(roomToken))
        {
            return -1;
            //nie ma takiej room
        }
        GameRoom room = gameRooms.get(roomToken);
        if(!room.hasPlayer(playerToken))
        {
            return 0;
            //nie ma takiego gracza w tej room
        }
        room.removePlayer(playerToken);
        //zostal usuniety z room
        return 1;

    }

    @Override
    public List<String> showRooms() throws RemoteException {
        List<String> gameRoomsList = new ArrayList<>();
        if(gameRooms.size() == 0)
        {
            String string = "No rooms have created yet";
            gameRoomsList.add(string);
            return gameRoomsList;
        }
        for(Map.Entry<String,GameRoom> entry: gameRooms.entrySet())
        {
            //System.out.println("name: " + entry.getKey() + " | token: " + entry.getValue());
            String string = "name: " + entry.getValue().getName() + " | number of player: " + entry.getValue().getPlayersNumber()+ " | token: " + entry.getKey();
            gameRoomsList.add(string);
        }
        return gameRoomsList;
    }

    @Override
    public String deleteRoom(String roomToken) throws RemoteException {
        for(String key : gameRooms.keySet())
        {
            if(key.equals(roomToken))
            {
                gameRooms.remove(key);
                return "Room with token: " + roomToken +" was successfully deleted!";
            }
        }
        return "Server don't have any room with token: " + roomToken + ".Try again.";
    }

    @Override
    public int getNumberOfPlayersInRoom(String roomToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        int numberOfPlayersInSession = room.getPlayersNumber();
        return numberOfPlayersInSession;
    }

    @Override
    public String getTokenOfOpponent(String roomToken, String playerToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getOpponentToken(playerToken);
    }

    @Override
    public String getFigureOfPlayer(String roomToken,String playerToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getPlayerFigure(playerToken);
    }

    @Override
    public String[][] getMap(String roomToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getMap();
    }

    @Override
    public boolean turnStatus(String roomToken,String playerToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        if(room.getTokenOfTurnPlayer().equals(playerToken))
        {
            return true;
        }
        else return false;
    }

    @Override
    public int makeMove(String roomToken, String playerToken, String playerFigure, int moveNumber) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        int status = room.makeMove(playerToken,playerFigure,moveNumber);
        /*
        1 - EVERYTHING IS OKAY
        0 - ON THIS POSITION FIGURE IS ALREADY
        -1 - ERROR There is no slot with this number
         */
        return status;
    }


    @Override
    public int checkCombination(String roomToken,String playerFigure) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        int result = room.checkCombination(playerFigure);
        //1 - game over
        //0 - draw
        //2 - nextMove
        return result;
    }
}
