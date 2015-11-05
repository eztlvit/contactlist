package com.zzt.springmvc.contactlist.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


import com.zzt.springmvc.contactlist.common.MD5Util;
import com.zzt.springmvc.contactlist.entity.User;
import com.zzt.springmvc.contactlist.service.UserService;


@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login")
	public void login(HttpServletRequest request,
			HttpServletResponse response,@RequestParam("name") String name,@RequestParam("pwd") String pwd) throws Exception {
		JSONObject jsonObject = new JSONObject();
		if (null!=name && null!=pwd) {
			User user = userService.findUserByNameAndPassword(name, MD5Util.string2MD5(pwd));
			if (null!=user) {
				request.getSession().setAttribute("loginUser", user);
				jsonObject.put("checkIn", true);
			}else {
				jsonObject.put("checkIn", false);
			}
		}
		response.getWriter().print(jsonObject.toString());
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logOut(HttpServletRequest request,
			HttpServletResponse response) {
		request.getSession().removeAttribute("loginUser");
		return "redirect:/loginPage";
	}
	
	@RequestMapping(value="/loginPage",method=RequestMethod.GET)
	public String index(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return "login";
	}

	public static void main(String[] args) {
		String httpUrl = "http://apis.baidu.com/apistore/weatherservice/citylist";
		String httpArg = "cityname=%E6%9C%9D%E9%98%B3";
		String jsonResult = request(httpUrl, httpArg);
		System.out.println(jsonResult);
	}
	
	/**
	 * @param urlAll
	 *            :请求接口
	 * @param httpArg
	 *            :参数
	 * @return 返回结果
	 */
	public static String request(String httpUrl, String httpArg) {
	    BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "d7484a51fff0f1bbe22596fcdf9cc8f8");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        JSONObject jsonObject = new JSONObject(sbf.toString());
	        result = jsonObject.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}
	
}
