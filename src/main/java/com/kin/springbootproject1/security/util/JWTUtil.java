package com.kin.springbootproject1.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
public class JWTUtil {

    private String secretKey = "secsecsecsecsecsecsecsecsecsecsecsecsecsecsecsecsecsecsecretKey";

    //1month
    private long expire = 60 * 24 * 30;

    public String generateToken(String content) throws Exception {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant())) //토큰 유효시간.
                .claim("sub", content)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();

    }

    public String validateAndExtract(String tokenStr)throws Exception{
        String contentValue=null;

        try{
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes("UTF-8"))
                    .build()
                    .parseClaimsJws(tokenStr);

            log.info("jws : " + jws);
            log.info("jws.getBody().getClass() : " + jws.getBody().getClass());

            Claims claims = (Claims) jws.getBody();

            contentValue = claims.getSubject();
            log.info("contentValue : " + contentValue);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("validateAndExtract error : " + e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }

}
