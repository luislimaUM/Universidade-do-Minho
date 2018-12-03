package database;
import business.Morador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import business.Utilizador;

public class UtilizadorDAO {
    
    public static void guardarUtilizador (Utilizador a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("INSERT INTO utilizador VALUES"
                                  + "( ?,?,?,?)");
        ps.setString(1, a.getNome());
        ps.setString(2, a.getPass());
        ps.setInt(3, a.getContacto());
        ps.setInt(4, a.getSaldo());
        
        ps.executeUpdate();
        c.close();
    }
    
    public static void removerUtilizador (Utilizador a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("DELETE FROM utilizador WHERE nome = "
                                  + "(?)");
        ps.setString(1, a.getNome());
        
        ps.executeUpdate();
        c.close();
    }
    
    public static void alteraUtilizador (Utilizador a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("UPDATE utilizador SET pass = " 
                                  + "(?)" 
                                  + ", contacto = " + "(?)" 
                                  + ", saldo = " + "(?)"
                                  + " WHERE nome = " + "(?)");

        
        ps.setString(4, a.getNome());
        ps.setString(1, a.getPass());
        ps.setInt(2, a.getContacto());
        ps.setInt(3, a.getSaldo());
        
        ps.executeUpdate();
        c.close();
        
    }
   
    public static List<Utilizador> list() throws SQLException {
        Connection c = Connect.connect();
        ResultSet rs = c.createStatement().
                         executeQuery("SELECT * FROM utilizador");
        
        List<Utilizador> list = new ArrayList<>();
        
        while (rs.next()) {
            String name = rs.getString("nome");
            String pass = rs.getString("pass");
            int contacto = rs.getInt("contacto");
            int saldo = rs.getInt("saldo");
            Morador a = new Morador(name, pass, contacto, saldo);
            list.add(a);
        }
        
        return list;
    }

    public boolean verificaLoginMorador (String login, String pass) throws SQLException {
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("SELECT * FROM utilizador WHERE nome = ? AND pass = ? ");
        ps.setString(1, login);
        ps.setString(2, pass);
        
        ResultSet rs = null;
        rs = ps.executeQuery();

        boolean check = false;
        
        if (rs.next()) check = true;
        
        return check;
    }
    
    public boolean verificaLoginSenhorio (String login, String pass) throws SQLException {
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("SELECT * FROM senhorio WHERE nome = ? AND pass = ? ");
        ps.setString(1, login);
        ps.setString(2, pass);
        
        ResultSet rs = null;
        rs = ps.executeQuery();

        boolean check = false;
        
        if (rs.next()) check = true;
        
        return check;
    }
    
}
