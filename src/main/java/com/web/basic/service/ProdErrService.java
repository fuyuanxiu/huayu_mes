package com.web.basic.service;

import com.app.base.data.ApiResponseResult;
import com.web.basic.entity.ProdErr;
import org.springframework.data.domain.PageRequest;

public interface ProdErrService {
	public ApiResponseResult add(ProdErr prodErr) throws Exception;

	public ApiResponseResult edit(ProdErr prodErr) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getProdErr(Long id) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
}
