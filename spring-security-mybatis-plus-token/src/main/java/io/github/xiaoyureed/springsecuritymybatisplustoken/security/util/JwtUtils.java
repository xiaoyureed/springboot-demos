package io.github.xiaoyureed.springsecuritymybatisplustoken.security.util;

import io.github.xiaoyureed.springsecuritymybatisplustoken.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : xiaoyu 775000738@qq.com
 * @since : 2020/11/16
 */
@Slf4j
final class JwtUtils {

    /**
     * 生成足够的安全随机密钥，以适合符合规范的签名
     */
    private static final byte[] apiKeySecretBytes;
    private static final SecretKey secretKey;

    static {
        apiKeySecretBytes = Base64.getEncoder().encode(SecurityConstants.JWT_SECRET_KEY.getBytes());
        secretKey = Keys.hmacShaKeyFor(apiKeySecretBytes);
    }

    public static String createToken(String username, List<String> roles, boolean isRememberMe) {
        long expiration = isRememberMe ? SecurityConstants.EXPIRATION_REMEMBER : SecurityConstants.EXPIRATION;
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
        String tokenPrefix = Jwts.builder()
                .setHeaderParam("type", SecurityConstants.TOKEN_TYPE)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .claim(SecurityConstants.ROLE_CLAIMS, String.join(",", roles))
                .setIssuer("SnailClimb")
                .setIssuedAt(createdDate)
                .setSubject(username)
                .setExpiration(expirationDate)
                .compact();
        return SecurityConstants.TOKEN_PREFIX + tokenPrefix;
    }

    public boolean isTokenExpired(String token) {
        Date expiredDate = getTokenBody(token).getExpiration();
        return expiredDate.before(new Date());
    }

    public static String getUsernameByToken(String token) {
        return getTokenBody(token).getSubject();
    }

    /**
     * @deprecated 弃用。改为从数据库中获取，保证权限的即时性。
     */
    @Deprecated
    public static List<SimpleGrantedAuthority> getUserRolesByToken(String token) {
        String role = (String) getTokenBody(token)
                .get(SecurityConstants.ROLE_CLAIMS);
        return Arrays.stream(role.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }




//    private static final String CLAIM_KEY_USERNAME = "username";

//    public static String createJwtToken(String username, Date expiresAt, String jwtSecret) {
//        // 构造 jwt header
//        HashMap<String, Object> jwtHeader = new HashMap<>(2);
//        jwtHeader.put("alg", "HS256");
//        jwtHeader.put("typ", "JWT");
//
//        return JWT.create()
//                .withHeader(jwtHeader) // header
//                // payload
//                .withClaim(CLAIM_KEY_USERNAME, username)
//                // 签名时间
//                .withIssuedAt(Date.from(LocalDateTime.now().atZone(
//                        ZoneId.systemDefault()).toInstant()))
//                // token 过期时间
//                .withExpiresAt(expiresAt)
//                // signature
//                .sign(Algorithm.HMAC256(jwtSecret));
//
//    }
//
//    public static Map<String, Claim> resolveClaims(String token, String jwtSecret) {
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtSecret)).build();
//        try {
//            DecodedJWT decodedJWT = jwtVerifier.verify(token);
//            return decodedJWT.getClaims();
//        } catch (Exception e) {
//            log.error(">>> error of resolve token");
//            return null;
//        }
//    }
//
//    public static String resolveUsername(String token, String jwtSecret, String jwtClaimKeyUsername) {
//        Map<String, Claim> claims = resolveClaims(token, jwtSecret);
//        if (claims == null) {
//            log.error(">>> error of resolve token");
//            return "";
//        }
//        return claims.get(CLAIM_KEY_USERNAME).asString();
//    }
}
