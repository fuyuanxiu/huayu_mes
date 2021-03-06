/**
 * 权限列表
 */
$(function() {
    //初始化treegrid 页面表格
    layui.config({
        base: context+'/treegrid/'
    }).use(['laytpl', 'treegrid'], function () {
        var laytpl = layui.laytpl,
            treegrid = layui.treegrid;
        treegrid.config.render = function (viewid, data) {
            var view = document.getElementById(viewid).innerHTML;
            return laytpl(view).render(data) || '';
        };

        var treeForm=treegrid.createNew({
            elem: 'permTable',
            view: 'view',
            data: { rows: permList },
            parentid: 'parenId',
            singleSelect: false,
            onCollapse:true
        });
        treeForm.build();

    });
    //操作
    layui.use('form', function(){
        var form = layui.form;
        //监听提交
        form.on('submit(permSubmit)', function(data){
            //校验 TODO
            $.ajax({
                type: "POST",
                data: $("#permForm").serialize(),
                url: context+"/quoteSum/getQuoteTreeList",
                success: function (data) {
                    if (data.result) {
                        layer.alert("操作成功",function(){
                            layer.closeAll();
                        });
                    } else {
                        layer.alert(data.msg);
                    }
                },
                error: function (data) {
                    layer.alert("操作请求错误，请您稍后再试");
                }
            });
            return false;
        });
        form.render();
    });

});

function edit(id,type){
    if(null!=id){
        $("#type").val(type);
        $("#id").val(id);
        $.get(context+"/sysPermission/getPerm",{"id":id},function(data) {
            // console.log(data);
            if(data.result){
                $("input[name='menuName']").val(data.data.menuName);
                $("input[name='menuCode']").val(data.data.menuCode);
                $("input[name='pageUrl']").val(data.data.pageUrl);
                $("input[name='zindex']").val(data.data.zindex);
                $("input[name='menuIcon']").val(data.data.menuIcon);
                $("textarea[name='description']").text(data.data.description);
                $("#parentId").val(data.data.parentId);
                data.data.istype==0?$("input[name='istype']").val(0).checked:$("input[name='istype']").val(1).checked;
                var index=layer.open({
                    type:1,
                    title: "更新权限",
                    fixed:false,
                    resize :false,
                    shadeClose: true,
                    area: ['500px'],
                    content:$('#updatePerm'),
                    end:function(){
                        location.reload();
                    }
                });
                layer.full(index);//弹出框全屏
            }else{
                layer.alert(data.msg);
            }
        });
    }
}
//开通权限
function addPerm(pid,flag){
    if(null!=pid){
        //flag[0:开通权限；1：新增子节点权限]
        //type[0:编辑；1：新增]
        if(flag==0){
            $("#type").val(1);
            $("#parentId").val(0);
        }else{
            //设置父id
            $("#type").val(1);
            $("#parentId").val(pid);
        }
       var index= layer.open({
            type:1,
            title: "添加权限",
            fixed:false,
            resize :false,
            shadeClose: true,
            area: ['500px'],
            content:$('#updatePerm'),  //页面自定义的div，样式自定义
            end:function(){
                location.reload();
            }
        });
       layer.full(index);//弹出框全屏
    }
}

function del(id,name){
    // console.log("===删除id："+id);
    if(null!=id){
        layer.confirm('您确定要删除'+name+'权限吗？', {
            btn: ['确认','返回'] //按钮
        }, function(){
            $.post(context+"/sysPermission/delete",{"id":id},function(data){
            	//alert(data.result)
                if(data.result){
                    //回调弹框
                    layer.alert("删除成功！",function(){
                        layer.closeAll();
                        //加载load方法
                        location.reload();//自定义
                    });
                }else{
                    layer.alert(data.msg);//弹出错误提示
                }
            });
        }, function(){
            layer.closeAll();
        });
    }

}

//关闭弹框
function close(){
    layer.closeAll();
}