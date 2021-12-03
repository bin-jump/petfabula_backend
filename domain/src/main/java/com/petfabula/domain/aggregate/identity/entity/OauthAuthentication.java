package com.petfabula.domain.aggregate.identity.entity;

import com.petfabula.domain.aggregate.identity.service.oauth.OauthServerName;
import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import com.petfabula.domain.common.validation.MessageKey;
import com.petfabula.domain.exception.InvalidValueException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
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
        validateServerName(serverName);
        setId(userId);
        this.serverName = serverName;
        this.oauthId = oauthId;
    }

    @Column(name = "oauth_server_name", nullable = false, length = 16)
    private String serverName;

    @Column(name = "oauth_id", nullable = false, length = 255)
    private String oauthId;

    @Column(name = "access_token", length = 128)
    private String accessToken;

    @Column(name = "expire")
    private Instant expire;

    private void validateServerName(String serverName) {
        for (OauthServerName c : OauthServerName.values()) {
            if (c.name().equals(serverName)) {
                return;
            }
        }
        throw new InvalidValueException(serverName, "wrong server name");
    }

}
