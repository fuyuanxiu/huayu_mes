/**
 * 制造部工艺管理
 * 五金:hardware
 * 注塑:molding
 * 表面处理:surface
 * 组装:packag
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','upload','tableSelect' ], function() {
		var table = layui.table, form = layui.form,upload = layui.upload,tableSelect = layui.tableSelect;

		tableIns = table.render({
			elem : '#productProcessList',
			url : context + '/productProcess/getList?bsType='+bsType+'&quoteId='+quoteId,
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			toolbar: '#toolbar',
			height:'full-110'//固定表头&full-查询框高度
			,even:true,//条纹样式
			page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				// 可进行数据操作
				return {
					"count" : res.data.total,
					"msg" : res.msg,
					"data" : res.data.rows,
					"code" : res.status
				// code值为200表示成功
				}
			},
			cols : [ [ {type : 'numbers',style:'background-color:#d2d2d2'},
				{field : 'bsName', width:150, title : '零件名称',sort:true,style:'background-color:#d2d2d2'},
				{field : 'bsOrder',width:150, title : '工艺顺序',sort:true,style:'background-color:#d2d2d2'},
				{field : 'proc', width:150, title : '工序名称',style:'background-color:#d2d2d2',
					templet:function (d) {
						if(d.proc!=null){
							return d.proc.procName==null||undefined?"":d.proc.procName;
						}else {
							return "";
						}
					}},
				{field : 'procfmemo', width:100, title : '工序说明',style:'background-color:#d2d2d2',
					templet:function (d) {
						if(d.proc!=null){
							return d.proc.fmemo==null||undefined?"":d.proc.fmemo;
						}else {
							return "";
						}
					}},
				{field : 'workcenterName', width:100, title : '工作中心',style:'background-color:#d2d2d2'
					,templet:function (d) {
						if(d.proc!=null){
							if(d.proc.bjWorkCenter!=null){
								return d.proc.bjWorkCenter.workcenterName==null||undefined?"":d.proc.bjWorkCenter.workcenterName;
							}else {
								return "";
							}
						}else {
							return "";
						}
					}},
				{field : 'bsModelType', width:100, title : '机台类型',edit:'text'},
				{field : 'bsRadix', title : '基数',edit:'text'},
				{field : 'bsUserNum', title : '人数',edit:'text'},
				{field : 'bsCycle', title : '成型周期(S)', width:150,edit:'text'},
				{field : 'bsYield', title : '工序良率%', width:120,edit:'text'},
				{field : 'bsCave', title : '穴数',edit:'text'},
				{field : 'bsCapacity', title : '产能',edit:'text'},
				{field : 'fmemo', title : '备注',edit:'text'},
				{fixed : 'right', title : '操作', align : 'center',width:120, toolbar : '#optBar'} ] ],
			done : function(res, curr, count) {
				pageCurr = curr;
			}
		});

		tableSelect=tableSelect.render({
			elem : '#procName',
			searchKey : 'keyword',
			checkedKey : 'id',
			searchPlaceholder : '关键字搜索',
			table : {
				url : context + '/basePrice/proc/getList',
					// ?pkQuote='+quoteId,
				method : 'get',

				parseData : function(res) {
					// 可进行数据操作
					return {
						"count" : res.data.total,
						"msg" : res.msg,
						"data" : res.data.rows,
						"code" : res.status
						// code值为200表示成功
					}
				},
				cols : [ [
					{ type: 'radio' },//单选  radio
					{field : 'id', title : 'id', width : 0,hide:true},
					{type : 'numbers'},
					{field : 'procNo', title : '工序编号' },
					{field : 'procName', title : '工序名称' },
					{field : 'workCenter', title : '工作中心名称' }
				] ],
				page : true,
				request : {
					pageName : 'page' // 页码的参数名称，默认：page
					,
					limitName : 'rows' // 每页数据量的参数名，默认：limit
				},

			},
			done : function(elem, data) {
				var da=data.data;
				//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
				form.val("productProcessForm", {
					"pkProc":da[0].id,
					"procName":da[0].procName
				});
				form.render();// 重新渲染
			}
		});
		// positiveNum
		//自定义验证规则
		form.verify({
			double: function(value){
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false && value!="" && value!=null)
				{
					return '用量/制品量 只能输入数字';
				}
			},
			positiveNum:function (value) {
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false || value<=0){
					return '基数只能输入数字且大于0';
				}

			}
		});

		table.on('edit(productProcessTable)',function (obj) {
			var bsRadix = obj.data.bsRadix;
			var bsUserNum = obj.data.bsUserNum;
			var bsYield = obj.data.bsYield;
			var bsCave = obj.data.bsCave;
			if(/^\d+$/.test(bsRadix)==false && /^\d+\.\d+$/.test(bsRadix)==false || bsRadix<=0)
			{
				layer.msg("基数必填且只能输入数字且大于0");
				loadAll();
				return false;
			}
			if(/^\d+$/.test(bsUserNum)==false && /^\d+\.\d+$/.test(bsUserNum)==false && bsUserNum!="" && bsUserNum!=null)
			{
				layer.msg("人数只能输入数字");
				loadAll();
				return false;
			}
			if(/^\d+$/.test(bsYield)==false && /^\d+\.\d+$/.test(bsYield)==false && bsYield!="" && bsYield!=null)
			{
				layer.msg("工序良率只能输入数字");
				loadAll();
				return false;
			}
			if(/^\d+$/.test(bsCave)==false && /^\d+\.\d+$/.test(bsCave)==false && bsCave!="" && bsCave!=null)
			{
				layer.msg("穴数只能输入数字");
				loadAll();
				return false;
			}
			obj.field = obj.data;
			editSubmit(obj);
		})

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(productProcessTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProdErr(data, data.id, data.bsName);
			} else if (obj.event === 'edit') {
				// 编辑
				getProdErr(data, data.id);
			}
		});
		// 监听提交
		form.on('submit(addSubmit)', function(data) {
			if (data.field.id == null || data.field.id == "") {
				// 新增
				addSubmit(data);
			} else {
				editSubmit(data);
			}
			return false;
		});
		// 监听搜索框
		form.on('submit(searchSubmit)', function(data) {
			// 重新加载table
			load(data);
			return false;
		});
		// 编辑五金工艺
		function getProdErr(obj, id) {
			console.log(obj);
			var procName="";
			if(obj.proc!=null){
				procName=obj.proc.procName
			}
			form.val("productProcessForm", {
				"id" : obj.id,
				"bsName" : obj.bsName,
				"procName" : procName,
				"pkProc":obj.pkProc,
				"bsOrder" : obj.bsOrder,
				"bsModelType" : obj.bsModelType,
				"bsRadix" : obj.bsRadix,
				"fmemo" : obj.fmemo,
				"bsUserNum":obj.bsUserNum,
				"bsCave":obj.bsCave,
				"bsCapacity":obj.bsCapacity,
				"bsCycle":obj.bsCycle,
				"bsYield":obj.bsYield,
			});
			openProdErr(id, "编辑工艺信息")
		};

		//导入
		upload.render({
			elem: '#upload'
			,url: context + '/productProcess/importExcel'
			,accept: 'file' //普通文件
			,data: {
				bsType: function(){
					return bsType;
				},
				pkQuote: function(){
					return quoteId;
				}
			}
			,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
				layer.load(); //上传loading
			}
			,done: function(res,index, upload){
				layer.closeAll('loading'); //关闭loading
				layer.alert(res.msg, function (index) {
					layer.close(index);
					loadAll();
				});

			}
			,error: function(index, upload){
				layer.alert("操作请求错误，请您稍后再试",function(){
				});
				layer.closeAll('loading'); //关闭loading
				layer.close(index);
			}
		});

	});

});

//模板下载
function  downloadExcel() {
	if(bsType =="hardware"){
		location.href = "../../excelFile/五金工艺模板.xlsx";
	}else if(bsType =="molding"){
		location.href = "../../excelFile/注塑工艺模板.xlsx";
	}else if(bsType =="surface"){
		location.href = "../../excelFile/表面处理工艺模板.xlsx";
	}else if(bsType =="packag"){
		location.href = "../../excelFile/组装工艺模板.xlsx";
	}
}

//导出数据
function exportExcel() {
	location.href = context + "/productProcess/exportExcel?bsType="+bsType+"&pkQuote="+quoteId;
}

// 新增编辑弹出框
function openProdErr(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
	}
	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setProdErr'),
		end : function() {
			cleanProdErr();
		}
	});
	layer.full(index);
}

// 添加五金工艺
function addHardware() {
	// 清空弹出框数据
	cleanProdErr();
	// 打开弹出框
	openProdErr(null, "添加工艺信息");
}
// 新增五金工艺提交
function addSubmit(obj) {
	obj.field.bsType = bsType;
	obj.field.pkQuote = quoteId;
	CoreUtil.sendAjax("/productProcess/add", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 编辑五金工艺提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/productProcess/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProdErr();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除五金工艺
function delProdErr(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除工艺：' + name + '吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/productProcess/delete", JSON.stringify(param),
					function(data) {
						if (isLogin(data)) {
							if (data.result == true) {
								// 回调弹框
								layer.alert("删除成功！", function() {
									layer.closeAll();
									// 加载load方法
									loadAll();
								});
							} else {
								layer.alert(data, function() {
									layer.closeAll();
								});
							}
						}
					});
		});
	}
}

// 重新加载表格（搜索）
function load(obj) {
	// 重新加载table
	tableIns.reload({
		where : {
			keyword : obj.field.keywordSearch
		},
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

// 重新加载表格（全部）
function loadAll() {
	// 重新加载table
	tableIns.reload({
		page : {
			curr : pageCurr
		// 从当前页码开始
		}
	});
}

// 清空新增表单数据
function cleanProdErr() {
	$('#productProcessForm')[0].reset();
	layui.form.render();// 必须写
}