package com.web.basePrice.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.WorkCenter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

/**
 * 人工制费维护
 *
 */
@Entity(name = "BaseFee")
@Table(name = BaseFee.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class BaseFee extends BaseEntity {
	 private static final long serialVersionUID = 4625660587007894370L;
	    public static final String TABLE_NAME = "BJ_BASE_FEE";
	    
	    /**
	     * 工作中心Id
	     */
	    @ApiModelProperty(name = "workcenterId", value = "工作中心Id")
	    @Column(length = 20)
	    protected Long workcenterId;
	    
	    @ApiModelProperty(name="workCenter",hidden=true,value="工作中心id")
	    @ManyToOne
	    @JoinColumn(name="workcenterId",insertable=false,updatable=false)
	    @NotFound(action=NotFoundAction.IGNORE)
	    protected BjWorkCenter workCenter;
	    
		/**
		 * 是否有效
		 */
		@ApiModelProperty(name = "enabled", value = "是否有效")
		@Column(length = 10)
		protected Integer enabled = 1;
		
		/**
	     * 工序Id
	     */
	    @ApiModelProperty(name = "procId", value = "工序Id")
	    @Column(length = 20)
	    protected Long procId;

	    /**
	     * 工序名称
	     */
	    @ApiModelProperty(name = "procName", value = "工序名称")
	    @Column(length = 50)
	    protected String procName;

		@ApiModelProperty(name = "proc", hidden = true, value = "工序")
		@ManyToOne
		@JoinColumn(name = "procId", insertable = false, updatable = false)
		@NotFound(action = NotFoundAction.IGNORE)
		protected Proc proc;

		/**
		 * 机台名称
		 */
		@ApiModelProperty(name = "mhType", value = "机台名称")
		@Column(length = 20)
		protected String mhType;

		/**
		 * 机台编码
		 */
		@ApiModelProperty(name = "modelCode", value = "机台编码")
		@Column(length = 20)
		protected String modelCode;


		/**
		 * 人工费率（元/小时）
		 */
		@ApiModelProperty(name = "feeLh", value = "人工费率（元/小时）")
		@Column(length = 30)
		protected String feeLh;

		/**
		 * 制费费率（元/小时）
		 */
		@ApiModelProperty(name = "feeMh", value = "制费费率（元/小时）")
		@Column(length = 10)
		protected String feeMh;

		/**
		 * 附件id
		 */
		@ApiModelProperty(name = "fileId", value = "附件id")
		@Column(length = 200)
		protected String fileId;

		/**
		 * 失效时间
		 */
		@Column
		@Temporal(TemporalType.DATE)
		@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
		@ApiModelProperty(name="delTime",value="失效时间")
		protected Date expiresTime;

		public String getFileId() {
			return fileId;
		}

		public void setFileId(String fileId) {
			this.fileId = fileId;
		}

		public Long getWorkcenterId() {
			return workcenterId;
		}

		public void setWorkcenterId(Long workcenterId) {
			this.workcenterId = workcenterId;
		}

		public Integer getEnabled() {
			return enabled;
		}

		public void setEnabled(Integer enabled) {
			this.enabled = enabled;
		}

		public String getProcName() {
			return procName;
		}

		public void setProcName(String procName) {
			this.procName = procName;
		}

		public String getMhType() {
			return mhType;
		}

		public void setMhType(String mhType) {
			this.mhType = mhType;
		}

		public String getFeeLh() {
			return feeLh;
		}

		public void setFeeLh(String feeLh) {
			this.feeLh = feeLh;
		}

		public String getFeeMh() {
			return feeMh;
		}

		public void setFeeMh(String feeMh) {
			this.feeMh = feeMh;
		}

		public BjWorkCenter getWorkCenter() {
			return workCenter;
		}

		public void setWorkCenter(BjWorkCenter workCenter) {
			this.workCenter = workCenter;
		}

		public Long getProcId() {
			return procId;
		}

		public void setProcId(Long procId) {
			this.procId = procId;
		}

		public Date getExpiresTime() {
			return expiresTime;
		}

		public void setExpiresTime(Date expiresTime) {
			this.expiresTime = expiresTime;
		}

		public Proc getProc() {
			return proc;
		}

		public void setProc(Proc proc) {
			this.proc = proc;
		}

		public String getModelCode() {
			return modelCode;
		}

		public void setModelCode(String modelCode) {
			this.modelCode = modelCode;
		}
}
