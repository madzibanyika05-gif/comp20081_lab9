/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.lab9;
import java.io.*;
import java.net.*;
import java.util.Date;

/**
 *
 * @author ntu-user
 */

 
public class Ex1 {
 
    public static void main(String[] args) {
        int port = 6868;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Server is listening on port "+port);
 
            while (true) {
                //TODO add your code here
                
                writer.println("Hello this is a message sent by the server!");
                writer.println(new Date().toString());
            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
