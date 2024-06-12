package com.project.repository;

import com.project.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    // Method to find a user by email
    User findByEmail(String email);
    // Method to remove user by email
    void removeByEmail(String email);

    // Method that cheacks if user in db
    boolean existsByEmail(String email);
}
