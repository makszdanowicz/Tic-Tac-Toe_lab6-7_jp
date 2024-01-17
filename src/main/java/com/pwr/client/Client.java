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

    public Client(Socket socket, String clientUserName, String role)
    {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = clientUserName;
            this.role = role;
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
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromSession;

                while(socket.isConnected())
                {
                    try{
                        messageFromSession = bufferedReader.readLine();
                        System.out.println(messageFromSession);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
         */
    }



    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nick-name for game session: ");
        String userName = scanner.nextLine();
        System.out.println("Enter the role, that you want to be: ");
        String role = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,userName,role);
        client.sendMessage(userName);
        //client.listenMessage();
        String messageFromSession = client.bufferedReader.readLine();
        System.out.println(messageFromSession);
        client.sendMessage(role);
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
