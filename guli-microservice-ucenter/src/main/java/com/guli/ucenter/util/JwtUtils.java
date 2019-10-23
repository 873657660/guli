package com.guli.ucenter.util;

import com.guli.ucenter.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @author Jay
 * @create 2019-10-16 11:43
 */
public class JwtUtils {

    private static final long EXPIRE = 1000 * 60 * 30;
    private static final String APP_SECRET = "vGq1fbRddObVFZ6M26iLXi8MVtPv4U";

    public static String genJWT(Member member) {

        String JwtToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("guli-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", member.getId())
                .claim("mobile", member.getMobile())
                .claim("nickname", member.getNickname())
                .claim("avatar", member.getAvatar())
                .signWith(SignatureAlgorithm.HS256, APP_SECRET)
                .compact();

        return JwtToken;
    }

    public static Claims checkJwt(String JwtToken) {

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(JwtToken);
        Claims claims = claimsJws.getBody();

        return claims;
    }

}
