package example0121;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class HelloWithoutReturnModelAndView extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.getWriter().write("Hello3333333333333 World!!");		
		//�����ֱ���ڸô�����/������д��Ӧ ����ͨ������null����DispatcherServlet�Լ��Ѿ�д����Ӧ�ˣ�����Ҫ��������ͼ����
		return null;
	}

}
