package com.springboot.app.forums.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "votes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote extends BaseEntity {
    @Column(name = "vote_name", length = 50)
    private String voterName; //username of the user who voted
    @Column(name = "vote_value")
    private short voteValue;
}
