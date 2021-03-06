package com.web.produce.entity;

import com.app.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.basic.entity.Defective;
import com.web.basic.entity.Department;
import com.web.basic.entity.Employee;
import com.web.basic.entity.Line;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.Process;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 指纹清除
 */
@Entity(name = "Clear")
@Table(name= Clear.TABLE_NAME)
@DynamicUpdate
public class Clear extends BaseEntity {
    private static final long serialVersionUID = -5951531333314901264L;
    public static final String TABLE_NAME = "MES_ISSUE_MAP";
   
    
    /**
     * 员工Id
     */
    @ApiModelProperty(name="empId",value="员工Id")
    @Column
    protected Long empId;

    @ApiModelProperty(name="emp",hidden=true,value="员工Id")
    @ManyToOne
    @JoinColumn(name = "empId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Employee employee;
    
    /**
     * 卡机Id
     */
    @ApiModelProperty(name="devClockId",value="卡机Id")
    @Column
    protected Long devClockId;

    @ApiModelProperty(name="devClock",hidden=true,value="卡机Id")
    @ManyToOne
    @JoinColumn(name = "devClockId", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected DevClock devClock;

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Long getDevClockId() {
		return devClockId;
	}

	public void setDevClockId(Long devClockId) {
		this.devClockId = devClockId;
	}

	public DevClock getDevClock() {
		return devClock;
	}

	public void setDevClock(DevClock devClock) {
		this.devClock = devClock;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("员工id:").append(this.empId);
		sb.append(",卡机id:").append(this.devClockId);
		return sb.toString();
	}
}
