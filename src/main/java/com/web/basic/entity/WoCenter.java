package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 工作中心基础信息表
 *
 */
@Entity(name = "WoCenter")
@Table(name = WoCenter.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class WoCenter extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "basic_wo_center";
	    
	    /**
	     * 工作中心编码
	     */
	    @ApiModelProperty(name = "bsCode", value = "工作中心编码")
	    @Column(length = 10)
	    protected String bsCode;

	    /**
	     * 工作中心名称
	     */
	    @ApiModelProperty(name = "bsName", value = "工作中心名称")
	    @Column(length = 15)
	    protected String bsName;
	    
	    /**
	     * 状态（0：正常 / 1：禁用）
	     */
	    @ApiModelProperty(name = "bsStatus", value = "状态（0：正常 / 1：禁用）")
	    @Column
	    protected Integer bsStatus = 0;

		public String getBsCode() {
			return bsCode;
		}

		public void setBsCode(String bsCode) {
			this.bsCode = bsCode;
		}

		public String getBsName() {
			return bsName;
		}

		public void setBsName(String bsName) {
			this.bsName = bsName;
		}

		public Integer getBsStatus() {
			return bsStatus;
		}

		public void setBsStatus(Integer bsStatus) {
			this.bsStatus = bsStatus;
		}    
}
