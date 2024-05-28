package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.Product;
import com.project.model.User;
import com.project.repository.UserRepository;

// UserService
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo; // ---> Inject your ProductRepository

    @Override
    public User addNewUser(User newUser) {
        return userRepo.save(newUser);
    }

    @Override // this method must be aded in the ProductRepostory&ProductService
    public User getUserByEmail(String productCode) {
        return userRepo.findByEmail(productCode);
    }

    @Override
    public void removeUserByEmail(String email) {
        userRepo.removeByEmail(email);
    }

    @Override
    public long getUserCount() {
        return userRepo.count(); // Add this method
    }

}
