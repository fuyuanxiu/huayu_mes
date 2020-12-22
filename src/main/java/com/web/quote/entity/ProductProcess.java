package com.web.quote.entity;

import java.math.BigDecimal;

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
import com.web.basePrice.entity.BjWorkCenter;
import com.web.basePrice.entity.Proc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报价工艺流程表
 *
 */
@Entity(name = "ProductProcess")
@Table(name = ProductProcess.TABLE_NAME)
@DynamicUpdate
@ApiModel
public class ProductProcess extends BaseEntity {
	
	private static final long serialVersionUID = 4625660587007894370L;
	public static final String TABLE_NAME = "price_product_process";
	
	/**
	 * 类型
	 * 五金:hardware
	 * 注塑:molding
	 * 表面处理:surface
	 * 组装:packag
	 */
	@ApiModelProperty(name = "bsType", value = "类型")
	@Column(length = 50)
	protected String bsType;
	
	/**
     * 状态
     * 0：草稿，1:完成
     */
	@Column
	@ApiModelProperty(name="bsStatus",value="状态")
    protected int bsStatus = 0;
	
	/**
     * 是否需要填写
     * 0：需要，1:不需要
     */
	@Column
	@ApiModelProperty(name="bsNeed",value="是否需要填写")
    protected int bsNeed = 0;
	
	/**
     * 关联主表
     */
    @ApiModelProperty(name="pkQuote",value="报价主表")
    @Column
    protected Long pkQuote;

    @ApiModelProperty(name="quote",hidden=true,value="报价主表")
    @ManyToOne
    @JoinColumn(name = "pkQuote", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Quote quote;
    
    /**
	 * Bom零件名称
	 */
	@ApiModelProperty(name = "bsName", value = "Bom零件名称")
	@Column(length = 100)
	protected String bsName;
    
    /**
     * 关联工序表
     */
    @ApiModelProperty(name="pkProc",value="工序表")
    @Column
    protected Long pkProc;
    
    @ApiModelProperty(name="proc",hidden=true,value="工序表")
    @ManyToOne
    @JoinColumn(name = "pkProc", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    protected Proc proc;
    
	
	/**
	 * 工序顺序
	 */
	@ApiModelProperty(name = "bsOrder", value = "工序顺序")
	protected int bsOrder;
	
	/**
	 * 机台类型
	 */
	@ApiModelProperty(name = "bsModelType", value = "机台类型")
	@Column(length = 100)
	protected String bsModelType;
	
	/**
	 * 基数
	 */
	@ApiModelProperty(name = "bsRadix", value = "基数")
	@Column(length = 50)
	protected int bsRadix=1;
	
	/**
	 * 人数
	 */
	@ApiModelProperty(name = "bsUserNum", value = "人数")
	@Column(length = 50)
	protected BigDecimal bsUserNum;
	
	/**
	 * 成型周期
	 */
	@ApiModelProperty(name = "bsCycle", value = "成型周期")
	@Column(length = 50)
	protected Integer bsCycle=0;
	
	/**
	 * 工序良率(%)
	 */
	@ApiModelProperty(name = "bsYield", value = "工序良率(%)")
	@Column(length = 50)
	protected Integer bsYield=0;


	/**
	 * 穴数
	 */
	@ApiModelProperty(name = "bsCave", value = "穴数")
	@Column(length = 100)
	protected String bsCave;

	/**
	 * 产能
	 */
	@ApiModelProperty(name = "bsCapacity", value = "产能")
	@Column(length = 100)
	protected String bsCapacity;

	public Long getPkQuote() {
		return pkQuote;
	}

	public void setPkQuote(Long pkQuote) {
		this.pkQuote = pkQuote;
	}

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public Long getPkProc() {
		return pkProc;
	}

	public void setPkProc(Long pkProc) {
		this.pkProc = pkProc;
	}

	public Proc getProc() {
		return proc;
	}

	public void setProc(Proc proc) {
		this.proc = proc;
	}

	

	public String getBsName() {
		return bsName;
	}

	public void setBsName(String bsName) {
		this.bsName = bsName;
	}

	public int getBsOrder() {
		return bsOrder;
	}

	public void setBsOrder(int bsOrder) {
		this.bsOrder = bsOrder;
	}

	public int getBsStatus() {
		return bsStatus;
	}

	public void setBsStatus(int bsStatus) {
		this.bsStatus = bsStatus;
	}

	public int getBsNeed() {
		return bsNeed;
	}

	public void setBsNeed(int bsNeed) {
		this.bsNeed = bsNeed;
	}

	public String getBsType() {
		return bsType;
	}

	public void setBsType(String bsType) {
		this.bsType = bsType;
	}

	public String getBsModelType() {
		return bsModelType;
	}

	public void setBsModelType(String bsModelType) {
		this.bsModelType = bsModelType;
	}

	public int getBsRadix() {
		return bsRadix;
	}

	public void setBsRadix(int bsRadix) {
		this.bsRadix = bsRadix;
	}

	public BigDecimal getBsUserNum() {
		return bsUserNum;
	}

	public void setBsUserNum(BigDecimal bsUserNum) {
		this.bsUserNum = bsUserNum;
	}

	public Integer getBsCycle() {
		return bsCycle;
	}

	public void setBsCycle(Integer bsCycle) {
		this.bsCycle = bsCycle;
	}

	public Integer getBsYield() {
		return bsYield;
	}

	public void setBsYield(Integer bsYield) {
		this.bsYield = bsYield;
	}

	public String getBsCave() {
		return bsCave;
	}

	public void setBsCave(String bsCave) {
		this.bsCave = bsCave;
	}

	public String getBsCapacity() {
		return bsCapacity;
	}

	public void setBsCapacity(String bsCapacity) {
		this.bsCapacity = bsCapacity;
	}
}