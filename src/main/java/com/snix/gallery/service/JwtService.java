package com.snix.gallery.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    public String getToken(String key, Object value);

    public Claims getClaims(String token);

    boolean isValid(String token);

    int getId(String token);
}
