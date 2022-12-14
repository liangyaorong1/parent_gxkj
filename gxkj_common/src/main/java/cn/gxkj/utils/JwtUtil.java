package cn.gxkj.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

/**
 * Created by 37269 on 2018/6/9.
 */
//@ConfigurationProperties("jwt.config")
public class JwtUtil {

    private String key;
    private long ttl;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    /**
     * 签发 token
     */
    public String createJWT(String id, String subject,Map<String,Object> map){
        long now=System.currentTimeMillis();
        long exp=now+ttl;
        JwtBuilder jwtBuilder = Jwts.builder().setId(id)
                .setSubject(subject).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, key);
        for(Map.Entry<String,Object> entry:map.entrySet()) {
            jwtBuilder.claim(entry.getKey(),entry.getValue());
        }
        if(ttl>0){
            jwtBuilder.setExpiration( new Date(exp));
        }
        String token = jwtBuilder.compact();
        return token;
    }

    /**
     * 解析JWT
     * @param token
     * @return
     */
    public Claims parseJWT(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token).getBody();
        }catch (Exception e){

        }
        return claims;
    }

}
