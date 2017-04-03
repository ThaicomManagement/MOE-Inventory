package biz.thaicom.backoffice.dao;

import java.util.List;
import java.util.Map;

public interface BackOfficeDao {

	List<Map<String, Object>> findInv(Integer orgId, Integer fiscalYear);

	List<Map<String, Object>> findAllOrganization();

	Map<String, String> findObtainMethod();

}
