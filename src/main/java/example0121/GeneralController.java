package example0121;

import org.springframework.stereotype.Controller;  
import org.springframework.ui.Model;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.servlet.mvc.AbstractController;;
  
@Controller  
public class GeneralController {  
  
    @RequestMapping(value="index")  
    public void index_jsdddddddddp(Model model){  
        model.addAttribute("str0121", "Hellow world");  
        System.out.println("index.jsp"); 
    }  
    
}  