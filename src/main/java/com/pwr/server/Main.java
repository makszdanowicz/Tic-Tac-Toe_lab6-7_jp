package com.pwr.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args)
    {
        //Create a server
        try {
            ServerSocket serverSocket = new ServerSocket(Server.port);
            Server server = new Server(serverSocket);

            Player player = new Player();
            server.registry = LocateRegistry.createRegistry(Server.rmiPort);
            server.registry.rebind("Player",player);

            server.startServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
