package com.web.report.service.internal;

import com.app.base.data.ApiResponseResult;
import com.utils.UserUtil;
import com.web.report.service.ProcDayService;
import com.web.report.service.RptUpphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 过程检验日报表
 */
@Service(value = "RptUpphService")
@Transactional(propagation = Propagation.REQUIRED)
public class RptUpphlmpl extends ReportPrcUtils implements RptUpphService {
	
	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public ApiResponseResult getDeptInfo(String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getDeptInfoPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "", "2", keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
		return ApiResponseResult.success().data(list.get(2));
	}
	

	
	@Override
	public ApiResponseResult getReport(String beginTime,
			String endTime,String keyword) throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = getReportPrc(UserUtil.getSessionUser().getFactory() + "",
				UserUtil.getSessionUser().getCompany() + "",
				beginTime, endTime,keyword);
		if (!list.get(0).toString().equals("0")) {// 存储过程调用失败 //判断返回游标
			return ApiResponseResult.failure(list.get(1).toString());
		}
//		Map map = new HashMap();
//		map.put("Depart", list.get(2));
//		map.put("Total", list.get(3));
//		map.put("List", list.get(4));
		return ApiResponseResult.success().data(list.get(2));
	}
	
	/**
	 * 品质检验日报表
	 * 2020-11-21
	 * */
	public List getReportPrc(String facoty,String company,String beginTime,
			String endTime,String keyword) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  PRC_MES_RPT_upph(?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, facoty);
				cs.setString(2, company);
				cs.setString(3, beginTime);
				cs.setString(4, endTime);
				cs.setString(5, keyword);
				cs.registerOutParameter(6, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(7, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(8, -10);// 输出参数 追溯数据
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(6));
				result.add(cs.getString(7));
				if (cs.getString(6).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(8);
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
