package com.web.produce.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import com.web.produce.service.VerifyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "上线确认")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "/verify")
public class VerifyController extends WebController {

    private String module = "上线确认信息";
	 @Autowired
	 private VerifyService verifyService;

    @ApiOperation(value = "上线确认页", notes = "上线确认页", hidden = true)
    @RequestMapping(value = "/toVerifyAdd")
    public String toProductAdd(){
        return "/web/produce/verify/verify_add";
    }

    @ApiOperation(value = "上线添加页", notes = "上线添加页", hidden = true)
    @RequestMapping(value = "/toVerifyForm")
    public String toProductForm(){
        return "/web/produce/verify/verify_form";
    }

    @ApiOperation(value="获取指令单信息", notes="获取指令单信息", hidden = true)
    @RequestMapping(value = "/getTaskNo", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getTaskNo(String keyword) {
        String method = "/verify/getTaskNo";String methodName ="获取指令单信息";
        try {
            ApiResponseResult result = verifyService.getTaskNo(keyword);
            logger.debug("获取指令单信息=getTaskNo:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("获取指令单信息失败！", e);
            getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
             return ApiResponseResult.failure("获取指令单信息失败！");
        }
    }


    
    @ApiOperation(value="加载页面默认数据", notes="加载页面默认数据", hidden = true)
    @RequestMapping(value = "/getInfoAdd", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getInfoAdd() {
        String method = "/verify/getInfoAdd";String methodName ="加载页面默认数据";
        try {
            ApiResponseResult result = verifyService.getInfoAdd();
            logger.debug("加载页面默认数据=getInfoAdd:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("加载页面默认数据失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("加载页面默认数据失败！");
        }
    }
    
    @ApiOperation(value="根据产线id获取未分配人员", notes="根据产线id获取未分配人员", hidden = true)
    @RequestMapping(value = "/getUserByLine", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getUserByLine(String lineId) {
        String method = "/verify/getUserByLine";String methodName ="根据产线id获取未分配人员";
        try {
            ApiResponseResult result = verifyService.getUserByLine(lineId,super.getPageRequest());
            logger.debug("根据产线id获取未分配人员=afterNei:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("根据产线id获取未分配人员失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("根据产线id获取未分配人员失败！");
        }
    }

    @ApiOperation(value = "上线确认", notes = "上线确认",hidden = true)
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult save(@RequestBody Map<String, Object> params){
        String method = "verify/save";String methodName ="上线确认";
        String task_no = params.get("task_no").toString();
        String line_id = params.get("line_id").toString();
		String hour_type = params.get("hour_type").toString();
		String class_id = params.get("class_id").toString();
		String wdate = params.get("wdate").toString();
		String emp_ids = params.get("emp_ids").toString();
		emp_ids = emp_ids.substring(0,emp_ids.length() - 1);
        try{
            ApiResponseResult result = verifyService.save(task_no,line_id,hour_type,class_id,wdate,emp_ids);
            logger.debug("上线确认=save:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("上线确认失败！", e);
            getSysLogService().error(module,method, methodName,params+ e.toString());
            return ApiResponseResult.failure("上线确认失败！"+e.toString());
        }
    }
    
    @ApiOperation(value="加载在线返工页面默认数据", notes="加载在线返工页面默认数据", hidden = true)
    @RequestMapping(value = "/getInfoCreateReturn", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getInfoCreateReturn() {
        String method = "/verify/getInfoCreateReturn";String methodName ="加载在线返工页面默认数据";
        try {
            ApiResponseResult result = verifyService.getInfoCreateReturn();
            logger.debug("加载在线返工页面默认数据=getInfoCreateReturn:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("加载在线返工页面默认数据失败！", e);
             getSysLogService().error(module,method, methodName, e.toString());
             return ApiResponseResult.failure("加载在线返工页面默认数据失败！");
        }
    }

    @ApiOperation(value="根据条码查询条码信息查询返工料号和物料", notes="根据条码查询条码信息查询返工料号和物料", hidden = true)
    @RequestMapping(value = "/getInfoBarcode", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getInfoBarcode(String barcode) {
        String method = "/verify/getInfoBarcode";String methodName ="根据条码查询条码信息查询返工料号和物料";
        String param ="条码:"+ barcode;
        try {
            ApiResponseResult result = verifyService.getInfoBarcode(barcode);
            logger.debug("根据条码查询条码信息查询返工料号和物料=getInfoBarcode:");
//            getSysLogService().success(module,method, methodName, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("根据条码查询条码信息查询返工料号和物料失败！", e);
            getSysLogService().error(module,method, methodName,param+";"+ e.toString());
            return ApiResponseResult.failure("根据条码查询条码信息查询返工料号和物料失败！");
        }
    }
    
    @ApiOperation(value="加载在线返工页面-返工料号", notes="加载在线返工页面-返工料号数据", hidden = true)
    @RequestMapping(value = "/getReworkItem", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getReworkItem(String keyword) {
        String method = "/verify/getReworkItem";String methodName ="加载在线返工页面-返工料号数据";
        try {
            ApiResponseResult result = verifyService.getReworkItem(keyword,super.getPageRequest());
            logger.debug("加载在线返工页面-返工料号数据=getReworkItem:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
        	 e.printStackTrace();
             logger.error("加载在线返工页面-返工料号数据失败！", e);
            getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
             return ApiResponseResult.failure("加载在线返工页面-返工料号数据失败！");
        }
    }
    
    @ApiOperation(value = "创建返工制令单", notes = "创建返工制令单",hidden = true)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResponseResult add(@RequestBody Map<String, Object> params){
        String method = "verify/add";String methodName ="创建返工制令单";
        String task_no = params.get("task_no").toString();
        String item_no = params.get("item_no").toString();
		String liner_name = params.get("liner_name").toString();
		String qty = params.get("qty").toString();
		String pdate = params.get("pdate").toString();
        String deptId = params.get("deptId").toString();
        String classId = params.get("classId").toString();
        try{
            ApiResponseResult result = verifyService.add(task_no,item_no,liner_name,qty,pdate,deptId,classId);
            logger.debug("创建返工制令单=add:");
            getSysLogService().success(module,method, methodName, params);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            logger.error("创建返工制令单失败！", e);
            getSysLogService().error(module,method, methodName,params+ e.toString());
            return ApiResponseResult.failure("创建返工制令单失败！");
        }
    }
    
    @ApiOperation(value = "获取历史列表", notes = "获取历史列表")
    @RequestMapping(value = "/getHistoryList", method = RequestMethod.GET)
    @ResponseBody
	public ApiResponseResult getHistoryList(
			@RequestParam(value = "hkeywork", required = false) String hkeywork,
			@RequestParam(value = "hStartTime", required = false) String hStartTime,
			@RequestParam(value = "hEndTime", required = false) String hEndTime){
	  String method = "verify/getHistoryList";String methodName ="获取历史列表";
        try {
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result =verifyService.getHistoryList(hkeywork,hStartTime,hEndTime, super.getPageRequest(sort));
            logger.debug(methodName+"=getList:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(methodName+"失败！", e);
            getSysLogService().error(module,method, methodName, e.toString());
            return ApiResponseResult.failure(methodName+"失败！");
        }
	}

    @ApiOperation(value="获取在线返工统一上线时间", notes="获取在线返工统一上线时间", hidden = true)
    @RequestMapping(value = "/getOnTime", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getOnTime() {
        String method = "/verify/getOnTime";String methodName ="获取在线返工统一上线时间";
        try {
            ApiResponseResult result = verifyService.getOnTimeByPrc(super.getPageRequest());
            logger.debug("获取在线返工统一上线时间=getOnTime:");
//            getSysLogService().success(module,method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取在线返工统一上线时间失败！", e);
            getSysLogService().error(module,method, methodName,"");
            return ApiResponseResult.failure("获取在线返工统一上线时间失败！");
        }
    }

}
