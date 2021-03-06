/**
 * 工序管理
 */
var pageCurr;
$(function() {
	layui.use([ 'form', 'table','tableFilter' ], function() {
		var table = layui.table, form = layui.form,tableFilter = layui.tableFilter;

		tableIns = table.render({
			elem : '#listTable',
			url : context + '/base/proc/getList',
			method : 'get' // 默认：get请求
			,
			cellMinWidth : 80,
			toolbar: '#toolbar', //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
			height: 'full-80'
			,even:true//条纹样式
			,page : true,
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
			cols : [ [ {
				type : 'numbers'
			},
				{type:'checkbox'}
			// ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
			, {
				field : 'procNo',
				title : '工序编码'
			}, {
				field : 'procName',
				title : '工序名称'
			},{
				field : 'procOrder',
				title : '工序排序'
			}, {
				field : 'checkStatus',
				title : '状态',
				width : 95,
				templet : '#statusTpl'
			}, {
				field : 'lastupdateDate',width : 145,
				title : '更新时间'
			}, {
				field : 'createDate',width : 145,
				title : '添加时间',
			}, {
				fixed : 'right',
				title : '操作',
				align : 'center',
				toolbar : '#optBar'
			} ] ],
			done : function(res, curr, count) {
				// 如果是异步请求数据方式，res即为你接口返回的信息。
				// 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
				// console.log(res);
				// 得到当前页码
				// console.log(curr);
				// 得到数据总量
				// console.log(count);
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
				{field: 'procNo', type:'input'},
				{field: 'procName', type:'input'},
				{field: 'checkStatus', type:'radio'},
				{field: 'procOrder', type:'input'},
				{field: 'lastupdateDate', type:'date'},
				{field: 'createDate', type:'date'},
				// {field: 'linerCode', type:'input'},
				// {field: 'lineName', type:'input'},
				/*{field: 'id', type:'input'},
				{field: 'date', type:'date'},
				{field: 'username', type:'checkbox', url:'json/filter.json'},
				{field: 'sex', type:'radio'},
				{field: 'class', type:'checkbox', data:[{ "key":"12", "value":"十二班"}]}*/
			],
			'done': function(filters){}
		})

		//监听行双击查看事件
		table.on('rowDouble(listTable)', function(obj){
			getProc(obj.data, obj.data.id);
		});

		//头工具栏事件
		table.on('toolbar(listTable)', function(obj){
			var checkStatus = table.checkStatus(obj.config.id);
			switch(obj.event){
				case 'doAdd':
					addProc();
					break;
				case 'doDelete':
					var data = checkStatus.data;
					// console.log(data)
					if(data.length == 0){
						layer.msg("请先勾选数据!");
					}else{
						var id="";
						for(var i = 0; i < data.length; i++) {
							id += data[i].id+",";
							// console.log(data[i])
						}
						delLine(id);
					}

					break;
			};
		});

		// 监听操作
		form.on('switch(isStatusTpl)', function(obj) {
			setStatus(obj, this.value, this.name, obj.elem.checked);
		});
		// 监听工具条
		table.on('tool(listTable)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProc(data, data.id, data.procNo);
			} else if (obj.event === 'edit') {
				// 编辑
				getProc(data, data.id);
			}
		});
		form.verify({
		  order: [
		   /^\+?[1-9][0-9]*$/
		    ,'工序顺序应大于0且不含小数点'
		  ] 
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
		// 编辑工序
		function getProc(obj, id) {
			var param = {
				"id" : id
			};
			CoreUtil.sendAjax("/base/proc/getProcess", JSON.stringify(param),
					function(data) {
						if (data.result) {
							form.val("procForm", {
								"id" : data.data.id,
								"procNo" : data.data.procNo,
								"procName" : data.data.procName,
								"procOrder":data.data.procOrder
							});
							openProc(id, "编辑工序")
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
			layer.confirm('您确定要把工序：' + name + '设置为' + deaprtisStatus + '状态吗？',
					{
						btn1 : function(index) {
							var param = {
								"id" : id,
								"checkStatus" : isStatus
							};
							CoreUtil.sendAjax("/base/proc/doStatus", JSON
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

// 新增编辑弹出框
function openProc(id, title) {
	if (id == null || id == "") {
		$("#id").val("");
		$('#procNo').removeAttr("readonly");
		$('#procNo').removeClass("grey");
	}else {
		$('#procNo').addClass("grey");
		$('#procNo').attr("readonly","readonly");
	}
	var index=layer.open({
		type : 1,
		title : title,
		fixed : false,
		resize : false,
		shadeClose : true,
		area : [ '550px' ],
		content : $('#setProc'),
		end : function() {
			cleanProc();
		}
	});
	 layer.full(index);
}

function doDelete(){
	/*var check_id = tableIns.checkStatus('#lineList');
	alert(check_id)*/
	var checkStatus = tableIns.checkStatus("lineList");
}

// 添加工序
function addProc() {
	// 清空弹出框数据
	cleanProc();
	// 打开弹出框
	openProc(null, "添加工序");
}
// 新增工序提交
function addSubmit(obj) {
	CoreUtil.sendAjax("/base/proc/add", JSON.stringify(obj.field),
			function(data) {
				if (data.result) {
					layer.alert("操作成功", function() {
						layer.closeAll();
						cleanProc();
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

// 编辑工序提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/base/proc/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.alert("操作成功", function() {
				layer.closeAll();
				cleanProc();
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

// 删除工序
function delProc(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除' + name + '工序吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/base/proc/delete", JSON.stringify(param),
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
function cleanProc() {
	$('#procForm')[0].reset();
	layui.form.render();// 必须写
}
