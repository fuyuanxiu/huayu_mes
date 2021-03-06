/**
 * 线体管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableFilter' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter;

		tableIns = table.render({
			elem : '#listTable',
			url : context + '/base/line/getList',
			method : 'get' // 默认：get请求
			, toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			,cellMinWidth : 80
			,height:'full-80'//固定表头&full-查询框高度
		    ,even:true//条纹样式
			,page : true,
			request : {
				pageName : 'page' // 页码的参数名称，默认：page
				,
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {
				if(!res.result){
					return {
						"count" : 0,
						"msg" : res.msg,
						"data" : [],
						"code" : res.status
					} 
				}
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
				type : 'numbers'
			},
			{type:'checkbox'}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'lineNo',
				title : '线体编码', sort: true,filter: true
			}, {
				field : 'lineName',
				title : '线体名称', sort: true
			},
			{
				field : 'linerCode',
				title : '线长工号', sort: true
			}, {
				field : 'linerName',
				title : '线长姓名', sort: true
			}
			, {
				field : 'checkStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl', sort: true
			}, {
				field : 'lastupdateDate',width : 145,
				title : '更新时间', sort: true
			}, {
				field : 'createDate',width : 145,
				title : '添加时间', sort: true
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar'
			} ] ],
			done : function(res, curr, count) {
				localtableFilterIns.reload();
				pageCurr = curr;
			}
		});
		
		var localtableFilterIns = tableFilter.render({
			'elem' : '#listTable',
			//'parent' : '#doc-content',
			'mode' : 'api',//服务端过滤
			'filters' : [
				/*{field: 'lineNo', type:'checkbox'},*/
				{field: 'lineNo', type:'input'},
				{field: 'lastupdateDate', type:'date'},
				{field: 'checkStatus', type:'radio'},
				{field: 'linerName', type:'input'},
				{field: 'createDate', type:'date'},
				{field: 'linerCode', type:'input'},
				{field: 'lineName', type:'input'},
				/*{field: 'id', type:'input'},
				{field: 'date', type:'date'},
				{field: 'username', type:'checkbox', url:'json/filter.json'},
				{field: 'sex', type:'radio'},
				{field: 'class', type:'checkbox', data:[{ "key":"12", "value":"十二班"}]}*/
			],
			'done': function(filters){}
		})
		
		
		//头工具栏事件
		table.on('toolbar(listTable)', function(obj){
		    var checkStatus = table.checkStatus(obj.config.id);
		    switch(obj.event){
		      case 'doAdd':
		    	  addLine();
		      break;
		      case 'doDelete':
		        var data = checkStatus.data;
		        console.log(data)
		        if(data.length == 0){
		        	layer.msg("请先勾选数据!");
		        }else{
		        	var id="";
		        	for(var i = 0; i < data.length; i++) {
		        		id += data[i].id+",";
		        		console.log(data[i])
		        	}
		        	delLine(id);
		        }
		        
		      break;
		    };
		  });

		// 监听在职操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(listTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				
				layer.confirm('您确定要删除' + data.lineNo + '线体吗？', {
					btn : [ '确认', '返回' ]
				// 按钮
				}, function() {
					delLine(data.id);
				});
			} else if (obj.event === 'edit') {
				// 编辑
				getLine(data, data.id);
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
		// 编辑线体
		function getLine(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/base/line/getLine", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("lineForm", {
								"id" : data.data.id,
								"lineNo" : data.data.lineNo,
								"lineName" : data.data.lineName,
								"linerCode" : data.data.linerCode,
								"linerName" : data.data.linerName,
							});
							openLine(id, "编辑线体")
						} else {
							layer.alert(data.msg)
						}
					}, "POST", false, function(res) {
						layer.alert("操作请求错误，请您稍后再试");
					});
		}

		// 设置正常/禁用
		function setStatus(obj, id, name, checked) {
			var isStatus = checked ? 1 : 0;
			var deaprtisStatus = checked ? "正常":"禁用";
			// 正常/禁用
			layer.confirm('您确定要把线体：' + name + '设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/line/doStatus", JSON
									.stringify(param), function(data) {
								if (data.result) {
									layer.alert("操作成功", function() {
										layer.closeAll();
										loadAll();
									});
								} else {
									layer.alert(data.msg, function() {
										layer.closeAll();
									});
								}
							}, "POST", false, function(res) {
								layer.alert("操作请求错误，请您稍后再试", function() {

									layer.closeAll();
								});
							});
						},
						btn2 : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						},
						cancel : function() {
							obj.elem.checked = !isStatus;
							form.render();
							layer.closeAll();
						}
					});
		}

	});

});

// 设置用户正常/禁用
function setStatus(obj, id, name, checked) {
	var isStatus = checked ? 0 : 1;
	var deaprtisStatus = checked ? "正常" : "禁用";
	// 正常/禁用
	layer.confirm('您确定要把线体：' + name + '设置为' + deaprtisStatus + '状态吗？', {
		btn : [ '确认', '返回' ]
	// 按钮
	}, function() {
		var param = {
			"id" : id,
			"checkStatus" : isStatus
		};
		CoreUtil.sendAjax("/base/line/doStatus", JSON.stringify(param),
				function(data) {
					if (data.result) {
						layer.alert("操作成功", function() {
							layer.closeAll();
							loadAll();
						});
					} else {
						layer.alert(data.msg, function() {
							layer.closeAll();
						});
					}
				}, "POST", false, function(res) {
					layer.alert("操作请求错误，请您稍后再试", function() {
						layer.closeAll();
					});
				});
	});
}

// 新增编辑弹出框
function openLine(id, title) {
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
		content : $('#setLine'),
		end : function() {
			cleanLine();
		}
	});
	layer.full(index);//弹出框全屏
}

// 添加线体
function addLine() {
	// 清空弹出框数据
	cleanLine();
	// 打开弹出框
	openLine(null, "添加线体");
}
// 新增线体提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/base/line/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanLine();
						// 加载页面
						loadAll();
					});
				} else {
					layer.alert(data.msg, function() {
						layer.closeAll();
					});
				}
			}, "POST", false, function(res) {
				layer.alert(res.msg);
			});
}

// 编辑线体提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/base/line/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanLine();
				// 加载页面
				loadAll();
			});
		} else {
			layer.alert(data.msg, function() {
				layer.closeAll();
			});
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}
function doDelete(){
	/*var check_id = tableIns.checkStatus('#lineList');
	alert(check_id)*/
	var checkStatus = tableIns.checkStatus("lineList");
}
// 删除线体
function delLine(id) {
		var param = {
			"id" : id
		};
		CoreUtil.sendAjax("/base/line/delete", JSON.stringify(param),
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
function cleanLine() {
	$('#lineForm')[0].reset();
	layui.form.render();// 必须写
}
