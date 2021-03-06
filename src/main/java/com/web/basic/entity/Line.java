package com.web.basic.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 线体基础信息表
 *
 */
@Entity(name = "Line")
@Table(name = Line.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class Line extends BaseEntity {
    private static final long serialVersionUID = 4625660587007894370L;
    public static final String TABLE_NAME = "MES_base_LINE";
    
   /* public Line(String lineNo1,String linerName1,String lineName1,
    		String linerCode1, Date createDate1,Date lastupdateDate1,
    		int checkStatus1,Long idd){
    	lineNo = lineNo1;
    	lineName = lineName1;
    	checkStatus = checkStatus1;
    	linerCode = linerCode1;
    	linerName = linerName1;
    	createDate = createDate1;
    	lastupdateDate = lastupdateDate1;
    	id= idd;
    }*/

    /**
     * 线体编码
     */
    @ApiModelProperty(name = "lineNo", value = "线体编码")
    @Column(length = 50)
    protected String lineNo;

    /**
     * 线体名称
     */
    @ApiModelProperty(name = "lineName", value = "线体名称")
    @Column(length = 50)
    protected String lineName;

    /**
     * 状态1:正常 0:禁用
     */
    @ApiModelProperty(name = "checkStatus", value = "状态")
    @Column(length = 1)
    protected Integer checkStatus = 1;

    /**
     * 线长工号
     */
    @ApiModelProperty(name = "linerCode", value = "线长工号")
    @Column(length = 50)
    protected String linerCode;

    /**
     * 线长姓名
     */
    @ApiModelProperty(name = "linerName", value = "线长姓名")
    @Column(length = 50)
    protected String linerName;

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getLinerCode() {
        return linerCode;
    }

    public void setLinerCode(String linerCode) {
        this.linerCode = linerCode;
    }

    public String getLinerName() {
        return linerName;
    }

    public void setLinerName(String linerName) {
        this.linerName = linerName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("线体编码:").append(this.lineNo);
        sb.append(",线体名称:").append(this.lineName);
        sb.append(",状态:").append(this.checkStatus);
        sb.append(",线长工号:").append(this.linerCode);
        sb.append(",线长姓名:").append(this.linerName);
        return sb.toString();
    }
}
