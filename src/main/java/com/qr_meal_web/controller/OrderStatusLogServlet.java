package com.qr_meal_web.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.model.OrderStatusLog;
import com.qr_meal_web.service.OrderStatusLogService;
import com.qr_meal_web.service.impl.OrderStatusLogServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet("/order/status-logs")
public class OrderStatusLogServlet extends HttpServlet {
    private final OrderStatusLogService orderStatusLogService = new OrderStatusLogServiceImpl();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "orderId required");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdStr);
            List<OrderStatusLog> logs = orderStatusLogService.findByOrderId(orderId);
            // Convert to JSON (tùy lib: Gson / Jackson). Ví dụ dùng Gson:
            resp.setContentType("application/json;charset=UTF-8");
            String json = new com.google.gson.GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
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
                    .create()
                    .toJson(logs);
            resp.getWriter().print(json);
        } catch (Exception e) {
            resp.sendError(500, e.getMessage());
        }
    }
}
