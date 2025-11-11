package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.TableStatus;
import com.qr_meal_web.model.Table;
import com.qr_meal_web.repository.TableRepository;
import com.qr_meal_web.util.DBConnection;
import com.qr_meal_web.util.QRCode;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableRepositoryImpl implements TableRepository {
    private static final String SELECT_LIST_TABLE = "SELECT * FROM `table` WHERE status <> 0";
    private static final String SELECT_ALL_TABLE = "SELECT * FROM `table` LIMIT ? OFFSET ?";
    private static final String SELECT_ONE = "SELECT * FROM `table` WHERE id = ?";
    private static final String FILTER_TABLE = "SELECT * FROM `table` WHERE 1=1";
    private static final String INSERT_TABLE = "INSERT INTO `table` (name) values (?)";
    private static final String UPDATE_TABLE = "UPDATE `table` SET qr_code = ?, name = ? WHERE id = ?";
    private static final String UPDATE_TABLE_STATUS = "UPDATE `table` SET status = ? WHERE id = ?";
    private static final String UPDATE_TABLE_POSITION = "UPDATE `table` SET pos_x = ?, pos_y = ?, width = ?, height = ? WHERE id = ?";
    private static final String UPDATE_QR_CODE = "UPDATE `table` SET qr_code = ? WHERE id = ?";
    private static final String CHECK_CAN_DELETE = "SELECT count(*) AS result FROM `order` WHERE table_id = ?";
    private static final String DELETE_TABLE = "DELETE FROM `table` WHERE id = ?";
    private static final String SET_INACTIVE = "UPDATE `table` SET status = 0 WHERE id = ?";

    @Override
    public List<Table> selectListTable() {
        List<Table> tables = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_LIST_TABLE)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tables.add(mapToTable(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tables;
    }

    @Override
    public List<Table> selectAllTable(int limit, int offset) {
        List<Table> tables = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_TABLE)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tables.add(mapToTable(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tables;
    }

    @Override
    public Table selectOne(int id) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ONE)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapToTable(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<Table> filtersTable(String filterString, List<Object> params) {
        List<Table> tables = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FILTER_TABLE + filterString)) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                tables.add(mapToTable(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tables;
    }

    @Override
    public boolean insertTable(String name) {
        Connection connection = DBConnection.getConnection();
        try {
            connection.setAutoCommit(false);

            PreparedStatement statementInsert =
                    connection.prepareStatement(INSERT_TABLE, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement statementUpdate = connection.prepareStatement(UPDATE_QR_CODE);

            statementInsert.setString(1, name);
            int resultInsert = statementInsert.executeUpdate();

            ResultSet rs = statementInsert.getGeneratedKeys();
            int table_id = 0;
            if (rs.next())
                table_id = rs.getInt(1);

            if (resultInsert != 1) return false;

            String qr_code = QRCode.generateBase64QRCode(table_id);
            statementUpdate.setString(1, qr_code);
            statementUpdate.setInt(2, table_id);
            statementUpdate.executeUpdate();

            connection.commit();
            return true;
        } catch (SQLException | IOException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean updateTablePositions(List<Table> tables) {
        Connection connection = DBConnection.getConnection();
        try {
            connection.setAutoCommit(false);

            for (Table item : tables) {
                PreparedStatement statement = connection.prepareStatement(UPDATE_TABLE_POSITION);
                statement.setInt(1, item.getPos_x());
                statement.setInt(2, item.getPos_y());
                statement.setInt(3, item.getWidth());
                statement.setInt(4, item.getHeight());
                statement.setInt(5, item.getId());
                statement.executeUpdate();
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean updateTable(int id, String qr_code, String name) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_TABLE)) {
            statement.setString(1, qr_code);
            statement.setString(2, name);
            statement.setInt(3, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateTableStatus(Connection connection, int id, int status) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TABLE_STATUS)) {
            statement.setInt(1, status);
            statement.setInt(2, id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean checkCanDelete(int id) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_CAN_DELETE)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int result = rs.getInt("result");
                return result == 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteTable(int id) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_TABLE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean setInactive(int id) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SET_INACTIVE)) {
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM `table`";
        try (Connection connection = DBConnection.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @Override
    public int countFilter(String filterString, List<Object> params) {
        String sql = "SELECT COUNT(*) FROM `table` WHERE 1=1" + filterString;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement st = connection.prepareStatement(sql);) {
            for (int i = 0; i < params.size(); i++) {
                st.setObject(i + 1, params.get(i));
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    private Table mapToTable(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String qr_code = rs.getString("qr_code");
        Timestamp created_at = rs.getTimestamp("created_at");
        Timestamp updated_at = rs.getTimestamp("updated_at");
        int status_code = rs.getInt("status");
        TableStatus status = TableStatus.fromCode(status_code);
        int pos_x = rs.getInt("pos_x");
        int pos_y = rs.getInt("pos_y");
        int width = rs.getInt("width");
        int height = rs.getInt("height");
        String shape = rs.getString("shape");
        String area = rs.getString("area");
        return new Table(id, name, qr_code, created_at, updated_at, status, pos_x, pos_y, width, height, shape, area);
    }
}
