package com.web.report.controller;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.report.service.RptUpphService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Api(description = "各部UPPH日报表")
@CrossOrigin
@ControllerAdvice
@Controller
@RequestMapping(value = "report/rpt_upph")
public class rptUpphController extends WebController {

	private String module = "各部UPPH日报表";

	@Autowired
	private RptUpphService rptUpphService;

	@ApiOperation(value = "各部UPPH日报表", notes = "各部UPPH日报表", hidden = true)
	@RequestMapping(value = "/toRptUpph", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toProcDay() {
		ModelAndView mav = new ModelAndView();
//		mav.addObject("Depart", this.getDeptInfo(""));
		mav.setViewName("/web/report/rpt_upph");// 返回路径
		return mav;
	}

//	@ApiOperation(value = "获取部门信息", notes = "获取部门信息", hidden = true)
//	@RequestMapping(value = "/getDeptInfo", method = RequestMethod.GET)
//	@ResponseBody
//	public ApiResponseResult getDeptInfo(String keyword) {
//		String method = "report/rpt_upph/getDeptInfo";
//		String methodName = "获取部门信息";
//		try {
//			ApiResponseResult result = rptUpphService.getDeptInfo(keyword);
//			logger.debug("获取部门信息=getDeptInfo:");
////			getSysLogService().success(module, method, methodName, null);
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("获取部门信息失败！", e);
//			getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
//			return ApiResponseResult.failure("获取部门信息失败！");
//		}
//	}
	

	
	@ApiOperation(value = "获取各部UPPH日报表", notes = "获取各部UPPH日报表", hidden = true)
	@RequestMapping(value = "/getReport", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getReport(@RequestParam(value = "dates", required = false) String dates,
                                       @RequestParam(value = "keyword", required = false) String keyword) {
		String method = "report/rpt_upph/getReport";
		String methodName = "获取各部UPPH日报表";
		String param = "日期:"+dates ;
//				+";部门Id:" +deptId +";制令单："+itemNo;
		try {
			String[] date = {"",""};
			if(StringUtils.isNotEmpty(dates)){
				date = dates.split(" - ");
			}
			ApiResponseResult result = rptUpphService.getReport(date[0],date[1],
                    keyword);
			logger.debug(methodName+"=getReport:");
//			getSysLogService().success(module, method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName+"失败！", e);
			getSysLogService().error(module, method, methodName, param+e.toString());
			return ApiResponseResult.failure(methodName+"失败！");
		}
	}

}
