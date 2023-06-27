package com.zrapp.warehouse.DAO;

import android.util.Log;

import com.zrapp.warehouse.Database.DbSqlServer;
import com.zrapp.warehouse.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    Connection objConn;

    public ProductDAO() {
        DbSqlServer db = new DbSqlServer();
        objConn = db.openConnect();
    }

    public List<Product> getAll_Prod() {
        List<Product> list = new ArrayList<Product>();

        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM SanPham";
                Statement statement = this.objConn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                while (resultSet.next()) {
                    Product objCat = new Product();
                    objCat.setId(resultSet.getString("maSP"));
                    objCat.setName(resultSet.getString("tenSP"));
                    objCat.setViTri(resultSet.getString("viTRi"));
                    objCat.setPrice(resultSet.getInt("giaBan"));
                    objCat.setCost_price(resultSet.getInt("giaVon"));
                    objCat.setImg(resultSet.getString("anhSP"));
                    list.add(objCat);
                }
            }
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getAll: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return list;
    }

    public Product getProdByID(String prodID) {
        Product prod = new Product();
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM SanPham WHERE maSP='" + prodID + "'";
                Statement statement = this.objConn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                resultSet.next();
                prod.setId(resultSet.getString("maSP"));
                prod.setName(resultSet.getString("tenSP"));
                prod.setViTri(resultSet.getString("viTRi"));
                prod.setPrice(resultSet.getInt("giaBan"));
                prod.setCost_price(resultSet.getInt("giaVon"));
                prod.setImg(resultSet.getString("anhSP"));
            }
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getAll: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return prod;
    }

    public Product getProdByName(String prodName) {
        Product prod = new Product();
        try {
            if (this.objConn != null) {
                String sqlQuery = "SELECT * FROM SanPham WHERE tenSP='" + prodName + "'";
                Statement statement = this.objConn.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                resultSet.next();
                prod.setId(resultSet.getString("maSP"));
                prod.setName(resultSet.getString("tenSP"));
                prod.setViTri(resultSet.getString("viTRi"));
                prod.setPrice(resultSet.getInt("giaBan"));
                prod.setCost_price(resultSet.getInt("giaVon"));
                prod.setImg(resultSet.getString("anhSP"));
            }
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "getAll: Có lỗi truy vấn dữ liệu ");
            e.printStackTrace();
        }
        return prod;
    }

    public void insertProd(Product objProd) {
        try {
            if (this.objConn != null) {
                String insertSQL =
                        "INSERT INTO SanPham VALUES (default,N'" + objProd.getName() + "',N'" +
                                objProd.getViTri() + "'," + "'" + objProd.getPrice() + "', '" +
                                objProd.getCost_price() + "', '"+objProd.getImg()+"') ";
                PreparedStatement stmtInsert = this.objConn.prepareStatement(insertSQL);
                stmtInsert.execute();
                Log.d("zzzzz", "insertRow: finish insert");
            }
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "insertRow: Có lỗi thêm dữ liệu ");
            e.printStackTrace();
        }
    }

    public void deleteProd(String maSp) {
        try {
            if (this.objConn != null) {
                String deleteSQL = "DELETE SanPham WHERE maSP='" + maSp + "'";
                PreparedStatement stmtDelete = this.objConn.prepareStatement(deleteSQL);
                stmtDelete.execute();
                Log.d("TAG Debug", "deleteProd: finish delete");
            }
        } catch (Exception e) {
            Log.e("TAG Lỗi", "deleteProd: Có lỗi xóa dữ liệu ");
            e.printStackTrace();
        }
    }

    public void updateProd(Product prod) {
        try {
            if (this.objConn != null) {
                // ghép chuỗi SQL
                String sqlUpdate =
                        "UPDATE SanPham SET tenSP = N'" + prod.getName() + "' ," + "viTRi = N'" +
                                prod.getViTri() + "' ," + "giaBan = '" + prod.getPrice() + "' ," +
                                "giaVon = '" + prod.getCost_price() + "' ," + "anhSP = '" +
                                prod.getImg() + "' " + "WHERE maSP = '" + prod.getId() + "' ";

                PreparedStatement stmtUpdate = this.objConn.prepareStatement(sqlUpdate);
                stmtUpdate.execute(); // thực thi câu lệnh SQL
                Log.d("zzzzz", "updateProd: finish Update");
            }
        } catch (Exception e) {
            Log.e("zzzzzzzzzz", "updateProd: Có lỗi sửa dữ liệu ");
            e.printStackTrace();
        }
    }
}
