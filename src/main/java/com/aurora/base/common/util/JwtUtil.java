package com.aurora.base.common.util;

import com.alibaba.fastjson.JSON;
import com.aurora.base.common.model.Global;
import com.aurora.base.model.auth.User;
import com.aurora.base.model.system.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * jwt token 工具类
 * @author PHQ
 * @create 2020-05-02 20:42
 **/
public class JwtUtil {

    public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
    private static final String CLAIM_KEY_USER_ID = "user_id";
    private static final String CLAIM_KEY_AUTHORITIES = "scope";
    //用来存token 后续换redis
    private Map<String, String> tokenMap = new ConcurrentHashMap<>(32);

    //@Value("${jwt.secret}")
    private String secret = Global.JWT_SECRET;

   // @Value("${jwt.expiration}")
    private Long access_token_expiration= Global.JWT_EXPIRATION;

   // @Value("${jwt.expiration}")
    private Long refresh_token_expiration =Global.JWT_EXPIRATION;

    //签名算法hs256
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    /**
     * 从token中获取用户信息
     * @param token
     * @return
     */
    public User getUserFromToken(String token) {
        User user;
        try {
            final Claims claims = getClaimsFromToken(token);
            long userId = getUserIdFromToken(token);
            String username = claims.getSubject();
            String roleName = claims.get(CLAIM_KEY_AUTHORITIES).toString();
            Role role = Role.builder().name(roleName).build();
            user = new User(userId, username, role, "");
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

    /**
     * 获取用户主键id
     * @param token
     * @return
     */
    public long getUserIdFromToken(String token) {
        long userId;
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = Long.parseLong(String.valueOf(claims.get(CLAIM_KEY_USER_ID)));
        } catch (Exception e) {
            userId = 0;
        }
        return userId;
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 通过用户名获取token 的用户信息
     * @param userName
     * @return
     */
    public User getUseFromTokenMap(String userName){
        if(this.tokenMap.get(userName)!=null){
           return  this.getUserFromToken(this.tokenMap.get(userName));

        };
        return null;
    }


    /**
     * 获取token创建时间
     * @param token
     * @return
     */
    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = claims.getIssuedAt();
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 生成token
     * @param user
     * @return
     */
    public String generateAccessToken(User user) {
        Map<String, Object> claims = generateClaims(user);
        claims.put(CLAIM_KEY_AUTHORITIES, authoritiesToArray(user.getAuthorities()).get(0));
        return generateAccessToken(user.getUsername(), claims);
    }

    /**
     * 根据时间判断你是否过期
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    /**
     * 执行刷新token操作
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            refreshedToken = generateAccessToken(claims.getSubject(), claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * token校验
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final long userId = getUserIdFromToken(token);
        final String username = getUsernameFromToken(token);
//        final Date created = getCreatedDateFromToken(token);
        return (userId == user.getId()
                && username.equals(user.getUsername())
                && !isTokenExpired(token)
//                && !isCreatedBeforeLastPasswordReset(created, userDetail.getLastPasswordResetDate())
        );
    }

    public String generateRefreshToken(User user) {
        Map<String, Object> claims = generateClaims(user);
        // 只授于更新 token 的权限
        String roles[] = new String[]{JwtUtil.ROLE_REFRESH_TOKEN};
        claims.put(CLAIM_KEY_AUTHORITIES, JSON.toJSONString(roles));
        return generateRefreshToken(user.getUsername(), claims);
    }

    /**
     * 往同步map 存放token
     * @param userName
     * @param token
     */
    public void putToken(String userName, String token) {
        tokenMap.put(userName, token);
    }

    /**
     * 删除token
     * @param userName
     */
    public void deleteToken(String userName) {
        tokenMap.remove(userName);
    }

    /**
     * 同步map 是否存在token
     * @param userName
     * @param token
     * @return
     */
    public boolean containToken(String userName, String token) {
        if (userName != null && tokenMap.containsKey(userName) && tokenMap.get(userName).equals(token)) {
            return true;
        }
        return false;
    }

    /**
     *  从token中获取Claims 载荷(Payload)
     * @param token
     * @return
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 过期时间
     * @param expiration
     * @return
     */
    private Date generateExpirationDate(long expiration) {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Map<String, Object> generateClaims(User user) {
        Map<String, Object> claims = new HashMap<>(16);
        claims.put(CLAIM_KEY_USER_ID, user.getId());
        return claims;
    }

    /**
     * 组装用户及jwt相关信息 生成token
     * @param subject
     * @param claims
     * @return
     */
    private String generateAccessToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, access_token_expiration);
    }

    /**
     * 用户相关权限
     * @param authorities
     * @return
     */
    private List authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        List<String> list = new ArrayList<>();
        for (GrantedAuthority ga : authorities) {
            list.add(ga.getAuthority());
        }
        return list;
    }

    /**
     * 刷新 token 设置新的过期时间
     * @param subject
     * @param claims
     * @return
     */
    private String generateRefreshToken(String subject, Map<String, Object> claims) {
        return generateToken(subject, claims, refresh_token_expiration);
    }


    /**
     *  生成token
     * @param subject   用户信息 名称
     * @param claims     载荷
     * @param expiration  过期时间
     * @return
     */
    private String generateToken(String subject, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .setClaims(claims)   //创建payload的私有声明
                .setSubject(subject)  //jwt主体 可以理解为与用户唯一标识
                .setId(UUID.randomUUID().toString()) //token id
                .setIssuedAt(new Date()) //jwt的签发时间
                .setExpiration(generateExpirationDate(expiration)) //过期时间
                .compressWith(CompressionCodecs.DEFLATE)
                .signWith(SIGNATURE_ALGORITHM, secret) //jwt使用的算法和秘钥
                .compact();
    }

}
