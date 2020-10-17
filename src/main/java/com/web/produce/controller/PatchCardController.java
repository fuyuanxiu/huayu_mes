package com.web.produce.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.produce.entity.PatchCard;
import com.web.produce.entity.DevClock;
import com.web.produce.service.PatchCardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "补卡 数据模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "produce/patch")
public class PatchCardController extends WebController{

	 @Autowired
	 private PatchCardService patchCardService;
	 
	 @ApiOperation(value = "补卡 数据表结构", notes = "补卡 数据表结构"+PatchCard.TABLE_NAME)
	    @RequestMapping(value = "/getPatchCard", method = RequestMethod.GET)
		@ResponseBody
	    public PatchCard getPatchCard(){
	        return new PatchCard();
	    }
	  
	 @ApiOperation(value = "补卡 数据列表页", notes = "补卡 数据列表页", hidden = true)
	    @RequestMapping(value = "/toPatchCard")
	    public String toPatchCard(){
	        return "/web/produce/dev_clock/patch";
	    }
	 
	    @ApiOperation(value = "获取补卡 数据列表", notes = "获取补卡 数据列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword) {
	        String method = "produce/patch/getList";String methodName ="获取补卡 数据列表";
	        try {
	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = patchCardService.getList(keyword, super.getPageRequest(sort));
	            logger.debug("获取补卡 数据列表=getList:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取补卡 数据列表失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取补卡 数据列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增补卡数据记录", notes = "新增补卡数据记录",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody PatchCard patchCard) {
	        String method = "produce/patch/add";String methodName ="新增补卡数据记录";
	        try{
	            ApiResponseResult result = patchCardService.add(patchCard);
	            logger.debug("新增补卡数据记录=add:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("补卡数据记录新增失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("补卡数据记录新增失败！");
	        }
	    }
	    @ApiOperation(value = "根据ID获取补卡数据记录", notes = "根据ID获取补卡数据记录",hidden = true)
	    @RequestMapping(value = "/getPatchCard", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getPatchCard(@RequestBody Map<String, Object> params){
	        String method = "produce/patch/getPatchCard";String methodName ="根据ID获取补卡数据记录";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = patchCardService.getPatchCard(id);
	            logger.debug("根据ID获取补卡数据记录=getPatchCard:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取补卡数据记录失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("获取补卡数据记录失败！");
	        }
	    }
		
		@ApiOperation(value = "删除补卡数据记录", notes = "删除补卡数据记录",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "produce/patch/delete";String methodName ="删除补卡数据记录";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = patchCardService.delete(id);
	            logger.debug("删除补卡数据记录=delete:");
	            getSysLogService().success(method, methodName, null);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除补卡数据记录失败！", e);
	            getSysLogService().error(method, methodName, e.toString());
	            return ApiResponseResult.failure("删除补卡数据记录失败！");
	        }
	    }	
}
