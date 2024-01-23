package com.pwr.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable{

    //public static ArrayList<com.pwr.client.ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientTocken;
    private Player player;

    //private String role;

    public ClientHandler(Socket clientSocket,String clientTocken,Player player)
    {
        try{
            this.socket = clientSocket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientTocken = clientTocken;
            this.player = player;
            //this.role = role;
        } catch (IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    @Override
    public void run() {
        System.out.println("Thread started");
        String clientUserName = clientTocken.substring(clientTocken.indexOf("@")+1);
        try {
            //sent a message that server got a name and return it with information that connection is good
            bufferedWriter.write(clientUserName + " you have connected successfully to server!");
            bufferedWriter.newLine();
            bufferedWriter.flush();

            //reading a role of person
            String roleLetter = bufferedReader.readLine();
            if(roleLetter.equals("p") || roleLetter.equals("P")) {
                String role = "player";
                System.out.println(clientUserName + " have chosen " + role + " mode" );
            }
            else if (roleLetter.equals("w") || roleLetter.equals("W")) {
                String role = "watcher";
                System.out.println(clientUserName + " have chosen " + role + " mode" );
            }

            while (true)
            {
                String request = bufferedReader.readLine();
                //System.out.println(clientUserName + " request was: " + request);
                if(request.equals("exit") || request.equals("quit"))
                {
                    System.out.println(clientUserName + " request was: " + request);
                    System.out.println(clientUserName + " disconnected with server");
                    break;
                }
                System.out.println(clientUserName + " request was: " + request);
                if(request.startsWith("getPlayersInfo"))
                {
                    String roomToken = request.split(":")[1];
                    String player1 = player.getTokenOfPlayerWhoTurn(roomToken);
                    String player2 = player.getTokenOfOpponent(roomToken,player1);
                    bufferedWriter.write(player1 + "," + player2);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                else if (request.startsWith("getMap"))
                {
                    String roomToken = request.split(":")[1];
                    String[][] map = player.getMap(roomToken);
                    StringBuilder stringBuilder = new StringBuilder();
                    for(String[] row : map)
                    {
                        for(String element : row)
                        {
                            stringBuilder.append(element).append(" ");
                        }
                        stringBuilder.append("*");
                    }
                    bufferedWriter.write(stringBuilder.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                else if(request.startsWith("currentPlayer"))
                {
                    String roomToken = request.split(":")[1];
                    String currentPlayerToken = player.getTokenOfPlayerWhoTurn(roomToken);
                    bufferedWriter.write(currentPlayerToken);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
                else if(request.startsWith("checkCombinationX"))
                {
                    String roomToken = request.split(":")[1];
                    int combinationX = player.checkCombination(roomToken,"X");
                    if(combinationX == 1)
                    {
                        String combinationXString = "Game over. O wins!";
                        bufferedWriter.write(combinationXString);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    else if(combinationX == 5)
                    {
                        String combinationXString = "Game over. X wins!";
                        bufferedWriter.write(combinationXString);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    else if(combinationX == 0)
                    {
                        String combinationXString = "It's draw!";
                        bufferedWriter.write(combinationXString);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                    else if(combinationX == 2){
                        String combinationXString = "Players have next moves!";
                        bufferedWriter.write(combinationXString);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                }
            }
            closeEverything(socket,bufferedReader,bufferedWriter);



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /*
    public void broadcastMessage(String message)
    {
        for(com.pwr.client.ClientHandler clientHandler: clientHandlers)
        {
            try{
                if(!clientHandler.clientUserName.equals(clientUserName))
                {
                    clientHandler.bufferedWriter.write(message);
                    clientHandler.bufferedWriter.flush();
                }
            }
            catch (IOException e)
            {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }



    public void removeClientHandler()
    {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUserName + " has left the group session");
    }

     */

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        //removeClientHandler();
        try{
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}

