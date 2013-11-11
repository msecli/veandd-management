package gestione.pack.server;

import gestione.pack.shared.DatiTimbratriceExt;
import gestione.pack.shared.DettaglioTimbrature;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;


public class GenerateFileServlet  extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private String file="/var/lib/tomcat7/webapps/ROOT/FileStorage/FileUtili/tmb";
	final String fileAmazon = "/var/lib/tomcat7/webapps/ROOT/FileStorage/FileUtili/tmb";
	final String fileLocale = "C:\\Users\\VE&D\\workspace\\veandd-management\\gestVanDHibernate\\war\\FileStorage\\FileUtili\\tmb";
	
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<DettaglioTimbrature> listaDT= new ArrayList<DettaglioTimbrature>();
					
		HttpSession httpSession = request.getSession();
		String sede=(String) httpSession.getAttribute("sede"); 
		String dataRif= (String) httpSession.getAttribute("data");
		
		String numeroBadge;
		String giornoRiferimento;
		String movimento;
		String orario;
		String giustificativo;
		String nGiustificativi;
		Date data= new Date();
		SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yy",Locale.ITALIAN);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				
			tx=	session.beginTransaction();
				
			session.createSQLQuery("truncate dati_timbratrice_ext").executeUpdate();
			
			listaDT=session.createSQLQuery("select p.numeroBadge, iu.movimento, iu.orario, dg.giornoRiferimento, dg.giustificativo " +
					"	from gestionaledb.dettaglio_intervalli_i_u as iu, gestionaledb.dettaglio_ore_giornaliere as dg, " +
					"		gestionaledb.foglio_ore_mese as f, gestionaledb.personale as p " +
					"	where iu.ID_DETT_ORE_GIORNO=dg.ID_DETTAGLIO_ORE_GIORNALIERE " +
					"	and dg.ID_FOGLIO_ORE=f.ID_FOGLIO_ORE and f.meseRiferimento=:data " +
					"	and f.ID_PERSONALE=p.ID_PERSONALE and p.sedeOperativa=:sede").setParameter("data", dataRif)
					.setParameter("sede", sede).list();
			
			tx.commit();
			session.close();
			
			for(ListIterator iter = listaDT.listIterator(); iter.hasNext(); ) {
				 Object[] row = (Object[]) iter.next();
				 
				 numeroBadge=(String) row[0];
				 data=(Date) row[3];
				 movimento=(String) row[1];
				 orario=(String) row[2];			
				 giustificativo=(String) row[4];
				 
				 //TODO vedo il numero alla fine della stringa e se è maggiore di uno assegno "00"
				 
				 /*if(giustificativo.length()>2){
					 nGiustificativi=giustificativo.substring(giustificativo.length()-1);
					 giustificativo=giustificativo.substring(0, ..length()-1);
					 
					 if(Float.valueOf(nGiustificativi)==2)
						 giustificativo="00"; //sono presenti più di un giustificativo e quindi nel file che genero non ne faccio comparire
					 else
						 giustificativo=getCodiceGiustificativo(giustificativo);
				 }
				 	else*/
				 
				 //		giustificativo=getCodiceGiustificativo(giustificativo);		 
							 
				 if(orario.length()==4)
					 orario="0"+orario;
				 
				 giornoRiferimento=formatter.format(data);
				 movimento=movimento.substring(0,1).toUpperCase();
			
				 if(numeroBadge.length()>0)
					 createEntryTmbExt(numeroBadge, giornoRiferimento, movimento, orario, "00");//cambiare in caso con giustificativo
	        }		
					
			File f=new File(fileAmazon);			
			if(f.exists()){
				f.delete();
			}
			
			exportDataToFile();
			f=new File(fileAmazon);
				
			FileInputStream fin = new FileInputStream(f);
			ServletOutputStream outStream = response.getOutputStream();
			// SET THE MIME TYPE.
			response.setContentType("text/cvs");
			// set content dispostion to attachment in with file name.
			// case the open/save dialog needs to appear.
			response.setHeader("Content-Disposition", "attachment;filename=tmb");

			byte[] buffer = new byte[1024];
			int n = 0;
			while ((n = fin.read(buffer)) != -1) {
			outStream.write(buffer, 0, n);
			System.out.println(buffer);
			}
			
			outStream.flush();
			fin.close();
			outStream.close();
					
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
		}finally{
			//session.close();
		}			
	}
	

	private String getCodiceGiustificativo(String codice) {
		
		switch (codice) {
		
		case "09":
			return"09";
			
		case "27":
			return"27";
			
		case "01":
			return"01";
			
		case "02":
			return"02";
		
		case "05.Permesso non Retrib.":
			return "05";
			
		case "06.Malattia":
			return "10";
			
		case "07.Infortunio":
			return "13";
			
		case "08.Maternita' Obblig.":
			return "15";
			
		case "09.Maternita' Facolt.":
			return "16";
			
		case "09.1.Maternita' Antic.":
			return "19";
			
		case "10.Aspettativa":
			return "17";
			
		case "11.Permesso Elettor.":
			return "20";
			
		case "12.Permesso Sindac.":
			return "21";
			
		case "13.Donazione Sangue":
			return "22";
			
		case "14.Congedo Matrim.":
			return "23";
			
		case "15.Allattamento":
			return "25";
			
		case "16.Presente":
			return "34";
			
		case "18.Sciopero":
			return "48";
			
		case "22.Permesso Lutto":
			return "55";
			
		/*case "23.Abbuono":
			return "82";*/
			
		case "24.Cassa Integrazione":
			return "85";
			
		case "25.Permesso Legge 104":
			return "88";
			
		case "26.Assenza Studio":
			return "99";
			
		case "27.Ore Viaggio":
			return "70";
			
		case "28.Santo Patrono":
			return "31";		

		default:
			return "00";
		}
		
	}


	private void exportDataToFile() {
				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
					
		try {			
			tx=	session.beginTransaction();
			
			session.doWork(new Work() {
                @Override
                public void execute(Connection conn) throws SQLException {
                      PreparedStatement pStmt = null;
                      ResultSet rs = null;
                      try
                      {	
                    	  String sqlQry = "SELECT codice,matricolaPersonale,giorno,orario,movimento,codice1,codice2,codice3 FROM gestionaledb.dati_timbratrice_ext";
                    	  pStmt = conn.prepareStatement(sqlQry);
                    	  rs=pStmt.executeQuery();
                    	                     	  
                    	  File file = new File(fileAmazon);  
                    	  FileWriter fstream;
                    	  BufferedWriter out=null;
                    	  try {
                    		  fstream = new FileWriter(file);
                    		  out = new BufferedWriter(fstream);
                    		  
                    		  while(rs.next()){                                  
                    			  out.write(rs.getString(1));
                    			  out.write(" ");
                    			  out.write(rs.getString(2));
                    			  out.write(" ");
                    			  out.write(rs.getString(3));
                    			  out.write(" ");
                    			  out.write(rs.getString(4));
                    			  out.write(" ");
                    			  out.write(rs.getString(5));
                    			  out.write(" ");
                    			  out.write(rs.getString(6));
                    			  out.write(" ");
                    			  out.write(rs.getString(7));
                    			  out.write(" ");
                    			  out.write(rs.getString(8));
                    			  out.write("\r\n");
                              }
                    		  
                    		  out.close();                   		  
                    	  } catch (IOException e) {
								e.printStackTrace();
                    	  }           	                     	
                      }
                      finally
                      {
                             pStmt.close();
                      }                                
                }
			});				
			if (!tx.wasCommitted())
			    tx.commit();		
		}catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
		}finally{
			session.close();
		}
	}


	private void createEntryTmbExt(String numeroBadge, String giornoRiferimento, String movimento, String orario, String giustificativo) {
		
		DatiTimbratriceExt datoTmbExt;
				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try{
				
			tx=session.beginTransaction();
			
			datoTmbExt= new DatiTimbratriceExt();
			datoTmbExt.setCodice("01");
			datoTmbExt.setMatricolaPersonale(numeroBadge);
			datoTmbExt.setGiorno(giornoRiferimento);
			datoTmbExt.setMovimento(movimento);
			datoTmbExt.setOrario(orario);
			datoTmbExt.setCodice1("0003");
			datoTmbExt.setCodice2(giustificativo); //giustificativo
			datoTmbExt.setCodice3("P");	
			
			session.save(datoTmbExt);		
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
