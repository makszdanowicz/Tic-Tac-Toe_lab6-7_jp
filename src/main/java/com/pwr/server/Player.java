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
    public void createGameRoom(String roomName) throws RemoteException {
        String roomToken = UUID.randomUUID().toString() + "@" + roomName;
        if(gameRooms.containsKey(roomToken))
        {
            System.out.println("ERROR!Can't create a new game room using this token, cause room with this token already exists!");
        }
        else {
            gameRooms.put(roomToken, new GameRoom(roomName,roomToken));
            System.out.println("Your room named " + roomName + " was created!");
            System.out.println("If u want to connect to this room use this token: " + roomToken);
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
        for(Map.Entry<String,GameRoom> entry: gameRooms.entrySet())
        {
            //System.out.println("name: " + entry.getKey() + " | token: " + entry.getValue());
            String string = "name" + entry.getKey() + " | token: " + entry.getValue().toString();
            gameRoomsList.add(string);
        }
        return gameRoomsList;
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
