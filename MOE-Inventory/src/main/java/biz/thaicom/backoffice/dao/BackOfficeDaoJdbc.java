package biz.thaicom.backoffice.dao;



import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BackOfficeDaoJdbc implements BackOfficeDao {
	

	
	private static final Logger logger = LoggerFactory.getLogger(BackOfficeDaoJdbc.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	@Autowired() 
	@Qualifier("dataSource")
	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(ds);
	}

	private RowMapper<Map<String, Object>> genericRowMapperForJS = new RowMapper<Map<String, Object>>() {
		public Map<String, Object> mapRow(ResultSet rs, int rowNum)
				throws SQLException {
				Map<String, Object> map = new HashMap <String, Object>();
				
				// now get all the column
				ResultSetMetaData rsmd  = rs.getMetaData();
				Integer columnCount = rsmd.getColumnCount();
				for(Integer i=0; i<columnCount;  i++) {
					//logger.debug("geting columnName: " + rsmd.getColumnName(i+1));
					//logger.debug("geting value: " + rs.getObject(i+1));
					
					Object result;
					if(rsmd.getColumnType(i+1) == java.sql.Types.DATE || 
							rsmd.getColumnType(i+1) == java.sql.Types.TIMESTAMP	) {
						Date date = rs.getDate(i+1);
						
						if(date != null) {
							result = sdf.format(date);
						} else {
							result = null;
						}
					} else {
						result = rs.getObject(i+1);
					}
					
					
					map.put(rsmd.getColumnName(i+1),result);
				}
			return map;
		}
	};
	
	private RowMapper<Map<String, Object>> genericRowMapper = new RowMapper<Map<String, Object>>() {
		public Map<String, Object> mapRow(ResultSet rs, int rowNum)
				throws SQLException {
				Map<String, Object> map = new HashMap <String, Object>();
				
				// now get all the column
				ResultSetMetaData rsmd  = rs.getMetaData();
				Integer columnCount = rsmd.getColumnCount();
				for(Integer i=0; i<columnCount;  i++) {
					//logger.debug("geting columnName: " + rsmd.getColumnName(i+1));
					//logger.debug("geting value: " + rs.getObject(i+1));
					
					Object result;
					result = rs.getObject(i+1);
					
					map.put(rsmd.getColumnName(i+1),result);
				}
			return map;
		}
	};
	
	

	@Override
	public List<Map<String, Object>> findAllOrganization() {
		String sql1 = ""
				+ " "
				+ "	Select inv_Code org_code,	"
				+ "	Substr(lpad(Org_Name,(level*5)+length(Org_Name),' '),5,200) As Org_name,"
				+ "       Org_abbr,Org_id"
				+ "	From  Glb_Organization"
				+ "	Where Org_flg = '1'"
				+ "		and inv_code is not null"
				+ "	Start with Org_Id = 0	"
				+ "	Connect by prior Org_id = Org_Org_id ";
		
		List<Map<String, Object>> returnList;
		
		returnList = this.jdbcTemplate.query(
					sql1,
					genericRowMapper
					);
		
		
		return returnList;
	}



	@Override
	public List<Map<String, Object>> findInv(Integer orgId, Integer fiscalYear) {
		String sql1 = ""
				+ " SELECT	a.Id As Inv_info_id,a.Price,a.Prod_Sn,a.Inv_Name,"
				+ "		Datee2std(a.Reg_Date,'DTE','SHORT_MON','FULL_YR','')  Reg_Date,"
				+ "		a.General_Spec,a.Model,a.Bgt_Source,a.Vendor_vendor_id,"
				+ "		a.Doc_No||' '||DateE2T(a.Doc_Date) As Inv_Doc_No,a.Inv_Uom,"
				+ "		a.Remarks,o.Org_Id,o.Org_Name,b.Inv_Asset_id,a.Obtain_Method,"
				+ "		a.Gs_Inv_Subexpt_id As Inv_Parent,a.Fiscal_year ,a.brand_name,"
				+ "		a.inv_org_no||'-'||a.inv_fyr_no||'-'||a.inv_act_no||'-'||a.inv_typ_no||'-'||a.inv_ord_no Inv_No,a.inv_use"
				+ " From	Pro_Inv_info a, Glb_Organization o,"
				+ "		Glb_Inv_Asset b, Glb_Inv_Subexpt e ";
		String where = ""
				+ " Where a.Gs_Inv_Subexpt_id = e.Inv_Subexpt_Id "
				+ "		and e.Inv_Asset_id = b.Inv_Asset_id"
				+ "		and a.Org_Org_id = o.Org_id"
				+ "		and  a.id not in (select inv_info_id from pro_distribution )"
				+ "		and  not exists (select * from pro_inv_ret ret where a.id = ret.inv_info_id ) "
				+ "		and  ((o.inv_code = '200' and (a.id in (select inv_info_id from pro_inv_ret ))) or"
				+ "			(o.inv_code != '200' and (a.id not in (select inv_info_id from pro_inv_ret )) )) ";
		String order = "" 
				+ " order by a.reg_date asc, a.inv_org_no||a.inv_fyr_no||a.inv_act_no||a.inv_typ_no||a.inv_ord_no asc";
				
		
		if(orgId != null) {
			where += " AND o.Org_id = " + orgId;
		}

		if(fiscalYear != null) {
			where += " AND o.fiscal_year = " + orgId;
		}

		
		List<Map<String, Object>> returnList;
		
		returnList = this.jdbcTemplate.query(
					sql1+ where + order,
					genericRowMapper
					);
		
		
		return returnList;
	}





}
