/**
 * 上线确认
 */
var pageCurr,localtableFilterIns;
var tabledata = [];
$(function() {
	layui
			.use(
					[ 'table', 'form', 'layedit', 'laydate', 'tableSelect','tableFilter' ],
					function() {
						var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table, table1 = layui.table,
							laydate = layui.laydate, tableSelect = layui.tableSelect, tableSelect1 = layui.tableSelect,tableFilter = layui.tableFilter;

						tableIns = table
								.render({
									elem : '#colTable'
									// ,url:context+'/interfaces/getRequestList'
									,
									where : {},
									method : 'get' // 默认：get请求
									// ,toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
									,
									defaultToolbar : [],
									cellMinWidth : 80,
									height:'full-70'//固定表头&full-查询框高度
									,even:true,//条纹样式
									page : true,
									limit:200,
									limits:[200,400,600,1000],
									data : [],
									request : {
										pageName : 'page' // 页码的参数名称，默认：page
										,
										limitName : 'rows' // 每页数据量的参数名，默认：limit
									},
									parseData : function(res) {
										console.log(res)
										// 可进行数据操作
										return {
											"count" : res.data.total,
											"msg" : res.msg,
											"data" : res.data.rows,
											"code" : res.status
										// code值为200表示成功
										}
									},
									cols : [ [ {
										type : 'checkbox',
										width : 50
									}, {
										field : 'EMP_CODE',
										title : '工号',
										width : 100,
										sort : true
									}, {
										field : 'EMP_NAME',
										title : '姓名',
										width : 100,
										sort : true
									}, {
										field : 'TIME_BEGIN',
										title : '上线时间',
										width : 150,
										sort : true
									}, {
										field : 'DEV_IP',
										title : '卡机IP',
										width : 120,
										sort : true
									}, {
										field : 'FMEMO',
										title : '备注',
									// width : 180
									} ] ],
									done : function(res, curr, count) {
										// console.log(res)
										pageCurr = curr;

										for (j = 0, len = res.data.length; j < len; j++) {
											// console.log(res.data[j])
											if (res.data[j].CHECK_STATUS == '1') {
												res.data[j]["LAY_CHECKED"] = 'true';
												// 下面三句是通过更改css来实现选中的效果
												var index = res.data[j]['LAY_TABLE_INDEX'];
												$(
														'tr[data-index='
																+ index
																+ '] input[type="checkbox"]')
														.prop('checked', true);
												$(
														'tr[data-index='
																+ index
																+ '] input[type="checkbox"]')
														.next()
														.addClass(
																'layui-form-checked');
											}
										}
									}
								});

						localtableFilterIns = tableFilter.render({
							'elem' : '#colTable',
							'mode' : 'local',//本地过滤
							'filters' : [
								{field: 'EMP_CODE', type:'input'},
								{field: 'EMP_NAME', type:'input'},
								{field: 'DEV_IP', type:'checkbox'},
							],
							'done': function(filters){
							}
						})

						tableSelect = tableSelect.render({
							elem : '#num',
							searchKey : 'keyword',
							checkedKey : 'id',
							searchPlaceholder : '试着搜索',
							table : {
								url : context + '/verify/getTaskNo',
								// url: context +'base/prodproc/getProdList',
								method : 'get',
								width : 800,
								cols : [ [
									{fixed:'left',type:'numbers',title:"序号"},
									{fixed:'left',
									type : 'radio',
									
								},// 多选 radio
								 {fixed:'left',field : 'id', title : 'id', width : 0, hide : true},
									{fixed:'left',field : 'PROD_DATE', title : '生产日期', width : 100, sort : true,
										templet:function (d) {
											if(d.PROD_DATE!=null){
												return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
											}
										}
									},
									{fixed:'left',field : 'LINER_NAME', title : '组长', width : 70, sort : true},
									 {fixed:'left',field : 'ITEM_NO', title : '物料编码', width : 145, sort : true},
									{field : 'TASK_NO', title : '制令单号', width : 150, sort : true},
									{field : 'WS_SECTION', title : '工段', width : 60},
									{field : 'FMEMO', title : '备注', width : 80},
									{field : 'CUST_NAME_S', title : '客户', width : 80, sort : true} ,
									{field : 'ITEM_NAME', title : '物料描述', width : 260, sort : true}] ],
								page : false,
								request : {
									pageName : 'page' // 页码的参数名称，默认：page
									,
									limitName : 'rows' // 每页数据量的参数名，默认：limit
								},
								parseData : function(res) {
									if (res.result) {
										// 可进行数据操作
										return {
											"count" : 0,
											"msg" : res.msg,
											"data" : res.data,
											"code" : res.status
										// code值为200表示成功
										}
									}
								},
							},
							done : function(elem, data) {
								// 选择完后的回调，包含2个返回值
								// elem:返回之前input对象；data:表格返回的选中的数据 []
								var da = data.data;
								var prodDate = /\d{4}-\d{1,2}-\d{1,2}/g.exec(da[0].PROD_DATE);
								if(new Date().toDateString() === new Date(prodDate).toDateString()){
									form.val("itemFrom", {
										"num" : da[0].TASK_NO,
										"mtrcode" : da[0].ITEM_NO,
										// "mtrdescr" : da[0].ITEM_NAME,
										"cus" : da[0].CUST_NAME_S,
										"linerName":da[0].LINER_NAME
									});
									form.render();// 重新渲染
								}else {
									layer.confirm("选择的计划日期不是今天,是否确认继续操作", {
										btn: ["取消","确定"] //按钮
									}, function(){
										layer.closeAll('dialog');
									}, function(){
										form.val("itemFrom", {
											"num" : da[0].TASK_NO,
											"mtrcode" : da[0].ITEM_NO,
											// "mtrdescr" : da[0].ITEM_NAME,
											"cus" : da[0].CUST_NAME_S,
											"linerName":da[0].LINER_NAME
										});
										form.render();// 重新渲染
									});
								}
							}
						});

						laydate.render({
							elem : '#ptimeInput', trigger: 'click',
							type : 'time' // 默认，可不填
						});


						form.on('select(on_time)', function (data) {   //选择移交单位 赋值给input框
							$("#ptimeInput").val(data.value);
							$("#on_time").next().find("dl").css({ "display": "none" });
							form.render();
						});


						liaoTableIns = tableSelect1.render({
							elem : '#pliao',
							searchKey : 'keyword',
							checkedKey : 'ITEM_NO',
							searchPlaceholder : '试着搜索',
							page : true,
							request : {
								pageName : 'page' // 页码的参数名称，默认：page
								,
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							table : {
								url : context + '/verify/getReworkItem',
								method : 'get',
								cols : [ [ {
									type : 'radio'
								},// 多选 radio
								 {
									field : 'ITEM_NO',
									title : '物料编号',
									width : 150,
									sort : true
								}, {
									field : 'ITEM_NAME',
									title : '物料描述',
									width : 400
								},
									{
										field : 'ITEM_NAME_S',
										title : '物料简称',
										width : 110,
										sort : true
									},
								] ],
								parseData : function(res) {
									//console.log(res)
									if (res.result) {
										// 可进行数据操作
										return {
											"count" : res.data.total,
											"msg" : res.msg,
											"data" : res.data.rows,
											"code" : res.status
										// code值为200表示成功
										}
									}
								},
							},
							done : function(elem, data) {
								var da = data.data;
								form.val("createForm", {
									"pliao" : da[0].ITEM_NO,
									"itemName":da[0].ITEM_NAME,
								});
								form.render();// 重新渲染
							}
						});

						laydate.render({
							elem : '#on_date',
							trigger : 'click'// 呼出事件改成click
							,type: 'date',
							value:new Date()
							//value : getCurDate(0)
						});

						laydate.render({
							elem : '#pdate1',
							trigger : 'click'// 呼出事件改成click
							,
							value : getCurDate(0)
						});

						// 监听
						form.on('submit(update)', function(data) {
							update(data.field.pline);// 获取打卡数据
						});

						form.on('submit(save)', function(data) {
							if ($('#num').val() != '') {
								// save(data.field);//保存
								var list = table.checkStatus('colTable').data;// 被选中行的数据
																				// id

								if (list.length == 0) {
									layer.alert("请至少选择一位人员上线")
									return false;
								}

								var empList = "";
								for (var i = 0; i < list.length; i++) {// 获取被选中的行
									empList += list[i].ID + ","// 用“；”分隔
								}
								save(data.field, empList);// 保存
							} else {
								layer.alert('请先选择制令单', {
									icon : 2,
									skin : 'layer-ext-moon'
								})
							}

						});

						// 线体选中
						form.on('select(pline)', function(data) {
							getUserByLine(data.value);
						});

						$('#barcode').bind('keypress', function(event) {
							if (event.keyCode == "13") {
								// alert('你输入的内容为：' + $('#barcode').val());
								if ($('#barcode').val()) {
									getInfoBarcode($('#barcode').val())
								} else {
									layer.alert("请先扫描条码!",function () {
										$('#barcode').focus();
										layer.closeAll();
									});
								}
							}
						});

						getInfoAdd();
						if(openType==1){
							open(1);
						}
						$(document).on('click', '#addBtn', function() {
							open(0);
						});
						getOnTime();
						form.on('submit(add)', function(data) {
							add(data.field);// 保存创建在线返工制令单
							return false;
						});

						// 监听提交
						form.on('submit(hsearchSubmit)', function(data) {
							hTableIns.reload({
								url : context + '/verify/getHistoryList',
								where : data.field
							});
							return false;
						});
						hTableIns = table1.render({// 历史
							elem : '#hcolTable',
							where : {},
							method : 'get',// 默认：get请求
							defaultToolbar : [],
							page : true,
							data : [],
							height:'full-70'//固定表头&full-查询框高度
								,even:true,//条纹样式
							request : {
								pageName : 'page', // 页码的参数名称，默认：page
								limitName : 'rows' // 每页数据量的参数名，默认：limit
							},
							parseData : function(res) {// 可进行数据操作
								return {
									"count" : res.data.total,
									"msg" : res.msg,
									"data" : res.data.rows,
									"code" : res.status
								// code值为200表示成功
								}
							},
							cols : [ [ {
								type : 'numbers'
							}, {
								field : 'TASK_NO',
								title : '制令单号',
								width : 160,
								sort : true
							},
								{
									field : 'ITEM_NO',
									title : '物料编号',
									width : 150,
									sort : true
								},
								{
									field : 'LINER_NAME',
									title : '组长',
									width : 90,
									sort : true
								},
								{
								field : 'LINE_NAME',
								title : '线体',
								width : 90,
								sort : true
							},
							// 	{
							// 	field : 'DEV_IP',
							// 	title : '卡机IP',
							// 	width : 150,
							// 	sort : true
							// },
								{
								field : 'EMP_NAME',
								title : '员工姓名',
								width : 110,
								sort : true
							},
								{
								field : 'TIME_BEGIN',
								title : '上线时间',
								width : 150,
								sort : true
							},
								{
									field : 'TIME_END',
									title : '下线时间',
									width : 145,
									sort : true
								},
								{
									field : 'CREATE_DATE',
									title : '分配时间',
									width : 150,
									sort : true
								},
								{
									field : 'PROD_DATE',
									title : '生产日期',
									width : 150,
									sort : true
								},
							] ],
							done : function(res, curr, count) {
								pageCurr = curr;
							}
						});

					});

});

function getOnTime() {
	CoreUtil.sendAjax("/verify/getOnTime", {}, function(data) {
		// console.log(data)
		if (data.result) {
				$("#on_time").empty();
				var pline = data.data;
				$("#ptimeInput").val(pline[0].SUB_NAME);
				for (var i = 0; i < pline.length; i++) {
					$("#on_time").append(
						"<option value=" + pline[i].SUB_NAME + ">"
						+ pline[i].SUB_NAME + "</option>");
				}
				layui.form.render('select');

		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

function searchInput() {
	var value = $("#ptimeInput").val();
	$("#on_time").val(value);
	layui.form.render();
	$("#on_time").next().find("dl").css({ "display": "block" });
	var dl = $("#on_time").next().find("dl").children();
	var j = -1;
	for (var i = 0; i < dl.length; i++) {
		if (dl[i].innerHTML.indexOf(value) <= -1) {
			dl[i].style.display = "none";
			j++;
		}
		if (j == dl.length-1) {
			$("#on_time").next().find("dl").css({ "display": "none" });
		}
	}
}

function getInfoAdd() {
	CoreUtil.sendAjax("/verify/getInfoAdd", {}, function(data) {
		// console.log(data)
		if (data.result) {
			if (data.data.Class) {
				$("#pclass").empty();
				var pclass = data.data.Class;
				for (var i = 0; i < pclass.length; i++) {
					$("#pclass").append(
							"<option value=" + pclass[i].ID + ">"
									+ pclass[i].CLASS_NAME + "</option>");
				}
				layui.form.render('select');
			}
			if (data.data.Line) {
				$("#pline").empty();
				var pline = data.data.Line;
				for (var i = 0; i < pline.length; i++) {
					if (i == 0) {
						$("#pline").append("<option value=''>请选择线体</option>");
					}
					$("#pline").append(
							"<option value=" + pline[i].ID + ">"
									+ pline[i].LINE_NAME + "</option>");
				}
				layui.form.render('select');
			}
		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

function update(lineId) {
	CoreUtil.sendAjax("/produce/card_data/updateDataByLine", {
		"line_ids" : lineId
	}, function(data) {

		layer.alert(data.msg, function() {
			getUserByLine(lineId);
			layer.closeAll();
		});
		/*
		 * if (data.result) {
		 *  } else { layer.alert(data.msg, function() { layer.closeAll(); }); }
		 */
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

function save(params, emp_ids) {
	console.log(params);
	params.ptime = $("#ptimeInput").val();
	var param = {
		"task_no" : params.num,
		"line_id" : params.pline,
		"hour_type" : params.ptyle,
		"class_id" : params.pclass,
		"wdate" : params.pdate + " "+ params.ptime,
		// "wdate" : params.pdate,
		"emp_ids" : emp_ids
	};
	CoreUtil.sendAjax("/verify/save", JSON.stringify(param), function(data) {
		if(data.result){
			layer.alert(data.msg, function() {
				getUserByLine(params.pline);
				layer.closeAll();
			});
		}else {
			playMusic();
			layer.alert(data.msg, function() {
				getUserByLine(params.pline);
				layer.closeAll();
			});
		}

	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function getUserByLine(lineId) {

	var scrollTop;
	var scrollLeft;
	var layuitable = null;
	var dev_obj = $("#table_and_page_div_id")//定位到表格
	if (dev_obj != null) {//防止未获取到表格对象
		layuitable =dev_obj[0].getElementsByClassName("layui-table-main");//定位到layui-table-main对象
	}
	if (layuitable != null && layuitable.length > 0) {
		scrollTop =layuitable[0].scrollTop; //layuitable获取到的是class=layui-table-main的集合，所以直接获取其中的scrollTop属性。
		// scrollLeft=layuitable[0].scrollLeft;
	}

	tableIns.reload({
		url : context + '/verify/getUserByLine?lineId=' + lineId
		,done: function (res, curr, count) {
			//滚轮控制
			// pageCurr=curr;
			dev_obj = $("#table_and_page_div_id")//定位到表格
			if (dev_obj != null) {
				layuitable =dev_obj[0].getElementsByClassName("layui-table-main");
			}
			if (layuitable != null && layuitable.length > 0) {//将属性放回去
				layuitable[0].scrollTop = scrollTop;
				// layuitable[0].scrollLeft = scrollLeft;
			}
		}

	});

	/*
	 * CoreUtil.sendAjax("verify/getUserByLine", {"lineId" : lineId}, function(
	 * data) { console.log(data); if (data.result) { tableIns.reload({
	 * url:context+'verify/getUserByLine?lineId='+lineId,
	 * 
	 * }); } else { layer.alert(data.msg, function() { layer.closeAll(); }); } },
	 * "GET", false, function(res) { layer.alert(res.msg); });
	 */
}
function open(openType) {
	CoreUtil.sendAjax("/verify/getInfoCreateReturn", "", function(data) {
		console.log(data)
		if (data.result) {
			/*
			 * if(data.data.Task){ $("#ptask").empty(); var
			 * ptask=data.data.Task; for (var i = 0; i < ptask.length; i++) {
			 * if(i == 0){ $("#ptask").append("<option value=''>请选择单号</option>"); }
			 * $("#ptask").append("<option value=" + ptask[i].TASK_NO+ ">" +
			 * ptask[i].TASK_NO + "</option>"); } layui.form.render('select'); }
			 */
			/*if (data.data.Liao) {	
				 * $("#pliao").empty(); var pliao=data.data.Liao; for (var i =
				 * 0; i < pliao.length; i++) { if(i == 0){ $("#pliao").append("<option
				 * value=''>请选择料号</option>"); } $("#pliao").append("<option
				 * value=" + pliao[i].ITEM_NO+ ">" + pliao[i].ITEM_NO + "</option>"); }
				 * layui.form.render('select');			 
			}*/

			if (data.data.Class) {
				$("#pclass2").empty();
				var pclass = data.data.Class;
				for (var i = 0; i < pclass.length; i++) {
					$("#pclass2").append(
						"<option value=" + pclass[i].ID + ">"
						+ pclass[i].CLASS_NAME + "</option>");
				}
				layui.form.render('select');
			}

			if (data.data.Dept) {
				$("#deptId").empty();
				var dept = data.data.Dept;
				for (var i = 0; i < dept.length; i++) {
					$("#deptId").append(
						"<option value=" + dept[i].ID + ">"
						+ dept[i].LEAD_BY + "</option>");
				}
				layui.form.render('select');
			}

			if (data.data.User) {
				$("#puser").empty();
				var puser = data.data.User;
				for (var i = 0; i < puser.length; i++) {
					if (i == 0) {
						$("#puser").append("<option value=''>请选择组长</option>");
					}
					$("#puser").append(
							"<option value=" + puser[i].LEAD_BY + ">"
									+ puser[i].LEAD_BY + "</option>");
				}
				layui.form.render('select');
			}
			if(openType!=1) {
				var index = layer.open({
					type: 1,
					title: '创建在线返工制令单',
					fixed: false,
					resize: false,
					shadeClose: true,
					area: ['650px'],
					content: $('#createDiv'),
					end: function () {
						cleanForm();
					}
				});
				layer.full(index);
			}
		} else {
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
	return false;
}
function add(params) {
	console.log(params)
	// "task_no":params.ptask--2020/11/03废除工单号
	var param = {
		"task_no" : "",
		"item_no" : params.pliao,
		"liner_name" : params.puser,
		"qty" : params.qty,
		"pdate" : params.pdate1,
		"deptId" : params.deptId,
		"classId" : params.pclass2
	};
	CoreUtil.sendAjax("/verify/add", JSON.stringify(param), function(data) {
		if(data.result){
			layer.alert(data.msg, function() {
				layer.closeAll();
			});
		}else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}
// 清空新增表单数据
function cleanForm() {
	$('#createForm')[0].reset();
	layui.form.render();// 必须写
}

function getInfoBarcode(barcode) {
	// console.log(taskNo);
	var params = {
		"barcode" : barcode
	}
	CoreUtil.sendAjax("/verify/getInfoBarcode", params, function(data) {
		if (data.result) {
			$("input[name='pliao']").val(data.data[0].ITEM_NO);
			$("input[name='itemName']").val(data.data[0].ITEM_NAME);
		} else {
			playMusic();
			layer.alert(data.msg,function (index) {
				$('#barcode').val('');
				$('#pliao').val('');
				$('#itemName').val('');
				$('#barcode').focus();
				layer.close(index);
			});
			// $('#barcode').val('');
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}