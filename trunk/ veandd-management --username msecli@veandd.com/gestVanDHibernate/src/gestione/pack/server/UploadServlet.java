package gestione.pack.server;

import gestione.pack.shared.DatiTimbratriceExt;
import gestione.pack.shared.DettaglioTimbrature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class UploadServlet extends HttpServlet  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//String rootDirectory = "FileStorage/";
		
		/*Amazon*/
		String rootDirectory = "/var/lib/tomcat7/webapps/ROOT/FileStorage/";
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			try {
				List items = upload.parseRequest(request);
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
					} else {
						String fileName = item.getName();
						if (fileName != null && !fileName.equals("")) {
							fileName = FilenameUtils.getName(fileName);
							File uploadedFile = new File(rootDirectory+  fileName);
							try {
								item.write(uploadedFile);
								response.getWriter().write(fileName);								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							loadDataByFile(fileName);
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void loadDataByFile(String fileName){		
		List<DatiTimbratriceExt> listaT= new ArrayList<DatiTimbratriceExt>();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		try {
			tx=	session.beginTransaction();
				
			session.createSQLQuery("truncate dati_timbratrice_ext").executeUpdate();
			
			//session.createSQLQuery("LOAD DATA INFILE :file INTO TABLE dati_timbratrice_ext FIELDS TERMINATED BY ' ' LINES TERMINATED BY '\r\n' (codice,matricolaPersonale,giorno,orario,movimento,codice1,codice2,codice3)")
		    //.setString("file", "C:/Users/Mauro/workspace/gestVanDHibernate/war/FileStorage/"+fileName).executeUpdate(); //Locale
			//.setString("file", "C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 7.0\\webapps\\ROOT\\FileStorage\\"+fileName).executeUpdate(); //webserverlocale
			
			/*Amazon*/
			session.createSQLQuery("LOAD DATA LOCAL INFILE :file INTO TABLE dati_timbratrice_ext FIELDS TERMINATED BY ' ' LINES TERMINATED BY '\r\n' (codice,matricolaPersonale,giorno,orario,movimento,codice1,codice2,codice3)")
				.setString("file", "/var/lib/tomcat7/webapps/ROOT/FileStorage/"+fileName).executeUpdate(); //ServerAmazon
		
			listaT=(List<DatiTimbratriceExt>)session.createQuery("from DatiTimbratriceExt").list();
			
			tx.commit();
			session.close();
			
			for(DatiTimbratriceExt d:listaT){
					createIntervalloTimbr(d);
			}
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
		}finally{
			//session.close();
		}			
	}


	private void createIntervalloTimbr(DatiTimbratriceExt d) {
		
		DettaglioTimbrature dettT=new DettaglioTimbrature();	
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		try{
					
			dettT.setGiorno(d.getGiorno());
			dettT.setMovimento(d.getMovimento());
			dettT.setNumeroBadge(d.getMatricolaPersonale());
			dettT.setOrario(d.getOrario());
			
			tx=session.beginTransaction();
			
			session.save(dettT);
			tx.commit();
			
		}catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
		}	finally{
			session.close();
		}
	}
}
