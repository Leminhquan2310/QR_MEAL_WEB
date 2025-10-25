package com.qr_meal_web.controller;

import com.google.gson.*;
import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.model.Order;
import com.qr_meal_web.model.OrderDetail;
import com.qr_meal_web.service.OrderDetailService;
import com.qr_meal_web.service.OrderService;
import com.qr_meal_web.service.impl.OrderDetailServiceImpl;
import com.qr_meal_web.service.impl.OrderServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(name = "APIOrderDetailServlet", urlPatterns = "/api/order-detail")
public class APIOrderDetailServlet extends HttpServlet {
    private final OrderService orderService = new OrderServiceImpl();
    private final OrderDetailService orderDetailService = new OrderDetailServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "by-table-id":
                getOrderDetailByTableId(req, resp);
                break;
        }
    }

    private void getOrderDetailByTableId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderService.selectOrderByTableIdAvailable(id);
        List<OrderDetail> details = orderDetailService.selectOrderDetailByTableId(id);

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("details", details);

        response.setContentType("application/json; charset=UTF-8");
        new GsonBuilder()
                .registerTypeAdapter(OrderStatus.class, new JsonSerializer<OrderStatus>() {
                    @Override
                    public JsonElement serialize(OrderStatus status, Type typeOfSrc, JsonSerializationContext context) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("code", status.getCode());
                        obj.addProperty("label", status.getLabel());
                        obj.addProperty("badge", status.getBadge());
                        return obj;
                    }
                })
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
                .toJson(result, response.getWriter());
    }
}
