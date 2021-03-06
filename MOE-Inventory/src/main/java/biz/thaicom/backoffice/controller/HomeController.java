package biz.thaicom.backoffice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import biz.thaicom.backoffice.dao.BackOfficeDao;
import biz.thaicom.backoffice.reports.ThJasperReportsPdfView;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;





@Controller
public class HomeController {

	public static Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private BackOfficeDao backOfficeDao;
	
	@Autowired 
	private ApplicationContext appContext;
	
	@RequestMapping("/login") 
	public String login(Model model) {
		
		return "auth/login";
	}
	
	@RequestMapping("/")
	public String home(Model model) {
		
		logger.debug("/ is called");
		
		return "home";
	}
	
	@RequestMapping("/page/{pageName}") 
	public String report(@PathVariable String pageName) {
		
		return "pages/"+pageName;
	}
	
	
	
	@RequestMapping("/reports/allInv") 
	public ModelAndView report(
			@RequestParam(required=false) Integer orgId,
			@RequestParam(required=false) Integer fiscalYearBegin,
			@RequestParam(required=false) Integer fiscalYearEnd,
			@RequestParam(required=false) String reportMode,
			@RequestParam(required=false) Integer empId,
			@RequestParam(required=false) String empName,
			HttpServletResponse response) {
		logger.debug("orgId: " + orgId);
		logger.debug("fiscalYearBegin:"+ fiscalYearBegin);
		logger.debug("fiscalYearEnd:"+ fiscalYearEnd);
		logger.debug("reportMode:"+ reportMode);
		logger.debug("empId:"+ empId);
		logger.debug("empName:"+ empName);
		
		List<Map<String, Object>> list = null;
		Map<String, Object> model = new HashMap<>();
		
		list=backOfficeDao.findInv(orgId,fiscalYearBegin, fiscalYearEnd, reportMode, empId);
		
		
	
		
		Map<String, String> obtainMethodMap = backOfficeDao.findObtainMethod();
		
		JRRewindableDataSource invList = new JRBeanCollectionDataSource(list);
		
		model.put("invList",invList);
		model.put("obtainMethodMap", obtainMethodMap);
		model.put("reportMode", reportMode);
		model.put("empId", empId);
		model.put("empName", empName);
		logger.debug(obtainMethodMap.get("1"));
	
		final JasperReportsPdfView view = new ThJasperReportsPdfView();
	    
		
		logger.debug(reportMode);
		if(reportMode.equals("employeeName") || reportMode.equals("receiverName")) {
			view.setUrl("classpath:reports/employeeReport.jrxml");
			Map<String, Object> employee = backOfficeDao.findEmployee(empId);	
			
			logger.debug("reportMode: employeeName");
			
			for (String key : employee.keySet()) {
				logger.debug("key:" + key + " value: " + employee.get(key));
			}
			
			model.put("PFIX_ABBR", employee.get("PFIX_ABBR"));
			model.put("POSITION", employee.get("POSITION"));
			model.put("POSITION_LEVEL", employee.get("POSITION_LEVEL"));
		} else {
			view.setUrl("classpath:reports/allInv.jrxml");
		}
		
		
	    view.setApplicationContext(appContext);
	    view.setReportDataKey("invList");
	    
	    
	    
	    ModelAndView viewReturn = new ModelAndView(view, model);
	    
	    Cookie cookie = new Cookie("fileDownload", "true");
		cookie.setPath("/");
		response.addCookie(cookie);
		return viewReturn;
	}
	
	 
}
