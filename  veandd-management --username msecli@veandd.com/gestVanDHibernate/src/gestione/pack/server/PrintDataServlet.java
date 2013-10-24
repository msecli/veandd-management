package gestione.pack.server;

import gestione.pack.client.model.DatiFatturazioneMeseJavaBean;
import gestione.pack.client.model.RiepilogoAnnualeJavaBean;
import gestione.pack.client.model.RiepilogoCostiDipendentiBean;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.model.RiepilogoDatiOreMeseJavaBean;
import gestione.pack.client.model.RiepilogoMensileDatiIntervalliCommesseJavaBean;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliJavaBean;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoSALPCLJavaBean;
import gestione.pack.client.model.RiepilogoSALPCLModel;
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
			
			List<RiepilogoMensileDatiIntervalliCommesseJavaBean> listaJB=new ArrayList<RiepilogoMensileDatiIntervalliCommesseJavaBean>();
			
			String nome=username.substring(0, username.indexOf("."));
			nome=nome.substring(0, 1).toUpperCase()+nome.substring(1,nome.length());
			
			String cognome=username.substring(username.indexOf(".")+1, username.length());
			cognome=cognome.substring(0, 1).toUpperCase()+cognome.substring(1,cognome.length());
			
			String nomeFile=cognome+nome+"_Report.pdf";
			
			String totOreCommesse=(String) httpSession.getAttribute("totOreCommesse");
			String totOreIU=(String) httpSession.getAttribute("totOreIU");
			
			
			listaJB= ServerUtility.getRiepilogoGiornalieroCommesse(username, dataRif);

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

						fis = new FileInputStream(/*Constanti.PATHAmazon+*/"JasperReport/ReportRiepilogoSalPcl.jasper");
												
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
						exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, /*Constanti.PATHAmazon+*/"FileStorage/ReportRiepilogoSalPcl.xls");
						exporterXLS.exportReport();
							
						File f=new File(/*Constanti.PATHAmazon+*/"FileStorage/ReportRiepilogoSalPcl.xls");
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


	private static JRDataSource getDataSourceRiepilogoCommesse(List<RiepilogoMensileDatiIntervalliCommesseJavaBean> listaD) {
		Collection<RiepilogoMensileDatiIntervalliCommesseJavaBean> riep= new ArrayList<RiepilogoMensileDatiIntervalliCommesseJavaBean>();
		for(RiepilogoMensileDatiIntervalliCommesseJavaBean r: listaD)
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