package com.system.todo.service.internal;


import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.app.base.data.DataGrid;
import com.system.todo.dao.TodoInfoDao;
import com.system.todo.entity.TodoInfo;
import com.system.todo.service.TodoInfoService;
import com.utils.BaseService;
import com.utils.SearchFilter;
import com.utils.UserUtil;
import com.utils.enumeration.BasicStateEnum;
import com.web.basic.entity.Client;

@Service
@Transactional(rollbackFor = Exception.class)
public class TodoInfoImpl  implements TodoInfoService {
    @Autowired
    private TodoInfoDao todoInfoDao;
    
    

    @Transactional
    public ApiResponseResult add(TodoInfo todoInfo) throws Exception {
        if (null == todoInfo.getBsUserId() ) {
            return ApiResponseResult.failure("待办人不能为空");
        }
        if (StringUtils.isEmpty(todoInfo.getBsTitle()) || StringUtils.isEmpty(todoInfo.getBsTitle().trim())) {
            return ApiResponseResult.failure("标题不能为空");
        }
        todoInfo.setBsTitle(todoInfo.getBsTitle().trim());
        //todoInfo.setBsSystemType(BasicStateEnum.TODO_COST.intValue());//待办类型
        todoInfo.setCreateDate(new Date());
        //todoInfo.setPkCreatedBy(SessionContextUtils.getCurrentUser().getId());
        if(null == todoInfo.getBsEndTime()){
            //如果没有传截止时间，则默认为一周之后
            Calendar curr = Calendar.getInstance();
            curr.setTime(todoInfo.getCreateDate());
            curr.add(Calendar.WEEK_OF_YEAR, 1);
            Date after7Days=curr.getTime();
            todoInfo.setBsEndTime(after7Days);                  //有效结束时间
        }
        //1.用户ID不为空时只添加一个用户
        if(null != todoInfo.getBsUserId()){
            todoInfoDao.save(todoInfo);
/*
            //发送邮件
            //先判断是否开启了邮件功能，开启了就可以发邮件
            String flagMail = appConfig.getString("scm.mail.enabled");
            if(StringUtils.isNotEmpty(flagMail) && flagMail.equals("true")){
                SysUser sysUser = sysUserDao.findOne(todoInfo.getBsUserId());
                if(sysUser != null && StringUtils.isNotEmpty(sysUser.getBsEmail())){
                    String nameTo = sysUser.getBsName();    //收件人名
                    String mailTo = sysUser.getBsEmail().trim();   //收件人邮箱
                    String subject = "待办事项提醒";    //主题
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                    String dateStr = simpleDateFormat.format(new Date());
                    StringBuffer text = new StringBuffer();   //邮件内容
                    text.append("<div style='padding: 5px;'>" + nameTo + "，" + "</div>");
                    text.append("<div style='padding: 5px;'>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;你好！" + todoInfo.getBsTitle() +"。请及时处理！</div>");
                    text.append("<div style='padding: 5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请<a href='http://www.unind.net'>点击此处</a>进入QMS系统。</div>");
                    text.append("<div style='padding: 5px;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;此致</div>");
                    text.append("<div style='padding: 5px;'>敬礼</div>");
                    text.append("<div style='padding: 5px; float: right; margin: 0 50px 0 0;'>QMS系统</div><br>");
                    text.append("<div style='padding: 5px; float: right; margin: 0 20px 0 0;clear: both;'>" + dateStr + "</div><br>");
                    mailSenderService.send(mailTo, subject, text.toString());
                }
            }*/
        }

        return ApiResponseResult.success("新增成功！").data(todoInfo);
    }

    @Transactional
    public ApiResponseResult edit(TodoInfo todoInfo) throws Exception {
        if (null == todoInfo || null == todoInfo.getId()) {
            return ApiResponseResult.failure("记录ID为必填项");
        }
        if (null == todoInfo.getBsUserId()) {
            return ApiResponseResult.failure("待办人不能为空");
        }
        if (StringUtils.isEmpty(todoInfo.getBsTitle()) || StringUtils.isEmpty(todoInfo.getBsTitle().trim())) {
            return ApiResponseResult.failure("标题不能为空");
        }
        TodoInfo o = todoInfoDao.findById((long)todoInfo.getId());
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除");
        }
//        if (!StringUtils.equals(approvedFlow.getBsName().trim(), o.getBsName())) {
//            int counts = approvedFlowDao.countByBsName(approvedFlow.getBsName());
//            if (counts > 0) {
//                return ApiResponseResult.failure("项目名称不能重复");
//            }
//            o.setBsName(approvedFlow.getBsName().trim());
//        }
        o.setBsUserId(todoInfo.getBsUserId());
        o.setBsRouter(todoInfo.getBsRouter().trim());
        o.setBsTitle(todoInfo.getBsTitle().trim());
        o.setBsContent(todoInfo.getBsContent());
        o.setBsType(todoInfo.getBsType());
        o.setBsPriority(todoInfo.getBsPriority());
        o.setBsReferId(todoInfo.getBsReferId());

        o.setLastupdateDate(new Date());
	/*	o.setPkModifiedBy(SessionContextUtils.getCurrentUser().getId());*/
//        o.setPkModifiedBy(Long.parseLong("1"));
        todoInfoDao.save(o);
        return ApiResponseResult.success("修改成功！").data(o);
    }

    @Transactional
    public ApiResponseResult close(Long id, Long bsUserId, Long roleId, Long bsReferId) throws Exception {
        if(null != id){
            todoInfoDao.closeById(id);
            return ApiResponseResult.success("关闭成功！");
        }
        if(null == id && null != bsUserId && null != bsReferId){
            todoInfoDao.closeByBsUserIdAndBsReferId(bsUserId, bsReferId);
            return ApiResponseResult.success("关闭成功！");
        }
        if(null == id && null == bsUserId && null != roleId && null != bsReferId){
            todoInfoDao.closeByRoleIdAndBsReferId(roleId, bsReferId);
            return ApiResponseResult.success("关闭成功！");
        }
        if(null == id && null == bsUserId && null == roleId && null != bsReferId){
            todoInfoDao.closeByBsReferId(bsReferId);
            return ApiResponseResult.success("关闭成功！");
        }
        return ApiResponseResult.failure("关闭失败！");
    }

    @Transactional
    public ApiResponseResult delete(Long id) throws Exception {
    	TodoInfo o = todoInfoDao.findById((long)id);
        if (null == o) {
            return ApiResponseResult.failure("记录ID不存在或已被删除").status("error1");
        }
        if (o.getId() <= 0) {
            return ApiResponseResult.failure("没有操作权限");
        }
        o.setDelFlag(BasicStateEnum.TRUE.intValue());
        todoInfoDao.save(o);
        return ApiResponseResult.success("删除成功！");
    }

    @Transactional(readOnly = true)
    public ApiResponseResult getlist(int bsStatus, PageRequest pageRequest) throws Exception {
    	
    	Long uid = UserUtil.getSessionUser().getId();
    	
        List<SearchFilter> filters = new ArrayList<SearchFilter>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("bsStatus", SearchFilter.Operator.EQ, bsStatus));
        filters.add(new SearchFilter("bsUserId", SearchFilter.Operator.EQ, uid));
        filters.add(new SearchFilter("bsType", SearchFilter.Operator.EQ, 1));
        Specification<TodoInfo> spec = Specification.where(BaseService.and(filters, TodoInfo.class));
        
        
        
        //1.获取当前登录用户待办
        Page<TodoInfo> page = todoInfoDao.findAll(spec, pageRequest);
        DataGrid dataGrid = DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize());

        //2.统计当前登录用户所有待办数量
        int totalNum = todoInfoDao.countByDelFlagAndBsUserId(BasicStateEnum.FALSE.intValue(), uid);
        int completedNum = todoInfoDao.countByDelFlagAndBsUserIdAndBsStatus(BasicStateEnum.FALSE.intValue(), uid, 1);
        int inCompleteNum = todoInfoDao.countByDelFlagAndBsUserIdAndBsStatus(BasicStateEnum.FALSE.intValue(), uid, 0);

        //3.封装数据
        Map<String, Object> map = new HashMap<>();
        map.put("list", dataGrid);
        map.put("totalNum", totalNum);
        map.put("completedNum", completedNum);
        map.put("inCompleteNum", inCompleteNum);
        
        map.put("Check", todoInfoDao.findByDelFlagAndBsUserIdAndBsTypeAndBsStatus(0, uid, 2,0));

        return ApiResponseResult.success().data(map);
    }

    /**
     * 20210114
     * -lst
     * 获取登陆人所有待办事项
     * **/
    @Transactional(readOnly = true)
    public ApiResponseResult getlist2(String keyword,String bsStatus,PageRequest pageRequest) throws Exception {
    	
    	Long uid = UserUtil.getSessionUser().getId();
    	
        List<SearchFilter> filters = new ArrayList<SearchFilter>();
        filters.add(new SearchFilter("delFlag", SearchFilter.Operator.EQ, BasicStateEnum.FALSE.intValue()));
        filters.add(new SearchFilter("bsUserId", SearchFilter.Operator.EQ, uid));
        if(StringUtils.isNotEmpty(bsStatus)){
            filters.add(new SearchFilter("bsStatus", SearchFilter.Operator.EQ, bsStatus));
        }
        List<SearchFilter> filters1 = new ArrayList<>();
		if (StringUtils.isNotEmpty(keyword)) {
			filters1.add(new SearchFilter("bsTitle", SearchFilter.Operator.LIKE, keyword));
			filters1.add(new SearchFilter("bsContent", SearchFilter.Operator.LIKE, keyword));
		}
        
        Specification<TodoInfo> spec = Specification.where(BaseService.and(filters, TodoInfo.class));        
        Specification<TodoInfo> spec1 = spec.and(BaseService.or(filters1, TodoInfo.class));
        
        //获取当前登录用户待办
        Page<TodoInfo> page = todoInfoDao.findAll(spec1, pageRequest);
        DataGrid dataGrid = DataGrid.create(page.getContent(), (int) page.getTotalElements(), pageRequest.getPageNumber() + 1, pageRequest.getPageSize());

        return ApiResponseResult.success().data(dataGrid);
    }
    
	@Override
	public ApiResponseResult closeByIdAndModel(Long bsReferId,String model) throws Exception {
		// TODO Auto-generated method stub
		todoInfoDao.closeByBsReferIdAndModel(bsReferId,model);
		return ApiResponseResult.success();
	}

    @Override
    public ApiResponseResult openByIdAndModel(Long bsReferId,String model) throws Exception {
        // TODO Auto-generated method stub
        todoInfoDao.openByBsReferIdAndModel(bsReferId,model);
        return ApiResponseResult.success();
    }
}
