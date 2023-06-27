package com.zrapp.warehouse.DAO;

import android.util.Log;

import com.zrapp.warehouse.Database.DbSqlServer;
import com.zrapp.warehouse.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderDao {
    Connection objConn;

    public OrderDao() {
        // hàm khởi tạo để mở kết nối
        DbSqlServer db = new DbSqlServer();
        objConn = db.openConnect(); // tạo mới DAO thì mở kết nối CSDL
    }

    public List<Order> getAll() {
        List<Order> listCat = new ArrayList<>();
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM DonHang";
                Statement statement = this.objConn.createStatement(); // khởi tạo cấu trúc truy vấn
                ResultSet resultSet = statement.executeQuery(sqlQuery); // thực thi câu lệnh truy vấn
                while (resultSet.next()) { // đọc dữ liệu gán vào đối tượng và đưa vào list
                    Order order = new Order();
                    order.setId_order(resultSet.getString("maDH")); // truyền tên cột dữ liệu
                    order.setId_staff(resultSet.getString("maNV")); // tên cột dữ liệu là name
                    order.setDate(resultSet.getString("ngayTao"));
                    order.setKindOfOrder(resultSet.getString("loaiDH"));
                    listCat.add(order);
                }
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getAll: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return listCat;
    }

    public String getOrderID(int loaiDH) {
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT dbo.AUTO_IDDH(" + loaiDH + ")";
                Statement statement = this.objConn.createStatement(); // khởi tạo cấu trúc truy vấn
                ResultSet resultSet = statement.executeQuery(sqlQuery); // thực thi câu lệnh truy vấn
                resultSet.next();
                return resultSet.getString(1);
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng

        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getAll: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return "";
    }

    public void insertRow(Order order) {
        int type = order.getKindOfOrder().equals("Nhập") ? 0 : 1;
        try {
            if (this.objConn != null) {
                // ghép chuỗi SQL
                String insertSQL = "INSERT INTO DonHang VALUES (dbo.AUTO_IDDH(" + type + "), '" +
                        order.getId_staff() + "', N'" + order.getKindOfOrder() +
                        "', LEFT(CONVERT(VARCHAR, GETDATE(), 120), 10));";
                PreparedStatement stmtInsert = this.objConn.prepareStatement(insertSQL);
                stmtInsert.execute();
                Log.d("zzzzz", "insertRow: finish insert");
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "insertRow: Có lỗi thêm dữ liệu ");
            e.printStackTrace();
        }
    }

}
