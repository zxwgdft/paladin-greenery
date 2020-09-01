package com.paladin.organization.dao.mapper;

import com.paladin.framework.mybatis.CustomMapper;
import com.paladin.organization.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author TontoZhou
 * @since 2019/12/12
 */
public interface SysUserMapper extends CustomMapper<SysUser> {

    @Update("UPDATE sys_user SET account = #{nowAccount}, update_time=now() WHERE user_id = #{userId} AND user_type = #{userType}")
    int updateAccount(@Param("userType") int userType, @Param("userId") String userId, @Param("nowAccount") String nowAccount);


}
