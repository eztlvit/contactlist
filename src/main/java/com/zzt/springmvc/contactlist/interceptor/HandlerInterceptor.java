package com.zzt.springmvc.contactlist.interceptor;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class HandlerInterceptor extends HandlerInterceptorAdapter {

	private String[] urls = {"/contactsList","/saveOrUpdateContacts","/goAddContacts"};

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("===========HandlerInterceptor preHandle");
		Arrays.sort(urls);
		Object userModel = request.getSession().getAttribute("loginUser");
		String url=request.getRequestURL().toString();
		url = url.substring(url.lastIndexOf("/"), url.length());
		if (null == userModel && Arrays.binarySearch(urls, url) >= 0) {
			response.sendRedirect("/springMVC4/loginPage");
			return false;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("===========HandlerInterceptor postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("===========HandlerInterceptor afterCompletion");
	}

	public String[] getUrls() {
		return urls;
	}

	public void setUrls(String[] urls) {
		this.urls = urls;
	}
	
	public static void main(String[] args) {
		String[] urls = {"/GetAll","/AddContacts","/login"};
		Arrays.sort(urls);
		String url = "/GetAll";
		System.out.println(Arrays.binarySearch(urls, url));
	}
}
