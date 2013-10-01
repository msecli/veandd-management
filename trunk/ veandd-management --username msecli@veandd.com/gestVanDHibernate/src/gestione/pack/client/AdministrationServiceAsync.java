package gestione.pack.client;

import java.util.Date;
import java.util.List;

import gestione.pack.client.model.ClienteModel;
import gestione.pack.client.model.CommentiModel;
import gestione.pack.client.model.CommessaModel;
import gestione.pack.client.model.CostiHwSwModel;
import gestione.pack.client.model.CostingModel;
import gestione.pack.client.model.DatiFatturazioneCommessaModel;
import gestione.pack.client.model.DatiFatturazioneMeseModel;
import gestione.pack.client.model.FoglioFatturazioneModel;
import gestione.pack.client.model.GestioneCostiDipendentiModel;
import gestione.pack.client.model.GestioneRdoCommesse;
import gestione.pack.client.model.GiustificativiModel;
import gestione.pack.client.model.IntervalliCommesseModel;
import gestione.pack.client.model.IntervalliIUModel;
import gestione.pack.client.model.PersonaleAssociatoModel;
import gestione.pack.client.model.PersonaleModel;
import gestione.pack.client.model.RdaModel;
import gestione.pack.client.model.RdoCompletaModel;
import gestione.pack.client.model.RiepilogoCostiDipendentiModel;
import gestione.pack.client.model.RiepilogoMeseGiornalieroModel;
import gestione.pack.client.model.RiepilogoOreAnnualiDipendente;
import gestione.pack.client.model.RiepilogoOreNonFatturabiliModel;
import gestione.pack.client.model.RiepilogoFoglioOreModel;
import gestione.pack.client.model.RiepilogoOreDipCommesse;
import gestione.pack.client.model.RiepilogoOreDipCommesseGiornaliero;
import gestione.pack.client.model.RiepilogoOreDipFatturazione;
import gestione.pack.client.model.RiepilogoOreModel;
import gestione.pack.client.model.RiepilogoOreTotaliCommesse;
import gestione.pack.client.model.RiepilogoSALPCLModel;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdministrationServiceAsync {

	/*
	void addCliente(int id, String ragSociale, AsyncCallback<Void> callback);

	void addRda(int numRda, int idcliente, AsyncCallback<Void> asyncCallback);
*/
	
//-----------------cliente
	void getAllClientiModel(AsyncCallback<List<ClienteModel>> asyncCallback);

	void insertDataCliente(int codCliente, String ragSociale,
			String codFiscale, String partitaIVA, String codRaggr,
			String comune, String provincia, String stato, String indirizzo,
			String cap, String telefono, String fax, String email,
			AsyncCallback<Void> asyncCallback);

	void editDataCliente(int parseInt, String ragSociale, String codFiscale,
			String partitaIVA, String codRaggr, String comune,
			String provincia, String stato, String indirizzo, String cap,
			String telefono, String fax, String email,
			AsyncCallback<Void> asyncCallback);

	void removeDataCliente(int parseInt, AsyncCallback<Void> asyncCallback);
	
	void getRagioneSociale(AsyncCallback<List<String>> callback);

//-------------------Personale
	void insertDataPersonale(String nome, String cognome, String username,
			String password, String nBadge, String ruolo, String tipoOrario,
			String tipoLavoratore, String gruppoLavoro, String costoOrario,
			String costoStruttura, String sede, String oreDirette,
			String oreIndirette, String permessi, String ferie, String ext,String oreRecupero,
			String oreRecupero2, AsyncCallback<Void> callback);

	void editDataPersonale(int id, String nome, String cognome,
			String username, String password, String nBadge, String ruolo,
			String tipoOrario, String tipoLavoratore, String gruppoLavoro,
			String costoOrario, String costoStruttura, String sede,
			String oreDirette, String oreIndirette, String permessi,
			String ferie, String ext,String oreRecupero, String oreRecupero2, AsyncCallback<Void> callback);

	void removeDataPersonale(int id, AsyncCallback<Void> callback);

	void getAllPersonaleModel(AsyncCallback<List<PersonaleModel>> callback);

	
	

//-----------------------------RDA
	void getAllNumeroRdo(AsyncCallback<List<String>> asyncCallback);
	
	void getAllRdaModel( AsyncCallback<List<RdaModel>> asyncCallback);
	
	/*void insertDataRda(int idcliente, String numRda, String ragioneSociale,
			AsyncCallback<Boolean> asyncCallback);

	void editDataRda(int idRda, int idcliente, String numRda,
			AsyncCallback<Boolean> asyncCallback);

	void deleteDataRda(int idRda, AsyncCallback<Boolean> asyncCallback);*/
	
	void getAllRdoCompletaModel(
			AsyncCallback<List<RdoCompletaModel>> asyncCallback);	
	
	void saveRdoCompleta(String numRdo, String cliente, String numOfferta,
			Date dataOfferte, String importo, String numOrdine,
			String descrizione, Date dataInizio, Date dataFine, String tariffa,
			String numRisorse, String oreDisp, String oreRes,
			AsyncCallback<Boolean> asyncCallback);
	
	void editRdoCompleta(int idRdo, String numRdo, String cliente,
			String numOfferta, Date dataOfferte, String importo,
			String numOrdine, String descrizione, Date dataInizio,
			Date dataFine, String tariffa, String numRisorse, String oreDisp,
			String oreRes, AsyncCallback<Boolean> asyncCallback);

	void deleteRdoCompleta(int idRdo, AsyncCallback<Boolean> asyncCallback);
	
	void closeOrdine(String numeroOrdine, AsyncCallback<Boolean> asyncCallback);

/*	
//--------------------------Offerta
	void insertDataOfferta(String numOfferta, String numRda,
			Date dataOfferta, String descrizione, String tariffa,
			AsyncCallback<Void> asyncCallback);

	void editDataOfferta(int idofferta, String numOfferta, String numRda,
			Date dataOfferta, String descrizione, String tariffa,
			AsyncCallback<Void> asyncCallback);

	void deleteDataOfferta(int idOfferta, AsyncCallback<Void> asyncCallback);

	void getAllOfferteModel(AsyncCallback<List<OffertaModel>> callback);

	void getAllOfferte(AsyncCallback<Set<Offerta>> callback);

	
//-------------------------Ordine
	void insertDataOrdine(String numOrdine, String numRda, String numeroCommessa,String estensione, Date dataInizio,
			Date dataFine, String descrizione, String tariffaOraria,
			String numeroRisorse, String numeroOre,
			AsyncCallback<Boolean> asyncCallback);

	void editDataOrdine(int idrdine, String numOrdine, String numRda,
			String numeroCommessa, String estensione, Date dataInizio, Date dataFine, String descrizione, String tariffaOraria,
			String numeroRisorse, String numeroOre,
			AsyncCallback<Boolean> asyncCallback);

	void getAllOrdineModel(AsyncCallback<List<OrdineModel>> asyncCallback);

	void deleteDataOrdine(int idOrdine, AsyncCallback<Boolean> asyncCallback);
*/
	void getNomePM(AsyncCallback<List<String>> asyncCallback);
	
	
//-----------------------------Commesse

	void getAllCommesseModel(String pm, String statoSelezionato, AsyncCallback<List<CommessaModel>> asyncCallback);

	void insertDataCommessa(String numCommessa, String estensione,
			String tipoCommessa, String pM, String statoCommessa,
			String oreLavoro, String oreLavoroResidue, String tariffaSal, 
			String salAttuale, String pclAttuale, String descrizione,
			String note, AsyncCallback<Boolean> asyncCallback);

	void editDataCommessa(int i,String numCommessa, String estensione,
			String tipoCommessa, String pM, String statoCommessa,
			String oreLavoro, String oreLavoroResidue, String tariffaSal, 
			String salAttuale, String pclAttuale, String descrizione,
			String note, AsyncCallback<Boolean> asyncCallback);

	void deleteDataCommessa(int parseInt, AsyncCallback<Boolean> asyncCallback);

	void getAllListaOrdini(AsyncCallback<List<String>> asyncCallback);

	void associaOrdineCommessa(String numeroCommessa, String numOrdine,
			String numRda, Date dataInizio, Date dataFine, String descrizione,
			String tariffaOraria, String numeroRisorse, String numeroOre,
			AsyncCallback<Boolean> asyncCallback);

	void associaOrdinePresenteCommessa(String idCommessa, String numOrdine,
			AsyncCallback<Boolean> asyncCallback);

	void closeCommessa(int parseInt, AsyncCallback<Boolean> asyncCallback);

	void getCommesse(AsyncCallback<List<String>> asyncCallback);
	
	void getCommesseAperte(AsyncCallback<List<String>> callback);

	void getCommesseByPM(String nome, String cognome, /*String pm*/AsyncCallback<List<String>> asyncCallback) ;

	void getAllRdoCommesse(AsyncCallback<List<GestioneRdoCommesse>> asyncCallback);
	
	//--------------------------------------Associazioni Commesse Attivita Dipend
	void createAssociazioneCommessaDipendenti(String pm, String commessa,
			List<String> listaDipendenti, AsyncCallback<Boolean> asyncCallback);

	void deleteAssociazioneCommessaDipendenti(int idAssociazione, String commessa, String nome,
			String cognome, AsyncCallback<Boolean> asyncCallback);

	void editAssociazioneCommessaDipendenti(String pm, String commessa,
			List<String> listaDipendenti, AsyncCallback<Boolean> asyncCallback);

	void getAssociazioniPersonaleCommessaByPM(String cognome,
			AsyncCallback<List<PersonaleAssociatoModel>> callback);
	
	void getAssociazioniPersonaleCommessa(
			AsyncCallback<List<PersonaleAssociatoModel>> asyncCallback);


	//------------------------------------------FOGLIO ORE
	void getAssociazioniPersonaleCommessaByUsername(
			String username, Date data, AsyncCallback<List<IntervalliCommesseModel>> asyncCallback);

	//void getIntervalliIUTimbratrice(String u, AsyncCallback<List<IntervalliIUModel>> asyncCallback);

	void insertFoglioOreGiorno(String username, Date giorno,
			String totOreGenerale, String delta, String oreViaggio,
			String oreAssRecupero, String deltaOreViaggio,
			String giustificativo, String oreStraordinario, String oreFerie, String orePermesso, 
			String revisione, String oreAbbuono, List<String> intervalliIU,
			List<IntervalliCommesseModel> intervalliC, String oreRecuperoTot,
			String noteAggiuntive, AsyncCallback<Boolean> asyncCallback);
	/*void getOrePreviste(String username, AsyncCallback<String> asyncCallback);

	void loadFoglioOre(AsyncCallback<Boolean> asyncCallback);*/
	
	void insertFoglioOreGiorno(String username, Date giorno,
			List<IntervalliCommesseModel> intervalliC,
			AsyncCallback<Boolean> asyncCallback);

	void loadIntervalliIU(String username, Date data,
			AsyncCallback<List<IntervalliIUModel>> asyncCallback);

	void loadGiustificativi(String username, Date giorno,
			AsyncCallback<GiustificativiModel> asyncCallback);

	void loadRiepilogoMese(String username, Date mese,
			AsyncCallback<RiepilogoOreModel> asyncCallback);

	void getListaDipendenti(String string, AsyncCallback<List<String>> asyncCallback);

	void getRiepilogoOreDipCommesse(String mese,
			String pm, AsyncCallback<List<RiepilogoOreDipCommesse>> asyncCallback);

	void getRiepilogoOreTotCommesse(String pm,
			String data, AsyncCallback<List<RiepilogoOreTotaliCommesse>> asyncCallback);
	
	void getRiepilogoMeseFoglioOre(String username,
			Date data, AsyncCallback<List<RiepilogoFoglioOreModel>> asyncCallback);

	void checkOreIntervalliIUOreCommesse(String username, Date data, AsyncCallback<List<RiepilogoOreDipCommesseGiornaliero>> asyncCallback);
	
//----------------------------------------------FATTURAZIONE
	
	void getRiepilogoOreDipFatturazione(String mese, String pm, AsyncCallback<List<RiepilogoOreDipFatturazione>> asyncCallback);

	void getDatiFatturazionePerOrdine(String numeroCommessa, String string,
			AsyncCallback<FoglioFatturazioneModel> asyncCallback);

	void insertDatiFoglioFatturazione(String oreEseguite, String salAttuale,
			String pclAttuale, String oreFatturere, String variazioneSAL,
			String variazionePCL, String meseCorrente,
			String note, String statoElaborazione, String commessaSelezionata,  
			String tariffaUtilizzata, String flagSal, AsyncCallback<Boolean> asyncCallback);

	void getReportDatiFatturazioneMese(String mese,
			AsyncCallback<List<DatiFatturazioneMeseModel>> asyncCallback);

	void getCommesseRiepilogoDatiFatturazione(
			AsyncCallback<List<String>> asyncCallback);

	void getRiepilogoDatiFatturazioneCommessa(String commessaSelected, AsyncCallback<List<DatiFatturazioneCommessaModel>> asyncCallback);

	void loadIntervalliToolTip(String username, Date giorno,
			AsyncCallback<List<String>> asyncCallback);
	
	void getElencoCommesseSuFoglioFatturazione(String numCommessa,
			String numEstensione, String data,
			AsyncCallback<List<RiepilogoOreTotaliCommesse>> asyncCallback);

//----------------------------------------------------VARIE
	void invioCommenti(String testo, String username,
			AsyncCallback<Boolean> asyncCallback);
	
	void getAllCommenti(String utente,
			AsyncCallback<List<CommentiModel>> asyncCallback);

	void getAllCommesseModelByPm(String cognomePm,
			AsyncCallback<List<CommessaModel>> asyncCallback);

	void getListaDipendentiModel(String string,
			AsyncCallback<List<PersonaleModel>> asyncCallback);

	void setRiepilogoOreOnSession(List<RiepilogoFoglioOreModel> lista,
			AsyncCallback<Boolean> asyncCallback);

	void getRiepilogoGiornalieroCommesse(int idDip, String username, Date data,
			AsyncCallback<List<RiepilogoOreDipCommesseGiornaliero>> asyncCallback);

	void getRiepilogoMeseFoglioOre(Date value, String pm, String sede,
			String cognome, AsyncCallback<List<RiepilogoFoglioOreModel>> asyncCallback);

	void confermaGiorniDipendente(String username, Date data, AsyncCallback<Boolean> asyncCallback);

	void confermaGiorniTuttiDipendenti(String string, Date data,
			AsyncCallback<Boolean> asyncCallback);

	void getAllCommenti(AsyncCallback<List<CommentiModel>> asyncCallback);

	void deleteCommento(int id, AsyncCallback<Boolean> asyncCallback);

	void getRiepilogoSalPcl(String data, String tabSelected,
			AsyncCallback<List<RiepilogoSALPCLModel>> asyncCallback);

	void getRiepilogoSalPclRiassunto(String data, String tabSelected,
			AsyncCallback<List<RiepilogoSALPCLModel>> asyncCallback);

	void confermaEditCommenti(int i, AsyncCallback<Boolean> asyncCallback);

	void getRiepilogoMensileDettagliatoHorizontalLayout(String sede,
			String data,
			AsyncCallback<List<RiepilogoMeseGiornalieroModel>> asyncCallback);

	void getRiepilogoAnnualeOreDipendenti(String anno, String sede,
			AsyncCallback<List<RiepilogoOreAnnualiDipendente>> asyncCallback);

	void getRiepilogoOreNonFatturate(String data, String groupBy,
			AsyncCallback<List<RiepilogoOreNonFatturabiliModel>> asyncCallback);

	void getRiepilogoTotCommessePerDipendenti(String mese, String sede,
			AsyncCallback<List<RiepilogoOreDipFatturazione>> callback);

//-------------------------------------------------COSTI---------------------------------
	
	void getDatiCostiPersonale(int idPersonale,
			AsyncCallback<List<GestioneCostiDipendentiModel>> asyncCallback);

	void getDatiCostiHwSw(int id,
			AsyncCallback<List<CostiHwSwModel>> asyncCallback);

	void saveAssociaCostiHwSw(int idSelected, CostiHwSwModel costo,
			AsyncCallback<Boolean> asyncCallback);

	void editDatiCostiAzienda(GestioneCostiDipendentiModel g,
			AsyncCallback<Void> asyncCallback);

	void getRiepilogoDatiCostiPersonale(String string,
			AsyncCallback<List<RiepilogoCostiDipendentiModel>> asyncCallback);

	void checkIntervallicommesse(AsyncCallback<List<String>> asyncCallback);
	

	void getListaDatiCosting(String username,
			AsyncCallback<List<CostingModel>> asyncCallback);

	void getDatiCosting(int costing,
			AsyncCallback<List<CostingModel>> asyncCallback);
			
}
