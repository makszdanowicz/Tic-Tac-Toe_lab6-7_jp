package com.pwr.client;

import com.pwr.server.PlayerFeaturesInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException, NotBoundException {
        //Creating a client

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nick-name for game session: ");
        String userName = scanner.nextLine();
        String userToken = UUID.randomUUID() + "@" + userName;

        Registry registry = LocateRegistry.getRegistry("localhost",1099);
        PlayerFeaturesInterface player = (PlayerFeaturesInterface) registry.lookup("Player");

        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,userToken);
        client.sendMessage(userToken);

        //Get message are we get connection to the server with our name
        client.listenMessage();

        //sent role
        scanner = new Scanner(System.in);
        System.out.println("If u want to be a player pls type and send 'p'");
        System.out.println("If u want to be a watcher, pls type and send 'w'");
        System.out.println("Enter the role,that u want to be: ");
        String role = scanner.nextLine();
        client.sendMessage(role);

        if(role.equals("p"))
        {
            System.out.println("You have chosen player mode");
            client.playerMenuPanel();
            Scanner scannerOption = new Scanner(System.in);
            String option = scannerOption.nextLine();
            while(!option.equals("5")) {
                switch (option) {
                    case "1":
                        client.createRoomPanel(player);
                        break;
                    case "2":
                        client.checkListOfRoomsPanel(player);
                        break;
                    case "3":
                        client.joinGameRoomPanel(player);
                        break;
                    case "4":
                        client.deleteGameRoomPanel(player);
                        break;
                    default:
                        System.out.println("There is not available option of number - " + option + ".Please enter a number of option from '1' to '5' to continue the program!");
                        break;
                }
                client.playerMenuPanel();
                option = scannerOption.nextLine();
            }
        }
        else if(role.equals("w"))
        {
            System.out.println("You have chosen watcher mode");
        }

        //sent next requests
        while(true)
        {

            System.out.println("what u would like to do?(if 'exit' type it)");
            String request = scanner.nextLine();
            if(request.equals("exit") || request.equals("quit"))
            {
                break;
            }
            client.sendMessage(request);
        }
        client.closeEverything(socket,client.getBufferedReader(),client.getBufferedWriter());
    }
}
