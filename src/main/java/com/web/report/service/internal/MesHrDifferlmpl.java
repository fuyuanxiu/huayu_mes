package com.web.report.service.internal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.report.service.MesHrDifferService;
import com.web.report.service.LineEfficService;

/**
 * 人数差异统计表
 */
@Service(value = "MesHrDifferService")
@Transactional(propagation = Propagation.REQUIRED)
public class MesHrDifferlmpl extends ReportPrcUtils implements MesHrDifferService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	
	@Override
	public ApiResponseResult getEmpCode(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getEmpCodePrc(UserUtil.getSessionUser().getCompany() + "",
				UserUtil.getSessionUser().getFactory() + "", UserUtil.getSessionUser().getId() + "", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	
	/**
	 * 获取数据
	 * 2020-11-18
	 * */
	@Override
	public ApiResponseResult getList(String sdate,String edate,
			String empCode,PageRequest pageRequest) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getListPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",UserUtil.getSessionUser().getId()+"",
				sdate, edate,empCode,pageRequest);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		Map map = new HashMap();
		map.put("total", list.get(2));
		map.put("rows", list.get(3));
		return ApiResponseResult.success("").data(map);
	}

	private List getListPrc(String facoty,String company,String user_id ,String  beginTime,
			String endTime,String empCode,PageRequest pageRequest) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_hr_att_diff_calc(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, user_id);
				cs.setString(4, beginTime);
				cs.setString(5, endTime);
				cs.setString(6, empCode);
				cs.setInt(7, pageRequest.getPageSize());
				cs.setInt(8, pageRequest.getPageNumber()+1);
				cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(10, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(12, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(10));
				result.add(cs.getString(11));
				if (cs.getString(10).toString().equals("0")) {
					result.add(cs.getInt(9));
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(12);
					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
				}
				System.out.println(l);
				return result;
			}
		});
		return resultList;
	}
}
