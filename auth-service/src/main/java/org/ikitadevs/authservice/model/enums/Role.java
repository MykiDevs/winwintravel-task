package org.ikitadevs.authservice.model.enums;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
