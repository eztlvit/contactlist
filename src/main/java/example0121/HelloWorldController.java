package example0121;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class HelloWorldController implements Controller {
	
	private String pwd;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		// 1���ռ���������֤����
		// 2���󶨲������������
		// 3�������������ҵ��������ҵ����
		// 4��ѡ����һ��ҳ��
		ModelAndView mv = new ModelAndView();
		// ����ģ������ �����������POJO����
		mv.addObject("message", "Hello22 World!!!!!!!!!!!");
		// �����߼���ͼ������ͼ����������ݸ����ֽ������������ͼҳ��
		mv.setViewName("index");
		return mv;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
}