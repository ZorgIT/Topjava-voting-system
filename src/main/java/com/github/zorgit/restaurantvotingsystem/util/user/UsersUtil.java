package com.github.zorgit.restaurantvotingsystem.util.user;

import com.github.zorgit.restaurantvotingsystem.model.Role;
import com.github.zorgit.restaurantvotingsystem.dto.AdminUserTo;
import lombok.experimental.UtilityClass;
import com.github.zorgit.restaurantvotingsystem.model.User;
import com.github.zorgit.restaurantvotingsystem.dto.UserTo;

@UtilityClass
public class UsersUtil {
    public static User createNewFromTo(UserTo userTo) {
        return new User(null,
                userTo.getName(),
                userTo.getEmail().toLowerCase(),
                userTo.getPassword(),
                Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword());
    }

    public static AdminUserTo asAdminUserTo(User user) {
        return new AdminUserTo(user.getId(), user.getName(), user.getEmail(),
                user.getPassword(), user.isEnabled(), user.getRoles());
    }

}