package com.example.wincloud.config;

import com.example.wincloud.dao.SysRolePermissionMapper;
import com.example.wincloud.dao.SysUserRoleMapper;
import com.example.wincloud.dao.UserMapper;
import com.example.wincloud.pojo.SysPermission;
import com.example.wincloud.pojo.SysRole;
import com.example.wincloud.pojo.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
public class ShiroConfig {

    @Resource
    UserMapper userMapper;

    @Resource
    SysUserRoleMapper sysUserRoleMapper;

    @Resource
    SysRolePermissionMapper sysRolePermissionMapper;

    private static final String AUTHORIZATION = "Authorization";

    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";


    @Bean
    public Realm realm() {
        return new AuthorizingRealm() {

            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
                UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
                String username = token.getUsername();

                log.debug("数据库查询用户[{}]", username);

                SysUser sysUser = userMapper.getUserByUsername(username);
                if (sysUser == null) return null;

                System.out.println("-- " + sysUser.getPassword() + " " + String.valueOf(token.getPassword()));
                if (!sysUser.getPassword().equals(String.valueOf(token.getPassword()))) {
                    throw new IncorrectCredentialsException();
                }
                return new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), sysUser.getUsername());
            }

            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
                System.out.println("a_>" + principalCollection);
                Object primaryPrincipal = principalCollection.getPrimaryPrincipal();
                System.out.println("a" + primaryPrincipal);
                SysUser sysUser = (SysUser) primaryPrincipal;
                Long id = sysUser.getId();
                log.debug("用户[{}]获取权限", id);
                SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();

                List<SysRole> sysRoleList = sysUserRoleMapper.listRoleByUserId(id);
                log.debug(sysRoleList.toString());

                // 获取用户角色集
                Set<String> roleSet = new HashSet<>();
                for (SysRole sysRole : sysRoleList) {
                    roleSet.add(sysRole.getName());
                }
                authorizationInfo.setRoles(roleSet);

                // 获取角色权限集
                for (SysRole sysRole : sysRoleList) {
                    Long role_id = sysRole.getId();
                    List<SysPermission> sysPermissionList = sysRolePermissionMapper.listSysPermissionByRoleId(role_id);
                    Set<String> permissionSet = new HashSet<>();
                    for (SysPermission sysPermission : sysPermissionList) {
                        permissionSet.add(sysPermission.getName());
                    }
                    authorizationInfo.addStringPermissions(permissionSet);
                }

                authorizationInfo.getStringPermissions().forEach(System.out::println);
                return authorizationInfo;
            }
        };
    }

    @Bean
    DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm());
        manager.setSessionManager(sessionManager());
        manager.setCacheManager(redisCacheManager());
        return manager;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("192.168.189.130:6379");
        redisManager.setPassword("123456");
        return redisManager;
    }

    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager manager = new DefaultWebSessionManager(){
            @Override
            protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
                String sessionId = WebUtils.toHttp(request).getHeader(AUTHORIZATION);
                if(!StringUtils.isEmpty(sessionId)) {
                    request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
                    request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
                    request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
                    return sessionId;
                } else {
                    return super.getSessionId(request, response);
                }
            }
        };
        manager.setSessionDAO(redisSessionDAO());
        return manager;
    }

    @Bean
    public SessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setExpire(1800);
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return  authorizationAttributeSourceAdvisor;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/**", "authc");
        return chainDefinition;
    }

}
