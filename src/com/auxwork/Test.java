package com.auxwork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Test {
    public static void main(String[] args) {
        // 读取文本信息
    	JSONObject result = null;
    	result = JSON.parseObject("{\r\n" + 
    			"	\"appName\": \"KTPDM\"\r\n" + 
    			"}");   
    	String appName=result.getString("appName");
    	System.out.println(appName);   	
    }
}
