package com.web.kanban.service.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.system.user.entity.SysUser;
import com.utils.UserUtil;
import com.web.kanban.KanbanDao;
import com.web.kanban.service.KanbanService;

@Service(value = "KanbanService")
@Transactional(propagation = Propagation.REQUIRED)
public class KanbanImpl extends PrcKanbanUtils  implements KanbanService {

	@Autowired
	KanbanDao kanbanDao;

	@Override
	public ApiResponseResult getCjbgList(String class_no, String dep_id, String sdata, String dev_ip) throws Exception {
		String usr_id = "1";
		SysUser su = UserUtil.getSessionUser();
		if(su != null){
			usr_id = su.getId()+"";
		}
		// TODO Auto-generated method stub
		List<Object> list = getCjbgListPrc("","",usr_id,class_no,  dep_id,  sdata,  dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List", list.get(2));
		map.put("Sdata", list.get(3));
		map.put("Edata", list.get(4));
		map.put("LineNum", list.get(5));//开线数
		map.put("Title", list.get(6));//开线数
		return ApiResponseResult.success().data(map);
	}
	@Override
	public ApiResponseResult getCjbgDetailList(String liner, String dev_ip) throws Exception {
		// TODO Auto-generated method stub
		String usr_id = "1";
		SysUser su = UserUtil.getSessionUser();
		if(su != null){
			usr_id = su.getId()+"";
		}
		if(liner.equals("总体")){
			liner = "";
		}
		//List<Object> list = getCjbgDetailListPrc(usr_id,liner, dev_ip);
		List<Object> list = getCjbgDetailListPrc(usr_id,liner, "1");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getCjbgDepList() throws Exception {
		// TODO Auto-generated method stub
		//return ApiResponseResult.success().data(kanbanDao.getDepList());
		List<Object> list = getOrgListPrc("","","","电子看板");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getScdzList(String class_no, String dep_id, String sdata,  String dev_ip)
			throws Exception {
		String user_id = "1";
		SysUser su = UserUtil.getSessionUser();
		if(su != null){
			user_id = su.getId()+"";
		}
		List<Object> list = getScdzListPrc("","",user_id,class_no,  dep_id,  sdata,  dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List", list.get(2));
		map.put("Sdata", list.get(3));
		map.put("Edata", list.get(4));
		map.put("LINE_NUM_PLN", list.get(5));//
		map.put("LINE_NUM_NOW", list.get(6));
		map.put("EMP_NUM_PLN", list.get(7));
		map.put("EMP_NUM_NOW", list.get(8));
		map.put("PRD_NUM_PLN", list.get(9));
		map.put("PRD_NUM_DONE", list.get(10));
		map.put("PRD_RATE_DONE", list.get(11));
		map.put("PO_NUM_EMP_OFF", list.get(12));
		map.put("Title", list.get(13));
		return ApiResponseResult.success().data(map);
	}
	@Override
	public ApiResponseResult getScdzDetailList(String liner, String dep_id, 
			String dev_ip,String fieldword) throws Exception {
		// TODO Auto-generated method stub
		String user_id = "1";
		SysUser su = UserUtil.getSessionUser();
		if(su != null){
			user_id = su.getId()+"";
		}
		List<Object> list = getScdzDetailListPrc( user_id, liner,dep_id, dev_ip,fieldword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	/**
	 * 制程不良看板
	 * 车间看板、拉头看板的的部门获取
	 * **/
	@Override
	public ApiResponseResult getZcblDepList() throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getOrgListPrc("","","2","");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	@Override
	public ApiResponseResult getZcblList(String class_no, String dep_id, String sdata,String dev_ip)
			throws Exception {
		// TODO Auto-generated method stub
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
				
		List<Object> list = getZcblListPrc("","",usr_id,class_no,  dep_id,  sdata, dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List", list.get(2));
		map.put("DeptName", list.get(3));
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getXlpmList( String class_id,String dep_id, 
			String sdata, String dev_ip,String liner,String taskno) throws Exception{
		SysUser sysUser = UserUtil.getSessionUser();
		String userId = "";
		if(sysUser!=null){
			userId = sysUser.getId().toString();
		}else {
			userId = "1";
		}
		List<Object> list = getXlpmListPrc("","",userId,class_id,dep_id,sdata,dev_ip,liner,taskno);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_table", list.get(2));
		map.put("List_line", list.get(3));
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		map.put("Title", list.get(6));
		
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getDfgList(String class_id,String dep_id, String sdata,
			String usr_id,String dev_ip) throws Exception{	
		List<Object> list = getDfgListPrc("","",usr_id,class_id,dep_id,sdata,dev_ip);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_table", list.get(2));
		map.put("List_dept", list.get(3));
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		
		return ApiResponseResult.success().data(map);
	}
	
	public ApiResponseResult getLiner (String deptId)throws Exception{
		List<Object> list = getOrgListPrc("","",deptId,"部门组长");
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}	
		return ApiResponseResult.success().data(list.get(2));
	}

	@Override
	public ApiResponseResult getCxdzList(String class_id, String dep_id, String sdata, String dev_ip, String liner)
			throws Exception {
		// TODO Auto-generated method stub
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
		List<Object> list = getCxdzListtPrc("","",usr_id,class_id,dep_id,sdata,dev_ip,liner);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_table", list.get(2));//工单明细
		map.put("List_line", list.get(3));//产线信息
		map.put("Sdata", list.get(4));
		map.put("Edata", list.get(5));
		map.put("Title", list.get(6));
		
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getCxscList(String taskNo,String deptId,String liner,
			String dev_ip,String interval)throws Exception{
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
		List<Object> list = getCxscList("","",taskNo,deptId,liner,dev_ip,usr_id,interval);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_result1", list.get(2));
		map.put("List_result2", list.get(3));
		map.put("Title", list.get(4));
		
		return ApiResponseResult.success().data(map);		
	}	
	
	@Override
	public ApiResponseResult getCxscList2(String taskNo,String deptId,String liner,
			String dev_ip,String interval)throws Exception{
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
		List<Object> list = getCxscList2Prc("","",taskNo,deptId,liner,dev_ip,usr_id,interval);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_result", list.get(2));
		map.put("Title", list.get(3));
		
		return ApiResponseResult.success().data(map);		
	}	
	/**
	 * 获取达成率表格-用在拉头看板（zzdzkb）
	 *2021-6-3
	 * **/
	@Override
	public ApiResponseResult getFinishRate(String taskNo,String deptId,String liner,
			String dev_ip,String interval)throws Exception{
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "1";
		}else{
			usr_id = su.getId()+"";
		}
		List<Object> list = getFinishRatePrc("","",taskNo,deptId,liner,dev_ip,usr_id,interval);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("List_result", list.get(2));
		map.put("Title", list.get(3));
		
		return ApiResponseResult.success().data(map);		
	}	
	
	/**
	 * 获取看板刷新间隔的时间
	 *2020-11-19 
	 * **/
	@Override
	public ApiResponseResult getIntervalTime()throws Exception{
		return ApiResponseResult.success().data(kanbanDao.getIntervalTime());
	}
	
	/**
	 * 获取看板轮播间隔的时间
	 *2020-11-21
	 * **/
	@Override
	public ApiResponseResult getRotationTime()throws Exception{
		return ApiResponseResult.success().data(kanbanDao.getRotationTime());
	}
	/**
	 * 车间看板新增图表：在线良率图表
	 * 2021-6-3
	 * **/
	@Override
	public ApiResponseResult getZxllList(String class_id,String deptId, String date,String dev_ip)throws Exception{
		String usr_id = "";
		SysUser su = UserUtil.getSessionUser();
		if(su == null){
			usr_id = "5602";
		}else{
			usr_id = su.getId()+"";
		}
		List<Object> list = getZxllListPrc("","",class_id,deptId,date,dev_ip,usr_id);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("ListResult", list.get(2));
		map.put("BegTime", list.get(3));
		map.put("EndTime", list.get(4));
		map.put("DeptName", list.get(5));
		
		return ApiResponseResult.success().data(map);	
	}
}
