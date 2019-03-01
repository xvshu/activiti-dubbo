<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html lang="en">
<head>
    <%@ include file="/common/global.jsp"%>
    <title>展示流转状态</title>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <%@ include file="/common/include-jquery-ui-theme.jsp" %>
    <link href="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css" type="text/css" rel="stylesheet" />
    <link href="${ctx }/js/common/plugins/qtip/jquery.qtip.min.css" type="text/css" rel="stylesheet" />
    <%@ include file="/common/include-custom-styles.jsp" %>
    <style type="text/css">
        /* block ui */
        .blockOverlay {
            z-index: 1004 !important;
        }
        .blockMsg {
            z-index: 1005 !important;
        }
    </style>

    <script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/plugins/blockui/jquery.blockUI.js" type="text/javascript"></script>
    <script src="${ctx }/js/module/activiti/workflow.js" type="text/javascript"></script>
    <script src="${ctx }/js/module/activiti/demo-todo.js" type="text/javascript"></script>
</head>

<body>

<a class="trace" id="trace_workflow_imgshow" href='#' pid="${pi_id }" pdid="${pi_processDefinitionId}"></a>
</body>

<script>
    $(function() {
        $("#trace_workflow_imgshow").click();
    });
</script>

</html>
