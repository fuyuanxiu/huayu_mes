package com.web.basePrice.service;

import org.springframework.data.domain.PageRequest;
import com.app.base.data.ApiResponseResult;
import com.web.basePrice.entity.BjWorkCenter;

/**s
 *
 * @date Nov 4, 2020 4:50:15 PM
 */
public interface BjWorkCenterService {

  public ApiResponseResult add(BjWorkCenter workCenter) throws Exception;

  public ApiResponseResult edit(BjWorkCenter workCenter) throws Exception;

  // 根据ID获取详情
  public ApiResponseResult getWorkCenter(Long id) throws Exception;

  public ApiResponseResult delete(Long id) throws Exception;

  public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception;
  
  public ApiResponseResult doStatus(Long id, Integer bsStatus) throws Exception;// 状态改变

}