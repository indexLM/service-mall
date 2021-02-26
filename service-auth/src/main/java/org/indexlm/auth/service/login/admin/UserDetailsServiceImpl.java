package org.indexlm.auth.service.login.admin;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * UserDetailsServiceImpl
 *
 * @author LiuMing
 * @since 2021/2/26
 */
@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录核心方法
     *
     * @param username 用户名
     * @return {@link UserDetails}
     * @author LiuMing
     * @since 2021/2/26
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return null;
    }

    /**
     * 获得用户权限信息
     *
     * @param userId 用户权限信息
     * @return {@link List<GrantedAuthority>}
     */
    private List<GrantedAuthority> getPermissions(Long userId) {
        //根据角色信息查询出用户权限列表
        List<String> permissions = null;
        String userPermissionString = StringUtils.join(permissions.toArray(), ",");
        return AuthorityUtils.commaSeparatedStringToAuthorityList(userPermissionString);
    }
}
