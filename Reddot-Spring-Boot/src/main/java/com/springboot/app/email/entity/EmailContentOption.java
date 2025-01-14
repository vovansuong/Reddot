package com.springboot.app.email.entity;

import com.springboot.app.model.BaseEntity;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "email_content_options")
public class EmailContentOption extends BaseEntity {
    @Column(unique = true, nullable = false)
    private EmailOptionType optionType;

    @Column(length = 200)
    private String emailSubject;
    @Lob
    @Basic
    @Column(columnDefinition = "LONGTEXT")
    private String emailBodyTemplate;

}
