/**
 * 接口管理
 */
var pageCurr;
var pageCurr2;
$(function () {
    layui.use(['form', 'table','element', 'laydate', 'upload','tableSelect'], function(){
        var table = layui.table
            ,form = layui.form
            ,laydate = layui.laydate
            ,upload = layui.upload
            ,tableSelect = layui.tableSelect
            ,tableSelect2 = layui.tableSelect;
        var element = layui.element;


        laydate.render({
            elem : '#ftime',
            trigger : 'click',
            type : 'datetime', // 默认，可不填
            done : function(value, date, endDate) {
            }
        });

        form.on('select(ftype)', function(data) {
            if(data.value == '解除'){
                $("#time_label").show();//显示div
                $("#time_div").show();
                $("#reason_label").hide();
                $("#reason_div").hide();
                $("#ftimeLong").attr("lay-verify","required");
            }else{
                $("#time_label").hide();//显示div
                $("#time_div").hide();
                $("#reason_label").show();
                $("#reason_div").show();
                $("#ftimeLong").attr("lay-verify","");
            }
            form.render('select');//select是固定写法 不是选择器
            return false;
        });



        getScheduling();

        //监听搜索框
        form.on('submit(search)', function(data){
            //重新加载table
            load(data);
            return false;
        });
        form.on('submit(editBtn)', function(data){
            //新增
           // doEdit();
            return false;
        });
        // form.on('select(itemId)', function(data){
        //     var itemId = data.value;
        //     for(var i = 0; i < itemList.length; i++){
        //         if(itemId == itemList[i].id){
        //             $("#itemName").val(itemList[i].itemName);
        //             break;
        //         }
        //     }
        //     return false;
        // });
        laydate.render({
            elem: '#prodDate'
        });

        tableSelect.render({
            elem : '#empId1',
            searchKey : 'keyword',
            checkedKey : 'id',
            searchPlaceholder : '员工搜索',
            table : {
                url:  context +'/base/employee/getList?empStatus=1',
                method : 'get',
                cols : [ [
                    { type: 'radio' },//多选  radio
                    , {
                        field : 'id',
                        title : 'id',
                        width : 0,hide:true
                    }
                    , {
                        field : 'empCode',
                        title : '员工工号',
                        width : 110
                    },{
                        field : 'empName',
                        title : '员工姓名',
                        width : 200
                    } ] ],
                page : true,
                request : {
                    pageName : 'page' // 页码的参数名称，默认：page
                    ,
                    limitName : 'rows' // 每页数据量的参数名，默认：limit
                },
                parseData : function(res) {
                    if(res.result){
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
                //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
                console.log(data);
                var da=data.data;
                console.log(da[0])
                form.val("editForm1", {
                    "empId1":da[0].empName,
                });
                layui.form.render();// 重新渲染
            }
        });

        tableSelect2.render({
            elem : '#empId2',
            searchKey : 'keyword',
            checkedKey : 'EMP_ID',
            searchPlaceholder : '员工搜索',
            table : {
                url:  context +'/produce/scheduling/getEmpList',
                method : 'get',
                where:{ mid:id },
                cols : [ [
                    { type: 'checkbox' },//多选  radio
                    , {
                        field : 'EMP_ID',
                        title : 'EMP_ID',
                        width : 0,hide:true
                    }
                    , {
                        field : 'EMP_CODE',
                        title : '员工工号',
                        width : 110
                    },{
                        field : 'EMP_NAME',
                        title : '员工姓名',
                        width : 200,
                        align:'center'
                    },
                    {
                        field : 'DEPT_NAME',
                        title : '部门',
                        width : 120
                    }
                    ] ],
                page : true,
                request : {
                    pageName : 'page' // 页码的参数名称，默认：page
                    ,
                    limitName : 'rows' // 每页数据量的参数名，默认：limit
                },
                parseData : function(res) {
                    if(res.result){
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
                //选择完后的回调，包含2个返回值 elem:返回之前input对象；data:表格返回的选中的数据 []
                console.log(data);
                var da=data.data;
                var names = "";
                data.data.forEach(function(element) {
                    names += element.EMP_NAME+",";
                });
                if(names !=""){
                    names = names.substring(0,names.length-1);
                }
                form.val("editForm2", {
                    "empId2":names,
                });
                layui.form.render();// 重新渲染
            }

        });

        element.on('tab(tabFilter)', function () {
            var tableId = this.getAttribute('lay-id');
            if(tableId =='list1'){
                table.render({
                    elem: '#iList'
                    ,url:context+'/produce/scheduling/getProcessList'
                    ,method: 'get' //默认：get请求
                    ,where:{
                        mid: function(){
                            return  $("#taskNo").val();
                        }
                    }
                    ,cellMinWidth: 80
                    ,height:'full-150'//固定表头&full-查询框高度
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
                        // ,{type:"checkbox", field:'checkColumn'}
                        ,{field:'PROC_ORDER', title:'工序顺序', width:100, sort:true}
                        ,{field:'PROC_NO', title:'工序编号', width:100}
                        ,{field:'PROC_NAME', title:'工序名称', width:250}
                        ,{field:'JOB_ATTR', title:'过程属性', width:100, templet:'#statusTpl'}
                        // ,{field:'EMP_NAME', title:'作业人员', width:150}
                        ,{field:'QTY_IN', title:'投入数', width:150}
                        ,{field:'QTY_OUT', title:'产出数', width:150}
                        // ,{fixed:'right', title:'操作',width:150, align:'center', toolbar:'#optBar'}
                        //,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
                    ]]
                    ,done: function(res, curr, count){
                        pageCurr=curr;
                        // for(var i = 0; i < res.data.length; i++){
                        //     if(res.data[i].isCheck == "1"){
                        //         res.data[i]["LAY_CHECKED"]='true';
                        //         $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', true);
                        //         $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().addClass('layui-form-checked');
                        //         // $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] div.layui-form-checkbox').addClass('layui-form-checked');
                        //         // $('tbody tr[data-index="'+i+'"]  div.layui-form-checkbox').addClass('layui-form-checked');
                        //     }
                        // }
                    }
                });
            }else if(tableId =='list2'){
                tableIns2 = table.render({
                    elem: '#iList2'
                    ,url:context+'/produce/scheduling/getItemList'
                    ,method: 'get' //默认：get请求
                    ,where:{ mid:id }
                    ,cellMinWidth: 80
                    ,height:'full-150'//固定表头&full-查询框高度
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
                        ,{field:'itemNo', title:'物料编号', width:150}
                        ,{field:'itemName', title:'物料描述',width:300, templet:'<span>{{d.mtrial ? d.mtrial.itemName : ""}}</span>'}
                        ,{field:'itemQty', title:'组件用量', width:90}
                        ,{field:'itemUnit', title:'组件单位', width:90}
                        ,{field:'itemQtyPr', title:'单位用量', width:90}
                        ,{field:'fokRate', title:'良率%', width:90}
                        ,{field:'empNames', title:'作业员', width:90}
                        ,{fixed:'right', title:'操作',width:90, align:'center', toolbar:'#optBar2'}
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
            }else if(tableId =='list3'){
                tableIns3=table.render({
                    elem: '#iList3'
                    ,url:context+'/produce/scheduling/getEmpList'
                    ,method: 'get' //默认：get请求
                    ,where:{ mid:id }
                    ,cellMinWidth: 80
                    ,height:'full-150'//固定表头&full-查询框高度
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
                        ,{field:'EMP_CODE', title:'员工工号', width:90}
                        ,{field:'EMP_NAME', title:'员工姓名', width:90,}
                        ,{field:'EMP_TYPE', title:'员工类型', width:120}
                        ,{field:'DEPT_NAME', title:'部门名称', width:100}
                        ,{field:'TIME_BEGIN', title:'上线时间', width:150,}
                        ,{field:'TIME_END', title:'下线时间', width:150,}
                        ,{field:'CREATE_DATE', title:'分配时间', width:150,}
                        ,{field:'FTIMES', title:'上线时长(小时)', width:120,},
                        {field : 'TIME_BEGIN_HD', title : '统一上线时间', sort : true, width:150},
                        {field : 'TIME_END_HD', title : '统一下线时间', sort : true, width:150}
                        // ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar2'}
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
            }else if(tableId =='list4'){
                table.render({
                    elem: '#iList4'
                    ,url:context+'/produce/scheduling/getProdOrderList'
                    ,method: 'get' //默认：get请求
                    ,where:{ mid:id }
                    ,cellMinWidth: 80
                    ,height:'full-150'//固定表头&full-查询框高度
                    ,page: true,
                    align:'center',
                    request: {
                        pageName: 'page' //页码的参数名称，默认：page
                        ,limitName: 'rows' //每页数据量的参数名，默认：limit
                    },
                    parseData: function (res) {
                    	//console.log(res)
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
                        ,{field:'FEED_TYPE', title:'投料类型', width:100,align:'center',}
                        ,{field:'ITEM_BARCODE', title:'物料条码', width:180,}
                        ,{field:'ITEM_NAME', title:'物料名称', width:190,}
                        ,{field:'ITEM_NO', title:'物料编码', width:145}
                        // ,{field:'ITEM_MODEL', title:'机型', width:80,align:'center',}
                        ,{field:'QUANTITY', title:'投料数量(PCS)', width:120,align:'center',}
                        ,{field:'USER_NAME', title:'操作人', width:100,align:'center',}
                        ,{field:'CREATE_DATE', title:'操作时间', width:160,align:'center',}
                        // ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar2'}
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
            }else if(tableId =='list5'){
                table.render({
                    elem: '#iList5'
                    ,url:context+'/produce/scheduling/getProdOrderOutList'
                    ,method: 'get' //默认：get请求
                    ,where:{
                        mid: function(){
                            return  $("#taskNo").val();
                        }
                    }
                    ,cellMinWidth: 80
                    ,height:'full-150'//固定表头&full-查询框高度
                    , limit:50,
                    // ,page: true,
                    align:'center',
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
                        ,{field:'PROC_ORDER', title:'工序顺序', width:80,align:'center',}
                        ,{field:'PROC_NAME', title:'工序名称', width:90,align:'center',}
                        ,{field:'SCAN_TYPE', title:'产出类型', width:90,align:'center',}
                        ,{field:'ITEM_BARCODE', title:'产品条码', width:160,align:'center',}
                        ,{field:'ITEM_NO', title:'产品编码', width:140,align:'center',}
                        ,{field:'QUANTITY', title:'产出/送检数', width:100,align:'center',}
                        ,{field:'USER_NAME', title:'操作人', width:80,align:'center',}
                        ,{field:'CREATE_DATE', title:'操作时间', width:150,align:'center',}
                        ,{field:'ITEM_NAME', title:'产品名称', width:140,align:'center',}
                        // ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar2'}
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
            }else if(tableId =='list6'){
                table.render({
                    elem: '#iList6'
                    ,url:context+'/produce/scheduling/getProdOrderQcList'
                    ,method: 'get' //默认：get请求
                    ,where:{
                        mid: function(){
                            return  $("#taskNo").val();
                        }
                    }
                    ,cellMinWidth: 80
                    ,height:'full-150'//固定表头&full-查询框高度
                    , limit:50,
                    // ,page: true,
                    align:'center',
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
                        {fixed:'left',type:'numbers'}
                        ,{fixed:'left',field:'PROC_ORDER', title:'工序顺序', width:80,align:'center',}
                        ,{fixed:'left',field:'PROC_NAME', title:'工序名称', width:90,}
                        ,{fixed:'left',field:'LOT_NO', title:'虚拟批次', width:80,}
                        ,{fixed:'left',field:'ITEM_BARCODE', title:'产品条码', width:160}
                        ,{field:'ITEM_NO', title:'产品编码', width:140,align:'center',}
                        ,{field:'ITEM_NAME', title:'产品名称', width:150,align:'center',}
                        ,{field:'QUANTITY', title:'检验数', width:80,align:'center',}
                        ,{field:'QTY_PROC', title:'检验总数', width:80,align:'center',}
                        ,{field:'SAMPLE_QTY', title:'抽检总数', width:80,align:'center',}
                        ,{field:'QTY_DONE', title:'抽检合格数', width:90,align:'center',}
                        ,{field:'USER_NAME', title:'抽检人', width:80,align:'center',}
                        ,{field:'CREATE_DATE', title:'操作时间', width:150,align:'center',}
                        // ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar2'}
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
            }else if(tableId =='list7'){
                tableIns7 = table.render({
                    elem: '#iList7'
                    ,url:context+'/produce/scheduling/getProdOrderErrList'
                    ,method: 'get' //默认：get请求
                    ,where:{
                        mid: function(){
                            return  $("#taskNo").val();
                        }
                    }
                    ,cellMinWidth: 80
                    ,height:'full-150'//固定表头&full-查询框高度
                    ,
                    // limit:50,
                    page: true,
                    align:'center',
                    request: {
                        pageName: 'page' //页码的参数名称，默认：page
                        ,limitName: 'rows' //每页数据量的参数名，默认：limit
                    },
                    parseData: function (res) {
                        // 可进行数据操作
                        return {
                            "count": res.data.count,
                            "msg":res.msg,
                            "data":res.data.rows,
                            "code": res.status //code值为200表示成功
                        }
                    },
                    cols: [[
                        {type:'numbers'}
                        // ,{field:'FTYPE', title:'登记类型', width:100,align:'center',}
                        ,{field:'ITEM_NO', title:'物料编码', width:140,}
                        ,{field:'ITEM_NAME', title:'物料名称', width:150,}
                        ,{field:'CUST_NAME', title:'客户名称', width:90}
                        ,{field:'DEPT_NAME', title:'部门名称', width:80,align:'center',}
                        ,{field:'FTIME', title:'登记时间', width:100,align:'center',}
                        ,{field:'DESCRIPTION', title:'异常描述', width:145}
                        ,{field:'FOR_REASON', title:'异常原因', width:80,align:'center',}
                        ,{field:'FTIME_LONG', title:'异常时长', width:80,align:'center',}
                        ,{field:'CREATE_DATE', title:'创建时间', width:150,align:'center',}
                        ,{field:'RELEASE_TIME', title:'解除时间', width:150,align:'center',}
                        // ,{fixed:'right', title:'操作', align:'center', toolbar:'#optBar2'}
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
            }
        });


        //监听工具条-工艺维护
        table.on('tool(iTable)', function(obj){
            var data = obj.data;
            if(obj.event === 'edit1'){
                //编辑
                getProcess(data,data.id);
            }
        });
        form.on('submit(editSubmit1)', function(data){
            //编辑-工艺维护
            doEditProcess(data.field);
            return false;
        });
        form.on('submit(saveProcess)', function(data){
            //保存-工艺维护
            saveProcess(table);
            return false;
        });

        tableIns=table.render({
            elem: '#iList'
            ,url:context+'/produce/scheduling/getProcessList'
            ,method: 'get' //默认：get请求
            ,where:{
                mid: function(){
                    return  $("#taskNo").val();
                }
            }
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
                // ,{type:"checkbox", field:'checkColumn'}
                ,{field:'PROC_ORDER', title:'工序顺序', width:100, sort:true}
                ,{field:'PROC_NO', title:'工序编号', width:100}
                ,{field:'PROC_NAME', title:'工序名称', width:250}
                ,{field:'JOB_ATTR', title:'过程属性', width:100, templet:'#statusTpl'}
                // ,{field:'EMP_NAME', title:'作业人员', width:150}
                ,{field:'QTY_IN', title:'投入数', width:150}
                ,{field:'QTY_OUT', title:'产出数', width:150}
                // ,{fixed:'right', title:'操作',width:150, align:'center', toolbar:'#optBar'}
                //,{fixed:'right', title:'操作', width:200, align:'center', toolbar:'#optBar'}
            ]]
            ,done: function(res, curr, count){
                pageCurr=curr;
                // for(var i = 0; i < res.data.length; i++){
                //     if(res.data[i].isCheck == "1"){
                //         res.data[i]["LAY_CHECKED"]='true';
                //         $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', true);
                //         $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().addClass('layui-form-checked');
                //         // $('tbody tr[data-index="'+i+'"] td[data-field="checkColumn"] div.layui-form-checkbox').addClass('layui-form-checked');
                //         // $('tbody tr[data-index="'+i+'"]  div.layui-form-checkbox').addClass('layui-form-checked');
                //     }
                // }
            }
        });

        getAddList();

        form.on('select(fdemoName)', function(data){//监听选择事件
            //var params={"client":$("#pkClient").val()}
            getProcByClient($("#fdemoName").val());
        });

        // 监听
        form.on('submit(addSubmit2)', function(data) {
            var params={
                "fname":$("#fdemoName").val(),
                "mid":id,
            };
            // $("#fdemoName").val();
            CoreUtil.sendAjax("/produce/scheduling/saveProc", JSON.stringify(params), function(
                data) {
                if (data.result) {
                    loadAll1();
                    layer.alert("操作成功", function() {
                        layer.closeAll();
                        // 加载页面
                    });
                } else {
                    layer.alert(data.msg, function() {
                        layer.closeAll();
                    });
                }
            }, "POST", false, function(res) {
                layer.alert(res.msg);
            });
            return false;
        });


        // 监听异常原因的提交
            form.on('submit(addSubmit7)', function(obj) {
                 // console.log(obj.field);
                CoreUtil.sendAjax("/abnormalProduct/add", JSON.stringify(obj.field),
                    function(data) {
                        if (data.result) {
                            layer.alert("操作成功", function() {
                                layer.closeAll();
                                // cleanAbnormalHours();
                                // 加载页面
                                loadAll7();
                            });
                        } else {
                            layer.alert(data.msg);
                        }
                    }, "POST", false, function(res) {
                        layer.alert(res.msg);
                    });
                return false;
            });


        tableProc=table.render({
            elem : '#procList',
            limit: 40,
            method : 'get' ,// 默认：get请求
            cols : [ [ {
                type : 'numbers'
            },
                {
                    field : 'process.procNo',
                    title : '编码', width: 200,
                    templet:function (d) {
                        return d.process.procNo;
                    }
                }, {
                    field : 'process.procName',
                    title : '名称', width: 200,
                    templet:function (d) {
                        return d.process.procName;
                    }
                }, {
                    field : 'jobAttr',width:200,
                    title : '过程属性',
                    templet:'#statusTp2'
                },
                ] ],
            data:[]
        });

        //监听工具条-工单组件
        table.on('tool(iTable2)', function(obj){
            var data = obj.data;
            if(obj.event === 'edit2'){
                //编辑
                getItem(data,data.id);
            }
        });
        form.on('submit(editSubmit2)', function(data){
            //编辑-工单组件
            doEditItem();
            return false;
        });
     // 监听勾选过程属性操作
		form.on('checkbox(isStatusTpl)', function(obj) {
			 //当前元素
            var data = $(obj.elem);
			var rowIndex = data.parents('tr').first().attr("data-index");
			//console.log(rowIndex)
			// if(obj.elem.checked){
			// 	//res.data[rowIndex]["LAY_CHECKED"]='true';
            //     $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', true);
            //     $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().addClass('layui-form-checked');
			// }else{
			//     $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').prop('checked', false);
	        //     $('tbody tr[data-index="'+rowIndex+'"] td[data-field="checkColumn"] input[type="checkbox"]').next().removeClass('layui-form-checked');
			// }
			
		});
		//监听行选择事件---根据顺序（错误），应当获取行号【未修改】
	   table.on('checkbox(iTable)', function(obj){
			if(!obj.checked){	 
				  obj.data.jobAttr = 0;
				  var rowIndex=$(obj.tr).attr("data-index");
				  console.log(rowIndex)
				  $('tbody tr[data-index="'+rowIndex+'"] td[data-field="jobAttr"] input[type="checkbox"]').prop('checked', false);
				 $('tbody tr[data-index="'+rowIndex+'"] td[data-field="jobAttr"] input[type="checkbox"]').next().removeClass('layui-form-checked');
			  }
			});		


    });
});

function getReasonSelect(editReason) {
    CoreUtil.sendAjax("/abnormalProduct/getErrorInfo", "", function(data) {
        if (data.result) {
            $("#forReason").empty();
            var forReason = data.data;
            for (var i = 0; i < forReason.length; i++) {
                if(forReason[i].abnormalType ==editReason) {
                    $("#forReason").append(
                        "<option value=" + forReason[i].ID + "  selected='selected'>"
                        + forReason[i].ERR_NAME + "</option>");
                }else{
                    $("#forReason").append(
                        "<option value=" + forReason[i].ID + ">"
                        + forReason[i].ERR_NAME + "</option>");
                }
            }
            layui.form.render('select');
        } else {
            layer.alert(data.msg);
        }
    }, "GET", false, function(res) {
        layer.alert(res.msg);
    });
    return false;
}

//根据客户信息获取工序数据
function getProcByClient(params){
    var params={"fdemoName":params}
    CoreUtil.sendAjax("/base/client_proc/getClientItem", JSON.stringify(params), function(
        data) {
        console.log(data);
        if (data.result) {
            tableProc.reload({
                data : data.data,
                done : function(res, curr, count) {
                }
            });
        } else {
            layer.alert(data.msg);
        }
    }, "POST", false, function(res) {
        layer.alert(res.msg);
    });
}

//获取客户，工序信息
function getAddList(id){
    CoreUtil.sendAjax("/base/prodproc/getAddList", "",
        function(data) {
            if (data.result) {
                $("#fdemoName").empty();
                var c=data.data.Client;
                for (var i = 0; i < c.length; i++) {
                    if(i==0){
                        $("#fdemoName").append("<option value=''>请点击选择</option>");
                    }
                    if(id != ''){
                        if(c[i].id == id){
                            $("#fdemoName").append("<option value=" + c[i][0]+ " selected>"+c[i][1]+"</option>");
                        }else{
                            $("#fdemoName").append("<option value=" + c[i][0]+ ">"+c[i][1]+"</option>");
                        }
                    }else{
                        $("#fdemoName").append("<option value=" + c[i][0]+ ">"+c[i][1]+"</option>");
                    }
                }
                layui.form.render('select');
            } else {
                layer.alert(data.msg)
            }
            //console.log(data)
        }, "POST", false, function(res) {
            layer.alert("操作请求错误，请您稍后再试");
        });
}


//新增编辑弹出框
function openProc() {
     $("#taskNo2").val(id);
    var index = layer.open({
        type : 1,
        title : "更新产品工艺",
        fixed : false,
        resize : false,
        shadeClose : true,
        //area : [ '800px','410px'],
        content : $('#setClientProc'),
        end : function() {
            // cleanProc();
        }
    });
    layer.full(index);
}

//渲染基本信息
function getScheduling(){
	/*
    var optionHtml = '<option value=""></option>';
    var optionHtml2 = '<option value=""></option>';
    var optionHtml3 = '';
    var optionHtml4 = '<option value=""></option>';
    //添加物料列表
    for(var i = 0; i < itemList.length; i++){
        optionHtml += '<option value="'+itemList[i].id+'">'+itemList[i].itemNo+'</option>';
    }
    $("#itemId").html(optionHtml);
    //添加客户信息
    for(var i = 0; i < clientList.length; i++){
        optionHtml2 += '<option value="'+clientList[i].id+'">'+clientList[i].custName+'</option>';
    }
    $("#custId").html(optionHtml2);
    //添加组长信息
    
    var lineData=lineList[2];
    for(var i = 0; i < lineData.length; i++){
        optionHtml3 += '<option value="'+lineData[i].ORG_NAME+'">'+lineData[i].ORG_NAME+'</option>';
    }
    $("#linerName").html(optionHtml3);
    //添加部门信息
    for(var i = 0; i < orgList.length; i++){
        optionHtml4 += '<option value="'+orgList[i].id+'">'+orgList[i].orgName+'</option>';
    }
    $("#deptId").html(optionHtml4);
*/
	console.log(scheduling)
    $("#id").val(id);
    $("#qytDone").val(qty);
    $("#rateDone").val(rate);
    $("#inputSum").val(input);//2020-12-08新增
    $("#taskNo").val(scheduling.taskNo);
    $("#prodNo").val(scheduling.prodNo);
    $("#produceState").val(scheduling.produceState);
    $("#itemId").val(scheduling.itemNo); //显示编码
    $("#itemName").val(scheduling.itemName);
    $("#custName").val(scheduling.custNameS);
    $("#groupNo").val(scheduling.groupNo);
    $("#custId").val(scheduling.custId);
    $("#linerName").val(scheduling.linerName);
    $("#prodDate").val(scheduling.prodDate);
    $("#deptId").val(scheduling.deptName);//2020-12-08 scheduling.deptId->scheduling.deptName
    $("#classNo").val(scheduling.classNo);
    $("#qtyPlan").val(scheduling.qtyPlan);

    //渲染
   // layui.form.render('select');
}

//编辑
function doEdit(){
    $.ajax({
        type: "POST",
        data: $("#editForm").serialize(),
        url: context+"/produce/scheduling/edit",
        success: function (data) {
            if (data.result) {
                layer.alert("操作成功",function(){
                    layer.closeAll();
                });
            } else {
                layer.alert(data.msg,function(){
                    layer.closeAll();
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
            });
        }
    });
}

//获取编辑信息-工艺维护
function getProcess(obj, id){
    // var optionHtml = '<option value=""></option>';
    // //添加作业员列表
    // for(var i = 0; i < employeeList.length; i++){
    //     optionHtml += '<option value="'+employeeList[i].id+'">'+employeeList[i].empName+'</option>';
    // }
    // $("#empId1").html(optionHtml);
    console.log(obj);
    $("#processId1").val(id);
    $("#mid1").val(obj.mid);
    $("#procOrder1").val(obj.procOrder);
    // $("#jobAttr1").val(obj.jobAttr);
    if(obj.jobAttr==1){
        $("#jobAttr1").prop("checked", true);
    }else{
        $("#jobAttr1").prop("checked", false);
    }
    $("#empId1").val(obj.empName);
    $("#empId1").attr('ts-selected',obj.empId);
    $("#procNo1").val(obj.procNo);
    $("#procName1").val(obj.procName);

    //渲染
    layui.form.render();
    layui.form.render('select');
    layui.form.render('checkbox');

    //打开弹出框
    openProcess("编辑工艺");
}
//编辑弹出框-工艺维护
function openProcess(title){
   var index= layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['600px'],
        content:$('#editDiv1'),
        end:function(){
            cleanProcess();
            loadAll1();
        }
    });
   layer.full(index);
}
//清空编辑框数据-工艺维护
function cleanProcess(){
    $("#processId1").val("");
    $("#mid1").val("");
    $("#procOrder1").val("");
    $("#jobAttr1").prop("checked", false);
    $("#empId1").val("");
    $("#procNo1").val("");
    $("#procName1").val("");

    //渲染
    layui.form.render('select');
    layui.form.render('checkbox');
}
//编辑-工艺维护
function doEditProcess(obj){
    //checkbox转换
    if(!obj.jobAttr){
        obj.jobAttr = 0;
    }
    obj.empId = $('#empId1').attr("ts-selected");
    CoreUtil.sendAjax("/produce/scheduling/editProcess",JSON.stringify(obj),function (res) {
        if (res.result == true) {
            layer.alert("编辑成功",function(){
                layer.closeAll();
                loadAll1();
            });
        } else {
            layer.alert(res.msg,function(){
                layer.closeAll();
            });
        }
    },"POST",false,function (res) {
        layer.alert("操作请求错误，请您稍后再试",function(){
            layer.closeAll();
        });
    });

    // $.ajax({
    //     type: "POST",
    //     data: $("#editForm1").serialize(),
    //     url: context+"/produce/scheduling/editProcess",
    //     success: function (res) {
    //         if (res.result) {
    //             layer.alert("编辑成功",function(){
    //                 layer.closeAll();
    //                 loadAll1();
    //             });
    //         } else {
    //             layer.alert(res.msg,function(){
    //                 layer.closeAll();
    //             });
    //         }
    //     },
    //     error: function () {
    //         layer.alert("操作请求错误，请您稍后再试",function(){
    //             layer.closeAll();
    //         });
    //     }
    // });
}

//保存工艺
function saveProcess(table) {
    //获取选中工艺
    var processIds = ""; //lst-获取的jobAttr有问题
   // var processIds1 = "";
   // var checkStatus = table.checkStatus("iList");
    var checkStatus = table.cache.iList;
    $('tbody tr td[data-field="checkColumn"] input[type="checkbox"]').each(function(i){
        if ($(this).is(":checked")) {
        	//fyx-202011-02
        	var checks = $('tbody tr[data-index="'+i+'"] td[data-field="jobAttr"] input[type="checkbox"]:checked');
        	if(checks.length == 1){
        		processIds += checkStatus[i].procId+"@1,";
        	}else{
        		processIds += checkStatus[i].procId+"@0,";
        	}
			    //操作
        	//processIds += checkStatus[i].procId+",";
        	//processIds += checkStatus[i].procId+"@"+checkStatus[i].jobAttr+",";
			  }
         });
    console.log(processIds)

   /* for(var i = 0; i < checkStatus.data.length; i++){
        if(i == 0){
            processIds += checkStatus.data[i].procId;
        }else{
            processIds += "," + checkStatus.data[i].procId;
        }
    }*/

    var param = {
        mid: $("#id").val(),
        processIds: processIds
    }

    $.ajax({
        type: "POST",
        data: param,
        url: context+"/produce/scheduling/saveProcessProc",
        success: function (res) {
            if (res.result) {
                layer.alert("保存成功！",function(){
                    layer.closeAll();
                    loadAll1();
                });
            } else {
                layer.alert(res.msg,function(){
                    layer.closeAll();
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试！",function(){
                layer.closeAll();
            });
        }
    });
}

//获取编辑信息-工单组件
function getItem(obj, id){

    $("#itemId2").val(id);
    $("#mid2").val(obj.mid);
    $("#itemNo2").val(obj.itemNo);
    $("#itemName2").val(obj.mtrial ? obj.mtrial.itemName : "");
    $("#itemQty2").val(obj.itemQty);
    $("#empId2").val(obj.empNames);
    $("#empId2").attr('ts-selected',obj.empIds);
    // if(obj.employee!=null&&obj.employee!=undefined) {
    //     $("#empId2").val(obj.employee.empName);
    // }

    //渲染
    layui.form.render('select');

    //打开弹出框
    openItem("编辑工单组件");
}
//编辑弹出框-工单组件
function openItem(title){
    layer.open({
        type:1,
        title: title,
        fixed:false,
        resize :false,
        shadeClose: true,
        area: ['600px'],
        content:$('#editDiv2'),
        end:function(){
            cleanItem();
        }
    });
}
//清空编辑框数据-工单组件
function cleanItem(){
    $("#itemId2").val("");
    $("#mid2").val("");
    $("#itemNo2").val("");
    $("#itemName2").val("");
    $("#itemQty2").val("");
    $("#empId2").val("");

    //渲染
    layui.form.render('select');
}
//编辑-工单组件
function doEditItem(){
    var empId = $('#empId2').attr("ts-selected");
    console.log(empId);
    $("#empIds").val(empId);
    $("#empNames").val($('#empId2').val());
    $.ajax({
        type: "POST",
        data: $("#editForm2").serialize(),
        url: context+"/produce/scheduling/editItem",
        success: function (res) {
            if (res.result) {
                layer.alert("编辑成功",function(){
                    layer.closeAll();
                    loadAll2();
                });
            } else {
                layer.alert(res.msg,function(){
                    layer.closeAll();
                });
            }
        },
        error: function () {
            layer.alert("操作请求错误，请您稍后再试",function(){
                layer.closeAll();
            });
        }
    });
}

// 添加异常原因
function addProdErr() {
    // 清空弹出框数据
    getProdErr();
    getReasonSelect("");
    // 打开弹出框
    openProdErr(null, "添加异常原因");
}

function getProdErr() {
    $('#taskNo7').val(scheduling.taskNo);
    $('#itemName7').val(scheduling.itemName);
    $('#itemNo').val(scheduling.itemNo);
    $('#custNameS').val(scheduling.custNameS);
    $('#linerName7').val(scheduling.linerName);
    layui.form.render();
}

// 清空新增表单数据
function cleanProdErr() {
    $('#cardForm')[0].reset();
    layui.form.render();// 必须写
}

// 新增编辑弹出框
function openProdErr(id, title) {
    var index=layer.open({
        type : 1,
        title : title,
        fixed : false,
        resize : false,
        shadeClose : true,
        area : [ '550px' ],
        content : $('#setAbnormalHours'),
        end : function() {
            cleanProdErr();
        }
    });
    layer.full(index);
}


//重新加载表格-工艺维护（全部）
function loadAll1(){
    //重新加载table
    tableIns.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}

function loadAll7(){
    //重新加载table
    tableIns7.reload({
        page: {
            curr: pageCurr //从当前页码开始
        }
    });
}
//重新加载表格-工单组件（全部）
function loadAll2(){
    //重新加载table
    tableIns2.reload({
        page: {
            curr: pageCurr2 //从当前页码开始
        }
    });
}

