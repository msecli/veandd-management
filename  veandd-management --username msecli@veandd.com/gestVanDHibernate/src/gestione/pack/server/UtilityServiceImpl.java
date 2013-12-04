package gestione.pack.server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import gestione.pack.client.UtilityService;
import gestione.pack.shared.AssociazionePtoA;
import gestione.pack.shared.AttivitaOrdine;
import gestione.pack.shared.Commessa;
import gestione.pack.shared.DettaglioIntervalliCommesse;
import gestione.pack.shared.DettaglioOreGiornaliere;
import gestione.pack.shared.FoglioFatturazione;
import gestione.pack.shared.FoglioOreMese;
import gestione.pack.shared.Ordine;
import gestione.pack.shared.Personale;
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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> checkIntervallicommesse(String periodo) {
		
		List<Personale> listaP= new ArrayList<Personale>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaD= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaDC= new ArrayList<DettaglioIntervalliCommesse>();
		List<AssociazionePtoA> listaAss= new ArrayList<AssociazionePtoA>();
		Commessa c= new Commessa();
		FoglioOreMese f= new FoglioOreMese();
		List<String> listaCheck= new ArrayList<String>();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx=session.beginTransaction();
			
			listaP=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede").setParameter("sede", "T").list();
			
			for(Personale p:listaP){
								
				if(!p.getAssociazionePtoas().isEmpty())
					listaAss.addAll(p.getAssociazionePtoas());
				
					f=(FoglioOreMese)session.createQuery("from FoglioOreMese where meseRiferimento=:mese and id_personale=:id")
						.setParameter("id", p.getId_PERSONALE()).setParameter("mese", periodo).uniqueResult();//TODO impostare la scelta del mese
					if(f!=null)
						if(!f.getDettaglioOreGiornalieres().isEmpty())
							listaD.addAll(f.getDettaglioOreGiornalieres());
				
					for(DettaglioOreGiornaliere d: listaD){					
						for(AssociazionePtoA ass:listaAss){
							c= ass.getAttivita().getCommessa();
						
							listaDC=(List<DettaglioIntervalliCommesse>)session.createQuery("from DettaglioIntervalliCommesse " +
									"where id_dettaglio_ore=:id and numeroCommessa=:numeroCommessa and estensioneCommessa=:estensione")
									.setParameter("id", d.getIdDettaglioOreGiornaliere()).setParameter("numeroCommessa", c.getNumeroCommessa())
									.setParameter("estensione", c.getEstensione()).list();	
						
							if(listaDC.size()>1)
								listaCheck.add(c.getNumeroCommessa()+" "+c.getEstensione()+" "+String.valueOf(d.getIdDettaglioOreGiornaliere())+" "+p.getCognome());
						
							listaDC.clear();
						}				
				}	
				listaD.clear();
				listaF.clear();
				listaAss.clear();
			}
			
			tx.commit();		
			return listaCheck;			
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}				
	}
	
	
}
