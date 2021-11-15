package zitech.ziorder.Models;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import zitech.ziorder.Controller.JdbcController;
import zitech.ziorder.Objects.Category;
import zitech.ziorder.Objects.CategorySpinner;

public class CategoryModel {

    private JdbcController jdbcController = new JdbcController();
    private Connection connection;
    private String connectResult = "";

    public CategoryModel() {
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

    public ArrayList<CategorySpinner> GetCategorySpinnerList() throws SQLException {
        ArrayList<CategorySpinner> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        String sql = "select * from danhmuc";
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            list.add(new CategorySpinner(rs.getInt("madanhmuc"), rs.getString("tendanhmuc")));
        }
        connection.close();
        return list;
    }

    public ArrayList<Category> GetCategoryList() throws SQLException {
        ArrayList<Category> list = new ArrayList<>();
        // Tạo đối tượng Statement.
        Statement statement = connection.createStatement();
        String sql = "select * from danhmuc";
        // Thực thi câu lệnh SQL trả về đối tượng ResultSet.
        // Mọi kết quả trả về sẽ được lưu trong ResultSet
        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()) {
            // Đọc dữ liệu từ ResultSet
            list.add(new Category(rs.getInt("madanhmuc"), rs.getString("tendanhmuc")));
        }
        connection.close();// Đóng kết nối
        return list;
    }

    public boolean Insert(Category category) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "insert into danhmuc(tendanhmuc) values(" + category.getName() + ")";
        if (statement.executeUpdate(sql) > 0) { // Dùng lệnh executeUpdate cho các lệnh CRUD
            connection.close();
            return true;
        } else {
            connection.close();
            return false;
        }
    }

    public boolean Update(Category category) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "Update danhmuc set Name = " + category.getName() + " where madanhmuc = " + category.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }

    public boolean Delete(Category category) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "delete from danhmuc where madanhmuc = " + category.getId();
        if (statement.executeUpdate(sql) > 0) {
            connection.close();
            return true;
        } else
            connection.close();
        return false;
    }
}
