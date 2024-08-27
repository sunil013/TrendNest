package com.sunil.TrendNest.Initialization;

import com.sunil.TrendNest.Constants.UserRolesData;
import com.sunil.TrendNest.model.UserRoles;
import com.sunil.TrendNest.repo.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RolesDataLoader implements CommandLineRunner {

    @Autowired
    private UserRoleRepo userRoleRepo;

    @Override
    public void run(String... args) throws Exception {
        for (UserRolesData roleEnum : UserRolesData.values()) {
            if (userRoleRepo.findByName(String.valueOf(roleEnum)) == null) {
                UserRoles role = new UserRoles();
                role.setName(String.valueOf(roleEnum));
                userRoleRepo.save(role);
            }
        }
    }
}
