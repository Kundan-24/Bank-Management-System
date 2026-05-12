package com.bank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.entities.User;


@Repository
public interface UserRepo extends JpaRepository<User,Long>{
     
    Optional<User> findByEmail(String email);
    Optional<User> findByAadhaarNumber(String aadhaarNumber);
    
    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE " + "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " + "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " + "u.phoneNumber LIKE CONCAT('%', :keyword, '%')")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    long countByEnabledFalse();
}
