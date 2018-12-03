/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import business.Despesa;
import business.DespesaExtraordinaria;
import business.DespesaRecorrente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DespesaDAO {
    public static void guardarDespesaRec (DespesaRecorrente a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("INSERT INTO despesarecorrente VALUES"
                                  + "(?,?,?,?)");
        ps.setInt(1, a.getMontante());
        ps.setString(2, a.getData());
        ps.setString(3, a.getEstado());
        ps.setString(4, a.getTipo());
        
        ps.executeUpdate();
        c.close();
    }
    
    public static void removerDespesaRec (DespesaRecorrente a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("DELETE FROM despesarecorrente WHERE montante = ? and data = ? and tipo = ?");
        ps.setInt(1, a.getMontante());
        ps.setString(2, a.getData());
        ps.setString(3, a.getTipo());
        
        ps.executeUpdate();
        c.close();
    }

    public static List<DespesaRecorrente> listDespesaRec() throws SQLException {
        Connection c = Connect.connect();
        ResultSet rs = c.createStatement().
                         executeQuery("SELECT * FROM despesarecorrente");
        
        List<DespesaRecorrente> list = new ArrayList<>();
        
        while (rs.next()) {
            int montante = rs.getInt("montante");
            String data = rs.getString("data");
            String estado = rs.getString("estado");
            String tipo = rs.getString("tipo");
            DespesaRecorrente a = new DespesaRecorrente(montante, data, estado, tipo);
            list.add(a);
        }
        
        return list;
    }
    
    public static void alteraEstadoDespesaRec (DespesaRecorrente a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("UPDATE despesarecorrente SET estado = ? WHERE montante = ? AND data = ? AND tipo = ?" );
        
        ps.setString(1, a.getEstado());
        ps.setInt(2, a.getMontante());
        ps.setString(3, a.getData());
        ps.setString(4, a.getTipo());
        
        ps.executeUpdate();
        c.close();
        
    }
    
    public static void guardarDespesaExt (DespesaExtraordinaria a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("INSERT INTO despesaextraordinaria VALUES"
                                  + "(?,?,?,?)");
        ps.setInt(1, a.getMontante());
        ps.setString(2, a.getData());
        ps.setString(3, a.getEstado());
        ps.setString(4, a.getDescricao());
        
        ps.executeUpdate();
        c.close();
    }
   
    public static void removerDespesaExt (DespesaExtraordinaria a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("DELETE FROM despesaextraordinaria WHERE montante = ? and data = ? and descricao = ?");
        ps.setInt(1, a.getMontante());
        ps.setString(2, a.getData());
        ps.setString(3, a.getDescricao());
        
        ps.executeUpdate();
        c.close();
    }
    
    public static List<DespesaExtraordinaria> listDespesaExt() throws SQLException {
        Connection c = Connect.connect();
        ResultSet rs = c.createStatement().
                         executeQuery("SELECT * FROM despesaextraordinaria");
        
        List<DespesaExtraordinaria> list = new ArrayList<>();
        
        while (rs.next()) {
            int montante = rs.getInt("montante");
            String data = rs.getString("data");
            String estado = rs.getString("estado");
            String descricao = rs.getString("descricao");
            DespesaExtraordinaria a = new DespesaExtraordinaria(montante, data, estado, descricao);
            list.add(a);
        }
        
        return list;
    }
    
    public static void alteraEstadoDespesaExt (DespesaExtraordinaria a) throws SQLException {
        
        Connection c = Connect.connect();
        PreparedStatement ps = c
                .prepareStatement("UPDATE despesaextraordinaria SET estado = ? WHERE montante = ? AND data = ? AND descricao = ?" );
        
        ps.setString(1, a.getEstado());
        ps.setInt(2, a.getMontante());
        ps.setString(3, a.getData());
        ps.setString(4, a.getDescricao());
        
        ps.executeUpdate();
        c.close();
        
    }
    
    
    
    
}

    