/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.lab11;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ntu-user
 */
public class Lab11 {

    static private String fileName = "jdbc:sqlite:comp20081.db";
    static private int timeout = 30;
    static private String dataBaseName = "COMP20081";
    static private String dataBaseTableName = "Users";
    static Connection connection = null;

    /**
     * @brief main method that populates the dataset, retrieves values from the
     * dataset and attempts to authenticate
     * @param[in] argv - input arguments
     */
    public static void main(String[] args) {
        log("-------- Simple Tutorial on how to make JDBC connection to SQLite DB ------------");
        log("\n---------- Drop table ----------");
        delTable(dataBaseTableName);
        log("\n---------- Create table ----------");
        createTable(dataBaseTableName);
        log("\n---------- Adding Users ----------");
        addDataToDB("ntu-user", "1234");
        addDataToDB("ntu-user2", "1255");
        addDataToDB("ntu-user3", "4255");
        log("\n---------- get Data from the Table ----------");
        getDataFromTable(dataBaseTableName);
        log("\n---------- Validate users ----------");
        String[] users= new String[] {"ntu-user","ntu-user","ntu-user1"};
        String[] passwords= new String[] {"1234","1235","1234"};
        String[] messages= new String[] {"VALID user and password",
            "VALID user and INVALID password","INVALID user and VALID password"};

        for (int i=0;i<3;i++){
            System.out.println("Testing "+messages[i]);
            if(validateUser(users[i],passwords[i],dataBaseTableName)){
                log("++++++++++VALID credentials!++++++++++++");
            }
            else{
                log("----------INVALID credentials!----------");
            }
        }
    }

    private static void createTable(String tableName) {
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

    private static void delTable(String tableName) {
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
     * @brief encode password method
     * @param[in] plain password of type String
     * @return encoded password of type String
     */
    private static String encodePassword(String plainPassword) {
        byte[] bPass = plainPassword.getBytes(StandardCharsets.UTF_8);
        byte[] passBase64 = Base64.getEncoder().encode(bPass);
        String encodedPassword = new String(passBase64, StandardCharsets.UTF_8);
        return encodedPassword;
    }

    /**
     * @brief decode password method
     * @param[in] encoded password of type String
     * @return decoded password of type String
     */
    private static String decodePassword(String encodedPassword) {
        String decodedString = new String(Base64.getDecoder().decode(encodedPassword));
        return decodedString;
    }

    /**
     * @brief add data to the database method
     * @param[in] user name of type String
     * @param[in] user password of type String
     */
    private static void addDataToDB(String user, String password) {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            System.out.println("Adding User: "+user+", Password: "+password);
            statement.executeUpdate("insert into " + dataBaseTableName + " (name, password) values('" + user + "','" + encodePassword(password) + "')");
        } catch (SQLException ex) {
            Logger.getLogger(Lab11.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
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
    }
        /**
         * @brief get data from the Database method
         */
    private static void getDataFromTable(String tabName) {
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select * from "+tabName);
            while (rs.next()) {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("password = " + decodePassword(rs.getString("password")));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Lab11.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
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
     * @brief decode password method
     * @param[in] user name as type String
     * @param[in] plain password of type String
     * @return true if the credentials are valid, otherwise false
     */
    private static boolean validateUser(String user, String pass, String tabName) {
        Boolean flag=false;
        try {
            connection = DriverManager.getConnection(fileName);
            var statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery("select name, password from "+tabName);

            // Let's iterate through the java ResultSet
            while (rs.next()) {
                if (user.equals(rs.getString("name")) && pass.equals(decodePassword(rs.getString("password")))) {
                    flag=true;
                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(Lab11.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        return flag;
    }

    /**
     * @brief print a message on screen method
     * @param message of type String
     */
    private static void log(String message) {
        System.out.println(message);

    }
}
