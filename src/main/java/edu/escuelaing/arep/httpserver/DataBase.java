package edu.escuelaing.arep.httpserver;

import java.sql.*;
import java.util.*;

/**
 * This class makes the connection with the data base and obtain datas
 * @author Maria Fernanda Hernandez Vargas
 */
public class DataBase {
    private static String url = "jdbc:postgresql://ec2-54-235-192-146.compute-1.amazonaws.com:5432/d98eeu5e11qd9q";
    private static String user = "vqutduqigizrpu";
    private static String passwd = "4b13f1b319aac26027d8839941271d1bf8585eaad56a9ed7c974752699b1cfcf";
    private static Connection connect = null;

    /**
     * Method that does the connection with data base
     */
    public DataBase (){
        try {
            Class.forName("org.postgresql.Driver");
            connect = DriverManager.getConnection(url, user, passwd);
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    /**
     * Method that obtains data list since data base
     * @return data list
     */
    public ArrayList <String[]> getTable (){
        ArrayList <String[]> data = new ArrayList<>();
        try {
            Statement state = connect.createStatement();
            ResultSet ans = state.executeQuery("SELECT * FROM Article");
            while (ans.next()){
                String id = ans.getString("Id");
                String name = ans.getString("Name");
                String [] inf = {id, name};
                data.add(inf);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return data;
    }
}