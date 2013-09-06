package gestione.pack.server;

import gestione.pack.client.model.DatiFatturazioneMeseJavaBean;
import gestione.pack.client.model.RiepilogoAnnualeJavaBean;
import gestione.pack.shared.Personale;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
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
		String operazione= (String) httpSession.getAttribute("operazione");
				
		
		if (operazione.compareTo("ALL") == 0) {

			stampato = ServerUtility.PrintRiepilogoOreMese(dataRif, sedeOperativa);

			if (stampato) {
				//List<DatiOreMese> lista = new ArrayList<DatiOreMese>();

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

					fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoOre.jasper");
										
					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters);

					JasperExportManager.exportReportToPdfFile(jasperPrint,Constanti.PATHAmazon+"FileStorage/RiepilogoTotali.pdf");

				} catch (JRException e) {

					e.printStackTrace();
				}
				tx.rollback();
				session.close();
				httpSession.setAttribute("result", stampato);
			}
			else
				httpSession.setAttribute("result", stampato);
		
		}else
		
			if(operazione.compareTo("COMM")==0){
			
			String nome=username.substring(0, username.indexOf("."));
			nome=nome.substring(0, 1).toUpperCase()+nome.substring(1,nome.length());
			
			String cognome=username.substring(username.indexOf(".")+1, username.length());
			cognome=cognome.substring(0, 1).toUpperCase()+cognome.substring(1,cognome.length());
			
			String nomeFile=cognome+nome+"_Report.pdf";
			
			String totOreCommesse=(String) httpSession.getAttribute("totOreCommesse");
			String totOreIU=(String) httpSession.getAttribute("totOreIU");
			
			stampato = ServerUtility.getRiepilogoGiornalieroCommesse(username, dataRif);

			if (stampato) {
				//List<DatiOreMese> lista = new ArrayList<DatiOreMese>();

				Session session = MyHibernateUtil.getSessionFactory().openSession();
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
				parameters.put("TotOreCommesse", totOreCommesse);
				parameters.put("TotOreIU", totOreIU);
				
				JasperPrint jasperPrint;
				FileInputStream fis;
				BufferedInputStream bufferedInputStream;

				try {

					fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoCommesse.jasper");

					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters);
					JasperExportManager.exportReportToPdfFile(jasperPrint,Constanti.PATHAmazon+"FileStorage/RiepiloghiCommesse/"+nomeFile);
					
				} catch (JRException e) {
					e.printStackTrace();
				}

				tx.rollback();
				session.close();
				httpSession.setAttribute("result", stampato);//ritorno all'applicazione se è andata a buon fine o meno
			}
			else
				httpSession.setAttribute("result", stampato);	//TODO effettuare il controllo sul return	
		}
		
		else 
			if(operazione.compareTo("RIEP.ANNUALE")==0)
			{
				String anno=(String) httpSession.getAttribute("anno");
				String sede=(String) httpSession.getAttribute("sede");
				
				List<RiepilogoAnnualeJavaBean> listaR= new ArrayList<RiepilogoAnnualeJavaBean> ();
				listaR = ServerUtility.PrintRiepilogoOreAnnuale(anno, sede);
				
				Map parameters = new HashMap();
				/*parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION,
									session);// Parametri che usa il file jasper per
											 // connettersi!*/
					
				JasperPrint jasperPrint;
				FileInputStream fis;
				BufferedInputStream bufferedInputStream;

				try {

					fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoAnnuale.jasper");
											
					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
								.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
								parameters, getDataSource(listaR));
					
					JRXlsExporter exporterXLS = new JRXlsExporter();
					exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/RiepilogoAnnuale.xls");
					exporterXLS.exportReport();
						
					File f=new File(Constanti.PATHAmazon+"FileStorage/RiepilogoAnnuale.xls");
					FileInputStream fin = new FileInputStream(f);
					ServletOutputStream outStream = response.getOutputStream();
					// SET THE MIME TYPE.
					response.setContentType("application/vnd.ms-excel");
					// set content dispostion to attachment in with file name.
					// case the open/save dialog needs to appear.
					response.setHeader("Content-Disposition", "attachment;filename=RiepilogoAnnuale.xls");

					byte[] buffer = new byte[1024];
					int n = 0;
					while ((n = fin.read(buffer)) != -1) {
					outStream.write(buffer, 0, n);
					System.out.println(buffer);
					}
					
					outStream.flush();
					fin.close();
					outStream.close();
					
				} catch (JRException e) {
						e.printStackTrace();
				}
			
			}				
			else
				if(operazione.compareTo("RIEP.FATT")==0){
					
					String anno=(String) httpSession.getAttribute("anno");
					String mese=(String) httpSession.getAttribute("mese");
					
					List<DatiFatturazioneMeseJavaBean> listaR= new ArrayList<DatiFatturazioneMeseJavaBean> ();
					listaR = ServerUtility.PrintRiepilogoDatiFatturazione(anno, mese);
					
					Map parameters = new HashMap();
					/*parameters.put(JRHibernateQueryExecuterFactory.PARAMETER_HIBERNATE_SESSION,
										session);// Parametri che usa il file jasper per
												 // connettersi!*/
						
					JasperPrint jasperPrint;
					FileInputStream fis;
					BufferedInputStream bufferedInputStream;

					try {

						fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/RiepilogoDatiFatturazione.jasper");
												
						bufferedInputStream = new BufferedInputStream(fis);

						JasperReport jasperReport = (JasperReport) JRLoader
									.loadObject(bufferedInputStream);

						jasperPrint = JasperFillManager.fillReport(jasperReport,
									parameters, getDataSourceFattMese(listaR));
						
						JRXlsExporter exporterXLS = new JRXlsExporter();
						exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/RiepilogoDatiFatturazione.xls");
						exporterXLS.exportReport();
							
						File f=new File(Constanti.PATHAmazon+"FileStorage/RiepilogoDatiFatturazione.xls");
						FileInputStream fin = new FileInputStream(f);
						ServletOutputStream outStream = response.getOutputStream();
						// SET THE MIME TYPE.
						response.setContentType("application/vnd.ms-excel");
						// set content dispostion to attachment in with file name.
						// case the open/save dialog needs to appear.
						response.setHeader("Content-Disposition", "attachment;filename=RiepilogoDatiFatturazione.xls");

						byte[] buffer = new byte[1024];
						int n = 0;
						while ((n = fin.read(buffer)) != -1) {
						outStream.write(buffer, 0, n);
						System.out.println(buffer);
						}
						
						outStream.flush();
						fin.close();
						outStream.close();
						
					} catch (JRException e) {
							e.printStackTrace();
					}					
				}
		
			else
			{
			//operazione ONE
			stampato = ServerUtility.PrintRiepilogoOreMese(dataRif, sedeOperativa, username);

			if (stampato) {
				//List<DatiOreMese> lista = new ArrayList<DatiOreMese>();

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

					fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoOre.jasper");
										
					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters);
					
					JasperExportManager.exportReportToPdfFile(jasperPrint,Constanti.PATHAmazon+"FileStorage/RiepilogoOre_"+username+".pdf");

				} catch (JRException e) {

					e.printStackTrace();
				}
				tx.rollback();
				session.close();
				httpSession.setAttribute("result", stampato);
			}
			else
				httpSession.setAttribute("result", stampato);				
		}
	}

	private static JRDataSource getDataSourceFattMese(List<DatiFatturazioneMeseJavaBean> listaR) {
		Collection<DatiFatturazioneMeseJavaBean> riep= new ArrayList<DatiFatturazioneMeseJavaBean>();		
		for(DatiFatturazioneMeseJavaBean r: listaR){
			riep.add(r);
		}	
		return new JRBeanCollectionDataSource(riep);
	}

	private static JRDataSource getDataSource(List<RiepilogoAnnualeJavaBean> listaR) {	
		Collection<RiepilogoAnnualeJavaBean> riep= new ArrayList<RiepilogoAnnualeJavaBean>();	
		for(RiepilogoAnnualeJavaBean r: listaR){
			riep.add(r);
		}	
		return new JRBeanCollectionDataSource(riep);
	}
	
}