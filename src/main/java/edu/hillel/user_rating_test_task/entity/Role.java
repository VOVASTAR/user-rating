package edu.hillel.user_rating_test_task.entity;

import org.springframework.security.core.GrantedAuthority;


public enum Role implements GrantedAuthority {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}

