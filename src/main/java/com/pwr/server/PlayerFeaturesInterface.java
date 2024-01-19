package com.pwr.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PlayerFeaturesInterface extends Remote {
    void createGameRoom(String roomName) throws RemoteException;
    String joinGameRoom(String playerToken,String roomToken) throws RemoteException;
    String leaveGameRoom(String playerToken,String roomToken) throws RemoteException;
    List<String> showRooms() throws RemoteException;
    int makeMove(String token,String roomToken, String playerType) throws RemoteException;
    boolean checkCombination(String roomToken) throws RemoteException;
    void showMap(String roomToken) throws RemoteException;

}
