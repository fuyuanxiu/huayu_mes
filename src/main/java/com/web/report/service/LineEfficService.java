package com.web.report.service;

import org.springframework.data.domain.PageRequest;

import com.app.base.data.ApiResponseResult;

public interface LineEfficService {

	public ApiResponseResult getList(String keyword,String sdate,String edate) throws Exception;
}
