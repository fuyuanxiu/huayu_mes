package com.web.kanban.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.kanban.service.KanbanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "看板")
@ApiIgnore
@CrossOrigin
@ControllerAdvice
// @RestController
@Controller
@RequestMapping(value = "/kanban")
public class kanbanController extends WebController {

	private String module = "看板信息";

	@Autowired
	private KanbanService kanbanService;

	@RequestMapping(value = "/toText", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toText() {
		String method = "/kanban/toText";
		String methodName = "看板text";
		ModelAndView mav = new ModelAndView();
		// mav.addObject("pname", p);
		mav.setViewName("/kanban/text");// 返回路径
		return mav;
	}

	@RequestMapping(value = "/toDemo", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toDemo() {
		String method = "/kanban/toDemo";
		String methodName = "看板demo";
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/kanban/demo");// 返回路径
		return mav;
	}
	
	//看板apk指向此路径-隐藏了部分看板
	@RequestMapping(value = "/toIndex", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toIndex() {
		String method = "/kanban/toIndex";
		String methodName = "看板demo";
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/kanban/index_apk");// 返回路径
		return mav;
	}
	// 显示所有看板
	@RequestMapping(value = "/toIndexAll", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toIndexAll() {
		String method = "/kanban/toIndexAll";
		ModelAndView mav = new ModelAndView();
		mav.setViewName("/kanban/index1");// 返回路径
		return mav;
	}

	//复合看板（多个看板部分数据组装拼接合成
	@RequestMapping(value = "/toCjdzkb", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjdzkb(String inType,String deptId) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjdzkb";
		String methodName = "车间电子看板";
		try {
			ApiResponseResult deptList = kanbanService.getZcblDepList();
			ApiResponseResult interval = kanbanService.getIntervalTime();
			ApiResponseResult cjbg_data = kanbanService.getCjbgList("999", deptId, "", this.getIpAddr());
			ApiResponseResult scdz_data = kanbanService.getScdzList("999", deptId, "", this.getIpAddr());
			ApiResponseResult zxll_data = kanbanService.getZxllList("999", deptId, "", this.getIpAddr());
			mav.addObject("cjbg_data", cjbg_data);// 车间看板数据
			mav.addObject("scdz_data", scdz_data);// 生产电子数据
			mav.addObject("zxll_data", zxll_data);// 在线良率数据
			mav.addObject("deptList", deptList);// 部门列表
			mav.addObject("interval", interval);// 刷新间隔
			mav.addObject("deptId", deptId);// 部门
			mav.addObject("inType", inType);// 显示类型
			mav.setViewName("/kanban/cjdzkb");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("车间电子看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}
	//内容同上-多个部门轮播
	@RequestMapping(value = "/toCjdzkbAll", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjdzkbAll() {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjdzkbAll";
		String methodName = "车间电子看板";
		try {
			ApiResponseResult deptList = kanbanService.getZcblDepList();
			
			Object deptList1 = kanbanService.getZcblDepList().getData();//取首页部门ID数据
			List<Map<String, Object>> lm = (List<Map<String, Object>>) deptList1;
			String tString=lm.get(0).get("ID").toString();
			
			ApiResponseResult interval = kanbanService.getIntervalTime();
			ApiResponseResult cjbg_data = kanbanService.getCjbgList("999", tString, "", this.getIpAddr());
			ApiResponseResult scdz_data = kanbanService.getScdzList("999", tString, "", this.getIpAddr());
			ApiResponseResult zxll_data = kanbanService.getZxllList("999", tString, "", this.getIpAddr());
			mav.addObject("cjbg_data", cjbg_data);// 车间看板数据
			mav.addObject("scdz_data", scdz_data);// 生产电子数据
			mav.addObject("zxll_data", zxll_data);// 生产电子数据
			mav.addObject("deptList", deptList);// 部门列表
			mav.addObject("interval", interval);// 刷新间隔
			mav.setViewName("/kanban/cjdzkb_all");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("车间电子看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toZzdzkb", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toZzdzkb(String inType, String liner,String deptId) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toZzdzkb";
		String methodName = "组长详细电子看板";
		try {
			ApiResponseResult deptList = kanbanService.getCjbgDepList();
			ApiResponseResult interval = kanbanService.getIntervalTime();
			ApiResponseResult linerList = kanbanService.getLiner(deptId);
			// ApiResponseResult xlpm_data =
			// kanbanService.getXlpmList("999","","",this.getIpAddr(),liner);
			ApiResponseResult cxdz_data = kanbanService.getCxdzList("999", deptId, "", this.getIpAddr(), liner);
			// 制令单切换单独获取
			// ApiResponseResult cxsc_data =
			// kanbanService.getCxscList2("","",liner,this.getIpAddr(),"1");
			ApiResponseResult rotation = kanbanService.getRotationTime();
			// mav.addObject("xlpm_data",xlpm_data);//效率排名看板数据
			mav.addObject("cxdz_data", cxdz_data);// 产线电子看板数据
			// mav.addObject("cxsc_data",cxsc_data);//产线生产数据
			mav.addObject("linerList", linerList);// 组长列表
			mav.addObject("nowLiner", liner);// 当前被选择的组长
			mav.addObject("deptList", deptList);// 部门列表
			mav.addObject("deptId", deptId);// 部门
			mav.addObject("rotation", rotation);// 整体页面刷新间隔
			mav.addObject("interval", interval);// 制令单刷新间隔
			mav.addObject("inType", inType);// 显示类型
			mav.setViewName("/kanban/zzdzkb");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取组长详细电子看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	//（复数看板）轮播页面
	@RequestMapping(value = "/toCjkbs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjkbs(String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjkbs";
		String methodName = "车间看板【车间报工+生产电子】";
		try {
			ApiResponseResult rotation = kanbanService.getRotationTime();
			mav.addObject("rotation", rotation);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/cjkbs");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("车间看板【车间报工+生产电子】异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}
	//暂时废弃
	@RequestMapping(value = "/toLtkbs", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toLtkbs(String liner, String inType,String deptId) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toLtkbs";
		String methodName = "获取拉头看板";
		try {
			ApiResponseResult rotation = kanbanService.getRotationTime();
			mav.addObject("rotation", rotation);
			mav.addObject("liner", liner);
			mav.addObject("inType", inType);
			mav.addObject("deptId", deptId);
			mav.setViewName("/kanban/ltkbs");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取拉头看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toSetLiner", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toSetLiner(String inType, String pageType,String deptId) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toSetLiner";
		String methodName = "获取组长数据";
		try {
			ApiResponseResult linerList = kanbanService.getLiner(deptId);
			mav.addObject("linerList", linerList);
			mav.addObject("inType", inType);
			mav.addObject("pageType", pageType);
			mav.addObject("deptId", deptId);
			mav.setViewName("/kanban/setliner");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取组长数据！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}
	
	@RequestMapping(value = "/toSetDept", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toSetDept(String inType, String pageType) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toSetDept";
		String methodName = "获取部门数据";
		try {
			ApiResponseResult deptList = kanbanService.getZcblDepList();
			mav.addObject("deptList", deptList);
			mav.addObject("inType", inType);
			mav.addObject("pageType", pageType);
			mav.setViewName("/kanban/setDept");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取部门数据异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}
	

	// 不带参数的默认获取
	@RequestMapping(value = "/toCjbg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbg() {
		String method = "/kanban/toCjbg";
		String methodName = "车间报工看板";
		ModelAndView mav = new ModelAndView();
		// mav.addObject("pname", p);
		mav.setViewName("/kanban/index1");// 返回路径
		return mav;
	}

	@RequestMapping(value = "/toCjbg1", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbg1(String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjbg1";
		String methodName = "车间报工看板";
		try {
			ApiResponseResult result = kanbanService.getCjbgList("999", "", "", this.getIpAddr());
			ApiResponseResult deptList = kanbanService.getCjbgDepList();
			ApiResponseResult interval = kanbanService.getIntervalTime();
			logger.debug("获取看板=toCjbg1:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("interval", interval);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/cjbg1");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toCjbgDetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCjbgDetail(String liner) {
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toCjbgDetail";
		String methodName = "车间报工看板数据穿透";
		try {
			ApiResponseResult result = kanbanService.getCjbgDetailList(liner, this.getIpAddr());
			logger.debug(methodName + "=toCjbgDetail:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("liner", liner);
			mav.setViewName("/kanban/cjbg_detail");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName + "异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toScdz", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toScdz(String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toScdz";
		String methodName = "生产电子看板";
		try {
			ApiResponseResult result = kanbanService.getScdzList("999", "", "", this.getIpAddr());
			ApiResponseResult deptList = kanbanService.getCjbgDepList();
			ApiResponseResult interval = kanbanService.getIntervalTime();
			logger.debug("获取生产电子看板=toScdz:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("interval", interval);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/scdz");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产电子看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toScdzDetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toScdzDetail(String liner, String fieldword) {// 数据穿透sjct1
		ModelAndView mav = new ModelAndView();
		String method = "/kanban/toScdzDetail";
		String methodName = "生产电子看板数据穿透";
		try {
			ApiResponseResult result = kanbanService.getScdzDetailList(liner, "", this.getIpAddr(), fieldword);
			logger.debug(methodName + "=toScdzDetail:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("liner", liner);
			mav.addObject("fieldword", fieldword);
			mav.setViewName("/kanban/scdz_detail");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName + "异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toZcbl", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toZcbl(String line, String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toZcbl";
		String methodName = "制程不良看板";
		try {
			ApiResponseResult result = kanbanService.getZcblList("999", "", "", this.getIpAddr());
			ApiResponseResult deptList = kanbanService.getZcblDepList();
			ApiResponseResult interval = kanbanService.getIntervalTime();
			logger.debug("制程不良看板=toZcbl:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("interval", interval);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/zcbl");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制程不良看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toXlpm", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toXlpm(String liner, String inType,String deptId) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toXlpm";
		String methodName = "效率排名看板";
		try {
			ApiResponseResult result = kanbanService.getXlpmList("999", "", "", this.getIpAddr(), liner, "");
			ApiResponseResult deptList = kanbanService.getCjbgDepList();
			ApiResponseResult linerList = kanbanService.getLiner(deptId);
			ApiResponseResult interval = kanbanService.getIntervalTime();
			logger.debug("效率排名看板=toXlpm:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("linerList", linerList);
			mav.addObject("nowLiner", liner);
			mav.addObject("interval", interval);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/xlpm");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取效率排名看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toDfg", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toDfg(String inType) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toDfg";
		String methodName = "待返工看板";
		try {
			String usr_id = "";
			SysUser currUser = UserUtil.getSessionUser();
			if (currUser == null) {
				usr_id = "1";
			} else {
				usr_id = currUser.getId().toString();
			}
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ApiResponseResult result = kanbanService.getDfgList("1", "", formatter.format(date), usr_id, "1");// this.getIpAddr()
			ApiResponseResult deptList = kanbanService.getZcblDepList();
			ApiResponseResult interval = kanbanService.getIntervalTime();
			logger.debug("待返工看板=toDfg:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("interval", interval);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/dfg");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取待返工看板看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toCxdz", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCxdz(String liner, String inType,String deptId) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toCxdz";
		String methodName = "产线电子看板";
		try {
			ApiResponseResult result = kanbanService.getCxdzList("", "", "", this.getIpAddr(), liner);// this.getIpAddr()
			ApiResponseResult deptList = kanbanService.getCjbgDepList();
			ApiResponseResult linerList = kanbanService.getLiner(deptId);
			ApiResponseResult interval = kanbanService.getIntervalTime();
			logger.debug("产线电子看板=toCxdz:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("linerList", linerList);
			mav.addObject("interval", interval);
			mav.addObject("nowLiner", liner);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/cxdz");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线电子看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}

	@RequestMapping(value = "/toCxsc", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView toCxsc(String liner, String inType,String deptId) {
		ModelAndView mav = new ModelAndView();
		String method = "kanban/toCxsc";
		String methodName = "产线生产看板";
		try {
			ApiResponseResult result = kanbanService.getCxscList("", "", liner, this.getIpAddr(), "1");// this.getIpAddr()
			ApiResponseResult deptList = kanbanService.getCjbgDepList();
			ApiResponseResult linerList = kanbanService.getLiner(deptId);
			ApiResponseResult interval = kanbanService.getIntervalTime();
			logger.debug("产线生产看板=toCxsc:" + result);
			// getSysLogService().success(module,method,methodName,result);
			mav.addObject("kanbanDataList", result);
			mav.addObject("deptList", deptList);
			mav.addObject("linerList", linerList);
			mav.addObject("interval", interval);
			mav.addObject("nowLiner", liner);
			mav.addObject("inType", inType);
			mav.setViewName("/kanban/cxsc");// 返回路径
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线生产看板异常！", e);
			getSysLogService().error(module, method, methodName, e.toString());
		}
		return mav;
	}
	
	//更新页面数据调用的方法
	// --------------getList-------------------
	@ApiOperation(value = "获取车间报工看板信息", notes = "获取车间报工看板信息", hidden = true)
	@RequestMapping(value = "/getCjbgList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCjbgList(String class_nos, String dep_id, String sdata) {
		String method = "/kanban/getCjbgList";
		String methodName = "获取车间报工看板信息";
		try {
			ApiResponseResult result = kanbanService.getCjbgList(class_nos, dep_id, sdata, this.getIpAddr());
			logger.debug("获取车间报工看板信息=getCjbgList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取车间报工看板信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取车间报工看板信息失败！");
		}
	}

	@ApiOperation(value = "获取车间报工看板数据穿透信息", notes = "获取车间报工看板数据穿透信息", hidden = true)
	@RequestMapping(value = "/getCjbgDetailList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCjbgDetailList(String liner) {
		String method = "/kanban/getCjbgDetailList";
		String methodName = "获取车间报工看板数据穿透信息";
		try {
			ApiResponseResult result = kanbanService.getCjbgDetailList(liner, this.getIpAddr());
			logger.debug(methodName + "getCjbgDetailList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName + "失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure(methodName + "失败！");
		}
	}

	// 带参数的二次获取
	@ApiOperation(value = "获取制程不良看板信息", notes = "获取制程不良看板信息", hidden = true)
	@RequestMapping(value = "/getZcblList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getZcblList(String class_nos, String dep_id, String sdata) {
		String method = "/kanban/getZcblList";
		String methodName = "获取制程不良看板信息";
		try {
			ApiResponseResult result = kanbanService.getZcblList(class_nos, dep_id, sdata, this.getIpAddr());
			logger.debug("获取制程不良看板信息=getZcblList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取制程不良看板信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取制程不良看板信息失败！");
		}
	}

	@ApiOperation(value = "获取生产电子看板信息", notes = "获取生产电子看板信息", hidden = true)
	@RequestMapping(value = "/getScdzList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getScdzList(String class_nos, String dep_id, String sdata) {
		String method = "/kanban/getScdzList";
		String methodName = "获取生产电子看板信息";
		try {
			ApiResponseResult result = kanbanService.getScdzList(class_nos, dep_id, sdata, this.getIpAddr());
			logger.debug("获取生产电子看板信息=getScdzList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取生产电子看板信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取生产电子看板信息失败！");
		}
	}

	@ApiOperation(value = "获取生产电子看板数据穿透信息", notes = "获取生产电子看板数据穿透信息", hidden = true)
	@RequestMapping(value = "/getScdzDetailList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getScdzDetailList(String liner, String dep_id, String fieldword) {// sjct2
																								// 数据穿透
		String method = "/kanban/getScdzDetailList";
		String methodName = "获取生产电子看板数据穿透信息";
		try {
			ApiResponseResult result = kanbanService.getScdzDetailList(liner, dep_id, this.getIpAddr(), fieldword);
			logger.debug(methodName + "=getScdzDetailList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(methodName + "失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure(methodName + "失败！");
		}
	}

	@ApiOperation(value = "获取效率排名看板信息", notes = "获取效率排名看板信息", hidden = true)
	@RequestMapping(value = "/getXlpmList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getXlpmList(String class_nos, String dep_id, String sdata, String liner, String taskno) {
		String method = "/kanban/getXlpmList";
		String methodName = "获取效率排名看板信息";
		try {
			ApiResponseResult result = kanbanService.getXlpmList(class_nos, dep_id, sdata, this.getIpAddr(), liner,
					taskno);
			logger.debug("获取效率排名看板信息=getXlpmList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取效率排名看板信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取效率排名看板信息失败！");
		}
	}

	@ApiOperation(value = "获取待返工看板信息", notes = "获取返工看板信息", hidden = true)
	@RequestMapping(value = "/getDfgList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getDfgList(String class_id, String dep_id, String sdata) {
		String method = "/kanban/getDfgList";
		String methodName = "获取返工看板信息";
		try {
			String usr_id = "";
			SysUser currUser = UserUtil.getSessionUser();
			if (currUser == null) {
				usr_id = "1";
			} else {
				usr_id = currUser.getId().toString();
			}
			ApiResponseResult result = kanbanService.getDfgList(class_id, dep_id, sdata, usr_id, "1");
			logger.debug("获取返工看板信息=getDfgList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取返工看板信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取返工看板信息失败！");
		}
	}

	/**
	 * 获取产线电子看板
	 */
	@ApiOperation(value = "获取产线电子看板信息", notes = "获取产线电子看板信息", hidden = true)
	@RequestMapping(value = "/getCxdzList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCxdzList(String class_nos, String dep_id, String sdata, String liner) {
		String method = "/kanban/getCxdzList";
		String methodName = "获取产线电子看板信息";
		try {
			ApiResponseResult result = kanbanService.getCxdzList(class_nos, dep_id, sdata, this.getIpAddr(), liner);
			logger.debug("获取产线电子看板信息=getCxdzList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线电子看板信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取产线电子看板信息失败！");
		}
	}

	/**
	 * 获取产线生产看板
	 */
	@ApiOperation(value = "获取产线生产看板信息", notes = "获取产线生产看板信息", hidden = true)
	@RequestMapping(value = "/getCxscList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCxscList(String taskNo, String deptId, String liner, String interval) {
		String method = "/kanban/getCxscList";
		String methodName = "获取产线生产看板信息";
		try {
			ApiResponseResult result = kanbanService.getCxscList(taskNo, deptId, liner, this.getIpAddr(), "1");
			logger.debug("获取产线生产看板信息=getCxscList:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取产线生产看板信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取产线生产看板信息失败！");
		}
	}

	@ApiOperation(value = "获取组长详细看板时间段信息", notes = "获取组长详细看板时间段信息", hidden = true)
	@RequestMapping(value = "/getCxscList2", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getCxscList2(String taskNo, String deptId, String liner, String interval) {
		String method = "/kanban/getCxscList2";
		String methodName = "获取组长详细看板时间段信息";
		try {
			ApiResponseResult result = kanbanService.getCxscList2(taskNo, deptId, liner, this.getIpAddr(), "1");
			logger.debug("获取组长详细看板时间段信息=getCxscList2:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取组长详细看板时间段信息失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取组长详细看板时间段信息失败！");
		}
	}
	
	@ApiOperation(value = "获取组长详细看板达成率表格", notes = "获取组长详细看板达成率表格", hidden = true)
	@RequestMapping(value = "/getFinishRate", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult getFinishRate(String taskNo, String deptId, String liner, String interval) {
		String method = "/kanban/getFinishRate";
		String methodName = "获取组长详细看板达成率表格";
		try {
			ApiResponseResult result = kanbanService.getFinishRate(taskNo, deptId, liner, this.getIpAddr(), "1");
			logger.debug("获取组长详细看板达成率表格=getFinishRate:" + result);
			// getSysLogService().success(module,method, methodName, null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取组长详细看板达成率表格失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取组长详细看板达成率表格失败！");
		}
	}
	// 复合看板（多个看板部分数据组装拼接合成
	/**
	 * 获取看板数据
	 */
	@ApiOperation(value = "获取车间电子看板数据", notes = "获取车间电子看板数据", hidden = true)
	@RequestMapping(value = "/toCjdzkbList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult toCjdzkbList(String class_nos, String dep_id, String sdata) {
		String method = "/kanban/toCjdzkbList";
		String methodName = "获取车间电子看板数据";
		try {
			ApiResponseResult cjbg_data = kanbanService.getCjbgList(class_nos, dep_id, sdata, this.getIpAddr());
			ApiResponseResult scdz_data = kanbanService.getScdzList(class_nos, dep_id, sdata, this.getIpAddr());
			ApiResponseResult zxll_data = kanbanService.getZxllList(class_nos, dep_id, sdata, this.getIpAddr());
			Map map = new HashMap();
			map.put("cjbg_data", cjbg_data);
			map.put("scdz_data", scdz_data);
			map.put("zxll_data", zxll_data);
			return ApiResponseResult.success().data(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取车间电子看板数据失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取车间电子看板数据失败！");
		}
	}

	@ApiOperation(value = "组长详细电子看板数据", notes = "组长详细电子看板数据", hidden = true)
	@RequestMapping(value = "/toZzdzkbList", method = RequestMethod.GET)
	@ResponseBody
	public ApiResponseResult toZzdzkbList(String class_nos, String taskNo, String deptId, String liner, String sdata,
			String dep_id) {
		String method = "/kanban/toZzdzkbList";
		String methodName = "获取组长详细电子看板数据";
		try {
			// ApiResponseResult xlpm_data =
			// kanbanService.getXlpmList(class_nos, dep_id,
			// sdata,this.getIpAddr(),liner);
			ApiResponseResult cxdz_data = kanbanService.getCxdzList(class_nos, dep_id, sdata, this.getIpAddr(), liner);
			// 制令单切换他处获取
			// ApiResponseResult cxsc_data = kanbanService.getCxscList(taskNo,
			// deptId, liner,this.getIpAddr(),"1");
			Map map = new HashMap();
			// map.put("xlpm_data", xlpm_data);
			map.put("cxdz_data", cxdz_data);
			// map.put("cxsc_data", cxsc_data);
			return ApiResponseResult.success().data(map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取组长详细电子看板数据失败！", e);
			getSysLogService().error(module, method, methodName, e.toString());
			return ApiResponseResult.failure("获取组长详细电子看板数据失败！");
		}
	}
}
