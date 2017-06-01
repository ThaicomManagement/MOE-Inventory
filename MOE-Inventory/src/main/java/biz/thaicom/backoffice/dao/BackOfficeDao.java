package biz.thaicom.backoffice.dao;

import java.util.List;
import java.util.Map;

public interface BackOfficeDao {

	List<Map<String, Object>> findInv(Integer orgId, Integer fiscalYear, Integer fiscalYearEnd, String reportMode, Integer empId);

	List<Map<String, Object>> findAllOrganization();

	Map<String, String> findObtainMethod();
	
	Map<String, Object> findEmployee(Integer empId);
	
	List<Map<String, Object>> findAllEmployee(String name, Boolean isReceiver);

}
