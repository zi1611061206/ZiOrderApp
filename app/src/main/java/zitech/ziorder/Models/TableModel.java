package zitech.ziorder.Models;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import zitech.ziorder.Controller.JdbcController;
import zitech.ziorder.Objects.Table;

public class TableModel {

    private JdbcController jdbcController = new JdbcController();
    private Connection connection;
    private String connectResult = "";

    public TableModel() {
        try {
            connection = jdbcController.ConnnectionData();
            if (connection == null) {
                connectResult = "Check your internet access!";
            }
        } catch (Exception e) {
            connectResult = e.getMessage();
        }
        Log.e("test", connectResult);
    }

    public ArrayList<Table> GetTableList() throws SQLException {
        ArrayList<Table> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "select * from ban";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new Table(rs.getInt("maban"),
                    rs.getInt("makhuvuc"),
                    rs.getString("tenban"),
                    rs.getInt("trangthaisudung")));
        }
        connection.close();
        return list;
    }

    public int CountFullTable() throws SQLException{
        Statement statement = connection.createStatement();
        String sql = "select count(*) as soluong from dbo.ban where trangthaisudung = 1";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            return rs.getInt("soluong");
        }
        connection.close();
        return 0;
    }

    public boolean Insert(int areaId, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into ban(makhuvuc, tenban) values("+areaId+",'"+tableName+"')";
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else {
            connection.close();
        }
        return false;
    }

    public boolean Update(Table table) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update ban set makhuvuc = " + table.getAreaId()
                + ", tenban = " + table.getTableName()
                + " where makhuvuc = " + table.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean UpdateStatus(int tableId) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update ban set trangthaisudung = 0 where maban = " + tableId;
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean Delete(Table table) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from ban where maban = " + table.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public ArrayList<Table> GetTableListByAreaId(int id) throws SQLException {
        ArrayList<Table> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "select * from ban where makhuvuc = " + id;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new Table(rs.getInt("maban"),
                    rs.getInt("makhuvuc"),
                    rs.getString("tenban"),
                    rs.getInt("trangthaisudung")));
        }
        connection.close();
        return list;
    }

    public ArrayList<Table> SearchTableByName(String tableName) throws SQLException {
        ArrayList<Table> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "exec exec SearchTableName '"+tableName+"'";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new Table(rs.getInt("maban"),
                    rs.getInt("makhuvuc"),
                    rs.getString("tenban"),
                    rs.getInt("trangthaisudung")));
        }
        connection.close();
        return list;
    }

    public ResultSet CheckTableStatus(int id) throws SQLException{
        Statement statement=connection.createStatement();
        String sql = "select * from ban where maban = " + id;
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }
}
