package com.project.share_message.spring.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(exclude ={"subscriptions", "subscribers"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@ToString(exclude = {"subscriptions", "subscribers"})

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private boolean active;
    @Column(name = "activation_code")
    private String activationCode;
    private String email;
    private String avatar;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @Builder.Default
    Set<Message> messages = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Builder.Default
    Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id"))
    @Builder.Default
    Set<User> subscriptions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id"))
    @Builder.Default
    Set<User> subscribers = new HashSet<>();
@ManyToMany
    @JoinTable(name = "users_likes",
            inverseJoinColumns = @JoinColumn (name="message_id"),
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    Set<Message>likes = new HashSet<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role r : roles) {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

}
