package com.web.basic.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 不良类别基础信息表
 *
 */
@Entity(name = "Defective")
@Table(name = Defective.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Defective extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	   
	 public static final String TABLE_NAME = "MES_BASE_DEFECT_TYPE";
	    /**
	     * 不良类别编码
	     */
	    @ApiModelProperty(name = "defectTypeCode", value = "不良类别编码")
	    @Column(length = 50)
	    protected String defectTypeCode;

	    /**
	     * 不良类别名称
	     */
	    @ApiModelProperty(name = "defectTypeName", value = "不良类别名称")
	    @Column(length = 50)
	    protected String defectTypeName;

		/**
		 * 不良类别类型
		 */
		@ApiModelProperty(name = "defectClass", value = "不良类别类型")
		@Column
		protected Integer defectClass;
	    
	    /**
	     * 状态（1：正常 / 0：禁用）
	     */
	    @ApiModelProperty(name = "checkStatus", value = "状态（1：正常 / 0：禁用）")
	    @Column
	    protected Integer checkStatus = 1;

		public String getDefectTypeCode() {
			return defectTypeCode;
		}

		public void setDefectTypeCode(String defectTypeCode) {
			this.defectTypeCode = defectTypeCode;
		}

		public String getDefectTypeName() {
			return defectTypeName;
		}

		public void setDefectTypeName(String defectTypeName) {
			this.defectTypeName = defectTypeName;
		}

		public Integer getCheckStatus() {
			return checkStatus;
		}

		public void setCheckStatus(Integer checkStatus) {
			this.checkStatus = checkStatus;
		}

		public Integer getDefectClass() {
			return defectClass;
		}

		public void setDefectClass(Integer defectClass) {
			this.defectClass = defectClass;
		}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("不良类别编码:").append(this.defectTypeCode);
		sb.append("不良类别名称:").append(this.defectTypeName);
		sb.append("类型:").append(this.defectClass==0?"品质问题":"制程问题");
		sb.append("状态:").append(this.checkStatus==0?"禁用":"正常");
		return sb.toString();
	}
}
