package com.pwr.server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.Registry;
import java.util.*;


public class Server {
    private String[][] map;
    public Registry registry;
    public static int rmiPort = 9090;
    public static int port = 9091;
    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    public void startServer(Player player)
    {
        try{
            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String userToken = reader.readLine();
                System.out.println(userToken);
                ClientHandler clientThread = new ClientHandler(socket,userToken,player);
                clients.add(clientThread);
                Thread thread = new Thread(clientThread);
                thread.start();

//                //to juz handler
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//                writer.write(name + " you have connected successfully to server!");
//                writer.newLine();
//                writer.flush();

            }
        }catch(IOException e)
        {
            closeServerSocket();
        }
    }


    public void closeServerSocket()
    {
        try {
            if(serverSocket != null)
                System.out.println("Server closed");
                serverSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void createMap()
    {
        map = new String[][] { {"0", "1", "2"},
                {"3", "4", "5"},
                {"6", "7", "8"}
        };
        System.out.println("Map was successfully created!");
    }

    public void showMap()
    {
        System.out.println("-------------");
        for(int i = 0; i < map.length; i++)
        {
            for(int j = 0; j < map.length; j++)
            {
                System.out.print("| "+ map[i][j] + " ");
            }
            System.out.println("|");
            System.out.println("|-----------|");
        }
    }




    public int makeMove(String type)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter slot number on which you want to make your move:");
        int move = scanner.nextInt();
        try{
            if(move > -1 && move < 9)
            {
                System.out.println("You chosen slot with number: " + move);
                String moveString = String.valueOf(move);
                for(int i = 0; i < map.length; i++)
                {
                    for(int j = 0; j < map[i].length; j++)
                    {
                        if(map[i][j].equals(moveString))
                        {
                            map[i][j] = type;
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            System.out.println("ERROR!Invalid input,please re-enter slot number");
            makeMove(type);
        }
        return move;
    }

    public String showResultOfGame(String type)
    {
        //Checking maybe somebody wins(X or O)
        if(checkCombination(type))
        {
            System.out.println("Game over!" + type + "wins");
            //System.out.println("Game over!");
            return "over";
        }
        //Checking maybe game is draw or not completed
        else {
            //Checking does map have any numbers
            int numberCounter = 0;
            for(int i = 0; i < map.length; i++)
            {
                for(int j = 0; j < map[i].length; j++) {
                    if (!map[i][j].equals("X") || !map[i][j].equals("O")) {
                        numberCounter++;
                    }
                }
            }
            if(numberCounter == 0)
            {
                System.out.println("Draw!The map don't have free slots for move");
                return "draw";
            }
            else {
                System.out.println("Let's make a next moves, cause game has free slots");
                return "next";
            }
        }
    }

    private boolean checkCombination(String type)
    {
        //Calkowita liczba kombinacji = 3+3+2 =8
        //sprawdzamy kombinacje w linijke
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

        //sprawdzamy kombinacje na diagonal
        if(map[0][0].equals(type) && map[1][1].equals(type) && map[2][2].equals(type) || map[2][0].equals(type) && map[1][1].equals(type) && map[0][2].equals(type))
        {
            return true;
        }
        return false;
    }

}
