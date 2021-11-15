package zitech.ziorder.Models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import zitech.ziorder.Controller.JdbcController;
import zitech.ziorder.Objects.Account;

public class AccountModel {

    private JdbcController jdbcController = new JdbcController();
    private Connection connection;

    public AccountModel() {
        connection = jdbcController.ConnnectionData();
    }

    public boolean CheckLoginInfo(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select * from taikhoan where tendangnhap='" + username + "' and matkhau='" + password + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            connection.close();
            return true;
        } else {
            connection.close();
            return false;
        }
    }

    public Account GetAccount(String currentAccount) throws SQLException {
        Account account = null;
        Statement statement = connection.createStatement();
        String sql = "select * from dbo.nhanvien as n, dbo.taikhoan as t where n.manhanvien=t.manhanvien and tendangnhap = '" + currentAccount + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            account = new Account(rs.getString("tendangnhap"),
                    rs.getInt("machucvu"),
                    rs.getInt("manhanvien"),
                    rs.getString("matkhau"),
                    rs.getString("hoten"),
                    rs.getString("diachi"),
                    rs.getString("sodienthoai"),
                    rs.getString("chungminhnhandan"),
                    rs.getBytes("anhdaidien"));
        }
        connection.close();
        return account;
    }

    public String GetPositionName(int positionId) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select * from chucvu where machucvu = " + positionId;
        ResultSet rs = statement.executeQuery(sql);
        String positionName = "";
        while (rs.next()) {
            positionName = rs.getString("tenchucvu");
        }
        connection.close();
        return positionName;
    }

    public boolean CheckOldPass(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "select * from taikhoan where tendangnhap = '" + username + "' and matkhau = '" + password + "'";
        ResultSet rs = statement.executeQuery(sql);
        if (rs.next()) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean Insert(Account account) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into taikhoan(tendangnhap, matkhau, machucvu, manhanvien) values("
                + account.getUsername()
                + account.getPassword()
                + account.getPositionId()
                + account.getStaffId() + ")";
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else {
            connection.close();
            return false;
        }
    }

    public boolean UpdatePass(String username, String password) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update taikhoan set matkhau = '" + password + "' where tendangnhap = '" + username + "'";
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean ResetPass(Account account) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update taikhoan set matkhau = 1 where tendangnhap = " + account.getUsername();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean Delete(Account account) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from taikhoan where tendangnhap = " + account.getUsername();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }
}
