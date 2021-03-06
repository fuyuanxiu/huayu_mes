package com.web.quote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.app.base.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价单项目
 * 基础设置
 */

@Entity(name = "QuoteItemBase")
@Table(name = QuoteItemBase.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class QuoteItemBase extends BaseEntity{
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_quote_item_base";
	
	/**
	 * 类型
	 * 1:item
	 * 2:mater
	 * 3:process
	 */
	@ApiModelProperty(name = "bsStyle", value = "报价项目-编码")
	@Column(length = 50)
	protected String bsStyle;
	
    /**
	 * 报价项目-编码
	 */
	@ApiModelProperty(name = "bsCode", value = "报价项目-编码")
	@Column(length = 50)
	protected String bsCode;
	/**
	 * 报价项目名
	 */
	@ApiModelProperty(name = "bsName", value = "报价项目名")
	@Column(length = 50)
	protected String bsName;
	
	/**
	 * 待办人
	 */
	@ApiModelProperty(name = "bsPerson", value = "待办人")
	@Column(length = 50)
	protected String bsPerson;
	
	/**
     * 待办人Id
     */
	@ApiModelProperty(name = "toDoBy", value = "待办人Id")
    @Column(length = 30)
    protected Long toDoBy;
    
    /**
     * 页面路由
     */
    @ApiModelProperty(name="bsRouter",value="页面路由")
    @Column(length = 100)
    protected String bsRouter;

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

	public String getBsPerson() {
		return bsPerson;
	}

	public void setBsPerson(String bsPerson) {
		this.bsPerson = bsPerson;
	}

	public Long getToDoBy() {
		return toDoBy;
	}

	public void setToDoBy(Long toDoBy) {
		this.toDoBy = toDoBy;
	}

	public String getBsRouter() {
		return bsRouter;
	}

	public void setBsRouter(String bsRouter) {
		this.bsRouter = bsRouter;
	}

	public String getBsStyle() {
		return bsStyle;
	}

	public void setBsStyle(String bsStyle) {
		this.bsStyle = bsStyle;
	}
	
	
	
}
