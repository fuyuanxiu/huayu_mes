package com.web.basePrice.service.internal;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import com.system.file.dao.FsFileDao;
import com.system.file.entity.FsFile;
import com.system.user.dao.SysUserDao;

import com.utils.ExcelExport;
import com.web.basePrice.dao.*;
import com.web.basePrice.entity.*;
import com.web.basic.service.SysParamService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basePrice.service.BaseFeeService;
import com.web.basic.dao.WorkCenterDao;
import com.web.basic.entity.Mtrial;
import com.web.basic.entity.WorkCenter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @date Dec 8, 2020 11:57:13 AM
 */
@Service(value = "BaseFeeService")
@Transactional(propagation = Propagation.REQUIRED)
public class BaseFeeImpl extends BasePriceUtils implements BaseFeeService {
	@Autowired
	private BaseFeeDao baseFeeDao;

	@Autowired
	private BaseFeeFileDao baseFeeFileDao;

	@Autowired
	private BjModelTypeDao bjModelTypeDao;

	@Autowired
	private FsFileDao fsFileDao;

	@Autowired
	private ProcDao procDao;

	@Autowired
	private SysUserDao sysUserDao;

	@Autowired
	private SysParamService sysParamService;
		
	/**
	 * 新增人工制费信息维护
	 */
	@Override
	@Transactional
	public ApiResponseResult add(BaseFee baseFee) throws Exception {
		if (baseFee == null) {
			return ApiResponseResult.failure("人工制费信息不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getWorkcenterId().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeLh())) {
			return ApiResponseResult.failure("人工费率不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeMh())) {
			return ApiResponseResult.failure("制费费率不能为空！");
		}
		List<BaseFee> baseFeeList =baseFeeDao.findByDelFlagAndProcIdAndMhTypeAndWorkCenterId(0,baseFee.getProcId(),baseFee.getMhType(),baseFee.getWorkcenterId());
		if(baseFeeList.size()>0){
			return ApiResponseResult.failure("该人工制费信息已存在，请重新选择！");
		}
		baseFee.setCreateDate(new Date());
		baseFee.setCreateBy(UserUtil.getSessionUser().getId());
		baseFee.setFeeLh(baseFee.getFeeLh().trim());
		baseFee.setFeeMh(baseFee.getFeeMh().trim());
		baseFeeDao.save(baseFee);

		String[] fileIds =  baseFee.getFileId().split(",");
		List<BaseFeeFile> fileList = new ArrayList<>();
		for(String fileId :fileIds){
			if(StringUtils.isNotEmpty(fileId)){
				FsFile fsFile = fsFileDao.findById(Long.parseLong(fileId));
				if(fsFile.getDelFlag()==0) {
					BaseFeeFile baseFeeFile = new BaseFeeFile();
					baseFeeFile.setmId(baseFee.getId());
					baseFeeFile.setFileId(fsFile.getId());
					baseFeeFile.setFileName(fsFile.getBsName());
					baseFeeFile.setCreateDate(new Date());
					baseFeeFile.setCreateBy(UserUtil.getSessionUser().getId());
					fileList.add(baseFeeFile);
				}
			}
		}
		baseFeeFileDao.saveAll(fileList);

		return ApiResponseResult.success("人工制费信息添加成功！").data(baseFee);
	}

	/**
	 * 修改人工制费
	 */
	@Override
	@Transactional
	public ApiResponseResult edit(BaseFee baseFee) throws Exception {
		if (baseFee == null) {
			return ApiResponseResult.failure("人工制费信息不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getWorkcenterId().toString())) {
			return ApiResponseResult.failure("工作中心不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeLh())) {
			return ApiResponseResult.failure("人工费率不能为空！");
		}
		if (StringUtils.isEmpty(baseFee.getFeeMh())) {
			return ApiResponseResult.failure("制费费率不能为空！");
		}
		BaseFee o = baseFeeDao.findById((long) baseFee.getId());
		if (o == null) {
			return ApiResponseResult.failure("该人工制费不存在！");
		}
		List<BaseFee> baseFeeList =baseFeeDao.findByDelFlagAndProcIdAndMhTypeAndWorkCenterId(0,baseFee.getProcId(),baseFee.getMhType(),baseFee.getWorkcenterId());
		if(baseFeeList.size()>0){
			if(!baseFeeList.get(0).getId().equals(baseFee.getId())) {
				return ApiResponseResult.failure("该人工制费信息已存在，请重新选择！");
			}
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		//o.setEnabled(baseFee.getEnabled());
		o.setWorkcenterId(baseFee.getWorkcenterId());
		o.setProcId(baseFee.getProcId());
	    o.setProcName(baseFee.getProcName());
	    o.setMhType(baseFee.getMhType());
	    o.setModelCode(baseFee.getModelCode());
	    o.setFeeLh(baseFee.getFeeLh().trim());
	    o.setFeeMh(baseFee.getFeeMh().trim());
	    o.setExpiresTime(baseFee.getExpiresTime());
		baseFeeDao.save(o);

		String[] fileIds =  baseFee.getFileId().split(",");
		List<BaseFeeFile> fileList = new ArrayList<>();
		for(String fileId :fileIds){
			if(StringUtils.isNotEmpty(fileId)){
				FsFile fsFile = fsFileDao.findById(Long.parseLong(fileId));
				if(fsFile.getDelFlag()==0) {
					BaseFeeFile baseFeeFile = new BaseFeeFile();
					baseFeeFile.setmId(baseFee.getId());
					baseFeeFile.setFileId(fsFile.getId());
					baseFeeFile.setFileName(fsFile.getBsName());
					baseFeeFile.setCreateDate(new Date());
					baseFeeFile.setCreateBy(UserUtil.getSessionUser().getId());
					fileList.add(baseFeeFile);
				}
			}
		}
		baseFeeFileDao.saveAll(fileList);

		return ApiResponseResult.success("编辑成功！");
	}

	/**
	 * 删除人工制费
	 */
	@Override
	@Transactional
	public ApiResponseResult delete(Long id) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("人工制费ID不能为空！");
		}
		BaseFee o = baseFeeDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("人工制费不存在！");
		}
		o.setDelFlag(1);
		baseFeeDao.save(o);
		return ApiResponseResult.success("删除成功！");
	}

	/**
	 * 有效状态切换
	 */
	@Override
	@Transactional
	public ApiResponseResult doStatus(Long id, Integer enabled) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("人工制费ID不能为空！");
		}
		if (enabled == null) {
			return ApiResponseResult.failure("请正确设置正常或禁用！");
		}
		BaseFee o = baseFeeDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("此人工制费信息不存在！");
		}
		o.setLastupdateDate(new Date());
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setEnabled(enabled);
		baseFeeDao.save(o);
		return ApiResponseResult.success("设置成功！").data(o);
	}

	/**
	 * 查询人工制费信息维护列表
	 */
	@Override
	@Transactional
	public ApiResponseResult getList(String keyword, PageRequest pageRequest) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mhType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("workCenter.workcenterName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("workCenter.workcenterCode", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<BaseFee> spec = Specification.where(BaseService.and(filters, BaseFee.class));
		Specification<BaseFee> spec1 = spec.and(BaseService.or(filters1, BaseFee.class));
		Page<BaseFee> page = baseFeeDao.findAll(spec1, pageRequest);
		List<BaseFee> baseFeeList = page.getContent();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BaseFee baseFee : baseFeeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", baseFee.getId());
			map.put("workcenterId", baseFee.getWorkcenterId());
			map.put("workcenter", baseFee.getWorkCenter().getWorkcenterName());
			map.put("enabled", baseFee.getEnabled());
			map.put("procName", baseFee.getProcName());
			map.put("modelCode",baseFee.getModelCode());
			map.put("procId", baseFee.getProcId());
			map.put("mhType", baseFee.getMhType());
			map.put("expiresTime",baseFee.getExpiresTime());
			map.put("feeLh", baseFee.getFeeLh());
			map.put("feeMh", baseFee.getFeeMh());

			if(baseFee.getCreateBy()!=null) {
				map.put("createBy", sysUserDao.findById((long) baseFee.getCreateBy()).getUserName());
				map.put("createDate", df.format(baseFee.getCreateDate()));
			}
			if (baseFee.getLastupdateBy() != null) {
				map.put("lastupdateBy", sysUserDao.findById((long) baseFee.getLastupdateBy()).getUserName());
				map.put("lastupdateDate", df.format(baseFee.getLastupdateDate()));
			}
			mapList.add(map);
		}
		return ApiResponseResult.success().data(DataGrid.create(mapList, (int) page.getTotalElements(),
				pageRequest.getPageNumber() + 1, pageRequest.getPageSize()));
	}
	
	@Override
	public ApiResponseResult getProcList(String type, String condition,PageRequest pageRequest)throws Exception{
				List<Object> list = getBJProcPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,condition,pageRequest);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Total", list.get(2));
				map.put("List", list.get(3));
				return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getType(String keyword ,PageRequest pageRequest)throws Exception{
		List<Object> list = getSystemSubParamPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getId() + "",keyword,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Total", list.get(2));
		map.put("List", list.get(3));
		return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult getWorkCenterList(String type, String condition,PageRequest pageRequest)throws Exception{
				List<Object> list = getBJWorkCenterPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,condition,pageRequest);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("Total", list.get(2));
				map.put("List", list.get(3));
				return ApiResponseResult.success().data(map);
	}
	
	@Override
	public ApiResponseResult doCheckInfo(String type,String input1,String input2,
			String input3,String input4)throws Exception{
				List<Object> list = chkCenterAndProcPrc(UserUtil.getSessionUser().getFactory() + "",UserUtil.getSessionUser().getCompany() + "",
						UserUtil.getSessionUser().getId() + "",type,input1,input2,input3,input4);
				if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
					return ApiResponseResult.failure(list.get(1).toString());
				}
				return ApiResponseResult.success();
	}

	//防止读取Excel为null转String 报空指针异常
	public String tranCell(Object object)
	{
		if(object==null||object==""||("").equals(object)){
			return null;
		}else return object.toString().trim();
	}

	@Override
	public ApiResponseResult doExcel(MultipartFile[] file) throws Exception {
		try {

			Date expiresTime = null;
			Object data = sysParamService.getValueByCodeList("BJ_EXPIRES_TIME").getData();
			if(data==null){
				return ApiResponseResult.success("没有维护系统参数失效时间(BJ_EXPIRES_TIME)");
			}else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.MONTH, 1);
				expiresTime = calendar.getTime();
			}

			Date doExcleDate = new Date();
			Long userId = UserUtil.getSessionUser().getId();
			InputStream fin = file[0].getInputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(fin);//创建工作薄
			XSSFSheet sheet = workbook.getSheetAt(0);
			//获取最后一行的num，即总行数。此处从0开始计数
			int maxRow = sheet.getLastRowNum();
//			Integer successes = 0;
			Integer failures = 0;
			List<BaseFee> baseFeeList = new ArrayList<>();
			for (int row = 1; row <= maxRow; row++) {
//				String errInfo = "";
				String workName = tranCell(sheet.getRow(row).getCell(0)); //工作中心
				String procNo = tranCell(sheet.getRow(row).getCell(1)); //工序名称
				String modelCode = tranCell(sheet.getRow(row).getCell(2));//机台名称
				String feeLh = tranCell(sheet.getRow(row).getCell(3));//人工费率（元/小时）
				String feeMh = tranCell(sheet.getRow(row).getCell(4));//制费费率（元/小时）
//				String expiredTime = tranCell(sheet.getRow(row).getCell(5));//失效时间
				XSSFCell cell5 = sheet.getRow(row).getCell(5);
				cell5.setCellType(XSSFCell.CELL_TYPE_STRING);
				String expiredTime = tranCell(cell5);
//				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				if(StringUtils.isNotEmpty(expiredTime)){
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					expiresTime = dateFormat.parse(expiredTime);
				}
				BaseFee baseFee = new BaseFee();
//					List<Proc> procList = procDao.findByDelFlagAndProcNo(0,procNo);
				    List<Proc> procList = procDao.findByDelFlagAndProcName(0,procNo);
					if(procList.size()==1){
						Proc proc = procList.get(0);
						baseFee.setProcId(proc.getId());
						baseFee.setProcName(proc.getProcName());
						baseFee.setWorkcenterId(proc.getWorkcenterId());
					}else if(procList.size()>1){
						if(StringUtils.isNotEmpty(workName)) {
							for (Proc proc : procList) {
								if (workName.equals(proc.getBjWorkCenter().getWorkcenterName())){
									baseFee.setProcId(proc.getId());
									baseFee.setProcName(proc.getProcName());
									baseFee.setWorkcenterId(proc.getWorkcenterId());
								}
							}
						}
					} else {
						failures++;
						continue;
					}
//					List<BjModelType> bjModelTypeList = bjModelTypeDao.findByDelFlagAndModelCode(0,modelCode);
					if(StringUtils.isNotEmpty(modelCode)) {
						List<BjModelType> bjModelTypeList = bjModelTypeDao.findByDelFlagAndModelName(0, modelCode);
						if (bjModelTypeList.size() > 0) {
							baseFee.setMhType(bjModelTypeList.get(0).getModelName());
							baseFee.setModelCode(modelCode.trim());
						} else {
							failures++;
							continue;
						}
					}
					//根据 工序ID和机台和工作中心id判断是否新增或编辑
					List<BaseFee> baseFeeList1 =baseFeeDao.findByDelFlagAndProcIdAndMhTypeAndWorkCenterId(0,baseFee.getProcId(),baseFee.getMhType(),baseFee.getWorkcenterId());
					if(baseFeeList1.size()>0){
						baseFee.setId(baseFeeList1.get(0).getId());
						baseFee.setLastupdateBy(userId);
						baseFee.setLastupdateDate(doExcleDate);
						baseFee.setExpiresTime(expiresTime);
					}else {
						baseFee.setExpiresTime(expiresTime);
						baseFee.setCreateBy(userId);
						baseFee.setCreateDate(doExcleDate);
					}
					if (!feeMh.matches("^\\d+\\.\\d+$") && !feeMh.matches("^^\\d+$")){
						failures++;
						continue;
					}else {
						baseFee.setFeeMh(feeMh);
					}
					if (!feeLh.matches("^\\d+\\.\\d+$") && !feeLh.matches("^^\\d+$")){
						failures++;
						continue;
					}else {
						baseFee.setFeeLh(feeLh);
					}
					baseFeeList.add(baseFee);
			}

			baseFeeDao.saveAll(baseFeeList);
			return ApiResponseResult.success("导入成功!,共导入:"+baseFeeList.size()+";不通过:"+failures);
		}
		catch (Exception e){
			e.printStackTrace();
			return ApiResponseResult.failure("导入失败！请查看导入文件数据格式是否正确！");
		}
	}

	/**
	 *
	 * @param mId 主表id
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResponseResult getFileList(Long mId) throws Exception {
		List<BaseFeeFile> customQsFiles =  baseFeeFileDao.findByDelFlagAndMId(0,mId);
		List<Map<String, Object>> mapList = new ArrayList<>();
		for(BaseFeeFile qsFile :customQsFiles){
			Map<String, Object> map = new HashMap<>();
			map.put("id",qsFile.getFileId());
			map.put("qsFileId",qsFile.getId());
			map.put("bsName",qsFile.getFileName());
			map.put("bsContentType","stp");
			mapList.add(map);
		}
		return ApiResponseResult.success().data(mapList);
	}

	/**
	 * 删除客户品质标准附件
	 */
	@Override
	@Transactional
	public ApiResponseResult delFile(Long id,Long fileId) throws Exception {
		if (id == null) {
			return ApiResponseResult.failure("ID不能为空！");
		}
		BaseFeeFile o = baseFeeFileDao.findById((long) id);
		if (o == null) {
			return ApiResponseResult.failure("附件信息不存在！");
		}
		o.setDelFlag(1);
		o.setLastupdateBy(UserUtil.getSessionUser().getId());
		o.setLastupdateDate(new Date());
		baseFeeFileDao.save(o);

		FsFile fsFile = fsFileDao.findById((long) fileId);
		fsFile.setDelFlag(1);
		fsFileDao.save(fsFile);

		return ApiResponseResult.success("删除附件成功！");
	}

	/**
	 * 查询工序基础信息维护列表
	 */
	@Override
	@Transactional
	public void exportExcel(HttpServletResponse response, String keyword) throws Exception {
		// 查询条件1
		List<SearchFilter> filters = new ArrayList<>();
		filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
		// 查询2
		List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("procName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("mhType", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("workCenter.workcenterName", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("workCenter.workcenterCode", SearchFilter.Operator.LIKE, keyword));
		}
		Specification<BaseFee> spec = Specification.where(BaseService.and(filters, BaseFee.class));
		Specification<BaseFee> spec1 = spec.and(BaseService.or(filters1, BaseFee.class));
		List<BaseFee> baseFeeList = baseFeeDao.findAll(spec1);

		String excelPath = "static/excelFile/";
		String fileName = "人工制费维护模板.xlsx";
		String[] map_arr = new String[]{"wcName","procNo","modelCode","feeLh","feeMh","expiresTime"};
		XSSFWorkbook workbook = new XSSFWorkbook();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> mapList = new ArrayList<>();
		for (BaseFee baseFee: baseFeeList) {
			Map<String, Object> map = new HashMap<>();
			map.put("wcName", baseFee.getProc()!=null?baseFee.getProc().getBjWorkCenter().getWorkcenterName():"");
			map.put("procNo", baseFee.getProc()!=null?baseFee.getProc().getProcName():"");
			List<BjModelType> bjModelTypeList = bjModelTypeDao.findByDelFlagAndModelName (0,baseFee.getMhType());
			map.put("modelCode", bjModelTypeList.size()>0?bjModelTypeList.get(0).getModelName():"");
			map.put("feeLh", baseFee.getFeeLh());
			map.put("feeMh",baseFee.getFeeMh());
			map.put("expiresTime", baseFee.getExpiresTime()!=null?baseFee.getExpiresTime():"");
			mapList.add(map);
		}
		ExcelExport.exportByRow(response,mapList,workbook,map_arr,excelPath+fileName,fileName,1);

	}

}