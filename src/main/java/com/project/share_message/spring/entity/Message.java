package com.project.share_message.spring.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="messages")

public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String text;
    private String tag;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    private String picture;
    @ManyToMany
    @JoinTable(name = "users_likes",
    joinColumns = @JoinColumn (name="message_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
            @Builder.Default
    Set<User>likes = new HashSet<>();
}
