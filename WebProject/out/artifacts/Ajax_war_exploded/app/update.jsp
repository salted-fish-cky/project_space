<%--
  Created by IntelliJ IDEA.
  User: cky
  Date: 2017/10/17
  Time: 21:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Upload</title>
    <script type="text/javascript">
        window.onload = function () {
            var i = 2;
            document.getElementById("addFile").onclick = function () {
                var tr1 = document.createElement("tr");
                var tr2 = document.createElement("tr");
                tr1.innerHTML = "<td>File"+i+":</td>" +
                    "<td><input type='file' name='file"+i+"'></td>";
                tr2.innerHTML = "<td>Desc"+i+":</td>" +
                    "<td><input type='text' name='desc"+i+"'><button id='delete"+i+"'>delete</button></td>";
                tr1.className = "file";
                tr2.className = "desc";
                this.parentNode.parentNode.parentNode.insertBefore(tr1,this.parentNode.parentNode);
                this.parentNode.parentNode.parentNode.insertBefore(tr2,this.parentNode.parentNode);

                i++;

                /*
                点击删除按钮
                 */
                document.getElementById("delete"+(i-1)).onclick = function () {
                    var tr = this.parentNode.parentNode;
                    var pretr = tr.previousSibling;
                    pretr.parentNode.removeChild(pretr);
                    tr.parentNode.removeChild(tr);


                    //给i排序
                    var files = document.getElementsByClassName("file");
                    var descs = document.getElementsByClassName("desc");
                    for(var j = 0;j<files.length;j++){
                        var n = j+2;
                        files[j].firstChild.innerHTML = "File"+n;
                        files[j].lastChild.firstChild.name = "File"+n;
                    }
                    for(var j = 0;j<descs.length;j++){
                        var n = j+2;
                        descs[j].firstChild.innerHTML = "Desc"+n;
                        descs[j].lastChild.firstChild.name = "Desc"+n;
                    }
                    i--;
                    alert(descs[1].lastChild.firstChild.name);
                }
                return false;

            }
        }
    </script>
</head>
<body>
<center>
    <font color="red">${requestScope.message}</font>
    <br><br>
    <form action="<%=request.getContextPath()%>/upload" method="post" enctype="multipart/form-data">
        <input type="hidden" name="fileNum" id="fileNum" value="1">
        <table>
            <tr>
                <td>File1:</td>
                <td><input type="file" name="file1"></td>
            </tr>
            <tr>
                <td>Desc1:</td>
                <td><input type="text" name="desc1"></td>
            </tr>
            <tr>
                <td><input type="submit" value="submit"></td>
                <td><button id="addFile">新增一个附件</button></td>
            </tr>

        </table>
    </form>
</center>
</body>
</html>
