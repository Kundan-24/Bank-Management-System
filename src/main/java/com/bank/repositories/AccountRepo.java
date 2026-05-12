package com.bank.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bank.entities.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long>{

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a WHERE " + "a.accountNumber LIKE %:keyword% OR " + "LOWER(a.user.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Account> searchAccounts(@Param("keyword") String keyword);

    long countByActiveTrue();

    long countByActiveFalse();

    @Query("SELECT SUM(a.balance) FROM Account a")
    Double getTotalBalance();
}
