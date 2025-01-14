package com.springboot.app.accounts.entity;

import com.springboot.app.accounts.enumeration.RoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Setter
@Getter
@Entity
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @NaturalId
    @NonNull
    private RoleName name;

    public Role(@NonNull RoleName name) {
        this.name = name;
    }
}