package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.base.EntityBase;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "identity_oauth_authentication",
        uniqueConstraints={@UniqueConstraint(columnNames = {"oauth_server_name", "oauth_id"})})
public class OauthAuthentication extends EntityBase {

    public OauthAuthentication(Long userId, String serverName, String oauthId) {
        EntityValidationUtils.notEmpty("serverName", serverName);
        EntityValidationUtils.notEmpty("oauthId", oauthId);
        setId(userId);
        this.serverName = serverName;
        this.oauthId = oauthId;
    }

    @Column(name = "oauth_server_name", nullable = false)
    private String serverName;

    @Column(name = "oauth_id", nullable = false, length = 255)
    private String oauthId;

    @Column(name = "access_token", length = 255)
    private String accessToken;

    @Column(name = "expire")
    private Date expire;

}
