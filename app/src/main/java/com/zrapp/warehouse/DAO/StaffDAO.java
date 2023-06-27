package com.zrapp.warehouse.DAO;

import android.util.Log;

import com.zrapp.warehouse.Database.DbSqlServer;
import com.zrapp.warehouse.model.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StaffDAO {
    Connection objConn;

    public StaffDAO() {
        DbSqlServer db = new DbSqlServer();
        objConn = db.openConnect();
    }

    //Hiển thị danh sách nhân viên
    public ArrayList<Staff> getAll() {
        ArrayList<Staff> list = new ArrayList<>();

        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM NhanVien";
                Statement statement = this.objConn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                resultSet.next();
                while (resultSet.next()) {
                    Staff staff = new Staff();
                    staff.setId(resultSet.getString("maNV"));
                    staff.setName(resultSet.getString("tenNV"));
                    staff.setUsername(resultSet.getString("taiKhoan"));
                    staff.setPass(resultSet.getString("matKhau"));
                    staff.setTel(resultSet.getString("dienThoai"));
                    staff.setPost(resultSet.getString("chucVu"));
                    staff.setImg(resultSet.getString("anhNV"));
                    list.add(staff);
                }
            }
        } catch (Exception e) {
            Log.e("TAG Lỗi", "getAll: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return list;
    }

    //Lấy ra tài khoản
    public Staff getStaff(String username) {
        Staff staff = new Staff();
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM NhanVien WHERE taiKhoan = '" + username + "'";

                Statement statement = this.objConn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                while (resultSet.next()) {
                    staff.setId(resultSet.getString("maNV"));
                    staff.setName(resultSet.getString("tenNV"));
                    staff.setUsername(resultSet.getString("taiKhoan"));
                    staff.setPass(resultSet.getString("matKhau"));
                    staff.setTel(resultSet.getString("dienThoai"));
                    staff.setImg(resultSet.getString("anhNV"));
                    staff.setPost(resultSet.getString("chucVu"));
                    staff.setStatus(resultSet.getBoolean("trangThai"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    //Lấy ra tài khoản theo id
    public Staff getStaffByID(String id) {
        Staff staff = new Staff();
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM NhanVien WHERE maNV = '" + id + "'";

                Statement statement = this.objConn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                while (resultSet.next()) {
                    staff.setId(resultSet.getString("maNV"));
                    staff.setName(resultSet.getString("tenNV"));
                    staff.setUsername(resultSet.getString("taiKhoan"));
                    staff.setPass(resultSet.getString("matKhau"));
                    staff.setTel(resultSet.getString("dienThoai"));
                    staff.setImg(resultSet.getString("anhNV"));
                    staff.setPost(resultSet.getString("chucVu"));
                    staff.setStatus(resultSet.getBoolean("trangThai"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    //Lấy ra danh sách tài khoản yêu cầu
    public ArrayList<Staff> getRequest() {
        ArrayList<Staff> list = new ArrayList<>();
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM NhanVien WHERE trangThai='false'";
                Statement statement = this.objConn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                while (resultSet.next()) {
                    Staff staff = new Staff();
                    staff.setId(resultSet.getString("maNV"));
                    staff.setName(resultSet.getString("tenNV"));
                    staff.setUsername(resultSet.getString("taiKhoan"));
                    staff.setPass(resultSet.getString("matKhau"));
                    staff.setTel(resultSet.getString("dienThoai"));
                    staff.setImg(resultSet.getString("anhNV"));
                    staff.setPost(resultSet.getString("chucVu"));
                    staff.setStatus(resultSet.getBoolean("trangThai"));
                    list.add(staff);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //Thêm nhân viên
    public void insertStaff(Staff staff, int status) {
        try {
            if (this.objConn != null) {
                String insertSQL =
                        "INSERT INTO NhanVien VALUES (default," + "N'" + staff.getName() + "'," +
                                "'" + staff.getUsername() + "'," + "'" + staff.getPass() + "'," +
                                "'" + staff.getTel() + "','"+ staff.getImg() +"'," + "N'" + staff.getPost() + "'," +
                                status + ") ";

                PreparedStatement stmtInsert = this.objConn.prepareStatement(insertSQL);
                stmtInsert.execute();
                Log.d("TAG Debug", "insertRow: finish insert");
            }
        } catch (Exception e) {
            Log.e("TAG Lỗi", "insertRow: Có lỗi thêm dữ liệu ");
            e.printStackTrace();
        }
    }

    //Xóa nhân viên
    public void deleteRow(String maNv) {
        try {
            if (this.objConn != null) {
                String deleteSQL = "DELETE NhanVien WHERE maNV='" + maNv + "'";

                PreparedStatement stmtDelete = this.objConn.prepareStatement(deleteSQL);
                stmtDelete.execute();
                Log.d("TAG Debug", "deleteRow: delete success");
            }
        } catch (Exception e) {
            Log.e("TAG Lỗi", "deleteRow: Có lỗi xóa dữ liệu ");
            e.printStackTrace();
        }
    }

    //Sửa thông tin nhân viên
    public void updateRow(Staff staff) {
        try {
            if (this.objConn != null) {
                String sqlUpdate = "UPDATE NhanVien SET " + "tenNV = '" + staff.getName() + "'," +
                        "taiKhoan = '" + staff.getUsername() + "'," + "dienThoai ='" + staff.getTel() + "'," + "anhNV='" +
                        staff.getImg() + "'," + "chucVu='" + staff.getPost() + "'," + "trangThai='" +
                        staff.isStatus() + "' " + "WHERE maNV = '" + staff.getId() + "'";

                PreparedStatement stmtUpdate = this.objConn.prepareStatement(sqlUpdate);
                stmtUpdate.execute();
                Log.d("zzzzz", "updateRow: success");
            }
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "updateRow: Có lỗi sửa dữ liệu");
            e.printStackTrace();
        }
    }

    public boolean isExistsStaff(String username) {
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM NhanVien WHERE maNV='" + username + "'";

                PreparedStatement statement = this.objConn.prepareStatement(sqlQuery);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }
}

