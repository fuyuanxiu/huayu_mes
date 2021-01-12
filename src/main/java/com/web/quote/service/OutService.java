package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.ProductMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface OutService {
	
	public ApiResponseResult getList(String keyword,String bsStatus,PageRequest pageRequest) throws Exception;

	public ApiResponseResult getQuoteList(String keyword,String quoteId,PageRequest pageRequest) throws Exception;

	public ApiResponseResult edit(ProductMater productMater) throws Exception;

	public void exportExcel(HttpServletResponse response, Long quoteId) throws Exception;

	public ApiResponseResult doExcel(MultipartFile[] file, Long quoteId) throws Exception;
}
