package com.qr_meal_web.service.impl;

import com.qr_meal_web.model.Role;
import com.qr_meal_web.repository.RoleRepository;
import com.qr_meal_web.repository.impl.RoleRepositoryImpl;
import com.qr_meal_web.service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository = new RoleRepositoryImpl();

    @Override
    public List<Role> selectAllRole() {
        return roleRepository.selectAllRole();
    }
}
