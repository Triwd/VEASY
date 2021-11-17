package org.veasy.utils;

import org.veasy.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;


public class Util {
    public static User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
