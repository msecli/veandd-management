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

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
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

import com.google.gwt.i18n.client.DateTimeFormat;

import gestione.pack.client.AdministrationService;
import gestione.pack.client.model.AnagraficaHardwareModel;
import gestione.pack.client.model.AttivitaFatturateModel;
import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.CommentiModel;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.CostiHwSwModel;
import gestione.pack.client.model.CostingModel;
import gestione.pack.client.model.CostingRisorsaModel;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.DettaglioTrasfertaModel;
import gestione.pack.client.model.FatturaModel;
import gestione.pack.client.model.FoglioFatturazioneModel;
import gestione.pack.client.model.GestioneCostiDipendentiModel;
import gestione.pack.client.model.GestioneRdoCommesse;
import gestione.pack.client.model.GiorniFestiviModel;
import gestione.pack.client.model.GiustificativiModel;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.IntervalliIUModel;
import gestione.pack.client.model.PeriodoSbloccoModel;
import gestione.pack.client.model.PersonaleAssociatoModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RdaModel;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoMensileOrdiniModel;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.model.RiepilogoOreAnnualiDipendente;
import gestione.pack.client.model.RiepilogoOreDipCommesse;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreModel;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;
import gestione.pack.client.model.RiepilogoRichiesteModel;
import gestione.pack.client.model.RiepilogoSALPCLModel;
import gestione.pack.client.model.RiferimentiRtvModel;
import gestione.pack.client.model.RtvModel;
import gestione.pack.client.model.TariffaOrdineModel;
import gestione.pack.shared.AssociazionePtoA;
import gestione.pack.shared.Attivita;
import gestione.pack.shared.Cliente;
import gestione.pack.shared.Commessa;
//import gestione.pack.shared.DatiTimbratriceExt;
import gestione.pack.shared.AnagraficaHardware;
import gestione.pack.shared.AssociazionePtoHw;
import gestione.pack.shared.AssociazionePtohwsw;
import gestione.pack.shared.AttivitaFatturata;
import gestione.pack.shared.AttivitaOrdine;
import gestione.pack.shared.Commenti;
import gestione.pack.shared.CostiHwSw;
import gestione.pack.shared.Costing;
import gestione.pack.shared.CostingRisorsa;
import gestione.pack.shared.CostoAzienda;
import gestione.pack.shared.DatiFatturazioneAzienda;
import gestione.pack.shared.DettaglioIntervalliCommesse;
import gestione.pack.shared.DettaglioIntervalliIU;
import gestione.pack.shared.DettaglioOreGiornaliere;
import gestione.pack.shared.DettaglioTimbrature;
import gestione.pack.shared.DettaglioTrasferta;
import gestione.pack.shared.Fattura;
import gestione.pack.shared.FoglioFatturazione;
import gestione.pack.shared.FoglioOreMese;
import gestione.pack.shared.GiorniFestivi;
import gestione.pack.shared.LogErrori;
import gestione.pack.shared.Offerta;
import gestione.pack.shared.Ordine;
import gestione.pack.shared.PeriodoSbloccoGiorni;
import gestione.pack.shared.Personale;
import gestione.pack.shared.Rda;
import gestione.pack.shared.RichiesteIt;
import gestione.pack.shared.RiferimentiRtv;
import gestione.pack.shared.Rtv;


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
	public boolean insertDataPersonale(String nome, String cognome, String username, String password, String statoRapporto, String nBadge, String ruolo, String tipoOrario, String tipoLavoratore,
			String gruppoLavoro, String costoOrario,  String costoStruttura,String sede, String sedeOperativa,  String oreDirette, String oreIndirette,  String permessi, 
			String ferie, String ext, String oreRecupero)throws IllegalArgumentException {
		
		Boolean esito=true;
		String errore= new String();
		
			Personale p1=new Personale();
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
			p.setStatoRapporto(statoRapporto);
			
			Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
			Transaction tx= null;		
			
			try {
				
					tx=	session.beginTransaction();
				
					if(nBadge.compareTo("")==0)
						p1=(Personale)session.createQuery("from Personale where username=:username")
							.setParameter("username", username).uniqueResult();
					else
						p1=(Personale)session.createQuery("from Personale where numeroBadge=:numeroBadge or username=:username")
							.setParameter("numeroBadge", nBadge).setParameter("username", username).uniqueResult();
					
					if(p1==null){		
						session.save(p);
						tx.commit();
						ServerLogFunction.logOkMessage("insertDataPersonale", new Date(), "", "Success");
						return true;
					}
					else{
						tx.commit();
						ServerLogFunction.logFailedMessage("insertDataPersonale", new Date(), "", "Fail", "Username o n°Badge già presenti!");
						return false;
					}
															
			    } catch (Exception e) {
			    	esito=false;
			    	errore=e.getMessage();
		    		e.printStackTrace();
			    	if (tx!=null)    	
			    		tx.rollback();
			    	return false;
			    } finally {
			    	 if(!esito){
				        ServerLogFunction.logErrorMessage("insertDataPersonale", new Date(), "", "Error", errore);
				        return false;
			    	 }			    	
			    }									
	}
	
	
	@Override
	public void editDataPersonale(int id, String nome, String cognome, String username, String password, String statoRapporto, 
			String nBadge, String ruolo, String tipoOrario, String tipoLavoratore, String gruppoLavoro, 
			String costoOrario, String costoStruttura, String sede, String sedeOperativa, String oreDirette, String oreIndirette,  
			String permessi, String ferie, String ext, String oreRecupero) throws IllegalArgumentException {
			
		Boolean esito=true;
		String errore= new String();
		
		Personale p = new Personale();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		if(nome.compareTo("PSSWD")!=0){//nel caso in qui viene richiamata la funzione dal modulo di modifica password soltanto
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
				p.setStatoRapporto(statoRapporto);
				
				//TODO controllo edit username che non può già esserci?
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
				errore=e.getMessage();
				e.printStackTrace();
				esito=false;
				if (tx!=null)
					tx.rollback();			
			} finally {
				session.close();
				if(!esito)
		        	ServerLogFunction.logErrorMessage("editDataPersonale", new Date(), username, "Error", errore);
				else				
					ServerLogFunction.logOkMessage("editDataPersonale", new Date(), username, "Success");
			}
		
		}else{
			try {
				tx=	session.beginTransaction();
			
				p=(Personale)session.createQuery("from Personale where id_personale=:id").setParameter("id", id).uniqueResult();						
				p.setPassword(password);
				
				tx.commit();

			} catch (Exception e) {
				esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx!=null)
					tx.rollback();
					
			} finally {
				session.close();
				if(!esito)
		        	ServerLogFunction.logErrorMessage("editDataPersonale", new Date(), username, "Error", errore);
				else				
					ServerLogFunction.logOkMessage("editDataPersonale", new Date(), username, "Success");
			}
		}
	}
	
	//TODO boolean
	@Override
	public void removeDataPersonale(int id) throws IllegalArgumentException {
		
		Boolean esito=true;
		String errore= new String();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		Personale p= new Personale();
			
		try {
			  tx=	session.beginTransaction();
			  p=(Personale)session.get(Personale.class, id);
			  session.delete(p);
			  tx.commit();
			 
		    } catch (Exception e) {
		    	esito=false;
		    	e.printStackTrace();
	    		errore=e.getMessage();
		    	if (tx!=null)		    		
		    		tx.rollback();	    		
		    		
		    } finally {
		    	if(!esito)
		        	ServerLogFunction.logErrorMessage("removeDataPersonale", new Date(), "", "Error", errore);
				else
					 ServerLogFunction.logOkMessage("removeDataPersonale", new Date(), "", "Success");
		    }
	}
	
	
	@Override
	public List<PersonaleModel> getAllPersonaleModel() throws IllegalArgumentException{	
		
		List<Personale> listaP = (ArrayList<Personale>) ConverterUtil.getPersonale();  
		List<PersonaleModel> listaDTO = new ArrayList<PersonaleModel>(listaP!=null ? listaP.size() : 0);  
		    
		     try{
					if(listaP!=null){			
						for(Personale p : listaP)	
							listaDTO.add(ConverterUtil.personaleToModelConverter(p));
							
						ServerLogFunction.logOkMessage("getAllPersonaleModel", new Date(), "", "Success");
					}else
						ServerLogFunction.logFailedMessage("getAllPersonaleModel", new Date(), "","Failed", "Lista Personale vuota o nulla!");
					
				}catch (Exception e) {
						e.printStackTrace();
						ServerLogFunction.logErrorMessage("getAllPersonaleModel", new Date(), "", "Error", "Exception:"+e.getMessage());
				} 
		     
			return listaDTO;
	} 
		
	
	//TODO passare invece della lista di stringhe una lista di entity personale, in modo da usare una combobox lato view
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNomePM() throws IllegalArgumentException {
		
		Boolean esito=true;
		String errore= new String();
		
		List<Personale> listaP= new ArrayList<Personale>();
		List<String> listaNomi= new ArrayList<String>();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			  tx=session.beginTransaction();
			  listaP=(ArrayList<Personale>)session.createQuery("from Personale where ruolo='PM' or ruolo='DIR'").list();			  
			  tx.commit();
			 
			  for(Personale p :listaP)			
					  listaNomi.add(p.getCognome()+" "+p.getNome());			  
			  	     
		    } catch (Exception e) {
		    	esito= false;
		    	e.printStackTrace();
	    		errore=e.getMessage();
		    	if (tx!=null)		    		
		    		tx.rollback();		    	
		    }finally{
		    	if(!esito){
		    		ServerLogFunction.logErrorMessage("getNomePM", new Date(), "", "Error",errore);
		    		return null;
		    	}else
		    		ServerLogFunction.logOkMessage("getNomePM", new Date(), "", "Success");
		    }		
		
		return listaNomi;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> getListaDipendenti(String ruolo)throws IllegalArgumentException{	
		Boolean esito=true;
		String errore= new String();
		
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
		    	esito=false;
		    	errore=e.getMessage();
	    		e.printStackTrace();
		    	if (tx!=null)		    		
		    		tx.rollback();	    				    	
		    	
		    } finally {
		    	session.close();
		    	if(!esito){
		    		ServerLogFunction.logErrorMessage("getListaDipendenti", new Date(), "", "Error", errore);
		    		return null;
		    	}else{		    		
		    		ServerLogFunction.logOkMessage("getListaDipendenti", new Date(), "", "Success");
		    	}
		    }	
		
		return listaNomi;
	}	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PersonaleModel> getListaDipendentiModel(String ruolo) {
		
		List<PersonaleModel> listaNomi= new ArrayList<PersonaleModel>();
		PersonaleModel personaM= new PersonaleModel();
		List<Personale> listaP=new ArrayList<Personale>();
		
		Boolean esito=true;
		String errore= new String();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		try {
			tx=	session.beginTransaction();
			listaP = (List<Personale>)session.createQuery("from Personale").list();
			tx.commit();
			
			for(Personale p: listaP){
				personaM=new PersonaleModel(p.getId_PERSONALE(), p.getNome(), p.getCognome(), p.getUsername(), 
						"", "", "", "", "", "", "", "", "",  "",  "",  "",  "",  "",  "",  "", "");
				
				/*if(ruolo.compareTo("PM")==0)//ruolo di chi effettua la ricerca
					if(p.getRuolo().compareTo("DIP")==0)//se è il pm a richiederlo allora preleverò solo i dipendenti
						listaNomi.add(personaM);		
				if(ruolo.compareTo("PM")!=0)//se non è un PM a richiederlo allora ci saranno tutti i nomi
				*/
				listaNomi.add(personaM);					
			  }
		    } catch (Exception e) {
		    	esito=false;
		    	e.printStackTrace();
	    		errore=e.getMessage();
		    	if (tx!=null)	    		
		    		tx.rollback();		    		
		      
		    } finally {
		        session.close();
		        if(!esito){
		        	ServerLogFunction.logErrorMessage("getListaDipendentiModel", new Date(), "", "Error", errore);
		        	return null;
		        }
		        else
		        	ServerLogFunction.logOkMessage("getListaDipendentiModel", new Date(), "", "Success");
		    }			
		return listaNomi;
	}
	
//--------------------------------------------------------------------------------------------------------------

	
	
//-----------------------------------------Gestione RDA---------------------------------------------------------
	
	/*
	 * A livello db il fatto che rda possa avere più offerte e ordini è dovuto al fatto che erano previste le revisioni da gestire. 
	 * Non essendo gestite ogni rda avrà un'ordine e un'offerta.
	 */

	
	@Override
	public boolean eliminaAssociazioneOrdine(String numeroOrdine)
			throws IllegalArgumentException {
		
		Boolean esito=true;
		String errore= new String();
		//Chiudo l'ordine e libero la commessa (non lo elimino)
		Ordine o=new Ordine();
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();			
			o=(Ordine)session.createQuery("from Ordine where codiceOrdine=:codiceOrdine")
					.setParameter("codiceOrdine", numeroOrdine).uniqueResult();
		
			o.setCommessa(null);
			session.save(o);
		    tx.commit();	
		    
		} catch (Exception e) {
			esito=false;
			e.printStackTrace();
			errore=e.getMessage();
			if (tx!=null)
	    		tx.rollback();		    	
		}finally{			
			if(!esito){
				ServerLogFunction.logErrorMessage("eliminaAssociazioneOrdine", new Date(), numeroOrdine, "Error", errore);
				return false;
			}else
				ServerLogFunction.logOkMessage("eliminaAssociazioneOrdine", new Date(), numeroOrdine, "Success");
		}
		return true;
	}
	
	
	public List<RdaModel> getAllRdaModel() throws IllegalArgumentException {		
		
		Boolean esito=true;
		String errore= new String();
		
		Set<Rda> listaR =  ConverterUtil.getRda();  
		List<RdaModel> listaM = new ArrayList<RdaModel>(listaR!=null ? listaR.size() : 0);  
				    
		try{
			
			if(listaR!=null)
				for(Rda r : listaR)	
					listaM.add(ConverterUtil.rdaToModelConverter(r));				
			else{
					ServerLogFunction.logFailedMessage("getAllRdaModel", new Date(), "", "Failed","Lista Rda vuota o nulla!");
					return listaM;
			}
															
			}catch (Exception e) {
				esito=false;
				errore=e.getMessage();
				e.printStackTrace();			
				
			}finally{
				if(!esito){
					ServerLogFunction.logErrorMessage("getAllRdaModel", new Date(), "", "Error", errore);
					return null;
				}else
					ServerLogFunction.logOkMessage("getAllRdaModel", new Date(), "", "Success");
			}
		return listaM;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<String> getAllNumeroRdo() throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		List<String> listaRdo= new ArrayList<String>();
		List<Rda> lista=new ArrayList<Rda>();
				
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=	session.beginTransaction();
			  lista=(List<Rda>)session.createQuery("from Rda").list();
			  
			  for(Rda r:lista)				  
				  listaRdo.add(r.getCodiceRDA()+"("+r.getCliente().getRagioneSociale()+")"); //il codice che rappresenta il numero effettivo della Rdo
			  
			  tx.commit();
			  	     
		    } catch (Exception e) {
		    	esito=false;
		    	errore=e.getMessage();
		    	e.printStackTrace();
		    	if (tx!=null)
		    		tx.rollback();	    	
		    }finally{
		    	if(!esito){
		    		ServerLogFunction.logErrorMessage("getAllNumeroRdo", new Date(), "", "Error", errore);
					return null;
		    	}else
		    		ServerLogFunction.logOkMessage("getAllNumeroRdo", new Date(), "", "Success");
		    }
		return listaRdo;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RdoCompletaModel> getAllRdoCompletaModel()	throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		List<Rda> lista=new ArrayList<Rda>();
		List<RdoCompletaModel> listaM= new ArrayList<RdoCompletaModel>();
		RdoCompletaModel rdoM= new RdoCompletaModel();
		Ordine or= new Ordine();
		Offerta of=new Offerta();
			
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);	
		
		String numRdo="#";
		
		String numOfferta="#";
		String dataOfferte="#";
		String importo="0.0";
		
		String numOrdine="#";
		String descrizione="#";
		String elementoWbs="#";
		String conto="#";
		String prCenter="#";
		String bem="#";
		String numCommessa= "#";
		String dataInizio="#";
		String dataFine="#";
		String tariffa="0.0";
		String numRisorse="0";
		String oreDisp="0.0";
		String oreRes="0.0";
		String statoOrdine="N";//non definito
		String importoOrdine="0.00";
		String importoResiduo="0.00";
		
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
					  elementoWbs=or.getElementoWbs();
					  conto=or.getConto();
					  prCenter=or.getPrCenter();
					  bem=or.getBem();
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
					  statoOrdine=or.getStatoOrdine();
					  importoOrdine=or.getImporto();
					  importoResiduo=or.getImportoResiduo();
					  
				  }
					  
				  if(r.getOffertas().iterator().hasNext()){
					  of=r.getOffertas().iterator().next();
					  numOfferta=of.getCodiceOfferta();
					  if(of.getDataRedazione()!=null)
						  dataOfferte=formatter.format(of.getDataRedazione());
					  importo=of.getImporto();
				  }
					  			  
				  rdoM=new RdoCompletaModel(r.getNumeroRda()/*id*/, numRdo, r.getCliente().getRagioneSociale(), 
						  numOfferta, dataOfferte, descrizione,  elementoWbs, conto, prCenter, bem,
						  importo, numOrdine, numCommessa, dataInizio, dataFine, 
						  tariffa, numRisorse, oreDisp, oreRes, importoOrdine,importoResiduo, statoOrdine);
			  
				  listaM.add(rdoM);
				  numRdo="#";
				  elementoWbs="#";
				  conto="#";
				  prCenter="#";
				  bem="#";
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
				  importoOrdine="0.00";
				  importoResiduo="0.00";
				  statoOrdine="N";					  
			  }  
			  
			  tx.commit();		  
			  
		    } catch (Exception e) {
		    	esito=false;
		    	e.printStackTrace();
		    	errore=e.getMessage();
		    	if (tx!=null)
		    		tx.rollback();		    		    	
		    }finally{
		    	session.close();
		    	if(!esito){
		    		ServerLogFunction.logErrorMessage("getAllRdoCompletaModel", new Date(), "", "Error", errore);
			    	return null;
		    	}else
		    		ServerLogFunction.logOkMessage("getAllRdoCompletaModel", new Date(), "", "Success");
		    }		
		return listaM;
	}
	
	
	@Override
	public boolean saveRdoCompleta(String numRdo, String cliente,
			String numOfferta, Date dataOfferta, String importo,
			String numOrdine, String descrizione, String elementoWbs, String conto, 
			String prCenter, String bem, Date dataInizio,
			Date dataFine, String tariffa, String numRisorse, String oreDisp,
			String oreRes, List<TariffaOrdineModel>listaTar, String importoOrdine, String importoResiduoOrdine) throws IllegalArgumentException {
		
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
			
			//TODO aggiungere dei check o boolean
			insertDataOfferta(numOfferta, id+1, dataOfferta, descrizione, importo);
			insertDataOrdine(numOrdine, id+1, dataInizio, dataFine, descrizione, "0.00", numRisorse, oreDisp, oreRes, importoOrdine, importoResiduoOrdine);
			
			for(TariffaOrdineModel trf:listaTar){
				AttivitaOrdine att=new AttivitaOrdine();
				att.setDescrizioneAttivita((String)trf.get("descrizione"));
				att.setTariffaAttivita((String)trf.get("tariffaAttivita"));
				
				insertTariffeOrdine(numOrdine, att);
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx!=null)
	    		tx.rollback();					
			return false;
		}finally{
			session.close();
		}		
		return true;
	}

	
	private void insertTariffeOrdine(String numOrdine,
			AttivitaOrdine attivita) {
		Boolean esito=true;
		String errore= new String();
		Ordine ordine= new Ordine();	
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
			ordine=(Ordine)session.createQuery("from Ordine where codiceOrdine=:nordine").setParameter("nordine", numOrdine).uniqueResult();
			
			attivita.setOrdine(ordine);
			ordine.getAttivitaOrdines().add(attivita);
			
			session.save(ordine);
			tx.commit();
			
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();	
			if (tx!=null)
	    		tx.rollback();			
		}finally{
			session.close();
			if(!esito)
				ServerLogFunction.logErrorMessage("insertTariffeOrdine", new Date(), "", "Error", errore);
	    	else
	    		ServerLogFunction.logOkMessage("insertTariffeOrdine", new Date(), "", "Success");				
		}
	}
		

	private void insertDataOrdine(String numOrdine, int id,
			Date dataInizio, Date dataFine, String descrizione, String tariffa,
			String numRisorse, String oreDisp, String oreRes, String importoOrdine, String importoResiduoOrdine) {
		Boolean esito=true;
		String errore= new String();
		
		Rda r=new Rda();
		Ordine o= new Ordine();
		float oreResidue;
		float oreOrdine;
		oreOrdine=Float.valueOf(oreDisp);
		oreResidue=Float.valueOf(oreRes);	
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		oreRes=d.format(oreResidue);
		oreDisp=d.format(oreOrdine);
		
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
			o.setStatoOrdine("A");
			o.setImporto(importoOrdine);
			o.setImportoResiduo(importoResiduoOrdine);
			
			r.getOrdines().add(o);
		    
		    session.save(r);
		    tx.commit();		  
		    		
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();	
			if (tx!=null)
	    		tx.rollback();				
		}finally{
			//session.close();
			if(!esito)
				ServerLogFunction.logErrorMessage("insertDataOrdine", new Date(), "", "Error", errore);
	    	else
	    		ServerLogFunction.logOkMessage("insertDataOrdine", new Date(), "", "Success");				
		}	
	}


	private void insertDataOfferta(String numOfferta, int i, Date dataOfferta,
			String descrizione, String importo) {
		Boolean esito=true;
		String errore= new String();
		
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
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();	
			if (tx!=null)
	    		tx.rollback();	
		}finally{
			//session.close();
			if(!esito)
				ServerLogFunction.logErrorMessage("insertDataOfferta", new Date(), "", "Error", errore);
	    	else
	    		ServerLogFunction.logOkMessage("insertDataOfferta", new Date(), "", "Success");				
		}			
	}
	
	
	@Override
	public boolean editRdoCompleta(int idRdo, String numRdo, String cliente,
			String numOfferta, Date dataOfferta, String importo,
			String numOrdine, String descrizione,  String elementoWbs, String conto, 
			String prCenter, String bem, Date dataInizio,
			Date dataFine, String tariffa, String numRisorse, String oreDisp,
			String oreRes, List<TariffaOrdineModel>listaTar, String importoOrdine, String importoResiduoOrdine) throws IllegalArgumentException {
		
		Boolean esito=true;
		String errore= new String();
		Rda r=new Rda();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
				
			r=(Rda)session.createQuery("from Rda where numero_rda=:idrdo").setParameter("idrdo", idRdo).uniqueResult();		
			r.setCodiceRDA(numRdo);
			
			tx.commit();
			
			editDataOfferta(numOfferta, idRdo, dataOfferta, descrizione, importo);
			editDataOrdine(numOrdine, idRdo, dataInizio, dataFine, descrizione, tariffa, numRisorse, oreDisp, oreRes, importoOrdine, importoResiduoOrdine);
			for(TariffaOrdineModel trf:listaTar){
					
				String idAtt=(String)trf.get("idAttivitaOrdine");
				String tariffaAtt=(String)trf.get("tariffaAttivita");
				String descrizioneAtt=(String)trf.get("descrizione");
				
				if(idAtt==null)
					idAtt="0";
								
				editTariffeOrdine(idRdo, Integer.valueOf(idAtt), tariffaAtt, descrizioneAtt);
			}			
			
		   
			
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();	
			if (tx!=null)
	    		tx.rollback();	
		}finally{
			session.close();
			if(!esito){
				ServerLogFunction.logErrorMessage("editRdoCompleta", new Date(), "", "Error", errore);
				return false;
			}
	    	else
	    		ServerLogFunction.logOkMessage("editRdoCompleta", new Date(), "", "Success");	
		}
		
		 return true;
	}
	

	private void editTariffeOrdine(int idRdo, int idAtt, String tariffaAtt, String descrizioneAtt) {
		Boolean esito=true;
		String errore= new String();
		
		Ordine ordine= new Ordine();
		Rda rda= new Rda();
		AttivitaOrdine att= new AttivitaOrdine();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		
		try {
			
			tx=session.beginTransaction();
			
			rda= (Rda)session.createQuery("from Rda where numeroRda=:idRda").setParameter("idRda", idRdo).uniqueResult();
			//ordine=(Ordine)session.createQuery("from Ordine where codiceOrdine=:nordine").setParameter("nordine", idRdo).uniqueResult();
			ordine=rda.getOrdines().iterator().next();
						
			if(idAtt!=0){
				att=(AttivitaOrdine)session.createQuery("from AttivitaOrdine where idAttivitaOrdine=:id").setParameter("id", idAtt).uniqueResult();
			
				att.setDescrizioneAttivita(descrizioneAtt);
				att.setTariffaAttivita(tariffaAtt);
				
				tx.commit();
			}else{//creo la nuova perchè ancora non c'è
				
				att.setDescrizioneAttivita(descrizioneAtt);
				att.setTariffaAttivita(tariffaAtt);
				att.setOrdine(ordine);
				ordine.getAttivitaOrdines().add(att);
			
				session.save(ordine);
				tx.commit();			
			}
						
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();			
			if (tx!=null)
	    		tx.rollback();			
		}finally{
			session.close();
			if(!esito)
				ServerLogFunction.logErrorMessage("editTariffeOrdine", new Date(), "", "Error", errore);		
	    	else
	    		ServerLogFunction.logOkMessage("editTariffeOrdine", new Date(), "", "Success");
		}
	}

	
	private void editDataOrdine(String numOrdine, int idRdo, Date dataInizio,
			Date dataFine, String descrizione, String tariffa,
			String numRisorse, String oreDisp, String oreRes, String importoOrdine, String importoResiduoOrdine) {
		Boolean esito=true;
		String errore= new String();
		Ordine o= new Ordine();
		//AttivitaOrdine att;
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
			o.setImporto(importoOrdine);
			o.setImportoResiduo(importoResiduoOrdine);
			/*
			List<AttivitaOrdine> listaAttO= new ArrayList<AttivitaOrdine>();
			listaAttO.addAll(o.getAttivitaOrdines());
			for(AttivitaOrdine attO:listaAttO){
				attO.setOrdine(null);
				session.delete(attO); //TODO non permettere l'eliminazione delle tariffe su un ordine
			}
			
			o.setAttivitaOrdines(null);*/
			tx.commit();		  
	    		
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();			
			if (tx!=null)
	    		tx.rollback();		
		}finally{
			if(!esito)
				ServerLogFunction.logErrorMessage("editDataOrdine", new Date(), numOrdine, "Error", errore);		
	    	else
	    		ServerLogFunction.logOkMessage("editDataOrdine", new Date(), numOrdine, "Success");
		}			
	}


	private void editDataOfferta(String numOfferta, int idRdo,
			Date dataOfferta, String descrizione, String importo) {
		Boolean esito=true;
		String errore= new String();
		
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
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();			
			if (tx!=null)
	    		tx.rollback();	
		}finally{
			if(!esito)
				ServerLogFunction.logErrorMessage("editDataOfferta", new Date(), "", "Error", errore);		
	    	else
	    		ServerLogFunction.logOkMessage("editDataOfferta", new Date(), "", "Success");
		}	
	}
	
	
	@Override
	public boolean deleteRdoCompleta(int idRdo) throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		Rda r= new Rda();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
				
		try {
			  tx=	session.beginTransaction();
			  	  
			  r=(Rda)session.createQuery("from Rda where numero_rda =:idrda").setParameter("idrda", idRdo).uniqueResult();
			  for(Offerta o: r.getOffertas())
				  session.delete(o);
			  
			  for(Ordine or: r.getOrdines())
				  session.delete(or);
			  
			  session.delete(r);
			  tx.commit();	  
	     
		   } catch (Exception e) {
			   esito=false;
			   errore=e.getMessage();
			   e.printStackTrace();			
			   if (tx!=null)
				   tx.rollback();	
		    }finally{
		    	session.close();
		    	if(!esito){
					ServerLogFunction.logErrorMessage("deleteRdoCompleta", new Date(), "", "Error", errore);
					return false;
		    	}
		    	else
		    		ServerLogFunction.logOkMessage("deleteRdoCompleta", new Date(), "", "Success");
		    }
		 return true;
	}

	
	@Override
	public List<TariffaOrdineModel> loadTariffePerOrdine(int idRdo)
			throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		
		List<TariffaOrdineModel> listaTariffe= new ArrayList<TariffaOrdineModel>();
		List<AttivitaOrdine> listaAtt=new ArrayList<AttivitaOrdine>();
		Rda rda= new Rda();
		Ordine ordine= new Ordine();
		TariffaOrdineModel trf;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
				
		try {
			  tx=session.beginTransaction();
			  	 
			  rda=(Rda)session.get(Rda.class, idRdo);
			  if(rda!=null){
				  ordine=rda.getOrdines().iterator().next();
				  listaAtt.addAll(ordine.getAttivitaOrdines());			  
			  }
			  
			  for(AttivitaOrdine a:listaAtt){
				  trf=new TariffaOrdineModel(String.valueOf(a.getIdAttivitaOrdine()), a.getTariffaAttivita(), a.getDescrizioneAttivita());
				  listaTariffe.add(trf);
			  }
			  tx.commit();	  
			   
		   } catch (Exception e) {
			   esito=false;
			   errore=e.getMessage();
			   e.printStackTrace();			
			   if (tx!=null)
				   tx.rollback();	
		    }finally{
		    	session.close();
		    	if(!esito){
					ServerLogFunction.logErrorMessage("loadTariffePerOrdine", new Date(), "", "Error", errore);
					return null;
		    	}
		    	else
		    		ServerLogFunction.logOkMessage("loadTariffePerOrdine", new Date(), "", "Success");
		    }
		
		return listaTariffe;
	}


	@Override
	public boolean chiudiOrdine(String numeroOrdine)
			throws IllegalArgumentException {
		Boolean esito=true;
		Ordine o= new Ordine();
		Commessa c= new Commessa();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;	
		
		try {
			tx=	session.beginTransaction();
			
			o=(Ordine)session.createQuery("from Ordine where codiceOrdine=:numeroOrdine").setParameter("numeroOrdine", numeroOrdine).uniqueResult();
			c=o.getCommessa();
			o.setStatoOrdine("C");
			c.setStatoCommessa("Conclusa");
				
			tx.commit();
	    } catch (Exception e) {
	    	 esito=false;
			 e.printStackTrace();			
			 if (tx!=null)
				 tx.rollback();	
	    }finally{
	    	session.close();
	    	if(!esito)
				return false;	    	
	    }
		
		return true;
	}
//---------------------------------------------------------------------------------------
	
	
//--------------------Gestione Clienti---------------------------------------------------

	@Override
	public void insertDataCliente(int codCliente, String ragSociale, String codFiscale, String partitaIVA, String codRaggr,	String codFornitore, 
			String comune, String provincia, String stato, String indirizzo,
			String cap, String telefono, String fax, String email) throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		
		Cliente c = new Cliente();
		
		c.setCodCliente(codCliente);
		c.setRagioneSociale(ragSociale);
		c.setCodFiscale(codFiscale);
		c.setPartitaIVA(partitaIVA);
		c.setCodRaggr(codRaggr);
		c.setCodFornitore(codFornitore);
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
		    	 esito=false;
				 errore=e.getMessage();
				 e.printStackTrace();			
				 if (tx!=null)
					 tx.rollback();	
		    }finally{
		    	if(!esito)
					ServerLogFunction.logErrorMessage("insertDataCliente", new Date(), "", "Error", errore);
		    	else
		    		ServerLogFunction.logOkMessage("insertDataCliente", new Date(), "", "Success");
		    }
	}


	@Override
	public void editDataCliente(int codCliente, String ragSociale, String codFiscale, String partitaIVA, String codRaggr, String codFornitore,
			String comune, String provincia, String stato, String indirizzo,
			String cap, String telefono, String fax, String email) throws IllegalArgumentException {
			
		Boolean esito=true;
		String errore= new String();
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
			c.setCodFornitore(codFornitore);
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
		    	 esito=false;
				 errore=e.getMessage();
				 e.printStackTrace();			
				 if (tx!=null)
					 tx.rollback();	
		    }finally{
		    	if(!esito)
					ServerLogFunction.logErrorMessage("editDataCliente", new Date(), "", "Error", errore);
		    	else
		    		ServerLogFunction.logOkMessage("editDataCliente", new Date(), "", "Success");
		    } 			
	}


	@Override
	public void removeDataCliente(int id) throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		Cliente c= new Cliente();
				
		try {
			  tx=	session.beginTransaction();
			  c=(Cliente)session.createQuery("from Cliente where cod_cliente=:idcliente").setParameter("idcliente", id).uniqueResult();
			  session.delete(c);
			  tx.commit();
		     
		    } catch (Exception e) {
		    	 esito=false;
				 errore=e.getMessage();
				 e.printStackTrace();			
				 if (tx!=null)
					 tx.rollback();	
		    } finally{
		    	if(!esito)
					ServerLogFunction.logErrorMessage("removeDataCliente", new Date(), "", "Error", errore);
		    	else
		    		ServerLogFunction.logOkMessage("removeDataCliente", new Date(), "", "Success");
		    } 	
	}
	
	
	@Override
	public List<ClienteModel> getAllClientiModel() throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		List<Cliente> listaC = (ArrayList<Cliente>) ConverterUtil.getClienti();  
		List<ClienteModel> listaDTO = new ArrayList<ClienteModel>(listaC!=null ? listaC.size() : 0);  	    
		
		try{		
			if(listaC!=null){
				for(Cliente c : listaC)
					listaDTO.add(ConverterUtil.clienteToModelConverter(c));
					
				ServerLogFunction.logOkMessage("getAllClientiModel", new Date(), "", "Success");
			}else{
				ServerLogFunction.logFailedMessage("getAllClientiModel", new Date(), "", "Failed", "Lista Clienti vuota o nulla!");
				return null;
			}
			
			}catch (Exception e) {
				 esito=false;
				 errore=e.getMessage();
				 e.printStackTrace();	
				 ServerLogFunction.logErrorMessage("getAllClientiModel", new Date(), "", "Error", errore);
				 return null;
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
			  
			  for(Cliente c:listaC)				  
				  listaRS.add(c.getRagioneSociale()); //il codice che rappresenta il numero effettivo della Rdo
			  			  
			  tx.commit();
		     
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	if (tx!=null)
		    		tx.rollback();		    	
		    }	
		return listaRS;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClienteModel> getListaClientiModel() throws IllegalArgumentException {
		
		List<ClienteModel> listaCM= new ArrayList<ClienteModel>();
		List<Cliente> listaC= new ArrayList<Cliente>();
		ClienteModel cm;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
			
		try {
			  tx=	session.beginTransaction();
			  listaC=(List<Cliente>)session.createQuery("from Cliente").list();
			  
			  for(Cliente c:listaC){			  
				 cm=new ClienteModel(c.getCodCliente(), c.getRagioneSociale());
				 listaCM.add(cm);
			  }
			  
			  tx.commit();
		     
		    }catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return null;
			} finally {
				session.close();
			}
		
		return listaCM;		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getAllListaOrdini() throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
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
		     
		    } catch (Exception e) {
		    	 esito=false;
				 errore=e.getMessage();
				 e.printStackTrace();	
				 if (tx != null)
						tx.rollback();
		    }finally{
		    	if(!esito){
		    		ServerLogFunction.logErrorMessage("getAllListaOrdini", new Date(), "", "Error", errore);
				    return null;
		    	}else
		    		ServerLogFunction.logOkMessage("getAllListaOrdini", new Date(), "", "Success");	    	
		    }
		return listaOrdini;
	}

	
//------------------------------------COMMESSA--------------------------------------------------------------

	@Override
	public List<CommessaModel> getAllCommesseModel(String pm, String statoSelected) throws IllegalArgumentException {

		Boolean esito=true;
		String errore= new String();
		
		Set<Commessa> listaC = ConverterUtil.getCommesse(statoSelected);
		List<CommessaModel> listaDTO = new ArrayList<CommessaModel>(listaC != null ? listaC.size() : 0);
		Ordine o= new Ordine();

		try {
			if (listaC != null) {
				for (Commessa c : listaC) {							
						o=getOrdineByCommessa(c.getCodCommessa());
					    listaDTO.add(ConverterUtil.commesseToModelConverter(c,o));					    
				}
			}else{
				ServerLogFunction.logFailedMessage("getAllCommesseModel", new Date(), "", "Failed", errore);
				return null;
			}			

		} catch (Exception e) {
			 esito=false;
			 errore=e.getMessage();
			 e.printStackTrace();
		}finally{
			if(!esito){
				ServerLogFunction.logErrorMessage("getAllCommesseModel", new Date(), "", "Error", errore);
				return null;
			}
			else
				ServerLogFunction.logOkMessage("getAllCommesseModel", new Date(), "", "Success");								
		}	
		return listaDTO;
	}
	
	
	@Override
	public List<CommessaModel> getAllCommesseModelByPm(String cognomePm) {
		
		Boolean esito=true;
		String errore= new String();
		
		Set<Commessa> listaC = ConverterUtil.getCommesse("Tutte");
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
			
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();
		}finally{
			if(!esito){
				ServerLogFunction.logErrorMessage("getAllCommesseModelByPm", new Date(), "", "Error", errore);
				return null;
			}
			else
				ServerLogFunction.logOkMessage("getAllCommesseModelByPm", new Date(), "", "Success");			
		}		
		return listaDTO;
	}

	
	private Ordine getOrdineByCommessa(int idCommessa) {
		Boolean esito=true;
		String errore= new String();
		Ordine o = new Ordine();
		Commessa c= new Commessa();

		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;	
		
		try {
			  tx=	session.beginTransaction();
			  c=(Commessa)session.createQuery("from Commessa where cod_commessa =:idcommessa").setParameter("idcommessa", idCommessa).uniqueResult();
			  
			  if(c.getOrdines().iterator().hasNext())
				  o=c.getOrdines().iterator().next();
			  else
				  o=null;
			  
			  tx.commit();
		     
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
		    	if (tx!=null)
		    		tx.rollback();		    		    		
		    }finally{
		    	if(!esito){
					ServerLogFunction.logErrorMessage("getOrdineByCommessa", new Date(), String.valueOf("ID commessa ricercata: "+idCommessa), "Error", errore);
					return null;
				}
				/*else
					ServerLogFunction.logOkMessage("getOrdineByCommessa", new Date(), "", "Success");*/			    	
		    }
		
		 return o;
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
			if (tx!=null)
	    		tx.rollback();
			return null;
		}	
	}

	
	@Override
	public boolean insertDataCommessa(String numCommessa, String estensione, String tipoCommessa, String pM, String statoCommessa, 
			/*Date dataInizio,*/String oreLavoro, String oreLavoroResidue, String tariffaSal, String salAttuale, String pclAttuale,
			String descrizione, String note) {
		Boolean esito=true;
		String errore= new String();
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

			} catch (Exception e) {
				esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();			
			} finally{
				session.close();
				if(!esito){
					ServerLogFunction.logErrorMessage("insertDataCommessa", new Date(), String.valueOf("Commessa: "+numCommessa), "Error", errore);
					return false;
				}
				else
					ServerLogFunction.logOkMessage("insertDataCommessa", new Date(), "", "Success");
			}		
			return true;
		} else
			return false;
	}


	@Override
	public boolean editDataCommessa(int id, String numCommessa, String estensione,
			String tipoCommessa, String pM, String statoCommessa, String oreLavoro,String oreLavoroResidue,
			/*Date dataInizio,*/ String tariffaSal, String salAttuale, String pclAttuale, String descrizione, String note)
			throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
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
		
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();
			if (tx != null)
				tx.rollback();	
			
		} finally{
			session.close();
			if(!esito){
				ServerLogFunction.logErrorMessage("editDataCommessa", new Date(), String.valueOf("Commessa: "+numCommessa), "Error", errore);
				return false;
			}
			else
				ServerLogFunction.logOkMessage("editDataCommessa", new Date(), "", "Success");
		}	
		return true;
	}


	@Override
	public boolean deleteDataCommessa(int idCommessa)	throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
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
		     
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();	  			    	
		    }finally{				
				if(!esito){
					ServerLogFunction.logErrorMessage("deleteDataCommessa", new Date(), String.valueOf("Commessa: "+String.valueOf(idCommessa)), "Error", errore);
					return false;
				}
				else
					ServerLogFunction.logOkMessage("deleteDataCommessa", new Date(), "", "Success");
			}
		 return true;
	}


	//Associazione con creazione nuovo ordine
	@Override
	public boolean associaOrdineCommessa(String idCommessa,
			String numOrdine, String numRda, Date dataInizio, Date dataFine,
			String descrizione, String tariffaOraria, String numeroRisorse,
			String numeroOre) {
		
		Boolean esito=true;
		String errore= new String();
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
			
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();
			if (tx != null)
				tx.rollback();	
		}finally{				
			if(!esito){
				ServerLogFunction.logErrorMessage("associaOrdineCommessa", new Date(), String.valueOf("Commessa: "+String.valueOf(idCommessa)), "Error", errore);
				return false;
			}
			else
				ServerLogFunction.logOkMessage("associaOrdineCommessa", new Date(), "", "Success");
		}
		
		return true;
	}


	//Associazione su un ordine già presente
	@Override
	public boolean associaOrdinePresenteCommessa(String idCommessa,	String numOrdine) {		
		Boolean esito=true;
		String errore= new String();
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
	
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
		}finally{				
			if(!esito){
				ServerLogFunction.logErrorMessage("associaOrdineCommessa", new Date(), String.valueOf("Commessa: "+String.valueOf(idCommessa)), "Error", errore);
				return false;
			}
			else
				ServerLogFunction.logOkMessage("associaOrdineCommessa", new Date(), "", "Success");
		}
		
		 return true;
	}


	@Override
	public boolean closeCommessa(int idCommessa) {
		Boolean esito=true;
		String errore= new String();
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
		} catch (Exception e) {
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
		}finally{				
			if(!esito){
				ServerLogFunction.logErrorMessage("closeCommessa", new Date(), String.valueOf("Commessa: "+String.valueOf(idCommessa)), "Error", errore);
				return false;
			}
			else
				ServerLogFunction.logOkMessage("closeCommessa", new Date(), "", "Success");
		}		
		 return true;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCommesse() throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
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
	     
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
		    }finally{				
				if(!esito){
					ServerLogFunction.logErrorMessage("getCommesse", new Date(), "", "Error", errore);
					return null;
				}
				else
					ServerLogFunction.logOkMessage("getCommesse", new Date(), "", "Success");
			}	
		 return listaCommesse;
	}
	
	
	//Richiamata se ad accedere è un PM
	@SuppressWarnings("unchecked")
	public List<CommessaModel> getCommesseByPM(String nome, String cognome) throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		List<CommessaModel> listaCommesse= new ArrayList<CommessaModel>();
		List<Commessa> lista=new ArrayList<Commessa>();
		String app=new String();		
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  cognome=cognome.substring(0,1).toUpperCase()+cognome.substring(1,cognome.length());
			  tx=session.beginTransaction();
			  lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa!='Conclusa'").list();
			 		  
			  for(Commessa c: lista){
				  if(c.getMatricolaPM().compareTo("Tutti")==0){
					  listaCommesse.add(new CommessaModel(c.getCodCommessa(), c.getNumeroCommessa(), c.getEstensione(), c.getDenominazioneAttivita()));
				  }
				  else{
					  app=c.getMatricolaPM();
					  
					  if(app.indexOf(" ")!=-1){
						  app=app.substring(0,app.indexOf(" "));//prendo il cognome del pm
					  if((!c.getAttivitas().iterator().hasNext())&&(app.compareTo(cognome)==0))
						  listaCommesse.add(new CommessaModel(c.getCodCommessa(), c.getNumeroCommessa(), c.getEstensione(), c.getDenominazioneAttivita()));
					  }					 
				  }
			  }
			  tx.commit();
	     
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
		    }finally{				
				if(!esito){
					ServerLogFunction.logErrorMessage("getCommesseByPM", new Date(), nome+" "+cognome, "Error", errore);
					return null;
				}
				else
					ServerLogFunction.logOkMessage("getCommesseByPM", new Date(), "", "Success");
			}			
		return listaCommesse;
	}
	
	
	//Richiamata se ad accedere è AMM o DIR
	@SuppressWarnings("unchecked")
	public List<CommessaModel> getCommesseAperte() throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		List<CommessaModel> listaCommesse= new ArrayList<CommessaModel>();
		List<Commessa> listaTutteCommesse=new ArrayList<Commessa>();
		
		List<Integer> listaNumeriCommesse= new ArrayList<Integer>();
				
		int maxCommessa=0;
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=session.beginTransaction();
			 // lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa!='Conclusa'").list();
			  listaTutteCommesse=(List<Commessa>)session.createQuery("from Commessa").list();
			  
			  for(Commessa c: listaTutteCommesse){
				  //app=c.getMatricolaPM();
				  listaNumeriCommesse.add(Integer.valueOf(c.getNumeroCommessa()));
				  
				  if((!c.getAttivitas().iterator().hasNext())&&c.getStatoCommessa().compareTo("Conclusa")!=0){
					  listaCommesse.add(new CommessaModel(c.getCodCommessa(), c.getNumeroCommessa(), c.getEstensione(), c.getDenominazioneAttivita()));				  
				  }
			  }
			  
			  for(Integer i: listaNumeriCommesse){
				if(i>maxCommessa)
				  maxCommessa=i;
			  }
			  
			  maxCommessa++;	  
			  listaCommesse.add(new CommessaModel(0, String.valueOf(maxCommessa), "0", ""));
			  
			  tx.commit();
   
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
		    }finally{				
				if(!esito){
					ServerLogFunction.logErrorMessage("getCommesseAperte", new Date(), "", "Error", errore);
					return null;
				}
				else
					ServerLogFunction.logOkMessage("getCommesseAperte", new Date(), "", "Success");
			}	
		 return listaCommesse;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CommessaModel> getCommesseByPmConAssociazioni(String nome,
			String cognome) throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();		
		List<CommessaModel> listaCommesse= new ArrayList<CommessaModel>();
		List<Commessa> lista=new ArrayList<Commessa>();
		String app=new String();		
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  cognome=cognome.substring(0,1).toUpperCase()+cognome.substring(1,cognome.length());
			  tx=session.beginTransaction();
			  lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa!='Conclusa'").list();
			 		  
			  for(Commessa c: lista){
				  if(c.getMatricolaPM().compareTo("Tutti")==0){
					  listaCommesse.add(new CommessaModel(c.getCodCommessa(), c.getNumeroCommessa(), c.getEstensione(), c.getDenominazioneAttivita()));
				  }
				  else{
					  app=c.getMatricolaPM();
					
					  if(app.indexOf(" ")!=-1){
						  app=app.substring(0,app.indexOf(" "));//prendo il cognome del pm
					  if(/*(c.getAttivitas().iterator().hasNext())&&*/(app.compareTo(cognome)==0))
						  listaCommesse.add(new CommessaModel(c.getCodCommessa(), c.getNumeroCommessa(), c.getEstensione(), c.getDenominazioneAttivita()));
					  }				 
				  }
			  }
			  tx.commit();
     
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
		    }finally{				
				if(!esito){
					ServerLogFunction.logErrorMessage("getCommesseByPmConAssociazioni", new Date(), "", "Error", errore);
					return null;
				}
				else
					ServerLogFunction.logOkMessage("getCommesseByPmConAssociazioni", new Date(), "", "Success");
			}	
		
		return listaCommesse;
	}

	
	@Override
	public List<CommessaModel> getCommesseAperteConAssociazioni()
			throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();	
		List<CommessaModel> listaCommesse= new ArrayList<CommessaModel>();
		List<Commessa> listaTutteCommesse=new ArrayList<Commessa>();
		
		List<Integer> listaNumeriCommesse= new ArrayList<Integer>();
				
		int maxCommessa=0;
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=session.beginTransaction();
			 // lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa!='Conclusa'").list();
			  listaTutteCommesse=(List<Commessa>)session.createQuery("from Commessa").list();
			  
			  for(Commessa c: listaTutteCommesse){
				  //app=c.getMatricolaPM();
				  listaNumeriCommesse.add(Integer.valueOf(c.getNumeroCommessa()));
				  
				  if((c.getAttivitas().iterator().hasNext())&&c.getStatoCommessa().compareTo("Conclusa")!=0){
					  listaCommesse.add(new CommessaModel(c.getCodCommessa(), c.getNumeroCommessa(), c.getEstensione(), c.getDenominazioneAttivita()));				  
				  }
			  }
			  
			  for(Integer i: listaNumeriCommesse){
				if(i>maxCommessa)
				  maxCommessa=i;
			  }
			  
			  maxCommessa++;	  
			  listaCommesse.add(new CommessaModel(0, String.valueOf(maxCommessa), "0", ""));
			  
			  tx.commit();
			 		     
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
		    }finally{				
				if(!esito){
					ServerLogFunction.logErrorMessage("getCommesseAperteConAssociazioni", new Date(), "", "Error", errore);
					return null;
				}
				else
					ServerLogFunction.logOkMessage("getCommesseAperteConAssociazioni", new Date(), "", "Success");
			}
		return listaCommesse;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CommessaModel> getCommesseAperteSenzaOrdine()
			throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		List<CommessaModel> listaCommesse= new ArrayList<CommessaModel>();
		List<Commessa> listaTutteCommesse=new ArrayList<Commessa>();
		CommessaModel cm= new CommessaModel();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
			
		try {
			  tx=session.beginTransaction();
			  //lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa!='Conclusa'").list();
			  listaTutteCommesse=(List<Commessa>)session.createQuery("from Commessa").list();
			  
			  for(Commessa c: listaTutteCommesse){			 
					if(!c.getOrdines().iterator().hasNext()&&c.getStatoCommessa().compareTo("Aperta")==0){
					 
						cm=new CommessaModel(c.getCodCommessa(), c.getNumeroCommessa(), "", "", "", c.getEstensione(), 
								"", "", "", "", "", "", "", "", "", "", "", "", "", "");
						listaCommesse.add(cm);
					}
			  }		  
			 			  
			  tx.commit();		     
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
		    }finally{				
				if(!esito){
					ServerLogFunction.logErrorMessage("getCommesseAperteSenzaOrdine", new Date(), "", "Error", errore);
					return null;
				}
				else
					ServerLogFunction.logOkMessage("getCommesseAperteSenzaOrdine", new Date(), "", "Success");
			}		
		return listaCommesse;
	}	

	
	@SuppressWarnings("unchecked")
	@Override
	public List<GestioneRdoCommesse> getAllRdoCommesse() {
		Boolean esito=true;
		String errore= new String();
		List<GestioneRdoCommesse> listaM= new ArrayList<GestioneRdoCommesse>();
		//List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		GestioneRdoCommesse rdoM= new GestioneRdoCommesse();
		Ordine or= new Ordine();
		Offerta of=new Offerta();
		Rda r= new Rda();
		
		//FoglioFatturazione f;
		
		int idRdo=0;
		int idCommessa=0;
		
		String cliente="";
		String pm="";
		String numCommessa= "#";
		String estensioneCommessa="#";
		String statoCommessa="";
		String tipologiaCommessa="";
		String tariffaSalPcl="0.0";
		String oreLavoroCommessa="0.00";
		String descrizione="";
				
		/*--non utilizzare
		String salAttuale="0.0";
		String pclAttuale="0.0";
		String oreRes="0.0";
		*/
		String numRdo="#";
		String numOfferta="#";
		String numOrdine="#";	
		String statoOrdine="";
		String tariffaOrdine="0.0";		
		String oreOrdine="0.0";			
		Date dataOrdine=null;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
			
		try {
			  tx=session.beginTransaction();
			  listaC=(List<Commessa>)session.createQuery("from Commessa").list();
			  
			  for(Commessa c:listaC) {
				  idCommessa=c.getCodCommessa();
				  numCommessa=c.getNumeroCommessa();
				  estensioneCommessa=c.getEstensione();
				  statoCommessa=c.getStatoCommessa();
				  pm=c.getMatricolaPM();
				  descrizione=c.getDenominazioneAttivita();
				  tariffaSalPcl=c.getTariffaSal();
				  tipologiaCommessa=c.getTipoCommessa();
				  oreLavoroCommessa= String.valueOf(c.getOreLavoro());
				  
				  if(c.getOrdines().iterator().hasNext()){
					  r=c.getOrdines().iterator().next().getRda();
					  or=c.getOrdines().iterator().next();
					  
					  dataOrdine=or.getDataInizio();
					  numRdo=r.getCodiceRDA();
					  idRdo=r.getNumeroRda();
					  cliente=r.getCliente().getRagioneSociale();					  
					  
					  numOrdine=or.getCodiceOrdine(); 
					  tariffaOrdine=or.getTariffaOraria();
					  oreOrdine=or.getOreBudget();
					  //oreRes=or.getOreResidueBudget();
					  statoOrdine=or.getStatoOrdine();
					  
					  if(r.getOffertas().iterator().hasNext()){
						  of=r.getOffertas().iterator().next();
						  numOfferta=of.getCodiceOfferta();			 
					  }
					  
					  //listaFF.addAll(c.getFoglioFatturaziones());
					  /*if(listaFF.size()>1)
						  Collections.sort(listaFF, new Comparator<FoglioFatturazione>(){
							  public int compare(FoglioFatturazione s1, FoglioFatturazione s2) {
								  String p1= String.valueOf(s1.getIdFoglioFatturazione());
								  String p2= String.valueOf(s2.getIdFoglioFatturazione());
								  
								 return p1.compareTo(p2);
							  }
					  });
						  
					  f=new FoglioFatturazione();
						  
					  if(listaFF.size()>0){
							f=listaFF.get(listaFF.size()-1);
							salAttuale=ServerUtility.aggiornaTotGenerale(f.getSALattuale(), f.getVariazioneSAL());
						  	pclAttuale=ServerUtility.aggiornaTotGenerale(f.getPCLattuale(), f.getVariazionePCL()); 	
					  }*/
					  
				  }else{
					  
					  /*listaFF.addAll(c.getFoglioFatturaziones());
					  
					  if(listaFF.size()>1)
						  Collections.sort(listaFF, new Comparator<FoglioFatturazione>(){
							  public int compare(FoglioFatturazione s1, FoglioFatturazione s2) {
								  String p1= String.valueOf(s1.getIdFoglioFatturazione());
								  String p2= String.valueOf(s2.getIdFoglioFatturazione());
								  
								 return p1.compareTo(p2);
							  }
					  });
						  
					  f=new FoglioFatturazione();
						  
					  if(listaFF.size()>0){
							f=listaFF.get(listaFF.size()-1);
							salAttuale=ServerUtility.aggiornaTotGenerale(f.getSALattuale(), f.getVariazioneSAL());
						  	pclAttuale=ServerUtility.aggiornaTotGenerale(f.getPCLattuale(), f.getVariazionePCL());
						  	tariffaSalPcl=c.getTariffaSal();
					  }	*/		  
				  }
					 		  			  
				  rdoM=new GestioneRdoCommesse(idRdo, idCommessa, cliente, pm, numCommessa, estensioneCommessa, statoCommessa,
						  tipologiaCommessa, tariffaSalPcl, oreLavoroCommessa, descrizione, numRdo, numOfferta, numOrdine,
						  statoOrdine, tariffaOrdine, oreOrdine, dataOrdine);
							  
				 // listaFF.clear();
				  cliente="";
				  pm="";
				  numCommessa= "#";
				  estensioneCommessa="#";
				  statoCommessa="";
				  tipologiaCommessa="";
				  tariffaSalPcl="0.0";
				  oreLavoroCommessa="0.00";
				  descrizione="";
									
				  numRdo="#";
				  numOfferta="#";
				  numOrdine="#";	
				  statoOrdine="";
				  tariffaOrdine="0.0";		
				  oreOrdine="0.0";			
				  dataOrdine=null;
				  				  
				  listaM.add(rdoM);
			  }  
			  
			  tx.commit();			  
		    } catch (Exception e) {
		    	esito=false;
				errore=e.getMessage();
				e.printStackTrace();
				if (tx != null)
					tx.rollback();
		    }finally{
		    	session.close();
		    	if(!esito){
					ServerLogFunction.logErrorMessage("getCommesseAperteSenzaOrdine", new Date(), "", "Error", errore);
					return null;
				}
				else
					ServerLogFunction.logOkMessage("getCommesseAperteSenzaOrdine", new Date(), "", "Success");
		    }		
		
		 return listaM;
	}
	
	
//----------------------------- ASSOCIAZIONI COMMESSE ATTIVITA PERSONALE---------------------------------------------------------

	
/* Implementazione temporanea che non tiene conto di un numero di attività >1
*/
	@Override
	public boolean createAssociazioneCommessaDipendenti(String pm, String commessa, List<String> listaDipendenti)	throws IllegalArgumentException {
		
		String app=new String();
		String numeroCommessa= new String();
		String estensione=new String();
		Boolean result=false;
		
		app=commessa;
		int index = app.indexOf('.');
		numeroCommessa = app.substring(0, index);
		estensione=app.substring(index+1,app.length());
		
		result=createAttivita(numeroCommessa, estensione);
		
		if(result){
			for(String dipendente:listaDipendenti)			
				createAssociazionePtoA(numeroCommessa, estensione, dipendente);
			return true;
		}else
			return false;
	}


	private Boolean createAttivita(String numeroCommessa, String estensione) {
		Boolean esito=true;
		String errore= new String();
		
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
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
		}finally{
			session.close();
			if(!esito){
				ServerLogFunction.logErrorMessage("createAttivita", new Date(), numeroCommessa+" "+estensione, "Error", errore);
				return false;
			}
			else
				ServerLogFunction.logOkMessage("createAttivita", new Date(), "", "Success");
		}		
		return true;
	}	
	
	
	private void createAssociazionePtoA(String numeroCommessa, String estensione, String dipendente) {		
		Boolean esito=true;
		String errore= new String();
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
			esito=false;
			errore=e.getMessage();
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
		}finally{
			session.close();
			if(!esito)
				ServerLogFunction.logErrorMessage("createAssociazionePtoA", new Date(), numeroCommessa+" "+estensione, "Error", errore);			
			else
				ServerLogFunction.logOkMessage("createAssociazionePtoA", new Date(), "", "Success");
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
			
			for(String dipendente:listaDipendenti)				
				createAssociazionePtoA(numeroCommessa, estensione, dipendente);
			
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

				for (AssociazionePtoA a : listaC)						
					    listaDTO.addAll(ConverterUtil.associazionePtoAToModelConverter(a));					    			
				
				ServerLogFunction.logOkMessage("getAssociazioniPersonaleCommessa", new Date(), "", "Success");
			}else
				ServerLogFunction.logFailedMessage("getAssociazioniPersonaleCommessa", new Date(), "","Failed", "Lista Associazioni vuota o nulla!");

		} catch (Exception e) {			
			e.printStackTrace();
			ServerLogFunction.logErrorMessage("getAssociazioniPersonaleCommessa", new Date(), "", "Error", "Exception:"+e.getMessage());
			return null;
		}
		return listaDTO;
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
		
		if(nome.compareTo("ALL")==0){
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
			
		}else
		
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
	
	//TODO LOG FIN QUA
	
//-------------------------------------------------FOGLIO ORE----------------------------------------------

	//FOGLIO ORE COLOCATION
	@Override
	public boolean insertFoglioOreGiorno(String username, Date giorno,
			List<IntervalliCommesseModel> intervalliC) {
		
		Personale p= new Personale();
		FoglioOreMese foglioOre=new FoglioOreMese();
		DettaglioOreGiornaliere dettOreGiornaliero= new DettaglioOreGiornaliere();
		
		Set<DettaglioOreGiornaliere> listaDettOreGiorno= new HashSet<DettaglioOreGiornaliere>();
		
		String data=new String();
		String mese=new String();
		String anno= new String();
				
		int idDettGiorno;
		boolean intervalliCommPresenti;
		boolean duplicato= false;
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giorno);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giorno);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    formatter = new SimpleDateFormat("dd");
	    
		data=(mese+anno);//sostituito la ricerca su mese con quella su data
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try{
			
			tx=session.beginTransaction();	
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			foglioOre=(FoglioOreMese) session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese").setParameter("id", p.getId_PERSONALE())
					.setParameter("mese", data).uniqueResult();
			if(!ServerUtility.mesePresente(data, p.getFoglioOreMeses()))
			/*if(foglioOre==null)*/{	
				foglioOre=new FoglioOreMese();
				foglioOre.setPersonale(p);
				foglioOre.setMeseRiferimento(data);
				foglioOre.setStatoRevisioneMese("0");
				
				for(FoglioOreMese fm:p.getFoglioOreMeses())
					if(foglioOre.equals(fm))
						duplicato=true;
				
				if(!duplicato){		
					p.getFoglioOreMeses().add(foglioOre);
					
					session.save(p);
					tx.commit();
					    		
					createDettaglioGiornaliero(username, giorno, "0.00", "0.00", "0.00", 
							"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "0");
									
					createDettaglioIntervalliCommesse(intervalliC, username, giorno);	
				
				}else{
					tx.commit();
					//TODO log di duplicato
				}					
			}
			
			else //il mese è già stato creato quindi prelevo il foglio ore del mese
			{				
				listaDettOreGiorno.addAll(foglioOre.getDettaglioOreGiornalieres()); 		
				//controllo che il giorno sia già presente
				if(giornoPresente(listaDettOreGiorno, giorno)){
					
					dettOreGiornaliero=(DettaglioOreGiornaliere)session.createQuery("from DettaglioOreGiornaliere where giornoRiferimento=:giorno and id_foglio_ore=:idFoglioOre")
							.setParameter("giorno", giorno).setParameter("idFoglioOre", foglioOre.getIdFoglioOre()).uniqueResult();
					
					idDettGiorno=dettOreGiornaliero.getIdDettaglioOreGiornaliere();
														
					if(dettOreGiornaliero.getDettaglioIntervalliCommesses().isEmpty())
						intervalliCommPresenti=false;
					else 
						intervalliCommPresenti=true;
					
					tx.commit();
					
					//controllo che gli intervalli di commesse e IU esistano già
					
					if(intervalliCommPresenti)
						editDettaglioIntervalliCommesse(intervalliC, idDettGiorno);
					else
						createDettaglioIntervalliCommesse(intervalliC, username, giorno);
				}
				else{
					//se il giorno  non è presente lo "creo"
					dettOreGiornaliero.setFoglioOreMese(foglioOre);
					dettOreGiornaliero.setGiornoRiferimento(giorno);
					dettOreGiornaliero.setTotaleOreGiorno("0.00");
					dettOreGiornaliero.setDeltaOreGiorno("0.00");
					dettOreGiornaliero.setOreAssenzeRecupero("0.00");
					dettOreGiornaliero.setOreViaggio("0.00");
					dettOreGiornaliero.setDeltaOreViaggio("0.00");
					dettOreGiornaliero.setOreStraordinario("0.00");
					dettOreGiornaliero.setOreFerie("0.00");
					dettOreGiornaliero.setOrePermesso("0.00");
					dettOreGiornaliero.setOreAbbuono("0.00");
					//dettOreGiornaliero.setOreExtFest(oreExtFest);
					dettOreGiornaliero.setGiustificativo("");
					dettOreGiornaliero.setNoteAggiuntive("");
					dettOreGiornaliero.setStatoRevisione("0");
					foglioOre.getDettaglioOreGiornalieres().add(dettOreGiornaliero);
																	
					session.save(foglioOre);
					tx.commit();
					
					createDettaglioIntervalliCommesse(intervalliC, username, giorno);
				}			
			}			
			return true;
			
		}catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}		
	}

	
	@Override
	public boolean eliminaDatiGiorno(String username, Date giornoRiferimento)
			throws IllegalArgumentException {
		
		Personale p= new Personale();
		FoglioOreMese foglioOre=new FoglioOreMese();
		//DettaglioOreGiornaliere dettOreGiornaliero= new DettaglioOreGiornaliere();
		
		String data=new String();
		String mese=new String();
		String anno= new String();
		String data1= new String();
		String data2=new String();
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    formatter = new SimpleDateFormat("dd");
		
	    data=(mese+anno);
	    
	    Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				tx=session.beginTransaction();
								
				p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
				
				foglioOre=(FoglioOreMese) session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese").setParameter("id", p.getId_PERSONALE())
						.setParameter("mese", data).uniqueResult();
				formatter = new SimpleDateFormat("yyyy-MM-dd");	
				data1=formatter.format(giornoRiferimento);
				for(DettaglioOreGiornaliere d:foglioOre.getDettaglioOreGiornalieres()){							
					data2=formatter.format(d.getGiornoRiferimento());
					
					if(data1.compareTo(data2)==0){
						foglioOre.getDettaglioOreGiornalieres().remove(d);
						session.delete(d);
						break;
					}					
				}
							
				tx.commit();
				
		}catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}	    
		return true;
	}
	
	
	@Override
	public boolean insertFoglioOreGiorno(String username, Date giornoRiferimento,
			String totOreGenerale, String delta, String oreViaggio,
			String oreAssRecupero, String deltaOreViaggio,
			String giustificativo, String oreStraordinario, String oreFerie, String orePermesso, 
			String revisione, String oreAbbuono, List<String> intervalliIU,
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
		boolean duplicato=false;
		
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
		
					for(FoglioOreMese fm:p.getFoglioOreMeses())
						if(foglioOre.equals(fm))
							duplicato=true;
					
					if(!duplicato){		
						p.getFoglioOreMeses().add(foglioOre);				
						session.save(p);
						tx.commit();
					
						createDettaglioGiornaliero(username, giornoRiferimento, totOreGenerale, delta, oreViaggio, 
								oreAssRecupero, deltaOreViaggio, oreStraordinario, oreFerie, orePermesso, oreAbbuono, giustificativo, noteAggiuntive, revisione);
									
						createDettaglioIntervalliIU(intervalliIU ,username, giornoRiferimento);
						createDettaglioIntervalliCommesse(intervalliC, username, giornoRiferimento);
					
					}else{
						tx.commit();
						//TODO log di duplicato
					}
					
					/*p.getFoglioOreMeses().add(foglioOre);
					
					session.save(p);
					tx.commit();
					
					createDettaglioGiornaliero(username, giornoRiferimento, totOreGenerale, delta, oreViaggio, 
							oreAssRecupero, deltaOreViaggio, oreStraordinario, oreFerie, orePermesso, oreAbbuono, giustificativo, noteAggiuntive, revisione);
								
					createDettaglioIntervalliIU(intervalliIU ,username, giornoRiferimento);
					createDettaglioIntervalliCommesse(intervalliC, username, giornoRiferimento);	*/		
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
						dettOreGiornaliero.setOreAbbuono(oreAbbuono);
						dettOreGiornaliero.setGiustificativo(giustificativo);
						dettOreGiornaliero.setNoteAggiuntive(noteAggiuntive);
						dettOreGiornaliero.setStatoRevisione(revisione);					
						
						//TODO possibile ottimizzare?
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
						dettOreGiornaliero.setOreAbbuono(oreAbbuono);
						dettOreGiornaliero.setGiustificativo(giustificativo);
						dettOreGiornaliero.setNoteAggiuntive(noteAggiuntive);
						dettOreGiornaliero.setStatoRevisione(revisione);
						//foglioOre.getDettaglioOreGiornalieres().add(dettOreGiornaliero);
								
						for(DettaglioOreGiornaliere dtt:foglioOre.getDettaglioOreGiornalieres())
							if(dettOreGiornaliero.equals(dtt))
								duplicato=true;
								
						if(!duplicato){		
							foglioOre.getDettaglioOreGiornalieres().add(dettOreGiornaliero);				
							session.save(foglioOre);
							tx.commit();
						}else
							tx.commit();
							//session.close();
												
						//session.save(foglioOre);
						//tx.commit();

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
			String oreAssRecupero, String deltaOreViaggio, String oreStraordinario, String oreFerie, String orePermesso, String oreAbbuono ,
			String giustificativo, String noteAggiuntive, String revisione) {
		
		DettaglioOreGiornaliere dettOreGiornaliero= new DettaglioOreGiornaliere();
		Personale p= new Personale();
		FoglioOreMese foglioOre= new FoglioOreMese();
		
		String data=new String();
		String mese=new String();
		String anno= new String();
		boolean duplicato=false;
		
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
			dettOreGiornaliero.setOreAbbuono(oreAbbuono);
			dettOreGiornaliero.setOreFerie(oreFerie);
			dettOreGiornaliero.setOrePermesso(orePermesso);
			dettOreGiornaliero.setOreViaggio(oreViaggio);
			dettOreGiornaliero.setDeltaOreViaggio(deltaOreViaggio);
			dettOreGiornaliero.setGiustificativo(giustificativo);
			dettOreGiornaliero.setNoteAggiuntive(noteAggiuntive);
			dettOreGiornaliero.setStatoRevisione(revisione);
				
			for(DettaglioOreGiornaliere dtt:foglioOre.getDettaglioOreGiornalieres())
				if(dettOreGiornaliero.equals(dtt))
					duplicato=true;
					
			if(!duplicato){		
				foglioOre.getDettaglioOreGiornalieres().add(dettOreGiornaliero);				
				session.save(foglioOre);
				tx.commit();
			}else{
				tx.commit();
				//session.close();
			}
							
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
		boolean duplicato=false;
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
			
			
			for(DettaglioIntervalliCommesse dtt: dettGiorno.getDettaglioIntervalliCommesses())
				if(dettCommesse.equals(dtt))
					duplicato=true;
			
			if(!duplicato){
				dettGiorno.getDettaglioIntervalliCommesses().add(dettCommesse);			
				session.save(dettGiorno);
				tx.commit();
			}else{			
				tx.commit();
			}		
						
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
		boolean duplicato=false;
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
						
			for(DettaglioIntervalliIU dtt: dettGiorno.getDettaglioIntervalliIUs())
				if(dettIU.equals(dtt))
					duplicato=true;
			
			if(!duplicato){
				dettGiorno.getDettaglioIntervalliIUs().add(dettIU);			
				session.save(dettGiorno);
				tx.commit();
			}else{				
				tx.commit();
			}					
			
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
		boolean duplicato=false;
		
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
				
				for(DettaglioIntervalliCommesse dtt: d.getDettaglioIntervalliCommesses())
					if(dettCommesse.equals(dtt))
						duplicato=true;
				
				if(!duplicato){
					d.getDettaglioIntervalliCommesses().add(dettCommesse);			
					session.save(d);
					tx.commit();
				}else{			
					tx.commit();
				}
						
				/*
				d.getDettaglioIntervalliCommesses().add(dettCommesse);
				session.save(d);
				tx.commit();*/
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
		String compilato= new String();
		String giornoCompl= new String();
		String sblocco; //Si-No
				
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
		formatter=new SimpleDateFormat("yyyy-MM-dd", Locale.ITALIAN);
	    giornoCompl=formatter.format(giornoRiferimento);
		
		data=(mese+anno);//Sostituito mese con data
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and  meseRiferimento=:mese")
						.setParameter("id", p.getId_PERSONALE()).setParameter("mese", data).uniqueResult();
			
			
			//controllo che la data sia presente in un periodo inserito per sbloccare la compilazione
			sblocco=ServerUtility.confrontaDataSblocco(giornoRiferimento, p.getSedeOperativa()); 
			
			if(foglioOre!=null){
				String giorno= new String();
				String statoRevisione=foglioOre.getStatoRevisioneMese();
				
				for(DettaglioOreGiornaliere d: foglioOre.getDettaglioOreGiornalieres()){
					giorno=formatter.format(d.getGiornoRiferimento());
					if(giorno.compareTo(giornoCompl)==0){
						compilato="s";
						break;
					}
					else
						compilato="n";					
				}
				
				listaIntervalli.add(new IntervalliIUModel(statoRevisione, compilato, sblocco, "")); //il primo che inserisco sarà lo stato revsione. questo verrà poi rimosso
				
				if(!foglioOre.getDettaglioOreGiornalieres().isEmpty())
					listaGiorni.addAll(foglioOre.getDettaglioOreGiornalieres());			
				else 
					return listaIntervalli;				
			}
			else {
				String statoRevisione="0";
				
				compilato="n";
				listaIntervalli.add(new IntervalliIUModel(statoRevisione, compilato, sblocco, "")); //il primo che inserisco sarà lo stato revsione. questo verrà poi rimosso
				
				return listaIntervalli;
			}
			
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
		String statoRevisione="0";		
		
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
			
			foglioOre=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese")
						.setParameter("id", p.getId_PERSONALE()).setParameter("mese", data).uniqueResult();
		
			if(foglioOre!=null)
				if(!foglioOre.getDettaglioOreGiornalieres().isEmpty()){
					listaGiorni.addAll(foglioOre.getDettaglioOreGiornalieres());
					statoRevisione=foglioOre.getStatoRevisioneMese();
				}
				else return giust= new GiustificativiModel(orePreviste, "0.00", ("-"+orePreviste+".00"), "0.00", "0.00", "0.00", "","0", "0.00", "0.00", "0.00","0.00", "");
			else return giust= new GiustificativiModel(orePreviste, "0.00", ("-"+orePreviste+".00"), "0.00", "0.00", "0.00", "","0", "0.00", "0.00", "0.00","0.00", "");
			
			giust=new GiustificativiModel(orePreviste, "0.00", ("-"+orePreviste+".00"), "0.00", "0.00", "0.00", "",statoRevisione, "0.00", "0.00", "0.00","0.00", "");
			
			for(DettaglioOreGiornaliere d:listaGiorni){
				formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);
				String formattedDate = formatter.format(giornoRiferimento);
				
				if(formattedDate.equals(d.getGiornoRiferimento().toString()))
				{
					giust=new GiustificativiModel(orePreviste, d.getTotaleOreGiorno(), d.getDeltaOreGiorno(), d.getOreViaggio(), d.getDeltaOreViaggio()
							, d.getOreAssenzeRecupero(), d.getGiustificativo(), statoRevisione, d.getOreStraordinario(), d.getOreFerie(), d.getOrePermesso(),d.getOreAbbuono(), d.getNoteAggiuntive());	
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
		String parzialeOreRecuperoMese="0.0"; //lo uso perchè nel caso in cui fossero negative in un mese non le devo considerare
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
			
			monteOreRecuperoTotale=p.getOreRecupero();//considero quelle inserite in anagrafica eventualmente in esubero
			if(p.getTipologiaOrario().compareTo("A")==0)
				tipoOrario="8";
			else tipoOrario=p.getTipologiaOrario();
			
			orePrevisteMese=ServerUtility.calcolaOreLavorativeMese(giornoRiferimento, tipoOrario, p.getSedeOperativa());
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
			monteOreRecuperoTotale= "0.00";
			parzialeOreRecuperoMese="0.00";
			List<DettaglioOreGiornaliere> listaGiorniM= new ArrayList<DettaglioOreGiornaliere>();
			listaMesi.clear();
			if(!p.getFoglioOreMeses().isEmpty()){
				listaMesi.addAll(p.getFoglioOreMeses());
				for(FoglioOreMese f:listaMesi){
					if(ServerUtility.isPrecedente(f.getMeseRiferimento(), data ))
					  if(f.getMeseRiferimento().compareTo("Feb2013")!=0 /*&& f.getMeseRiferimento().compareTo(data)!=0*/){//per omettere le ore inserite nel mese di prova di Feb2013 e quelle relative al mese in corso
						listaGiorniM.clear();
						if(!f.getDettaglioOreGiornalieres().isEmpty()){
							parzialeOreRecuperoMese="0.00";
							listaGiorniM.addAll(f.getDettaglioOreGiornalieres());
							for(DettaglioOreGiornaliere d:listaGiorniM){
								parzialeOreRecuperoMese=ServerUtility.aggiornaTotGenerale(parzialeOreRecuperoMese, d.getOreAssenzeRecupero());		
							}	
																			
							monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(monteOreRecuperoTotale,parzialeOreRecuperoMese);	
						}
					  }								
				}		
			}
			
			monteOreRecuperoTotale=ServerUtility.aggiornaMonteOreRecuperoTotale(monteOreRecuperoTotale, p.getOreRecupero());
			
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
	
	
	//RIEPILOGO CON TUTTI I DIPENDENTI SEPARATO PER SEDI
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoFoglioOreModel> getRiepilogoMeseFoglioOre(Date dataRif,
			String pm, String sede, String cognome) {//Solo sede poi eventualmente anche pm
		
		List<RiepilogoFoglioOreModel> listaGiorni= new ArrayList<RiepilogoFoglioOreModel>();
		List<RiepilogoFoglioOreModel> listaGiorniAll= new ArrayList<RiepilogoFoglioOreModel>();
		
		List<DettaglioOreGiornaliere> listaDettGiorno= new ArrayList<DettaglioOreGiornaliere>();
		List<FoglioOreMese> listaMesi=new ArrayList<FoglioOreMese>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioIntervalliIU> listaDtt= new ArrayList<DettaglioIntervalliIU>();
		
		RiepilogoFoglioOreModel giorno;
		
		String i1=" ";
		String u1=" ";
		String i2=" ";
		String u2=" ";
		String i3=" ";
		String u3=" ";
		String i4=" ";
		String u4=" ";
		
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
			
			if(cognome.compareTo("")==0)
				listaP=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede").setParameter("sede", sede).list();
			else
				listaP=(List<Personale>)session.createQuery("from Personale where cognome=:cognome").setParameter("cognome", cognome).list();
			
			String confermato="0";
			
			for(Personale p:listaP){
				
				if(!p.getFoglioOreMeses().isEmpty()){
					listaMesi.addAll(p.getFoglioOreMeses());
					for(FoglioOreMese f:listaMesi){
						if(f.getMeseRiferimento().compareTo(data)==0){
							listaDettGiorno.addAll(f.getDettaglioOreGiornalieres());
							break;
						}
					}
								
					//precompilo la lista di giorni con tutti i giorni lavorativi del mese in modo tale da segnalare una eventuale mancata compilazione
					int giorniMese=ServerUtility.getGiorniMese(mese, anno);
					for(int i=1; i<=giorniMese;i++){
						String g= new String();	
						String dataCompLow=new String();
						String dataCompUpp=new String();
						String giornoW= new String();
						String meseApp= new String();
						Date d= new Date();
						
						meseApp=mese.toLowerCase();
						
						if(i<10)
							g="0"+String.valueOf(i);
						else
							g=String.valueOf(i);
						
						dataCompLow=(g+"-"+meseApp+"-"+anno);
						//mese=mese.toLowerCase();
						dataCompUpp=(anno+"-"+meseApp+"-"+g);
						
						formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
						d=formatter.parse(dataCompLow);
						giornoW=d.toString().substring(0,3);
						if(giornoW.compareTo("Sun")!=0 && !ServerUtility.isFestivo(dataCompUpp, p.getSedeOperativa())){		
							giorno= new RiepilogoFoglioOreModel(p.getUsername() , p.getCognome()+" "+p.getNome(), data, dataCompLow, false, "0"
									,"","","","","","","","");
							listaGiorni.add(giorno);					
						}
					}
								
					//considero la lista di giorni eventualmente compilati
					for(DettaglioOreGiornaliere d:listaDettGiorno){
						int id=d.getIdDettaglioOreGiornaliere();						
						String day=new String();
												
						formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
						day=formatter.format(d.getGiornoRiferimento());
						
						if(d.getStatoRevisione().compareTo("0")!=0)
							confermato="2";
							
						listaDtt=(List<DettaglioIntervalliIU>)session.createQuery("from DettaglioIntervalliIU where id_dett_ore_giorno=:id")
								.setParameter("id", id).list();
						//listaDtt.addAll(d.getDettaglioIntervalliIUs());
						/*Collections.sort(listaDtt, new Comparator<DettaglioIntervalliIU>(){
							  public int compare(DettaglioIntervalliIU s1, DettaglioIntervalliIU s2) {
								  String p1=s1.getMovimento();
								  String p2=s2.getMovimento();
								  //p1=new StringBuffer(p1).reverse().toString();
								  //p2=new StringBuffer(p2).reverse().toString();
								  p1=p1.substring(1, 2)+p1.substring(0, 1);
								  p2=p2.substring(1, 2)+p2.substring(0, 1);								  
								 return p1.compareTo(p2);
							  }
						});*/
						
						
						/*						
						for(int i=0; i<listaDtt.size(); i++){
							String movimento= listaDtt.get(i).getMovimento();
							
							if (movimento.compareTo("i1")==0){
								i1=listaDtt.get(i).getOrario();
								continue;
							}
							
							if (movimento.compareTo("u1")==0) {
								u1=listaDtt.get(i).getOrario();
								continue;
							}
							
							if (movimento.compareTo("i2")==0){
								i2=listaDtt.get(i).getOrario();
								continue;
							}
							
							if (movimento.compareTo("u2")==0) {
								u2=listaDtt.get(i).getOrario();
								continue;
							}
							
							if (movimento.compareTo("i3")==0) {
								i3=listaDtt.get(i).getOrario();
								continue;
							}
							
							if (movimento.compareTo("u3")==0) {
								u3=listaDtt.get(i).getOrario();
								continue;
							}
							
							if (movimento.compareTo("i4")==0) {
								i4=listaDtt.get(i).getOrario();
								continue;
							}
							
							if (movimento.compareTo("u4")==0) {
								u4=listaDtt.get(i).getOrario();
								continue;
							}					
						}		
						*/
						
						if(listaDtt.size()>0)
							i1=listaDtt.get(0).getOrario();
						else i1="";
						if(listaDtt.size()>1)
							u1=listaDtt.get(1).getOrario();
						else u1="";
						if(listaDtt.size()>2)
							i2=listaDtt.get(2).getOrario();
						else i2="";
						if(listaDtt.size()>3)
							u2=listaDtt.get(3).getOrario();
						else u2="";
						if(listaDtt.size()>4)
							i3=listaDtt.get(4).getOrario();
						else i3="";
						if(listaDtt.size()>5)
							u3=listaDtt.get(5).getOrario();
						else u3="";
						if(listaDtt.size()>6)
							i4=listaDtt.get(6).getOrario();
						else i4="";
						if(listaDtt.size()>7)
							u4=listaDtt.get(7).getOrario();
						else u4="";			
						
						giorno= new RiepilogoFoglioOreModel(p.getUsername(), p.getCognome()+" "+p.getNome(), data, day, true, confermato,
								i1,u1,i2,u2,i3,u3,i4,u4);
						
						Iterator<RiepilogoFoglioOreModel> itr = listaGiorni.iterator();
						while(itr.hasNext()) {
					         RiepilogoFoglioOreModel g = itr.next();
					         String dd=g.get("giorno");
								if(dd.compareTo((String)giorno.get("giorno"))==0){
									int index=listaGiorni.indexOf(g);
									listaGiorni.set(index, giorno);
									break;
								}
					    }	
						
						i1="";
						i2="";
						i3="";
						i4="";
						u1="";
						u2="";
						u3="";
						u4="";
						
						listaDtt.clear();
					}
					
					listaGiorniAll.addAll(listaGiorni);
					listaGiorni.clear();			
				}
				
				listaMesi.clear();
				listaDettGiorno.clear();
				/*else {
					tx.commit();
					return listaGiorni; //sarà vuota
				}	*/								
			}	
			
			tx.commit();
			return listaGiorniAll;	
			
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
		Date dt= new Date();				
		String letteraGiorno="#";
		
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
					String meseApp= new String();
					//Date d= new Date();
					
					meseApp=(mese.substring(0,1).toLowerCase()+mese.substring(1,3));
					
					if(i<10)
						g="0"+String.valueOf(i);
					else
						g=String.valueOf(i);
					
					dataCompLow=(g+"-"+meseApp+"-"+anno);
					//dataCompUpp=(anno+"-"+mese+"-"+g);
					
					formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
					dt=formatter.parse(dataCompLow);					
					formatter=new SimpleDateFormat("EEE", Locale.ITALIAN);
					letteraGiorno=formatter.format(dt);
					letteraGiorno=letteraGiorno.substring(0, 1).toUpperCase();
						
					giorno= new RiepilogoFoglioOreModel(0,"", data, dataCompLow, letteraGiorno, "", Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"),
								Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"),Float.valueOf("0.00"),Float.valueOf("0.00"),
									"", "", "1");
					listaGiorni.add(giorno);					
					
				}
							
				//considero la lista di giorni eventualmente compilati
				for(DettaglioOreGiornaliere d:listaDettGiorno){
					
					String statoCompilazione="1"; //0 per OK, 1 non compilato, 2 mancano le commesse
					String day=new String();
					String oreTotali= "0.00";
					
					formatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
					day=formatter.format(d.getGiornoRiferimento());
					formatter=new SimpleDateFormat("EEE", Locale.ITALIAN);
					letteraGiorno=formatter.format(d.getGiornoRiferimento());
					letteraGiorno=letteraGiorno.substring(0, 1).toUpperCase();
					
					if(d.getDettaglioIntervalliIUs().size()>0 && ServerUtility.hasValue(d.getDettaglioIntervalliCommesses()))
						statoCompilazione="0";
					else 	
						if(!ServerUtility.hasValue(d.getDettaglioIntervalliCommesses()))
							statoCompilazione="2";										
							
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
					
					//Date gRiferimento=d.getGiornoRiferimento();
					formatter = new SimpleDateFormat("dd/MM/yy",Locale.ITALIAN);
					//String giornoR=formatter.format(gRiferimento);
					
					//@SuppressWarnings("unchecked")
					/*List<DettaglioTimbrature> listaT=(List<DettaglioTimbrature>)session.createQuery("from DettaglioTimbrature where numeroBadge=:nBadge and giorno=:giorno")
							.setParameter("nBadge", p.getNumeroBadge()).setParameter("giorno", giornoR).list();
						*/		
					giorno= new RiepilogoFoglioOreModel(d.getIdDettaglioOreGiornaliere(), "", data, day, letteraGiorno, p.getTipologiaOrario(), Float.valueOf(d.getTotaleOreGiorno()),  Float.valueOf(d.getOreViaggio()), 
							 Float.valueOf(d.getDeltaOreViaggio()),  Float.valueOf(oreTotali),  Float.valueOf(d.getOreFerie()),  Float.valueOf(d.getOrePermesso()) ,  Float.valueOf(d.getOreAssenzeRecupero()),
							 Float.valueOf(d.getOreStraordinario()), Float.valueOf(d.getOreAbbuono()), d.getGiustificativo(), d.getNoteAggiuntive(), statoCompilazione);
					
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
				String parzialeOreRecuperoMese="0.00";
				List<DettaglioOreGiornaliere> listaGiorniM= new ArrayList<DettaglioOreGiornaliere>();
				listaMesi.clear();
				if(!p.getFoglioOreMeses().isEmpty()){
					listaMesi.addAll(p.getFoglioOreMeses());
					for(FoglioOreMese f:listaMesi){
						if(ServerUtility.isPrecedente(f.getMeseRiferimento(), data ))
						  if(f.getMeseRiferimento().compareTo("Feb2013")!=0 && f.getMeseRiferimento().compareTo(data)!=0){//per omettere le ore inserite nel mese di prova di Feb2013 e quelle relative al mese in corso
							listaGiorniM.clear();
							if(!f.getDettaglioOreGiornalieres().isEmpty()){
								parzialeOreRecuperoMese="0.00";
								listaGiorniM.addAll(f.getDettaglioOreGiornalieres());
								for(DettaglioOreGiornaliere d:listaGiorniM){
									parzialeOreRecuperoMese=ServerUtility.aggiornaTotGenerale(parzialeOreRecuperoMese, d.getOreAssenzeRecupero());		
								}	
																				
								monteOreRecuperoTotale=ServerUtility.aggiornaTotGenerale(monteOreRecuperoTotale,parzialeOreRecuperoMese);	
							}
						 }
					}		
				}
				
				monteOreRecuperoTotale=ServerUtility.aggiornaMonteOreRecuperoTotale(monteOreRecuperoTotale, p.getOreRecupero());
				//aggiunta di un campo nella tabella fogliooremese che indiche le ore a recupero residue ai mesi precedenti
				giorno=new RiepilogoFoglioOreModel(0, "", data, "RESIDUI", "","", Float.valueOf(0),Float.valueOf(0) , Float.valueOf(0), Float.valueOf(0), 
						Float.valueOf(0), Float.valueOf(0), Float.valueOf(monteOreRecuperoTotale), Float.valueOf(0), Float.valueOf(0), "", "", "0");
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
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
				
				listaD=session.createSQLQuery("select distinct movimento, orario  " +
						"from dettaglio_timbrature d where d.numeroBadge=:badge and d.giorno=:giorno order by orario")
						.setParameter("badge", numeroBadge).setParameter("giorno", data).list();
				
				for(ListIterator iter = listaD.listIterator(); iter.hasNext(); ) {
					 Object[] row = (Object[]) iter.next();
					 
					 listaIntervalliTimbr.add((String) row[0]);
					 listaIntervalliTimbr.add((String) row[1]);
		         }
			
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

	
	@Override
	//public List<RiepilogoOreDipCommesseGiornaliero> checkOreIntervalliIUOreCommesse(String username, Date meseRiferimento)throws IllegalArgumentException {
	public Boolean checkOreIntervalliIUOreCommesse(String username, Date meseRiferimento)throws IllegalArgumentException {
		List<RiepilogoOreDipCommesseGiornaliero> listaG= new ArrayList<RiepilogoOreDipCommesseGiornaliero>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaD= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalliC= new ArrayList<DettaglioIntervalliCommesse>();
				
		List<AssociazionePtoA> listaAssociazioniPA= new ArrayList<AssociazionePtoA>();
		List<DettaglioIntervalliCommesse> listaCommesse= new ArrayList<DettaglioIntervalliCommesse>(); //verrà usata per considerare tutte le commesse con intervalli inseriti e non solo quelle associate al momento
		
		FoglioOreMese fM=null;
		Personale p= new Personale();
	
		String totaleOreC= "0.00";
		Boolean check=false;
		//totali usati per il confronto ore totali intervalli IU e intervalli commesse
		String totaleOreDaCommesse= "0.00";
		String totaleOreDaIU="0.00";
		
		Date giorno= new Date();  
		String dipendente= new String();
		String letteraGiorno;
		
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
			
			//se non ci sono commesse associate ritorno 1 record con 0 e 0
			if(!p.getAssociazionePtoas().isEmpty()){
			
				dipendente= p.getCognome()+" "+p.getNome();
			
				listaF.addAll(p.getFoglioOreMeses());
				listaAssociazioniPA.addAll(p.getAssociazionePtoas());
			
				for(FoglioOreMese f:listaF){//scorro i mesi per trovare il foglio ore desiderato
					if(f.getMeseRiferimento().compareTo(data)==0){
						fM=f;
						break;
					}
					
				}
			
				if(fM!=null)
					listaD.addAll(fM.getDettaglioOreGiornalieres()); //prendo tutti i giorni del mese
					
				for(DettaglioOreGiornaliere d: listaD ){//scorro i giorni del mese e calcolo il totale ore per ogni commessa selezionata
					giorno= d.getGiornoRiferimento();
					formatter = new SimpleDateFormat("dd-MMM-yyy",Locale.ITALIAN) ; 
					String giornoF=formatter.format(giorno);
					formatter=new SimpleDateFormat("EEE", Locale.ITALIAN);
					letteraGiorno=formatter.format(giorno);
					
					if(!d.getDettaglioIntervalliCommesses().isEmpty()){
						listaIntervalliC.addAll(d.getDettaglioIntervalliCommesses());	
						for(DettaglioIntervalliCommesse dd: listaIntervalliC){
							if(ServerUtility.isNotIncludedCommessa(listaCommesse, dd))							
								listaCommesse.add(dd);
						}					
					}
					
					//calcolo il totale ore ricavato dagli intervalli IU della bollatrice
					if(!d.getDettaglioIntervalliIUs().isEmpty()){
						totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, d.getTotaleOreGiorno());
						totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, d.getOreViaggio());					
					}
													
					for(DettaglioIntervalliCommesse dett:listaIntervalliC){							
						if(Float.valueOf(ServerUtility.aggiornaTotGenerale(dett.getOreLavorate(),  dett.getOreViaggio()))>0){
														
							RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero(p.getUsername(),dett.getNumeroCommessa()+"."+dett.getEstensioneCommessa()
								, dipendente, giornoF, letteraGiorno,Float.valueOf(dett.getOreLavorate()), Float.valueOf(dett.getOreViaggio()), Float.valueOf(ServerUtility.aggiornaTotGenerale(dett.getOreLavorate(),  dett.getOreViaggio())), "S");
						
							listaG.add(riep);																	
						}
					}					
					listaIntervalliC.clear();							
				}	
			
				//elaboro un record per i totali per ogni commessa
				//for(AssociazionePtoA ass:listaAssociazioniPA){
				for(DettaglioIntervalliCommesse dc: listaCommesse){
					String commessa= dc.getNumeroCommessa() +"."+ dc.getEstensioneCommessa();
									
					for(RiepilogoOreDipCommesseGiornaliero g:listaG){
						if(g.getNumeroCommessa().compareTo(commessa)==0){
						
							String oreTotali=String.valueOf(g.getTotOre());
							if(oreTotali.substring(oreTotali.indexOf(".")+1, oreTotali.length()).length()==1)
								oreTotali=oreTotali+"0";
						
							totaleOreC=ServerUtility.aggiornaTotGenerale(totaleOreC, oreTotali);
						}
					}
							
					totaleOreDaCommesse=ServerUtility.aggiornaTotGenerale(totaleOreDaCommesse, totaleOreC);
				
					totaleOreC= "0.00";
				}
			/*
				RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero("","",
						"", "",  Float.valueOf("0.00"), Float.valueOf(totaleOreDaIU),Float.valueOf(totaleOreDaCommesse), "S");
				listaG.add(riep);*/
				
				if (totaleOreDaIU.compareTo(totaleOreDaCommesse)==0)
					check=true;			}
			else{
				/*RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero("","",
						"", "",  Float.valueOf("0.00"), Float.valueOf("0.00"),Float.valueOf("0.00"), "S");
				listaG.add(riep);*/
				check=true;
			}
						
			tx.commit();
			return check;
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}
	}
	
	
	@Override
	public List<CommentiModel> getAllCommenti(String utente) {
		List<Commenti> listaC= new ArrayList<Commenti>();
		List<CommentiModel> listaCommentiM= new ArrayList<CommentiModel>();
		CommentiModel cm;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
			
		try {
							
			tx=session.beginTransaction();
				
			listaC=(List<Commenti>)session.createQuery("from Commenti where username=:username").setParameter("username", utente).list();
				
			tx.commit();
			
			for(Commenti c:listaC){
				cm=new CommentiModel(c.getIdCommenti(),c.getUsername(), c.getDataRichiesta(), c.getTesto(), Boolean.valueOf(c.getEditated()));
				listaCommentiM.add(cm);
			}
			
			return listaCommentiM;
				
		} catch (Exception e) {
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
	public List<CommentiModel> getAllCommenti() throws IllegalArgumentException{

		List<Commenti> listaC= new ArrayList<Commenti>();
		List<CommentiModel> listaCommentiM= new ArrayList<CommentiModel>();
		CommentiModel cm;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
			
		try {
							
			tx=session.beginTransaction();
				
			listaC=(List<Commenti>)session.createQuery("from Commenti").list();
				
			tx.commit();
			
			for(Commenti c:listaC){
				cm=new CommentiModel(c.getIdCommenti(),c.getUsername(), c.getDataRichiesta(), c.getTesto(), Boolean.valueOf(c.getEditated()));
				listaCommentiM.add(cm);
			}
			
			return listaCommentiM;
				
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}			
	}

	@Override
	public boolean deleteCommento(int id) {
		Commenti c= new Commenti();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
			
		try {
							
			tx=session.beginTransaction();
				
			c=(Commenti)session.createQuery("from Commenti where id=:id").setParameter("id", id).uniqueResult();
			
			session.delete(c);
			
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
	
	
	@Override
	public boolean confermaEditCommenti(int i) throws IllegalArgumentException {
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		Commenti cm= new Commenti();
		try {
			
			tx=session.beginTransaction();
			
			cm=(Commenti)session.createQuery("from Commenti where idCommenti=:id").setParameter("id", i).uniqueResult();
			cm.setEditated("true");			
			
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
	
	
	@Override
	public List<RiepilogoMeseGiornalieroModel> getRiepilogoMensileDettagliatoCommesseHorizontalLayout(
			String dipendente, String data) throws IllegalArgumentException {

		List<RiepilogoMeseGiornalieroModel> listaR= new ArrayList<RiepilogoMeseGiornalieroModel>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaG= new ArrayList<DettaglioOreGiornaliere>();
		List<AssociazionePtoA> listaAssociazioniPtoC= new ArrayList<AssociazionePtoA>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		RiepilogoMeseGiornalieroModel riep;
		List<IntervalliCommesseModel> listaDatiCommessePerGiorno= new ArrayList<IntervalliCommesseModel>();
		List<String> listaGiustificativiPerGiorno= new ArrayList<String>();
		List<String> listaLettereGiorno= new ArrayList<String>();
		Commessa comm= new Commessa();
		String numeroCommessa= new String();
		String estensioneCommessa= new String();
		
		Personale p= new Personale();
		
		String giorno1="0.00"; 	String giorno2="0.00";	String giorno3="0.00";	String giorno4="0.00";	String giorno5="0.00";
		String giorno6="0.00";	String giorno7="0.00"; 	String giorno8="0.00"; 	String giorno9="0.00"; 	String giorno10="0.00";
		String giorno11="0.00";	String giorno12="0.00";	String giorno13="0.00";	String giorno14="0.00";	String giorno15="0.00";
		String giorno16="0.00";	String giorno17="0.00";	String giorno18="0.00";	String giorno19="0.00";	String giorno20="0.00";
		String giorno21="0.00";	String giorno22="0.00";	String giorno23="0.00";	String giorno24="0.00";	String giorno25="0.00";
		String giorno26="0.00";	String giorno27="0.00";	String giorno28="0.00";	String giorno29="0.00";	String giorno30="0.00";
		String giorno31="0.00";
				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		String mese=ServerUtility.traduciMeseToIt(data.substring(0,3));
		String anno=data.substring(3,7);
		
		String meseN;
		String giornoR;
		String totOreLavoro="0.00";
		String totOreViaggio="0.00";
		
		try {
			tx=session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", dipendente).uniqueResult();
						
			meseN=ServerUtility.traduciMeseToNumber(mese.toLowerCase());
			
			listaF.addAll(p.getFoglioOreMeses());
			for(FoglioOreMese f: listaF)
				if(f.getMeseRiferimento().compareTo(mese+anno)==0){
					listaG.addAll(f.getDettaglioOreGiornalieres());
					break;
			}
			
			//Mi prendo le commesse associate al dipendente
			
			listaAssociazioniPtoC.addAll(p.getAssociazionePtoas());
			for(AssociazionePtoA ass: p.getAssociazionePtoas()){
				numeroCommessa=ass.getAttivita().getCommessa().getNumeroCommessa();
				estensioneCommessa=ass.getAttivita().getCommessa().getEstensione();
				comm=(Commessa)session.createQuery("from Commessa where numeroCommessa=:ncommessa and estensione=:nestensione").setParameter("ncommessa", numeroCommessa)
						.setParameter("nestensione", estensioneCommessa).uniqueResult();
				
				listaCommesse.add(comm);
			}
			
			/*
			for(DettaglioIntervalliCommesse dc: listaG.get(0).getDettaglioIntervalliCommesses()){
				//listaCommessePerDip.add(dc.getNumeroCommessa()+"."+dc.getEstensioneCommessa());
				comm=(Commessa)session.createQuery("from Commessa where numeroCommessa=:ncommessa and estensione=:nestensione").setParameter("ncommessa", dc.getNumeroCommessa())
						.setParameter("nestensione", dc.getEstensioneCommessa()).uniqueResult();
				
				listaCommesse.add(comm);
			}
			*/
			//Per ogni commessa considerata elaboro un record di 31 giorni con le ore
			//for(String commessa:listaCommessePerDip){
			for(Commessa c:listaCommesse){
				for(int i=1; i<=31; i++){					
					String ngiorno=String.valueOf(i);
					if (ngiorno.length()==1)
						ngiorno="0"+ngiorno;							
					giornoR=anno+"-"+meseN+"-"+ngiorno;									
					//listaDatiCommessePerGiorno.add(ServerUtility.checkDatiGiornoCommessa(listaG, giornoR, commessa));
					listaDatiCommessePerGiorno.add(ServerUtility.checkDatiGiornoCommessa(listaG, giornoR, c));
				}
				
				for(IntervalliCommesseModel intrv:listaDatiCommessePerGiorno){
					
					totOreLavoro=ServerUtility.aggiornaTotGenerale(totOreLavoro, intrv.getOreLavoro());
					totOreViaggio=ServerUtility.aggiornaTotGenerale(totOreViaggio, intrv.getOreViaggio());
				}
					
				String somma=totOreLavoro;
				somma=ServerUtility.aggiornaTotGenerale(somma, totOreViaggio);
				if(somma.compareTo("0.00")!=0){
				//con la commessa selezionata creo l'entry sul model con tutti i giorni per ore lavoro e ore viaggio
				riep= new RiepilogoMeseGiornalieroModel(p.getUsername(), p.getCognome()+" "+p.getNome(), c.getNumeroCommessa()+"."+c.getEstensione()+" ("+c.getDenominazioneAttivita()+" )",
						listaDatiCommessePerGiorno.get(0).getOreLavoro(), listaDatiCommessePerGiorno.get(1).getOreLavoro(), listaDatiCommessePerGiorno.get(2).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(3).getOreLavoro(), listaDatiCommessePerGiorno.get(4).getOreLavoro(), listaDatiCommessePerGiorno.get(5).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(6).getOreLavoro(), listaDatiCommessePerGiorno.get(7).getOreLavoro(), listaDatiCommessePerGiorno.get(8).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(9).getOreLavoro(), listaDatiCommessePerGiorno.get(10).getOreLavoro(), listaDatiCommessePerGiorno.get(11).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(12).getOreLavoro(), listaDatiCommessePerGiorno.get(13).getOreLavoro(), listaDatiCommessePerGiorno.get(14).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(15).getOreLavoro(), listaDatiCommessePerGiorno.get(16).getOreLavoro(), listaDatiCommessePerGiorno.get(17).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(18).getOreLavoro(), listaDatiCommessePerGiorno.get(19).getOreLavoro(), listaDatiCommessePerGiorno.get(20).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(21).getOreLavoro(), listaDatiCommessePerGiorno.get(22).getOreLavoro(), listaDatiCommessePerGiorno.get(23).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(24).getOreLavoro(), listaDatiCommessePerGiorno.get(25).getOreLavoro(), listaDatiCommessePerGiorno.get(26).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(27).getOreLavoro(), listaDatiCommessePerGiorno.get(28).getOreLavoro(), listaDatiCommessePerGiorno.get(29).getOreLavoro(), 
						listaDatiCommessePerGiorno.get(30).getOreLavoro(), totOreLavoro);
				
				listaR.add(riep);
				
				riep= new RiepilogoMeseGiornalieroModel(p.getUsername(), p.getCognome()+" "+p.getNome(), c.getNumeroCommessa()+"."+c.getEstensione()+"(viaggio)",
						listaDatiCommessePerGiorno.get(0).getOreViaggio(), listaDatiCommessePerGiorno.get(1).getOreViaggio(), listaDatiCommessePerGiorno.get(2).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(3).getOreViaggio(), listaDatiCommessePerGiorno.get(4).getOreViaggio(), listaDatiCommessePerGiorno.get(5).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(6).getOreViaggio(), listaDatiCommessePerGiorno.get(7).getOreViaggio(), listaDatiCommessePerGiorno.get(8).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(9).getOreViaggio(), listaDatiCommessePerGiorno.get(10).getOreViaggio(), listaDatiCommessePerGiorno.get(11).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(12).getOreViaggio(), listaDatiCommessePerGiorno.get(13).getOreViaggio(), listaDatiCommessePerGiorno.get(14).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(15).getOreViaggio(), listaDatiCommessePerGiorno.get(16).getOreViaggio(), listaDatiCommessePerGiorno.get(17).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(18).getOreViaggio(), listaDatiCommessePerGiorno.get(19).getOreViaggio(), listaDatiCommessePerGiorno.get(20).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(21).getOreViaggio(), listaDatiCommessePerGiorno.get(22).getOreViaggio(), listaDatiCommessePerGiorno.get(23).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(24).getOreViaggio(), listaDatiCommessePerGiorno.get(25).getOreViaggio(), listaDatiCommessePerGiorno.get(26).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(27).getOreViaggio(), listaDatiCommessePerGiorno.get(28).getOreViaggio(), listaDatiCommessePerGiorno.get(29).getOreViaggio(), 
						listaDatiCommessePerGiorno.get(30).getOreViaggio(), totOreViaggio);
				
				listaR.add(riep);
				}							
				listaDatiCommessePerGiorno.clear();
				totOreLavoro="0.00";
				totOreViaggio="0.00";								
			}	
			
			//Totale giornaliero per le commesse
			String totaleComm="0.00";
			for(RiepilogoMeseGiornalieroModel rM:listaR){
				
				giorno1=ServerUtility.aggiornaTotGenerale(giorno1, (String)rM.get("giorno1"));
				giorno2=ServerUtility.aggiornaTotGenerale(giorno2, (String)rM.get("giorno2"));
				giorno3=ServerUtility.aggiornaTotGenerale(giorno3, (String)rM.get("giorno3"));
				giorno4=ServerUtility.aggiornaTotGenerale(giorno4, (String)rM.get("giorno4"));
				giorno5=ServerUtility.aggiornaTotGenerale(giorno5, (String)rM.get("giorno5"));
				giorno6=ServerUtility.aggiornaTotGenerale(giorno6, (String)rM.get("giorno6"));
				giorno7=ServerUtility.aggiornaTotGenerale(giorno7, (String)rM.get("giorno7"));
				giorno8=ServerUtility.aggiornaTotGenerale(giorno8, (String)rM.get("giorno8"));
				giorno9=ServerUtility.aggiornaTotGenerale(giorno9, (String)rM.get("giorno9"));
				giorno10=ServerUtility.aggiornaTotGenerale(giorno10, (String)rM.get("giorno10"));
				giorno11=ServerUtility.aggiornaTotGenerale(giorno11, (String)rM.get("giorno11"));
				giorno12=ServerUtility.aggiornaTotGenerale(giorno12, (String)rM.get("giorno12"));
				giorno13=ServerUtility.aggiornaTotGenerale(giorno13, (String)rM.get("giorno13"));
				giorno14=ServerUtility.aggiornaTotGenerale(giorno14, (String)rM.get("giorno14"));
				giorno15=ServerUtility.aggiornaTotGenerale(giorno15, (String)rM.get("giorno15"));
				giorno16=ServerUtility.aggiornaTotGenerale(giorno16, (String)rM.get("giorno16"));
				giorno17=ServerUtility.aggiornaTotGenerale(giorno17, (String)rM.get("giorno17"));
				giorno18=ServerUtility.aggiornaTotGenerale(giorno18, (String)rM.get("giorno18"));
				giorno19=ServerUtility.aggiornaTotGenerale(giorno19, (String)rM.get("giorno19"));
				giorno20=ServerUtility.aggiornaTotGenerale(giorno20, (String)rM.get("giorno20"));
				giorno21=ServerUtility.aggiornaTotGenerale(giorno21, (String)rM.get("giorno21"));
				giorno22=ServerUtility.aggiornaTotGenerale(giorno22, (String)rM.get("giorno22"));
				giorno23=ServerUtility.aggiornaTotGenerale(giorno23, (String)rM.get("giorno23"));
				giorno24=ServerUtility.aggiornaTotGenerale(giorno24, (String)rM.get("giorno24"));
				giorno25=ServerUtility.aggiornaTotGenerale(giorno25, (String)rM.get("giorno25"));
				giorno26=ServerUtility.aggiornaTotGenerale(giorno26, (String)rM.get("giorno26"));
				giorno27=ServerUtility.aggiornaTotGenerale(giorno27, (String)rM.get("giorno27"));
				giorno28=ServerUtility.aggiornaTotGenerale(giorno28, (String)rM.get("giorno28"));
				giorno29=ServerUtility.aggiornaTotGenerale(giorno29, (String)rM.get("giorno29"));
				giorno30=ServerUtility.aggiornaTotGenerale(giorno30, (String)rM.get("giorno30"));
				giorno31=ServerUtility.aggiornaTotGenerale(giorno31, (String)rM.get("giorno31"));
				totaleComm=ServerUtility.aggiornaTotGenerale(totaleComm, (String)rM.get("totale"));
			}
			
			
			riep=new RiepilogoMeseGiornalieroModel("", "", "_Tot.Giorno", giorno1, giorno2, giorno3, giorno4, giorno5, giorno6, giorno7, giorno8,
					giorno9, giorno10, giorno11, giorno12, giorno13, giorno14, giorno15, giorno16, giorno17, giorno18, giorno19, giorno20, giorno21, 
					giorno22, giorno23, giorno24, giorno25, giorno26, giorno27, giorno28, giorno29, giorno30, giorno31, totaleComm);
			listaR.add(riep);
			
			
			//Lista dei Giustificativi e lettera giorno
			String letteraGiorno;
			String g;
			for(int i=1; i<=31; i++){				
				String ngiorno=String.valueOf(i);
				if (ngiorno.length()==1)
					ngiorno="0"+ngiorno;					
				giornoR=anno+"-"+meseN+"-"+ngiorno;							
				listaGiustificativiPerGiorno.add(ServerUtility.checkGiustificativiGiornoCommessa(listaG, giornoR));
				
				//ricavo la lettera del giorno
				g=anno+"-"+mese.toLowerCase()+"-"+ngiorno;
				Date dt= new Date();
				DateFormat formatter=new SimpleDateFormat("yyyy-MMM-dd",Locale.ITALIAN);
				dt=formatter.parse(g);	
				formatter=new SimpleDateFormat("EEE", Locale.ITALIAN);
				letteraGiorno=formatter.format(dt);
				letteraGiorno=letteraGiorno.substring(0, 1).toUpperCase();				
				listaLettereGiorno.add(letteraGiorno);
			}			
			riep= new RiepilogoMeseGiornalieroModel(p.getUsername(), p.getCognome()+" "+p.getNome(),"__Giustificativi",
					listaGiustificativiPerGiorno.get(0), listaGiustificativiPerGiorno.get(1), listaGiustificativiPerGiorno.get(2), 
					listaGiustificativiPerGiorno.get(3), listaGiustificativiPerGiorno.get(4), listaGiustificativiPerGiorno.get(5), 
					listaGiustificativiPerGiorno.get(6), listaGiustificativiPerGiorno.get(7), listaGiustificativiPerGiorno.get(8), 
					listaGiustificativiPerGiorno.get(9), listaGiustificativiPerGiorno.get(10), listaGiustificativiPerGiorno.get(11), 
					listaGiustificativiPerGiorno.get(12), listaGiustificativiPerGiorno.get(13), listaGiustificativiPerGiorno.get(14), 
					listaGiustificativiPerGiorno.get(15), listaGiustificativiPerGiorno.get(16), listaGiustificativiPerGiorno.get(17), 
					listaGiustificativiPerGiorno.get(18), listaGiustificativiPerGiorno.get(19), listaGiustificativiPerGiorno.get(20), 
					listaGiustificativiPerGiorno.get(21), listaGiustificativiPerGiorno.get(22), listaGiustificativiPerGiorno.get(23), 
					listaGiustificativiPerGiorno.get(24), listaGiustificativiPerGiorno.get(25), listaGiustificativiPerGiorno.get(26), 
					listaGiustificativiPerGiorno.get(27), listaGiustificativiPerGiorno.get(28), listaGiustificativiPerGiorno.get(29), 
					listaGiustificativiPerGiorno.get(30), "");			
			listaR.add(riep);		
			
			//creo il record con la lettera del giorno
			riep=new RiepilogoMeseGiornalieroModel("", ""," "+data, listaLettereGiorno.get(0),  listaLettereGiorno.get(1),  listaLettereGiorno.get(2),  listaLettereGiorno.get(3),
					 listaLettereGiorno.get(4),  listaLettereGiorno.get(5),  listaLettereGiorno.get(6),  listaLettereGiorno.get(7),  listaLettereGiorno.get(8),  listaLettereGiorno.get(9),
					 listaLettereGiorno.get(10),  listaLettereGiorno.get(11),  listaLettereGiorno.get(12),  listaLettereGiorno.get(13),  listaLettereGiorno.get(14),
					 listaLettereGiorno.get(15),  listaLettereGiorno.get(16),  listaLettereGiorno.get(17),  listaLettereGiorno.get(18),  listaLettereGiorno.get(19),
					 listaLettereGiorno.get(20),  listaLettereGiorno.get(21),  listaLettereGiorno.get(22),  listaLettereGiorno.get(23),  listaLettereGiorno.get(24),
					 listaLettereGiorno.get(25),  listaLettereGiorno.get(26),  listaLettereGiorno.get(27),  listaLettereGiorno.get(28),  listaLettereGiorno.get(29),
					 listaLettereGiorno.get(30), "");
			listaR.add(riep);
			
			
			//Calcolo il totale delle ore I/U
			String totaleOreDaIU="0.00";
			for(DettaglioOreGiornaliere dtt: listaG){
				if(!dtt.getDettaglioIntervalliIUs().isEmpty()){
					totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, dtt.getTotaleOreGiorno());
					totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, dtt.getOreViaggio());					
				}
			}
			riep=new RiepilogoMeseGiornalieroModel(totaleOreDaIU, totaleComm, "", giorno1, giorno2, giorno3, giorno4, giorno5,
					giorno6, giorno7, giorno8, giorno9, giorno10, giorno11, giorno12, giorno13, giorno14, giorno15, giorno16, giorno17, 
					giorno18, giorno19, giorno20, giorno21, giorno22, giorno23, giorno24, giorno25, giorno26, giorno27, giorno28, giorno29, giorno30, giorno31, "");
			listaR.add(riep);	
			
			tx.commit();			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}		
		return listaR;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoMeseGiornalieroModel> getRiepilogoMensileDettagliatoHorizontalLayout(
			String sede, String data) throws IllegalArgumentException{
		
		List<RiepilogoMeseGiornalieroModel> listaR= new ArrayList<RiepilogoMeseGiornalieroModel>();
		List<Personale> listaP= new ArrayList<Personale>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaG= new ArrayList<DettaglioOreGiornaliere>();
		List<String> listaCodiciPerOgniGiorno;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		String mese=data.substring(0,3);
		String anno=data.substring(3,7);
		String meseN;
		String giornoR;
				
		try {
			tx=session.beginTransaction();
			
			listaP=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede and statoRapporto=:rapporto")
					.setParameter("sede", sede).setParameter("rapporto", "Attivo").list();
			
			//ngiorni=ServerUtility.getGiorniMese(mese, anno);
			meseN=ServerUtility.traduciMeseToNumber(mese.toLowerCase());
			
			for(Personale p:listaP){
				
				listaCodiciPerOgniGiorno= new ArrayList<String>();
				
				listaF.addAll(p.getFoglioOreMeses());
				for(FoglioOreMese f: listaF)
					if(f.getMeseRiferimento().compareTo(data)==0){
						listaG.addAll(f.getDettaglioOreGiornalieres());
						break;
				}
												
				for(int i=1; i<=31; i++){
					
					String statoCompilazione="1";			
					
					String ngiorno=String.valueOf(i);
					if (ngiorno.length()==1)
						ngiorno="0"+ngiorno;
							
					giornoR=anno+"-"+meseN+"-"+ngiorno;
								
					statoCompilazione=ServerUtility.exsistGiorno(listaG, giornoR);
					if(statoCompilazione.compareTo("0")==0)
						listaCodiciPerOgniGiorno.add("0");
					else
						if(statoCompilazione.compareTo("2")==0)
							listaCodiciPerOgniGiorno.add("2");	
						else
							listaCodiciPerOgniGiorno.add("1");
				}
				
				
				RiepilogoMeseGiornalieroModel riep= new RiepilogoMeseGiornalieroModel(p.getUsername(), p.getCognome()+" "+p.getNome(), "",
						listaCodiciPerOgniGiorno.get(0), listaCodiciPerOgniGiorno.get(1), listaCodiciPerOgniGiorno.get(2), 
						listaCodiciPerOgniGiorno.get(3), listaCodiciPerOgniGiorno.get(4), listaCodiciPerOgniGiorno.get(5), 
						listaCodiciPerOgniGiorno.get(6), listaCodiciPerOgniGiorno.get(7), listaCodiciPerOgniGiorno.get(8), 
						listaCodiciPerOgniGiorno.get(9), listaCodiciPerOgniGiorno.get(10), listaCodiciPerOgniGiorno.get(11), 
						listaCodiciPerOgniGiorno.get(12), listaCodiciPerOgniGiorno.get(13), listaCodiciPerOgniGiorno.get(14), 
						listaCodiciPerOgniGiorno.get(15), listaCodiciPerOgniGiorno.get(16), listaCodiciPerOgniGiorno.get(17), 
						listaCodiciPerOgniGiorno.get(18), listaCodiciPerOgniGiorno.get(19), listaCodiciPerOgniGiorno.get(20), 
						listaCodiciPerOgniGiorno.get(21), listaCodiciPerOgniGiorno.get(22), listaCodiciPerOgniGiorno.get(23), 
						listaCodiciPerOgniGiorno.get(24), listaCodiciPerOgniGiorno.get(25), listaCodiciPerOgniGiorno.get(26), 
						listaCodiciPerOgniGiorno.get(27), listaCodiciPerOgniGiorno.get(28), listaCodiciPerOgniGiorno.get(29), 
						listaCodiciPerOgniGiorno.get(30), "");
				
				listaG.clear();
				listaF.clear();
				listaCodiciPerOgniGiorno.clear();
				listaR.add(riep);
			}			
			tx.commit();
			return listaR;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}	
			
	}
	
	
	//salvo i dati del dipendente sul giorno indicato per la commessa con le ore indicate
	@Override
	public boolean elaboraDatiOreCollaboratori(RiepilogoOreDipCommesseGiornaliero g, Date giornoRiferimento)
			throws IllegalArgumentException {
				
		Personale p= new Personale();
		IntervalliCommesseModel intC;
		List<IntervalliCommesseModel> listaIntervalli= new ArrayList<IntervalliCommesseModel>();
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
	    String username;
	    username=(String)g.get("username");
		//int idCommessa=g.get("idCommessa");
		//String oreLavoro=d.format(g.getOreLavoro());
		//String oreViaggio=d.format(g.getOreViaggio());
		//String username;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			username=p.getUsername();
			tx.commit();
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}
		
		intC= new IntervalliCommesseModel(g.getNumeroCommessa(), d.format(g.getOreLavoro()), 
				d.format(g.getOreViaggio()), "",  "", "", "");
		listaIntervalli.add(intC);
		insertFoglioOreGiorno(username, giornoRiferimento, listaIntervalli);
	    
		return true;
	}	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoOreDipCommesseGiornaliero> getDatiOreCollaboratori(
			String pm, Date giornoRiferimento) throws IllegalArgumentException {
		
		Personale p= new Personale();
		List<RiepilogoOreDipCommesseGiornaliero> listaRiep= new ArrayList<RiepilogoOreDipCommesseGiornaliero>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		
		RiepilogoOreDipCommesseGiornaliero riep;
		
		String numeroCommessa, numeroCommessaRif;;
		String anno, mese , data;
		String dataGiornoRif, dataComp;//usate per ilo confronto tra giorno scelto eed eventuale giorno già compilato
		Float oreViaggio=(float)0.0;
		Float oreLavoro=(float)0.0;
		
		DateFormat formatter = new SimpleDateFormat("yyyy") ; 
		anno=formatter.format(giornoRiferimento);
		formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));	
		data=(mese+anno);
		
		formatter = new SimpleDateFormat("yyyy-MMM-dd",Locale.ITALIAN);
		dataGiornoRif = formatter.format(giornoRiferimento);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
		
			listaC=(List<Commessa>)session.createQuery("from Commessa where matricolaPM=:pm and statoCommessa=:stato")
					.setParameter("pm", pm).setParameter("stato", "Aperta").list();
			
			for(Commessa c: listaC){
				numeroCommessaRif=c.getNumeroCommessa()+"."+c.getEstensione();
				//nel giorno indicato cerca un intervallo commesse 
				
				for(Attivita a: c.getAttivitas()){
					
					for(AssociazionePtoA ass: a.getAssociazionePtoas()){
						
						if(ass.getPersonale().getTipologiaLavoratore().compareTo("C")==0 || ass.getPersonale().getSede().compareTo("F")==0){
						
							p=ass.getPersonale();
							for(FoglioOreMese fm:p.getFoglioOreMeses())
								if(fm.getMeseRiferimento().compareTo(data)==0)
									for(DettaglioOreGiornaliere d:fm.getDettaglioOreGiornalieres()){
										dataComp=formatter.format(d.getGiornoRiferimento());
										if(dataComp.compareTo(dataGiornoRif)==0){
											//prelevo i dati di ore sulla commessa desiderata
											for(DettaglioIntervalliCommesse dc:d.getDettaglioIntervalliCommesses()){
												numeroCommessa=dc.getNumeroCommessa()+"."+dc.getEstensioneCommessa();
												if(numeroCommessa.compareTo(numeroCommessaRif)==0){
													oreLavoro=Float.valueOf(dc.getOreLavorate());
													oreViaggio=Float.valueOf(dc.getOreViaggio());
													break;
												}												
												
											}
											
										}
													
									}
														
							riep=new RiepilogoOreDipCommesseGiornaliero(p.getUsername(), c.getNumeroCommessa()+"."+c.getEstensione(), 
									p.getCognome()+" "+p.getNome(), "", "", oreLavoro, oreViaggio, (float)0.0, "");
							
							listaRiep.add(riep);
							
							oreLavoro=(float)0.0;
							oreViaggio=(float)0.0;									
						}
					}
					
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
			
			DateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy") ; 
			String data=formatter.format(d);
			
			try {
				c.setTesto(testo);
				c.setUsername(username);
				c.setDataRichiesta(data);
				c.setEditated("false");
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
				int idDip, String username, Date meseRiferimento) throws IllegalArgumentException {//a seconda del Layout che la richiama mi ritrovo username o idDip
			
			List<RiepilogoOreDipCommesseGiornaliero> listaG= new ArrayList<RiepilogoOreDipCommesseGiornaliero>();
			List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
			List<DettaglioOreGiornaliere> listaD= new ArrayList<DettaglioOreGiornaliere>();
			List<DettaglioIntervalliCommesse> listaIntervalliC= new ArrayList<DettaglioIntervalliCommesse>();
			//List<DettaglioIntervalliIU> listaIntervalliIU=new ArrayList<DettaglioIntervalliIU>();
			
			List<AssociazionePtoA> listaAssociazioniPA= new ArrayList<AssociazionePtoA>();
			List<DettaglioIntervalliCommesse> listaCommesse= new ArrayList<DettaglioIntervalliCommesse>(); //verrà usata per considerare tutte le commesse con intervalli inseriti e non solo quelle associate al momento
			
			FoglioOreMese fM=new FoglioOreMese();
			Personale p= new Personale();
			//Commessa c= new Commessa();
			
			String totaleOreLavoroC= "0.00";
			String totaleOreViaggioC= "0.00";
			String totaleOreC= "0.00";
			
			//totali usati per il confronto ore totali intervalli IU e intervalli commesse
			String totaleOreDaCommesse= "0.00";
			String totaleOreDaIU="0.00";
			
			Date giorno= new Date();  
			String dipendente= new String();
			String letteraGiorno;
			
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
				
				if(username.compareTo("")==0)
					p=(Personale)session.createQuery("from Personale where ID_PERSONALE=:id").setParameter("id", idDip).uniqueResult();
				else
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
				
				if(fM!=null)
					listaD.addAll(fM.getDettaglioOreGiornalieres()); //prendo tutti i giorni del mese
						
				for(DettaglioOreGiornaliere d: listaD ){//scorro i giorni del mese e calcolo il totale ore per ogni commessa selezionata
						giorno= d.getGiornoRiferimento();
						formatter = new SimpleDateFormat("dd-MMM-yyy",Locale.ITALIAN) ; 
						String giornoF=formatter.format(giorno);
						formatter=new SimpleDateFormat("EEE", Locale.ITALIAN);
						letteraGiorno=formatter.format(giorno);
						letteraGiorno=letteraGiorno.substring(0, 1).toUpperCase();
						
						if(!d.getDettaglioIntervalliCommesses().isEmpty()){
							listaIntervalliC.addAll(d.getDettaglioIntervalliCommesses());	
							for(DettaglioIntervalliCommesse dd: listaIntervalliC){
								if(ServerUtility.isNotIncludedCommessa(listaCommesse, dd))							
									listaCommesse.add(dd);
							}					
						}
						
						//calcolo il totale ore ricavato dagli intervalli IU della bollatrice
						if(!d.getDettaglioIntervalliIUs().isEmpty()){
							totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, d.getTotaleOreGiorno());
							totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, d.getOreViaggio());					
						}
														
						for(DettaglioIntervalliCommesse dett:listaIntervalliC){							
							if(Float.valueOf(ServerUtility.aggiornaTotGenerale(dett.getOreLavorate(),  dett.getOreViaggio()))>0){
															
								RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero(p.getUsername(),dett.getNumeroCommessa()+"."+dett.getEstensioneCommessa()
									, dipendente, giornoF, letteraGiorno, Float.valueOf(dett.getOreLavorate()), Float.valueOf(dett.getOreViaggio()), Float.valueOf(ServerUtility.aggiornaTotGenerale(dett.getOreLavorate(),  dett.getOreViaggio())), "S");
							
								listaG.add(riep);																	
							}
						}					
						listaIntervalliC.clear();							
				}
				
				//elaboro un record per i totali per ogni commessa
				//for(AssociazionePtoA ass:listaAssociazioniPA){
				for(DettaglioIntervalliCommesse dc: listaCommesse){
					String commessa= dc.getNumeroCommessa() +"."+ dc.getEstensioneCommessa();
					
					//precompilo la lista di giorni con tutti i giorni lavorativi del mese in modo tale da segnalare una eventuale mancata compilazione
					for(int i=1; i<=ServerUtility.getGiorniMese(mese, anno);i++){
						String g= new String();	
						String dataCompLow=new String();
						String meseApp= new String();
						String c= new String();
						Date d= new Date();
						boolean trovato= false;
						
						meseApp=(mese.substring(0,1).toLowerCase()+mese.substring(1,3));
						
						if(i<10)
							g="0"+String.valueOf(i);
						else
							g=String.valueOf(i);
						
						dataCompLow=(g+"-"+meseApp+"-"+anno);
						formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
						d=formatter.parse(dataCompLow);
						formatter=new SimpleDateFormat("EEE", Locale.ITALIAN);
						letteraGiorno=formatter.format(d);
						letteraGiorno=letteraGiorno.substring(0, 1).toUpperCase();						
						
						formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
						RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero(p.getUsername(),
								commessa,dipendente	, dataCompLow, letteraGiorno, Float.valueOf("0.00"), Float.valueOf("0.00"), 
								Float.valueOf("0.00"), "N");	
						
						Iterator<RiepilogoOreDipCommesseGiornaliero> itr = listaG.iterator();
						
						//confronto il riep creato adesso con la lista di giorni che già possiedo
						while(itr.hasNext()) {				
							RiepilogoOreDipCommesseGiornaliero dg = itr.next(); //giorno preso dalla lista con i giorni compilati
					         String dd=dg.get("giorno");
					         c=dg.get("numeroCommessa");
					         if(c.compareTo(commessa)==0)
								if(dd.compareTo((String)riep.get("giorno"))==0){ 
									trovato=true;
									break;
								}				
								//else trovato=false;
					    }		
						if(!trovato){
							listaG.add(riep);				
						}		
					}
									
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
								
					totaleOreDaCommesse=ServerUtility.aggiornaTotGenerale(totaleOreDaCommesse, totaleOreC);
					RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero(p.getUsername(), commessa
							, dipendente, "Totale", "", Float.valueOf(totaleOreLavoroC), Float.valueOf(totaleOreViaggioC), Float.valueOf(totaleOreC), "S");
					
					listaG.add(riep);
					totaleOreLavoroC= "0.00";
					totaleOreViaggioC= "0.00";
					totaleOreC= "0.00";
				}
				
				RiepilogoOreDipCommesseGiornaliero riep= new RiepilogoOreDipCommesseGiornaliero("","",
						 "", "", "",  Float.valueOf("0.00"), Float.valueOf(totaleOreDaIU),Float.valueOf(totaleOreDaCommesse), "S");
				listaG.add(riep);
				
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

		@Override
		public boolean confermaGiorniDipendente(String username, Date data) throws IllegalArgumentException {
		
			List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
			List<DettaglioOreGiornaliere> listaD=new ArrayList<DettaglioOreGiornaliere>();
			Personale p= new Personale();
			
			DateFormat formatter = new SimpleDateFormat("yyyy") ; 
			String anno=formatter.format(data);
			formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
			String mese=formatter.format(data);
		    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
			
		    String periodo=mese+anno;
		    
		    Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
			
			try {
							
				tx=session.beginTransaction();
				
				p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
				
				listaF.addAll(p.getFoglioOreMeses());
							
				for(FoglioOreMese f:listaF){//scorro i mesi per trovare il foglio ore desiderato
					if(f.getMeseRiferimento().compareTo(periodo)==0){
						listaD.addAll(f.getDettaglioOreGiornalieres());
						f.setStatoRevisioneMese("2");					
						break;
					}
				}
						
				for(DettaglioOreGiornaliere d:listaD){
					d.setStatoRevisione("2");
				}
				
				tx.commit();
				
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			}finally{
				session.close();
			}
			
			return true;
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean confermaGiorniTuttiDipendenti(String sede, Date data)  throws IllegalArgumentException{
			List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
			List<DettaglioOreGiornaliere> listaD=new ArrayList<DettaglioOreGiornaliere>();
			List<Personale> listaP= new ArrayList<Personale>();
			
			DateFormat formatter = new SimpleDateFormat("yyyy") ; 
			String anno=formatter.format(data);
			formatter = new SimpleDateFormat("MMM",Locale.ITALIAN);
			String mese=formatter.format(data);
		    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
			
		    String periodo=mese+anno;
		    
		    Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
			
			try {
							
				tx=session.beginTransaction();
				
				listaP=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede").setParameter("sede", sede).list();	
										
				for(Personale p:listaP){
					listaF.addAll(p.getFoglioOreMeses());
					
					for(FoglioOreMese f:listaF){//scorro i mesi per trovare il foglio ore desiderato
						if(f.getMeseRiferimento().compareTo(periodo)==0){
							listaD.addAll(f.getDettaglioOreGiornalieres());
							f.setStatoRevisioneMese("2");
							break;
						}
					}
					listaF.clear();				
				}
				
				for(DettaglioOreGiornaliere d:listaD){
					d.setStatoRevisione("2");
				}
				tx.commit();
				
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			}finally{
				session.close();
			}		
			return true;
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public List<PeriodoSbloccoModel> getDatiPeriodoSblocco() throws IllegalArgumentException {
			List<PeriodoSbloccoModel> listaPM= new ArrayList<PeriodoSbloccoModel>();
			PeriodoSbloccoModel p;
			List<PeriodoSbloccoGiorni> listaP= new ArrayList<PeriodoSbloccoGiorni>();
			
			Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
				
			try {
				tx = session.beginTransaction();
				
				listaP=(List<PeriodoSbloccoGiorni>)session.createQuery("from PeriodoSbloccoGiorni").list();
				
				if(listaP!=null)
					for(PeriodoSbloccoGiorni ps:listaP){
						p= new PeriodoSbloccoModel(ps.getIdPeriodo(), ps.getSede(), ps.getDataInizio(), ps.getDataFine());	
						listaPM.add(p);
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
			
			return listaPM;
		}
		
		
		@Override
		public boolean confermaPeriodoSblocco(Date dataInizio,
				Date dataFine, String sede) {
			
			PeriodoSbloccoGiorni p= new PeriodoSbloccoGiorni();
			Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
				
			try {
				tx = session.beginTransaction();
				
				p.setDataFine(dataFine);
				p.setDataInizio(dataInizio);
				p.setSede(sede);
				
				session.save(p);
				
				tx.commit();			
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			} finally {
				session.close();
			}
			
			return true;
		}

		
		@Override
		public boolean eliminaPeriodoSblocco(int idSel) {
			PeriodoSbloccoGiorni p= new PeriodoSbloccoGiorni();
			Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
				
			try {
				tx = session.beginTransaction();
				
				p=(PeriodoSbloccoGiorni)session.createQuery("from PeriodoSbloccoGiorni where idPeriodo=:id").setParameter("id", idSel).uniqueResult();
					
				session.delete(p);
				
				tx.commit();			
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			} finally {
				session.close();
			}
			
			return true;
		}
		
		
		@Override
		public boolean inserisciGiornoFestivo(Date giorno, String sede)
				throws IllegalArgumentException {
			GiorniFestivi g= new GiorniFestivi();
			Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
				
			try {
				tx = session.beginTransaction();
				
				g.setGiorno(giorno);
				g.setSede(sede);
				session.save(g);
				
				tx.commit();			
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			} finally {
				session.close();
			}			
			return true;
		}
		

		@Override
		public boolean eliminaGiornoFestivi(int idSel)
				throws IllegalArgumentException {
			GiorniFestivi g= new GiorniFestivi();
			Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
				
			try {
				tx = session.beginTransaction();
				
				g=(GiorniFestivi)session.createQuery("from GiorniFestivi where idgiorno=:id").setParameter("id", idSel).uniqueResult();
					
				session.delete(g);
				
				tx.commit();			
			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				e.printStackTrace();
				return false;
			} finally {
				session.close();
			}	
			return true;
		}
		

		@SuppressWarnings("unchecked")
		@Override
		public List<GiorniFestiviModel> getGiorniFestivi()
				throws IllegalArgumentException {
			List<GiorniFestivi> listaG= new ArrayList<GiorniFestivi>();
			List<GiorniFestiviModel> listaGM=new ArrayList<GiorniFestiviModel>();
			GiorniFestiviModel gm;
			Session session= MyHibernateUtil.getSessionFactory().openSession();
			Transaction tx= null;
			
			try {
				tx = session.beginTransaction();
				
				listaG=(List<GiorniFestivi>)session.createQuery("from GiorniFestivi").list();
				
				if(listaG!=null)
					for(GiorniFestivi g: listaG){
						gm=new GiorniFestiviModel(g.getIdGiorno(), g.getGiorno(), g.getSede());
					
						listaGM.add(gm);
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
			
			
			return listaGM;
		}

	
	
	
//RIEPILOGHI ORE COMMESSE DIPENDENTI--------------------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoOreDipCommesse> getRiepilogoOreDipCommesse(String mese, String pm) {
		List<RiepilogoOreDipCommesse> listaR= new ArrayList<RiepilogoOreDipCommesse>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();
		
		List<DettaglioIntervalliCommesse> listaDettComm= new ArrayList<DettaglioIntervalliCommesse>();
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
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();

			listaCommesse = (List<Commessa>) session.createQuery("from Commessa where matricolaPM=:pm").setParameter("pm", pm).list();//tolta la clausola and stato=aperta
		
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
								
				listaP=session.createQuery("select distinct p from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g" +
						" join g.foglioOreMese f join f.personale p" +
						" where f.meseRiferimento=:mese and d.numeroCommessa=:numeroCommessa and d.estensioneCommessa=:estensioneCommessa")
						.setParameter("numeroCommessa", commessa).setParameter("estensioneCommessa", estensione).setParameter("mese", mese).list();

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
								riep = new RiepilogoOreDipCommesse(0,numeroCommessa, 0, dipendente, Float.valueOf(oreTotMeseLavoro), Float.valueOf(oreTotMeseViaggio), Float.valueOf(oreTot));
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
		//String cliente= new String();
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
					//cliente=a.getCommessa().getOrdines().iterator().next().getRda().getCliente().getRagioneSociale();
					numeroOrdine=a.getCommessa().getOrdines().iterator().next().getCodiceOrdine();
					oreBudget=a.getCommessa().getOrdines().iterator().next().getOreBudget();
				}
				else {
					//cliente="#";
					numeroOrdine="#";
					oreBudget="0.0";
				}
				
				numeroCommessa =commessa; //una stringa più dettagliata che descriva la commessa
				
				listaP=session.createQuery("select distinct p from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g" +
						" join g.foglioOreMese f join f.personale p" +
						" where f.meseRiferimento=:mese and d.numeroCommessa=:numeroCommessa and d.estensioneCommessa=:estensioneCommessa")
						.setParameter("numeroCommessa", commessa).setParameter("estensioneCommessa", estensione).setParameter("mese", data).list();
				
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
		//List<Commessa> listaCommesseTutti= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();
		
		List<AssociazionePtoA> listaAssociazioni= new ArrayList<AssociazionePtoA>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalli= new ArrayList<DettaglioIntervalliCommesse>();
		RiepilogoOreDipFatturazione riep= new RiepilogoOreDipFatturazione();
		
		String numeroCommessa= new String();
		String commessa= new String();
		String estensione= new String();
		String descrizione= new String();
		String dipendente= new String();
		String oreLavoro= new String();
		String oreViaggio= new String();
		String oreTotMeseLavoro= "0.0";
		String oreTotMeseViaggio= "0.0";
		String oreSommaLavoroViaggio="0.0";
		String oreTotLavoroCommessa="0.0";
		String oreTotViaggioCommessa="0.0";
		String oreTotCommessa="0.0";
		String oreTotIU="0.00";
		Boolean check=false;
		int idDip;
		
		String statoCommessa="Aperta";
		
		String annoRif=mese.substring(3,7);
		String meseRif=mese.substring(0, 3);
		
		Date data= new Date();
		    
	    meseRif=(meseRif.substring(0,1).toLowerCase()+meseRif.substring(1,3));
	    
	    DateFormat  formatter = new SimpleDateFormat("MMMyyyy", Locale.ITALIAN);
	    	    
		try {			
			data= formatter.parse(meseRif+annoRif);
						
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			if(pm.compareTo("ALL")==0)
				listaCommesse = (List<Commessa>) session.createQuery("from Commessa where statoCommessa=:stato")
				.setParameter("stato", statoCommessa).list() ;
			else
				listaCommesse = (List<Commessa>) session.createQuery("from Commessa where matricolaPM=:pm and statoCommessa=:stato")
					.setParameter("pm", pm).setParameter("stato", statoCommessa).list() ; //seleziono tuttle le commesse per pm indicato
			
			for (Commessa c : listaCommesse) {
				if (c.getAttivitas().size() > 0)
					//listaAttivita.add(c.getAttivitas().iterator().next());
					listaAttivita.addAll(c.getAttivitas());
			}

			for (Attivita a : listaAttivita) {  // in questo caso la lista  Attivita rappresenta la lista di commesse associate al PM
												// selezionato: ottengo tutte le associazioni e quindi tutti i dipendenti associati a quella commessa
				listaAssociazioni.addAll(a.getAssociazionePtoas());
				commessa = a.getCommessa().getNumeroCommessa();
				estensione = a.getCommessa().getEstensione();
				
				descrizione=a.getCommessa().getDenominazioneAttivita();
								
				numeroCommessa =(commessa + "." + estensione+" ("+descrizione+")"); //una stringa più dettagliata che descriva la commessa
				
				for (AssociazionePtoA ass : listaAssociazioni) { // per tutte le associazioni della  commessa considerata prelevo i dipendenti
					listaP.add(ass.getPersonale());
				}
				
				//TODO
				/*listaP=session.createQuery("select distinct p from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g" +
						" join g.foglioOreMese f join f.personale p" +
						" where f.meseRiferimento=:mese and d.numeroCommessa=:numeroCommessa and d.estensioneCommessa=:estensioneCommessa")
						.setParameter("numeroCommessa", commessa).setParameter("estensioneCommessa", estensione).setParameter("mese", data).list();*/
				
				for (Personale p : listaP)
					if(p.getGruppoLavoro().compareTo("Indiretti")!=0){ // per ogni dipendente in questa commessa selezioni i fogli ore del mese desiderato
						dipendente = p.getCognome() + " " + p.getNome();
												
						idDip=p.getId_PERSONALE();
						
						for (FoglioOreMese f : p.getFoglioOreMeses()) {
							if (f.getMeseRiferimento().compareTo(mese) == 0) { //se è il mese cercato
								listaGiorni.addAll(f.getDettaglioOreGiornalieres()); //prendo tutti i giorni
																								
								for (DettaglioOreGiornaliere giorno : listaGiorni) { 
														
									//calcolo ore su intervalli commesse
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
																
								
								riep = new RiepilogoOreDipFatturazione(numeroCommessa, "", "", idDip, dipendente, Float.valueOf(oreTotMeseLavoro), 
										Float.valueOf(oreTotMeseViaggio), Float.valueOf(oreSommaLavoroViaggio),Float.valueOf(oreTotIU), 
										checkOreIntervalliIUOreCommesse(p.getUsername(), data));
								listaR.add(riep);
								
								//Per ogni dipendente incremento il totale sulla commessa in esame
								oreTotLavoroCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, oreTotMeseLavoro);
								oreTotViaggioCommessa=ServerUtility.aggiornaTotGenerale(oreTotViaggioCommessa, oreTotMeseViaggio);
								oreTotCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, oreTotViaggioCommessa);
								
								oreTotMeseLavoro="0.0";
								oreTotMeseViaggio="0.0";
								oreSommaLavoroViaggio="0.0";
								//oreTotIU="0.00";
							}
														
						}//end ciclo su mesi(fatto solo una volta per il mese necessario)
															
					}//end ciclo su lista persone nella stessa commessa
					
					riep = new RiepilogoOreDipFatturazione(numeroCommessa, "", "", 0, "_TOTALE", Float.valueOf(oreTotLavoroCommessa), 
							Float.valueOf(oreTotViaggioCommessa), Float.valueOf(oreTotCommessa),Float.valueOf("0.00"), true);
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


	
	@SuppressWarnings({ "unchecked"})
	@Override
	public FoglioFatturazioneModel getDatiFatturazionePerOrdine(String numeroCommessa, String mese ,int idAttivita)throws IllegalArgumentException {
		
		String commessa= new String();
		String estensione= new String();
		String tariffaUtilizzata= new String();
		String sommaVariazioniSal= "0.00";
		String sommaVariazioniPcl= "0.00";
		String importoRtv="#";
		
		String oreResidueBudget="0.00";
		String importoResiduo="0.00";
		
		Boolean esistePa= false; //controllo l'esistenza di una eventuale commessa madre .pa
				
		Commessa c= new Commessa();
		Commessa c_pa= new Commessa();
		Ordine o= new Ordine();
		FoglioFatturazione f= new FoglioFatturazione();
		FoglioFatturazioneModel foglioModel= new FoglioFatturazioneModel();	
		List<Commessa> listaC= new ArrayList<Commessa>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
			
		int codCommessa;
		
		//mesePrecedente=ServerUtility.getMesePrecedente(mese);
		commessa=numeroCommessa.substring(0, numeroCommessa.indexOf("."));
		estensione=numeroCommessa.substring(numeroCommessa.indexOf(".")+1, numeroCommessa.length());
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
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
					//accumulo tutto il sal e pcl dei mesi successivi anche su tutte le eventuali attivita					
					if(ServerUtility.isPrecedente(f1.getMeseCorrente(),mese)){
						sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f1.getVariazionePCL());
						sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f1.getVariazioneSAL());
					}
				}
			
				sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, c_pa.getSalAttuale());
				sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, c_pa.getPclAttuale());
			
				f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:attivitaOrdine").setParameter("id", codCommessa)
						.setParameter("mese", mese).setParameter("attivitaOrdine", idAttivita).uniqueResult();
				if(f==null)
					//provo a beccarlo eventualmente con attivita ordine a 0 se compilato prima dell'ordine
					f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:attivitaOrdine").setParameter("id", codCommessa)
					.setParameter("mese", mese).setParameter("attivitaOrdine", 0).uniqueResult();
			
				tx.commit();
				
				if(o==null){//se non c'è un ordine associato, permetto l'inserimento di eventuali sal pcl
					tariffaUtilizzata=c.getTariffaSal();//prendo la tariffa della commessa
					if(f==null){						
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", "0.0", "0.0", Float.valueOf(tariffaUtilizzata), "0.0",
								sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.00","#","0.0", "0.0", "0.0", "", "0");
					}
					else{	
						//il foglio fatturazione era già stato compilato quindi tolgo alla sommavariazioni quella in esame
						sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
						sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", "0.0", "0.0", Float.valueOf(f.getTariffaUtilizzata()), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getImportoRealeFatturato(),"#", f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
							
				}else{					
					List<Rtv> listaRtv= new ArrayList<Rtv>();
					listaRtv.addAll(o.getRtvs());
					for(Rtv rtv:listaRtv)
						if(rtv.getMeseRiferimento().compareTo(mese.toLowerCase())==0)
							importoRtv=String.valueOf((Float)rtv.getImporto());
					
					//--
					oreResidueBudget=o.getOreResidueBudget();
					importoResiduo=o.getImportoResiduo();
					if(importoResiduo==null)
						importoResiduo="0.00";

					for(FoglioFatturazione ff:listaFF){
						if(ff.getMeseCorrente().compareTo("Mag2013")!=0)//elimino il mese di maggio in quanto compilato ancora con una modalità non corretta per l'aggiornamento delle ore residue
							if(ServerUtility.isPrecedente(ff.getMeseCorrente(), mese))
								if(ff.getCommessa().getCodCommessa()==codCommessa){ //aggiorno il residuo se il foglio fatturazione è della commessa considerata
									oreResidueBudget=ServerUtility.getDifference(oreResidueBudget, ff.getOreFatturare());
									importoResiduo=d.format( Float.valueOf(importoResiduo)- Float.valueOf(ff.getImportoRealeFatturato()));
								}
					}
					//---
					
					for(AttivitaOrdine a:o.getAttivitaOrdines())
						if(a.getIdAttivitaOrdine()==idAttivita)
							tariffaUtilizzata=a.getTariffaAttivita();					
										
					String numOrdine=o.getCodiceOrdine();
					String importo=o.getImporto();
					if(importo==null)
						importo="0.00";
					
					if(f==null){																			//--				
						foglioModel= new FoglioFatturazioneModel(numOrdine, o.getOreBudget(), oreResidueBudget,importo, importoResiduo,
								Float.valueOf(tariffaUtilizzata), "0.0", sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.00",importoRtv, "0.0", "0.0", "0.0", "", "0");
					}
					else{		
						sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
						sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						foglioModel= new FoglioFatturazioneModel(numOrdine, o.getOreBudget(), oreResidueBudget , importo, importoResiduo, Float.valueOf(f.getTariffaUtilizzata()), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getImportoRealeFatturato(),importoRtv, f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
				}	
				
			}else{ //Non esiste la Pa
				
				f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:attivitaOrdine").setParameter("id", codCommessa)
						.setParameter("mese", mese).setParameter("attivitaOrdine", idAttivita).uniqueResult();
				if(f==null)
					//provo a beccarlo eventualmente con attivita ordine a 0 se compilato prima dell'ordine
					f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:attivitaOrdine").setParameter("id", codCommessa)
					.setParameter("mese", mese).setParameter("attivitaOrdine", 0).uniqueResult();
			
				listaFF.addAll(c.getFoglioFatturaziones());			
				
				if(o!=null){
					
					oreResidueBudget=o.getOreResidueBudget();
					importoResiduo=o.getImportoResiduo();
					if(importoResiduo==null)
						importoResiduo="0.00";
				}
				//Considero tutti i FF compilati in mesi differenti da quello in esame
				for(FoglioFatturazione f1:listaFF){
									
					if(ServerUtility.isPrecedente(f1.getMeseCorrente(),mese))
						if(f1.getMeseCorrente().compareTo(mese)!=0){
							sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f1.getVariazionePCL());
							sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f1.getVariazioneSAL());
						}
									
					//---
					if(f1.getMeseCorrente().compareTo("Mag2013")!=0)//elimino il mese di maggio in quanto compilato ancora con una modalità non corretta per l'aggiornamento delle ore residue
						if(ServerUtility.isPrecedente(f1.getMeseCorrente(), mese))
							if(f1.getCommessa().getCodCommessa()==codCommessa){
								oreResidueBudget=ServerUtility.getDifference(oreResidueBudget, f1.getOreFatturare());
								importoResiduo=d.format( Float.valueOf(importoResiduo)- Float.valueOf(f1.getImportoRealeFatturato()));
							}
					//--
				}
			
				//Aggiungo anche un eventuale SAL/PCL iniziale sulla commessa
				sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, c.getSalAttuale());
				sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, c.getPclAttuale());
				
				tx.commit();		
				
				if(o==null){//se non c'è un ordine associato, permetto l'inserimento di eventuali sal pcl
					tariffaUtilizzata=c.getTariffaSal();//prendo la tariffa della commessa
					if(f==null){						
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", "0.0", "0.0",Float.valueOf(tariffaUtilizzata), "0.0", 
								sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.00", "#", "0.0", "0.0", "0.0", "", "0");
					}
					else{	
						//sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL());
						//sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL());
						foglioModel= new FoglioFatturazioneModel("#", "0.0", "0.0", "0.0", "0.0",Float.valueOf(f.getTariffaUtilizzata()), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getImportoRealeFatturato(),"#", f.getVariazioneSAL(), f.getVariazionePCL(), 
								f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
							
				}else{	
					
					List<Rtv> listaRtv= new ArrayList<Rtv>();
					listaRtv.addAll(o.getRtvs());
					for(Rtv rtv:listaRtv)
						if(rtv.getMeseRiferimento().compareTo(mese)==0)
							importoRtv=String.valueOf((Float)rtv.getImporto());
					
					for(AttivitaOrdine a:o.getAttivitaOrdines())
						if(a.getIdAttivitaOrdine()==idAttivita)
							tariffaUtilizzata=a.getTariffaAttivita();					
										
					String numOrdine=o.getCodiceOrdine();
					
					String importo=o.getImporto();
					if(importo==null)
						importo="0.00";
					
					if(f==null){		
						//--
						foglioModel= new FoglioFatturazioneModel(numOrdine, o.getOreBudget(),oreResidueBudget, importo, importoResiduo,
								Float.valueOf(tariffaUtilizzata), "0.0",sommaVariazioniSal, sommaVariazioniPcl, "0.0", "0.00", importoRtv, "0.0", "0.0", "0.0", "", "0");
					}
					else{			
						//tariffa utilizza prendo quella registrata al momento della registrazione del foglio fatturazione
						foglioModel= new FoglioFatturazioneModel(numOrdine, o.getOreBudget(), oreResidueBudget, importo, importoResiduo, Float.valueOf(f.getTariffaUtilizzata()), f.getOreEseguite(),
								sommaVariazioniSal, sommaVariazioniPcl, f.getOreFatturare(), f.getImportoRealeFatturato(), importoRtv, f.getVariazioneSAL(), f.getVariazionePCL(), f.getOreScaricate(), f.getNote(), f.getStatoElaborazione());
					}		
				}						
			}
			
			return foglioModel;
		}
		catch (Exception e) {
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
			return null;
		}finally{
			session.close();
		}	
	}

	
	@Override
	public FatturaModel elaboraDatiPerFattura(String numeroOrdine,
			int idFoglioFatturazione) {
		
		FatturaModel fM;
		Fattura f=new Fattura();
		FoglioFatturazione ff= new FoglioFatturazione();
		List<AttivitaFatturateModel> listaAttF= new ArrayList<AttivitaFatturateModel>();
		AttivitaFatturateModel attF= new AttivitaFatturateModel();
		
		Ordine o= new Ordine();
		DatiFatturazioneAzienda df= new DatiFatturazioneAzienda();
			
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			ff=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where idFoglioFatturazione=:id").setParameter("id", idFoglioFatturazione).uniqueResult();
			o=(Ordine)session.createQuery("from Ordine where codiceOrdine=:nOrdine").setParameter("nOrdine", numeroOrdine).uniqueResult();
			df=(DatiFatturazioneAzienda)session.createQuery("from DatiFatturazioneAzienda").uniqueResult();
			f=(Fattura)session.createQuery("from Fattura where idFoglioFatturazione=:id").setParameter("id", idFoglioFatturazione).uniqueResult();		
			
			if(f==null){
				Date dataAnno= new Date();		
				String anno=dataAnno.toString();
				anno=anno.substring(anno.length()-4, anno.length());
				String numeroFattura= new String();
				numeroFattura=(String)session.createSQLQuery("SELECT MAX(numeroFattura) FROM gestionaledb.fattura where annoFattura=:anno")
						.setParameter("anno", anno).uniqueResult();	
				
				if(numeroFattura==null){
					numeroFattura="1";
				}else {				
					int parteNumeroFattura=Integer.valueOf(numeroFattura)+1;
					numeroFattura=String.valueOf(parteNumeroFattura);
				}
				
				String ragioneSociale=o.getRda().getCliente().getRagioneSociale();
				String indirizzo=o.getRda().getCliente().getIndirizzo();
				String cap=o.getRda().getCliente().getCap();
				String citta=o.getRda().getCliente().getCitta();
				String piva=o.getRda().getCliente().getPartitaIVA();
				String codiceFornitore=o.getRda().getCliente().getCodFornitore();			
				//String dataFattura="#";
				String condizioni="#";
				String filiale=df.getDatiFiliale();
				String iban=df.getIban();
				String numeroOfferta=o.getRda().getOffertas().iterator().next().getCodiceOfferta();
				String lineaOrdine=o.getRda().getCodiceRDA();//RDO
				String bem=o.getBem();
				String elementoWbs=o.getElementoWbs();
				String conto=o.getConto();
				String prCenter=o.getPrCenter();
				String imponibile=ff.getImportoRealeFatturato();
				String iva=df.getAliquotaIva();
				String totaleIva="0.00";
				//String totaleImporto="0.00";
								
				attF= new AttivitaFatturateModel(0, o.getDescrizioneAttivita(), imponibile);
				listaAttF.add(attF);
				fM=new FatturaModel(0, ff.getIdFoglioFatturazione(), "" ,ragioneSociale, indirizzo, cap, citta, piva, codiceFornitore, numeroFattura, new Date(), condizioni, 
						filiale, iban, numeroOrdine, numeroOfferta, lineaOrdine, bem, elementoWbs, conto, prCenter, imponibile, iva, totaleIva,
						"", df.getRagioneSociale(), df.getCapitaleSociale(), df.getSedeLegale(), df.getSedeOperativa(), df.getRegistroImprese(),
						df.getRea());
				fM.setListaAttF(listaAttF);
				
			}else{
				AttivitaFatturateModel afM;
				String ragioneSociale=o.getRda().getCliente().getRagioneSociale();
				String indirizzo=o.getRda().getCliente().getIndirizzo();
				String cap=o.getRda().getCliente().getCap();
				String citta=o.getRda().getCliente().getCitta();
				String piva=o.getRda().getCliente().getPartitaIVA();
				String codiceFornitore=o.getRda().getCliente().getCodFornitore();
				String numeroFattura=f.getNumeroFattura();
				String dataFattura=f.getDataFatturazione();
				String condizioni=f.getCondizioniPagamento();
				String filiale=df.getDatiFiliale();
				String iban=df.getIban();
				String numeroOfferta=o.getRda().getOffertas().iterator().next().getCodiceOfferta();
				String lineaOrdine=o.getRda().getCodiceRDA();//RDO
				String bem=o.getBem();
				String elementoWbs=o.getElementoWbs();
				String conto=o.getConto();
				String prCenter=o.getPrCenter();
				String iva=f.getAliquotaIva();
				String imponibile="0.00";
				String totaleIva="0.00";
				String totaleImporto="0.00";
				String importoIva="0.00";
				//List<AttivitaFatturateModel> listaAttivita=new ArrayList<AttivitaFatturateModel>();
				
				SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);
				Date dataF=formatter.parse(dataFattura);
				
				for(AttivitaFatturata af:f.getAttivitaFatturatas()){
					afM=new AttivitaFatturateModel(af.getIdATTIVITA_FATTURATA(), af.getDescrizione(), af.getImporto());
					listaAttF.add(afM);
					
					imponibile=ServerUtility.aggiornaTotGenerale(imponibile, af.getImporto());
					importoIva=d.format(Float.valueOf(iva)*Float.valueOf(af.getImporto())/100);
					totaleIva=ServerUtility.aggiornaTotGenerale(totaleIva, importoIva);
					totaleImporto=ServerUtility.aggiornaTotGenerale(totaleImporto, ServerUtility.aggiornaTotGenerale(af.getImporto(), importoIva));					
				}
				
				fM=new FatturaModel(f.getIdFattura(),ff.getIdFoglioFatturazione(), o.getDescrizioneAttivita(),ragioneSociale, indirizzo, cap, citta, piva, codiceFornitore,
						numeroFattura, dataF, condizioni, filiale, iban, numeroOrdine, numeroOfferta, lineaOrdine, bem, elementoWbs, conto, prCenter, imponibile, iva, totaleIva,
						totaleImporto, df.getRagioneSociale(), df.getCapitaleSociale(), df.getSedeLegale(), df.getSedeOperativa(), df.getRegistroImprese(),
						df.getRea());	
				fM.setListaAttF(listaAttF);
			}
			
			tx.commit();
			return fM;			
			
		}catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
	}	
	

	@Override
	public boolean insertDatiFoglioFatturazione(String oreEseguite,
			String salIniziale, String pclIniziale, String oreFatturare, String importoFatturare,
			String variazioneSAL, String variazionePCL, String meseCorrente, String note,
			String statoElaborazione, String commessa, String tariffaUtilizzata, String flagSal, int idAttivita)
			throws IllegalArgumentException {
	
		Commessa c= new Commessa();
		//Commessa c_pa=new Commessa();
		Ordine o = new Ordine();
		FoglioFatturazione f= new FoglioFatturazione();
		
		//List<FoglioFatturazione> listaf= new ArrayList<FoglioFatturazione>();
		
		String numeroC= new String();
		String estensione= new String();
		//String oreResidueBudget= new String();
		int idCommessa;
		//Boolean esistePa=false;
		
		numeroC=commessa.substring(0,commessa.indexOf("."));
		estensione=commessa.substring(commessa.indexOf(".")+1, commessa.length());
		//oreResidueBudget="0.00";

		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
			
			c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", numeroC)
					.setParameter("estensione", estensione).uniqueResult();
			idCommessa=c.getCodCommessa();
		
			if(c.getOrdines().iterator().hasNext()){//se è presente un ordine
				o=c.getOrdines().iterator().next();
	
			}
			else o=null;
							
			f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:idCommessa and meseCorrente=:mese and attivitaOrdine=:idAttivita")
					.setParameter("idAttivita", idAttivita).setParameter("idCommessa", idCommessa).setParameter("mese", meseCorrente).uniqueResult();
			if(f==null)
				//provo a beccarlo eventualmente con attivita ordine a 0 se compilato prima dell'ordine
				f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:idCommessa and meseCorrente=:mese and attivitaOrdine=:idAttivita")
				.setParameter("idAttivita", 0).setParameter("idCommessa", idCommessa).setParameter("mese", meseCorrente).uniqueResult();
			
			if(f==null){
				//insert new
				f=new FoglioFatturazione();
				f.setCommessa(c);
				
				f.setOreEseguite(oreEseguite);
				f.setOreFatturare(oreFatturare);
				f.setImportoRealeFatturato(importoFatturare);
				f.setSALattuale(salIniziale);//ho così indicazione del sal e del pcl prima della variazione del mese registrato
				f.setPCLattuale(pclIniziale);
				f.setVariazionePCL(variazionePCL);
				f.setVariazioneSAL(variazioneSAL);
				f.setMeseCorrente(meseCorrente);
				f.setNote(note);
				f.setStatoElaborazione(statoElaborazione);
				f.setTariffaUtilizzata(tariffaUtilizzata);
				f.setFlagSalDaButtare(flagSal);
							
				f.setAttivitaOrdine(idAttivita);
						
				c.getFoglioFatturaziones().add(f);
												
				tx.commit();
				return true;
				
			}else{
								
				f.setOreEseguite(oreEseguite);
				f.setOreFatturare(oreFatturare);
				f.setImportoRealeFatturato(importoFatturare);
				f.setSALattuale(salIniziale); //L'iniziale prima della variazione
				f.setPCLattuale(pclIniziale);
				f.setVariazionePCL(variazionePCL);
				f.setVariazioneSAL(variazioneSAL);
				f.setMeseCorrente(meseCorrente);
				f.setNote(note);
				f.setStatoElaborazione(statoElaborazione);
				f.setTariffaUtilizzata(tariffaUtilizzata);
				f.setFlagSalDaButtare(flagSal);	
				
				f.setAttivitaOrdine(idAttivita);
				
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
		List<String> matricolePM= new ArrayList<String>();
		DatiFatturazioneMeseModel datiModel;
		Fattura fattura;
		Ordine o;
		Personale p= new Personale();
		float importo=0;
		float importoEffettivo=0;
		float margine=0;
		String oreMargine= new String();
		float efficienza=0;
		String oreScaricate= new String();
		String numeroOrdine="";
		/*String totOreEseguite="0.00";
		String totOreFatturate="0.00";
		String totVarSal="0.00";
		String totVarPcl="0.00";
		Float totImport= (float) 0;*/
		Float importoSal= (float) 0;
		Float importoPcl= (float) 0;
		/*Float totImportoSal= (float) 0;
		Float totImportoPcl= (float) 0;
		String totOreScaricate="0.00";
		String totOreMargine="0.00";*/
		String attivitaOrdine="";
		String statoFattura="N";
		String cognome="";
		
		Float[] totaleOreSalPcl={(float)0.0,(float)0.0};
		
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);	
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
		
			listaFF=(List<FoglioFatturazione>)session.createQuery("from FoglioFatturazione where meseCorrente=:mese").setParameter("mese", mese).list();
			for(FoglioFatturazione f: listaFF){
				
				fattura=(Fattura)session.createQuery("from Fattura where idFoglioFatturazione=:id").setParameter("id", f.getIdFoglioFatturazione()).uniqueResult();
				if(fattura!=null)
					statoFattura="S";
				else statoFattura="N";
				
				if(f.getCommessa().getMatricolaPM()!=null && !exsistMatricolaPM(f.getCommessa().getMatricolaPM(), matricolePM))
					matricolePM.add(f.getCommessa().getMatricolaPM());
				
				cognome=f.getCommessa().getMatricolaPM();
				cognome=cognome.substring(0,cognome.indexOf(" "));
				
				p=(Personale)session.createQuery("from Personale where cognome=:cognome").setParameter("cognome", cognome).uniqueResult();				
				
				if(!f.getCommessa().getOrdines().isEmpty()){
					o=f.getCommessa().getOrdines().iterator().next();
					numeroOrdine=o.getCodiceOrdine();
					oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
					oreScaricate=oreMargine;	
					
					if(f.getOreEseguite().compareTo("0.00")==0 && oreScaricate.compareTo("0.00")==0)
						efficienza=0;
					else
					if(f.getOreEseguite().compareTo("0.00")!=0)
						efficienza=Float.valueOf(ServerUtility.getOreCentesimi(oreScaricate))/Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite()));
					else
						efficienza=0;
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
					margine=Float.valueOf(ServerUtility.getOreCentesimi(oreMargine));
					importoEffettivo=Float.valueOf(f.getImportoRealeFatturato());
					
					for(AttivitaOrdine a:o.getAttivitaOrdines())
						if(a.getIdAttivitaOrdine()==f.getAttivitaOrdine()){
							
							importo=ServerUtility.calcolaImporto(a.getTariffaAttivita(),f.getOreFatturare());
							importoSal=ServerUtility.calcolaImporto(a.getTariffaAttivita(),f.getVariazioneSAL());
							importoPcl=ServerUtility.calcolaImporto(a.getTariffaAttivita(),f.getVariazionePCL());
							attivitaOrdine=a.getDescrizioneAttivita();
							
						}else
							if(f.getAttivitaOrdine()==0){
								importo=ServerUtility.calcolaImporto(f.getTariffaUtilizzata(),f.getOreFatturare());
								importoSal=ServerUtility.calcolaImporto(f.getTariffaUtilizzata(),f.getVariazioneSAL());
								importoPcl=ServerUtility.calcolaImporto(f.getTariffaUtilizzata(),f.getVariazionePCL());
								attivitaOrdine="";
							}	
					
					totaleOreSalPcl=ServerUtility.calcolaSalPclTotale(f.getCommessa().getNumeroCommessa(), f.getCommessa().getEstensione(), "pcl", true, mese);
					
					datiModel=new DatiFatturazioneMeseModel(f.getIdFoglioFatturazione(), p.getSedeOperativa(),f.getCommessa().getMatricolaPM(), f.getCommessa().getNumeroCommessa()+"."+f.getCommessa().getEstensione(), o.getRda().getCliente().getRagioneSociale(), 
							numeroOrdine, o.getCommessa().getDenominazioneAttivita(),attivitaOrdine , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf(ServerUtility.getOreCentesimi(f.getOreFatturare()))
							, Float.valueOf(f.getTariffaUtilizzata()),	importo, importoEffettivo, Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), importoSal, 
							Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), importoPcl, Float.valueOf(ServerUtility.getOreCentesimi(oreScaricate)), margine, d.format(efficienza), f.getNote(), statoFattura
							,totaleOreSalPcl[0], totaleOreSalPcl[1]);
				}
				else{
					numeroOrdine="";
					oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
					oreScaricate=oreMargine;
					if(f.getOreEseguite().compareTo("0.00")==0 && oreScaricate.compareTo("0.00")==0)
						efficienza=0;
					else
					if(f.getOreEseguite().compareTo("0.00")!=0)
						efficienza=Float.valueOf(ServerUtility.getOreCentesimi(oreScaricate))/Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite()));
					else
						efficienza=0;
					oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
					margine=Float.valueOf(ServerUtility.getOreCentesimi(oreMargine));
					importoSal=ServerUtility.calcolaImporto(f.getTariffaUtilizzata(),f.getVariazioneSAL());
					importoPcl=ServerUtility.calcolaImporto(f.getTariffaUtilizzata(),f.getVariazionePCL());
					
					totaleOreSalPcl=ServerUtility.calcolaSalPclTotale(f.getCommessa().getNumeroCommessa(), f.getCommessa().getEstensione(), "pcl", false, mese);
					
					datiModel=new DatiFatturazioneMeseModel(f.getIdFoglioFatturazione(), p.getSedeOperativa(), f.getCommessa().getMatricolaPM(), f.getCommessa().getNumeroCommessa()+"."+f.getCommessa().getEstensione(), "#", numeroOrdine,
							f.getCommessa().getDenominazioneAttivita(),attivitaOrdine, Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf(ServerUtility.getOreCentesimi(f.getOreFatturare())),
							Float.valueOf(f.getTariffaUtilizzata()), (float) 0.0, (float)0.0, Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), importoSal, Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
							importoPcl, Float.valueOf(ServerUtility.getOreCentesimi(oreScaricate)), margine, d.format(efficienza), f.getNote(), statoFattura
							,totaleOreSalPcl[0], totaleOreSalPcl[1]);	
				}
				listaDati.add(datiModel);
				
				importoSal= (float) 0;
				importoPcl= (float) 0;
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


	private boolean exsistMatricolaPM(String matricolaPM,
			List<String> matricolePM) {
		
		for(String pm: matricolePM)
			if (pm.compareTo(matricolaPM)==0)
				return true;
		return false;		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean setStatoFoglioFatturazione(String mese, String anno) {
		
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		String data=mese+anno;
		Boolean esito=true;
		String errore="";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx=session.beginTransaction();
			
			listaFF=(List<FoglioFatturazione>)session.createQuery("from FoglioFatturazione where meseCorrente=:data").setParameter("data", data).list();
					
			tx.commit();
			
			for(FoglioFatturazione ff:listaFF)
				ServerUtility.setStatoFoglioFatturazione(ff.getIdFoglioFatturazione());
			
		} catch (Exception e) {
	    	esito=false;
			errore=e.getMessage();
			e.printStackTrace();
			if (tx != null)
				tx.rollback();
	    }finally{
	    	session.close();
	    	if(!esito){
				ServerLogFunction.logErrorMessage("setStatoFoglioFatturazione", new Date(), "", "Error", errore);
				return false;
			}
			else
				ServerLogFunction.logOkMessage("setStatoFoglioFatturazione", new Date(), "", "Success");
	    }	
		
		return true;
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
	public List<DatiFatturazioneCommessaModel> getRiepilogoDatiFatturazioneCommessa(String commessaSelected) throws IllegalArgumentException {
	
		List<DatiFatturazioneCommessaModel> listaDati= new ArrayList<DatiFatturazioneCommessaModel>();
		List<FoglioFatturazione> listaFF=new ArrayList<FoglioFatturazione>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		DatiFatturazioneCommessaModel datiModel;
		Ordine o;
		Commessa c_pa=new Commessa();
		
		float importo;
		float margine;
		boolean esistePa=false;
		String commessa= new String();
		String estensione= new String();
		String attivita=new String();
		
		String oreMargine= new String();
		String nCommessa=commessaSelected.substring(0, commessaSelected.indexOf("."));
		String nEstensione=commessaSelected.substring(commessaSelected.indexOf(".")+1, commessaSelected.length());		
		String tariffa="0.00";
		String oreOrdineIniziali="0.00";
		String importoOrdineIniziale="0.00";
		String salIniziale="0.00";
		String pclIniziale="0.00";
		String numeroOrdine="";
		String importoO="0.00";
		String importoResiduo="0.00";
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);		
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx = session.beginTransaction();
						
			c_pa=(Commessa)session.createQuery("from Commessa where statoCommessa='Aperta' and numeroCommessa=:numeroCommessa and estensione=:numeroEstensione")
					.setParameter("numeroCommessa", nCommessa).setParameter("numeroEstensione", "pa").uniqueResult();
			
			if(c_pa!=null){
				esistePa=true;
				listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa")
						.setParameter("numeroCommessa", nCommessa).list();
			}else
				//ce ne sarà comunque 1
				listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:numeroEstensione")
				.setParameter("numeroCommessa", nCommessa).setParameter("numeroEstensione", nEstensione).list();
			
			for(Commessa c:listaC){
				commessa=c.getNumeroCommessa();//+"."+c.getEstensione();
				estensione=c.getEstensione();
				attivita=c.getDenominazioneAttivita();
				
				if(c.getFoglioFatturaziones().size()>=0){
					listaFF.addAll(c.getFoglioFatturaziones());								
					if(!c.getOrdines().isEmpty()){
						o=c.getOrdines().iterator().next();
						if(o.getImporto()!=null)
							importoO=o.getImporto();
						if(o.getImportoResiduo()!=null)					
							importoResiduo=o.getImportoResiduo();
						
						if(o.getOreResidueBudget().compareTo(o.getOreBudget())!=0){
							oreOrdineIniziali=ServerUtility.aggiornaTotGenerale(oreOrdineIniziali, o.getOreResidueBudget());
							importoOrdineIniziale=d.format(Float.valueOf(importoOrdineIniziale)+ Float.valueOf(importoResiduo));
						}
						else{
							oreOrdineIniziali=ServerUtility.aggiornaTotGenerale(oreOrdineIniziali, o.getOreBudget());
							importoOrdineIniziale= d.format(Float.valueOf(importoOrdineIniziale)+ Float.valueOf(importoO));
						}
					}
					for(FoglioFatturazione f: listaFF){
						
						String numMese=f.getMeseCorrente();
						numMese=numMese.substring(0,3).toLowerCase();
						numMese=ServerUtility.traduciMeseToNumber(numMese);
						
						if(!c.getOrdines().isEmpty()){
							o=c.getOrdines().iterator().next();
							
							for(AttivitaOrdine a:o.getAttivitaOrdines())
								if(a.getIdAttivitaOrdine()==f.getAttivitaOrdine())
									tariffa=a.getTariffaAttivita();
								
							if(f.getMeseCorrente().compareTo("Mag2013")==0){//se è stato compilato il mese di maggio2013 aggiungo le ore fatturate in quel mese
								oreOrdineIniziali=ServerUtility.aggiornaTotGenerale(oreOrdineIniziali, f.getOreFatturare());
								
							}
							oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
							margine=Float.valueOf(ServerUtility.getOreCentesimi(oreMargine));
							importo=Float.valueOf(o.getTariffaOraria())*Float.valueOf(f.getOreFatturare());
							
							if(Float.valueOf(f.getOreFatturare())!=0)
								numeroOrdine=o.getCodiceOrdine();
							else
								numeroOrdine="#";
								
							datiModel=new DatiFatturazioneCommessaModel(commessa, estensione, attivita, numeroOrdine, numMese,
									f.getMeseCorrente(), tariffa, Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), 
									Float.valueOf(ServerUtility.getOreCentesimi(f.getOreFatturare()))*-1, Float.valueOf(f.getImportoRealeFatturato())*-1,
									importo, Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()))
									, margine);
													
						}
						else{
							tariffa=c.getTariffaSal();
							oreMargine=ServerUtility.aggiornaTotGenerale(String.valueOf(f.getOreFatturare()), String.valueOf(f.getVariazioneSAL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getVariazionePCL()));
							oreMargine=ServerUtility.getDifference(oreMargine, String.valueOf(f.getOreEseguite()));
							margine=Float.valueOf(ServerUtility.getOreCentesimi(oreMargine));
							
							datiModel=new DatiFatturazioneCommessaModel(commessa, estensione, attivita, "", numMese, f.getMeseCorrente(), tariffa, 
									Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf(ServerUtility.getOreCentesimi(f.getOreFatturare())),
									Float.valueOf(f.getImportoRealeFatturato()),	(float)0.0, 
									Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())),
									margine);
						}
						listaDati.add(datiModel);
						
						oreMargine="0.00";					
					}	
					
					if(!c.getOrdines().isEmpty()){
						o=c.getOrdines().iterator().next();
						//per ogni estensione commessa metto l'eventuale importo/num ore ordine, se c'è!
						datiModel=new DatiFatturazioneCommessaModel(commessa, estensione, attivita, o.getCodiceOrdine(), "0", "INIZIALI", tariffa, 
								(float)0, Float.valueOf(ServerUtility.getOreCentesimi(o.getOreBudget())), Float.valueOf(o.getImporto()),(float)0.0, 
								(float)0, (float)0, (float)0);
						listaDati.add(datiModel);
					}
					
					salIniziale=c.getSalAttuale();
					pclIniziale=c.getPclAttuale();
					listaFF.clear();				
				}
			}		
			
			//se esiste la pa sostituisco i valori considerati precedentemente
			if(esistePa){
				salIniziale=c_pa.getSalAttuale();
				pclIniziale=c_pa.getPclAttuale();
			}				
				
			datiModel=new DatiFatturazioneCommessaModel(commessa,"pa", "","", "",".INIZIALI", "", (float)0.0, Float.valueOf(ServerUtility.getOreCentesimi(oreOrdineIniziali)), 
					 Float.valueOf(importoOrdineIniziale), (float)0.0 , Float.valueOf(ServerUtility.getOreCentesimi(salIniziale)), 
					 Float.valueOf(ServerUtility.getOreCentesimi(pclIniziale)), (float)0.0);
			listaDati.add(datiModel);
			
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
		
		Ordine ordine=new Ordine(); 
		List<AttivitaOrdine> listaAttOrdine= new ArrayList<AttivitaOrdine>();
		
		String salDaButtare= new String();
		String flagCompilato= new String();
		String oreEseguite= "0.00";
		String numeroOrdine="#";
		String oggettoOrdine="#";
		String importoFatturato="0.00";
		String totImportoFatturato="0.00";
		Float sal=(float) 0.0;
		Float pcl=(float) 0.0;
		Float[] totaleSalPcl={(float)0.0,(float)0.0};

		numEstensione=numEstensione.toLowerCase();
				
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
	    
		if(numCommessa.compareTo("")!=0){
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		//prendere il foglio fatturazione del mese indicato, se c'è, e prendere le eventuali ore eseguite
		
		try {
			tx = session.beginTransaction();	
			if(numEstensione.compareTo("pa")==0){
				listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and statoCommessa=:stato").
						setParameter("numeroCommessa", numCommessa).setParameter("stato", "Aperta").list();
				
				for(Commessa comm:listaC){
					if(comm.getEstensione().toLowerCase().compareTo("pa")!=0){	//se non è la .pa la inserisco in lista
						if(comm.getOrdines().size()>0){
							numeroOrdine=comm.getOrdines().iterator().next().getCodiceOrdine();
							
							ordine=comm.getOrdines().iterator().next();
							oggettoOrdine=ordine.getDescrizioneAttivita();
							listaAttOrdine.addAll(ordine.getAttivitaOrdines());
							
							for(AttivitaOrdine att:listaAttOrdine){
							
								listaFF.addAll(comm.getFoglioFatturaziones());
								if(listaFF.isEmpty())
									flagCompilato="No";
								else
									for(FoglioFatturazione ff:listaFF){
										
									  if((ff.getAttivitaOrdine()==att.getIdAttivitaOrdine())||(ff.getAttivitaOrdine()==0))
										if(ff.getMeseCorrente().compareTo(data)==0){
											flagCompilato="Si";
											oreEseguite=ff.getOreEseguite();
											sal=Float.valueOf(ff.getVariazioneSAL());
											pcl=Float.valueOf(ff.getVariazionePCL());
											salDaButtare=ff.getFlagSalDaButtare();
											if(Float.valueOf(ff.getImportoRealeFatturato())!=0)
												importoFatturato=ff.getImportoRealeFatturato();
											else
												importoFatturato=d.format(ServerUtility.calcolaImporto(ff.getTariffaUtilizzata(), ff.getOreFatturare()));
											
											break;
										}else{
											flagCompilato="No";
											sal=Float.valueOf("0.00");
											pcl=Float.valueOf("0.00");
											salDaButtare="N";
										}							
									}
								
								totaleSalPcl=ServerUtility.calcolaTotaleSalPclPerEstensione(comm.getNumeroCommessa(), comm.getEstensione(), data);
																
								riep= new RiepilogoOreTotaliCommesse(comm.getNumeroCommessa(), comm.getEstensione(),sal, totaleSalPcl[0], salDaButtare, pcl,totaleSalPcl[1],
										numeroOrdine, oggettoOrdine,att.getDescrizioneAttivita(),  att.getIdAttivitaOrdine(), oreEseguite, Float.valueOf("0.00"), flagCompilato, importoFatturato);
							   	listaRiep.add(riep);
							    oreEseguite="0.00";
							    importoFatturato="0.00";
							    sal=Float.valueOf("0.00");
								pcl=Float.valueOf("0.00");
							    flagCompilato="No";						   
							   
							    listaFF.clear();							
							}	
							numeroOrdine="#";	
							listaAttOrdine.clear();
						
						}
						else {
							
							numeroOrdine="#";
							listaFF.addAll(comm.getFoglioFatturaziones());
							if(listaFF.isEmpty())
								flagCompilato="No";
							else
								for(FoglioFatturazione ff:listaFF){						
									if(ff.getMeseCorrente().compareTo(data)==0){
										flagCompilato="Si";
										oreEseguite=ff.getOreEseguite();
										sal=Float.valueOf(ff.getVariazioneSAL());
										pcl=Float.valueOf(ff.getVariazionePCL());
										salDaButtare=ff.getFlagSalDaButtare();
										if(Float.valueOf(ff.getImportoRealeFatturato())!=0)
											importoFatturato=ff.getImportoRealeFatturato();
										else
											importoFatturato=d.format(ServerUtility.calcolaImporto(ff.getTariffaUtilizzata(), ff.getOreFatturare()));
										
										break;
									}else{
										flagCompilato="No";
										sal=Float.valueOf("0.00");
										pcl=Float.valueOf("0.00");
										salDaButtare="N";
									}							
								}
							
							totaleSalPcl=ServerUtility.calcolaTotaleSalPclPerEstensione(comm.getNumeroCommessa(), comm.getEstensione(), data);
							
							riep= new RiepilogoOreTotaliCommesse(comm.getNumeroCommessa(), comm.getEstensione(),sal, totaleSalPcl[0], salDaButtare, pcl, totaleSalPcl[1], numeroOrdine,"#", "#",
									0,oreEseguite, Float.valueOf("0.00"), flagCompilato, importoFatturato);
							listaRiep.add(riep);
							oreEseguite="0.00";
							importoFatturato="0.00";
							sal=Float.valueOf("0.00");
							pcl=Float.valueOf("0.00");
							flagCompilato="No";
					   
							numeroOrdine="#";	
							oggettoOrdine="#";
							listaFF.clear();						
							
						}					
					}
				}
			}
			
			//viene considerata solo la commessa selezionata
			else{
				
			    c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numCommessa and estensione=:numEstensione").
						setParameter("numCommessa", numCommessa).setParameter("numEstensione", numEstensione).uniqueResult();
				
			    if(!c.getOrdines().isEmpty()){
					numeroOrdine=c.getOrdines().iterator().next().getCodiceOrdine();
					
					ordine=c.getOrdines().iterator().next();
					oggettoOrdine=ordine.getDescrizioneAttivita();
					listaAttOrdine.addAll(ordine.getAttivitaOrdines());
					
					for(AttivitaOrdine att:listaAttOrdine){
						listaFF.addAll(c.getFoglioFatturaziones());
						if(listaFF.isEmpty())
							flagCompilato="No";
						else
						for(FoglioFatturazione ff:listaFF){
							
							//controllo per beccare l'attività giusta
						  if((ff.getAttivitaOrdine()==att.getIdAttivitaOrdine())||(ff.getAttivitaOrdine()==0))
							if(ff.getMeseCorrente().compareTo(data)==0)
							{
								flagCompilato="Si";
								oreEseguite=ff.getOreEseguite();
								sal=Float.valueOf(ff.getVariazioneSAL());
								pcl=Float.valueOf(ff.getVariazionePCL());
								salDaButtare=ff.getFlagSalDaButtare();
								if(Float.valueOf(ff.getImportoRealeFatturato())!=0)
									importoFatturato=ff.getImportoRealeFatturato();
								else
									importoFatturato=d.format(ServerUtility.calcolaImporto(ff.getTariffaUtilizzata(), ff.getOreFatturare()));
								break;
							}else{
								flagCompilato="No";
								sal=Float.valueOf("0.00");
								pcl=Float.valueOf("0.00");
								salDaButtare="N";
							}
						}	
						
						totaleSalPcl=ServerUtility.calcolaTotaleSalPclPerEstensione(numCommessa, numEstensione, data);
						riep= new RiepilogoOreTotaliCommesse(numCommessa, numEstensione,sal, totaleSalPcl[0], salDaButtare, pcl, totaleSalPcl[1], numeroOrdine, oggettoOrdine, 
								att.getDescrizioneAttivita(), att.getIdAttivitaOrdine(), 
								oreEseguite, Float.valueOf("0.00"), flagCompilato, importoFatturato);
						listaRiep.add(riep);					
					}
					listaAttOrdine.clear();
					
				}
				else {
					numeroOrdine="#";
					oggettoOrdine="#";
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
							salDaButtare=ff.getFlagSalDaButtare();
							if(Float.valueOf(ff.getImportoRealeFatturato())!=0)
								importoFatturato=ff.getImportoRealeFatturato();
							else
								importoFatturato=d.format(ServerUtility.calcolaImporto(ff.getTariffaUtilizzata(), ff.getOreFatturare()));
							break;
						}else{
							flagCompilato="No";
							sal=Float.valueOf("0.00");
							pcl=Float.valueOf("0.00");
							salDaButtare="N";
						}
					}
					
					totaleSalPcl=ServerUtility.calcolaTotaleSalPclPerEstensione(numCommessa, numEstensione, data);
					
					riep= new RiepilogoOreTotaliCommesse(numCommessa, numEstensione,sal, totaleSalPcl[0], salDaButtare, pcl, totaleSalPcl[1], numeroOrdine, "" , "",
							0,oreEseguite, Float.valueOf("0.00"), flagCompilato, importoFatturato);
					listaRiep.add(riep);			
				}					
			}
								
			tx.commit();
			
			oreEseguite="0.00";
			String salTotale="0.00";
			String pclTotale="0.00";
			
			for(RiepilogoOreTotaliCommesse r: listaRiep){
				String salF=d.format(r.get("sal"));
				String pclF=d.format(r.get("pcl"));
				oreEseguite=ServerUtility.aggiornaTotGenerale(oreEseguite, r.getOreOrdine());	
				salTotale=ServerUtility.aggiornaTotGenerale(salTotale, salF);
				pclTotale=ServerUtility.aggiornaTotGenerale(pclTotale, pclF);
				//totImportoFatturato=ServerUtility.aggiornaTotGenerale(totImportoFatturato, (String)r.get("importoFatturato"));
				totImportoFatturato=d.format(Float.valueOf(totImportoFatturato)+Float.valueOf((String)r.get("importoFatturato")));
			}
			riep= new RiepilogoOreTotaliCommesse("TOTALE", "", Float.valueOf(salTotale), (float)0.0, "N", Float.valueOf(pclTotale), (float)0.0, "", "", "",
					0, oreEseguite , Float.valueOf("0.00"), "", totImportoFatturato);
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

	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoSALPCLModel> getRiepilogoSalPcl(String data,
			String tabSelected, String pm) {
		
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		List<FoglioFatturazione> listaFF1= new ArrayList<FoglioFatturazione>();
		Ordine o= new Ordine();
		FoglioFatturazione f= new FoglioFatturazione();
		FoglioFatturazione fapp= new FoglioFatturazione();
		RiepilogoSALPCLModel riepM= new RiepilogoSALPCLModel();
		List<RiepilogoSALPCLModel> listaM= new ArrayList<RiepilogoSALPCLModel>();
		
		String commessa= new String();
		String tariffaUtilizzata= new String();
		String sommaVariazioniSal= "0.00";
		String sommaVariazioniPcl= "0.00";
		String sommaVariazioniSal1= "0.00";
		String sommaVariazioniPcl1= "0.00";
		String sommaVariazioniMeseSal= "0.00";
		String sommaVariazioniMesePcl= "0.00";
		String variazioneSal="0.00";
		String variazionePcl="0.00";
		String sommaVar="0.00";
		String cliente="#";
				
		Boolean esito= true;
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
	    
	    int codCommessa;
		Float importo=(float) 0.00;
		Float importoMese=(float) 0.00;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			listaCommesse=(List<Commessa>)session.createQuery("from Commessa where matricolaPM=:pm").
					setParameter("pm", pm).list();
			
			for(Commessa c: listaCommesse){				
				commessa=c.getNumeroCommessa();
				codCommessa=c.getCodCommessa(); //id commessa
				
				if(commessa.compareTo("11017")==0)
					System.out.print("");
				
				o=(Ordine)session.createQuery("from Ordine where cod_commessa=:id").setParameter("id", codCommessa).uniqueResult();
								
				//--------------------------controllo che ci sia del Sal o Pcl residuo sulla commessa dopo tutte le variazioni altrimenti non proseguo
				listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa ")
						.setParameter("numeroCommessa", commessa).list();
					
				//sommo le eventuali iniziali
				/*for(Commessa c1:listaC){
					listaFF1.addAll(c1.getFoglioFatturaziones());
					sommaVariazioniSal1=d.format(Float.valueOf(sommaVariazioniSal1)+ Float.valueOf(ServerUtility.getOreCentesimi(c1.getSalAttuale())));
					sommaVariazioniPcl1=d.format(Float.valueOf(sommaVariazioniPcl1)+ Float.valueOf(ServerUtility.getOreCentesimi(c1.getPclAttuale())));
				}
				//aggiungo tutte le variazioni
				for(FoglioFatturazione ff:listaFF1){
					//if(ServerUtility.isPrecedente(ff.getMeseCorrente(), data))
						//if(ff.getMeseCorrente().compareTo(data)!=0)
							if(tabSelected.compareTo("pcl")==0){
								if(ff.getMeseCorrente().compareTo(data)==0)
									sommaVariazioniMesePcl=d.format(Float.valueOf(sommaVariazioniMesePcl)+ Float.valueOf(ServerUtility.getOreCentesimi(ff.getVariazionePCL())));
								sommaVariazioniPcl1=d.format(Float.valueOf(sommaVariazioniPcl1)+ Float.valueOf(ServerUtility.getOreCentesimi(ff.getVariazionePCL())));
							}
							else{
								if(ff.getMeseCorrente().compareTo(data)==0)
									sommaVariazioniMeseSal=d.format(Float.valueOf(sommaVariazioniMeseSal)+ Float.valueOf(ServerUtility.getOreCentesimi(ff.getVariazioneSAL())));
								sommaVariazioniSal1=d.format(Float.valueOf(sommaVariazioniSal1)+ Float.valueOf(ServerUtility.getOreCentesimi(ff.getVariazioneSAL())));
							}
				}	*/
				
				//controllo se ci sono variazioni positive/negative nel mese
				/*fapp=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese")
						.setParameter("id", codCommessa).setParameter("mese", data).uniqueResult();
				if(fapp!=null){
					if(tabSelected.compareTo("pcl")==0)
						variazionePcl=fapp.getVariazionePCL();
					else
						variazioneSal=fapp.getVariazioneSAL();
				}*/
				//--------------------------
				
				//TODO sistemareeeeeeeeeeeeeeeee 
				
				/*if((sommaVariazioniPcl1.compareTo("0.00")!=0 && tabSelected.compareTo("pcl")==0)
						||(sommaVariazioniPcl1.compareTo("0.00")==0 && fapp!=null && Float.valueOf(fapp.getVariazionePCL())!=0)
						||(sommaVariazioniSal1.compareTo("0.00")!=0 && tabSelected.compareTo("sal")==0)
						||(sommaVariazioniSal1.compareTo("0.00")==0 && fapp!=null && Float.valueOf(fapp.getVariazioneSAL())!=0))	*/
				/*if(((sommaVariazioniPcl1.compareTo("0.00")!=0 || Float.valueOf(sommaVariazioniMesePcl)!=0) &&  tabSelected.compareTo("pcl")==0)						
						||((sommaVariazioniSal1.compareTo("0.00")!=0 || Float.valueOf(sommaVariazioniMeseSal)!=0) && tabSelected.compareTo("sal")==0))*/
				if(o!=null){
					cliente=o.getRda().getCliente().getRagioneSociale();			
					for(AttivitaOrdine att:o.getAttivitaOrdines()){			
						listaFF=(List<FoglioFatturazione>)session.createQuery("from FoglioFatturazione where cod_commessa=:id")
								.setParameter("id", codCommessa).list();		
						if(listaFF!=null)
						for(FoglioFatturazione f1:listaFF){
							if(f1.getAttivitaOrdine()==att.getIdAttivitaOrdine() || f1.getAttivitaOrdine()==0)
							if(ServerUtility.isPrecedente(f1.getMeseCorrente(), data)){
								sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazionePCL())));
								sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf( ServerUtility.getOreCentesimi(f1.getVariazioneSAL())));
							}
						}				
						sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(c.getSalAttuale())));
						sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(c.getPclAttuale())));
						
						tariffaUtilizzata=att.getTariffaAttivita();
							
						f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:idAttivita")
								.setParameter("id", codCommessa).setParameter("idAttivita", att.getIdAttivitaOrdine()).setParameter("mese", data).uniqueResult();
						if(f==null)//doppio controllo per verificare che l'ordine non sia stato inserito successivamente e quindi il foglio fatturazione non abbia regist
							f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:idAttivita")
								.setParameter("id", codCommessa).setParameter("idAttivita", 0).setParameter("mese", data).uniqueResult();
													
						if(f==null){//non c'è foglio di fatturazione quindi non ci sarà variazione nel mese
						
							if(tabSelected.compareTo("pcl")==0)
								sommaVar=sommaVariazioniPcl;
							else
								sommaVar=sommaVariazioniSal;
							
							importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVar);
							
							if(Float.valueOf(sommaVar)!=0 ){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(), cliente
											, c.getDenominazioneAttivita(), Float.valueOf(sommaVar),Float.valueOf("0.00"), 
											Float.valueOf(sommaVar), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
									listaM.add(riepM);
							}
						}
						else{	
							//il foglio fatturazione era già stato compilato quindi tolgo alla sommavariazioni quella in esame
							
							sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
							sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
							
							if(tabSelected.compareTo("pcl")==0){
								importo=Float.valueOf(tariffaUtilizzata)*
										(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
								importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()));
								
								//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()))!=0){
								if(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()))!=0){
									if(!(Float.valueOf(f.getVariazionePCL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
											Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
									}
								}else
									if(Float.valueOf(f.getVariazionePCL())!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
												Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);										
									}
							}
							else{
								importo=Float.valueOf(tariffaUtilizzata)*
										(Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
								importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()));
								//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()))!=0){
								if(Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
									if(!(Float.valueOf(f.getVariazioneSAL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
											Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
									}
								}else
									if(Float.valueOf(f.getVariazioneSAL())!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
												Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
									}									
							}
						}	
						
						sommaVariazioniPcl="0.00";
						sommaVariazioniSal="0.00";
						if(listaFF!=null)
							listaFF.clear();
					}				
				}else{
					listaFF.addAll(c.getFoglioFatturaziones());			
					for(FoglioFatturazione f1:listaFF){					
						if(ServerUtility.isPrecedente(f1.getMeseCorrente(), data)){
							sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazionePCL())));
							sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf( ServerUtility.getOreCentesimi(f1.getVariazioneSAL())));
						}
					}				
					sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(c.getSalAttuale())));
					sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(c.getPclAttuale())));
					
					tariffaUtilizzata=c.getTariffaSal();
					
					f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
							.setParameter("mese", data).uniqueResult();
					
					if(f==null){//non c'è foglio di fatturazione quindi non ci sarà variazione nel mese
						
						if(tabSelected.compareTo("pcl")==0)
							sommaVar=sommaVariazioniPcl;
						else
							sommaVar=sommaVariazioniSal;
										
						importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVar);
						if(Float.valueOf(sommaVar)!=0 ){
							riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
									"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVar),Float.valueOf("0.00"), 
									Float.valueOf(sommaVar), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
							listaM.add(riepM);
						}					
					
					}else{
						//c'è il foglio fatturazione e ci sarà variazione nel mese
						sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
						sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
						
						if(tabSelected.compareTo("pcl")==0){
							//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()));
							//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazionePCL());
							importo=Float.valueOf(tariffaUtilizzata)*
									(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
							importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()));
							
							//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()))!=0){
							if(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()))!=0){
								if(!(Float.valueOf(f.getVariazionePCL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
											Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);
								}
							}else
								if(Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
											Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);										
								}
						}
						else{
							//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()));
							//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazioneSAL());
							importo=Float.valueOf(tariffaUtilizzata)*(Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
							importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()));
							//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()))!=0){
							if(Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
								if(!(Float.valueOf(f.getVariazioneSAL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
											Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);
								}
							}else
								//if(Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
								if(Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
											Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
								}
						}	
					}						
				}
				
				if(listaFF!=null)
					listaFF.clear();
				listaFF1.clear();
				listaC.clear();
				sommaVariazioniPcl="0.00";
				sommaVariazioniSal="0.00";
				sommaVariazioniPcl1="0.00";
				sommaVariazioniSal1="0.00";
				importo=(float)0.00;
				importoMese=(float)0.00;
				
			}//for commessa
			
			
			tx.commit();
		}
		catch (Exception e) {
			esito=false;
			e.printStackTrace();
			if (tx != null)
				tx.rollback();			
			return null;
		}finally{
			if(esito)
				session.close();
		}		
		return listaM;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoSALPCLModel> getRiepilogoSalPclOld(String data,
			String tabSelected) throws IllegalArgumentException {
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		List<RiepilogoSALPCLModel> listaM= new ArrayList<RiepilogoSALPCLModel>();
		String commessa= new String();
		//String estensione= new String();
		String tariffaUtilizzata= new String();
		String sommaVariazioniSal= "0.00";
		String sommaVariazioniPcl= "0.00";
		String cliente="#";
				
		Boolean esistePa= false; //controllo l'esistenza di una eventuale commessa madre .pa
		
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		Commessa c_pa= new Commessa();
		Ordine o= new Ordine();
		FoglioFatturazione f= new FoglioFatturazione();
		RiepilogoSALPCLModel riepM= new RiepilogoSALPCLModel();
		
		int codCommessa;
		Float importo=(float) 0.00;
		Float importoMese=(float) 0.00;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
				
			/*listaCommesse=(List<Commessa>)session.createQuery("from Commessa where statoCommessa=:stato and estensione<>:estensione").
					setParameter("stato", "Aperta").setParameter("estensione", "pa").list();*/
			listaCommesse=(List<Commessa>)session.createQuery("from Commessa where estensione<>:estensione").
					setParameter("estensione", "pa").list();
			
			for(Commessa c: listaCommesse){
								
			 	commessa=c.getNumeroCommessa();
				codCommessa=c.getCodCommessa(); //id commessa
				o=(Ordine)session.createQuery("from Ordine where cod_commessa=:id").setParameter("id", codCommessa).uniqueResult();
										
				//controllo la presenza di una commessa .pa 
				c_pa=(Commessa)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione=:estensione").setParameter("commessa", commessa)
						.setParameter("estensione", "pa").uniqueResult();
				if(c_pa!=null)
					esistePa=true;
												
				if(esistePa){		
					
					// se esiste la Pa devo ciclare sulle attivita ordine perchè potrebbero essercene più di una altrimenti basta prendere la prima sotto l'ordine
				 
					/*listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione<>:estensione and statoCommessa<>:stato").setParameter("commessa", commessa)
							.setParameter("estensione", "pa").setParameter("stato", "Chiusa").list();*/
					listaC=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione<>:estensione").setParameter("commessa", commessa)
							.setParameter("estensione", "pa").list();
					
					for(Commessa c1:listaC)
						listaFF.addAll(c1.getFoglioFatturaziones());			
					
					for(FoglioFatturazione f1:listaFF){ 
						
						if(ServerUtility.isPrecedente(f1.getMeseCorrente(), data)){
							//sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f1.getVariazionePCL());
							//sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f1.getVariazioneSAL());
							sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazionePCL())));
							sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf( ServerUtility.getOreCentesimi(f1.getVariazioneSAL())));
						}
					}
				
					//sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, c_pa.getSalAttuale());
					//sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, c_pa.getPclAttuale());
					sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(c_pa.getSalAttuale())));
					sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(c_pa.getPclAttuale())));
										
					if(o==null){
						tariffaUtilizzata=c.getTariffaSal();
						f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
								.setParameter("mese", data).uniqueResult();
						
						if(f==null){//non c'è foglio di fatturazione quindi non ci sarà variazione nel mese
							
							if(tabSelected.compareTo("pcl")==0){
								//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniPcl);
								importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVariazioniPcl);
								if(Float.valueOf(sommaVariazioniPcl)!=0 && c.getStatoCommessa().compareTo("Conclusa")!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
							    			"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl),Float.valueOf("0.00"), 
							    			Float.valueOf(sommaVariazioniPcl), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
										listaM.add(riepM);
								}
							}
							else{
								importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVariazioniSal);
								if(Float.valueOf(sommaVariazioniSal)!=0 && c.getStatoCommessa().compareTo("Conclusa")!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf("0.00"), 
												Float.valueOf(sommaVariazioniSal), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
										listaM.add(riepM);
									
								}
							}
						}
						else{	
							//il foglio fatturazione era già stato compilato quindi tolgo alla sommavariazioni quella in esame
							//sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
							//sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
							sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
							sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
							
							if(tabSelected.compareTo("pcl")==0){
								//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()));
								//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazionePCL());
								importo=Float.valueOf(tariffaUtilizzata)*
										(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
								importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()));
								
								//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()))!=0){
								if(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()))!=0){
									if(!(Float.valueOf(f.getVariazionePCL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
												Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
									}
								}else
									if(Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
												Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);										
									}
							}
							else{
								//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()));
								//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazioneSAL());
								importo=Float.valueOf(tariffaUtilizzata)*(Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
								importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()));
								//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()))!=0){
								if(Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
									if(!(Float.valueOf(f.getVariazioneSAL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
												Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
									}
								}else
									//if(Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
									if(Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
												Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
											listaM.add(riepM);
									}
							}
						}						
						
					}else{
						
						cliente=o.getRda().getCliente().getRagioneSociale();
						
						for(AttivitaOrdine att:o.getAttivitaOrdines()){
						
							tariffaUtilizzata=att.getTariffaAttivita();
								
							f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:idAttivita")
									.setParameter("id", codCommessa).setParameter("idAttivita", att.getIdAttivitaOrdine()).setParameter("mese", data).uniqueResult();
							if(f==null)//doppio controllo per verificare che l'ordine non sia stato inserito successivamente e quindi il foglio fatturazione non abbia regist
								f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:idAttivita")
									.setParameter("id", codCommessa).setParameter("idAttivita", 0).setParameter("mese", data).uniqueResult();
														
							if(f==null){//non c'è foglio di fatturazione quindi non ci sarà variazione nel mese
							
								if(tabSelected.compareTo("pcl")==0){
									//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniPcl);
									importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVariazioniPcl);
									if(Float.valueOf(sommaVariazioniPcl)!=0 && c.getStatoCommessa().compareTo("Conclusa")!=0){
											riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(), cliente
													, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl),Float.valueOf("0.00"), 
													Float.valueOf(sommaVariazioniPcl), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
											listaM.add(riepM);
										
									}
								}
								else{
									//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniSal);
									importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVariazioniSal);
									if(Float.valueOf(sommaVariazioniSal)!=0 && c.getStatoCommessa().compareTo("Conclusa")!=0){
											riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf("0.00"), 
												Float.valueOf(sommaVariazioniSal), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
											listaM.add(riepM);
									}
									
								}
							}
							else{	
								//il foglio fatturazione era già stato compilato quindi tolgo alla sommavariazioni quella in esame
								//sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
								//sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
								sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
								sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)- Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
								
								if(tabSelected.compareTo("pcl")==0){
									//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()));
									//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazionePCL());
									importo=Float.valueOf(tariffaUtilizzata)*
											(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
									importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()));
									
									//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()))!=0){
									if(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()))!=0){
										if(!(Float.valueOf(f.getVariazionePCL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
											riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
												Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
											listaM.add(riepM);
										}
									}else
										if(Float.valueOf(f.getVariazionePCL())!=0){
											riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
													cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
													Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
													importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
											listaM.add(riepM);										
										}
								}
								else{
									//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()));
									//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazioneSAL());
									importo=Float.valueOf(tariffaUtilizzata)*
											(Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
									importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()));
									//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()))!=0){
									if(Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()))!=0){
										if(!(Float.valueOf(f.getVariazioneSAL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
											riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
												Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
												importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
											listaM.add(riepM);
										}
									}else
										if(Float.valueOf(f.getVariazioneSAL())!=0){
											riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
													cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
													Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
													importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
											listaM.add(riepM);
										}
											
								}
							}						
						}
					}	
				}else
				{ //Non esiste la Pa e quindi non potranno esserci ordini con più tariffe
					
					f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
							.setParameter("mese", data).uniqueResult();
				
					listaFF.addAll(c.getFoglioFatturaziones());			
									
					//Considero tutti i FF compilati in mesi differenti da quello in esame
					for(FoglioFatturazione f1:listaFF)										
						if(ServerUtility.isPrecedente(f1.getMeseCorrente(),data))
							if(f1.getMeseCorrente().compareTo(data)!=0){
								//sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f1.getVariazionePCL());
								//sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f1.getVariazioneSAL());
								sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazionePCL())));
								sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf( ServerUtility.getOreCentesimi(f1.getVariazioneSAL())));
							}
									
					//Aggiungo anche un eventuale SAL/PCL iniziale sulla commessa
					//sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, c.getSalAttuale());
					//sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, c.getPclAttuale());
					sommaVariazioniSal=d.format(Float.valueOf(sommaVariazioniSal)+ Float.valueOf(ServerUtility.getOreCentesimi(c.getSalAttuale())));
					sommaVariazioniPcl=d.format(Float.valueOf(sommaVariazioniPcl)+ Float.valueOf(ServerUtility.getOreCentesimi(c.getPclAttuale())));
					
					
					if(o==null){//se non c'è un ordine associato, permetto l'inserimento di eventuali sal pcl
						tariffaUtilizzata=c.getTariffaSal();//prendo la tariffa della commessa
						cliente="#";
					}
					else{
						tariffaUtilizzata=o.getAttivitaOrdines().iterator().next().getTariffaAttivita();
						cliente=o.getRda().getCliente().getRagioneSociale();
					}
					
					if(f==null){						
						if(tabSelected.compareTo("pcl")==0){
							//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniPcl);
							importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVariazioniPcl);
							if(Float.valueOf(sommaVariazioniPcl)!=0 && c.getStatoCommessa().compareTo("Cocnlusa")!=0){
								riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
										cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl),Float.valueOf("0.00"), 
										Float.valueOf(sommaVariazioniPcl), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
								listaM.add(riepM);
							}
						}
						else{
							//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniSal);
							importo=Float.valueOf(tariffaUtilizzata)*Float.valueOf(sommaVariazioniSal);
							if(Float.valueOf(sommaVariazioniSal)!=0 && c.getStatoCommessa().compareTo("Cocnlusa")!=0){
								riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
									cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf("0.00"), 
									Float.valueOf(sommaVariazioniSal), tariffaUtilizzata, importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
								listaM.add(riepM);
							}
						}						
						
					}
					else{	
							
						if(tabSelected.compareTo("pcl")==0){
							//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()));
							//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazionePCL());
							importo=Float.valueOf(tariffaUtilizzata)*
									(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())));
							importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()));
							//if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()))!=0){
							if(Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL()))!=0){
								if(!(Float.valueOf(f.getVariazionePCL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
										cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
										Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
										importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);	
								}
							}else 
								if(Float.valueOf(f.getVariazionePCL())!=0){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), 
											Float.valueOf(sommaVariazioniPcl)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazionePCL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);	
								}
						}
						else{
							//importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()));
							//importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazioneSAL());
							importo=Float.valueOf(tariffaUtilizzata)*
									(Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())));
							importoMese=Float.valueOf(tariffaUtilizzata)*Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL()));
							if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()))!=0){
								if(!(Float.valueOf(f.getVariazioneSAL())==0 && c.getStatoCommessa().compareTo("Conclusa")==0)){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
										cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
										Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
										importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);
								}
							}else
								if(Float.valueOf(f.getVariazioneSAL())!=0){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), 
											Float.valueOf(sommaVariazioniSal)+Float.valueOf(ServerUtility.getOreCentesimi(f.getVariazioneSAL())), tariffaUtilizzata, 
											importo , Float.valueOf(ServerUtility.getOreCentesimi(f.getOreEseguite())), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);
								}
						}
						
					}														
				}
				
				//listaM.add(riepM);
				listaFF.clear();
				listaC.clear();
				sommaVariazioniPcl="0.00";
				sommaVariazioniSal="0.00";
				importo=(float)0.00;
				importoMese=(float)0.00;
				esistePa=false;
			}
			
			
			//-------------Calcolo totali---------------------------//
			List<String> listaPM= new ArrayList<String>();
			List<RiepilogoSALPCLModel> listaTot= new ArrayList<RiepilogoSALPCLModel>();
			List<RiepilogoSALPCLModel> listaApp= new ArrayList<RiepilogoSALPCLModel>();
			List<String> listaComm= new ArrayList<String>();
			
			listaPM=getNomePM();
			String pm=new String();
			
			String totVarMese="0.00";
			String totAttuale="0.00";
			String totPrecedente="0.00";
			
			Float totTotEuro=(float)0.00;
			Float totVarEuro=(float)0.00;
			
			listaApp.addAll(listaM);
						
			for(String nome:listaPM){
				for(RiepilogoSALPCLModel r:listaApp){
					pm=(String)r.get("pm");					
					if(nome.compareTo(pm)==0){						
						if((Float)r.get("variazione")!=0){
							totVarMese=d.format(Float.valueOf(totVarMese)+(Float) r.get("variazione"));
							totVarEuro=totVarEuro+ (Float) r.get("importoMese");							
						}
						
						if(!isIncluded(listaComm, (String)r.get("numeroCommessa"))){
							listaComm.add((String)r.get("numeroCommessa"));
							totAttuale=d.format(Float.valueOf(totAttuale)+(Float) r.get("attuale"));
							totTotEuro=totTotEuro + (Float) r.get("importoComplessivo");
						}
					}						
				}	
				
				totPrecedente=d.format(Float.valueOf(totAttuale)-Float.valueOf(totVarMese));				
				
				riepM=new RiepilogoSALPCLModel(nome, "TOTALE", "", "", "",Float.valueOf(totPrecedente) , Float.valueOf(totVarMese), 
						Float.valueOf(totAttuale), "", totTotEuro, (float)0.00, (float)0.00, totVarEuro);
				listaTot.add(riepM);
				listaComm.clear();
				totVarMese="0.00";
				totAttuale="0.00";
				totPrecedente="0.00";
				totTotEuro=(float)0.00;
				totVarEuro=(float)0.00;
			}
			
			listaM.addAll(listaTot);
			
			tx.commit();
			return listaM;
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}

	}
	
	
	@SuppressWarnings("unchecked")
	//@Override
	public List<RiepilogoSALPCLModel> getRiepilogoSalPcl1(String data,
			String tabSelected) throws IllegalArgumentException {
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		List<RiepilogoSALPCLModel> listaM= new ArrayList<RiepilogoSALPCLModel>();
		String commessa= new String();
		//String estensione= new String();
		String tariffaUtilizzata= new String();
		String sommaVariazioniSal= "0.00";
		String sommaVariazioniPcl= "0.00";
		String cliente="#";
			
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Commessa> listaC= new ArrayList<Commessa>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
	
		Ordine o= new Ordine();
		RiepilogoSALPCLModel riepM= new RiepilogoSALPCLModel();
		
		int codCommessa;
		Float importo=(float) 0.00;
		Float importoMese=(float) 0.00;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			/*listaCommesse=(List<Commessa>)session.createQuery("from Commessa where statoCommessa=:stato and estensione<>:estensione").
					setParameter("stato", "Aperta").setParameter("estensione", "pa").list();*/
			listaCommesse=(List<Commessa>)session.createQuery("from Commessa where estensione<>:estensione").
					setParameter("estensione", "pa").list();
			
			for(Commessa c: listaCommesse){
				
			 	commessa=c.getNumeroCommessa();
				codCommessa=c.getCodCommessa(); //id commessa
				o=(Ordine)session.createQuery("from Ordine where cod_commessa=:id").setParameter("id", codCommessa).uniqueResult();
											
				listaFF.addAll(c.getFoglioFatturaziones());
				for(FoglioFatturazione ff:listaFF){
					if(ServerUtility.isPrecedente(ff.getMeseCorrente(), data)){
						sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, ff.getVariazionePCL());
						sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, ff.getVariazioneSAL());
					}				
				}			
				
				//Aggiungo anche un eventuale SAL/PCL iniziale sulla commessa
				sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, c.getSalAttuale());
				sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, c.getPclAttuale());
				
				if(o==null){
					tariffaUtilizzata=c.getTariffaSal();
					FoglioFatturazione f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese").setParameter("id", codCommessa)
							.setParameter("mese", data).uniqueResult();
					
					if(f==null){//non c'è foglio di fatturazione quindi non ci sarà variazione nel mese
						
						if(tabSelected.compareTo("pcl")==0){
							importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniPcl);
							if(Float.valueOf(sommaVariazioniPcl)!=0){
						    	riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
						    			"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl),Float.valueOf("0.00"), 
						    			Float.valueOf(sommaVariazioniPcl), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
						    	listaM.add(riepM);
							}
						}
						else{
							importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniSal);
							if(Float.valueOf(sommaVariazioniSal)!=0){
						    	riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
									"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf("0.00"), 
									Float.valueOf(sommaVariazioniSal), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
						    	listaM.add(riepM);
							}
						}
					}
					else{	
						//il foglio fatturazione era già stato compilato quindi tolgo alla sommavariazioni quella in esame
						sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
						sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						
						if(tabSelected.compareTo("pcl")==0){
							importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()));
							importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazionePCL());
							if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()))!=0){
								riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
										"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(f.getVariazionePCL()), 
										Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL())), tariffaUtilizzata, 
										importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
								listaM.add(riepM);
							}else
								if(Float.valueOf(f.getVariazioneSAL())!=0){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(f.getVariazionePCL()), 
											Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL())), tariffaUtilizzata, 
											importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);										
								}
						}
						else{
							importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()));
							importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazioneSAL());
							if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()))!=0){
								riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
									"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(f.getVariazioneSAL()), 
									Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL())), tariffaUtilizzata, 
									importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
								listaM.add(riepM);
							}else
								if(Float.valueOf(f.getVariazioneSAL())!=0){										
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											"#", c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(f.getVariazioneSAL()), 
											Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL())), tariffaUtilizzata, 
											importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
								}
						}
					}						
					
				}else{
					
					cliente=o.getRda().getCliente().getRagioneSociale();
					
					for(AttivitaOrdine att:o.getAttivitaOrdines()){
					
						tariffaUtilizzata=att.getTariffaAttivita();
							
						FoglioFatturazione f=(FoglioFatturazione)session.createQuery("from FoglioFatturazione where cod_commessa=:id and meseCorrente=:mese and attivitaOrdine=:idAttivita")
								.setParameter("id", codCommessa).setParameter("idAttivita", att.getIdAttivitaOrdine()).setParameter("mese", data).uniqueResult();
						
						if(f==null){//non c'è foglio di fatturazione quindi non ci sarà variazione nel mese
						
							if(tabSelected.compareTo("pcl")==0){
								importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniPcl);
								if(Float.valueOf(sommaVariazioniPcl)!=0){
								  riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(), cliente
										, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl),Float.valueOf("0.00"), 
										Float.valueOf(sommaVariazioniPcl), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
								  listaM.add(riepM);
								}
							}
							else{
								importo=ServerUtility.calcolaImporto(tariffaUtilizzata, sommaVariazioniSal);
								if(Float.valueOf(sommaVariazioniSal)!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf("0.00"), 
											Float.valueOf(sommaVariazioniSal), tariffaUtilizzata,importo , Float.valueOf("0.00"), Float.valueOf("0.00"), Float.valueOf("0.00"));
										listaM.add(riepM);
								}
							}
						}
						else{	
							//il foglio fatturazione era già stato compilato quindi tolgo alla sommavariazioni quella in esame
							sommaVariazioniSal=ServerUtility.getDifference(sommaVariazioniSal, f.getVariazioneSAL());
							sommaVariazioniPcl=ServerUtility.getDifference(sommaVariazioniPcl, f.getVariazionePCL());
						
							if(tabSelected.compareTo("pcl")==0){
								importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()));
								importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazionePCL());
								if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL()))!=0){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(f.getVariazionePCL()), 
											Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL())), tariffaUtilizzata, 
											importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);
								}else
									if(Float.valueOf(f.getVariazionePCL())!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniPcl), Float.valueOf(f.getVariazionePCL()), 
												Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f.getVariazionePCL())), tariffaUtilizzata, 
												importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);										
									}
							}
							else{
								importo=ServerUtility.calcolaImporto(tariffaUtilizzata, ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()));
								importoMese=ServerUtility.calcolaImporto(tariffaUtilizzata, f.getVariazioneSAL());
								
								if(Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL()))!=0){
									riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
											cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(f.getVariazioneSAL()), 
											Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL())), tariffaUtilizzata, 
											importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
									listaM.add(riepM);
								}else
									if(Float.valueOf(f.getVariazioneSAL())!=0){
										riepM= new RiepilogoSALPCLModel(c.getMatricolaPM(), commessa, c.getEstensione(),
												cliente, c.getDenominazioneAttivita(), Float.valueOf(sommaVariazioniSal),Float.valueOf(f.getVariazioneSAL()), 
												Float.valueOf(ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f.getVariazioneSAL())), tariffaUtilizzata, 
												importo , Float.valueOf(f.getOreEseguite()), Float.valueOf("0.00"), importoMese);
										listaM.add(riepM);
									}										
							}
						}						
					}
				}
				
				listaFF.clear();
				listaC.clear();
				sommaVariazioniPcl="0.00";
				sommaVariazioniSal="0.00";
				importo=(float)0.00;
				importoMese=(float)0.00;
				
			}
			//-------------Calcolo totali---------------------------//
				List<String> listaPM= new ArrayList<String>();
				List<RiepilogoSALPCLModel> listaTot= new ArrayList<RiepilogoSALPCLModel>();
				List<String> listaComm= new ArrayList<String>();
				listaPM=getNomePM();
				String pm=new String();
				
				String totVarMese="0.00";
				String totAttuale="0.00";
				String totPrecedente="0.00";
				Float totTotEuro=(float)0.00;
				Float totVarEuro=(float)0.00;
				
				for(String nome:listaPM){
					for(RiepilogoSALPCLModel r:listaM){
						pm=(String)r.get("pm");
						
						if(nome.compareTo(pm)==0)
							
							if(!isIncluded(listaComm, (String)r.get("numeroCommessa"))){
								listaComm.add((String)r.get("numeroCommessa"));
								totPrecedente=ServerUtility.aggiornaTotGenerale(totPrecedente,d.format((Float) r.get("precedente")));
								totVarMese=ServerUtility.aggiornaTotGenerale(totVarMese,d.format((Float) r.get("variazione")));
								totAttuale=ServerUtility.aggiornaTotGenerale(totAttuale,d.format((Float) r.get("attuale")));
								
								totTotEuro=totTotEuro + (Float) r.get("importoComplessivo");
								totVarEuro=totVarEuro+ (Float) r.get("importoMese");
							}			
					}	
					
					riepM=new RiepilogoSALPCLModel(nome, "TOTALE", "", "", "",Float.valueOf(totPrecedente) , Float.valueOf(totVarMese), 
							Float.valueOf(totAttuale), "", totTotEuro, (float)0.00, (float)0.00, totVarEuro);
					listaTot.add(riepM);
					listaComm.clear();
					totVarMese="0.00";
					totAttuale="0.00";
					totPrecedente="0.00";
					totTotEuro=(float)0.00;
					totVarEuro=(float)0.00;
				}
				
				listaM.addAll(listaTot);
			
			tx.commit();
		}
		catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}		
		return listaM;
	}
	
	
	private boolean isIncluded(List<String> listaComm, String commessa) {	
		for(String c:listaComm){
			if(c.compareTo(commessa)==0)
				return true;			
		}
		return false;		
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoSALPCLModel> getRiepilogoSalPclRiassunto(String data,
			String tabSelected) throws IllegalArgumentException {
		List<RiepilogoSALPCLModel> listaM= new ArrayList<RiepilogoSALPCLModel>();
		
		String commessa= new String();	
		String cliente="";
		Boolean esistePa= false; //controllo l'esistenza di una eventuale commessa madre .pa
		
		List<Commessa> listaC= new ArrayList<Commessa>();
		List<Commessa> listaCPa= new ArrayList<Commessa>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		Commessa c_pa= new Commessa();
		List<String> listaNomiCommesse= new ArrayList<String>();
		
		RiepilogoSALPCLModel riepM= new RiepilogoSALPCLModel();
				
		Float importoSal=(float) 0.00;
		Float importoPcl=(float) 0.00;
		Float oreSal=(float) 0.00;
		Float orePcl=(float) 0.00;
		
		List<Personale> listaP= new ArrayList<Personale>();
		//FoglioFatturazione ff= new FoglioFatturazione();
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			listaP=(List<Personale>)session.createQuery("from Personale where ruolo=:ruolo or ruolo=:ruolo2")
					.setParameter("ruolo", "PM").setParameter("ruolo2", "DIR").list();
			
			for(Personale p:listaP){
				String matricolaPM=p.getCognome()+" "+p.getNome();
				
				/*listaC=(List<Commessa>)session.createQuery("from Commessa where estensione<>:estensione and matricolaPM=:pm and statoCommessa=:stato")
						.setParameter("estensione", "pa").setParameter("pm", matricolaPM).setParameter("stato", "Aperta").list();*/
				listaC=(List<Commessa>)session.createQuery("from Commessa where estensione<>:estensione and matricolaPM=:pm")
						.setParameter("estensione", "pa").setParameter("pm", matricolaPM).list();
				
				for(Commessa c:listaC){
				 if(!ServerUtility.esisteCommessa(c.getNumeroCommessa(), listaNomiCommesse)){
					 //salvo le commesse che incontro per evitare di cumulare il totale per ogni estensione
					listaNomiCommesse.add(c.getNumeroCommessa());
					commessa=c.getNumeroCommessa();
					if(c.getOrdines().iterator().hasNext())
						cliente=c.getOrdines().iterator().next().getRda().getCliente().getRagioneSociale();
					else
						cliente="#";
					c_pa=(Commessa)session.createQuery("from Commessa where numeroCommessa=:commessa and estensione=:estensione").setParameter("commessa", commessa)
							.setParameter("estensione", "pa").uniqueResult();
					
					if(c_pa==null)
						esistePa=false;
					else
						esistePa=true;
					
					if(esistePa){
						
							listaNomiCommesse.add(c.getNumeroCommessa());
							/*listaCPa=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:commessa " +
									"and estensione<>:estensione and matricolaPM=:pm").setParameter("commessa", commessa)
									.setParameter("estensione", "pa").setParameter("pm", matricolaPM).list();*/
							
							listaCPa=(List<Commessa>)session.createQuery("from Commessa where numeroCommessa=:commessa " +
									"and estensione<>:estensione").setParameter("commessa", commessa)
									.setParameter("estensione", "pa").list();
							
							for(Commessa c1:listaCPa)
								listaFF.addAll(c1.getFoglioFatturaziones());			
							
							for(FoglioFatturazione f1:listaFF){ 					
								if(ServerUtility.isPrecedente(f1.getMeseCorrente(), data)){
									
									oreSal= oreSal + Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazioneSAL()));
									orePcl= orePcl + Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazionePCL()));
									
									importoSal= importoSal + ServerUtility.calcolaImporto(c_pa.getTariffaSal(), f1.getVariazioneSAL());
									importoPcl= importoPcl + ServerUtility.calcolaImporto(c_pa.getTariffaSal(), f1.getVariazionePCL());
								}
							}
							listaFF.clear();
											
							importoSal= importoSal +ServerUtility.calcolaImporto(c_pa.getTariffaSal(), c_pa.getSalAttuale());
							importoPcl= importoPcl+ServerUtility.calcolaImporto(c_pa.getTariffaSal(), c_pa.getPclAttuale());
							
							oreSal= oreSal + Float.valueOf(ServerUtility.getOreCentesimi(c_pa.getSalAttuale()));
							orePcl= orePcl + Float.valueOf(ServerUtility.getOreCentesimi(c_pa.getPclAttuale())); 
																		
							if(importoSal!=(float)0.00 || importoPcl!=(float)0.00){
								riepM= new RiepilogoSALPCLModel(matricolaPM, commessa, "", cliente, "", (float)0.00, (oreSal), 
									orePcl, "", importoSal, importoPcl, (float)0.00, (float)0.00);
								listaM.add(riepM);	
								importoSal=(float)0.00;
								importoPcl=(float)0.00;								
								oreSal=(float)0.00;
								orePcl=(float)0.00;
							}	
							
							importoSal=(float)0.00;
							importoPcl=(float)0.00;								
							oreSal=(float)0.00;
							orePcl=(float)0.00;
																		
					}else{
					  
						listaFF.addAll(c.getFoglioFatturaziones());			
						
						for(FoglioFatturazione f1:listaFF){ 
							
							if(ServerUtility.isPrecedente(f1.getMeseCorrente(), data)){
								//sommaVariazioniPcl=ServerUtility.aggiornaTotGenerale(sommaVariazioniPcl, f1.getVariazionePCL());
								//sommaVariazioniSal=ServerUtility.aggiornaTotGenerale(sommaVariazioniSal, f1.getVariazioneSAL());
								oreSal= oreSal + Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazioneSAL()));
								orePcl= orePcl + Float.valueOf(ServerUtility.getOreCentesimi(f1.getVariazionePCL()));
								
								importoSal= importoSal +ServerUtility.calcolaImporto(c.getTariffaSal(), f1.getVariazioneSAL());
								importoPcl= importoPcl+ServerUtility.calcolaImporto(c.getTariffaSal(), f1.getVariazionePCL());								
							}
						}
						listaFF.clear();	
						oreSal= oreSal + Float.valueOf(ServerUtility.getOreCentesimi(c.getSalAttuale()));
						orePcl= orePcl + Float.valueOf(ServerUtility.getOreCentesimi(c.getPclAttuale())); 
						
						importoSal= importoSal +ServerUtility.calcolaImporto(c.getTariffaSal(), c.getSalAttuale());
						importoPcl= importoPcl+ServerUtility.calcolaImporto(c.getTariffaSal(), c.getPclAttuale());
					  
						if(importoSal!=(float)0.00 || importoPcl!=(float)0.00){
							riepM= new RiepilogoSALPCLModel(matricolaPM, commessa, "", cliente, "", (float)0.00, oreSal, 
								orePcl, "", importoSal, importoPcl, (float)0.00, (float)0.00);
							listaM.add(riepM);	
							importoSal=(float)0.00;
							importoPcl=(float)0.00;
							oreSal=(float)0.00;
							orePcl=(float)0.00;
						}		
						
						importoSal=(float)0.00;
						importoPcl=(float)0.00;								
						oreSal=(float)0.00;
						orePcl=(float)0.00;
					  }					  
				 	}
				}
					
				listaNomiCommesse.clear();
				listaC.clear();								
			}
				
			tx.commit();
			return listaM;
			
		} catch (Exception e) {
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
	public List<RiepilogoOreAnnualiDipendente> getRiepilogoAnnualeOreDipendenti(
			String anno, String sede) throws IllegalArgumentException {
		
		List<RiepilogoOreAnnualiDipendente> listaR= new ArrayList<RiepilogoOreAnnualiDipendente>();
		List<Personale> listaP= new ArrayList<Personale>();
		List<FoglioOreMese> listaF= new ArrayList<FoglioOreMese>();
		List<DettaglioOreGiornaliere> listaG= new ArrayList<DettaglioOreGiornaliere>();
		RiepilogoOreAnnualiDipendente riep= new RiepilogoOreAnnualiDipendente();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		String oreOrdinarie="0.00";
		String oreStraOrdinarie="0.00";
		String orePermessoROL="0.00";
		String oreRecupero="0.00";
		String oreViaggio="0.00";
		String oreTotali="0.00";
		String oreFerie="0.00";
		String oreMutua="0.00";
		String oreCassa="0.00";
		String oreLegge104="0.00";
		String oreMaternita="0.00";
		String oreTotaliGiustificativi="0.00";
		
		try {
			tx=session.beginTransaction();
			
			listaP=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede").setParameter("sede", sede).list();
			
			for(Personale p:listaP){
				
				listaF.addAll(p.getFoglioOreMeses());
				
				for(FoglioOreMese f: listaF){
					if(f.getMeseRiferimento().compareTo("Feb2013")!=0 && f.getMeseRiferimento().compareTo("Mar2013")!=0 &&
						f.getMeseRiferimento().compareTo("Apr2013")!=0 && f.getMeseRiferimento().compareTo("Mag2013")!=0 )
						listaG.addAll(f.getDettaglioOreGiornalieres());
				}				
				
				
				for(DettaglioOreGiornaliere d:listaG){
					if(d.getGiustificativo().compareTo("")!=0)
						if(d.getGiustificativo().compareTo("06.Malattia")==0 || d.getGiustificativo().compareTo("07.Infortunio")==0)
							oreMutua=ServerUtility.aggiornaTotGenerale(oreMutua, d.getDeltaOreGiorno());
						else if(d.getGiustificativo().compareTo("24.Cassa Integrazione")==0)
							oreCassa=ServerUtility.aggiornaTotGenerale(oreCassa, d.getDeltaOreGiorno());
						else if(d.getGiustificativo().compareTo("25.Permesso Legge 104")==0)
							oreLegge104=ServerUtility.aggiornaTotGenerale(oreLegge104, d.getDeltaOreGiorno());
						else if(d.getGiustificativo().compareTo("08.Maternita' Obblig.")==0 || d.getGiustificativo().compareTo("09.Maternita' Facolt.")==0
								|| d.getGiustificativo().compareTo("09.1.Maternita' Antic.")==0)
							oreMaternita=ServerUtility.aggiornaTotGenerale(oreMaternita, d.getDeltaOreGiorno());
												
					oreViaggio=ServerUtility.aggiornaTotGenerale(oreViaggio, d.getDeltaOreViaggio());
					oreFerie=ServerUtility.aggiornaTotGenerale(oreFerie, d.getOreFerie());
					orePermessoROL=ServerUtility.aggiornaTotGenerale(orePermessoROL, d.getOrePermesso());
					oreStraOrdinarie=ServerUtility.aggiornaTotGenerale(oreStraOrdinarie, d.getOreStraordinario());
					oreRecupero=ServerUtility.aggiornaTotGenerale(oreRecupero, d.getOreAssenzeRecupero());
					oreOrdinarie=ServerUtility.aggiornaTotGenerale(oreOrdinarie, d.getTotaleOreGiorno());
												
				}
				
				if(Float.valueOf(oreRecupero)>0)
					oreOrdinarie=ServerUtility.getDifference(oreOrdinarie, oreRecupero);
				oreOrdinarie=ServerUtility.getDifference(oreOrdinarie, oreStraOrdinarie);
				
				oreTotali=ServerUtility.aggiornaTotGenerale(oreTotali, oreOrdinarie);
				oreTotali=ServerUtility.aggiornaTotGenerale(oreTotali, oreViaggio);
				oreTotali=ServerUtility.aggiornaTotGenerale(oreTotali, oreStraOrdinarie);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreMaternita);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreMutua);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreFerie);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, orePermessoROL);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreLegge104);
				oreTotaliGiustificativi=ServerUtility.aggiornaTotGenerale(oreTotaliGiustificativi, oreCassa);
				
				riep=new RiepilogoOreAnnualiDipendente(anno, p.getCognome(), p.getNome(), Float.valueOf(oreOrdinarie), Float.valueOf(oreStraOrdinarie), Float.valueOf(oreRecupero),
						Float.valueOf(oreViaggio), Float.valueOf(oreTotali), Float.valueOf(oreFerie), Float.valueOf(orePermessoROL), Float.valueOf(oreMutua), Float.valueOf(oreCassa)
						, Float.valueOf(oreLegge104), Float.valueOf(oreMaternita), Float.valueOf(oreTotaliGiustificativi));
						
				listaR.add(riep);
				
				oreOrdinarie="0.00";
				oreStraOrdinarie="0.00";
				oreRecupero="0.00";
				oreViaggio="0.00";
				oreTotali="0.00";
				oreFerie="0.00";
				oreMutua="0.00";
				oreCassa="0.00";
				oreLegge104="0.00";
				oreMaternita="0.00";
				oreTotaliGiustificativi="0.00";
				orePermessoROL="0.00";
				
				listaF.clear();
				listaG.clear();			
			}			
			
			tx.commit();
			return listaR;
		} catch (Exception e) {
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
	public List<RiepilogoOreNonFatturabiliModel> getRiepilogoOreNonFatturate(
			String data, String groupBy) throws IllegalArgumentException {
		
		RiepilogoOreNonFatturabiliModel riep;
		List<RiepilogoOreNonFatturabiliModel> listaRO= new ArrayList<RiepilogoOreNonFatturabiliModel>();
		List<Personale> listaP= new ArrayList<Personale>();	
		List<AssociazionePtoA> listaAss=new ArrayList<AssociazionePtoA>();
		List<Commessa> listaCommAss= new ArrayList<Commessa>();
		List<DettaglioIntervalliCommesse> listaDettCommAll= new ArrayList<DettaglioIntervalliCommesse>();
		List<String> listaMesiConsiderati= new ArrayList<String>();
		List<DettaglioIntervalliCommesse> listaDettComm= new ArrayList<DettaglioIntervalliCommesse>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		CostoAzienda costoP= new CostoAzienda();		
		
		String totOreCommMese="0.00";
		String totMesi[]= new String[12];
		String totOre="0.00";
		String costoOrario="0.00";
		Float costoEffettivo;
		String numeroCommessa;
		Float importoSal=(float)0;
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat df= new DecimalFormat(pattern,formatSymbols);
	    
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx=session.beginTransaction();
			
			listaMesiConsiderati=ServerUtility.getListaMesiPerAnno(data);
			
			//Le commesse devono essere di tipo "i" interne
			listaCommAss=(List<Commessa>)session.createQuery("from Commessa where tipoCommessa=:tipo").setParameter("tipo", "i").list();
			
			for(Commessa c:listaCommAss){
				for(String mese:listaMesiConsiderati){
					
					listaDettComm=session.createQuery("select d from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g " +
							" join g.foglioOreMese f where" +
							" d.numeroCommessa=:numCommessa and d.estensioneCommessa=:estensione" +
							" and f.meseRiferimento=:mese").setParameter("numCommessa", c.getNumeroCommessa())
							.setParameter("estensione", c.getEstensione())
							.setParameter("mese", mese).list();			
															
					for(DettaglioIntervalliCommesse d:listaDettComm)					
						//prendo tutti i dip che hanno lavorato su quella commessa nei mesi considerati
						if(ServerUtility.isNotIncludedPersonale(listaP, d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale()))
							listaP.add(d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale());
					
					listaDettCommAll.addAll(listaDettComm);
				}
			}						
			
			
			for(Personale p:listaP){
				if(p.getGruppoLavoro().compareTo("Indiretti")!=0)//i dipendenti considerati non devono essere indiretti
					for(Commessa c:listaCommAss){
						for(String mese:listaMesiConsiderati){
							for(DettaglioIntervalliCommesse d:listaDettCommAll){							
								if(d.getNumeroCommessa().compareTo(c.getNumeroCommessa())==0 && d.getEstensioneCommessa().compareTo(c.getEstensione())==0
										&& mese.compareTo(d.getDettaglioOreGiornaliere().getFoglioOreMese().getMeseRiferimento())==0
										&& p.getId_PERSONALE()==d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale().getId_PERSONALE() ){
									totOreCommMese=ServerUtility.aggiornaTotGenerale(totOreCommMese, d.getOreLavorate());
									totOreCommMese=ServerUtility.aggiornaTotGenerale(totOreCommMese, d.getOreViaggio());											
								}
							}
							totOreCommMese=ServerUtility.getOreCentesimi(totOreCommMese);
							
							totMesi[listaMesiConsiderati.indexOf(mese)]=totOreCommMese;
							totOreCommMese="0.00";						
						}//mesi	
						
						for(int i=0; i<12;i++)
							totOre=df.format(Float.valueOf(totOre)+Float.valueOf(totMesi[i]));
						
						if(p.getCostoAziendas().iterator().hasNext()){
							costoP=p.getCostoAziendas().iterator().next();
							
							//TODO
							
							//se non carico i costi mi darà errore!!!
							//costoOrario=df.format(Float.valueOf(costoP.getCostoAnnuo())/Float.valueOf(costoP.getOrePianificate()));		
							
							costoEffettivo=Float.valueOf(totOre)* Float.valueOf(costoOrario);
						}else
						{
							costoOrario="0.00";
							costoEffettivo=(float)0;
						}
						
						if(Float.valueOf(totOre)!=0){
					
							riep=new RiepilogoOreNonFatturabiliModel(p.getSedeOperativa(), c.getMatricolaPM(), c.getDenominazioneAttivita()
								, p.getCognome()+" "+ p.getNome(), Float.valueOf(totMesi[0]), Float.valueOf(totMesi[1]), Float.valueOf(totMesi[2]), Float.valueOf(totMesi[3]), Float.valueOf(totMesi[4])
								, Float.valueOf(totMesi[5]), Float.valueOf(totMesi[6]), Float.valueOf(totMesi[7]), Float.valueOf(totMesi[8]), Float.valueOf(totMesi[9]), Float.valueOf(totMesi[10]), 
								Float.valueOf(totMesi[11]), costoOrario, Float.valueOf(totOre), costoEffettivo);
							listaRO.add(riep);
							totOre="0.00";
						}
				}//commessa			
			}
			
			//Aggiungo record anche per commesse con fogli fatturazione con ore di sal scartate
			listaFF=(List<FoglioFatturazione>)session.createQuery("from FoglioFatturazione where flagSalDaButtare=:flag").setParameter("flag", "S").list();
			for(FoglioFatturazione f:listaFF){
				numeroCommessa=f.getCommessa().getNumeroCommessa()+"."+f.getCommessa().getEstensione();
				for(String mese:listaMesiConsiderati)
					if(mese.compareTo(f.getMeseCorrente())==0){
						totMesi[listaMesiConsiderati.indexOf(mese)]=f.getVariazioneSAL().substring(1, f.getVariazioneSAL().length());
						totOre=f.getVariazioneSAL().substring(1, f.getVariazioneSAL().length());
					}
					else
						totMesi[listaMesiConsiderati.indexOf(mese)]="0.00";
				if(Float.valueOf(totOre)!=0){
					riep=new RiepilogoOreNonFatturabiliModel("#", f.getCommessa().getMatricolaPM(), "SAL scartato", numeroCommessa, Float.valueOf(totMesi[0]), Float.valueOf(totMesi[1]), Float.valueOf(totMesi[2]), Float.valueOf(totMesi[3]), Float.valueOf(totMesi[4])
						, Float.valueOf(totMesi[5]), Float.valueOf(totMesi[6]), Float.valueOf(totMesi[7]), Float.valueOf(totMesi[8]), Float.valueOf(totMesi[9]), Float.valueOf(totMesi[10]), 
						Float.valueOf(totMesi[11]), f.getTariffaUtilizzata(), Float.valueOf(totOre), importoSal);
					listaRO.add(riep);
				}
			}
		
			tx.commit();
			return listaRO;
			
		} catch (Exception e) {
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
	public List<RiepilogoOreNonFatturabiliModel> getRiepilogoOreIndiretti(
			String data, String string) throws IllegalArgumentException {
		RiepilogoOreNonFatturabiliModel riep;
		List<RiepilogoOreNonFatturabiliModel> listaRO= new ArrayList<RiepilogoOreNonFatturabiliModel>();
		List<Personale> listaP= new ArrayList<Personale>();	
		List<Commessa> listaCommAss= new ArrayList<Commessa>();
		List<String> listaMesiConsiderati= new ArrayList<String>();
		List<DettaglioIntervalliCommesse> listaDettComm= new ArrayList<DettaglioIntervalliCommesse>();
		List<DettaglioIntervalliCommesse> listaDettCommAll= new ArrayList<DettaglioIntervalliCommesse>();
		
		CostoAzienda costoP= new CostoAzienda();		
		
		String totOreCommMese="0.00";
		String totMesi[]= new String[12];
		String totOre="0.00";
		String costoOrario="0.00";
		Float costoEffettivo;
				
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat df= new DecimalFormat(pattern,formatSymbols);
	    
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			tx=session.beginTransaction();
		
			listaMesiConsiderati=ServerUtility.getListaMesiPerAnno(data);			
			
			/*for(Personale p:listaP){
				
				if(!p.getAssociazionePtoas().isEmpty())
					listaAss.addAll(p.getAssociazionePtoas());
				else 
					continue;
				
				//considero tutte le commesse associate al dipendente considerato
				for(AssociazionePtoA ass:listaAss)
					listaCommAss.add(ass.getAttivita().getCommessa());
								
					for(Commessa c:listaCommAss){
						if(c.getTipoCommessa().compareTo("i")==0){
							for(String mese:listaMesiConsiderati){
						
								listaDettComm=session.createQuery("select d from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g " +
									" join g.foglioOreMese f " +
									" join f.personale p where p.ID_PERSONALE =:idP " + 
									" and d.numeroCommessa=:numCommessa and d.estensioneCommessa=:estensione" +
									" and f.meseRiferimento=:mese").setParameter("numCommessa", c.getNumeroCommessa()).setParameter("estensione", c.getEstensione())
									.setParameter("idP", p.getId_PERSONALE()).setParameter("mese", mese).list();										
						
								for(DettaglioIntervalliCommesse d:listaDettComm){
									totOreCommMese=ServerUtility.aggiornaTotGenerale(totOreCommMese, d.getOreLavorate());
									totOreCommMese=ServerUtility.aggiornaTotGenerale(totOreCommMese, d.getOreViaggio());
								}
									
								totOreCommMese=ServerUtility.getOreCentesimi(totOreCommMese);
								
								totMesi[listaMesiConsiderati.indexOf(mese)]=totOreCommMese;
								totOreCommMese="0.00";
								listaDettComm.clear();						
							}//mesi	
							
							for(int i=0; i<12;i++)
								totOre=df.format(Float.valueOf(totOre)+Float.valueOf(totMesi[i]));
							
							if(p.getCostoAziendas().iterator().hasNext()){
								costoP=p.getCostoAziendas().iterator().next();
								
								//se non carico i costi mi darà errore!!!
								costoOrario=df.format(Float.valueOf(costoP.getCostoAnnuo())/Float.valueOf(costoP.getOrePianificate()));		
								
								costoEffettivo=Float.valueOf(totOre)* Float.valueOf(costoOrario);
							}else
							{
								costoOrario="0.00";
								costoEffettivo=(float)0;
							}
							
							if(Float.valueOf(totOre)!=0){							
								riep=new RiepilogoOreNonFatturabiliModel(p.getSedeOperativa(), p.getGruppoLavoro(), c.getAttivitas().iterator().next().getDescrizioneAttivita()
									, p.getCognome()+" "+ p.getNome(), Float.valueOf(totMesi[0]), Float.valueOf(totMesi[1]), Float.valueOf(totMesi[2]), Float.valueOf(totMesi[3]), Float.valueOf(totMesi[4])
									, Float.valueOf(totMesi[5]), Float.valueOf(totMesi[6]), Float.valueOf(totMesi[7]), Float.valueOf(totMesi[8]), Float.valueOf(totMesi[9]), Float.valueOf(totMesi[10]), 
									Float.valueOf(totMesi[11]), costoOrario, Float.valueOf(totOre), costoEffettivo);
								listaRO.add(riep);
								totOre="0.00";
							}
						}
					}//commessa
				
				listaCommAss.clear();
				listaAss.clear();			
			}	//personale	*/
			
			
			listaCommAss=(List<Commessa>)session.createQuery("from Commessa where tipoCommessa=:tipo").setParameter("tipo", "i").list();
			
			for(Commessa c:listaCommAss){
				for(String mese:listaMesiConsiderati){
					
					listaDettComm=session.createQuery("select d from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g " +
							" join g.foglioOreMese f where" +
							" d.numeroCommessa=:numCommessa and d.estensioneCommessa=:estensione" +
							" and f.meseRiferimento=:mese").setParameter("numCommessa", c.getNumeroCommessa())
							.setParameter("estensione", c.getEstensione())
							.setParameter("mese", mese).list();			
															
					for(DettaglioIntervalliCommesse d:listaDettComm)					
						//prendo tutti i dip che hanno lavorato su quella commessa nei mesi considerati
						if(ServerUtility.isNotIncludedPersonale(listaP, d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale()))
							listaP.add(d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale());
					
					listaDettCommAll.addAll(listaDettComm);
				}
			}						
						
			for(Personale p:listaP){
				if(p.getGruppoLavoro().compareTo("Indiretti")==0)
					for(Commessa c:listaCommAss){
						for(String mese:listaMesiConsiderati){
							for(DettaglioIntervalliCommesse d:listaDettCommAll){
								
								if(d.getNumeroCommessa().compareTo(c.getNumeroCommessa())==0 && d.getEstensioneCommessa().compareTo(c.getEstensione())==0
										&& mese.compareTo(d.getDettaglioOreGiornaliere().getFoglioOreMese().getMeseRiferimento())==0
										&& p.getId_PERSONALE()==d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale().getId_PERSONALE() ){
									totOreCommMese=ServerUtility.aggiornaTotGenerale(totOreCommMese, d.getOreLavorate());
									totOreCommMese=ServerUtility.aggiornaTotGenerale(totOreCommMese, d.getOreViaggio());
											
								}
							}
							totOreCommMese=ServerUtility.getOreCentesimi(totOreCommMese);
							
							totMesi[listaMesiConsiderati.indexOf(mese)]=totOreCommMese;
							totOreCommMese="0.00";						
						}//mesi	
						
						for(int i=0; i<12;i++)
							totOre=df.format(Float.valueOf(totOre)+Float.valueOf(totMesi[i]));
						
						if(p.getCostoAziendas().iterator().hasNext()){
							costoP=p.getCostoAziendas().iterator().next();
							
							//se non carico i costi mi darà errore!!!
							//costoOrario=df.format(Float.valueOf(costoP.getCostoAnnuo())/Float.valueOf(costoP.getOrePianificate()));		
							
							
							//TODO
							
							costoEffettivo=Float.valueOf(totOre)* Float.valueOf(costoOrario);
						}else
						{
							costoOrario="0.00";
							costoEffettivo=(float)0;
						}
						
						if(Float.valueOf(totOre)!=0){
					
							riep=new RiepilogoOreNonFatturabiliModel(p.getSedeOperativa(), p.getGruppoLavoro(), c.getDenominazioneAttivita()
								, p.getCognome()+" "+ p.getNome(), Float.valueOf(totMesi[0]), Float.valueOf(totMesi[1]), Float.valueOf(totMesi[2]), Float.valueOf(totMesi[3]), Float.valueOf(totMesi[4])
								, Float.valueOf(totMesi[5]), Float.valueOf(totMesi[6]), Float.valueOf(totMesi[7]), Float.valueOf(totMesi[8]), Float.valueOf(totMesi[9]), Float.valueOf(totMesi[10]), 
								Float.valueOf(totMesi[11]), costoOrario, Float.valueOf(totOre), costoEffettivo);
							listaRO.add(riep);
							totOre="0.00";
						}			
				}//commessa			
			}
						
			tx.commit();
			return listaRO;
			
		} catch (Exception e) {
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
	public List<RiepilogoOreDipFatturazione> getRiepilogoTotCommessePerDipendenti(String mese, String sede, String pm) 
			throws IllegalArgumentException{
		
		List<RiepilogoOreDipFatturazione> listaR= new ArrayList<RiepilogoOreDipFatturazione>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		//List<Commessa> listaCommesseTutti= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();
		
		List<AssociazionePtoA> listaAssociazioni= new ArrayList<AssociazionePtoA>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalli= new ArrayList<DettaglioIntervalliCommesse>();
		//usa una lista per salvare per ogni dipendente il suo identificativo e il numero totale di ore rilevate dagli IU
		List<RiepilogoOreDipFatturazione> listaAppCalcoloOreIU= new ArrayList<RiepilogoOreDipFatturazione>();
		RiepilogoOreDipFatturazione riep= new RiepilogoOreDipFatturazione();
		
		String numeroCommessa= new String();
		String commessa= new String();
		String estensione= new String();
		String descrizione= new String();
		String dipendente= new String();
		String oreLavoro= new String();
		String oreViaggio= new String();
		String oreTotMeseLavoro= "0.0";
		String oreTotMeseViaggio= "0.0";
		String oreSommaLavoroViaggio="0.0";
		String oreTotLavoroCommessa="0.0";
		String oreTotViaggioCommessa="0.0";
		String oreTotCommessa="0.0";
		String totaleOreDaIU="0.00";
		int idDip;
		
		String statoCommessa="Aperta";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx = session.beginTransaction();
			
			listaCommesse = (List<Commessa>) session.createQuery("from Commessa where statoCommessa=:stato")
				.setParameter("stato", statoCommessa).list() ;
			

			for (Commessa c : listaCommesse) {
				if (c.getAttivitas().size() > 0)
					//listaAttivita.add(c.getAttivitas().iterator().next());
					listaAttivita.addAll(c.getAttivitas());
			}

			for (Attivita a : listaAttivita) {  // in questo caso la lista  Attivita rappresenta la lista di commesse associate al PM
												// selezionato: ottengo tutte le associazioni e quindi tutti i dipendenti associati a quella commessa
				listaAssociazioni.addAll(a.getAssociazionePtoas());
				commessa = a.getCommessa().getNumeroCommessa();
				estensione = a.getCommessa().getEstensione();
				
				descrizione=a.getCommessa().getDenominazioneAttivita();
								
				numeroCommessa =(commessa + "." + estensione+" ("+descrizione+")"); //una stringa più dettagliata che descriva la commessa
				
				listaP=session.createQuery("select distinct p from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g" +
						" join g.foglioOreMese f join f.personale p" +
						" where f.meseRiferimento=:mese and d.numeroCommessa=:numeroCommessa and d.estensioneCommessa=:estensioneCommessa")
						.setParameter("numeroCommessa", commessa).setParameter("estensioneCommessa", estensione).setParameter("mese", mese).list();
				
				for (Personale p : listaP) {// per ogni dipendente in questa commessa selezioni i fogli ore del mese desiderato e della sede voluta
						
						if(p.getCognome().compareTo("Moda")==0)
							System.out.print("");
						
						//if(p.getSedeOperativa().compareTo(sede)==0){
						dipendente = p.getCognome() + " " + p.getNome();
						idDip=p.getId_PERSONALE();
						
						for (FoglioOreMese f : p.getFoglioOreMeses()) {
							if (f.getMeseRiferimento().compareTo(mese) == 0) { //se è il mese cercato
								listaGiorni.addAll(f.getDettaglioOreGiornalieres()); //prendo tutti i giorni

								for (DettaglioOreGiornaliere giorno : listaGiorni) { 
										
									//calcolo le ore mensili rilevate dagli intervalli di IU
									totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, giorno.getTotaleOreGiorno());
									totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, giorno.getOreViaggio());
									
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
								
								//salvo un record con commessa, singolo dipendente e totali ore nel mese considerato sulla commessa in esame
								riep = new RiepilogoOreDipFatturazione(numeroCommessa, "", "", idDip, dipendente, Float.valueOf(oreTotMeseLavoro), 
										Float.valueOf(oreTotMeseViaggio), Float.valueOf(oreSommaLavoroViaggio),Float.valueOf("0.00"), true);
								
								listaR.add(riep);
																				
								oreTotMeseLavoro="0.0";
								oreTotMeseViaggio="0.0";
								oreSommaLavoroViaggio="0.0";							
							}
																					
						}//end ciclo su mesi(fatto solo una volta per il mese necessario)
						
						//if(ServerUtility.notExistDip(listaAppCalcoloOreIU, idDip))
						riep= new RiepilogoOreDipFatturazione("", "", "", idDip, dipendente, Float.valueOf(totaleOreDaIU), 
								Float.valueOf("0.00"), Float.valueOf("0.00"),Float.valueOf("0.00"), true);
						listaAppCalcoloOreIU.add(riep);
						
						totaleOreDaIU="0.00";
					//}
					}//end ciclo su lista persone nella stessa commessa
									
					listaP.clear();
					listaAssociazioni.clear();
			}
						
			DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		    formatSymbols.setDecimalSeparator('.');
		    String pattern="0.00";
		    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		   
		    List<RiepilogoOreDipFatturazione> listaAppRiep= new ArrayList<RiepilogoOreDipFatturazione>();
		    listaAppRiep.addAll(listaR);
		    boolean check=false;
		    String totOreIU= "0.00";
		    
			List<Personale> listaPers=new ArrayList<Personale>();
			listaPers=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede")
					.setParameter("sede", sede).list();	
			
		    for(Personale p:listaPers){
				//if(p.getCognome().compareTo("Guerra")==0){
				for(RiepilogoOreDipFatturazione r1:listaAppCalcoloOreIU){
					if(p.getId_PERSONALE()==r1.getIdPersonale()){
						totOreIU=d.format(r1.getOreLavoro());
						break;
					}
				}
				
				for(RiepilogoOreDipFatturazione r:listaAppRiep){
					if(p.getId_PERSONALE()==r.getIdPersonale()){
						dipendente = p.getCognome() + " " + p.getNome();
						oreTotLavoroCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, d.format(r.getOreLavoro()));
						oreTotViaggioCommessa=ServerUtility.aggiornaTotGenerale(oreTotViaggioCommessa, d.format(r.getOreViaggio()));
						oreTotCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, oreTotViaggioCommessa);
					}
				}
				if(totOreIU.compareTo(oreTotCommessa)==0)
					check=true;
				else
					check=false;
				
				if(!(totOreIU.compareTo("0.00")==0 && oreTotCommessa.compareTo("0.00")==0)){
					riep = new RiepilogoOreDipFatturazione(".TOTALE","", "", p.getId_PERSONALE(), dipendente, Float.valueOf(oreTotLavoroCommessa), 
						Float.valueOf(oreTotViaggioCommessa), Float.valueOf(oreTotCommessa), Float.valueOf(totOreIU), check);
					listaR.add(riep);
				}
				oreTotLavoroCommessa="0.0";
				oreTotViaggioCommessa="0.0";
				oreTotCommessa="0.00";
				totOreIU="0.00";
			}	
			//}	
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
	public List<RiepilogoOreDipFatturazione> getRiepilogoOreCommesseDettDipendenti(
			String mese, String sede) throws IllegalArgumentException {
		List<RiepilogoOreDipFatturazione> listaR= new ArrayList<RiepilogoOreDipFatturazione>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		//List<Commessa> listaCommesseTutti= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();
		//List<Attivita> listaAttivitaTutti= new ArrayList<Attivita>();
		
		List<AssociazionePtoA> listaAssociazioni= new ArrayList<AssociazionePtoA>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalli= new ArrayList<DettaglioIntervalliCommesse>();
		//usa una lista per salvare per ogni dipendente il suo identificativo e il numero totale di ore rilevate dagli IU
		List<RiepilogoOreDipFatturazione> listaAppCalcoloOreIU= new ArrayList<RiepilogoOreDipFatturazione>();
		RiepilogoOreDipFatturazione riep= new RiepilogoOreDipFatturazione();
		
		String numeroCommessa= new String();
		String commessa= new String();
		String estensione= new String();
		String descrizione= new String();
		String dipendente= new String();
		String oreLavoro= new String();
		String oreViaggio= new String();
		String oreTotMeseLavoro= "0.0";
		String oreTotMeseViaggio= "0.0";
		String oreSommaLavoroViaggio="0.0";
		
		String totaleOreDaIU="0.00";
		int idDip;
		
		String statoCommessa="Aperta";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx = session.beginTransaction();
			
			listaCommesse = (List<Commessa>) session.createQuery("from Commessa where statoCommessa=:stato")
				.setParameter("stato", statoCommessa).list() ;
			

			for (Commessa c : listaCommesse) {
				if (c.getAttivitas().size() > 0)
					//listaAttivita.add(c.getAttivitas().iterator().next());
					listaAttivita.addAll(c.getAttivitas());
			}

			for (Attivita a : listaAttivita) {  // in questo caso la lista  Attivita rappresenta la lista di commesse associate al PM
												// selezionato: ottengo tutte le associazioni e quindi tutti i dipendenti associati a quella commessa
				listaAssociazioni.addAll(a.getAssociazionePtoas());
				commessa = a.getCommessa().getNumeroCommessa();
				estensione = a.getCommessa().getEstensione();
				
				descrizione=a.getCommessa().getDenominazioneAttivita();
								
				numeroCommessa =(commessa + "." + estensione+" ("+descrizione+")"); //una stringa più dettagliata che descriva la commessa
				
				for (AssociazionePtoA ass : listaAssociazioni) { // per tutte le associazioni della  commessa considerata prelevo i dipendenti
					listaP.add(ass.getPersonale());
					}

					for (Personale p : listaP) {// per ogni dipendente in questa commessa selezioni i fogli ore del mese desiderato e della sede voluta
						if(p.getSedeOperativa().compareTo(sede)==0){
						dipendente = p.getCognome() + " " + p.getNome();
						idDip=p.getId_PERSONALE();
						
						for (FoglioOreMese f : p.getFoglioOreMeses()) {
							if (f.getMeseRiferimento().compareTo(mese) == 0) { //se è il mese cercato
								listaGiorni.addAll(f.getDettaglioOreGiornalieres()); //prendo tutti i giorni

								for (DettaglioOreGiornaliere giorno : listaGiorni) { 
										
									//calcolo le ore mensili rilevate dagli intervalli di IU
									totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, giorno.getTotaleOreGiorno());
									totaleOreDaIU=ServerUtility.aggiornaTotGenerale(totaleOreDaIU, giorno.getOreViaggio());
									
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
								
								//salvo un record con commessa, singolo dipendente e totali ore nel mese considerato sulla commessa in esame
								riep = new RiepilogoOreDipFatturazione(numeroCommessa,"", "", idDip, dipendente, Float.valueOf(ServerUtility.getOreCentesimi(oreTotMeseLavoro)), 
										Float.valueOf(ServerUtility.getOreCentesimi(oreTotMeseViaggio)), Float.valueOf(ServerUtility.getOreCentesimi(oreSommaLavoroViaggio))
										,Float.valueOf("0.00"), true);
								
								listaR.add(riep);
																				
								oreTotMeseLavoro="0.0";
								oreTotMeseViaggio="0.0";
								oreSommaLavoroViaggio="0.0";							
							}
							
														
						}//end ciclo su mesi(fatto solo una volta per il mese necessario)
						
						//if(ServerUtility.notExistDip(listaAppCalcoloOreIU, idDip))
						riep = new RiepilogoOreDipFatturazione("","", "", idDip, dipendente, Float.valueOf(ServerUtility.getOreCentesimi(totaleOreDaIU)), 
								Float.valueOf("0.00"), Float.valueOf("0.00"),Float.valueOf("0.00"), true);
						listaAppCalcoloOreIU.add(riep);
						
						totaleOreDaIU="0.00";
					}
					}//end ciclo su lista persone nella stessa commessa
									
					listaP.clear();
					listaAssociazioni.clear();
			}
			
			/*		
			DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
		    formatSymbols.setDecimalSeparator('.');
		    String pattern="0.00";
		    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		   
		    List<RiepilogoOreDipFatturazione> listaAppRiep= new ArrayList<RiepilogoOreDipFatturazione>();
		    listaAppRiep.addAll(listaR);
		    boolean check=false;
		    String totOreIU= new String();
		    
			List<Personale> listaPers=new ArrayList<Personale>();
			listaPers=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede")
					.setParameter("sede", sede).list();	
			
		    for(Personale p:listaPers){
				//if(p.getCognome().compareTo("Guerra")==0){
				for(RiepilogoOreDipFatturazione r1:listaAppCalcoloOreIU){
					if(p.getId_PERSONALE()==r1.getIdPersonale()){
						totOreIU=d.format(r1.getOreLavoro());
						break;
					}
				}
				
				for(RiepilogoOreDipFatturazione r:listaAppRiep){
					if(p.getId_PERSONALE()==r.getIdPersonale()){
						dipendente = p.getCognome() + " " + p.getNome();
						oreTotLavoroCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, d.format(r.getOreLavoro()));
						oreTotViaggioCommessa=ServerUtility.aggiornaTotGenerale(oreTotViaggioCommessa, d.format(r.getOreViaggio()));
						oreTotCommessa=ServerUtility.aggiornaTotGenerale(oreTotLavoroCommessa, oreTotViaggioCommessa);
					}
				}
				if(totOreIU.compareTo(oreTotCommessa)==0)
					check=true;
				else
					check=false;
				
				if(!(totOreIU.compareTo("0.00")==0 && oreTotCommessa.compareTo("0.00")==0)){
					riep = new RiepilogoOreDipFatturazione(".TOTALE", p.getId_PERSONALE(), dipendente, Float.valueOf(oreTotLavoroCommessa), 
						Float.valueOf(oreTotViaggioCommessa), Float.valueOf(oreTotCommessa), Float.valueOf(totOreIU), check);
					listaR.add(riep);
				}
				oreTotLavoroCommessa="0.0";
				oreTotViaggioCommessa="0.0";
				oreTotCommessa="0.00";
				totOreIU="0.00";
			}	
			//}	*/
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
	public List<RiepilogoOreDipFatturazione> getRiepilogoOreCommesseDettDipendentiPeriodo(
			String annoI, String meseI, String annoF, String meseF, String pm, List<CommessaModel> listaCommesseSel) {//pm in formato Cognome Nome
		List<RiepilogoOreDipFatturazione> listaR= new ArrayList<RiepilogoOreDipFatturazione>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>();
		List<Attivita> listaAttivita= new ArrayList<Attivita>();
				
		List<DettaglioIntervalliCommesse> listaDettComm= new ArrayList<DettaglioIntervalliCommesse>();
		List<AssociazionePtoA> listaAssociazioni= new ArrayList<AssociazionePtoA>();
		List<Personale> listaP=new ArrayList<Personale>();
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		List<DettaglioIntervalliCommesse> listaIntervalli= new ArrayList<DettaglioIntervalliCommesse>();
		RiepilogoOreDipFatturazione riep= new RiepilogoOreDipFatturazione();
		Commessa commessaSel= new Commessa();
		
		String numeroCommessa= new String();
		String commessa= new String();
		String estensione= new String();
		String descrizione= new String();
		String dipendente= new String();
		String oreLavoro= new String();
		String oreViaggio= new String();
		String oreTotMeseLavoro= "0.0";
		String oreTotMeseViaggio= "0.0";
		String oreSommaLavoroViaggio="0.0";
		String oreTotLavoro="0.00";
		String oreTotViaggio="0.00";
		String oreTotali="0.00";
	
		int idDip;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx = session.beginTransaction();			
			for(CommessaModel cm:listaCommesseSel){
				
				commessaSel=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", cm.get("numeroCommessa"))
						.setParameter("estensione", cm.get("estensione")).uniqueResult();
							
				if(commessaSel!=null){					
					listaAttivita.addAll(commessaSel.getAttivitas());						
					listaCommesse.add(commessaSel);
				}		
			}	
			
			
			for (Attivita a : listaAttivita) {  // in questo caso la lista  Attivita rappresenta la lista di commesse associate al PM
												// selezionato: ottengo tutte le associazioni e quindi tutti i dipendenti associati a quella commessa
				listaAssociazioni.addAll(a.getAssociazionePtoas());
				commessa = a.getCommessa().getNumeroCommessa();
				estensione = a.getCommessa().getEstensione();
				
				descrizione=a.getCommessa().getDenominazioneAttivita();
								
				numeroCommessa =(commessa + "." + estensione+" ("+descrizione+")"); //una stringa più dettagliata che descriva la commessa
			
				listaDettComm=session.createQuery("select d from DettaglioIntervalliCommesse d join d.dettaglioOreGiornaliere g " +
						" where d.numeroCommessa=:numCommessa and d.estensioneCommessa=:estensione" +
						" ").setParameter("numCommessa", commessa)
						.setParameter("estensione", estensione).list();			
														
				for(DettaglioIntervalliCommesse d:listaDettComm)					
					//prendo tutti i dip che hanno lavorato su quella commessa nei mesi considerati
					if(ServerUtility.isNotIncludedPersonale(listaP, d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale()))
						listaP.add(d.getDettaglioOreGiornaliere().getFoglioOreMese().getPersonale());
				
				for (Personale p : listaP) {// per ogni dipendente in questa commessa selezioni i fogli ore del mese desiderato e della sede voluta
						
						dipendente = p.getCognome() + " " + p.getNome();
						idDip=p.getId_PERSONALE();
						
						for (FoglioOreMese f : p.getFoglioOreMeses()) {
							String mese=f.getMeseRiferimento().substring(0, 3).toLowerCase();
							
							if(ServerUtility.isIncluded(f.getMeseRiferimento(), annoI, meseI, annoF, meseF)){
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
															
								//salvo un record con commessa, singolo dipendente e totali ore nel mese considerato sulla commessa in esame
								riep = new RiepilogoOreDipFatturazione(numeroCommessa, f.getMeseRiferimento(), ServerUtility.traduciMeseToNumber(mese), idDip, dipendente, Float.valueOf(ServerUtility.getOreCentesimi(oreTotMeseLavoro)), 
										Float.valueOf(ServerUtility.getOreCentesimi(oreTotMeseViaggio)), Float.valueOf(ServerUtility.getOreCentesimi(oreSommaLavoroViaggio))
										,Float.valueOf("0.00"), true);
								
								listaR.add(riep);
								
								oreTotLavoro=ServerUtility.aggiornaTotGenerale(oreTotLavoro, oreTotMeseLavoro);
								oreTotViaggio=ServerUtility.aggiornaTotGenerale(oreTotViaggio, oreTotMeseViaggio);									
								oreTotMeseLavoro="0.0";
								oreTotMeseViaggio="0.0";
								oreSommaLavoroViaggio="0.0";							
							}
				
						}//end ciclo su mesi(fatto solo una volta per il mese necessario)
					
						oreTotali=ServerUtility.aggiornaTotGenerale(oreTotLavoro, oreTotViaggio);
						riep = new RiepilogoOreDipFatturazione(numeroCommessa, "Totale", "13", idDip, dipendente, Float.valueOf(ServerUtility.getOreCentesimi(oreTotLavoro)), 
								Float.valueOf(ServerUtility.getOreCentesimi(oreTotViaggio)), Float.valueOf(ServerUtility.getOreCentesimi(oreTotali))
								,Float.valueOf("0.00"), true);
						
						listaR.add(riep);
						oreTotLavoro="0.00";
						oreTotViaggio="0.00";
						oreTotali="0.00";
						
					}//end ciclo su lista persone nella stessa commessa
									
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
	
	
//-------------
//---------------------------------------------------COSTI------------------------------------------------------------------------------------------------
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GestioneCostiDipendentiModel> getDatiCostiPersonale(
			int idPersonale) throws IllegalArgumentException {
		
		List<GestioneCostiDipendentiModel> lista= new ArrayList<GestioneCostiDipendentiModel>();
		List<Personale> listaP=new ArrayList<Personale>();
		GestioneCostiDipendentiModel g;
		CostoAzienda c= new CostoAzienda();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx = session.beginTransaction();
			
			listaP= (List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede and gruppoLavoro<>:gruppoLavoro").setParameter("gruppoLavoro", "Indiretti")
					.setParameter("sede", "T").list();
			
			for(Personale p:listaP){
				//se c'è ce n'è comunque solo 1
				if(p.getCostoAziendas().iterator().hasNext()){
					
					c=p.getCostoAziendas().iterator().next();
					//g= new GestioneCostiDipendentiModel(idPersonale, cognome, costoAnnuo, oreAnno, costoStruttura, costoOneri, tipoOrario, costoSwCadVari, costoSwOffice, costoHw, orePianificate, costoOrarioTot, gruppoLavoro);
					//lista.add(g);
				}
				else{
					/*g=new GestioneCostiDipendentiModel(p.getId_PERSONALE(), p.getCognome() +" "+ p.getNome(), 
							"0.00", "0.00", "0.00", "0", Float.valueOf("0.00"), Float.valueOf("0.00"));*/
					//lista.add(g);
				}				
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
		return lista;
	}

	
	//NON USATA
	@SuppressWarnings("unchecked")
	@Override
	public List<CostiHwSwModel> getDatiCostiHwSw(int id)throws IllegalArgumentException {
		
		List<CostiHwSwModel> listaCM= new ArrayList<CostiHwSwModel>();
		List<AssociazionePtohwsw> listaAss= new ArrayList<AssociazionePtohwsw>();
		List<CostiHwSw> listaCosti= new ArrayList<CostiHwSw>();
		CostiHwSwModel costo;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx = session.beginTransaction();
			
			listaAss=(List<AssociazionePtohwsw>)session.createQuery("from AssociazionePtohwsw where idPersonale=:id").setParameter("id", id).list();
			
			listaCosti=(List<CostiHwSw>)session.createQuery("from CostiHwSw").list();
			
			//Considero tutti i costi
			for(CostiHwSw c: listaCosti){
				costo=new CostiHwSwModel(c.getIdCostoHwSw(), c.getTipologia(), c.getDescrizione(), c.getCosto(), false);
				listaCM.add(costo);				
			}
			
			//con le associazioni prelevo i costi che uno ha
			for(AssociazionePtohwsw ass:listaAss){
				int idcosto=ass.getCostiHwSw().getIdCostoHwSw();
				for(CostiHwSwModel cm:listaCM){
					if( idcosto==(int) cm.get("idCosto")){
						cm.set("utilizzato", true);
						break;
					}						
				}
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
		return listaCM;		
	}
	

	//NON USATA
	@Override
	public boolean saveAssociaCostiHwSw(int idSelected,
			CostiHwSwModel costoHS) throws IllegalArgumentException {
			
		AssociazionePtohwsw ass= new AssociazionePtohwsw();
		Personale p= new Personale();
		CostiHwSw costo= new CostiHwSw();
		
		int idCosto;
		boolean utilizzato=false;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx = session.beginTransaction();
					
			idCosto=costoHS.get("idCosto");
			utilizzato=costoHS.get("utilizzato");
			
			ass=(AssociazionePtohwsw)session.createQuery("from AssociazionePtohwsw where idPersonale=:id and idCostiHwSw=:idCosto").setParameter("id", idSelected)
						.setParameter("idCosto", idCosto).uniqueResult();				
				
			if(ass==null && utilizzato)	{		
				
				ass=new AssociazionePtohwsw();
				p=(Personale)session.get(Personale.class, idSelected);
				
				costo=(CostiHwSw)session.get(CostiHwSw.class, idCosto);
				
				ass.setPersonale(p);
				ass.setCostiHwSw(costo);
				
				p.getAssociazionePtohwsws().add(ass);
				costo.getAssociazionePtohwsws().add(ass);
			
				session.save(p);
				session.save(costo);
				
				tx.commit();
				
			}
			else
				if(ass!=null && !utilizzato){
					
					ass.setPersonale(null);
					ass.setCostiHwSw(null);
					session.delete(ass);
					
					tx.commit();
					
				}		
			
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}				
		return true;
	}


	//NON USATA
	@Override
	public void editDatiCostiAzienda(GestioneCostiDipendentiModel g)
			throws IllegalArgumentException {
		
		CostoAzienda c= new CostoAzienda();
		Personale p= new Personale();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		int idP= g.get("idPersonale");
		
		try {
			
			tx = session.beginTransaction();
			
			c=(CostoAzienda)session.createQuery("from CostoAzienda where idPersonale=:idPersonale").setParameter("idPersonale", idP).uniqueResult();
			
			if(c!=null){
				c.setCostiOneri((String)g.get("costoOneri"));
				c.setCostoAnnuo((String)g.get("costoAnnuo"));
				c.setCostoStruttura((String)g.get("costoStruttura"));
				
				
				tx.commit();
			}
			else{
				
				p=(Personale)session.get(Personale.class, idP);
				c= new CostoAzienda();
				c.setCostiOneri((String)g.get("costoOneri"));
				c.setCostoAnnuo((String)g.get("costoAnnuo"));
				c.setCostoStruttura((String)g.get("costoStruttura"));
				
				
				c.setPersonale(p);
				
				p.getCostoAziendas().add(c);				
						
				session.save(p);
				
				tx.commit();				
			}
		
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}				
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoCostiDipendentiModel> getRiepilogoDatiCostiPersonale(
			String sede) throws IllegalArgumentException {

		List<RiepilogoCostiDipendentiModel> listaC= new ArrayList<RiepilogoCostiDipendentiModel>();
		List<Personale> listaP= new ArrayList<Personale>();
		CostoAzienda costo= new CostoAzienda();
			
		RiepilogoCostiDipendentiModel r;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		sede="T";
	
		int idDip=0;
		String nome="";	
		Float costoOrarioTotale=(float)0.00;
		
				
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		try {
			
			tx = session.beginTransaction();
			listaP=(List<Personale>)session.createQuery("from Personale where sedeOperativa=:sede").setParameter("sede", sede).list();
			
			for(Personale p:listaP){
				if(p.getCostoAziendas().iterator().hasNext()){
					costo=p.getCostoAziendas().iterator().next();
					
					costoOrarioTotale=Float.valueOf(costo.getCostoOrario())+Float.valueOf(costo.getCostoStruttura())+Float.valueOf(costo.getCostiOneri())
							+Float.valueOf(costo.getCostoSwCadVari())+Float.valueOf(costo.getCostoSwOffice())+Float.valueOf(costo.getCostoHw());
					
					nome=p.getCognome() + " "+p.getNome();
					r= new RiepilogoCostiDipendentiModel(idDip, nome, costo.getCostoAnnuo(), p.getTipologiaOrario(), costo.getOreAnno(), 
							costo.getCostoOrario(),  costo.getCostoStruttura(), costo.getCostiOneri(), costo.getCostoSwCadVari(), costo.getCostoSwOffice(),
							costo.getCostoHw(), d.format(costoOrarioTotale), p.getGruppoLavoro());
					
					listaC.add(r);
					costoOrarioTotale=(float)0.0;
				}
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
		return listaC;
	}

	

	@SuppressWarnings("unchecked")
	@Override
	//Lista che riempie la combobox a seconda se è un PM o meno
	public List<CostingModel> getListaDatiCosting(String username) throws IllegalArgumentException{
		List<CostingModel> listaCM= new ArrayList<CostingModel>();
		List<Costing> listaC= new ArrayList<Costing>();
		List<Commessa> listaCommesse= new ArrayList<Commessa>(); 
		Personale p=new Personale();
		CostingModel costingM;
		Cliente cliente;
		String commessa;
		String ragSociale;
		
		String pm= new String();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		try {
			tx=session.beginTransaction();
		
			if(username.compareTo("")!=0){
			
				p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
				//TODO mettere in matricola pm l'username!!!!!!!!!!!!!!!!!!
				pm=p.getCognome()+" "+p.getNome();
				
				listaCommesse=(List<Commessa>)session.createQuery("from Commessa where matricolaPM=:pm").setParameter("pm", pm).list();
				
				for(Commessa c: listaCommesse)
					if(!c.getCostings().isEmpty())
						listaC.add(c.getCostings().iterator().next());
			
				for(Costing co: listaC){
					cliente=(Cliente) session.get(Cliente.class, co.getCliente());
					ragSociale=cliente.getRagioneSociale();
					commessa=co.getCommessa().getNumeroCommessa()+"."+co.getCommessa().getEstensione();
					costingM=new CostingModel(co.getIdCosting(), ragSociale, co.getArea(), commessa, co.getDescrizioneProgetto(), co.getNumerorevisione()
							,co.getStatoCosting());
					listaCM.add(costingM);
				}
			
				tx.commit();
			}else{
				listaCommesse=(List<Commessa>)session.createQuery("from Commessa").list();
				
				for(Commessa c: listaCommesse)
					if(!c.getCostings().isEmpty())
						listaC.add(c.getCostings().iterator().next());
			
				for(Costing co: listaC){
					cliente=(Cliente) session.get(Cliente.class, co.getCliente());
					ragSociale=cliente.getRagioneSociale();
					commessa=co.getCommessa().getNumeroCommessa()+"."+co.getCommessa().getEstensione();
					costingM=new CostingModel(co.getIdCosting(), ragSociale, co.getArea(), commessa, co.getDescrizioneProgetto(), co.getNumerorevisione()
							,co.getStatoCosting());
					listaCM.add(costingM);
				}	
			}			
			
		}catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}		
		return listaCM;		
	}

	
	@Override
	public List<CostingModel> getDatiCosting(int id)
			throws IllegalArgumentException {
		
		Costing c= new Costing();
		CostingModel cM;
		Cliente cliente;
		Commessa comm= new Commessa();
		
		String ragSociale= new String();
			
		List<Costing> listaCosting= new ArrayList<Costing>();
		List<CostingModel> listaCostingM= new ArrayList<CostingModel>();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
	
		try {		
			tx=session.beginTransaction();
			
			c=(Costing)session.get(Costing.class, id);
			//dal costing risalgo alla commessa e prelevo tutte le revisioni
			if(c!=null){
				comm=c.getCommessa();		
				listaCosting.addAll(comm.getCostings());
							
				for(Costing cos:listaCosting){		
					cliente=(Cliente) session.get(Cliente.class, cos.getCliente());
					ragSociale=cliente.getRagioneSociale();
					
					cM=new CostingModel(cos.getIdCosting(), ragSociale, cos.getArea(), cos.getCommessa().getNumeroCommessa(), 
							cos.getDescrizioneProgetto(), cos.getNumerorevisione(), cos.getStatoCosting());
					listaCostingM.add(cM);
				}
			}
			tx.commit();
			
		}catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}	
		return listaCostingM;
	}
	

	@Override
	public boolean insertDataCosting(String commessa, String area, int idCliente,
			String descrizione, String usernamePM) {
		
		Costing cs= new Costing();
		Commessa c= new Commessa();
		
		String numeroCommessa= commessa.substring(0,commessa.indexOf("."));
		String estensione= commessa.substring(commessa.indexOf(".")+1,commessa.length());		
		
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		Boolean costingInserito=false;
		
		try {		
			tx=session.beginTransaction();
			
			/*if(usernamePM.compareTo("")!=0){
				p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", usernamePM);
				nomePM=p.getCognome()+" "+p.getNome();			
			}*/
			c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", numeroCommessa)
					 .setParameter("estensione", estensione).uniqueResult();
			
			if(c==null){
				insertDataCommessa(numeroCommessa, estensione, "c", usernamePM, "Costing", "0.00", "0.00", 
						"0.00", "0.00", "0.00", descrizione, "");
				
				tx.commit();
				//session.close();				
				costingInserito=insertDataCosting(commessa, area, idCliente, descrizione, usernamePM);
			}
				
			if(!costingInserito){
				cs.setCliente(idCliente);
				cs.setArea(area);
				cs.setDescrizioneProgetto(descrizione);
				cs.setNumerorevisione("1");
				cs.setCommessa(c);
				cs.setStatoCosting("S"); //S:sospeso, A:approvato, R:respinto, C:chiuso
			
				c.getCostings().add(cs);					
				session.save(c);
				
				tx.commit();
				session.close();
			}			
			return true;
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return false;
		}finally{
			//session.close();
		}
	}
	

	@Override
	public List<CostingRisorsaModel> getRiepilogoDatiCostingRisorse(int idCosting)
			throws IllegalArgumentException {
		
		List<CostingRisorsaModel> listaCostR= new ArrayList<CostingRisorsaModel>();
		List<CostingRisorsa> listaCost= new ArrayList<CostingRisorsa>();
		CostoAzienda costoA= new CostoAzienda();
		Costing cost;
		CostingRisorsaModel costRMod;
		Cliente cliente;
		DettaglioTrasferta dettTrasferta= new DettaglioTrasferta();
		
		String area="";
		String ragioneSociale="";
		String commessa="";
		int idRisorsa=0;
		String risorsa="";
		String progetto="";
		Float oreViaggio=(float)0.0;
		Float giorniViaggio=(float)0;
		Float diaria=(float)0.0;
		Float costoDiaria=(float)0.0;
		Float costoTotOre=(float)0.0;
		Float costoTrasferta=(float)0.0;
		Float costoTotale=(float)0.0;
		Float costoOrario=(float)0.0;
		Float costoStruttura=(float)0.0;
		Float efficienza=(float)1.0;
		Float oreDaFatturare=(float)0.0;
		Float oreTrasferta=(float)0.0;
		String tariffa="0.00";
		Float fatturatoTotale=(float)0.0;
		Float ebit=(float)0.0;//solo su totale commessa
		Float ebitPerc=(float)0.0;
		Date dataInizioAttivita= null;
		Date dataFineAttivita= null;
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
				
		try {		
			tx=session.beginTransaction();
			
			cost=(Costing)session.get(Costing.class, idCosting);
			
			if(cost!=null){
				cliente=(Cliente)session.get(Cliente.class, cost.getCliente());
				ragioneSociale=cliente.getRagioneSociale();
			
				if(cost.getCostingRisorsas().iterator().hasNext())//prelevo i dati di costing di eventuali risorse inserite
					listaCost.addAll(cost.getCostingRisorsas());
			
				area=cost.getArea();		
				progetto=cost.getDescrizioneProgetto();
				commessa=cost.getCommessa().getNumeroCommessa();
				
			for(CostingRisorsa c:listaCost){	
				
				costoA=c.getPersonale().getCostoAziendas().iterator().next();
				
				idRisorsa=c.getPersonale().getId_PERSONALE();
				risorsa=c.getPersonale().getCognome()+" "+c.getPersonale().getNome();

				efficienza=Float.valueOf(c.getEfficienza());
				tariffa=c.getTariffa();
				costoOrario=Float.valueOf(c.getCostoOrario());
				costoStruttura=Float.valueOf(c.getCostoStruttura());
				
				//dati di viaggio
				if(c.getDettaglioTrasfertas().iterator().hasNext()){
					dettTrasferta=c.getDettaglioTrasfertas().iterator().next();
					costoTrasferta=ServerUtility.calcoloTotaleCostotrasferta(dettTrasferta);
					
					oreViaggio=Float.valueOf(dettTrasferta.getOreViaggio())*Float.valueOf(dettTrasferta.getNumViaggi());
					giorniViaggio= Float.valueOf(dettTrasferta.getNumGiorni());
					diaria=Float.valueOf(dettTrasferta.getDiariaGiorno());
					costoDiaria=diaria*giorniViaggio;
					
					oreTrasferta=costoTrasferta/Float.valueOf(c.getTariffa());
				}	
				
				oreDaFatturare=(float) ((Float.valueOf(c.getOreLavoro())+(oreViaggio*0.85))*efficienza);							
				
				costoTotOre=costoOrario+costoStruttura+Float.valueOf(c.getCostoOneri())+Float.valueOf(c.getCostoHwSw());
				costoTotale=costoTotOre*Float.valueOf(c.getOreLavoro());
				costoTotale=costoTotale+costoTrasferta;//costo trasferta comprende già costo diaria		
				fatturatoTotale=(oreDaFatturare+oreTrasferta)*Float.valueOf(tariffa);
				
				ebit=fatturatoTotale-costoTotale;
				
				ebitPerc=ebit/costoTotale;
							
				costRMod=new CostingRisorsaModel(c.getIdCostingRisorsa(), area, ragioneSociale, progetto, commessa, idRisorsa, risorsa,
						Float.valueOf(c.getCostoOrarioRisorsa()), Float.valueOf(c.getCostoStruttura()), Float.valueOf(c.getOreLavoro()), oreViaggio, giorniViaggio, /*diaria,*/ costoDiaria,
						costoTotOre, costoTrasferta, costoTotale, String.valueOf(efficienza), oreDaFatturare, oreTrasferta, tariffa, fatturatoTotale, ebit, ebitPerc,
						dataInizioAttivita, dataFineAttivita);
				
				listaCostR.add(costRMod);
				
				dettTrasferta=new DettaglioTrasferta();
				costoTrasferta=(float)0.0;
				oreViaggio=(float)0.0;
				giorniViaggio=(float)0.0;
				costoDiaria=(float)0.0;
				oreTrasferta=(float)0.0;
				
			}
			/*
				if(listaCost.size()>0)
					listaCostR.add(ServerUtility.elaboraRecordTotaliCostingCommessa(listaCostR));
			*/
			}
			tx.commit();
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;
		}finally{
			session.close();
		}
		return listaCostR;		
	}

	
	@Override
	public CostingRisorsaModel getDatiCostiDipendenteSelezionato(int idPersonale, int idCosting)throws IllegalArgumentException {
		CostingRisorsaModel cM;
		Costing costing;
		Cliente cl;
		Personale p= new Personale();
		CostoAzienda costoAzienda= new CostoAzienda();	
		String area="";	
		Float ebit=(float) 0.0;
		Float ebitPerc=(float) 0.0;
		Float costoOrario=(float) 0.0;
		Float costoStruttura=(float) 0.0;
		Float costoTotOre=(float)0.0;
		Float costoHwSw=(float)0.0;
		Date dataInizioAttivita= null;
		Date dataFineAttivita= null;
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;

		try {		
			tx=session.beginTransaction();
			
			p=(Personale)session.get(Personale.class, idPersonale);
			costing=(Costing)session.get(Costing.class, idCosting);
			cl=(Cliente)session.get(Cliente.class, costing.getCliente());
			area=costing.getArea();
			
			if(p.getCostoAziendas().iterator().hasNext()){				
				costoAzienda= p.getCostoAziendas().iterator().next();		
				costoOrario= Float.valueOf(costoAzienda.getCostoOrario());
				costoStruttura=Float.valueOf(costoAzienda.getCostoStruttura());
				costoHwSw=Float.valueOf(costoAzienda.getCostoHw())+Float.valueOf(costoAzienda.getCostoSwCadVari())+Float.valueOf(costoAzienda.getCostoSwOffice());
				
				costoTotOre=costoOrario+costoStruttura+Float.valueOf(costoAzienda.getCostoOneri())+costoHwSw;				
				
				cM= new CostingRisorsaModel(0 , area, cl.getRagioneSociale(), costing.getDescrizioneProgetto(), costing.getCommessa().getNumeroCommessa(), p.getId_PERSONALE(), 
						p.getCognome()+" "+p.getNome(), costoOrario, costoStruttura, (float)0.0, (float)0.0, (float)0, /*(float) 0,*/(float)0.0, costoTotOre,  (float)0.0, (float)0.0, "0.00", 
						(float)0.0, (float)0.0, "0.00", (float)0.0, ebit, ebitPerc, dataInizioAttivita, dataFineAttivita);				
				
				tx.commit();
				return cM;
			}else{
				tx.commit();
				return null;
			}
			
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
	public boolean confermaCostingDipendente(int idSelected,
			CostingRisorsaModel c) throws IllegalArgumentException {
		
		Costing costing;
		CostoAzienda costoA= new CostoAzienda();
		Commessa commessa= new Commessa();
		Personale p= new Personale();
		CostingRisorsa cR= new CostingRisorsa();
		List<CostingRisorsa> listaCR= new ArrayList<CostingRisorsa>();
		
		Float costoHwSw=(float)0.0;
		Float costoOneri=(float)0.0;
		int idCosting=0;
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
									
			tx=session.beginTransaction();
			p=(Personale)session.get(Personale.class, (int)c.get("idRisorsa"));
			costing=(Costing)session.get(Costing.class, idSelected);
			
			if(p.getCostoAziendas().iterator().hasNext()){
				costoA=p.getCostoAziendas().iterator().next();					
				costoHwSw=Float.valueOf(costoA.getCostoHw())+Float.valueOf(costoA.getCostoSwOffice())+Float.valueOf(costoA.getCostoSwCadVari());
				costoOneri=Float.valueOf(costoA.getCostoOneri());
			}
			
			if(costing.getCostingRisorsas().iterator().hasNext()){
				listaCR.addAll(costing.getCostingRisorsas());
				
				idCosting=ServerUtility.costingPresente(listaCR, p.getId_PERSONALE());
				if(idCosting!=0){
					
					cR=(CostingRisorsa)session.get(CostingRisorsa.class, idCosting);					
					cR.setCostoOrarioRisorsa(d.format((Float) c.get("costoOrario")));
					cR.setCostoStruttura(d.format((Float) c.get("costoStruttura")));
					cR.setCostoOneri(d.format(costoOneri));
					cR.setCostoHwSw(d.format(costoHwSw));
					cR.setEfficienza((String) c.get("efficienza"));	
					String a=d.format(((Float) c.get("oreLavoro")));
					cR.setOreLavoro(a);
					cR.setTariffa((String) c.get("tariffa"));
					
					tx.commit();
					
				}else{//creo il nuovo e l'associazione del dipendente alla commessa.
					
					commessa=costing.getCommessa();						
					createAssociazionePtoA(commessa.getNumeroCommessa(), commessa.getEstensione(), p.getUsername());
				
					cR.setCostoOrarioRisorsa(d.format((Float) c.get("costoOrario")));	
					cR.setCostoStruttura(d.format((Float) c.get("costoStruttura")));
					cR.setCostoOneri(d.format(costoOneri));
					cR.setCostoHwSw(d.format(costoHwSw));
					cR.setEfficienza((String) c.get("efficienza"));				
					String a=((String) c.get("oreLavoro"));
					cR.setOreLavoro(a);
					cR.setTariffa((String) c.get("tariffa"));				
					cR.setCosting(costing);
					cR.setPersonale(p);
					costing.getCostingRisorsas().add(cR);
					p.getCostingRisorsas().add(cR);				
					session.save(p);
					session.save(costing);
					
					tx.commit();
				}
				
			}else{
				commessa=costing.getCommessa();
				createAttivita(commessa.getNumeroCommessa(), commessa.getEstensione());
				createAssociazionePtoA(commessa.getNumeroCommessa(), commessa.getEstensione(), p.getUsername());
				cR.setCostoOrarioRisorsa(d.format((Float) c.get("costoOrario")));	
				cR.setCostoStruttura(d.format((Float) c.get("costoStruttura")));
				cR.setCostoOneri(d.format(costoOneri));
				cR.setCostoHwSw(d.format(costoHwSw));
				cR.setEfficienza((String) c.get("efficienza"));
				String a=((String) c.get("oreLavoro"));
				cR.setOreLavoro(a);
				cR.setTariffa((String) c.get("tariffa"));
				cR.setCosting(costing);
				cR.setPersonale(p);				
				costing.getCostingRisorsas().add(cR);
				p.getCostingRisorsas().add(cR);
				
				session.save(p);
				session.save(costing);
				
				tx.commit();
			}			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return false;
		}finally{
			session.close();
		}	
		return true;
	}

	
	@Override
	public boolean deleteRisorsaCosting(int idRisorsa)
			throws IllegalArgumentException {
		
		Commessa commessa= new Commessa();
		List<AssociazionePtoA> listaAss= new ArrayList<AssociazionePtoA>();
		CostingRisorsa c= new CostingRisorsa();
		int idPersonale;
		int idAssociazionePa=0;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				
			tx=session.beginTransaction();
					
			c=(CostingRisorsa)session.get(CostingRisorsa.class, idRisorsa);			
			commessa=c.getCosting().getCommessa();
			idPersonale=c.getPersonale().getId_PERSONALE();
			
			listaAss.addAll(commessa.getAttivitas().iterator().next().getAssociazionePtoas());
			
			//elimino l'associazione creata sulla commessa
			for(AssociazionePtoA ass:listaAss)
				if(ass.getPersonale().getId_PERSONALE()==idPersonale)				
					idAssociazionePa=ass.getIdAssociazionePa();					
			
			tx.commit();
			session.close();
			
			deleteAssociazioneCommessaDipendenti(idAssociazionePa, "", "", "");
			deleteDatiCostingRisorsa(idRisorsa);
			//elimino ildettaglio del costing del dipendente
			//session.delete(c);			
			//tx.commit();
			return true;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return false;
		}finally{
			//session.close();
		}	
	}
	

	private void deleteDatiCostingRisorsa(int idRisorsa) {
		
		CostingRisorsa c= new CostingRisorsa();				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				
			tx=session.beginTransaction();
					
			c=(CostingRisorsa)session.get(CostingRisorsa.class, idRisorsa);			
		
			session.delete(c);
			
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
	public boolean saveNewVersionCosting(int idSelected)
			throws IllegalArgumentException {
		Costing c= new Costing();
		Costing newC= new Costing();
		List<CostingRisorsa> listaCR= new ArrayList<CostingRisorsa>();
		Commessa commessa= new Commessa();
		int revisione;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				
			tx=session.beginTransaction();			
			
			c=(Costing)session.get(Costing.class, idSelected);			
			commessa=c.getCommessa();
						
			revisione=commessa.getCostings().size()+1;
			newC.setArea(c.getArea());
			newC.setCliente(c.getCliente());
			newC.setDescrizioneProgetto(c.getDescrizioneProgetto());
			newC.setStatoCosting(c.getStatoCosting());
			newC.setNumerorevisione(String.valueOf(revisione));			
			newC.setCommessa(commessa);
			
			if(c.getCostingRisorsas().iterator().hasNext())
				listaCR.addAll(c.getCostingRisorsas());
												
			commessa.getCostings().add(newC);
			session.save(commessa);
			
			tx.commit();
			session.close();
			
			for(CostingRisorsa cr:listaCR){
				createCostingRisorsaPerNuovaVersione(commessa.getCodCommessa(), String.valueOf(revisione), cr);
			}
			
			return true;	
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return false;
		}finally{
			//session.close();
		}
	}

	private void createCostingRisorsaPerNuovaVersione(int codCommessa,
			String revisione, CostingRisorsa cr) {
		
		Commessa c= new Commessa();
		Personale p= new Personale();
		List<Costing> listaC= new ArrayList<Costing>();
		Costing costing= new Costing();
		CostingRisorsa costingR= new CostingRisorsa();
		int idPersonale;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
		
			tx=session.beginTransaction();	
			c=(Commessa)session.get(Commessa.class, codCommessa);
			listaC.addAll(c.getCostings());
			idPersonale=cr.getPersonale().getId_PERSONALE();
			
			p=(Personale)session.get(Personale.class, idPersonale);
			
			for(Costing cs:listaC)
				if(cs.getNumerorevisione().compareTo(revisione)==0)
					costing=cs;
						
			costingR.setCostoOrarioRisorsa(cr.getCostoOrarioRisorsa());
			costingR.setEfficienza(cr.getEfficienza());
			costingR.setOreLavoro(cr.getOreLavoro());
			costingR.setTariffa(cr.getTariffa());
			
			costingR.setCosting(costing);
			costingR.setPersonale(p);
			
			costing.getCostingRisorsas().add(costingR);
			p.getCostingRisorsas().add(costingR);
			
			session.save(p);
			session.save(costing);
						
			tx.commit();
			//session.close();
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			
		}finally{
			session.close();
		}	
	}
	

	@Override
	public boolean editStatoCosting(int idSelected, String operazione)
			throws IllegalArgumentException {
		
		Costing costing= new Costing();
		Commessa commessa= new Commessa();
		List<Costing> listaCosting= new ArrayList<Costing>();
		List<AssociazionePtoA> listaAss= new ArrayList<AssociazionePtoA>();
		Attivita att= new Attivita();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
				tx=session.beginTransaction();
				costing=(Costing)session.get(Costing.class, idSelected);
				commessa=costing.getCommessa();
				
				if(operazione.compareTo("C")==0){//conferma
					//cambio lo stato ad aperta, al dipendente associato comparirà la nuova commessa
					commessa.setStatoCommessa("Aperta");
					costing.setStatoCosting("A");
					tx.commit();
					session.close();
					return true;
				}else 
					if(operazione.compareTo("S")==0){//scarta
						//cambio lo stato del costing a respinto
						costing.setStatoCosting("R");//respinto
						commessa.setStatoCommessa("Costing");
						tx.commit();
						session.close();
						return true;
					}
					else 
						if(operazione.compareTo("D")==0){
							//libero e chiudo la commessa ed elimino tutte le associazioni con i dipendenti
							listaCosting.addAll(commessa.getCostings());
							
							for(Costing c:listaCosting)
								c.setStatoCosting("C");
								 
							commessa.setStatoCommessa("Conclusa");
							
							if(commessa.getAttivitas().iterator().hasNext()){
								att=commessa.getAttivitas().iterator().next();
								listaAss.addAll(att.getAssociazionePtoas());
							
								tx.commit();
								session.close();
														
								for(AssociazionePtoA ass:listaAss){
									deleteAssociazioneCommessaDipendenti(ass.getIdAssociazionePa(), "", "", "");
								}
							}else{
								tx.commit();
								session.close();
							}								
							
							return true;
						}else
							return true;			
		
		} catch (Exception e) {
			if (tx!=null)
				tx.rollback();	
			e.printStackTrace();
			return false;		
		}finally{
						
		}				
	}
	
	
	@Override
	public boolean saveDatiTrasfertaUtente(int idRisorsa,
			int idCostingSelected,String numeroViaggi, String oreViaggio, String kmStradali,
			String carburante, String autostrada, boolean usoAutoPropria,
			String costotreno, String costoAereo, String costiVari,
			String numeroGiorni, String costoDiaria, String costoAlbergo, String costoPranzo,
			String costoCena, String noleggioAuto, String trasportoLocale) {

		CostingRisorsa costing= new CostingRisorsa();
		DettaglioTrasferta dettT= new DettaglioTrasferta();
		
		String usoAuto="F";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		if(usoAutoPropria)
			usoAuto="T";
		
		try {
			tx=session.beginTransaction();
		
			costing=(CostingRisorsa)session.createQuery("from CostingRisorsa where idCostingRisorsa=:idCosting").setParameter("idCosting", idCostingSelected).uniqueResult();
		
			if(costing.getDettaglioTrasfertas().iterator().hasNext()){
				//edit
				dettT=costing.getDettaglioTrasfertas().iterator().next();
						
				dettT.setUsoVetturaPropria(usoAuto);
				dettT.setCostiVari(costiVari);
				dettT.setCostoAereo(costoAereo);
				dettT.setCostoAlbergo(costoAlbergo);
				dettT.setCostoAutostrada(autostrada);
				dettT.setCostoCarburante(carburante);
				dettT.setCostoCena(costoCena);
				dettT.setCostoNoleggioAuto(noleggioAuto);
				dettT.setCostoPranzo(costoPranzo);
				dettT.setCostoTrasportiLocali(trasportoLocale);
				dettT.setCostoTreno(costotreno);
				dettT.setDiariaGiorno(costoDiaria);
				dettT.setKmStradali(Float.valueOf(kmStradali));
				dettT.setNumGiorni(numeroGiorni);
				dettT.setNumViaggi(numeroViaggi);
				dettT.setOreViaggio(oreViaggio);
				
				tx.commit();
				
			}else{
				//new
				dettT.setUsoVetturaPropria(usoAuto);
				dettT.setCostiVari(costiVari);
				dettT.setCostoAereo(costoAereo);
				dettT.setCostoAlbergo(costoAlbergo);
				dettT.setCostoAutostrada(autostrada);
				dettT.setCostoCarburante(carburante);
				dettT.setCostoCena(costoCena);
				dettT.setCostoNoleggioAuto(noleggioAuto);
				dettT.setCostoPranzo(costoPranzo);
				dettT.setCostoTrasportiLocali(trasportoLocale);
				dettT.setCostoTreno(costotreno);
				dettT.setDiariaGiorno(costoDiaria);
				dettT.setKmStradali(Float.valueOf(kmStradali));
				dettT.setNumGiorni(numeroGiorni);
				dettT.setNumViaggi(numeroViaggi);
				dettT.setOreViaggio(oreViaggio);

				dettT.setCostingRisorsa(costing);
				costing.getDettaglioTrasfertas().add(dettT);
				
				session.save(costing);
				tx.commit();
			}
						
		} catch (Exception e) {
			
			e.printStackTrace();
			if (tx!=null)
				tx.rollback();				
			return false;		
		}finally{
			session.close();		
		}		
		return true;
	}
	
	
	@Override
	public DettaglioTrasfertaModel loadDataTrasferta(int idCostingSelected)
			throws IllegalArgumentException {
		
		Boolean esito=true;
		Boolean usoVetturaB=false;
		String errore= new String();
		
		DettaglioTrasfertaModel dettTM=null;
		DettaglioTrasferta dettT= new DettaglioTrasferta();
		CostingRisorsa cr= new CostingRisorsa();
				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			cr=(CostingRisorsa)session.createQuery("from CostingRisorsa where idCostingRisorsa=:idCosting").setParameter("idCosting", idCostingSelected).uniqueResult();
			
			if(cr.getDettaglioTrasfertas().iterator().hasNext()){
				dettT=cr.getDettaglioTrasfertas().iterator().next();
				
				String usoVettura=dettT.getUsoVetturaPropria();
				
				if(usoVettura.compareTo("T")==0)
					usoVetturaB=true;					
				
				dettTM=new DettaglioTrasfertaModel( dettT.getID_DETTAGLIO_TRASFERTA(), Float.valueOf(dettT.getNumGiorni()), Float.valueOf(dettT.getNumViaggi()), 
						Float.valueOf(dettT.getOreViaggio()),dettT.getKmStradali(), Float.valueOf(dettT.getCostoCarburante()), Float.valueOf(dettT.getDiariaGiorno()), 
						Float.valueOf(dettT.getCostoAutostrada()), usoVetturaB, Float.valueOf(dettT.getCostoTreno()), Float.valueOf(dettT.getCostoAereo()), 
						Float.valueOf(dettT.getCostoAlbergo()), Float.valueOf(dettT.getCostoPranzo()), Float.valueOf(dettT.getCostoCena()), 
						Float.valueOf(dettT.getCostoNoleggioAuto()), Float.valueOf(dettT.getCostoTrasportiLocali()), Float.valueOf(dettT.getCostiVari()));				
			}		
			
			tx.commit();
			
		} catch (Exception e) {
			esito=false;
			e.printStackTrace();
			errore=e.getMessage();
			if (tx!=null)
				tx.rollback();				
			return null;		
		}finally{
			session.close();
			if(!esito){
	        	ServerLogFunction.logErrorMessage("getRiepilogoAnagraficaHardware", new Date(), "", "Error", errore);
	        	return null;
			}
			else				
				ServerLogFunction.logOkMessage("getRiepilogoAnagraficaHardware", new Date(), "", "Success");
		}		
		
		return dettTM;
	}
	
	
	//----------------------------------STRUMENTI AMMINISTRATIVI

	@SuppressWarnings("unchecked")
	@Override
	public List<AnagraficaHardwareModel> getRiepilogoAnagraficaHardware()
			throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		
		List<AnagraficaHardwareModel> listaAM= new ArrayList<AnagraficaHardwareModel>();
		List<AnagraficaHardware> listaA=new ArrayList<AnagraficaHardware>();
		AnagraficaHardwareModel aModel;
		String username="";
		String gruppoLavoro="";
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			listaA=(List<AnagraficaHardware>)session.createQuery("from AnagraficaHardware").list();
			
			for(AnagraficaHardware an:listaA){
				if(an.getAssociazionePtohws().iterator().hasNext()){
					username=an.getAssociazionePtohws().iterator().next().getPersonale().getUsername();
					gruppoLavoro=an.getAssociazionePtohws().iterator().next().getPersonale().getUsername();
				}
							
				aModel= new AnagraficaHardwareModel(an.getIdHardware(), username, gruppoLavoro, an.getAssistenza(), an.getCodiceModello(), an.getCpu(), an.getFornitoreAssistenza(),
						an.getHardware(), an.getHd(), an.getIp(), an.getIpFiat(), an.getModello(), an.getNodo(), an.getNote(), an.getRam(), an.getScadenzaControllo()
						, an.getSede(), an.getSerialId(), an.getSerialNumber(), an.getSistemaOperativo(), an.getStato(), an.getSvga(), an.getTipologia(), an.getUtilizzo());
				
				listaAM.add(aModel);			
			}
			
			tx.commit();
			
		} catch (Exception e) {
			esito=false;
			e.printStackTrace();
			errore=e.getMessage();
			if (tx!=null)
				tx.rollback();				
			return null;		
		}finally{
			session.close();
			if(!esito){
	        	ServerLogFunction.logErrorMessage("getRiepilogoAnagraficaHardware", new Date(), "", "Error", errore);
	        	return null;
			}
			else				
				ServerLogFunction.logOkMessage("getRiepilogoAnagraficaHardware", new Date(), "", "Success");
		}		
		return listaAM;
	}	

	
	@Override
	public boolean editDataAnagraficaHardware(Integer idHardware, String username, String gruppoLavoro, String assistenza, String codiceModello,  String cpu, String fornitoreAssistenza, String hardware,
			String hd, String ip, String ipFiat, String modello, String nodo, String note, String ram, Date scadenzaControllo,  String sede,
			String serialId, String serialNumber, String sistemaOperativo, String stato, String svga, String tipologia, String utilizzo) throws IllegalArgumentException {
		
		Boolean esito=true;
		String errore= new String();
		
		AnagraficaHardware an;
		Personale p = new Personale();
		AssociazionePtoHw ass= new AssociazionePtoHw();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			//discrimino idHardware che se 0 o "" ne inserisco uno nuovo altrimenti edito
			if(idHardware!=null){
				an=(AnagraficaHardware)session.createQuery("from AnagraficaHardware where idHardware=:idHardware").setParameter("idHardware", idHardware).uniqueResult();				
				
				if(username.compareTo("")!=0)
					p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
				
				an.setAssistenza(assistenza);
				an.setCodiceModello(codiceModello);
				an.setCpu(cpu);
				an.setFornitoreAssistenza(fornitoreAssistenza);
				an.setHardware(hardware);
				an.setHd(hd);
				an.setIp(ip);
				an.setIpFiat(ipFiat);
				an.setModello(modello);
				an.setNodo(nodo);
				an.setNote(note);
				an.setRam(ram);
				an.setScadenzaControllo(scadenzaControllo);
				an.setSede(sede);
				an.setSerialId(serialId);
				an.setSerialNumber(serialNumber);
				an.setSistemaOperativo(sistemaOperativo);
				an.setStato(stato);
				an.setSvga(svga);
				an.setTipologia(tipologia);
				an.setUtilizzo(utilizzo);
								
				if(an.getAssociazionePtohws().iterator().hasNext()){//c'è un'associazione con un utente
					Personale p2= new Personale();
					ass=an.getAssociazionePtohws().iterator().next();	
					p2=ass.getPersonale();
					
					if(p2.getUsername().compareTo(username)!=0){//se il nuovo username è diverso da quello già assegnato
						ass.setPersonale(p);	
						p.getAssociazionePtoHw().add(ass);
						session.save(p);
					}
				}else //creao associazione
				{
					if(username.compareTo("")!=0){
						ass.setPersonale(p);
						ass.setAnagraficaHardware(an);
						p.getAssociazionePtoHw().add(ass);
						an.getAssociazionePtohws().add(ass);
						session.save(an);
						session.save(p);
					}									
				}	
				
				tx.commit();
				session.close();
							
			}else{
				//creo il record per l'anagrafica e l'associazione se c'è un username selezionato
				
				an= new AnagraficaHardware();
				an.setAssistenza(assistenza);
				an.setCodiceModello(codiceModello);
				an.setCpu(cpu);
				an.setFornitoreAssistenza(fornitoreAssistenza);
				an.setHardware(hardware);
				an.setHd(hd);
				an.setIp(ip);
				an.setIpFiat(ipFiat);
				an.setModello(modello);
				an.setNodo(nodo);
				an.setNote(note);
				an.setRam(ram);
				an.setScadenzaControllo(scadenzaControllo);
				an.setSede(sede);
				an.setSerialId(serialId);
				an.setSerialNumber(serialNumber);
				an.setSistemaOperativo(sistemaOperativo);
				an.setStato(stato);
				an.setSvga(svga);
				an.setTipologia(tipologia);
				an.setUtilizzo(utilizzo);				
				
				session.save(an);
				
				tx.commit();
				session.close();
				
				if(username.compareTo("")!=0) //l'associazione la creo solo se c'è un utente selezionato							
					creaAssociazionePtoHW(username, nodo);					
			}
		} catch (Exception e) {
			esito=false;
			e.printStackTrace();
			errore=e.getMessage();
			if (tx!=null)
				tx.rollback();				
			return false;		
		}finally{			
			if(!esito){
				session.close();
	        	ServerLogFunction.logErrorMessage("editDataAnagraficaHardware", new Date(), username, "Error", errore);
	        	return false;
			}
			else				
				ServerLogFunction.logOkMessage("editDataAnagraficaHardware", new Date(), username, "Success");
		}	
		return true;
	}
	

	private void creaAssociazionePtoHW(String username, String nodo) {
		Boolean esito=true;
		String errore= new String();
		
		AnagraficaHardware an= new AnagraficaHardware();
		Personale p = new Personale();
		AssociazionePtoHw ass= new AssociazionePtoHw();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
			
			an=(AnagraficaHardware)session.createQuery("from AnagraficaHardware where nodo=:nodo").setParameter("nodo", nodo).uniqueResult();				
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username).uniqueResult();
			
			ass.setAnagraficaHardware(an);
			ass.setPersonale(p);
			
			p.getAssociazionePtoHw().add(ass);
			an.getAssociazionePtohws().add(ass);
			
			session.save(p);
			session.save(an);
			
			tx.commit();
			
		} catch (Exception e) {
			esito=false;
			e.printStackTrace();
			errore=e.getMessage();
			if (tx!=null)
				tx.rollback();				
					
		}finally{
			session.close();
			if(!esito)
	        	ServerLogFunction.logErrorMessage("creaAssociazionePtoHW", new Date(), username+" "+nodo, "Error", errore);
			else				
				ServerLogFunction.logOkMessage("creaAssociazionePtoHW", new Date(), username, "Success");
		}	
	}

	@Override
	public boolean insertRichiestaIt(String username, Date dataR, String ora,
			String pc) {
		
		
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoRichiesteModel> getRiepilogoRichiesteItUtente(
			String username) {
		
		Boolean esito=true;
		String errore= new String();
		
		List<RiepilogoRichiesteModel> listaRM= new ArrayList<RiepilogoRichiesteModel>();
		List<RichiesteIt> listaR= new ArrayList<RichiesteIt>();
		Personale p= new Personale();
		RiepilogoRichiesteModel rM;
		
		int idUtente;
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {
			
			tx=session.beginTransaction();
		
			p=(Personale)session.createQuery("from Personale where username=:username ").setParameter("username", username).uniqueResult();
			idUtente=p.getId_PERSONALE();
			
			listaR=(List<RichiesteIt>)session.createQuery("from RichiesteIt where idPersonale=:id").setParameter("id", idUtente).list();
									
			tx.commit();
			
			for(RichiesteIt r:listaR){
				long timeRich=r.getDataRichiesta().getTime();
				long timeEvasR= r. getDataEvasioneRichiesta().getTime();
				Time tRich= new Time(timeRich);
				Time tEv= new Time(timeEvasR);				
				
				rM= new RiepilogoRichiesteModel(r.getIdRichiesta(), r.getAnagraficaHardware().getIdHardware(), idUtente, username, 
						r.getAnagraficaHardware().getNodo(), r.getDataRichiesta(), tRich.toString(), r.getDataEvasioneRichiesta(), tEv.toString(), r.getStato(), r.getGuasto());
				
				listaRM.add(rM);				
			}
			
		} catch (Exception e) {
			esito=false;
			e.printStackTrace();
			errore=e.getMessage();
			if (tx!=null)
				tx.rollback();				
					
		}finally{
			session.close();
			if(!esito){
	        	ServerLogFunction.logErrorMessage("getRiepilogoRichiesteItUtente", new Date(), username, "Error", errore);
	        	return null;
			}
	        else				
				ServerLogFunction.logOkMessage("getRiepilogoRichiesteItUtente", new Date(), username, "Success");
		}	
		return listaRM;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiepilogoMensileOrdiniModel> getRiepilogoOrdini(String statoOrdini)
			throws IllegalArgumentException {
		
		Boolean esito=true;
		List<RiepilogoMensileOrdiniModel> listaRM= new ArrayList<RiepilogoMensileOrdiniModel>();
		List<Ordine> listaO=new ArrayList<Ordine>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		RiepilogoMensileOrdiniModel riep;
		
		String commessa= "";
		Float importoRes= (float)0.0;
		String oreResidue="0.00";
		
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols();
	    formatSymbols.setDecimalSeparator('.');
	    String pattern="0.00";
	    DecimalFormat d= new DecimalFormat(pattern,formatSymbols);
		
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);
		String formattedDate ;
	    
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {		
			
			tx=session.beginTransaction();
			
			if(statoOrdini.compareTo("Tutti")==0)
				listaO=session.createQuery("from Ordine").list();
			else
				listaO=session.createQuery("from Ordine where statoOrdine=:stato").setParameter("stato", statoOrdini.substring(0, 1)).list();			
			
			for(Ordine o:listaO){
				
				if(o.getCommessa()!=null){
					commessa=o.getCommessa().getNumeroCommessa()+"."+o.getCommessa().getEstensione();
				
					importoRes=Float.valueOf(o.getImportoResiduo());
					oreResidue=o.getOreResidueBudget();
					if(o.getDataInizio()!=null)
						formattedDate = formatter.format(o.getDataInizio());
					else
						formattedDate="#";
				
					listaFF.addAll(o.getCommessa().getFoglioFatturaziones());
						
					for(FoglioFatturazione ff:listaFF)
						if(ff.getMeseCorrente().compareTo("Mag2013")!=0){//escludo anche qui il mese di mag2013
							importoRes=importoRes- Float.valueOf(ff.getImportoRealeFatturato());
							oreResidue=ServerUtility.getDifference(oreResidue, ff.getOreFatturare());
						}
				
					for(AttivitaOrdine a:o.getAttivitaOrdines()){
						riep= new RiepilogoMensileOrdiniModel(o.getRda().getCliente().getRagioneSociale(), o.getCommessa().getMatricolaPM(), 
								o.getCodiceOrdine(), formattedDate, commessa, o.getRda().getCodiceRDA(), o.getDescrizioneAttivita(), o.getRda().getOffertas().iterator().next().getCodiceOfferta(), 
								a.getTariffaAttivita(), Float.valueOf(o.getImporto()),Float.valueOf(ServerUtility.getOreCentesimi(o.getOreBudget())), Float.valueOf(importoRes), 
								Float.valueOf(ServerUtility.getOreCentesimi(oreResidue)), o.getStatoOrdine());
						listaRM.add(riep);
					}
				
					listaFF.clear();
					importoRes=(float)0;
					oreResidue="0.00";
				}
			}
			
			tx.commit();
			
		}catch (Exception e) {
			esito=false;
			e.printStackTrace();
			if (tx!=null)
				tx.rollback();				
					
		}finally{
			session.close();
			if(!esito)	        	
	        	return null;
		}
		return listaRM;
	}

	
	@Override
	public List<RiepilogoMensileOrdiniModel> getDettaglioMensileOrdine(
			String numeroOrdine) throws IllegalArgumentException {
		
		Boolean esito=true;
		List<RiepilogoMensileOrdiniModel> listaRM= new ArrayList<RiepilogoMensileOrdiniModel>();
		List<FoglioFatturazione> listaFF= new ArrayList<FoglioFatturazione>();
		RiepilogoMensileOrdiniModel riep= new RiepilogoMensileOrdiniModel();
		Ordine o= new Ordine();
		
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {		
			
			tx=session.beginTransaction();
			
			o=(Ordine) session.createQuery("from Ordine where codiceOrdine=:no").setParameter("no", numeroOrdine).uniqueResult();
			
			listaFF.addAll(o.getCommessa().getFoglioFatturaziones());
			for(AttivitaOrdine a:o.getAttivitaOrdines()){
				for(FoglioFatturazione ff:listaFF){
					if(ff.getAttivitaOrdine()==a.getIdAttivitaOrdine()){
					
						riep= new RiepilogoMensileOrdiniModel(ff.getMeseCorrente(), o.getCommessa().getMatricolaPM(),	"", "", "", "", "", "", 
							"", Float.valueOf(ff.getImportoRealeFatturato()), Float.valueOf(ServerUtility.getOreCentesimi(ff.getOreFatturare())), 
							(float)0, (float)0,"");
						listaRM.add(riep);					
					}
				}							
			}			
					
			tx.commit();
			
		}catch (Exception e) {
			esito=false;
			e.printStackTrace();
			if (tx!=null)
				tx.rollback();				
					
		}finally{
			session.close();
			if(!esito)	        	
	        	return null;
		}
		return listaRM;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<RiferimentiRtvModel> getDatiReferenti()	throws IllegalArgumentException {
		List<RiferimentiRtvModel> listaRM= new ArrayList<RiferimentiRtvModel>();
		List<RiferimentiRtv> listaR= new ArrayList<RiferimentiRtv>();
		RiferimentiRtvModel rtvM;		
		
		Boolean esito=true;				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {		
			
			tx=session.beginTransaction();
			
			listaR=(List<RiferimentiRtv>)session.createQuery("from RiferimentiRtv").list();
			
			for(RiferimentiRtv r:listaR){
				rtvM=new RiferimentiRtvModel(r.getIdRiferimentiRtv(), 0, r.getCliente().getRagioneSociale(), 
						r.getSezione(), r.getReparto(), r.getIndirizzo(), r.getRiferimento(), r.getTelefono(), r.getEmail());
				listaRM.add(rtvM);
			}
					
			tx.commit();
			
		}catch (Exception e) {
			esito=false;
			e.printStackTrace();
			if (tx!=null)
				tx.rollback();				
					
		}finally{
			session.close();
			if(!esito)	        	
	        	return null;
		}
		return listaRM;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<RtvModel> getDatiRtv(String numeroO) {
		List<RtvModel> listaRM= new ArrayList<RtvModel>();
		List<Rtv> listaR= new ArrayList<Rtv>();
		RtvModel rtvM;	
		Ordine o;
		
		Boolean esito=true;				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx= null;
		
		try {		
			
			tx=session.beginTransaction();
			
			o=(Ordine)session. createQuery("from Ordine where codiceOrdine=:nOrdine").setParameter("nOrdine", numeroO).uniqueResult();
			int idOrdine=o.getNumeroOrdine();
			
			listaR=(List<Rtv>)session.createQuery("from Rtv where idOrdine=:idOrdine").setParameter("idOrdine", idOrdine).list();
			
			for(Rtv r:listaR){
				rtvM=new RtvModel(r.getIdRtv(), r.getOrdine().getNumeroOrdine(), numeroO, r.getNumeroRtv(), 
						r.getCodiceFornitore(), r.getOrdine().getCommessa().getMatricolaPM(), r.getDataOrdine(),
						r.getDataEmissione(), r.getImporto(), "0.00", "0.00", "", r.getAttivita(), "", r.getCdcCliente(),
						r.getCommessaCliente(), r.getEnte(), null, null);
				listaRM.add(rtvM);
			}
					
			tx.commit();
			
		}catch (Exception e) {
			esito=false;
			e.printStackTrace();
			if (tx!=null)
				tx.rollback();				
					
		}finally{
			session.close();
			if(!esito)	        	
	        	return null;
		}
		return listaRM;
	}
}
