package com.web.basic.controller;

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
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.Process;
import com.web.basic.service.ProcessService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "工序信息模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "base/proc")
public class ProcessController extends WebController{

	private String module = "工序信息";

	 @Autowired
	 private ProcessService processService;
	 
	 @ApiOperation(value = "工序基础信息表结构", notes = "工序基础信息表结构"+Process.TABLE_NAME)
	    @RequestMapping(value = "/getProcess", method = RequestMethod.GET)
		@ResponseBody
	    public Process getProcess(){
	        return new Process();
	    }
	 
	 @ApiOperation(value = "工序列表页", notes = "工序列表页", hidden = true)
	    @RequestMapping(value = "/toProcess")
	    public String toProcess(){
	        return "/web/basic/proc";
	    }
	    @ApiOperation(value = "获取工序列表", notes = "获取工序列表", hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword,String procNo,String procName,String procOrder,
										 String checkStatus,String createDate,String lastupdateDate) {
	        String method = "base/proc/getList";String methodName ="获取工序列表";
	        try {

	            Sort sort = new Sort(Sort.Direction.ASC, "procOrder");
	            ApiResponseResult result = processService.getList(keyword,procNo,procName,procOrder,checkStatus,
						createDate,lastupdateDate,super.getPageRequest(sort));
	            logger.debug("获取工序列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取工序列表失败！", e);
	            getSysLogService().error(module,method, methodName, keyword+":"+e.toString());
	            return ApiResponseResult.failure("获取工序列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增工序", notes = "新增工序", hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody Process proc) {
	        String method = "base/proc/add";String methodName ="新增工序";
	        try{
	            ApiResponseResult result = processService.add(proc);
	            logger.debug("新增工序=add:");
	            getSysLogService().success(module,method, methodName, proc.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("工序新增失败！", e);
	            getSysLogService().error(module,method, methodName,proc.toString()+":"+ e.toString());
	            return ApiResponseResult.failure("工序新增失败！");
	        }
	    }
	    
	    @ApiOperation(value = "编辑工序", notes = "编辑工序", hidden = true)
	    @RequestMapping(value = "/edit", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult edit(@RequestBody Process proc){
	        String method = "base/proc/edit";String methodName ="编辑工序";
	        try{
	            ApiResponseResult result = processService.edit(proc);
	            logger.debug("编辑工序=edit:");
	            getSysLogService().success(module,method, methodName, proc.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("编辑工序失败！", e);
	            getSysLogService().error(module,method, methodName, proc.toString()+":"+e.toString());
	            return ApiResponseResult.failure("编辑工序失败！");
	        }
	    }
		@ApiOperation(value = "根据ID获取工序", notes = "根据ID获取工序", hidden = true)
	    @RequestMapping(value = "/getProcess", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getProcess(@RequestBody Map<String, Object> params){
	        String method = "base/proc/getProcess";String methodName ="根据ID获取工序";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = processService.getProcess(id);
	            logger.debug("根据ID获取工序=getProcess:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取工序失败！", e);
	            getSysLogService().error(module,method, methodName,params+":"+ e.toString());
	            return ApiResponseResult.failure("获取工序失败！");
	        }
	    }
		
		@ApiOperation(value = "删除工序", notes = "删除工序", hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "base/proc/delete";String methodName ="删除工序";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = processService.delete(id);
	            logger.debug("删除工序=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除工序失败！", e);
	            getSysLogService().error(module,method, methodName,params+":"+ e.toString());
	            return ApiResponseResult.failure("删除工序失败！");
	        }
	    }
		
		 @ApiOperation(value = "设置正常/禁用", notes = "设置正常/禁用", hidden = true)
		    @RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		    @ResponseBody
		    public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) throws Exception{
			 //Long id, Integer deStatus
		        String method = "base/proc/doStatus";String methodName ="设置正常/禁用";
		        try{
		        	long id = Long.parseLong(params.get("id").toString()) ;
		        	Integer bsStatus=Integer.parseInt(params.get("checkStatus").toString());
		            ApiResponseResult result = processService.doStatus(id, bsStatus);
		            logger.debug("设置正常/禁用=doJob:");
		            getSysLogService().success(module,method, methodName, params);
		            return result;
		        }catch (Exception e){
		            e.printStackTrace();
		            logger.error("设置正常/禁用失败！", e);
		            getSysLogService().error(module,method, methodName, params+":"+e.toString());
		            return ApiResponseResult.failure("设置正常/禁用失败！");
		        }
		    }
}
