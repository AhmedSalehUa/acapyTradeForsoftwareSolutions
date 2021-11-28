/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import acapytradeforsoftwaresolutions.AcapyTradeForSoftwareSolutions;
import assets.classes.TableToExcel;  
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AHMED
 */
public class get {

    private static String url = "";
    private static Connection con; 
    private static void setURL() throws Exception {
        try {
            Preferences prefs = Preferences.userNodeForPackage(AcapyTradeForSoftwareSolutions.class);

            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://" + prefs.get("database_id", "192.168.1.90") + ":3306/software.solutions?useUnicode=true&characterEncoding=UTF-8";

        } catch (ClassNotFoundException ex) {
            throw new Exception("error in database url");
        }

    }

    public static Connection getReportCon() throws Exception {
        try {
            setURL();
            con = DriverManager.getConnection(url, "acapytradeahmedsaleh", "as01203904426");
        } catch (SQLException ex) {
            throw new Exception("Error in connection to database ERROR Code: \n" + ex.getMessage());
        }
        return con;
    }
    public static boolean canCon() throws Exception {
        try {
            setURL();
            con = DriverManager.getConnection(url, "acapytradeahmedsaleh", "as01203904426");
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }
   
    public static void setConnection()  throws Exception{

        try {
            setURL();
            con = DriverManager.getConnection(url, "acapytradeahmedsaleh", "as01203904426");
        } catch (SQLException ex) {
           throw new Exception( "Error in connection to database ERROR Code: \n" + ex.getMessage());

        }
    }
  

    public static JTable getTableData(String statement) throws Exception{

        try {
            setConnection();
            Statement st = con.createStatement();
            ResultSet rs;
            rs = st.executeQuery(statement);
            ResultSetMetaData rsmt = rs.getMetaData();
            int c = rsmt.getColumnCount();

            JTable table = new JTable(0, c);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            while (rs.next()) {
                Object rows[] = new Object[c];

                for (int i = 0; i < c; i++) {
                    rows[i] = rs.getString(i + 1);
                }
                model.addRow(rows);
            }
            con.close();
            return table;
        } catch (SQLException ex) {
           throw new Exception( ex.getMessage());
        }
    }
 
    public static boolean runNonQuery(String statement)  throws Exception{
        try {
            setConnection();
            Statement stmt = con.createStatement();
            stmt.execute(statement);
            con.close();
            return true;
        } catch (SQLException ex) {
           throw new Exception(  ex.getMessage());
            
        }

    }
 

    public static boolean runNonQueryPrepare(PreparedStatement statement)  throws Exception{
        try {
            setConnection();
            statement.execute();
            con.close();
            return true;
        } catch (SQLException ex) {
            throw new Exception( ex.getMessage());
           
        }

    }

    public static boolean excutePrepare(PreparedStatement statement) throws Exception {
        try {
            setConnection();
            statement.execute();
            con.close();
            return true;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public static PreparedStatement Prepare(String statement)throws Exception {
        try {
            setConnection();
            PreparedStatement stat = con.prepareStatement(statement);
            return stat;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());

        }
       
    }

    public static Statement getBatchStatement() throws Exception{
        try {
            setConnection();
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            con.setAutoCommit(false);
            return st;
        } catch (SQLException ex) {
            throw new Exception( ex.getMessage());
        }
        
    }
   
    public static void exportToExcel(JTable table) throws Exception {
        TableToExcel tte = new TableToExcel(table, null, "My Table");
        try {
            File directory = new File(System.getProperty("user.home") + "\\Desktop\\Judges Club Report");
            directory.mkdir();
            String Filename = JOptionPane.showInputDialog(null, "ادخل اسم الملف");
            File f = new File(directory + "\\" + Filename + ".xls");
            boolean a = tte.generate(f);
            if (a) {
                JOptionPane.showMessageDialog(null, "تم تصدير الملف الى فولدر (desktop//Judges Club Report//" + Filename + ".xls ) بنجاح");
            } else {
                JOptionPane.showMessageDialog(null, "حدث خطأ");
            }

        } catch (Exception ex) {
           throw new Exception( ex.getMessage());
        }

    }
 
}
 
