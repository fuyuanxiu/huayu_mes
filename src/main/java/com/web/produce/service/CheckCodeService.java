package com.web.produce.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface CheckCodeService {

	public ApiResponseResult getTaskNo(String keyword) throws Exception;

	public ApiResponseResult subCode(String taskNo, String barcode1, String barcode2) throws Exception;
	
	public ApiResponseResult getHistoryList(String keyword, String hStartTime,String hEndTime,PageRequest pageRequest) throws Exception;
	
}
