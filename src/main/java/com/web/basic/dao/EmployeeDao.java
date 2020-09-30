package com.web.basic.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

import com.web.basic.entity.Employee;;

public interface EmployeeDao extends CrudRepository<Employee, Long>,JpaSpecificationExecutor<Employee>{

	public List<Employee> findAll();
	public List<Employee> findByDelFlag(Integer delFlag);
	public Employee findById(long id);
	public int countByDelFlagAndEmpCode(Integer delFlag, String empCode);//查询是否存在
}