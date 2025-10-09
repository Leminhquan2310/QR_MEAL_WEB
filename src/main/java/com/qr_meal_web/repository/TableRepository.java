package com.qr_meal_web.repository;

import com.qr_meal_web.model.Table;

import java.util.List;

public interface TableRepository {
    List<Table> selectAllTable();

    List<Table> filtersTable(String filterString, List<Object> params);

    boolean insertTable(String name);

    boolean updateTable(int id, String qr_code, String name);

    boolean checkCanDelete(int id);

    boolean deleteTable(int id);

    boolean setInactive(int id);
}
