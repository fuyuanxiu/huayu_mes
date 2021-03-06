package com.web.quote.service;

import com.app.base.data.ApiResponseResult;
import com.web.quote.entity.QuoteFile;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

public interface QuoteFileService {

	//新增产品资料文件
	public ApiResponseResult add(QuoteFile productFile) throws Exception;

	//删除产品资料文件
	public ApiResponseResult delete(Long id) throws Exception;

	//获取产品资料文件列表
	public ApiResponseResult getList(String keyword, String pkQuote, PageRequest pageRequest) throws Exception;

	//确认完成（变更状态bsStatus字段）
	public ApiResponseResult doStatus(String quoteId,String code)throws Exception;

	//取消完成
	public ApiResponseResult cancelStatus(String quoteId,String code)throws Exception;
}
