package com.pwr.server;

import java.util.HashMap;
import java.util.Map;

public class GameRoom {
    private String name;
    private String token;
    private HashMap<String, String> playersInfo;
    private TicTacToeGame game;

    public GameRoom(String name, String token)
    {
        this.name = name;
        this.token = token;
        this.playersInfo = new HashMap<>();
        this.game = new TicTacToeGame();
    }

    public String getName() {
        return name;
    }
    public int getPlayersNumber()
    {
        return playersInfo.size();
    }

    public int addNewPlayer(String playerToken)
    {   if(playersInfo.size() >= 2)
        {
            return 0;
        }
        if(playersInfo.size() == 0)
        {
            playersInfo.put(playerToken,"X");
        }
        else {
            playersInfo.put(playerToken,"O");
        }
        System.out.println("Added new player ( " + playerToken.substring(playerToken.indexOf("@")+1) + " ) with token: " + playerToken);
        System.out.println("Number of players in game room: " + playersInfo.size());
        return 1;
    }

    public boolean hasPlayer(String playerToken)
    {
        if(playersInfo.containsKey(playerToken))
        {
            return true;
        }
        return false;

    }

    public String getOpponentToken(String playerToken)
    {
        for(Map.Entry<String,String> entry : playersInfo.entrySet())
        {
            if(!entry.getKey().equals(playerToken))
            {
                return entry.getKey();
            }
        }
        return playerToken;
    }
    public String getPlayerFigure(String playerToken)
    {
        for(Map.Entry<String,String> entry : playersInfo.entrySet())
        {
            if(entry.getKey().equals(playerToken))
            {
                return entry.getValue();
            }
        }
        return "error";
    }

    public void removePlayer(String playerToken)
    {
        playersInfo.remove(playerToken);
    }

    public String[][] getMap()
    {
        return game.getMap();
    }


    @Override
    public String toString() {
        return "GameRoom{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", playersInfo=" + playersInfo +
                '}';
    }
}
