package gestione.pack.server;

import gestione.pack.shared.DatiOreMese;
import gestione.pack.shared.Personale;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.query.JRHibernateQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;


public class PrintDataServlet extends HttpServlet  {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean stampato;
		 
		Personale p = new Personale();
		HttpSession httpSession = request.getSession();

		String dataRif = (String) httpSession.getAttribute("mese");
		String username = (String) httpSession.getAttribute("username");
		String sedeOperativa=(String) httpSession.getAttribute("sede");
		
		if (username.compareTo("") == 0) {// se non ha valore allora stampo il
											// report del riepilogo giornaliero
											// altrimenti quello delle commesse

			stampato = ServerUtility.PrintRiepilogoOreMese(dataRif, sedeOperativa);

			if (stampato) {
				List<DatiOreMese> lista = new ArrayList<DatiOreMese>();

				Session session = MyHibernateUtil.getSessionFactory()
						.openSession();
				Transaction tx = null;
				tx = session.beginTransaction();

				Map parameters = new HashMap();
				parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION,
								session);// Parametri che usa il file jasper per
										 // connettersi!
				
				JasperPrint jasperPrint;
				FileInputStream fis;
				BufferedInputStream bufferedInputStream;

				try {

					fis = new FileInputStream("/var/lib/tomcat7/webapps/ROOT/JasperReport/ReportRiepilogoOre.jasper");
					//fis = new FileInputStream("JasperReport/ReportRiepilogoOre.jasper");
					
					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters);

					JasperExportManager.exportReportToPdfFile(jasperPrint,
							"FileStorage/RiepilogoTotali.pdf");

				} catch (JRException e) {

					e.printStackTrace();
				}
				tx.rollback();
				session.close();
				httpSession.setAttribute("result", stampato);
			}

			else
				httpSession.setAttribute("result", stampato);
		
		}else{
			
			String nome=username.substring(0, username.indexOf("."));
			nome=nome.substring(0, 1).toUpperCase()+nome.substring(1,nome.length());
			
			String cognome=username.substring(username.indexOf(".")+1, username.length());
			cognome=cognome.substring(0, 1).toUpperCase()+cognome.substring(1,cognome.length());
			
			String nomeFile=cognome+nome+"_Report.pdf";
			
			stampato = ServerUtility.getRiepilogoGiornalieroCommesse(username, dataRif);

			if (stampato) {
				List<DatiOreMese> lista = new ArrayList<DatiOreMese>();

				Session session = MyHibernateUtil.getSessionFactory()
						.openSession();
				Transaction tx = null;
				tx = session.beginTransaction();

				Map parameters = new HashMap();
				parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION,
								session);// Parametri che usa il file jasper per
											// connettersi!
				parameters.put("Nominativo", username);
				parameters.put("Data", dataRif);
				parameters.put("Nome", nome);
				parameters.put("Cognome", cognome);
				
				JasperPrint jasperPrint;
				FileInputStream fis;
				BufferedInputStream bufferedInputStream;

				try {

				//	fis = new FileInputStream("JasperReport/ReportRiepilogoCommesse.jasper");
					
					fis = new FileInputStream("/var/lib/tomcat7/webapps/ROOT/JasperReport/ReportRiepilogoCommesse.jasper");

					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters);

					JasperExportManager.exportReportToPdfFile(jasperPrint,"/var/lib/tomcat7/webapps/ROOT/FileStorage/RiepiloghiCommesse/"+nomeFile);
					
					//JasperExportManager.exportReportToPdfFile(jasperPrint,"FileStorage/RiepiloghiCommesse/"+nomeFile);

				} catch (JRException e) {

					e.printStackTrace();
				}

				tx.rollback();
				session.close();
				httpSession.setAttribute("result", stampato);//ritorno all'applicazione se � andata a buon fine o meno
			}

			else
				httpSession.setAttribute("result", stampato);	//TODO effettuare il controllo sul return	
		}
	}
}