package com.springboot.app.accounts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springboot.app.accounts.dto.responce.UserStatResponse;
import com.springboot.app.accounts.enumeration.AccountStatus;
import com.springboot.app.accounts.enumeration.AuthProvider;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Setter
@Getter
@NoArgsConstructor
public class User extends BaseEntity {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @OrderBy("name ASC")
    Set<Role> roles = new HashSet<>();
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @JsonIgnore
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    @Lob
    private String avatar;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "FK_USER_PERS"))
    private Person person;
    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", length = 50)
    private AccountStatus accountStatus;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_stat_id", foreignKey = @ForeignKey(name = "FK_USER_USER_STAT"))
    private UserStat stat;
    @Column(name = "provider_id", length = 100)
    private String providerId;
    @Column(name = "provider_name", length = 100)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "email_verified")
    private Boolean emailVerified = false;

    public User(String username, String email, String password, AuthProvider provider) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.accountStatus = AccountStatus.ACTIVE;
    }

    public static UserStatResponse toUserStatResponse(User user) {
        UserStatResponse userStatResponse = new UserStatResponse();
        userStatResponse.setUserId(user.getId());
        userStatResponse.setJoinDate(user.getCreatedAt());
        userStatResponse.setUsername(user.getUsername());
        userStatResponse.setEmail(user.getEmail());
        userStatResponse.setName(user.getName());

        Set<String> rolesString = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        userStatResponse.setRoles(rolesString);
        userStatResponse.setAccountStatus(user.getAccountStatus());
        userStatResponse.setImageUrl(user.getImageUrl());
        userStatResponse.setAvatar(user.getAvatar());
        userStatResponse.setUserStat(user.getStat());

        return userStatResponse;
    }
}
