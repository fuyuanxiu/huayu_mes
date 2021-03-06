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

        layui.form.render('select');
        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/scheduling/getList'
            ,method: 'get' //默认：get请求
            // ,cellMinWidth: 80
            ,page: true
            , toolbar: '#toolbar' //开启工具栏，此处显示默认图标，可以自定义模板，详见文档
            ,height:'full-80'//固定表头&full-查询框高度
            ,even:true,//条纹样式
            limit: 20,
            limits: [20,50,100,200],
            request: {
                pageName: 'page' //页码的参数名称，默认：page
                ,limitName: 'rows' //每页数据量的参数名，默认：limit
            },
            parseData: function (res) {
            	console.log(res)
                // 可进行数据操作
                return {
                    "count": res.data.total,
                    "msg":res.msg,
                    "data":res.data.rows,
                    "code": res.status //code值为200表示成功
                }
            },
            cols: [[
                {type:'numbers' ,align: 'center',fixed:'left'},
                {type:'checkbox' ,width:60,align: 'center',fixed:'left'}
                ,{field:'PRODUCE_STATE', title:'状态', width:60,align: 'center',fixed:'left'}
                ,{field:'PROD_DATE', title:'生产日期',templet:function (d) {
                        if(d.PROD_DATE!=null){
                            return d.PROD_DATE.slice(0,4)+"-"+ d.PROD_DATE.slice(4,6)+"-"+d.PROD_DATE.slice(6,8)
                        }
                    },
                    width:100,align: 'center',fixed:'left'}
                ,{field:'LINER_NAME', title:'组长', width:70,align: 'center',fixed:'left'}
                ,{field:'FMODEL', title:'产品型号', width:90,align: 'center',fixed:'left'}
                ,{field:'ITEM_NO', title:'物料编码', width:150,align: 'center',fixed:'left'}
                ,{field:'TASK_NO', title:'制令单号', width:150,align: 'center'}
                ,{field:'CLASS_NO', title:'班次', width:60,align: 'center'}
                ,{field:'QTY_PLAN', title:'计划数量', width:80,align: 'center'}
                ,{field:'QTY_DONE', title:'完工数', width:80,align: 'center'}
                ,{field:'QUANTITY', title:'投料总数', width:80,align: 'center'}//2020-12-08新增
                ,{field:'RATE_DONE', title:'完工率(%)', width:100,align: 'center'}
                // ,{field:'deptName', title:'部门', width:70}
                ,{field:'MANPOWER', title:'标准人力', width:80,align: 'center'}
                ,{field:'CAPACITY', title:'标准产能', width:80,align: 'center'}
                ,{field:'MAN_ACT', title:'实际用人', width:80,align: 'center'}
               ,{field:'DEPT_NAME', title:'部门名称', width:80,align: 'center'}
                ,{field:'PROD_NO', title:'工单号', width:130,align: 'center'}
                // ,{field:'FMODEL', title:'产品型号', width:55,align: 'center'}
                ,{field:'CUST_NAME_S', title:'客户', width:150,align: 'center'}
                ,{field:'ITEM_NAME', title:'物料描述', width:200,align: 'center'},
                // ,{fixed:'right', title:'操作', width:120, align:'center', toolbar:'#optBar'}
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

        // tableIns2=table.render({
        //     elem: '#iList2'
        //     ,url:context+'/produce/scheduling/getTempList'
        //     ,method: 'get' //默认：get请求
        //     ,cellMinWidth: 80
        //     ,page: true,
        //     request: {
        //         pageName: 'page' //页码的参数名称，默认：page
        //         ,limitName: 'rows' //每页数据量的参数名，默认：limit
        //     },
        //     parseData: function (res) {
        //         // 可进行数据操作
        //         return {
        //             "count": res.data.total,
        //             "msg":res.msg,
        //             "data":res.data.rows,
        //             "code": res.status //code值为200表示成功
        //         }
        //     },
        //     cols: [[
        //         {type:'numbers'}
        //         ,{field:'checkStatus', title:'状态', width:100, templet:'#statusTpl'}
        //         ,{field:'errorInfo', title:'错误信息', width:110}
        //         ,{field:'groupNo', title:'组合', width:70}
        //         ,{field:'custName', title:'客户', width:70}
        //         ,{field:'linerName', title:'组长', width:70}
        //         ,{field:'prodDate', title:'日期', width:100}
        //        // ,{field:'deptName', title:'组装部', width:80}
        //         ,{field:'classNo', title:'班次', width:60}
        //         ,{field:'prodNo', title:'工单号', width:120}
        //         ,{field:'itemNo', title:'物料编码', width:120}
        //         ,{field:'itemName', title:'物料描述', width:200}
        //         ,{field:'qtyPlan', title:'计划生产数量', width:110}
        //         //,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
        //     ]]
        //     ,done: function(res, curr, count){
        //         //如果是异步请求数据方式，res即为你接口返回的信息。
        //         //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
        //         //console.log(res);
        //         //得到当前页码
        //         //console.log(curr);
        //         //得到数据总量
        //         //console.log(count);
        //         pageCurr2=curr;
        //     }
        // });

        //监听行单击事件（双击事件为：rowDouble）
        table.on('rowDouble(iTable)', function(obj){
            //标注选中样式
            obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
            var a = document.createElement('a');
            var  url = "/produce/scheduling/toSchedulingEdit?id=" + obj.data.ID+"&qty="+obj.data.QTY_DONE+"&rate="+obj.data.RATE_DONE+"&input="+obj.data.QUANTITY;
            // console.log(url);
            a.setAttribute('lay-href', context + url);
            a.setAttribute('lay-text', '排产编辑');
            a.setAttribute('id', 'js_a');
            if(document.getElementById('js_a')) {//防止反复添加
                document.body.removeChild(document.getElementById('js_a'));
            }
            document.body.appendChild(a);//创建
            a.click();//点击
            return false;

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
                var a = document.createElement('a');
                a.setAttribute('lay-href', context + "/produce/scheduling/toSchedulingEdit?id=" + data.ID);
                a.setAttribute('lay-text', '排产编辑');
                a.setAttribute('id', 'js_a');
                if(document.getElementById('js_a')) {//防止反复添加
                    document.body.removeChild(document.getElementById('js_a'));
                }
                document.body.appendChild(a);//创建
                a.click();//点击
                return false;
            }
        });

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });

        //监听提交按钮
        form.on('submit(submitQty)', function(data){
            //重新加载table
            console.log(data);
            updateQty(data);
            return false;
        });


        //监听导入按钮
        form.on('submit(importBtn)', function(data){
            //导入弹出框
            openUpload('导入');
            return false;
        });
        //监听提取工序按钮
        form.on('submit(processBtn)', function(data){
            //提取工序
            layer.confirm('您确定要提取工序吗？', {
                btn: ['确认','返回'] //按钮
            }, function(){
                getProcess();
            }, function(){
                layer.closeAll();
            });
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

function changeSubmit() {
    var checkdata = layui.table.checkStatus("iList").data;
    var ids = "";
    var statues =""; //旧状态
    for(var i = 0;i<checkdata.length;i++){
        ids = ids+ checkdata[i].TASK_NO+',';
        statues = statues+ checkdata[i].TASK_NO+'-'+checkdata[i].PRODUCE_STATE+','
    }
    var param = {
        "taskNos" : ids,
        "statue":$('#changeSelect').val(),
        "statues":statues,
    };
    CoreUtil.sendAjax("/produce/scheduling/changeStatue", JSON.stringify(param),
        function(data) {
            if (data.result) {
                layer.alert("更改成功",function () {
                    loadAll();
                    layer.closeAll();
                })
            } else {
                layer.alert(data.msg)
            }
        }, "POST", false, function(res) {
            layer.alert("操作请求错误，请您稍后再试");
        });

}


function delProdOrder() {
    var checkdata = layui.table.checkStatus("iList").data;
    console.log(checkdata[0]);
    if(checkdata.length ==0){
        layer.msg("请选中制令单");
        return  false;
    }else if(checkdata.length >=2){
        layer.msg("只能选中一个制令单");
        return false
    }
    var taskNo = checkdata[0].TASK_NO;
    var param = {
        "taskNo" : taskNo,
    };
    CoreUtil.sendAjax("/produce/scheduling/delProdOrder", JSON.stringify(param),
        function(data) {
            if (data.result) {
                layer.alert("删除成功",function () {
                    loadAll();
                    layer.closeAll();
                })
            } else {
                layer.alert(data.msg)
            }
        }, "POST", false, function(res) {
            layer.alert("操作请求错误，请您稍后再试");
        });

}


function updateQty(obj) {
    var param = {
        "taskNo" : obj.field.taskNo,
        "qtyPlan":obj.field.newQty,
        "manNumber": obj.field.manNumber,
        "capacity": obj.field.capacity,
    };
    CoreUtil.sendAjax("/produce/scheduling/updateOrderQty", JSON.stringify(param), function(
        data) {
        if (data.result) {
            layer.alert("操作成功", function() {
                layer.closeAll();
                // cleanMtrial();
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

//导出
function exportExcel(){
    //导出模板
    //location.href = context + "/produce/scheduling/getExcel";
	location.href = "../../excelFile/排产导入模板.xlsx";//从文件夹内直接提取
    return false;
}

function openChange() {
    var checkdata = layui.table.checkStatus("iList").data;
    if(checkdata.length == 0){
        layer.msg('请先选中需要修改的制令单');
        return false;
    }
    var index = layer.open({
        type:1,
        title: "修改制令单状态",
        // fixed:false,
        // resize :false,
        shadeClose: false,//是否点击遮罩关闭
        area: ['400px','300px'],
        content:$('#changeStateDiv'),
        end:function(){
        }
    });
}

function openPlan() {
    var checkdata = layui.table.checkStatus("iList").data;
    if(checkdata.length == 0){
        layer.msg('请先选中需要修改的制令单');
        return false;
    }
    if(checkdata.length > 1){
        layer.msg('只能选中一条指令单进行修改');
        return false;
    }
    console.log(checkdata[0]);
    layui.form.val("changeQtyDiv", {
        "taskNo":checkdata[0].TASK_NO,
        "newQty" : checkdata[0].QTY_PLAN,
        "manpower":checkdata[0].MANPOWER,
        "capacity":checkdata[0].CAPACITY,
        "manNumber":checkdata[0].MAN_ACT,
    });
    layui.form.render();// 重新渲染
    var index = layer.open({
        type:1,
        title: "修改制令单计划数量",
        // fixed:false,
        // resize :false,
        shadeClose: false,//是否点击遮罩关闭
        area: ['400px','300px'],
        content:$('#changeQtyDiv'),
        end:function(){
            $('#changeQtyDiv')[0].reset();
            layui.form.render();// 必须写
        }
    });
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

//提取工序
function getProcess(){
    layer.load(); //loading
    $.ajax({
        type: "POST",
        data: {},
        url: context+"/produce/scheduling/getProcessProc",
        success: function (res) {
            if (res.result) {
                layer.alert(res.msg,function(){
                    layer.closeAll();
                });
            } else {
                layer.alert(res.msg,function(index){
                    layer.closeAll();
                });
            }
            layer.closeAll('loading'); //关闭loading
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll('loading'); //关闭loading
            });
        }
    });
}

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
