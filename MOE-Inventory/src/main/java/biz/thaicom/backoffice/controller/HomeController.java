package biz.thaicom.backoffice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class HomeController {

	public static Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/login") 
	public String login(Model model) {
		
		return "auth/login";
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		
		logger.debug("/ is called");
		
		return "home";
	}
	
	@RequestMapping("/reports/{reportName}") 
	public String report(@PathVariable String reportName) {
		
		return "reports/"+reportName;
	}
}
