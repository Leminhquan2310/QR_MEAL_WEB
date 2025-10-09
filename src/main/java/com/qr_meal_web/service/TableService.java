package com.qr_meal_web.service;

import com.qr_meal_web.model.Table;

import java.util.List;

public interface TableService {
    List<Table> selectAllTable();

    List<Table> filtersTable(String createdFrom, String createdTo);

    boolean insertTable(String name);

    boolean updateTable(int id, String qr_code, String name);

    boolean checkCanDelete(int id);

    boolean deleteTable(int id);

    boolean setInactive(int id);
}
