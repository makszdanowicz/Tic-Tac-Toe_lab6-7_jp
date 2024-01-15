package com.pwr.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) {
        try( ServerSocket serverSocket = new ServerSocket(1234);) {
            System.out.println("Server started");
            while(true)
            {
                try(Socket clientSocket = serverSocket.accept();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));)
                {
                    System.out.println("Client connected");
                    String message = bufferedReader.readLine();
                    String answer = String.format("Hello, got your message! = %s", message);
                    bufferedWriter.write(answer);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
            /*
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client accepted ");
//                //clientSocket.getOutputStream().write("abc".getBytes());
//                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
//                outputStreamWriter.write("Hello Client from Server!");
//                outputStreamWriter.flush();//gwarancuje ze wiadomosc zostanie wyslana
//                outputStreamWriter.close();

                Scanner in = new Scanner(clientSocket.getInputStream());
                while(in.hasNext())
                {
                    System.out.println(in.nextLine());
                }
                in.close();
                //clientSocket.close();
            }

            //serverSocket.close();
            */
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private String[][] map;
    public Server()
    {
        this.map = createMap();
        playGame();
    }

    public String[][] createMap()
    {
        map = new String[3][3];
        for(int i = 0; i < map.length; i++)
        {
            Arrays.fill(map[i], "0");
        }
        return map;
    }

    public boolean checkCombination(String type)
    {
        //sprawdzamy kombinacje w linijke czyli XXX
        for(int i = 0; i < map.length; i++)
        {
            int j = 0;
            if(map[i][j].equals(type) && map[i][j+1].equals(type) && map[i][j+2].equals(type)) {
                return true;
            }
        }

        //sprawdzamy kombinacje w dol
        for(int j = 0; j < map.length; j++)
        {
            int i = 0;
            if(map[i][j].equals(type) && map[i+1][j].equals(type) && map[i+2][j].equals(type))
            {
                return true;
            }
        }

        //sprawdzamy kombinacje na X
        if(map[0][0].equals(type) && map[1][1].equals(type) && map[2][2].equals(type) || map[2][0].equals(type) && map[1][1].equals(type) && map[0][2].equals(type))
        {
            return true;
        }

        return false;
    }

    public void playGame()
    {
        map[0][0] = "X";
        map[1][1] = "X";
        map[2][2] = "X";
//        boolean result = checkCombination("X");
        if(true == checkCombination("X"))
        {
            System.out.println("GAME OVER! X WINS");
        }
        else {
            System.out.println("Continue game, next step:");
        }
    }
}
