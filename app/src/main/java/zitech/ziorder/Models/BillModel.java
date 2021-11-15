package zitech.ziorder.Models;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import zitech.ziorder.Controller.JdbcController;
import zitech.ziorder.Objects.Bill;

public class BillModel {

    private JdbcController jdbcController = new JdbcController();
    private Connection connection;
    private String connectResult = "";

    public BillModel() {
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

    public Bill GetBill(int tableId) throws SQLException {
        Bill bill = null;
        Statement statement = connection.createStatement();
        String sql =
                "select * " +
                "from PhieuGoiBan as p, Ban as b, HoaDon as h " +
                "where p.MaBan=b.MaBan and p.MaHoaDon=h.MaHoaDon and p.MaBan = "+tableId+" and h.TrangThaiThanhToan=0";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            bill = new Bill(rs.getInt("mahoadon"),
                    rs.getInt("trangthaithanhtoan"),
                    rs.getString("ngaylap"),
                    rs.getFloat("khuyenmai"),
                    rs.getFloat("tonggiatri"));
        }
        connection.close();
        return bill;
    }

    public boolean UpdatePay(int billId, float total) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update hoadon set tonggiatri = "+total+", trangthaithanhtoan = 1 where mahoadon = " + billId;
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean HandlingUsingTable(int tableId, int dishesId, int amount) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select * from dbo.hoadon as h, dbo.phieugoiban as p where h.MaHoaDon=p.MaHoaDon and MaBan= "+tableId+" and trangthaithanhtoan = 0 ";
        ResultSet rs = statement.executeQuery(sql);
        int billId = 0;
        while (rs.next()){
            billId = rs.getInt("mahoadon");
        }

        Statement statement2 = connection.createStatement();
        String sql2 = "exec UpdateIntoExistedBill "+billId+" , "+dishesId+" , "+amount+"";
        if (statement2.executeUpdate(sql2) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public int HandlingTempTable(int tableId, String currentAccount) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into hoadon (ngaylap) values ( getdate() )";
        statement.executeUpdate(sql);

        Statement statement2 = connection.createStatement();
        String sql2 = "select max(mahoadon) as mahoadon from hoadon";
        ResultSet rs = statement2.executeQuery(sql2);
        int newBillId = 0;
        while (rs.next()){
            newBillId = rs.getInt("mahoadon");
        }

        Statement statement3 = connection.createStatement();
        String sql3 = "insert into phieugoiban (maban, mahoadon, tendangnhap) values ( "+tableId+" , "+newBillId+" , '"+currentAccount+"' )";
        statement3.executeUpdate(sql3);
        connection.close();
        return newBillId;
    }
}
