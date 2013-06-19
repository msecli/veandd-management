/*******************************************************************************
F * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package gestione.pack.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;
import net.sf.gilead.gwt.PersistentRemoteService;

import org.hibernate.Session;
import org.hibernate.Transaction;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.FoglioFatturazioneModel;
import gestione.pack.client.model.GiustificativiModel;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.IntervalliIUModel;
import gestione.pack.client.model.PersonaleAssociatoModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RdaModel;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoOreDipCommesse;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreModel;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;
import gestione.pack.shared.AssociazionePtoA;
import gestione.pack.shared.Attivita;
import gestione.pack.shared.Cliente;
import gestione.pack.shared.Commessa;
//import gestione.pack.shared.DatiTimbratriceExt;
import gestione.pack.shared.Commenti;
import gestione.pack.shared.DettaglioIntervalliCommesse;
import gestione.pack.shared.DettaglioIntervalliIU;
import gestione.pack.shared.DettaglioOreGiornaliere;
import gestione.pack.shared.DettaglioTimbrature;
import gestione.pack.shared.FoglioFatturazione;
import gestione.pack.shared.FoglioOreMese;
import gestione.pack.shared.Offerta;
import gestione.pack.shared.Ordine;
import gestione.pack.shared.Personale;
import gestione.pack.shared.Rda;


/*
 * Importante porre attenzione alla gestione delle relazioni oneToMany: Nel momento in cui è necessario che la relazione "figlia"
 * resti in memoria, allora è importante che il valore della chiave secondaria sia impostabile a null. Inoltre è poi importante gestire opportunamente 
 * la creazione del model per la visualizzazione nelle grid.
 * */


public class AdministrationServiceImpl extends PersistentRemoteService implements AdministrationService {

	private static final long serialVersionUID = 1L;

	public AdministrationServiceImpl(){
		HibernateUtil gileadHibernateUtil = new HibernateUtil();
	    gileadHibernateUtil.setSessionFactory(MyHibernateUtil.getSessionFactory());

	    PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
	    persistentBeanManager.setPersistenceUtil(gileadHibernateUtil);
	    persistentBeanManager.setProxyStore(new StatelessProxyStore());

	    setBeanManager(persistentBeanManager);
	}
		
//------------------Gestione Personale-----------------------------------------------------------------------------------------
	@Override
	public void insertDataPersonale(String nome, String cognome, String username, String password, String nBadge, String ruolo, String tipoOrario, String tipoLavoratore,
			String gruppoLavoro, String costoOrario,  String costoStruttura,String sede, String sedeOperativa,  String oreDirette, String oreIndirette,  String permessi, String ferie, String ext, String oreRecupero)throws IllegalArgumentException {
		
		//TODO controllo presenza username
		
			Personale p = new Personale();		
			p.setNome(nome);
			p.setCognome(cognome);
			p.setNumeroBadge(nBadge);
			p.setTipologiaOrario(tipoOrario);
			p.setTipologiaLavoratore(tipoLavoratore);
			p.setCostoOrario(costoOrario);
			p.setCostoStruttura(costoStruttura);
			p.setRuolo(ruolo);
			p.setUsername(username);
			p.setPassword(username);//sarà sempre lo username passato lato client
			p.setGruppoLavoro(gruppoLavoro);
			p.setSede(sede);
			p.setSedeOperativa(sedeOperativa);
			p.setOreDirette(oreDirette);
			p.setOreIndirette(oreIndirette);
			p.setOrePermessi(permessi);
			p.setOreFerie(ferie);
			p.setOreExFest(ext);
			p.setOreRecupero(oreRecupero);
			
			Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tx= null;		
			
			try {
				tx=	session.beginTransaction();
				session.save(p);
				tx.commit();
			    } catch (Exception e) {
			    	if (tx!=null)
			    		tx.rollback();		    	
			      e.printStackTrace();
			    } finally {
			    	//session.close();
			    }									
		}
	
	
	@Override
	public void editDataPersonale(int id, String nome, String cognome, String username, String password, String nBadge, String ruolo, String tipoOrario, String tipoLavoratore, String gruppoLavoro, 
			String costoOrario, String costoStruttura, String sede, String sedeOperativa, String oreDirette, String oreIndirette,  String permessi, String ferie, String ext, String oreRecupero) throws IllegalArgumentException {
				
		Personale p = new Personale();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;

		try {
			tx=	session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where id_personale=:id").setParameter("id", id).uniqueResult();
			
			p.setNome(nome);
			p.setCognome(cognome);
			p.setNumeroBadge(nBadge);
			p.setTipologiaOrario(tipoOrario);
			p.setTipologiaLavoratore(tipoLavoratore);
			p.setCostoOrario(costoOrario);
			p.setCostoStruttura(costoStruttura);
			p.setRuolo(ruolo);
			p.setUsername(username);
			//p.setPassword(password);
			p.setGruppoLavoro(gruppoLavoro);
			p.setSede(sede);
			p.setSedeOperativa(sedeOperativa);
			p.setOreDirette(oreDirette);
			p.setOreIndirette(oreIndirette);
			p.setOrePermessi(permessi);
			p.setOreFerie(ferie);
			p.setOreRecupero(oreRecupero);
			//p.setOreExFest(ext);

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
	public void removeDataPersonale(int id) throws IllegalArgumentException {
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		Personale p= new Personale();
			
		try {
			  tx=	session.beginTransaction();
			  p=(Personale)session.get(Personale.class, id);
			  session.delete(p);
			  tx.commit();
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();
		    } finally {
		    	//session.close();		      
		    }
	}
	
	
	@Override
	public List<PersonaleModel> getAllPersonaleModel() throws IllegalArgumentException{	
		
		List<Personale> listaP = (ArrayList<Personale>) ConverterUtil.getPersonale();  
		List<PersonaleModel> listaDTO = new ArrayList<PersonaleModel>(listaP!=null ? listaP.size() : 0);  
		    
		     try{
					if(listaP!=null){
					
						for(Personale p : listaP){	
							listaDTO.add(ConverterUtil.personaleToModelConverter(p));
							}
						}
							
					}catch (Exception e) {
						e.printStackTrace();
					} 
		     
			return listaDTO;
		} 
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNomePM() throws IllegalArgumentException {
		
		List<Personale> listaP= new ArrayList<Personale>();
		List<String> listaNomi= new ArrayList<String>();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			  tx=	session.beginTransaction();
			  listaP=(ArrayList<Personale>)session.createQuery("from Personale where ruolo='PM' or ruolo='DIR'").list();
			  
			  tx.commit();
			 
			  for(Personale p :listaP){	
			
					  listaNomi.add(p.getCognome()+" "+p.getNome());			  
			  }
			  return listaNomi;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();
		    		return null;
		    }			
	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> getListaDipendenti(String ruolo)throws IllegalArgumentException{	
		List<String> listaNomi= new ArrayList<String>();
		List<Personale> listaP=new ArrayList<Personale>();
		
		String nominativoPersonale= new String();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		try {
			tx=	session.beginTransaction();
			listaP = (List<Personale>)session.createQuery("from Personale").list();
			tx.commit();
			
			  for(Personale p: listaP){
				  
				//passare Cognome Nome (ID)
				nominativoPersonale=(p.getCognome()+" "+p.getNome()+" ("+p.getId_PERSONALE()+")");
				if(ruolo.compareTo("PM")==0)//ruolo di chi effettua la ricerca
					if(p.getRuolo().compareTo("DIP")==0)//se è il pm a richiederlo allora preleverò solo i dipendenti
						listaNomi.add(nominativoPersonale);		
				if(ruolo.compareTo("PM")!=0)//se non è un PM a richiederlo allora ci saranno tutti i nomi
					listaNomi.add(nominativoPersonale);					
			  }
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    } finally {
		        session.close();
		    }			
		return listaNomi;
	}	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PersonaleModel> getListaDipendentiModel(String ruolo) {
		
		List<PersonaleModel> listaNomi= new ArrayList<PersonaleModel>();
		PersonaleModel personaM= new PersonaleModel();
		List<Personale> listaP=new ArrayList<Personale>();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		try {
			tx=	session.beginTransaction();
			listaP = (List<Personale>)session.createQuery("from Personale").list();
			tx.commit();
			
			  for(Personale p: listaP){
				personaM=new PersonaleModel(p.getId_PERSONALE(), p.getNome(), p.getCognome(), p.getUsername(), 
						"", "", "", "", "", "", "", "", "",  "",  "",  "",  "",  "",  "",  "");
				
				if(ruolo.compareTo("PM")==0)//ruolo di chi effettua la ricerca
					if(p.getRuolo().compareTo("DIP")==0)//se è il pm a richiederlo allora preleverò solo i dipendenti
						listaNomi.add(personaM);		
				if(ruolo.compareTo("PM")!=0)//se non è un PM a richiederlo allora ci saranno tutti i nomi
					listaNomi.add(personaM);					
			  }
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    } finally {
		        session.close();
		    }			
		return listaNomi;
	}
	
//--------------------------------------------------------------------------------------------------------------

	
	
//-----------------------------------------Gestione RDA---------------------------------------------------------
	
	/*
	 * A livello db il fatto che rda possa avere più offerte e ordini è dovuto al fatto che erano previste le revisioni da gestire. 
	 * Non essendo gestite ogni rda avrà un'ordine e un'offerta.
	 */
		
	/*public boolean insertDataRda(int idCliente, String numRda, String ragioneSociale){
		
		Rda r=new Rda();
		Cliente c= new Cliente();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
			c = (Cliente)session.createQuery("from Cliente where ragioneSociale= :ragSociale").setParameter("ragSociale", ragioneSociale).uniqueResult();	
					
			r.setCliente(c);
			r.setCodiceRDA(numRda);
		    c.getRdas().add(r);
		    
		    session.save(c);
		    tx.commit();
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			
			return false;
		}
	}
	
	public boolean editDataRda(int idRda, int idCliente, String numRda){
		
		Rda r=new Rda();
				
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
			r = (Rda)session.get(Rda.class, idRda);	
					
			r.setCodiceRDA(numRda);
		    		    
		    tx.commit();
			return true;
		    
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			return false;
		}				
	}
	
	
	@Override
	public boolean deleteDataRda(int idRda) {
		
		Rda r= new Rda();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
				
		try {
			  tx=	session.beginTransaction();
			  r=(Rda)session.createQuery("from Rda where numero_rda =:idrda").setParameter("idrda", idRda).uniqueResult();
			  
			  for(Offerta o: r.getOffertas()){
				  
				  o.setRda(null);
			  }
			  
			  for(Ordine or: r.getOrdines()){
				  or.setRda(null);
			  }
			  
			  session.delete(r);
			  tx.commit();
			  
			  return true;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();
		    		return false;
		    }		
	}
	*/
	
	public List<RdaModel> getAllRdaModel() throws IllegalArgumentException {		
		Set<Rda> listaR =  ConverterUtil.getRda();  
		List<RdaModel> listaM = new ArrayList<RdaModel>(listaR!=null ? listaR.size() : 0);  
				    
		     try{
					if(listaR!=null){
					
						for(Rda r : listaR){	
							listaM.add(ConverterUtil.rdaToModelConverter(r));
							}
						}
					
					return listaM;	
						
				}catch (Exception e) {
						e.printStackTrace();			
					return null;
				} 	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> getAllNumeroRdo() throws IllegalArgumentException {
		
		List<String> listaRdo= new ArrayList<String>();
		List<Rda> lista=new ArrayList<Rda>();
				
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=	session.beginTransaction();
			  lista=(List<Rda>)session.createQuery("from Rda").list();
			  
			  for(Rda r:lista){
				  
				  listaRdo.add(r.getCodiceRDA()+"("+r.getCliente().getRagioneSociale()+")"); //il codice che rappresenta il numero effettivo della Rdo
			  }
			  
			  tx.commit();
			  return listaRdo;	     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    	return null;
		    }
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RdoCompletaModel> getAllRdoCompletaModel()	throws IllegalArgumentException {
		List<Rda> lista=new ArrayList<Rda>();
		List<RdoCompletaModel> listaM= new ArrayList<RdoCompletaModel>();
		RdoCompletaModel rdoM= new RdoCompletaModel();
		Ordine or= new Ordine();
		Offerta of=new Offerta();
			
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);	;
		
		String numRdo="#";
		
		String numOfferta="#";
		String dataOfferte="#";
		String importo="0.0";
		
		String numOrdine="#";
		String descrizione="#";
		String numCommessa= "#";
		String dataInizio="#";
		String dataFine="#";
		String tariffa="0.0";
		String numRisorse="0";
		String oreDisp="0.0";
		String oreRes="0.0";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
			
		try {
			  tx=session.beginTransaction();
			  lista=(List<Rda>)session.createQuery("from Rda").list();
			  
			  for(Rda r:lista){				  
				  numRdo=r.getCodiceRDA();//se in fase di inserimento non c'era allora sarà #
				  
				  if(r.getOrdines().iterator().hasNext()){
					  or=r.getOrdines().iterator().next();
					  numOrdine=or.getCodiceOrdine();
					  descrizione=or.getDescrizioneAttivita();
					  if(or.getCommessa()!=null)
						  numCommessa=or.getCommessa().getNumeroCommessa()+"."+or.getCommessa().getEstensione();
					  if(or.getDataInizio()!=null)	 	
						  dataInizio=formatter.format(or.getDataInizio());		  
					  if(or.getDataFine()!=null)
						  dataFine=formatter.format(or.getDataFine());
					  tariffa=or.getTariffaOraria();
					  numRisorse=or.getNumRisorse();
					  oreDisp=or.getOreBudget();
					  oreRes=or.getOreResidueBudget();					  
				  }
					  
				  if(r.getOffertas().iterator().hasNext()){
					  of=r.getOffertas().iterator().next();
					  numOfferta=of.getCodiceOfferta();
					  if(of.getDataRedazione()!=null)
						  dataOfferte=formatter.format(of.getDataRedazione());
					  importo=of.getImporto();
				  }
					  			  
				  rdoM=new RdoCompletaModel(r.getNumeroRda()/*id*/, numRdo, r.getCliente().getRagioneSociale(), 
						  numOfferta, dataOfferte, descrizione, importo, numOrdine, numCommessa, dataInizio, dataFine, tariffa, numRisorse, oreDisp, oreRes);
			  
				  listaM.add(rdoM);
				  numRdo="#";
				  numOfferta="#";
				  dataOfferte="#";
				  importo="0.0";
				  numOrdine="#";
				  descrizione="#";
				  numCommessa= "#";
				  dataInizio="#";
				  dataFine="#";
				  tariffa="0.0";
				  numRisorse="0";
				  oreDisp="0.0";
				  oreRes="0.0";
			  }  
			  
			  tx.commit();
			  return listaM;
			  
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    	return null;
		    }finally{
		    	session.close();
		    }		
	}
	
	
	@Override
	public boolean saveRdoCompleta(String numRdo, String cliente,
			String numOfferta, Date dataOfferta, String importo,
			String numOrdine, String descrizione, Date dataInizio,
			Date dataFine, String tariffa, String numRisorse, String oreDisp,
			String oreRes) throws IllegalArgumentException {
		
		Rda r=new Rda();
		Cliente c= new Cliente();
		
		int id;
				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		
		try {
			//non potendo fare riferimento al numero Rdo come identificatore univoco, prelevo l'ultimo id e 
			tx=session.beginTransaction();
			c = (Cliente)session.createQuery("from Cliente where ragioneSociale= :ragSociale").setParameter("ragSociale", cliente).uniqueResult();	
			
			//Per le query native usare i nomi reali delle tabelle
			if(session.createSQLQuery("SELECT MAX(NUMERO_RDA) from rda ").uniqueResult()!=null)
				id=(int)session.createSQLQuery("SELECT MAX(NUMERO_RDA) from rda ").uniqueResult();	
			else id=1;
			
			r.setCliente(c);
			r.setCodiceRDA(numRdo);
			r.setNumeroRda(id+1);
			
			c.getRdas().add(r);
			
			session.save(c);
			
			tx.commit();
			
			insertDataOfferta(numOfferta, id+1, dataOfferta, descrizione, importo);
			insertDataOrdine(numOrdine, id+1, dataInizio, dataFine, descrizione, tariffa, numRisorse, oreDisp, oreRes);
			
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();		
			return false;
		}finally{
			session.close();
		}	
	}

	
	private void insertDataOrdine(String numOrdine, int id,
			Date dataInizio, Date dataFine, String descrizione, String tariffa,
			String numRisorse, String oreDisp, String oreRes) {
		
		Rda r=new Rda();
		Ordine o= new Ordine();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {			
			tx=session.beginTransaction();		
			r = (Rda)session.createQuery("from Rda where numero_rda= :numRda").setParameter("numRda", id).uniqueResult(); 
			
			o.setRda(r);
			
			o.setCodiceOrdine(numOrdine);	//numero effettivo dell'ordine
			o.setDescrizioneAttivita(descrizione);
			o.setTariffaOraria(tariffa);
			o.setDataInizio(dataInizio);
			o.setDataFine(dataFine);
			o.setNumRisorse(numRisorse);
			o.setOreBudget(oreDisp);
			o.setOreResidueBudget(oreRes);
			
			r.getOrdines().add(o);
		    
		    session.save(r);
		    tx.commit();		  
		    		
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			
		}			
	}


	private void insertDataOfferta(String numOfferta, int i, Date dataOfferta,
			String descrizione, String importo) {
		Rda r=new Rda();
		Offerta o= new Offerta();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
			r = (Rda)session.createQuery("from Rda where numero_rda= :numRda").setParameter("numRda", i).uniqueResult(); 
					
			o.setRda(r);
			o.setCodiceOfferta(numOfferta);
			o.setDescrizioneTecnica(descrizione);
			o.setImporto(importo);
			o.setDataRedazione(dataOfferta);
			
			r.getOffertas().add(o);
		    
		    session.save(r);
		    tx.commit();
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
		}		
	}
	
	
	@Override
	public boolean editRdoCompleta(int idRdo, String numRdo, String cliente,
			String numOfferta, Date dataOfferta, String importo,
			String numOrdine, String descrizione, Date dataInizio,
			Date dataFine, String tariffa, String numRisorse, String oreDisp,
			String oreRes) throws IllegalArgumentException {
		Rda r=new Rda();
				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
				
			r=(Rda)session.createQuery("from Rda where numero_rda=:idrdo").setParameter("idrdo", idRdo).uniqueResult();
			
			r.setCodiceRDA(numRdo);
			
			tx.commit();
			
			editDataOfferta(numOfferta, idRdo, dataOfferta, descrizione, importo);
			editDataOrdine(numOrdine, idRdo, dataInizio, dataFine, descrizione, tariffa, numRisorse, oreDisp, oreRes);
			
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();		
			return false;
		}finally{
			session.close();
		}
	}
	

	private void editDataOrdine(String numOrdine, int idRdo, Date dataInizio,
			Date dataFine, String descrizione, String tariffa,
			String numRisorse, String oreDisp, String oreRes) {
	
		Ordine o= new Ordine();
	
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
	
		try {			
			tx=session.beginTransaction();		
			o=(Ordine)session.createQuery("from Ordine where numero_rda=:idRdo").setParameter("idRdo", idRdo).uniqueResult();
		
			o.setCodiceOrdine(numOrdine);	//numero effettivo dell'ordine
			o.setDescrizioneAttivita(descrizione);
			o.setTariffaOraria(tariffa);
			o.setDataInizio(dataInizio);
			o.setDataFine(dataFine);
			o.setNumRisorse(numRisorse);
			o.setOreBudget(oreDisp);
			o.setOreResidueBudget(oreRes);
	  
			tx.commit();		  
	    		
		} catch (Exception e) {
			if (tx!=null)
				tx.rollback();		    	
			e.printStackTrace();
		}			
	}


	private void editDataOfferta(String numOfferta, int idRdo,
			Date dataOfferta, String descrizione, String importo) {
		
		Offerta o = new Offerta();

		Session session = MyHibernateUtil.getSessionFactory()
				.getCurrentSession();
		Transaction tx = null;

		try {

			tx = session.beginTransaction();
			o=(Offerta)session.createQuery("from Offerta where numero_rda=:idRdo").setParameter("idRdo", idRdo).uniqueResult();
			o.setCodiceOfferta(numOfferta);
			o.setDescrizioneTecnica(descrizione);
			o.setImporto(importo);
			o.setDataRedazione(dataOfferta);

			tx.commit();

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}
	
	
	@Override
	public boolean deleteRdoCompleta(int idRdo) throws IllegalArgumentException {
		Rda r= new Rda();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
				
		try {
			  tx=	session.beginTransaction();
			  	  
			  r=(Rda)session.createQuery("from Rda where numero_rda =:idrda").setParameter("idrda", idRdo).uniqueResult();
			  for(Offerta o: r.getOffertas()){		  
				  session.delete(o);
			  }
			  
			  for(Ordine or: r.getOrdines()){
				  session.delete(or);
			  }
			  session.delete(r);
			  tx.commit();	  
			  
			  return true;
		     
		   } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();
		    		return false;
		    }finally{
		    	session.close();
		    }
	}


//---------------------------------------------------------------------------------------
	
	
//--------------------Gestione Clienti---------------------------------------------------

	@Override
	public void insertDataCliente(int codCliente, String ragSociale, String codFiscale, String partitaIVA, String codRaggr,	String comune, String provincia, String stato, String indirizzo,
			String cap, String telefono, String fax, String email) throws IllegalArgumentException {
		
		Cliente c = new Cliente();
		
		c.setCodCliente(codCliente);
		c.setRagioneSociale(ragSociale);
		c.setCodFiscale(codFiscale);
		c.setPartitaIVA(partitaIVA);
		c.setCodRaggr(codRaggr);
		c.setCitta(comune);
		c.setProvincia(provincia);
		c.setStato(stato);
		c.setIndirizzo(indirizzo);
		c.setCap(cap);
		c.setTelefono(telefono);
		c.setFax(fax);
		c.setEmail(email);
						
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;		
		
		try {
			tx=	session.beginTransaction();
			session.save(c);
			tx.commit();
		    } catch (Exception e) {
		    	e.printStackTrace();
		    } 
	}


	@Override
	public void editDataCliente(int codCliente, String ragSociale, String codFiscale, String partitaIVA, String codRaggr,	String comune, String provincia, String stato, String indirizzo,
			String cap, String telefono, String fax, String email) throws IllegalArgumentException {
										
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			tx=	session.beginTransaction();
			Cliente c = new Cliente();
			
			c=(Cliente) session.get(Cliente.class, codCliente);
			
			c.setRagioneSociale(ragSociale);
			c.setCodFiscale(codFiscale);
			c.setPartitaIVA(partitaIVA);
			c.setCodRaggr(codRaggr);
			c.setCitta(comune);
			c.setProvincia(provincia);
			c.setStato(stato);
			c.setIndirizzo(indirizzo);
			c.setCap(cap);
			c.setTelefono(telefono);
			c.setFax(fax);
			c.setEmail(email);		
			
			tx.commit();		
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    } 			
	}


	@Override
	public void removeDataCliente(int id) throws IllegalArgumentException {
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		Cliente c= new Cliente();
				
		try {
			  tx=	session.beginTransaction();
			  c=(Cliente)session.createQuery("from Cliente where cod_cliente=:idcliente").setParameter("idcliente", id).uniqueResult();
			  session.delete(c);
			  tx.commit();
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();
		    } 
	}
	
	
	@Override
	public List<ClienteModel> getAllClientiModel() throws IllegalArgumentException {
		
		List<Cliente> listaC = (ArrayList<Cliente>) ConverterUtil.getClienti();  
		List<ClienteModel> listaDTO = new ArrayList<ClienteModel>(listaC!=null ? listaC.size() : 0);  	    
		     try{
					if(listaC!=null){
						for(Cliente c : listaC){	
							listaDTO.add(ConverterUtil.clienteToModelConverter(c));
							}
						}						
					}catch (Exception e) {
						e.printStackTrace();
					} 
			return listaDTO;		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRagioneSociale() throws IllegalArgumentException{
	
		List<String> listaRS= new ArrayList<String>();
		List<Cliente> listaC= new ArrayList<Cliente>();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=	session.beginTransaction();
			  listaC=(List<Cliente>)session.createQuery("from Cliente").list();
			  
			  for(Cliente c:listaC){
				  
				  listaRS.add(c.getRagioneSociale()); //il codice che rappresenta il numero effettivo della Rdo
			  }
			  
			  tx.commit();
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    }
		
		return listaRS;
	}
	
//---------------------------------------------------------------------------------------------------------

	

//----------------------------OFFERTA----------------------------------------------------------------------
	/*
	@Override
	public void insertDataOfferta(String numOfferta, String numRda, Date dataOfferta, String descrizione, String tariffa) throws IllegalArgumentException {
		
		Rda r=new Rda();
		Offerta o= new Offerta();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
			r = (Rda)session.createQuery("from Rda where codiceRDA= :numRda").setParameter("numRda", numRda).uniqueResult(); //prelevo l'id dell'Rda conoscendo il numero dell'Rda che è comunque univoco	
					
			o.setRda(r);
			o.setCodiceOfferta(numOfferta);
			o.setDescrizioneTecnica(descrizione);
			o.setImporto(tariffa);
			o.setDataRedazione(dataOfferta);
			
			r.getOffertas().add(o);
		    
		    session.save(r);
		    tx.commit();
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
		}
	}


	@Override
	public void editDataOfferta(int idofferta, String numOfferta, String numRda, Date dataOfferta, String descrizione, String tariffa)throws IllegalArgumentException {
		
		Offerta o=new Offerta();
		Rda r=new Rda();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {	
			tx=session.beginTransaction();
			o = (Offerta)session.get(Offerta.class, idofferta);	
			if(o.getRda().getCodiceRDA().compareTo(numRda)!=0){
				r=(Rda)session.createQuery("from Rda where codiceRDA=:numRda").setParameter("numRda", numRda).uniqueResult();
				o.setRda(r);
				r.getOffertas().add(o);
			}
			
			o.setCodiceOfferta(numOfferta);
			o.setDescrizioneTecnica(descrizione);
			o.setImporto(tariffa);
			o.setDataRedazione(dataOfferta);	    
			
		    tx.commit();		
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
		}
	}


	@Override
	public void deleteDataOfferta(int idOfferta)  throws IllegalArgumentException{
		
		Offerta o= new Offerta();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
				
		try {
			  tx=	session.beginTransaction();
			  o=(Offerta)session.createQuery("from Offerta where numero_offerta =:idofferta").setParameter("idofferta", idOfferta).uniqueResult();
			  session.delete(o);
			  tx.commit();
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();
		    }		
	}
	
	@Override
	public Set<Offerta> getAllOfferte() throws IllegalArgumentException{
		
		Set<Offerta> listaO = ConverterUtil.getOfferte(); 
		return listaO;
		
	}
	
	@Override
	public List<OffertaModel> getAllOfferteModel() throws IllegalArgumentException {
		
		Set<Offerta> listaO = ConverterUtil.getOfferte();  
		List<OffertaModel> listaDTO = new ArrayList<OffertaModel>(listaO!=null ? listaO.size() : 0);  
		    
		     try{
					if(listaO!=null){
					
						for(Offerta o : listaO){	
							listaDTO.add(ConverterUtil.offerteToModelConverter(o));
							}
						}
							
					}catch (Exception e) {
						e.printStackTrace();
					} 
		     
			return listaDTO;		
	}
	
	
//---------------------------------------------------------------------------------------------------------
	

//----------------------------ORDINE-----------------------------------------------------------------------

	@Override
	public boolean insertDataOrdine(String numOrdine, String numRda, String numeroCommessa, String estensione,
			Date dataInizio, Date dataFine, String descrizione,
			String tariffaOraria, String numeroRisorse, String numeroOre)
			throws IllegalArgumentException {

		Rda r=new Rda();
		Ordine o= new Ordine();
		Commessa c= new Commessa();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {			
			tx=session.beginTransaction();		
			if(numeroCommessa.compareTo("")!=0){//se è stata inserita una commessa la cerca
				
				r = (Rda)session.createQuery("from Rda where codiceRDA=:numRda").setParameter("numRda", numRda).uniqueResult(); //prelevo l'id dell'Rda conoscendo il numero dell'Rda che è comunque univoco		
				c=(Commessa) session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione")
						.setParameter("numeroCommessa", numeroCommessa).setParameter("estensione", estensione).uniqueResult();
				
				o.setCommessa(c);
				o.setCodiceOrdine(numOrdine);	//numero effettivo dell'ordine
				o.setDescrizioneAttivita(descrizione);
				o.setTariffaOraria(tariffaOraria);
				o.setDataInizio(dataInizio);
				o.setDataFine(dataFine);
				o.setNumRisorse(numeroRisorse);
				o.setOreBudget(numeroOre);
				o.setOreResidueBudget(numeroOre);
				c.getOrdines().add(o);
				    	
				if(r!=null){
					o.setRda(r);
					r.getOrdines().add(o);
					session.save(r);
				}else{
					
				}			
		    	session.save(c);
		    	tx.commit();		
				
		    }else{
		    	r = (Rda)session.createQuery("from Rda where codiceRDA=:numRda").setParameter("numRda", numRda).uniqueResult(); //prelevo l'id dell'Rda conoscendo il numero dell'Rda che è comunque univoco		
				
				o.setCodiceOrdine(numOrdine);	//numero effettivo dell'ordine
				o.setDescrizioneAttivita(descrizione);
				o.setTariffaOraria(tariffaOraria);
				o.setDataInizio(dataInizio);
				o.setDataFine(dataFine);
				o.setNumRisorse(numeroRisorse);
				o.setOreBudget(numeroOre);
				o.setOreResidueBudget(numeroOre);
				if(r!=null){
					o.setRda(r);
					r.getOrdines().add(o);
					session.save(r);
				}else{
					session.save(o);
				}
		    	tx.commit();		    	
		    }    
		    return true;		
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			return false;
		}	
	}


	@Override
	public boolean editDataOrdine(int idordine, String numOrdine, String numRda, String numeroCommessa, String estensione,
			Date dataInizio, Date dataFine, String descrizione,
			String tariffaOraria, String numeroRisorse, String numeroOre)
			throws IllegalArgumentException {

		Ordine o=new Ordine();
		Commessa c= new Commessa();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
				
			tx=session.beginTransaction();
			o = (Ordine)session.get(Ordine.class, idordine);	
			o.setCodiceOrdine(numOrdine);
			o.setDescrizioneAttivita(descrizione);
			o.setTariffaOraria(tariffaOraria);
			o.setDataInizio(dataInizio);
			o.setDataFine(dataFine);
			o.setNumRisorse(numeroRisorse);
			o.setOreBudget(numeroOre);
			o.setOreResidueBudget(numeroOre);
		    if(numeroCommessa.compareTo("")!=0){    	
		    	c=(Commessa) session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", numeroCommessa).setParameter("estensione", estensione).uniqueResult();
		    	o.setCommessa(c);    
		    }		   
		    
		    if(numRda.compareTo("")!=0){//se è stato inserito un numero di Rda in fase di edit
		    	Rda r=new Rda();
		    	r=(Rda)session.createQuery("from Rda where codiceRDA=:numRda").setParameter("numRda", numRda).uniqueResult();
		    	r.setCodiceRDA(numRda);
		    	o.setRda(r);
				r.getOrdines().add(o);
				//session.save(r);
		    }
		    tx.commit();
		    
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();		
			return false;
		}
		
	}

	
	@Override
	public boolean deleteDataOrdine(int idOrdine) throws IllegalArgumentException {

		Ordine o= new Ordine();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
				
		try {
			  tx=	session.beginTransaction();
			  o=(Ordine)session.createQuery("from Ordine where numero_ordine =:idordine").setParameter("idordine", idOrdine).uniqueResult();
			  session.delete(o);
			  tx.commit();
			  
			  return true;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();    		
		    		return false;
		    }
	}
	

	@Override
	public List<OrdineModel> getAllOrdineModel() throws IllegalArgumentException {

		Set<Ordine> listaO = ConverterUtil.getOrdini();  
		List<OrdineModel> listaDTO = new ArrayList<OrdineModel>(listaO!=null ? listaO.size() : 0);  
		    
		try {
			if (listaO != null) {

				for (Ordine o : listaO) {
					listaDTO.add(ConverterUtil.ordiniToModelConverter(o));
				}
			}

			return listaDTO;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	*/
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllListaOrdini() throws IllegalArgumentException {
		
		List<String> listaOrdini= new ArrayList<String>();
		List<Ordine> lista=new ArrayList<Ordine>();
				
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=	session.beginTransaction();
			  lista=(List<Ordine>)session.createQuery("from Ordine").list();
			  
			  for(Ordine o:lista){
				  if(o.getCommessa()==null && o.getCodiceOrdine().compareTo("#")!=0) //se il codice ordine è # significa che è stata creata la entry nel db ma
					  																 //non c'è un ordine reale
				  listaOrdini.add(o.getCodiceOrdine()); //il codice che rappresenta il numero effettivo dell'Ordine
			  }
			  
			  tx.commit();
			  return listaOrdini;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    	return null;
		    }
	}

//------------------------------------COMMESSA--------------------------------------------------------------

	@Override
	public List<CommessaModel> getAllCommesseModel() throws IllegalArgumentException {

		Set<Commessa> listaC = ConverterUtil.getCommesse();
		List<CommessaModel> listaDTO = new ArrayList<CommessaModel>(listaC != null ? listaC.size() : 0);
		Ordine o= new Ordine();

		try {
			if (listaC != null) {

				for (Commessa c : listaC) {
								
						o=getOrdineByCommessa(c.getCodCommessa());
					    listaDTO.add(ConverterUtil.commesseToModelConverter(c,o));					    
				
				}
			}

			return listaDTO;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<CommessaModel> getAllCommesseModelByPm(String cognomePm) {
		Set<Commessa> listaC = ConverterUtil.getCommesse();
		List<CommessaModel> listaDTO = new ArrayList<CommessaModel>(listaC != null ? listaC.size() : 0);
		Ordine o= new Ordine();
		String cognome= new String();
		cognomePm=cognomePm.substring(0,1).toUpperCase()+cognomePm.substring(1,cognomePm.length());
		try {
			if (listaC != null) {

				for (Commessa c : listaC) {
						
					if(c.getMatricolaPM().compareTo("Tutti")==0){
						o=getOrdineByCommessa(c.getCodCommessa());
						listaDTO.add(ConverterUtil.commesseToModelConverter(c,o));
					}
					else{
						cognome=c.getMatricolaPM().substring(0,c.getMatricolaPM().indexOf(" "));
						if(cognome.compareTo(cognomePm)==0){
							o=getOrdineByCommessa(c.getCodCommessa());
							listaDTO.add(ConverterUtil.commesseToModelConverter(c,o));					    
						}
					}				
				}
			}

			return listaDTO;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	private Ordine getOrdineByCommessa(int idCommessa) {
		
		Ordine o = new Ordine();
		Commessa c= new Commessa();

		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		
		try {
			  tx=	session.beginTransaction();
			  c=(Commessa)session.createQuery("from Commessa where cod_commessa =:idcommessa").setParameter("idcommessa", idCommessa).uniqueResult();
			  
			  if(c.getOrdines().iterator().hasNext())
				  o=c.getOrdines().iterator().next();
			  else o=null;
			  
			  tx.commit();
			  
			  return o;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();
		    		
		    		return null;
		    }
		}

	
	//Verifica che il record non sia già presente
	private Commessa verificaPresenza(String numCommessa, String estensione) {
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		Commessa registrata= new Commessa();
		
		try {
			tx=session.beginTransaction();
			registrata=(Commessa)session.createQuery("from Commessa where numerocommessa =:numCommessa and estensione=:estensione").setParameter("numCommessa", numCommessa)
											.setParameter("estensione", estensione).uniqueResult();
			tx.commit();
			
			return registrata;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	
	@Override
	public boolean insertDataCommessa(String numCommessa, String estensione, String tipoCommessa, String pM, String statoCommessa, 
			/*Date dataInizio,*/String oreLavoro, String oreLavoroResidue, String tariffaSal, String salAttuale, String pclAttuale,
			String descrizione, String note) {
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		Commessa c= new Commessa();
		Commessa registrata= new Commessa();
		
		c.setNumeroCommessa(numCommessa);
		c.setEstensione(estensione);
		c.setTipoCommessa(tipoCommessa);
		c.setMatricolaPM(pM);//nel formato Cognome Nome
		c.setStatoCommessa(statoCommessa);
		c.setOreLavoro(Float.valueOf(oreLavoro));
		c.setResiduoOreLavoro(Float.valueOf(oreLavoroResidue));
		c.setTariffaSal(tariffaSal);
		c.setSalAttuale(salAttuale);
		c.setPclAttuale(pclAttuale);
		c.setDataElaborazione(new Date());//data in cui viene creata sul sistema una nuova commessa
		c.setDenominazioneAttivita(descrizione);
		c.setNote(note);
		
		registrata= verificaPresenza(numCommessa, estensione);
		
		if (registrata == null) {
			try {

				tx = session.beginTransaction();
				session.save(c);
				tx.commit();
				return true;

			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			} finally{
				session.close();
			}

		} else
			return false;
	}


	@Override
	public boolean editDataCommessa(int id, String numCommessa, String estensione,
			String tipoCommessa, String pM, String statoCommessa, String oreLavoro,String oreLavoroResidue,
			/*Date dataInizio,*/ String tariffaSal, String salAttuale, String pclAttuale, String descrizione, String note)
			throws IllegalArgumentException {
		
		Commessa c= new Commessa();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		
		
		try {
						
			tx=session.beginTransaction();
			c = (Commessa)session.get(Commessa.class, id);
			
			c.setNumeroCommessa(numCommessa);
			c.setEstensione(estensione);
			c.setTipoCommessa(tipoCommessa);
			c.setMatricolaPM(pM);
			c.setStatoCommessa(statoCommessa);
			
			if(statoCommessa.compareTo("Conclusa")==0)//se fosse già stat chiusa, la commessa, non si sarebbe potuto editarla, quindi questa è la prima volta che compare lo stato Conclusa
				c.setDataChiusura(new Date());				
			
			c.setOreLavoro(Float.valueOf(oreLavoro));
			c.setResiduoOreLavoro(Float.valueOf(oreLavoroResidue));
			c.setTariffaSal(tariffaSal);
			c.setSalAttuale(salAttuale);
			c.setPclAttuale(pclAttuale);
			//c.setDataElaborazione(dataInizio);
			c.setDenominazioneAttivita(descrizione);
			c.setNote(note);
		    		    
		    tx.commit();
		    
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			
			return false;
		} finally{
			session.close();
		}
		
		
	}


	@Override
	public boolean deleteDataCommessa(int idCommessa)	throws IllegalArgumentException {
		
		Commessa c= new Commessa();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
				
		try {
			  tx=	session.beginTransaction();
			  c=(Commessa)session.createQuery("from Commessa where cod_commessa =:idcommessa").setParameter("idcommessa", idCommessa).uniqueResult();
			  
			  for(Ordine or: c.getOrdines()){
				  or.setCommessa(null);
			  }
			  
			  session.delete(c);
			  tx.commit();
			  
			  return true;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    		e.printStackTrace();    		
		    	return false;
		    }
	}


	//Associazione con creazione nuovo ordine
	@Override
	public boolean associaOrdineCommessa(String idCommessa,
			String numOrdine, String numRda, Date dataInizio, Date dataFine,
			String descrizione, String tariffaOraria, String numeroRisorse,
			String numeroOre) {
		
		Rda r=new Rda();
		Ordine o= new Ordine();
		Commessa c=new Commessa();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
			r = (Rda)session.createQuery("from Rda where codiceRDA= :numRda").setParameter("numRda", numRda).uniqueResult(); //prelevo l'id dell'Rda conoscendo il numero dell'Rda che è comunque univoco	
			c=(Commessa)session.createQuery("from Commessa where cod_commessa=:idCommessa").setParameter("idCommessa", idCommessa).uniqueResult();	
			
			o.setRda(r);
			o.setCommessa(c);
			
			o.setCodiceOrdine(numOrdine);	//numero effettivo dell'ordine
			o.setDescrizioneAttivita(descrizione);
			o.setTariffaOraria(tariffaOraria);
			o.setDataInizio(dataInizio);
			o.setDataFine(dataFine);
			o.setNumRisorse(numeroRisorse);
			o.setOreBudget(numeroOre);
			o.setOreBudget(numeroOre);
			
			r.getOrdines().add(o);
			
			c.getOrdines().add(o);
		    
		    session.save(r);
		    session.save(c);
		    
		    tx.commit();
		    
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			return false;
		}
	}


	//Associazione su un ordine già presente
	@Override
	public boolean associaOrdinePresenteCommessa(String idCommessa,	String numOrdine) {		
		
		Ordine o= new Ordine();
		Commessa c=new Commessa();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			tx=session.beginTransaction();
				
			c=(Commessa)session.createQuery("from Commessa where cod_commessa=:idCommessa").setParameter("idCommessa", idCommessa).uniqueResult();
			o=(Ordine)session.createQuery("from Ordine where codiceOrdine= :numOrdine").setParameter("numOrdine", numOrdine).uniqueResult();
				
			o.setCommessa(c);
			c.getOrdines().add(o);
					
			session.save(c);			
		    tx.commit();
		    
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			return false;
		}
	}


	@Override
	public boolean closeCommessa(int idCommessa) {
		
		Commessa c=new Commessa();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
				
			c=(Commessa)session.createQuery("from Commessa where cod_commessa=:idCommessa").setParameter("idCommessa", idCommessa).uniqueResult();
			
			c.setStatoCommessa("Conclusa");
			c.setDataChiusura(new Date());		
			session.save(c);
			
		    tx.commit();
		    
		    return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();		    	
			e.printStackTrace();
			return false;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCommesse() throws IllegalArgumentException {
		
		List<String> listaCommesse= new ArrayList<String>();
		List<Commessa> lista=new ArrayList<Commessa>();
				
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=	session.beginTransaction();
			  lista=(List<Commessa>)session.createQuery("from Commessa").list();
			  
			  for(Commessa c: lista){	
				  
				  if(c.getOrdines().isEmpty())	//Se una commessa ha un ordine associato, questa non comparirà nella lista
					  listaCommesse.add(c.getNumeroCommessa()+"."+c.getEstensione()); //il codice che rappresenta il numero effettivo della Commessa
				   
			  }		  
			  tx.commit();
			  return listaCommesse;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    	return null;
		    }
	}
	
	//Richiamata se ad accedere è un PM
	@SuppressWarnings("unchecked")
	public List<String> getCommesseByPM(String nome, String cognome) throws IllegalArgumentException {
		
		List<String> listaCommesse= new ArrayList<String>();
		List<Commessa> lista=new ArrayList<Commessa>();
		String app=new String();		
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  cognome=cognome.substring(0,1).toUpperCase()+cognome.substring(1,cognome.length());
			  tx=	session.beginTransaction();
			  lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa!='Conclusa'").list();
			 		  
			  for(Commessa c: lista){
				  if(c.getMatricolaPM().compareTo("Tutti")==0)
					  listaCommesse.add(c.getNumeroCommessa()+"."+c.getEstensione());
				  else{
					  app=c.getMatricolaPM();
					  app=app.substring(0,app.indexOf(" "));//prendo il cognome del pm
					  if((!c.getAttivitas().iterator().hasNext())&&(app.compareTo(cognome)==0))
						  listaCommesse.add(c.getNumeroCommessa()+"."+c.getEstensione());
				  }
			  }
			  tx.commit();
			  return listaCommesse;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    	return null;
		    }
	}
	
	//Richiamata se ad accedere è AMM o DIR
	@SuppressWarnings("unchecked")
	public List<String> getCommesseAperte() throws IllegalArgumentException {
		
		List<String> listaCommesse= new ArrayList<String>();
		List<Commessa> lista=new ArrayList<Commessa>();
		//String app=new String();		
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=session.beginTransaction();
			  lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa!='Conclusa'").list();
			 		  
			  for(Commessa c: lista){
				  //app=c.getMatricolaPM();
				  
				  if((!c.getAttivitas().iterator().hasNext()))
				  listaCommesse.add(c.getNumeroCommessa()+"."+c.getEstensione()); 
			  }
			  tx.commit();
			  return listaCommesse;
		     
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    	return null;
		    }
	}

	
//----------------------------- ASSOCIAZIONI COMMESSE ATTIVITA PERSONALE---------------------------------------------------------

	
/* Implementazione temporanea che non tiene conto di un numero di attività >1
*/
	@Override
	public boolean createAssociazioneCommessaDipendenti(String pm, String commessa, List<String> listaDipendenti)	throws IllegalArgumentException {
		
		String app=new String();
		String numeroCommessa= new String();
		String estensione=new String();
				
		app=commessa;
		int index = app.indexOf('.');
		numeroCommessa = app.substring(0, index);
		estensione=app.substring(index+1,app.length());
		
		createAttivita(numeroCommessa, estensione);
		
		for(String dipendente:listaDipendenti){
			
			createAssociazionePtoA(numeroCommessa, estensione, dipendente);
		
		}
		
		return true;
	}


	private void createAttivita(String numeroCommessa, String estensione) {
		
		Attivita a= new Attivita();
		Commessa c= new Commessa();
		Date data= new Date();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		try {
			
			tx=session.beginTransaction();
			
			c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", numeroCommessa)
					 .setParameter("estensione", estensione).uniqueResult();
			a.setDataInizio(data);
			a.setDescrizioneAttivita(c.getDenominazioneAttivita());
			a.setCommessa(c);
			c.getAttivitas().add(a);		
			
			session.save(c);
			
			tx.commit();			
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}	
	
	private void createAssociazionePtoA(String numeroCommessa, String estensione, String dipendente) {
		
		Commessa c= new Commessa();
		Attivita a= new Attivita();
		Personale p= new Personale();
		AssociazionePtoA ptoa= new AssociazionePtoA();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				tx=session.beginTransaction();
			
				c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", numeroCommessa)
					 .setParameter("estensione", estensione).uniqueResult();
				a=c.getAttivitas().iterator().next();//perchè ne è prevista solo una ora!
				p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", dipendente).uniqueResult();
				
				ptoa.setAttivita(a);
				ptoa.setPersonale(p);
				
				a.getAssociazionePtoas().add(ptoa);
				p.getAssociazionePtoas().add(ptoa);
				
				session.save(c);
				tx.commit();
				
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}finally{
			session.close();
		}		
	}
	
	@Override
	public boolean editAssociazioneCommessaDipendenti(String pm, String commessa, List<String> listaDipendenti)	throws IllegalArgumentException {
		
		String app=new String();
		String numeroCommessa= new String();
		String estensione=new String();
		
		try {
			
			app=commessa;
			int index = app.indexOf('.');
			numeroCommessa = app.substring(0, index);
			estensione=app.substring(index+1,app.length());
			
			for(String dipendente:listaDipendenti){
				
				createAssociazionePtoA(numeroCommessa, estensione, dipendente);
			
			}	
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
		
	@Override//MODEL
	public List<PersonaleAssociatoModel> getAssociazioniPersonaleCommessa()throws IllegalArgumentException {
						
		Set<AssociazionePtoA> listaC = ConverterUtil.getAssociazioniPtoA();
		List<PersonaleAssociatoModel> listaDTO = new ArrayList<PersonaleAssociatoModel>();
		
		try {
			if (listaC != null) {

				for (AssociazionePtoA a : listaC) {								
					    listaDTO.addAll(ConverterUtil.associazionePtoAToModelConverter(a));					    			
				}
			}

			return listaDTO;

		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public List<PersonaleAssociatoModel> getAssociazioniPersonaleCommessaByPM(String cognome)throws IllegalArgumentException {
		
		Set<AssociazionePtoA> listaC = ConverterUtil.getAssociazioniPtoA();
		List<PersonaleAssociatoModel> listaDTO = new ArrayList<PersonaleAssociatoModel>();
		String cognomeApp= new String();
		
		try {
			
			cognome=cognome.substring(0,1).toUpperCase()+cognome.substring(1,cognome.length());
			if (listaC != null) {
				for (AssociazionePtoA a : listaC) {//si differenzia con Tutti
						cognomeApp=a.getAttivita().getCommessa().getMatricolaPM();
						if(cognomeApp.compareTo("Tutti")==0){
								listaDTO.addAll(ConverterUtil.associazionePtoAToModelConverter(a));
						}else{
							cognomeApp=cognomeApp.substring(0, cognomeApp.indexOf(" "));
							//essendo nel formato Cognome Nome allora dal primo carattere fino allo spazio avremo il cognome 
							if(cognomeApp.compareTo(cognome)==0)
								listaDTO.addAll(ConverterUtil.associazionePtoAToModelConverter(a));
						}
				}
			}
			return listaDTO;

		} catch (Exception e) {
			
			e.printStackTrace();
			return null;
		}
	}
	

	@Override
	public boolean deleteAssociazioneCommessaDipendenti(int idAssociazionePa, String commessa, String nome, String cognome) throws IllegalArgumentException {
		
		AssociazionePtoA ptoa= new AssociazionePtoA();
		Attivita a= new Attivita();
		
		if(!isLast(idAssociazionePa)){
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		
			try {
				tx = session.beginTransaction();
				ptoa = (AssociazionePtoA) session.get(AssociazionePtoA.class,
						idAssociazionePa);

				session.delete(ptoa);
				tx.commit();
				return true;

			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			} finally {
				session.close();
			}
			
		} else {

			Session session = MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx = null;

			try {
				tx = session.beginTransaction();
				ptoa = (AssociazionePtoA) session.get(AssociazionePtoA.class,
						idAssociazionePa);
				a=(Attivita)session.get(Attivita.class, ptoa.getAttivita().getIdAttivita());
				session.delete(a);
				tx.commit();
				return true;

			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			} finally {
				session.close();
			}
		}
	}
	
	//Se è l'ultima associazione presente nell'attività viene eliminata l'attività stessa
	private boolean isLast(int idAssociazionePa) {
		
		AssociazionePtoA ptoa = new AssociazionePtoA();
		Attivita a= new Attivita();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
				
		try {
			tx=	session.beginTransaction();
			ptoa=(AssociazionePtoA)session.get(AssociazionePtoA.class, idAssociazionePa);
			
			a=(Attivita)session.get(Attivita.class, ptoa.getAttivita().getIdAttivita());
			
			if(a.getAssociazionePtoas().size()>1)
				{
				tx.commit();
				return false;
				}
			else{
				tx.commit();
				return true;
			}		
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		} finally{
			session.close();
		}
		
		return false;
	}
	
//-------------------------------------------------FOGLIO ORE----------------------------------------------

	
	@Override
	public boolean insertFoglioOreGiorno(String username, Date giornoRiferimento,
			String totOreGenerale, String delta, String oreViaggio,
			String oreAssRecupero, String deltaOreViaggio,
			String giustificativo, String oreStraordinario, String oreFerie, String orePermesso, 
			String revisione, List<String> intervalliIU,
			List<IntervalliCommesseModel> intervalliC, String oreRecuperoTot, String noteAggiuntive)
			throws IllegalArgumentException {
		
		Personale p= new Personale();
		FoglioOreMese foglioOre=new FoglioOreMese();
		DettaglioOreGiornaliere dettOreGiornaliero= new DettaglioOreGiornaliere();
		
		Set<DettaglioOreGiornaliere> listaDettOreGiorno= new HashSet<DettaglioOreGiornaliere>();
		
		String data=new String();
		String mese=new String();
		String anno= new String();
		String tipoLavoratore= new String();
		
		int idDettGiorno;
		boolean intervalliIUpresenti;
		boolean intervalliCommPresenti;
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    formatter = new SimpleDateFormat("dd");
	    
		data=(mese+anno);//sostituito la ricerca su mese con quella su data
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				tx=session.beginTransaction();
				
				
				p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
				
				foglioOre=(FoglioOreMese) session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese").setParameter("id", p.getId_PERSONALE())
						.setParameter("mese", data).uniqueResult();
				
				tipoLavoratore=p.getTipologiaLavoratore();
				
				if(tipoLavoratore.compareTo("C")==0){
					//se è un collaboratore calcolo il totale generale sulla base delle ore negli intervalli commesse
					String totaleLavoro= "0.00";
					String totaleViaggio= "0.00";
					for(IntervalliCommesseModel d: intervalliC){
						totaleLavoro=ServerUtility.aggiornaTotGenerale(totaleLavoro, d.getOreLavoro());
						totaleViaggio=ServerUtility.aggiornaTotGenerale(totaleViaggio, d.getOreViaggio());
					}
					
					totOreGenerale=totaleLavoro;
					oreViaggio=totaleViaggio;
				}
				
				//se è un nuovo mese, e quindi un nuovo giorno
				if(foglioOre==null){	
					foglioOre=new FoglioOreMese();
					foglioOre.setPersonale(p);
					foglioOre.setMeseRiferimento(data);
					foglioOre.setStatoRevisioneMese("0");
					p.getFoglioOreMeses().add(foglioOre);
					
					session.save(p);
					tx.commit();
					    
					
					createDettaglioGiornaliero(username, giornoRiferimento, totOreGenerale, delta, oreViaggio, 
							oreAssRecupero, deltaOreViaggio, oreStraordinario, oreFerie, orePermesso, giustificativo, noteAggiuntive, revisione);
								
					createDettaglioIntervalliIU(intervalliIU ,username, giornoRiferimento);
					createDettaglioIntervalliCommesse(intervalliC, username, giornoRiferimento);			
				}
				
				else //il mese è già stato creato quindi prelevo il foglio ore del mese
				{				
					listaDettOreGiorno.addAll(foglioOre.getDettaglioOreGiornalieres()); 		
					//controllo che il giorno sia già presente
					if(giornoPresente(listaDettOreGiorno, giornoRiferimento)){
						
						dettOreGiornaliero=(DettaglioOreGiornaliere)session.createQuery("from DettaglioOreGiornaliere where giornoRiferimento=:giorno and id_foglio_ore=:idFoglioOre")
								.setParameter("giorno", giornoRiferimento).setParameter("idFoglioOre", foglioOre.getIdFoglioOre()).uniqueResult();
						
						idDettGiorno=dettOreGiornaliero.getIdDettaglioOreGiornaliere();
						
						dettOreGiornaliero.setTotaleOreGiorno(totOreGenerale);
						dettOreGiornaliero.setDeltaOreGiorno(delta);
						dettOreGiornaliero.setOreAssenzeRecupero(oreAssRecupero);
						dettOreGiornaliero.setOreViaggio(oreViaggio);
						dettOreGiornaliero.setDeltaOreViaggio(deltaOreViaggio);
						dettOreGiornaliero.setOreStraordinario(oreStraordinario);
						dettOreGiornaliero.setOreFerie(oreFerie);
						dettOreGiornaliero.setOrePermesso(orePermesso);
						//dettOreGiornaliero.setOreExtFest(oreExtFest);
						dettOreGiornaliero.setGiustificativo(giustificativo);
						dettOreGiornaliero.setNoteAggiuntive(noteAggiuntive);
						dettOreGiornaliero.setStatoRevisione(revisione);
						
						if(dettOreGiornaliero.getDettaglioIntervalliIUs().isEmpty())
							intervalliIUpresenti=false;
						else intervalliIUpresenti=true;
						
						if(dettOreGiornaliero.getDettaglioIntervalliCommesses().isEmpty())
							intervalliCommPresenti=false;
						else intervalliCommPresenti=true;
						
						tx.commit();
						
						//controllo che gli intervalli di commesse e IU esistano già
						if(intervalliIUpresenti)
							editDettaglioIntervalliIU(intervalliIU, idDettGiorno);
						else
							createDettaglioIntervalliIU(intervalliIU, username, giornoRiferimento);
						
						if(intervalliCommPresenti)
							editDettaglioIntervalliCommesse(intervalliC, idDettGiorno);
						else
							createDettaglioIntervalliCommesse(intervalliC, username, giornoRiferimento);
					}
					else{
						//se il giorno  non è presente lo "creo"
						dettOreGiornaliero.setFoglioOreMese(foglioOre);
						dettOreGiornaliero.setGiornoRiferimento(giornoRiferimento);
						dettOreGiornaliero.setTotaleOreGiorno(totOreGenerale);
						dettOreGiornaliero.setDeltaOreGiorno(delta);
						dettOreGiornaliero.setOreAssenzeRecupero(oreAssRecupero);
						dettOreGiornaliero.setOreViaggio(oreViaggio);
						dettOreGiornaliero.setDeltaOreViaggio(deltaOreViaggio);
						dettOreGiornaliero.setOreStraordinario(oreStraordinario);
						dettOreGiornaliero.setOreFerie(oreFerie);
						dettOreGiornaliero.setOrePermesso(orePermesso);
						//dettOreGiornaliero.setOreExtFest(oreExtFest);
						dettOreGiornaliero.setGiustificativo(giustificativo);
						dettOreGiornaliero.setNoteAggiuntive(noteAggiuntive);
						dettOreGiornaliero.setStatoRevisione(revisione);
						foglioOre.getDettaglioOreGiornalieres().add(dettOreGiornaliero);
																		
						session.save(foglioOre);
						tx.commit();

						createDettaglioIntervalliIU(intervalliIU ,username,giornoRiferimento);
						createDettaglioIntervalliCommesse(intervalliC, username, giornoRiferimento);
					}			
				}			
				return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}		
	}


	private void createDettaglioGiornaliero(String username, Date giornoRiferimento, String totOreGenerale, String delta, String oreViaggio, 
			String oreAssRecupero, String deltaOreViaggio, String oreStraordinario, String oreFerie, String orePermesso, String giustificativo, String noteAggiuntive, String revisione) {
		
		DettaglioOreGiornaliere dettOreGiornaliero= new DettaglioOreGiornaliere();
		Personale p= new Personale();
		FoglioOreMese foglioOre= new FoglioOreMese();
		
		String data=new String();
		String mese=new String();
		String anno= new String();
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
		
		data=(mese+anno);//sostituito mese con data
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese")
					.setParameter("id", p.getId_PERSONALE()).setParameter("mese", data).uniqueResult();
			
			dettOreGiornaliero.setFoglioOreMese(foglioOre);
			dettOreGiornaliero.setGiornoRiferimento(giornoRiferimento);
			dettOreGiornaliero.setTotaleOreGiorno(totOreGenerale);
			dettOreGiornaliero.setDeltaOreGiorno(delta);
			dettOreGiornaliero.setOreAssenzeRecupero(oreAssRecupero);
			dettOreGiornaliero.setOreStraordinario(oreStraordinario);
			dettOreGiornaliero.setOreFerie(oreFerie);
			dettOreGiornaliero.setOrePermesso(orePermesso);
			dettOreGiornaliero.setOreViaggio(oreViaggio);
			dettOreGiornaliero.setDeltaOreViaggio(deltaOreViaggio);
			dettOreGiornaliero.setGiustificativo(giustificativo);
			dettOreGiornaliero.setNoteAggiuntive(noteAggiuntive);
			dettOreGiornaliero.setStatoRevisione(revisione);
			foglioOre.getDettaglioOreGiornalieres().add(dettOreGiornaliero);
		
			session.save(foglioOre);
			tx.commit();
				
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}
		
	}


	private boolean giornoPresente(Set<DettaglioOreGiornaliere> listaDettOreGiorno, Date giornoRiferimento) {
		Boolean presente=false;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);
		String formattedDate = formatter.format(giornoRiferimento);
		
		for(DettaglioOreGiornaliere d:listaDettOreGiorno){
			if(d.getGiornoRiferimento().toString().equals(formattedDate)){		
				presente=true;
				break;
			}									
		}		
		return presente;
	}


	private void createDettaglioIntervalliCommesse(List<IntervalliCommesseModel> intervalliC, String username,	Date giorno) {
		
		Personale p= new Personale();
		FoglioOreMese foglioOre= new FoglioOreMese();
		DettaglioOreGiornaliere dettGiorno= new DettaglioOreGiornaliere();
		
		String data=new String();
		String mese=new String();
		String anno=new String();
		
		int id;
		int idDettGiorno;
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giorno);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giorno);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    formatter = new SimpleDateFormat("dd");
		
		data=(mese+anno);//sostituito mese con data per ricerca 
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			id=p.getId_PERSONALE();
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese").setParameter("id", id)
					.setParameter("mese", data).uniqueResult();
		
			dettGiorno=(DettaglioOreGiornaliere)session.createQuery("from DettaglioOreGiornaliere where id_foglio_ore=:idfoglioOre and giornoRiferimento=:giorno")
					.setParameter("idfoglioOre", foglioOre.getIdFoglioOre()).setParameter("giorno", giorno).uniqueResult();
			idDettGiorno=dettGiorno.getIdDettaglioOreGiornaliere();
			
			tx.commit();
			
			if(dettGiorno!=null){
				
				for(IntervalliCommesseModel i:intervalliC){				
					createIntervalloCommessa(i, idDettGiorno);	
				}	
			}		
				
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}
	}


	private void createIntervalloCommessa(IntervalliCommesseModel i, int idDettGiorno) {
		
		DettaglioIntervalliCommesse dettCommesse=new DettaglioIntervalliCommesse();
		DettaglioOreGiornaliere dettGiorno= new DettaglioOreGiornaliere();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		String commessa=new String();
		String numeroC= new String();
		String estensione= new String();
		int index;
		
		try {
			
			tx=session.beginTransaction();
			
			dettGiorno=(DettaglioOreGiornaliere)session.createQuery("from DettaglioOreGiornaliere where id_dettaglio_ore_giornaliere=:id")
					.setParameter("id", idDettGiorno).uniqueResult();
			
			commessa=i.getNumeroCommessa();
			index=commessa.indexOf(".");
			numeroC=commessa.substring(0,index);
			estensione=commessa.substring(index+1,commessa.length());
			
			dettCommesse.setNumeroCommessa(numeroC);
			dettCommesse.setEstensioneCommessa(estensione);
			dettCommesse.setOreLavorate(i.getOreLavoro());
			dettCommesse.setOreViaggio(i.getOreViaggio());
			
			dettCommesse.setDettaglioOreGiornaliere(dettGiorno);
			
			dettGiorno.getDettaglioIntervalliCommesses().add(dettCommesse);
			
			session.save(dettGiorno);
			tx.commit();
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}				
	}


	private void createDettaglioIntervalliIU(List<String> intervalliIU, String username, Date giornoRiferimento) {
		
		Personale p= new Personale();
		FoglioOreMese foglioOre= new FoglioOreMese();
		DettaglioOreGiornaliere dettGiorno= new DettaglioOreGiornaliere(); 
		
		String data=new String();
		String mese=new String();
		String intervallo=new String();
		String movimento= new String();
		String sorgente= new String();
		String anno= new String();
		
		int id;
		int idDett;

		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
		
		data=(mese+anno);
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();	
			id=p.getId_PERSONALE();
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese").setParameter("id", id).setParameter("mese", data).uniqueResult();
			
			dettGiorno=(DettaglioOreGiornaliere)session.createQuery("from DettaglioOreGiornaliere where id_foglio_ore=:idfoglioOre and giornoRiferimento=:giorno")
					.setParameter("idfoglioOre", foglioOre.getIdFoglioOre()).setParameter("giorno", giornoRiferimento).uniqueResult();
			idDett=dettGiorno.getIdDettaglioOreGiornaliere();
			
			tx.commit();
			/*
			for(int i=0; i<=10; i++){
				//serve mettere il controllo che se il valore è "" allora non lo creo
				intervallo=intervalliIU.get(i*2);//0,4,8,12,16
				movimento="i"+String.valueOf(i+1);
				if(intervallo.compareTo("")!=0)//
					createIntervalloIU(idDett, intervallo, movimento, "M" );
				
				intervallo=intervalliIU.get(i*2+1);//1,3,5,7,9
				movimento="u"+String.valueOf(i+1);
				if(intervallo.compareTo("")!=0)//
					createIntervalloIU(idDett, intervallo, movimento, "M");			
			}	
			*/
			for(int i=0; i<=4; i++){
				//serve mettere il controllo che se il valore è "" allora non lo creo
				intervallo=intervalliIU.get(i*4);//0,4,8,12,16
				movimento="i"+String.valueOf(i+1);//1,2,3,4,5
				sorgente=intervalliIU.get(i*4+1);//1,5,9,13,17
				if(intervallo.compareTo("")!=0)
					createIntervalloIU(idDett, intervallo, movimento, sorgente);
				
				intervallo=intervalliIU.get(i*4+2);//2,6,10,14,18
				movimento="u"+String.valueOf(i+1);//1,2,3,4,5
				sorgente=intervalliIU.get(i*4+3);//3,7,11,15,19
				if(intervallo.compareTo("")!=0)
					createIntervalloIU(idDett, intervallo, movimento, sorgente);			
			}	
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}	
	}

	
	private void createIntervalloIU(int idDett, String intervallo, String movimento, String sorgente) {
		
		DettaglioOreGiornaliere dettGiorno= new DettaglioOreGiornaliere();
		DettaglioIntervalliIU dettIU=new DettaglioIntervalliIU();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {		
			tx=session.beginTransaction();			
			dettGiorno=(DettaglioOreGiornaliere)session.createQuery("from DettaglioOreGiornaliere where id_dettaglio_ore_giornaliere=:id")
					.setParameter("id", idDett).uniqueResult();
			
			dettIU.setOrario(intervallo);
			dettIU.setMovimento(movimento);
			dettIU.setSorgente(sorgente);
			dettIU.setDettaglioOreGiornaliere(dettGiorno);
			dettIU.setSostituito("N");
			
			dettGiorno.getDettaglioIntervalliIUs().add(dettIU);
			
			session.save(dettGiorno);
			
			tx.commit();			
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}	
	}
	
	
	private void editDettaglioIntervalliCommesse(List<IntervalliCommesseModel> intervalliC, int idDettGiorno) {
		
		try {		
			for(IntervalliCommesseModel i:intervalliC){
					editIntervalloCommessa(i, idDettGiorno);	
				}					
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}


	private void editIntervalloCommessa(IntervalliCommesseModel i, int idDettGiorno) {
		
		DettaglioIntervalliCommesse dettCommesse=new DettaglioIntervalliCommesse();
		DettaglioOreGiornaliere d= new DettaglioOreGiornaliere();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		String commessa= i.getNumeroCommessa();
		String numCommessa= new String();
		String estensione= new String();
		
		numCommessa= commessa.substring(0, commessa.indexOf("."));
		estensione= commessa.substring(commessa.indexOf(".")+1, commessa.length());
			
		try {			
			tx=session.beginTransaction();	
			dettCommesse=(DettaglioIntervalliCommesse)session.createQuery("from DettaglioIntervalliCommesse where id_dettaglio_ore=:id and numeroCommessa=:numeroCommessa and estensioneCommessa=:estensione")
					.setParameter("id", idDettGiorno).setParameter("numeroCommessa", numCommessa)
					.setParameter("estensione", estensione).uniqueResult();
			
			if(dettCommesse==null){
				d=(DettaglioOreGiornaliere)session.createQuery("from DettaglioOreGiornaliere where idDettaglioOreGiornaliere=:id").setParameter("id", idDettGiorno).uniqueResult();
				dettCommesse=new DettaglioIntervalliCommesse();
				dettCommesse.setNumeroCommessa(numCommessa);
				dettCommesse.setEstensioneCommessa(estensione);
				dettCommesse.setOreLavorate(i.getOreLavoro());
				dettCommesse.setOreViaggio(i.getOreViaggio());
				
				dettCommesse.setDettaglioOreGiornaliere(d);
				
				d.getDettaglioIntervalliCommesses().add(dettCommesse);
				
				session.save(d);
				tx.commit();
			}else{
				dettCommesse.setOreLavorate(i.getOreLavoro());
				dettCommesse.setOreViaggio(i.getOreViaggio());	
				tx.commit();	
			}

					
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}					
	}


	private void editDettaglioIntervalliIU(List<String> intervalliIU, int idDettGiorno) {
		
		String intervallo= new String();
		String movimento= new String();
		String sorgente;
		
		try {		
			
			for(int i=0; i<=4; i++){
				//serve mettere il controllo che se il valore è "" allora non lo creo
				intervallo=intervalliIU.get(i*4);//0,4,8,12,16
				movimento="i"+String.valueOf(i+1);//1,2,3,4,5
				sorgente=intervalliIU.get(i*4+1);//1,5,9,13,17
				//if(intervallo.compareTo("")!=0)
					editIntervalloIU(idDettGiorno, intervallo, movimento, sorgente);
				
				intervallo=intervalliIU.get(i*4+2);//2,6,10,14,18
				movimento="u"+String.valueOf(i+1);//1,2,3,4,5
				sorgente=intervalliIU.get(i*4+3);//3,7,11,15,19
				//if(intervallo.compareTo("")!=0)
					editIntervalloIU(idDettGiorno, intervallo, movimento, sorgente);			
			}	
				
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	private void editIntervalloIU(int idDettGiorno, String intervallo, String movimento, String sorgente) {
		
		DettaglioIntervalliIU dettIU=new DettaglioIntervalliIU();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			dettIU=(DettaglioIntervalliIU)session.createQuery("from DettaglioIntervalliIU where id_dett_ore_giorno=:id and movimento=:movimento")
					.setParameter("id", idDettGiorno).setParameter("movimento", movimento).uniqueResult();
			//Se sul db è null, ma io c'ho qualcosa ne creo uno nuovo, altrimenti se sul db c'è ma ho passato il vuoto allora lo elimino
			
			if(dettIU==null){//non era stato creato precedentemente ma c'è per la modifica//
				if(intervallo.compareTo("")!=0){//se l'intervallo non è vuoto//
					tx.commit();
					createIntervalloIU(idDettGiorno, intervallo, movimento, sorgente);				
				}//
			}//
			else{//
				if(intervallo.compareTo("")!=0){//l'intervallo è stato inserito e allora lo modifico
					dettIU.setOrario(intervallo);
					dettIU.setMovimento(movimento);
					dettIU.setSorgente(sorgente);
					dettIU.setSostituito("N");
					
					tx.commit();
				}
				else{//lo devo eliminare perchè è stato modificato e tolto
					session.delete(dettIU);
					tx.commit();
				}
				
			}//	
					
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
		}	
	}

	//LOAD intervalli commesse-------
	@Override
	public List<IntervalliCommesseModel> getAssociazioniPersonaleCommessaByUsername(String username, Date giornoRiferimento)throws IllegalArgumentException{
		
		List<IntervalliCommesseModel> listaDTO = new ArrayList<IntervalliCommesseModel>();			
		IntervalliCommesseModel intervallo;
		
		Set<AssociazionePtoA> listaC = ConverterUtil.getAssociazioniPtoAByUsername(username);		
		
		try {
			if (listaC != null) {			
				for (AssociazionePtoA a : listaC) {
						intervallo=ConverterUtil.intervalliCommesseToModelConverter(a, giornoRiferimento);
						if(intervallo!=null)
							listaDTO.add(intervallo);					    			
				}
			}		
			return listaDTO;

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<IntervalliCommesseModel>();
		}  			
	}
	
	@Override
	public List<IntervalliIUModel> loadIntervalliIU(String username, Date giornoRiferimento)	throws IllegalArgumentException {
		
		Personale p= new Personale();
		FoglioOreMese foglioOre= new FoglioOreMese();
						
		IntervalliIUModel intervallo;
		
		List<DettaglioIntervalliIU> listaIntIU=new ArrayList<DettaglioIntervalliIU>(); 
		List<IntervalliIUModel> listaIntervalli= new ArrayList<IntervalliIUModel>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		
		String data=new String();
		String mese=new String();
		String anno= new String();
				
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
		
		data=(mese+anno);//Sostituito mese con data
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and  meseRiferimento=:mese")
						.setParameter("id", p.getId_PERSONALE()).setParameter("mese", data).uniqueResult();
			
			if(foglioOre!=null)
				if(!foglioOre.getDettaglioOreGiornalieres().isEmpty())
					listaGiorni.addAll(foglioOre.getDettaglioOreGiornalieres());			
				else return listaIntervalli;
			else return listaIntervalli;
			
			for(DettaglioOreGiornaliere d:listaGiorni){
				
				formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);
				String formattedDate = formatter.format(giornoRiferimento);
					
				if(d.getGiornoRiferimento().toString().equals(formattedDate))//seleziono il giorno desiderato
				{
					listaIntIU.addAll(d.getDettaglioIntervalliIUs());//prelevo gli intervalli che sono stati creati
					int size=listaIntIU.size();
					for(int i=0;i<size;i++){
					
						intervallo=new IntervalliIUModel(listaIntIU.get(i).getMovimento(), listaIntIU.get(i).getOrario(), listaIntIU.get(i).getSorgente(), listaIntIU.get(i).getSostituito());
						listaIntervalli.add(intervallo);
					}
				}
			}
			
			tx.commit();	
			return listaIntervalli;
				
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;
			
		}finally{
			session.close();
		}		
	}


	@Override
	public GiustificativiModel loadGiustificativi(String username, Date giornoRiferimento)throws IllegalArgumentException {
		
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		
		String orePreviste=new String();
		
		Personale p= new Personale();
		FoglioOreMese foglioOre= new FoglioOreMese();
		GiustificativiModel giust=new GiustificativiModel();	
		
		String data=new String();
		String mese=new String();
		String anno= new String();
				
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
		
		data=(mese+anno);//sostituito mese con data 
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			orePreviste=p.getTipologiaOrario();
			if(orePreviste.compareTo("A")==0)
				orePreviste="8";
			
			giust=new GiustificativiModel(orePreviste, "0.00", ("-"+orePreviste+".00"), "0.00", "0.00", "0.00", "", "0", "0.00", "0.00", "0.00", "");
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese")
						.setParameter("id", p.getId_PERSONALE()).setParameter("mese", data).uniqueResult();
			
			if(foglioOre!=null)
				if(!foglioOre.getDettaglioOreGiornalieres().isEmpty())
					listaGiorni.addAll(foglioOre.getDettaglioOreGiornalieres());			
				else return giust;
			else return giust;
			
			for(DettaglioOreGiornaliere d:listaGiorni){
				formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);
				String formattedDate = formatter.format(giornoRiferimento);
				
				if(formattedDate.equals(d.getGiornoRiferimento().toString()))
				{
					giust=new GiustificativiModel(orePreviste, d.getTotaleOreGiorno(), d.getDeltaOreGiorno(), d.getOreViaggio(), d.getDeltaOreViaggio()
							, d.getOreAssenzeRecupero(), d.getGiustificativo(), d.getStatoRevisione(), d.getOreStraordinario(), d.getOreFerie(), d.getOrePermesso(), d.getNoteAggiuntive());	
				}
			}
				
			tx.commit();
			return giust;
				
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;			
		}finally{
			session.close();
		}
	}


	@Override
	public RiepilogoOreModel loadRiepilogoMese(String username, Date giornoRiferimento)throws IllegalArgumentException {	
		RiepilogoOreModel riepilogo= new RiepilogoOreModel();
		Personale p =new Personale();
		FoglioOreMese foglioOre= new FoglioOreMese();
		List<FoglioOreMese> listaMesi= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		
		String data=new String();
		String mese=new String();
		String anno= new String();
		
		String totOre= "0.0";
		String oreAssenza= "0.0";
		String oreStraordinario= "0.0";
		String oreRecupero= "0.0";
		String monteOreRecuperoTotale=new String();
		int orePrevisteMese=0;		
		String  tipoOrario= new String();
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    
		data=(mese+anno);//sostituito mese con data 
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {		
			tx=session.beginTransaction();		
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			monteOreRecuperoTotale=p.getOreRecupero();
			if(p.getTipologiaOrario().compareTo("A")==0)
				tipoOrario="8";
			else tipoOrario=p.getTipologiaOrario();
			
			orePrevisteMese=ServerUtility.calcolaOreLavorativeMese(giornoRiferimento, tipoOrario);
			riepilogo=new RiepilogoOreModel("0.0", String.valueOf(orePrevisteMese), "0.0", "0.0", monteOreRecuperoTotale); //non essendoci giorni registrati le ore previste sono le ore di assenza
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese")
						.setParameter("id", p.getId_PERSONALE()).setParameter("mese", data).uniqueResult();
			
			if(foglioOre!=null)//controllo sul mese corrente
				if(!foglioOre.getDettaglioOreGiornalieres().isEmpty()){
					
					listaGiorni.addAll(foglioOre.getDettaglioOreGiornalieres());
					
					for(DettaglioOreGiornaliere d:listaGiorni){
						//Se ci sono ore di Viaggio mi calcolo il totale effettivo
						if(d.getOreViaggio().compareTo("0.00")!=0){
							if(Float.valueOf(d.getDeltaOreGiorno())<0){
								if(Float.valueOf(d.getOreViaggio())>Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){
									//il delta viaggio è positivo e setto il giustificativo a ore viaggio
									totOre=ServerUtility.aggiornaTotGenerale(totOre, p.getTipologiaOrario()+".00");
									
								}								
								if(Float.valueOf(d.getOreViaggio())<=Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){
									String oreTotGenerale=ServerUtility.aggiornaTotGenerale(d.getTotaleOreGiorno(), d.getOreViaggio());			
									totOre=ServerUtility.aggiornaTotGenerale(totOre, oreTotGenerale);
								}
							}else					
							if(Float.valueOf(d.getDeltaOreGiorno())>=0){			
								totOre=ServerUtility.aggiornaTotGenerale(totOre,d.getTotaleOreGiorno());
							}
							
						}else{						
						totOre=ServerUtility.aggiornaTotGenerale(totOre,d.getTotaleOreGiorno());
						}
						oreRecupero=ServerUtility.aggiornaTotGenerale(oreRecupero, d.getOreAssenzeRecupero());
						oreStraordinario=ServerUtility.aggiornaOreStraordinario(oreStraordinario, d.getOreStraordinario());
					}	
				}					
				else return riepilogo;
			else return riepilogo;				
			
			//Calcolo il monte ore Totale a recupero che deve considerare tutti i mesi e non solo il corrente
			if(!p.getFoglioOreMeses().isEmpty()){
				listaMesi.addAll(p.getFoglioOreMeses());
				for(FoglioOreMese f:listaMesi){
					if(f.getMeseRiferimento().compareTo("Feb2013")!=0){//per omettere le ore inserite nel mese di prova di Feb2013
						listaGiorni.clear();
						if(!f.getDettaglioOreGiornalieres().isEmpty()){
							listaGiorni.addAll(f.getDettaglioOreGiornalieres());
							for(DettaglioOreGiornaliere d:listaGiorni){
								monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(d.getOreAssenzeRecupero(), monteOreRecuperoTotale);	
							}		
						}
					}
									
				}		
			}
			
			tx.commit();
			//calcolo ore assenza in base al totale calcolato
			oreAssenza=ServerUtility.calcolaOreAssenza(totOre, orePrevisteMese);		
			
			riepilogo= new RiepilogoOreModel(String.valueOf(totOre), oreAssenza, String.valueOf(oreStraordinario), 
					String.valueOf(oreRecupero), monteOreRecuperoTotale);
					
			return riepilogo;
				
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;
			
		}finally{
			session.close();
		}	
	}


	@Override
	public List<RiepilogoFoglioOreModel> getRiepilogoMeseFoglioOre(String username, Date dataRif) {
		
		List<RiepilogoFoglioOreModel> listaGiorni= new ArrayList<RiepilogoFoglioOreModel>();
		
		List<DettaglioOreGiornaliere> listaDettGiorno= new ArrayList<DettaglioOreGiornaliere>();
		List<FoglioOreMese> listaMesi=new ArrayList<FoglioOreMese>();
		RiepilogoFoglioOreModel giorno;
		Personale p= new Personale();
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		String anno=formatter.format(dataRif);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		String mese=formatter.format(dataRif);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    
		String data=(mese+anno);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			if(!p.getFoglioOreMeses().isEmpty()){
				listaMesi.addAll(p.getFoglioOreMeses());
				for(FoglioOreMese f:listaMesi){
					if(f.getMeseRiferimento().compareTo(data)==0)
						listaDettGiorno.addAll(f.getDettaglioOreGiornalieres());
				}
				
				
				//precompilo la lista di giorni con tutti i giorni lavorativi del mese in modo tale da segnalare una eventuale mancata compilazione
				for(int i=1; i<=ServerUtility.getGiorniMese(mese, anno);i++){
					String g= new String();	
					String dataCompLow=new String();
					String dataCompUpp=new String();
					String giornoW= new String();
					String meseApp= new String();
					Date d= new Date();
					
					meseApp=(mese.substring(0,1).toLowerCase()+mese.substring(1,3));
					
					if(i<10)
						g="0"+String.valueOf(i);
					else
						g=String.valueOf(i);
					
					dataCompLow=(g+"-"+meseApp+"-"+anno);
					dataCompUpp=(anno+"-"+mese+"-"+g);
					
					formatter=new SimpleDateFormat("dd-MMM-yyyy");
					d=formatter.parse(dataCompLow);
					giornoW=d.toString().substring(0,3);
					if(giornoW.compareTo("Sun")!=0 && !ServerUtility.isFestivo(dataCompUpp)){				
						giorno= new RiepilogoFoglioOreModel(0, data, dataCompLow, "", Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"),
								Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"),Float.valueOf("0.00"), "", "", false);
						listaGiorni.add(giorno);					
					}
				}
							
				//considero la lista di giorni eventualmente compilati
				for(DettaglioOreGiornaliere d:listaDettGiorno){
					String day=new String();
					String oreTotali= "0.00";
					
					formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
					day=formatter.format(d.getGiornoRiferimento());
					
					if(d.getOreViaggio().compareTo("0.00")!=0){
						if(Float.valueOf(d.getDeltaOreGiorno())<0){
							if(Float.valueOf(d.getOreViaggio())>Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){							
								oreTotali=(p.getTipologiaOrario()+".00");					
							}								
							if(Float.valueOf(d.getOreViaggio())<=Math.abs(Float.valueOf(d.getDeltaOreGiorno()))){
								String oreTotGenerale=ServerUtility.aggiornaTotGenerale(d.getTotaleOreGiorno(), d.getOreViaggio());			
								oreTotali=(oreTotGenerale);
							}
						}else					
						if(Float.valueOf(d.getDeltaOreGiorno())>=0){			
							oreTotali=d.getTotaleOreGiorno();
						}
						
					}else{						
						oreTotali=(d.getTotaleOreGiorno());
					}
					
					Date gRiferimento=d.getGiornoRiferimento();
					formatter = new SimpleDateFormat("dd/MM/yy",Locale.ITALIAN);
					String giornoR=formatter.format(gRiferimento);
					@SuppressWarnings("unchecked")
					List<DettaglioTimbrature> listaT=(List<DettaglioTimbrature>)session.createQuery("from DettaglioTimbrature where numeroBadge=:nBadge and giorno=:giorno")
							.setParameter("nBadge", p.getNumeroBadge()).setParameter("giorno", giornoR).list();
								
					giorno= new RiepilogoFoglioOreModel(d.getIdDettaglioOreGiornaliere(), data, day, p.getTipologiaOrario(), Float.valueOf(d.getTotaleOreGiorno()),  Float.valueOf(d.getOreViaggio()), 
							 Float.valueOf(d.getDeltaOreViaggio()),  Float.valueOf(oreTotali),  Float.valueOf(d.getOreFerie()),  Float.valueOf(d.getOrePermesso()) ,  Float.valueOf(d.getOreAssenzeRecupero()),
							 Float.valueOf(d.getOreStraordinario()), d.getGiustificativo(), d.getNoteAggiuntive(), true);
					
					Iterator<RiepilogoFoglioOreModel> itr = listaGiorni.iterator();
					while(itr.hasNext()) {
				         RiepilogoFoglioOreModel g = itr.next();
				         String dd=g.get("giorno");
							if(dd.compareTo((String)giorno.get("giorno"))==0){
								int index=listaGiorni.indexOf(g);
								listaGiorni.set(index, giorno);							
							}
				    }								
				}				
				
				//Calcolo il monte ore Totale a recupero che deve considerare tutti i mesi e non solo il corrente
				String monteOreRecuperoTotale= "0.00";
				List<DettaglioOreGiornaliere> listaGiorniM= new ArrayList<DettaglioOreGiornaliere>();
				listaMesi.clear();
				if(!p.getFoglioOreMeses().isEmpty()){
					listaMesi.addAll(p.getFoglioOreMeses());
					for(FoglioOreMese f:listaMesi){
						if(f.getMeseRiferimento().compareTo("Feb2013")!=0 && f.getMeseRiferimento().compareTo(data)!=0){//per omettere le ore inserite nel mese di prova di Feb2013 e quelle relative al mese in corso
							listaGiorniM.clear();
							if(!f.getDettaglioOreGiornalieres().isEmpty()){
								listaGiorniM.addAll(f.getDettaglioOreGiornalieres());
								for(DettaglioOreGiornaliere d:listaGiorniM){
									monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(d.getOreAssenzeRecupero(), monteOreRecuperoTotale);	
								
								}		
							}
						}								
					}		
				}
				
				monteOreRecuperoTotale=ServerUtility.aggiornaMonteOreRecuperoTotale(monteOreRecuperoTotale, p.getOreRecupero());
				//aggiunta di un campo nella tabella fogliooremese che indiche le ore a recupero residue ai mesi precedenti
				giorno=new RiepilogoFoglioOreModel(0, data, "RESIDUI", "", Float.valueOf(0),Float.valueOf(0) , Float.valueOf(0), Float.valueOf(0), 
						Float.valueOf(0), Float.valueOf(0), Float.valueOf(monteOreRecuperoTotale), Float.valueOf(0), "", "", true);
				listaGiorni.add(giorno);
				
				tx.commit();
				return listaGiorni;				
			}
			else {
				tx.commit();
				return listaGiorni; //sarà vuota
			}		
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}			
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> loadIntervalliToolTip(String username, Date giorno) {
		Personale p= new Personale();
		List<DettaglioTimbrature> listaD= new ArrayList<DettaglioTimbrature>();
		List<String> listaIntervalliTimbr=new ArrayList<String>();
		String numeroBadge= new String();
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yy",Locale.ITALIAN) ; 
		String data=formatter.format(giorno);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			numeroBadge=p.getNumeroBadge();
			if(numeroBadge.compareTo("")!=0 && numeroBadge.compareTo("null")!=0){
				
				//usare la distinct		
				/*listaD=session.createSQLQuery("select distinct movimento as {DettaglioTimbrature.movimento}, orario as {DettaglioTimbrature.orario} " +
						"from dettaglio_timbrature {DettaglioTimbrature} where numeroBadge=:badge and giorno=:giorno")
						.addEntity("DettaglioTimbrature",DettaglioTimbrature.class)
						.setParameter("badge", numeroBadge).setParameter("giorno", data).list();*/
				
				listaD=session.createSQLQuery("select distinct movimento, orario  " +
						"from dettaglio_timbrature d where d.numeroBadge=:badge and d.giorno=:giorno order by orario")
						.setParameter("badge", numeroBadge).setParameter("giorno", data).list();
				
				for(ListIterator iter = listaD.listIterator(); iter.hasNext(); ) {
					 Object[] row = (Object[]) iter.next();
					 
					 listaIntervalliTimbr.add((String) row[0]);
					 listaIntervalliTimbr.add((String) row[1]);
		         }
				 /*
				for(DettaglioTimbrature d:listaD){
					listaIntervalliTimbr.add(d.getMovimento());
					listaIntervalliTimbr.add(d.getOrario());
				}*/
				
			}
			tx.commit();
			return listaIntervalliTimbr;
		
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}

	
//RIEPILOGHI ORE COMMESSE DIPENDENTI--------------------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoOreDipCommesse> getRiepilogoOreDipCommesse(String mese, String pm) {
		List<RiepilogoOreDipCommesse> listaR= new ArrayList<RiepilogoOreDipCommesse>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();
		
		List<AssociazionePtoA> listaAssociazioni= new ArrayList<AssociazionePtoA>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalli= new ArrayList<DettaglioIntervalliCommesse>();
		//List<RiepilogoOreDipCommesse> listaR= new ArrayList<RiepilogoOreDipCommesse>();
		RiepilogoOreDipCommesse riep= new RiepilogoOreDipCommesse();
		
		String numeroCommessa= new String();
		String commessa= new String();
		String estensione= new String();
		String cliente= new String();
		String dipendente= new String();
		String oreLavoro= new String();
		String oreViaggio= new String();
		String oreTotMeseLavoro= "0.0";
		String oreTotMeseViaggio= "0.0";
		String oreTot="0.0";
		
		String statoCommessa="Aperta";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();

			listaCommesse = (List<Commessa>) session.createQuery("from Commessa where matricolaPM=:pm").setParameter("pm", pm).list();//tolta la clausola and stato=aperta
					
					//.setParameter("statoCommessa", statoCommessa).list(); //seleziono tuttle le commesse aperte per pm indicato

			for (Commessa c : listaCommesse) {
								
				if (c.getAttivitas().size() > 0)
					listaAttivita.add(c.getAttivitas().iterator().next());	
			}

			for (Attivita a : listaAttivita) { // in questo caso la lista  Attivita rappresenta la lista di commesse associate al PM selezionato
												// ottengo tutte le associazioni e quindi tutti i dipendenti associati a quella commessa
				listaAssociazioni.addAll(a.getAssociazionePtoas());
				commessa = a.getCommessa().getNumeroCommessa();
				estensione = a.getCommessa().getEstensione();
				
				if(a.getCommessa().getOrdines().size()>0)
					cliente=a.getCommessa().getOrdines().iterator().next().getRda().getCliente().getRagioneSociale();
				else cliente="#";
				
				numeroCommessa =(commessa + "." + estensione+" ("+cliente+")"); //una stringa più dettagliata che descriva la commessa
				//numeroCommessa =(commessa+"("+cliente+")");
				for (AssociazionePtoA ass : listaAssociazioni) { // per tutte le associazioni della  commessa considerata prelevo i dipendenti
					listaP.add(ass.getPersonale());
					}

					for (Personale p : listaP) { // per ogni dipendente in questa commessa selezioni i fogli ore del mese desiderato
						dipendente = p.getCognome() + " " + p.getNome();

						for (FoglioOreMese f : p.getFoglioOreMeses()) {
							if (f.getMeseRiferimento().compareTo(mese) == 0) { //se è il mese cercato
								listaGiorni.addAll(f.getDettaglioOreGiornalieres()); //prendo tutti i giorni

								for (DettaglioOreGiornaliere giorno : listaGiorni) { 
									listaIntervalli.addAll(giorno.getDettaglioIntervalliCommesses());

									for (DettaglioIntervalliCommesse d : listaIntervalli) { //se c'è un intervallo per la commessa selezionata ricavo le ore
										if (commessa.compareTo(d.getNumeroCommessa()) == 0 && estensione.compareTo(d.getEstensioneCommessa()) == 0) {
											oreLavoro = d.getOreLavorate();
											oreViaggio = d.getOreViaggio();
											
											oreTotMeseLavoro = ServerUtility.aggiornaTotGenerale(oreTotMeseLavoro,oreLavoro);
											oreTotMeseViaggio = ServerUtility.aggiornaTotGenerale(oreTotMeseViaggio,oreViaggio);
										}
										
										oreLavoro="0.0";
										oreViaggio="0.0";
									}
									listaIntervalli.clear();
								}
								listaGiorni.clear();
								oreTot=ServerUtility.aggiornaTotGenerale(oreTotMeseLavoro, oreTotMeseViaggio);
								riep = new RiepilogoOreDipCommesse(numeroCommessa, dipendente, Float.valueOf(oreTotMeseLavoro), Float.valueOf(oreTotMeseViaggio), Float.valueOf(oreTot));
								listaR.add(riep);
								oreTotMeseLavoro="0.0";
								oreTotMeseViaggio="0.0";
							}
						}				
					}
					listaP.clear();
					listaAssociazioni.clear();
				}
							
			tx.commit();
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
		return listaR;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoOreTotaliCommesse> getRiepilogoOreTotCommesse(String pm, String data)throws IllegalArgumentException {
		
		List<RiepilogoOreTotaliCommesse> listaR= new ArrayList<RiepilogoOreTotaliCommesse>();
		
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();	
		List<AssociazionePtoA> listaAssociazioni= new ArrayList<AssociazionePtoA>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalli= new ArrayList<DettaglioIntervalliCommesse>();
		RiepilogoOreTotaliCommesse riep= new RiepilogoOreTotaliCommesse();
		
		String numeroCommessa= new String();
		String commessa= new String();
		String estensione= new String();
		String cliente= new String();
		String numeroOrdine= new String();
		String oreLavoro= new String();
		String oreViaggio= new String();
		String oreTotMeseLavoro= "0.0";
		String oreTotMeseViaggio= "0.0";
		String oreBudget= new String();
		String flagCompilato="No";
		
		String statoCommessa="Aperta";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();

			listaCommesse = (List<Commessa>) session.createQuery("from Commessa where matricolaPM=:pm and statoCommessa=:statoCommessa")
					.setParameter("pm", pm) 
					.setParameter("statoCommessa", statoCommessa).list(); //seleziono tutte le commesse aperte per pm indicato
			for (Commessa c : listaCommesse) {//se non ci sono dipendenti associati non verranno prese in considerazione
				if (c.getAttivitas().size() > 0)
					listaAttivita.add(c.getAttivitas().iterator().next());
			}
			for (Attivita a : listaAttivita) { // in questo caso la lista  Attivita rappresenta la lista di commesse associate al PM
												// selezionato ottengo tutte le associazioni e quindi tutti i dipendenti associati a quella commessa
				listaAssociazioni.addAll(a.getAssociazionePtoas());
				commessa = a.getCommessa().getNumeroCommessa();
				estensione = a.getCommessa().getEstensione();
				
				if(a.getCommessa().getOrdines().size()>0){
					cliente=a.getCommessa().getOrdines().iterator().next().getRda().getCliente().getRagioneSociale();
					numeroOrdine=a.getCommessa().getOrdines().iterator().next().getCodiceOrdine();
					oreBudget=a.getCommessa().getOrdines().iterator().next().getOreBudget();
				}
				else {
					cliente="#";
					numeroOrdine="#";
					oreBudget="0.0";
				}
				
				numeroCommessa =commessa; //una stringa più dettagliata che descriva la commessa
				
				for (AssociazionePtoA ass : listaAssociazioni) { // per tutte le associazioni della  commessa considerata prelevo i dipendenti
					listaP.add(ass.getPersonale());
					}
					for (Personale p : listaP) { // per ogni dipendente in questa commessa selezioni i fogli ore del mese desiderato						
						for (FoglioOreMese f : p.getFoglioOreMeses()) {
								listaGiorni.addAll(f.getDettaglioOreGiornalieres()); //prendo tutti i giorni
								for (DettaglioOreGiornaliere giorno : listaGiorni) { 
									listaIntervalli.addAll(giorno.getDettaglioIntervalliCommesses());
									for (DettaglioIntervalliCommesse d : listaIntervalli) { //se c'è un intervallo per la commessa selezionata ricavo le ore
										if (commessa.compareTo(d.getNumeroCommessa()) == 0 && estensione.compareTo(d.getEstensioneCommessa()) == 0) {
											oreLavoro = d.getOreLavorate();
											oreViaggio = d.getOreViaggio();
											oreTotMeseLavoro = ServerUtility.aggiornaTotGenerale(oreTotMeseLavoro,oreLavoro);
											oreTotMeseViaggio = ServerUtility.aggiornaTotGenerale(oreTotMeseViaggio,oreViaggio);
										}
										oreLavoro="0.0";
										oreViaggio="0.0";
									}
									listaIntervalli.clear();
								}
								listaGiorni.clear();			
							}
					}
					listaP.clear();
					
					//check per controllare se il foglio fatturazione nel mese è già stato compilato
					List<FoglioFatturazione> listaFF=new ArrayList<FoglioFatturazione>();
					listaFF.addAll(a.getCommessa().getFoglioFatturaziones());
					if(listaFF.isEmpty())
						flagCompilato="No";
					else
					for(FoglioFatturazione ff:listaFF){
						
						if(ff.getMeseCorrente().compareTo(data)==0)
						{
							flagCompilato="Si";
							break;
						}else
							flagCompilato="No";
					}
					
					riep = new RiepilogoOreTotaliCommesse(numeroCommessa, estensione, numeroOrdine, oreBudget, Float.valueOf(oreTotMeseLavoro), flagCompilato);
					oreTotMeseLavoro="0.0";
					oreTotMeseViaggio="0.0";
					listaR.add(riep);
					listaAssociazioni.clear();	
				}				
			tx.commit();		
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
		return listaR;
	}


//------------------------------------------------------FATTURAZIONE---------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoOreDipFatturazione> getRiepilogoOreDipFatturazione(String mese, String pm) {
		
		List<RiepilogoOreDipFatturazione> listaR= new ArrayList<RiepilogoOreDipFatturazione>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();
		
		List<AssociazionePtoA> listaAssociazioni= new ArrayList<AssociazionePtoA>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalli= new ArrayList<DettaglioIntervalliCommesse>();
		RiepilogoOreDipFatturazione riep= new RiepilogoOreDipFatturazione();
		
		String numeroCommessa= new String();
		String commessa= new String();
		String estensione= new String();
		String cliente= new String();
		String dipendente= new String();
		String oreLavoro= new String();
		String oreViaggio= new String();
		String oreTotMeseLavoro= "0.0";
		String oreTotMeseViaggio= "0.0";
		String oreSommaLavoroViaggio="0.0";
		String oreTotLavoroCommessa="0.0";
		String oreTotViaggioCommessa="0.0";
		String oreTotCommessa="0.0";
		
		
		String statoCommessa="Aperta";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			listaCommesse = (List<Commessa>) session.createQuery("from Commessa where matricolaPM=:pm")
					.setParameter("pm", pm).list() ; //seleziono tuttle le commesse per pm indicato

			for (Commessa c : listaCommesse) {
				if (c.getAttivitas().size() > 0)
					listaAttivita.add(c.getAttivitas().iterator().next());
			}

			for (Attivita a : listaAttivita) {  // in questo caso la lista  Attivita rappresenta la lista di commesse associate al PM
												// selezionato ottengo tutte le associazioni e quindi tutti i dipendenti associati a quella commessa
				listaAssociazioni.addAll(a.getAssociazionePtoas());
				commessa = a.getCommessa().getNumeroCommessa();
				estensione = a.getCommessa().getEstensione();
				
				if(a.getCommessa().getOrdines().size()>0)
					cliente=a.getCommessa().getOrdines().iterator().next().getRda().getCliente().getRagioneSociale();
				else cliente="#";
				
				numeroCommessa =(commessa + "." + estensione+" ("+cliente+")"); //una stringa più dettagliata che descriva la commessa
				
				for (AssociazionePtoA ass : listaAssociazioni) { // per tutte le associazioni della  commessa considerata prelevo i dipendenti
					listaP.add(ass.getPersonale());
					}

					for (Personale p : listaP) { // per ogni dipendente in questa commessa selezioni i fogli ore del mese desiderato
						dipendente = p.getCognome() + " " + p.getNome();

						for (FoglioOreMese f : p.getFoglioOreMeses()) {
							if (f.getMeseRiferimento().compareTo(mese) == 0) { //se è il mese cercato
								listaGiorni.addAll(f.getDettaglioOreGiornalieres()); //prendo tutti i giorni

								for (DettaglioOreGiornaliere giorno : listaGiorni) { 
									listaIntervalli.addAll(giorno.getDettaglioIntervalliCommesses());

									for (DettaglioIntervalliCommesse d : listaIntervalli) { //se c'è un intervallo per la commessa selezionata ricavo le ore
										if (commessa.compareTo(d.getNumeroCommessa()) == 0 && estensione.compareTo(d.getEstensioneCommessa()) == 0) {
											oreLavoro = d.getOreLavorate();
											oreViaggio = d.getOreViaggio();
											oreTotMeseLavoro = ServerUtility.aggiornaTotGenerale(oreTotMeseLavoro,oreLavoro);
											oreTotMeseViaggio = ServerUtility.aggiornaTotGenerale(oreTotMeseViaggio,oreViaggio);
										}
										oreLavoro="0.0";
										oreViaggio="0.0";										
									}
									listaIntervalli.clear();
								}
								listaGiorni.clear();
								oreSommaLavoroViaggio = ServerUtility.aggiornaTotGenerale(oreTotMeseLavoro, oreTotMeseViaggio);
								riep = new RiepilogoOreDipFatturazione(numeroCommessa, dipendente, Float.valueOf(oreTotMeseLavoro), Float.valueOf(oreTotMeseViaggio), Float.valueOf(oreSommaLavoroViaggio));
								listaR.add(riep);
								
								//Per ogni dipendente incremento il totale sulla commessa in esame
								oreTotLavoroCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, oreTotMeseLavoro);
								oreTotViaggioCommessa=ServerUtility.aggiornaTotGenerale(oreTotViaggioCommessa, oreTotMeseViaggio);
								oreTotCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, oreTotViaggioCommessa);
								
								oreTotMeseLavoro="0.0";
								oreTotMeseViaggio="0.0";
								oreSommaLavoroViaggio="0.0";
							}
														
						}//end ciclo su mesi(fatto solo una volta per il mese necessario)
															
					}//end ciclo su lista persone nella stessa commessa
					
					riep = new RiepilogoOreDipFatturazione(numeroCommessa, ".TOTALE", Float.valueOf(oreTotLavoroCommessa), Float.valueOf(oreTotViaggioCommessa), Float.valueOf(oreTotCommessa));
					listaR.add(riep);
					oreTotLavoroCommessa="0.0";
					oreTotViaggioCommessa="0.0";
					oreTotCommessa="0.0";
					
					listaP.clear();
					listaAssociazioni.clear();
				}
							
			tx.commit();
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
		return listaR;
	}


	@SuppressWarnings("unchecked")
	@Override
	public FoglioFatturazioneModel getDatiFatturazionePerOrdine(String numeroCommessa, String mese)throws IllegalArgumentException {
		
		String commessa= new String();
		String estensione= new String();
		String mesePrecedente=new String();
		String salAttuale= new String();
		String pclAttuale= new String();
		String tariffaUtilizzata= new String();
		String sommaVariazioniSal= "0.00";
		String sommaVariazioniPcl= "0.00";
		
		Boolean esistePa= false; //controllo l'esistenza di una eventuale commessa madre .pa
				
		Commessa c= new Commessa();
		Commessa c_pa= new Commessa();
		Ordine o= new Ordine();
		FoglioFatturazione f= new FoglioFatturazione();
		FoglioFatturazioneModel foglioModel= new FoglioFatturazioneModel();	
		List<Commessa> listaC= new ArrayList<Commessa>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		
		
		int codCommessa;
		
		mesePrecedente=ServerUtility.getMesePrecedente(mese);
		commessa=numeroCommessa.substring(0, numeroCommessa.indexOf("."));
		estensione=numeroCommessa.substring(numeroCommessa.indexOf(".")+1, numeroCommessa.length());
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
						
		try {
			tx = session.beginTransaction();
			
			c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione=:estensione").setParameter("commessa", commessa)
					.setParameter("estensione", estensione).uniqueResult();
			codCommessa=c.getCodCommessa(); //id commessa
			o=(Ordine)session.createQuery("from Ordine where cod_commessa=:id").setParameter("id", codCommessa).uniqueResult();
			
			//controllo la presenza di una commessa .pa 
			c_pa=(Commessa)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione=:estensione").setParameter("commessa", commessa)
					.setParameter("estensione", "pa").uniqueResult();
			if(c_pa==null)
				esistePa=false;
				else
					esistePa=true;
						
			if(esistePa){			
				listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione<>:estensione and statoCommessa<>:stato").setParameter("commessa", commessa)
						.setParameter("estensione", "pa").setParameter("stato", "Chiusa").list();
				for(Commessa c1:listaC)
					listaFF.addAll(c1.getFoglioFatturaziones());			
				
				for(FoglioFatturazione f1:listaFF){ 
						sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f1.getVariazionePCL());
						sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f1.getVariazioneSAL());			
				}
			
				sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, c_pa.getSalAttuale());
				sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, c_pa.getPclAttuale());
			
				f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
						.setParameter("mese", mese).uniqueResult();
			
				tx.commit();
				
				if(o==null){//se non c'è un ordine associato, permetto l'inserimento di eventuali sal pcl
					tariffaUtilizzata=c.getTariffaSal();//prendo la tariffa della commessa
					if(f==null){						
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), "0.0", sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.0", "0.0", "0.0", "", "0");
					}
					else{	
						//il foglio fatturazione era già stato compilato quindi tolgo alla sommavariazioni quella in esame
						sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
						sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
							
				}else{	
					tariffaUtilizzata=o.getTariffaOraria();
					if(f==null){					
						
						foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(), Float.valueOf(tariffaUtilizzata), "0.0",
								sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.0", "0.0", "0.0", "", "0");
					}
					else{		
						sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
						sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(),Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
				}	
				
			}else{ //Non esiste la Pa
				
				f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
						.setParameter("mese", mese).uniqueResult();
			
				listaFF.addAll(c.getFoglioFatturaziones());			
				
				for(FoglioFatturazione f1:listaFF){
					if(f1.getMeseCorrente().compareTo(mese)!=0){
						sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f1.getVariazionePCL());
						sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f1.getVariazioneSAL());
					}
				}
			
				sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, c.getSalAttuale());
				sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, c.getPclAttuale());
				
				tx.commit();		
				
				if(o==null){//se non c'è un ordine associato, permetto l'inserimento di eventuali sal pcl
					tariffaUtilizzata=c.getTariffaSal();//prendo la tariffa della commessa
					if(f==null){						
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), "0.0", sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.0", "0.0", "0.0", "", "0");
					}
					else{	
						sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
						sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
							
				}else{	
					tariffaUtilizzata=o.getTariffaOraria();
					if(f==null){					
						
						foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(), Float.valueOf(tariffaUtilizzata), "0.0",
								sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.0", "0.0", "0.0", "", "0");
					}
					else{			
						sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
						sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(),Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
				}	
							
			}
			/*
			
				o=(Ordine)session.createQuery("from Ordine where cod_commessa=:id").setParameter("id", codCommessa).uniqueResult();
				
				f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
						.setParameter("mese", mese).uniqueResult();
			
				tx.commit();		
				
				if(o==null){//se non c'è un ordine associato, permetto l'inserimento di eventuali sal pcl
					tariffaUtilizzata=c.getTariffaSal();//prendo la tariffa della commessa
					if(f==null){						
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), "0.0", salAttuale, pclAttuale, "0.0", "0.0", "0.0", "0.0", "", "0");
					}
					else{			
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
								f.getSALattuale(), f.getPCLattuale(), f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
							
				}else{	
					tariffaUtilizzata=o.getTariffaOraria();
					if(f==null){					
						
						foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(), Float.valueOf(tariffaUtilizzata), "0.0",
								salAttuale, pclAttuale, "0.0", "0.0", "0.0", "0.0", "", "0");
					}
					else{			
						foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(),Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
								f.getSALattuale(), f.getPCLattuale(), f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
				}			
			*/
				/*	
			//else
			o=(Ordine)session.createQuery("from Ordine where cod_commessa=:id").setParameter("id", codCommessa).uniqueResult();
			
			f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
					.setParameter("mese", mese).uniqueResult();
			
			//c'è un controllo sul mese precedente e non su tutti gli eventuali precedenti
			fPrec=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mesePrecedente")
					.setParameter("id", codCommessa).setParameter("mesePrecedente", mesePrecedente).uniqueResult();
			
			tx.commit();		
			
			if(o==null){//se non c'è un ordine associato, permetto l'inserimento di eventuali sal pcl
				tariffaUtilizzata=c.getTariffaSal();//prendo la tariffa della commessa
				if(f==null){						
					if(fPrec!=null || esistePrecedentePa){
										
						//c'è il foglio fatturazione del mese precedente quindi ricavo le ore sal pcl attuali
							pclAttuale=ServerUtility.aggiornaTotGenerale(fPrec.getPCLattuale(), fPrec.getVariazionePCL());
							salAttuale=ServerUtility.aggiornaTotGenerale(fPrec.getSALattuale(), fPrec.getVariazioneSAL());
						
					}else{
						//se non c'è foglio fatturazione e neanche precedente allora prendo sal e pcl iniziali sulla commessa(eventualmente la .PA)					
						
							pclAttuale=c.getPclAttuale();
							salAttuale=c.getSalAttuale();
										
					}
					foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), "0.0", salAttuale, pclAttuale, "0.0", "0.0", "0.0", "0.0", "", "0");
				}
				else{			
					foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
							f.getSALattuale(), f.getPCLattuale(), f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
				}		
						
			}else{	
				tariffaUtilizzata=o.getTariffaOraria();
				if(f==null){					
					if(fPrec!=null){
					//c'è il foglio fatturazione del mese precedente quindi ricavo le ore sal pcl attuali
						pclAttuale=ServerUtility.aggiornaTotGenerale(fPrec.getPCLattuale(), fPrec.getVariazionePCL());
						salAttuale=ServerUtility.aggiornaTotGenerale(fPrec.getSALattuale(), fPrec.getVariazioneSAL());									
					}else{
						//se non c'è foglio fatturazione e neanche precedente allora prendo sal e pcl iniziali
						if(esistePa){
							pclAttuale=c_pa.getPclAttuale();
							salAttuale=c_pa.getSalAttuale();
						}else{
							pclAttuale=c.getPclAttuale();
							salAttuale=c.getSalAttuale();
						}	
					}
					foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(), Float.valueOf(tariffaUtilizzata), "0.0",
							salAttuale, pclAttuale, "0.0", "0.0", "0.0", "0.0", "", "0");
				}
				else{			
					foglioModel= new FoglioFatturazioneModel(o.getCodiceOrdine(), o.getOreBudget(), o.getOreResidueBudget(),Float.valueOf(tariffaUtilizzata), f.getOreEseguite(),
							f.getSALattuale(), f.getPCLattuale(), f.getOreFatturare(), f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
				}		
			}*/
			return foglioModel;
		}
		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}	
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean insertDatiFoglioFatturazione(String oreEseguite,
			String salIniziale, String pclIniziale, String oreFatturare,
			String variazioneSAL, String variazionePCL, String meseCorrente, String note,
			String statoElaborazione, String commessa, String tariffaUtilizzata)
			throws IllegalArgumentException {
	
		Commessa c= new Commessa();
		Commessa c_pa=new Commessa();
		Ordine o = new Ordine();
		FoglioFatturazione f= new FoglioFatturazione();
		
		List<FoglioFatturazione> listaf= new ArrayList<FoglioFatturazione>();
		
		String numeroC= new String();
		String estensione= new String();
		String oreResidueBudget= new String();
		int idCommessa;
		Boolean esistePa=false;
		
		numeroC=commessa.substring(0,commessa.indexOf("."));
		estensione=commessa.substring(commessa.indexOf(".")+1, commessa.length());
		oreResidueBudget="0.0";

		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", numeroC)
					.setParameter("estensione", estensione).uniqueResult();
			idCommessa=c.getCodCommessa();
			
			c_pa=(Commessa)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione=:estensione").setParameter("commessa", numeroC)
					.setParameter("estensione", "pa").uniqueResult();
			if(c_pa==null)
				esistePa=false;
				else
					esistePa=true;
			
			if(c.getOrdines().iterator().hasNext()){//se è presente un ordine
				o=c.getOrdines().iterator().next();
				if(c.getFoglioFatturaziones().size()>0){ //calcolo le ore residue
					listaf.addAll(c.getFoglioFatturaziones());
					oreResidueBudget=o.getOreBudget();
					for(FoglioFatturazione f1:listaf){			
						oreResidueBudget=ServerUtility.getDifference(oreResidueBudget, f1.getOreFatturare());
					}	
					oreResidueBudget=ServerUtility.getDifference(oreResidueBudget, oreFatturare);
					o.setOreResidueBudget(oreResidueBudget);
				}else{
					//se il numero di ore residue è inferiore al numero totale devo prendere in considerazione le residue
					if(Float.valueOf(o.getOreBudget()) > Float.valueOf(oreResidueBudget))
					{
						oreResidueBudget=ServerUtility.getDifference(o.getOreResidueBudget(), oreFatturare);
						o.setOreResidueBudget(oreResidueBudget);
					}else{
						oreResidueBudget=ServerUtility.getDifference(o.getOreBudget(), oreFatturare);
						o.setOreResidueBudget(oreResidueBudget);
					}
				}
			}
			else o=null;
							
			f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese")
					.setParameter("id", idCommessa).setParameter("mese", meseCorrente).uniqueResult();
			
			if(f==null){
				//insert new
				f=new FoglioFatturazione();
				f.setCommessa(c);
				
				f.setOreEseguite(oreEseguite);
				f.setOreFatturare(oreFatturare);
				f.setSALattuale(salIniziale);//ho così indicazione del sal e del pcl prima della variazione del mese registrato
				f.setPCLattuale(pclIniziale);
				f.setVariazionePCL(variazionePCL);
				f.setVariazioneSAL(variazioneSAL);
				f.setMeseCorrente(meseCorrente);
				f.setNote(note);
				f.setStatoElaborazione(statoElaborazione);
				f.setTariffaUtilizzata(tariffaUtilizzata);
			
				c.getFoglioFatturaziones().add(f);
												
				tx.commit();
				return true;
				
			}else{
								
				f.setOreEseguite(oreEseguite);
				f.setOreFatturare(oreFatturare);
				f.setSALattuale(salIniziale); //L'iniziale prima della variazione
				f.setPCLattuale(pclIniziale);
				f.setVariazionePCL(variazionePCL);
				f.setVariazioneSAL(variazioneSAL);
				f.setMeseCorrente(meseCorrente);
				f.setNote(note);
				f.setStatoElaborazione(statoElaborazione);
				f.setTariffaUtilizzata(tariffaUtilizzata);
							
				tx.commit();
				return true;
			}
				
		}	
		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}			
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DatiFatturazioneMeseModel> getReportDatiFatturazioneMese(String mese) {
		
		List<DatiFatturazioneMeseModel> listaDati= new ArrayList<DatiFatturazioneMeseModel>();
		List<FoglioFatturazione> listaFF=new ArrayList<FoglioFatturazione>();
		DatiFatturazioneMeseModel datiModel;
		Ordine o;
		float importo;
		float margine;
		String oreMargine= new String();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
		
			listaFF=(List<FoglioFatturazione>)session.createQuery("from FoglioFatturazione where meseCorrente=:mese").setParameter("mese", mese).list();
			for(FoglioFatturazione f: listaFF){
				
				if(!f.getCommessa().getOrdines().isEmpty()){
					o=f.getCommessa().getOrdines().iterator().next();
					oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
					margine=Float.valueOf(oreMargine);
					importo=Float.valueOf(o.getTariffaOraria())*Float.valueOf(f.getOreFatturare());
					datiModel=new DatiFatturazioneMeseModel(f.getCommessa().getMatricolaPM(), f.getCommessa().getNumeroCommessa()+"."+f.getCommessa().getEstensione(), o.getRda().getCliente().getRagioneSociale(),
							o.getCommessa().getDenominazioneAttivita(), Float.valueOf(f.getOreEseguite()), Float.valueOf(f.getOreFatturare()), Float.valueOf(o.getTariffaOraria()),importo, Float.valueOf(f.getVariazioneSAL()), Float.valueOf(f.getVariazionePCL()), margine);
				}
				else{
					oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
					margine=Float.valueOf(oreMargine);
					
					datiModel=new DatiFatturazioneMeseModel(f.getCommessa().getMatricolaPM(), f.getCommessa().getNumeroCommessa()+"."+f.getCommessa().getEstensione(), "#",
							f.getCommessa().getDenominazioneAttivita(), Float.valueOf(f.getOreEseguite()), Float.valueOf(f.getOreFatturare()), (float)0.0, (float) 0.0, Float.valueOf(f.getVariazioneSAL()), Float.valueOf(f.getVariazionePCL()),margine);	
				}
				listaDati.add(datiModel);
			}			
			
			tx.commit();
			
			return listaDati;
		}catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCommesseRiepilogoDatiFatturazione()throws IllegalArgumentException {
		
		List<String> listaNomiC= new ArrayList<String>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		String statoCommessa= "Aperta";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx=session.beginTransaction();
			
			listaC=(List<Commessa>)session.createQuery("from Commessa where statoCommessa=:stato").setParameter("stato", statoCommessa).list();
			for(Commessa c:listaC){
				if(c.getFoglioFatturaziones().size()>0){
					listaNomiC.add(c.getNumeroCommessa()+"."+c.getEstensione());					
				}
				
			}			
			tx.commit();
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<DatiFatturazioneCommessaModel> getRiepilogoDatiFatturazioneCommessa() throws IllegalArgumentException {
		
		List<DatiFatturazioneCommessaModel> listaDati= new ArrayList<DatiFatturazioneCommessaModel>();
		List<FoglioFatturazione> listaFF=new ArrayList<FoglioFatturazione>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		DatiFatturazioneCommessaModel datiModel;
		Ordine o;
		
		float importo;
		float margine;
		String commessa= new String();
		String oreMargine= new String();
	
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			listaC=(List<Commessa>)session.createQuery("from Commessa where statoCommessa='Aperta'").list();
			for(Commessa c:listaC){
				commessa=c.getNumeroCommessa()+"."+c.getEstensione();
				
				if(c.getFoglioFatturaziones().size()>0){
					listaFF.addAll(c.getFoglioFatturaziones());								
					for(FoglioFatturazione f: listaFF){
						
						if(!f.getCommessa().getOrdines().isEmpty()){
							o=f.getCommessa().getOrdines().iterator().next();
							oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
							margine=Float.valueOf(oreMargine);
							importo=Float.valueOf(o.getTariffaOraria())*Float.valueOf(f.getOreFatturare());
							datiModel=new DatiFatturazioneCommessaModel(commessa, f.getMeseCorrente(), Float.valueOf(f.getOreEseguite()), Float.valueOf(f.getOreFatturare())
									, importo, Float.valueOf(f.getVariazioneSAL()), Float.valueOf(f.getVariazionePCL()), margine);
						}
						else{
							oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
							margine=Float.valueOf(oreMargine);
							
							datiModel=new DatiFatturazioneCommessaModel(commessa, f.getMeseCorrente(), Float.valueOf(f.getOreEseguite()), Float.valueOf(f.getOreFatturare())
									, (float)0.0, Float.valueOf(f.getVariazioneSAL()), Float.valueOf(f.getVariazionePCL()), margine);
						}
						listaDati.add(datiModel);
					}		
					listaFF.clear();				
				}
			}					
			tx.commit();
			return listaDati;
		}catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoOreTotaliCommesse> getElencoCommesseSuFoglioFatturazione(String numCommessa,
			String numEstensione, String data)
			throws IllegalArgumentException {
		
		Commessa c= new Commessa();
		List<Commessa> listaC= new ArrayList<Commessa>();
		List<RiepilogoOreTotaliCommesse> listaRiep= new ArrayList<RiepilogoOreTotaliCommesse>();
		RiepilogoOreTotaliCommesse riep;
		List<FoglioFatturazione> listaFF=new ArrayList<FoglioFatturazione>();
		
		
		String flagCompilato= new String();
		String oreEseguite= "0.00";
		String numeroOrdine="#";
		Float sal=(float) 0.0;
		Float pcl=(float) 0.0;
		numEstensione=numEstensione.toLowerCase();
		
		if(numCommessa.compareTo("")!=0){
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		//prendere il foglio fatturazione del mese indicato, se c'è, e prendere le eventuali ore eseguite
		
		try {
			tx = session.beginTransaction();		
			//prendere tutte le commesse con numero uguale a quella .pa
			if(numEstensione.compareTo("pa")==0){
				listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and statoCommessa=:stato").
						setParameter("numeroCommessa", numCommessa).setParameter("stato", "Aperta").list();
				
				for(Commessa comm:listaC){
					if(comm.getEstensione().toLowerCase().compareTo("pa")!=0){	//se non è la .pa la inserisco in lista
						if(comm.getOrdines().size()>0){
							numeroOrdine=comm.getOrdines().iterator().next().getCodiceOrdine();
						}
						else {
							numeroOrdine="#";
						}
				    
						listaFF.addAll(comm.getFoglioFatturaziones());
						if(listaFF.isEmpty())
							flagCompilato="No";
						else
							for(FoglioFatturazione ff:listaFF){
						
								if(ff.getMeseCorrente().compareTo(data)==0)
								{
									flagCompilato="Si";
									oreEseguite=ff.getOreEseguite();
									sal=Float.valueOf(ff.getVariazioneSAL());
									pcl=Float.valueOf(ff.getVariazionePCL());
									break;
								}else{
									flagCompilato="No";
									sal=Float.valueOf("0.00");
									pcl=Float.valueOf("0.00");
								}
							}
					
					riep= new RiepilogoOreTotaliCommesse(comm.getNumeroCommessa(), comm.getEstensione(),sal,pcl, numeroOrdine, oreEseguite, Float.valueOf("0.00"), flagCompilato);
					listaRiep.add(riep);
				    oreEseguite="0.00";
				    flagCompilato="No";
				    numeroOrdine="#";	
				    listaFF.clear();
				  }
				}
			}
			
			//viene considerata solo la commessa selezionata
			else{
				
			    c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numCommessa and estensione=:numEstensione").
						setParameter("numCommessa", numCommessa).setParameter("numEstensione", numEstensione).uniqueResult();
				
			    if(!c.getOrdines().isEmpty()){
					numeroOrdine=c.getOrdines().iterator().next().getCodiceOrdine();
				}
				else {
					numeroOrdine="#";
				}
			    
				listaFF.addAll(c.getFoglioFatturaziones());
				if(listaFF.isEmpty())
					flagCompilato="No";
				else
				for(FoglioFatturazione ff:listaFF){
					
					if(ff.getMeseCorrente().compareTo(data)==0)
					{
						flagCompilato="Si";
						oreEseguite=ff.getOreEseguite();
						sal=Float.valueOf(ff.getVariazioneSAL());
						pcl=Float.valueOf(ff.getVariazionePCL());
						break;
					}else{
						flagCompilato="No";
						sal=Float.valueOf("0.00");
						pcl=Float.valueOf("0.00");
					}
				}			
				riep= new RiepilogoOreTotaliCommesse(numCommessa, numEstensione,sal,pcl, numeroOrdine, oreEseguite, Float.valueOf("0.00"), flagCompilato);
				listaRiep.add(riep);		
			}
								
			tx.commit();
			
			oreEseguite="0.00";
			String salTotale="0.00";
			String pclTotale="0.00";
			
			for(RiepilogoOreTotaliCommesse r: listaRiep){
				oreEseguite=ServerUtility.aggiornaTotGenerale(oreEseguite, r.getOreOrdine());	
				salTotale=ServerUtility.aggiornaTotGenerale(salTotale, String.valueOf(r.get("sal")));
				pclTotale=ServerUtility.aggiornaTotGenerale(pclTotale, String.valueOf(r.get("pcl")));
			}
			riep= new RiepilogoOreTotaliCommesse("TOTALE", "", Float.valueOf(salTotale), Float.valueOf(pclTotale), "", oreEseguite , Float.valueOf("0.00"), "");
			listaRiep.add(riep);
			
			return listaRiep;
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		
		}else 
			return listaRiep;
	}

	
//----------------------------------------------------------VARIE
	@Override
	public boolean invioCommenti(String testo, String username)
			throws IllegalArgumentException {
		
		Commenti c= new Commenti();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		Date d= new Date();
		try {
			c.setTesto(testo);
			c.setUsername(username);
			c.setDataRichiesta(d.toString());
			
			tx=session.beginTransaction();
			session.save(c);			
			tx.commit();
			return true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}
		
	}

	//non usato----------
	@Override
	public boolean setRiepilogoOreOnSession(List<RiepilogoFoglioOreModel> lista) {
		HttpSession httpSession = getThreadLocalRequest().getSession(true);  
	    httpSession.setAttribute("listaRiepilogo", lista);
		return true;
	}
	

	@Override
	public List<RiepilogoOreDipCommesseGiornaliero> getRiepilogoGiornalieroCommesse(
			String username, Date meseRiferimento) throws IllegalArgumentException {
		
		List<RiepilogoOreDipCommesseGiornaliero> listaG= new ArrayList<RiepilogoOreDipCommesseGiornaliero>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaD= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalliC= new ArrayList<DettaglioIntervalliCommesse>();
		List<AssociazionePtoA> listaAssociazioniPA= new ArrayList<AssociazionePtoA>();
		
		FoglioOreMese fM=new FoglioOreMese();
		Personale p= new Personale();
		Commessa c= new Commessa();
		
		String totaleOreLavoroC= "0.00";
		String totaleOreViaggioC= "0.00";
		String totaleOreC= "0.00";
		
		Date giorno= new Date();  
		String dipendente= new String();
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		String anno=formatter.format(meseRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		String mese=formatter.format(meseRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
		
	    String data=mese+anno;
	    
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
						
			tx=session.beginTransaction();
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			dipendente= p.getCognome()+" "+p.getNome();
			
			listaF.addAll(p.getFoglioOreMeses());
			listaAssociazioniPA.addAll(p.getAssociazionePtoas());
			
			for(FoglioOreMese f:listaF){//scorro i mesi per trovare il foglio ore desiderato
				if(f.getMeseRiferimento().compareTo(data)==0){
					fM=f;
					break;
				}
			}
			
			listaD.addAll(fM.getDettaglioOreGiornalieres()); //prendo tutti i giorni del mese
			
						
			for(DettaglioOreGiornaliere d: listaD ){//scorro i giorni del mese e calcolo il totale ore per ogni commessa selezionata
					giorno= d.getGiornoRiferimento();
					formatter = new SimpleDateFormat("dd-MMM-yyy",Locale.ITALIAN) ; 
					String giornoF=formatter.format(giorno);
					
					if(!d.getDettaglioIntervalliCommesses().isEmpty())
						listaIntervalliC.addAll(d.getDettaglioIntervalliCommesses());
								
					for(DettaglioIntervalliCommesse dett:listaIntervalliC){
						
						if(Float.valueOf(ServerUtility.aggiornaTotGenerale(dett.getOreLavorate(),  dett.getOreViaggio()))>0){
							RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero(dett.getNumeroCommessa()+"."+dett.getEstensioneCommessa()
								, dipendente, giornoF, Float.valueOf(dett.getOreLavorate()), Float.valueOf(dett.getOreViaggio()), Float.valueOf(ServerUtility.aggiornaTotGenerale(dett.getOreLavorate(),  dett.getOreViaggio())));
						
							listaG.add(riep);
													
						}
					}	
					
					listaIntervalliC.clear();							
			}
			
			//elaboro un record per i totali per ogni commessa
			for(AssociazionePtoA ass:listaAssociazioniPA){
				String commessa= ass.getAttivita().getCommessa().getNumeroCommessa() +"."+ ass.getAttivita().getCommessa().getEstensione();
				
				for(RiepilogoOreDipCommesseGiornaliero g:listaG){
					if(g.getNumeroCommessa().compareTo(commessa)==0){
						String oreLavoro=String.valueOf(g.getOreLavoro());
						String oreViaggio=String.valueOf(g.getOreViaggio());
						String oreTotali=String.valueOf(g.getTotOre());
						
						if(oreLavoro.substring(oreLavoro.indexOf(".")+1, oreLavoro.length()).length()==1)
							oreLavoro=oreLavoro+"0";
						if(oreViaggio.substring(oreViaggio.indexOf(".")+1, oreViaggio.length()).length()==1)
							oreViaggio=oreViaggio+"0";
						if(oreTotali.substring(oreTotali.indexOf(".")+1, oreTotali.length()).length()==1)
							oreTotali=oreTotali+"0";
						
						totaleOreLavoroC= ServerUtility.aggiornaTotGenerale(totaleOreLavoroC, oreLavoro);
						totaleOreViaggioC= ServerUtility.aggiornaTotGenerale(totaleOreViaggioC, oreViaggio);
						totaleOreC=ServerUtility.aggiornaTotGenerale(totaleOreC, oreTotali);
					}
				}
				
				RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero(commessa
						, dipendente, "Totale", Float.valueOf(totaleOreLavoroC), Float.valueOf(totaleOreViaggioC), Float.valueOf(totaleOreC));
				
				listaG.add(riep);
				
				totaleOreLavoroC= "0.00";
				totaleOreViaggioC= "0.00";
				totaleOreC= "0.00";
			}
			
			tx.commit();
			
			return listaG;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}

	
}
