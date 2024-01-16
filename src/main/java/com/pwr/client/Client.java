package com.pwr.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

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

    public void sendUserName()
    {
        try {
            bufferedWriter.write(clientUserName);
            bufferedWriter.flush();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void listenMessage()
    {
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
    }



    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nick-name for game session: ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,userName);
        client.sendUserName();
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
