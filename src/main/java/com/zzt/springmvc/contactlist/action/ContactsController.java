package com.zzt.springmvc.contactlist.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.zzt.springmvc.contactlist.common.MD5Util;
import com.zzt.springmvc.contactlist.entity.User;
import com.zzt.springmvc.contactlist.entity.UserMyBatis;
import com.zzt.springmvc.contactlist.redis.RedisClientTemplate;
import com.zzt.springmvc.contactlist.service.UserService;

@Controller
public class ContactsController {
	
	@Autowired
	private UserService userService;
	
	private List<User> userlst;

	private List<UserMyBatis> userMyBatisLst;
	
	@Resource(name="redisClientTemplate")
	private RedisClientTemplate redisClientTemplate;
	
	@RequestMapping(value="/saveOrUpdateContacts")
	public String saveOrUpdateContacts(HttpServletRequest request,
			HttpServletResponse response,Model model) throws Exception {
		String type = request.getParameter("type");
		Integer id = request.getParameter("id") == null || request.getParameter("id").equalsIgnoreCase("") ? null:Integer.valueOf(request.getParameter("id"));
//		File targetFile = new File("E:\\uploadFile", file.getOriginalFilename());
//		file.transferTo(targetFile);
		
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String fileUrl = request.getParameter("fileUrl");

		UserMyBatis user = null;
		if (null==id) {
			user = new UserMyBatis();
			user.setCreateTime(new Date());
		}else {
//			user = userService.findUserById(id);
			user.setUpdateTime(new Date());
		}
		
		user.setUserName(name);
		user.setPassword(MD5Util.string2MD5(pwd));
		user.setPhoneNumber(phone);
		user.setAddress(address);
		user.setPicUrl(fileUrl);
		user.setCreateTime(user.getCreateTime());
		userService.saveUserByMyBatis(user);
		return "redirect:/contactsList";
	}
	
	@RequestMapping(value="/deleteContacts")
	public String deleteContacts(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] deleteIDs = request.getParameterValues("item");
		request.getParameterNames();
		request.getParameter("checkBoxItem");
		if (null!=deleteIDs) {
			for (String id : deleteIDs) {
				User user = userService.findUserById(Integer.valueOf(id));
				userService.deleteUser(user);
			}
		}
		
		return "redirect:/contactsList";
	}
	
	@RequestMapping(value="/contactsList",method=RequestMethod.GET)
	public ModelAndView contactsList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("contactsList");
		System.out.println("----------------------------------");

//		userlst = new ArrayList<User>();
//		userlst = userService.findAllList();
		
		userMyBatisLst = new ArrayList<UserMyBatis>();
		userMyBatisLst = userService.getList();
		mv.addObject("userlst", userMyBatisLst);
		return mv;
	}
	
	@RequestMapping(value="/checkUserName")
	public void checkUserName(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("name") String name) throws IOException {
		String userName = name.trim();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("isHave", false);
			if (userName.length()==0) 
				response.getWriter().print(jsonObject.toString());
			List<User> userLst = userService.checkUserName(name);
			if (userLst.size()>0) {
				jsonObject.put("isHave", true);
			}else {
				jsonObject.put("isHave", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().print(jsonObject.toString());
	}
	
	@RequestMapping(value="/editContactsDetail/{id}")
	public String editContactsDetail(HttpServletRequest request,
			HttpServletResponse response, Model model,@PathVariable int id) {
		User user = userService.findUserById(id);
		model.addAttribute("user",user);
		return "editContactsDetail";
	}
	
	@RequestMapping(value="/goAddContacts")
	public String goAddContact(Model model){
		User user = new User();
		model.addAttribute("user", user);
		return "editContactsDetail";
	}
	
    @RequestMapping(value = "/upload")
    @ResponseBody  
    public void upload(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws IOException, JSONException {  
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("msg", "success");
        System.out.println("开始"); 
        String type = request.getParameter("type"); 
        String path=request.getSession().getServletContext().getRealPath("resource/images/");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = null;
        if("1".equals(type)){  
            file = multipartRequest.getFile("imgFile");// 获取上传文件名  
        }else{  
            file = multipartRequest.getFile("attachFile");// 获取上传文件名  
        }
//        String fileName = file.getOriginalFilename();  
        String fileName = new Date().getTime()+".jpg";  
        System.out.println(path);  
        File targetFile = new File(path, fileName);  
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        jsonObject.put("fileUrl", "images/"+fileName);
        //保存  
        try {  
            file.transferTo(targetFile);  
        } catch (Exception e) {  
            e.printStackTrace();
            jsonObject.put("msg", "error");
        }  
        model.addAttribute("fileUrl", "images/"+fileName);  
        response.getWriter().print(jsonObject.toString());
    }
    
    /**
     * 通过url请求返回图像的字节流
     */
    @RequestMapping("icon/{cateogry}")
    public void getIcon(@PathVariable("cateogry") String cateogry,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

        if(StringUtils.isEmpty(cateogry)) {
            cateogry = "";
        }

        String fileName = request.getSession().getServletContext().getRealPath("/")
                + "resource\\icons\\auth\\"
                + cateogry.toUpperCase().trim() + ".png";

        File file = new File(fileName);

        //判断文件是否存在如果不存在就返回默认图标
        if(!(file.exists() && file.canRead())) {
            file = new File(request.getSession().getServletContext().getRealPath("/")
                    + "resource/icons/auth/root.png");
        }

        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        int length = inputStream.read(data);
        inputStream.close();

        response.setContentType("image/png");

        OutputStream stream = response.getOutputStream();
        stream.write(data);
        stream.flush();
        stream.close();
    }
    
    @RequestMapping(value = "/image/get")
    @ResponseBody
    public void getImage(HttpServletRequest request,HttpServletResponse response,@RequestParam("imgFile") MultipartFile imgFile) {
        FileInputStream fis = null;
        response.setContentType("image/jgp");
        try {
            OutputStream out = response.getOutputStream();
            File file = new File("E:/uploadFile/1.jpg");
            fis = new FileInputStream(file);
            byte[] b = imgFile.getBytes();
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (Exception e) {
             e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                   fis.close();
                } catch (IOException e) {
                e.printStackTrace();
            }   
              }
        }
    }
}
