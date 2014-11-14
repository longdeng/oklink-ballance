package com.oklink.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author longdeng.zhang@gmail.com
 * @version 创建时间：2014-11-7 下午5:38:29
 * 类说明
 */
public class HttpClientUtil {
	
	/**
	 * httpclient请求
	 * @param url
	 * @param httpMethodType 枚举类型，目前只支付get和post请求
	 * @return
	 */
	public static String send(String url,HttpMethodEnum httpMethodEnum){
		String result = null;
		try {
			HttpClient client=new HttpClient();
			HttpMethod method = httpMethodEnum==HttpMethodEnum.GET?new GetMethod(url):new PostMethod(url);
			if(httpMethodEnum!=HttpMethodEnum.GET){
				method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
			}
			int res = client.executeMethod(method);
			if(res==200){
				InputStream resStream = method.getResponseBodyAsStream();  
			    BufferedReader br = new BufferedReader(new InputStreamReader(resStream));  
			    StringBuffer resBuffer = new StringBuffer();  
			    String resTemp = "";  
			    while((resTemp = br.readLine()) != null){
			          resBuffer.append(resTemp);
			    }  
			    result = resBuffer.toString();  
			}else{
				Logs.geterrorLogger().error("HttpClientUtil send：httpClient请求失败，请求返回结果:"+res);
			}
		}catch(ConnectException ce){
			Logs.geterrorLogger().error("HttpClientUtil send：网络异常"+ce.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s = HttpClientUtil.send("https://www.oklink.com", HttpMethodEnum.POST);
		System.out.println(s);
	}

}
