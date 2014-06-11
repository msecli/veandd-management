package gestione.pack.server;

import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.OffertaModel;
import gestione.pack.client.model.OrdineModel;
import gestione.pack.client.model.PersonaleAssociatoModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RdaModel;
import gestione.pack.shared.AssociazionePtoA;
import gestione.pack.shared.Attivita;
import gestione.pack.shared.Cliente;
import gestione.pack.shared.Commessa;
import gestione.pack.shared.DettaglioIntervalliCommesse;
import gestione.pack.shared.DettaglioOreGiornaliere;
import gestione.pack.shared.FoglioOreMese;
import gestione.pack.shared.Offerta;
import gestione.pack.shared.Ordine;
import gestione.pack.shared.Personale;
import gestione.pack.shared.Rda;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import org.hibernate.Session;
import org.hibernate.Transaction;


public class ConverterUtil {

	
//----------------Personale--------------------------
	
	@SuppressWarnings("unchecked")
	public static List<Personale> getPersonale()throws IllegalArgumentException{
		
		Boolean esito=true;
		String errore= new String();
		
		List<Personale> listaP= new ArrayList<>();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
				tx=	session.beginTransaction();
				listaP = (List<Personale>)session.createQuery("from Personale where statoRapporto<>:statoRapporto")
						.setParameter("statoRapporto", "Cessato").list();
				tx.commit();
				
		    } catch (Exception e) {
		    	esito=false;
		    	e.printStackTrace();
	    		errore=e.getMessage();
		    	if (tx!=null)		    		
		    		tx.rollback();	    		
		    	
		    } finally {
		    	if(!esito){
		        	ServerLogFunction.logErrorMessage("ConverterUtil.getPersonale", new Date(), "", "Error", errore);
		        	return null;
		    	}
		    	else
		    		ServerLogFunction.logOkMessage("ConverterUtil.getPersonale", new Date(), "", "Success");
		    }					
		return listaP;
	}	
	
	
	static PersonaleModel personaleToModelConverter(Personale p) {
		
		PersonaleModel pm= new PersonaleModel(p.getId_PERSONALE(),p.getNome(), p.getCognome(), p.getUsername(), p.getPassword(), p.getNumeroBadge(),p.getRuolo(), 
				p.getTipologiaOrario(), p.getTipologiaLavoratore(), p.getGruppoLavoro(), p.getCostoOrario(), p.getCostoStruttura(), p.getSede(), p.getSedeOperativa(), p.getOreDirette(), 
				p.getOreIndirette(), p.getOrePermessi(), p.getOreFerie(), p.getOreExFest(), p.getOreRecupero(), p.getStatoRapporto(), false
				,p.getDataInizioAbilitazioneStrao(), "");
		
		return pm;	
	}	
	

//----------------Clienti---------------------------------------
	
	static ClienteModel clienteToModelConverter(Cliente c) {
		
		ClienteModel cliente=new ClienteModel(c.getCodCliente(),c.getRagioneSociale(),c.getCodFiscale(),c.getPartitaIVA(),c.getCodRaggr(), 
				c.getCodFornitore(), c.getTestoIvaNonImponibile(), c.getCitta(),c.getProvincia(),
				c.getStato(),c.getIndirizzo(),c.getCap(),c.getTelefono(),c.getFax(),c.getEmail(),c.getBanca(),c.getValuta(),c.getCodABI(),c.getCodCAB(),
				c.getCodCIN(),c.getCondizioniPagamento(),c.getRdas());
			
		return cliente;
	
	}	
	
	
	@SuppressWarnings("unchecked")
	public static List<Cliente> getClienti() throws IllegalArgumentException {
		Boolean esito=true;
		String errore= new String();
		List<Cliente> listaC= new ArrayList<>();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		try {
			tx=	session.beginTransaction();
			listaC = (List<Cliente>)session.createQuery("from Cliente").list();
			tx.commit();
			
		    } catch (Exception e) {
		    	esito=false;
		    	e.printStackTrace();
	    		errore=e.getMessage();
		    	if (tx!=null)		    		
		    		tx.rollback();	 
		    } finally {
		    	if(!esito){
		        	ServerLogFunction.logErrorMessage("ConverterUtil.getClienti", new Date(), "", "Error", errore);
		        	return null;
		    	}
		    	else
		    		ServerLogFunction.logOkMessage("ConverterUtil.getClienti", new Date(), "", "Success");
		    }			
		
		return listaC;
	}


//--------------------RDA----------------------------------
	
	@SuppressWarnings("unchecked")
	public static Set<Rda> getRda() {
		Boolean esito=true;
		String errore= new String();
			
		Set<Rda> rdas=new HashSet<>();
		List<Rda> listaApp=new ArrayList<>();						
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx=null;
				
		try {
			tx=	session.beginTransaction();								
			listaApp = (List<Rda>)session.createQuery("from Rda").list();			
			rdas.addAll(listaApp);
			tx.commit();
				
		    } catch (Exception e) {
		    	esito=false;
		    	e.printStackTrace();
	    		errore=e.getMessage();
		    	if (tx!=null)		    		
		    		tx.rollback();
		    } finally {
		    	if(!esito){
		        	ServerLogFunction.logErrorMessage("ConverterUtil.getRda", new Date(), "", "Error", errore);
		        	return null;
		    	}
		    	else
		    		ServerLogFunction.logOkMessage("ConverterUtil.getRda", new Date(), "", "Success");		
			    }
		return rdas;
	}
		
	
	public static RdaModel rdaToModelConverter(Rda r) {
		
		Boolean esito=true;
		String errore= new String();
		
		Cliente c=new Cliente();
		String ragioneSociale=new String();
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
				
		try {
			  tx=session.beginTransaction();
			  c=(Cliente)session.createQuery("from Cliente where cod_cliente=:idcliente").setParameter("idcliente", r.getCliente().getCodCliente()).uniqueResult();
			  ragioneSociale=c.getRagioneSociale();
			  tx.commit();
		     
		    } catch (Exception e) {
		    	esito=false;
		    	errore=e.getMessage();
		    	e.printStackTrace();
		    	if (tx!=null)
		    		tx.rollback();			    	
		    }finally{
		    	if(!esito){
		    		ServerLogFunction.logErrorMessage("ConverterUtil.rdaToModelConverter", new Date(), "", "Error", errore);
		        	return null;
		    	}
		    	else
		    		ServerLogFunction.logOkMessage("ConverterUtil.rdaToModelConverter", new Date(), "", "Success");	
		    }
			
		RdaModel rda= new RdaModel(r.getNumeroRda(), r.getCliente().getCodCliente(), r.getCodiceRDA(),ragioneSociale);
		return rda;
	}	
	
	
	
//---------------Offerte-------------------------------------------
		
	public static OffertaModel offerteToModelConverter(Offerta o) {
		
		Date data=new Date();
		SimpleDateFormat formatter;
		String dataOfferta=new String();
		String codiceRda= new String();
		
		try {
			if(o.getDataRedazione()==null){
				
				dataOfferta="00-00-0000";	
			} else{
				
				formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
				data=o.getDataRedazione();
				dataOfferta=formatter.format(data);
			}
			
			if(o.getRda()==null){
				codiceRda="0";	
			}else codiceRda= o.getRda().getCodiceRDA()+"("+o.getRda().getCliente().getRagioneSociale()+")";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		OffertaModel offerta= new OffertaModel(o.getNumeroOfferta(), o.getCodiceOfferta(), codiceRda, dataOfferta, o.getDescrizioneTecnica(), o.getImporto());
		
		return offerta;
		
	}


	@SuppressWarnings("unchecked")
	public static Set<Offerta> getOfferte() {
		
		Set<Offerta> setOfferte=new HashSet<>();
		List<Offerta> lista=new ArrayList<>();				
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx=null;
			
		try {
			tx=	session.beginTransaction();		
			
			lista=(List<Offerta>)session.createQuery("from Offerta").list();
			setOfferte.addAll(lista);
			
			tx.commit();
			
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		    } 		
		
		return setOfferte;
	}

	
	
//---------------------ORDINI-------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public static Set<Ordine> getOrdini() {
		
		Set<Ordine> setOrdini=new HashSet<>();
		List<Ordine> lista=new ArrayList<>();				
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx=null;
			
		try {
			tx=	session.beginTransaction();		
			
			lista=(List<Ordine>)session.createQuery("from Ordine").list();
			setOrdini.addAll(lista);
			
			tx.commit();
			return setOrdini;
			
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		      e.printStackTrace();
		      return null;
		    } 		
		
	}


	public static OrdineModel ordiniToModelConverter(Ordine o) {
		
		Date dataInizio=new Date();
		Date dataFine= new Date();
				
		SimpleDateFormat formatter;
		String dataInizioO=new String();
		String dataFineO=new String();
		String commessa= new String();
		
		String codiceRda= new String();
		
		//Utile convertire, nel caso in cui non fosse stata inserita una data, il valore di null nel formato 00-00-0000
		try {
			
			if(o.getDataInizio()==null){
				
				dataInizioO="00-00-0000";	
			} else{
				formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
				dataInizio=o.getDataInizio();
				dataInizioO=formatter.format(dataInizio);
			}
			
			if(o.getDataFine()==null){
				dataFineO="00-00-0000";	
				
			} else{
				formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
				dataFine=o.getDataFine();
				dataFineO=formatter.format(dataFine);		
			}
						
			if(o.getRda()==null){
				codiceRda="#";	
			}else codiceRda= o.getRda().getCodiceRDA()+"("+o.getRda().getCliente().getRagioneSociale()+")";
				
			commessa= getCommessaByOrdine(o);								
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		OrdineModel ordine= new OrdineModel(o.getNumeroOrdine(), o.getCodiceOrdine(), codiceRda, commessa,
				dataInizioO, dataFineO, o.getDescrizioneAttivita(), o.getTariffaOraria(), o.getNumRisorse(), o.getOreBudget(), "");
		
		return ordine;
	}

	
	private static String getCommessaByOrdine(Ordine o) {
		
		Session session= MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx= null;
		
		String commessa= new String();
		
		try {
			  tx=	session.beginTransaction();
			  
			  if(o.getCommessa()==null) commessa="#";
				  else commessa=o.getCommessa().getNumeroCommessa()+"."+o.getCommessa().getEstensione();
			  
			  tx.commit();
		     
			  return commessa;
			  
		    } catch (Exception e) {
		    	if (tx!=null)
		    		tx.rollback();		    	
		    	e.printStackTrace();
		    	return "#";
		    }	
		
	}


//----------------------------------COMMESSE-------------------------------------------------
	

	public static CommessaModel commesseToModelConverter(Commessa c, Ordine o) {
		String cliente = new String();
		String numRisorse= new String();
		String dataElaborazione= new String();
		String dataChiusura= new String();
		String numeroOrdine= new String();
		String numeroRdo= new String();
		String numeroOfferta= new String();
		
		Rda r= new Rda();
		Offerta of= new Offerta();
		
		Date data= new Date();
		Date dataC=new Date();
		
		SimpleDateFormat formatter;
		numRisorse="0"; //sarà utile al momento dell'associazione con i dipendenti
		
		try {
			
			//formattazione data
			if(c.getDataElaborazione()==null){			
				dataElaborazione="00-00-0000";	
			} else{
				formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
				data=c.getDataElaborazione();
				dataElaborazione=formatter.format(data);
			}
			
			//formattazione data
			if(c.getDataChiusura()==null){		
				dataChiusura="00-00-0000";	
			} else{
				formatter=new SimpleDateFormat("dd-MMM-yyyy",Locale.ITALIAN);
				dataC=c.getDataChiusura();
				dataChiusura=formatter.format(dataC);
			}
				

			if (o != null) {

				Session session = MyHibernateUtil.getSessionFactory().openSession();
				Transaction tx = null;

				try {
					tx = session.beginTransaction();
					cliente = o.getRda().getCliente().getRagioneSociale();
					numeroOrdine= o.getCodiceOrdine();
					
					r=o.getRda();
					
					if(r.getCodiceRDA()!=null)
						numeroRdo=r.getCodiceRDA();
					else
						numeroRdo="#";
						
					of=(Offerta)session.createQuery("from Offerta where numero_rda=:idRda").setParameter("idRda", r.getNumeroRda()).uniqueResult();
					
					if(of!=null)
						numeroOfferta=of.getCodiceOfferta();
					else
						numeroOfferta="#";
					
					tx.commit();

				} catch (Exception e) {
					if (tx != null)
						tx.rollback();
					e.printStackTrace();
					cliente = "#";
					numeroOrdine="#";
					numeroOfferta="#";
					numeroRdo="#";
				}finally{
					session.close();
				}

			} else{
				cliente = c.getRagioneSocialeCliente();
				numeroOrdine="#";
				numeroRdo="#";
				numeroOfferta="#";
			}
								
			CommessaModel cm= new CommessaModel( c.getCodCommessa(),  c.getNumeroCommessa(), numeroRdo, numeroOfferta, numeroOrdine,  c.getEstensione(),  c.getTipoCommessa(), cliente ,  c.getMatricolaPM(),  c.getStatoCommessa(),
					String.valueOf(c.getOreLavoro()), String.valueOf(c.getResiduoOreLavoro()),String.valueOf(c.getTariffaSal()), c.getSalAttuale(), c.getPclAttuale(), dataElaborazione, dataChiusura, c.getDenominazioneAttivita(), c.getNote(), numRisorse);
			return cm;			
		
			} catch (Exception e) {
				e.printStackTrace();
				return null;
		}		
	}


	@SuppressWarnings("unchecked")
	public static Set<Commessa> getCommesse(String statoSelected) {
		
		Boolean esito=true;
		String errore= new String();
		
		Set<Commessa> setCommesse=new HashSet<>();
		List<Commessa> lista=new ArrayList<>();				
		Session session= MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx=null;
			
		try {
			tx=	session.beginTransaction();		
			
			if(statoSelected.compareTo("Aperta")==0)
				lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa=:stato").
				setParameter("stato", statoSelected).list();
			if(statoSelected.compareTo("Conclusa")==0)
				lista=(List<Commessa>)session.createQuery("from Commessa where statoCommessa=:stato").
				setParameter("stato", statoSelected).list();
			if(statoSelected.compareTo("Tutte")==0)
				lista=(List<Commessa>)session.createQuery("from Commessa").list();
			
			setCommesse.addAll(lista);
			tx.commit();				
			
		    } catch (Exception e) {
		    	esito=false;
		    	e.printStackTrace();
	    		errore=e.getMessage();
		    	if (tx!=null)		    		
		    		tx.rollback();
		    } finally {
		    	session.close();		    	
		    	if(!esito){
		        	ServerLogFunction.logErrorMessage("ConverterUtil.getCommesse", new Date(), "", "Error", errore);
		        	return null;
		    	}
		    	else
		    		ServerLogFunction.logOkMessage("ConverterUtil.getCommesse", new Date(), "", "Success");
		    }
		return setCommesse;
	}

	
//------------------------------------------------FOGLIO ORE Associazioni Personale Commesse-------------------------------------------
	

	public static List<PersonaleAssociatoModel> associazionePtoAToModelConverter(AssociazionePtoA ass) {
		Boolean esito=true;
		String errore= new String();
		List<PersonaleAssociatoModel> pModelList= new ArrayList<PersonaleAssociatoModel>();
		PersonaleAssociatoModel pModel= new PersonaleAssociatoModel();
		Attivita a= new Attivita();
		String commessa = new String();
		String nome = new String();
		String cognome = new String();
		String attivita= new String();
		int idAssociazione=ass.getIdAssociazionePa();
		
		Session session = MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			
			a=ass.getAttivita();
			nome=ass.getPersonale().getNome();
			cognome=ass.getPersonale().getCognome();
			commessa=a.getCommessa().getNumeroCommessa()+"."+a.getCommessa().getEstensione()+"("+a.getCommessa().getDenominazioneAttivita()+")";
			attivita=ass.getAttivita().getCommessa().getDenominazioneAttivita();
			
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
	        	ServerLogFunction.logErrorMessage("ConverterUtil.associazionePtoAToModelConverter", new Date(), "", "Error", errore);
	        	return null;
	    	}
	    	else
	    		ServerLogFunction.logOkMessage("ConverterUtil.associazionePtoAToModelConverter", new Date(), "", "Success");
		}
		
		pModel = new PersonaleAssociatoModel(idAssociazione, commessa, nome, cognome, attivita);
		pModelList.add(pModel);
		
		return pModelList;
	}


	@SuppressWarnings("unchecked")
	public static Set<AssociazionePtoA> getAssociazioniPtoA() {

		List<AssociazionePtoA> ptoaList= new ArrayList<AssociazionePtoA>();
		Set<AssociazionePtoA> set= new HashSet<>();
		Session session = MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			
			tx = session.beginTransaction();
			
			ptoaList=(List<AssociazionePtoA>)session.createQuery("from AssociazionePtoA").list();
			set.addAll(ptoaList);	
			
			tx.commit();
			return set;
			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx!=null)
	    		tx.rollback();				
			return null;
		} finally{		
			session.close();
		}
	
	}
		
	
	//-------------------------------FOGLIO ORE UTILITY-----------------------------------------------------	
	
	
	/*
	 * Se una commessa viene chiusa, l'associazione resta ma non ne permettiamo la visualizzazione nella lista del foglio ore.
	 */
	public static IntervalliCommesseModel intervalliCommesseToModelConverter(AssociazionePtoA ass, Date giornoRiferimento) {
		
		//Personale p = new Personale();
		Attivita a= new Attivita();
		FoglioOreMese f= new FoglioOreMese();
		Commessa c= new Commessa();
		//DettaglioOreGiornaliere dettGiorno= new DettaglioOreGiornaliere();
		DettaglioIntervalliCommesse intervalloCommessa= new DettaglioIntervalliCommesse();
		IntervalliCommesseModel intervalloModel;
		List<DettaglioOreGiornaliere> listaGiorni= new ArrayList<DettaglioOreGiornaliere>();
		
		int idPersonale;

		String data=new String();
		String mese=new String();
		String commessa= new String();
		String numeroCommessa= new String();
		String estensione= new String();
		String anno=new String();
		
		DateFormat formatter1 = new SimpleDateFormat("yyyy") ; 
		anno=formatter1.format(giornoRiferimento);
		formatter1 = new SimpleDateFormat("MMM",Locale.ITALIAN);
		mese=formatter1.format(giornoRiferimento);
	    mese=(mese.substring(0,1).toUpperCase()+mese.substring(1,3));
	    formatter1 = new SimpleDateFormat("dd");
		
		data=(mese+anno);
			
		Session session = MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			idPersonale=ass.getPersonale().getId_PERSONALE();
			a=ass.getAttivita();
			numeroCommessa=a.getCommessa().getNumeroCommessa();
			estensione=a.getCommessa().getEstensione();
			commessa=numeroCommessa+"."+estensione;
			
			//prelevo la commessa relativa all'associazione
			c=(Commessa)session.createQuery("from Commessa where numeroCommessa=:numeroCommessa and estensione=:estensione").setParameter("numeroCommessa", numeroCommessa)
					.setParameter("estensione", estensione).uniqueResult();
			
			//se la commessa non è Conclusa
			if(c.getStatoCommessa().compareTo("Conclusa")!=0 && c.getStatoCommessa().compareTo("Costing")!=0){ 
				
				intervalloModel= new IntervalliCommesseModel(commessa,  "0.00",  "0.00", "0.00", "0.00", "0.00", c.getDenominazioneAttivita(),"");
				
				f=(FoglioOreMese)session.createQuery("from FoglioOreMese where meseRiferimento=:mese and id_personale=:id")
						.setParameter("mese", data).setParameter("id", idPersonale).uniqueResult();	
				
				if(f!=null)//se il foglio ore è stato in parte compilato, seleziono i giorni compilati
					if(!f.getDettaglioOreGiornalieres().isEmpty())
						listaGiorni.addAll(f.getDettaglioOreGiornalieres());			
					else{
						tx.commit();
						return intervalloModel;//nessun giorno ancora creato
					}
				else {
					tx.commit();
					return intervalloModel;//nessun mese ancora creato
				}
				
				for(DettaglioOreGiornaliere d:listaGiorni){
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.ITALIAN);
					String formattedDate = formatter.format(giornoRiferimento);
					
					if(formattedDate.equals(d.getGiornoRiferimento().toString()))//se è presente il giorno di riferimento 
					{
						//c'è il giorno e cerco gli intervalli commesse
						intervalloCommessa=(DettaglioIntervalliCommesse)session.createQuery("from DettaglioIntervalliCommesse " +
								"where id_dettaglio_ore=:id and numeroCommessa=:numeroCommessa and estensioneCommessa=:estensione")
								.setParameter("id", d.getIdDettaglioOreGiornaliere()).setParameter("numeroCommessa", numeroCommessa)
								.setParameter("estensione", estensione).uniqueResult();	
						
						if(intervalloCommessa==null){ //non è presente alcun intervallo commesse
							tx.commit();
							return  intervalloModel; 
						}
							
						else{
							//sto trasformando gli int in stringhe per un calcolo ottimale degli orari
							String oreLavoroTotali= new String();
							String oreViaggioTotali= new String();
							
							oreLavoroTotali=calcolaOreLavoroTotaliMese(data, idPersonale, numeroCommessa, estensione);
							oreViaggioTotali=calcolaOreViaggioTotaliMese(data, idPersonale, numeroCommessa, estensione);
							
							intervalloModel=new IntervalliCommesseModel(commessa, intervalloCommessa.getOreLavorate(), 
									intervalloCommessa.getOreViaggio(), intervalloCommessa.getOreStraordinario(), String.valueOf(oreLavoroTotali), String.valueOf(oreViaggioTotali), 
									c.getDenominazioneAttivita(), "");
							break;
						}
					}else{
						//non è un giorno con intervalli commesse registrati ma comunque devo produrre il totale mensile
						String oreLavoroTotali= new String();
						String oreViaggioTotali= new String();
						
						oreLavoroTotali=calcolaOreLavoroTotaliMese(data, idPersonale, numeroCommessa, estensione);
						oreViaggioTotali=calcolaOreViaggioTotaliMese(data, idPersonale, numeroCommessa, estensione);
						
						intervalloModel=new IntervalliCommesseModel(commessa, "0.00", "0.00", "0.00", oreLavoroTotali, oreViaggioTotali,
								c.getDenominazioneAttivita(), "");
						//break;
					}
			}
			
			}else {
				return null; //la commessa è chiusa e non devo aggiungere un elemento alla lista
			}
					
			tx.commit();
			return intervalloModel;
		
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();	
			return null;
		}finally{
			session.close();
		}	
	}
	
	
	private static String calcolaOreViaggioTotaliMese(String mese, int idPersonale, String numeroCommessa, String estensione) {
		FoglioOreMese f= new FoglioOreMese();
		
		List<DettaglioOreGiornaliere>listaGiorni= new ArrayList<DettaglioOreGiornaliere>();	
		List<DettaglioIntervalliCommesse> listaCommesse;
		String totale= new String();
		totale="0.00";
		
		Session session = MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			//p=(Personale)session.createQuery("from Personale where id_personale=:id").setParameter("id", idPersonale).uniqueResult();
			f=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese").setParameter("id", idPersonale)
					.setParameter("mese", mese).uniqueResult();
			listaGiorni.addAll(f.getDettaglioOreGiornalieres());
			
			for(DettaglioOreGiornaliere g:listaGiorni){
				listaCommesse=new ArrayList<DettaglioIntervalliCommesse>();
				listaCommesse.addAll(g.getDettaglioIntervalliCommesses());
				
				for(DettaglioIntervalliCommesse c:listaCommesse){
					if((c.getNumeroCommessa().compareTo(numeroCommessa)==0)&&(c.getEstensioneCommessa().compareTo(estensione)==0))
						totale=ServerUtility.aggiornaTotGenerale(totale ,c.getOreViaggio());
				}				
			}	
			tx.commit();
			return totale;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;
		}
	}

/*
	private static String calcolaOreViaggio(String totaleOre, String oreViaggio) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a,b;
		
		totA=totaleOre.substring(0, totaleOre.indexOf("."));
		totB=totaleOre.substring(totaleOre.indexOf(".")+1, totaleOre.length());
		
		giornoA= oreViaggio.substring(0, oreViaggio.indexOf("."));
		giornoB= oreViaggio.substring(oreViaggio.indexOf(".")+1, oreViaggio.length());
						
		a=Integer.parseInt(totA)+Integer.parseInt(giornoA);
		b=Integer.parseInt(totB)+Integer.parseInt(giornoB);
		
		if(b==0)
			totale = (String.valueOf(a)+".00");
		else
			if(b>59)
				if(b%60 <10)
					totale = (String.valueOf(a+1)+".0"+String.valueOf(b%60));
				else
					totale = (String.valueOf(a+1)+"."+String.valueOf(b%60));
			else
				totale = (String.valueOf(a)+"."+String.valueOf(b));
		
		return totale;
	}
*/

	private static String calcolaOreLavoroTotaliMese(String mese, int idPersonale, String numeroCommessa, String estensione) {
		FoglioOreMese f= new FoglioOreMese();
		
		List<DettaglioOreGiornaliere>listaGiorni= new ArrayList<DettaglioOreGiornaliere>();	
		List<DettaglioIntervalliCommesse> listaCommesse;
		String totale= new String();
		totale="0.00";
		Session session = MyHibernateUtil.getSessionFactory().getCurrentSession();
		Transaction tx = null;
		
		try {
			tx = session.beginTransaction();
			
			//p=(Personale)session.createQuery("from Personale where id_personale=:id").setParameter("id", idPersonale).uniqueResult();
			f=(FoglioOreMese)session.createQuery("from FoglioOreMese where id_personale=:id and meseRiferimento=:mese").setParameter("id", idPersonale)
					.setParameter("mese", mese).uniqueResult();
			listaGiorni.addAll(f.getDettaglioOreGiornalieres());
			
			for(DettaglioOreGiornaliere g:listaGiorni){
				listaCommesse=new ArrayList<DettaglioIntervalliCommesse>();
				listaCommesse.addAll(g.getDettaglioIntervalliCommesses());
				for(DettaglioIntervalliCommesse c:listaCommesse){
					if((c.getNumeroCommessa().compareTo(numeroCommessa)==0)&&(c.getEstensioneCommessa().compareTo(estensione)==0))
						totale=ServerUtility.aggiornaTotGenerale(totale,c.getOreLavorate());//TODO cambiata con quella esterna
				}				
			}		
			tx.commit();
			return totale;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;
		}
	}

/*
	private static String calcoloOreLavoro(String totaleOre, String oreLavorate) {
		String totale= new String();
		String totA= new String();
		String totB=new String();
		String giornoA=new String();
		String giornoB=new String();
		int a,b;
		
		totA=totaleOre.substring(0, totaleOre.indexOf("."));
		totB=totaleOre.substring(totaleOre.indexOf(".")+1, totaleOre.length());
		
		giornoA= oreLavorate.substring(0, oreLavorate.indexOf("."));
		giornoB= oreLavorate.substring(oreLavorate.indexOf(".")+1, oreLavorate.length());
						
		a=Integer.parseInt(totA)+Integer.parseInt(giornoA);
		b=Integer.parseInt(totB)+Integer.parseInt(giornoB);
		
		if(b==0)
			totale = (String.valueOf(a)+".00");
		else
			if(b>59)
				if(b%60 <10)
					totale = (String.valueOf(a+1)+".0"+String.valueOf(b%60));
				else
					totale = (String.valueOf(a+1)+"."+String.valueOf(b%60));
			else
				totale = (String.valueOf(a)+"."+String.valueOf(b));
		
		return totale;
	}
*/

	public static Set<AssociazionePtoA> getAssociazioniPtoAByUsername(String username) {
		Personale p = new Personale();
		Set<AssociazionePtoA> set= new HashSet<>();
		Session session = MyHibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;	
		try {
			
			tx = session.beginTransaction();
			
			p=(Personale)session.createQuery("from Personale where username=:username").setParameter("username", username)
					.uniqueResult();
			
			set.addAll(p.getAssociazionePtoas());	
			
			tx.commit();
			return set;
			
		} catch (Exception e) {
			if (tx!=null)
	    		tx.rollback();	
			e.printStackTrace();
			return null;
		} finally{
			
			session.close();
		}
	}	
	
}
