package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.HardwareMater;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface HardwareService {

	public ApiResponseResult add(HardwareMater hardwareMater)throws Exception;

	public ApiResponseResult edit(HardwareMater hardwareMater)throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult doExcel(MultipartFile[] file) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

    public ApiResponseResult getListByPkQuote(Long pkQuote, PageRequest pageRequest) throws Exception;
}
