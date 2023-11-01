package com.snix.gallery.controller;


import com.snix.gallery.config.AppConfig;
import com.snix.gallery.entity.Member;
import com.snix.gallery.repository.MemberRepository;
import com.snix.gallery.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class AccountController {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AppConfig appConfig;

    @Autowired
    JwtService jwtService;

    @PostMapping("/api/account/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> params, HttpServletResponse res) {

        String pwd;
        pwd = params.get("password");
        String encodedPwdNew = appConfig.bcryptEncoding(pwd);
        System.out.println("encodedPwdNew : " + encodedPwdNew);

        Member member = memberRepository.findByEmail(params.get("email"));
        String encodedPwd = member.getPassword();
        boolean loginProc = appConfig.bcryptMatching(pwd, encodedPwd);

        if (loginProc) {

            int id = member.getId();
            String token = jwtService.getToken("id", id);

            // bCrypt 변경된 패스워드 저장 aaa
            Member memberEntity;
            memberEntity = new Member();
            memberEntity.setId(id);
            memberEntity.setPassword(encodedPwdNew);
            memberEntity.setEmail(member.getEmail());
            memberRepository.save(memberEntity);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            res.addCookie(cookie);

            return new ResponseEntity<>(id, HttpStatus.OK);

        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/account/logout")
    public ResponseEntity<HttpStatus> logout(HttpServletResponse res) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        res.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity<?> check(@CookieValue(value = "token", required = false) String token) {
        Claims claims = jwtService.getClaims(token);

        if (claims != null) {
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
