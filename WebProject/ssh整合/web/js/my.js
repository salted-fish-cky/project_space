//使用ajax加载数据字典，生成select
/**
 * 参数1：数据字典类型
 * 参数2：将下拉选放入标签id
 * 参数3：生成下拉选时，select标签的name属性
 * 参数4：需要回显时，选中哪个option
 */

function  loadSelect(typeCode,positionId,selectName,selectedId) {
    //1.创建select对象，将name属性指定

    var $select = $("<select name="+selectName+"></select>")
    //2.添加提示选项
    $select.append("<option value=''>----请选择----</option>")
    $("#"+positionId).append($select);

    //3.试用ajax方法，访问后台action
    //便利

    //4.返回json数组对象，对其便利
    $.post("${pageContext.request.contextPath}/BaseDictAction",{dict_type_code:typeCode,time:new Date()},function (data) {
        $.each(data,function (i,json) {
            //判断是否需要回显，如果需要使其被选中
            var $option = $("<option value='"+json['dictId']+"'>"+json["dictItemName"]+"</option>")
            if(json['dictId'] == selectedId){

                $option.attr("selected","selected");
            }
            //5.将组装好的select对象放入页面指定位置
            $select.append($option);
        })
    },"json");


}