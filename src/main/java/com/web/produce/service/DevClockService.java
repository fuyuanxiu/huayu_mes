package com.web.produce.service;

import java.util.Date;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.DevClock;

import javax.servlet.http.HttpServletResponse;


public interface DevClockService {
	public ApiResponseResult add(DevClock devClock) throws Exception;

	public ApiResponseResult edit(DevClock devClock) throws Exception;

	// 根据ID获取
	public ApiResponseResult getDevClock(Long id) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

	public void exportList(String keyword, HttpServletResponse response) throws Exception;
	
	public ApiResponseResult getLineList() throws Exception;//获取线体
	
	public ApiResponseResult doEnabled(Long id, Integer enabled) throws Exception;// 是否有效
	
	public ApiResponseResult test(DevClock devClock) throws Exception;
}
