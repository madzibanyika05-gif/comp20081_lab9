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

    private String fileName = "jdbc:sqlite:comp20081.db";
    private int timeout = 30;
    private String dataBaseName = "COMP20081";
    private String dataBaseTableName = "Users";
    Connection connection = null;
    
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
    public void addDataToDB(String user, String password) {
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
     * @param tabName of type String
     */
    public void getDataFromTable(String tabName) {
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
     * @param user name as type String
     * @param pass plain password of type String
     * @param tabName of type String
     * @return true if the credentials are valid, otherwise false
     */
    public boolean validateUser(String user, String pass, String tabName) {
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
     * @brief encode password method
     * @param plainPassword of type String
     * @return encodedPassword of type String
     */
    public String encodePassword(String plainPassword) {
        byte[] bPass = plainPassword.getBytes(StandardCharsets.UTF_8);
        byte[] passBase64 = Base64.getEncoder().encode(bPass);
        String encodedPassword = new String(passBase64, StandardCharsets.UTF_8);
        return encodedPassword;
    }

    /**
     * @brief decode password method
     * @param encodedPassword of type String
     * @return decoded password of type String
     */
    protected String decodePassword(String encodedPassword) {
        String decodedString = new String(Base64.getDecoder().decode(encodedPassword));
        return decodedString;
    }
    
    /**
     * @brief get table name
     * @return table name as String
     */
    public String getTableName(){
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
    public static void main(String[] args) {
        Lab11 myObj=new Lab11();
        myObj.log("-------- Simple Tutorial on how to make JDBC connection to SQLite DB ------------");
        myObj.log("\n---------- Drop table ----------");
        myObj.delTable(myObj.getTableName());
        myObj.log("\n---------- Create table ----------");
        myObj.createTable(myObj.getTableName());
        myObj.log("\n---------- Adding Users ----------");
        myObj.addDataToDB("ntu-user", "1234");
        myObj.addDataToDB("ntu-user2", "1255");
        myObj.addDataToDB("ntu-user3", "4255");
        myObj.log("\n---------- get Data from the Table ----------");
        myObj.getDataFromTable(myObj.getTableName());
        myObj.log("\n---------- Validate users ----------");
        String[] users= new String[] {"ntu-user","ntu-user","ntu-user1"};
        String[] passwords= new String[] {"1234","1235","1234"};
        String[] messages= new String[] {"VALID user and password",
            "VALID user and INVALID password","INVALID user and VALID password"};

        for (int i=0;i<3;i++){
            System.out.println("Testing "+messages[i]);
            if(myObj.validateUser(users[i],passwords[i],myObj.getTableName())){
                myObj.log("++++++++++VALID credentials!++++++++++++");
            }
            else{
                myObj.log("----------INVALID credentials!----------");
            }
        }
    }
}
