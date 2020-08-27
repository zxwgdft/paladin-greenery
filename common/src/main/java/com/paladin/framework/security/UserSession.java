package com.paladin.framework.security;


import java.io.Serializable;
import java.util.Objects;

/**
 * 用户会话信息
 *
 * @author TontoZhou
 * @since 2018年1月29日
 */
public class UserSession implements Serializable {

    private String userId;
    private String userType;

    public UserSession(String userId, String userType) {
        this.userId = userId;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        final UserSession that = (UserSession) o;
        return getUserId().equals(that.getUserId()) && getUserType().equals(that.getUserType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getUserType());
    }


}
