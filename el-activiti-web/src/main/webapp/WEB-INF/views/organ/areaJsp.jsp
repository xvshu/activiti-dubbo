<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/js/organ/area.js"></script>
<div style="margin-bottom:20px" id="areaMsgDiv">
    <select id="province" name="province" class="easyui-combobox" label="省：" labelPosition="top"
            data-options="valueField:'code', textField:'provinceName',panelHeight:'200'" style="width:70%;">
    </select>
    <select id="city" name="city" class="easyui-combobox" label="市：" labelPosition="top"
            data-options="valueField:'code', textField:'cityName',panelHeight:'200'" style="width:70%;">
    </select>
    <select id="county" name="county" class="easyui-combobox" label="区县：" labelPosition="top"
            data-options="valueField:'code', textField:'countyName',panelHeight:'200'" style="width:70%;">
    </select>
</div>