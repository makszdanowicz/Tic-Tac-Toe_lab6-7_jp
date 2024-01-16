package com.pwr;

import com.pwr.server.Server;
import com.pwr.server.TicTacToeGame;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Клиент отправляет запрос серверу для получения информации о состоянии карты, после чего обрабатывает его, делает ход и отправляет результат серверу
        //Server server = new Server();
        TicTacToeGame game = new TicTacToeGame();
        game.createMap();
        String result ="next";
        while(result.equals("next"))
        {
            Scanner scanner = new Scanner(System.in);
            game.showMap();
            System.out.println("Player 1(X). Type your symbol:");
            String type = scanner.nextLine();
            game.makeMove(type);
            result = game.showResultOfGame(type);
            if(result.equals("game over") || result.equals("draw"))
            {
                break;
            }
            game.showMap();
            System.out.println("Player 2(O). Type your sign:");
            type = scanner.nextLine();
            game.makeMove(type);
            result = game.showResultOfGame(type);
            if(result.equals("game over") || result.equals("draw"))
            {
                break;
            }

        }
    }
}
