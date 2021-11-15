package zitech.ziorder.Models;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import zitech.ziorder.Controller.JdbcController;
import zitech.ziorder.Objects.BillDetail;

public class BillDetailModel {

    private JdbcController jdbcController = new JdbcController();
    private Connection connection;
    private String connectResult = "";

    public BillDetailModel() {
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

    public int CountBillDetailData() throws SQLException{
        Statement statement = connection.createStatement();
        String sql = "select count(*) as soluong from dbo.chitiethoadon";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            return rs.getInt("soluong");
        }
        connection.close();
        return 0;
    }

    public List<BillDetail> GetBillDetail(int billId) throws SQLException {
        List<BillDetail> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "select h.mahoadon, c.madichvu, c.soluong, c.thanhtien " +
                "from hoadon as h, ChiTietHoaDon as c " +
                "where h.MaHoaDon=c.MaHoaDon and h.MaHoaDon = "+billId;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new BillDetail(rs.getInt("mahoadon"),
                    rs.getInt("madichvu"),
                    rs.getInt("soluong"),
                    rs.getFloat("thanhtien")));
        }
        connection.close();
        return list;
    }

    public float GetTotal(int billId) throws SQLException {
        float total=0;
        Statement statement = connection.createStatement();
        String sql = "select sum(c.thanhtien) as total " +
                "from hoadon as h, ChiTietHoaDon as c " +
                "where h.MaHoaDon=c.MaHoaDon and h.MaHoaDon = "+billId;
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            total=rs.getFloat("total");
        }
        connection.close();
        return total;
    }
}
