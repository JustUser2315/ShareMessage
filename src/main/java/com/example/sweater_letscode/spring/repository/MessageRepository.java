package com.example.sweater_letscode.spring.repository;

import com.example.sweater_letscode.spring.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>, QuerydslPredicateExecutor<Message> {
    Optional<Message>findByTag(String tag);

    List<Message> findAllByAuthorId(Long id);

}
