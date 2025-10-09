package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Table;
import com.qr_meal_web.repository.TableRepository;
import com.qr_meal_web.repository.impl.TableRepositoryImpl;
import com.qr_meal_web.service.TableService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository = new TableRepositoryImpl();

    @Override
    public List<Table> selectAllTable() {
        return tableRepository.selectAllTable();
    }

    @Override
    public List<Table> filtersTable(String createdFrom, String createdTo) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        // Filter theo khoảng ngày
        if (createdFrom != null && !createdFrom.isEmpty() && createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        } else if (createdFrom != null && !createdFrom.isEmpty()) {
            sql.append(" AND created_at >= ?");
            params.add(Timestamp.valueOf(createdFrom + " 00:00:00"));
        } else if (createdTo != null && !createdTo.isEmpty()) {
            sql.append(" AND created_at <= ?");
            params.add(Timestamp.valueOf(createdTo + " 23:59:59"));
        }

        return tableRepository.filtersTable(sql.toString(), params);
    }

    @Override
    public boolean insertTable(String name) {
        return tableRepository.insertTable(name);
    }

    @Override
    public boolean updateTable(int id, String qr_code, String name) {
        return tableRepository.updateTable(id, qr_code, name);
    }

    @Override
    public boolean checkCanDelete(int id) {
        return tableRepository.checkCanDelete(id);
    }

    @Override
    public boolean deleteTable(int id) {
        return tableRepository.deleteTable(id);
    }

    @Override
    public boolean setInactive(int id) {
        return tableRepository.setInactive(id);
    }
}
