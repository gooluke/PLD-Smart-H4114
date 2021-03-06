/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
public class DBConnection {
    
    public Connection connection;

    public DBConnection() throws ClassNotFoundException, SQLException {
        
        String user = "root";
        String password = "root";
        String url = "jdbc:mysql://localhost:3306/tpsmart";
        Class.forName("com.mysql.jdbc.Driver");
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        this.connection = DriverManager.getConnection(url, user, password);  
        
    }
    
    public Connection getConnection() {
         
         return connection;
         
    }
    /*public static int CreateTableUser(Connection conn) throws SQLException{
        Statement stmt = conn.createStatement();
        String sql = "create table user(id_user int(255),firstName varchar(50),Age int(100),Address varchar(50),Pwd varchar(50))";
        int result = stmt.executeUpdate(sql);
        return result;
    }*/
    public static int Insert(Connection conn,String email,String pseudo,String password) throws SQLException{
        Statement stmt = conn.createStatement();
        String value="'"+email+"','"+pseudo+"','"+password+"'";
        String sql = "insert ignore into utilisateurs(email,pseudo,password) values("+value+")";
        int result = stmt.executeUpdate(sql);
        return result;
    } 
    /*public static void Delete(Connection conn,String email,String pwd) throws SQLException{
        String sql = "delete * from account where Email = ? and Pwd= ?";
        PreparedStatement stmt = conn.prepareStatement(sql);    
        stmt.setString(1, email); 
        stmt.setString(2, pwd); 
        ResultSet rs = stmt.executeQuery();
    }*/ 
    public static boolean Connect(String email,String password,Connection conn) throws SQLException{
        String sql="select * from utilisateurs where email = ? and password= ? ";
        PreparedStatement stmt = conn.prepareStatement(sql);    
        stmt.setString(1, email); 
        stmt.setString(2, password); 
        ResultSet rs = stmt.executeQuery();
        rs.last();    
        int size = rs.getRow(); 
        if (size==0){
            return false;
        }
        else{
            rs.beforeFirst();
            return true;   
        }
    }
    public static ResultSet FindUserWithEmail(String email,Connection conn) throws SQLException{
        String sql="select * from user where email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);    
        stmt.setString(1, email); 
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    
    public static int createRally(Connection conn, String rally, String description, 
            String place, String date, String time, String radio, int moderator) throws SQLException{
        
        Statement stmt = conn.createStatement();
        String value="'"+rally+"','"+description+"','"+place+"','"+date+"','"+time+"','"+radio+"','"+moderator+"'";
        String sql = "insert ignore into rassemblements(rally,description,place,date,time,radio) values("+value+")";
        int result = stmt.executeUpdate(sql);
        return result;
    }
    
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            // Ignore
        }
    }
}	