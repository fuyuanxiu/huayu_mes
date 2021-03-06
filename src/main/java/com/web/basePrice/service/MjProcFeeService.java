package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.MjProcFee;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @date Nov 4, 2020 4:27:53 PM
 */
public interface MjProcFeeService {

	public ApiResponseResult add(MjProcFee mjProcFee) throws Exception;

	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;

	public ApiResponseResult edit(MjProcFee mjProcFee) throws Exception;

	public ApiResponseResult delete(Long id) throws Exception;

	public ApiResponseResult getFileList(Long customId) throws Exception;

	public ApiResponseResult delFile(Long id,Long fileId) throws Exception;

	public ApiResponseResult doExcel(MultipartFile[] file) throws Exception;

	public void exportExcel(HttpServletResponse response, String keyword) throws Exception;
}