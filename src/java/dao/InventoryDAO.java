/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Inventory;
import model.ProductOption;

/**
 *
 * @author PC
 */
public class InventoryDAO extends DAO {

    public InventoryDAO() {
        super();
    }

    public ArrayList<Inventory> getAllInventoryByPid(int productId) {
        try {
            ArrayList<Inventory> listInventory = new ArrayList<>();
            String sql2 = "select id, productOptionId, stockQuantity from tblInventory where productId = ?";
            PreparedStatement st2 = connection.prepareStatement(sql2);
            st2.setInt(1, productId);
            ResultSet rs2 = st2.executeQuery();
            while (rs2.next()) {
                Inventory inventory = new Inventory();
                inventory.setId(rs2.getInt("id"));
                inventory.setStockQuantity(rs2.getInt("stockQuantity"));
                int productOptionId = rs2.getInt("productOptionId");

                ProductOption productOption = new ProductOptionDAO().getProductOptionById(productId);
                inventory.setProductOption(productOption);
                listInventory.add(inventory);
            }
            return listInventory;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean insert(Inventory i, int productId) {
        try {
            connection.setAutoCommit(false); // Disable auto-commit
            String sql = "insert into tblProductOption(size, color) values(?, ?)";
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, i.getProductOption().getSize());
            st.setString(2, i.getProductOption().getColor());
            st.executeUpdate();

            // Lấy id của productOption vừa được insert
            ResultSet generatedKeys = st.getGeneratedKeys();
            int productOptionId = -1;
            if (generatedKeys.next()) {
                productOptionId = generatedKeys.getInt(1);
            }
            generatedKeys.close();
            st.close();

            String sql1 = "insert into tblInventory(productId, productOptionId, stockQuantity) values(?, ?, ?)";
            PreparedStatement st1 = connection.prepareStatement(sql1);
            st1.setInt(1, productId);
            st1.setInt(2, productOptionId);
            st1.setInt(3, i.getStockQuantity());
            st1.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback if any error occurs
                } catch (SQLException ex) {
                    System.out.println("Error during rollback: " + ex.getMessage());
                }
            }
            System.out.println("Error during data update: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Re-enable auto-commit
                } catch (SQLException ex) {
                    System.out.println("Error re-enabling auto-commit: " + ex.getMessage());
                }
            }
        }
        return false;
    }
}
