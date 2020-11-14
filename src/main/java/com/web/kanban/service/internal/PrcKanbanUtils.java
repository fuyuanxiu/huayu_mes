package com.web.kanban.service.internal;

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
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

public class PrcKanbanUtils {
	@Autowired
    private JdbcTemplate jdbcTemplate;

	// 获取车间报工看板信息
	public List getCjbgListPrc(String company,String facoty,String user_id, String class_id,
			String dep_id, String sdata, String dev_ip) throws Exception {
		List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {
				String storedProc = "{call  prc_mes_rpt_cjbg (?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
				CallableStatement cs = con.prepareCall(storedProc);
				cs.setString(1, company);//company
				cs.setString(2, facoty);//facoty
				cs.setString(3, class_id);//class_id
				cs.setString(4, dep_id);//dep_id--部门ID
				cs.setString(5, sdata);//sdata
				cs.setString(6, dev_ip);//dev_ip
				cs.setString(7, user_id);
				cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
				cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(10, -10);// 输出参数 追溯数据
				cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
				cs.registerOutParameter(13, java.sql.Types.INTEGER);// 输出参数 返回标识
				return cs;
			}
		}, new CallableStatementCallback() {
			public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
				List<Object> result = new ArrayList<>();
				List<Map<String, Object>> l = new ArrayList();
				cs.execute();
				result.add(cs.getInt(8));
				result.add(cs.getString(9));
				if (cs.getString(8).toString().equals("0")) {
					// 游标处理
					ResultSet rs = (ResultSet) cs.getObject(10);

					try {
						l = fitMap(rs);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.add(l);
					
					result.add(cs.getString(11));
					result.add(cs.getString(12));
					result.add(cs.getString(13));
				}
				System.out.println(l);
				return result;
			}

		});
		return resultList;
	}
	
	// 获取生产电子看板信息
		public List getScdzListPrc(String company,String facoty,String user_id, String class_id,
				String dep_id, String sdata, String dev_ip) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  PRC_MES_RPT_SCDZ (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, company);//company
					cs.setString(2, facoty);//facoty
					cs.setString(3, class_id);//class_id
					cs.setString(4, dep_id);//dep_id
					cs.setString(5, sdata);//sdata
					cs.setString(6, dev_ip);//dev_ip
					cs.setString(7, user_id);//
					cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(10, -10);// 输出参数 追溯数据
					cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(13, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(14, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(15, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(16, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(17, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(18, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(19, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(20, java.sql.Types.INTEGER);// 输出参数 返回标识
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					List<Object> result = new ArrayList<>();
					List<Map<String, Object>> l = new ArrayList();
					cs.execute();
					result.add(cs.getInt(8));
					result.add(cs.getString(9));
					if (cs.getString(8).toString().equals("0")) {
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(10);

						try {
							l = fitMap(rs);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result.add(l);
						
						result.add(cs.getString(11));
						result.add(cs.getString(12));
						result.add(cs.getString(13));
						result.add(cs.getString(14));
						result.add(cs.getString(15));
						result.add(cs.getString(16));
						result.add(cs.getString(17));
						result.add(cs.getString(18));
						result.add(cs.getString(19));
						result.add(cs.getString(20));
					}
					System.out.println(l);
					return result;
				}

			});
			return resultList;
		}
		
		//获取制程不良部门参数
		public List getZcblDepListPrc(String company,String facoty,int type,String keyword) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  prc_mes_cof_org_chs (?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, facoty);//facoty
					cs.setString(2, company);//company
					cs.setInt(3, type);//唯一硬件标识/ip
					cs.setString(4, keyword);//user_id
					cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(7, -10);// 输出参数 追溯数据
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					List<Object> result = new ArrayList<>();
					List<Map<String, Object>> l = new ArrayList();
					cs.execute();
					result.add(cs.getInt(5));
					result.add(cs.getString(6));
					if (cs.getString(5).toString().equals("0")) {
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(7);

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
		
		// 获取制程不良看板信息
		public List getZcblListPrc(String company,String facoty,String user_id, String class_id,
				String dep_id, String sdata, String dev_ip) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  prc_mes_rpt_zcbl (?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, facoty);//facoty
					cs.setString(2, company);//company
					cs.setString(3, dev_ip);//唯一硬件标识/ip
					cs.setString(4, user_id);//user_id
					cs.setString(5, class_id);//班次ID
					cs.setString(6, sdata);//日期
					cs.setString(7, dep_id);//部门ID
					cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(10, -10);// 输出参数 追溯数据
					cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(13, java.sql.Types.VARCHAR);// 输出参数 返回标识
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					List<Object> result = new ArrayList<>();
					List<Map<String, Object>> l = new ArrayList();
					cs.execute();
					result.add(cs.getInt(8));
					result.add(cs.getString(9));
					if (cs.getString(8).toString().equals("0")) {
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(10);

						try {
							l = fitMap(rs);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result.add(l);
						result.add(cs.getString(11));
						result.add(cs.getString(12));
						result.add(cs.getString(13));
					}
					System.out.println(l);
					return result;
				}
			});
			return resultList;
		}
		
		
		//获取效率排名看板
		public List getXlpmListPrc(String company,String facoty,String user_id, String class_id,
				String dep_id, String sdata, String dev_ip,String liner) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  PRC_MES_RPT_XLPM(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, company);//-公司
					cs.setString(2, facoty);//工厂
					cs.setString(3, class_id);// 班次
					cs.setString(4, dep_id);//--部门ID*
					cs.setString(5, liner);//-组长*
					cs.setString(6, sdata);//日期*
					cs.setString(7, dev_ip);//电视IP或mac*
					cs.setString(8, user_id);//用户id
					cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(11, -10);// 输出参数 追溯数据
					cs.registerOutParameter(12, -10);// 输出参数 追溯数据
					cs.registerOutParameter(13, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(14, java.sql.Types.VARCHAR);// 输出参数 返回标识
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					List<Object> result = new ArrayList<>();
					List<Map<String, Object>> l = new ArrayList();
					List<Map<String, Object>> l_2 = new ArrayList();
					cs.execute();
					result.add(cs.getInt(9));
					result.add(cs.getString(10));
					if (cs.getString(9).toString().equals("0")) {
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(11);
						try {
							l = fitMap(rs);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result.add(l);
						
						ResultSet rs_2 = (ResultSet) cs.getObject(12);
						try {
							l_2 = fitMap(rs_2);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						result.add(l_2);
						result.add(cs.getString(13));
						result.add(cs.getString(14));
					}
					System.out.println(l);
					return result;
				}
			});
			return resultList;
		}
		/*
		 * 获取线长
		 * */
		public List getLinerPrc(String company,String facoty) throws Exception {
			List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String storedProc = "{call  prc_mes_cof_org_chs (?,?,?,?,?,?,?)}";// 调用的sql
					CallableStatement cs = con.prepareCall(storedProc);
					cs.setString(1, company);//company
					cs.setString(2, facoty);//facoty
					cs.setString(3, "");//组织机构级别 - pi_org_leve
					cs.setString(4, "组长");//选择数据-pi_condition
					cs.registerOutParameter(5, java.sql.Types.INTEGER);// 输出参数 返回标识
					cs.registerOutParameter(6, java.sql.Types.VARCHAR);// 输出参数 返回标识
					cs.registerOutParameter(7, -10);// 输出参数 追溯数据
					return cs;
				}
			}, new CallableStatementCallback() {
				public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
					List<Object> result = new ArrayList<>();
					List<Map<String, Object>> l = new ArrayList();
					cs.execute();
					result.add(cs.getInt(5));
					result.add(cs.getString(6));
					if (cs.getString(5).toString().equals("0")) {
						// 游标处理
						ResultSet rs = (ResultSet) cs.getObject(7);

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
		
		// 获取产线电子看板
				public List getCxdzListtPrc(String company,String facoty,String user_id, String class_id,
						String dep_id, String sdata, String dev_ip,String liner) throws Exception {
					List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
						@Override
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							String storedProc = "{call  PRC_MES_RPT_CXDZ (?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
							CallableStatement cs = con.prepareCall(storedProc);
							cs.setString(1, facoty);//facoty
							cs.setString(2, company);//company
							cs.setString(3, class_id);//班次ID
							cs.setString(4, dep_id);//部门ID
							cs.setString(5, liner);//组长
							cs.setString(6, sdata);//日期
							cs.setString(7, dev_ip);//电视IP或mac
							cs.setString(8, user_id);//部门ID
							cs.registerOutParameter(9, java.sql.Types.INTEGER);// 输出参数 返回标识
							cs.registerOutParameter(10, java.sql.Types.VARCHAR);// 输出参数 返回标识
							cs.registerOutParameter(11, -10);// 输出参数 追溯数据
							cs.registerOutParameter(12,  -10);// 输出参数 返回标识
							cs.registerOutParameter(13, java.sql.Types.VARCHAR);// 输出参数 返回标识
							cs.registerOutParameter(14, java.sql.Types.VARCHAR);// 输出参数 返回标识
							return cs;
						}
					}, new CallableStatementCallback() {
						public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
							List<Object> result = new ArrayList<>();
							List<Map<String, Object>> l = new ArrayList();
							List<Map<String, Object>> l2 = new ArrayList();
							cs.execute();
							result.add(cs.getInt(9));
							result.add(cs.getString(10));
							if (cs.getString(9).toString().equals("0")) {
								// 游标处理
								ResultSet rs = (ResultSet) cs.getObject(11);
								try {
									l = fitMap(rs);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								result.add(l);
								
								ResultSet rs1 = (ResultSet) cs.getObject(12);
								try {
									l2 = fitMap(rs1);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								result.add(l2);
								
								result.add(cs.getString(13));
								result.add(cs.getString(14));
							}
							return result;
						}
					});
					return resultList;
				}	
				// 获取待返工看板信息
				public List getDfgListPrc(String company,String facoty,String user_id, String class_id,
						String dep_id, String sdata, String dev_ip) throws Exception {
					List resultList = (List) jdbcTemplate.execute(new CallableStatementCreator() {
						@Override
						public CallableStatement createCallableStatement(Connection con) throws SQLException {
							String storedProc = "{call  PRC_MES_RPT_DFG (?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
							CallableStatement cs = con.prepareCall(storedProc);
							cs.setString(1, facoty);//facoty
							cs.setString(2, company);//company
							cs.setString(3, dev_ip);//唯一硬件标识/ip
							cs.setString(4, user_id);//user_id
							cs.setString(5, class_id);//班次ID
							cs.setString(6, sdata);//日期
							cs.setString(7, dep_id);//部门ID
							cs.registerOutParameter(8, java.sql.Types.INTEGER);// 输出参数 返回标识
							cs.registerOutParameter(9, java.sql.Types.VARCHAR);// 输出参数 返回标识
							cs.registerOutParameter(10, -10);// 输出参数 追溯数据
							cs.registerOutParameter(11, java.sql.Types.VARCHAR);// 输出参数 返回标识
							cs.registerOutParameter(12, java.sql.Types.VARCHAR);// 输出参数 返回标识
							cs.registerOutParameter(13, java.sql.Types.VARCHAR);// 输出参数 返回标识
							return cs;
						}
					}, new CallableStatementCallback() {
						public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
							List<Object> result = new ArrayList<>();
							List<Map<String, Object>> l = new ArrayList();
							cs.execute();
							result.add(cs.getInt(8));
							result.add(cs.getString(9));
							if (cs.getString(8).toString().equals("0")) {
								// 游标处理
								ResultSet rs = (ResultSet) cs.getObject(10);

								try {
									l = fitMap(rs);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								result.add(l);
								result.add(cs.getString(11));
								result.add(cs.getString(12));
								result.add(cs.getString(13));
							}
							System.out.println(l);
							return result;
						}
					});
					return resultList;
				}
	//-------------------------------------	
		
	private List<Map<String, Object>> fitMap(ResultSet rs) throws Exception {
		List<Map<String, Object>> list = new ArrayList<>();
		if (null != rs) {
			Map<String, Object> map;
			int colNum = rs.getMetaData().getColumnCount();
			List<String> columnNames = new ArrayList<String>();
			for (int i = 1; i <= colNum; i++) {
				columnNames.add(rs.getMetaData().getColumnName(i));
			}
			while (rs.next()) {
				map = new HashMap<String, Object>();
				for (String columnName : columnNames) {
					map.put(columnName, rs.getString(columnName));
				}
				list.add(map);
			}
		}
		return list;
	}
	
	
	
				
}
