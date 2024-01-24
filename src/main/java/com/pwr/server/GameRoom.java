package com.pwr.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRoom {
    private String name;
    private String token;
    private HashMap<String, String> playersInfo;
    private TicTacToeGame game;
    private String tokenOfTurnPlayer;

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

    public String getToken() {
        return token;
    }

    public String getTokenOfTurnPlayer() {
        return tokenOfTurnPlayer;
    }

    public void setTokenOfTurnPlayer(String tokenOfTurnPlayer) {
        this.tokenOfTurnPlayer = tokenOfTurnPlayer;
    }

    public void setGame(TicTacToeGame game) {
        this.game = game;
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
            setTokenOfTurnPlayer(playerToken);//SET WHO WILL BE TURN FIRST, BY SENDING HIS TOKEN
        }
        else {
            playersInfo.put(playerToken,"O");
        }
        System.out.println("Added new player ( " + playerToken.substring(playerToken.indexOf("@")+1) + " ) with token: " + playerToken);
        System.out.println("Number of players in game room - " +getName()+ ": " + playersInfo.size());
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

    public void turnStatus(String playerToken)
    {

    }

    public int makeMove(String playerToken, String playerFigure, int moveNumber)
    {
        System.out.println("Now turn has: " + getTokenOfTurnPlayer());
        int status = game.makeMove(playerFigure,moveNumber);
        System.out.println(playerToken + " with figure " + playerFigure + " chosen slot " + moveNumber);
        if(status == 1) {
            for (String keyToken : playersInfo.keySet())
            {
                if (!keyToken.equals(playerToken)) {
                    setTokenOfTurnPlayer(keyToken);
                    break;
                }
            }
        }
        System.out.println("Next turn has: " + getTokenOfTurnPlayer());
        //System.out.println("Player turn: " + getTokenOfTurnPlayer());
        return status;
    }

    public int checkCombination(String playerFigure)
    {
        int result = game.showResultOfGame(playerFigure);
        if(result == 1)
        {
            //End of game O won
            return 1;
        }
        else if(result == 5)
        {
            //End of game X won
            return 5;
        }
        else if(result == 0)
        {
            //draw
            return 0;
        } else if (result == 2)
        {
            //nextMoves
            return 2;
        }
        return -1;//error
    }

    public int restart(String playerToken)
    {
        playersInfo.remove(playerToken);
        //tokenOfTurnPlayer = null;
        System.out.println("Kicked " + playerToken.substring(playerToken.indexOf("@")+1) + " from room - " + getName());
        if(playersInfo.size() == 0)
        {
            TicTacToeGame game1 = new TicTacToeGame();
            game = game1;
            System.out.println("restart: Create a new clear board for room - " + getName());
            return 1;
        }
        return 0;
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
