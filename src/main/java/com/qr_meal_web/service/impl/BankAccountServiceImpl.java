package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.BankAccount;
import com.qr_meal_web.repository.BankAccountRepository;
import com.qr_meal_web.repository.impl.BankAccountRepositoryImpl;
import com.qr_meal_web.service.BankAccountService;

public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository = new BankAccountRepositoryImpl();

    @Override
    public BankAccount getBankAccount() {
        return bankAccountRepository.selectBankAccount();
    }
}
