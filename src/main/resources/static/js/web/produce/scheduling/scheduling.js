/**
 * 接口管理
 */
var pageCurr;
var pageCurr2;
$(function () {
    layui.use(['form', 'table', 'laydate', 'upload'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate
            ,upload = layui.upload;

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/scheduling/getList'
            ,method: 'get' //默认：get请求
            ,cellMinWidth: 80
            ,page: true,
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
                // ,{field:'id', title:'ID', width:80, unresize:true, sort:true}
                // ,{field:'departName', title:'部门', width:60, templet:'<span>{{d.department ? d.department.bsName : ""}}<span>'}
                ,{field:'prodNo', title:'工单号', width:150}
                ,{field:'groupNo', title:'组合', width:60}
                ,{field:'custName', title:'客户', width:70}
                ,{field:'linerName', title:'线别', width:70}
                ,{field:'prodDate', title:'日期', width:100}
                ,{field:'deptName', title:'部门', width:80}
                ,{field:'classNo', title:'班次', width:60}
                ,{field:'taskNo', title:'制令单号', width:120}
                ,{field:'itemNo', title:'物料编码', width:150}
                ,{field:'itemName', title:'物料描述'}
                ,{fixed:'right', title:'操作', width:120, align:'center', toolbar:'#optBar'}
            ]]
            ,done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr=curr;
            }
        });

        tableIns2=table.render({
            elem: '#iList2'
            ,url:context+'/produce/scheduling/getTempList'
            ,method: 'get' //默认：get请求
            ,cellMinWidth: 80
            ,page: true,
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
                ,{field:'checkStatus', title:'状态', width:100, templet:'#statusTpl'}
                ,{field:'errorInfo', title:'错误信息', width:100}
                ,{field:'groupNo', title:'组合', width:60}
                ,{field:'custName', title:'客户', width:70}
                ,{field:'linerName', title:'线别', width:70}
                ,{field:'prodDate', title:'日期', width:100}
                ,{field:'deptName', title:'组装部', width:80}
                ,{field:'classNo', title:'班次', width:60}
                ,{field:'prodNo', title:'工单号', width:120}
                ,{field:'itemNo', title:'物料编码', width:120}
                ,{field:'itemName', title:'物料描述', width:150}
                ,{field:'qtyPlan', title:'计划生产数量', width:110}
                //,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
            ]]
            ,done: function(res, curr, count){
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //console.log(res);
                //得到当前页码
                //console.log(curr);
                //得到数据总量
                //console.log(count);
                pageCurr2=curr;
            }
        });

        //监听工具条
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                //删除
                doDel(data.id);
            } else if(obj.event === 'edit'){
                //编辑
                //window.location = context + "/produce/scheduling/toSchedulingEdit?id=" + data.id;
                return false;
            }
        });

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });
        //监听导入按钮
        form.on('submit(importBtn)', function(data){
            //导入弹出框
            openUpload('导入');
            return false;
        });

        //导入
        upload.render({
            elem: '#upload'
            ,url: context + '/produce/scheduling/doExcel'
            ,accept: 'file' //普通文件
            ,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
                layer.load(); //上传loading
            }
            ,done: function(res,index, upload){
                layer.closeAll('loading'); //关闭loading
                if(res.result){
                    $.ajax({
                        type: "POST",
                        data: {},
                        url: context+"/produce/scheduling/doCheckProc",
                        success: function (res) {
                            if (res.result) {
                                loadAll2();//重新加载表格
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

    });
});

//删除
function doDel(id) {
    if(id!=null){
        layer.confirm('您确定要删除吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.ajax({
                type: "POST",
                data: { "id": id },
                url: context+"/produce/scheduling/delete",
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

//导出
function exportExcel(){
    //导出模板
    location.href = context + "/produce/scheduling/getExcel";
}

//导入弹出框
function openUpload(title){
    deleteTempAll();
    var index = layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: false,//是否点击遮罩关闭
        area: ['1000px'],
        content:$('#uploadDiv'),
        macmin:true,//弹出框全屏
        end:function(){

        }
    });
    layer.full(index);//弹出框全屏
}

//根据当前登录用户删除临时表所有数据
function deleteTempAll(){
    $.ajax({
        url:context + '/produce/scheduling/deleteTempAll',
        type:"POST",
        data:{},
        success:function(data){
            //重新获取临时列表数据
            loadAll2();
        },
        error:function(e){
            layer.alert("操作请求错误，请您稍后再试",function(){
            });
        }
    });
}

//确认保存临时数据
$("#save").click(function () {
    $.ajax({
        type: "POST",
        data: {},
        url: context+"/produce/scheduling/confirmTemp",
        success: function (data) {
            if (data.result) {
                layer.alert(data.msg,function(){
                    loadAll();
                    layer.closeAll();
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
});

//重新加载表格（搜索）
function load(obj){
    //重新加载table
    tableIns.reload({
        where: {
            keyword:obj.field.keyword
        }
        , page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

//重新加载表格（全部）
function loadAll(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

//重新加载临时表格（全部）
function loadAll2(){
    //重新加载table
    tableIns2.reload({
        page: {
            curr: pageCurr2 //从当前页码开始
        }
    });
};