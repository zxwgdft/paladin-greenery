package com.paladin.organization.model;

import com.paladin.framework.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.util.Date;

/**
 * @author TontoZhou
 * @since 2019/12/13
 */
@Getter
@Setter
public class SysUser extends BaseModel {

    public static final String FIELD_ACCOUNT = "account";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_USER_TYPE = "userType";

    /**
     * 启用状态
     */
    public final static Integer STATE_ENABLED = 1;
    /**
     * 停用状态
     */
    public final static Integer STATE_DISABLED = 0;


    // 系统管理员
    public final static int USER_TYPE_ADMIN = 1;
    // 个人用户
    public final static int USER_TYPE_PERSONNEL = 2;


    @Id
    private String id;
    private String account;
    private String password;
    private String salt;
    private Integer userType;
    private String userId;
    private Integer state;
    private String lastLoginIp;
    private Date lastLoginTime;
    private Boolean isFirstLogin;

}
