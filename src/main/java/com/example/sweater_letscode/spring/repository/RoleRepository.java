package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

   List<Role> findAllByNameIn(List<String>names);
}
