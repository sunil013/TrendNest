package com.sunil.TrendNest.repo;

import com.sunil.TrendNest.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRoles,Long> {
    UserRoles findByName(String user);
}
