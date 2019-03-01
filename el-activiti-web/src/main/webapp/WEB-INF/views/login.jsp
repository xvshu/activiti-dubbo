<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>配置中心</title>
	<script>
		var logon = ${not empty user};
		if (logon) {
			location.href = '${ctx}/main/index';
		}
	</script>
	<%@ include file="/common/meta.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/login.css"/>

    <style type="text/css">
        #code
        {
            font-family:Arial;
            font-style:italic;
            font-weight:bold;
            border:0;
            letter-spacing:2px;
            color:blue;
        }
    </style>
    <script type = "text/javascript" src = "${ctx }/js/checkCode.js"></script>

    <script type = "text/javascript" >
        function confirmLogin(){
            return validate();
        }

    </script>

</head>

<body>
<c:if test="${not empty param.error}">
    <c:if test="${param.error == 'nouser'}">
        <%--<h2 id="error" style="text-align: center;color: red;font-size:50px;">用户名或密码错误！！！</h2>--%>
        <script type = "text/javascript" >
            alert("用户名或密码错误！");
        </script>
    </c:if>
    <c:if test="${param.error == 'wrongstats'}">
        <%--<h2 id="error" style="text-align: center;color: red;font-size:50px;">用户名或密码错误！！！</h2>--%>
        <script type = "text/javascript" >
            alert("该用户无权登录！");
        </script>
    </c:if>
</c:if>
<c:if test="${not empty param.timeout}">
    <%--<h2 id="error" style="text-align: center;color: red;font-size:50px;">未登录或超时！！！</h2>--%>
    <script type = "text/javascript" >
        alert("未登录或超时！！！");
    </script>
</c:if>
<div class="login">
    <div class="message">统一配置中心</div>
    <div id="darkbannerwrap"></div>


		<form onsubmit="return confirmLogin()" action="${ctx }/user/logon" method="get">
            <input  id="username" name="username" placeholder="用户名" required="" type="text">
            <hr class="hr15">
            <input id="password" name="password" placeholder="密码" required="" type="password">
            <hr class="hr15">
            <input type = "text" style="width:50%" placeholder="验证码" required="" id = "codeText"/>
            <input type = "button" style="width:48%" id="code" onclick="createCode()"/>
            <hr class="hr15">
            <input value="登录" style="width:100%;" type="submit">
            <hr class="hr20">
		</form>
    </div>
</div>
<div class="copyright">© 2017统一配置中心 by<a href="https://www.eloancn.com/" target="_blank">翼龙贷</a></div>
</body>
</html>
