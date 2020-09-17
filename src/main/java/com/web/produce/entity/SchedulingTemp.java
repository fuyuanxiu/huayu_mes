package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 排产信息导入临时表
 */
@Entity(name = "SchedulingTemp")
@Table(name= SchedulingTemp.TABLE_NAME)
@DynamicUpdate
public class SchedulingTemp extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "produce_scheduling_temp";

    /**
     * 部门ID
     */
    @ApiModelProperty(name = "pkDepartment", value = "部门ID")
    @Column
    protected Long pkDepartment;

    /**
     * 部门编码或名称
     */
    @ApiModelProperty(name = "bsDepartCode", value = "部门编码或名称")
    @Column(length = 100)
    protected String bsDepartCode;

    /**
     * 生产日期
     */
    @ApiModelProperty(name = "bsProduceTime", value = "生产日期")
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
    protected Date bsProduceTime;

    /**
     * 班次
     */
    @ApiModelProperty(name = "bsShift", value = "班次")
    @Column(length = 50)
    protected String bsShift;

    /**
     * 客户
     */
    @ApiModelProperty(name = "bsCustomer", value = "客户")
    @Column(length = 50)
    protected String bsCustomer;

    /**
     * 线别
     */
    @ApiModelProperty(name = "bsLine", value = "线别")
    @Column(length = 50)
    protected String bsLine;

    /**
     * 工单类别
     */
    @ApiModelProperty(name = "bsOrderType", value = "工单类别")
    @Column(length = 50)
    protected String bsOrderType;

    /**
     * 制令单号
     */
    @ApiModelProperty(name = "bsUniqueOrderNo", value = "制令单号")
    @Column
    protected String bsUniqueOrderNo;

    /**
     * 工单号
     */
    @ApiModelProperty(name = "bsOrderNo", value = "工单号")
    @Column(length = 100)
    protected String bsOrderNo;

    /**
     * 生产状态
     */
    @ApiModelProperty(name = "bsStatus", value = "生产状态")
    @Column(length = 50)
    protected String bsStatus;

    /**
     * 物料ID
     */
    @ApiModelProperty(name = "pkMtrial", value = "物料ID")
    @Column
    protected Long pkMtrial;

    /**
     * 物料编码或名称
     */
    @ApiModelProperty(name = "bsMtrialCode", value = "物料编码或名称")
    @Column(length = 100)
    protected String bsMtrialCode;

    /**
     * 物料描述
     */
    @ApiModelProperty(name = "bsMtrialDesc", value = "物料描述")
    @Column
    protected String bsMtrialDesc;

    /**
     * 加工工艺ID（工序ID）
     */
    @ApiModelProperty(name = "pkWoProc", value = "加工工艺ID（工序ID）")
    @Column
    protected Long pkWoProc;

    /**
     * 加工工艺编码或名称
     */
    @ApiModelProperty(name = "bsProcCode", value = "加工工艺编码或名称")
    @Column(length = 100)
    protected String bsProcCode;

    /**
     * 工单残
     */
    @ApiModelProperty(name = "bsRestNum", value = "工单残")
    @Column
    protected Integer bsRestNum;

    /**
     * 计划生产数量
     */
    @ApiModelProperty(name = "bsPlanNum", value = "计划生产数量")
    @Column
    protected Integer bsPlanNum;

    /**
     * 用人量
     */
    @ApiModelProperty(name = "bsPeopleNum", value = "用人量")
    @Column
    protected Integer bsPeopleNum;

    /**
     * 产能
     */
    @ApiModelProperty(name = "bsCapacityNum", value = "产能")
    @Column
    protected Integer bsCapacityNum;

    /**
     * 预计工时(H/人)
     */
    @ApiModelProperty(name = "bsPlanHours", value = "预计工时(H/人)")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsPlanHours;

    /**
     * 实际生产数量
     */
    @ApiModelProperty(name = "bsActualNum", value = "实际生产数量")
    @Column
    protected Integer bsActualNum;

    /**
     * 实际工时(H/人)
     */
    @ApiModelProperty(name = "bsActualHours", value = "实际工时(H/人)")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsActualHours;

    /**
     * 计划金额
     */
    @ApiModelProperty(name = "bsPlanPrice", value = "计划金额")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsPlanPrice;

    /**
     * 实际金额
     */
    @ApiModelProperty(name = "bsActualPrice", value = "实际金额")
    @Column(precision = 24, scale = 5)
    protected BigDecimal bsActualPrice;

    /**
     * 备注
     */
    @ApiModelProperty(name = "bsRemark", value = "备注")
    @Column(length = 500)
    protected String bsRemark;

    /**
     * 错误信息
     */
    @ApiModelProperty(name = "bsError", value = "错误信息")
    @Column(length = 1000)
    protected String bsError;

    /**
     * 校验状态（0：校验通过 / 1：校验不通过 / 2：警告）
     * 警告：检验值不在上下限阀值之间
     */
    @ApiModelProperty(name = "bsCheckStatus", value = "校验状态（0：校验通过 / 1：校验不通过 / 2：警告）")
    @Column
    protected Integer bsCheckStatus = 0;

    /**
     * 用户ID
     */
    @ApiModelProperty(name = "pkSysUser", value = "用户ID")
    @Column
    protected Long pkSysUser;

    public Long getPkDepartment() {
        return pkDepartment;
    }

    public void setPkDepartment(Long pkDepartment) {
        this.pkDepartment = pkDepartment;
    }

    public String getBsDepartCode() {
        return bsDepartCode;
    }

    public void setBsDepartCode(String bsDepartCode) {
        this.bsDepartCode = bsDepartCode;
    }

    public Date getBsProduceTime() {
        return bsProduceTime;
    }

    public void setBsProduceTime(Date bsProduceTime) {
        this.bsProduceTime = bsProduceTime;
    }

    public String getBsShift() {
        return bsShift;
    }

    public void setBsShift(String bsShift) {
        this.bsShift = bsShift;
    }

    public String getBsCustomer() {
        return bsCustomer;
    }

    public void setBsCustomer(String bsCustomer) {
        this.bsCustomer = bsCustomer;
    }

    public String getBsLine() {
        return bsLine;
    }

    public void setBsLine(String bsLine) {
        this.bsLine = bsLine;
    }

    public String getBsOrderType() {
        return bsOrderType;
    }

    public void setBsOrderType(String bsOrderType) {
        this.bsOrderType = bsOrderType;
    }

    public String getBsUniqueOrderNo() {
        return bsUniqueOrderNo;
    }

    public void setBsUniqueOrderNo(String bsUniqueOrderNo) {
        this.bsUniqueOrderNo = bsUniqueOrderNo;
    }

    public String getBsOrderNo() {
        return bsOrderNo;
    }

    public void setBsOrderNo(String bsOrderNo) {
        this.bsOrderNo = bsOrderNo;
    }

    public String getBsStatus() {
        return bsStatus;
    }

    public void setBsStatus(String bsStatus) {
        this.bsStatus = bsStatus;
    }

    public Long getPkMtrial() {
        return pkMtrial;
    }

    public void setPkMtrial(Long pkMtrial) {
        this.pkMtrial = pkMtrial;
    }

    public String getBsMtrialCode() {
        return bsMtrialCode;
    }

    public void setBsMtrialCode(String bsMtrialCode) {
        this.bsMtrialCode = bsMtrialCode;
    }

    public String getBsMtrialDesc() {
        return bsMtrialDesc;
    }

    public void setBsMtrialDesc(String bsMtrialDesc) {
        this.bsMtrialDesc = bsMtrialDesc;
    }

    public Long getPkWoProc() {
        return pkWoProc;
    }

    public void setPkWoProc(Long pkWoProc) {
        this.pkWoProc = pkWoProc;
    }

    public String getBsProcCode() {
        return bsProcCode;
    }

    public void setBsProcCode(String bsProcCode) {
        this.bsProcCode = bsProcCode;
    }

    public Integer getBsRestNum() {
        return bsRestNum;
    }

    public void setBsRestNum(Integer bsRestNum) {
        this.bsRestNum = bsRestNum;
    }

    public Integer getBsPlanNum() {
        return bsPlanNum;
    }

    public void setBsPlanNum(Integer bsPlanNum) {
        this.bsPlanNum = bsPlanNum;
    }

    public Integer getBsPeopleNum() {
        return bsPeopleNum;
    }

    public void setBsPeopleNum(Integer bsPeopleNum) {
        this.bsPeopleNum = bsPeopleNum;
    }

    public Integer getBsCapacityNum() {
        return bsCapacityNum;
    }

    public void setBsCapacityNum(Integer bsCapacityNum) {
        this.bsCapacityNum = bsCapacityNum;
    }

    public BigDecimal getBsPlanHours() {
        return bsPlanHours;
    }

    public void setBsPlanHours(BigDecimal bsPlanHours) {
        this.bsPlanHours = bsPlanHours;
    }

    public Integer getBsActualNum() {
        return bsActualNum;
    }

    public void setBsActualNum(Integer bsActualNum) {
        this.bsActualNum = bsActualNum;
    }

    public BigDecimal getBsActualHours() {
        return bsActualHours;
    }

    public void setBsActualHours(BigDecimal bsActualHours) {
        this.bsActualHours = bsActualHours;
    }

    public BigDecimal getBsPlanPrice() {
        return bsPlanPrice;
    }

    public void setBsPlanPrice(BigDecimal bsPlanPrice) {
        this.bsPlanPrice = bsPlanPrice;
    }

    public BigDecimal getBsActualPrice() {
        return bsActualPrice;
    }

    public void setBsActualPrice(BigDecimal bsActualPrice) {
        this.bsActualPrice = bsActualPrice;
    }

    public String getBsRemark() {
        return bsRemark;
    }

    public void setBsRemark(String bsRemark) {
        this.bsRemark = bsRemark;
    }

    public String getBsError() {
        return bsError;
    }

    public void setBsError(String bsError) {
        this.bsError = bsError;
    }

    public Integer getBsCheckStatus() {
        return bsCheckStatus;
    }

    public void setBsCheckStatus(Integer bsCheckStatus) {
        this.bsCheckStatus = bsCheckStatus;
    }

    public Long getPkSysUser() {
        return pkSysUser;
    }

    public void setPkSysUser(Long pkSysUser) {
        this.pkSysUser = pkSysUser;
    }
}
