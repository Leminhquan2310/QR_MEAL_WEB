package com.qr_meal_web.service;

import com.qr_meal_web.model.Table;

import java.util.List;

public interface TableService {
    List<Table> selectAllTable(int limit, int page);

    List<Table> filtersTable(String createdFrom, String createdTo, int limit, int page);

    boolean insertTable(String name);

    boolean updateTable(int id, String qr_code, String name);

    boolean checkCanDelete(int id);

    boolean deleteTable(int id);

    boolean setInactive(int id);

    int getCountTotal();

    int getCountTotalTableFilter(String createdFrom, String createdTo);
}
