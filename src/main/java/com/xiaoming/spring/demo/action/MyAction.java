package com.xiaoming.spring.demo.action;

import com.xiaoming.spring.framework.annotation.XMAutowired;
import com.xiaoming.spring.framework.annotation.XMController;
import com.xiaoming.spring.framework.annotation.XMRequestMapping;
import com.xiaoming.spring.framework.annotation.XMRequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 公布接口url
 * @author Tom
 *
 */
@XMController
public class MyAction {


	public String query(HttpServletRequest request, HttpServletResponse response,
								String name){
		System.out.println("hello" + name);
		return "";
	}
	
	/*@GPRequestMapping("/add*.json")
	public GPModelAndView add(HttpServletRequest request,HttpServletResponse response,
			   @GPRequestParam("name") String name,@GPRequestParam("addr") String addr){
		String result = modifyService.add(name,addr);
		return out(response,result);
	}
	
	@GPRequestMapping("/remove.json")
	public GPModelAndView remove(HttpServletRequest request,HttpServletResponse response,
		   @GPRequestParam("id") Integer id){
		String result = modifyService.remove(id);
		return out(response,result);
	}
	
	@GPRequestMapping("/edit.json")
	public GPModelAndView edit(HttpServletRequest request,HttpServletResponse response,
			@GPRequestParam("id") Integer id,
			@GPRequestParam("name") String name){
		String result = modifyService.edit(id,name);
		return out(response,result);
	}
	
	
	
	private GPModelAndView out(HttpServletResponse resp,String str){
		try {
			resp.getWriter().write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
}
