package com.oss.ossv1.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oss.ossv1.data.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    
    /**
     * Find all users who are administrators
     */
    List<User> findByIsAdminTrue();
}
