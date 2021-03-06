/**
 * 产品价格维护
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
			elem : '#productPriceList',
			url : context + '/productMater/getList?quoteId='+quoteId,
			method : 'get' ,// 默认：get请求
			cellMinWidth : 80,
			//toolbar: '#toolbar',
			height:'full-95'//固定表头&full-查询框高度
			//,even:true,//条纹样式
			,page : true,
			limit:20,
			request : {
				pageName : 'page', // 页码的参数名称，默认：page
				limitName : 'rows' // 每页数据量的参数名，默认：limit
			},
			parseData : function(res) {// 可进行数据操作	
				return {
					"count" : res.data.total,
					"msg" : res.msg,
					"data" : res.data.rows,
					"code" : res.status// code值为200表示成功
				}
			},
			cols : [ [ 
			    {type : 'numbers',style:'background-color:#d2d2d2'}, 
				{field: 'bsType',width: 100,title: '类型', sort: true,style:'background-color:#d2d2d2',	
				templet: function (d) {
						if (d.bsType == 'hardware') {// * 五金:hardware
							return '五金'
						} else if (d.bsType == 'molding') {// * 注塑:molding
							return '注塑'
						}else if (d.bsType == 'surface') {// * 表面处理:surface
							return '表面处理'
						}else if (d.bsType == 'packag') {// * 组装:packag
							return '组装'
						}
					}
				},
				{field : 'bsComponent',width:150,title : '零/组件名称',sort:true,style:'background-color:#d2d2d2'}, 
				{field : 'bsMaterName',width:120,title : '材料名称',sort:true,style:'background-color:#d2d2d2'},
			    {field : 'bsModel',width:150,title : '材料规格',style:'background-color:#d2d2d2'},
			    {field : 'bsQty',width:80,title : '用量',style:'background-color:#d2d2d2'},
				{field : 'bsUnit',width:80,title : '单位',style:'background-color:#d2d2d2'},
				{field : 'bsRadix',width:80,title : '基数',style:'background-color:#d2d2d2'},
				{field : 'bsGeneral',width:120,title : '是否通用物料',style:'background-color:#d2d2d2'},
				{field : 'bsGear',width:80,title : '价格挡位',edit:'text',templet: '#selectGear',style : 'background-color:#ffffff'},
				{field : 'bsRefer',width:110,title : '参考价格',style:'background-color:#d2d2d2'},
				{field : 'bsAssess',width:110,title : '评估价格',edit:'number',style : 'background-color:#ffffff'},// placeholder:'请输入评估价格(数字)',
				{field : 'fmemo',width:110,title : '备注',edit:'text',style : 'background-color:#ffffff'},
				{field : 'bsSupplier',width:110,title : '供应商',edit:'text',style : 'background-color:#ffffff'}
				] ],
			done : function(res, curr, count) {
				pageCurr = curr;
				//不可填写的颜色变灰色
				res.data.forEach(function (item, index) {
					/*if(item.bsStatus=="99"){
						$('div[lay-id="listTable"]').find('tr[data-index="' + index + '"]').find('td[data-field="bsStatus"]').css('background-color', '#979797');
					}*/
				});
			}
		});
		
		//下拉框监听事件
		form.on('select(roleIdSelect)', function(data) {
			alert('1');
			return;
			//获取行tr对象
			var elem = data.othis.parents('tr');
	        //获取第一列的值，第一列为ID列，
			var id = elem.first().find('td').eq(1).text();
	        //选择的select对象值；
	        var selectValue=data.value;
			//处理字段更新的逻辑
		});

		//自定义验证规则
		form.verify({
			double: function(value){
				if(/^\d+$/.test(value)==false && /^\d+\.\d+$/.test(value)==false && value!="" && value!=null)
				{
					return '用量/制品量 只能输入数字';
				}
			}
		});

		table.on('edit(productPriceTable)',function (obj) {
			var bsAssess = obj.data.bsAssess;
			if(/^\d+$/.test(bsAssess)==false && /^\d+\.\d+$/.test(bsAssess)==false && bsAssess!="" && bsAssess!=null)
			{
				layer.msg("评估价格只能输入数字");
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
		table.on('tool(productPriceList)', function(obj) {
			var data = obj.data;
			if (obj.event === 'del') {
				// 删除
				delProdErr(data, data.id, data.bsComponent);
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
		// 编辑五金材料
		function getProdErr(obj, id) {
			form.val("hardwareForm", {
				"id" : obj.id,
				"bsComponent" : obj.bsComponent,
				"bsMaterName" : obj.bsMaterName,
				"bsModel" : obj.bsModel,
				"bsQty" : obj.bsQty,
				"bsUnit" : obj.bsUnit,
				"bsRadix" : obj.bsRadix,
				"bsSupplier" : obj.bsSupplier,
				"fmemo" : obj.fmemo,
				"bsWaterGap":obj.bsWaterGap,
				"bsCave":obj.bsCave,
				"bsMachiningType":obj.bsMachiningType,
				"bsColor":obj.bsColor,
			});
			openProdErr(id, "编辑材料信息")
		};

		//导入
		upload.render({
			elem: '#upload'
			,url: context + '/productMater/importExcel'
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
		location.href = "../../excelFile/五金材料模板.xlsx";
	}else if(bsType =="molding"){
		location.href = "../../excelFile/注塑材料模板.xlsx";
	}else if(bsType =="surface"){
		location.href = "../../excelFile/表面处理材料模板.xlsx";
	}else if(bsType =="packag"){
		location.href = "../../excelFile/组装材料模板.xlsx";
	}
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

// 添加五金材料
function addHardware() {
	// 清空弹出框数据
	cleanProdErr();
	// 打开弹出框
	openProdErr(null, "添加材料");
}
// 新增五金材料提交
function addSubmit(obj) {
	obj.field.bsType = bsType;
	obj.field.pkQuote = quoteId;
	CoreUtil.sendAjax("/productMater/add", JSON.stringify(obj.field), function(
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

// 编辑、提交
function editSubmit(obj) {
	CoreUtil.sendAjax("/productMater/edit", JSON.stringify(obj.field), function(
			data) {
		if (data.result) {
			layer.closeAll();
			loadAll();
		} else {
			layer.alert(data.msg);
		}
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

// 删除五金材料
function delProdErr(obj, id, name) {
	if (id != null) {
		var param = {
			"id" : id
		};
		layer.confirm('您确定要删除材料：' + name + '吗？', {
			btn : [ '确认', '返回' ]
		// 按钮
		}, function() {
			CoreUtil.sendAjax("/productMater/delete", JSON.stringify(param),
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
	$('#hardwareForm')[0].reset();
	layui.form.render();// 必须写
}
