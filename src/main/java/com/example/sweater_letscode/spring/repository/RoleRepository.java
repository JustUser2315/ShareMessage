package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

   List<Role> findAllByNameIn(List<String>names);
   Optional<Role> findRoleByName(String roleName);
//   @Query(nativeQuery = true, value = "select * from roles where id = (select role_id from users_roles where user_id = :userId)")
//   Optional<Role> findRoleByUserId(Long userId);
   @Query(nativeQuery = true,
   value = "delete from users_roles where user_id = :userId and role_id = :roleId")
   @Modifying
   void clearUserRoles(Long userId, Long roleId);
}
