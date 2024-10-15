/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.CategoryDAO;
import dao.ReviewDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import model.Category;
import model.Inventory;
import model.Product;
import model.ProductOption;
import model.Reviews;
import model.User;

/**
 *
 * @author PC
 */
@WebServlet(name = "ProductDetailControl", urlPatterns = {"/product-detail"})
public class ProductDetailControl extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductDetailControl</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductDetailControl at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "changeColor" ->
                changeColor(request, response);
//            case "changeSize" ->
//                changeSize(request, response);
            default ->
                getProductDetail(request, response);
        }
    }

    private void changeColor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        int productId = Integer.parseInt(request.getParameter("id"));
        String selectedColor = request.getParameter("selectedColor");
        session.setAttribute("selectedColor", selectedColor);
        response.sendRedirect("product-detail?id=" + productId);
    }

//    private void changeSize(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        HttpSession session = request.getSession();
//        int productId = Integer.parseInt(request.getParameter("id"));
//        String selectedSize = request.getParameter("selectedSize");
//        session.setAttribute("selectedSize", selectedSize);
//        response.sendRedirect("product-detail?id=" + productId);
//    }

    private void getProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ArrayList<Category> listCategory = (ArrayList<Category>) session.getAttribute("listCategory");
        if (listCategory == null) {
            CategoryDAO categoryDao = new CategoryDAO();
            listCategory = categoryDao.getAllCategory();
            session.setAttribute("listCategory", listCategory);
        }
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = null;
        for (int i = 0; i < listCategory.size(); i++) {
            ArrayList<Product> list = listCategory.get(i).getProducts();
            for (int j = 0; j < list.size(); j++) {
                Product p = list.get(j);
                if (productId == p.getId()) {
                    product = new Product(p);
                    break;
                }
            }
        }

        Map<String, ArrayList<String>> colorSizeMap = new HashMap<>();
        // Iterate through each Inventory object
        for (Inventory inventory : product.getInventory()) {
            ProductOption productOption = inventory.getProductOption();
            String color = productOption.getColor();
            String size = productOption.getSize();

            // Check if the color already exists in the map
            if (colorSizeMap.containsKey(color)) {
                // If color already exists, add size to its ArrayList
                ArrayList<String> sizes = colorSizeMap.get(color);
                sizes.add(size);
            } else {
                // If color doesn't exist, create a new ArrayList for sizes
                ArrayList<String> sizes = new ArrayList<>();
                sizes.add(size);
                // Add color and corresponding sizes to the map
                colorSizeMap.put(color, sizes);
            }
        }
        // lấy màu chọn mặc định đầu tiên
        String selectedColor = (String) session.getAttribute("selectedColor");
//        String selectedSize = (String) session.getAttribute("selectedSize");
        if (selectedColor == null || colorSizeMap.get(selectedColor) == null) {
            ArrayList<Inventory> list = product.getInventory();
            selectedColor = list.get(0).getProductOption().getColor();
            session.setAttribute("selectedColor", selectedColor);
        }
//        if (selectedSize == null) {
//            ArrayList<Inventory> list = product.getInventory();
//            selectedSize = list.get(0).getProductOption().getSize();
//            session.setAttribute("selectedSize", selectedSize);
//        }

        // lấy danh sách bình luận 
        ReviewDAO reviewDao = new ReviewDAO();
        ArrayList<Reviews> listReview = reviewDao.getAllReviewByPid(productId);

        ArrayList<Product> cart = getCartFromCookie(request);
        int cartSize = 0;
        for (Product p : cart) {
            cartSize += p.getInventory().size();
        }

        request.setAttribute("cartSize", cartSize);
        request.setAttribute("colorSizeMap", colorSizeMap);
        request.setAttribute("listReview", listReview);
        request.setAttribute("product", product);
        request.getRequestDispatcher("client/product-detail.jsp").forward(request, response);
    }

    private ArrayList<Product> getCartFromCookie(HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        ArrayList<Product> cart = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cart")) {
                    String cartJson = URLDecoder.decode(cookie.getValue(), "UTF-8");
                    Type listType = new TypeToken<ArrayList<Product>>() {
                    }.getType();
                    cart = new Gson().fromJson(cartJson, listType);
                    break;
                }
            }
        }
        return cart;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int pid = Integer.parseInt(request.getParameter("pid"));
        String comment = request.getParameter("comment");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");
        if (user == null) {
            session.setAttribute("requireLogin", "Vui lòng đăng nhập trước khi bình luận");
            response.sendRedirect("product-detail?id=" + pid);
        } else {
            Reviews r = new Reviews();
            r.setComment(comment);
            Product p = new Product();
            p.setId(pid);
            r.setProduct(p);
            r.setUser(user);
            Date reviewDate = new Date();
            r.setReviewDate(reviewDate);

            ReviewDAO reviewDao = new ReviewDAO();
            reviewDao.addComment(r);
            response.sendRedirect("product-detail?id=" + pid);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
