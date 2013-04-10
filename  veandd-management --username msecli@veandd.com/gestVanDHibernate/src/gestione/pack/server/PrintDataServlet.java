package gestione.pack.server;

import gestione.pack.shared.DatiOreMese;
import gestione.pack.shared.Personale;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;


public class PrintDataServlet extends HttpServlet  {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean stampato;
		 
		HttpSession httpSession = request.getSession();
		 
		String mex=(String) httpSession.getAttribute("mese");
		
		Date data= new Date();
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd",Locale.ITALIAN);
		try {
			data = (Date)formatter.parse("2013-Mar-22");
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		if(stampato=ServerUtility.PrintRiepilogoOreMese(data));
		
		
		List<DatiOreMese> lista= new ArrayList<DatiOreMese>();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		tx = session.beginTransaction();

		Map parameters = new HashMap();
		parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION, session);//Parametri che usa il file jasper per connettersi!
		
		
		JasperPrint jasperPrint;
		FileInputStream fis;
        BufferedInputStream bufferedInputStream;
		
		try {
			
			 fis = new FileInputStream("Jasper Report/Blank_A4.jasper");
	         
			 bufferedInputStream = new BufferedInputStream(fis);
	            
			 JasperReport jasperReport = (JasperReport) JRLoader.loadObject(bufferedInputStream);
			  
		     jasperPrint = JasperFillManager.fillReport(jasperReport,  parameters);
			
			 
			JasperExportManager.exportReportToPdfFile(jasperPrint, "C:/sample.pdf");
			 
		} catch (JRException e) {
			
			e.printStackTrace();
		}
		
		
		tx.rollback();
		session.close();
				   		
	}
	
}