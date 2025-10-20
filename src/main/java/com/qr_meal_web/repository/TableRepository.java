package com.qr_meal_web.repository;

import com.qr_meal_web.model.Table;

import java.util.List;

public interface TableRepository {
    List<Table> selectListTable();

    List<Table> selectAllTable(int limit, int offset);

    List<Table> filtersTable(String filterString, List<Object> params);

    boolean insertTable(String name);

    boolean updateTablePositions(List<Table> tables);

    boolean updateTable(int id, String qr_code, String name);

    boolean updateTableStatus(int id, int status);

    boolean checkCanDelete(int id);

    boolean deleteTable(int id);

    boolean setInactive(int id);

    int countAll();

    int countFilter(String filterString, List<Object> params);
}
