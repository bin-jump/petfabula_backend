package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.base.EntityBase;
import lombok.Builder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "identity_role")
public class UserRole extends EntityBase {

    public UserRole(RoleName name) {
        this.name = name.name();
    }

    public UserRole() {}

    @Column(name = "name", unique = true, nullable = false, length = 16)
    private String name;


    @ManyToMany(mappedBy = "roles", cascade = CascadeType.PERSIST)
    @Builder.Default
    private Set<UserAccount> users = new HashSet<>();


    public enum RoleName {
        ADMIN;

        public static List<String> toNames() {
            return Stream.of(RoleName.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
        }
    }
}
