package com.liansen.common.jwt;

import com.liansen.common.vo.AuthTokenDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * @Author: cdy
 * @Date: 2018/12/28 14:59
 * @Version 1.0
 */
public class JsonWebTokenUtility {

    Logger log = LoggerFactory.getLogger(JsonWebTokenUtility.class);

    private SignatureAlgorithm signatureAlgorithm;
    private Key secretKey;

    public JsonWebTokenUtility() {

        // 这里不是真正安全的实践
        // 为了简单，我们存储一个静态key在这里，
        signatureAlgorithm = SignatureAlgorithm.HS512;
        String encodedKey ="L7A/6zARSkK1j7Vd5SDD9pSSqZlqF7mAhiOgRbgv9Smce6tf4cJnvKOjtKPxNNnWQj+2lQEScm3XIUjhW+YVZg==";
        secretKey = deserializeKey(encodedKey);
    }

    private Key deserializeKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        Key key = new SecretKeySpec(decodedKey, getSignatureAlgorithm().getJcaName());
        return key;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    private Key getSecretKey() {
        return secretKey;
    }

    public AuthTokenDetails parseAndValidate(String token) {
        AuthTokenDetails authTokenDetails = null;
        try {
            Claims claims =  Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
            String userId = (String) claims.get("userId");
            String name = (String) claims.get("name");
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();

            authTokenDetails = new AuthTokenDetails();
            authTokenDetails.setName(name);
            authTokenDetails.setId(Long.valueOf(userId));
            authTokenDetails.setUsername(username);
            authTokenDetails.setExpirationDate(expirationDate);
        } catch (JwtException ex) {
            log.error(ex.getMessage(), ex);
        }
        return authTokenDetails;
    }
}
