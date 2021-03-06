package com.web.produce.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.web.produce.entity.DevClock;

import java.util.List;


public interface DevClockDao extends CrudRepository<DevClock, Long>,JpaSpecificationExecutor<DevClock>{

	public List<DevClock> findAll();
	public List<DevClock> findByDelFlag(Integer delFlag);
	public DevClock findById(long id);
	public int countByDelFlagAndDevCode(Integer delFlag, String devCode);//查询devCode是否存在
	public int countByDelFlagAndDevName(Integer delFlag, String devName);//查询devName是否存在
	public int countByDelFlagAndDevIp(Integer delFlag, String devIp);//查询devIp是否存在

	public List<DevClock> findByDelFlagAndLineIdAndDevTypeAndEnabled(Integer delFlag,Long lid,String devType,Integer enabled);

	public List<DevClock> findByDelFlagAndDevCode(Integer delFlag, String devCode);

	public List<DevClock> findByDelFlagAndIsOnline(Integer delFlag, Integer isOnLine);
}
