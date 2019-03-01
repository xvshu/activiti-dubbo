<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
	<%@ include file="/common/global.jsp"%>
	<title>设置示例用户</title>
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <%@ include file="/common/include-jquery-ui-theme.jsp" %>
    <link href="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css" type="text/css" rel="stylesheet" />
    <link href="${ctx }/js/common/plugins/qtip/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
    <%@ include file="/common/include-custom-styles.jsp" %>

    <script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js" type="text/javascript"></script>
	<script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js" type="text/javascript"></script>
	<script src="${ctx }/js/module/activiti/workflow.js" type="text/javascript"></script>
	<script type="text/javascript">
	$(function() {
		// 跟踪
	    $('.trace').click(graphTrace);
	});
	</script>
</head>

<body>
<div class="container showgrid">
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">${message}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
            setTimeout(function() {
                $('#message').hide('slow');
            }, 5000);
		</script>
	</c:if>
	<c:if test="${not empty error}">
		<div id="error" class="alert alert-error">${error}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
            setTimeout(function() {
                $('#error').hide('slow');
            }, 5000);
		</script>
	</c:if>
	<form:form id="inputForm" action="${ctx}/demo/activiti/setdemouser" method="post" class="form-horizontal">
		<fieldset>
			<legend><small>设置流程示例用户</small></legend>
			<table border="1">
				<tr>
					<td>
						<input type="text" name="userId" id="userId">
					</td>
					<td>
						<button type="submmit">提交</button>
					</td>
				</tr>
			</table>
		</fieldset>
	</form:form>
</div>

<script type="text/javascript">

    function setdemouser() {
        $("#userId").val("demo-user");
    }

    function setdemojingli() {
		$("#userId").val("demo-jingli");
    }

    function setdemozongjian() {
        $("#userId").val("demo-zongjian");
    }
</script>
</body>
</html>
