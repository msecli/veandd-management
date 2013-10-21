package gestione.pack.server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import gestione.pack.client.UtilityService;
import gestione.pack.shared.AttivitaOrdine;
import gestione.pack.shared.Commessa;
import gestione.pack.shared.FoglioFatturazione;
import gestione.pack.shared.Ordine;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

public class UtilityServiceImpl extends PersistentRemoteService implements UtilityService{
	
private static final long serialVersionUID = 1L;
	
	public UtilityServiceImpl(){
		
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
	    gileadHibernateUtil.setSessionFactory(MyHibernateUtil.getSessionFactory());

	    PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
	    persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
	    persistentBeanManager.setProxyStore(new StatelessProxyStore());

	    setBeanManager(persistentBeanManager);	
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void generateAttivitaOrdine() {
		
		List<Ordine> listaOrdini= new ArrayList<Ordine>(); 
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			tx=	session.beginTransaction();
			
			listaOrdini=(List<Ordine>)session.createQuery("from Ordine").list();
			tx.commit();
			session.close();
						
			for(Ordine o:listaOrdini)
				createAttivitaOrdine(o.getNumeroOrdine());
			
			
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    } finally {
		    	//session.close();
		    }	
		
	}


	private void createAttivitaOrdine(int id) {
		AttivitaOrdine att= new AttivitaOrdine();
		Ordine o=new Ordine();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			
			tx=	session.beginTransaction();
						
			o=(Ordine)session.get(Ordine.class, id);
			
			att.setDescrizioneAttivita("");
			att.setTariffaAttivita(o.getTariffaOraria());
			att.setOrdine(o);
			
			o.getAttivitaOrdines().add(att);
			
			session.save(o);			
			
			tx.commit();
					
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    } finally {
		    	session.close();
		    }			
	}	
	
	
	@Override
	public void insertIdAttivitaOrdineInFoglioFatturazione() {
		
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		Ordine o;
		int id;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;		
		
		try {
			tx=	session.beginTransaction();
			
			listaCommesse=(List<Commessa>)session.createQuery("from Commessa").list();
			
			for(Commessa c:listaCommesse){
				
				if(c.getOrdines().iterator().hasNext()){
					o=c.getOrdines().iterator().next();
					
					if(c.getFoglioFatturaziones().iterator().hasNext())
						listaFF.addAll(c.getFoglioFatturaziones());
					
					if(listaFF.size()>0){
						for(FoglioFatturazione f: listaFF)
							if(o.getAttivitaOrdines().iterator().hasNext()){
								id=o.getAttivitaOrdines().iterator().next().getIdAttivitaOrdine();
								f.setAttivitaOrdine(id);
							}
						listaFF.clear();						
					}
				}
			}		
					
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
