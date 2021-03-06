/**
 * 产出报工
 */
var pageCurr;
var tabledata=[];
$(function() {
	layui.use(
			[ 'table', 'form', 'layedit', 'laydate', 'tableSelect' ],
			function() {
				var form = layui.form, layer = layui.layer, layedit = layui.layedit, table = layui.table,table1 = layui.table, laydate = layui.laydate, tableSelect = layui.tableSelect, tableSelect1 = layui.tableSelect;
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
					height:'full-300'//固定表头&full-查询框高度
					,even:true,//条纹样式
					page : true,
					limit:20,
					limits:[20,50,100,200,400,600,1000],
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
					cols : [ [
						{
						type : 'numbers',
						width : 50
					},
					{
						field : 'ITEM_BARCODE',
						title : '箱内条码',
						width : 160,sort: true
					},{
						field : 'ITEM_BARCODE',
						title : '箱外条码',
						width : 160,sort: true
					}, {
						field : 'TYPE',
						title : '类型',
						width : 80,
					}, {
						field : 'QUANTITY',
						title : '数量',
						width : 70,sort: true
					},{
						field : 'TASK_NO',
						title : '制令单号',
							width : 150,
							sort: true
					},{
						field : 'ITEM_NO',
						title : '物料编号',
						width : 150,sort: true
					},{
						field : 'CREATE_BY',
						title : '操作人',
						width : 80
					},{
						field : 'CREATE_DATE',
						title : '操作时间',
						width : 145
					}] ],
					done : function(res, curr, count) {
						pageCurr = curr;
					}
				});
				tableSelect=tableSelect.render({
					elem : '#num',
					searchKey : 'keyword',
					checkedKey : 'id',
					searchPlaceholder : '试着搜索',
					table : {
						url:  context +'/product/getTaskNo',
						//url:  context +'base/prodproc/getProdList',
						method : 'get',
						width:800,
						cols : [ [
						{ fixed:'left',type: 'radio' },//多选  radio
							{fixed:'left',type :'numbers'},
						{fixed:'left',field : 'id', title : 'id', width : 0,hide:true},
							{fixed:'left',field : 'PROD_DATE', title : '生产日期', width : 95,
								templet:function (d) {
									if(d.PROD_DATE!=null){
										return /\d{4}-\d{1,2}-\d{1,2}/g.exec(d.PROD_DATE)
									}
								}
							},
							{fixed:'left',field : 'LINER_NAME', title : '组长', width : 70},
							{fixed:'left',field : 'ITEM_NO', title : '物料编码', width : 145},
							{field : 'TASK_NO', title : '制令单号', width : 150},
							{field : 'WS_SECTION', title : '工段', width : 60},
							{field : 'FMEMO', title : '备注', width : 80},
							{field : 'ITEM_NAME', title : '物料描述', width : 240},
							{field : 'QTY_PLAN', title : '制单数量', width : 80},
							{field : 'OTPT_QTY', title : '已包装数量', width : 80},
							{field : 'ORDER_RATE', title : '包装完成率', width : 90} ,
						] ],
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
							"mtrdescr" : da[0].ITEM_NAME,
							"qty" : da[0].QTY_PLAN,
							"linecode" : da[0].LINER_NAME,
							"inqty" : da[0].OTPT_QTY,
							"rate" : da[0].ORDER_RATE,
						});
						form.render();// 重新渲染
						
						getDetailByTask(da[0].TASK_NO);
						
						tableSelect2=tableSelect1.render({
							elem : '#num2',
							searchKey : 'keyword',
							checkedKey : 'TASK_NO',
							searchPlaceholder : '试着搜索',
							table : {
								url:  context +'/product/getHXTaskNo',
								where:{
									taskNo:$('#num').val()
								},
								method : 'get',
								cols : [ [
								{ type: 'checkbox' },//多选  radio
								 {
									field : 'ID',
									title : 'id',
									width : 10,hide:true
								}, {
									field : 'TASK_NO',
									title : '制令单号',
									width : 150,sort: true
								}, {
									field : 'ITEM_NO',
									title : '物料编码',
									width : 150,sort: true
								},{
									field : 'ITEM_NAME',
									title : '物料描述',
									width : 200,sort: true
								}, {
									field : 'LINER_NAME',
									title : '组长',
									width : 90,sort: true
								},{
									field : 'QTY_PLAN',
									title : '数量',sort: true,
									edit:'text',style:'background: #98FB98;opacity: 0.3',
									width : 80
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
							 done : function(elem1, data1) {
								//选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
								var da=data1.data;
								console.log(da)
								//console.log(_tableSelectEdite)
								var str = "";
								da.forEach(function(d){
									_tableSelectEdite.forEach(function(d2){
										if(d2.id == d.ID){
											d.QTY_PLAN = d2.value;
										}
									})
									if(d.QTY_PLAN){
										   str += d.TASK_NO+"@"+d.QTY_PLAN+",";
									   }else{
										   layer.alert("请在"+d.TASK_NO+"行输入数量!");
										   return false;
									   }
								});
								//console.log(str)
								$('#num2').val(str)
						}
						});
				}
				});
				
				/*tableSelect2=tableSelect1.render({
					elem : '#num2',
					searchKey : 'keyword',
					checkedKey : 'TASK_NO',
					searchPlaceholder : '试着搜索',
					table : {
						url:  context +'/product/getHXTaskNo',
						where:{
							taskNo:num
						},
						method : 'get',
						cols : [ [
						{ type: 'checkbox' },//多选  radio
						, {
							field : 'ID',
							title : 'id',
							width : 10,hide:true
						}, {
							field : 'TASK_NO',
							title : '制令单号',
							width : 180,sort: true
						}, {
							field : 'ITEM_NO',
							title : '物料编码',
							width : 150,sort: true
						},{
							field : 'ITEM_NAME',
							title : '物料描述',
							width : 240,sort: true
						}, {
							field : 'LINER_NAME',
							title : '组长',
							width : 100,sort: true
						},{
							field : 'QTY_PLAN',
							title : '数量',sort: true,
							edit:'text',style:'background: #98FB98;opacity: 0.3',
							width : 80
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
						console.log(da)
						//console.log(_tableSelectEdite)
						var str = "";
						da.forEach(function(d){
							_tableSelectEdite.forEach(function(d2){
								if(d2.id == d.ID){
									d.QTY_PLAN = d2.value;
								}
							})
							if(d.QTY_PLAN){
								   str += d.TASK_NO+"@"+d.QTY_PLAN+",";
							   }else{
								   layer.alert("请在"+d.TASK_NO+"行输入数量!");
								   return false;
							   }
						});
						//console.log(str)
						$('#num2').val(str)
				}
				});*/
				
				// 监听工具条
				table.on('tool(colTable)', function(obj) {
					console.log(obj)
					var data = obj.data;
					console.log(data)
					if (obj.event === 'del') {
						// 删除
						del(obj,data.ID, data.ITEM_BARCODE);
					}
				});
				//监听下拉框
				$("#hx_label").hide();
				$("#hx_div").hide();
				form.on('select(ptyle)', function(data){
					if(data.value == 1){
						 $("#hx_label").hide();
						 $("#hx_div").hide();
					}else{
						 $("#hx_label").show();
						 $("#hx_div").show();
					}
				});
				
				//监听提交
		    	  form.on('submit(hsearchSubmit)', function(data){
		    		  hTableIns.reload({
		    			  url:context+'/product/getHistoryList',
		                  where:data.field 
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
					height: 'full-210'
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
					cols : [ [ {fixed:'left',
						type : 'numbers'
					},{fixed:'left',
						field : 'TASK_NO',
						title : '制令单号',
						width : 150,sort: true
					}, {fixed:'left',
						field : 'LINE_NO',
						title : '组长',
						width : 80,sort: true
					}, {fixed:'left',
						field : 'ITEM_NO',
						title : '产品编码',
						width : 150,sort: true
					}, {fixed:'left',
						field : 'ITEM_BARCODE',
						title : '产品条码',
						width : 160,sort: true
					}, {
						field : 'QUANTITY',
						title : '包装数量',
						width : 100,sort: true
					},{
						field : 'SCAN_TYPE',
						title : '扫描类型',
						width : 100,sort: true
					},{
						field : 'USER_NAME',
						title : '操作人',
						width : 90,sort: true
					} ,{
						field : 'CREATE_DATE',
						title : '操作时间',
						width : 150,sort: true
					} ] ],
					done : function(res, curr, count) {
						pageCurr = curr;
					}
				});

			});	
	
	
	$('#nbarcode').bind('keypress',function(event){
        if(event.keyCode == "13") {
        	//alert('你输入的内容为：' + $('#barcode').val());
        	if(!$( "input[name='num']").val()){
        		layer.alert("请先选择制令单号!");
        		return false;
        	}
        	if($('#nbarcode').val()){
        		afterNei($('#nbarcode').val())
        	}else{
        		layer.alert("请先扫描内箱条码!");
        	}
        }
    });
	
	$('#wbarcode').bind('keypress',function(event){
        if(event.keyCode == "13") {
        	//alert('你输入的内容为：' + $('#barcode').val());
        	if(!$( "input[name='num']").val()){
        		layer.alert("请先选择制令单号!");
        		return false;
        	}
        	if(!$('#nbarcode').val()){
        		layer.alert("请先扫描内箱条码!",function () {
					$('#nbarcode').focus();
					layer.closeAll();
				});
        		return false;
        	}
        	if($('#wbarcode').val()){
        		afterWai($('#wbarcode').val())
        	}else{
        		layer.alert("请先扫描外箱条码!");
        	}
        }
    });
});
function getDetailByTask(taskNo){
	 var params={"taskNo":taskNo}
		CoreUtil.sendAjax("/product/getDetailByTask", params, function(data) {
			console.log(data)
			if (data.result) {
				tableIns.reload({
					data:data.data
				});
			}else{
				layer.alert(data.msg);
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
}
 function afterNei(barcode){
	 var params={"barcode":barcode,"task_no":$( "input[name='num']").val()}
		CoreUtil.sendAjax("/product/afterNei", params, function(data) {
			
			if (data.result) {
				// console.log(data.data[0].QTY);
				playSaoMiaoMusic();
				 $("#wbarcode").val("");
				 $("#cQty").val(data.data[0].QTY);
				 $("#wbarcode").focus();
				layer.form.render();// 重新渲染
			}else{
			    playMusic();
				layer.alert(data.msg,function () {
					$('#nbarcode').val('');
					$('#nbarcode').focus();
					layer.closeAll();
				});
				// $('#nbarcode').val('');
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
 }
 
 function afterWai(barcode){
	 var params={"wbarcode":barcode,"task_no":$( "input[name='num']").val(),"nbarcode":$('#nbarcode').val(),
			 "ptype":$("select[name='ptyle'] option:selected").val(),"hx":$('#num2').val()}
		CoreUtil.sendAjax("/product/afterWai", params, function(data) {
			console.log(data)
			if (data.result) {
				playSaoMiaoMusic();
				$("#cQty").val("");
				$('#inqty').val(data.data.Qty);
				$('#rate').val(data.data.Rate);
				tableIns.reload({
					data:data.data.List
				});
				$("#nbarcode").val("");//10-15内箱标签置空
				$("#wbarcode").val("");//10-15外箱标签标签置空
				$("#nbarcode").focus();//10-15光标移动到内箱标签栏
				if($("select[name='ptyle'] option:selected").val()=='2'){//若为合箱扫描模式则执行-2020-10-16
					$("#num2").val("");//合箱制令单置空
				}
				/*var ptype = "正常扫描";
				if($("select[name='ptyle'] option:selected").val()=='2'){
					ptype = "合箱扫描";
				}
				var temp={"nbarcode":$('#nbarcode').val(),
						"wbarcode":$('#wbarcode').val(),
						"ptype":ptype,
						"qty":0,
						"task_no":$( "input[name='num']").val(),
						"item_no":$( "input[name='mtrcode']").val()}	
				tabledata.push(temp)
				tableIns.reload({
					data:tabledata
				});*/
			}else{
			    playMusic();
				layer.alert(data.msg,function () {
					$("#wbarcode").val("");
					$("#nbarcode").val("");
					$("#nbarcode").focus();
					layer.closeAll();
				});
				// $("#wbarcode").val("");
			}
		}, "GET", false, function(res) {
			layer.alert(res.msg);
		});
 }
 
 function selectChange(){
	 alert($("select[name='ptyle'] option:selected").val())
 }
 
