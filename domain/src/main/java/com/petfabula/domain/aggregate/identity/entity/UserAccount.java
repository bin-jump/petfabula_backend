package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.common.domain.ConcurrentEntity;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraph(name = "userAccount.all",
        attributeNodes = {@NamedAttributeNode("roles")}
)
@NoArgsConstructor
@Getter
@Entity
@Table(name = "account",
        indexes = {@Index(name = "name_index", columnList="name, delete_at", unique = true),
                @Index(name = "email_index", columnList="email", unique = true)})
public class UserAccount extends ConcurrentEntity {

    public UserAccount(Long id, String name, String email, RegisterEntry registerEntry) {
        setId(id);
        setName(name);
        this.email = email;
        this.status = UserStatus.REGISTED;
        this.registerEntry = registerEntry;
        gender = null;
        setBio("");
    }

    @Column(name = "name", unique = true, nullable = false, length = 32)
    private String name;

    @Column(name = "photo", unique = true)
    private String photo;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "city_id")
    private Long cityId;

    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "register_entry", nullable = false)
    private RegisterEntry registerEntry;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"), foreignKey = @ForeignKey(name = "none"))
    private Set<Role> roles = new HashSet<>();

    public void setBio(String bio) {
        EntityValidationUtils.validStringLength("bio", bio, 0, 140);
        if (bio == null) {
            bio = "";
        }
        this.bio = bio;
    }

    public void addRole(Role role) {
        roles.add(role);
    }


    public void setBirthday(LocalDate birthday) {
        if (birthday == null) {
            return;
        }
        EntityValidationUtils.validBirthday("birthday", birthday);
        this.birthday = birthday;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public void setName(String name) {
        EntityValidationUtils.validUserName("name", name);
        this.name = name;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum UserStatus {
        REGISTED, VERIFIED, ABNORMAL, TEMP_BANNED, PERMANENT_BANNED
    }

    public enum RegisterEntry {
        EMAIL_PASSWORD, EMAIL_CODE, OAUTH, PHONE, OTHER
    }
}
