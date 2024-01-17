package com.pwr.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;
    private String role;

    public Client(Socket socket, String clientUserName)
    {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = clientUserName;
        }catch (IOException e)
        {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(String message)
    {
        try {
            bufferedWriter.write(clientUserName);
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
            System.out.println(messageFromSession);
        }
        catch (IOException e)
        {
            System.out.println("Closing");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }



    public static void main(String[] args) throws IOException {
        //Creating a client
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nick-name for game session: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,userName);
        client.sendMessage(userName);

        //Get message are we get connection to the server with our name
        client.listenMessage();

        //sent role
        scanner = new Scanner(System.in);
        System.out.println("Enter the role, that you want to be(player or watcher): ");
        String role = scanner.nextLine();

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
        client.closeEverything(socket,client.bufferedReader,client.bufferedWriter);
        //client.listenMessage();
        //client.listenMessage();
//        try (Socket clientSocket = new Socket("localhost",1234);
//             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
//        {
//            writer.write("Hello, I am client");
//            writer.newLine();
//            writer.flush();
//
//            String message = reader.readLine();
//            System.out.println(message);
//
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
//            out.println("Hello to Server from Client 1");
//            out.println("How are u?");
//            out.close();
//            clientSocket.close();
//
//            //clientSocket.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
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
