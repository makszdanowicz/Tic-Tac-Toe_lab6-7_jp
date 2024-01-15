package com.pwr.client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("localhost",1234);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())))
        {
            writer.write("Hello, I am client");
            writer.newLine();
            writer.flush();

            String message = reader.readLine();
            System.out.println(message);

            //System.out.println(clientSocket.getInputStream().read());
//            byte[] bytes = new byte[256];
//            clientSocket.getInputStream().read(bytes);
//            System.out.println(new String(bytes));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String message = reader.readLine();
//            System.out.println(message);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            out.println("Hello to Server from Client 1");
            out.println("How are u?");
            out.close();
            clientSocket.close();

            //clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
