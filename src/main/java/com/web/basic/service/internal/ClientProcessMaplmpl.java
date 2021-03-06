package com.web.basic.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.role.entity.RolePermissionMap;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.dao.ClientProcessMapDao;
import com.web.basic.dao.ClientDao;
import com.web.basic.dao.ProcessDao;
import com.web.basic.entity.ClientProcessMap;
import com.web.basic.entity.Client;
import com.web.basic.entity.Process;
import com.web.basic.entity.ProdProcDetail;
import com.web.basic.service.ClientProcessMapService;

/**
 * 客户通用工艺流程维护
 *
 */
@Service(value = "clientProcessMapService")
@Transactional(propagation = Propagation.REQUIRED)
public class ClientProcessMaplmpl implements ClientProcessMapService{

	@Autowired
	ClientProcessMapDao clientProcessMapDao;
	@Autowired
	ProcessDao processDao;
	@Autowired
	ClientDao clientDao;

	@Override
    @Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
				List<SearchFilter> filters = new ArrayList<>();
				filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
				// 查询2
				List<SearchFilter> filters1 = new ArrayList<>();
				if (StringUtils.isNotEmpty(keyword)) {
//					filters1.add(new SearchFilter("client.custNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("fdemoName", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("process.procNo", SearchFilter.Operator.LIKE, keyword));
					filters1.add(new SearchFilter("process.procName", SearchFilter.Operator.LIKE, keyword));
				}
				Specification<ClientProcessMap> spec = Specification.where(BaseService.and(filters, ClientProcessMap.class));
				Specification<ClientProcessMap> spec1 = spec.and(BaseService.or(filters1, ClientProcessMap.class));
				Page<ClientProcessMap> page = clientProcessMapDao.findAll(spec1, pageRequest);

				return ApiResponseResult.success().data(DataGrid.create(page.getContent(), (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
				
/*				List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
				for(ClientProcessMap bs:page.getContent()){
					Map<String, Object> map = new HashMap<>();
//					map.put("client_id", bs.getClient().getId());
//					map.put("custNo", bs.getClient().getCustNo());//获取关联表的数据-客户表
//					map.put("custName", bs.getClient().getCustName());//客户名
//					map.put("custId", bs.getClient().getId());
					map.put("fdemoName",bs.getFdemoName());
					map.put("procOrder", bs.getProcOrder());//工序顺序
					map.put("procNo", bs.getProcess().getProcNo());//工序
					map.put("procName", bs.getProcess().getProcName());//工序名
					map.put("procId", bs.getProcess().getId());//工序ID
					map.put("jobAttr", bs.getJobAttr());
					map.put("id", bs.getId());
					map.put("lastupdateDate",bs.getLastupdateDate());
					map.put("createDate", bs.getCreateDate());
					list.add(map);
				}

				return ApiResponseResult.success().data(DataGrid.create(list, (int) page.getTotalElements(),
						pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));*/
	}
	/*
	 * 增加工艺流程
	 * **/
	@Override
    @Transactional
    public ApiResponseResult addItem(String procIdList,String fdemoName) throws Exception{

		if(fdemoName == null||fdemoName==""){
            return ApiResponseResult.failure("模板名称不能为空！");
        }
		if(procIdList == null||procIdList==""){
            return ApiResponseResult.failure("至少增加一道工序！");
        }
//		int count = clientProcessMapDao.countByDelFlagAndFdemoName(0,fdemoName);
//        if(count > 0){
//            return ApiResponseResult.failure("该模板名称已存在，请填写其他模板名称！");
//        }
        //转换
        String[] porcIdArray = procIdList.split(",");
        List<String> procList = new ArrayList<String>();
        for(int i = 0; i < porcIdArray.length; i++){
            if(StringUtils.isNotEmpty(porcIdArray[i])) {
            	procList.add(porcIdArray[i]);
            }
        }
      //1.删除原工序信息
        List<ClientProcessMap> listOld = clientProcessMapDao.findByFdemoName(fdemoName);
        if(listOld.size() > 0){
            for(ClientProcessMap item : listOld){
            	item.setDelTime(new Date());
            	item.setDelFlag(1);
            	item.setDelBy(UserUtil.getSessionUser().getId());
            }
            clientProcessMapDao.saveAll(listOld);
        }
      //2.添加新工序信息
        List<ClientProcessMap> listNew = new ArrayList<>();
        if(procList.size() > 0){
        	Integer procOrder = 10;
            for(String proc : procList){
            	String[] procs = proc.split("@");
            	ClientProcessMap item = new ClientProcessMap();
                item.setCreateDate(new Date());
                item.setCreateBy(UserUtil.getSessionUser().getId());
                item.setFdemoName(fdemoName);
                item.setProcOrder(procOrder);
                item.setProcId(Long.valueOf(procs[0]));
                item.setJobAttr(Integer.valueOf(procs[1]));
                listNew.add(item);
				procOrder = procOrder+10;
            }
            clientProcessMapDao.saveAll(listNew);
        }
        return ApiResponseResult.success("工艺流程添加成功！");
    }

	/**
     * 修改过程属性
     */
	 @Override
	    @Transactional
	    public ApiResponseResult doJobAttr(Long id, Integer jobAttr) throws Exception{
	        if(id == null){
	            return ApiResponseResult.failure("工序ID不能为空！");
	        }
	        if(jobAttr == null){
	            return ApiResponseResult.failure("请正确设置过程属性！");
	        }
	        ClientProcessMap o = clientProcessMapDao.findById((long) id);
	        if(o == null){
	            return ApiResponseResult.failure("工序记录不存在！");
	        }
	        o.setLastupdateDate(new Date());
	        o.setLastupdateBy(UserUtil.getSessionUser().getId());
	        o.setJobAttr(jobAttr);
	        clientProcessMapDao.save(o);
	        return ApiResponseResult.success("设置成功！").data(o);
	    }

	/**
     * 客户ID获取原来工序记录
     */
	public ApiResponseResult getClientItem(String fdemoName) throws Exception{
		 List<ClientProcessMap> list = clientProcessMapDao.findByDelFlagAndFdemoName(0, fdemoName);
		return ApiResponseResult.success().data(list);
	}

	/**
	 * 删除工序记录
	 * */
	@Override
    @Transactional
    public ApiResponseResult delete(Long id) throws Exception{
        if(id == null){
            return ApiResponseResult.failure("ID不能为空！");
        }
        ClientProcessMap o  = clientProcessMapDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("该工序不存在！");
        }
        o.setDelTime(new Date());
        o.setDelFlag(1);
        o.setDelBy(UserUtil.getSessionUser().getId());
        clientProcessMapDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

	/**
     * 获取物料数据，工序数据
     */
	@Override
    @Transactional
	public ApiResponseResult getProcList() throws Exception {
		Map<String, Object> map = new HashMap<>();
		List<Process> pList = processDao.findByDelFlagAndCheckStatus(0,1);
		List<Client> cList = clientDao.findByDelFlag(0);

		map.put("process", pList);
		map.put("client", cList);
		return ApiResponseResult.success().data(map);
	}
	
	/*
	 * 修改工序顺序
	 * */
	@Override
	public ApiResponseResult doProcOrder(Long id, String procOrder) throws Exception {
		// TODO Auto-generated method stub
		if(id == null){
            return ApiResponseResult.failure("工序ID不能为空！");
        }
        if(procOrder == null){
            return ApiResponseResult.failure("请填写正确的数字！");
        }
        ClientProcessMap o = clientProcessMapDao.findById((long) id);
        if(o == null){
            return ApiResponseResult.failure("工序记录不存在！");
        }
        //20201114-fyx-判断序号已存在
        List<ClientProcessMap> lcp = clientProcessMapDao.findByDelFlagAndFdemoNameAndProcOrder(0, o.getFdemoName(), Integer.parseInt(procOrder));
        if(lcp.size()>0){
        	 return ApiResponseResult.failure("工序序号重复,请重新填写！");
        }
        //--end
        o.setLastupdateDate(new Date());
        o.setLastupdateBy(UserUtil.getSessionUser().getId());
        o.setProcOrder(Integer.parseInt(procOrder));
        clientProcessMapDao.save(o);
        return ApiResponseResult.success("修改成功！").data(o);
	}
}
