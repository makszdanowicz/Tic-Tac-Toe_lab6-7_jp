package com.pwr.server;

public class Player extends Client{
    private String numberOfWinGames;
    private String numberOfDrawGames;
    private String numberOfLoseGames;
    public Player(String name, String role) {
        super(name, role);
    }

    public void setNumberOfWinGames(String numberOfWinGames) {
        this.numberOfWinGames = numberOfWinGames;
    }

    public void setNumberOfDrawGames(String numberOfDrawGames) {
        this.numberOfDrawGames = numberOfDrawGames;
    }

    public void setNumberOfLooseGames(String numberOfLoseGames) {
        this.numberOfLoseGames = numberOfLoseGames;
    }

    @Override
    public String toString() {
        return  "numberOfWinGames='" + numberOfWinGames +
                "| numberOfDrawGames='" + numberOfDrawGames +
                "| numberOfLoseGames='" + numberOfLoseGames + '\n';
    }
    /*

    public void setNumberOfWinGames(String numberOfWinGames) {
        this.numberOfWinGames = numberOfWinGames;
    }

    public void setNumberOfDrawGames(String numberOfDrawGames) {
        this.numberOfDrawGames = numberOfDrawGames;
    }

    public void setNumberOfLooseGames(String numberOfLoseGames) {
        this.numberOfLoseGames = numberOfLoseGames;
    }

    @Override
    public String toString() {
        return  "name='" + name + '\'' +
                "| numberOfWinGames='" + numberOfWinGames + '\'' +
                "| numberOfDrawGames='" + numberOfDrawGames + '\'' +
                "| numberOfLooseGames='" + numberOfLoseGames + '\'' +
                '}';
    }
     */
}
