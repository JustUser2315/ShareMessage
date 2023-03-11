package com.project.share_message.spring.repository;

import com.project.share_message.spring.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>, QuerydslPredicateExecutor<Message> {
    Optional<Message>findByTag(String tag);

    List<Message> findAllByAuthorId(Long id);
    @Query(nativeQuery = true, value = "select * from messages order by id desc limit 10")
    List<Message> findTop10();

    @Query(nativeQuery = true, value = "select count(user_id) from users_likes where user_id = :userId and message_id = :messageId")
    Long isLiked(Long userId, Integer messageId);
}
