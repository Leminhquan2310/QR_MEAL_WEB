package com.qr_meal_web.model;

import com.qr_meal_web.enums.BankAccountStatus;

import java.sql.Timestamp;

public class BankAccount {
    private int id;
    private int bank_code;
    private String bank_name;
    private String account_name;
    private String account_number;
    private BankAccountStatus status;
    private Timestamp created_at;
    private Timestamp updated_at;

    public BankAccount() {
    }

    public BankAccount(int id, int bank_code, String bank_name, String account_name, String account_number, BankAccountStatus status, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.bank_code = bank_code;
        this.bank_name = bank_name;
        this.account_name = account_name;
        this.account_number = account_number;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBank_code() {
        return bank_code;
    }

    public void setBank_code(int bank_code) {
        this.bank_code = bank_code;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public BankAccountStatus getStatus() {
        return status;
    }

    public void setStatus(BankAccountStatus status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
