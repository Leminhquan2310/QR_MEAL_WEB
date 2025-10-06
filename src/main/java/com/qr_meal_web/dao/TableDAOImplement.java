package com.qr_meal_web.dao;

import com.qr_meal_web.enums.TableStatus;
import com.qr_meal_web.model.Table;
import com.qr_meal_web.util.DBConnection;
import com.qr_meal_web.util.QRCode;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAOImplement implements ITableDAO {
    private static final String SELECT_ALL_TABLE = "SELECT * FROM `table`";
    private static final String INSERT_TABLE = "INSERT INTO `table` (name) values (?)";
    private static final String UPDATE_TABLE = "UPDATE `table` SET qr_code = ?, name = ? WHERE id = ?";
    private static final String UPDATE_QR_CODE = "UPDATE `table` SET qr_code = ? WHERE id = ?";
    private static final String CHECK_CAN_DELETE = "SELECT count(*) AS result FROM `order` WHERE table_id = ?";
    private static final String DELETE_TABLE = "DELETE FROM `table` WHERE id = ?";
    private static final String SET_INACTIVE = "UPDATE `table` SET status = 0 WHERE id = ?";

    private static PreparedStatement getStatement(String sql) throws SQLException {
        Connection connection = DBConnection.getConnection();
        return connection.prepareStatement(sql);
    }

    @Override
    public List<Table> selectAllTable() {
        List<Table> tables = new ArrayList<>();
        try (PreparedStatement statement = getStatement(SELECT_ALL_TABLE)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String qr_code = rs.getString("qr_code");
                Timestamp created_at = rs.getTimestamp("created_at");
                Timestamp updated_at = rs.getTimestamp("updated_at");
                int status_code = rs.getInt("status");
                TableStatus status = TableStatus.fromCode(status_code);
                tables.add(new Table(id, name, qr_code, created_at, updated_at, status));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    @Override
    public List<Table> filtersTable(String createdFrom, String createdTo) {
        List<Table> tables = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM `table` WHERE 1=1");
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

        try (PreparedStatement statement = getStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String qr_code = rs.getString("qr_code");
                Timestamp created_at = rs.getTimestamp("created_at");
                Timestamp updated_at = rs.getTimestamp("updated_at");
                int status_code = rs.getInt("status");
                TableStatus status = TableStatus.fromCode(status_code);
                tables.add(new Table(id, name, qr_code, created_at, updated_at, status));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    @Override
    public boolean insertTable(String name) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statementInsert = connection.prepareStatement(INSERT_TABLE, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement statementUpdate = getStatement(UPDATE_QR_CODE)) {
            statementInsert.setString(1, name);
            int resultInsert = statementInsert.executeUpdate();

            ResultSet rs = statementInsert.getGeneratedKeys();
            int table_id = 0;
            if (rs.next())
                table_id = rs.getInt(1);

            if (resultInsert == 1) {
                String qr_code = QRCode.generateBase64QRCode(table_id);
                statementUpdate.setString(1, qr_code);
                statementUpdate.setInt(2, table_id);

                int result = statementUpdate.executeUpdate();
                return result > 0;
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean updateTable(int id, String qr_code, String name) {
        try (PreparedStatement statement = getStatement(UPDATE_TABLE)) {
            statement.setString(1, qr_code);
            statement.setString(2, name);
            statement.setInt(3, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkCanDelete(int id) {
        try (PreparedStatement statement = getStatement(CHECK_CAN_DELETE)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int result = rs.getInt("result");
                return result == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean deleteTable(int id) {
        try (PreparedStatement statement = getStatement(DELETE_TABLE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean setInactive(int id) {
        try (PreparedStatement statement = getStatement(SET_INACTIVE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
