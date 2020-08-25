package com.paladin.organization.core;

import com.paladin.framework.exception.BusinessException;
import com.paladin.framework.jwt.TokenProvider;
import com.paladin.framework.security.UserSession;
import com.paladin.framework.security.UserSessionFactory;
import com.paladin.framework.security.UserSessionThreadLocal;
import com.paladin.framework.utils.StringUtil;
import com.paladin.organization.model.SysUser;
import com.paladin.organization.service.PersonnelService;
import com.paladin.organization.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author TontoZhou
 * @since 2020/8/25
 */
@Slf4j
@Component
public class WebSecurityManager implements HandlerInterceptor, UserSessionFactory {

    @Value("${auth.user-field:User-ID}")
    private String authUserField;

    // 应用客户端token过期时间
    @Value("${app.client.token-expire-milliseconds:86400000}")
    private long appClientTokenExpireMilliseconds;

    // 用户token过期时间
    @Value("${user.token-expire-milliseconds:3600000}")
    private long userTokenExpireMilliseconds;

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PersonnelService personnelService;

    // 拼接userId和userType
    private final static String separator = "~";

    public final static int USER_TYPE_APP = 1;
    public final static int USER_TYPE_ORG_USER = 2;


    // session线程变量设置器
    private UserSessionThreadLocal sessionThreadLocal = new UserSessionThreadLocal(this);

    /**
     * 创建用户会话
     *
     * @param subject 主题，这里应该是payload信息，例如userId
     */
    @Override
    public UserSession createUserSession(String subject) {
        try {
            String[] args = subject.split(separator);
            String userId = args[0];
            int userType = Integer.valueOf(args[1]);

            switch (userType) {
                case USER_TYPE_APP: {
                    return new AppClientSession(userId, appClientDataLoader);

                }
                case USER_TYPE_ORG_USER: {
                    return new OrgUserSession(userId, orgUserDataLoader);
                }
            }
        } catch (Exception e) {
            log.error("创建用户session异常", e);
        }

        throw new BusinessException("验证用户异常，请尝试重新登录");
    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String subject = request.getHeader(authUserField);
        sessionThreadLocal.createUserSession(subject);
        return true;
    }


    /**
     * 获取当前用户session
     */
    public static OrgUserSession getCurrentOrgUserSession() {
        UserSession userSession = UserSessionThreadLocal.getCurrentUserSession();
        if (userSession != null && userSession instanceof OrgUserSession) {
            return (OrgUserSession) userSession;
        }
        return null;
    }

    /**
     * 获取当前应用端session
     */
    public static AppClientSession getCurrentAppClientSession() {
        UserSession userSession = UserSessionThreadLocal.getCurrentUserSession();
        if (userSession != null && userSession instanceof AppClientSession) {
            return (AppClientSession) userSession;
        }
        return null;
    }

    /**
     * 刷新token，产生一个新的token
     */
    public OpenToken refreshToken() {
        /*
         * 是否要创建黑名单机制，把旧token加入黑名单
         *
         * 刷新token主要针对即将过期的token，所以可以不考虑黑名单，
         * 并且大量的刷新可能会过分撑大黑名单
         *
         */

        UserSession userSession = UserSessionThreadLocal.getCurrentUserSession();
        if (userSession != null) {
            String userId = userSession.getUserId();

            if (userSession instanceof AppClientSession) {
                long expires = System.currentTimeMillis() + userTokenExpireMilliseconds;
                String jwtToken = tokenProvider.createJWT(createSubject(userId, USER_TYPE_APP), null, new Date(expires));
                return new OpenToken(jwtToken, expires);
            }

            if (userSession instanceof OrgUserSession) {
                long expires = System.currentTimeMillis() + userTokenExpireMilliseconds;
                String jwtToken = tokenProvider.createJWT(createSubject(userId, USER_TYPE_ORG_USER), null, new Date(expires));
                return new OpenToken(jwtToken, expires);
            }
        }

        throw new BusinessException("登录状态异常，无法刷新token");
    }

    /**
     * 创建token
     */
    public OpenToken createToken(String userId, int userType) {
        long expires = System.currentTimeMillis() + userTokenExpireMilliseconds;
        String jwtToken = tokenProvider.createJWT(createSubject(userId, userType), null, new Date(expires));
        return new OpenToken(jwtToken, expires);
    }

    /**
     * 拼接用户id和用户type作为subject，用于解析token中具体用户
     */
    private String createSubject(String userId, int userType) {
        return userId + separator + userType;
    }

    private OrgUserDataLoader orgUserDataLoader = new OrgUserDataLoader() {
        /**
         * 加载用户会话基本信息
         */
        @Override
        public void loadUserSession(OrgUserSession userSession) {
            String sysUserId = userSession.getUserId();

            SysUser sysUser = sysUserService.get(sysUserId);
            if (sysUser == null) {
                throw new BusinessException("登录用户不存在");
            }

            if (sysUser.getIsSysAdmin()) {
                userSession.isSystemAdmin = true;
            }

            userSession.personnelId = sysUser.getPersonnelId();
            userSession.userName = sysUser.getAccount();

            // TODO 加载权限
        }

        /**
         * 加载登录用户对应人员信息
         *
         */
        @Override
        public void loadPersonnel(OrgUserSession userSession) {
            if (StringUtil.isNotEmpty(userSession.personnelId)) {
                userSession.personnel = personnelService.get(userSession.personnelId);
                if (userSession.personnel == null) {
                    throw new BusinessException("登录用户对应人员信息已经不存在");
                }
            }
        }
    };

    private AppClientDataLoader appClientDataLoader = new AppClientDataLoader() {
        /**
         * 加载应用客户端会话基本信息
         *
         */
        @Override
        public void loadAppClientSession(AppClientSession session) {

            // TODO 读取客户端session相关信息
        }
    };

}
