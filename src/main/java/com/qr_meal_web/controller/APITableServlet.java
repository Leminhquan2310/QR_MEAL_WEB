package com.qr_meal_web.controller;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.qr_meal_web.enums.OrderStatus;
import com.qr_meal_web.enums.TableStatus;
import com.qr_meal_web.model.Table;
import com.qr_meal_web.service.TableService;
import com.qr_meal_web.service.impl.TableServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet(name = "APITableServlet", urlPatterns = "/api/table")
public class APITableServlet extends HttpServlet {
    private final TableService tableService = new TableServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Table> tables = tableService.getListTable();
        resp.setContentType("application/json; charset=UTF-8");
        String json = new com.google.gson.GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(TableStatus.class, new JsonSerializer<TableStatus>() {
                    @Override
                    public JsonElement serialize(TableStatus status, Type typeOfSrc, JsonSerializationContext context) {
                        JsonObject obj = new JsonObject();
                        obj.addProperty("code", status.getCode());
                        obj.addProperty("label", status.getLabel());
                        obj.addProperty("badge", status.getBadge());
                        return obj;
                    }
                })
                .create()
                .toJson(tables);
        resp.getWriter().print(json);
    }
}
