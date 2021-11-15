package zitech.ziorder.Models;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import zitech.ziorder.Controller.JdbcController;
import zitech.ziorder.Objects.Area;
import zitech.ziorder.Objects.AreaSpinner;

public class AreaModel {

    private JdbcController jdbcController = new JdbcController();
    private Connection connection;
    private String connectResult = "";

    public AreaModel() {
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

    public ArrayList<Area> GetAreaList() throws SQLException {
        ArrayList<Area> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "select * from khuvuc";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new Area(rs.getInt("makhuvuc"), rs.getString("tenkhuvuc")));
        }
        connection.close();
        return list;
    }

    public ArrayList<AreaSpinner> GetAreaSpinnerList() throws SQLException {
        ArrayList<AreaSpinner> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "select * from khuvuc";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new AreaSpinner(rs.getInt("makhuvuc"), rs.getString("tenkhuvuc")));
        }
        connection.close();
        return list;
    }

    public boolean Insert(Area area) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into khuvuc(tenkhuvuc) values(" + area.getAreaName() + ")";
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else {
            connection.close();
            return false;
        }
    }

    public boolean Update(Area area) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update khuvuc set tenkhuvuc = " + area.getAreaName() + " where makhuvuc = " + area.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean Delete(Area area) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from khuvuc where makhuvuc = " + area.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }
}
