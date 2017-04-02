package biz.thaicom.backoffice.reports;

import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperPrint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.googlecode.jthaipdf.jasperreports.engine.export.ThaiJRPdfExporter;


public class ThJasperReportsPdfView extends JasperReportsPdfView {

	public static Logger logger = LoggerFactory.getLogger(ThJasperReportsPdfView.class);
	
	
	public ThJasperReportsPdfView() {
		setContentType("application/pdf");
		Properties prop = new Properties();
		prop.setProperty("Content-Disposition", "attachment; filename=\"report.pdf\"");
		
		setHeaders(prop);
	}
	
	@Override
	protected JRExporter createExporter() {
		// TODO Auto-generated method stub
		return new ThaiJRPdfExporter();
	}

}
