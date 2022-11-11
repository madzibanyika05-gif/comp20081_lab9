/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.lab11;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author ntu-user
 */
public class Lab11 {

    private String fileName = "jdbc:sqlite:comp20081.db";
    private int timeout = 30;
    private String dataBaseName = "COMP20081";
    private String dataBaseTableName = "Users";
    Connection connection = null;
    private Random random = new SecureRandom();
    private String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int iterations = 10000;
    private int keylength = 256;
    private String saltValue;
    
    /**
     * @brief constructor - generates the salt if it doesn't exists or load it from the file .salt
     */
    Lab11() {
        try {
            File fp = new File(".salt");
            if (!fp.exists()) {
                saltValue = this.getSaltvalue(30);
                FileWriter myWriter = new FileWriter(fp);
                myWriter.write(saltValue);
                myWriter.close();
            } else {
                Scanner myReader = new Scanner(fp);
                while (myReader.hasNextLine()) {
                    saltValue = myReader.nextLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    /**
     * @brief create a new table
     * @param tableName name of type String
     */
    public void createTable(String tableName) {
        try {
            // create a database connection
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("create table if not exists " + tableName + "(id integer primary key autoincrement, name string, password string)");

        } catch (SQLException ex) {
            Logger.getLogger(Lab11.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * @brief delete table
     * @param tableName of type String
     */
    public void delTable(String tableName) {
        try {
            // create a database connection
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate("drop table if exists " + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(Lab11.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * @brief add data to the database method
     * @param user name of type String
     * @param password of type String
     */
    public void addDataToDB(String user, String password) throws InvalidKeySpecException {
        // TODO add code here
    }

    /**
     * @brief get data from the Database method
     * @param tabName of type String
     */
    public void getDataFromTable(String tabName) {
        // TODO add code here
    }

    /**
     * @brief decode password method
     * @param user name as type String
     * @param pass plain password of type String
     * @param tabName of type String
     * @return true if the credentials are valid, otherwise false
     */
    public boolean validateUser(String user, String pass, String tabName) throws InvalidKeySpecException {
        Boolean flag = false;
        // TODO add code here

        return flag;
    }

    private String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            finalval.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new String(finalval);
    }

    /* Method to generate the hash value */
    private byte[] hash(char[] password, byte[] salt) throws InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keylength);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public String generateSecurePassword(String password) throws InvalidKeySpecException {
        String finalval = null;

        byte[] securePassword = hash(password.toCharArray(), saltValue.getBytes());

        finalval = Base64.getEncoder().encodeToString(securePassword);

        return finalval;
    }

    /**
     * @brief get table name
     * @return table name as String
     */
    public String getTableName() {
        return this.dataBaseTableName;
    }

    /**
     * @brief print a message on screen method
     * @param message of type String
     */
    public void log(String message) {
        System.out.println(message);

    }

    /**
     * @brief main method that populates the database, retrieves values from the
     * database and attempts to authenticate
     * @param args - input arguments
     */
    public static void main(String[] args) throws InvalidKeySpecException {
        Lab11 myObj = new Lab11();
        myObj.log("-------- Simple Tutorial on how to make JDBC connection to SQLite DB ------------");
        myObj.log("\n---------- Drop table ----------");
        myObj.delTable(myObj.getTableName());
        myObj.log("\n---------- Create table ----------");
        myObj.createTable(myObj.getTableName());
        myObj.log("\n---------- Adding Users ----------");
        myObj.addDataToDB("ntu-user", "12z34");
        myObj.addDataToDB("ntu-user2", "12yx4");
        myObj.addDataToDB("ntu-user3", "a1234");
        myObj.log("\n---------- get Data from the Table ----------");
        myObj.getDataFromTable(myObj.getTableName());
        myObj.log("\n---------- Validate users ----------");
        String[] users = new String[]{"ntu-user", "ntu-user", "ntu-user1"};
        String[] passwords = new String[]{"12z34", "1235", "1234"};
        String[] messages = new String[]{"VALID user and password",
            "VALID user and INVALID password", "INVALID user and VALID password"};

        for (int i = 0; i < 3; i++) {
            System.out.println("Testing " + messages[i]);
            if (myObj.validateUser(users[i], passwords[i], myObj.getTableName())) {
                myObj.log("++++++++++VALID credentials!++++++++++++");
            } else {
                myObj.log("----------INVALID credentials!----------");
            }
        }
    }
}
