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
            return "Your room named " + roomName + " was created!\n" + "If u want to connect to this room use this token: " + roomToken;
        }

    }

    @Override
    public String joinGameRoom(String playerToken, String roomToken) throws RemoteException {
        return null;
    }

    @Override
    public String leaveGameRoom(String playerToken, String roomToken) throws RemoteException {
        return null;
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
            String string = "name" + entry.getValue().toString() + " | token: " + entry.getKey();
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
    public int makeMove(String token, String roomToken, String playerType) throws RemoteException {
        return 0;
    }

    @Override
    public boolean checkCombination(String roomToken) throws RemoteException {
        return false;
    }

    @Override
    public void showMap(String roomToken) throws RemoteException {

    }
}
