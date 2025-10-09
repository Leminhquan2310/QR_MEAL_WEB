package com.qr_meal_web.repository;

import com.qr_meal_web.model.Role;

import java.util.List;

public interface RoleRepository {
    List<Role> selectAllRole();
}
