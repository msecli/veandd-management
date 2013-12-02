package gestione.pack.server;

import gestione.pack.client.model.AttivitaFatturateJavaBean;
import gestione.pack.client.model.AttivitaFatturateModel;
import gestione.pack.client.model.DatiFatturazioneMeseJavaBean;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.FatturaJavaBean;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.model.RiepilogoAnnualeJavaBean;
import gestione.pack.client.model.RiepilogoCostiDipendentiBean;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.model.RiepilogoDatiOreMeseJavaBean;
import gestione.pack.client.model.RiepilogoMeseGiornalieroJavaBean;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliJavaBean;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoSALPCLJavaBean;
import gestione.pack.client.model.RiepilogoSALPCLModel;
import gestione.pack.shared.AttivitaFatturata;
import gestione.pack.shared.Fattura;
import gestione.pack.shared.FoglioFatturazione;
import gestione.pack.shared.Ordine;
import gestione.pack.shared.Personale;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
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
			
			
			List<RiepilogoDatiOreMeseJavaBean> listaJB= new ArrayList<RiepilogoDatiOreMeseJavaBean>();
			listaJB= ServerUtility.PrintRiepilogoOreMese(dataRif, sedeOperativa);
			
				Map parameters = new HashMap();
				
				JasperPrint jasperPrint;
				FileInputStream fis;
				BufferedInputStream bufferedInputStream;

				try {

					fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoOre.jasper");
										
					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, getDataSourceRiepMensile(listaJB));

					JasperExportManager.exportReportToPdfFile(jasperPrint,Constanti.PATHAmazon+"FileStorage/RiepilogoTotali.pdf");

				} catch (JRException e) {

					e.printStackTrace();
				}
			
		}else
		
		if(operazione.compareTo("COMM")==0){
			
			List<RiepilogoMeseGiornalieroJavaBean> listaJB=new ArrayList<RiepilogoMeseGiornalieroJavaBean>();
			List<RiepilogoMeseGiornalieroModel> listaM=new ArrayList<RiepilogoMeseGiornalieroModel>();
			
			String nome=username.substring(0, username.indexOf("."));
			nome=nome.substring(0, 1).toUpperCase()+nome.substring(1,nome.length());
			
			String cognome=username.substring(username.indexOf(".")+1, username.length());
			cognome=cognome.substring(0, 1).toUpperCase()+cognome.substring(1,cognome.length());
			
			String nomeFile=cognome+nome+"_Report.pdf";
			
			String totOreCommesse=(String) httpSession.getAttribute("totOreCommesse");
			String totOreIU=(String) httpSession.getAttribute("totOreIU");
			
			listaM= (List<RiepilogoMeseGiornalieroModel>) httpSession.getAttribute("listaM");
			listaJB.addAll(ServerUtility.traduciDatiRiepilogoCommesseGiornalieri(listaM));
			
			try {
				
				Map parameters = new HashMap();
				
				parameters.put("Nominativo", username);
				parameters.put("Data", dataRif);
				parameters.put("Nome", nome);
				parameters.put("Cognome", cognome);
				parameters.put("TotOreCommesse", totOreCommesse);
				parameters.put("TotOreIU", totOreIU);
				
				JasperPrint jasperPrint;
				FileInputStream fis;
				BufferedInputStream bufferedInputStream;

				fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoCommesseJB.jasper");
				bufferedInputStream = new BufferedInputStream(fis);

				JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

				jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,	
						getDataSourceRiepilogoCommesse(listaJB));
				
				JasperExportManager.exportReportToPdfFile(jasperPrint, Constanti.PATHAmazon+"FileStorage/RiepiloghiCommesse/"+nomeFile);
				
			/*	JRXlsExporter exporterXLS = new JRXlsExporter();
				exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/RiepiloghiCommesse/"+nomeFile);
				exporterXLS.exportReport();
				*/
				
				File f=new File(Constanti.PATHAmazon+"FileStorage/RiepiloghiCommesse/"+nomeFile);
				FileInputStream fin = new FileInputStream(f);
				ServletOutputStream outStream = response.getOutputStream();
				// SET THE MIME TYPE.
				response.setContentType("application/pdf ");
				//response.setContentType("application/vnd.ms-excel");
				// set content dispostion to attachment in with file name.
				// case the open/save dialog needs to appear.
				response.setHeader("Content-Disposition", "attachment;filename="+nomeFile);

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
					if(operazione.compareTo("RIEPDATIFATT")==0){
						List<DatiFatturazioneMeseModel> lista= new ArrayList<DatiFatturazioneMeseModel>();
						List<DatiFatturazioneMeseJavaBean> listaR= new ArrayList<DatiFatturazioneMeseJavaBean>();
						
						lista= (List<DatiFatturazioneMeseModel>) httpSession.getAttribute("lista");
						
						listaR.addAll(ServerUtility.traduciDatiFatturazioneModelToBean(lista));
						
						Map parameters = new HashMap();
												
						JasperPrint jasperPrint;
						FileInputStream fis;
						BufferedInputStream bufferedInputStream;

						try {

							fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/RiepilogoDatiFatturazioneRidotto.jasper");
													
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
							exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/DatiFatturazione.xls");
							exporterXLS.exportReport();
								
							File f=new File(Constanti.PATHAmazon+"FileStorage/DatiFatturazione.xls");
							FileInputStream fin = new FileInputStream(f);
							ServletOutputStream outStream = response.getOutputStream();
							// SET THE MIME TYPE.
							response.setContentType("application/vnd.ms-excel");
							// set content dispostion to attachment in with file name.
							// case the open/save dialog needs to appear.
							response.setHeader("Content-Disposition", "attachment;filename=DatiFatturazione.xls");

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
				if(operazione.compareTo("RIEP.NON.FATT")==0){
					List<RiepilogoOreNonFatturabiliModel> lista= new ArrayList<RiepilogoOreNonFatturabiliModel>();
					List<RiepilogoOreNonFatturabiliJavaBean> listaR= new ArrayList<RiepilogoOreNonFatturabiliJavaBean>();
					
					lista= (List<RiepilogoOreNonFatturabiliModel>) httpSession.getAttribute("lista");
					
					listaR.addAll(ServerUtility.traduciNonFatturabiliModelToBean(lista));
					
					Map parameters = new HashMap();
											
					JasperPrint jasperPrint;
					FileInputStream fis;
					BufferedInputStream bufferedInputStream;

					try {

						fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoOreNonFatturabili.jasper");
												
						bufferedInputStream = new BufferedInputStream(fis);

						JasperReport jasperReport = (JasperReport) JRLoader
									.loadObject(bufferedInputStream);

						jasperPrint = JasperFillManager.fillReport(jasperReport,
									parameters, getDataSourceNonFatt(listaR));
						
						JRXlsExporter exporterXLS = new JRXlsExporter();
						exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/ReportRiepilogoOreNonFatturabili.xls");
						exporterXLS.exportReport();
							
						File f=new File(Constanti.PATHAmazon+"FileStorage/ReportRiepilogoOreNonFatturabili.xls");
						FileInputStream fin = new FileInputStream(f);
						ServletOutputStream outStream = response.getOutputStream();
						// SET THE MIME TYPE.
						response.setContentType("application/vnd.ms-excel");
						// set content dispostion to attachment in with file name.
						// case the open/save dialog needs to appear.
						response.setHeader("Content-Disposition", "attachment;filename=ReportRiepilogoOreNonFatturabili.xls");

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
				if(operazione.compareTo("RIEP.COSTI")==0){
					List<RiepilogoCostiDipendentiModel> lista= new ArrayList<RiepilogoCostiDipendentiModel>();
					List<RiepilogoCostiDipendentiBean> listaB= new ArrayList<RiepilogoCostiDipendentiBean>();
					
					lista= (List<RiepilogoCostiDipendentiModel>) httpSession.getAttribute("lista");
					
					listaB.addAll(ServerUtility.traduciCostiModelToBean(lista));
					
					Map parameters = new HashMap();
											
					JasperPrint jasperPrint;
					FileInputStream fis;
					BufferedInputStream bufferedInputStream;

					try {
											
						fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoCostiDipendenti.jasper");
												
						bufferedInputStream = new BufferedInputStream(fis);

						JasperReport jasperReport = (JasperReport) JRLoader
									.loadObject(bufferedInputStream);

						jasperPrint = JasperFillManager.fillReport(jasperReport,
									parameters, getDataSourceCosti(listaB));
						
						JRXlsExporter exporterXLS = new JRXlsExporter();
						exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.FALSE);
						exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/ReportRiepilogoCostiDipendenti.xls");
						exporterXLS.exportReport();
							
						File f=new File(Constanti.PATHAmazon+"FileStorage/ReportRiepilogoCostiDipendenti.xls");
						FileInputStream fin = new FileInputStream(f);
						ServletOutputStream outStream = response.getOutputStream();
						// SET THE MIME TYPE.
						response.setContentType("application/vnd.ms-excel");
						// set content dispostion to attachment in with file name.
						// case the open/save dialog needs to appear.
						response.setHeader("Content-Disposition", "attachment;filename=ReportRiepilogoCostiDipendenti.xls");

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
				if(operazione.compareTo("RIEP.SALPCL")==0){
					List<RiepilogoSALPCLModel> lista= new ArrayList<RiepilogoSALPCLModel>();
					List<RiepilogoSALPCLJavaBean> listaR= new ArrayList<RiepilogoSALPCLJavaBean>();
					
					lista= (List<RiepilogoSALPCLModel>) httpSession.getAttribute("lista");
					
					listaR.addAll(ServerUtility.traduciSALPCLModelToBean(lista));
					
					Map parameters = new HashMap();
											
					JasperPrint jasperPrint;
					FileInputStream fis;
					BufferedInputStream bufferedInputStream;

					try {

						fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoSalPcl.jasper");
												
						bufferedInputStream = new BufferedInputStream(fis);

						JasperReport jasperReport = (JasperReport) JRLoader
									.loadObject(bufferedInputStream);

						jasperPrint = JasperFillManager.fillReport(jasperReport,
									parameters, getDataSourceSalPcl(listaR));
						
						JRXlsExporter exporterXLS = new JRXlsExporter();
						exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_CELL_BACKGROUND, Boolean.FALSE);
						exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.FALSE);
						exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/ReportRiepilogoSalPcl.xls");
						exporterXLS.exportReport();
							
						File f=new File(Constanti.PATHAmazon+"FileStorage/ReportRiepilogoSalPcl.xls");
						FileInputStream fin = new FileInputStream(f);
						ServletOutputStream outStream = response.getOutputStream();
						// SET THE MIME TYPE.
						response.setContentType("application/vnd.ms-excel");
						// set content dispostion to attachment in with file name.
						// case the open/save dialog needs to appear.
						response.setHeader("Content-Disposition", "attachment;filename=ReportRiepilogoSalPcl.xls");

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
		if(operazione.compareTo("RIEP.SALPCLRIASS")==0){
			List<RiepilogoSALPCLModel> lista= new ArrayList<RiepilogoSALPCLModel>();
			List<RiepilogoSALPCLJavaBean> listaR= new ArrayList<RiepilogoSALPCLJavaBean>();
			
			lista= (List<RiepilogoSALPCLModel>) httpSession.getAttribute("lista");
			
			listaR.addAll(ServerUtility.traduciSALPCLModelToBean(lista));
			
			Map parameters = new HashMap();
									
			JasperPrint jasperPrint;
			FileInputStream fis;
			BufferedInputStream bufferedInputStream;

			try {

				fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoSalPclRiassunto.jasper");
										
				bufferedInputStream = new BufferedInputStream(fis);

				JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

				jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, getDataSourceSalPcl(listaR));
				
				JRXlsExporter exporterXLS = new JRXlsExporter();
				exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, Constanti.PATHAmazon+"FileStorage/ReportRiepilogoSalPclRiassunto.xls");
				exporterXLS.exportReport();
					
				File f=new File(Constanti.PATHAmazon+"FileStorage/ReportRiepilogoSalPclRiassunto.xls");
				FileInputStream fin = new FileInputStream(f);
				ServletOutputStream outStream = response.getOutputStream();
				// SET THE MIME TYPE.
				response.setContentType("application/vnd.ms-excel");
				// set content dispostion to attachment in with file name.
				// case the open/save dialog needs to appear.
				response.setHeader("Content-Disposition", "attachment;filename=ReportRiepilogoSalPclRiassunto.xls");

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
			if(operazione.compareTo("STAMPAFATTURA")==0){
				
				Map parameters = new HashMap();	
				JasperPrint jasperPrint;
				FileInputStream fis;
				BufferedInputStream bufferedInputStream;
				byte[] rtfResume = null;
				
				String imponibile="0.00";
				String importoIva="0.00";
				String totaleIva="0.00";
				String totaleImporto="0.00";
				String dataFattura="";
				
				DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
			    formatSymbols.setDecimalSeparator('.');
			    String pattern="0.00";
			    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
						
			    SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy",Locale.ITALIAN);				
				
				try{
								
					List<FatturaJavaBean> listaO= new ArrayList<FatturaJavaBean>();						
					FatturaModel fm= (FatturaModel) httpSession.getAttribute("fatturaModel");
					List<AttivitaFatturateJavaBean> listaAF= elaboraListaAttivita((List<AttivitaFatturateModel>) httpSession.getAttribute("listaA"));
				
					//salvare i dati della fattura con le attivita fatturate
					boolean saveOk=ServerUtility.saveDataFattura(fm, listaAF);					
													
					for(AttivitaFatturateJavaBean af:listaAF){
						imponibile=ServerUtility.aggiornaTotGenerale(imponibile, af.getImporto());
						importoIva=d.format(Float.valueOf((String)fm.get("iva"))*Float.valueOf(af.getImporto())/100);
						totaleIva=ServerUtility.aggiornaTotGenerale(totaleIva, importoIva);
						totaleImporto=ServerUtility.aggiornaTotGenerale(totaleImporto, ServerUtility.aggiornaTotGenerale(af.getImporto(), importoIva));	
					}
									
					listaAF.add(0, new AttivitaFatturateJavaBean("", "0.00"));//aggiungo una riga all'inizio della lista per un problema di formattazione del report
					listaAF.add(new AttivitaFatturateJavaBean("COMPLESSIVI", imponibile));
					dataFattura=formatter.format((Date)fm.get("dataFattura"));
					
					listaO.add(new FatturaJavaBean((String)fm.get("ragioneSociale"),(String) fm.get("indirizzo"), (String)fm.get("cap"), (String)fm.get("citta"), (String)fm.get("piva"),
							(String) fm.get("codiceFornitore"), (String)fm.get("numeroFattura"), dataFattura, (String)fm.get("condizioni"), (String)fm.get("filiale"), (String)fm.get("iban"),
							(String)fm.get("numeroOrdine"),(String) fm.get("numeroOfferta"), (String)fm.get("lineaOrdine"), (String)fm.get("bem"), (String)fm.get("elementoWbs"), (String)fm.get("conto"), 
							(String)fm.get("prCenter"), imponibile, (String)fm.get("iva"), totaleIva, totaleImporto, listaAF,
							(String)fm.get("nomeAzienda"),(String)fm.get("capitaleSociale"),(String)fm.get("sedeLegale"),(String)fm.get("sedeOperativa"),(String)fm.get("registroImprese"),(String)fm.get("rea")));
					
					fis = new FileInputStream(/*Constanti.PATHAmazon+*/"JasperReport/report1.jasper");
					
					bufferedInputStream = new BufferedInputStream(fis);

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);

					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters,  getDataSourceFatture(listaO));
					
					final JRRtfExporter rtfExporter = new JRRtfExporter();
					final ByteArrayOutputStream rtfStream = new ByteArrayOutputStream();
					rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
					rtfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,rtfStream);
					rtfExporter.exportReport();
					rtfResume = rtfStream.toByteArray();
					
					ServletOutputStream outStream = response.getOutputStream();
					response.setContentType("application/rtf");
					//response.setContentType("application/vnd.ms-excel");
					// set content dispostion to attachment in with file name.
					// case the open/save dialog needs to appear.
					response.setHeader("Content-Disposition", "attachment;filename="+"1");
								
					for(int i=0; i<rtfResume.length; i++){
						outStream.write(rtfResume[i]);			
					}
							
					outStream.flush();
					outStream.close();		
					
				} catch (JRException e) {
					e.printStackTrace();
				}				
			}
			else
			{
			//operazione ONE
				
			List<RiepilogoDatiOreMeseJavaBean> listaJB= new ArrayList<RiepilogoDatiOreMeseJavaBean>();
			listaJB= ServerUtility.PrintRiepilogoOreMese(dataRif, sedeOperativa, username);
			
			Map parameters = new HashMap();
			
			JasperPrint jasperPrint;
			FileInputStream fis;
			BufferedInputStream bufferedInputStream;

			try {

				fis = new FileInputStream(Constanti.PATHAmazon+"JasperReport/ReportRiepilogoOre.jasper");
									
				bufferedInputStream = new BufferedInputStream(fis);
				JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(bufferedInputStream);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, getDataSourceRiepMensile(listaJB));
					
				JasperExportManager.exportReportToPdfFile(jasperPrint,Constanti.PATHAmazon+"FileStorage/RiepilogoOre_"+username+".pdf");

			} catch (JRException e) {

				e.printStackTrace();
			}				
		}
	}

	
	
	private List<AttivitaFatturateJavaBean> elaboraListaAttivita(
			List<AttivitaFatturateModel> listaA) {
		List<AttivitaFatturateJavaBean>listaJB=new ArrayList<AttivitaFatturateJavaBean>();
		AttivitaFatturateJavaBean aJ;
		for(AttivitaFatturateModel a:listaA){
			aJ=new AttivitaFatturateJavaBean((String)a.get("descrizione"),(String) a.get("importo"));
			listaJB.add(aJ);
		}
			
		return listaJB;
	}



	private static JRDataSource getDataSourceFatture(List<FatturaJavaBean> listaO) {
		Collection<FatturaJavaBean> riep= new ArrayList<FatturaJavaBean>();
		for(FatturaJavaBean r: listaO)
			riep.add(r);		
		return new JRBeanCollectionDataSource(riep);
	}



	private static JRDataSource getDataSourceSalPcl(List<RiepilogoSALPCLJavaBean> listaR) {
		Collection<RiepilogoSALPCLJavaBean> riep= new ArrayList<RiepilogoSALPCLJavaBean>();
		for(RiepilogoSALPCLJavaBean r: listaR)
			riep.add(r);		
		return new JRBeanCollectionDataSource(riep);
	}


	private static JRDataSource getDataSourceRiepMensile(List<RiepilogoDatiOreMeseJavaBean> listaJB) {
		Collection<RiepilogoDatiOreMeseJavaBean> riep= new ArrayList<RiepilogoDatiOreMeseJavaBean>();
		for(RiepilogoDatiOreMeseJavaBean r: listaJB)
			riep.add(r);
		
		return new JRBeanCollectionDataSource(riep);
	}


	private static JRDataSource getDataSourceRiepilogoCommesse(List<RiepilogoMeseGiornalieroJavaBean> listaD) {
		Collection<RiepilogoMeseGiornalieroJavaBean> riep= new ArrayList<RiepilogoMeseGiornalieroJavaBean>();
		for(RiepilogoMeseGiornalieroJavaBean r: listaD)
			riep.add(r);
		
		return new JRBeanCollectionDataSource(riep);
	}


	private static JRDataSource getDataSourceCosti(List<RiepilogoCostiDipendentiBean> listaB) {
		Collection<RiepilogoCostiDipendentiBean> riep= new ArrayList<RiepilogoCostiDipendentiBean>();
		for(RiepilogoCostiDipendentiBean r: listaB)
			riep.add(r);
		
		return new JRBeanCollectionDataSource(riep);
	}


	private static JRDataSource getDataSourceNonFatt(List<RiepilogoOreNonFatturabiliJavaBean> listaR) {
		Collection<RiepilogoOreNonFatturabiliJavaBean> riep= new ArrayList<RiepilogoOreNonFatturabiliJavaBean>();		
		for(RiepilogoOreNonFatturabiliJavaBean r: listaR){
			riep.add(r);
		}	
		return new JRBeanCollectionDataSource(riep);
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