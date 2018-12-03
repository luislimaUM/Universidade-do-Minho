/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package business;

import database.DespesaDAO;
import database.UtilizadorDAO;
import java.util.List;

public class Facade {
    
    public static void adicionaUtilizador (Utilizador a) {
        try {
            UtilizadorDAO.guardarUtilizador(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void removerUtilizador (Utilizador a) {
        try {
            UtilizadorDAO.removerUtilizador(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void alteraUtilizador (Utilizador a) {
        try {
            UtilizadorDAO.alteraUtilizador(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<Utilizador> listUtilizador() {
        try {
            return UtilizadorDAO.list();
        } catch (Exception e) {
            
        }
        return null;
    }
    
    
    
    public static void adicionaDespesaRec (DespesaRecorrente a) {
        try {
            DespesaDAO.guardarDespesaRec(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<DespesaRecorrente> listDespesaRec() {
        try {
            return DespesaDAO.listDespesaRec();
        } catch (Exception e) {
            
        }
        return null;
    }
    
    public static void removerDespesaRec (DespesaRecorrente a) {
        try {
            DespesaDAO.removerDespesaRec(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    public static void alteraEstadoDespesaRec (DespesaRecorrente a) {
        try {
            DespesaDAO.alteraEstadoDespesaRec(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
 
    
    public static void adicionaDespesaExt (DespesaExtraordinaria a) {
        try {
            DespesaDAO.guardarDespesaExt(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<DespesaExtraordinaria> listDespesaExt() {
        try {
            return DespesaDAO.listDespesaExt();
        } catch (Exception e) {
            
        }
        return null;
    }
    
    public static void removerDespesaExt (DespesaExtraordinaria a) {
        try {
            DespesaDAO.removerDespesaExt(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void alteraEstadoDespesaExt (DespesaExtraordinaria a) {
        try {
            DespesaDAO.alteraEstadoDespesaExt(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    
    
}
