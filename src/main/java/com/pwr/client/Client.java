package com.pwr.client;

import com.pwr.server.GameRoom;
import com.pwr.server.PlayerFeaturesInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userToken;
    private String role;

    public Client(Socket socket,String userToken)
    {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userToken = userToken;
        }catch (IOException e)
        {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public BufferedReader getBufferedReader() {
        return bufferedReader;
    }

    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    //    public static void main(String[] args) throws IOException, NotBoundException {
//        //Creating a client
//
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter your nick-name for game session: ");
//        String userName = scanner.nextLine();
//        String userToken = UUID.randomUUID() + "@" + userName;
//
//        Registry registry = LocateRegistry.getRegistry("localhost",1099);
//        PlayerFeaturesInterface player = (PlayerFeaturesInterface) registry.lookup("Player");
//
//        Socket socket = new Socket("localhost",1234);
//        Client client = new Client(socket,userToken);
//        client.sendMessage(userToken);
//
//        //Get message are we get connection to the server with our name
//        client.listenMessage();
//
//        //sent role
//        scanner = new Scanner(System.in);
//        System.out.println("If u want to be a player pls type and send 'p'");
//        System.out.println("If u want to be a watcher, pls type and send 'w'");
//        System.out.println("Enter the role,that u want to be: ");
//        String role = scanner.nextLine();
//        client.sendMessage(role);
//
//        if(role.equals("p"))
//        {
//            System.out.println("You have chosen player mode");
//            client.playerMenuPanel();
//            Scanner scannerOption = new Scanner(System.in);
//            String option = scannerOption.nextLine();
//            while(!option.equals("5")) {
//                switch (option) {
//                    case "1":
//                        client.createRoomPanel();
//                        break;
//                    case "2":
//                        client.checkListOfRoomsPanel();
//                        break;
//                    case "3":
//                        client.joinGameRoomPanel();
//                        break;
//                    case "4":
//                        client.deleteGameRoomPanel();
//                        break;
//                    default:
//                        System.out.println("There is not available option of number - " + option + ".Please enter a number of option from '1' to '5' to continue the program!");
//                }
//            }
//        }
//        else if(role.equals("w"))
//        {
//            System.out.println("You have chosen watcher mode");
//        }
//
//        //sent next requests
//        while(true)
//        {
//
//            System.out.println("what u would like to do?(if 'exit' type it)");
//            String request = scanner.nextLine();
//            if(request.equals("exit") || request.equals("quit"))
//            {
//                break;
//            }
//            client.sendMessage(request);
//        }
//        client.closeEverything(socket,client.bufferedReader,client.bufferedWriter);
//    }

    public void sendMessage(String message)
    {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        catch (IOException e)
        {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void listenMessage()
    {
        try{
            String messageFromSession = bufferedReader.readLine();
            System.out.println("Server: "+messageFromSession);
        }
        catch (IOException e)
        {
            System.out.println("Closing");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void playerMenuPanel()
    {
        System.out.println("------------------------------------------------------");
        System.out.println("In player mode you can:");
        System.out.println("Create a game room -- type [1]");
        System.out.println("See the list of created rooms -- type [2]");
        System.out.println("Join a game room -- type [3]");
        System.out.println("Delete a game room -- type [4]");
        System.out.println("Exit from program -- type [5]");
        System.out.println("------------------------------------------------------");
        System.out.println("Type and send option that u want to do:");
    }

    public void createRoomPanel(PlayerFeaturesInterface player) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("                   CREATING A GAME ROOM");
        System.out.println("Provide a room name: ");
        String roomName = scanner.nextLine();
        player.createGameRoom(roomName);
        //String gameRoomToken = UUID.randomUUID().toString() + "@" + roomName;


    }

    public void checkListOfRoomsPanel(PlayerFeaturesInterface player) throws RemoteException {
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("                    LIST OF GAME ROOMS");
        List<String> gameRooms = player.showRooms();
        for(String room : gameRooms)
        {
            System.out.println(room);
        }

    }

    public void joinGameRoomPanel(PlayerFeaturesInterface player)
    {

    }

    public void deleteGameRoomPanel(PlayerFeaturesInterface player)
    {

    }



    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
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
