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
import com.web.produce.entity.CardData;
import com.web.produce.entity.DevClock;
import com.web.produce.service.CardDataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "卡点原始数据模块")
@CrossOrigin
@ControllerAdvice
//@RestController
@Controller
@RequestMapping(value = "produce/card_data")
public class CardDataController extends WebController{

	private String module = "卡点原始数据";

	 @Autowired
	 private CardDataService cardDataService;
	 
	 @ApiOperation(value = "卡点原始数据表结构", notes = "卡点原始数据表结构"+CardData.TABLE_NAME)
	    @RequestMapping(value = "/getCardData", method = RequestMethod.GET)
		@ResponseBody
	    public CardData getCardData(){
	        return new CardData();
	    }
	  
	 @ApiOperation(value = "卡点原始数据列表页", notes = "卡点原始数据列表页", hidden = true)
	    @RequestMapping(value = "/toCardData")
	    public String toCardData(){
	        return "/web/produce/dev_clock/card_data";
	    }
	 
	    @ApiOperation(value = "获取卡点原始数据列表", notes = "获取卡点原始数据列表",hidden = true)
	    @RequestMapping(value = "/getList", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult getList(String keyword,String status) {
	        String method = "produce/card_data/getList";String methodName ="获取卡点原始数据列表";
	        try {
//	        	System.out.println(keyword);
	            Sort sort = new Sort(Sort.Direction.DESC, "id");
	            ApiResponseResult result = cardDataService.getList(keyword, status,super.getPageRequest(sort));
	            logger.debug("获取卡点原始数据列表=getList:");
//	            getSysLogService().success(module,method, methodName, keyword);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取卡点原始数据列表失败！", e);
				getSysLogService().error(module,method, methodName,"关键字"+keyword==null?";":keyword+";"+e.toString());
	            return ApiResponseResult.failure("获取卡点原始数据列表失败！");
	        }
	    }
	    
	    
	    @ApiOperation(value = "新增卡点数据记录", notes = "新增卡点数据记录",hidden = true)
	    @RequestMapping(value = "/add", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult add(@RequestBody CardData cardData) {
	        String method = "produce/card_data/add";String methodName ="新增卡点数据记录";
	        try{
	            ApiResponseResult result = cardDataService.add(cardData);
	            logger.debug("新增卡点数据记录=add:");
	            getSysLogService().success(module,method, methodName, cardData.toString());
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("卡点数据记录新增失败！", e);
	            getSysLogService().error(module,method, methodName,cardData.toString()+";"+ e.toString());
	            return ApiResponseResult.failure("卡点数据记录新增失败！");
	        }
	    }
	    @ApiOperation(value = "根据ID获取卡点数据记录", notes = "根据ID获取卡点数据记录",hidden = true)
	    @RequestMapping(value = "/getCardData", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getCardData(@RequestBody Map<String, Object> params){
	        String method = "produce/card_data/getCardData";String methodName ="根据ID获取卡点数据记录";
	        long id = Long.parseLong(params.get("id").toString()) ;
	        try{
	            ApiResponseResult result = cardDataService.getCardData(id);
	            logger.debug("根据ID获取卡点数据记录=getCardData:");
//	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch (Exception e){
	            e.printStackTrace();
	            logger.error("根据ID获取卡点数据记录失败！", e);
	            getSysLogService().error(module,method, methodName, params+";"+e.toString());
	            return ApiResponseResult.failure("获取卡点数据记录失败！");
	        }
	    }
		
		@ApiOperation(value = "删除卡点数据记录", notes = "删除卡点数据记录",hidden = true)
	    @RequestMapping(value = "/delete", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult delete(@RequestBody Map<String, Object> params){
	        String method = "produce/card_data/delete";String methodName ="删除卡点数据记录";
	        try{
	        	long id = Long.parseLong(params.get("id").toString()) ;
	            ApiResponseResult result = cardDataService.delete(id);
	            logger.debug("删除卡点数据记录=delete:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        }catch(Exception e){
	            e.printStackTrace();
	            logger.error("删除卡点数据记录失败！", e);
	            getSysLogService().error(module,method, methodName,params+":"+ e.toString());
	            return ApiResponseResult.failure("删除卡点数据记录失败！");
	        }
	    }
		
		@ApiOperation(value = "获取人员信息列表", notes = "获取人员信息列表", hidden = true)
	    @RequestMapping(value = "/getEmp", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getEmp() {
	        String method = "produce/card_data/getEmp";String methodName ="获取人员信息列表";
	        try {
	            ApiResponseResult result = cardDataService.getEmp();
	            logger.debug("获取人员信息列表=getEmp:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取人员信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取人员信息列表失败！");
	        }
	    }
		@ApiOperation(value = "获取卡机信息列表", notes = "获取卡机信息列表", hidden = true)
	    @RequestMapping(value = "/getDev", method = RequestMethod.POST)
	    @ResponseBody
	    public ApiResponseResult getDev() {
	        String method = "produce/card_data/getDev";String methodName ="获取卡机信息列表";
	        try {
	            ApiResponseResult result = cardDataService.getDev();
	            logger.debug("获取卡机信息列表=getDev:");
//	            getSysLogService().success(module,method, methodName, null);
	            return result;
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("获取卡机信息列表失败！", e);
	            getSysLogService().error(module,method, methodName, e.toString());
	            return ApiResponseResult.failure("获取卡机信息列表失败！");
	        }
	    }
		
		
		@ApiOperation(value="手动更新卡机打卡数据", notes="手动更新卡机打卡数据", hidden = true)
	    @RequestMapping(value = "/updateData", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult updateData(String devIds,String stype) {
	        String method = "/product/card_data/updateData";String methodName ="手动更新卡机打卡数据";
	        try {
	            ApiResponseResult result = cardDataService.updateData(devIds,stype);
	            logger.debug("手动更新卡机打卡数据=getHXTaskNo:");
	            getSysLogService().success(module,method, methodName, "卡机id:"+devIds);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("手动更新卡机打卡数据失败！", e);
	             getSysLogService().error(module,method, methodName, "卡机id:"+devIds+";"+e.toString());
	             return ApiResponseResult.failure("手动更新卡机打卡数据失败！");
	        }
	    }
		
		@ApiOperation(value="根据产线更新指纹机信息", notes="根据产线更新指纹机信息", hidden = true)
	    @RequestMapping(value = "/updateDataByLine", method = RequestMethod.GET)
	    @ResponseBody
	    public ApiResponseResult updateDataByLine(String line_ids) {
	        String method = "/product/card_data/updateDataByLine";String methodName ="根据产线更新指纹机信息";
	        try {
	            ApiResponseResult result = cardDataService.updateDataByLine(line_ids);
	            logger.debug("根据产线更新指纹机信息=updateDataByLine:");
	            getSysLogService().success(module,method, methodName, "产线id;"+line_ids);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("根据产线更新指纹机信息失败！", e);
	             getSysLogService().error(module,method, methodName,"产线id;"+line_ids+";"+ e.toString());
	             return ApiResponseResult.failure("根据产线更新指纹机信息失败！");
	        }
	    }
		
		@ApiOperation(value="设置卡点状态",notes="设置卡点状态",hidden=true)
		@RequestMapping(value = "/doStatus", method = RequestMethod.POST)
		@ResponseBody
		public ApiResponseResult doStatus(@RequestBody Map<String, Object> params) {
			String method = "/produce/card_data/doStatus";String methodName ="设置卡点状态";
			try {
				String ids = params.get("id").toString() ;
//				Integer fstatus=Integer.parseInt(params.get("checkStatus").toString());
	            ApiResponseResult result = cardDataService.doStatus(ids,0);
	            logger.debug("根据产线更新指纹机信息=doStatus:");
	            getSysLogService().success(module,method, methodName, params);
	            return result;
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error("设置卡点状态失败！", e);
	             getSysLogService().error(module,method, methodName,params+";"+ e.toString());
	             return ApiResponseResult.failure("设置卡点状态失败！");
	        }
			
		}
		
		@ApiOperation(value="导出数据", notes="导出数据", hidden = true)
	    @RequestMapping(value = "/doExport", method = RequestMethod.GET)
	    @ResponseBody
	    public void  doExport(String keywork) {
	        String method = "/product/card_data/doExport";String methodName ="导出数据";
	        try {
	            cardDataService.doExport(this.getResponse(),keywork);
	            logger.debug(methodName+method);
	            getSysLogService().success(module,method, methodName, "关键字:"+keywork);
	        } catch (Exception e) {
	        	 e.printStackTrace();
	             logger.error(methodName+"失败！", e);
	             getSysLogService().error(module,method, methodName, "关键字:"+keywork+";"+e.toString());
	        }
	    }
}
