package ru.javaops.topjava2.util;

import lombok.experimental.UtilityClass;
import ru.javaops.topjava2.model.Role;
import ru.javaops.topjava2.model.User;
import ru.javaops.topjava2.to.AdminUserTo;
import ru.javaops.topjava2.to.UserTo;

@UtilityClass
public class UsersUtil {


    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
    public static AdminUserTo asAdminUserTo(User user) {
        return new AdminUserTo(user.getId(), user.getName(), user.getEmail(),
                user.getPassword(),user.isEnabled(),user.getRoles());
    }


}