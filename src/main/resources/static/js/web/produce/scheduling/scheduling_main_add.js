/**
 * 排产导入从表
 */
var pageCurr;
$(function () {
    layui.use(['form', 'table', 'laydate', 'upload','tableSelect'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate
            ,upload = layui.upload
            ,tableSelect = layui.tableSelect
            ,tableSelect2 = layui.tableSelect;

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
        laydate.render({
            elem: '#prodDate'
        });
        //
        form.on('select(deptId)', function(obj){
            $("#deptName").val(obj.elem[obj.elem.selectedIndex].text);
        });
        //监听校验按钮
        form.on('submit(checkBtn)', function(data){
            doCheckProc(table);
            return false;
        });
        //监听生效按钮
        form.on('submit(effectBtn)', function(data){
            doEffect(table);
            return false;
        });
        //监听删除按钮
        form.on('submit(delBtn)', function(data){
            doDel(table);
            return false;
        });
        //监听导入的编辑提交
        form.on('submit(editSubmit)', function(data){
            doEdit(data);
        });
        //监听工具条
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'edit1'){
                //编辑
                getData(data,data.ID);
            }
        });

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/schedulingMain/getDetList'
            ,method: 'get' //默认：get请求
            ,where:{ keyword:"", mid:id }
            ,cellMinWidth: 80
            ,page: false,
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'rows' //每页数据量的参数名，默认：limit
            },
            parseData: function (res) {
                // 可进行数据操作
                return {
                    "count": res.data.total,
                    "msg":res.msg,
                    "data":res.data.rows,
                    "code": res.status //code值为200表示成功
                }
            },
            cols: [[
                {type:'numbers'}
                ,{type:'checkbox'}
                // ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
                // ,{field:'departName', title:'部门', width:60, templet:'<span>{{d.department ? d.department.bsName : ""}}<span>'}
                ,{field:'CHECK_STATUS', title:'校验结果', width:100, templet:'#statusTpl'}
                ,{field:'ENABLED', title:'生效状态', width:80, templet:'#enabledTpl'}
                ,{field:'ERROR_INFO', title:'错误信息', width:140}
                ,{field:'PROD_NO', title:'工单号', width:120}
                ,{field:'TASK_NO', title:'生产制令单', width:100,
                    templet: '<div><a cursor: pointer; onclick="toSchedulingEdit({{d.TASK_ID}})">{{ d.TASK_NO==null?"":d.TASK_NO }}</a></div>'}
                ,{field:'GROUP_NO', title:'组合', width:70}
                ,{field:'CUST_NAME', title:'客户', width:80}
                ,{field:'LINER_NAME', title:'组长', width:70}
                ,{field:'ITEM_NO', title:'物料编码', width:150}
                ,{field:'ITEM_NAME', title:'物料描述', width:150}
                ,{field:'QTY_PLAN', title:'计划数量', width:100}
                ,{fixed:'right', title:'操作', width:80, align:'center', toolbar:'#optBar'}
            ]]
            ,done: function(res, curr, count){
                pageCurr=curr;
            }
        });

        //导入
        upload.render({
            elem: '#uploadBtn'
            ,url: context + '/produce/schedulingMain/doExcel'
            ,accept: 'file' //普通文件
            ,data: { mid:id }
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                layer.load(); //上传loading
            }
            ,done: function(res,index, upload){
                layer.closeAll('loading'); //关闭loading
                console.log(res);
                var ids = "";
                for(var i = 0;i<res.data.length;i++){
                    ids += res.data[i].id +",";
                }
                if(res.result){
                    $.ajax({
                        type: "POST",
                        data: {},
                        url: context+"/produce/schedulingMain/doCheckProc?ids="+ids,
                        success: function (res) {
                            if (res.result) {
                                loadAll();//重新加载表格
                                layer.alert("导入成功！",function(index){
                                    layer.close(index);
                                });
                            } else {
                                layer.alert(res.msg,function(index){
                                    layer.close(index);
                                });
                            }
                        },
                        error: function () {
                            layer.alert("操作请求错误，请您稍后再试",function(){
                            });
                        }
                    });
                }else{
                    layer.alert(res.msg,function (index) {
                        layer.close(index);
                    });
                }
                $(window).resize();
            }
            ,error: function(index, upload){
                layer.closeAll('loading'); //关闭loading
            }
        });

        //getDeptSelect();//获取部门下拉数据
        getMainData();//获取数据
    });
});

//获取部门下拉数据
function getDeptSelect() {
    $.ajax({
        type: "post",
        data: {},
        url: context+"/produce/schedulingMain/getDeptSelect",
        success: function (res) {
            $("#deptId").empty();
            if (res.result) {
                var itemList = res.data.rows;
                for(var i = 0; i < itemList.length; i++){
                    if(i==0){
                        $("#deptId").append("<option value=''>请点击选择</option>");
                    }
                    $("#deptId").append( '<option value="'+itemList[i].ID+'">'+itemList[i].ORG_NAME+'</option>');
                }
                layui.form.render('select');
            } else {
                layer.alert(res.msg,function(index){
                });
            }
        },
        error: function () {
        }
    });
}
//初始化数据
function getMainData(){
    if(deptList != null){
        for(var i = 0; i < deptList.length; i++){
            if(i==0){
                $("#deptId").append("<option value=''>请点击选择</option>");
            }
            $("#deptId").append( '<option value="'+deptList[i].ID+'">'+deptList[i].ORG_NAME+'</option>');
        }
    }

    $("#id").val(id);
    $("#deptName").val(schedulingMain.deptName);
    $("#idNo").val(schedulingMain.idNo);
    $("#deptId").val(schedulingMain.deptId);
    $("#prodDate").val(schedulingMain.prodDate);
    $("#className").val(schedulingMain.className);
    if(schedulingMain.fenable == 1){
        $("input[name='fenable']").prop("checked", true);
    }else{
        $("input[name='fenable']").prop("checked", false);
    }

    //渲染
    layui.form.render('select');
    layui.form.render('checkbox');
}

//新增排产导入的保存
function addSubmit(obj) {
    console.log(obj.field);
    if(!obj.field.bsStatus){
        obj.field.bsStatus = 0;
    }
    CoreUtil.sendAjax("/produce/schedulingMain/add", JSON.stringify(obj.field),
        function(data) {
            if (data.result) {
                $("#id").val(data.data.id);
                $("#idNo").val(data.data.idNo);
                layer.alert("操作成功", function() {
                    layer.closeAll();
                });
            } else {
                layer.alert(data.msg);
            }
        }, "POST", false, function(res) {
            layer.alert(res.msg);
        });
}

//编辑排产导入的保存
function editSubmit(obj) {
    console.log(obj.field);
    if(!obj.field.bsStatus){
        obj.field.bsStatus = 0;
    }
    CoreUtil.sendAjax("/produce/schedulingMain/edit", JSON.stringify(obj.field),
        function(data) {
            if (data.result) {
                $("#id").val(data.data.id);
                $("#idNo").val(data.data.idNo);
                layer.alert("操作成功", function() {
                    layer.closeAll();
                });
            } else {
                layer.alert(data.msg);
            }
        }, "POST", false, function(res) {
            layer.alert(res.msg);
        });
}

//重新加载表格
function loadAll(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

//导出模板
function exportExcel(){
    location.href = "../../excelFile/排产导入模板.xlsx";//从文件夹内直接提取
    return false;
}

//校验
function doCheckProc(table){
    var tempIds = "";
    var checkStatus = table.checkStatus("iList");
    for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            tempIds += checkStatus.data[i].ID;
        }else{
            tempIds += "," + checkStatus.data[i].ID;
        }
    }

    $.ajax({
        type: "POST",
        data: { "ids":tempIds },
        url: context+"/produce/schedulingMain/doCheckProc",
        success: function (res) {
            if (res.result) {
                layer.alert("校验成功！",function(index){
                    loadAll();//重新加载表格
                    layer.close(index);
                });
            } else {
                layer.alert(res.msg,function(index){
                    layer.close(index);
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}

//生效
function doEffect(table){
    var tempIds = "";
    var checkStatus = table.checkStatus("iList");
    for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            tempIds += checkStatus.data[i].ID;
        }else{
            tempIds += "," + checkStatus.data[i].ID;
        }
    }

    $.ajax({
        type: "POST",
        data: { "ids":tempIds },
        url: context+"/produce/schedulingMain/doEffect",
        success: function (res) {
            if (res.result) {
                layer.alert("生效成功！",function(index){
                    loadAll();//重新加载表格
                    layer.close(index);
                });
            } else {
                layer.alert(res.msg,function(index){
                    layer.close(index);
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}

//删除
function doDel(table) {
    var tempIds = "";
    var checkStatus = table.checkStatus("iList");
    for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            tempIds += checkStatus.data[i].ID;
        }else{
            tempIds += "," + checkStatus.data[i].ID;
        }
    }

    if(tempIds.length > 0){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                data: { "ids": tempIds },
                url: context+"/produce/schedulingMain/deleteDet",
                success: function (data) {
                    if (data.result) {
                        layer.alert("删除成功",function(){
                            layer.closeAll();
                            loadAll();
                        });
                    } else {
                        layer.alert(data.msg,function(index){
                            layer.close(index);
                        });
                    }
                },
                error: function () {
                    layer.alert("操作请求错误，请您稍后再试",function(){
                    });
                }
            });
        }, function(){
            layer.closeAll();
        });
    }
}

//导入编辑
function getData(obj,id){
    debugger;
    $.ajax({
        type: "GET",
        data: { "id":id },
        url: context+"/produce/schedulingMain/getDet",
        success: function (data) {
            if (data.result) {
                $("#id2").val(data.data.id==null?'':data.data.id);
                $("#groupNo2").val(data.data.groupNo==null?'':data.data.groupNo);
                $("#custName2").val(data.data.custName==null?'':data.data.custName);
                $("#linerName2").val(data.data.linerName==null?'':data.data.linerName);
                $("#prodNo2").val(data.data.prodNo==null?'':data.data.prodNo);
                $("#itemNo2").val(data.data.itemNo==null?'':data.data.itemNo);
                $("#itemName2").val(data.data.itemName==null?'':data.data.itemName);
                $("#qtyPlan2").val(data.data.qtyPlan==null?'':data.data.qtyPlan);
                openView(id,"导入编辑")
            } else {
                layer.alert(data.msg,function(){
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}

//导入编辑弹出框
function openView(id,title){
    if(id==null || id==""){
        $("#id2").val("");
    }
    var index=layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['550px'],
        content:$('#editDiv'),
        end:function(){
            clean();
        }
    });
    layer.full(index);//弹出框全屏
}

//清空表单
function clean(){
    $("#editForm")[0].reset();
    layui.form.render();
}

function doEdit(obj){
    debugger;
    var ids = obj.field.id + "";
    CoreUtil.sendAjax("/produce/schedulingMain/editDet", JSON.stringify(obj.field),
        function(data) {
            if (data.result) {
                //校验
                $.ajax({
                    type: "POST",
                    data: {},
                    url: context+"/produce/schedulingMain/doCheckProc?ids="+ids,
                    success: function (res) {
                        debugger;
                        if (res.result) {
                            layer.alert("编辑成功", function() {
                                layer.closeAll();
                            });
                        } else {
                            layer.alert(res.msg,function(index){
                                layer.close(index);
                            });
                        }
                        loadAll();
                    },
                    error: function () {
                        layer.alert("操作请求错误，请您稍后再试",function(){
                            loadAll();
                        });
                    }
                });
            } else {
                layer.alert(data.msg);
                loadAll();
            }
        }, "POST", false, function(res) {
            layer.alert(res.msg);
        });
}

