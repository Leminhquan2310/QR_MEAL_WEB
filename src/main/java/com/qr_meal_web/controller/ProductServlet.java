package com.qr_meal_web.controller;

import com.qr_meal_web.dao.CategoryDAOImplement;
import com.qr_meal_web.dao.IProductDAO;
import com.qr_meal_web.dao.ProductDAOImplement;
import com.qr_meal_web.enums.ProductStatus;
import com.qr_meal_web.model.Category;
import com.qr_meal_web.model.Product;
import com.qr_meal_web.util.FileUtil;
import com.qr_meal_web.util.Helper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB
)
@WebServlet(name = "ProductServlet", urlPatterns = "/product")
public class ProductServlet extends HttpServlet {
    private IProductDAO productDAO = new ProductDAOImplement();
    private List<Category> categories = new CategoryDAOImplement().selectAllCategory();
    private List<ProductStatus> statuses = Arrays.asList(ProductStatus.values());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("pageActive", "product");
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                showCreateProduct(request, response);
                break;
            case "update":
                showUpdateProduct(request, response);
                break;
            case "filters":
                showFilterProduct(request, response);
                break;
            default:
                showAllProduct(request, response);
                break;
        }
        request.getRequestDispatcher("WEB-INF/views/manage/layout/layout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "create":
                handleCreateProduct(request, response);
                break;
            case "update":
                handleUpdateProduct(request, response);
                break;
            case "delete":
                handleDeleteProduct(request, response);
                break;
        }
        response.sendRedirect(request.getContextPath() + "/product");
    }

    private void showAllProduct(HttpServletRequest request, HttpServletResponse response) {
        List<Product> products = productDAO.selectAllProduct();
        request.setAttribute("pageTitle", "Quản lý sản phẩm");
        request.setAttribute("pageContent", "../product/list.jsp");
        request.setAttribute("pageJs", "/resources/js/product.js");
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("statuses", statuses);
    }

    private void showCreateProduct(HttpServletRequest request, HttpServletResponse response) {
        List<ProductStatus> statuses = Arrays.asList(ProductStatus.values());
        List<Category> categories = new CategoryDAOImplement().selectAllCategory();
        request.setAttribute("pageTitle", "Thêm mới sản phẩm");
        request.setAttribute("pageContent", "../product/create.jsp");
        request.setAttribute("pageJs", "/resources/js/product.js");
        request.setAttribute("statuses", statuses);
        request.setAttribute("categories", categories);
    }

    private void showUpdateProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        List<ProductStatus> statuses = Arrays.asList(ProductStatus.values());
        List<Category> categories = new CategoryDAOImplement().selectAllCategory();
        Product product = productDAO.selectById(id);
        request.setAttribute("pageTitle", "Chỉnh sửa sản phẩm");
        request.setAttribute("pageContent", "../product/update.jsp");
//        request.setAttribute("pageCss", "/resources/css/product.css");
        request.setAttribute("pageJs", "/resources/js/product.js");
        request.setAttribute("statuses", statuses);
        request.setAttribute("categories", categories);
        request.setAttribute("product", product);
    }

    private void showFilterProduct(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("keyword");
        double minPrice = Helper.parseDoubleSafe(request.getParameter("minPrice"), 0);
        double maxPrice = Helper.parseDoubleSafe(request.getParameter("maxPrice"), 0);
        int category = Integer.parseInt(request.getParameter("category"));
        int status = Integer.parseInt(request.getParameter("status"));
        Map<String, Object> filters = new HashMap<>();
        filters.put("keyword", keyword);
        filters.put("minPrice", minPrice);
        filters.put("maxPrice", maxPrice);
        filters.put("category", category);
        filters.put("status", status);

        List<Product> products = productDAO.filterProduct(keyword, minPrice, maxPrice, category, status);
        request.setAttribute("pageTitle", "Quản lý sản phẩm");
        request.setAttribute("pageContent", "../product/list.jsp");
        request.setAttribute("pageJs", "/resources/js/product.js");
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("statuses", statuses);
        request.setAttribute("filters", filters);
    }

    // ----------- do post --------------
    private void handleCreateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        int statusCode = Integer.parseInt(request.getParameter("status"));
        ProductStatus status = ProductStatus.fromCode(statusCode);
        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        Category category = new Category();
        category.setId(categoryId);
        int cookingTime = Integer.parseInt(request.getParameter("cooking_time"));

        // xử l file image
        Part filePart = request.getPart("image");
        String imageBase64 = FileUtil.toBase64(filePart);

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStatus(status);
        product.setCategory(category);
        product.setImage(imageBase64);
        product.setCooking_time(cookingTime);

        boolean isSuccess = productDAO.insertProduct(product);
        HttpSession session = request.getSession();
        if (isSuccess) {
            session.setAttribute("message", "Thêm mới thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Thêm mới thất bại!");
            session.setAttribute("status", "error");
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        String description = request.getParameter("description");
        int statusCode = Integer.parseInt(request.getParameter("status"));
        ProductStatus status = ProductStatus.fromCode(statusCode);
        int categoryId = Integer.parseInt(request.getParameter("category_id"));
        Category category = new Category();
        category.setId(categoryId);
        int cookingTime = Integer.parseInt(request.getParameter("cooking_time"));

        // xử l file image
        Part filePart = request.getPart("image");
        String imageBase64 = FileUtil.toBase64(filePart);

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setStatus(status);
        product.setCategory(category);
        product.setImage(imageBase64);
        product.setCooking_time(cookingTime);

        boolean isSuccess = productDAO.updateProduct(product);
        HttpSession session = request.getSession();
        if (isSuccess) {
            session.setAttribute("message", "Chỉnh sửa sản phẩm thành công!");
            session.setAttribute("status", "success");
        } else {
            session.setAttribute("message", "Chỉnh sửa sản phẩm thất bại!");
            session.setAttribute("status", "error");
        }
    }

    private void handleDeleteProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean canDelete = productDAO.checkProductDeletable(id);

        HttpSession session = request.getSession();
        if (canDelete) {
            boolean isSuccess = productDAO.deleteProduct(id);
            if (isSuccess) {
                session.setAttribute("message", "Xóa sản phẩm thành công!");
                session.setAttribute("status", "success");
            } else {
                session.setAttribute("message", "Xóa sản phẩm thất bại!");
                session.setAttribute("status", "error");
            }
        } else {
            session.setAttribute("message", "Sản phẩm đã có đơn hàng - Không thể xóa!");
            session.setAttribute("status", "error");
        }
    }
}
