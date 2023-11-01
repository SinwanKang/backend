package com.snix.gallery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder(String type) {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder(16384, 8, 1, 32, 64));
        encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
        encoders.put("sha256", new StandardPasswordEncoder());

        return new DelegatingPasswordEncoder(type, encoders);
    }

    public String bcryptEncoding(String password) {
        String encPassword = "";
        encPassword = passwordEncoder("bcrypt").encode(password);
        return encPassword;
    }

    public boolean bcryptMatching(String password, String encPassword) {
        //encPassword = "{sha256}"+encPassword;
        return passwordEncoder("bcrypt").matches(password, encPassword);
    }
}
