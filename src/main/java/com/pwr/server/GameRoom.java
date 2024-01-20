package com.pwr.server;

import java.util.HashMap;

public class GameRoom {
    private String name;
    private String token;
    private HashMap<String, String> playersInfo;

    public GameRoom(String name, String token)
    {
        this.name = name;
        this.token = token;
        this.playersInfo = new HashMap<>();
    }

    public String getName() {
        return name;
    }
    public int getPlayerNumber()
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

    @Override
    public String toString() {
        return "GameRoom{" +
                "name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", playersInfo=" + playersInfo +
                '}';
    }
}
