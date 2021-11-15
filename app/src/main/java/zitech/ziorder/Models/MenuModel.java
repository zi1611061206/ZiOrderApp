package zitech.ziorder.Models;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import zitech.ziorder.Controller.JdbcController;
import zitech.ziorder.Objects.Menu;

public class MenuModel {

    private JdbcController jdbcController = new JdbcController();
    private Connection connection;
    private String connectResult = "";

    public MenuModel() {
        try {
            // Tạo kết nối tới database
            connection = jdbcController.ConnnectionData();
            if (connection == null) {
                connectResult = "Check your internet access!";
            }
        } catch (Exception e) {
            connectResult = e.getMessage();
        }
        Log.e("test", connectResult);
    }

    public ArrayList<Menu> GetMenuList() throws SQLException {
        ArrayList<Menu> list = new ArrayList<>();
        // Tạo đối tượng Statement.
        Statement statement = connection.createStatement();
        String sql = "select * from dichvu";
        // Thực thi câu lệnh SQL trả về đối tượng ResultSet.
        // Mọi kết quả trả về sẽ được lưu trong ResultSet
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            // Đọc dữ liệu từ ResultSet
            list.add(new Menu(rs.getInt("madichvu"),
                    rs.getInt("madanhmuc"),
                    rs.getString("tendichvu"),
                    rs.getBytes("hinhanh"),
                    rs.getFloat("dongia")));
        }
        connection.close();// Đóng kết nối
        return list;
    }

    public boolean Insert(int categoryId, String dishesName, float price, Bitmap dishesImage) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into dbo.dichvu (madanhmuc,tendichvu,dongia,hinhanh) values ( "+categoryId+" , '"+dishesName+"' , "+price+" , '"+ConvertImageToBase64(dishesImage)+"' )";
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else {
            connection.close();
        }
        return false;
    }

    private String ConvertImageToBase64(Bitmap dishImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        dishImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public boolean Update(Menu menu) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update dichvu set tendichvu = " + menu.getDishName()
                + ", hinhanh = " + menu.getImage()
                + ", dongia = " + menu.getPrice()
                + " where madichvu = " + menu.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean Delete(Menu menu) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from dichvu where madichvu = " + menu.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public ArrayList<Menu> GetMenuListByCategoryId(int id) throws SQLException {
        ArrayList<Menu> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "select * from dichvu where madanhmuc = " + id;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new Menu(rs.getInt("madichvu"),
                    rs.getInt("madanhmuc"),
                    rs.getString("tendichvu"),
                    rs.getBytes("hinhanh"),
                    rs.getFloat("dongia")));
        }
        connection.close();
        return list;
    }

    public ArrayList<Menu> SearchTableByName(String dishesName) throws SQLException {
        ArrayList<Menu> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "exec SearchMenuName '"+dishesName+"'";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new Menu(rs.getInt("madichvu"),
                    rs.getInt("madanhmuc"),
                    rs.getString("tendichvu"),
                    rs.getBytes("hinhanh"),
                    rs.getFloat("dongia")));
        }
        connection.close();
        return list;
    }

    public Menu GetDishDetail(int id) throws SQLException{
        Menu menu = null;
        Statement statement = connection.createStatement();
        String sql ="select * from dichvu where madichvu = "+id;
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            menu = new Menu(rs.getInt("madichvu"),
                    rs.getInt("madanhmuc"),
                    rs.getString("tendichvu"),
                    rs.getBytes("hinhanh"),
                    rs.getFloat("dongia"));
        }
        connection.close();
        return menu;
    }
}
