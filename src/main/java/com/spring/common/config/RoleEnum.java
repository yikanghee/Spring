package com.spring.common.config;

public enum RoleEnum {
    USER(Authority.USER),
    ADMIN(Authority.ADMIN),
    ;

    private final String authority;

    RoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
    }

}
