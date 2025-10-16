package com.qr_meal_web.repository.impl;

import com.qr_meal_web.enums.BankAccountStatus;
import com.qr_meal_web.enums.CategoryStatus;
import com.qr_meal_web.model.BankAccount;
import com.qr_meal_web.model.Category;
import com.qr_meal_web.repository.BankAccountRepository;
import com.qr_meal_web.util.DBConnection;

import java.sql.*;

public class BankAccountRepositoryImpl implements BankAccountRepository {
    private final Connection connection;
    private static final String SELECT_BANK_ACCOUNT = "SELECT * FROM bank_account WHERE status = 1";

    public BankAccountRepositoryImpl() {
        this.connection = DBConnection.getConnection();
    }

    @Override
    public BankAccount selectBankAccount() {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BANK_ACCOUNT)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapResultSetToBankAccount(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private BankAccount mapResultSetToBankAccount(ResultSet rs) throws SQLException {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(rs.getInt("id"));
        bankAccount.setBank_code(rs.getInt("bank_code"));
        bankAccount.setBank_name(rs.getString("bank_name"));
        bankAccount.setAccount_name(rs.getString("account_name"));
        bankAccount.setAccount_number(rs.getString("account_number"));
        bankAccount.setStatus(BankAccountStatus.fromCode(rs.getInt("status")));
        Timestamp createdAt = rs.getTimestamp("created_at");
        Timestamp updatedAt = rs.getTimestamp("updated_at");

        if (createdAt != null) bankAccount.setCreated_at(createdAt);
        if (updatedAt != null) bankAccount.setUpdated_at(updatedAt);

        return bankAccount;
    }
}
