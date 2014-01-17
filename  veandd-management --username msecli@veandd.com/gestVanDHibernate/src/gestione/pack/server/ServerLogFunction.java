package gestione.pack.server;

import gestione.pack.shared.Personale;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class ServerLogFunction {
	
	private void logOkMessage(String operazione, Date dataOperazione, String username){
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			
				tx=	session.beginTransaction();
			
				tx.commit();
														
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    
		    } finally {
		    	session.close();
		    }				
		
	}
	
	private void logFailedMessage(String operazione, Date dataOperazione, String username, String esitoOperazione, String tipoErrore){
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			
				tx=	session.beginTransaction();
			
				tx.commit();
														
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    
		    } finally {
		    	session.close();
		    }	
		
	}

	private void logErrorMessage(String operazione, Date dataOperazione, String username, String esitoOperazione, String tipoErrore){
	
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			
				tx=	session.beginTransaction();
			
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
