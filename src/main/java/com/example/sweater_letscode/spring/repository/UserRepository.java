package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByUsername(String username);
    @Query(nativeQuery = true, value = "update users set password = :newPassword where id=:id")
    @Modifying
    void updatePassword(Long id, String newPassword);

}
