/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import model.FilterRequest;
import model.FilterResponse;
import model.Inventory;
import model.Order;
import model.OrderDetail;
import model.PaymentMethod;
import model.Product;
import model.Status;
import model.User;

/**
 *
 * @author PC
 */
public class OrderDAO extends DAO {

    public OrderDAO() {
        super();
    }

    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            String sql = "select * from tblOrder";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                int orderId = rs.getInt("id");
                order.setId(orderId);
                order.setOrderDate(rs.getDate("orderDate"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                order.setReasonForCancel(rs.getString("reasonForCancel"));
                order.setNote(rs.getString("note"));

                Status status = new Status();
                int statusId = rs.getInt("statusId");
                status.setId(statusId);
                String sql1 = "select * from tblStatus where id = ?";
                PreparedStatement st1 = connection.prepareStatement(sql1);
                st1.setInt(1, statusId);
                ResultSet rs1 = st1.executeQuery();
                if (rs1.next()) {
                    status.setStatusName(rs1.getString("statusName"));
                }
                st1.close();
                order.setStatus(status);

                int paymentMethodId = rs.getInt("paymentMethodId");
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setId(paymentMethodId);
                String sql2 = "select * from tblPaymentMethod where id = ?";
                PreparedStatement st2 = connection.prepareStatement(sql2);
                st2.setInt(1, paymentMethodId);
                ResultSet rs2 = st2.executeQuery();
                if (rs2.next()) {
                    paymentMethod.setMethodName(rs2.getString("methodName"));
                }
                st2.close();
                order.setPaymentMethod(paymentMethod);

                ArrayList<OrderDetail> listOrderDetail = new ArrayList<>();
                String sql3 = "select * from tblOrderDetail where orderId = ?";
                PreparedStatement st3 = connection.prepareStatement(sql3);
                st3.setInt(1, orderId);
                ResultSet rs3 = st3.executeQuery();
                while (rs3.next()) {
                    OrderDetail od = new OrderDetail();
                    od.setId(rs3.getInt("id"));
                    od.setPrice(rs3.getDouble("price"));
                    od.setQuantity(rs3.getInt("quantity"));

                    int productId = rs3.getInt("productId");
                    ProductDAO productDao = new ProductDAO();
                    Product p = productDao.getProductById(productId);
                    od.setProduct(p);
                    listOrderDetail.add(od);
                }
                order.setOrderDetails(listOrderDetail);

                int userId = rs.getInt("userId");
                String sql4 = "select * from tblUser where id = ?";
                PreparedStatement st4 = connection.prepareStatement(sql4);
                st4.setInt(1, userId);
                ResultSet rs4 = st4.executeQuery();
                User u = new User();
                if (rs4.next()) {
                    u.setId(userId);
                    u.setAddress(rs4.getString("address"));
                    u.setEmail(rs4.getString("email"));
                    u.setPhone(rs4.getString("phone"));
                    u.setFullName(rs4.getString("fullName"));
                    u.setUsername(rs4.getString("username"));
                }
                order.setUser(u);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return orders;
    }

    public boolean updateStatusOrder(int orderId, Status s) {
        String sql = "update tblOrder set statusId = ? where id = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, s.getId());
            st.setInt(2, orderId);
            int row = st.executeUpdate();
            return row != 0;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public ArrayList<Order> getAllOrderByUid(int uid) {
        ArrayList<Order> orders = new ArrayList<>();
        try {
            String sql = "select * from tblOrder where userId = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, uid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                int orderId = rs.getInt("id");
                order.setId(orderId);
                order.setOrderDate(rs.getDate("orderDate"));
                order.setTotalAmount(rs.getDouble("totalAmount"));
                order.setReasonForCancel(rs.getString("reasonForCancel"));
                order.setNote(rs.getString("note"));

                Status status = new Status();
                int statusId = rs.getInt("statusId");
                status.setId(statusId);
                String sql1 = "select * from tblStatus where id = ?";
                PreparedStatement st1 = connection.prepareStatement(sql1);
                st1.setInt(1, statusId);
                ResultSet rs1 = st1.executeQuery();
                if (rs1.next()) {
                    status.setStatusName(rs1.getString("statusName"));
                }
                st1.close();
                order.setStatus(status);

                int paymentMethodId = rs.getInt("paymentMethodId");
                PaymentMethod paymentMethod = new PaymentMethod();
                paymentMethod.setId(paymentMethodId);
                String sql2 = "select * from tblPaymentMethod where id = ?";
                PreparedStatement st2 = connection.prepareStatement(sql2);
                st2.setInt(1, paymentMethodId);
                ResultSet rs2 = st2.executeQuery();
                if (rs2.next()) {
                    paymentMethod.setMethodName(rs2.getString("methodName"));
                }
                st2.close();
                order.setPaymentMethod(paymentMethod);

                ArrayList<OrderDetail> listOrderDetail = new ArrayList<>();
                String sql3 = "select * from tblOrderDetail where orderId = ?";
                PreparedStatement st3 = connection.prepareStatement(sql3);
                st3.setInt(1, orderId);
                ResultSet rs3 = st3.executeQuery();
                while (rs3.next()) {
                    OrderDetail od = new OrderDetail();
                    od.setId(rs3.getInt("id"));
                    od.setPrice(rs3.getDouble("price"));
                    od.setQuantity(rs3.getInt("quantity"));

                    int productId = rs3.getInt("productId");
                    ProductDAO productDao = new ProductDAO();
                    Product p = productDao.getProductById(productId);
                    od.setProduct(p);
                    listOrderDetail.add(od);
                }
                order.setOrderDetails(listOrderDetail);

                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return orders;
    }

    public boolean addOrder(Order o) {
        try {
            connection.setAutoCommit(false); // Disable auto-commit      

            // Insert into tblOrder
            String sql = "INSERT INTO tblOrder (orderDate, totalAmount, statusId, paymentMethodId, userId, note) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement st = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setDate(1, new java.sql.Date(o.getOrderDate().getTime()));
            st.setDouble(2, o.getTotalAmount());
            st.setInt(3, o.getStatus().getId());
            st.setInt(4, o.getPaymentMethod().getId());
            st.setInt(5, o.getUser().getId());
            st.setString(6, o.getNote());
            st.executeUpdate();

            // Get the generated order ID
            ResultSet generatedKeys = st.getGeneratedKeys();
            int orderId = -1;
            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }
            o.setId(orderId);
            generatedKeys.close();
            st.close();

            // Insert into tblOrderDetail and update tblInventory
            for (OrderDetail detail : o.getOrderDetails()) {
                for (Inventory iv : detail.getProduct().getInventory()) {
                    // Insert into tblOrderDetail
                    String detailSql = "INSERT INTO tblOrderDetail (orderId, productId, quantity, price) VALUES (?, ?, ?, ?)";
                    PreparedStatement st1 = connection.prepareStatement(detailSql);
                    st1.setInt(1, orderId);
                    st1.setInt(2, detail.getProduct().getId());
                    st1.setInt(3, iv.getStockQuantity());
                    st1.setDouble(4, detail.getPrice());
                    st1.executeUpdate();
                    st1.close();

                    // Update tblInventory
                    String inventorySql = "UPDATE tblInventory SET stockQuantity = stockQuantity - ? WHERE productId = ? and productOptionId = ?";
                    PreparedStatement st2 = connection.prepareStatement(inventorySql);
                    st2.setInt(1, iv.getStockQuantity());
                    st2.setInt(2, detail.getProduct().getId());
                    st2.setInt(3, iv.getProductOption().getId());
                    st2.executeUpdate();
                    st2.close();
                }

            }

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
            System.out.println("Error during data insertion: " + e.getMessage());
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

    public FilterResponse statsRevenue(FilterRequest fr) {
        FilterResponse filterResponse = new FilterResponse();
        try {
            List<String> labels = new ArrayList<>();
            List<Double> amounts = new ArrayList<>();
            String query = "SELECT orderDate, SUM(totalAmount) AS totalAmount FROM tblOrder WHERE orderDate BETWEEN ? AND ? GROUP BY orderDate";
            PreparedStatement pst = connection.prepareStatement(query);

            LocalDate fromDate = null;
            LocalDate toDate = null;

            if (fr.getFilterCriteria() != null && !fr.getFilterCriteria().equals("")) {
                switch (fr.getFilterCriteria()) {
                    case "lastMonth":
                        YearMonth lastMonth = YearMonth.now().minusMonths(1);
                        fromDate = lastMonth.atDay(1);
                        toDate = lastMonth.atEndOfMonth();
                        break;
                    case "thisMonth":
                        YearMonth thisMonth = YearMonth.now();
                        fromDate = thisMonth.atDay(1);
                        toDate = thisMonth.atEndOfMonth();
                        break;
                    case "year":
                        fromDate = LocalDate.now().withDayOfYear(1);
                        toDate = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
                        break;
                }
            } else {
                fromDate = LocalDate.parse(fr.getFromDate());
                toDate = LocalDate.parse(fr.getToDate());
            }

            pst.setDate(1, java.sql.Date.valueOf(fromDate));
            pst.setDate(2, java.sql.Date.valueOf(toDate));
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                labels.add(rs.getString("orderDate"));
                amounts.add(rs.getDouble("totalAmount"));
            }

            rs.close();
            pst.close();

            filterResponse.setLabels(labels);
            filterResponse.setAmounts(amounts);
        } catch (SQLException e) {
            System.out.println(e);
        }

        return filterResponse;
    }

}
