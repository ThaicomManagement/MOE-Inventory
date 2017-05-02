package biz.thaicom.backoffice.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import biz.thaicom.backoffice.dao.BackOfficeDao;

@RestController
@RequestMapping("JSON")
public class JSONController {

public static Logger logger = LoggerFactory.getLogger(JSONController.class);
	
	@Autowired
	private BackOfficeDao backOfficeDao;
	
	@RequestMapping("/Organization")
	public List<Map<String, Object>> findAllOrganization() {
		return backOfficeDao.findAllOrganization();
	}
	
	
	@RequestMapping(value= "/Employee", method=RequestMethod.POST )
	public List<Map<String, Object>> findAllEmployee(
			@RequestParam String name) {
		return backOfficeDao.findAllEmployee(name, false);
	}
	
	
}
