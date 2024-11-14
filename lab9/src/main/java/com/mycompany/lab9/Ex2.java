/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.lab9;

import java.net.*;
import java.io.*;

/**
 *
 * @author ntu-user
 */
public class Ex2 {
 
    public static void main(String[] args) {
        
        int port=6868;
        String hostname="localhost";
 
        try (Socket socket = new Socket(hostname, port)) {
 
            //TODO add code here
 
            String line;
            System.out.println("Print received the following message:\n");
            
            while((line=reader.readLine())!= null){
                System.out.println(line);
            }

        } catch (UnknownHostException ex) {
 
            System.out.println("Server not found: " + ex.getMessage());
 
        } catch (IOException ex) {
 
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
