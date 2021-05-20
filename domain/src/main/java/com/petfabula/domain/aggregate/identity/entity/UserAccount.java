package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.base.ConcurrentEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "account",
        indexes = {@Index(name = "name_index",  columnList="name", unique = true)})
public class UserAccount extends ConcurrentEntity {

    public UserAccount(Long id, String name, RegisterEntry registerEntry) {
        setId(id);
        setName(name);
        this.status = UserStatus.REGISTED;
        this.registerEntry = registerEntry;
        gender = Gender.UNSET;
    }

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @Column(name = "email", unique = true, length = 128)
    private String email;

    @Column(name = "avatar_url", unique = true)
    private String avatarUrl;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "about", length = 255)
    private String about;

    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "register_entry", nullable = false)
    private RegisterEntry registerEntry;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserRole> roles = new HashSet<>();

    private void setName(String name) {
        EntityValidationUtils.validUserName("name", name);
        this.name = name;
    }

    public enum Gender {
        MALE, FEMALE, UNSET
    }

    public enum UserStatus {
        REGISTED, VERIFIED, ABNORMAL, TEMP_BANNED, BANNED
    }

    public enum RegisterEntry {
        EMAIL_PASSWORD, EMAIL_CODE, OAUTH, PHONE
    }
}
