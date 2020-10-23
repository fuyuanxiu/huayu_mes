/**
 * 上线确认
 */
var pageCurr;
var tabledata=[];
$(function() {
	layui.use(
			[ 'table', 'form', 'layedit', 'laydate', 'tableSelect' ],
			function() {
				var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table, laydate = layui.laydate, tableSelect = layui.tableSelect, tableSelect1 = layui.tableSelect;
				;

				tableIns = table.render({
					elem : '#colTable'
					//,url:context+'/interfaces/getRequestList'
					,
					where : {},
					method : 'get' //默认：get请求
					//,toolbar: '#toolbar' //开启头部工具栏，并为其绑定左侧模板
					,
					defaultToolbar : [],
					cellMinWidth : 80,
					page : false,
					data : [],
					request : {
						pageName : 'page' //页码的参数名称，默认：page
						,
						limitName : 'rows' //每页数据量的参数名，默认：limit
					},
					parseData : function(res) {
						// 可进行数据操作
						return {
							"count" : res.data.total,
							"msg" : res.msg,
							"data" : res.data.rows,
							"code" : res.status
						//code值为200表示成功
						}
					},
					cols : [ [ {
						type : 'checkbox',
						width : 50
					},
					{
						field : 'EMP_CODE',
						title : '工号',
						width : 130
					},{
						field : 'EMP_NAME',
						title : '姓名',
						width : 130
					}, {
						field : 'TIME_BEGIN',
						title : '上线时间',
						width : 150
					}, {
						field : 'DEV_IP',
						title : '卡机IP',
						width : 120
					}, {
						field : 'FMEMO',
						title : '备注',
						//width : 180
					}] ],
					done : function(res, curr, count) {
						pageCurr = curr;
						
						for(j = 0,len=res.data.length; j < len; j++) {
							console.log(res.data[j])
							if(res.data[j].CHECK_STATUS == '1'){
								res.data[j]["LAY_CHECKED"]='true';
						        //下面三句是通过更改css来实现选中的效果
						        var index= res.data[j]['LAY_TABLE_INDEX'];
						        $('tr[data-index=' + index + '] input[type="checkbox"]').prop('checked', true);
						        $('tr[data-index=' + index + '] input[type="checkbox"]').next().addClass('layui-form-checked');
							}
						}
					}
				});
				tableSelect=tableSelect.render({
					elem : '#num',
					searchKey : 'keyword',
					checkedKey : 'id',
					searchPlaceholder : '试着搜索',
					table : {
						url:  context +'verify/getTaskNo',
						//url:  context +'base/prodproc/getProdList',
						method : 'get',
						cols : [ [
						{ type: 'radio' },//多选  radio
						, {
							field : 'id',
							title : 'id',
							width : 0,hide:true
						}, {
							field : 'TASK_NO',
							title : '制令单号',
							width : 180
						}, {
							field : 'ITEM_NO',
							title : '物料编码',
							width : 150
						},{
							field : 'ITEM_NAME',
							title : '物料描述',
							width : 240
						}, {
							field : 'CUST_NAME_S',
							title : '客户简称',
							width : 100
						}] ],
						page : false,
						request : {
							pageName : 'page' // 页码的参数名称，默认：page
							,
							limitName : 'rows' // 每页数据量的参数名，默认：limit
						},
						parseData : function(res) {
							if(res.result){
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
						//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
						var da=data.data;
						//console.log(da[0])
						form.val("itemFrom", {
							"num":da[0].TASK_NO,
							"mtrcode" : da[0].ITEM_NO,
							//"mtrdescr" : da[0].ITEM_NAME,
							"cus" : da[0].CUST_NAME_S
						});
						form.render();// 重新渲染

				}
				});
				
				laydate.render({
				    elem: '#on_date'
				    ,value:getCurDate(0)
				  });
				
				laydate.render({
				    elem: '#pdate1'
				    ,value:getCurDate(0)
				  });
				
				// 监听
				form.on('submit(update)', function(data) {
					update(data.field.pline);//获取打卡数据
				});

				form.on('submit(save)', function(data) {
					if($('#num').val() != ''){
						//save(data.field);//保存
						var list = table.checkStatus('colTable').data;// 被选中行的数据 id
						var empList ="";
						for (var i = 0; i < list.length; i++) {// 获取被选中的行
							empList += list[i].ID + ","// 用“；”分隔
						}
						save(data.field,empList);//保存
					}else{
						layer.alert('请先选择制令单', {
                            icon: 2,
                            skin: 'layer-ext-moon'
                        })
					}
					
				});
				
				//线体选中
				form.on('select(pline)', function(data){
					getUserByLine(data.value);
				});
				
				getInfoAdd();
				
				$(document).on('click','#addBtn',function(){
					open();
					});
				
				form.on('submit(add)', function(data) {
					add(data.field);//保存创建在线返工制令单
					return false;
				});

			});	

});

function getInfoAdd(){
	CoreUtil.sendAjax("verify/getInfoAdd", {}, function(data) {
		console.log(data)
		if (data.result) {
			if(data.data.Class){
				$("#pclass").empty();
				var pclass=data.data.Class;
				for (var i = 0; i < pclass.length; i++) {
					$("#pclass").append("<option value=" + pclass[i].ID+ ">" + pclass[i].CLASS_NAME + "</option>");
				}					
				layui.form.render('select');
			}
			if(data.data.Line){
				$("#pline").empty();
				var pline=data.data.Line;
				for (var i = 0; i < pline.length; i++) {
					if(i == 0){
						$("#pline").append("<option value=''>请选择线体</option>");
					}
					$("#pline").append("<option value=" + pline[i].ID+ ">" + pline[i].LINE_NAME + "</option>");
				}					
				layui.form.render('select');
			}
		}else{
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}
 
function update(lineId){
	CoreUtil.sendAjax("produce/card_data/updateDataByLine", {"line_ids" : lineId}, function(
			data) {
		getUserByLine(lineId);
		layer.alert(data.msg, function() {
			layer.closeAll();
		});
		/*if (data.result) {
			
		} else {
			layer.alert(data.msg, function() {
				layer.closeAll();
			});
		}*/
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}

function save(params,emp_ids){
	var param = {"task_no":params.num,"line_id":params.pline,"hour_type":params.ptyle,
			"class_id":params.pclass,"wdate":params.pdate,"emp_ids":emp_ids};
	CoreUtil.sendAjax("verify/save", JSON.stringify(param), function(
			data) {
		layer.alert(data.msg, function() {
			layer.closeAll();
		});
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}

function getUserByLine(lineId){
	CoreUtil.sendAjax("verify/getUserByLine", {"lineId" : lineId}, function(
			data) {
		console.log(data);
		if (data.result) {
			tableIns.reload({
				data:data.data
			});
		} else {
			layer.alert(data.msg, function() {
				layer.closeAll();
			});
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
}
function open(){
	CoreUtil.sendAjax("verify/getInfoCreateReturn", {}, function(data) {
		console.log(data)
		if (data.result) {
			if(data.data.Task){
				$("#ptask").empty();
				var ptask=data.data.Task;
				for (var i = 0; i < ptask.length; i++) {
					if(i == 0){
						$("#ptask").append("<option value=''>请选择单号</option>");
					}
					$("#ptask").append("<option value=" + ptask[i].TASK_NO+ ">" + ptask[i].TASK_NO + "</option>");
				}					
				layui.form.render('select');
			}
			if(data.data.Liao){
				$("#pliao").empty();
				var pliao=data.data.Liao;
				for (var i = 0; i < pliao.length; i++) {
					if(i == 0){
						$("#pliao").append("<option value=''>请选择料号</option>");
					}
					$("#pliao").append("<option value=" + pliao[i].ITEM_NO+ ">" + pliao[i].ITEM_NO + "</option>");
				}					
				layui.form.render('select');
			}
			if(data.data.User){
				$("#puser").empty();
				var puser=data.data.User;
				for (var i = 0; i < puser.length; i++) {
					if(i == 0){
						$("#puser").append("<option value=''>请选择组长</option>");
					}
					$("#puser").append("<option value=" + puser[i].LINER_NAME+ ">" + puser[i].LINER_NAME + "</option>");
				}					
				layui.form.render('select');
			}
			layer.open({
				type : 1,
				title : '创建在线返工制令单',
				fixed : false,
				resize : false,
				shadeClose : true,
				area : [ '650px' ],
				content : $('#createDiv'),
				end : function() {
					cleanForm();
				}
			});
		}else{
			layer.alert(data.msg);
		}
	}, "GET", false, function(res) {
		layer.alert(res.msg);
	});
	return false;
}
function add(params){
	console.log(params)
	var param = {"task_no":params.ptask,"item_no":params.pliao,"liner_name":params.puser,
			"qty":params.qty,"pdate":params.pdate1};
	CoreUtil.sendAjax("verify/add", JSON.stringify(param), function(
			data) {
		layer.alert(data.msg, function() {
			layer.closeAll();
		});
	}, "POST", false, function(res) {
		layer.alert(res.msg);
	});
}
//清空新增表单数据
function cleanForm() {
	$('#createForm')[0].reset();
	layui.form.render();// 必须写
}