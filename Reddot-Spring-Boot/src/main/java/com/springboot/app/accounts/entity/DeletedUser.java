package com.springboot.app.accounts.entity;

import com.springboot.app.accounts.enumeration.RoleName;
import com.springboot.app.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "deleted_users")
@Data
@NoArgsConstructor
public class DeletedUser extends BaseEntity {
    @Column(name = "deleted_at", columnDefinition = "DATETIME")
    private LocalDateTime deletedAt;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "password", length = 200, nullable = false)
    private String password;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 50, nullable = false)
    private RoleName role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "fk_del_user_person"))
    private Person person;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_stat_id", foreignKey = @ForeignKey(name = "fk_del_user_stat"))
    private UserStat stat;

    public static DeletedUser fromUser(User user) {
        DeletedUser deletedUser = new DeletedUser();
        deletedUser.setId(user.getId());
        deletedUser.setUsername(user.getUsername());
        deletedUser.setEmail(user.getEmail());
        deletedUser.setPassword(user.getPassword());
        deletedUser.setPerson(user.getPerson());
        deletedUser.setStat(user.getStat());
        deletedUser.setCreatedBy(user.getCreatedBy());
        deletedUser.setCreatedAt(user.getCreatedAt());
        deletedUser.setUpdatedBy(user.getUpdatedBy());
        deletedUser.setUpdatedAt(user.getUpdatedAt());
        Set<String> roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toSet());
        deletedUser.setRole(RoleName.valueOf(roles.iterator().next()));
        LocalDateTime now = LocalDateTime.now();
        deletedUser.setDeletedAt(now);
        return deletedUser;
    }

}
