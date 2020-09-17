package com.web.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.app.base.control.WebController;
import com.app.base.data.ApiResponseResult;
import com.web.report.service.CommonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "看板")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "/report/comm")
public class CommonController extends WebController {

    @Autowired
    private CommonService commonService;
	
    @ApiOperation(value = "通用报表页面", notes = "通用报表页面", hidden = true)
	@RequestMapping(value = "/toCom", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCom(String pro) {
		ModelAndView mav=new ModelAndView();
		mav.addObject("pro", pro);
		mav.setViewName("/web/report/common");//返回路径
		return mav;
	}
    
    @ApiOperation(value = "获取配置列表", notes = "获取配置列表")
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    @ResponseBody
    public ApiResponseResult getlist(@RequestParam(value = "keyword", required = false) String keyword,
    		@RequestParam(value = "pro_name", required = false) String pro_name) {
        String method = "/getlist";String methodName ="获取配置列表";
        try {
        	Sort sort = new Sort(Sort.Direction.DESC, "id");
            ApiResponseResult result = commonService.getlist(keyword,  super.getPageRequest(sort));
            logger.debug("获取配置列表=getList:");
            getSysLogService().success(method, methodName, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取配置列表失败！", e);
            getSysLogService().error(method, methodName, e.toString());
            return ApiResponseResult.failure("获取配置列表失败！");
        }
    }
    
}
