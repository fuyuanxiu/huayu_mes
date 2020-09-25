package com.system.user.entity;

import com.app.base.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户基础信息表
 *
 */
@Entity(name = "SysUser")
@Table(name = SysUser.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class SysUser extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "SYS_USER";

    /**
     * 用户账号
     */
    @ApiModelProperty(name = "userCode", value = "用户账号")
    @Column(length = 50)
    protected String userCode;

    /**
     * 用户名称
     */
    @ApiModelProperty(name = "userName", value = "用户名称")
    @Column(length = 50)
    protected String userName;
   
    /**
     * 真实名称
     */
    @ApiModelProperty(name = "realName", value = "真实名称")
    @Column(length = 50)
    protected String realName;

    /**
     * 密码
     */
    @ApiModelProperty(name = "password", value = "密码")
    @Column(length = 50)
    protected String password;

    /**
     * 状态（0：正常 / 1：禁用）
     */
    @ApiModelProperty(name = "status", value = "状态（0：正常 / 1：禁用）")
    @Column
    protected Integer status = 0;

    /**
     * 性别（男/女）
     */
    @ApiModelProperty(name = "sex", value = "性别")
    @Column(length = 10)
    protected String sex;
    
    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email", value = "邮箱")
    @Column(length = 100)
    protected String email;
    
    /**
     * 手机号
     */
    @ApiModelProperty(name = "mobile", value = "手机号")
    @Column(length = 50)
    protected String mobile;

    /**
     * 注册来源
     */
    @ApiModelProperty(name = "registerSrc", value = "注册来源")
    @Column(length = 50)
    protected String registerSrc;

    /**
     * 角色ID
     */
    @Transient
    protected String roleIds;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRegisterSrc() {
		return registerSrc;
	}

	public void setRegisterSrc(String registerSrc) {
		this.registerSrc = registerSrc;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	

   
}
