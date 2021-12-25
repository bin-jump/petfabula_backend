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
@Table(name = "identity_third_party_authentication",
        uniqueConstraints={@UniqueConstraint(columnNames = {"third_party_server_name", "third_party_id"})})
public class ThirdPartyAuthentication extends EntityBase {

    public ThirdPartyAuthentication(Long userId, String serverName, String thirdPartyId) {
        EntityValidationUtils.notEmpty("serverName", serverName);
        EntityValidationUtils.notEmpty("thirdPartyId", thirdPartyId);
        validateServerName(serverName);
        setId(userId);
        this.serverName = serverName;
        this.thirdPartyId = thirdPartyId;
    }

    @Column(name = "third_party_server_name", nullable = false, length = 16)
    private String serverName;

    @Column(name = "third_party_id", nullable = false, length = 255)
    private String thirdPartyId;

    @Column(name = "access_token", length = 128)
    private String accessToken;

    @Column(name = "expire")
    private Instant expire;

    @Column(name = "extra", length = 128)
    private String extra;

    private void validateServerName(String serverName) {
        for (OauthServerName c : OauthServerName.values()) {
            if (c.name().equals(serverName)) {
                return;
            }
        }
        throw new InvalidValueException(serverName, "wrong server name");
    }

}
