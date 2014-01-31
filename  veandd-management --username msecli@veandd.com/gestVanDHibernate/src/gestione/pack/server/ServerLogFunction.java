package gestione.pack.server;

import gestione.pack.shared.LogErrori;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ServerLogFunction {
	
	public static void logOkMessage(String operazione, Date dataOperazione, String username, String esitoOperazione){
		
		LogErrori logMsg= new LogErrori();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			
				tx=	session.beginTransaction();
			
				logMsg.setOperazione(operazione);
				logMsg.setUsername(username);
				logMsg.setOraOperazione(dataOperazione);
				logMsg.setEsitoOperazione(esitoOperazione);
				
				session.save(logMsg);
				
				tx.commit();
														
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    
		    } finally {
		    	session.close();
		    }					
	}
	
	public static void logFailedMessage(String operazione, Date dataOperazione, String username, String esitoOperazione, String tipoErrore){
		
		LogErrori logMsg= new LogErrori();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			
				tx=	session.beginTransaction();
			
				logMsg.setOperazione(operazione);
				logMsg.setUsername(username);
				logMsg.setOraOperazione(dataOperazione);
				logMsg.setEsitoOperazione(esitoOperazione);
				logMsg.setCodiceErrore(tipoErrore);
				
				session.save(logMsg);
				tx.commit();
														
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    
		    } finally {
		    	session.close();
		    }			
	}

	public static void logErrorMessage(String operazione, Date dataOperazione, String username, String esitoOperazione, String tipoErrore){
	
		LogErrori logMsg= new LogErrori();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			
				tx=	session.beginTransaction();
			
				logMsg.setOperazione(operazione);
				logMsg.setUsername(username);
				logMsg.setOraOperazione(dataOperazione);
				logMsg.setEsitoOperazione(esitoOperazione);
				logMsg.setCodiceErrore(tipoErrore);
				
				session.save(logMsg);
				tx.commit();
														
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    
		    } finally {
		    	session.close();
		    }		
	}

	
}
