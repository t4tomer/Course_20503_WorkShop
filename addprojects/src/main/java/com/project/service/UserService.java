package com.project.service;

import org.springframework.stereotype.Service;
import com.project.model.User;

@Service
public interface UserService {
    User addNewUser(User newUser);

    User getUserByEmail(String productCode);

    void removeUserByEmail(String email);

    public long getUserCount();

    public String getUserFirstNameByEmail(String emailClient);
}
