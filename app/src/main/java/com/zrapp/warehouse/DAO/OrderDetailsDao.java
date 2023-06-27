package com.zrapp.warehouse.DAO;

import android.util.Log;

import androidx.core.util.Pair;

import com.zrapp.warehouse.Database.DbSqlServer;
import com.zrapp.warehouse.model.Order;
import com.zrapp.warehouse.model.OrderDetails;
import com.zrapp.warehouse.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class OrderDetailsDao {
    Connection objConn;

    public OrderDetailsDao() {
        // hàm khởi tạo để mở kết nối
        DbSqlServer db = new DbSqlServer();
        objConn = db.openConnect(); // tạo mới DAO thì mở kết nối CSDL
    }

    public List<OrderDetails> getAll(String orderID) {
        List<OrderDetails> list = new ArrayList<>();

        try {
            if (this.objConn != null) {
                String sqlQuery =
                        "SELECT DHCT.maSP, tenSP, giaBan, giaVon, soLuong, loaiDH " + "FROM DHCT " +
                                "INNER JOIN SanPham SP ON SP.maSP = DHCT.maSP " +
                                "INNER JOIN DonHang DH ON DH.maDH = DHCT.maHD " + "WHERE maHD = '" +
                                orderID + "'";
                Statement statement = this.objConn.createStatement(); // khởi tạo cấu trúc truy vấn
                ResultSet resultSet = statement.executeQuery(sqlQuery); // thực thi câu lệnh truy vấn
                while (resultSet.next()) { // đọc dữ liệu gán vào đối tượng và đưa vào list
                    OrderDetails orderDetails = new OrderDetails();
                    orderDetails.setOrder(new Order(orderID, resultSet.getString("loaiDH")));
                    orderDetails.setProd(new Product(resultSet.getString("maSP"), resultSet.getString("tenSP"), resultSet.getInt("giaBan"), resultSet.getInt("giaVon")));
                    orderDetails.setQty(resultSet.getInt("soLuong"));
                    list.add(orderDetails);
                }
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng

        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getAll: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return list;
    }

    public void insertOrderDetail(OrderDetails details) {
        try {
            if (this.objConn != null) {
                // ghép chuỗi SQL
                String insertSQL =
                        "INSERT INTO DHCT VALUES('" + details.getOrder().getId_order() + "', '" +
                                details.getProd().getId() + "', " + details.getQty() + ")";
                PreparedStatement stmtInsert = this.objConn.prepareStatement(insertSQL);
                stmtInsert.execute();

                Log.d("zzzzz", "insertOrderDetail: finish insert");
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "insertOrderDetail: Có lỗi thêm dữ liệu ");
            e.printStackTrace();
        }
    }

    public void deleteOrderDetail(String idOrder) {
        try {
            if (this.objConn != null) {
                String deleteSQL = "DELETE DHCT WHERE maHD = '" + idOrder + "' " +
                        "DELETE DonHang WHERE maDH = '" + idOrder + "'";
                PreparedStatement stmtDelete = this.objConn.prepareStatement(deleteSQL);
                stmtDelete.execute();
                Log.d("TAG Debug", "deleteOrderDetail: success");
            }
        } catch (Exception e) {
            Log.e("TAG Lỗi", "deleteOrderDetail: Có lỗi xóa dữ liệu ");
            e.printStackTrace();
        }
    }

    public String getTotal(Order order) {
        String type;
        if (order.getKindOfOrder().equals("Nhập")) type = "giaVon";
        else type = "giaBan";
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT SUM(" + type +
                        " * soLuong) FROM DHCT INNER JOIN SanPham SP ON SP.maSP = DHCT.maSP " +
                        "WHERE maHD='" + order.getId_order() + "' " + "GROUP BY maHD";
                Statement statement = this.objConn.createStatement(); // khởi tạo cấu trúc truy vấn
                ResultSet resultSet = statement.executeQuery(sqlQuery); // thực thi câu lệnh truy vấn
                Locale locale = new Locale("vi", "VN");
                NumberFormat nf = NumberFormat.getInstance(locale);
                resultSet.next();
                return nf.format(resultSet.getInt(1));
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getTotal: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return 0 + "";
    }

    public List<Product> getStatistic(int type, int time, int value) {
        List<Product> list = new ArrayList<>();

        try {
            if (this.objConn != null) {
                String sqlQuery =
                        "SELECT * FROM dbo.fuStatistic(" + type + "," + time + "," + value + ")";
                Statement statement = this.objConn.createStatement(); // khởi tạo cấu trúc truy vấn
                ResultSet resultSet = statement.executeQuery(sqlQuery); // thực thi câu lệnh truy vấn
                while (resultSet.next()) { // đọc dữ liệu gán vào đối tượng và đưa vào list
                    Product prod = new Product();
                    prod.setName(resultSet.getString("tenSP"));
                    prod.setPrice(resultSet.getInt("Giá trị"));
                    list.add(prod);
                }
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getStatistic: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getStatisticDiff(int type, Object selection, int value) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Pair<Long, Long> st = (Pair<Long, Long>) selection;
        calendar.setTimeInMillis(st.first);
        String dateFrom = dateFormat.format(calendar.getTime());
        calendar.setTimeInMillis(st.second);
        String dateTo = dateFormat.format(calendar.getTime());

        List<Product> list = new ArrayList<>();
        try {
            if (this.objConn != null) {
                String sqlQuery =
                        "SELECT * FROM dbo.fuStatisticDiff (" + type + ",'" + dateFrom + "','" +
                                dateTo + "'," + value + ")";
                Statement statement = this.objConn.createStatement(); // khởi tạo cấu trúc truy vấn
                ResultSet resultSet = statement.executeQuery(sqlQuery); // thực thi câu lệnh truy vấn
                while (resultSet.next()) { // đọc dữ liệu gán vào đối tượng và đưa vào list
                    Product prod = new Product();
                    prod.setName(resultSet.getString("tenSP"));
                    prod.setPrice(resultSet.getInt("Giá trị"));
                    list.add(prod);
                }
            } // nếu kết nối khác null thì mới select và thêm dữ liệu vào, nếu không thì trả về ds rỗng
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getStatisticDiff: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return list;
    }
}
