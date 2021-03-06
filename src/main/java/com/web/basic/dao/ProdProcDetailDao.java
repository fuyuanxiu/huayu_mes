package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.ProdProcDetail;

public interface ProdProcDetailDao extends CrudRepository<ProdProcDetail, Long>,JpaSpecificationExecutor<ProdProcDetail>{
	
	public List<ProdProcDetail> findAll();
	//public List<ProdProcDetai> findByDelFlag(Integer delFlag);
	public ProdProcDetail findById(long id);
	
	public List<ProdProcDetail> findByDelFlagAndItemIdAndProcOrder(Integer delFlag,Long itemId,Integer order);
	
	@Modifying
    @Query("update ProdProcDetail t set t.delFlag=1 where t.itemId=?1 and t.delFlag=0")
    public void delteProdProcDetailByItemIdAnd(Long itemId);
}
