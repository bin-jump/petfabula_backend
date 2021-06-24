package com.petfabula.domain.aggregate.identity.service.oauth;

import com.petfabula.domain.common.validation.EntityValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OauthService {

    @Autowired
    private OauthServer oauthServer;

    public OauthResponse doOauth(String serverName, String code) {
        EntityValidationUtils.notEmpty("oauthServerName", serverName);
        EntityValidationUtils.notEmpty("oauthCode", code);
        serverName = serverName.toUpperCase();
        checkServerName(serverName);
        OauthServerName oauthServerName = OauthServerName.valueOf(serverName);

        OauthRequest request = new OauthRequest(oauthServerName, code);
        OauthResponse response = oauthServer.doOauth(request);
        response.setUserName(formatUserName(response.getUserName()));

        return response;
    }

    private String formatUserName(String name) {
        return name.trim()
                .replace(" ", "-")
                .replace("　", "-");
    }

    private void checkServerName(String test) {
        for (OauthServerName c : OauthServerName.values()) {
            if (c.name().equals(test)) {
                return;

            }
        }
        throw new RuntimeException("Not a valid oauth server name: " + test);

    }

}
